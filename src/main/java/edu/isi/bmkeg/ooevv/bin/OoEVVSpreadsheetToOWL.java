package edu.isi.bmkeg.ooevv.bin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import edu.isi.bmkeg.ooevv.controller.impl.OoevvEngineImpl;
import edu.isi.bmkeg.ooevv.dao.ExtendedOoevvDaoImpl;
import edu.isi.bmkeg.ooevv.model.OoevvElementSet;
import edu.isi.bmkeg.ooevv.utils.OoevvExcelEngine;
import edu.isi.bmkeg.uml.interfaces.OwlUmlInterface;
import edu.isi.bmkeg.uml.model.UMLmodel;
import edu.isi.bmkeg.vpdmf.controller.VPDMfKnowledgeBaseBuilder;
import edu.isi.bmkeg.vpdmf.model.definitions.VPDMf;

public class OoEVVSpreadsheetToOWL {

	public static String USAGE = "arguments: <Spreadsheet-file-path> <Owl-file-name> [bioPortalLookup?]\n";

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		if (args.length != 2 && args.length != 3) {
			System.err.println(USAGE);
			System.exit(-1);
		}

		File ooevvSheet = new File(args[0]);

		if (!ooevvSheet.exists()) {
			System.err.println("Can't find " + args[0]);
			System.exit(-1);
		}

		File owlFile = new File(ooevvSheet.getParentFile().getAbsolutePath()
				+ "/" + args[1]);

		boolean lookup = false;
		if (args.length == 3)
			lookup = true;

		List<String> stems = new ArrayList<String>();
		if (args.length > 2) {
			for (int j = 2; j < args.length; j++) {
				stems.add(args[j]);
			}
		}

		OoevvExcelEngine xlEngine = new OoevvExcelEngine();

		String buildFilePath = ClassLoader.getSystemClassLoader()
				.getResource("edu/isi/bmkeg/ooevv/ooevv-mysql.zip").getFile();
		File buildFile = new File(buildFilePath);

		VPDMfKnowledgeBaseBuilder builder = new VPDMfKnowledgeBaseBuilder(
				buildFile, null, null, null);

		VPDMf top = builder.readTop();
		UMLmodel m = top.getUmlModel();

		OwlUmlInterface oui = new OwlUmlInterface();
		oui.setUmlModel(m);

		String uri = "http://bmkeg.isi.edu/ooevv/";

		OoevvElementSet exptVbSet = xlEngine.createExpVariableSetFromExcel(
				ooevvSheet, lookup);

		OoevvEngineImpl engine = new OoevvEngineImpl();
		
		if (!owlFile.exists())
			engine.saveOoevvSystemAsOwl(top, owlFile, uri, ".model.");

		engine.saveOoevvElementSetToOwl(top, exptVbSet, owlFile, uri);

	}

}

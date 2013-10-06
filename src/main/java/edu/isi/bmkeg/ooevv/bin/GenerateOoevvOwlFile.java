package edu.isi.bmkeg.ooevv.bin;

import java.io.File;

import edu.isi.bmkeg.ooevv.controller.impl.OoevvEngineImpl;
import edu.isi.bmkeg.ooevv.dao.ExtendedOoevvDaoImpl;
import edu.isi.bmkeg.uml.interfaces.OwlUmlInterface;
import edu.isi.bmkeg.uml.model.UMLmodel;
import edu.isi.bmkeg.vpdmf.controller.VPDMfKnowledgeBaseBuilder;
import edu.isi.bmkeg.vpdmf.model.definitions.VPDMf;

public class GenerateOoevvOwlFile {

	public static String USAGE = "arguments: <owl-file-path>\n"; 
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		if ( args.length != 1 ) {
			System.err.println(USAGE);
			System.exit(-1);
		}

		File owlFile = new File(args[0]);

		String buildFilePath = ClassLoader.getSystemClassLoader()
				.getResource("edu/isi/bmkeg/ooevv/ooevv-mysql.zip").getFile();
		File buildFile = new File(buildFilePath);

		VPDMfKnowledgeBaseBuilder builder = new VPDMfKnowledgeBaseBuilder(
				buildFile, null, null, null);

		VPDMf top = builder.readTop();
		UMLmodel m = top.getUmlModel();

		OwlUmlInterface oui = new OwlUmlInterface();
		oui.setUmlModel(m);
		oui.convertAttributes();

		String uri = "http://bmkeg.isi.edu/ooevv/";

		OoevvEngineImpl engine = new OoevvEngineImpl();
		
		engine.saveOoevvSystemAsOwl(top, owlFile, uri, ".model.");
		
		
	}
	
}

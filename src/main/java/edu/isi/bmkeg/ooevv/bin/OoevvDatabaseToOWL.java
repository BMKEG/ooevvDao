package edu.isi.bmkeg.ooevv.bin;

import java.io.File;

import edu.isi.bmkeg.ooevv.controller.impl.OoevvEngineImpl;
import edu.isi.bmkeg.uml.interfaces.OwlUmlInterface;
import edu.isi.bmkeg.uml.model.UMLmodel;
import edu.isi.bmkeg.vpdmf.model.definitions.VPDMf;

public class OoevvDatabaseToOWL {

	public static String USAGE = "arguments: <database-name> <login> <dbPassword> <workingDirectory> <owl-file>\n"; 
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if( args.length != 4 ) {
			System.err.println(USAGE);
			System.exit(-1);
		}
		
		try { 
			
			OoevvEngineImpl engine = new OoevvEngineImpl();
			engine.getDao().init(args[1], args[2], args[0], args[3]);

			VPDMf top = engine.getDao().getCoreDao().getTop();
			
			UMLmodel m = top.getUmlModel();
			OwlUmlInterface oui = new OwlUmlInterface();
			oui.setUmlModel(m);

			String uri = "http://bmkeg.isi.edu/ooevv/";

			File owlFile = new File(args[4]);	

			oui.saveUmlAsOwl(owlFile, uri, ".model.");
			
			engine.saveAllOoevvElementSetToOwl(owlFile, uri);
												
		} catch (Exception e) {
			
			e.printStackTrace();
		
		}
		
	}
	
}

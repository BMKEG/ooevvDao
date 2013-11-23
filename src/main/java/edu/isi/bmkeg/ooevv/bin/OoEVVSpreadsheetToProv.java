package edu.isi.bmkeg.ooevv.bin;

import java.io.File;

import org.springframework.context.ApplicationContext;

import edu.isi.bmkeg.ooevv.dao.ExtendedOoevvDaoImpl;
import edu.isi.bmkeg.ooevv.model.OoevvElementSet;
import edu.isi.bmkeg.ooevv.prov.OoevvProvBuilder;
import edu.isi.bmkeg.ooevv.utils.OoevvExcelEngine;
import edu.isi.bmkeg.utils.springContext.AppContext;

public class OoEVVSpreadsheetToProv {

	public static String USAGE = "arguments: <spreadsheet-file-path> <prov-archive>\n"; 
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if( args.length != 2 ) {
			System.err.println(USAGE);
			System.exit(-1);
		}
		
		File ooevvSheet = new File(args[0]);	
		File zip = new File(args[1]);	

		if( !ooevvSheet.exists() ) {
			System.err.println("Can't find " + args[0]);
			System.exit(-1);		
		} 

		OoevvExcelEngine xlEngine = new OoevvExcelEngine();

		try { 

			OoevvProvBuilder builder = new OoevvProvBuilder();
			
			OoevvElementSet oes = xlEngine
					.createExpVariableSetFromExcel(ooevvSheet, false);

			builder.generateProvFilesForOoevvElementSet(oes, zip);
						
		} catch (Exception e) {
			
			e.printStackTrace();
		
		}
		
	}
	
}

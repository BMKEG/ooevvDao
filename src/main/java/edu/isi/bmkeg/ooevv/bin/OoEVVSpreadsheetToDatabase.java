package edu.isi.bmkeg.ooevv.bin;

import java.io.File;

import org.springframework.context.ApplicationContext;

import edu.isi.bmkeg.ooevv.dao.ExtendedOoevvDaoImpl;
import edu.isi.bmkeg.ooevv.model.OoevvElementSet;
import edu.isi.bmkeg.ooevv.utils.OoevvExcelEngine;
import edu.isi.bmkeg.utils.springContext.AppContext;

public class OoEVVSpreadsheetToDatabase {

	public static String USAGE = "arguments: <Spreadsheet-file-path> <dbName> <login> <dbPassword> [bioPortalLookup?]\n"; 
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if( args.length != 4 && args.length != 5) {
			System.err.println(USAGE);
			System.exit(-1);
		}
		
		File ooevvSheet = new File(args[0]);	

		if( !ooevvSheet.exists() ) {
			System.err.println("Can't find " + args[0]);
			System.exit(-1);		
		} 

		OoevvExcelEngine xlEngine = new OoevvExcelEngine();

		try { 

			ApplicationContext ctx = AppContext.getApplicationContext();

			ExtendedOoevvDaoImpl dao = new ExtendedOoevvDaoImpl();
			dao.init(args[2], args[3], args[1]);

			boolean lookup = false; 
			if( args.length == 5 )
				lookup = true;
			
			OoevvElementSet exptVbSet = xlEngine
					.createExpVariableSetFromExcel(ooevvSheet, lookup);

			dao.insertOoevvElementSetInDatabase(exptVbSet);
						
		} catch (Exception e) {
			
			e.printStackTrace();
		
		}
		
	}
	
}

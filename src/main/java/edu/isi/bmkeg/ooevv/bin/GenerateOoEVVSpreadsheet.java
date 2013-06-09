package edu.isi.bmkeg.ooevv.bin;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.test.context.ContextConfiguration;

import edu.isi.bmkeg.ooevv.utils.OoevvExcelEngine;
import edu.isi.bmkeg.uml.model.UMLmodel;

public class GenerateOoEVVSpreadsheet {

	public static String USAGE = "arguments: <Spreadsheet-file-path>\n"; 
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if( args.length != 1 ) {
			System.err.println(USAGE);
			System.exit(-1);
		}
		
		File ooevvSheet = new File(args[0]);	
		
		OoevvExcelEngine xlEngine = new OoevvExcelEngine();

		try { 
						
			xlEngine.generateBlankOoevvExcelWorkbook(ooevvSheet);
			
		} catch (Exception e) {
			
			e.printStackTrace();
		
		}
		
	}
	
}

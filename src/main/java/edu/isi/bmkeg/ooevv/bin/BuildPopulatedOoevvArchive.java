package edu.isi.bmkeg.ooevv.bin;

import java.io.File;

import edu.isi.bmkeg.ooevv.dao.ExtendedOoevvDaoImpl;
import edu.isi.bmkeg.ooevv.model.OoevvElementSet;
import edu.isi.bmkeg.ooevv.utils.OoevvExcelEngine;
import edu.isi.bmkeg.vpdmf.bin.DumpDatabaseToVpdmfArchive;
import edu.isi.bmkeg.vpdmf.controller.VPDMfKnowledgeBaseBuilder;

public class BuildPopulatedOoevvArchive  {

	public static String USAGE = "arguments: <xl-dir> <db-name> <db-login> <db-password> <new-archive-file> \n";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		if (args.length != 5) {
			System.err.println(USAGE);
			System.exit(-1);
		}
		
		String localDirPath = args[0];
		String dbName = args[1];
		String dbLogin = args[2];
		String dbPassword = args[3];
		String archive = args[4];
		
		String[] args1 = new String[] { 
				localDirPath, dbName, dbLogin,dbPassword
				};
		
		OoevvDirToDatabase.main(args1);
		
		String archiveFile = ClassLoader.getSystemResource("edu/isi/bmkeg/ooevv/ooevv-mysql.zip").getFile();

		String[] args2 = new String[] { 
				archiveFile, dbName, dbLogin, dbPassword, archive
				};
			
		DumpDatabaseToVpdmfArchive.main(args2);
	
	}
	

}

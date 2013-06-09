package edu.isi.bmkeg.ooevv.bin;

import java.io.File;

import edu.isi.bmkeg.ooevv.dao.ExtendedOoevvDaoImpl;
import edu.isi.bmkeg.ooevv.exceptions.MalformedOoevvFileException;
import edu.isi.bmkeg.ooevv.model.OoevvElementSet;
import edu.isi.bmkeg.ooevv.utils.OoevvExcelEngine;

public class OoevvDirToDatabase  {

	public static String USAGE = "arguments: <xl-dir> <db-name> <db-login> <db-dbPassword> [ontology-lookup?]\n";
	
	private String svnUrl;
	private String svnLogin;
	private String svnPassword;
	private String localDirPath;
	private String dbName;
	private String dbLogin;
	private String dbPassword;
	private boolean lookupFlag = false;
			
	private OoevvExcelEngine xlEngine;
	private ExtendedOoevvDaoImpl dao;
	
	public OoevvDirToDatabase(String dbName, String dbLogin, String dbPassword, boolean lookupFlag) throws Exception{
		this.dbName = dbName;
		this.dbLogin = dbLogin;
		this.dbPassword = dbPassword;
		this.lookupFlag = lookupFlag;
		
		xlEngine = new OoevvExcelEngine();

		dao = new ExtendedOoevvDaoImpl();
		dao.init(this.dbLogin, this.dbPassword, this.dbName);
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		if (args.length != 4 && args.length != 5) {
			System.err.println(USAGE);
			System.exit(-1);
		}
		
		String localDirPath = args[0];
		String dbName = args[1];
		String dbLogin = args[2];
		String dbPassword = args[3];
		
		boolean lookupFlag = false;
		if( args.length == 5 ) {
			lookupFlag = true;
		}
		
		OoevvDirToDatabase svnToDb = new OoevvDirToDatabase(
				dbName, dbLogin, dbPassword, lookupFlag
				);
	
		File localDir = new File(localDirPath);

		if( !localDir.exists() ) {
			throw new Exception("Target folder does not exist: " + localDir.getPath() );
		}
		
		if( !localDir.isDirectory() ) {
			throw new Exception("Target folder is not a directory: " + localDir.getPath() );
		}
		
		svnToDb.process(localDir);
		
	}
	
	public void process(File fd) throws Exception {

		if( fd.isDirectory() ) {
			File[] fArray = fd.listFiles(); 
			for(int i=0; i<fArray.length; i++) {
				File f = fArray[i];
				process(f);
			}
		} else {
			
			try {
				
				if( !fd.getName().endsWith("_ooevv.xls") ) {
					return;
				}
				
				OoevvElementSet exptVbSet = xlEngine
					.createExpVariableSetFromExcel(fd, this.lookupFlag);
				dao.insertOoevvElementSetInDatabase(exptVbSet);

			} catch( IndexOutOfBoundsException e ) {

				System.err.println("insertion of file: " + fd.getPath() + " failed.");
				
			} catch( MalformedOoevvFileException e ) {

				System.err.println("insertion of file: " + fd.getPath() + " failed.");
				
			}
			
		}
		
	}

}

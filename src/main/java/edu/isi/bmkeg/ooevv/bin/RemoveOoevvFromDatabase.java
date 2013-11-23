package edu.isi.bmkeg.ooevv.bin;

import java.io.File;

import edu.isi.bmkeg.ooevv.dao.ExtendedOoevvDaoImpl;
import edu.isi.bmkeg.ooevv.exceptions.MalformedOoevvFileException;
import edu.isi.bmkeg.ooevv.model.OoevvElementSet;
import edu.isi.bmkeg.ooevv.utils.OoevvExcelEngine;

public class RemoveOoevvFromDatabase  {

	public static String USAGE = "arguments: <db-name> <db-login> <db-dbPassword>\n";
	
	private String dbName;
	private String dbLogin;
	private String dbPassword;
	private boolean lookupFlag = false;
			
	private OoevvExcelEngine xlEngine;
	private ExtendedOoevvDaoImpl dao;
	
	public RemoveOoevvFromDatabase(String dbName, String dbLogin, String dbPassword) throws Exception{
		this.dbName = dbName;
		this.dbLogin = dbLogin;
		this.dbPassword = dbPassword;
		
		xlEngine = new OoevvExcelEngine();

		dao = new ExtendedOoevvDaoImpl();
		dao.init(this.dbLogin, this.dbPassword, this.dbName);
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		if ( args.length != 3 ) {
			System.err.println(USAGE);
			System.exit(-1);
		}
		
		String dbName = args[0];
		String dbLogin = args[1];
		String dbPassword = args[2];
				
		ExtendedOoevvDaoImpl dao = new ExtendedOoevvDaoImpl();
		dao.init(dbLogin, dbPassword, dbName);
		
		dao.removeOoevv();
		
	}

}

package edu.isi.bmkeg.ooevv.bin;

import java.io.File;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.ISVNStatusHandler;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNStatus;
import org.tmatesoft.svn.core.wc.SVNStatusClient;

import edu.isi.bmkeg.ooevv.dao.ExtendedOoevvDaoImpl;
import edu.isi.bmkeg.ooevv.model.OoevvElementSet;
import edu.isi.bmkeg.ooevv.utils.OoevvExcelEngine;
import edu.isi.bmkeg.utils.svn.SvnUtils;

public class OoevvSvnToDatabase implements ISVNStatusHandler {

	public static String USAGE = "arguments: <svn-url> <svn-login> <svn-dbPassword> <local-archiveFile> " +
				"<db-name> <db-login> <db-dbPassword>\n";
	
	private String svnUrl;
	private String svnLogin;
	private String svnPassword;
	private String localDirPath;
	private String dbName;
	private String dbLogin;
	private String dbPassword;
			
	private OoevvExcelEngine xlEngine;
	private ExtendedOoevvDaoImpl dao;
	
	public OoevvSvnToDatabase(String dbName, String dbLogin, String dbPassword) throws Exception{
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
	public static void main(String[] args) {

		if (args.length != 7) {
			System.err.println(USAGE);
			System.exit(-1);
		}
		
		String svnUrl = args[0];
		String svnLogin = args[1];
		String svnPassword = args[2];
		String localDirPath = args[3];
		String dbName = args[4];
		String dbLogin = args[5];
		String dbPassword = args[6];

		try {
			
			OoevvSvnToDatabase svnToDb = new OoevvSvnToDatabase(dbName, dbLogin, dbPassword);
		
			File localDir = new File(localDirPath);
		
			svnToDb.uploadSvnToDatabase(svnUrl, svnLogin, svnPassword, localDir);
		
		} catch (Exception e) {

			e.printStackTrace();
			System.exit(-1);

		}
		
	}
	
	public long uploadSvnToDatabase(String svnUrl, String svnLogin, String svnPassword, 
			File localDir) throws Exception {
		
		if( !localDir.getParentFile().exists() ) {
			throw new Exception("The parent of " + localDir.getPath() + " must exist");
		}
		
		boolean checkoutFlag = false;
		if( !localDir.exists() )  {
			localDir.mkdir();
			checkoutFlag = true;
		}
		
		SvnUtils svnUtils = new SvnUtils(svnUrl, svnLogin, svnPassword, localDir);
		
		if( checkoutFlag )
			svnUtils.checkout();
		else 
			svnUtils.update();
			
		SVNClientManager ourClientManager = SVNClientManager.newInstance();
		SVNStatusClient checker = ourClientManager.getStatusClient();
		long status = checker.doStatus(localDir, true, true, true, false, this);
	
		return status;
		
	}
	
	// We are using this as a convenient method to iterate over the 
	// contents of the SVN repository
	public void handleStatus(SVNStatus status) throws SVNException {

		File ooevvSheet = status.getFile();
		
		try {
			OoevvElementSet exptVbSet = xlEngine
				.createExpVariableSetFromExcel(ooevvSheet);
			dao.insertOoevvElementSetInDatabase(exptVbSet);
		} catch (Exception e) {
			System.err.println(ooevvSheet.getAbsolutePath() + " caused an error:");
			e.printStackTrace();
		}
		
	}

}

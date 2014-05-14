package edu.isi.bmkeg.ooevv.bin;

import static org.junit.Assert.*;

import java.io.File;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.isi.bmkeg.utils.springContext.AppContext;
import edu.isi.bmkeg.utils.springContext.BmkegProperties;
import edu.isi.bmkeg.vpdmf.controller.VPDMfKnowledgeBaseBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/edu/isi/bmkeg/ooevv/applicationContext-ooevvTest-noJPA.xml"})
public class OoevvDirToDatabaseTest {

	ApplicationContext ctx;
	
	File excel;
	String output;

	String dbLogin, dbPassword, dbUrl, wd;
	String svnLogin, svnPassword, svnUrl;
	File svnDir;
	
	VPDMfKnowledgeBaseBuilder builder;
	
	@Before
	public void setUp() throws Exception {
		
		ctx = AppContext.getApplicationContext();
		BmkegProperties prop = (BmkegProperties) ctx.getBean("bmkegProperties");

		dbLogin = prop.getDbUser();
		dbPassword = prop.getDbPassword();
		dbUrl = prop.getDbUrl();
		wd = prop.getWorkingDirectory();

		int l = dbUrl.lastIndexOf("/");
		if (l != -1)
			dbUrl = dbUrl.substring(l + 1, dbUrl.length());
		
		svnDir = ctx.getResource(
				"classpath:edu/isi/bmkeg/ooevv/svnStore").getFile();
		
		File archiveFile = ctx.getResource(
				"classpath:edu/isi/bmkeg/ooevv/ooevv-mysql.zip").getFile();
		builder = new VPDMfKnowledgeBaseBuilder(archiveFile, 
				dbLogin, dbPassword, dbUrl);
		
		try {
			builder.destroyDatabase(dbUrl);
		} catch (SQLException sqlE) {
			// Gully: Make sure that this runs, avoid silly issues.
			if( !sqlE.getMessage().contains("database doesn't exist") ) {
				sqlE.printStackTrace();
//				throw sqlE;
			}
		}
		builder.buildDatabaseFromArchive();
		
	}
	
	@After
	public void tearDown() throws Exception {

		builder.destroyDatabase(dbUrl);
		
	}
	
	@Test
	public final void testRunExecWithFullPaths() throws Exception {
		
		String[] args = new String[] { 
				svnDir.getPath(), dbUrl, dbLogin, dbPassword, wd
				};
		
		OoevvDirToDatabase.main(args);
				
	}
	
	// @Test	
	// NOTE THIS DOES NOT WORK.
/*	public final void testRunExecWithOntologyUpload() throws Exception {
		
		String[] args = new String[] { 
				svnDir.getPath(), dbUrl, dbLogin, dbPassword, "true"
				};
		//
		OoevvDirToDatabase.main(args);
				
	}*/
	
}

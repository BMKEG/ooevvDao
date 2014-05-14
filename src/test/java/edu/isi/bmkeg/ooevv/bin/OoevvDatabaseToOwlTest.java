package edu.isi.bmkeg.ooevv.bin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.isi.bmkeg.ooevv.dao.ExtendedOoevvDaoImpl;
import edu.isi.bmkeg.ooevv.utils.OoevvExcelEngine;
import edu.isi.bmkeg.utils.springContext.AppContext;
import edu.isi.bmkeg.utils.springContext.BmkegProperties;
import edu.isi.bmkeg.vpdmf.controller.VPDMfKnowledgeBaseBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/edu/isi/bmkeg/ooevv/applicationContext-ooevvTest-noJPA.xml"})
public class OoevvDatabaseToOwlTest {
	
	ApplicationContext ctx;

	OoevvDatabaseToOWL p;
	
	String login, password, dbUrl, wd;
	File archiveFile, owlFile, svnDir, tte, vaccine;
	VPDMfKnowledgeBaseBuilder builder;
	
	@Before
	public void setUp() throws Exception {
		
		ctx = AppContext.getApplicationContext();
		BmkegProperties prop = (BmkegProperties) ctx.getBean("bmkegProperties");

		login = prop.getDbUser();
		password = prop.getDbPassword();
		dbUrl = prop.getDbUrl();
		wd = prop.getWorkingDirectory();
		
		int l = dbUrl.lastIndexOf("/");
		if (l != -1)
			dbUrl = dbUrl.substring(l + 1, dbUrl.length());
		
		archiveFile = ctx.getResource(
				"classpath:edu/isi/bmkeg/ooevv/ooevv-mysql.zip").getFile();
		builder = new VPDMfKnowledgeBaseBuilder(archiveFile, 
				login, password, dbUrl); 
		
		svnDir = ctx.getResource(
				"classpath:edu/isi/bmkeg/ooevv/svnStore").getFile();

		tte = ctx.getResource("classpath:edu/isi/bmkeg/ooevv/svnStore/tractTracing_ooevv.xls").getFile();
		vaccine = ctx.getResource("classpath:edu/isi/bmkeg/ooevv/svnStore/vaccine_ooevv.xls").getFile();
		
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
		
		svnDir = ctx.getResource(
				"classpath:edu/isi/bmkeg/ooevv/svnStore").getFile();
				
		File sample = ctx.getResource("classpath:Testcontext-inc.xml").getFile();
		File root = sample.getParentFile();
		owlFile = new File( root.getAbsolutePath() + "/edu/isi/bmkeg/ooevv/ooevv-text.owl");

	}

	@After
	public void tearDown() throws Exception {

		builder.destroyDatabase(dbUrl);
		
	}
	
	/*@Test
	public final void testBuildWholeOwlFile() throws Exception {
				
		String[] args = new String[] { 
				svnDir.getPath(), dbUrl, login, password
				};
		
		OoevvDirToDatabase.main(args);

		args = new String[] { 
				dbUrl, login, password, owlFile.getAbsolutePath()
				};
		
		OoevvDatabaseToOWL.main(args);
		
		long fileSize = owlFile.length();
		
		assertTrue("Owl file expected to be 258.1 KB: " + fileSize, 
				fileSize > 257800 && fileSize < 258200 );
				
	}*/
	
	@Test
	public final void testOnlyTwoExcelSheets() throws Exception {
				
		String[] args = new String[] { 
				vaccine.getAbsolutePath(), dbUrl, login, password, wd 
				};
		OoEVVSpreadsheetToDatabase.main(args);
		
		args = new String[] { 
				tte.getAbsolutePath(), dbUrl, login, password, wd
				};
		OoEVVSpreadsheetToDatabase.main(args);
		
		args = new String[] { 
				dbUrl, login, password, owlFile.getAbsolutePath()
				};
		
		OoevvDatabaseToOWL.main(args);
		
		long fileSize = owlFile.length();
		
		assertTrue("Owl file size: " + fileSize, 
				fileSize > 10 );
				
	}
		
}


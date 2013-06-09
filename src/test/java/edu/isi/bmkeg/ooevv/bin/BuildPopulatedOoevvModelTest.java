package edu.isi.bmkeg.ooevv.bin;

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
public class BuildPopulatedOoevvModelTest {
	
	ApplicationContext ctx;

	OoevvDatabaseToOWL p;
	
	String login, password, dbUrl;
	File archiveFile, newArchive, svnDir;
	VPDMfKnowledgeBaseBuilder builder;
	
	@Before
	public void setUp() throws Exception {
		
		ctx = AppContext.getApplicationContext();
		BmkegProperties prop = (BmkegProperties) ctx.getBean("bmkegProperties");

		login = prop.getDbUser();
		password = prop.getDbPassword();
		dbUrl = prop.getDbUrl();
		
		int l = dbUrl.lastIndexOf("/");
		if (l != -1)
			dbUrl = dbUrl.substring(l + 1, dbUrl.length());
		
		archiveFile = ctx.getResource(
				"classpath:edu/isi/bmkeg/ooevv/ooevv_VPDMf.zip").getFile();
		builder = new VPDMfKnowledgeBaseBuilder(archiveFile, 
				login, password, dbUrl); 
		
		svnDir = ctx.getResource(
				"classpath:edu/isi/bmkeg/ooevv/svnStore").getFile();

		newArchive = new File(archiveFile.getParent() + "/ooevv_vpdmf_populated.zip");
		
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
	public void testListAllOoevvElementSets() throws Exception {
					
		String[] args = new String[] { 
				svnDir.getPath(), dbUrl, login, password, newArchive.getPath()
				};
		
		BuildPopulatedOoevvArchive.main(args);					
		
	}

}

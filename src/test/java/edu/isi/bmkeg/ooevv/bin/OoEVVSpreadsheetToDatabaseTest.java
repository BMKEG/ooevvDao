package edu.isi.bmkeg.ooevv.bin;

import static org.junit.Assert.assertEquals;

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
public class OoEVVSpreadsheetToDatabaseTest {
	
	ApplicationContext ctx;

	OoEVVSpreadsheetToDatabase p;
	
	File tte, radOnc, gdiFile, stroke, vaccine;
	String output;

	String login, password, dbUrl;
	File archiveFile;
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
				"classpath:edu/isi/bmkeg/ooevv/ooevv-mysql.zip").getFile();
		builder = new VPDMfKnowledgeBaseBuilder(archiveFile, 
				login, password, dbUrl); 
		
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
				
		tte = ctx.getResource("classpath:edu/isi/bmkeg/ooevv/svnStore/tractTracing_ooevv.xls").getFile();

		//stroke = ctx.getResource("classpath:edu/isi/bmkeg/ooevv/svnStore/stroke_ooevv.xls").getFile();

		vaccine = ctx.getResource("classpath:edu/isi/bmkeg/ooevv/svnStore/vaccine_ooevv.xls").getFile();

		p = new OoEVVSpreadsheetToDatabase(); 
		
	}

	@After
	public void tearDown() throws Exception {
		builder.destroyDatabase(dbUrl);
	}
	
	@Test
	public final void testRunExecWithFullPaths_tte() throws Exception {
		
		String[] args = new String[] { 
				tte.getAbsolutePath(), dbUrl, login, password
				};
		
		OoEVVSpreadsheetToDatabase.main(args);
		
		// Should run a query to check this here. 		
	}
	
	@Test
	public final void testRunExecWithFullPaths_vaccine() throws Exception {
		
		String[] args = new String[] { 
				vaccine.getAbsolutePath(), dbUrl, login, password
				};
		
		OoEVVSpreadsheetToDatabase.main(args);
		
		// Should run a query to check this here. 		
	}

/*	public final void testRunExecWithFullPaths_radOnc() throws Exception {
		
		String[] args = new String[] { 
				radOnc.getAbsolutePath(), dbUrl, login, password
				};
		
		OoEVVSpreadsheetToDatabase.main(args);
		
		// Should run a query to check this here. 		
	}*/

}


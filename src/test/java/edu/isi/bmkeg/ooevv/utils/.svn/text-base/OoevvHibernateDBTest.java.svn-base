/** $Id: OoevvExcelEngineTest.java 2628 2011-07-21 01:01:24Z tom $
 * 
 */
package edu.isi.bmkeg.ooevv.utils;

import java.io.File;

import org.springframework.context.ApplicationContext;

import edu.isi.bmkeg.ooevv.dao.ExtendedOoevvDaoImpl;
import edu.isi.bmkeg.uml.interfaces.OwlUmlInterface;
import edu.isi.bmkeg.uml.utils.OwlAPIUtility;
import edu.isi.bmkeg.vpdmf.controller.VPDMfKnowledgeBaseBuilder;
import edu.isi.bmkeg.vpdmf.controller.queryEngineTools.VPDMfChangeEngineInterface;
import edu.isi.bmkeg.vpdmf.model.definitions.VPDMf;

/**
 * 
 * @author University of Southern California
 * @date $Date: 2012-01-27 13:01:24 -0700 (Fri, 27 Jan 2012) $
 * @version $Revision: 3360 $
 * 
 */
// TODO implement TestFramework TransactionManager that automatically rolls back
// transactions after each test
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "/edu/isi/bmkeg/ooevv/applicationContext-ooevvTest.xml" })
public class OoevvHibernateDBTest {

	ApplicationContext ctx;

	VPDMfKnowledgeBaseBuilder builder;
	VPDMfChangeEngineInterface ce;

	VPDMf top;
	ClassLoader cl;

	String login, password, dbUrl, dbName;

	private OoevvExcelEngine xlEngine;
	private ExtendedOoevvDaoImpl dao;

	File xlSheet, archiveFile, owlFile;

	String vbSet = "";

	OwlUmlInterface oui;
	OwlAPIUtility owlUtil;
	
	/*@Before
	public void setUp() throws Exception {

		ctx = AppContext.getApplicationContext();
		BmkegProperties prop = (BmkegProperties) ctx.getBean("bmkegProperties");

		login = prop.getDbUser();
		dbPassword = prop.getDbPassword();
		dbUrl = prop.getDbUrl();

		int l = dbUrl.lastIndexOf("/");
		if (l != -1)
			dbName = dbUrl.substring(l + 1, dbUrl.length());

		archiveFile = ctx.getResource(
				"classpath:edu/isi/bmkeg/ooevv/ooevv_VPDMf.zip").getFile();

		owlFile = new File(archiveFile.getParent() + "/ooevv.owl");	
		
		builder = new VPDMfKnowledgeBaseBuilder(archiveFile, login, dbPassword,
				dbName);
		
		xlSheet = ctx
				.getResource(
						"classpath:edu/isi/bmkeg/ooevv/OoEVVModel-tractTracingExperiments-02-08-12-1213-gully.xls")
				.getFile();

		xlEngine = new OoevvExcelEngine();

	}

	@After
	public void tearDown() throws Exception {

	}

	@Test public void test_001_BuildDatabase() throws Exception {	 

		try {
			builder.destroyDatabase(dbName);
		} catch (SQLException sqlE) {

			if (!sqlE.getMessage().contains("database doesn't exist")) {
				sqlE.printStackTrace();
			}

		}

		builder.buildDatabaseFromArchive();

		dao = new OoevvDao();
		dao.init(login, dbPassword, dbName);

		ExperimentalVariableSet exptVbSet = xlEngine.createExpVariableSetFromExcel(xlSheet);
		dao.insertExperimentalVariableSet(exptVbSet);

	}

	@Test public void testUploadExptVbSet() throws Exception {	 
	}*/

	
}
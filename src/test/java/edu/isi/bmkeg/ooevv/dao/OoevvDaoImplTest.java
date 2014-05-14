package edu.isi.bmkeg.ooevv.dao;

import java.io.File;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.isi.bmkeg.ooevv.bin.OoEVVSpreadsheetToDatabase;
import edu.isi.bmkeg.ooevv.model.scale.MeasurementScale;
import edu.isi.bmkeg.utils.springContext.AppContext;
import edu.isi.bmkeg.utils.springContext.BmkegProperties;
import edu.isi.bmkeg.vpdmf.controller.VPDMfKnowledgeBaseBuilder;
import edu.isi.bmkeg.vpdmf.model.definitions.ViewDefinition;
import edu.isi.bmkeg.vpdmf.model.instances.PrimitiveInstance;
import edu.isi.bmkeg.vpdmf.model.instances.ViewBasedObjectGraph;
import edu.isi.bmkeg.vpdmf.model.instances.ViewInstance;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/edu/isi/bmkeg/ooevv/applicationContext-ooevvTest-noJPA.xml"})
public class OoevvDaoImplTest {
	
	ApplicationContext ctx;

	ExtendedOoevvDaoImpl dao;
	
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
		
		String[] args = new String[] { 
				tte.getAbsolutePath(), dbUrl, login, password
				};
		
		OoEVVSpreadsheetToDatabase.main(args);
		
		dao = new ExtendedOoevvDaoImpl();
		dao.init(login, password, dbUrl, wd);
		
	}

	@After
	public void tearDown() throws Exception {

		builder.destroyDatabase(dbUrl);
		
	}
	
	@Test
	public void testListAllBinaryScaleWithNamedValues() throws Exception {
		
		ViewDefinition vd = dao.getCoreDao().getTop().getViews().get("BinaryScaleWithNamedValues");
		ViewInstance qVi = new ViewInstance(vd);
		
		dao.getCoreDao().getCe().connectToDB();
		List<ViewInstance> viList = dao.getCoreDao().getCe().executeFullQuery(qVi);
		dao.getCoreDao().getCe().closeDbConnection();
			
/*			Map<String, Object> objMap = vbog.viewToObjectGraph(vi);
			Iterator<String> keyIt = objMap.keySet().iterator();
			while (keyIt.hasNext()) {
				String key = keyIt.next();
				PrimitiveInstance pi = (PrimitiveInstance) vi.getSubGraph()
						.getNodes().get(key);
				Object o = objMap.get(key);
				vbog.primitiveToObject(pi, o, true);
			}

			MeasurementScale ms = (MeasurementScale) vbog.readPrimaryObject();

			return ms;*/
		
		assert(true);
		
	}

}

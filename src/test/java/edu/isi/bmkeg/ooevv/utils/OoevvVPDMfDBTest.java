/** $Id: OoevvExcelEngineTest.java 2628 2011-07-21 01:01:24Z tom $
 * 
 */
package edu.isi.bmkeg.ooevv.utils;

import java.io.File;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.isi.bmkeg.ooevv.dao.ExtendedOoevvDaoImpl;
import edu.isi.bmkeg.uml.interfaces.OwlUmlInterface;
import edu.isi.bmkeg.uml.utils.OwlAPIUtility;
import edu.isi.bmkeg.utils.springContext.AppContext;
import edu.isi.bmkeg.utils.springContext.BmkegProperties;
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
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/edu/isi/bmkeg/ooevv/applicationContext-ooevvTest-noJPA.xml" })
public class OoevvVPDMfDBTest {

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
	
	@Before
	public void setUp() throws Exception {

		ctx = AppContext.getApplicationContext();
		BmkegProperties prop = (BmkegProperties) ctx.getBean("bmkegProperties");

		login = prop.getDbUser();
		password = prop.getDbPassword();
		dbUrl = prop.getDbUrl();

		int l = dbUrl.lastIndexOf("/");
		if (l != -1)
			dbName = dbUrl.substring(l + 1, dbUrl.length());

		archiveFile = ctx.getResource(
				"classpath:edu/isi/bmkeg/ooevv/ooevv_VPDMf.zip").getFile();

		owlFile = new File(archiveFile.getParent() + "/ooevv.owl");	
		
		builder = new VPDMfKnowledgeBaseBuilder(archiveFile, login, password,
				dbName);
		try {
			builder.destroyDatabase(dbName);
		} catch (SQLException sqlE) {

			if (!sqlE.getMessage().contains("database doesn't exist")) {
				sqlE.printStackTrace();
			}

		}

		builder.buildDatabaseFromArchive();

		xlSheet = ctx
				.getResource(
						"classpath:edu/isi/bmkeg/ooevv/OoEVVModel-tractTracingExperiments-02-15-12-1511-gully.xls")
				.getFile();

		xlEngine = new OoevvExcelEngine();

		dao = new ExtendedOoevvDaoImpl();
		dao.getCoreDao().init(login, password, dbName);

	}

//	@After
//	public void tearDown() throws Exception {
//
//		builder.destroyDatabase(dbName);
//
//	}

	@Test public void testParseExptVbSet() throws Exception {	 
//		ExperimentalVariableSet exptVbSet = xlEngine.createExpVariableSetFromExcel(xlSheet);
	}

	@Test public void testUploadExptVbSet() throws Exception {	 
//		ExperimentalVariableSet exptVbSet = xlEngine.createExpVariableSetFromExcel(xlSheet);
//		dao.insertExperimentalVariableSet(exptVbSet);
	}

/*	@Test
	public void testSaveExptVbSetAsOwlClasses() throws Exception {

		VPDMf top = dao.getTop();
		
		UMLmodel m = top.getUmlModel();
		OwlUmlInterface rdfsInt = new OwlUmlInterface();
		rdfsInt.setUmlModel(m);

		rdfsInt.saveUmlAsOwl(owlFile, "http://bmkeg.isi.edu/ooevv/", ".model.");

	} */
	
/*	@Test
	public void testSaveExptVbSetAsOwlIndividuals() throws Exception {

		ExperimentalVariableSet exptVbSet = xlEngine
				.createExpVariableSetFromExcel(xlSheet);
		
		dao.insertExperimentalVariableSet(exptVbSet);

		VPDMf top = dao.getTop();
		
		UMLmodel m = top.getUmlModel();
		OwlUmlInterface rdfsInt = new OwlUmlInterface();

		rdfsInt = new OwlUmlInterface();
		rdfsInt.setUmlModel(m);

		String uri = "http://bmkeg.isi.edu/ooevv/";
		
		rdfsInt.saveUmlAsOwl(owlFile, uri, ".model.");
		
		dao.saveAllExperimentalVariableSetToOwl(owlFile, uri);
	
	}
	
	@Test
	public void testSaveExptVbSetDirectlyFromFileAsOwl() throws Exception {

		ExperimentalVariableSet exptVbSet = xlEngine
				.createExpVariableSetFromExcel(xlSheet);
		
		VPDMf top = dao.getTop();
		
		UMLmodel m = top.getUmlModel();
		OwlUmlInterface rdfsInt = new OwlUmlInterface();
		rdfsInt = new OwlUmlInterface();
		rdfsInt.setUmlModel(m);

		String uri = "http://bmkeg.isi.edu/ooevv/";
		
		rdfsInt.saveUmlAsOwl(owlFile, uri, ".model.");
		
		dao.saveExperimentalVariableSetToOwl(exptVbSet, owlFile, uri);
	
	}
		
	/*@Test
	public void testQueryExptVbSetFromDatabase() throws Exception {

		VPDMf top = dao.getTop();
		
		HSSFWorkbook wb = new HSSFWorkbook();
		FileOutputStream fileOut = new FileOutputStream(xlSheet);
		
		dao.insertExperimentalVariableSet(exptVbSet);
		
		dao.getCe().connectToDB();
		String viewName = "ExperimentalVariable";
		String attrAddr = "]ExperimentalVariableSet|ExperimentalVariableSet.name";

		ViewDefinition vd = top.getViews().get(viewName);
		ViewInstance qVi = new ViewInstance(vd);
		ViewInstance vi = dao.getCe().executeUIDQuery("ExperimentalVariable", "42");
		
		HSSFSheet sheetOoevv =  wb.createSheet("OoEVV");
		
		HSSFCellStyle style = wb.createCellStyle();
	    HSSFFont font = wb.createFont();
	    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	    style.setFont(font);
		
		AttributeInstance ai = vi.readAttributeInstance(attrAddr, 0);
		String scaleClassType = ai.readValueString();
		
		HSSFRow row = null;
		row = sheetOoevv.createRow(0);
		HSSFCell cell = null;
		cell = row.createCell((short)0);
		cell.setCellValue("name");
		cell.setCellStyle(style);
		
		cell = row.createCell((short)1);
		cell.setCellValue(scaleClassType);
		
		
		attrAddr = "]ExperimentalVariableSet|ExperimentalVariableSet.description";
		ai = vi.readAttributeInstance(attrAddr, 0);
		scaleClassType = ai.readValueString();
		
		row = sheetOoevv.createRow(1);
		
		cell = row.createCell((short)0);
		cell.setCellValue("description");
		cell.setCellStyle(style);
		
		cell = row.createCell((short)1);
		cell.setCellValue(scaleClassType);
		
		
		HSSFSheet sheet =  wb.createSheet("OoEVV Variables");
		HSSFRow headerRow = null;
		headerRow = sheet.createRow(0);
		
		HSSFCell headerCell = null;
		headerCell = headerRow.createCell((short)0);
		headerCell.setCellValue("shortId");
		headerCell.setCellStyle(style);
		
		headerCell = headerRow.createCell((short)1);
		headerCell.setCellValue("name");
		headerCell.setCellStyle(style);
		
		headerCell = headerRow.createCell((short)2);
		headerCell.setCellValue("definition");
		headerCell.setCellStyle(style);
		
		headerCell = headerRow.createCell((short)3);
		headerCell.setCellValue("term");
		headerCell.setCellStyle(style);
		
		headerCell = headerRow.createCell((short)4);
		headerCell.setCellValue("scale");
		headerCell.setCellStyle(style);
		
		List<ViewInstance> varList = dao.getCe().executeFullQuery(qVi);
		Iterator<ViewInstance> vIt = varList.iterator();
		
		int rowNum = 1;
		while(vIt.hasNext()){
			vi = vIt.next();
			
			attrAddr = "]Term|Term.shortTermId";
			ai = vi.readAttributeInstance(attrAddr, 0);
			scaleClassType = ai.readValueString();
			
			HSSFRow r = null;
			r = sheet.createRow(rowNum++);
			HSSFCell c = null;
			c = r.createCell((short)0);
			c.setCellValue(scaleClassType);
			
			attrAddr = "]Term|Term.termValue";
			ai = vi.readAttributeInstance(attrAddr, 0);
			scaleClassType = ai.readValueString();
			c = r.createCell((short)1);
			c.setCellValue(scaleClassType);
			
			attrAddr = "]Term|Term.definition";
			ai = vi.readAttributeInstance(attrAddr, 0);
			scaleClassType = ai.readValueString();
			c = r.createCell((short)2);
			c.setCellValue(scaleClassType);
			
			attrAddr = "]QualityDefinition|Term.shortTermId";
			ai = vi.readAttributeInstance(attrAddr, 0);
			scaleClassType = ai.readValueString();
			c = r.createCell((short)3);
			c.setCellValue(scaleClassType);
			
			
			attrAddr = "]ScaleDefinition|Term.shortTermId";
			ai = vi.readAttributeInstance(attrAddr, 0);
			scaleClassType = ai.readValueString();
			c = r.createCell((short)4);
			c.setCellValue(scaleClassType);
		}
		
		HSSFSheet sheetScales =  wb.createSheet("OoEVV Scales");
		
		viewName = "MeasurementScale";
		
		headerRow = null;
		headerRow = sheetScales.createRow(0);
		
		
		
		headerCell = null;
		headerCell = headerRow.createCell((short)0);
		headerCell.setCellValue("shortId");
		headerCell.setCellStyle(style);
		
		headerCell = headerRow.createCell((short)1);
		headerCell.setCellValue("definition");
		headerCell.setCellStyle(style);
		
		headerCell = headerRow.createCell((short)2);
		headerCell.setCellValue("definition");
		headerCell.setCellStyle(style);
		
		headerCell = headerRow.createCell((short)3);
		headerCell.setCellValue("type");
		headerCell.setCellStyle(style);
		
		vd = top.getViews().get(viewName);
		qVi = new ViewInstance(vd);
		List<ViewInstance> viewList = dao.getCe().executeFullQuery(qVi);
		
		vIt = viewList.iterator();
		int i =1;
		while (vIt.hasNext()) {
			ViewInstance view = vIt.next();
			attrAddr = "]Term|Term.shortTermId";
			ai = view.readAttributeInstance(attrAddr, 0);
			scaleClassType = ai.readValueString();
			
			HSSFRow scaleRow = null;
			scaleRow = sheetScales.createRow(i++);
			HSSFCell scaleCell = null;
			scaleCell = scaleRow.createCell((short)0);
			scaleCell.setCellValue(scaleClassType);
			
			attrAddr = "]Term|Term.termValue";
			ai = view.readAttributeInstance(attrAddr, 0);
			scaleClassType = ai.readValueString();
			scaleCell = scaleRow.createCell((short)1);
			scaleCell.setCellValue(scaleClassType);
			
			attrAddr = "]Term|Term.definition";
			ai = view.readAttributeInstance(attrAddr, 0);
			scaleClassType = ai.readValueString();
			scaleCell = scaleRow.createCell((short)2);
			scaleCell.setCellValue(scaleClassType);
			
			attrAddr = "]MeasurementScale|MeasurementScale.classType";
			ai = view.readAttributeInstance(attrAddr, 0);
			scaleClassType = ai.readValueString();
			scaleCell = scaleRow.createCell((short)3);
			scaleCell.setCellValue(scaleClassType);
			
			attrAddr = "]MeasurementScale|MeasurementScale.id";
			ai = view.readAttributeInstance(attrAddr, 0);
			String idStr = ai.readValueString();

			if (scaleClassType != null) {

				// ---------------------------------
				// query MeasurementScale
				// ---------------------------------
				
				ViewBasedObjectGraph vbog = dao.getVbogs().get(scaleClassType);
				ViewDefinition scaleVD = top.getViews().get(scaleClassType);
				ViewInstance scaleVI = new ViewInstance(scaleVD);
				AttributeInstance measureAttr = scaleVI.readAttributeInstance(attrAddr, 0);
				measureAttr.writeValueString(idStr);
				List<ViewInstance> scaleList =  dao.getCe().executeFullQuery(scaleVI);
				Iterator<ViewInstance> scaleIterator = scaleList.iterator();
				while(scaleIterator.hasNext()){
				       
					ViewInstance scaleView = scaleIterator.next();
					attrAddr = "]Term|Term.shortTermId";
					ai = scaleView.readAttributeInstance(attrAddr, 0);
					scaleClassType = ai.readValueString();
					
//					HSSFRow scaleRow = null;
					scaleRow = sheetScales.createRow(i++);
//					HSSFCell scaleCell = null;
					scaleCell = scaleRow.createCell((short)0);
					scaleCell.setCellValue(scaleClassType);
					
					attrAddr = "]Term|Term.termValue";
					ai = scaleView.readAttributeInstance(attrAddr, 0);
					scaleClassType = ai.readValueString();
					scaleCell = scaleRow.createCell((short)1);
					scaleCell.setCellValue(scaleClassType);
					
					attrAddr = "]Term|Term.definition";
					ai = scaleView.readAttributeInstance(attrAddr, 0);
					scaleClassType = ai.readValueString();
					scaleCell = scaleRow.createCell((short)2);
					scaleCell.setCellValue(scaleClassType);
					System.out.println(scaleView.getAlias());
				}
			}
		}
		
		wb.write(fileOut);
	
		dao.getCe().closeDbConnection();
	
	}*/

	
}
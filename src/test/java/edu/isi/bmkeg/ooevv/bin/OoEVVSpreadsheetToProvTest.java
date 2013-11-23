package edu.isi.bmkeg.ooevv.bin;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.isi.bmkeg.utils.Converters;
import edu.isi.bmkeg.utils.springContext.AppContext;
import edu.isi.bmkeg.utils.springContext.BmkegProperties;
import edu.isi.bmkeg.vpdmf.controller.VPDMfKnowledgeBaseBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/edu/isi/bmkeg/ooevv/applicationContext-ooevvTest-noJPA.xml"})
public class OoEVVSpreadsheetToProvTest {
	
	ApplicationContext ctx;

	File tte, radOnc, gdiFile, stroke, vaccine;
	String output;

	File zip;
	VPDMfKnowledgeBaseBuilder builder;
	
	@Before
	public void setUp() throws Exception {
		
		ctx = AppContext.getApplicationContext();
		BmkegProperties prop = (BmkegProperties) ctx.getBean("bmkegProperties");
						
		tte = ctx.getResource("classpath:edu/isi/bmkeg/ooevv/svnStore/tractTracing_ooevv.xls").getFile();
		
		vaccine = ctx.getResource("classpath:edu/isi/bmkeg/ooevv/svnStore/vaccine_ooevv.xls").getFile();
		
	}

	@After
	public void tearDown() throws Exception {
		zip.delete();
	}
	
	@Test
	public final void testRunExecWithFullPaths_tte() throws Exception {

		zip = new File(tte.getParent() + "/tte_ooevvProv.zip"); 
		
		String[] args = new String[] { 
				tte.getPath(), zip.getPath()
				};
		
		OoEVVSpreadsheetToProv.main(args);
		
		Converters.unzipIt(zip, zip.getParentFile());
		
	}
	
	@Test
	public final void testRunExecWithFullPaths_vaccine() throws Exception {
		
		zip = new File(tte.getParent() + "/vaccine_ooevvProv.zip"); 

		String[] args = new String[] { 
				vaccine.getAbsolutePath(), zip.getPath()
				};
		
		OoEVVSpreadsheetToProv.main(args);

	}

}


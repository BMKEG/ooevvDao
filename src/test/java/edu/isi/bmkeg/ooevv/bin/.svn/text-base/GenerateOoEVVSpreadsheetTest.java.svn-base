package edu.isi.bmkeg.ooevv.bin;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.isi.bmkeg.uml.model.UMLmodel;
import edu.isi.bmkeg.utils.springContext.AppContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/edu/isi/bmkeg/ooevv/applicationContext-ooevvTest-noJPA.xml"})
public class GenerateOoEVVSpreadsheetTest {
	
	ApplicationContext ctx;

	GenerateOoEVVSpreadsheet p;
	
	File excel;
	String output;
	
	@Before
	public void setUp() throws Exception {
		
		ctx = AppContext.getApplicationContext();
		
		File sample = ctx.getResource("classpath:Testcontext-inc.xml").getFile();
		File root = sample.getParentFile();
		excel = new File( root.getAbsolutePath() + "/edu/isi/bmkeg/ooevv/tempOoevv.xls");
		
		p = new GenerateOoEVVSpreadsheet();
		
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public final void testRunExecWithFullPaths() throws Exception {
		
		String[] args = new String[] { 
				excel.getAbsolutePath() 
				};
		
		GenerateOoEVVSpreadsheet.main(args);
		
		long fileSize = excel.length();
		
		assertEquals("Empty OoEVV Spreadsheet expected to be 6.1 KB: ", 6144L, fileSize);
		
	}
		
}


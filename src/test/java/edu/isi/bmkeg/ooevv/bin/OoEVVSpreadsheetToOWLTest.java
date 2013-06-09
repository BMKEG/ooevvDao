package edu.isi.bmkeg.ooevv.bin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.isi.bmkeg.utils.springContext.AppContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/edu/isi/bmkeg/ooevv/applicationContext-ooevvTest-noJPA.xml"})
public class OoEVVSpreadsheetToOWLTest {
	
	ApplicationContext ctx;

	OoEVVSpreadsheetToOWL p;
	
	File drugInfusion, fbirn, gdi, radOnc, stroke, tractTracing, vaccine;
	File out;
	String output;

	@Before
	public void setUp() throws Exception {
		
		ctx = AppContext.getApplicationContext();
		
		tractTracing = ctx.getResource(
				"classpath:edu/isi/bmkeg/ooevv/svnStore/tractTracing_ooevv.xls"
				).getFile();
		vaccine = ctx.getResource(
				"classpath:edu/isi/bmkeg/ooevv/svnStore/vaccine_ooevv.xls"
				).getFile();
		
		output = "out.owl";
		
		p = new OoEVVSpreadsheetToOWL();
		
	}

	@After
	public void tearDown() throws Exception {
		out.delete();
	}
	
	@Test
	public final void testRunExecWithFullPaths_tractTracing() throws Exception {
		
		String[] args = new String[] { 
				tractTracing.getAbsolutePath(), 
				output};
		
		OoEVVSpreadsheetToOWL.main(args);
		
		out = new File(tractTracing.getParent() + "/" + output); 
		long fileSize = out.length();
		
		assertTrue("Owl file expected to be 92.0 KB: " + fileSize, 
				(fileSize > 85000 && fileSize < 95000 ) );
		
	}
	
	@Test
	public final void testRunExecWithFullPaths_vaccine() throws Exception {
		
		String[] args = new String[] { 
				vaccine.getAbsolutePath(), 
				output};
		
		OoEVVSpreadsheetToOWL.main(args);
		
		out = new File(vaccine.getParent() + "/" + output); 
		long fileSize = out.length();
		
		assertTrue("Owl file expected to be 168K KB: " + fileSize, 
				(fileSize > 160000 ) && (fileSize < 180000 ) );
		
	}
	
	@Test
	public final void testRunExecWithFullPaths_all() throws Exception {
		
		String[] args = new String[] { 
				vaccine.getAbsolutePath(), 
				output};
		
		OoEVVSpreadsheetToOWL.main(args);

		args = new String[] { 
				tractTracing.getAbsolutePath(), 
				output};
		
		OoEVVSpreadsheetToOWL.main(args);
		
		out = new File(vaccine.getParent() + "/" + output); 
		long fileSize = out.length();
		
		assertTrue("Owl file expected to be 184.0 KB: " + fileSize, 
				(fileSize > 170000 ) && (fileSize < 190000 ) );
		
	}
		
}


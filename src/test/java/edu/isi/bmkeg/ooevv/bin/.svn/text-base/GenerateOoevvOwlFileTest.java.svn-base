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
public class GenerateOoevvOwlFileTest {
	
	ApplicationContext ctx;

	GenerateOoevvOwlFile p;
	
	File dir, owlFile;
	String output;

	@Before
	public void setUp() throws Exception {
		
		ctx = AppContext.getApplicationContext();
		
		dir = ctx.getResource(
				"classpath:edu/isi/bmkeg/ooevv"
				).getFile();

		
		owlFile = new File(dir.getPath() + "/ooevvTest.owl");
		
	}

	@After
	public void tearDown() throws Exception {
		owlFile.delete();
	}
	
	@Test
	public final void testRunExecWithFullPaths_all() throws Exception {
		
		String[] args = new String[] { 
				owlFile.getAbsolutePath()};
		
		GenerateOoevvOwlFile.main(args);

		long fileSize = owlFile.length();
		
		assertTrue("Owl file expected to be 58.3 KB: " + fileSize, 
				(fileSize > 55000 ) && (fileSize < 65000 ) );
		
	}
		
}


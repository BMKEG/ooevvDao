/** $Id: OoevvExcelEngineTest.java 2628 2011-07-21 01:01:24Z tom $
 * 
 */
package edu.isi.bmkeg.ooevv.utils;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.isi.bmkeg.utils.springContext.AppContext;

/**
 *
 * @author University of Southern California
 * @date $Date: 2011-07-20 18:01:24 -0700 (Wed, 20 Jul 2011) $
 * @version $Revision: 2628 $
 *
 */
//TODO implement TestFramework TransactionManager that automatically rolls back transactions after each test
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/edu/isi/bmkeg/ooevv/applicationContext-ooevvTest-noJPA.xml"})
public class OoevvExcelEngineTest {
	
	ApplicationContext ctx;
	
	private OoevvExcelEngine xlEngine;	
	
	File tteXl, radOnc;
	
	@Before
	public void setUp() throws Exception {
        
		ctx = AppContext.getApplicationContext();
		
		//tteXl = ctx.getResource("classpath:edu/isi/bmkeg/ooevv/OoEVVModel-tractTracingExperiments-02-15-12-1511-gully.xls").getFile();
		//radOnc = ctx.getResource("classpath:edu/isi/bmkeg/ooevv/svnStore/radOnc_ooevv.xls").getFile();
		
		xlEngine = new OoevvExcelEngine();
			
	}

	@After
	public void tearDown() throws Exception {
		
	}

	@Test
	public void testParseTractTracingOoevv() throws Exception {
	
//		ExperimentalVariableSet expVbs = xlEngine.createExpVariableSetFromExcel(tteXl);
		
//		assertEquals("Expect that the TTE variables contains 10 variables", 10, expVbs.getExptVbs().size() );
		
	}
	
	@Test
	public void testParseRadOncOoevv() throws Exception {
	
//		ExperimentalVariableSet expVbs = xlEngine.createExpVariableSetFromExcel(radOnc);
		
//		assertEquals("Expect that the RadOnc variables contains 35 variables", 35, expVbs.getExptVbs().size() );
		
	}

}
package edu.isi.bmkeg.ooevv.bin;

import java.io.File;
import java.net.URL;

import edu.isi.bmkeg.vpdmf.controller.VPDMfKnowledgeBaseBuilder;

public class GenerateOoEVVDatabase {

	public static String USAGE = "arguments: <database-name> <login> <dbPassword>\n"; 
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if( args.length != 3 ) {
			System.err.println(USAGE);
			System.exit(-1);
		}
		
		try { 

			URL url = ClassLoader.getSystemClassLoader().getResource("edu/isi/bmkeg/ooevv/ooevv-mysql.zip");
			String buildFilePath = url.getFile();
			File buildFile = new File( buildFilePath );
			VPDMfKnowledgeBaseBuilder builder = new VPDMfKnowledgeBaseBuilder(buildFile, 
					args[1], args[2], args[0]); 

			builder.buildDatabaseFromArchive();
				
		} catch (Exception e) {
			
			e.printStackTrace();
		
		}
		
	}
	
}

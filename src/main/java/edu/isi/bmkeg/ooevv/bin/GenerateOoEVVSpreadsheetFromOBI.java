package edu.isi.bmkeg.ooevv.bin;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.openrdf.query.QueryLanguage;

import edu.isi.bmkeg.ooevv.model.ExperimentalVariable;
import edu.isi.bmkeg.ooevv.model.OoevvElementSet;
import edu.isi.bmkeg.ooevv.model.OoevvEntity;
import edu.isi.bmkeg.ooevv.model.OoevvProcess;
import edu.isi.bmkeg.ooevv.utils.OoevvExcelEngine;
import edu.isi.bmkeg.vpdmf.bigdata.BigDataBean;

/**
 * This loads the OBI owl file and 
 * and imports it into an OoEVV Spreadsheet
 * 
 * @author Gully
 *
 */

public class GenerateOoEVVSpreadsheetFromOBI {

	public static String USAGE = "arguments: <Spreadsheet-file-path> <path-to-journal> <load-directory>\n"; 
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		if( args.length != 3 ) {
			System.err.println(USAGE);
			System.exit(-1);
		}
		
		File excelOutput = new File(args[0]);		
		File journal = new File(args[1]);			
		File loadDirectory = new File(args[2]);			
		
		BigDataBean bdb = new BigDataBean(
				journal.getPath(),
				loadDirectory.getPath(),
				"http://bmkeg.isi.edu/ooevv/");
		
		String sparql = "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>\n"
				+ "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
				+ "PREFIX obi:<http://purl.obolibrary.org/obo/OBI_>\n"
				+ "PREFIX owl:<http://www.w3.org/2002/07/owl#>\n"
					+ "SELECT ?id ?name ?defn\n" 
					+ "WHERE {\n"
					+ "	?id rdfs:label ?name . \n" 
					+ "	?id <http://purl.obolibrary.org/obo/IAO_0000115> ?defn . \n" // definition
					+ "	?id rdfs:subClassOf* ?p . \n " 
					+ "	{?p rdfs:label \"assay\"@en} UNION " 
					+ "{?p rdfs:label \"data transformation\"@en} UNION" 
					+ "{?p rdfs:label \"material processing\"@en} . \n " 
					+ "}\n";

		String[] keys = new String[] { "id", "name", "defn"};

		List<Map<String,String>> assays = bdb.executeSelectQuery(bdb.getRepo(),
					sparql, QueryLanguage.SPARQL, keys);
		
		OoevvElementSet oes = new OoevvElementSet();
		oes.setDefinition( "A subset of the OBI ontology tuned for OoEVV curation "+ new Date());
		oes.setTermValue("OBI-derived terminology");
		oes.setFullTermURI("http://bmkeg.isi.edu/ooevv/oesTerminology");

		for( Map<String,String> a : assays) {
			OoevvProcess op = new OoevvProcess();
			op.setDefinition(a.get("defn"));
			op.setFullTermURI(a.get("id"));
			op.setElementType("OoevvEntity");
			op.setTermValue(a.get("name"));
			String stem = "http://purl.obolibrary.org/obo/";
			String shortId = op.getFullTermURI().replace(stem, "");
			op.setShortTermId(shortId);
			
			oes.getOoevvEls().add(op);
		}

		sparql = "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>\n"
				+ "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
				+ "PREFIX obi:<http://purl.obolibrary.org/obo/OBI_>\n"
				+ "PREFIX owl:<http://www.w3.org/2002/07/owl#>\n"
					+ "SELECT ?id ?name ?defn\n" 
					+ "WHERE {\n"
					+ "	?id rdfs:label ?name . \n" 
					+ "	?id <http://purl.obolibrary.org/obo/IAO_0000115> ?defn . \n" // definition
					+ "	?id rdfs:subClassOf* ?p . \n " 
					+ "	{?p rdfs:label \"processed material\"@en} . \n " 
					+ "}\n";

		List<Map<String,String>> entities = bdb.executeSelectQuery(bdb.getRepo(),
					sparql, QueryLanguage.SPARQL, keys);
		
		for( Map<String,String> a : entities) {
			OoevvEntity op = new OoevvEntity();
			op.setDefinition(a.get("defn"));
			op.setFullTermURI(a.get("id"));
			op.setElementType("OoevvEntity");
			op.setTermValue(a.get("name"));
			String stem = "http://purl.obolibrary.org/obo/";
			String shortId = op.getFullTermURI().replace(stem, "");
			op.setShortTermId(shortId);

			oes.getOoevvEls().add(op);
		}

		sparql = "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>\n"
				+ "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
				+ "PREFIX obi:<http://purl.obolibrary.org/obo/OBI_>\n"
				+ "PREFIX owl:<http://www.w3.org/2002/07/owl#>\n"
					+ "SELECT ?id ?name ?defn\n" 
					+ "WHERE {\n"
					+ "	?id rdfs:label ?name . \n" 
					+ "	?id <http://purl.obolibrary.org/obo/IAO_0000115> ?defn . \n" // definition
					+ "	?id rdfs:subClassOf* ?p . \n " 
					+ "	{?p rdfs:label \"data item\"@en} . \n " 
					+ "}\n";
		
		List<Map<String,String>> variables = bdb.executeSelectQuery(bdb.getRepo(),
				sparql, QueryLanguage.SPARQL, keys);
	
		for( Map<String,String> a : variables) {
			ExperimentalVariable op = new ExperimentalVariable();
			op.setDefinition(a.get("defn"));
			op.setFullTermURI(a.get("id"));
			op.setElementType("ExperimentalVariable");
			op.setTermValue(a.get("name"));
			String stem = "http://purl.obolibrary.org/obo/";
			String shortId = op.getFullTermURI().replace(stem, "");
			op.setShortTermId(shortId);

			oes.getOoevvEls().add(op);
		}
				
		OoevvExcelEngine xlEngine = new OoevvExcelEngine();
		HSSFWorkbook wb = xlEngine.generateOoevvExcelWorkbook(oes);

		FileOutputStream out = new FileOutputStream(excelOutput);
		wb.write(out);
		out.close();
		
	}
	
}

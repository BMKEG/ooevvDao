package edu.isi.bmkeg.terminology.utils.bioportal;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import edu.isi.bmkeg.terminology.model.Ontology;
import edu.isi.bmkeg.terminology.model.Term;

/**
 * A service class that provides access to BioPortal's ReST services
 * 
 * @author burns
 */
public class BioportalSearch {

    String currentMatch = "";
    boolean error = false;
    
    private BioPortalSearchHandler h1 = new BioPortalSearchHandler();
    private BioPortalConceptHandler h2 = new BioPortalConceptHandler();
    private BioPortalOntologyHandler h3 = new BioPortalOntologyHandler();
    
	public static String apikey = "apikey=af39dd48-ba3e-43a0-bf72-461030d945d8";
	private String address = "rest.bioontology.org";
	private String searchStem = "/bioportal/search/";
	private String conceptStem = "/bioportal/virtual/ontology/";
	
	public List<Term> nameSearch(String name) throws Exception {
		
		if( name.length() == 0 )
			return new ArrayList<Term>();
		
		URI uri = new URI("http", address, searchStem + name, apikey, null);
		URL url = new URL(uri.toASCIIString());
		
		InputStream is = url.openConnection().getInputStream();        
        SAXParser p = SAXParserFactory.newInstance().newSAXParser();
        p.parse(is, h1);

        return h1.terms;
        
	}
	
	public List<Term> termSearch(int ontologyId, String term) throws Exception {
		
		if( term.length() == 0 )
			return new ArrayList<Term>();
		
		URL url = new URL("http://" + address + conceptStem + ontologyId + "?conceptid=" + term + "&" + apikey);
		
		InputStream is = url.openConnection().getInputStream();        
        SAXParser p = SAXParserFactory.newInstance().newSAXParser();
        p.parse(is, h2);

        return h2.terms;
        
	}
	
	public List<Ontology> ontologySearch(int ontologyId) throws Exception {
				
		URL url = new URL("http://" + address + conceptStem + ontologyId + "?" + apikey);
		
		InputStream is = url.openConnection().getInputStream();        
        SAXParser p = SAXParserFactory.newInstance().newSAXParser();
        p.parse(is, h3);

        return h3.ontologies;
        
	}
	
	
	
}

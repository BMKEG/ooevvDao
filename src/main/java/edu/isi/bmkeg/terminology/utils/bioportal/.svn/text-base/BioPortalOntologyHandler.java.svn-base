package edu.isi.bmkeg.terminology.utils.bioportal;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import edu.isi.bmkeg.terminology.model.Ontology;
import edu.isi.bmkeg.terminology.model.Term;

public class BioPortalOntologyHandler extends DefaultHandler {

	List<Ontology> ontologies;

	Pattern whitespace = Pattern.compile("^[\\s]+$", Pattern.MULTILINE);
	
	String currentMatch = "";
	boolean error = false;
	
	private String fullName;
	private String shortName;
	private String displayName;
	private String description;
	
	public void startDocument() {
		ontologies = new ArrayList<Ontology>();
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attribute) {
		this.currentMatch += "." + qName;
			
	}

	public void endElement(String uri, String localName, String qName) {
		String c = this.currentMatch;
		this.currentMatch = c.substring(0, c.lastIndexOf("." + qName));

		if (c.endsWith(".success")) {
			Ontology o = new Ontology();
			
			o.setFullName(this.fullName);
			o.setShortName(this.shortName);
			o.setDisplayName(this.displayName);
			o.setDescription(this.description);
			
			ontologies.add( o );
	
		}
	
	}

	public void characters(char[] ch, int start, int length) {
		String value = new String(ch, start, length);
		
		Matcher m = whitespace.matcher(value);
		
		if(m.find()) {
			return;
		}

		if (currentMatch.endsWith("success.data.ontologyBean.id")) {
			shortName = value;
		} 
		else if (currentMatch.endsWith("success.data.ontologyBean.ontologyId")) {
			fullName = value;
		} 
		else if (currentMatch.endsWith("success.data.ontologyBean.displayLabel")) {
			displayName = value;
		} 
		else if (currentMatch.endsWith("success.data.ontologyBean.description")) {
			description = value;
		} 

	}

}

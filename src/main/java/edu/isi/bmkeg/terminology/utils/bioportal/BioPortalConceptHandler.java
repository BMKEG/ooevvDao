package edu.isi.bmkeg.terminology.utils.bioportal;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import edu.isi.bmkeg.terminology.model.Term;

public class BioPortalConceptHandler extends DefaultHandler {

	List<Term> terms;

	Pattern whitespace = Pattern.compile("^[\\s]+$", Pattern.MULTILINE);
	
	String currentMatch = "";
	boolean error = false;
	
	private String conceptId;
	private String conceptIdShort;
	private String preferredName;
	private String type;
	private String definition;
	private String isObsolete;
	
	public void startDocument() {
		terms = new ArrayList<Term>();
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attribute) {
		this.currentMatch += "." + qName;
			
	}

	public void endElement(String uri, String localName, String qName) {
		String c = this.currentMatch;
		this.currentMatch = c.substring(0, c.lastIndexOf("." + qName));

		if (c.endsWith(".success")) {
			Term t = new Term();
			
			t.setTermValue(this.preferredName);
			t.setShortTermId(this.conceptIdShort);
			t.setFullTermURI(this.conceptId);
			t.setDefinition(this.definition);
			
			terms.add( t );
	
		}
	
	}

	public void characters(char[] ch, int start, int length) {
		String value = new String(ch, start, length);
		
		Matcher m = whitespace.matcher(value);
		
		if(m.find()) {
			return;
		}

		if (currentMatch.endsWith("success.data.classBean.id")) {
			conceptIdShort = value;
		} 
		else if (currentMatch.endsWith("success.data.classBean.fullId")) {
			conceptId = value;
		} 
		else if (currentMatch.endsWith("success.data.classBean.label")) {
			preferredName = value;
		} 
		else if (currentMatch.endsWith("success.data.classBean.type")) {
			type = value;
		} 
		else if (currentMatch.endsWith("success.data.classBean.definitions.string")) {
			definition = value;
		} 
		else if (currentMatch.endsWith("success.data.classBean.definitions.isObsolete")) {
			isObsolete = value;
		} 

	}

}

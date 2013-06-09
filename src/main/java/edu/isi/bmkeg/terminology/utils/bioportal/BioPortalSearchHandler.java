package edu.isi.bmkeg.terminology.utils.bioportal;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import edu.isi.bmkeg.terminology.model.Term;

public class BioPortalSearchHandler extends DefaultHandler {

	List<Term> terms;

	Pattern whitespace = Pattern.compile("^[\\s]+$", Pattern.MULTILINE);
	
	String currentMatch = "";
	boolean error = false;
	
	private String ontologyVersionId;
	private String ontologyId;
	private String ontologyDisplayLabel;
	private String recordType;
	private String objectType;
	private String conceptId;
	private String conceptIdShort;
	private String preferredName;
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

		if (c.endsWith(".searchResultList.searchBean")) {
			Term t = new Term();
			
			t.setTermValue(this.preferredName);
			t.setShortTermId(this.conceptIdShort);
			t.setFullTermURI(this.conceptId);
			
			terms.add( t );
	
		}
	
	}

	public void characters(char[] ch, int start, int length) {
		String value = new String(ch, start, length);
		
		Matcher m = whitespace.matcher(value);
		
		if(m.find()) {
			return;
		}

		if (currentMatch.endsWith(".searchResultList.searchBean.ontologyVersionId")) {
			ontologyVersionId = value;
		} 
		else if (currentMatch.endsWith(".searchResultList.searchBean.ontologyId")) {
			ontologyId = value;
		} 
		else if (currentMatch.endsWith(".searchResultList.searchBean.ontologyDisplayLabel")) {
			ontologyDisplayLabel = value;
		} 
		else if (currentMatch.endsWith(".searchResultList.searchBean.recordType")) {
			recordType = value;
		} 
		else if (currentMatch.endsWith(".searchResultList.searchBean.objectType")) {
			objectType = value;
		} 
		else if (currentMatch.endsWith(".searchResultList.searchBean.conceptId")) {
			conceptId = value;
		} 
		else if (currentMatch.endsWith(".searchResultList.searchBean.conceptIdShort")) {
			conceptIdShort = value;
		} 
		else if (currentMatch.endsWith(".searchResultList.searchBean.preferredName")) {
			preferredName = value;
		} 		
		else if (currentMatch.endsWith(".searchResultList.searchBean.isObsolete")) {
			isObsolete = value;
		} 

	}

}

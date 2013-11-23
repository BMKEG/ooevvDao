/**
 * 
 */
package edu.isi.bmkeg.ooevv.prov;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.Agent;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.HasExtensibility;
import org.openprovenance.prov.model.NamedBundle;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.xml.NamespacePrefixMapper;
import org.openprovenance.prov.xml.ProvFactory;
import org.openprovenance.prov.xml.ProvUtilities;
import org.openprovenance.prov.xml.ValueConverter;

import com.google.common.io.Files;

import edu.isi.bmkeg.ooevv.model.ExperimentalVariable;
import edu.isi.bmkeg.ooevv.model.OoevvElement;
import edu.isi.bmkeg.ooevv.model.OoevvElementSet;
import edu.isi.bmkeg.ooevv.model.OoevvEntity;
import edu.isi.bmkeg.ooevv.model.OoevvProcess;
import edu.isi.bmkeg.ooevv.model.scale.BinaryScale;
import edu.isi.bmkeg.ooevv.model.scale.BinaryScaleWithNamedValues;
import edu.isi.bmkeg.ooevv.model.scale.CompositeScale;
import edu.isi.bmkeg.ooevv.model.scale.DecimalScale;
import edu.isi.bmkeg.ooevv.model.scale.FileScale;
import edu.isi.bmkeg.ooevv.model.scale.HierarchicalScale;
import edu.isi.bmkeg.ooevv.model.scale.IntegerScale;
import edu.isi.bmkeg.ooevv.model.scale.MeasurementScale;
import edu.isi.bmkeg.ooevv.model.scale.NaturalLanguageScale;
import edu.isi.bmkeg.ooevv.model.scale.NominalScale;
import edu.isi.bmkeg.ooevv.model.scale.NominalScaleWithAllowedTerms;
import edu.isi.bmkeg.ooevv.model.scale.NumericScale;
import edu.isi.bmkeg.ooevv.model.scale.OrdinalScale;
import edu.isi.bmkeg.ooevv.model.scale.OrdinalScaleWithMaxRank;
import edu.isi.bmkeg.ooevv.model.scale.OrdinalScaleWithNamedRanks;
import edu.isi.bmkeg.ooevv.model.scale.RelativeTermScale;
import edu.isi.bmkeg.ooevv.model.scale.TimestampScale;
import edu.isi.bmkeg.terminology.model.Term;
import edu.isi.bmkeg.utils.Converters;

/**
 * Utility function to convert an OoevvElementSet to a set of Prov entities.
 * 
 * @author Gully APC Burns
 * (derived from org.openprovenance.prov.xml.ContextualizationPC1Test unit test from prov-xml)
 * 
 */
public class OoevvProvBuilder {

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// PROV Variables
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public static final String SKS_NS = "http://sciknowsoft.org/";
	public static final String OOEVV_PREFIX = "ooevv";
	public static final String OOEVV_NS = SKS_NS + OOEVV_PREFIX + "#";
	public static final String KEFED_PREFIX = "kefed";
	public static final String KEFED_NS = SKS_NS + KEFED_PREFIX + "#";
	
	public static final String PRIM_NS = "http://openprovenance.org/primitives#";
	public static final String PRIM_PREFIX = "prim";
	public static final String DOT_NS = "http://openprovenance.org/Toolbox/dot#";
	public static final String DOT_PREFIX = "dot";
	
	static String PATH_PROPERTY = "http://openprovenance.org/primitives#path";
	static String URL_PROPERTY = "http://openprovenance.org/primitives#url";
	static String PRIMITIVE_PROPERTY = "http://openprovenance.org/primitives#primitive";
	
	static final Hashtable<String, String> namespaces;
	private Map<String, QName> ooevvTypes = new HashMap<String, QName>();

	public static ProvFactory pFactory;
	static final ProvUtilities util = new ProvUtilities();
	public static ValueConverter vconv;

	static {
		
		namespaces = new Hashtable<String, String>();

		namespaces.put("ooevv", OOEVV_NS);
		namespaces.put("kefed", KEFED_NS);
		namespaces.put("prim", PRIM_NS);
		namespaces.put("dot", DOT_NS);
		namespaces.put("xsd", NamespacePrefixMapper.XSD_NS);
		
		pFactory = new ProvFactory(namespaces);
		vconv = new ValueConverter(pFactory);
	
	}
	
	public boolean urlFlag = true;
	
	public static Document graph;


	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// OoEVV Variables
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private OoevvElementSet oes;
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public OoevvProvBuilder() {
		this.pFactory = ProvFactory.getFactory();
	}
	
	public void generateProvFilesForOoevvElementSet(OoevvElementSet oes, File zip) throws Exception {
		
		Document provDoc = pFactory.newDocument();
		
		NamedBundle ooevvBundle = makeOoevvTopGraph(pFactory);
		provDoc.getStatementOrBundle().add(ooevvBundle);

		NamedBundle repBundle = makeOoevvFullGraph(pFactory, oes);
		provDoc.getStatementOrBundle().add(repBundle);

		namespaces.put(oes.getShortName(), oes.getNs()); 
		namespaces.put("xsd", NamespacePrefixMapper.XSD_NS);
		namespaces.put(DOT_PREFIX, DOT_NS);
		pFactory.setNamespaces(namespaces);	

		provDoc.setNss(namespaces);

		//
		// Write to temp file
		//
		File tempDir = Files.createTempDir();
		File ooevvProvnFile = new File(tempDir.getPath() + "/" + oes.getShortName() + "_ooevvProv.provn");		
		File ooevvXmlFile = new File(tempDir.getPath() + "/" + oes.getShortName() + "_ooevvProv.xml");		
		File ooevvJsonFile = new File(tempDir.getPath() + "/" + oes.getShortName() + "_ooevvProv.json");
		File ooevvDotFile = new File(tempDir.getPath() + "/" + oes.getShortName() + "_ooevvProv.dot");
		File ooevvPdfFile = new File(tempDir.getPath() + "/" + oes.getShortName() + "_ooevvProv.pdf");
		File ooevvRdfFile = new File(tempDir.getPath() + "/" + oes.getShortName() + "_ooevvProv.rdf");
		
		InteropFramework iop = new InteropFramework();
		
		//
		// Build PROV-N document, XML, JSON, PDF and RDF
		//
		iop.writeDocument(ooevvProvnFile.getPath(), provDoc);
		iop.writeDocument(ooevvXmlFile.getPath(), provDoc);
		iop.writeDocument(ooevvJsonFile.getPath(), provDoc);
		iop.writeDocument(ooevvDotFile.getPath(), provDoc);
		iop.writeDocument(ooevvRdfFile.getPath(), provDoc);		
				
		//
		// Build ZIP
		//
		Map<String, File> filesToZip = new HashMap<String, File>();
		Converters.zipPrep(tempDir.getPath(), tempDir, filesToZip);
		Converters.zipIt(filesToZip, zip);
		
		//
		// Clean up temp directory
		//
		Converters.recursivelyDeleteFiles(tempDir);
		
	}
	


	public void addValue(HasExtensibility p1, String val) {
		pFactory.addAttribute(p1, OOEVV_NS, OOEVV_PREFIX, "value", val, vconv);
	}

	public void addUrl(HasExtensibility p1, String val) {
		pFactory.addAttribute(p1, OOEVV_NS, OOEVV_PREFIX, "url", val, vconv);
	}
	
	public Entity newFile(ProvFactory pFactory, String id, String label,
			String file, String location) {

		org.openprovenance.prov.model.Entity a = pFactory.newEntity(id, label);
		pFactory.addType(a,
				URI.create("http://openprovenance.org/primitives#File"));

		addUrl(a, location + file);

		return a;
	}

	public Entity newParameter(ProvFactory pFactory, String id, String label,
			String value) {

		Entity a = pFactory.newEntity(id, label);
		pFactory.addType(a,
				URI.create("http://openprovenance.org/primitives#String"));

		addValue(a, value);

		return a;
	
	}
	
	
	/**
	 * Create the Ooevv PROV model here.
	 * @param pFactory
	 * @param oes
	 * @return
	 */
	public NamedBundle makeOoevvFullGraph(ProvFactory pFactory,
			OoevvElementSet oes) {
		
		Set<Entity> entities = new HashSet<Entity>();
		Set<Activity> activities = new HashSet<Activity>();
		
		for(Term t : oes.getTerm()) {

			QName thisQn = new QName( oes.getNs(), 
					t.getShortTermId(), 
					oes.getShortName() );
			
			if( t instanceof ExperimentalVariable ) {
				
				ExperimentalVariable exptVb = (ExperimentalVariable) t;
				if( exptVb.getPartOf().size() > 0 ) {
					continue;
				}

				QName parentQn = new QName( OOEVV_NS, "ev", OOEVV_PREFIX );				

				Entity e = pFactory.newEntity(thisQn, 
						t.getTermValue());
				pFactory.addType(e, parentQn, ValueConverter.QNAME_XSD_QNAME);		
				entities.add(e);	
				
			}
			else if( t instanceof MeasurementScale ) {
				
				MeasurementScale ms = (MeasurementScale) t;
				QName parentQn = new QName( OOEVV_NS, 
						createOoevvAbb(ms.getClassType()),
						OOEVV_PREFIX );
				Entity e = pFactory.newEntity(thisQn, 
						t.getTermValue());
				
				pFactory.addType(e, parentQn, ValueConverter.QNAME_XSD_QNAME);		
				entities.add(e);				
			}
			else if( t instanceof OoevvEntity ) {
			
				Entity e = pFactory.newEntity(thisQn, 
						t.getTermValue());
				entities.add(e);				

			} else if( t instanceof OoevvProcess ) {
				
				Activity a = pFactory.newActivity(thisQn, 
						t.getTermValue());
				activities.add(a);				

			}

		}	
		
		Activity[] aArray = activities.toArray(new Activity[]{});
		Entity[] eArray = entities.toArray(new Entity[]{});
		
		QName qn = new QName( oes.getNs(), 
				oes.getShortName(), 
				oes.getShortName() );
		
		NamedBundle graph = pFactory.newNamedBundle(qn, 
				activities,
				entities,
				new ArrayList<Agent>(), 
				new ArrayList<Statement>()
		);

		Hashtable<String,String> namespaces = new Hashtable<String, String>();
		namespaces.put(oes.getShortName(), oes.getNs());
		graph.setNss(namespaces);
		
		return graph;
	}
	
	/**
	 * Create the Ooevv PROV model here.
	 * @param pFactory
	 * @param oes
	 * @return
	 */
	public NamedBundle makeOoevvTopGraph(ProvFactory pFactory) {
		
		Class[] entityClasses = new Class[] {
				OoevvEntity.class,
				ExperimentalVariable.class,
				MeasurementScale.class,
				BinaryScale.class,
				BinaryScaleWithNamedValues.class,
				BinaryScaleWithNamedValues.class,
				NumericScale.class,
				DecimalScale.class,
				IntegerScale.class,
				TimestampScale.class,
				HierarchicalScale.class,
				CompositeScale.class,
				NominalScale.class,
				NominalScaleWithAllowedTerms.class,
				OrdinalScale.class,
				OrdinalScaleWithMaxRank.class,
				OrdinalScaleWithNamedRanks.class,
				NaturalLanguageScale.class,
				RelativeTermScale.class,
				FileScale.class
		};

		Set<Entity> entities = new HashSet<Entity>();
		for( int i=0; i< entityClasses.length; i++) {
			Class c = entityClasses[i];

			QName thisQn = new QName( OOEVV_NS, 
					createOoevvAbb(c.getSimpleName()), 
					OOEVV_PREFIX );
			Entity e = pFactory.newEntity(thisQn, c.getSimpleName() );

			//
			// Set all types of non-top-level-elements as
			// PROV extensions of entities
			//
			if( !c.getSuperclass().equals( OoevvElement.class ) &&
					!c.getSuperclass().equals( Term.class )  ) {

				QName parentQn = new QName( OOEVV_NS, 
						createOoevvAbb(c.getSuperclass().getSimpleName()), 
						OOEVV_PREFIX );
				ooevvTypes.put( c.getSimpleName(), thisQn);
				pFactory.addType(e, parentQn, ValueConverter.QNAME_XSD_QNAME);	
			} 

			entities.add(e);

		}
		Entity[] eArray = entities.toArray(new Entity[]{});
		
		QName ooevvQn = new QName( OOEVV_NS, 
				OOEVV_PREFIX, 
				OOEVV_PREFIX );
		
		NamedBundle graph = pFactory.newNamedBundle(ooevvQn, 
				new ArrayList<Activity>(),
				entities,
				new ArrayList<Agent>(), 
				new ArrayList<Statement>()
		);
		
		Hashtable<String,String> namespaces = new Hashtable<String, String>();
		namespaces.put(OOEVV_PREFIX, OOEVV_NS);
		graph.setNss(namespaces);

		return graph;
	}
	
	private String createOoevvAbb(String name) {
		String abb = "";
		for( int j=0; j<name.length(); j++) {
			char jc = name.charAt(j);
			if( Character.isUpperCase(jc) ) {
				abb += Character.toLowerCase(jc);
			}
		}
		return abb;
	}
	
}

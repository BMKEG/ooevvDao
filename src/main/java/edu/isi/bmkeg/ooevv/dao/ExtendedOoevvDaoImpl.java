/** $Id: OoevvDaoImpl.java 2628 2011-07-21 01:01:24Z tom $
 * 
 */
package edu.isi.bmkeg.ooevv.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.isi.bmkeg.ooevv.model.ExperimentalVariable;
import edu.isi.bmkeg.ooevv.model.OoevvElement;
import edu.isi.bmkeg.ooevv.model.OoevvElementSet;
import edu.isi.bmkeg.ooevv.model.OoevvEntity;
import edu.isi.bmkeg.ooevv.model.OoevvProcess;
import edu.isi.bmkeg.ooevv.model.scale.BinaryScaleWithNamedValues;
import edu.isi.bmkeg.ooevv.model.scale.CompositeScale;
import edu.isi.bmkeg.ooevv.model.scale.HierarchicalScale;
import edu.isi.bmkeg.ooevv.model.scale.MeasurementScale;
import edu.isi.bmkeg.ooevv.model.scale.NominalScaleWithAllowedTerms;
import edu.isi.bmkeg.ooevv.model.scale.OrdinalScaleWithNamedRanks;
import edu.isi.bmkeg.ooevv.model.scale.RelativeTermScale;
import edu.isi.bmkeg.ooevv.model.value.HierarchicalValue;
import edu.isi.bmkeg.ooevv.model.value.MeasurementValue;
import edu.isi.bmkeg.ooevv.model.value.NominalValue;
import edu.isi.bmkeg.ooevv.model.value.OrdinalValue;
import edu.isi.bmkeg.terminology.model.Ontology;
import edu.isi.bmkeg.terminology.model.Term;
import edu.isi.bmkeg.utils.superGraph.SuperGraphNode;
import edu.isi.bmkeg.vpdmf.dao.CoreDao;
import edu.isi.bmkeg.vpdmf.dao.CoreDaoImpl;
import edu.isi.bmkeg.vpdmf.model.definitions.PrimitiveDefinition;
import edu.isi.bmkeg.vpdmf.model.definitions.ViewDefinition;
import edu.isi.bmkeg.vpdmf.model.instances.AttributeInstance;
import edu.isi.bmkeg.vpdmf.model.instances.LightViewInstance;
import edu.isi.bmkeg.vpdmf.model.instances.PrimitiveInstance;
import edu.isi.bmkeg.vpdmf.model.instances.ViewBasedObjectGraph;
import edu.isi.bmkeg.vpdmf.model.instances.ViewInstance;

/**
 * DAO interface for OoEVV objects. Data formats are based on Hibernate, VPDMf
 * and OWL.
 * 
 * @author University of Southern California
 * @date $Date: 2011-07-20 18:01:24 -0700 (Wed, 20 Jul 2011) $
 * @version $Revision: 2628 $
 * 
 */
@Repository
@Transactional
public class ExtendedOoevvDaoImpl implements ExtendedOoevvDao {

	private Set<ExperimentalVariable> exptVbs = new HashSet<ExperimentalVariable>();
	private Set<MeasurementScale> scales = new HashSet<MeasurementScale>();
	private Set<Term> terms = new HashSet<Term>();

	@Autowired
	private CoreDao coreDao;

	private Map<String, ViewBasedObjectGraph> vbogs;
	
	public ExtendedOoevvDaoImpl() throws Exception {}

	public void init(String login, String password, String uri) throws Exception {
		
		if( coreDao == null ) {
			this.coreDao = new CoreDaoImpl();
		}
		
		this.coreDao.init(login, password, uri);
		this.vbogs = this.coreDao.generateVbogs();
		
	}
	
	public CoreDao getCoreDao() {
		return coreDao;
	}

	public void setCoreDao(CoreDao coreDao) {
		this.coreDao = coreDao;
	}

	public Map<String, ViewBasedObjectGraph> getVbogs() {
		return vbogs;
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Construct sets of OoEVV Elements from database
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	/*public Set<OoevvProcess> listProcessesInDatabase(OoevvElementSet exptVbSet) {

		OoevvProcess_qo op_qo = new OoevvProcess_qo();
		
	}
	
	public Set<OoevvEntity> listEntitiesInObjectGraph(
			OoevvElementSet exptVbSet) {

		Set<OoevvEntity> entities = new HashSet<OoevvEntity>();

		Iterator<OoevvElement> entityIt = exptVbSet.getElements().iterator();
		while (entityIt.hasNext()) {
			OoevvElement el = entityIt.next();
			if( el instanceof OoevvEntity ) {	
				OoevvEntity entity = (OoevvEntity) el;	
				entities.add(entity);
			}
		}

		return entities;
	}*/
	
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Insert data into database from Object Graph
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public static Set<OoevvProcess> listProcessesInObjectGraph(
			OoevvElementSet exptVbSet) {

		Set<OoevvProcess> procs = new HashSet<OoevvProcess>();

		Iterator<OoevvElement> procIt = exptVbSet.getElements().iterator();
		while (procIt.hasNext()) {
			OoevvElement el = procIt.next();
			if( el instanceof OoevvProcess ) {				
				OoevvProcess proc = (OoevvProcess) el;
				procs.add(proc);
			}

		}

		return procs;
	}


	public static Set<OoevvEntity> listEntitiesInObjectGraph(
			OoevvElementSet exptVbSet) {

		Set<OoevvEntity> entities = new HashSet<OoevvEntity>();

		Iterator<OoevvElement> entityIt = exptVbSet.getElements().iterator();
		while (entityIt.hasNext()) {
			OoevvElement el = entityIt.next();
			if( el instanceof OoevvEntity ) {	
				OoevvEntity entity = (OoevvEntity) el;	
				entities.add(entity);
			}
		}

		return entities;
	}

	public static Set<ExperimentalVariable> listExptVbsInObjectGraph(
			OoevvElementSet exptVbSet) {

		Set<ExperimentalVariable> exptVbs = new HashSet<ExperimentalVariable>();

		Iterator<OoevvElement> exptVbIt = exptVbSet.getElements().iterator();
		while (exptVbIt.hasNext()) {
			OoevvElement el = exptVbIt.next();
			if( el instanceof ExperimentalVariable ) {	
				ExperimentalVariable exptVb = (ExperimentalVariable) el;
				exptVbs.add(exptVb);
			}
		}

		return exptVbs;
	}

	public static Set<MeasurementScale> listScalesInObjectGraph(
			OoevvElementSet exptVbSet) {

		Set<MeasurementScale> scales = new HashSet<MeasurementScale>();

		Iterator<OoevvElement> exptVbIt = exptVbSet.getElements().iterator();
		while (exptVbIt.hasNext()) {
			OoevvElement el = exptVbIt.next();
			if( el instanceof ExperimentalVariable ) {	
				ExperimentalVariable exptVb = (ExperimentalVariable) el;
				if (exptVb.getScale() != null) {
					MeasurementScale ms = exptVb.getScale();
					scales.add(ms);
				}
			}
		}
		
		return scales;

	}

	public static Set<MeasurementValue> listValuesInObjectGraph(
			OoevvElementSet exptVbSet) {

		// explore the data and list all the objects in an accessible way.
		Set<MeasurementValue> values = new HashSet<MeasurementValue>();

		Iterator<OoevvElement> exptVbIt = exptVbSet.getElements().iterator();
		while (exptVbIt.hasNext()) {
			OoevvElement el = exptVbIt.next();
			if( !(el instanceof ExperimentalVariable) ) {
				continue;
			}
			
			ExperimentalVariable exptVb = (ExperimentalVariable) el;
			
			if (exptVb.getScale() != null) {

				MeasurementScale ms = exptVb.getScale();

				if (ms instanceof BinaryScaleWithNamedValues) {

					BinaryScaleWithNamedValues bswnv = (BinaryScaleWithNamedValues) ms;
					values.add(bswnv.getTrueValue());
					values.add(bswnv.getFalseValue());

				} else if (ms instanceof NominalScaleWithAllowedTerms) {

					NominalScaleWithAllowedTerms nswat = (NominalScaleWithAllowedTerms) ms;
					Iterator<NominalValue> tIt = nswat.getNominalValues()
							.iterator();
					while (tIt.hasNext()) {
						NominalValue nv = tIt.next();
						values.add(nv);
					}

				} else if (ms instanceof OrdinalScaleWithNamedRanks) {

					OrdinalScaleWithNamedRanks pswnr = (OrdinalScaleWithNamedRanks) ms;
					Iterator<OrdinalValue> tIt = pswnr.getOrdinalValues()
							.iterator();
					while (tIt.hasNext()) {
						OrdinalValue ov = tIt.next();
						values.add(ov);
					}

				} else if (ms instanceof HierarchicalScale) {

					HierarchicalScale hts = (HierarchicalScale) ms;
					Iterator<HierarchicalValue> tIt = hts
							.getHierarchicalValues().iterator();
					while (tIt.hasNext()) {
						HierarchicalValue hv = tIt.next();
						values.add(hv);
					}

				}

			}

		}

		return values;

	}

	public static Set<Term> listTerms(OoevvElementSet exptVbSet) {

		// explore the data and list all the objects in an accessible way.
		Set<Term> terms = new HashSet<Term>();

		terms.add(exptVbSet);

		Iterator<OoevvElement> exptVbIt = exptVbSet.getElements().iterator();
		while (exptVbIt.hasNext()) {
			OoevvElement el = exptVbIt.next();
			if( !(el instanceof ExperimentalVariable) ) {
				continue;
			}
			
			ExperimentalVariable exptVb = (ExperimentalVariable) el;
			
			terms.add(exptVb);

			if (exptVb.getMeasures() != null)
				terms.add(exptVb.getMeasures());

			if (exptVb.getScale() != null) {

				MeasurementScale ms = exptVb.getScale();
				terms.add(ms);

				if (ms instanceof BinaryScaleWithNamedValues) {

					BinaryScaleWithNamedValues bswnv = (BinaryScaleWithNamedValues) ms;
					terms.add(bswnv.getTrueValue());
					terms.add(bswnv.getFalseValue());

				} else if (ms instanceof NominalScaleWithAllowedTerms) {

					NominalScaleWithAllowedTerms nswat = (NominalScaleWithAllowedTerms) ms;
					Iterator<NominalValue> tIt = nswat.getNominalValues()
							.iterator();
					while (tIt.hasNext()) {
						NominalValue nv = tIt.next();
						terms.add(nv);
					}

				} else if (ms instanceof OrdinalScaleWithNamedRanks) {

					OrdinalScaleWithNamedRanks pswnr = (OrdinalScaleWithNamedRanks) ms;
					Iterator<OrdinalValue> tIt = pswnr.getOrdinalValues()
							.iterator();
					while (tIt.hasNext()) {
						OrdinalValue ov = tIt.next();
						terms.add(ov);
					}

				} else if (ms instanceof RelativeTermScale) {

					RelativeTermScale rts = (RelativeTermScale) ms;
					Iterator<Term> tIt = rts.getAllowedRelations().iterator();
					while (tIt.hasNext()) {
						terms.add(tIt.next());
					}

				} else if (ms instanceof HierarchicalScale) {

					HierarchicalScale hts = (HierarchicalScale) ms;
					Iterator<HierarchicalValue> tIt = hts
							.getHierarchicalValues().iterator();
					while (tIt.hasNext()) {
						HierarchicalValue hv = tIt.next();
						terms.add(hv);
					}

				}

			}

		}

		Iterator<OoevvElement> procIt = exptVbSet.getElements().iterator();
		while (procIt.hasNext()) {
			OoevvElement el = procIt.next();
			if( !(el instanceof OoevvProcess) ) {
				continue;
			}
			OoevvProcess proc = (OoevvProcess) el;

			terms.add(proc);
			if (proc.getObiTerm() != null) {
				terms.add(proc.getObiTerm());
			}
		}

		Iterator<OoevvElement> entIt = exptVbSet.getElements().iterator();
		while (entIt.hasNext()) {
			OoevvElement el = entIt.next();
			if( !(el instanceof OoevvEntity) ) {
				continue;
			}
			OoevvEntity ent = (OoevvEntity) el;

			terms.add(ent);
			if (ent.getObiTerm() != null) {
				terms.add(ent.getObiTerm());
			}
		}

		return terms;

	}

	// ~~~~~~~~~~~~~~~~~~~~~
	// Database interactions
	// ~~~~~~~~~~~~~~~~~~~~~
	public void insertOoevvElementSetInDatabase(OoevvElementSet exptVbSet)
			throws Exception {

		Set<ExperimentalVariable> exptVbs = ExtendedOoevvDaoImpl
				.listExptVbsInObjectGraph(exptVbSet);
		Set<OoevvProcess> procs = ExtendedOoevvDaoImpl
				.listProcessesInObjectGraph(exptVbSet);
		Set<OoevvEntity> ents = ExtendedOoevvDaoImpl
				.listEntitiesInObjectGraph(exptVbSet);
		Set<MeasurementScale> scales = ExtendedOoevvDaoImpl
				.listScalesInObjectGraph(exptVbSet);
		Set<MeasurementValue> values = ExtendedOoevvDaoImpl
				.listValuesInObjectGraph(exptVbSet);
		Set<Term> terms = ExtendedOoevvDaoImpl.listTerms(exptVbSet);

		try {

			coreDao.getCe().connectToDB();
			coreDao.getCe().turnOffAutoCommit();

			//
			// 1. insert the OoeVV ontology if it's not there.
			//
			ViewBasedObjectGraph vbog1 = new ViewBasedObjectGraph(coreDao.getTop(), coreDao.getCl(), "Ontology");
			Term t = terms.iterator().next();
			Ontology ont = t.getOntology();
			
			ViewInstance vi1 = vbog1.objectGraphToView(ont);
			Map<String, Object> objMap1 = vbog1.getObjMap();
			coreDao.getCe().executeInsertQuery(vi1);

			Iterator<String> keyIt1 = objMap1.keySet().iterator();
			while (keyIt1.hasNext()) {
				String key = keyIt1.next();
				PrimitiveInstance pi = (PrimitiveInstance) vi1.getSubGraph()
						.getNodes().get(key);
				Object o = objMap1.get(key);
				vbog1.primitiveToObject(pi, o, true);
			}

			//
			// 2. insert all the different MeasurementValues as views
			//
			Iterator<MeasurementValue> vIt = values.iterator();
			while (vIt.hasNext()) {
				MeasurementValue v = vIt.next();

				String vName = v.getClass().getName();
				vName = vName.substring(vName.lastIndexOf(".") + 1,
						vName.length());
				ViewBasedObjectGraph vbog = new ViewBasedObjectGraph(coreDao.getTop(), coreDao.getCl(), vName);
				
				ViewInstance vi = null;
				try {
					vi = vbog.objectGraphToView(v);
				} catch (Exception e) {
					System.err
							.println("Conversion error from object graph to view in value: "
									+ v.getShortTermId());
					throw e;
				}
				Map<String, Object> objMap = vbog.getObjMap();

				coreDao.getCe().executeInsertQuery(vi);

				Iterator<String> keyIt = objMap.keySet().iterator();
				while (keyIt.hasNext()) {
					String key = keyIt.next();
					PrimitiveInstance pi = (PrimitiveInstance) vi.getSubGraph()
							.getNodes().get(key);
					Object o = objMap.get(key);
					vbog.primitiveToObject(pi, o, true);
				}

			}

			//
			// 3. insert all the different MeasurementScales as views
			//
			Iterator<MeasurementScale> sIt = scales.iterator();
			while (sIt.hasNext()) {
				MeasurementScale s = sIt.next();

				String sName = s.getClass().getName();
				sName = sName.substring(sName.lastIndexOf(".") + 1,
						sName.length());
				ViewBasedObjectGraph vbog = new ViewBasedObjectGraph(coreDao.getTop(), coreDao.getCl(), sName);
				
				ViewInstance vi = null;
				try {
					vi = vbog.objectGraphToView(s);
				} catch (Exception e) {
					System.err
							.println("Conversion error from object graph to view in scale: "
									+ s.getShortTermId());
					throw e;
				}
				Map<String, Object> objMap = vbog.getObjMap();

				if( s instanceof CompositeScale ) {
					Map<String,SuperGraphNode> g = vi.getDefinition().getSubGraph().getNodes();
					PrimitiveDefinition pd = (PrimitiveDefinition ) g.get("ExperimentalVariable");
					vi.nullify(pd);
					pd = (PrimitiveDefinition ) g.get("ExperimentalVariableTerm");
					vi.nullify(pd);
					pd = (PrimitiveDefinition ) g.get("ExperimentalVariableOntology");
					vi.nullify(pd);
				}
				
				coreDao.getCe().executeInsertQuery(vi);

				Iterator<String> keyIt = objMap.keySet().iterator();
				while (keyIt.hasNext()) {
					String key = keyIt.next();
					PrimitiveInstance pi = (PrimitiveInstance) vi.getSubGraph()
							.getNodes().get(key);
					Object o = objMap.get(key);
					try {
						vbog.primitiveToObject(pi, o, true);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}

			//
			// 4. insert all the different Variables as views
			//
			Iterator<ExperimentalVariable> vbIt = exptVbs.iterator();
			while (vbIt.hasNext()) {
				ExperimentalVariable v = vbIt.next();

				ViewBasedObjectGraph vbog = new ViewBasedObjectGraph(coreDao.getTop(), coreDao.getCl(), "ExperimentalVariable");

				ViewInstance vi = null;
				try {
					vi = vbog.objectGraphToView(v);
				} catch (Exception e) {
					System.err
							.println("Conversion error from object graph to view in variable: "
									+ v.getShortTermId());
					throw e;
				}
				Map<String, Object> objMap = vbog.getObjMap();

				coreDao.getCe().executeInsertQuery(vi);

				Iterator<String> keyIt = objMap.keySet().iterator();
				while (keyIt.hasNext()) {
					String key = keyIt.next();
					PrimitiveInstance pi = (PrimitiveInstance) vi.getSubGraph()
							.getNodes().get(key);
					Object o = objMap.get(key);
					vbog.primitiveToObject(pi, o, true);
				}

			}
			
			//
			// 4a. repeat the insertion of the Composite Scales 
			// to take into account the connections to the variables
			//
			sIt = scales.iterator();
			while (sIt.hasNext()) {
				MeasurementScale s = sIt.next();
				
				if( !(s instanceof CompositeScale) )
					continue;

				String sName = s.getClass().getName();
				sName = sName.substring(sName.lastIndexOf(".") + 1,
						sName.length());
				ViewBasedObjectGraph vbog = new ViewBasedObjectGraph(coreDao.getTop(), coreDao.getCl(), sName);

				ViewInstance vi = null;
				try {
					vi = vbog.objectGraphToView(s);
				} catch (Exception e) {
					System.err
							.println("Conversion error from object graph to view in scale: "
									+ s.getShortTermId());
					throw e;
				}
				Map<String, Object> objMap = vbog.getObjMap();

				coreDao.getCe().storeViewInstanceForUpdate(vi);
				coreDao.getCe().executeUpdateQuery(vi);

				Iterator<String> keyIt = objMap.keySet().iterator();
				while (keyIt.hasNext()) {
					String key = keyIt.next();
					PrimitiveInstance pi = (PrimitiveInstance) vi.getSubGraph()
							.getNodes().get(key);
					Object o = objMap.get(key);
					try {
						vbog.primitiveToObject(pi, o, true);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
			
			
			

			//
			// 5. insert all the different Processes as views
			//
			Iterator<OoevvProcess> pIt = procs.iterator();
			while (pIt.hasNext()) {
				OoevvProcess v = pIt.next();

				ViewBasedObjectGraph vbog = new ViewBasedObjectGraph(coreDao.getTop(), coreDao.getCl(), "OoevvProcess");

				ViewInstance vi = null;
				try {
					vi = vbog.objectGraphToView(v);
				} catch (Exception e) {
					System.err
							.println("Conversion error from object graph to view in process: "
									+ v.getShortTermId());
					throw e;
				}
				Map<String, Object> objMap = vbog.getObjMap();

				coreDao.getCe().executeInsertQuery(vi);

				Iterator<String> keyIt = objMap.keySet().iterator();
				while (keyIt.hasNext()) {
					String key = keyIt.next();
					PrimitiveInstance pi = (PrimitiveInstance) vi.getSubGraph()
							.getNodes().get(key);
					Object o = objMap.get(key);
					vbog.primitiveToObject(pi, o, true);
				}

			}

			//
			// 6. insert all the different Entities as views
			//
			Iterator<OoevvEntity> entIt = ents.iterator();
			while (entIt.hasNext()) {
				OoevvEntity v = entIt.next();

				ViewBasedObjectGraph vbog = new ViewBasedObjectGraph(coreDao.getTop(), coreDao.getCl(), "OoevvEntity");

				ViewInstance vi = null;
				try {
					vi = vbog.objectGraphToView(v);
				} catch (Exception e) {
					System.err
							.println("Conversion error from object graph to view in variable: "
									+ v.getShortTermId());
					throw e;
				}
				Map<String, Object> objMap = vbog.getObjMap();

				coreDao.getCe().executeInsertQuery(vi);

				Iterator<String> keyIt = objMap.keySet().iterator();
				while (keyIt.hasNext()) {
					String key = keyIt.next();
					PrimitiveInstance pi = (PrimitiveInstance) vi.getSubGraph()
							.getNodes().get(key);
					Object o = objMap.get(key);
					vbog.primitiveToObject(pi, o, true);
				}

			}

			//
			// 6. insert the OoevvElementSet as a view
			//
			ViewBasedObjectGraph vbog = new ViewBasedObjectGraph(coreDao.getTop(), coreDao.getCl(), "OoevvElementSet");
			ViewInstance vi = vbog.objectGraphToView(exptVbSet);
			Map<String, Object> objMap = vbog.getObjMap();

			coreDao.getCe().executeInsertQuery(vi);

			Iterator<String> keyIt = objMap.keySet().iterator();
			while (keyIt.hasNext()) {
				String key = keyIt.next();
				PrimitiveInstance pi = (PrimitiveInstance) vi.getSubGraph()
						.getNodes().get(key);
				Object o = objMap.get(key);
				vbog.primitiveToObject(pi, o, true);
			}

			coreDao.getCe().commitTransaction();

		} catch (Exception e) {

			coreDao.getCe().rollbackTransaction();
			e.printStackTrace();
			throw e;

		} finally {

			coreDao.getCe().closeDbConnection();

		}

	}
	
	public List<OoevvElementSet> listOoevvElementSetsFromName(String name)
			throws Exception {

		ViewDefinition vd = coreDao.getTop().getViews().get("OoevvElementSet");
		ViewBasedObjectGraph vbog = new ViewBasedObjectGraph(coreDao.getTop(), coreDao.getCl(),
				"OoevvElementSet");

		ViewInstance vi = new ViewInstance(vd);
		AttributeInstance ai = vi.readAttributeInstance(
				"]OoevvElementSet|OoevvElementSet.name", 0);
		ai.writeValueString(name);

		List<OoevvElementSet> l = new ArrayList<OoevvElementSet>();
		Iterator<ViewInstance> it = coreDao.getCe().executeFullQuery(vi).iterator();
		while (it.hasNext()) {
			ViewInstance lvi = it.next();

			vbog.viewToObjectGraph(lvi);
			OoevvElementSet oa = (OoevvElementSet) vbog.readPrimaryObject();

			l.add(oa);

		}

		return l;

	}

	public List<OoevvElementSet> loadAllOoevvElementSetsFromDatabase()
			throws Exception {

		String viewName = "OoevvElementSet";
		ViewBasedObjectGraph vbog = new ViewBasedObjectGraph(coreDao.getTop(), coreDao.getCl(),
				viewName);
		ViewDefinition vd = coreDao.getTop().getViews().get(viewName);
		ViewInstance qVi = new ViewInstance(vd);
		List<OoevvElementSet> l = new ArrayList<OoevvElementSet>();

		coreDao.getCe().connectToDB();
		
		List<ViewInstance> viewList = coreDao.getCe().executeFullQuery(qVi);
		coreDao.getCe().closeDbConnection();

		for (Iterator<ViewInstance> iterator = viewList.iterator(); iterator
				.hasNext();) {
			ViewInstance viewInstance = (ViewInstance) iterator.next();
			vbog.viewToObjectGraph(viewInstance);
			OoevvElementSet oa = (OoevvElementSet) vbog.readPrimaryObject();
			l.add(oa);
		}
		return l;

	}

	public List<LightViewInstance> listOoevvElementSets() throws Exception {

		String viewName = "OoevvElementSet";
		ViewDefinition vd = coreDao.getTop().getViews().get(viewName);
		ViewInstance qVi = new ViewInstance(vd);

		coreDao.getCe().connectToDB();
		List<LightViewInstance> viewList = coreDao.getCe().executeListQuery(qVi);
		coreDao.getCe().closeDbConnection();

		return viewList;

	}

	public OoevvElementSet loadWholeOoevvElementSet(Long uid)
			throws Exception {

		OoevvElementSet evs = new OoevvElementSet();
		Set<ViewInstance> exptVbs;

		ViewBasedObjectGraph vbog = getVbogs().get("OoevvElementSet");

		coreDao.getCe().connectToDB();
		ViewInstance vi = coreDao.getCe().executeUIDQuery("OoevvElementSet", uid);
		coreDao.getCe().closeDbConnection();
		
		Map<String, Object> objMap = vbog.viewToObjectGraph(vi);
		Iterator<String> keyIt = objMap.keySet().iterator();
		while (keyIt.hasNext()) {
			String key = keyIt.next();
			PrimitiveInstance pi = (PrimitiveInstance) vi.getSubGraph()
					.getNodes().get(key);
			Object o = objMap.get(key);
			vbog.primitiveToObject(pi, o, true);
		}

		OoevvElementSet oa = (OoevvElementSet) vbog.readPrimaryObject();

		return oa;

	}

}

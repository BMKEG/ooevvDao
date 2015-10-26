/** $Id: OoevvDaoImpl.java 2628 2011-07-21 01:01:24Z tom $
 * 
 */
package edu.isi.bmkeg.ooevv.dao;

import java.util.ArrayList;
import java.util.HashMap;
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
import edu.isi.bmkeg.ooevv.model.qo.ExperimentalVariable_qo;
import edu.isi.bmkeg.ooevv.model.qo.OoevvElementSet_qo;
import edu.isi.bmkeg.ooevv.model.qo.OoevvEntity_qo;
import edu.isi.bmkeg.ooevv.model.qo.OoevvProcess_qo;
import edu.isi.bmkeg.ooevv.model.qo.scale.MeasurementScale_qo;
import edu.isi.bmkeg.ooevv.model.qo.value.MeasurementValue_qo;
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
import edu.isi.bmkeg.terminology.model.Term;
import edu.isi.bmkeg.vpdmf.dao.CoreDao;
import edu.isi.bmkeg.vpdmf.dao.CoreDaoImpl;
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
	
	public ExtendedOoevvDaoImpl() throws Exception {}

	public ExtendedOoevvDaoImpl(CoreDao coreDao) throws Exception {
		this.coreDao = coreDao;
	}
	
	public void init(String login, String password, String uri, String wd) throws Exception {
		
		if( coreDao == null ) {
			this.coreDao = new CoreDaoImpl();
		}
		
		this.coreDao.init(login, password, uri, wd);
		
	}
	
	public CoreDao getCoreDao() {
		return coreDao;
	}

	public void setCoreDao(CoreDao coreDao) {
		this.coreDao = coreDao;
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

		Iterator<OoevvElement> procIt = exptVbSet.getOoevvEls().iterator();
		while (procIt.hasNext()) {
			OoevvElement el = (OoevvElement) procIt.next();
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

		Iterator<OoevvElement> entityIt = exptVbSet.getOoevvEls().iterator();
		while (entityIt.hasNext()) {
			OoevvElement el = (OoevvElement) entityIt.next();
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

		Iterator<OoevvElement> exptVbIt = exptVbSet.getOoevvEls().iterator();
		while (exptVbIt.hasNext()) {
			OoevvElement el = (OoevvElement) exptVbIt.next();
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

		Iterator<OoevvElement> exptVbIt = exptVbSet.getOoevvEls().iterator();
		while (exptVbIt.hasNext()) {
			OoevvElement el = (OoevvElement) exptVbIt.next();
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

		Iterator<OoevvElement> exptVbIt = exptVbSet.getOoevvEls().iterator();
		while (exptVbIt.hasNext()) {
			OoevvElement el = (OoevvElement) exptVbIt.next();
			if( !(el instanceof ExperimentalVariable) ) {
				continue;
			}
			
			ExperimentalVariable exptVb = (ExperimentalVariable) el;
			
			values.addAll(extractValuesFromVariable(exptVb).values());

		}

		return values;

	}

	public static Map<String, MeasurementValue> extractValuesFromVariable(
			ExperimentalVariable exptVb) {

		Map<String, MeasurementValue> values = new HashMap<String, MeasurementValue>();
		
		if (exptVb.getScale() == null) 
				return values;		

		MeasurementScale ms = exptVb.getScale();

		if (ms instanceof BinaryScaleWithNamedValues) {

			BinaryScaleWithNamedValues bswnv = (BinaryScaleWithNamedValues) ms;
			values.put(bswnv.getTrueValue().getShortTermId(), bswnv.getTrueValue());
			values.put(bswnv.getFalseValue().getShortTermId(), bswnv.getFalseValue());

		} else if (ms instanceof NominalScaleWithAllowedTerms) {

			NominalScaleWithAllowedTerms nswat = (NominalScaleWithAllowedTerms) ms;
			Iterator<NominalValue> tIt = nswat.getNVal()
					.iterator();
			while (tIt.hasNext()) {
				NominalValue nv = tIt.next();
				values.put(nv.getShortTermId(), nv);
			}

		} else if (ms instanceof OrdinalScaleWithNamedRanks) {

			OrdinalScaleWithNamedRanks pswnr = (OrdinalScaleWithNamedRanks) ms;
			Iterator<OrdinalValue> tIt = pswnr.getOVal()
					.iterator();
			while (tIt.hasNext()) {
				OrdinalValue ov = tIt.next();
				values.put(ov.getShortTermId(), ov);
			}

		} else if (ms instanceof HierarchicalScale) {

			HierarchicalScale hts = (HierarchicalScale) ms;
			Iterator<HierarchicalValue> tIt = hts
					.getHValues().iterator();
			while (tIt.hasNext()) {
				HierarchicalValue hv = tIt.next();
				values.put(hv.getShortTermId(), hv);
			}

		}

		return values;
		
	}

	public static Set<Term> listTerms(OoevvElementSet exptVbSet) {

		// explore the data and list all the objects in an accessible way.
		Set<Term> terms = new HashSet<Term>();

		// We've made each OoevvElementSet an Ontology rather than a term
		// TODO: NEED TO ADD EQUIVALENT LISTING OF ONTOLOGIES
		//terms.add(exptVbSet);

		Iterator<OoevvElement> exptVbIt = exptVbSet.getOoevvEls().iterator();
		while (exptVbIt.hasNext()) {
			//
			OoevvElement el = (OoevvElement) exptVbIt.next();
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
					Iterator<NominalValue> tIt = nswat.getNVal()
							.iterator();
					while (tIt.hasNext()) {
						NominalValue nv = tIt.next();
						terms.add(nv);
					}

				} else if (ms instanceof OrdinalScaleWithNamedRanks) {

					OrdinalScaleWithNamedRanks pswnr = (OrdinalScaleWithNamedRanks) ms;
					Iterator<OrdinalValue> tIt = pswnr.getOVal()
							.iterator();
					while (tIt.hasNext()) {
						OrdinalValue ov = tIt.next();
						terms.add(ov);
					}

				} else if (ms instanceof RelativeTermScale) {

					RelativeTermScale rts = (RelativeTermScale) ms;
					Iterator<Term> tIt = rts.getAllowReln().iterator();
					while (tIt.hasNext()) {
						terms.add(tIt.next());
					}

				} else if (ms instanceof HierarchicalScale) {

					HierarchicalScale hts = (HierarchicalScale) ms;
					Iterator<HierarchicalValue> tIt = hts
							.getHValues().iterator();
					while (tIt.hasNext()) {
						HierarchicalValue hv = tIt.next();
						terms.add(hv);
					}

				}

			}

		}

		Iterator<OoevvElement> procIt = exptVbSet.getOoevvEls().iterator();
		while (procIt.hasNext()) {
			OoevvElement el = (OoevvElement) procIt.next();
			if( !(el instanceof OoevvProcess) ) {
				continue;
			}
			OoevvProcess proc = (OoevvProcess) el;

			terms.add(proc);
			if (proc.getObiTerm() != null) {
				terms.add(proc.getObiTerm());
			}
		}

		Iterator<OoevvElement> entIt = exptVbSet.getOoevvEls().iterator();
		while (entIt.hasNext()) {
			OoevvElement el = (OoevvElement) entIt.next();
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
	public void insertOoevvElementSetInDatabase(OoevvElementSet oes)
			throws Exception {

		Set<ExperimentalVariable> exptVbs = ExtendedOoevvDaoImpl
				.listExptVbsInObjectGraph(oes);
		Set<OoevvProcess> procs = ExtendedOoevvDaoImpl
				.listProcessesInObjectGraph(oes);
		Set<OoevvEntity> ents = ExtendedOoevvDaoImpl
				.listEntitiesInObjectGraph(oes);
		Set<MeasurementScale> scales = ExtendedOoevvDaoImpl
				.listScalesInObjectGraph(oes);
		Set<MeasurementValue> values = ExtendedOoevvDaoImpl
				.listValuesInObjectGraph(oes);
		Set<Term> terms = ExtendedOoevvDaoImpl.listTerms(oes);

		try {

			coreDao.getCe().connectToDB();
			coreDao.getCe().turnOffAutoCommit();

			//
			// 1. insert the OoevvElementSet as a view
			//
			OoevvElementSet_qo oesQ = new OoevvElementSet_qo();
			oesQ.setShortTermId( oes.getShortTermId() );
			List<LightViewInstance> l = coreDao.listInTrans(oesQ, "OoevvElementSet");
			if( l.size() == 0 ) {
				coreDao.insertInTrans(oes, "OoevvElementSet");
			} else if (l.size() == 1) {
				oes.setVpdmfId( l.get(0).getVpdmfId() );
				coreDao.updateInTrans(oes, "OoevvElementSet");
			} else {
				throw new Exception("Ambiguity in adding " + oes.getShortTermId());
			}
			
			//
			// 2. insert all the different MeasurementValues as views
			//
			insertSetOfMeasurementValues(values);

			//
			// 3. insert all the different MeasurementScales as views
			//
			insertSetOfMeasurementScales(scales);

			//
			// 4. insert all the different Variables as views
			//
			insertSetOfExperimentalVariables(exptVbs);
			
			//
			// 4a. repeat the insertion of the Composite Scales 
			// to take into account the connections to the variables
			// 4b. Now fill in the accompanying variables
			//
			insertSetOfCompositeVariablesAndScales(exptVbs, scales);
			
			//
			// 5. insert all the different Processes as views
			//
			insertSetOfOoevvProcesses(procs);

			//
			// 6. insert all the different Entities as views
			//
			insertSetOfOoevvEntities(ents);
			
			coreDao.getCe().commitTransaction();

		} catch (Exception e) {

			coreDao.getCe().rollbackTransaction();
			e.printStackTrace();
			throw e;

		} finally {

			coreDao.getCe().closeDbConnection();

		}

	}

	/**
	 * @param ents
	 * @throws Exception
	 */
	public void insertSetOfOoevvEntities(Set<OoevvEntity> ents)
			throws Exception {
		Iterator<OoevvEntity> entIt = ents.iterator();
		while (entIt.hasNext()) {
			OoevvEntity v = entIt.next();

			OoevvEntity_qo vQ = new OoevvEntity_qo();
			vQ.setShortTermId( v.getShortTermId() );
			List<LightViewInstance> l2 = coreDao.listInTrans(vQ, "OoevvEntity");
			if( l2.size() == 0 ) {
				coreDao.insertInTrans(v, "OoevvEntity");
			} else if (l2.size() == 1) {
				v.setVpdmfId( l2.get(0).getVpdmfId() );
				coreDao.updateInTrans(v, "OoevvEntity");
			} else {
				throw new Exception("Ambiguity in adding " + v.getShortTermId());
			}									

		}
	}

	/**
	 * @param procs
	 * @throws Exception
	 */
	public void insertSetOfOoevvProcesses(Set<OoevvProcess> procs) throws Exception {
		Iterator<OoevvProcess> pIt = procs.iterator();
		while (pIt.hasNext()) {
			OoevvProcess v = pIt.next();

			OoevvProcess_qo vQ = new OoevvProcess_qo();
			vQ.setShortTermId( v.getShortTermId() );
			List<LightViewInstance> l2 = coreDao.listInTrans(vQ, "OoevvProcess");
			if( l2.size() == 0 ) {
				coreDao.insertInTrans(v, "OoevvProcess");
			} else if (l2.size() == 1) {
				v.setVpdmfId( l2.get(0).getVpdmfId() );
				coreDao.updateInTrans(v, "OoevvProcess");
			} else {
				throw new Exception("Ambiguity in adding " + v.getShortTermId());
			}

		}
	}

	/**
	 * @param exptVbs
	 * @param scales
	 * @throws Exception
	 */
	public void insertSetOfCompositeVariablesAndScales(
			Set<ExperimentalVariable> exptVbs,
			Set<MeasurementScale> scales)
			throws Exception {
		
		Iterator<MeasurementScale> sIt = scales.iterator();
		while (sIt.hasNext()) {
			MeasurementScale s = sIt.next();
			
			if( !(s instanceof CompositeScale) )
				continue;
			
			String sName = s.getClass().getName();
			sName = sName.substring(sName.lastIndexOf(".") + 1,
					sName.length());
			
			MeasurementScale_qo sQ = new MeasurementScale_qo();
			sQ.setShortTermId( s.getShortTermId() );
			List<LightViewInstance> l2 = coreDao.listInTrans(sQ, "MeasurementScale");
			if( l2.size() == 0 ) {
				coreDao.insertInTrans(s, sName);
			} else if (l2.size() == 1) {
				s.setVpdmfId( l2.get(0).getVpdmfId() );
				coreDao.updateInTrans(s, sName);
			} else {
				throw new Exception("Ambiguity in adding " + s.getShortTermId());
			}

		}

		Iterator<ExperimentalVariable> vbIt = exptVbs.iterator();
		while (vbIt.hasNext()) {
			ExperimentalVariable v = vbIt.next();
			
			if( !(v.getScale() instanceof CompositeScale) ) 
				continue;
			
			ExperimentalVariable_qo vQ = new ExperimentalVariable_qo();
			vQ.setShortTermId( v.getShortTermId() );
			List<LightViewInstance> l2 = coreDao.listInTrans(vQ, "ExperimentalVariable");
			if( l2.size() == 0 ) {
				coreDao.insertInTrans(v, "ExperimentalVariable");
			} else if (l2.size() == 1) {
				v.setVpdmfId( l2.get(0).getVpdmfId() );
				coreDao.updateInTrans(v, "ExperimentalVariable");
			} else {
				throw new Exception("Ambiguity in adding " + v.getShortTermId());
			}								
			
		}
	}

	/**
	 * @param exptVbs
	 * @throws Exception
	 */
	public void insertSetOfExperimentalVariables(
			Set<ExperimentalVariable> exptVbs) throws Exception {
		Iterator<ExperimentalVariable> vbIt = exptVbs.iterator();
		while (vbIt.hasNext()) {
			ExperimentalVariable v = vbIt.next();

			if( v.getScale() instanceof CompositeScale ) 
				continue;
			
			String vName = v.getClass().getName();
			vName = vName.substring(vName.lastIndexOf(".") + 1,
					vName.length());

			ExperimentalVariable_qo vQ = new ExperimentalVariable_qo();
			vQ.setShortTermId( v.getShortTermId() );
			List<LightViewInstance> l2 = coreDao.listInTrans(vQ, "ExperimentalVariable");
			if( l2.size() == 0 ) {
				coreDao.insertInTrans(v, vName);
			} else if (l2.size() == 1) {
				v.setVpdmfId( l2.get(0).getVpdmfId() );
				coreDao.updateInTrans(v, vName);
			} else {
				throw new Exception("Ambiguity in adding " + v.getShortTermId());
			}
		
		}
	}

	/**
	 * @param scales
	 * @throws Exception
	 */
	public void insertSetOfMeasurementScales(Set<MeasurementScale> scales)
			throws Exception {
		Iterator<MeasurementScale> sIt = scales.iterator();
		while (sIt.hasNext()) {
			MeasurementScale s = sIt.next();

			if( s instanceof CompositeScale )
				continue;

			String sName = s.getClass().getName();
			sName = sName.substring(sName.lastIndexOf(".") + 1,
					sName.length());

			MeasurementScale_qo sQ = new MeasurementScale_qo();
			sQ.setShortTermId( s.getShortTermId() );
			List<LightViewInstance> l2 = coreDao.listInTrans(sQ, "MeasurementScale");
			if( l2.size() == 0 ) {
				coreDao.insertInTrans(s, sName);
			} else if (l2.size() == 1) {
				s.setVpdmfId( l2.get(0).getVpdmfId() );
				coreDao.updateInTrans(s, sName);
			} else {
				throw new Exception("Ambiguity in adding " + s.getShortTermId());
			}
						
		}
	}

	/**
	 * @param values
	 * @throws Exception
	 */
	public void insertSetOfMeasurementValues(Set<MeasurementValue> values)
			throws Exception {
		Iterator<MeasurementValue> vIt = values.iterator();
		while (vIt.hasNext()) {
			MeasurementValue v = vIt.next();

			MeasurementValue_qo vQ = new MeasurementValue_qo();
			vQ.setShortTermId( v.getShortTermId() );
			List<LightViewInstance> l2 = coreDao.listInTrans(vQ, "MeasurementValue");
			if( l2.size() == 0 ) {
				coreDao.insertInTrans(v, "MeasurementValue");
			} else if (l2.size() == 1) {
				v.setVpdmfId( l2.get(0).getVpdmfId() );
				coreDao.updateInTrans(v, "MeasurementValue");
			} else {
				throw new Exception("Ambiguity in adding " + v.getShortTermId());
			}

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

		ViewBasedObjectGraph vbog = new ViewBasedObjectGraph(coreDao.getTop(),
				coreDao.getCl(), "OoevvElementSet");

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
	
	/**
	 * Removes All Ooevv data completely from the database, rolls back if there are any failures
	 * @throws Exception
	 */
	public void removeOoevv()
			throws Exception {

		coreDao.getCe().connectToDB();
		try {

			coreDao.getCe().connectToDB();
			coreDao.getCe().turnOffAutoCommit();

			//
			// REMOVE ALL OOEVV DATA FROM DATABASE
			//
			String[] tables = new String[] {
					
					//
					// linking tables
					//
					"NominalScaleWithAllowedTerms_nScales__nominalValues_NominalValue",
					"RelativeValue_sourceValue__valueMappings_RelativeValueMapping",
					"OoevvElement_elements__sets_OoevvElementSet",
					"OrdinalScaleWithNamedRanks_oScales__ordinalValues_OrdinalValue",
					"RelativeTermScale_relativeScales__allowedRelations_Term",
					"RelativeValue_sourceValue__valueMappings_RelativeValueMapping",
					"HierarchicalScale_hScales__hierarchicalValues_HierarchicalValue",
					"CompositeScale_partOf__hasParts_ExperimentalVariable",

					//
					// Variables 
					//
					"ExperimentalVariable",
					
					//
					// Scale tables 
					//
					"TimeStampScale", 
					"DecimalScale", 
					"IntegerScale",
					"NumericScale",
					"BinaryScaleWithNamedValues",
					"BinaryScale",
					"HierarchicalScale",
					"NominalScaleWithAllowedTerms",
					"NominalScale",
					"OrdinalScaleWithNamedRanks",
					"OrdinalScaleWithMaxRank",
					"OrdinalScale",
					"CompositeScale",
					"FileScale",
					"NaturalLanguageScale",
					"RelativeTermScale",
					"MeasurementScale",

					//
					// Value tables 
					//
					"HierarchicalValue",
					"DecimalValue", 
					"IntegerValue", 
					"NumericValue",
					"OrdinalValue",
					"NominalValue",
					"BinaryValue",
					"MeasurementValue",

					//
					// Elements 
					//
					"OoevvProcess",
					"OoevvEntity",
					"OoevvElement",
					"OoevvElementSet",
					
					
					//
					// Elements 
					//
					"Term"					
			};
			
			for( int i=0; i<tables.length; i++) {
				
				String sql = "DELETE FROM " + tables[i];
				int nRowsChanged = this.getCoreDao().getCe().executeRawUpdateQuery(sql);				
			
			}
		
			String sql = "DELETE FROM ViewTable WHERE viewType LIKE '.Term.%'";
			int nRowsChanged = this.getCoreDao().getCe().executeRawUpdateQuery(sql);				
			
			coreDao.getCe().commitTransaction();
			
		} catch (Exception e) {

			coreDao.getCe().rollbackTransaction();
			e.printStackTrace();
			throw e;

		} finally {

			coreDao.getCe().closeDbConnection();

		}
		
	}

	

}

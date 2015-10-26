package edu.isi.bmkeg.ooevv.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.isi.bmkeg.ooevv.model.ExperimentalVariable;
import edu.isi.bmkeg.ooevv.model.OoevvElementSet;
import edu.isi.bmkeg.ooevv.model.OoevvEntity;
import edu.isi.bmkeg.ooevv.model.OoevvProcess;
import edu.isi.bmkeg.ooevv.model.scale.MeasurementScale;
import edu.isi.bmkeg.ooevv.model.value.MeasurementValue;
import edu.isi.bmkeg.vpdmf.dao.CoreDao;
import edu.isi.bmkeg.vpdmf.model.instances.LightViewInstance;
import edu.isi.bmkeg.vpdmf.model.instances.ViewBasedObjectGraph;

public interface ExtendedOoevvDao {	
	
	public CoreDao getCoreDao();
	
	public void init(String login, String password, String uri, String wd) throws Exception;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public void insertOoevvElementSetInDatabase(OoevvElementSet exptVbSet)
			throws Exception;
	
	public void insertSetOfOoevvEntities(Set<OoevvEntity> ents) throws Exception;
	
	public void insertSetOfOoevvProcesses(Set<OoevvProcess> procs) throws Exception;
	
	public void insertSetOfCompositeVariablesAndScales(Set<ExperimentalVariable> exptVbs,
			Set<MeasurementScale> scales) throws Exception;
		
	public void insertSetOfExperimentalVariables(Set<ExperimentalVariable> exptVbs) throws Exception;
	
	public void insertSetOfMeasurementScales(Set<MeasurementScale> scales) throws Exception;
	
	public void insertSetOfMeasurementValues(Set<MeasurementValue> values) throws Exception;
	
	public List<OoevvElementSet> listOoevvElementSetsFromName(String name)
			throws Exception;

	public List<OoevvElementSet> loadAllOoevvElementSetsFromDatabase()
			throws Exception;

	public List<LightViewInstance> listOoevvElementSets() throws Exception;

	public OoevvElementSet loadWholeOoevvElementSet(Long uid) throws Exception;

}

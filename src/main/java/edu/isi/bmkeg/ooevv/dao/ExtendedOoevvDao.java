package edu.isi.bmkeg.ooevv.dao;

import java.util.List;
import java.util.Map;

import edu.isi.bmkeg.ooevv.model.OoevvElementSet;
import edu.isi.bmkeg.vpdmf.dao.CoreDao;
import edu.isi.bmkeg.vpdmf.model.instances.LightViewInstance;
import edu.isi.bmkeg.vpdmf.model.instances.ViewBasedObjectGraph;

public interface ExtendedOoevvDao {	
	
	public CoreDao getCoreDao();

	public Map<String, ViewBasedObjectGraph> getVbogs();	
	
	public void init(String login, String password, String uri) throws Exception;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public List<LightViewInstance> listOoevvElementSets() throws Exception;

	public OoevvElementSet loadWholeOoevvElementSet(Long uid) throws Exception;
	
}

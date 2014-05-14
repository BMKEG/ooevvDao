package edu.isi.bmkeg.ooevv.services.impl;

import java.io.File;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.io.Files;

import edu.isi.bmkeg.vpdmf.dao.*;
import edu.isi.bmkeg.ooevv.dao.ExtendedOoevvDao;
import edu.isi.bmkeg.ooevv.model.OoevvElementSet;
import edu.isi.bmkeg.ooevv.services.ExtendedOoevvService;
import edu.isi.bmkeg.ooevv.utils.OoevvExcelEngine;
import edu.isi.bmkeg.utils.Converters;

@RemotingDestination
@Transactional
@Service
public class ExtendedOoevvServiceImpl implements ExtendedOoevvService {

	private static final Logger logger = Logger.getLogger(ExtendedOoevvServiceImpl.class);

	@Autowired
	private ExtendedOoevvDao extOoevvDao;

	public void setExtOoevvDao(ExtendedOoevvDao ooevvDao) {
		this.extOoevvDao = ooevvDao;
	}
	
	public Long uploadExcelFile(byte[] excelFileData, boolean lookup) throws Exception {
		
		OoevvExcelEngine xlEngine = new OoevvExcelEngine();
			
		OoevvElementSet exptVbSet = xlEngine.createExpVariableSetFromExcel(excelFileData, lookup);

		extOoevvDao.insertOoevvElementSetInDatabase(exptVbSet);
		
		return exptVbSet.getVpdmfId();
		
	}
	
	public byte[] generateExcelFile(String name) throws Exception {
		
		File myTempDir = Files.createTempDir();
		File newXlFile = new File( myTempDir.getPath() + "/" + name );		

		OoevvExcelEngine xlEngine = new OoevvExcelEngine();
		xlEngine.generateBlankOoevvExcelWorkbook(newXlFile);
		
		byte[] data = Converters.fileContentsToBytesArray(newXlFile);		

		Converters.recursivelyDeleteFiles(myTempDir);
		
		return data;
		
	}
	
	public boolean deleteOoevvElementSet(Long vpdmfId) throws Exception {
	
		CoreDao coreDao = this.extOoevvDao.getCoreDao();
		
		// TODO:  Need to add the code here to drive the deletion of the OoevvElementSet
		
		return false;
		
		
	}
	

}
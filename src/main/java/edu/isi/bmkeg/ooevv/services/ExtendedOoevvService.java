package edu.isi.bmkeg.ooevv.services;


public interface ExtendedOoevvService {

	public Long uploadExcelFile(byte[] excelFileData, boolean lookup) throws Exception;
	
	public byte[] generateExcelFile(String name) throws Exception;
	
	public boolean deleteOoevvElementSet(Long vpdmfId) throws Exception;
	
}
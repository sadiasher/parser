package com.ef.dao;

public interface FileDao {

	public boolean isParsed(String fileName);
	
	public boolean save(String fileName) throws Exception;
}

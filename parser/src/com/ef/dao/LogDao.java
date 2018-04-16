package com.ef.dao;

import java.util.stream.Stream;

public interface LogDao {

	public boolean batchSave(Stream<String> streams) throws Exception ;
	
}

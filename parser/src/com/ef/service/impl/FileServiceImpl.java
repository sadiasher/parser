package com.ef.service.impl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import com.ef.dao.FileDao;
import com.ef.dao.LogDao;
import com.ef.dao.impl.FileDaoImpl;
import com.ef.dao.impl.LogDaoImpl;
import com.ef.service.FileService;

public class FileServiceImpl implements FileService {

	private FileDao fileDao = new FileDaoImpl();
	private LogDao logDao = new LogDaoImpl();
	
	public Boolean parseFile(String fileName)  throws Exception  {
		
		Boolean isSuccess = Boolean.FALSE;
		
		try {
			//Path path = Paths.get(getClass().getClassLoader().getResource(fileName).toURI());
			Path path = Paths.get(fileName);
			fileDao.save(fileName);
			
			try(Stream<String>  streams = Files.lines(path)){
				isSuccess = logDao.batchSave(streams);
			}
		} catch (Exception e) {
			 throw new Exception(e);
		} 
		
		return isSuccess;		
	}
	
	public boolean isParsed(String fileName) {
		return fileDao.isParsed(fileName);
	}
}

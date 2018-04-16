package com.ef.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import com.ef.dao.RequestDao;
import com.ef.dao.impl.RequestDaoImpl;
import com.ef.pojo.RequestCount;
import com.ef.service.RequestService;

public class RequestServiceImpl implements RequestService {

	private RequestDao requestDao = new RequestDaoImpl();
	
	public List<RequestCount> getRequestCount(LocalDateTime startDate, LocalDateTime endDate, int threshold){
		return requestDao.getRequestCount(startDate, endDate, threshold);
	}

	@Override
	public Boolean save(List<RequestCount> requestCounts, String comments, String threshold, String duration,
			LocalDateTime startDateTime)  throws Exception  {
		return requestDao.save(requestCounts, comments, threshold, duration, startDateTime);
		
	}
}

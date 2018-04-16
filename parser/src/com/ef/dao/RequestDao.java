package com.ef.dao;

import java.time.LocalDateTime;
import java.util.List;

import com.ef.pojo.RequestCount;

public interface RequestDao {

	public List<RequestCount> getRequestCount(LocalDateTime startDate, LocalDateTime endDate, int threshold);
	public Boolean save(List<RequestCount> requestCounts, String comments, String threshold, String duration,
			LocalDateTime startDateTime)   throws Exception ;
}

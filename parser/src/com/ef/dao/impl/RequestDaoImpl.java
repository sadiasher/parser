package com.ef.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ef.dao.RequestDao;
import com.ef.db.connection.ConnectionWrapper;
import com.ef.pojo.RequestCount;

public class RequestDaoImpl implements RequestDao {

	static Logger logger = Logger.getLogger(RequestDaoImpl.class.getName());
	
	@Override
	public List<RequestCount> getRequestCount(LocalDateTime startDate, LocalDateTime endDate, int threshold) {

		String query = "SELECT ip,count(*) AS requestCount FROM log " + 
				"WHERE date BETWEEN  ? AND ? " + 
				"GROUP BY ip " + 
				"HAVING requestCount >= " + threshold+ 
				" ORDER BY requestCount ";
		
		List<RequestCount> requestCounts = new ArrayList<>();
		
		try {
			Connection connection = ConnectionWrapper.getConnection();
			
			try(PreparedStatement stmt = connection.prepareStatement(query)){
				stmt.setString(1, startDate.toString());
				stmt.setString(2, endDate.toString());
//				stmt.setInt(3, threshold);
				
				ResultSet rs = stmt.executeQuery();
				
				while (rs.next()) {
					RequestCount rc = new RequestCount();
					rc.setIp(rs.getString("ip"));
					rc.setRequestCount(rs.getInt("requestCount"));
					requestCounts.add(rc);
				}
			}
			
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		
		return requestCounts;
	}

	@Override
	public Boolean save(List<RequestCount> requestCounts, String comments, String threshold, String duration,
			LocalDateTime startDateTime)   throws Exception {
		
		String insertQuery = "INSERT INTO db_log.request_detail (ip,start_date,duration,threshold,request_count,comments)\n" + 
				"VALUES (?,?,?,?,?,?)";
		
		Boolean isSuccess = Boolean.FALSE;
		Connection connection = null;
		
		try {
			connection = ConnectionWrapper.getConnection();
			
			try(PreparedStatement stmt = connection.prepareStatement(insertQuery);){

				requestCounts.forEach(requestCount -> {
					
					try {
						stmt.setString(1, requestCount.getIp());
			            stmt.setString(2, startDateTime.toString());
			            stmt.setString(3, duration);
			            stmt.setString(4, threshold);
			            stmt.setInt(5, requestCount.getRequestCount());
			            stmt.setString(6, comments);

			            stmt.addBatch();
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				});
				
				logger.log(Level.INFO, "Saving request details, it will take some time..." );
				int[] i = stmt.executeBatch();
				logger.log(Level.INFO, "Request details saved" );

				if(i.length > 0) {
					isSuccess = Boolean.TRUE;
				}
			}
			
		} catch (Exception e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				throw new Exception(e);
			}
			throw new Exception(e);
		}
		
		return isSuccess;
	}
}

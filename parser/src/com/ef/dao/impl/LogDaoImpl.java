package com.ef.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import com.ef.dao.LogDao;
import com.ef.db.connection.ConnectionWrapper;

public class LogDaoImpl implements LogDao {

	static Logger logger = Logger.getLogger(LogDaoImpl.class.getName());
	
	@Override
	public boolean batchSave(Stream<String> streams)  throws Exception {
		
		String insertQuery = "INSERT INTO log ( date, ip, request, status, user_agent)\n" + 
				"VALUES ( ?, ?, ?, ?, ?);";
		
		Boolean isSuccess = Boolean.FALSE;
		Connection connection = null;
		
		try {
			connection = ConnectionWrapper.getConnection();
			
			try(PreparedStatement stmt = connection.prepareStatement(insertQuery);){

				streams.forEach(stream -> {

					String[] arr = stream.split("\\|");
					
					try {
						stmt.setString(1, arr[0]);
			            stmt.setString(2, arr[1]); 
			            stmt.setString(3, arr[2]); 
			            stmt.setString(4, arr[3]); 
			            stmt.setString(5, arr[4]); 
			            stmt.addBatch();
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				});
				
				logger.log(Level.INFO, "Log file batch processing starts, it will take some time..." );
				int[] i = stmt.executeBatch();
				connection.commit();
				logger.log(Level.INFO, "Log file batch processing completed" );
				
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

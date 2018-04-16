package com.ef.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.ef.dao.FileDao;
import com.ef.db.connection.ConnectionWrapper;

public class FileDaoImpl implements FileDao {

	@Override
	public boolean isParsed(String fileName) {
		
		String query = "SELECT count(*) as count FROM file WHERE name = ?";
		Boolean isParsed = Boolean.FALSE;
		
		try {
			Connection connection = ConnectionWrapper.getConnection();
			
			try(PreparedStatement stmt = connection.prepareStatement(query)){
				stmt.setString(1, fileName);
				ResultSet rs = stmt.executeQuery();
				rs.next();
				int count = rs.getInt("count");
				
				if( count > 0 ) {
					isParsed = Boolean.TRUE;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return isParsed;
	}

	@Override
	public boolean save(String fileName) throws Exception {
		
		String insertQuery = "INSERT INTO file (name) VALUES (?)";
		Boolean isSuccess = Boolean.FALSE;
		
		try {
			Connection connection = ConnectionWrapper.getConnection();
			connection.setAutoCommit(Boolean.FALSE);
			
			try(PreparedStatement stmt = connection.prepareStatement(insertQuery)){
				stmt.setString(1, fileName);
				int a = stmt.executeUpdate();
				
				if( a > 0 ) {
					isSuccess = Boolean.TRUE;
				}
			} 			
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		
		return isSuccess;
	}

}

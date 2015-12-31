package com.icbms.batch;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.icbms.appserver.http.RequestManager;
import com.icbms.appserver.util.L;

public class uploadSurvey {
	private static Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(
						"jdbc:mysql://166.104.112.32:38221/ss_db",
						"iot",
						"grip1q2w3e");
		} catch (SQLException e) {
			L.e(ExceptionUtils.getStackTrace(e));
		} catch (ClassNotFoundException e) {
			L.e(ExceptionUtils.getStackTrace(e));
		} 
		return conn;
	}
	
	public static void main(String[] args) {
		System.out.println("test 1");
		RequestManager reqMgr = new RequestManager();
		RequestManager.mRequestManager = reqMgr;
		Connection con = getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			if(con != null) {
				stmt = con.createStatement();
				String query = "select * from survey where insert_time > curdate()";
				rs = stmt.executeQuery(query);
				
				JSONArray array = new JSONArray();
				
				while(rs.next()) {
					JSONObject obj = new JSONObject();
					obj.put("user_id",   rs.getString("user_id"));
					obj.put("zone",      rs.getString("zone"));
					obj.put("class_code",rs.getString("classcode"));
					obj.put("val",       rs.getString("val"));
					array.put(obj);
				}

				if(array.length() > 0) {
					reqMgr.sender.createContentInstance("/herit-in/herit-cse/Survey/status/Data", array.toString());
					L.d("upload is completed ");
				} else {
					L.d("No data need to send!");
				}
				
			} else {
				L.d("uploadSurvey connection is failed");
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(rs != null)
					rs.close();
				if(stmt != null)
					stmt.close();
				if(con != null)
					con.close();
			} catch(Exception e) {
				
			}
		}
		
	}
}

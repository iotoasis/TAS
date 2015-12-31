package com.icbms.batch;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.exception.ExceptionUtils;

import com.icbms.appserver.util.L;

public class summaryToday {

	public static int updateAttendance(Connection con, String user_id, String classcode, String status) {
		int row = -1;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Statement stmt = null;
		try {
			String query = "";
			query = "select count(*) as CT from attendance_summary where user_id='" + user_id +"' and classcode='" + classcode + "'";
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			int ct = 0;
			
			if(rs.next()) {
				ct = rs.getInt("CT");
			}
			if(ct == 0) {
				if(status.equalsIgnoreCase("A")) {
					query = "insert into attendance_summary(user_id, classcode, presence, absence, late) values (?, ?, 0, 1, 0) ";
				} else if(status.equalsIgnoreCase("P")) {
					query = "insert into attendance_summary(user_id, classcode, presence, absence, late) values (?, ?, 1, 0, 0) ";
				} else {
					query = "insert into attendance_summary(user_id, classcode, presence, absence, late) values (?, ?, 0, 0, 1) ";
				}
			} else {
				
			
				if(status.equalsIgnoreCase("A")) {   // absence
					query = "update attendance_summary set absence = absence + 1"
						    + " where user_id=? and classcode=?";
					
				} else if(status.equalsIgnoreCase("P")) { // presence
					query = "update attendance_summary set presence = presence + 1"
						    + " where user_id=? and classcode=?";
					
				} else {  // late
					query = "select late from attendance_summary where user_id='" + user_id +"' and classcode='" + classcode + "'";
					stmt = con.createStatement();
					rs = stmt.executeQuery(query);
					int late = 0;
					if(rs.next()) {
						late = rs.getInt("late");
					}
					if(late == 2) {
						query = "update attendance_summary set absence = absence + 1, late=0 "
							    + " where user_id=? and classcode=?";
	 
					} else {
						query = "update attendance_summary set late = late + 1 "
							    + " where user_id=? and classcode=?";
					}
				}
			}
			
			pstmt = con.prepareStatement(query);
			
			pstmt.setString(1, user_id );
			pstmt.setString(2, classcode);

			row = pstmt.executeUpdate();

		} catch(Exception e) {
			L.e(ExceptionUtils.getStackTrace(e));

		} finally {
			try {
				if(rs != null)
					rs.close();
				if(stmt != null)
					stmt.close();
				if(pstmt != null) {
					pstmt.close();
				} 
				
			} catch(Exception e) {}
			return row;
		}	
		
	}	
	
	
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

	public static String getDateDay() throws Exception {
	 
	    String day = "";
	    
	    Date d = new Date();
	    
	    Calendar cal = Calendar.getInstance() ;
	    cal.setTime(d);
	     
	    int dayNum = cal.get(Calendar.DAY_OF_WEEK) ;
	     
	    switch(dayNum){
	        case 1:
	            day = "Sun";
	            break ;
	        case 2:
	            day = "Mon";
	            break ;
	        case 3:
	            day = "Tue";
	            break ;
	        case 4:
	            day = "Wed";
	            break ;
	        case 5:
	            day = "Thu";
	            break ;
	        case 6:
	            day = "Fri";
	            break ;
	        case 7:
	            day = "Sat";
	            break ;
	             
	    }
	    return day ;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("run summary go~");
		Connection con = getConnection();
		Statement stmt = null;
		ResultSet rs = null;

		try {
			if(con != null) {
				stmt = con.createStatement();
				String query = "select * from attendance_status where today = curdate() ";
				
				rs = stmt.executeQuery(query);
				String user_id, classcode, status = "";
				
				while(rs.next()) {
					user_id = rs.getString("user_id");
					classcode = rs.getString("classcode");
					status    = rs.getString("status");
					updateAttendance(con, user_id, classcode, status);
				}
				L.d("summary End");
				
			} else {
				L.d("connection is failed");
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			L.e(ExceptionUtils.getStackTrace(e));
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

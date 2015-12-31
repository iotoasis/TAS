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
import org.json.JSONArray;
import org.json.JSONObject;

import com.icbms.appserver.http.RequestManager;
import com.icbms.appserver.util.L;

public class makeToday {
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
		System.out.println("run go~");
		Connection con = getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		
		try {
			if(con != null) {
				stmt = con.createStatement();
				String query = "select * from user_timetable where weekday = '" + getDateDay() + "' ";
				
				rs = stmt.executeQuery(query);
				String insert = "insert into attendance_status(user_id, today, classcode) values( ?, ?, ? ) ";
				
				pstmt = con.prepareStatement(insert);
		        Date d = new Date();
		        
		        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		        L.d("today=" + sdf.format(d) );
		        L.d("Data insertion is started!");
				while(rs.next()) {
					pstmt.setString(1, rs.getString("user_id") );
					pstmt.setString(2, sdf.format(d));
					pstmt.setString(3, rs.getString("classcode"));
					pstmt.executeUpdate();
				}
				L.d("Data insertion is completed!");
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
				if(pstmt != null)
					pstmt.close();
				
				if(con != null)
					con.close();
			} catch(Exception e) {
				
			}
		}
		
	}
	

}

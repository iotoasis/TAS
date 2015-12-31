package com.icbms.appserver.http;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.json.JSONObject;

import com.icbms.appserver.dao.model.onem2m.OneM2MTypes;
import com.icbms.appserver.util.L;
import com.icbms.appserver.util.Util;


public class RequestManager {
	
	
	public HttpRequestSender sender;
	

	public static RequestManager mRequestManager = null;
	
	public RequestManager() {

		
		sender = new HttpRequestSender();
	}
	


	private static Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(
						"jdbc:mysql://192.168.0.160/iot_db",
						"iot",
						"cot");
		} catch (SQLException e) {
			L.e(ExceptionUtils.getStackTrace(e));
		} catch (ClassNotFoundException e) {
			L.e(ExceptionUtils.getStackTrace(e));
		} 
		return conn;
	}
	
	void run() {
	    Properties properties = new Properties();
	    // Read properties file.
	    try {
	      properties.load(new FileInputStream("/device.properties"));
	      
	      while(true) {
	    	  String LR_Temp1 = properties.getProperty("LR_Temperature1");
	    	  String LR_Temp2 = properties.getProperty("LR_Temperature2");
	    	  String LR_Temp3 = properties.getProperty("LR_Temperature3");
	    	  String LR_Temp4 = properties.getProperty("LR_Temperature4");
	    	  String LR_Temp5 = properties.getProperty("LR_Temperature5");
	    	  L.d("LR_Temp1=" + LR_Temp1 + ": " + "LR_Temp2=" + LR_Temp2 + ": " + "LR_Temp3=" + LR_Temp3 
	    			  + ": " + "LR_Temp4=" + LR_Temp4 + ": " + "LR_Temp5=" + LR_Temp5 + ": ");
	    	  
	    	  String LR_Humi1 = properties.getProperty("LR_Humidity1");
	    	  String LR_Humi2 = properties.getProperty("LR_Humidity2");
	    	  String LR_Humi3 = properties.getProperty("LR_Humidity3");
	    	  String LR_Humi4 = properties.getProperty("LR_Humidity4");
	    	  String LR_Humi5 = properties.getProperty("LR_Humidity5");
	    	  L.d("LR_Humi1=" + LR_Humi1 + ": " + "LR_Humi2=" + LR_Humi2 + ": " + "LR_Humi3=" + LR_Humi3 
	    			  + ": " + "LR_Humi4=" + LR_Humi4 + ": " + "LR_Humi5=" + LR_Humi5 + ": ");
	    	  
	    	  
	    	  String LR_Lux1  = properties.getProperty("LR_Lux1");
	    	  String LR_Lux2  = properties.getProperty("LR_Lux2");
	    	  String LR_Lux3  = properties.getProperty("LR_Lux3");
	    	  String LR_Lux4  = properties.getProperty("LR_Lux4");
	    	  String LR_Lux5  = properties.getProperty("LR_Lux5");
	    	  L.d("LR_Lux1=" + LR_Lux1 + ": " + "LR_Lux2=" + LR_Lux2 + ": " + "LR_Lux3=" + LR_Lux3 
	    			  + ": " + "LR_Lux3=" + LR_Lux4 + ": " + "LR_Lux5=" + LR_Lux5 + ": ");

	    	  
	    	  String LB_Temp1 = properties.getProperty("LB_Temperature1");
	    	  String LB_Temp2 = properties.getProperty("LB_Temperature2");
	    	  String LB_Temp3 = properties.getProperty("LB_Temperature3");
	    	  L.d("LB_Temp1=" + LB_Temp1 + ": " + "LB_Temp2=" + LB_Temp2 + ": " + "LB_Temp3=" + LB_Temp3 + ": " );
	    	  
	    	  String LB_Energy = properties.getProperty("LB_EnergyMeter");
	    	  L.d("LB_Energy=" + LB_Energy + ": "  );
	    	  
	    	  String DT_Temp1 = properties.getProperty("DT_Temperature1");
	    	  String DT_Temp2 = properties.getProperty("DT_Temperature2");
	    	  L.d("DT_Temp1=" + DT_Temp1 + ": " + "DT_Temp2=" + DT_Temp2 + ": "  );
	    	  
	    	  String DT_Humi1 = properties.getProperty("DT_Humidity1");
	    	  String DT_Humi2 = properties.getProperty("DT_Humidity2");
	    	  L.d("DT_Humi1=" + DT_Humi1 + ": " + "DT_Humi1=" + DT_Humi1 + ": " );
	    	  
	    	  String DT_Lux1  = properties.getProperty("DT_Lux1");
	    	  String DT_Lux2  = properties.getProperty("DT_Lux2");	    	  
	    	  L.d("DT_Lux1=" + DT_Lux1 + ": " + "DT_Lux2=" + DT_Lux2 + ": "  );
	    	  
	      }
	      
	    } catch (IOException e) {
	    }

	    String avalue = properties.getProperty("a");
	    System.out.println(avalue);
	    properties.setProperty("a", "properties test");

	    // Write properties file.
	    try {
	      properties.store(new FileOutputStream("/device.properties"), null);
	    } catch(IOException e) {
	    }
				
	}
		
	
	public static void main(String[] args) {
		
		RequestManager reqMgr = new RequestManager();
		RequestManager.mRequestManager = reqMgr;
		
//		List<String> resources = reqMgr.sender.retrieveResourceAndChildren("/herit-in/herit-cse/Wally5/zone_ctl/Excute");
//		L.d("total number of resources: " + (resources==null?0:resources.size()));
		L.e("+++++++++++++++++++++++++++start of Test+++++++++++++++++++++++++++++++++++");

//		reqMgr.makeResourceFromDB();
		
//		Map<String, String> param = new HashMap<>();
//		param.put("rty", "2");
//		reqMgr.sender.discovery(null, param);
//		reqMgr.sender.retrieveResource("/herit-in/herit-cse/Wally5/status/Data", 5);
//		reqMgr.sender.retrieveResource("/herit-in/herit-cse/CONTAINER_3525", 5);
//		reqMgr.sender.retrieveResourceAndChildren("/herit-in/herit-cse/Siren_LB0001SR0001");
//		reqMgr.sender.retrieveResourceAndChildren("/herit-in/herit-cse/Siren_LB0001SR0001/action/Result");
		

//		String str = "eyJkYXRhIjoiU0lSRU4iLCJleGVjX2lkIjoiMzQifQ==";
//		L.d(Util.decodeBase64(str));
		
		
		
		
		
		
		
//		JSONObject obj = new JSONObject().put("exec_id", "34").put("data", "SIREN");
//		JSONObject obj = new JSONObject().put("exec_id", "34").put("data", "SILENT");
		JSONObject obj = new JSONObject().put("exec_id", "34").put("data", "0");
		String base64Str = Util.encodeBase64(obj.toString());
		
//		reqMgr.sender.createContentInstance("/herit-in/herit-cse/Siren_LB0001SR0001/action/Execute", base64Str, OneM2MTypes.ContentType.JSON, OneM2MTypes.EncodingType.BASE64_STRING);
//		reqMgr.sender.createContentInstance("/herit-in/herit-cse/Dehumidifier_DT0001DH0001/action/Execute", base64Str, OneM2MTypes.ContentType.JSON, OneM2MTypes.EncodingType.BASE64_STRING);
		reqMgr.sender.createContentInstance("/herit-in/herit-cse/LightingBulb_LR0001LB0001/action/Execute", base64Str, OneM2MTypes.ContentType.JSON, OneM2MTypes.EncodingType.BASE64_STRING);
		
//		reqMgr.sender.retrieveResourceAndChildren("/herit-in/herit-cse/Siren_LB0001SR0001/action/Execute");
//		reqMgr.sender.retrieveResourceAndChildren("/herit-in/herit-cse/LightingBulb_LR0001LB0001");
		
		
		
		
		
		
		
		
		
		
		
//		reqMgr.sender.createContentInstance("/herit-in/herit-cse/Siren_LB0001SR0001/action/Execute", "SILENT");

//		reqMgr.sender.createContentInstance("/herit-in/herit-cse/Aircon_LR0001AC0001/action/Execute", "ON");
//		reqMgr.sender.createContentInstance("/herit-in/herit-cse/Dehumidifier_LR0001DH0001/action/Execute", "ON");
//		reqMgr.sender.createContentInstance("/herit-in/herit-cse/Humidifier_LR0001HU0001/action/Execute", "ON");
		
//		reqMgr.sender.retrieveResourceAndChildren("/herit-in/herit-cse/Aircon_LR0001AC0001/action/Result");
//		reqMgr.sender.retrieveResourceAndChildren("/herit-in/herit-cse/Dehumidifier_LR0001DH0001/action/Result");
//		reqMgr.sender.retrieveResourceAndChildren("/herit-in/herit-cse/Humidifier_LR0001HU0001/action/Result");
		
//		reqMgr.sender.retrieveResource("/herit-in/herit-cse/Aircon_DT0001AC0001", 5 );
//		reqMgr.sender.retrieveResource("/herit-in/herit-cse/HumiditySensor_DT0001TS0002", 5 );
//		reqMgr.sender.retrieveResource("/herit-in/herit-cse/LightSensor_DT0001LS0001", 5 );
//		Group grp = new Group("TestGroup");
//		grp.setMemberType(OneM2MTypes.ResourceType.AE);
//		grp.setMemberIDs("SAE_239, SAE_243, SAE_209");
//		grp.setMaxNrOfMembers("5");
//		reqMgr.sender.sendRequest(grp);
		
		L.e("---------------------------end of Test-------------------------------------");
	}

}

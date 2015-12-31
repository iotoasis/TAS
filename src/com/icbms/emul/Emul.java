package com.icbms.emul;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.json.JSONObject;

import com.icbms.appserver.dao.model.onem2m.OneM2MTypes;
import com.icbms.appserver.http.RequestManager;
import com.icbms.appserver.util.L;
import com.icbms.appserver.util.Util;

public class Emul {
	
		
	public static void main(String[] args) {
		run();
	}
	
	public static void run() {
	    Properties properties = new Properties();
	    // Read properties file.
	    try {

			RequestManager reqMgr = new RequestManager();
			RequestManager.mRequestManager = reqMgr;
			
			L.e("+++++++++++++++++++++++++++start of Test+++++++++++++++++++++++++++++++++++");
			
//			JSONObject obj = new JSONObject().put("exec_id", "34").put("data", "SIREN");
//			JSONObject obj = new JSONObject().put("exec_id", "34").put("data", "SILENT");
			
			JSONObject obj =  null;
			String base64Str = null;
			
//			reqMgr.sender.createContentInstance("/herit-in/herit-cse/LightingBulb_LR0001LB0001/action/Execute", base64Str, OneM2MTypes.ContentType.JSON, OneM2MTypes.EncodingType.BASE64_STRING);
				      
	      while(true) {
   	    	  File path = new File(".");
		      System.out.println(path.getAbsolutePath() + File.separator + "bin" + File.separator +  "device.properties"); 
		        
		      properties.load(new FileInputStream(path.getAbsolutePath() + File.separator + "bin" + File.separator +  "device.properties"));
	          
		      String period = properties.getProperty("period");
	    	  String LR_Temp1 = properties.getProperty("LR_Temperature1");
	    	  String LR_Temp2 = properties.getProperty("LR_Temperature2");
	    	  String LR_Temp3 = properties.getProperty("LR_Temperature3");
	    	  String LR_Temp4 = properties.getProperty("LR_Temperature4");
	    	  String LR_Temp5 = properties.getProperty("LR_Temperature5");
	    	  String LR_TempVal = properties.getProperty("LR_TempVal");
	    	  L.d("LR_Temp1=" + LR_Temp1 + ": " + "LR_Temp2=" + LR_Temp2 + ": " + "LR_Temp3=" + LR_Temp3 
	    			  + ": " + "LR_Temp4=" + LR_Temp4 + ": " + "LR_Temp5=" + LR_Temp5 + ": LR_TempVal=" + LR_TempVal);
	    	  
	    	  if( LR_TempVal != null && !LR_TempVal.equals("") ) {
		    	  if( LR_Temp1 != null && !LR_Temp1.equals("") ) {
   					  reqMgr.sender.createContentInstance(LR_Temp1, LR_TempVal);					  
				  }
		    	  if( LR_Temp2 != null && !LR_Temp2.equals("") ) {
   					  reqMgr.sender.createContentInstance(LR_Temp2, LR_TempVal);					  
				  }
		    	  if( LR_Temp3 != null && !LR_Temp3.equals("") ) {
   					  reqMgr.sender.createContentInstance(LR_Temp3, LR_TempVal);					  
				  }
		    	  if( LR_Temp4 != null && !LR_Temp4.equals("") ) {
   					  reqMgr.sender.createContentInstance(LR_Temp4, LR_TempVal);					  
				  }
		    	  if( LR_Temp5 != null && !LR_Temp5.equals("") ) {
   					  reqMgr.sender.createContentInstance(LR_Temp5, LR_TempVal);					  
				  }
		    	  
	    	  }
	    	  String LR_Humi1 = properties.getProperty("LR_Humidity1");
	    	  String LR_Humi2 = properties.getProperty("LR_Humidity2");
	    	  String LR_Humi3 = properties.getProperty("LR_Humidity3");
	    	  String LR_Humi4 = properties.getProperty("LR_Humidity4");
	    	  String LR_Humi5 = properties.getProperty("LR_Humidity5");
	    	  String LR_HumiVal = properties.getProperty("LR_HumiVal");
	    	  
	    	  L.d("LR_Humi1=" + LR_Humi1 + ": " + "LR_Humi2=" + LR_Humi2 + ": " + "LR_Humi3=" + LR_Humi3 
	    			  + ": " + "LR_Humi4=" + LR_Humi4 + ": " + "LR_Humi5=" + LR_Humi5 + ": LR_HumiVal=" + LR_HumiVal);
	    	  
	    	  if( LR_HumiVal != null && !LR_HumiVal.equals("") ) {
		    	  if( LR_Humi1 != null && !LR_Humi1.equals("") ) {
   					  reqMgr.sender.createContentInstance(LR_Humi1, LR_HumiVal);					  
				  }
		    	  if( LR_Humi2 != null && !LR_Humi2.equals("") ) {
   					  reqMgr.sender.createContentInstance(LR_Humi2, LR_HumiVal);					  
				  }
		    	  if( LR_Humi3 != null && !LR_Humi3.equals("") ) {
   					  reqMgr.sender.createContentInstance(LR_Humi3, LR_HumiVal);					  
				  }
		    	  if( LR_Humi4 != null && !LR_Humi4.equals("") ) {
   					  reqMgr.sender.createContentInstance(LR_Humi4, LR_HumiVal);					  
				  }
		    	  if( LR_Humi5 != null && !LR_Humi5.equals("") ) {
   					  reqMgr.sender.createContentInstance(LR_Humi5, LR_HumiVal);					  
				  }
		    	  
	    	  }
	    	  
	    	  String LR_Lux1  = properties.getProperty("LR_Lux1");
	    	  String LR_Lux2  = properties.getProperty("LR_Lux2");
	    	  String LR_Lux3  = properties.getProperty("LR_Lux3");
	    	  String LR_Lux4  = properties.getProperty("LR_Lux4");
	    	  String LR_Lux5  = properties.getProperty("LR_Lux5");
	    	  String LR_LuxVal  = properties.getProperty("LR_LuxVal");
	    	  L.d("LR_Lux1=" + LR_Lux1 + ": " + "LR_Lux2=" + LR_Lux2 + ": " + "LR_Lux3=" + LR_Lux3 
	    			  + ": " + "LR_Lux3=" + LR_Lux4 + ": " + "LR_Lux5=" + LR_Lux5 + ": LR_LuxVal=" + LR_LuxVal);

	    	  if( LR_LuxVal != null && !LR_LuxVal.equals("") ) {
		    	  if( LR_Lux1 != null && !LR_Lux1.equals("") ) {
   					  reqMgr.sender.createContentInstance(LR_Lux1, LR_LuxVal);					  
				  }
		    	  if( LR_Lux2 != null && !LR_Lux2.equals("") ) {
   					  reqMgr.sender.createContentInstance(LR_Lux2, LR_LuxVal);					  
				  }
		    	  if( LR_Lux3 != null && !LR_Lux3.equals("") ) {
   					  reqMgr.sender.createContentInstance(LR_Lux3, LR_LuxVal);					  
				  }
		    	  if( LR_Lux4 != null && !LR_Lux4.equals("") ) {
   					  reqMgr.sender.createContentInstance(LR_Lux4, LR_LuxVal);					  
				  }
		    	  if( LR_Lux5 != null && !LR_Lux5.equals("") ) {
   					  reqMgr.sender.createContentInstance(LR_Lux5, LR_LuxVal);					  
				  }
		    	  
	    	  }
	    	  
	    	  String LB_Temp1 = properties.getProperty("LB_Temperature1");
	    	  String LB_Temp2 = properties.getProperty("LB_Temperature2");
	    	  String LB_Temp3 = properties.getProperty("LB_Temperature3");
	    	  String LB_TempVal = properties.getProperty("LB_TemperatureVal");
	    	  L.d("LB_Temp1=" + LB_Temp1 + ": " + "LB_Temp2=" + LB_Temp2 + ": " + "LB_Temp3=" + LB_Temp3 + ": LB_TempVal=" + LB_TempVal );

	    	  if( LB_TempVal != null && !LB_TempVal.equals("") ) {
		    	  if( LB_Temp1 != null && !LB_Temp1.equals("") ) {
   					  reqMgr.sender.createContentInstance(LB_Temp1, LB_TempVal);					  
				  }
		    	  if( LB_Temp2 != null && !LB_Temp2.equals("") ) {
   					  reqMgr.sender.createContentInstance(LB_Temp2, LB_TempVal);					  
				  }
		    	  if( LB_Temp3 != null && !LB_Temp3.equals("") ) {
   					  reqMgr.sender.createContentInstance(LB_Temp3, LB_TempVal);					  
				  }
	    	  }
	    	  
	    	  String LB_Energy = properties.getProperty("LB_EnergyMeter");
	    	  String LB_EnergyVal = properties.getProperty("LB_EnergyVal");
	    	  
	    	  L.d("LB_Energy=" + LB_Energy + ": LB_EnergyVal=" + LB_EnergyVal  );
	    	  if( LB_EnergyVal != null && !LB_EnergyVal.equals("") ) {
		    	  if( LB_Energy != null && !LB_Energy.equals("") ) {
   					  reqMgr.sender.createContentInstance(LB_Energy, LB_EnergyVal);					  
				  }
	    	  }
	    	  
	    	  String DT_Temp1 = properties.getProperty("DT_Temperature1");
	    	  String DT_Temp2 = properties.getProperty("DT_Temperature2");
	    	  String DT_TempVal = properties.getProperty("DT_TempVal");
	    	  
	    	  L.d("DT_Temp1=" + DT_Temp1 + ": " + "DT_Temp2=" + DT_Temp2 + ": DT_TempVal=" + DT_TempVal  );
	    	  if( LB_TempVal != null && !LB_TempVal.equals("") ) {
		    	  if( DT_Temp1 != null && !DT_Temp1.equals("") ) {
   					  reqMgr.sender.createContentInstance(DT_Temp1, DT_TempVal);					  
				  }
		    	  if( DT_Temp2 != null && !DT_Temp2.equals("") ) {
   					  reqMgr.sender.createContentInstance(DT_Temp2, DT_TempVal);					  
				  }

	    	  }
	    	  
	    	  String DT_Humi1 = properties.getProperty("DT_Humidity1");
	    	  String DT_Humi2 = properties.getProperty("DT_Humidity2");
	    	  String DT_HumiVal = properties.getProperty("DT_HumiVal");
	    	  L.d("DT_Humi1=" + DT_Humi1 + ": " + "DT_Humi2=" + DT_Humi2 + ": DT_HumiVal=" + DT_HumiVal );
	    	  
	    	  if( DT_HumiVal != null && !DT_HumiVal.equals("") ) {
		    	  if( DT_Humi1 != null && !DT_Humi1.equals("") ) {
   					  reqMgr.sender.createContentInstance(DT_Temp1, DT_HumiVal);					  
				  }
		    	  if( DT_Humi2 != null && !DT_Humi2.equals("") ) {
   					  reqMgr.sender.createContentInstance(DT_Temp2, DT_HumiVal);					  
				  }

	    	  }
	    	  
	    	  String DT_Lux1  = properties.getProperty("DT_Lux1");
	    	  String DT_Lux2  = properties.getProperty("DT_Lux2");
	    	  String DT_LuxVal  = properties.getProperty("DT_LuxVal");	
	    	  
	    	  L.d("DT_Lux1=" + DT_Lux1 + ": " + "DT_Lux2=" + DT_Lux2 + ": DT_LuxVal=" + DT_LuxVal  );
	    	  
	    	  if( DT_LuxVal != null && !DT_LuxVal.equals("") ) {
		    	  if( DT_Lux1 != null && !DT_Lux1.equals("") ) {
   					  reqMgr.sender.createContentInstance(DT_Lux1, DT_LuxVal);					  
				  }
		    	  if( DT_Lux2 != null && !DT_Lux2.equals("") ) {
   					  reqMgr.sender.createContentInstance(DT_Lux2, DT_LuxVal);					  
				  }

	    	  }
	    	  
	    	try {
				Thread.sleep(Integer.parseInt(period)*1000 );
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	      }
	      
	    } catch (IOException e) {
	    	System.out.println("error:" + e.getMessage());
	    }

//	    String avalue = properties.getProperty("a");
//	    System.out.println(avalue);
//	    properties.setProperty("a", "properties test");
//
//	    // Write properties file.
//	    try {
//	      properties.store(new FileOutputStream("/device.properties"), null);
//	    } catch(IOException e) {
//	    }
				
	}
	
}

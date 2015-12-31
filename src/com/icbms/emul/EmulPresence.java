package com.icbms.emul;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.json.JSONObject;

import com.icbms.appserver.http.RequestManager;
import com.icbms.appserver.util.L;

public class EmulPresence {
	public static void main(String[] args) {
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
		      System.out.println(path.getAbsolutePath() + File.separator + "bin" + File.separator +  "ss.properties"); 
		        
		      properties.load(new FileInputStream(path.getAbsolutePath() + File.separator + "bin" + File.separator +  "ss.properties"));
	          
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
	}
}

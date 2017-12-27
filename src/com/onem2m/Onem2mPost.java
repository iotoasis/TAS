package com.onem2m;

import java.util.Calendar;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.common.Base64Convertor;
import com.http.HttpURLConnecterThread;
import com.service.utile.MethodTypeSelect;
import com.service.utile.Onem2mThingsTypeSelect;

public class Onem2mPost extends Thread{
	public  static  Logger tasLog = Logger.getRootLogger();
	HashMap aeResultHashMap = new HashMap<>();
	HashMap containerResultHashMap = new HashMap<>();
	HashMap instanceResultHashMap = new HashMap<>();
	HashMap pollingChannelresultHashMap = new HashMap<>();
	HashMap subscriptionResultHashMap = new HashMap<>();
	HashMap semanticcommandResultHashMap = new HashMap<>();
	String url = null;
	String ae = null;
	HashMap<String, String> headerMashMap = null;
	HashMap<String, String> bodyHashMap = null;
	String bodyString = null;
	
	
	public void setData(String url, String ae, HashMap<String, String> headerMashMap, HashMap<String, String> bodyHashMap, String bodyString){
		this.url = url;
		this.ae = ae;
		this.headerMashMap = headerMashMap;
		this.bodyHashMap = bodyHashMap;
		this.bodyString = bodyString;
		
	}
	
	
	
	public HashMap getData() throws InterruptedException{

		if(headerMashMap != null){
			
			HttpURLConnecterThread connecterThread = null;
			
			HashMap hashMap = new HashMap<>();
			HashMap<String, String> paramHashmap = new HashMap<>();
			
			String paramString = "";
			String lbl_apn_api_rn_String = "";
			String poaString = "";
			String rrString = "";
			String etString = "";
			String mniString = "";
			String mbsString = "";
			String miaString = "";
			String cnfString = "";
			String cnfString2 = "";
			String conString = "";
			String netString = "";
			String nuString = "";
			String dcrpString = "";
			String dspString = "";
			
			switch (ae) {
			
			case Onem2mThingsTypeSelect.AE:
				
				if(headerMashMap.get("X-M2M-Origin") != null){
					paramHashmap.put("X-M2M-Origin", headerMashMap.get("X-M2M-Origin").toString());
				}else{
					paramHashmap.put("X-M2M-Origin", headerMashMap.get("ae").toString() + "_origin");
				}
				
				if(headerMashMap.get("X-M2M-RI") != null){
					paramHashmap.put("X-M2M-RI", headerMashMap.get("X-M2M-Origin").toString());
				}else{
					paramHashmap.put("X-M2M-RI", headerMashMap.get("ae").toString() + "_ri");
				}
				
				paramHashmap.put("Content-Type", "application/vnd.onem2m-res+json; ty=2");
				
				paramString = null;
				
				lbl_apn_api_rn_String = null;
				poaString = "166.104.112.34:8080";
				rrString = "FALSE";
				etString = "20991231T235959";
				
				if(headerMashMap != null && headerMashMap.get("ae") != null){
					lbl_apn_api_rn_String = headerMashMap.get("ae");
				}
				
				paramString = "{\"m2m:ae\":{";
				paramString += "\"lbl\": [\"" + lbl_apn_api_rn_String + "_Label" + "\"],";
				paramString += "\"apn\": \"" + lbl_apn_api_rn_String + "_AppName" + "\",";
				paramString += "\"api\": \"" + lbl_apn_api_rn_String + "_AppID" + "\",";
				paramString += "\"poa\": \"" + poaString + "" + "\",";
				paramString += "\"rr\": \"" + rrString + "" + "\",";
				paramString += "\"rn\": \"" + lbl_apn_api_rn_String + "" + "\",";
				paramString += "\"et\": \"" + etString + "" + "\"";
				paramString += "}}";
				
				connecterThread = new HttpURLConnecterThread(url, MethodTypeSelect.POST, paramHashmap, paramString, null);
				connecterThread.start();
				connecterThread.join();
				tasLog.debug("[ TAS INFO ] ::" +  "AE post complete");

				return connecterThread.getData();
				
				
			case Onem2mThingsTypeSelect.CONTAINER:
				
				
				if(headerMashMap.get("X-M2M-Origin") != null){
					paramHashmap.put("X-M2M-Origin", headerMashMap.get("X-M2M-Origin").toString());
				}else{
					paramHashmap.put("X-M2M-Origin", headerMashMap.get("ae").toString() + "_origin");
				}
				
				if(headerMashMap.get("X-M2M-RI") != null){
					paramHashmap.put("X-M2M-RI", headerMashMap.get("X-M2M-Origin").toString());
				}else{
					paramHashmap.put("X-M2M-RI", headerMashMap.get("ae").toString() + "_ri");
				}
				
				paramHashmap.put("Content-Type", "application/vnd.onem2m-res+json; ty=3");
				
				
				
				lbl_apn_api_rn_String = null;
				mniString = "1000";
				mbsString = "1024000";
				miaString = "36000";
				etString = "20991231T235959";
				if(headerMashMap != null && headerMashMap.get("Container_rn") != null){
					lbl_apn_api_rn_String = headerMashMap.get("Container_rn");
				}
				paramString = "";
				paramString += "{";
				paramString += 		"\"m2m:cnt\":";
				paramString += 		"{";
				paramString += 			"\"lbl\": [\"" + lbl_apn_api_rn_String + "_Label" + "\"],";
				paramString += 			"\"mni\": \"" + mniString +"\",";
				paramString += 			"\"mbs\": \""+ mbsString +"\",";
				paramString += 			"\"mia\": \"" + miaString + "\",";
				paramString += 			"\"rn\": \"" + lbl_apn_api_rn_String + "\",";
				paramString += 			"\"et\": \"" + etString + "\"";
				paramString += 		"}";
				paramString += "}";
				
				connecterThread = new HttpURLConnecterThread(url, MethodTypeSelect.POST, paramHashmap, paramString, null);
				connecterThread.start();
				connecterThread.join();
				tasLog.debug("[ TAS INFO ] ::" +  "CONTAINER post complete");
				
				return connecterThread.getData();
				
			case Onem2mThingsTypeSelect.INSTANCE:
				
				if(headerMashMap.get("X-M2M-Origin") != null){
					paramHashmap.put("X-M2M-Origin", headerMashMap.get("X-M2M-Origin").toString());
				}else{
					paramHashmap.put("X-M2M-Origin", headerMashMap.get("ae").toString() + "_origin");
				}
				
				if(headerMashMap.get("X-M2M-RI") != null){
					paramHashmap.put("X-M2M-RI", headerMashMap.get("X-M2M-Origin").toString());
				}else{
					paramHashmap.put("X-M2M-RI", headerMashMap.get("ae").toString() + "_ri");
				}
				
				paramHashmap.put("Content-Type", "application/vnd.onem2m-res+json; ty=4");
				
				
				
				lbl_apn_api_rn_String = "";
				Calendar cCal = Calendar.getInstance();

				int year = Calendar.getInstance().get(Calendar.YEAR);
				
				int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
				
				int day = Calendar.getInstance().get(Calendar.DATE) + 1;
				
				int hour = Calendar.getInstance().get(Calendar.HOUR);
				
				int minute = Calendar.getInstance().get(Calendar.MINUTE);
				
				int second = Calendar.getInstance().get(Calendar.SECOND);
				
				String startDateString = Integer.toString(year) + Integer.toString(month) + Integer.toString(day) + "T" + Integer.toString(hour) + Integer.toString(minute) + Integer.toString(second);
				
//				etString = "20991231T235959";
				cnfString = "text/plain:0";
				cnfString2 = "application/json:1";
				conString = "";
				if(headerMashMap != null && headerMashMap.get("Instance_rn") != null){
					lbl_apn_api_rn_String = headerMashMap.get("Instance_rn");
				}
				
				if(headerMashMap != null && headerMashMap.get("conString") != null){
					conString = headerMashMap.get("conString");
				}
				
				if(headerMashMap != null && headerMashMap.get("measurement") != null){
					conString = headerMashMap.get("measurement");
					
					conString = conString.replace("\"", "\\\"");
				}
				
				paramString = "";
				paramString += "{";
				paramString += 		"\"m2m:cin\":";
				paramString += 		"{";
				paramString += 			"\"lbl\": [\"" + lbl_apn_api_rn_String + "_Label" + "\"],";
//				paramString += 			"\"et\": \"" + etString +"\","; 2017-12-26 인스턴스 사용제한을 1일 후로 변경
				paramString += 			"\"et\": \"" + startDateString +"\",";
				
				
//				{ 
//					"exec_id": "xxxxxxxxxxxxxxxxxxxxxx0021203", 
//					"data": "[디바이스 제어항목별로 정의된 데이터]",
//				   }

				if(lbl_apn_api_rn_String.equals("execute")){
					paramString += 			"\"cnf\": \"" + cnfString2 +"\",";
					String newConString;
					newConString = "{"
							+ "\"exec_id\":" + "\"" + "TAS" + "\""+ ","
							+ "\"data\":" + "\""+ conString +"\""
							+ "}";
					
					Base64Convertor base64Convertor = new Base64Convertor();
					
					conString = base64Convertor.encrypt(newConString);
					
				}else if(lbl_apn_api_rn_String.equals("result")){	
					paramString += 			"\"cnf\": \"" + cnfString2 +"\",";
					String newConString;
					newConString = "{"
							+ "\"exec_id\":" + "\"" + headerMashMap.get("exec_id") + "\""+ ","
							+ "\"exec_result\":" + "\"" + headerMashMap.get("exec_result") + "\""+ ""
							+ "}";
					
					Base64Convertor base64Convertor = new Base64Convertor();
					
					conString = base64Convertor.encrypt(newConString);
					
					
				}else{
					
					paramString += 			"\"cnf\": \"" + cnfString +"\",";
					
				}
				
				paramString += 			"\"con\": \"" + conString +"\"";
				paramString += 		"}";
				paramString += "}";
				
				connecterThread = new HttpURLConnecterThread(url, MethodTypeSelect.POST, paramHashmap, paramString, null);
				connecterThread.start();
				connecterThread.join();
				tasLog.debug("[ TAS INFO ] ::" +  "INSTANCE post complete");
				
				return connecterThread.getData();
				
				
			case Onem2mThingsTypeSelect.POLLINGCHANNEL:
				
				if(headerMashMap.get("X-M2M-Origin") != null){
					paramHashmap.put("X-M2M-Origin", headerMashMap.get("X-M2M-Origin").toString());
				}else{
					paramHashmap.put("X-M2M-Origin", headerMashMap.get("ae").toString() + "_origin");
				}
				
				if(headerMashMap.get("X-M2M-RI") != null){
					paramHashmap.put("X-M2M-RI", headerMashMap.get("X-M2M-Origin").toString());
				}else{
					paramHashmap.put("X-M2M-RI", headerMashMap.get("ae").toString() + "_ri");
				}
				
				paramHashmap.put("Content-Type", "application/vnd.onem2m-res+json; ty=15");
				
				
				
				lbl_apn_api_rn_String = "";
				etString = "20991231T235959";
				if(headerMashMap != null && headerMashMap.get("ae") != null){
					lbl_apn_api_rn_String = headerMashMap.get("ae");
				}
				paramString = "";
				paramString += "{";
				paramString += 		"\"m2m:pch\":";
				paramString += 		"{";
				paramString += 			"\"lbl\": [\"" + lbl_apn_api_rn_String + "_Label" + "\"],";
				paramString += 			"\"rn\": \"" + "pollingchannel" +"\",";
				paramString += 			"\"et\": \"" + etString + "\"";
				paramString += 		"}";
				paramString += "}";
				
				connecterThread = new HttpURLConnecterThread(url, MethodTypeSelect.POST, paramHashmap, paramString, null);
				connecterThread.start();
				connecterThread.join();
				tasLog.debug("[ TAS INFO ] ::" +  "POLLINGCHANNEL post complete");
				
				return connecterThread.getData();
				
				
				
			case Onem2mThingsTypeSelect.SUBSCRIPTION:
				
				if(headerMashMap.get("X-M2M-Origin") != null){
					paramHashmap.put("X-M2M-Origin", headerMashMap.get("X-M2M-Origin").toString());
				}else{
					paramHashmap.put("X-M2M-Origin", headerMashMap.get("ae").toString() + "_origin");
				}
				
				if(headerMashMap.get("X-M2M-RI") != null){
					paramHashmap.put("X-M2M-RI", headerMashMap.get("X-M2M-Origin").toString());
				}else{
					paramHashmap.put("X-M2M-RI", headerMashMap.get("ae").toString() + "_ri");
				}
				paramHashmap.put("Content-Type", "application/vnd.onem2m-res+json; ty=23");
				
				
				
				lbl_apn_api_rn_String = "";
				etString = "20991231T235959";
				netString = "1,2,3,4";
				nuString = headerMashMap.get("subscription_url");
				if(headerMashMap != null && headerMashMap.get("ae") != null){
					lbl_apn_api_rn_String = headerMashMap.get("ae");
				}
				String[] nuStringList;
				
				nuStringList = nuString.split(",");
				
				paramString = "";
				paramString += "{";
				paramString += 		"\"m2m:sub\":";
				paramString += 		"{";
				paramString += 			"\"lbl\": [\"" + lbl_apn_api_rn_String + "_Label" + "\"],";
				paramString += 			"\"rn\": \"" + "tassubscription" +"\",";
				paramString += 			"\"et\": \"" + etString +"\",";
				paramString += 			"\"enc\":";
				paramString += 			"{";
				paramString += 				"\"net\": [ 1,2,3,4 ]";
				paramString += 			"},";
				paramString += 			"\"nu\": [ " ;
				
				for(int i = 0 ; i < nuStringList.length; i ++){
					paramString += "\"" + nuStringList[i].trim() + "\"" ;
					
					if(i + 1 != nuStringList.length){
						paramString += ",";
					}
				}
				
				paramString +=			" ]";
				
				
				paramString += 		"}";
				paramString += "}";
				
				
				
				connecterThread = new HttpURLConnecterThread(url, MethodTypeSelect.POST, paramHashmap, paramString, null);
				connecterThread.start();
				connecterThread.join();
				tasLog.debug("[ TAS INFO ] ::" +  "SUBSCRIPTION post complete");
				
				return connecterThread.getData();
				
			case Onem2mThingsTypeSelect.SEMANTICDESCRIPTOR:
				
				if(headerMashMap.get("X-M2M-Origin") != null){
					paramHashmap.put("X-M2M-Origin", headerMashMap.get("X-M2M-Origin").toString());
				}else{
					paramHashmap.put("X-M2M-Origin", headerMashMap.get("ae").toString() + "_origin");
				}
				
				if(headerMashMap.get("X-M2M-RI") != null){
					paramHashmap.put("X-M2M-RI", headerMashMap.get("X-M2M-Origin").toString());
				}else{
					paramHashmap.put("X-M2M-RI", headerMashMap.get("ae").toString() + "_ri");
				}
				
				paramHashmap.put("Content-Type", "application/vnd.onem2m-res+json; ty=24");
				
				paramString = null;
				
				lbl_apn_api_rn_String = null;
				rrString = "semanticDescriptor";
				etString = "20991231T235959";
				dcrpString = "application/rdf+xml:1";
				dspString = "";
				
				if(headerMashMap != null && headerMashMap.get("ae") != null){
					lbl_apn_api_rn_String = headerMashMap.get("ae");
				}
				
				if(headerMashMap != null && headerMashMap.get("smd") != null){
					
					Base64Convertor base64Convertor = new Base64Convertor();
					
					dspString = base64Convertor.encrypt(headerMashMap.get("smd"));
				}
				
				paramString = "{\"m2m:smd\":{";
				paramString += "\"lbl\": [\"" + lbl_apn_api_rn_String + "_Label" + "\"],";
				paramString += "\"rn\": \"" + "semanticDescriptor" + "" + "\",";
				paramString += "\"et\": \"" + etString + "" + "\",";
				paramString += "\"dcrp\": \"" + dcrpString + "" + "\",";
				paramString += "\"dsp\": \"" + dspString + "" + "\"";
				paramString += "}}";
				
				connecterThread = new HttpURLConnecterThread(url, MethodTypeSelect.POST, paramHashmap, paramString, null);
				connecterThread.start();
				connecterThread.join();
				tasLog.debug("[ TAS INFO ] ::" +  "SEMANTICDESCRIPTOR post complete");
				
				return connecterThread.getData();
				
				
			default:
				
				if(headerMashMap.get("X-M2M-Origin") != null){
					paramHashmap.put("X-M2M-Origin", headerMashMap.get("X-M2M-Origin").toString());
				}else{
					paramHashmap.put("X-M2M-Origin", "Tas_origin");
				}
				
				if(headerMashMap.get("X-M2M-RI") != null){
					paramHashmap.put("X-M2M-RI", headerMashMap.get("X-M2M-Origin").toString());
				}else{
					paramHashmap.put("X-M2M-RI", "Tas_ri");
				}
				
				paramHashmap.put("Content-Type", "application/vnd.onem2m-res+json;");
				
				
				
				connecterThread = new HttpURLConnecterThread(url, MethodTypeSelect.POST, paramHashmap, "", null);
				connecterThread.start();
				connecterThread.join();
				return connecterThread.getData();
				
			}
			
		}
		return null;
		
	}
	
}


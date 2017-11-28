package com.connecter;

import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.common.Base64Convertor;
import com.common.SQLManager;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.onem2m.Onem2mCheker;
import com.onem2m.Onem2mDeleter;
import com.onem2m.Onem2mGet;
import com.onem2m.Onem2mPost;
import com.service.utile.Onem2mThingsTypeSelect;

public class SmartThingsConnecter extends Thread{
	
	String smartthingSelect = "tas-sql.smartthingSelect";
	String smartthingInsert = "tas-sql.smartthingInsert";
	String smartthingUpdate = "tas-sql.smartthingUpdate";
	String smartthingDelete = "tas-sql.smartthingDelete";
	
	SqlMapClient sqlMapper = null;
	
	public  static  Logger tasLog = Logger.getRootLogger();
	
	private Base64Convertor base64Convertor = new Base64Convertor();
	
	Pattern urlPattern = Pattern.compile("^(https?):\\/\\/([^:\\/\\s]+)(:([^\\/]*))?((\\/[^\\s/\\/]+)*)?\\/([^#\\s\\?]*)(\\?([^#\\s]*))?(#(\\w*))?$");
	

	HashMap headerParamHashmap = null;
	
	String orderString = "";
	
	JSONObject bodyJsonObject = null;
	
	StringBuilder buf = null;
	
	String jsonStr = "";
	Onem2mCheker cheker = null;
	Onem2mGet onem2mGet = null;
	Onem2mPost onem2mPost = null;
	Onem2mDeleter onem2mDeleter = null;
	JSONObject jsonResultObject = null;
	HashMap hashMap2 = new HashMap<>();
	String rttimeString = "";
	String valueString = "";
	String resultString = "Success";
	JSONParser jsonParser = new JSONParser();
	
	public void setData(HashMap headerParamHashmap, String orderString, JSONObject bodyJsonObject, StringBuilder buf, String jsonStr){
		
		this.headerParamHashmap = headerParamHashmap;
		this.orderString = orderString;
		this.bodyJsonObject = bodyJsonObject;
		this.buf = buf;
		this.jsonStr = jsonStr;
		
	}

	
	public StringBuilder getData(){
		
		if(sqlMapper == null){

			sqlMapper = SQLManager.sqlMapper;

		}


		if(sqlMapper == null){

			tasLog.debug("[ TAS ERROR ] ::" +  "Database is unopen");

		}else{

			tasLog.debug("[ TAS INFO ] ::" +  "Database OK!!");

		}
		
		tasLog.debug("[ TAS INFO ] ::" +  "This request device type is Smart Things !!");

		
		ArrayList resultList = new ArrayList<>();

		HashMap<String, String> param = new HashMap<>();
		
		if(headerParamHashmap.get("deviceId") != null){
			
			param.put("device_id", (String) headerParamHashmap.get("deviceId"));
			
		}else{
			param.put("device_id", "all");
		}
		
		Boolean notiFlag = false;
		
		switch (orderString) {
		case "result_noti":
			notiFlag = true;
			
			
			break;
		case "execute_noti":
			notiFlag = true;
			//body xml 파싱
			DocumentBuilderFactory t_dbf = null;
			DocumentBuilder t_db = null;
			Document t_doc = null;
			NodeList t_nodes1 = null;
			NodeList t_nodes2 = null;
			NodeList t_nodes3 = null;
			Element t_element = null;
			Element t_element2 = null;
			Element t_element3 = null;
			InputSource t_is = new InputSource();
			
			try
			{
				HashMap command_notiParam = null;
				t_dbf = DocumentBuilderFactory.newInstance();
				t_db = t_dbf.newDocumentBuilder();
				t_is = new InputSource();
				t_is.setCharacterStream(new StringReader(jsonStr));
				t_doc = t_db.parse(t_is);
				t_nodes1 = t_doc.getElementsByTagName("con");
				t_nodes2 = t_doc.getElementsByTagName("sur");
				t_nodes3 = t_doc.getElementsByTagName("cnf");
				for (int i = 0, t_len = t_nodes1.getLength(); i < t_len; i ++){
					
					t_element = (Element)t_nodes1.item(i);
					t_element2 = (Element)t_nodes2.item(i);
					t_element3 = (Element)t_nodes3.item(i);
					
					String conString = null;
					
					String execIdString = null;
					
					if(t_element3.getTextContent().equals("text/plain:0")){
						
						conString = t_element.getTextContent();
						
						execIdString = "";
						
						headerParamHashmap.put("status", conString);
						
					}else if(t_element3.getTextContent().equals("application/json:1")){
						
						conString = base64Convertor.decrypt(t_element.getTextContent());
						
						jsonResultObject = new JSONObject();
						try {
							jsonResultObject = (JSONObject) jsonParser.parse(conString);
							
							execIdString = (String) jsonResultObject.get("exec_id");
							conString = (String) jsonResultObject.get("data");
							
						} catch (Exception e) {
							// TODO: handle exception
						}
						
						
						headerParamHashmap.put("status", conString);
					}
					
					
					String surString = t_element2.getTextContent();
					
					String[] surStringList = surString.split("/");
					
					param.put("device_id", surStringList[3]);
					headerParamHashmap.put("subfunction", surStringList[5]);
					resultList = selectStatus(param);
					
					
					if(resultList.size() > 0){
						
						for(int ii = 0; ii < resultList.size(); ii++){
							
							HashMap<String, String> hashMap = new HashMap<>();
							
							hashMap = (HashMap<String, String>) resultList.get(ii);
							
							String commandUrl = "";
							commandUrl = hashMap.get("api_url") + "/" + hashMap.get("device_id") + "/" + "command" + "/" + conString 
							+"?" + "access_token=" + hashMap.get("token");
							onem2mPost = new Onem2mPost();
							
							onem2mPost.setData(commandUrl , "command", headerParamHashmap, null, null);

							HashMap<String, String> hashMap2 = onem2mPost.getData();
							
							if(hashMap2 != null){
								

								if(hashMap2.get("content") != null){
									
									jsonResultObject = new JSONObject();
									try {
										
										String contentString = String.valueOf(hashMap2.get("content"));
										
										jsonResultObject = (JSONObject) jsonParser.parse(contentString);
									} catch (ParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}



								if(jsonResultObject == null && !jsonResultObject.get("result").equals("Success")){
									resultString = "Instance Not Found";
								}

								headerParamHashmap.put("ae", hashMap.get("device_id"));
								headerParamHashmap.put("deviceId", hashMap.get("device_id"));
								headerParamHashmap.put("X-M2M-Origin", hashMap.get("device_id") + "_origin");
								headerParamHashmap.put("X-M2M-RI", hashMap.get("device_id") + "_ri");
								headerParamHashmap.put("smd", "");
								headerParamHashmap.put("requestStatus", headerParamHashmap.get("status"));
								headerParamHashmap.put("url", hashMap.get("path_result"));
								headerParamHashmap.put("Instance_rn", "result");
								headerParamHashmap.put("conString", headerParamHashmap.get("status"));
								headerParamHashmap.put("exec_id", execIdString);
								headerParamHashmap.put("exec_result", "success");
								
								
								onem2mPost.setData(hashMap.get("path_result") , Onem2mThingsTypeSelect.INSTANCE, headerParamHashmap, null, null);

								onem2mPost.getData();
								

								buf.append("{");
								buf.append("\"deviceId\": \"" + hashMap.get("device_id") + "\",");
								buf.append("\"result\": \"" + resultString + "\",");
								buf.append("\"reason\": \"N/A\"");
								buf.append("}");

							}else{
								headerParamHashmap.put("ae", hashMap.get("device_id"));
								headerParamHashmap.put("deviceId", hashMap.get("device_id"));
								headerParamHashmap.put("X-M2M-Origin", hashMap.get("device_id") + "_origin");
								headerParamHashmap.put("X-M2M-RI", hashMap.get("device_id") + "_ri");
								headerParamHashmap.put("smd", "");
								headerParamHashmap.put("requestStatus", headerParamHashmap.get("status"));
								headerParamHashmap.put("url", hashMap.get("path_result"));
								headerParamHashmap.put("Instance_rn", "result");
								headerParamHashmap.put("conString", headerParamHashmap.get("status"));
								headerParamHashmap.put("exec_id", execIdString);
								headerParamHashmap.put("exec_result", "failure");
								
								
								onem2mPost.setData(hashMap.get("path_result") , Onem2mThingsTypeSelect.INSTANCE, headerParamHashmap, null, null);

								onem2mPost.getData();
								
								buf.append("{");
								buf.append("\"deviceId\": \"" + hashMap.get("device_id") + "\",");
								buf.append("\"result\": \"" + "Instance Not Found" + "\",");
								buf.append("\"reason\": \"N/A\"");
								buf.append("}");
								
								
								
							}
							
							
							return buf;
						}
						
						
						
						
					}else{
						buf.append("{");
						buf.append("\"deviceId\": \"" + param.get("device_id") + "\",");
						buf.append("\"result\": \"" + "Fail" + "\",");
						buf.append("\"reason\": \"This Smart things is not registered at Database !!\"");
						buf.append("}");
						return buf;
					}
					
					updateStatus(headerParamHashmap);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			buf.append("{");
			buf.append("\"deviceId\": \"" + param.get("device_id") + "\",");
			buf.append("\"result\": \"" + "Success" + "\",");
			buf.append("\"reason\": \"N/A\"");
			buf.append("}");
			return buf;
			
			
		default:
			break;
		}
		
		
		
		
		
			if(!notiFlag){
				resultList = selectStatus(param);
			}
			
			
			if(!notiFlag && resultList.size() > 0 ){

				HashMap<String, String> hashMap = new HashMap<>();

				for(int i = 0; i < resultList.size(); i++){

					hashMap = (HashMap<String, String>) resultList.get(i);
					headerParamHashmap.put("ae", hashMap.get("ae"));
					headerParamHashmap.put("id", hashMap.get("id"));
					try {

						if(param.get("device_id").equals("all") || headerParamHashmap.get("token") != null  ){


							
							
							if(headerParamHashmap.get("X-M2M-Origin") == null){
								if(headerParamHashmap.get("ae") != null){
									headerParamHashmap.put("X-M2M-Origin", headerParamHashmap.get("ae").toString() + "_origin");
								}else{
									headerParamHashmap.put("X-M2M-Origin", "Tas_origin");
								}
								
							}
							
							if(headerParamHashmap.get("X-M2M-RI") == null){
								if(headerParamHashmap.get("ae") != null){
									headerParamHashmap.put("X-M2M-RI", headerParamHashmap.get("ae").toString() + "_ri");
								}else{
									headerParamHashmap.put("X-M2M-RI", "Tas_ri");
								}
								
							}
							switch (orderString) {
								
							
							case "status":
//								cheker = new Onem2mCheker(hashMap.get("path_status"), headerParamHashmap, bodyJsonObject);
//
//								cheker.start();
//								cheker.join();
//								
								
								
								
								Matcher mc = null;

								if(hashMap.get("path_status") != null){
									mc = urlPattern.matcher(hashMap.get("path_status"));
									if(((Matcher) mc).matches()){
										for(int ii=0;ii<=((MatchResult) mc).groupCount();ii++);
									}

								}
								else {
									System.out.println("not found");
								}
								
								
								if(onem2mPost == null){
									onem2mPost = new Onem2mPost();
								}
								
								headerParamHashmap.put("Instance_rn", mc.group(7).toString());
								headerParamHashmap.put("conString", headerParamHashmap.get("status"));

								onem2mPost.setData(hashMap.get("path_status") , Onem2mThingsTypeSelect.INSTANCE, headerParamHashmap, null, null);
								
								
								
								hashMap2 = onem2mPost.getData();
								
								if(hashMap2 != null){
									
									

									if(hashMap2.get("content") != null){
										JSONParser jsonParser = new JSONParser();
										jsonResultObject = new JSONObject();
										try {
											String contentString = hashMap2.get("content").toString();
											jsonResultObject = (JSONObject) jsonParser.parse(contentString);
										} catch (ParseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										jsonResultObject = (JSONObject) jsonResultObject.get("m2m:cin");
									}


									if(jsonResultObject != null && jsonResultObject.get("ct") != null){
										rttimeString = jsonResultObject.get("ct").toString();
										headerParamHashmap.put("response_date", rttimeString);
									}

									if(jsonResultObject != null && jsonResultObject.get("con") != null){
										valueString = jsonResultObject.get("con").toString();
										headerParamHashmap.put("response_value", valueString);
									}

									updateStatus(headerParamHashmap);
									
									
									if(jsonResultObject == null ){
										resultString = "Instance Not Found";
									}

									buf.append("{");
									buf.append("\"deviceId\": \"" + headerParamHashmap.get("ae") + "\",");
									buf.append("\"result\": \"" + resultString + "\",");
									buf.append("\"reason\": \"N/A\"");
									buf.append("}");

								}else{
									
									buf.append("{");
									buf.append("\"deviceId\": \"" + headerParamHashmap.get("ae") + "\",");
									buf.append("\"result\": \"" + resultString + "\",");
									buf.append("\"reason\": \"N/A\"");
									buf.append("}");
									
									
									
								}
								
								
								break;
							
							case "result":
//								cheker = new Onem2mCheker(hashMap.get("path_status"), headerParamHashmap, bodyJsonObject);
//
//								cheker.start();
//								cheker.join();
//								
								
								
								
								Matcher resultMc = null;

								if(hashMap.get("path_result") != null){
									resultMc = urlPattern.matcher(hashMap.get("path_result"));
									if(((Matcher) resultMc).matches()){
										for(int ii=0;ii<=((MatchResult) resultMc).groupCount();ii++);
									}

								}
								else {
									System.out.println("not found");
								}
								
								
								if(onem2mPost == null){
									onem2mPost = new Onem2mPost();
								}
								
								headerParamHashmap.put("Instance_rn", resultMc.group(7).toString());
								headerParamHashmap.put("conString", headerParamHashmap.get("status"));

								onem2mPost.setData(hashMap.get("path_result") , Onem2mThingsTypeSelect.INSTANCE, headerParamHashmap, null, null);
								
								
								
								hashMap2 = onem2mPost.getData();
								
								if(hashMap2 != null){
									
									

									if(hashMap2.get("content") != null){
										JSONParser jsonParser = new JSONParser();
										jsonResultObject = new JSONObject();
										try {
											String contentString = hashMap2.get("content").toString();
											jsonResultObject = (JSONObject) jsonParser.parse(contentString);
										} catch (ParseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										jsonResultObject = (JSONObject) jsonResultObject.get("m2m:cin");
									}


									if(jsonResultObject != null && jsonResultObject.get("ct") != null){
										rttimeString = jsonResultObject.get("ct").toString();
										headerParamHashmap.put("response_date", rttimeString);
									}

									if(jsonResultObject != null && jsonResultObject.get("con") != null){
										valueString = jsonResultObject.get("con").toString();
										headerParamHashmap.put("response_value", valueString);
									}

									updateStatus(headerParamHashmap);
									
									
									if(jsonResultObject == null ){
										resultString = "Instance Not Found";
									}

									buf.append("{");
									buf.append("\"deviceId\": \"" + headerParamHashmap.get("ae") + "\",");
									buf.append("\"result\": \"" + resultString + "\",");
									buf.append("\"reason\": \"N/A\"");
									buf.append("}");

								}else{
									
									buf.append("{");
									buf.append("\"deviceId\": \"" + headerParamHashmap.get("ae") + "\",");
									buf.append("\"result\": \"" + resultString + "\",");
									buf.append("\"reason\": \"N/A\"");
									buf.append("}");
									
									
									
								}
								
								
								break;
							case "getstatus":
								
								
								
								onem2mGet = new Onem2mGet();

								onem2mGet.setData(hashMap.get("path_status") + "/la" , headerParamHashmap);

								hashMap2 = onem2mGet.getData();
								
								if(hashMap2 != null){
									

									if(hashMap2.get("content") != null){
										JSONParser jsonParser = new JSONParser();
										jsonResultObject = new JSONObject();
										try {
											String contentString = (String)hashMap2.get("content").toString();
											jsonResultObject = (JSONObject) jsonParser.parse(contentString);
										} catch (ParseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										jsonResultObject = (JSONObject) jsonResultObject.get("cin");
									}

									

									if(jsonResultObject != null && jsonResultObject.get("ct") != null){
										rttimeString = jsonResultObject.get("ct").toString();
										headerParamHashmap.put("response_date", rttimeString);
									}

									if(jsonResultObject != null && jsonResultObject.get("con") != null){
										valueString = jsonResultObject.get("con").toString();
										headerParamHashmap.put("response_value", valueString);
									}

									updateStatus(headerParamHashmap);

									if(jsonResultObject == null ){
										resultString = "Fail";
									}


									buf.append("{");
									buf.append("\"deviceId\": \"" + headerParamHashmap.get("ae") + "\",");
									buf.append("\"status\": \"" + valueString + "\",");
									buf.append("\"result\": \"" + resultString + "\",");
									buf.append("\"reason\": \"N/A\"");
									buf.append("}");

								}else{
									
									buf.append("{");
									buf.append("\"deviceId\": \"" + headerParamHashmap.get("ae") + "\",");
									buf.append("\"status\": \"" + valueString + "\",");
									buf.append("\"result\": \"" + resultString + "\",");
									buf.append("\"reason\": \"N/A\"");
									buf.append("}");
									
									
									
								}
								
								
								break;

							case "measurement":

								cheker = new Onem2mCheker(hashMap.get("path_status"), headerParamHashmap, bodyJsonObject);

								cheker.start();
								cheker.join();
								
								
								
								Matcher measurementMc = null;

								if(hashMap.get("path_status") != null){
									measurementMc = urlPattern.matcher(hashMap.get("path_status"));
									if(((Matcher) measurementMc).matches()){
										for(int ii=0;ii<=((MatchResult) measurementMc).groupCount();ii++);
									}

								}
								else {
									System.out.println("not found");
								}
								
								
								
								onem2mPost = new Onem2mPost();
								
								headerParamHashmap.put("Instance_rn", measurementMc.group(7).toString());
								headerParamHashmap.put("measurement", jsonStr);

								onem2mPost.setData(hashMap.get("path_status") , Onem2mThingsTypeSelect.INSTANCE, headerParamHashmap, null, null);

								hashMap2 = onem2mPost.getData();
								
								if(hashMap2 != null){
									

									if(hashMap2.get("content") != null){
										JSONParser jsonParser = new JSONParser();
										jsonResultObject = new JSONObject();
										try {
											String contentString = hashMap2.get("content").toString();
											jsonResultObject = (JSONObject) jsonParser.parse(contentString);
										} catch (ParseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										jsonResultObject = (JSONObject) jsonResultObject.get("cin");
									}


									if(jsonResultObject != null && jsonResultObject.get("ct") != null){
										rttimeString = jsonResultObject.get("ct").toString();
										headerParamHashmap.put("response_date", rttimeString);
									}

									if(jsonResultObject != null && jsonResultObject.get("con") != null){
										valueString = jsonResultObject.get("con").toString();
										headerParamHashmap.put("response_value", valueString);
									}

									updateStatus(headerParamHashmap);

									if(jsonResultObject == null ){
										resultString = "Instance Not Found";
									}


									buf.append("{");
									buf.append("\"deviceId\": \"" + headerParamHashmap.get("ae") + "\",");
									buf.append("\"result\": \"" + resultString + "\",");
									buf.append("\"reason\": \"N/A\"");
									buf.append("}");

								}else{
									
									buf.append("{");
									buf.append("\"deviceId\": \"" + headerParamHashmap.get("ae") + "\",");
									buf.append("\"result\": \"" + resultString + "\",");
									buf.append("\"reason\": \"N/A\"");
									buf.append("}");
									
									
									
								}
								
								break;
								
							case "smartstatus" :	
							
								
								
								
								String smartstatusUrl = "";
								smartstatusUrl = hashMap.get("api_url") + "/" + hashMap.get("device_id") + "/" + "status" + ""  
								+"?" + "access_token=" + hashMap.get("token");
								onem2mGet = new Onem2mGet();
								
								onem2mGet.setData(smartstatusUrl , headerParamHashmap);
								onem2mGet.start();
								onem2mGet.join();

								hashMap2 = onem2mGet.getData();
								
								if(hashMap2 != null){
									

									if(hashMap2.get("content") != null){
										JSONParser jsonParser = new JSONParser();
										jsonResultObject = new JSONObject();
										try {
											String contentString = hashMap2.get("content").toString();
											jsonResultObject = (JSONObject) jsonParser.parse(contentString);
										} catch (ParseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}



									if(jsonResultObject != null && jsonResultObject.get("status") != null){
										
										valueString= jsonResultObject.get("status").toString();
										
									}else{
										
										resultString = "Instance Not Found";
										
									}


									buf.append("{");
									buf.append("\"deviceId\": \"" + headerParamHashmap.get("ae") + "\",");
									buf.append("\"status\": \"" + valueString + "\",");
									buf.append("\"result\": \"" + resultString + "\",");
									buf.append("\"reason\": \"N/A\"");
									buf.append("}");

								}else{
									
									buf.append("{");
									buf.append("\"deviceId\": \"" + headerParamHashmap.get("ae") + "\",");
									buf.append("\"status\": \"" + valueString + "\",");
									buf.append("\"result\": \"" + resultString + "\",");
									buf.append("\"reason\": \"N/A\"");
									buf.append("}");
									
									
									
								}
								
								break;
								
							case "command" :	
								
								String commandUrl = "";
								commandUrl = hashMap.get("api_url") + "/" + hashMap.get("device_id") + "/" + "command" + "/" + headerParamHashmap.get("status") 
								+"?" + "access_token=" + hashMap.get("token");
								onem2mPost = new Onem2mPost();
								
								onem2mPost.setData(commandUrl , "command", headerParamHashmap, null, null);

								hashMap2 = onem2mPost.getData();
								
								if(hashMap2 != null){
									

									if(hashMap2.get("content") != null){
										JSONParser jsonParser = new JSONParser();
										jsonResultObject = new JSONObject();
										try {
											String contentString = hashMap2.get("content").toString();
											jsonResultObject = (JSONObject) jsonParser.parse(contentString);
										} catch (ParseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										jsonResultObject = (JSONObject) jsonResultObject.get("cin");
									}


									if(jsonResultObject != null && jsonResultObject.get("ct") != null){
										rttimeString = jsonResultObject.get("ct").toString();
									}

									if(jsonResultObject != null && jsonResultObject.get("con") != null){
										valueString = jsonResultObject.get("con").toString();
									}

									if(jsonResultObject == null ){
										resultString = "Instance Not Found";
									}


									buf.append("{");
									buf.append("\"deviceId\": \"" + headerParamHashmap.get("ae") + "\",");
									buf.append("\"result\": \"" + resultString + "\",");
									buf.append("\"reason\": \"N/A\"");
									buf.append("}");

								}else{
									
									buf.append("{");
									buf.append("\"deviceId\": \"" + headerParamHashmap.get("ae") + "\",");
									buf.append("\"result\": \"" + resultString + "\",");
									buf.append("\"reason\": \"N/A\"");
									buf.append("}");
									
									
									
								}
								
								break;
								
							case "smartstatusall" :	
								
								String smartstatusallUrl = "";
								smartstatusallUrl = hashMap.get("api_url") + "/" + "status" + ""  
								+"?" + "access_token=" + hashMap.get("token");
								onem2mGet = new Onem2mGet();
								
								onem2mGet.setData(smartstatusallUrl , headerParamHashmap);

								hashMap2 = onem2mGet.getData();
								
								if(hashMap2 != null){
									

									if(hashMap2.get("content") != null){
										JSONParser jsonParser = new JSONParser();
										jsonResultObject = new JSONObject();
										
										if(hashMap2.get("content") != null){
												
											valueString= hashMap2.get("content").toString();
												
										}else{
												
											resultString = "Instance Not Found";
												
										}	
									}



									buf.append("{");
									buf.append("\"deviceId\": \"" + headerParamHashmap.get("ae") + "\",");
									buf.append("\"status\": \"" + valueString + "\",");
									buf.append("\"result\": \"" + resultString + "\",");
									buf.append("\"reason\": \"N/A\"");
									buf.append("}");

								}else{
									
									buf.append("{");
									buf.append("\"deviceId\": \"" + headerParamHashmap.get("ae") + "\",");
									buf.append("\"status\": \"" + valueString + "\",");
									buf.append("\"result\": \"" + resultString + "\",");
									buf.append("\"reason\": \"N/A\"");
									buf.append("}");
									
									
									
								}
								
								break;
								
							case "delete":
								
								onem2mDeleter = new Onem2mDeleter();

								onem2mDeleter.setData("" ,headerParamHashmap);
								onem2mDeleter.start();
								onem2mDeleter.join();

								if(onem2mDeleter.getData() != null){
									hashMap = onem2mDeleter.getData();

									if(hashMap.get("content") != null){
										JSONParser jsonParser = new JSONParser();
										jsonResultObject = new JSONObject();
										try {
											jsonResultObject = (JSONObject) jsonParser.parse(hashMap.get("content").toString());
										} catch (ParseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										jsonResultObject = (JSONObject) jsonResultObject.get("cnt");
									}

									if(jsonResultObject != null && jsonResultObject.get("ct") != null){
										rttimeString = jsonResultObject.get("ct").toString();
									}

									if(jsonResultObject != null && jsonResultObject.get("con") != null){
										valueString = jsonResultObject.get("con").toString();
									}

//									deleteStatus(headerParamHashmap);
									
									if(jsonResultObject == null ){
										resultString = "Instance Not Found";
									}


									buf.append("{");
									buf.append("\"deviceId\": \"" + headerParamHashmap.get("ae") + "\",");
									buf.append("\"result\": \"" + resultString + "\",");
									buf.append("\"reason\": \"N/A\"");
									buf.append("}");

								}else{
									
									buf.append("{");
									buf.append("\"deviceId\": \"" + headerParamHashmap.get("ae") + "\",");
									buf.append("\"result\": \"" + resultString + "\",");
									buf.append("\"reason\": \"N/A\"");
									buf.append("}");
									
									
									
								}
								
								
								
								break;
								
							default:

								tasLog.debug("[ TAS ERROR ] :: " + "Smart Things Request command is unuse ->" +  orderString);

								break;
							}


						}else{

							tasLog.debug("[ TAS ERROR ] :: " + "Smart Things Token is not same!!");

						}
					} catch (InterruptedException e) {

						tasLog.debug("[ TAS ERROR ] :: " + "Smart Things Parameter parsing error!!");

						e.printStackTrace();
					}
				}

			}else{

				tasLog.debug("[ TAS ERROR ] ::" +  "This Smart things is not registered at Database !!");

			}
			
		return buf;
		
	}
	
	private void updateStatus(HashMap headerParamHashmap){
		
		if(sqlMapper == null){
			sqlMapper = SQLManager.sqlMapper;
		}
		
		ModelAndView mavRetn = new ModelAndView();
		
		//Create JsonObject
		JSONArray array = new JSONArray();

		ArrayList UserList = new ArrayList<>();
		
		HashMap<String, String> param = new HashMap<>();
		param.put("id", headerParamHashmap.get("id").toString());
		if(headerParamHashmap.get("response_value") == null){
			param.put("response_value", "");
		}else{
			param.put("response_value", headerParamHashmap.get("response_value").toString());
		}
		
		if(headerParamHashmap.get("response_date") == null){
			param.put("response_date", "");
		}else{
			param.put("response_date", headerParamHashmap.get("response_date").toString());
		}
		
		try
		{	
			sqlMapper.startTransaction();

			sqlMapper.update(smartthingUpdate, param);		 

			sqlMapper.commitTransaction();
			
		}catch( Exception e )
		{
			tasLog.debug("[ TAS ERROR ] ::" +  "updateStatus Error !!");
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				sqlMapper.endTransaction();
			} 
			catch ( SQLException e ) 
			{
				e.printStackTrace();
			}
		}
		
	}
	
	private void deleteStatus(HashMap headerParamHashmap){
		if(sqlMapper == null){
			sqlMapper = SQLManager.sqlMapper;
		}

		ModelAndView mavRetn = new ModelAndView();

		JSONObject totalObject = new JSONObject();

		try
		{	
			sqlMapper.startTransaction();

			sqlMapper.insert(smartthingDelete, headerParamHashmap);		 

			sqlMapper.commitTransaction();

		}catch( Exception e )
		{
			tasLog.debug("[ TAS ERROR ] ::" +  "deleteStatus Error !!");
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				sqlMapper.endTransaction();
			} 
			catch ( SQLException e ) 
			{
				e.printStackTrace();
			}
		}

	}
	
	private ArrayList selectStatus(HashMap headerParamHashmap){
		if(sqlMapper == null){
			sqlMapper = SQLManager.sqlMapper; 
		}

		ModelAndView mavRetn = new ModelAndView();

		ArrayList resultList = new ArrayList<>();


		try {
			resultList = (ArrayList)sqlMapper.queryForList(smartthingSelect,headerParamHashmap);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultList;
	}
	
}

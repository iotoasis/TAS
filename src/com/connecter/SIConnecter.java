package com.connecter;

import java.util.HashMap;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.common.Base64Convertor;
import com.http.HttpURLConnecterThread;
import com.onem2m.Onem2mCheker;
import com.onem2m.Onem2mDeleter;
import com.onem2m.Onem2mGet;
import com.onem2m.Onem2mPost;
import com.service.utile.MethodTypeSelect;
import com.service.utile.Onem2mThingsTypeSelect;

public class SIConnecter extends Thread{

	
	public  static  Logger tasLog = Logger.getRootLogger();
	
	private Base64Convertor base64Convertor = new Base64Convertor();
	
	Pattern urlPattern = Pattern.compile("^(https?):\\/\\/([^:\\/\\s]+)(:([^\\/]*))?((\\/[^\\s/\\/]+)*)?\\/([^#\\s\\?]*)(\\?([^#\\s]*))?(#(\\w*))?$");

	

	HashMap headerParamHashmap = null;
	
	String orderString = "";
	
	JSONObject bodyJsonObject = null;
	
	StringBuilder buf = null;
	
	String jsonStr = "";
	
	Onem2mCheker onem2mCheker = null;
	Onem2mDeleter onem2mDeleter = null;
	Onem2mGet onem2mGet = null;
	Onem2mPost onem2mPost = null;
	
	
	public void setData(HashMap headerParamHashmap, String orderString, JSONObject bodyJsonObject, StringBuilder buf, String jsonStr){
		this.headerParamHashmap = headerParamHashmap;
		this.orderString = orderString;
		this.bodyJsonObject = bodyJsonObject;
		this.buf = buf;
		this.jsonStr = jsonStr;
		
	}
	
	public StringBuilder getData(){
		
		tasLog.debug("[ TAS INFO ] ::" +  "This request device type is Normal Device !!");

		try {



			

			Pattern urlPattern = Pattern.compile("^(https?):\\/\\/([^:\\/\\s]+)(:([^\\/]*))?((\\/[^\\s/\\/]+)*)?\\/([^#\\s\\?]*)(\\?([^#\\s]*))?(#(\\w*))?$");
			Matcher mc = null;

			if(headerParamHashmap.get("url") != null){
				mc = urlPattern.matcher(headerParamHashmap.get("url").toString());
				if(((Matcher) mc).matches()){
					for(int i=0;i<=((MatchResult) mc).groupCount();i++);
				}

			}
			else {
			}
			JSONObject jsonResultObject = new JSONObject();

			switch (orderString) {
			case "getsm":
				HashMap hashMap = new HashMap<>();
				
				HttpURLConnecterThread connecterThread2 = new HttpURLConnecterThread(headerParamHashmap.get("url") + "/la", MethodTypeSelect.GET.toString(), headerParamHashmap, null, null);
				connecterThread2.start();
				try {
					connecterThread2.join();
					hashMap = connecterThread2.getData();
					connecterThread2.init();
					connecterThread2 = null;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				

				if(hashMap != null){

					if(hashMap.get("content") != null){
						JSONParser jsonParser = new JSONParser();
						jsonResultObject = new JSONObject();
						try {
							jsonResultObject = (JSONObject) jsonParser.parse(hashMap.get("content").toString());
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						jsonResultObject = (JSONObject) jsonResultObject.get("m2m:cin");
					}

					String rttimeString = "";
					String valueString = "";
					String resultString = "Success";

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
					buf.append("\"id\": \"" + headerParamHashmap.get("id") + "\",");
					buf.append("\"deviceId\": \"" + headerParamHashmap.get("ae") + "\",");
					buf.append("\"result\": \"" + resultString + "\",");
					buf.append("\"rtime\": \"" + rttimeString + "\",");
					buf.append("\"value\": \"" + valueString + "\",");
					buf.append("\"reason\": \"N/A\"");
					buf.append("}");

				}else{
					
					buf.append("{");
					buf.append("\"id\": \"" + headerParamHashmap.get("id") + "\",");
					buf.append("\"deviceId\": \"" + headerParamHashmap.get("ae") + "\",");
					buf.append("\"result\": \"" + "Instance Not Found" + "\",");
					buf.append("\"rtime\": \"" + "" + "\",");
					buf.append("\"value\": \"" + "" + "\",");
					buf.append("\"reason\": \"N/A\"");
					buf.append("}");
					
					
					
				}


				break;
			case "postsm":

				if(onem2mPost == null){
					onem2mPost = new Onem2mPost();
				}

				headerParamHashmap.put("Instance_rn", mc.group(7).toString());
				headerParamHashmap.put("conString", headerParamHashmap.get("requestStatus"));

				onem2mPost.setData(headerParamHashmap.get("url").toString() , Onem2mThingsTypeSelect.INSTANCE, headerParamHashmap, null, null);
				onem2mPost.start();
				onem2mPost.join();	
				hashMap = onem2mPost.getData();
				if(!hashMap.isEmpty() && hashMap.get("responseCode").equals("200") || hashMap.get("responseCode").equals("201")  ){

					if(hashMap.get("content") != null){
						JSONParser jsonParser = new JSONParser();
						jsonResultObject = new JSONObject();
						try {
							jsonResultObject = (JSONObject) jsonParser.parse(hashMap.get("content").toString());
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						jsonResultObject = (JSONObject) jsonResultObject.get("cin");
					}

					String rttimeString = "";
					String valueString = "";
					String resultString = "Success";

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
					buf.append("\"id\": \"" + headerParamHashmap.get("id") + "\",");
					buf.append("\"deviceId\": \"" + headerParamHashmap.get("ae") + "\",");
					buf.append("\"result\": \"" + resultString + "\",");
					buf.append("\"rtime\": \"" + rttimeString + "\",");
					buf.append("\"value\": \"" + valueString + "\",");
					buf.append("\"reason\": \"N/A\"");
					buf.append("}");
				}else{
					if(hashMap.get("responseCode").equals("400") || hashMap.get("responseCode").equals("404")){
						onem2mCheker = new Onem2mCheker(headerParamHashmap.get("url").toString(), headerParamHashmap, bodyJsonObject);

						onem2mCheker.start();
						onem2mCheker.join();
					}
					
					
					
					buf.append("{");
					buf.append("\"id\": \"" + headerParamHashmap.get("id") + "\",");
					buf.append("\"deviceId\": \"" + headerParamHashmap.get("ae") + "\",");
					buf.append("\"result\": \"" + "Fail" + "\",");
					buf.append("\"rtime\": \"" + "" + "\",");
					buf.append("\"value\": \"" + "" + "\",");
					buf.append("\"reason\": \"N/A\"");
					buf.append("}");
					
					
				}
				break;

			case "deletesm":

				if(onem2mDeleter == null){
					onem2mDeleter = new Onem2mDeleter();
				}
				

				onem2mDeleter.setData(headerParamHashmap.get("url").toString(), headerParamHashmap);
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

					String rttimeString = "";
					String valueString = "";
					String resultString = "Success";

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
					buf.append("\"id\": \"" + headerParamHashmap.get("id") + "\",");
					buf.append("\"deviceId\": \"" + headerParamHashmap.get("ae") + "\",");
					buf.append("\"result\": \"" + resultString + "\",");
					buf.append("\"rtime\": \"" + rttimeString + "\",");
					buf.append("\"value\": \"" + valueString + "\",");
					buf.append("\"reason\": \"N/A\"");
					buf.append("}");

				}else{
					
					buf.append("{");
					buf.append("\"id\": \"" + headerParamHashmap.get("id") + "\",");
					buf.append("\"deviceId\": \"" + headerParamHashmap.get("ae") + "\",");
					buf.append("\"result\": \"" + "Fail" + "\",");
					buf.append("\"rtime\": \"" + "" + "\",");
					buf.append("\"value\": \"" + "" + "\",");
					buf.append("\"reason\": \"N/A\"");
					buf.append("}");
					
					
					
				}

				break;

				
			case "check":

				headerParamHashmap.put("Instance_rn", mc.group(7).toString());
				headerParamHashmap.put("conString", headerParamHashmap.get("requestStatus"));

				onem2mCheker = new Onem2mCheker(headerParamHashmap.get("url").toString(), headerParamHashmap, bodyJsonObject);

				onem2mCheker.start();
				onem2mCheker.join();

				buf.append("{");
				buf.append("\"id\": \"" + headerParamHashmap.get("id") + "\",");
				buf.append("\"deviceId\": \"" + headerParamHashmap.get("ae") + "\",");
				buf.append("\"result\": \"" + "Success" + "\"");
				buf.append("}");

				break;


			case "timeout":
				
				for(int i=0 ; i< 40; i ++){
					Thread.sleep(1000);
				}
				
				
				break;
				
			default:

				tasLog.debug("[ TAS ERROR ] :: " + "Normal Device Request command is unuse ->" +  orderString);
				buf.append("{");
				buf.append("\"result\": \"" + "Fail" + "\",");
				buf.append("\"reason\": \"Request command is unuse\"");
				buf.append("}");
				break;
			}




		} catch (InterruptedException e) {

			tasLog.debug("[ TAS ERROR ] :: " + "Normal Device Parameter parsing error!!");

			e.printStackTrace();
		}
		
		return buf;
		
	}
	
}

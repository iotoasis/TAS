package com.onem2m;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import com.service.utile.Onem2mThingsTypeSelect;


public class Onem2mCheker extends Thread{

	public  static  Logger tasLog = Logger.getRootLogger();
	String requestUrl = null;
	HashMap requestHashMap = null;
	JSONObject requestContentJsonObject = null;
	
	HashMap aeResultHashMap = new HashMap<>();
	HashMap containerResultHashMap = new HashMap<>();
	HashMap instanceResultHashMap = new HashMap<>();
	HashMap pollingChannelresultHashMap = new HashMap<>();
	HashMap subscriptionResultHashMap = new HashMap<>();
	HashMap semanticcommandResultHashMap = new HashMap<>();
	
	Onem2mPost onem2mPost = null;
	Onem2mGet onem2mGet = null;
	Onem2mDeleter onem2mDeleter = null;

	String httpString = "http://";
	String hostString = null;
	String portString = null;

	ArrayList<String> cseStringList = null;
	String cseString = "";

	String deviceIdString = null;

	ArrayList<String> containerArrayList = null;
	String containerString = null;

	ArrayList<String> instanceArrayList = null;
	String instanceString = null;

	public Onem2mCheker(String requestUrl, HashMap requestHashMap, JSONObject requestContentJsonObject){

		this.requestUrl = requestUrl;
		this.requestHashMap = requestHashMap;
		this.requestContentJsonObject = requestContentJsonObject;

	}


	/*
		1.널체크
		2.Request URI 방식확인(POST, GET, PUT, DELETE)
	 	3.Request Parameter 방식(POST, GET) - POST 우선
	 	4.Request Parameter 항목들 확인
	 	5.Request Content-Body 확인
	 	6.URI 항목별로 생성(최초 1회 생성 - 에러 무시)

	 */

	public void run() {

		containerArrayList = new ArrayList<>();

		cseStringList = new ArrayList<>();

		instanceArrayList = new ArrayList<>();

		Pattern urlPattern = Pattern.compile("^(https?):\\/\\/([^:\\/\\s]+)(:([^\\/]*))?((\\/[^\\s/\\/]+)*)?\\/([^#\\s\\?]*)(\\?([^#\\s]*))?(#(\\w*))?$");


		Matcher mc = urlPattern.matcher(requestUrl);

		if(((Matcher) mc).matches()){
			for(int i=0;i<=((MatchResult) mc).groupCount();i++);
		} else {
		}


		hostString = mc.group(2);
		portString = mc.group(4);

		deviceIdString = (String) requestHashMap.get("ae");

		String[] strings = mc.group(5).split(deviceIdString);

		String[] strings2 = strings[0].split("/");
		
		
		for(int i = 0; i < strings2.length; i++){
			
			cseString += strings2[i];
			
			if(i + 1 < strings2.length){
				
				cseString += "/";
				
			}
			
		}
		
		
		for(int i = 6; i < mc.groupCount();  i ++){
			if(mc.group(i) != null){

				containerArrayList.add(mc.group(i).replace("/", ""));
			}
		}


		/*
		 1. AE
		 2. Cotainer * n개
		 3. Instance * n개
		 4. polling channel
		 5. Subscription
		 6. semantic descriptor
		 */


		onem2mGet = new Onem2mGet();
		onem2mPost = new Onem2mPost();

		HashMap<String, String> aePostHashMap = null;
		// 1. AE
		try {

			onem2mGet = new Onem2mGet();
			onem2mGet.setData(httpString + hostString + ":" + portString + "" + cseString + "/" + deviceIdString, requestHashMap);
			onem2mGet.start();
			onem2mGet.join();

			HashMap hashMap = new HashMap<>();
			
			hashMap = onem2mGet.getData();
			
			if(hashMap.isEmpty() || !hashMap.get("responseCode").equals("200")){
				
				onem2mPost.setData(httpString + hostString + ":" + portString + "" + cseString , Onem2mThingsTypeSelect.AE, requestHashMap, null, null);

				aePostHashMap = onem2mPost.getData();
				
				tasLog.debug("[ TAS INFO ]" + "Create AE Complete!!");
				
			}

		} catch (InterruptedException e) {

			tasLog.debug("[ TAS ERROR ] :: " + "Onem2mChecker AE Part error!!");

			e.printStackTrace();
		}

		// 2. Cotainer * n개
		containerString = "";
		String oldString ="";
		for(int i = 0; i < containerArrayList.size(); i++){

			containerString += containerArrayList.get(i);

			try {
				onem2mGet = new Onem2mGet();
				onem2mGet.setData(httpString + hostString + ":" + portString + "" + cseString + "/" + deviceIdString + "/" + containerString, requestHashMap);

				HashMap hashMap = new HashMap<>();
				
				hashMap = onem2mGet.getData();
				
				if(hashMap.isEmpty() || !hashMap.get("responseCode").equals("200")){
					requestHashMap.put("Container_rn", containerArrayList.get(i));
					onem2mPost.setData(httpString + hostString + ":" + portString + "" + cseString + "/" + deviceIdString + oldString , Onem2mThingsTypeSelect.CONTAINER, requestHashMap, null, null);

					containerResultHashMap = onem2mPost.getData();
					
					tasLog.debug("[ TAS INFO ]" + "Create Container Complete!!");

					
				}



			} catch (InterruptedException e) {

				tasLog.debug("[ TAS ERROR ] :: " + "Onem2mChecker Container Part error!!");

				e.printStackTrace();
			}

			oldString += "/" + containerString;

			if(i + 1 < containerArrayList.size()){
				containerString += "/";
			}

		}

		// 3. Instance * n개
		instanceString = "";

		try {
			onem2mGet = new Onem2mGet();
			onem2mGet.setData(httpString + hostString + ":" + portString + "" + cseString + "/" + deviceIdString + "/" + containerString + "/" + "la", requestHashMap);

			HashMap hashMap = new HashMap<>();
			
			hashMap = onem2mGet.getData();
			
			if(hashMap.isEmpty() || !hashMap.get("responseCode").equals("200")){
				onem2mPost = new Onem2mPost();
				requestHashMap.put("Instance_rn", containerArrayList.get(containerArrayList.size() -1 ));
				requestHashMap.put("conString", requestHashMap.get("requestStatus"));
				
				onem2mPost.setData(httpString + hostString + ":" + portString + "" + cseString + "/" + deviceIdString + "/" + containerString , Onem2mThingsTypeSelect.INSTANCE, requestHashMap, null, null);

				instanceResultHashMap = onem2mPost.getData();
				
				tasLog.debug("[ TAS INFO ]" + "Create Instance Complete!!");


			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// 4. Polling Channel
		if(requestHashMap.get("polling") != null){

			try {
				onem2mGet = new Onem2mGet();
				onem2mGet.setData(httpString + hostString + ":" + portString + "" + cseString + "/" + deviceIdString + "/pollingchannel", requestHashMap);
				HashMap hashMap = new HashMap<>();
				
				hashMap = onem2mGet.getData();
				
				if(hashMap.isEmpty() || !hashMap.get("responseCode").equals("200")){
					onem2mPost = new Onem2mPost();
					onem2mPost.setData(httpString + hostString + ":" + portString + "" + cseString + "/" + deviceIdString, Onem2mThingsTypeSelect.POLLINGCHANNEL, requestHashMap, null, null);

					pollingChannelresultHashMap = onem2mPost.getData();
					
					tasLog.debug("[ TAS INFO ]" + "Create Polling Channel Complete!!");

					
				}

			} catch (InterruptedException e) {

				tasLog.debug("[ TAS ERROR ] :: " + "Onem2mChecker Instance-Polling Part error!!");

				e.printStackTrace();
			}




			// 5. Subscription
			try {
				onem2mGet = new Onem2mGet();
				
				onem2mGet.setData(httpString + hostString + ":" + portString + "" + cseString + "/" + deviceIdString + "/" + containerString + "/subscription", requestHashMap);
				HashMap hashMap = new HashMap<>();
				
				hashMap = onem2mGet.getData();
				
				if(hashMap.isEmpty() || !hashMap.get("responseCode").equals("200")){

					if(requestHashMap.get("polling").equals("Y") || requestHashMap.get("polling").equals("y")){
						requestHashMap.put("subscription_url", httpString + hostString + ":" + portString + "" + cseString + "/" + deviceIdString + "/" + "pollingchannel/pcu");
					}else{
						requestHashMap.put("subscription_url", requestHashMap.get("polling").toString());
					}
					onem2mPost = new Onem2mPost();
					
					onem2mPost.setData(httpString + hostString + ":" + portString + "" + cseString + "/" + deviceIdString + "/" + containerString , Onem2mThingsTypeSelect.SUBSCRIPTION, requestHashMap, null, null);

					subscriptionResultHashMap = onem2mPost.getData();
					
					tasLog.debug("[ TAS INFO ]" + "Create Subscription Complete!!");
					
				}

			} catch (InterruptedException e) {

				tasLog.debug("[ TAS ERROR ] :: " + "Onem2mChecker Instance-Subscription Part error!!");

				e.printStackTrace();
			}
		}
		// 6. semantic descriptor
		if(requestHashMap.get("smd") != null){
			try {
				onem2mGet = new Onem2mGet();
				onem2mGet.setData(httpString + hostString + ":" + portString + "" + cseString + "/" + deviceIdString + "/" + "semanticDescriptor", requestHashMap);
				HashMap hashMap = new HashMap<>();
				
				hashMap = onem2mGet.getData();
				
				if(hashMap.isEmpty() || !hashMap.get("responseCode").equals("200")){
					onem2mPost = new Onem2mPost();
					onem2mPost.setData(httpString + hostString + ":" + portString + "" + cseString + "/" + deviceIdString, Onem2mThingsTypeSelect.SEMANTICDESCRIPTOR, requestHashMap, null, null);

					semanticcommandResultHashMap = onem2mPost.getData();
					
					tasLog.debug("[ TAS INFO ]" + "Create semantic descriptor Complete!!");
					
				}

			} catch (InterruptedException e) {

				tasLog.debug("[ TAS ERROR ] :: " + "Onem2mChecker semantic descriptor Part error!!");

				e.printStackTrace();
			}
		}


		tasLog.debug("[ TAS INFO ]" + "Onem2mChecker Check Complete!!");

	}
	
	public HashMap getAeData(){
		return aeResultHashMap;
		
	}
	
	public HashMap getContainerData(){
		return containerResultHashMap;
		
	}
	
	public HashMap getInstanceData(){
		return instanceResultHashMap;
		
	}
	
	public HashMap getPollingChannelData(){
		return pollingChannelresultHashMap;
		
	}
	
	public HashMap getSubscriptionData(){
		return subscriptionResultHashMap;
		
	}
	
	public HashMap getSemanticcommandData(){
		return semanticcommandResultHashMap;
		
	}

}

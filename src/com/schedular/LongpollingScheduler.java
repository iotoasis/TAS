package com.schedular;

import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.common.SQLManager;
import com.http.HttpURLConnecterThread;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.service.utile.MethodTypeSelect;

public class LongpollingScheduler implements Runnable{

	String simulatorSelect = "tas-sql.simulatorSelect";
	String simulator1Update = "tas-sql.simulator1Update";

	SqlMapClient sqlMapper = null;

	ArrayList resultList = null;
	HttpURLConnecterThread pollingThread = null;
	boolean pollingThreadFlag = true;
	HttpURLConnecterThread statusThread = null;
	boolean statusThreadlag = true;
	LongpollingScheduler longpollingScheduler2 = null;
	public void run(String command) {

		if(sqlMapper == null){
			sqlMapper 			= SQLManager.sqlMapper;
		}

		try {
			resultList = new ArrayList<>();
			resultList = (ArrayList)sqlMapper.queryForList(simulatorSelect,null);
			for(int i = 0; i < resultList.size(); i ++){

			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		if(command == "start"){
			for(int i = 0 ; i < resultList.size(); i ++ ){
				HashMap param1 = new HashMap<>();

				status((HashMap) resultList.get(i));

			}

		}

		for(int i = 0 ; i < resultList.size(); i ++ ){
			HashMap param1 = new HashMap<>();

			polling((HashMap) resultList.get(i));

		}

		

	}

	public void endScheduler() throws InterruptedException{
		if(pollingThread != null){
			
			pollingThread.interrupt();
			pollingThread.join();
			pollingThreadFlag = false;
		}

//		if(statusThread != null){
//			statusThread.interrupt();
//		}
	}

	public void polling(HashMap pollingParam){


		HashMap<String, String> hashMap = new HashMap<>();
		pollingParam.put("X-M2M-Origin", pollingParam.get("device_id") + "_origin");
		pollingParam.put("X-M2M-RI", pollingParam.get("device_id") + "_ri");
		pollingParam.put("Content-Type", "application/vnd.onem2m-res+json;");
		longpollingScheduler2 = new LongpollingScheduler();

//		longpollingScheduler2.setName("path_polling");
		pollingParam.put("ThreadType", "polling");
		pollingParam.put("pollingThread", "pollingThread");
		if(pollingParam.get("path_polling") != null){
			pollingParam.put("poolingurl", pollingParam.get("path_polling").toString());
			pollingThread = new HttpURLConnecterThread(pollingParam.get("path_polling").toString(), MethodTypeSelect.GET, pollingParam, null, longpollingScheduler2 ) ;
			
			pollingThread.start();
		}


	}

	public void status(HashMap pollingParam){


		HashMap<String, String> hashMap = new HashMap<>();
		pollingParam.put("X-M2M-Origin", pollingParam.get("device_id") + "_origin");
		pollingParam.put("X-M2M-RI", pollingParam.get("device_id") + "_ri");
		pollingParam.put("Content-Type", "application/vnd.onem2m-res+json;");
		LongpollingScheduler longpollingScheduler2 = new LongpollingScheduler();

		pollingParam.put("ThreadType", "status");

		if(pollingParam.get("path_command") != null){

			statusThread = new HttpURLConnecterThread(pollingParam.get("path_command").toString() + "/la", MethodTypeSelect.GET, pollingParam, null, longpollingScheduler2 ) ;
			statusThread.start();
			try {
				statusThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			statusThread.stop();
		}



	}

	public void pollingCallBack(String string, HashMap<String, String> param) {

		if(string != null){


			System.out.println(string);

			DocumentBuilderFactory t_dbf = null;
			DocumentBuilder t_db = null;
			Document t_doc = null;
			NodeList t_nodes1 = null;
			NodeList t_nodes2 = null;
			Element t_element = null;
			InputSource t_is = new InputSource();

			try
			{
				t_dbf = DocumentBuilderFactory.newInstance();
				t_db = t_dbf.newDocumentBuilder();
				t_is = new InputSource();
				t_is.setCharacterStream(new StringReader(string));
				t_doc = t_db.parse(t_is);
				t_nodes1 = t_doc.getElementsByTagName("con");
				t_nodes2 = t_doc.getElementsByTagName("ct");


				for (int i = 0, t_len = t_nodes1.getLength(); i < t_len; i ++){

					t_element = (Element)t_nodes1.item(i);
					param.put("response_value", t_element.getTextContent());

				}



				for (int i = 0, t_len = t_nodes2.getLength(); i < t_len; i ++){

					t_element = (Element)t_nodes2.item(i);
					param.put("response_date", t_element.getTextContent());

				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			//Status Update
			updateStatus(param);
		}
		

		//Restart job
		if(pollingThreadFlag){
			pollingThread = new HttpURLConnecterThread(param.get("poolingurl"), MethodTypeSelect.GET, param, null, longpollingScheduler2 ) ;
			pollingThread.start();
		}else{
			pollingThread.interrupt();
		}
		

	}

	public void statusCallBack(String string, HashMap<String, String> param) {


		System.out.println(string);


		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = null;
		JSONObject jsonObjectResult = new JSONObject();
		try {
			jsonObject = (JSONObject) jsonParser.parse(string);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		jsonObjectResult = (JSONObject) jsonObject.get("cin");

		if(jsonObjectResult.get("con") != null){
			param.put("response_value", jsonObjectResult.get("con").toString());
		}

		if(jsonObjectResult.get("ct") != null){
			param.put("response_date", jsonObjectResult.get("ct").toString());
		}

		//Status Update
		updateStatus(param);

	}

	public void updateStatus(HashMap<String, String> param){
		if(sqlMapper == null){
			sqlMapper = SQLManager.sqlMapper; 
		}
		try
		{	

			sqlMapper.startTransaction();

			sqlMapper.update(simulator1Update, param);		 

			sqlMapper.commitTransaction();


		}catch( Exception e )
		{
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

	@Override
	public void run() {
		System.out.println("Thread ID="+Thread.currentThread().getName()+"  task name=Longpolling start");
		if(sqlMapper == null){
			sqlMapper 			= SQLManager.sqlMapper;
		}

		try {
			resultList = new ArrayList<>();
			resultList = (ArrayList)sqlMapper.queryForList(simulatorSelect,null);
			for(int i = 0; i < resultList.size(); i ++){

			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

//			for(int i = 0 ; i < resultList.size(); i ++ ){
//				HashMap param1 = new HashMap<>();
//
//				status((HashMap) resultList.get(i));
//
//			}


		for(int i = 0 ; i < resultList.size(); i ++ ){
			HashMap param1 = new HashMap<>();

			polling((HashMap) resultList.get(i));

		}
		pollingThreadFlag = true;
		
	}

}
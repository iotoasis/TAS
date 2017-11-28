
package com.connecter;

import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.common.Base64Convertor;
import com.common.SQLManager;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.onem2m.Onem2mPost;
import com.service.utile.Onem2mThingsTypeSelect;

@SuppressWarnings("unchecked")
public class RuleenginConnector {
	String rulesensordevice = "rule-sql.rulesensordevice";
	String rulemstSelect = "rule-sql.rulemstSelect";
	String ruleinfoSelect = "rule-sql.ruleinfoSelect";
	String ruleUpdate = "rule-sql.ruleUpdate";
	String ruleNameSelect = "rule-sql.ruleNameSelect";
	String ruleIdSelect = "rule-sql.ruleIdSelect";
	String rulemstStatus = "rule-sql.rulemstStatus";
	String ruleinfoUpdate1 = "rule-sql.ruleinfoUpdate1";
	String ruleserver = "rule-sql.ruleserver";
	String ruleresult = "rule-sql.ruleresult";
	String Updateruleresult = "rule-sql.Updateruleresult";
	
	SqlMapClient sqlMapper = null;

	Onem2mPost onem2mPost = null;
	
	public  static  Logger tasLog = Logger.getRootLogger();

	private Base64Convertor base64Convertor = new Base64Convertor();
	
	Pattern urlPattern = Pattern.compile("^(https?):\\/\\/([^:\\/\\s]+)(:([^\\/]*))?((\\/[^\\s/\\/]+)*)?\\/([^#\\s\\?]*)(\\?([^#\\s]*))?(#(\\w*))?$");

	HashMap headerParamHashmap = null;

	String orderString = "";

	JSONObject bodyJsonObject = null;

	StringBuilder buf = null;

	String jsonStr = "";
	
	HashMap serverInfo = null;
	
	public RuleenginConnector(HashMap headerParamHashmap, String orderString, JSONObject bodyJsonObject, StringBuilder buf, String jsonStr){
		this.headerParamHashmap = headerParamHashmap;
		this.orderString = orderString;
		this.bodyJsonObject = bodyJsonObject;
		this.buf = buf;
		this.jsonStr = jsonStr;

	}

	/*
	noti 가 왔을 경우
	1. xml 파싱
	
	2. 항목 추출
	
	3. cr(ae명?) 항목으로 tb_rule_info 테이블 조회
	
	4-1. 해당 조건이 만족되는 지 확인
	4-2. 해당 항목이 속한 상황의 발생 여부 확인
	
	5-1-1. 해당 조건 성립 + 상황 발생 = SKIP
	5-1-2. 해당 조건 비성립 + 상황 발생 + 상황에 속한 항목들 모두 계산 후 성립 = SKIP
	5-1-3. 해당 조건 비성립 + 상황 발생 + 상황에 속한 항목들 모두 계산 후 비성립 = 미발생으로 상황업데이트
	5-2-1. 해당 조건 성립 + 상황 미발생 + 상황에 속한 항목들 모두 계산 후 성립 = 발생으로 상황 업데이트
	5-2-2. 해당 조건 성립 + 상황 미발생 + 상황에 속한 항목들 모두 계산 후 비성립 = SKIP
	5-2-3. 해당 조건 비성립 + 상황 미발생 = SKIP	
	*/
	
	
	public StringBuilder getData(){
		HashMap<String, String> hashMap = new HashMap<>();
		hashMap = getXml();
			
			buf.append("{");
			buf.append("\"result\": \"" + "S" + "uccess" + "\"");
			buf.append("}");

		return buf;

	}
	
	private HashMap<String, String> getXml(){
		ruleserver();
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
			HashMap param = null;
			t_dbf = DocumentBuilderFactory.newInstance();
			t_db = t_dbf.newDocumentBuilder();
			t_is = new InputSource();
			t_is.setCharacterStream(new StringReader(jsonStr));
			t_doc = t_db.parse(t_is);
			t_nodes1 = t_doc.getElementsByTagName("con");
			t_nodes2 = t_doc.getElementsByTagName("sur");
			t_nodes3 = t_doc.getElementsByTagName("cnf");
			
			for (int i = 0, t_len = t_nodes1.getLength(); i < t_len; i ++){

				param = new HashMap<>();
				
				//xml parsing
				t_element = (Element)t_nodes1.item(i);
				t_element2 = (Element)t_nodes2.item(i);
				t_element3 = (Element)t_nodes3.item(i);
				
				//String parsing
				String conString;
				
				if(t_element3.getTextContent().equals("application/json:1")){

					conString = base64Convertor.decrypt(t_element.getTextContent());
					
					
					JSONParser jsonParser = new JSONParser();
					JSONObject jsonResultObject = new JSONObject();
					try {
						jsonResultObject = (JSONObject) jsonParser.parse(conString);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					conString = (String) jsonResultObject.get("data");
				}else{
					
					conString = t_element.getTextContent();
				}
				
				
				String surString = t_element2.getTextContent();
				surString = surString.replace("/" + serverInfo.get("in_cse") + "/", "");
				surString = surString.replace("/subscription", "");

				//parameter Setting
				String[] deviceId = surString.split("/");
				param.put("sensor_device_id", deviceId[0]);
				param.put("Instance_rn", deviceId[2]);
				param.put("url", deviceId[0] + "/" + deviceId[1] + "/" + deviceId[2]); 
				//DB 조회
				
				ArrayList ruleNameSelectList = ruleNameSelect(param);
				//조건 성립 확인
				
				if(ruleNameSelectList.size() > 0){
					param.put("triger_rule", conString);
					
					
					for(int ii = 0; ii < ruleNameSelectList.size(); ii ++){
						
						//get list
						String oldConString = conString;
						String newConString = null;
						String signString = null;
						String rulemstid = null;
						String statusString = null;
						String statusnameString = null;
						boolean calculationFlag = false;
						
						HashMap ruleNameSelectListHashMap = new HashMap<>();
						ruleNameSelectListHashMap = (HashMap) ruleNameSelectList.get(ii);
												
						if(ruleNameSelectListHashMap != null && ruleNameSelectListHashMap.get("triger_value") != null){
							newConString = ruleNameSelectListHashMap.get("triger_rule").toString();
						}
						if(ruleNameSelectListHashMap != null && ruleNameSelectListHashMap.get("triger_sign") != null){
							signString = ruleNameSelectListHashMap.get("triger_sign").toString();
						}
						
						int oldConInteger = Integer.parseInt(oldConString);
						int newConInteger = Integer.parseInt(newConString);
						//등호 계산
						if(signString.equals(">") && oldConInteger > newConInteger){
							calculationFlag = true;
						}else if(signString.equals("<") && oldConInteger < newConInteger){
								calculationFlag = true;
						}else if(signString.equals(">=") && oldConInteger >= newConInteger){
								calculationFlag = true;
						}else if(signString.equals("<=") && oldConInteger <= newConInteger){
								calculationFlag = true;
						}else if(signString.equals("=") && oldConInteger == newConInteger){
								calculationFlag = true;
						}
						//조건 성립
						if(calculationFlag){
							//rule_mst_id 가져오기
							param.put("rule_mst_id", ruleNameSelectListHashMap.get("rule_mst_id").toString());
							param.put("triger_value", oldConInteger);
							ruleinfoUpdate1(param);
							//rule_mst_id 확인
							ArrayList ruleIdSelectList = new ArrayList<>();
							ruleIdSelectList = ruleIdSelect(param);
							ArrayList rulemstSelectList = new ArrayList<>();
							param.remove("rule_value");
							rulemstSelectList = rulemstSelect(param);
							int occurInterger = 0;
//							for(int j = 0; j < ruleIdSelectList.size(); j++){
								HashMap ruleIdSelectListListHashMap = new HashMap<>();
								ruleIdSelectListListHashMap = (HashMap) ruleIdSelectList.get(0);
							
								//상황 확인
								for(int iii=0; iii < rulemstSelectList.size(); iii++){
									String ruleStatus = null;
									
									HashMap rulemstSelectListHashMap = new HashMap<>();
									rulemstSelectListHashMap = (HashMap) rulemstSelectList.get(iii);
									
									//rule_mst_id 상황확인
									if(rulemstSelectListHashMap.get("rule_mst_id").toString().equals(ruleIdSelectListListHashMap.get("rule_mst_id").toString())){
										ruleStatus = rulemstSelectListHashMap.get("rule_status").toString();
										//상황발생 확인
										if(ruleStatus != null && ruleStatus.equals("True")){
											//skip
											if(getSignResult(ruleIdSelectList, param)){
												param.put("rule_value", "on");
												ruleUpdate(param);
												System.out.println("상황발생");
												rulesensordevice(param);
											}else{
												
												param.put("rule_status", "False");
												param.put("rule_value", "on");
												ruleUpdate(param);
												System.out.println("상황미발생");
											}
										}
										//상황 미발생 False
										else{
											//상황에 속한 모든 항목 계산
											if(getSignResult(ruleIdSelectList, param)){
												param.put("rule_status", "True");
												param.put("rule_value", "on");
												
												rulesensordevice(param);
												System.out.println("상황발생");
											}else{
												param.put("rule_value", "on");
												ruleUpdate(param);
												System.out.println("상황미발생");
											}

										}
									}
								}
							
						}
						//조건 미성립
						else{
							//rule_mst_id 가져오기
							param.put("rule_mst_id", ruleNameSelectListHashMap.get("rule_mst_id").toString());
							param.put("triger_value", oldConInteger);
							ruleinfoUpdate1(param);
							//rule_mst_id 확인
							ArrayList ruleIdSelectList = ruleIdSelect(param);
							param.remove("rule_value");
							ArrayList rulemstSelectList = rulemstSelect(param);
							int occurInterger = 0;
//							for(int j = 0; j < ruleIdSelectList.size(); j++){
								HashMap ruleIdSelectListListHashMap = new HashMap<>();
								ruleIdSelectListListHashMap = (HashMap) ruleIdSelectList.get(0);
							
								//상황 확인
								for(int iii=0; iii < rulemstSelectList.size(); iii++){
									String ruleStatus = null;
									
									HashMap rulemstSelectListHashMap = new HashMap<>();
									rulemstSelectListHashMap = (HashMap) rulemstSelectList.get(iii);
									
									//rule_mst_id 상황확인
									if(rulemstSelectListHashMap.get("rule_mst_id").toString().equals(ruleIdSelectListListHashMap.get("rule_mst_id").toString())){
										ruleStatus = rulemstSelectListHashMap.get("rule_status").toString();
										//상황 발생 확인
										if(ruleStatus != null && ruleStatus.equals("True")){
											//상황에 속한 모든 항목 계산
											if(getSignResult(ruleIdSelectList, param)){
												param.put("rule_value", "on");
												ruleUpdate(param);
												rulesensordevice(param);
												System.out.println("상황발생");
											}else{
												
												param.put("rule_status", "False");
												param.put("rule_value", "on");
												
												ruleUpdate(param);
												System.out.println("상황미발생");
											}
										}
										//상황 미발생
										else{
											if(getSignResult(ruleIdSelectList, param)){
												param.put("rule_status", "True");
												param.put("rule_value", "on");
												rulesensordevice(param);
												System.out.println("상황발생");
											}else{
												param.put("rule_value", "on");
												ruleUpdate(param);
												System.out.println("상황미발생");
											}
											
										}
									}
								}
//							}
							
							
						}
						
					}
					
				}
				
			}
		}
	
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		if(sqlMapper != null){
			try {
				sqlMapper.endTransaction();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return bodyJsonObject;
		
	}
	
	@SuppressWarnings("unchecked")
	private void rulesensordevice(HashMap param){
		ArrayList resultList = null;
		if(sqlMapper == null){
			sqlMapper 			= SQLManager.sqlMapper;
		}

		try {
			resultList = new ArrayList<>();
			resultList = (ArrayList)sqlMapper.queryForList(ruleresult,param);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		Onem2mPost onem2mPost = new Onem2mPost();
		
		for(int i = 0; i< resultList.size(); i ++){
			
			HashMap postParam = new HashMap<>();
			
			postParam = (HashMap) resultList.get(i);
			
			String deviceIdString = param.get("url").toString();
			String[] deviceId = deviceIdString.split("/");
			
			postParam.put("ae", deviceId[0]);
			postParam.put("Instance_rn", deviceId[2]);
			postParam.put("conString", postParam.get("triger_result").toString());
			
			
			
			String url = "http://" + serverInfo.get("ip") + ":" + serverInfo.get("port") + "/" + serverInfo.get("in_cse") + "/" + postParam.get("url").toString();
			try {
				onem2mPost.setData(url , Onem2mThingsTypeSelect.INSTANCE, postParam, null, null);
				onem2mPost.getData();
				onem2mPost.join();
				postParam.put("triger_result_status", "Success");
				Updateruleresult(postParam, param);
				
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			sqlMapper.endTransaction();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
		
	private boolean getSignResult(ArrayList ruleIdSelectList, HashMap param){
		
		String triger_rule = null; 
		String triger_value = null;
		String triger_sign = null;
		String triger_operator = null;
		boolean oldResultBoolean = true;
		boolean newResultBoolean = false;
		boolean rule = true;
		for(int i = 0; i < ruleIdSelectList.size(); i++){
			
			HashMap hashMap = new HashMap<>();
			hashMap = (HashMap) ruleIdSelectList.get(i);
			
			triger_rule = (String) hashMap.get("triger_rule");
			triger_value = (String) param.get("triger_rule").toString();
			triger_sign = (String) hashMap.get("triger_sign");
			
						
			if(hashMap.get("url").toString().equals(param.get("url").toString())){
				triger_value = param.get("triger_rule").toString();
			}
			//null 처리
			if(triger_rule == null || triger_rule.equals("")){
				triger_rule = "0";
			}
			if(triger_value == null || triger_value.equals("")){
				triger_value = "0";
			}
			if(triger_sign == null || triger_sign.equals("")){
				triger_sign = "0";
			}
			
			
//			newResultBoolean = getTrigerResult(Integer.parseInt(triger_value), Integer.parseInt(triger_rule), triger_sign);
			
			if(i != 0){
				HashMap oldHashMap = new HashMap<>();
				oldHashMap = (HashMap) ruleIdSelectList.get(i-1);
				triger_operator = (String) oldHashMap.get("triger_operator");
				if(triger_operator == null){
					triger_operator = "";
				}
				boolean value = getTrigerResult( Integer.parseInt(triger_value),  Integer.parseInt(triger_rule), triger_sign);
				oldResultBoolean = getTrigerResultInteger(rule, value, triger_operator);
				rule = oldResultBoolean;
			//	newResultBoolean = oldResultBoolean;
			}else{
				rule = getTrigerResult( Integer.parseInt(triger_value),  Integer.parseInt(triger_rule), triger_sign);
				oldResultBoolean = rule;
			}
			
		}
			return oldResultBoolean;
		
	}
	
	private boolean getTrigerResultInteger(boolean oldConBoolean, boolean newConBoolean, String signString){
		
		if(signString.equals("AND")){
			if(oldConBoolean && newConBoolean){
				return true;
			}else{
				return false;
			}
		}
		else if(signString.equals("OR")){
			if(oldConBoolean || newConBoolean){
				return true;
			}else{
				return false;
			}
		}
		else if(signString.equals("")){
			return true;
		}
		else{
			return false;
		}
	}
	
	private boolean getTrigerResult(int oldConInteger, int newConInteger, String signString){
		if(signString.equals(">")){
			if(oldConInteger > newConInteger){
				return true;
			}else{
				return false;
			}
		}else if(signString.equals("<")){
			if(oldConInteger < newConInteger){
				return true;
			}
			else{
				return false;
			}
		}else if(signString.equals(">=")){
			if(oldConInteger >= newConInteger){
				return true;
			}
			else{
				return false;
			}
		}else if(signString.equals("<=")){
			if(oldConInteger <= newConInteger){
				return true;
			}
			else{
				return false;
			}
		}else if(signString.equals("=")){
			if(oldConInteger == newConInteger){
				return true;
			}
			else{
				return false;
			}
		}else{
			return false;
		}
		
		
		
	}
	

	
	private ArrayList<String> rulemstSelect(HashMap<String, String> param){
		ArrayList resultList = null;
		
		if(sqlMapper == null){
			sqlMapper 			= SQLManager.sqlMapper;
		}

		try {
			resultList = new ArrayList<>();
			resultList = (ArrayList)sqlMapper.queryForList(rulemstSelect,param);

		} catch (SQLException e1) {
			e1.printStackTrace();
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
		
		return resultList;
		
	}
	
	private ArrayList<String> ruleinfoSelect(HashMap<String, String> param){
		ArrayList resultList = null;
		
		if(sqlMapper == null){
			sqlMapper 			= SQLManager.sqlMapper;
		}

		try {
			resultList = new ArrayList<>();
			resultList = (ArrayList)sqlMapper.queryForList(ruleinfoSelect,param);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}finally
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
		
		return resultList;
		
	}
	
	
	private ArrayList<String> ruleNameSelect(HashMap<String, String> param){
		ArrayList resultList = null;
		
		if(sqlMapper == null){
			sqlMapper 			= SQLManager.sqlMapper;
		}

		try {
			resultList = new ArrayList<>();
			resultList = (ArrayList)sqlMapper.queryForList(ruleNameSelect,param);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}finally
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
		
		return resultList;
		
	}
	
	private ArrayList<String> rulemstStatus(HashMap<String, String> param){
		ArrayList resultList = null;
		
		if(sqlMapper == null){
			sqlMapper 			= SQLManager.sqlMapper;
		}

		try {
			resultList = new ArrayList<>();
			resultList = (ArrayList)sqlMapper.queryForList(rulemstStatus,param);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}finally
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
		
		return resultList;
		
	}
	
	private ArrayList<String> ruleIdSelect(HashMap<String, String> param){
		ArrayList resultList = null;
		
		if(sqlMapper == null){
			sqlMapper 			= SQLManager.sqlMapper;
		}

		try {
			resultList = new ArrayList<>();
			resultList = (ArrayList)sqlMapper.queryForList(ruleIdSelect,param);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}finally
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
		
		return resultList;
		
	}
	
	private void ruleUpdate(HashMap<String, String> param){
		if(sqlMapper == null){
			sqlMapper = SQLManager.sqlMapper; 
		}
		try
		{	

			sqlMapper.startTransaction();

			sqlMapper.update(ruleUpdate, param);		 

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
	private void ruleinfoUpdate1(HashMap<String, String> param){
		if(sqlMapper == null){
			sqlMapper = SQLManager.sqlMapper; 
		}
		try
		{	

			sqlMapper.startTransaction();

			sqlMapper.update(ruleinfoUpdate1, param);		 

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
	
	private void Updateruleresult(HashMap<String, String> postParam, HashMap<String, String> param){
		if(sqlMapper == null){
			sqlMapper = SQLManager.sqlMapper; 
		}
		try
		{	

			sqlMapper.startTransaction();

			sqlMapper.update(Updateruleresult, postParam);		 

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

		ruleUpdate(param);
	}
	
	private void ruleserver(){
		ArrayList arrayList = new ArrayList<>();
		
		if(sqlMapper == null){
			sqlMapper 			= SQLManager.sqlMapper;
		}

		try {
			arrayList = new ArrayList<>();
			arrayList = (ArrayList)sqlMapper.queryForList(ruleserver,null);
			serverInfo = (HashMap) arrayList.get(0);
		} catch (SQLException e1) {
			e1.printStackTrace();
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

}

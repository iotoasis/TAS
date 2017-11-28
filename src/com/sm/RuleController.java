package com.sm;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.common.ArrayToJsonArrayParser;
import com.common.SQLManager;
import com.http.HttpURLConnecter;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.onem2m.Onem2mGet;


@Controller
public class RuleController {
	String rule = "rule-sql.rule";
	String ruleinforesult = "rule-sql.ruleinforesult";
	String ruleDelete = "rule-sql.ruleDelete";
	String ruleInsert = "rule-sql.ruleInsert";
	String ruleUpdate ="rule-sql.ruleUpdate";
	String ruleserver ="rule-sql.ruleserver";
	String ruleValueUpdate ="rule-sql.ruleValueUpdate";
	String ruleserverUpdate = "rule-sql.ruleserverUpdate";
	String ruleinfo = "rule-sql.ruleinfo";
	String ruleinfoInsert = "rule-sql.ruleinfoInsert";
	String ruleinfoDelete = "rule-sql.ruleinfoDelete";
	String ruleinfoUpdate = "rule-sql.ruleinfoUpdate";
	String ruleresult = "rule-sql.ruleresult";
	String ruleresultInsert = "rule-sql.ruleresultInsert";
	String ruleresultDelete = "rule-sql.ruleresultDelete";
	String Updaterulemst = "rule-sql.Updaterulemst";
	String Updateruleinfo = "rule-sql.Updateruleinfo";
	String Updateruleresult = "rule-sql.Updateruleresult";
	String rulesensordevice = "rule-sql.rulesensordevice";
	String rulesensordeviceInsert = "rule-sql.rulesensordeviceInsert";
	/*
	 * 
	 * 새로 작성
	 * 
	 */
	String ruleTotalSelect = "rule-sql.ruleTotalSelect";
	String ruleSelect = "rule-sql.ruleSelect";
	
	HttpURLConnecter connecter = null;
	SqlMapClient 			sqlMapper 			= null;
	public static Logger clsLogg = Logger.getRootLogger();

	/**
	 * @author 최인규
	 * 
	 * @date 2016-11-10 최초작성
	 * 
	 * @updateDate
	 * 
	 * @comment 키오스크 메뉴 조회
	 */

	@RequestMapping("rule.do")
	public ModelAndView rule(@RequestParam Map<String,String> param, HttpServletRequest httReqt, HttpSession session) throws Exception
	{

		if(sqlMapper == null){
			sqlMapper = SQLManager.sqlMapper; 
		}

		ModelAndView mavRetn = new ModelAndView();

		ArrayList resultList = new ArrayList<>();


		resultList = (ArrayList)sqlMapper.queryForList(rule,param);


		ArrayToJsonArrayParser array = new ArrayToJsonArrayParser();


		JSONObject totalObject = new JSONObject();

		totalObject.put("rows", array.getJsonArray(resultList));

		mavRetn.addObject("jsonData", totalObject);
		mavRetn.setViewName("/jsp/common/jsonJsp");

		return mavRetn;


	}
	
	/**
	 * @author 최인규
	 * 
	 * @date 2016-12-15 최초작성
	 * 
	 * @updateDate
	 * 
	 * @comment 
	 * 
	 * @service 스케줄 삭제
	 * 
	 */
	
	@RequestMapping("ruleinforesult.do")
	public ModelAndView ruleinforesult(@RequestParam Map<String,String> param, HttpServletRequest httReqt, HttpSession session) throws Exception
	{

		if(sqlMapper == null){
			sqlMapper = SQLManager.sqlMapper; 
		}

		ModelAndView mavRetn = new ModelAndView();

		ArrayList resultList = new ArrayList<>();


		resultList = (ArrayList)sqlMapper.queryForList(ruleinforesult,param);


		ArrayToJsonArrayParser array = new ArrayToJsonArrayParser();


		JSONObject totalObject = new JSONObject();

		totalObject.put("rows", array.getJsonArray(resultList));

		mavRetn.addObject("jsonData", totalObject);
		mavRetn.setViewName("/jsp/common/jsonJsp");

		return mavRetn;


	}
	

	@RequestMapping("ruleDelete.do")
	public ModelAndView ruleDelete(@RequestParam Map<String,String> param, HttpServletRequest httReqt, HttpSession session) throws Exception
	{
		if(sqlMapper == null){
			sqlMapper = SQLManager.sqlMapper;
		}

		ModelAndView mavRetn = new ModelAndView();

		JSONObject totalObject = new JSONObject();

		try
		{	
			sqlMapper.startTransaction();

			sqlMapper.insert(ruleDelete, param);		 

			sqlMapper.commitTransaction();

			totalObject.put("result", "Success");
		}catch( Exception e )
		{
			totalObject.put("result", "Fail");
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

		mavRetn.addObject("jsonData", totalObject);
		mavRetn.setViewName("/jsp/common/jsonJsp");

		return mavRetn;
	}
	
	@RequestMapping("ruleInsert.do")
	public ModelAndView ruleInsert(@RequestParam Map<String,String> param, HttpServletRequest httReqt, HttpSession session) throws Exception
	{
		if(sqlMapper == null){
			sqlMapper = SQLManager.sqlMapper;
		}

		ModelAndView mavRetn = new ModelAndView();

		JSONObject totalObject = new JSONObject();

		try
		{	
			sqlMapper.startTransaction();

			sqlMapper.insert(ruleInsert, param);		 

			sqlMapper.commitTransaction();
			totalObject.put("result", "Success");
		}catch( Exception e )
		{
			totalObject.put("result", "Fail");
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

		mavRetn.addObject("jsonData", totalObject);
		mavRetn.setViewName("/jsp/common/jsonJsp");

		return mavRetn;
	}
	
	@RequestMapping("ruleUpdate.do")
	public ModelAndView ruleUpdate(@RequestParam Map<String,String> param, HttpServletRequest httReqt, HttpSession session) throws Exception
	{
		if(sqlMapper == null){
			sqlMapper = SQLManager.sqlMapper;
		}
		
		ModelAndView mavRetn = new ModelAndView();
		
		//Create JsonObject
		JSONArray array = new JSONArray();

		ArrayList UserList = new ArrayList<>();
		
		JSONObject totalObject = new JSONObject();
		
		try
		{	
			sqlMapper.startTransaction();

			sqlMapper.update(ruleUpdate, param);		 

			sqlMapper.commitTransaction();
			
			totalObject.put("result", "Success");
			
		}catch( Exception e )
		{
			
			totalObject.put("result", "Fail");
			
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
		
		
		mavRetn.addObject("jsonData", totalObject);
		mavRetn.setViewName("/jsp/common/jsonJsp");
		
		return mavRetn;
	}
	
	@RequestMapping("ruleserver.do")
	public ModelAndView ruleserver(@RequestParam Map<String,String> param, HttpServletRequest httReqt, HttpSession session) throws Exception
	{

		if(sqlMapper == null){
			sqlMapper = SQLManager.sqlMapper; 
		}

		ModelAndView mavRetn = new ModelAndView();

		ArrayList resultList = new ArrayList<>();


		resultList = (ArrayList)sqlMapper.queryForList(ruleserver,param);


		ArrayToJsonArrayParser array = new ArrayToJsonArrayParser();


		JSONObject totalObject = new JSONObject();

		totalObject.put("rows", array.getJsonArray(resultList));

		mavRetn.addObject("jsonData", totalObject);
		mavRetn.setViewName("/jsp/common/jsonJsp");

		return mavRetn;


	}
	
	@RequestMapping("ruleValueUpdate.do")
	public ModelAndView ruleValueUpdate(@RequestParam Map<String,String> param, HttpServletRequest httReqt, HttpSession session) throws Exception
	{
		if(sqlMapper == null){
			sqlMapper = SQLManager.sqlMapper;
		}
		
		ModelAndView mavRetn = new ModelAndView();
		
		//Create JsonObject
		JSONArray array = new JSONArray();

		ArrayList UserList = new ArrayList<>();
		
		JSONObject totalObject = new JSONObject();
		
		try
		{	
			sqlMapper.startTransaction();

			sqlMapper.update(ruleValueUpdate, param);		 

			sqlMapper.commitTransaction();
			
			totalObject.put("result", "Success");
			
		}catch( Exception e )
		{
			
			totalObject.put("result", "Fail");
			
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
		
		
		mavRetn.addObject("jsonData", totalObject);
		mavRetn.setViewName("/jsp/common/jsonJsp");
		
		return mavRetn;
	}
	
	@RequestMapping("ruleserverUpdate.do")
	public ModelAndView ruleserverUpdate(@RequestParam Map<String,String> param, HttpServletRequest httReqt, HttpSession session) throws Exception
	{
		if(sqlMapper == null){
			sqlMapper = SQLManager.sqlMapper;
		}
		
		ModelAndView mavRetn = new ModelAndView();
		
		//Create JsonObject
		JSONArray array = new JSONArray();

		ArrayList UserList = new ArrayList<>();
		
		JSONObject totalObject = new JSONObject();
		
		try
		{	
			sqlMapper.startTransaction();

			sqlMapper.update(ruleserverUpdate, param);		 

			sqlMapper.commitTransaction();
			
			totalObject.put("result", "Success");
			
		}catch( Exception e )
		{
			
			totalObject.put("result", "Fail");
			
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
		
		
		mavRetn.addObject("jsonData", totalObject);
		mavRetn.setViewName("/jsp/common/jsonJsp");
		
		return mavRetn;
	}
	
	@RequestMapping("ruleinfo.do")
	public ModelAndView ruleinfo(@RequestParam Map<String,String> param, HttpServletRequest httReqt, HttpSession session) throws Exception
	{

		if(sqlMapper == null){
			sqlMapper = SQLManager.sqlMapper; 
		}

		ModelAndView mavRetn = new ModelAndView();

		ArrayList resultList = new ArrayList<>();


		resultList = (ArrayList)sqlMapper.queryForList(ruleinfo,param);


		ArrayToJsonArrayParser array = new ArrayToJsonArrayParser();


		JSONObject totalObject = new JSONObject();

		totalObject.put("rows", array.getJsonArray(resultList));

		mavRetn.addObject("jsonData", totalObject);
		mavRetn.setViewName("/jsp/common/jsonJsp");

		return mavRetn;


	}
	
	@RequestMapping("ruleinfoInsert.do")
	public ModelAndView ruleinfoInsert(@RequestParam Map<String,String> param, HttpServletRequest httReqt, HttpSession session) throws Exception
	{
		if(sqlMapper == null){
			sqlMapper = SQLManager.sqlMapper;
		}

		ModelAndView mavRetn = new ModelAndView();

		JSONObject totalObject = new JSONObject();

		try
		{	
			sqlMapper.startTransaction();

			sqlMapper.insert(ruleinfoInsert, param);		 

			sqlMapper.commitTransaction();
			totalObject.put("result", "Success");
		}catch( Exception e )
		{
			totalObject.put("result", "Fail");
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

		mavRetn.addObject("jsonData", totalObject);
		mavRetn.setViewName("/jsp/common/jsonJsp");

		return mavRetn;
	}
	
	@RequestMapping("ruleinfoDelete.do")
	public ModelAndView ruleinfoDelete(@RequestParam Map<String,String> param, HttpServletRequest httReqt, HttpSession session) throws Exception
	{
		if(sqlMapper == null){
			sqlMapper = SQLManager.sqlMapper;
		}

		ModelAndView mavRetn = new ModelAndView();

		JSONObject totalObject = new JSONObject();

		try
		{	
			sqlMapper.startTransaction();

			sqlMapper.insert(ruleinfoDelete, param);		 

			sqlMapper.commitTransaction();

			totalObject.put("result", "Success");
		}catch( Exception e )
		{
			totalObject.put("result", "Fail");
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

		mavRetn.addObject("jsonData", totalObject);
		mavRetn.setViewName("/jsp/common/jsonJsp");

		return mavRetn;
	}
	
	@RequestMapping("ruleinfoUpdate.do")
	public ModelAndView ruleinfoUpdate(@RequestParam Map<String,String> param, HttpServletRequest httReqt, HttpSession session) throws Exception
	{
		if(sqlMapper == null){
			sqlMapper = SQLManager.sqlMapper;
		}
		
		ModelAndView mavRetn = new ModelAndView();
		
		//Create JsonObject
		JSONArray array = new JSONArray();

		ArrayList UserList = new ArrayList<>();
		
		JSONObject totalObject = new JSONObject();
		
		try
		{	
			sqlMapper.startTransaction();

			sqlMapper.update(ruleinfoUpdate, param);		 

			sqlMapper.commitTransaction();
			
			totalObject.put("result", "Success");
			
		}catch( Exception e )
		{
			
			totalObject.put("result", "Fail");
			
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
		
		
		mavRetn.addObject("jsonData", totalObject);
		mavRetn.setViewName("/jsp/common/jsonJsp");
		
		return mavRetn;
	}
	
	
	@RequestMapping("ruleresult.do")
	public ModelAndView ruleresult(@RequestParam Map<String,String> param, HttpServletRequest httReqt, HttpSession session) throws Exception
	{

		if(sqlMapper == null){
			sqlMapper = SQLManager.sqlMapper; 
		}

		ModelAndView mavRetn = new ModelAndView();

		ArrayList resultList = new ArrayList<>();


		resultList = (ArrayList)sqlMapper.queryForList(ruleresult,param);


		ArrayToJsonArrayParser array = new ArrayToJsonArrayParser();


		JSONObject totalObject = new JSONObject();

		totalObject.put("rows", array.getJsonArray(resultList));

		mavRetn.addObject("jsonData", totalObject);
		mavRetn.setViewName("/jsp/common/jsonJsp");

		return mavRetn;


	}
	
	@RequestMapping("ruleresultInsert.do")
	public ModelAndView ruleresultInsert(@RequestParam Map<String,String> param, HttpServletRequest httReqt, HttpSession session) throws Exception
	{
		if(sqlMapper == null){
			sqlMapper = SQLManager.sqlMapper;
		}

		ModelAndView mavRetn = new ModelAndView();

		JSONObject totalObject = new JSONObject();

		try
		{	
			sqlMapper.startTransaction();

			sqlMapper.insert(ruleresultInsert, param);		 

			sqlMapper.commitTransaction();
			totalObject.put("result", "Success");
		}catch( Exception e )
		{
			totalObject.put("result", "Fail");
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

		mavRetn.addObject("jsonData", totalObject);
		mavRetn.setViewName("/jsp/common/jsonJsp");

		return mavRetn;
	}
	
	@RequestMapping("ruleresultDelete.do")
	public ModelAndView ruleresultDelete(@RequestParam Map<String,String> param, HttpServletRequest httReqt, HttpSession session) throws Exception
	{
		if(sqlMapper == null){
			sqlMapper = SQLManager.sqlMapper;
		}

		ModelAndView mavRetn = new ModelAndView();

		JSONObject totalObject = new JSONObject();

		try
		{	
			sqlMapper.startTransaction();

			sqlMapper.insert(ruleresultDelete, param);		 

			sqlMapper.commitTransaction();

			totalObject.put("result", "Success");
		}catch( Exception e )
		{
			totalObject.put("result", "Fail");
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

		mavRetn.addObject("jsonData", totalObject);
		mavRetn.setViewName("/jsp/common/jsonJsp");

		return mavRetn;
	}
	
	
	@RequestMapping("Updaterulemst.do")
	public ModelAndView Updaterulemst(@RequestParam Map<String,String> param, HttpServletRequest httReqt, HttpSession session) throws Exception
	{
		if(sqlMapper == null){
			sqlMapper = SQLManager.sqlMapper;
		}
		
		ModelAndView mavRetn = new ModelAndView();
		
		//Create JsonObject
		JSONArray array = new JSONArray();

		ArrayList UserList = new ArrayList<>();
		
		JSONObject totalObject = new JSONObject();
		
		try
		{	
			sqlMapper.startTransaction();

			sqlMapper.update(Updaterulemst, param);		 

			sqlMapper.commitTransaction();
			
			totalObject.put("result", "Success");
			
		}catch( Exception e )
		{
			
			totalObject.put("result", "Fail");
			
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
		
		
		mavRetn.addObject("jsonData", totalObject);
		mavRetn.setViewName("/jsp/common/jsonJsp");
		
		return mavRetn;
	}
	
	@RequestMapping("Updateruleinfo.do")
	public ModelAndView Updateruleinfo(@RequestParam Map<String,String> param, HttpServletRequest httReqt, HttpSession session) throws Exception
	{
		if(sqlMapper == null){
			sqlMapper = SQLManager.sqlMapper;
		}
		
		ModelAndView mavRetn = new ModelAndView();
		
		//Create JsonObject
		JSONArray array = new JSONArray();

		ArrayList UserList = new ArrayList<>();
		
		JSONObject totalObject = new JSONObject();
		
		try
		{	
			sqlMapper.startTransaction();

			sqlMapper.update(Updateruleinfo, param);		 

			sqlMapper.commitTransaction();
			
			totalObject.put("result", "Success");
			
		}catch( Exception e )
		{
			
			totalObject.put("result", "Fail");
			
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
		
		
		mavRetn.addObject("jsonData", totalObject);
		mavRetn.setViewName("/jsp/common/jsonJsp");
		
		return mavRetn;
	}
	
	@RequestMapping("Updateruleresult.do")
	public ModelAndView Updateruleresult(@RequestParam Map<String,String> param, HttpServletRequest httReqt, HttpSession session) throws Exception
	{
		if(sqlMapper == null){
			sqlMapper = SQLManager.sqlMapper;
		}
		
		ModelAndView mavRetn = new ModelAndView();
		
		//Create JsonObject
		JSONArray array = new JSONArray();

		ArrayList UserList = new ArrayList<>();
		
		JSONObject totalObject = new JSONObject();
		
		try
		{	
			sqlMapper.startTransaction();

			sqlMapper.update(Updateruleresult, param);		 

			sqlMapper.commitTransaction();
			
			totalObject.put("result", "Success");
			
		}catch( Exception e )
		{
			
			totalObject.put("result", "Fail");
			
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
		
		
		mavRetn.addObject("jsonData", totalObject);
		mavRetn.setViewName("/jsp/common/jsonJsp");
		
		return mavRetn;
	}
	
	@RequestMapping("rulesensordevice.do")
	public ModelAndView rulesensordevice(@RequestParam Map<String,String> param, HttpServletRequest httReqt, HttpSession session) throws Exception
	{

		if(sqlMapper == null){
			sqlMapper = SQLManager.sqlMapper; 
		}

		ModelAndView mavRetn = new ModelAndView();

		ArrayList resultList = new ArrayList<>();


		resultList = (ArrayList)sqlMapper.queryForList(rulesensordevice,param);


		ArrayToJsonArrayParser array = new ArrayToJsonArrayParser();


		JSONObject totalObject = new JSONObject();

		totalObject.put("rows", array.getJsonArray(resultList));

		mavRetn.addObject("jsonData", totalObject);
		mavRetn.setViewName("/jsp/common/jsonJsp");

		return mavRetn;

	}
	
	@RequestMapping("rulesensordeviceInsert.do")
	public ModelAndView rulesensordeviceInsert(@RequestParam Map<String,String> param, HttpServletRequest httReqt, HttpSession session) throws Exception
	{
		if(sqlMapper == null){
			sqlMapper = SQLManager.sqlMapper;
		}
		
		ModelAndView mavRetn = new ModelAndView();
		
		//Create JsonObject
		JSONArray array = new JSONArray();

		ArrayList UserList = new ArrayList<>();
		
		JSONObject totalObject = new JSONObject();
		
		try
		{	
			sqlMapper.startTransaction();

			sqlMapper.update(rulesensordeviceInsert, param);		 

			sqlMapper.commitTransaction();
			
			totalObject.put("result", "Success");
			
		}catch( Exception e )
		{
			
			totalObject.put("result", "Fail");
			
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
		
		
		mavRetn.addObject("jsonData", totalObject);
		mavRetn.setViewName("/jsp/common/jsonJsp");
		
		return mavRetn;
	}
	
	/*
	 * 
	 * 새로 작성
	 * 
	 */
	
	
	@RequestMapping("ruleTotalSelect.do")
	public ModelAndView ruleTotalSelect(@RequestParam Map<String,String> param, HttpServletRequest httReqt, HttpSession session) throws Exception
	{

		if(sqlMapper == null){
			sqlMapper = SQLManager.sqlMapper; 
		}

		ModelAndView mavRetn = new ModelAndView();

		ArrayList resultList = new ArrayList<>();


		resultList = (ArrayList)sqlMapper.queryForList(ruleTotalSelect,param);


		ArrayToJsonArrayParser array = new ArrayToJsonArrayParser();


		JSONObject totalObject = new JSONObject();

		totalObject.put("rows", array.getJsonArray(resultList));

		mavRetn.addObject("jsonData", totalObject);
		mavRetn.setViewName("/jsp/common/jsonJsp");

		return mavRetn;

	}
	
	@RequestMapping("ruleSelect.do")
	public ModelAndView ruleSelect(@RequestParam Map<String,String> param, HttpServletRequest httReqt, HttpSession session) throws Exception
	{

		if(sqlMapper == null){
			sqlMapper = SQLManager.sqlMapper; 
		}

		ModelAndView mavRetn = new ModelAndView();

		ArrayList resultList = new ArrayList<>();


		resultList = (ArrayList)sqlMapper.queryForList(ruleSelect,param);


		ArrayToJsonArrayParser array = new ArrayToJsonArrayParser();


		JSONObject totalObject = new JSONObject();

		totalObject.put("rows", array.getJsonArray(resultList));

		mavRetn.addObject("jsonData", totalObject);
		mavRetn.setViewName("/jsp/common/jsonJsp");

		return mavRetn;

	}
	
	
}


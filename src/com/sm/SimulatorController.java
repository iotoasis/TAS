package com.sm;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.common.ArrayToJsonArrayParser;
import com.common.SQLManager;
import com.ibatis.sqlmap.client.SqlMapClient;


@Controller
public class SimulatorController {
	String simulator = "simulator-sql.simulator";
	String simulatorDelete = "simulator-sql.simulatorDelete";
	String simulatorInsert = "simulator-sql.simulatorInsert";
	String simulatorUpdate ="simulator-sql.simulatorUpdate";
	String simulatorserver ="simulator-sql.simulatorserver";
	String simulatorValueUpdate ="simulator-sql.simulatorValueUpdate";
	String simulatorLongPollingValueUpdate ="simulator-sql.simulatorLongPollingValueUpdate";
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

	@RequestMapping("simulator.do")
	public ModelAndView simulator(@RequestParam Map<String,String> param, HttpServletRequest httReqt, HttpSession session) throws Exception
	{

		if(sqlMapper == null){
			sqlMapper = SQLManager.sqlMapper; 
		}

		ModelAndView mavRetn = new ModelAndView();

		ArrayList resultList = new ArrayList<>();


		resultList = (ArrayList)sqlMapper.queryForList(simulator,param);


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

	@RequestMapping("simulatorDelete.do")
	public ModelAndView scheduleDelete(@RequestParam Map<String,String> param, HttpServletRequest httReqt, HttpSession session) throws Exception
	{
		if(sqlMapper == null){
			sqlMapper = SQLManager.sqlMapper;
		}

		ModelAndView mavRetn = new ModelAndView();

		JSONObject totalObject = new JSONObject();

		try
		{	
			sqlMapper.startTransaction();

			sqlMapper.insert(simulatorDelete, param);		 

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
	
	@RequestMapping("simulatorInsert.do")
	public ModelAndView scheduleInsert(@RequestParam Map<String,String> param, HttpServletRequest httReqt, HttpSession session) throws Exception
	{
		if(sqlMapper == null){
			sqlMapper = SQLManager.sqlMapper;
		}

		ModelAndView mavRetn = new ModelAndView();

		JSONObject totalObject = new JSONObject();

		try
		{	
			sqlMapper.startTransaction();

			sqlMapper.insert(simulatorInsert, param);		 

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
	
	@RequestMapping("simulatorUpdate.do")
	public ModelAndView simulatorUpdate(@RequestParam Map<String,String> param, HttpServletRequest httReqt, HttpSession session) throws Exception
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

			sqlMapper.update(simulatorUpdate, param);		 

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
	
	@RequestMapping("simulatorLongPollingValueUpdate.do")
	public ModelAndView simulatorLongPollingValueUpdate(@RequestParam Map<String,String> param, HttpServletRequest httReqt, HttpSession session) throws Exception
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

			sqlMapper.update(simulatorLongPollingValueUpdate, param);		 

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
	
	
	@RequestMapping("simulatorserver.do")
	public ModelAndView simulatorserver(@RequestParam Map<String,String> param, HttpServletRequest httReqt, HttpSession session) throws Exception
	{

		if(sqlMapper == null){
			sqlMapper = SQLManager.sqlMapper; 
		}

		ModelAndView mavRetn = new ModelAndView();

		ArrayList resultList = new ArrayList<>();


		resultList = (ArrayList)sqlMapper.queryForList(simulatorserver,param);


		ArrayToJsonArrayParser array = new ArrayToJsonArrayParser();


		JSONObject totalObject = new JSONObject();

		totalObject.put("rows", array.getJsonArray(resultList));

		mavRetn.addObject("jsonData", totalObject);
		mavRetn.setViewName("/jsp/common/jsonJsp");

		return mavRetn;


	}
	
	@RequestMapping("simulatorValueUpdate.do")
	public ModelAndView simulatorValueUpdate(@RequestParam Map<String,String> param, HttpServletRequest httReqt, HttpSession session) throws Exception
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

			sqlMapper.update(simulatorValueUpdate, param);		 

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
	
}


package com.tas;

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
public class TasController {
	
	String smartthingSelect = "tas-sql.smartthingSelect";
	String smartthingInsert = "tas-sql.smartthingInsert";
	String smartthingUpdate = "tas-sql.smartthingUpdate";
	String smartthingDelete = "tas-sql.smartthingDelete";
	
	SqlMapClient 			sqlMapper 			= null;
	public static Logger clsLogg = Logger.getRootLogger();

	/**
	 * @author 최인규
	 * 
	 * @date 2017-03-07 최초작성
	 * 
	 * @updateDate
	 * 
	 * @comment smartthing 조회
	 */

	@RequestMapping("smartthingSelect.do")
	public ModelAndView smartthingSelect(@RequestParam Map<String,String> param, HttpServletRequest httReqt, HttpSession session) throws Exception
	{

		if(sqlMapper == null){
			sqlMapper = SQLManager.sqlMapper; 
		}

		ModelAndView mavRetn = new ModelAndView();

		ArrayList resultList = new ArrayList<>();


		resultList = (ArrayList)sqlMapper.queryForList(smartthingSelect,param);


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
	 * @date 2017-03-07 최초작성
	 * 
	 * @updateDate
	 * 
	 * @comment smartthing 삭제
	 */

	@RequestMapping("smartthingDelete.do")
	public ModelAndView smartthingDelete(@RequestParam Map<String,String> param, HttpServletRequest httReqt, HttpSession session) throws Exception
	{
		if(sqlMapper == null){
			sqlMapper = SQLManager.sqlMapper;
		}

		ModelAndView mavRetn = new ModelAndView();

		JSONObject totalObject = new JSONObject();

		try
		{	
			sqlMapper.startTransaction();

			sqlMapper.insert(smartthingDelete, param);		 

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
	
	
	/**
	 * @author 최인규
	 * 
	 * @date 2017-03-07 최초작성
	 * 
	 * @updateDate
	 * 
	 * @comment smartthing 등록
	 */
	
	@RequestMapping("smartthingInsert.do")
	public ModelAndView smartthingInsert(@RequestParam Map<String,String> param, HttpServletRequest httReqt, HttpSession session) throws Exception
	{
		if(sqlMapper == null){
			sqlMapper = SQLManager.sqlMapper;
		}

		ModelAndView mavRetn = new ModelAndView();

		JSONObject totalObject = new JSONObject();

		try
		{	
			sqlMapper.startTransaction();

			sqlMapper.insert(smartthingInsert, param);		 

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
	
	/**
	 * @author 최인규
	 * 
	 * @date 2017-03-07 최초작성
	 * 
	 * @updateDate
	 * 
	 * @comment smartthing 수정
	 */
	
	@RequestMapping("smartthingUpdate.do")
	public ModelAndView emtestUpdate(@RequestParam Map<String,String> param, HttpServletRequest httReqt, HttpSession session) throws Exception
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

			sqlMapper.update(smartthingUpdate, param);		 

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


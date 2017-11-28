/***********************************
프로그램 명 :  SQLManager
기능    설명 :  xml파일과 연결 프로세스
작성    일자 :  2013.06.14
최종 수정자 :  정연혁 
***********************************/
package com.common;

import java.io.Reader;

import org.apache.log4j.Logger;
import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

public class SQLManager 
{
    public  static  Logger                  clsLogg                 = Logger.getRootLogger();
    public  static  SqlMapClient            sqlMapper               = null;
    
    private SQLManager() {}

    static
    {
    	Reader rdrReader = null;
	    try
	    {
	    	rdrReader = Resources.getResourceAsReader("sqlmap/sql-map-config.xml");
	        sqlMapper = SqlMapClientBuilder.buildSqlMapClient( rdrReader );
	    }
	    catch( Exception e ) 
	    {
            clsLogg.debug( e.toString() );
	        throw new RuntimeException("Something bad happened while building the SqlMapClient instance." + e );
	    }
    }
    public static SqlMapClient getSqlMapInstance() 
    {
    	return sqlMapper;
    }
}

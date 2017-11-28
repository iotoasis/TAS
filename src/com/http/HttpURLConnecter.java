package com.http;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

import com.service.utile.MethodTypeSelect;



public class HttpURLConnecter extends Thread{

	
	
	public HashMap getData(String urlString, String methodTypeSelect, HashMap<String, String> param ,String paramString ){
		try
		{
			
			URL url = new URL(urlString);
			
			HttpURLConnection  conn = (HttpURLConnection)url.openConnection();
			//Setting Request Property
			conn.setConnectTimeout(10000);
			conn.setRequestMethod(methodTypeSelect);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestProperty("X-M2M-Origin", param.get("X-M2M-Origin"));
			conn.setRequestProperty("X-M2M-RI", param.get("X-M2M-RI"));
			conn.setRequestProperty("Content-Type", param.get("Content-Type"));
			
			if(MethodTypeSelect.POST == methodTypeSelect){
				
				OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
				
				osw.write(paramString);
				osw.flush();
			}
			
			
			
			InputStreamReader isr = new InputStreamReader(conn.getInputStream(), "UTF-8");
			JSONObject object = null;
			
			if(isr != null){
				StringBuffer buffer;
				buffer = parse(conn.getInputStream());
				
				HashMap hashMap = new HashMap<>();
				
				hashMap.put("content", buffer);
				hashMap.put("responseCode", Integer.toString(conn.getResponseCode()));
				hashMap.put("responseMessage", conn.getResponseMessage());
				
				
				return hashMap;
			}else{
				System.out.println("[POST ERROR] :: " + "response is null!!"
				
				
				 );
				return null;
			}
			
		}
		catch( Exception e )
		{
			System.out.println("[POST ERROR] :: " + e );
			return null;
		}
		
	}
	
	public HashMap getDataMap(String urlString, String methodTypeSelect, Map<String, String> param ,String paramString ){
		try
		{
			
			URL url = new URL(urlString);
			
			HttpURLConnection  conn = (HttpURLConnection)url.openConnection();
			//Setting Request Property
			conn.setConnectTimeout(1000000);
			conn.setRequestMethod(methodTypeSelect);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestProperty("X-M2M-Origin", param.get("X-M2M-Origin"));
			conn.setRequestProperty("X-M2M-RI", param.get("X-M2M-RI"));
			conn.setRequestProperty("Content-Type", param.get("Content-Type"));
			
			if(MethodTypeSelect.POST == methodTypeSelect){
				
				OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
				
				osw.write(paramString);
				osw.flush();
			}
			
			
			
			InputStreamReader isr = new InputStreamReader(conn.getInputStream(), "UTF-8");
			JSONObject object = null;
			
			if(isr != null){
				StringBuffer buffer;
				buffer = parse(conn.getInputStream());
				
				HashMap hashMap = new HashMap<>();
				
				hashMap.put("content", buffer);
				hashMap.put("responseCode", Integer.toString(conn.getResponseCode()));
				hashMap.put("responseMessage", conn.getResponseMessage());
				
				
				return hashMap;
			}else{
				System.out.println("[POST ERROR] :: " + "response is null!!"
				
				
				 );
				return null;
			}
			
		}
		catch( Exception e )
		{
			System.out.println("[POST ERROR] :: " + e );
			return null;
		}
		
	}
	
	private StringBuffer parse(InputStream stream) throws Exception{
		 StringBuffer sb = new StringBuffer();
      try{
      	if(stream == null){
      		return null;
      	}
          byte[] b = new byte[4096];
          for (int n; (n = stream.read(b)) != -1;) {
              sb.append(new String(b, 0, n));
          }
       sb.toString();
      }catch(Exception ex){
          throw ex;
      }       

      return sb;
  }
	public JSONArray getDataArray(String urlString, String methodTypeSelect, HashMap<String, String> param ,String paramString ){
		try
		{
			
			URL url = new URL(urlString);
			
			HttpURLConnection  conn = (HttpURLConnection)url.openConnection();
			//Setting Request Property
			conn.setConnectTimeout(100000);
			conn.setRequestMethod(methodTypeSelect);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestProperty("X-M2M-Origin", param.get("X-M2M-Origin"));
			conn.setRequestProperty("X-M2M-RI", param.get("X-M2M-RI"));
			conn.setRequestProperty("Content-Type", param.get("Content-Type"));
			
			if(MethodTypeSelect.POST == methodTypeSelect){
				
				OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
				
				osw.write(paramString);
				osw.flush();
			}
			
			
			
			InputStreamReader isr = new InputStreamReader(conn.getInputStream(), "UTF-8");
			JSONArray array = new JSONArray();
			if(isr != null){
				
				JSONParser jsonParser = new JSONParser();
				
				array = (JSONArray) jsonParser.parse(isr);
				
			}else{
				System.out.println("[POST ERROR] :: " + "response is null!!"
				
				
				 );
				return null;
			}
			
			isr.close();
			
			return array;
		}
		catch( Exception e )
		{
			System.out.println("[POST ERROR] :: " + e );
			return null;
		}
		
	}
	

}

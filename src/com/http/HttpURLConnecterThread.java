package com.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONObject;

import com.activeintra.manager.x;
import com.onem2m.Onem2mGet;
import com.schedular.LongpollingScheduler;
import com.service.utile.MethodTypeSelect;

import netty.HttpSnoopServiceTxt;




public class HttpURLConnecterThread extends Thread{
	
	String urlString = null;
	String methodTypeSelect = null;
	HashMap<String, String> param = null;
	String paramString = null;
	int i = 0;
	JSONObject object = null;
	StringBuffer buffer = null;
	HttpURLConnection  conn = null;
	InputStreamReader isr = null;
	HttpSnoopServiceTxt httpSnoopServiceTxt = null;
	LongpollingScheduler longpollingScheduler2 = null;
	Onem2mGet onem2mGet = null;
	static HashMap hashMap = null;
	private final static String USER_AGENT = "Mozilla/5.0";
	public interface ThreadRecevie{
		public void onReciveRun(String tmp);
	}
	
	public HttpURLConnecterThread(String urlString, String methodTypeSelect, HashMap<String, String> param ,String paramString, Object callbackObject ){
		
		this.urlString = urlString;
		this.methodTypeSelect = methodTypeSelect;
		this.param = param;
		this.paramString = paramString;
		if(callbackObject != null){
			 if(callbackObject instanceof LongpollingScheduler){
					this.longpollingScheduler2 = (LongpollingScheduler) callbackObject;
				}else  if(callbackObject instanceof Onem2mGet){
					this.onem2mGet = (Onem2mGet) callbackObject;
				}
		}
		
		
		
	}
	
	public void run() {
		HashMap hashMap2 = new HashMap<>();
		
		try
		{
			URL url = new URL(urlString);
			
			conn = (HttpURLConnection)url.openConnection();
			//Setting Request Property
			conn.setRequestMethod(methodTypeSelect);
			if(param.get("ThreadType") != null ){
				if(param.get("ThreadType").equals("polling")){
					conn.setConnectTimeout(10000);
					conn.setReadTimeout(9000);
				}
			}else{
				conn.setConnectTimeout(5000);
				conn.setReadTimeout(4000);
			}
			
			
//			for(int i = 0; i < param.keySet().size(); i++){
//				Object[] x = param.keySet().toArray();
//				if(x[i] != null && param.get(x[i].toString()) != null){
//					conn.setRequestProperty(x[i].toString(), param.get(x[i].toString()));
//				}
//			}
			if(param != null && param.get("X-M2M-Origin") != null){
				conn.setRequestProperty("X-M2M-Origin", param.get("X-M2M-Origin"));
			}
			
			if(param != null && param.get("X-M2M-RI") != null){
				conn.setRequestProperty("X-M2M-RI", param.get("X-M2M-RI"));
			}
			
			if(param != null && param.get("Content-Type") != null){
				conn.setRequestProperty("Content-Type", param.get("Content-Type"));
			}
			
			conn.setRequestProperty("User-Agent", USER_AGENT);
			
			conn.setDoInput(true);
			conn.setDoOutput(true);
			
			System.out.println(methodTypeSelect + " URL ---> " + urlString);
			System.out.println(methodTypeSelect + " param ---> " + param);
			
			if(MethodTypeSelect.POST == methodTypeSelect){
				
				OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
				
				osw.write(paramString);
				osw.flush();
				
				i++;
			}
			
			
			isr = new InputStreamReader(conn.getInputStream(), "UTF-8");
			
			if(isr != null){
				
				object = new JSONObject();
				
				buffer = parse(conn.getInputStream());
				
				hashMap = new HashMap<>();
				
				
				hashMap.put("content", buffer);
				hashMap.put("responseCode", Integer.toString(conn.getResponseCode()));
				hashMap.put("responseMessage", conn.getResponseMessage());
				
				
				if(longpollingScheduler2 != null){
					if(param.get("ThreadType").equals("status")){
						
						this.longpollingScheduler2.statusCallBack(buffer.toString(), param);
						
					}else if(param.get("ThreadType").equals("polling")){
						
						this.longpollingScheduler2.pollingCallBack(buffer.toString(), param);
						
					}
				}else if(onem2mGet != null){
						
						this.onem2mGet.statusCallBack(hashMap);
						
				}
				
				
				
			}
			
		} catch (SocketTimeoutException e) {
		                System.out.println("Longpolling SocketTimeoutException !!!!!!!!!!!!!!!!!!!!!!!!!!");
		                if(longpollingScheduler2 != null){
							if(param.get("ThreadType").equals("status")){
								
								this.longpollingScheduler2.statusCallBack(null, param);
								
							}else if(param.get("ThreadType").equals("polling")){
								
								this.longpollingScheduler2.pollingCallBack(null, param);
								
							}
						}

		}catch( Exception e )
		{
			 System.out.println("Longpolling Exception !!!!!!!!!!!!!!!!!!!!!!!!!!");
             if(longpollingScheduler2 != null){
					if(param.get("ThreadType").equals("status")){
						
						this.longpollingScheduler2.statusCallBack(null, param);
						
					}else if(param.get("ThreadType").equals("polling")){
						
						this.longpollingScheduler2.pollingCallBack(null, param);
						
					}
				}
			try {
				if(conn != null && conn.getResponseMessage() != null){
					if(hashMap == null){
						hashMap = new HashMap<>();
					}
					hashMap.put("responseCode", Integer.toString(conn.getResponseCode()));
					hashMap.put("responseMessage", e.getLocalizedMessage());
					System.out.println("[POST ERROR CODE] :: " + conn.getResponseCode() + " --> " +  e.getLocalizedMessage());
				}
				
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
//			try {
//				if(param.get("ThreadType") != null ){
//					if(param.get("ThreadType").equals("polling")){
//						if(conn.getResponseCode() == 408){
//							
//							run();
//						}else if(conn.getResponseCode() == 500){
//							run();
//						}else if(conn.getResponseCode() == 502){
//							run();
//						}else{
//							run();
//						}
//					}
//				}
//				
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
			
		}finally{
//			try {
//				if(isr != null){
//					isr.close();
//				}
//				
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			this.hashMap = hashMap2;
//			conn.disconnect();
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
	
	public HashMap<String, String> getData(){
		return hashMap;
		
	}
	
	public void init(){
		hashMap = null;
	}
	
}

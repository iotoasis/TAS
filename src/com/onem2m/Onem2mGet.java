package com.onem2m;

import java.util.HashMap;

import com.http.HttpURLConnecterThread;
import com.service.utile.MethodTypeSelect;

public class Onem2mGet extends Thread{

	private HttpURLConnecterThread connecter = null;
	
	private HashMap<String, String> resultHashMap = null;
	
	
	
	public HashMap<String, String> setData(String url, HashMap<String, String> headerMashMap){
		
		resultHashMap = new HashMap<>();
		
		Onem2mGet onem2mGet = new Onem2mGet();

		headerMashMap.put("ThreadType", "Onem2mGet");
		
//		connecter = new  HttpURLConnecterThread(url, MethodTypeSelect.GET, headerMashMap, null, onem2mGet);
		connecter = new  HttpURLConnecterThread(url, MethodTypeSelect.GET, headerMashMap, null, null);
		connecter.start();
		try {
			connecter.join(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return connecter.getData();
	}
	
		
	public HashMap<String, String> getData(){
		return this.resultHashMap;
		
	}
		
	
	public void statusCallBack(HashMap<String, String> param) {
		
		

	}
}

package com.onem2m;

import java.util.HashMap;

import com.http.HttpURLConnecterThread;
import com.service.utile.MethodTypeSelect;

public class Onem2mDeleter extends Thread{
	
	HttpURLConnecterThread connecter = null;
	
	HashMap resultHashMap = new HashMap<>();

	public HashMap<String, String> setData(String url, HashMap<String, String> headerMashMap){

			HashMap hashMap = new HashMap<>();
			HashMap<String, String> paramHashmap = new HashMap<>();

			paramHashmap.put("X-M2M-Origin", headerMashMap.get("X-M2M-Origin").toString());
			paramHashmap.put("X-M2M-RI", headerMashMap.get("X-M2M-RI").toString());
			paramHashmap.put("Content-Type", "application/vnd.onem2m-res+json;");

			connecter = new HttpURLConnecterThread(url, MethodTypeSelect.DELETE, paramHashmap, null, null);

			connecter.start();
			
			try {
				connecter.join(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			resultHashMap = connecter.getData();

			return resultHashMap;
			
	}
	
	public HashMap getData(){
		return resultHashMap;

	}
}

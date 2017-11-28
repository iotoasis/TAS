package com.common;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ArrayToJsonArrayParser {

	public JSONArray getJsonArray(ArrayList arrayList){
		
		JSONArray array = new JSONArray();
		
		if(arrayList != null && arrayList.size() > 0){
			for(int i = 0; i < arrayList.size(); i++){
				HashMap hashMap2 = new HashMap<>();

				hashMap2 = (HashMap) arrayList.get(i);

				JSONObject jsonObject = new JSONObject();
				Object[] keySet = hashMap2.keySet().toArray();
				Object[] values = hashMap2.values().toArray();

				for(int ii = 0; ii < hashMap2.size(); ii++){
					//parameter init
					String keySetString = "";
					String valuesString = "";

					//get KeyString
					if(keySet[ii] != null){
						keySetString = keySet[ii].toString();
					}
					//get valueString
					if(values[ii] != null){
						valuesString = values[ii].toString();
					}
					
					//create 1 row
					jsonObject.put(keySetString, valuesString);
				}
				//add 1 row
				array.add(jsonObject);
			}
		}
		
		return array;
		
	}
}

package com.http;

import java.util.ArrayList;
import java.util.HashMap;

import com.service.utile.MethodTypeSelect;

public class ThreadTest {

	public static void main(String[] args) {

		ArrayList param = new ArrayList<>();
		param.add("http://localhost:8080/getsm");

		ArrayList<HashMap<String, String>> list = new ArrayList<>();

		HashMap hashMap1 = new HashMap<>();

		hashMap1.put("X-M2M-Origin", "TEST_TEST01_101");
		hashMap1.put("X-M2M-RI", "TEST_TEST01_101");
		hashMap1.put("id", "239");
		hashMap1.put("url", "http://166.104.112.34:8080/herit-in/herit-cse/TEST_TEST01_101/Action/status");
		hashMap1.put("ae", "TEST_TEST01_101");
		hashMap1.put("polling", "");
		hashMap1.put("Content-Type", "application/vnd.onem2m-res+json;");

		list.add(hashMap1);

		HashMap hashMap2 = new HashMap<>();

		hashMap2.put("X-M2M-Origin", "TEST_TEST01_101");
		hashMap2.put("X-M2M-RI", "TEST_TEST01_101");
		hashMap2.put("id", "240");
		hashMap2.put("url", "http://166.104.112.34:8080/herit-in/herit-cse/TEST_TEST01_101/Action/execute");
		hashMap2.put("ae", "TEST_TEST01_101");
		hashMap2.put("polling", "");
		hashMap2.put("Content-Type", "application/vnd.onem2m-res+json;");

		list.add(hashMap2);

		HashMap hashMap3 = new HashMap<>();

		hashMap3.put("X-M2M-Origin", "TEST_TEST01_102");
		hashMap3.put("X-M2M-RI", "TEST_TEST01_102");
		hashMap3.put("id", "241");
		hashMap3.put("url", "http://166.104.112.34:8080/herit-in/herit-cse/TEST_TEST01_102/Action/status");
		hashMap3.put("ae", "TEST_TEST01_102");
		hashMap3.put("polling", "");
		hashMap3.put("Content-Type", "application/vnd.onem2m-res+json;");

		list.add(hashMap3);

		HashMap hashMap4 = new HashMap<>();

		hashMap4.put("X-M2M-Origin", "TEST_TEST01_102");
		hashMap4.put("X-M2M-RI", "TEST_TEST01_102");
		hashMap4.put("id", "242");
		hashMap4.put("url", "http://166.104.112.34:8080/herit-in/herit-cse/TEST_TEST01_102/Action/execute");
		hashMap4.put("ae", "TEST_TEST01_102");
		hashMap4.put("polling", "");
		hashMap4.put("Content-Type", "application/vnd.onem2m-res+json;");

		list.add(hashMap4);

		HashMap hashMap5 = new HashMap<>();

		hashMap5.put("X-M2M-Origin", "TEST_TEST01_103");
		hashMap5.put("X-M2M-RI", "TEST_TEST01_103");
		hashMap5.put("id", "243");
		hashMap5.put("url", "http://166.104.112.34:8080/herit-in/herit-cse/TEST_TEST01_103/Action/status");
		hashMap5.put("ae", "TEST_TEST01_103");
		hashMap5.put("polling", "");
		hashMap5.put("Content-Type", "application/vnd.onem2m-res+json;");

		list.add(hashMap1);

		HashMap hashMap6 = new HashMap<>();

		hashMap6.put("X-M2M-Origin", "TEST_TEST01_103");
		hashMap6.put("X-M2M-RI", "TEST_TEST01_103");
		hashMap6.put("id", "244");
		hashMap6.put("url", "http://166.104.112.34:8080/herit-in/herit-cse/TEST_TEST01_103/Action/execute");
		hashMap6.put("ae", "TEST_TEST01_103");
		hashMap6.put("polling", "");
		hashMap6.put("Content-Type", "application/vnd.onem2m-res+json;");

		list.add(hashMap6);


		for(int i = 0; i < list.size(); i ++){
			HttpURLConnecterThread2 connecterThread2 = new HttpURLConnecterThread2("http://localhost:8080/getsm", MethodTypeSelect.POST.toString(), hashMap6, null, null);
			connecterThread2.start();
			try {
				connecterThread2.join();
				if(connecterThread2.getData() != null){
					System.out.println(connecterThread2.getData().toString());
				}else{
					System.out.println("content is null");
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

		}



	}

}

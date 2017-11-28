package com.schedular;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.common.SQLManager;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.onem2m.Onem2mPost;

import netty.HttpSnoopServer;

public class RepeatJobsListener implements ServletContextListener{

	HttpSnoopServer httpSnoopServer = null;
	LongpollingScheduler scheduler = null;

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		httpSnoopServer.stop();
		System.out.println("scheduler stop !!");
	}


	public void restart(){
		httpSnoopServer.stop();
		System.out.println("scheduler stop !!");
		httpSnoopServer.start();
		System.out.println("scheduler start !!");
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {

		/*

		 */

		try {
			httpSnoopServer = new HttpSnoopServer(7001);

			httpSnoopServer.start();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//longpollingSchedulerRequest();
		
//		NewPixedThreadPoolTest newPixedThreadPoolTest = new NewPixedThreadPoolTest();
//		newPixedThreadPoolTest.start();;

	}




	public void longpollingSchedulerRequest() throws InterruptedException{
		Onem2mPost onem2mPost = new Onem2mPost();
		HashMap hashMap = new HashMap<>();
		onem2mPost.setData("http://localhost:8080/restartpolling" , "restartpolling", hashMap, null, null);
		onem2mPost.getData();
	}

}

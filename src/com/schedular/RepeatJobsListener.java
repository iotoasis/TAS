package com.schedular;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import netty.HttpSnoopServer;

public class RepeatJobsListener implements ServletContextListener{

	HttpSnoopServer httpSnoopServer = null;
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		httpSnoopServer.stop();
	}


	public void restart(){
		httpSnoopServer.stop();
		httpSnoopServer.start();
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


	}


}

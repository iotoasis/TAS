package com.schedular;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.common.SQLManager;
import com.ibatis.sqlmap.client.SqlMapClient;

public class NewPixedThreadPoolTest extends Thread{

	public static void main(String[] args) throws InterruptedException {
		

	}
	
	public void run(){
		SqlMapClient sqlMapper = null;
		String simulatorSelect = "tas-sql.simulatorSelect";
		String simulator1Update = "tas-sql.simulator1Update";
		ArrayList oldResultList = null;
		ArrayList newResultList = null;
		LongpollingScheduler longpollingScheduler = null;
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		int i=0;
		longpollingScheduler = new LongpollingScheduler();
		executorService.execute(longpollingScheduler); // 3번 연속으로 쓰레드 수행 요청

		if(sqlMapper == null){
			sqlMapper 			= SQLManager.sqlMapper;
		}
		while(true){
			try {
				if(oldResultList == null){
					oldResultList = (ArrayList)sqlMapper.queryForList(simulatorSelect,null);
				}else{
					newResultList = (ArrayList)sqlMapper.queryForList(simulatorSelect,null);
				}

			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			if(newResultList != null && oldResultList.size() == newResultList.size()){
				System.out.println("똑같아요!");
			}else if(i != 0){
				System.out.println("틀려요! 3초 쉬었다 다시 시도!!");
				
				if(newResultList != null){
					oldResultList = newResultList;
				}
				try {
					longpollingScheduler.endScheduler();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("endScehduler Fail");
				}
				
				
				executorService.shutdownNow(); // 더이상의 쓰레드 요청은 받지 않음. 필요없으면 닫아줄것것
				if (!executorService.isTerminated()) { // 모든 쓰레드가 업무를 완료할때까지 대기
					executorService = Executors.newFixedThreadPool(10);
					longpollingScheduler = null;
					longpollingScheduler = new LongpollingScheduler();	
					executorService.submit(longpollingScheduler); 
				}
				
			}
			
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
}
package com.schedular;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ThreadTest  implements Runnable{
	  private String name;
	  public ThreadTest(String name) {
	    this.name = name;
	  }
	  public void run() {
	    System.out.println("Thread ID="+Thread.currentThread().getName()+"  task name="+name+" start");
	    try {Thread.sleep(1000000);
	    }catch(Exception e){
	    	
	    };
	    System.out.println("Thread ID="+Thread.currentThread().getName()+"  task name="+name+" end");
	  }
	};

	

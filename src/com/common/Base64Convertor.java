package com.common;

import java.io.IOException;

import org.apache.commons.codec.binary.Base64;

public class Base64Convertor {

	public Base64 base64 = new Base64();
	
	public String encrypt(String data){
		return new String(Base64.encodeBase64(data.getBytes()));
		
	}
	
	public String decrypt(String data) throws IOException{
		return new String(Base64.decodeBase64(data.getBytes()));
	}

}

package com.icbms.appserver.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.exception.ExceptionUtils;

import net.iharder.Base64;

public class Util {
	
	private volatile static long requestIdentifier = 1;
	private final static int REQUEST_ID_LENGTH = 16;
	
	public synchronized static String getRequestIdentifier() {
		RandomString rs = new RandomString(REQUEST_ID_LENGTH);
		return "Req-" + rs.nextString();
	}
	
	private volatile static long tmpResourceID = 1;
	public synchronized static String getTmpResourceID() {
		return "rscID:" + ++tmpResourceID;
	}
	
	public static int getByteLength(String str) {
		try {
			final byte[] utf8Bytes = str.getBytes("UTF-8");
			return utf8Bytes.length;
		} catch (UnsupportedEncodingException e) {
			L.e(ExceptionUtils.getStackTrace(e));
		}
		return -1;
	}
	
	public static String encodeBase64(String ori) {
		return Base64.encodeBytes(ori.getBytes());
	}
	
	public static String decodeBase64(String ori) {
		try {
			return new String(Base64.decode(ori));
		} catch (Exception e) {
			L.d(ExceptionUtils.getStackTrace(e));
			return null;
		}
	}
}

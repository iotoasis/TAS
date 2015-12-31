package com.icbms.appserver.dao.model.onem2m;

import org.apache.commons.lang.exception.ExceptionUtils;

import com.icbms.appserver.http.RequestManager;
import com.icbms.appserver.util.L;

public class Resource {
	protected Resource parentRsc = null;
	public String resourceName = "";
	public String resourcePath = "";

	protected boolean tobeCreated = true;
	
	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();
		if (resourceName    	!= null)		result.append("resourceName    	: " +  resourceName    			+ "\n");
		if (resourcePath    	!= null)		result.append("recourcePath    	: " +  resourcePath    			+ "\n");
		return result.toString();
	}
	
	public String getCurrentPath() {
		L.d(resourceName);
		if(null != parentRsc) {
			return parentRsc.getCurrentPath() + "/" + this.resourceName;
		} else {
			return "/" + this.resourceName;
		}
	}
	
	protected String getParentPath() {
		String currentPath = getCurrentPath();
		L.d("currentPath: " + currentPath);
		String path = currentPath.substring(0, currentPath.length() - resourceName.length());
		if(path.length() > 0 && path.lastIndexOf('/') == path.length()-1) {
			path = path.substring(0,path.length()-1);
		}
		L.d("Path is: " + path);
		return path;
	}
	
	public void setParentRsc(Resource rsc) {
		this.parentRsc = rsc;
	}



	public boolean isTobeCreated() {
		return tobeCreated;
	}

	public void setTobeCreated(boolean tobeCreated) {
		this.tobeCreated = tobeCreated;
	}
}

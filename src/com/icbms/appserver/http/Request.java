package com.icbms.appserver.http;

import java.util.Map;

public interface Request extends CommonInterface {
	//have to be changed to have parameter that indicate request type.(CRUD)
	public Map<?, ?> makeHeader();
	//have to be changed to have parameter that indicate request type.(CRUD)
	public String makeBody();
	public String getRequestIdentifier();
	public String getRequestType();
	public String getRscTypeOfReq();
	public String getStructuredPath();
	public String getUnstructuredPath();
}

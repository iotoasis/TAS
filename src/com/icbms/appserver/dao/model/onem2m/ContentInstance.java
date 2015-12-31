package com.icbms.appserver.dao.model.onem2m;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.icbms.appserver.http.Request;
import com.icbms.appserver.util.Util;
import com.icbms.appserver.util.xml.XMLBuilder2;

/**
 * Data class for oneM2M contentInstance resource.
 */
public class ContentInstance extends Resource implements Request {

	public String resourceType = null;
	public String resourceID = null;
	public String parentID = null;
	public String creationTime = null;
	public String lastModifiedTime = null;
	public String expirationTime = null;
	public String labels = null;
	public String stateTag = null;
	public String contentInfo = null;
	public String contentSize = null;
	public String content = null;
	public String linkType = null;

	public String containerName = null;
	public String appName = null;
	public String appId = null;
	
	public String requestId;	
	public Map<String, String> reqHeader = null;
	
	public ContentInstance() {
		requestId = Util.getRequestIdentifier();
	}
	
	@Override
	public Map<String, String> makeHeader() {
		reqHeader = new  HashMap<>();
		requestId = Util.getRequestIdentifier();
		reqHeader.put(OneM2MTypes.HeaderAttr.X_M2M_RI, "" + requestId);
		reqHeader.put(OneM2MTypes.HeaderAttr.X_M2M_ORIGIN, "emul");
//		reqHeader.put(OneM2MTypes.HeaderAttr.X_M2M_ORIGIN, getOrigin());		
		reqHeader.put(OneM2MTypes.HeaderAttr.CONTENT_TYPE, 
//				OneM2MTypes.HeaderAttr.CONTENT_TYPE_JSON_VALUE + OneM2MTypes.ResourceType.CONTENT_INSTANCE);
				OneM2MTypes.HeaderAttr.CONTENT_TYPE_XML_VALUE + OneM2MTypes.ResourceType.CONTENT_INSTANCE);
		
		return reqHeader;
	}
	
	@Override
	public String makeBody() {
		XMLBuilder2 builder2 = 
				XMLBuilder2.create("m2m:cin").ns("xmlns:m2m", "http://www.onem2m.org/xml/protocols");
		if(contentInfo != null) {
		builder2.e("cnf")
				  .t(contentInfo)
				.up();
		}
		if (labels != null) {
		builder2.e("lbl")
				  .t(labels)
				.up();
		}
		builder2.e("con")
				  .t(content)
				.up();
		Properties outputProperties = new Properties();
		outputProperties.put(javax.xml.transform.OutputKeys.ENCODING, "utf-8");
		outputProperties.put(javax.xml.transform.OutputKeys.METHOD, "xml");
		outputProperties.put(javax.xml.transform.OutputKeys.INDENT, "yes");
		outputProperties.put("{http://xml.apache.org/xslt}indent-amount", "2");
		outputProperties.put(javax.xml.transform.OutputKeys.OMIT_XML_DECLARATION, "yes");
		String body = builder2.asString(outputProperties);
		return body;
	}
	
	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append(super.toString());
		if(resourceType     != null) result.append("resourceType     : "  + resourceType    + "\n");
	    if(resourceID       != null) result.append("resourceID       : "  + resourceID      + "\n");
	    if(parentID         != null) result.append("parentID         : "  + parentID        + "\n");
	    if(creationTime     != null) result.append("creationTime     : "  + creationTime    + "\n");
	    if(lastModifiedTime != null) result.append("lastModifiedTime : "  + lastModifiedTime+ "\n");
	    if(expirationTime   != null) result.append("expirationTime   : "  + expirationTime  + "\n");
	    if(labels           != null) result.append("labels           : "  + labels          + "\n");
	    if(stateTag         != null) result.append("stateTag         : "  + stateTag        + "\n");
	    if(contentInfo      != null) result.append("contentInfo      : "  + contentInfo     + "\n");
	    if(contentSize      != null) result.append("contentSize      : "  + contentSize     + "\n");
	    if(content          != null) result.append("content          : "  + content         + "\n");
	    if(linkType         != null) result.append("linkType         : "  + linkType        + "\n");
	    if(containerName    != null) result.append("containerName    : "  + containerName   + "\n");
	    if(appName          != null) result.append("appName          : "  + appName         + "\n");
	    if(appId            != null) result.append("appId            : "  + appId           + "\n");
	    result.append(getRscTypeOfReq());
	    result.append("\n\n");
		return result.toString();
	}

	@Override
	public String getName() {
		return containerName;
	}

	@Override
	public String getID() {
		return resourceID;
	}

	@Override
	public String getParentID() {
		return parentID;
	}

	@Override
	public String getRequestIdentifier() {
		return "" + requestId;
	}
	
	@Override
	public void setParentID(String parentId) {
		this.parentID = parentId;
	}

	@Override
	public String getRequestType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRscTypeOfReq() {
		return OneM2MTypes.ResourceType.CONTENT_INSTANCE;
	}

	@Override
	public String getStructuredPath() {
		return getParentPath();
	}

	@Override
	public String getUnstructuredPath() {
		// TODO Auto-generated method stub
		return null;
	}
}
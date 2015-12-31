package com.icbms.appserver.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;








import com.icbms.appserver.dao.model.onem2m.ContentInstance;
import com.icbms.appserver.dao.model.onem2m.Resource;
import com.icbms.appserver.dao.model.onem2m.OneM2MTypes;
import com.icbms.appserver.util.L;
import com.icbms.appserver.util.Util;
import com.icbms.appserver.util.xml.XMLBuilder2;
import com.icbms.appserver.util.xml.XMLBuilderRuntimeException;

public class HttpRequestSender {
	

	public int sendRequest(Request req) {
		return sendRequest(req, null);
	}
	public int sendRequest(Request req, String path) { 
		L.d("send request to server");
		
		URI uri = null;
		String result_content_type = OneM2MTypes.ResultContent.HIERARCHICAL_ADDRESS;
		try {
			uri = new URIBuilder()
					.setScheme("http")
					.setHost(ServerInfo.HERIT_SERVER)
					.setPath(path == null ? ServerInfo.CSEBASE_PATH + req.getStructuredPath():path)
					.addParameter("rcn", result_content_type)
					.build();
			L.d("Request: uri: " + uri);
		} catch (URISyntaxException e1) {
			L.e(ExceptionUtils.getStackTrace(e1));
		}

//		HttpClient client = HttpClients.createDefault(); 
		RequestConfig requestConfig =  RequestConfig.custom()
				.setConnectTimeout(ServerInfo.HTTP_CONNECTION_TIMEOUT)
				.setSocketTimeout(ServerInfo.HTTP_SOCKET_TIMEOUT)
				.build();
		HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
		HttpPost post = new HttpPost(uri);
		
		//put header values
		Map<String, String> header = (Map<String, String>) req.makeHeader();
		L.d("Header values");
		for(String key:header.keySet()) {
			post.addHeader(key, header.get(key));
			L.d("key:" + key + ", value:" + header.get(key));
		}
		post.addHeader("Accept", OneM2MTypes.HeaderAttr.CONTENT_TYPE_XML);
		
		String bodyString = req.makeBody();
		L.d("body String: " + bodyString);
		//put body
		StringEntity entity = null;
		try {
			entity = new StringEntity(bodyString);
		} catch (UnsupportedEncodingException e1) {
			L.e(ExceptionUtils.getStackTrace(e1));
		}
		post.setEntity(entity);
		//excute and get response
		HttpResponse resp = null; 
		try {
			resp = client.execute(post);

			int responseCode = resp.getStatusLine().getStatusCode();
			L.d("Response Code : " + responseCode);
			HttpEntity respEntity = resp.getEntity();
			String responseString = EntityUtils.toString(respEntity);
			L.d(responseString);
			
			if(result_content_type.equals(OneM2MTypes.ResultContent.HIERARCHICAL_ADDRESS)) {
				((Resource)req).resourcePath = responseString;
			}
			return responseCode;
/*			builder.
			L.d("Response: " + resp);
			BufferedReader rd;
			rd = new BufferedReader(
					new InputStreamReader(resp.getEntity().getContent()));
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}*/
		} catch (UnsupportedOperationException e) {
			L.e(ExceptionUtils.getStackTrace(e));
		} catch (IOException e) {
			L.e(ExceptionUtils.getStackTrace(e));
		}
		return -9999;

	}
	
	/*private CSEBase registerCSE() {
		String requestBody = ""; 

		requestBody = 			
				"<m2m:remoteCSE\n" +
						"xmlns:m2m=\"http://www.onem2m.org/xml/protocols\"\n" +
						"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" + 
						"<cseType>3</cseType>\n" +
						"<CSE-ID>" + "0.2.481.1.0001.001.7592" + "</CSE-ID>\n" + 
						"<CSEBase>nCube</CSEBase>" +
						"<pointOfAccess>" + "HTTP|192.168.0.13:7579" + "</pointOfAccess>\n" + 
						"<requestReachability>" + true + "</requestReachability>\n" + 
				"</m2m:remoteCSE>";
		
		StringEntity entity = null;
		try {
			entity = new StringEntity(
					new String(requestBody.getBytes()));
		} catch (UnsupportedEncodingException e3) {
			L.e(ExceptionUtils.getStackTrace(e3));
		}

		URI uri = null;
		try {
			uri = new URIBuilder()
					.setScheme("http")
					.setHost("192.168.0.13:7579")
					.setPath("/Mobius")
					.build();
		} catch (URISyntaxException e2) {
			L.e(ExceptionUtils.getStackTrace(e2));
		}
		
		HttpPost post = new HttpPost(uri);
				post.setHeader("Accept", "application/xml");
				post.setHeader("Content-Type", "application/vnd.onem2m-res+xml;ty=16");
				post.setHeader("locale", "ko");
				post.setHeader("passCode", "1234");
				post.setHeader("X-M2M-Origin", "");
				post.setHeader("X-M2M-RI", "001");
				post.setHeader("X-M2M-NM", "pth");
				post.setEntity(entity);
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		
		HttpResponse response = null;
		try {
			response = httpClient.execute(post);
		} catch (IOException e) {
			L.e(ExceptionUtils.getStackTrace(e));
		}
		
		HttpEntity responseEntity = response.getEntity();
		
		int responseCode = response.getStatusLine().getStatusCode();

		String[] responseString = new String[2];
		
		Header[] responseHeader = response.getHeaders("dKey");
		
		if (responseHeader.length > 0) {
			responseString[0] = responseHeader[0].getValue();
		}
		else {
			responseString[0] = Integer.toString(responseCode);
		}
		
		try {
			responseString[1] = EntityUtils.toString(responseEntity);
		} catch (ParseException e1) {
			L.e(ExceptionUtils.getStackTrace(e1));
		} catch (IOException e1) {
			L.e(ExceptionUtils.getStackTrace(e1));
		}
		
		L.d("HTTP Response Code : " + responseCode);
		L.d("HTTP Response String : " + responseString[1]);
		
		try {
			httpClient.close();
		} catch (IOException e) {
			L.e(ExceptionUtils.getStackTrace(e));
		}
		
		return null;
	}*/
	
	private int createContentInstance(String path, ContentInstance cntInst) {
		return sendRequest(cntInst, path);
	}

	public int createContentInstance(String path, String content) {
		ContentInstance ctnt = new ContentInstance();
		ctnt.content = content;
		ctnt.contentInfo = "text/plain:0";
		return sendRequest(ctnt, path);
	}
	/**
	 * @param path
	 * @param content
	 * @param contentType  
	 * @param encodingType  
	 * @see OneM2MTypes.EncodingType
	 * @see OneM2MTypes.ContentType
	 * @return
	 */
	public int createContentInstance(String path, String content, String contentType, String encodingType) {
		ContentInstance ctnt = new ContentInstance();
		ctnt.content = content;
		ctnt.contentInfo = contentType + ":" + encodingType;
		return sendRequest(ctnt, path);
	}
	
	public String retrieveResource(String path, int resultContentType) {	
		L.d("send retrieve request to server");
		L.d("path is: " + path);
		
		URI uri = null;
		try {
			uri = new URIBuilder()
					.setScheme("http")
					.setHost(ServerInfo.HERIT_SERVER)
					.setPath((path == null ? "/" + ServerInfo.CSEBASE_PATH : path))
					.setParameter("rcn", resultContentType + "")
					.build();
		} catch (URISyntaxException e1) {
			L.e(ExceptionUtils.getStackTrace(e1));
		}

		HttpClient client = HttpClients.createDefault();
		HttpGet get = new HttpGet(uri);
		
		//put header values
		get.addHeader("X-M2M-RI", Util.getRequestIdentifier());
		get.addHeader("X-M2M-Origin", ServerInfo.ORIGIN);
		get.addHeader("Content-type", OneM2MTypes.HeaderAttr.CONTENT_TYPE_XML);
		get.addHeader("Accept", OneM2MTypes.HeaderAttr.CONTENT_TYPE_XML);
		
		//excute and get response
		HttpResponse resp = null; 
		String result = null;
		try {
			resp = client.execute(get);
			HttpEntity respEntity = resp.getEntity();
			String responseString = EntityUtils.toString(respEntity);

			L.d(responseString);
			
			return responseString;
			
		} catch (UnsupportedOperationException e) {
			result = result + " " + e.getMessage();
			L.e(ExceptionUtils.getStackTrace(e));
		} catch (IOException e) {
			result = result + " " + e.getMessage();
			L.e(ExceptionUtils.getStackTrace(e));
		}
		return result;
	}
	
	public List<String> getChildResourceName(String response) {
		List<String> childResourceName = new ArrayList<String>();

		try {
			XMLBuilder2 builder = XMLBuilder2.parse(response);
			Document doc = builder.getDocument();
			doc.getDocumentElement().normalize();
			NodeList nodeList = doc.getElementsByTagName("ch");
			for (int s = 0; s < nodeList.getLength(); s++) {
				Node node = nodeList.item(s);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element elmt = (Element) node;
					String childName = elmt.getAttribute("nm");
					L.d("childName: " + childName);
					childResourceName.add(childName);
				}
			}
			return childResourceName;
		} catch (XMLBuilderRuntimeException e) {
			L.d("there is no child");
			return null;
		}
	}
	
	public List<String> retrieveResourceAndChildren (String _path) {
		List<String> resources = new LinkedList<>();
		
		String response = retrieveResource(_path, 5);
		resources.add(response);
		List<String> resourcePath = getChildResourceName(response); 
		if(_path != null) {
			if(resourcePath != null ) {
				for(int i=0; i < resourcePath.size(); i++) {
					resourcePath.add(i, _path + "/" + resourcePath.remove(i)); 
				}
			} else {
				return null;
			}
		}
		while (resourcePath.size() != 0) {
			String path = resourcePath.remove(resourcePath.size()-1);
			L.d("current resourcepath: " + path);
			response = retrieveResource(path, 5);
			resources.add(response);
			List<String> childs = getChildResourceName(response);
			Collections.reverse(childs);
			for(String child:childs) {
				resourcePath.add(path + "/" + child);
			}
		}
		return resources;
	}
	
	
}

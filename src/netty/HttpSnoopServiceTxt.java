package netty;

import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.util.HashMap;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.connecter.RuleenginConnector;
import com.connecter.SIConnecter;
import com.connecter.SmartThingsConnecter;
import com.schedular.LongpollingScheduler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.CharsetUtil;

public class HttpSnoopServiceTxt extends SimpleChannelInboundHandler<Object> {

	public  static  Logger tasLog = Logger.getRootLogger();

	static HttpSnoopServiceTxt httpSnoopServiceTxt = null;

	LongpollingScheduler scheduler = null;
	
	private LastHttpContent trailer = null;

	private FullHttpRequest request;

	private StringBuilder buf = new StringBuilder();

	boolean optionBoolean = false;
	
	SmartThingsConnecter smartThingsConnecter = null;
	
	RuleenginConnector ruleenginConnector = null;
	
	SIConnecter siConnecter = null;
	
	JSONObject bodyJsonObject = null;
	
//	Pattern urlPattern = Pattern.compile("^(https?):\\/\\/([^:\\/\\s]+)(:([^\\/]*))?((\\/[^\\s/\\/]+)*)?\\/([^#\\s\\?]*)(\\?([^#\\s]*))?(#(\\w*))?$");
	Pattern urlPattern = Pattern.compile("^((\\/[^\\s/\\/]+)*)?\\/([^#\\s\\?]*)(\\?([^#\\s]*))?(#(\\w*))?$");
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws InterruptedException  {

		/*

		1. url 泥댄겕
		2. paramater 泥댄겕
		3. AE 泥댄겕
		4. DB �씤�꽕�듃
		5. �꽌踰꾩쟾�넚
		6. 寃곌낵 諛쏆쓬
		7. DB ���옣
		8. 寃곌낵 由ъ뒪�룿�뒪

		 */


		

		

		HashMap<String, String> headerParamHashmap = new HashMap<>();




		if (msg instanceof FullHttpRequest) {
			tasLog.debug("[ RESPONSE ADDRESS ] ::" +  ctx.channel());
			tasLog.debug("[ RESPONSE HEADER ] ::" +  msg);
			//header
			FullHttpRequest request = (FullHttpRequest) (this.request = (FullHttpRequest) msg);
			buf.setLength(0);
			
			if(request.getMethod().name().equals("OPTIONS")){

				optionBoolean = true;
				
				
			}else{

				// hostname
				// buf.append("HOSTNAME:").append(getHost(request, "uri"));

				//content-body
				ByteBuf jsonBuf = request.content();
				String jsonStr = jsonBuf.toString(CharsetUtil.UTF_8);


				if(jsonStr.length() > 0 ){
					//content-body is not null

					tasLog.debug("[ RESPONSE BODY ] ::" +  jsonStr);

					JSONParser jsonParser = new JSONParser();
					try {
						bodyJsonObject = (JSONObject) jsonParser.parse(jsonStr);
					} catch (ParseException e) {

						tasLog.debug("[ TAS ERROR ] :: " + "Response body parsing(json) error!!");

						e.printStackTrace();
					}

				}else{
					//content-body is null

					tasLog.debug("[ TAS INFO ] ::" +  "Content-Body is NULL!!");

				}

				// url

				String requestUriString = null;

				String orderString = null;

				if(!request.getUri().trim().equals("/")){
					//command url is not null
					requestUriString = request.getUri().trim();

					tasLog.debug("[ TAS INFO ] ::" +  "COMMAND URL --> " + requestUriString);

					String[] splitRequestUriString = requestUriString.split("[?]");
					
					
					Matcher mc = urlPattern.matcher(requestUriString);

					if(((Matcher) mc).matches()){
						for(int i=0;i<=((MatchResult) mc).groupCount();i++);
					}
					else {
						tasLog.debug("[ TAS INFO ] ::" +  "COMMAND URL not found");
					}
					orderString = mc.group(3);

					if(mc.group(5) != null){
						splitRequestUriString = mc.group(5).split("[&]");

					}


					String[] fucntionStringList;
					
					for(int i = 0 ; i < splitRequestUriString.length; i++){

						String fucntionString = splitRequestUriString[i];

						fucntionStringList = fucntionString.split("[=]");
						//Check URL command
						switch(fucntionStringList[0]){
						case "poststatus":
							headerParamHashmap.put("request_command", "poststatus");
							break;

						case "status":

							headerParamHashmap.put("status", fucntionStringList[1]);
							headerParamHashmap.put("request_command", "status");

							break;

						case "getstatus":

							headerParamHashmap.put("getstatus", fucntionStringList[1]);
							headerParamHashmap.put("request_command", "getstatus");

							break;

						case "measurement":
							headerParamHashmap.put("request_command", "measurement");
							break;

						case "token" :

							headerParamHashmap.put("token", splitRequestUriString[i].replace("token=", ""));

							break;

						case "deviceId" :

							headerParamHashmap.put("deviceId", fucntionStringList[1]);

							break;



						default:
							headerParamHashmap.put("request_command", "");
							break;

						}

					}


				}else{
					//command url is null

					tasLog.debug("[ TAS INFO ] ::" +  "COMMAND URL is NULL!!");
				}


				//header parameter parsing!!

				String x_m2m_orign = null;
				String x_m2m_ri = null;
				String content_type = null;

				String requestStatus = null;
				String smd = null;
				String url = null;
				String ae = null;
				String polling = null;
				String deviceId  = null;
				String id  = null;



				if(request.headers().get("X-M2M-Origin") != null){

					x_m2m_orign = request.headers().get("X-M2M-Origin");

				}else{

					tasLog.debug("[ TAS INFO ] ::" +  "Header X-M2M-Origin is NULL!!");
					
				}

				if(request.headers().get("X-M2M-RI") != null){

					x_m2m_ri = request.headers().get("X-M2M-RI");

				}else{

					tasLog.debug("[ TAS INFO ] ::" +  "Header X-M2M-RI is NULL!!");

				}

				if(request.headers().get("content_type") != null){

					content_type = request.headers().get("content_type");

				}else{

					tasLog.debug("[ TAS INFO ] ::" +  "Header content_type is NULL!!");

					content_type= "application/vnd.onem2m-res+json;";
				}

				if(request.headers().get("deviceId") != null){

					deviceId = request.headers().get("deviceId");

				}else{

					tasLog.debug("[ TAS INFO ] ::" +  "Header deviceId is NULL!!");

				}

				if(request.headers().get("status") != null){

					requestStatus = request.headers().get("status");

				}else{

					tasLog.debug("[ TAS INFO ] ::" +  "Header value is NULL!!");
					
					if(request.headers().get("requestStatus") != null){

						requestStatus = request.headers().get("requestStatus");

					}else{

						tasLog.debug("[ TAS INFO ] ::" +  "Header requestStatus is NULL!!");

					}
				}

				if(request.headers().get("smd") != null){

					smd = request.headers().get("smd").toString();

				}else{

					tasLog.debug("[ TAS INFO ] ::" +  "Header smd is NULL!!");

				}

				if(request.headers().get("url") != null){

					url = request.headers().get("url");

				}else{

					tasLog.debug("[ TAS INFO ] ::" +  "Header url is NULL!!");

				}

				if(request.headers().get("ae") != null){

					ae = request.headers().get("ae");

				}else{

					tasLog.debug("[ TAS INFO ] ::" +  "Header ae is NULL!!");

				}

				if(request.headers().get("polling") != null){

					polling = request.headers().get("polling");

				}else{

					tasLog.debug("[ TAS INFO ] ::" +  "Header value is NULL!!");

				}

				if(request.headers().get("id") != null){

					id = request.headers().get("id");

				}else{

					tasLog.debug("[ TAS INFO ] ::" +  "Header value is NULL!!");

				}

				

				headerParamHashmap.put("X-M2M-Origin", x_m2m_orign);
				headerParamHashmap.put("X-M2M-RI", x_m2m_ri);
				headerParamHashmap.put("Content-Type", content_type);
				
				if(headerParamHashmap.get("deviceId") == null){
					headerParamHashmap.put("deviceId", deviceId);
				}
				
				headerParamHashmap.put("requestStatus", requestStatus);
				headerParamHashmap.put("smd", smd);
				headerParamHashmap.put("url", url);
				headerParamHashmap.put("ae", ae);
				headerParamHashmap.put("polling", polling);
				headerParamHashmap.put("id", id);


				// kind of request 
				// 1. smart things           
				if(headerParamHashmap.get("deviceId") != null || orderString.equals("smartstatusall") || orderString.equals("execute_noti")){
						
					if(smartThingsConnecter == null){
						smartThingsConnecter = new SmartThingsConnecter();
					}

					smartThingsConnecter.setData(headerParamHashmap, orderString, bodyJsonObject, buf, jsonStr);
					
					buf = smartThingsConnecter.getData();

				}else{
					// 2. other things	
					if(orderString.equals("noti")){
						if(ruleenginConnector == null){
							ruleenginConnector = new RuleenginConnector(headerParamHashmap, orderString, bodyJsonObject, buf, jsonStr);
							
						}
						
						buf = ruleenginConnector.getData();
					
					}else if(orderString.equals("restartpolling")){

						

						pollingRestart();
						buf.append("{");
						buf.append("\"id\": \"" + "" + "\",");
						buf.append("\"deviceId\": \"" + "" + "\",");
						buf.append("\"result\": \"" + "Success" + "\",");
						buf.append("\"rtime\": \"" + "" + "\",");
						buf.append("\"value\": \"" + "" + "\",");
						buf.append("\"reason\": \"N/A\"");
						buf.append("}");
						
					}else if(orderString.equals("pollingKill")){
						pollingKill();
					}else{
						
						
						
//						ExtasConnector siConnecter = new ExtasConnector();
						SIConnecter siConnecter = new SIConnecter();
						siConnecter.setData(headerParamHashmap, orderString, bodyJsonObject, buf, jsonStr);
						buf = siConnecter.getData();
					}

					
					
				}

			}

		}


		if (msg instanceof HttpContent) {
			if (msg instanceof LastHttpContent) {
				trailer = (LastHttpContent) msg;
				if(httpSnoopServiceTxt == null){
					httpSnoopServiceTxt = new HttpSnoopServiceTxt();
				}
				writeResponse(trailer, ctx);
				//                WriterFile.printtxt(buf.toString());
			}
		}




	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
		ctx.close();
	}

	private boolean writeResponse(HttpObject currentObj,ChannelHandlerContext ctx) {

		boolean keepAlive = isKeepAlive(request);

		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, currentObj.getDecoderResult().isSuccess() ? OK : BAD_REQUEST, Unpooled.copiedBuffer(buf.toString(), CharsetUtil.UTF_8));
		response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");
		
			response.headers().set("Access-Control-Allow-Origin", "*");
			response.headers().set("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
			response.headers().set("Access-Control-Max-Age", "3600");
			response.headers().set("Access-Control-Allow-Headers", "Origin,Accept,X-Requested-With,Content-Type,Access-Control-Request-Method,Access-Control-Request-Headers,Authorization,"
					+ "ae,status,url,X-M2M-Origin,X-M2M-RI,content_type,polling,response_date,smd,Host,Connection,User-Agent,Referer,Accept-Encoding,Accept-Language,id,requestStatus");
		
		if (keepAlive) {
			response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
			response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
		}
		//        System.out.println(jsonObject.toString());
		ctx.write(response);

		return keepAlive;
	}


	public void callBack(String string) {

		tasLog.debug("[ TAS INFO ] :: " + "Call Back String !!");

		System.out.println(string);

	}
	
	public void pollingRestart(){
		if(scheduler != null ){

//			scheduler.endScheduler();
//			scheduler.currentThread().setName("pollingThread");
			scheduler.run("restart");
			
			tasLog.debug("[ TAS INFO ] :: " + "Longpolling restart!!");
			
		}else{
			
			scheduler = new LongpollingScheduler();
//			scheduler.currentThread().setName("pollingThread");
			scheduler.run("start");
			
			tasLog.debug("[ TAS INFO ] :: " + "Longpolling start!!");
		}
	}
	
	public void pollingKill(){
		Thread currentThread = Thread.currentThread();
        ThreadGroup threadGroup = currentThread.getThreadGroup();
        while (threadGroup.getParent() != null) {
            threadGroup = threadGroup.getParent();
        }
        int activeCount = threadGroup.activeCount();
        System.out.println("activeCount: " + activeCount);
        // NOTE:
        // The number of thread is changing so additional 5 is for guaranteeing
        // enough space.
        Thread[] activeThreads = new Thread[activeCount + 5];
        int enumeratedCount = threadGroup.enumerate(activeThreads);
        System.out.println("enumeratedCount: " + enumeratedCount);
        for (int i = 0; i < enumeratedCount; i++) {
            if (activeThreads[i].getName().equals("pollingThread")) {
            	ThreadGroup rootGroup = Thread.currentThread().getThreadGroup();
            	ThreadGroup parentGroup;
            	while ((parentGroup = rootGroup.getParent()) != null) {
            	    rootGroup = parentGroup;
            	}
            	 
            	Thread[] threads = new Thread[rootGroup.activeCount()];
            	while (rootGroup.enumerate(threads, true) == threads.length) {
            	    threads = new Thread[threads.length * 2];
            	}
            	 
            	for (Thread t : threads) {
            	    if (t.getId() == activeThreads[i].getId()) {
            	        System.out.println("found!!!!!");
            	    }
            	}


                
            }
        }

	}

}

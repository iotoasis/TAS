package com.icbms.appserver.dao.model.onem2m;

/**
 * Attributes name and short name and preDefined values of those related OneM2M Resource. 
 * This class <b>does not</b> contain all the types, enums, names, values defined in standard documents.
 * So consider it before using this.
 * @author Bowman
 */
public class OneM2MTypes {
	public static final String CSE_DEFAULT_NAME = "InCSE1";
	public static final String SYS_PERF_TEST_CSE = "SYS_PERF_TEST_CSE";

	public static final int MAX_RESOURCES = 1000000;
	public static final int MAX_TREE_WIDTH = 10000;
	public static final int MAX_TREE_LEVEL = 20;
	public static final int MAX_NR_INSTANCES_PER_CONTAINER = 20;
	public static final boolean USE_JSON_ANY_SYNTAX = false;

	public static class Operation {
		public static final String CREATE = "1";
		public static final String RETRIEVE = "2";
		public static final String UPDATE = "3";
		public static final String DELETE = "4";
		public static final String NOTIFY = "5";
	}

	// TODO: onem2m mime types: TS0004 section 6.7
	public static class ContentFormat {
		public static final String JSON = "json";
		public static final String XML = "xml";
	}
	
	public static class ContentType {
		public static final String JSON = "application/json";
		public static final String XML = "application/xml";
		public static final String TEXT = "text/plain";
		
	}

	public static class Protocol {
		public static final String COAP = "Coap";
		public static final String MQTT = "Mqtt";
		public static final String HTTP = "Http";
		public static final String NATIVEAPP = "NativeApp";
	}

	public static class ResourceType {
		public static final String AE = "2"; //"ae";
		public static final String CONTAINER = "3"; //"cnt";
		public static final String CONTENT_INSTANCE = "4"; //"cin";
		public static final String CSE_BASE = "5"; //"csb";
		public static final String GROUP = "9"; //"grp";
		public static final String SUBSCRIPTION = "23"; //"sub";
	}

	public static class ResponseType { // TS0001 section 8.2.1
		public static final String NON_BLOCKING_REQUEST_SYNCH = "1";
		public static final String NON_BLOCKING_REQUEST_ASYNCH = "2";
		public static final String BLOCKING_REQUEST = "3";
	}

	public static class NotificationContentType {
		public static final String MODIFIED_ATTRIBUTES = "1";
		public static final String WHOLE_RESOURCE = "2";
		public static final String REFERENCE_ONLY = "3";
	}

	public static class DiscoveryResultType {
		public static final String HIERARCHICAL = "1";
		public static final String NON_HIERARCHICAL = "2";

	}

	public static class FilterUsageType {
		public static final String DISCOVERY = "1";
		public static final String EVENT_NOTIFICATION_CRITERIA = "2";

	}

	public static class ResponseStatusCode {
		public static final String OK = "2000";
		public static final String CREATED = "2001";
		public static final String DELETED = "2002";
		public static final String CHANGED = "2004";

		public static final String BAD_REQUEST = "4000";
		public static final String NOT_FOUND = "4004";
		public static final String OPERATION_NOT_ALLOWED = "4005";
		public static final String CONTENTS_UNACCEPTABLE = "4102";
		public static final String CONFLICT = "4105";

		public static final String INTERNAL_SERVER_ERROR = "5000";
		public static final String NOT_IMPLEMENTED = "5001";
		public static final String ALREADY_EXISTS = "5106";
		public static final String TARGET_NOT_SUBSCRIBABLE = "5203";
		public static final String NON_BLOCKING_REQUEST_NOT_SUPPORTED = "5206";

		public static final String INVALID_ARGUMENTS = "6023";
		public static final String INSUFFICIENT_ARGUMENTS = "6024";
	}

	public static class CoapOption {
		public static final int ONEM2M_FR = 256;
		public static final int ONEM2M_RQI = 257;
		public static final int ONEM2M_NM = 258;
		public static final int ONEM2M_OT = 259;
		public static final int ONEM2M_RQET = 260;
		public static final int ONEM2M_RSET = 261;
		public static final int ONEM2M_OET = 262;
		public static final int ONEM2M_RTURI = 263;
		public static final int ONEM2M_EC = 264;
		public static final int ONEM2M_RSC = 265;
		public static final int ONEM2M_GID = 266;
	}

	public static class CommonResource {
		public static final String RESOURCE_TYPE = "rty";
		public static final String RESOURCE_ID = "ri";
		public static final String RESOURCE_NAME = "rn";
		public static final String PARENT_ID = "pi";
		public static final String EXPIRATION_TIME = "et";
		public static final String CREATION_TIME = "ct";
		public static final String LAST_MODIFIED_TIME = "lt";
		public static final String LABELS = "lbl";
		public static final String STATE_TAG = "st";
		public static final String CHILD_RESOURCE = "ch";
		public static final String CHILD_RESOURCE_REF = "chr";
	}

	public static class AE {
		public static final String APP_NAME = "apn";
		public static final String APP_ID = "api";
		public static final String AE_ID = "aei";
		public static final String POINT_OF_ACCESS = "apn";
		public static final String ONTOLOGY_REF = "or";
//		public static final String NODE_LINK = "nl"; // do not support node resource yet
	}

	public static class Container {
		public static final String CREATOR = "cr";
		public static final String MAX_NR_INSTANCES = "mni";
		public static final String MAX_BYTE_SIZE = "mbs";
		public static final String MAX_INSTANCE_AGE = "mia";
		public static final String CURR_NR_INSTANCES = "cni";
		public static final String CURR_BYTE_SIZE = "cbs";
		//public static final String LOCATION_ID = "li";
		public static final String ONTOLOGY_REF = "or";
		public static final String LATEST = "la";
		public static final String OLDEST = "ol";
	}

	public static class ContentInstance {
		public static final String CONTENT_INFO = "cnf";
		public static final String CONTENT_SIZE = "cs";
		public static final String CONTENT = "con";
		public static final String ONTOLOGY_REF = "or";
		public static final String NEXT = "next";
		public static final String PREV = "prev";
	}
	
	public static class EncodingType {
		public static final String PLAIN_TEXT = "0";
		public static final String BASE64_STRING = "1";
		public static final String BASE64_BINARY = "2";
	}
	
	public static class Subscription {
		public static final String NOTIFICATION_URI = "nu";
	    public static final String NOTIFICATION_CONTENT_TYPE = "nct";
	    public static final String NOTIFICATION_EVENT_CAT = "nec";
	    /**
	     * URI that is sent a notification when this <subscription> is deleted.
	     * @author Bowman
	     */
	    public static final String SUBSCRIBER_URI = "su";
	}
	
	public static class HeaderAttr{
		/**
		 * OM2M from parameter for http binding
		 * @author Bowman
		 */
		public static final String X_M2M_ORIGIN = "X-M2M-Origin";
		/**
		 * OM2M requestIdentifier parameter for http binding
		 * @author Bowman
		 */
	    public static final String X_M2M_RI = "X-M2M-RI";
	    /**
		 * OM2M resourceName parameter for http binding
		 * @author Bowman
		 */
	    public static final String X_M2M_NM = "X-M2M-NM";
	    /**
		 * OM2M Content-Type parameter for http binding
		 * @author Bowman
		 */
	    public static final String CONTENT_TYPE = "Content-Type";
	    /**
		 * OM2M Content-Type parameter JSON type value for http binding.<br>
		 * <b>USAGE</b>: new String contentType =  CONTENT_TYPE_JSON_VALUE + ResourceType.AE;
		 * @author Bowman
		 */
	    public static final String CONTENT_TYPE_JSON_VALUE = "application/vnd.onem2m-res+json;ty=";
	    /**
		 * OM2M Content-Type parameter XML type value for http binding.<br>
		 * <b>USAGE</b>: new String contentType =  CONTENT_TYPE_JSON_VALUE + ResourceType.AE;
		 * @author Bowman
		 */
	    public static final String CONTENT_TYPE_XML_VALUE = "application/vnd.onem2m-res+xml;ty=";
	    
	    public static final String CONTENT_TYPE_XML = "application/vnd.onem2m-res+xml";
	    
	    public static final String ACCEPT = "Accept";
	    
	    
//	    public static final String X_M2M_GID = "X-M2M-GID";
//	    public static final String X_M2M_RTU = "X-M2M-RTU";
//	    public static final String X_M2M_OT = "X-M2M-OT";
//	    public static final String X_M2M_RSC = "X-M2M-RSC";
	}
	
	public static class EventNotificationCriteria {
		public static final int UPDATE_OF_RESOURCE = 1;
		public static final int DELETE_OF_RESOURCE = 2;
		public static final int CREATE_OF_DIRECT_CHILD_RESOURCE = 3;
		public static final int DELETE_OF_DIRECT_CHILD_RESOURCE = 4;
	}
	
	/**
	 * Used for specifying content type when it retrieve resources from server. 
	 * The name of variables shows what it does.
	 * @author Bowman
	 */
	public static class ResultContent {
		public static String NOTHING = "0";
		public static String ATTRIBUTES = "1";
		public static String HIERARCHICAL_ADDRESS = "2";
		public static String HIERARCHICAL_ADDRESS_AND_ATTRIBUTES = "3";
		public static String ATTRIBUTES_AND_CHILD_RESOURCES = "4";
		public static String ATTRIBUTES_AND_CHILD_RESOURCE_REFERENCES = "5";
		public static String CHILD_RESOURCE_REFERENCES = "6";
		public static String ORIGINAL_RESOURCE = "7";
	}
	
	/**
	 * <p>
	 * Responponse values.
	 * </p>
	 * 
	 *<table>
	 *  <col width="50%"/>
	 *  <col width="50%"/>
	 *  <thead>
	 *       <tr><th>oneM2M Response Status Codes </th><th>HTTP Status Codes</th></tr>
	 *  <thead>
	 *  <tbody>
	 *   	 <tr><td>1000 (ACCEPTED)								   </td><td>202 (Accepted)              </td></tr>
     *       <tr><td>2000 (OK)                                         </td><td>200 (OK)                    </td></tr>
     *       <tr><td>2001 (CREATED)                                    </td><td>201 (Created)               </td></tr>
     *       <tr><td>4000 (BAD_REQUEST)                                </td><td>400 (Bad Request)           </td></tr>
     *       <tr><td>4004 (NOT_FOUND)                                  </td><td>404 (Not Found)             </td></tr>
     *       <tr><td>4005 (OPERATION_NOT_ALLOWED)                      </td><td>405 (Method Not Allowed)    </td></tr>
     *       <tr><td>4008 (REQUEST_TIMEOUT)                            </td><td>408 (Request Timeout)       </td></tr>
     *       <tr><td>4101 (SUBSCRIPTION_CREATOR_HAS_NO_PRIVILEGE)      </td><td>403 (Forbidden)             </td></tr>
     *       <tr><td>4102 (CONTENTS_UNACCEPTABLE)                      </td><td>400 (Bad Request)           </td></tr>
     *       <tr><td>4103 (ACCESS_DENIED)                              </td><td>403 (Forbidden)             </td></tr>
     *       <tr><td>4104 (GROUP_REQUEST_IDENTIFIER_EXISTS)            </td><td>409 (Conflict)              </td></tr>
     *       <tr><td>4015 (CONFLICT)                                   </td><td>409 (Conflict)              </td></tr>
     *       <tr><td>5000 (INTERNAL_SERVER_ERROR)                      </td><td>500 (Internal Server Error) </td></tr>
     *       <tr><td>5001 (NOT_IMPLEMENTED)                            </td><td>501 (Not Implemented)       </td></tr>
     *       <tr><td>5103 (TARGET_NOT_REACHABLE)                       </td><td>404 (Not Found)             </td></tr>
     *       <tr><td>5105 (NO_PRIVILEGE)                               </td><td>403 (Forbidden)             </td></tr>
     *       <tr><td>5106 (ALREADY_EXISTS)                             </td><td>403 (Forbidden)             </td></tr>
     *       <tr><td>5203 (TARGET_NOT_SUBSCRIBABLE)                    </td><td>403 (Forbidden)             </td></tr>
     *       <tr><td>5204 (SUBSCRIPTION_VERIFICATION_INITIATION_FAILED)</td><td>500 (Internal Server Error) </td></tr>
     *       <tr><td>5205 (SUBSCRIPTION_HOST_HAS_NO_PRIVILEGE)         </td><td>403 (Forbidden)             </td></tr>
     *       <tr><td>5206 (NON_BLOCKING_REQUEST_NOT_SUPPORTED)         </td><td>501 (Not Implemented)       </td></tr>
     *       <tr><td>6003 (EXTERNAL_OBJECT_NOT_REACHABLE)              </td><td>404 (Not Found)             </td></tr>
     *       <tr><td>6005 (EXTERNAL_OBJECT_NOT_FOUND)                  </td><td>404 (Not Found)             </td></tr>
     *       <tr><td>6010 (MAX_NUMBER_OF_MEMBER_EXCEEDED)              </td><td>400 (Bad Request)           </td></tr>
     *       <tr><td>6011 (MEMBER_TYPE_INCONSISTENT)                   </td><td>400 (Bad Request)           </td></tr>
     *       <tr><td>6020 (MANAGEMENT_SESSION_CANNOT_BE_ESTABLISHED)   </td><td>500 (Internal Server Error) </td></tr>
     *       <tr><td>6021 (MANAGEMENT_SESSION_ESTABLISHMENT_TIMEOUT)   </td><td>500 (Internal Server Error) </td></tr>
     *       <tr><td>6022 (INVALID_CMDTYPE)                            </td><td>400 (Bad Request)           </td></tr>
     *       <tr><td>6023 (INVALID_ARGUMENTS)                          </td><td>400 (Bad Request)           </td></tr>
     *       <tr><td>6024 (INSUFFICIENT_ARGUMENT)                      </td><td>400 (Bad Request)           </td></tr>
     *       <tr><td>6025 (MGMT_CONVERSION_ERROR)                      </td><td>500 (Internal Server Error) </td></tr>
     *       <tr><td>6026 (CANCELLATION_FAILED)                        </td><td>500 (Internal Server Error) </td></tr>
     *       <tr><td>6028 (ALREADY_COMPLETE)                           </td><td>400 (Bad Request)           </td></tr>
     *       <tr><td>6029 (COMMAND_NOT_CANCELLABLE)                    </td><td>400 (Bad Request)           </td></tr>
	 *  </tbody>
	 *</table>
	 * @author Bowman
	 *
	 */
	public static class ResponseCodes {  // Just for informational
		public static class M2M {
			public static String ACCEPTED                                      =   "1000";
			public static String OK                                            =   "2000";
			public static String CREATED                                       =   "2001";
			public static String BAD_REQUEST                                   =   "4000";
			public static String NOT_FOUND                                     =   "4004";
			public static String OPERATION_NOT_ALLOWED                         =   "4005";
			public static String REQUEST_TIMEOUT                               =   "4008";
			public static String SUBSCRIPTION_CREATOR_HAS_NO_PRIVILEGE         =   "4101";
			public static String CONTENTS_UNACCEPTABLE                         =   "4102";
			public static String ACCESS_DENIED                                 =   "4103";
			public static String GROUP_REQUEST_IDENTIFIER_EXISTS               =   "4104";
			public static String CONFLICT                                      =   "4015";
			public static String INTERNAL_SERVER_ERROR                         =   "5000";
			public static String NOT_IMPLEMENTED                               =   "5001";
			public static String TARGET_NOT_REACHABLE                          =   "5103";
			public static String NO_PRIVILEGE                                  =   "5105";
			public static String ALREADY_EXISTS                                =   "5106";
			public static String TARGET_NOT_SUBSCRIBABLE                       =   "5203";
			public static String SUBSCRIPTION_VERIFICATION_INITIATION_FAILED   =   "5204";
			public static String SUBSCRIPTION_HOST_HAS_NO_PRIVILEGE            =   "5205";
			public static String NON_BLOCKING_REQUEST_NOT_SUPPORTED            =   "5206";
			public static String EXTERNAL_OBJECT_NOT_REACHABLE                 =   "6003";
			public static String EXTERNAL_OBJECT_NOT_FOUND                     =   "6005";
			public static String MAX_NUMBER_OF_MEMBER_EXCEEDED                 =   "6010";
			public static String MEMBER_TYPE_INCONSISTENT                      =   "6011";
			public static String MANAGEMENT_SESSION_CANNOT_BE_ESTABLISHED      =   "6020";
			public static String MANAGEMENT_SESSION_ESTABLISHMENT_TIMEOUT      =   "6021";
			public static String INVALID_CMDTYPE                               =   "6022";
			public static String INVALID_ARGUMENTS                             =   "6023";
			public static String INSUFFICIENT_ARGUMENT                         =   "6024";
			public static String MGMT_CONVERSION_ERROR                         =   "6025";
			public static String CANCELLATION_FAILED                           =   "6026";
			public static String ALREADY_COMPLETE                              =   "6028";
			public static String COMMAND_NOT_CANCELLABLE                       =   "6029";
		}
		
		public static class HTTP {
			public static String ACCEPTED                                      =   "202";
			public static String OK                                            =   "200";
			public static String CREATED                                       =   "201";
			public static String BAD_REQUEST                                   =   "400";
			public static String NOT_FOUND                                     =   "404";
			public static String OPERATION_NOT_ALLOWED                         =   "405";
			public static String REQUEST_TIMEOUT                               =   "408";
			public static String SUBSCRIPTION_CREATOR_HAS_NO_PRIVILEGE         =   "403";
			public static String CONTENTS_UNACCEPTABLE                         =   "400";
			public static String ACCESS_DENIED                                 =   "403";
			public static String GROUP_REQUEST_IDENTIFIER_EXISTS               =   "409";
			public static String CONFLICT                                      =   "409";
			public static String INTERNAL_SERVER_ERROR                         =   "500";
			public static String NOT_IMPLEMENTED                               =   "501";
			public static String TARGET_NOT_REACHABLE                          =   "404";
			public static String NO_PRIVILEGE                                  =   "403";
			public static String ALREADY_EXISTS                                =   "403";
			public static String TARGET_NOT_SUBSCRIBABLE                       =   "403";
			public static String SUBSCRIPTION_VERIFICATION_INITIATION_FAILED   =   "500";
			public static String SUBSCRIPTION_HOST_HAS_NO_PRIVILEGE            =   "403";
			public static String NON_BLOCKING_REQUEST_NOT_SUPPORTED            =   "501";
			public static String EXTERNAL_OBJECT_NOT_REACHABLE                 =   "404";
			public static String EXTERNAL_OBJECT_NOT_FOUND                     =   "404";
			public static String MAX_NUMBER_OF_MEMBER_EXCEEDED                 =   "400";
			public static String MEMBER_TYPE_INCONSISTENT                      =   "400";
			public static String MANAGEMENT_SESSION_CANNOT_BE_ESTABLISHED      =   "500";
			public static String MANAGEMENT_SESSION_ESTABLISHMENT_TIMEOUT      =   "500";
			public static String INVALID_CMDTYPE                               =   "400";
			public static String INVALID_ARGUMENTS                             =   "400";
			public static String INSUFFICIENT_ARGUMENT                         =   "400";
			public static String MGMT_CONVERSION_ERROR                         =   "500";
			public static String CANCELLATION_FAILED                           =   "500";
			public static String ALREADY_COMPLETE                              =   "400";
			public static String COMMAND_NOT_CANCELLABLE                       =   "400";

		}
	}
}

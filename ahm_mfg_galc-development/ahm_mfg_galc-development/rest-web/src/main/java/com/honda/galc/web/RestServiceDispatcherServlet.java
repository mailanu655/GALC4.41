package com.honda.galc.web;

import java.io.IOException;
import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.property.RestServicePropertyBean;
import com.honda.galc.rest.common.ContentType;
import com.honda.galc.rest.security.BasicAuthDecoder;
import com.honda.galc.rest.security.BasicAuthFilter;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.web.service.RestService;

/**
 * @author Subu Kathiresan
 * @Date April 03, 2013
 *
 */
public class RestServiceDispatcherServlet extends HttpServlet {
	
	private static final String LOGGER_ID = "RestService";
	
	private static final int RESPONSE_CONTENT_TYPE_POS = 2;

	private static final long serialVersionUID = -7757313066765817060L;

	/**
	 * initialize servlet
	 */
	public void init() throws ServletException {
		try {
			new InitialContext();
		} catch (Exception ex) {
			throw new ServletException("RestServiceDispatcherServlet could not be initialized", ex);
		}
	}

	/**
	 * processes the incoming http GET request and returns the Web service results
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {
		try {
			handleRequest(request, response);
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex, "Could not process GET request form " + getClientIpAddress(request));
		}
	}

	/**
	 * processes the incoming http POST request and returns the Web service results
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
		throws javax.servlet.ServletException, java.io.IOException {
		try {
			handleRequest(request, response);
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex, "Could not process POST request form " + getClientIpAddress(request));
		}	
	}
	
	private void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		if (authenticateUser(request)) {
			processHttpRequest(request, response);
		} else {
			reportAuthFailure(response);
		}
	}
	
	private boolean authenticateUser(HttpServletRequest request) {
		if (PropertyService.getPropertyBean(RestServicePropertyBean.class).isUserAuthenticationEnabled()) {
			BasicAuthFilter authFilter = new BasicAuthFilter();
			try {
				String[] authInfo = BasicAuthDecoder.decode(request.getHeader(BasicAuthFilter.AUTHORIZATION_HEADER));
				getLogger().info(request.getRequestURI() 
						+ (request.getQueryString() == null ? "" : "?" + request.getQueryString())
						+ " requested by " + authInfo[BasicAuthFilter.USER_ID_INDEX]);
				if (!authFilter.authenticateUser(authInfo[BasicAuthFilter.USER_ID_INDEX], authInfo[BasicAuthFilter.PWD_INDEX])) {
					throw new WebApplicationException(Status.UNAUTHORIZED);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				return false;
			}
		} 
		return true;
	}

	/**
	 * URL structure A: http://{base url}/RestWeb/{service}/{method}?{(parameter type)parameter value}&{(parameter type)parameter value}
	 *                            <1>      <2>       <3>      <4>
	 * URL structure B: http://{base url}/RestWeb/{result content type}/{service}/{method}?{(parameter type)parameter value}&{(parameter type)parameter value}
	 *                            <1>      <2>                             <3>      <4>
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void processHttpRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		ArrayList<String> args = getArgs(request.getRequestURI().replaceFirst("\\..*$", "").split("/"));
		RestService restService = new RestService(request, response);
		
		setContentTypes(restService, args);
		args = trimArgs(args);
				
		if (args.size() < 3) { 	// no bean/method name provided
			getLogger().info("Received REST service directory lookup from " + getClientIpAddress(request));
			response.getWriter().println(restService.getAvailableServicesList(restService.getResContentType()));
			return;
		}
		String beanName = args.get(2);
		if (args.size() < 4) { 	// no method name provided
		
			getLogger().info("Received REST service method lookup for " + beanName + " from " + getClientIpAddress(request));
			response.getWriter().println(restService.getAvailableMethodsList(restService.getResContentType(), beanName));
			return;
		}
		String methodName = args.get(3);
		getLogger().info("Received REST Service request " + beanName + "." + methodName + " from " + getClientIpAddress(request));
		response.getWriter().println(restService.invokeMethod(beanName, methodName));
	}
	
	private void setContentTypes(RestService restService, ArrayList<String> args) {
		ContentType reqContentType = ContentType.get(restService.getRequest().getContentType());
		restService.setReqContentType(reqContentType == null ? ContentType.JSON : reqContentType);

		ContentType responseContentType = getContentType(args);
		restService.setResContentType(responseContentType);
		setResponseContentType(restService.getResponse(), responseContentType);	
	}
	
	public void setResponseContentType(HttpServletResponse response, ContentType responseContentType) {
		
		switch(responseContentType) {
		case JSON:
			response.setContentType("application/json");
			break;
		case XML:
			response.setContentType("application/xml");
			break;
		default:
			break;
		}
	}

	public ArrayList<String> getArgs(String[] uri) {
		ArrayList<String> args = new ArrayList<String>();
		for (String arg: uri) {
			args.add(arg);
		}
		return args;
	}

	public ContentType getContentType(ArrayList<String> args) {
		try {
			if (ContentType.get(args.get(2)) != null) {
				return ContentType.get(args.get(2));
			}
		} catch (Exception ex) {}
		
		return ContentType.JSON;
	}
	
	public ArrayList<String> trimArgs(ArrayList<String> args) {
		ArrayList<String> trimmedArgs = new ArrayList<String>();
		int i = -1;
		for (String arg: args) {
			i++;
			if (i == RESPONSE_CONTENT_TYPE_POS) {
				if (ContentType.get(arg) != null) {
					continue;
				} 
			} 
			trimmedArgs.add(arg);
		}
		return trimmedArgs;
	}
	
	public String getClientIpAddress(HttpServletRequest request) { 
		String[] headerNames = {"X-Forwarder-For", 
								"Proxy-Client-IP", 
								"WL-Proxy-Client-IP", 
								"HTTP_X_FORWARDED_FOR", 
								"HTTP_X_FORWARDED", 
								"HTTP_X_CLUSTER_CLIENT_IP", 
								"HTTP_CLIENT_IP", 
								"HTTP_FORWARDED_FOR", 
								"HTTP_FORWARDED", 
								"HTTP_VIA", 
								"REMOTE_ADDR"};
		
		for(String headerName : headerNames) {
		     String ip = request.getHeader(headerName);
		     if(!StringUtils.isEmpty(ip) && !ip.equalsIgnoreCase("unknown")) 
		    	 return ip;
		}
		return request.getRemoteAddr();  
	}

	private void reportAuthFailure(HttpServletResponse response) throws IOException {
		String authFailureWarning = "Unable to process request: Authentication failure";
		response.getWriter().println(authFailureWarning);
		getLogger().warn(authFailureWarning);
	}
	
	private Logger getLogger(){
		return Logger.getLogger(LOGGER_ID);
	}
}
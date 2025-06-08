package com.honda.galc.web;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * @author Subu Kathiresan
 * @Data April 03, 2012
 *
 */
public class WebServiceDispatcherServlet extends HttpServlet {

	/**
	 * All web GALC web services will be in this package.  Hard-coding
	 * this package name protects us from exposing classes that are not
	 * meant to be web services  
	 */
	public static final String SERVICE_PACKAGE = "com.honda.galc.web.service";

	private static final long serialVersionUID = -7757313066765817060L;

	/**
	 * initialize servlet
	 */
	public void init() throws ServletException {
		try {
			InitialContext context = new InitialContext();
		} catch (Exception ex) {
			throw new ServletException("WebServiceDispatcherServlet could not be initialized", ex);
		}
	}

	/**
	 * processes the incoming http GET request and returns the Web service results
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {
		try {
			// get client name
			String[] uri = request.getRequestURI().replaceFirst("\\..*$", "").split("/");
			String serviceName = uri[uri.length - 2];
			String methodName = uri[uri.length - 1];
			System.out.println("Received Web Service request for " + serviceName + "." + methodName);
			response.getWriter().println(invokeServiceEndPoint(serviceName, methodName));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}
	
	public String invokeServiceEndPoint(String className, String methodName, String...params) {
		Method targetMethod = null;
		String jsonVal = "";
		
		String serviceClassName = SERVICE_PACKAGE + "." +  className;
		try {
			
			Class<?> clazz = Class.forName(serviceClassName);
			targetMethod = clazz.getDeclaredMethod(methodName);
			Object returnObj  = targetMethod.invoke(clazz.newInstance());
			Gson gson = new Gson();
			jsonVal = gson.toJson(returnObj);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return jsonVal;
	}
}

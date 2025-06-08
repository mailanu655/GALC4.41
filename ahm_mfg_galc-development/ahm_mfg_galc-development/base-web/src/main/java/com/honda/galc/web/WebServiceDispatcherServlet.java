package com.honda.galc.web;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.honda.galc.common.logging.Logger;

/**
 * @author Subu Kathiresan
 * @Date April 03, 2012
 *
 */
public class WebServiceDispatcherServlet extends HttpServlet {

	/**
	 * All GALC web services will be housed in this package.  This constant
	 * protects us from exposing classes that are not meant to be web services  
	 */
	public static final String SERVICE_PACKAGE = "com.honda.galc.web.service";
	private static final String LOGGER_ID = "WebService";

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
			getLogger().info("Received Web Service request from " + getClientAddress(request) + ": " + serviceName + "." + methodName);
			Object[] params = getMethodParameters(request, serviceName, methodName);
			response.getWriter().println(invokeServiceEndPoint(serviceName, methodName, params));
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex, "Could not process GET request form " + getClientAddress(request));
		}
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}
	
	public String invokeServiceEndPoint(String className, String methodName, Object...params) {
		Method targetMethod = null;
		String jsonVal = "";
		
		String serviceClassName = SERVICE_PACKAGE + "." +  className;
		try {
			Class<?> clazz = Class.forName(serviceClassName);
			try {
				targetMethod = clazz.getDeclaredMethod(methodName);
			}catch(NoSuchMethodException e){
				targetMethod = clazz.getDeclaredMethod(methodName, Map.class);
			}
			Object returnObj  = targetMethod.invoke(clazz.newInstance(), params);
			Gson gson = new Gson();
			jsonVal = gson.toJson(returnObj);
			getLogger().debug("Json returned for " + className + "." + methodName + ": " + jsonVal);
			getLogger().info("Service End point " + className + "." + methodName + " invoked successfully");	
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex, "Service End point " + className + "." + methodName + " could not be invoked");
		}
		return jsonVal;
	}
	
	private String getClientAddress(HttpServletRequest request) {
        return request.getRemoteHost() + "/" + request.getRemoteAddr();
	}
	
	private Logger getLogger(){
		return Logger.getLogger(LOGGER_ID);
	}
	
	/**
	 * Gets the method parameters. This function now supports two types of fuction parameters:
	 * <li>If the function has no parameter, the function will retrun an empty object array (Object[0]).
	 * <li>If the function has a map as parameter(e.g getInventory(Map<?, ?>)), the function will return an object array with Object[0]=a map.
	 * The map will have the request parameters and values. The map key is the request parameter name. 
	 * If the request parameter value is a single value, the map value is the request parameter value. 
	 * If the request parameter value is an array, the map value is the array of request parameter value.
	 * <br>
	 * <b>Other function parameters are not supported yet.<b/>
	 * @param request the request
	 * @param className the class name
	 * @param methodName the method name
	 * @return the method parameters
	 */
	private Object[] getMethodParameters(HttpServletRequest request,
			String className, String methodName) {
		Method targetMethod = null;
		//for now, we only support Map<String, Object> as the method parameter
		String serviceClassName = SERVICE_PACKAGE + "." +  className;
		try {
			
			Class<?> clazz = Class.forName(serviceClassName);
			targetMethod = clazz.getDeclaredMethod(methodName, Map.class);
			if (targetMethod.getParameterTypes().length > 0) {
				Map<?,?> requestParams = request.getParameterMap();
				Map<String, Object> params = new HashMap<String, Object>(requestParams.size());
				for(Object obj : requestParams.keySet()){
					String key =  (String) obj;
					String[] values = request.getParameterValues(key);
					if (values != null && values.length == 1) {
						params.put(key, values[0]);
					} else {
						params.put(key, values);
					}
				}
				return new Object[] { params };
			}
		}
		catch(NoSuchMethodException e){
			
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return new Object[0];
	}
}

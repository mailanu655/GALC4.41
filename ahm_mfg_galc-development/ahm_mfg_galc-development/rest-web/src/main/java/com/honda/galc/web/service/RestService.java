package com.honda.galc.web.service;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.BeanFactoryUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.ApplicationContextProvider;
import com.honda.galc.rest.common.ContentType;
import com.honda.galc.rest.common.IContentHandler;
import com.honda.galc.rest.util.RestUtils;
import com.honda.galc.rest.util.UriParser;
import com.honda.galc.service.IService;
import com.honda.galc.util.ReflectionUtils;

/**
 * @author Subu Kathiresan
 * @date April 15, 2013
 */
public class RestService extends BaseWebService {
	
	private static final String LOGGER_ID = "RestService";
	
	private IContentHandler reqContentHandler = null; 
	private IContentHandler resContentHandler = null;
			
	private ContentType reqContentType = ContentType.JSON;
	private ContentType resContentType = ContentType.JSON;

	public RestService(HttpServletRequest request, HttpServletResponse response) {
		setRequest(request);
		setResponse(response);
	}
	
	/**
	 * invokes target REST method
	 * @param beanName
	 * @param methodName
	 * @return
	 */
	public String invokeMethod(String beanName, String methodName) {
		Object returnObj = null;
		String reqSignature = StringUtils.trimToEmpty(beanName) + "." + StringUtils.trimToEmpty(methodName);
		try {
			getResponse().setStatus(HttpServletResponse.SC_CREATED);
			Object bean = ApplicationContextProvider.getBean(beanName);
			Object[] parameters = getParameters(reqSignature);
			Method method = RestUtils.getMethod(bean, methodName, parameters);
			if (method != null) {
				returnObj = ReflectionUtils.invoke(bean, method, parameters);
			} else {
				getLogger().warn(RestUtils.getMethodSignature(beanName, methodName, parameters) + " does not exist");
				getResponse().setStatus(HttpServletResponse.SC_BAD_REQUEST);
				returnObj = RestUtils.getMethodSignature(beanName, methodName, parameters) + " does not exist";
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex, "Could not invoke " + beanName + "." + methodName);
			getResponse().setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		
		return getResContentHandler().getResponse(reqSignature, returnObj);
	}

	/**
	 * returns a list of available REST services
	 * @return
	 */
	public String getAvailableServicesList(ContentType resContentType) {
		String[] beans = {};
		try {
			// only retrieve beans from context that implement IService
			beans = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(ApplicationContextProvider.getApplicationContext(), IService.class);
			Arrays.sort(beans);
		} catch(Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex, "Could not create beans list");
		}
		
		return getResContentHandler().getResponse("available REST services listing", beans);
	}
	
	/**
	 * returns a list of available methods for the given REST service
	 * @param beanName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getAvailableMethodsList(ContentType resContentType, String beanName) {
		Class<?> implClass = ApplicationContextProvider.getBean(beanName).getClass();
		ArrayList<Method> methods = new ArrayList<Method>();
		
		// get all interfaces
		List<Class<?>> iFaces = ClassUtils.getAllInterfaces(implClass);
		for (Class<?> iFace: iFaces) {
			if (RestUtils.isSpringProxyInterface(iFace)) {
				continue;
			}
			// recursively get methods for all interfaces in the hierarchy 
			for (; iFace != null; iFace = iFace.getSuperclass()) {
				methods.addAll(Arrays.asList(iFace.getDeclaredMethods()));
			}
		}
		
		return getResContentHandler().getResponse(beanName + " method listing", RestUtils.getFormattedMethodsList(methods));
	}

	/**
	 * parse out parameters from url query string
	 *  
	 * @return
	 */
	public void getParametersFromQueryString(String reqSignature, ArrayList<Object> parameters) {
		if (getRequest().getQueryString() != null) {
			String q = getRequest().getQueryString();
			getLogger().info("Raw query string received for "
					+ reqSignature
					+ " from " + getClientAddress() 
					+ ": " 	+ q);
			try {
				q = URLDecoder.decode(q, "UTF8");
			} catch (UnsupportedEncodingException e) {
				getLogger().error(e, "Unable to decode URI, will attempt to process raw URI. " );
			}
			getLogger().info("Decoded REST parameters received in query string for "
					+ reqSignature
					+ " from " + getClientAddress() 
					+ ": " + q);
			String[] ParameterPairs =  q.split("&");
			for(String parameterPair: ParameterPairs) {
				UriParser.addQueryStringParam(parameters, parameterPair);
			}
		}
	}
	
	private Object[] getParameters(String reqSignature) {
		ArrayList<Object> parameters = getReqContentHandler().getParametersFromRequestBody(getRequest(), reqSignature);
		if (parameters.size() == 0) {
			getParametersFromQueryString(reqSignature, parameters);
		}
		return parameters.toArray();
	}
	
	private Logger getLogger(){
		return Logger.getLogger(LOGGER_ID);
	}

	protected IContentHandler getContentHandler(ContentType contentType) {
		try {
			Class<?>[] parameterTypes = {};
			Object[] parameters = {};
			Constructor<?> constructor = contentType.getHandler().getConstructor(parameterTypes);
			IContentHandler contentHandler = (IContentHandler) constructor.newInstance(parameters);
			contentHandler.setLogger(getLogger());
			contentHandler.setClientAddress(getClientAddress());
			return contentHandler;
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex, "Could not locate content handler for content type " + contentType);
			return null;
		}
	}
	
	public ContentType getReqContentType() {
		return reqContentType;
	}

	public void setReqContentType(ContentType reqContentType) {
		this.reqContentType = reqContentType;
	}
	
	
	public IContentHandler getReqContentHandler() {
		if (reqContentHandler == null) {
			reqContentHandler = getContentHandler(getReqContentType());
			reqContentHandler.setLogger(getLogger());
			reqContentHandler.setClientAddress(getClientAddress());
		}
		return reqContentHandler;
	}
	
	public void setReqContentHandler(IContentHandler reqContentHandler) {
		this.reqContentHandler = reqContentHandler;
	}

	public ContentType getResContentType() {
		return resContentType;
	}

	public void setResContentType(ContentType resContentType) {
		this.resContentType = resContentType;
	}
	
	public IContentHandler getResContentHandler() {
		if (resContentHandler == null) {
			resContentHandler = getContentHandler(getResContentType());
			resContentHandler.setLogger(getLogger());
			resContentHandler.setClientAddress(getClientAddress());
		}
		return resContentHandler;
	}

	public void setResContentHandler(IContentHandler resContentHandler) {
		this.resContentHandler = resContentHandler;
	}
}

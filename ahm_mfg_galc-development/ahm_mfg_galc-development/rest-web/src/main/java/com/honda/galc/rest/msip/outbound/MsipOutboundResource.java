package com.honda.galc.rest.msip.outbound;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import com.honda.galc.rest.msip.BaseMsipResource;
import com.honda.galc.rest.util.RestUtils;
import com.honda.galc.service.msip.dto.outbound.IMsipOutboundDto;
import com.honda.galc.util.ReflectionUtils;
import com.honda.galc.util.StringUtil;
import com.honda.galc.web.service.RestService;

/**
 * @author Subu Kathiresan
 * @date June 15, 2017
 */
@Path("msip/outbound/{interfaceName}/{resource}")
public class MsipOutboundResource extends BaseMsipResource {

	@SuppressWarnings("unchecked")
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	public <D extends IMsipOutboundDto> List<D> enrich(@Context HttpHeaders header, @Context HttpServletRequest request, 
    		@Context HttpServletResponse response, @PathParam("interfaceName") String interfaceName, 
    		@PathParam("resource") String resource) {
		
		try {
			String methodName = StringUtil.getMethodName(resource, METHOD_PREFIX);
			String invocationName = getInvocationName(interfaceName, methodName);
		
			Object bean = getHandler(interfaceName);
			Object[] parameters = getParameters(request, response, interfaceName);
			Method method = RestUtils.getMethod(bean, methodName, parameters);
			if (method != null) {
				getLogger(interfaceName).info("Attempting to invoke: " + invocationName);
				List<D> retVal = (List<D>) ReflectionUtils.invoke(bean, method, parameters);
				getLogger(interfaceName).info(interfaceName + " REST request completed");
				return retVal;
			} 
			getLogger(interfaceName).warn(invocationName + "() does not exist");
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger(interfaceName).error(ex, "Unable to process " +  interfaceName + " REST request");
		}
		throw new WebApplicationException(Status.BAD_REQUEST);
	}

	private String getInvocationName(String interfaceName, String methodName) {
		String invocationName = getPrefix(interfaceName) + SERVICE_SUFFIX + "." + methodName;
		return invocationName;
	}
	
	public Object[] getParameters(HttpServletRequest request, HttpServletResponse response, String interfaceName) {
		RestService restService = new RestService(request, response);
		ArrayList<Object> parameters = new ArrayList<Object>();
		restService.getParametersFromQueryString(interfaceName, parameters);
		return parameters.toArray();
	}
}

package com.honda.galc.rest.msip.inbound;

import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import com.google.gson.JsonElement;
import com.honda.galc.service.msip.dto.inbound.IMsipInboundDto;
import com.honda.galc.rest.json.JsonContentHandler;
import com.honda.galc.rest.json.ListJsonDeSerializer;
import com.honda.galc.rest.msip.BaseMsipResource;
import com.honda.galc.rest.util.RestUtils;
import com.honda.galc.util.ReflectionUtils;

/**
 * @author Subu Kathiresan, Jeffray Huang
 * @date May 3, 2017
 */
@Path("msip/inbound/{interfaceName}")
public class MsipInboundResource extends BaseMsipResource {
	
	private static final String DTO_PACKAGE_PREFIX = "com.honda.galc.service.msip.dto.inbound.";
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void update(@Context HttpHeaders header, @Context HttpServletRequest request, 
    		@Context HttpServletResponse response, @PathParam("interfaceName") String interfaceName, final String param) {
		try {
			Object bean = getHandler(interfaceName);
			Object[] parameters = {getParameterList(interfaceName, param)};
			Method method = RestUtils.getMethod(bean, METHOD_NAME, parameters);
			if (method != null) {
				getLogger(interfaceName).info("Attempting to save: " + parameters);
				ReflectionUtils.invoke(bean, method, parameters);
				getLogger(interfaceName).info(interfaceName + " REST request completed");
				return;
			} 
			getLogger(interfaceName).warn(interfaceName + "." + METHOD_NAME + " does not exist");
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger(interfaceName).error(ex, "Unable to process " +  interfaceName + " REST request");
		}
		throw new WebApplicationException(Status.BAD_REQUEST);
    }

	private List<IMsipInboundDto> getParameterList(String interfaceName, final String param) throws ClassNotFoundException {
		String dtoClass = getPrefix(interfaceName) + DTO_SUFFIX; 
		@SuppressWarnings("unchecked")
		Class<IMsipInboundDto> clazz = (Class<IMsipInboundDto>) Class.forName(DTO_PACKAGE_PREFIX + dtoClass);
		JsonElement json = JsonContentHandler.getGson().fromJson (param, JsonElement.class);
		List<IMsipInboundDto> dtoList = ListJsonDeSerializer.deserialize(json, clazz);
		return dtoList;
	}
}

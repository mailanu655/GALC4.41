package com.honda.galc.rest.product;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.product.PartSnNoValidate;
import com.honda.galc.service.PartSerialValidatorService;
import com.honda.galc.service.ServiceFactory;

@Path("/partSrNoValidateService")
public class PartSrNoValidateService {
	
    @Context
    private HttpServletResponse response;
	
	
	private static  class MyResponse  {
    	String message;
    	MyResponse(){}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		
		
    }
	
    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public MyResponse partSerialNumberValidate(String partSnNoValidateString) {
    	PartSnNoValidate  partSnNoValidate = null;
     	String status = "";
     	MyResponse myResponse = new MyResponse();
       	try {
       		partSnNoValidate = convertPartSpecJsonToObject(partSnNoValidateString);
			if (partSnNoValidate == null) {
				myResponse.setMessage("Incorrect input");
				return myResponse;
			}
		   	status = ServiceFactory.getService(PartSerialValidatorService.class).checkPartSerialNumberMask(partSnNoValidate);
		   	myResponse.setMessage(status);
		    return myResponse;
    	} catch (Exception ex)  { //if exception => input json not acceptable
    		String msg = "Unable to parse json object " + ex.getMessage();
			getLogger().error(ex, msg);
			myResponse.setMessage("Exception Occured");
			return myResponse;
    	}
     
    }
	/**
	 * Method converts json string to object using gson
	 * @param partSnNoValidateString - json object for conversion to object 
	 * @return PartSnNoValidate
	 */
    private PartSnNoValidate convertPartSpecJsonToObject(String partSnNoValidateString)  {
    	ObjectMapper mapper = new ObjectMapper();
    	PartSnNoValidate partSnNoValidate = null;
    	try {
    		if(!StringUtils.isBlank(partSnNoValidateString))  {
    			mapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
    			partSnNoValidate = mapper.readValue(partSnNoValidateString, PartSnNoValidate.class);
    		}
		} catch (Exception e) {
			getLogger().error(e, "Unable to parse json object " + e.getMessage());
		}
    	return partSnNoValidate;
    }
    /**
     * @return Logger object
     */
	public Logger getLogger() {
		return Logger.getLogger(this.getClass().getSimpleName());
	}
}

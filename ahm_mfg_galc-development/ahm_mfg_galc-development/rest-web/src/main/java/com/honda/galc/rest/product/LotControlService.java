 
package com.honda.galc.rest.product;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.honda.galc.service.LotControlRepairService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dto.rest.RepairDefectDto;

@Path("/lotControlService")
public class LotControlService {
    @Context
    private HttpServletRequest httpServletRequest;
    private LotControlRepairService serviceImpl = null;
    
    class MyResponse  {
    	String message;
    	String extSysName;
    	Long extSystemKey;
    	MyResponse(){}
    	MyResponse(String name, Long key){
    		extSysName = name;  extSystemKey = key;		
    	}
		public String getMessage() {
			return message;
		}
		public String getExtSysName() {
			return extSysName;
		}
		public Long getExtSystemKey() {
			return extSystemKey;
		}
    }
	/**
     * Default constructor. 
     */
    public LotControlService() {
        // TODO Auto-generated constructor stub
    }


    @PUT
	@Path("repair")
	@Consumes({"application/json"})
	@Produces("text/html")
	public Response repair(String jsonToRepair, @Context UriInfo uriInfo) { 
    	boolean isGood = false;
    	String json = "";
    	ObjectMapper mapper = new ObjectMapper();
       	MyResponse resp = new MyResponse();
       	RepairDefectDto dto = null;
       	try {
    	    dto = convertRepairJsonToDto(jsonToRepair);
			if (dto == null) {
	    		return Response.noContent().build();
			}
    	} catch (Exception ex)  { //if exception => input json not acceptable
			getLogger().error(ex, "repair: Error parsing json input");
       		return Response.status(Status.NOT_ACCEPTABLE).entity(jsonToRepair).build();
    	}
    	try  {
			resp.extSystemKey = dto.getExternalSystemKey();
			resp.extSysName = dto.getExternalSystemName();
			boolean retStatus = getServiceImpl().repairBuildResult(dto);
			if(retStatus)  {
				isGood = true;  //set status to good for further formatting
			}
		} catch (Exception e) {  //some exception in repair, send not acceptable and log error
			getLogger().error(e, "LotControlService: headless repair");
       		return Response.status(Status.NOT_ACCEPTABLE).entity(jsonToRepair).build();
		}
		try {
			json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(resp);
			if(isGood)  {
				return Response.status(Status.ACCEPTED).entity(json).build();
			}
			else  {
				return Response.status(Status.NOT_ACCEPTABLE).entity(json).build();				
			}
		} catch (JsonProcessingException e) {
			String msg = "repair: Unable to create json response" + e.getMessage();
			getLogger().error(e, msg);
		}
    	
    	//if it came here, it means there was a formatting error, send back an unformatted Response entity
		if(isGood)  {
			return Response.ok(resp).build();
    	}
    	else  {
    		return Response.status(Status.NOT_ACCEPTABLE).entity(resp).build();
    	}
	}

    private RepairDefectDto convertRepairJsonToDto(String json)  {
    	ObjectMapper mapper = new ObjectMapper();
    	RepairDefectDto dto = null;
    	try {
    		if(!StringUtils.isBlank(json))  {
	    	    mapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
	    	    dto = mapper.readValue(json, RepairDefectDto.class);
    		}
		} catch (Exception e) {
			String msg = "repair: Unable to parse repair json" + e.getMessage();
			getLogger().error(e, msg);
		}
    	return dto;
    }
    
	public LotControlRepairService getServiceImpl() {
		if(serviceImpl == null)  {
			serviceImpl = ServiceFactory.getService(LotControlRepairService.class);
		}
		return serviceImpl;
	}

	public Logger getLogger() {
		return Logger.getLogger(this.getClass().getSimpleName());
	}



    
}
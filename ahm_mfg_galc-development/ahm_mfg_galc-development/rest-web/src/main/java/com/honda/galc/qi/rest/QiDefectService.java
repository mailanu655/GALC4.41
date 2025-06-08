 
package com.honda.galc.qi.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.honda.galc.dao.qi.QiDefectResultDao;
import com.honda.galc.dao.qi.QiExternalSystemDefectIdMapDao;
import com.honda.galc.dto.qi.QiCreateDefectDto;
import com.honda.galc.dto.qi.QiRepairDefectDto;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiExternalSystemDefectIdMap;
import com.honda.galc.service.QiHeadlessDefectService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.common.logging.Logger;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.List;

@Path("/qiDefectService")
public class QiDefectService  {
    @Context
    private HttpServletRequest httpServletRequest;
    private QiHeadlessDefectService serviceImpl = null;
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
    public QiDefectService() {
        // TODO Auto-generated constructor stub
    }


    /**
     * Retrieves representation of an instance of ProductSequence
     * @return an instance of String
     */
	@GET
	@Path("getDefect")
	@Consumes("text/html")
	@Produces("text/html")
	public Response resourceMethodGet(
	        @DefaultValue("") @QueryParam("extSysName") String extSysName,
	        @DefaultValue("0") @QueryParam("extSysKey") Long extSysKey          
			)
	{ 
    	ObjectMapper mapper = new ObjectMapper();
    	String json = "";
		String extSysDesc = extSysName + ":" + extSysKey;
    	QiDefectResult qiDefect = null;
       	boolean isGood = false;
       	
       	//if input parameters are insufficient, send back no content
		if (StringUtils.isBlank(extSysName) || extSysKey == 0) {
    		return Response.noContent().build();
		}
		//try to get defect map and defect.  if defect is found, set flag to good.
    	try {
        	QiExternalSystemDefectIdMap defectMap = getDao(QiExternalSystemDefectIdMapDao.class).findByExternalSystemKey(extSysName, extSysKey);
        	if(defectMap != null)  {
            	qiDefect = getDao(QiDefectResultDao.class).findByKey(defectMap.getId().getDefectResultId());       		
        	}
        	if(qiDefect != null)  {
         		isGood = true;
        	}
		} catch (Exception e) {  //any exceptions in the dao call, send back error response
			getLogger().error(e, "resourceMethodGet");
       		return Response.status(Status.NOT_ACCEPTABLE).entity(extSysDesc).build();
		}
    	//at this point a defect is found or not
		try {
			//if defect found, send defect details back, else not found
			if(isGood)  {
				json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(qiDefect);
				return Response.status(Status.ACCEPTED).entity(json).build();
			}
			else  {
				return Response.status(Status.NOT_FOUND).entity(extSysDesc).build();				
			}
		} catch (JsonProcessingException e) {
			String msg = "resourceMethodGet: Unable to create json response" + e.getMessage();
			getLogger().error(e, msg);
		}
		//there was an error in creating json response, send back qiDefect
		return Response.status(Status.NOT_ACCEPTABLE).entity(qiDefect).build();
	}

    /**
     * Retrieves defects for a given productaa
     */
	@GET
	@Path("getDefectsForProduct")
	@Consumes("text/html")
	@Produces("text/html")
	public Response getDefectsForProduct(
	        @DefaultValue("") @QueryParam("productId") String productId
			)
	{ 
    	ObjectMapper mapper = new ObjectMapper();
    	String json = "";
    	List<QiDefectResult> qiDefects = null;
       	boolean isGood = false;
       	
       	//if input parameters are insufficient, send back no content
		if (StringUtils.isBlank(productId)) {
    		return Response.noContent().build();
		}
		//try to get defect map and defect.  if defect is found, set flag to good.
    	try {
        	qiDefects = getServiceImpl().getDefectsForProduct(productId);
        	if(qiDefects != null)  {
         		isGood = true;
        	}
		} catch (Exception e) {  //any exceptions in the dao call, send back error response
			getLogger().error(e, "resourceMethodGet");
       		return Response.status(Status.NOT_ACCEPTABLE).entity(productId).build();
		}
    	//at this point a defect is found or not
		try {
			//if defect found, send defect details back, else not found
			if(isGood)  {
				json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(qiDefects);
				return Response.status(Status.ACCEPTED).entity(json).build();
			}
			else  {
				return Response.status(Status.NOT_FOUND).entity(productId).build();				
			}
		} catch (JsonProcessingException e) {
			String msg = "resourceMethodGet: Unable to create json response" + e.getMessage();
			getLogger().error(e, msg);
		}
		//there was an error in creating json response, send back qiDefect
		return Response.status(Status.NOT_ACCEPTABLE).entity(qiDefects).build();
	}

	/**
     * PUT method for updating or creating an instance of ProductSequence
     * @content content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
	@PUT
	@Consumes({"application/json"})
	@Produces("text/html")
	public Response resourceMethodPut(String jsonToPut, @Context UriInfo uriInfo) { 
    	MyResponse resp = new MyResponse();
    	ObjectMapper mapper = new ObjectMapper();
    	QiCreateDefectDto dto = null;
    	boolean isGood = false;
       	//try to convert input json to dto
    	try {
			getLogger().info("QiDefetService::put:REST parameters received in JSON create: \n"
					+ jsonToPut);
					
    	    dto = convertJsonToDto(jsonToPut);
			if (dto == null) {  //if dto is null, then there is no content
	    		return Response.noContent().entity(jsonToPut).build();
			}
    	} catch (Exception ex)  {  //if there was an exception in marshaling to dto,  json not acceptable 
			getLogger().error(ex, "resourceMethodPut: Error parsing json input");
       		return Response.status(Status.NOT_ACCEPTABLE).entity(jsonToPut).build();
    	}
   		try  {
			resp.extSystemKey = dto.getExternalSystemKey();
			resp.extSysName = dto.getExternalSystemName();
			//try to create defect, no error => set flag for formatting response
			boolean status = getServiceImpl().createDefect(dto);
			if(status)  {
				isGood = true;
			}
		} catch (Exception e) {  //if exception, then json input not acceptable
			getLogger().error(e, "resourceMethodPut");
       		return Response.status(Status.NOT_ACCEPTABLE).entity(jsonToPut).build();
		}
		try {
			//convert response object to json
			String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(resp);
			//if status is good => accepted, else => not good
			if(isGood)  {
				return Response.status(Status.ACCEPTED).entity(json).build();
			}
			else  {
				return Response.status(Status.NOT_ACCEPTABLE).entity(json).build();				
			}
		} catch (JsonProcessingException e) {
			String msg = "resourceMethodPut: Unable to create json response" + e.getMessage();
			getLogger().error(e, msg);
		}
		//if it lands here, it means there was a json exception only
		if(isGood)  {  //if status was good, then defect was created
			return Response.status(Status.ACCEPTED).entity(resp).build();
		}
		else  {
			return Response.status(Status.NOT_ACCEPTABLE).entity(resp).build();				
		}
    	
	}
	

    @PUT
	@Path("repair")
	@Consumes({"application/json"})
	@Produces("text/html")
	public Response repair(String jsonToRepair, @Context UriInfo uriInfo) { 
    	String json = "";
    	ObjectMapper mapper = new ObjectMapper();
       	MyResponse resp = new MyResponse();
       	QiRepairDefectDto dto = null;
       	boolean isGood = false;
       	
       	//try to convert input json to dto
    	try {
			getLogger().info("QiDefetService::repair:REST parameters received in JSON repair: \n"
					+ jsonToRepair);
    	    dto = convertRepairJsonToDto(jsonToRepair);
			if (dto == null) { //if no conversion, then no content
	    		return Response.noContent().build();
			}
    	} catch (Exception ex)  { //if exception => input json not acceptable
			getLogger().error(ex, "resourceMethodPut: Error parsing json input");
       		return Response.status(Status.NOT_ACCEPTABLE).entity(jsonToRepair).build();
    	}
    	try  {
			resp.extSystemKey = dto.getExternalSystemKey();
			resp.extSysName = dto.getExternalSystemName();
			//try to repair defect
			boolean respStatus = getServiceImpl().repairDefect(dto);
			if(respStatus)  {
				isGood = true;  //set status to good for further formatting
			}
		} catch (Exception e) {//some exception in repair, send not acceptable and log error
			getLogger().error(e, "headless repair");
       		return Response.status(Status.NOT_ACCEPTABLE).entity(jsonToRepair).build();
		}
		try { //try to format response
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

 
    @PUT
	@Path("repairActualProblem")
	@Consumes({"application/json"})
	@Produces("text/html")
	public Response repairActualProblem(String jsonToRepair, @Context UriInfo uriInfo) { 
    	String json = "";
    	ObjectMapper mapper = new ObjectMapper();
       	MyResponse resp = new MyResponse();
       	QiRepairDefectDto dto = null;
       	boolean isGood = false;
       	
       	//try to convert input json to dto
    	try {
			getLogger().info("QiDefetService::repair:REST parameters received in JSON repair: \n"
					+ jsonToRepair);
    	    dto = convertRepairJsonToDto(jsonToRepair);
			if (dto == null) { //if no conversion, then no content
	    		return Response.noContent().build();
			}
    	} catch (Exception ex)  { //if exception => input json not acceptable
			getLogger().error(ex, "repairActualProblem: Error parsing json input");
       		return Response.status(Status.NOT_ACCEPTABLE).entity(jsonToRepair).build();
    	}
    	try  {
			//try to repair actual problem
			boolean respStatus = getServiceImpl().repairActualProblem(dto);
			if(respStatus)  {
				isGood = true;  //set status to good for further formatting
			}
		} catch (Exception e) {//some exception in repair, send not acceptable and log error
			getLogger().error(e, "headless repair");
       		return Response.status(Status.NOT_ACCEPTABLE).entity(jsonToRepair).build();
		}
		try { //try to format response
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

 
	/**
     * DELETE method for deleting an instance of QiDefectResult
     * @content content representation for the resource
     * @return an HTTP response with content of the deleted resource.
     */
	@DELETE
	@Consumes({"text/html"})
	@Produces("text/html")
	public Response resourceMethodDelete(
	        @DefaultValue("") @QueryParam("extSysName") String extSysName,
	        @DefaultValue("0") @QueryParam("extSysKey") Long extSysKey          
			) { 
    	String json = "";
		String extSysDesc = extSysName + ":" + extSysKey;
    	ObjectMapper mapper = new ObjectMapper();
       	MyResponse resp = new MyResponse();
       	boolean isGood = false;
       	//if input parameters are insufficient, send back no content
		if (StringUtils.isBlank(extSysName) || extSysKey == 0) {
    		return Response.noContent().build();
		}
		try {  //try to delete defect
			boolean respStatus = getServiceImpl().deleteDefect(extSysName, extSysKey);
			if(respStatus)  {
				isGood = true;  //set status to good for further formatting
			}
		} catch (Exception e) { //unable to delete, send back error response
			getLogger().error(e, "resourceMethodDelete");
       		return Response.status(Status.NOT_ACCEPTABLE).entity(extSysDesc).build();
		}
		try {  //try to format response
			json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(resp);
			if(isGood)  {
				return Response.status(Status.ACCEPTED).entity(json).build();
			}
			else  {
				return Response.status(Status.NOT_ACCEPTABLE).entity(json).build();				
			}
		} catch (JsonProcessingException e) {
			String msg = "resourceMethodDelete: Unable to create json response" + e.getMessage();
			getLogger().error(e, msg);
		}
    	//if it came here, it was just a json formatting error; the delete itself got a valid response
		if(isGood)  {
			return Response.ok(resp).build();
    	}
    	else  {
    		return Response.status(Status.NOT_ACCEPTABLE).entity(resp).build();
    	}
	}
	
    
   private QiCreateDefectDto convertJsonToDto(String json)  {
    	ObjectMapper mapper = new ObjectMapper();
    	QiCreateDefectDto dto = null;
    	try {
    		if(!StringUtils.isBlank(json))  {
	    	    mapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
	    	    dto = mapper.readValue(json, QiCreateDefectDto.class);
    		}
		} catch (Exception e) {
			String msg = "create: Unable to parse create json" + e.getMessage();
			getLogger().error(e, msg);
		}
    	return dto;
    }
    
    private QiRepairDefectDto convertRepairJsonToDto(String json)  {
    	ObjectMapper mapper = new ObjectMapper();
    	QiRepairDefectDto dto = null;
    	try {
    		if(!StringUtils.isBlank(json))  {
	    	    mapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
	    	    dto = mapper.readValue(json, QiRepairDefectDto.class);
    		}
		} catch (Exception e) {
			String msg = "repair: Unable to parse repair json" + e.getMessage();
			getLogger().error(e, msg);
		}
    	return dto;
    }
    
	public QiHeadlessDefectService getServiceImpl() {
		if(serviceImpl == null)  {
			serviceImpl = ServiceFactory.getService(QiHeadlessDefectService.class);
		}
		return serviceImpl;
	}

	public Logger getLogger() {
		return Logger.getLogger(this.getClass().getSimpleName());
	}
    
}
 
package com.honda.galc.web;

import java.util.ArrayList;
import java.util.List;

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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.honda.galc.dao.product.ProductSequenceDao;
import com.honda.galc.entity.product.ProductSequence;
import com.honda.galc.entity.product.ProductSequenceId;

import static com.honda.galc.service.ServiceFactory.getDao;


@Path("ProductIdQueueService")
public class ProductIdQueueService {
    @Context
    private HttpServletRequest httpServletRequest;
	/**
     * Default constructor. 
     */
    public ProductIdQueueService() {
        // TODO Auto-generated constructor stub
    }


    /**
     * Retrieves representation of an instance of ProductSequence
     * @return an instance of String
     */
	@GET
	@Produces("application/json")
	public Response resourceMethodGet(
	        @DefaultValue("") @QueryParam("stationId") String stationId          
			)
	{ 
		class ProductSequenceDtoList<E extends ProductSequenceDto> extends ArrayList<E> {			
		}
    	ObjectMapper mapper = new ObjectMapper();
    	String json = "";
    	boolean isGood = false;
    	try {
        	List<ProductSequence> messageList = getDao(ProductSequenceDao.class).findAll(stationId.trim());
        	List<ProductSequenceDto> dtoList = new ProductSequenceDtoList<ProductSequenceDto>();
        	for(ProductSequence prodSeq : messageList)  {
        		ProductSequenceDto dto = ProductSequenceDto.createMeFromEntity(prodSeq);
        		dtoList.add(dto);
        	}
 			json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dtoList);
			isGood = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	if(isGood)  {
			return Response.ok(json, MediaType.APPLICATION_JSON).build();
    	}
    	else  {
    		return Response.serverError().build();
    	}
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
    	boolean isGood = false;
    	try {
    	    ProductSequenceDto dto = convertJsonToDto(jsonToPut);
			if (dto != null) {
				getDao(ProductSequenceDao.class).save(dto.createProductSequence());
				isGood = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	if(isGood)  {
			return Response.created(uriInfo.getAbsolutePath()).build();
    	}
    	else  {
    		return Response.serverError().build();
    	}
	}

	/**
     * DELETE method for deleting an instance of ProductSequence
     * @content content representation for the resource
     * @return an HTTP response with content of the deleted resource.
     */
	@DELETE
	@Consumes({"application/json"})
	@Produces("text/html")
	public Response resourceMethodDelete(String jsonToDelete) { 
    	boolean isGood = false;
    	try {
    	    ProductSequenceDto dto = convertJsonToDto(jsonToDelete);
			if (dto != null) {
				ProductSequenceId id = new ProductSequenceId();
				id.setProcessPointId(dto.getStationId());
				id.setProductId(dto.getProductId());			
				getDao(ProductSequenceDao.class).removeByKey(id);
				isGood = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	if(isGood)  {
			return Response.noContent().build();
    	}
    	else  {
    		return Response.serverError().build();
    	}
	}
	
    
    private ProductSequenceDto convertJsonToDto(String json)  {
    	ObjectMapper mapper = new ObjectMapper();
    	ProductSequenceDto dto = null;
    	try {
    	    mapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
    	    dto = mapper.readValue(json, ProductSequenceDto.class);
    	    return dto;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return dto;
    }

}
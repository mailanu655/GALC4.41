package com.honda.galc.qics.mobile.client.dataservice;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

/**
 * This class defines the method interface to RestWeb.  The interfaces are 
 * used by RestyGWT to produce implementations.
 * 
 * @author vfc01346
 *
 */
public interface InspectionModelDataService extends RestService {

    @GET
    @Path("InspectionModelDao/findPartGroupNamesByApplicationIdAndModelCode?{processPoint}&{modelCode}")
	public void findPartGroupNamesByApplicationIdAndModelCode( 
			@PathParam("processPoint")  String processPoint, 
			@PathParam("modelCode") String modelCode,
			MethodCallback<List<String>> callback);


}

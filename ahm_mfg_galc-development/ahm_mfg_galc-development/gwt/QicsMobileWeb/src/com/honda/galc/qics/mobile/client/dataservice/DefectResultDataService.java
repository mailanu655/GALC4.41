package com.honda.galc.qics.mobile.client.dataservice;

import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import com.honda.galc.qics.mobile.shared.entity.AddNewDefectRepairResultRequest;
import com.honda.galc.qics.mobile.shared.entity.AddNewDefectResultRequest;
import com.honda.galc.qics.mobile.shared.entity.DefectResult;

/**
 * This class defines the method interface to RestWeb.  The interfaces are 
 * used by RestyGWT to produce implementations.
 * 
 * @author vfc01346
 *
 */
public interface DefectResultDataService extends RestService {
	
	    @GET
	    @Path("DefectResultDao/findAllByProductId?{vin}")
	    public void getDefects(@PathParam("vin") String vin, MethodCallback<List<DefectResult>> callback);

	    @POST
	    @Path("DefectResultDao/addNewDefectResult")
	    public void addNewDefectResult( Map<String,AddNewDefectResultRequest> request, MethodCallback<Void> callback );
	    		
	    @POST
	    @Path("DefectResultDao/addNewDefectRepairResult")
	    public void addNewDefectRepairResult( Map<String,AddNewDefectRepairResultRequest> request, MethodCallback<Void> callback );
	    		

}
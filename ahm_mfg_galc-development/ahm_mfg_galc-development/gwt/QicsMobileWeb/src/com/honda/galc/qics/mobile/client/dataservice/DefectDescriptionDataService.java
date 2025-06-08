/*
 * 
 */
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
public interface DefectDescriptionDataService extends RestService {
	
	    @GET
	    @Path("DefectDescriptionDao/findInspectionPartNamesByPartGroupName?{partGroupName}")
	    public void findInspectionPartNamesByPartGroupName(@PathParam("partGroupName") String partGroupName, MethodCallback<List<String>> callback);

	    @GET
	    @Path("DefectDescriptionDao/findInspectionPartLocationNamesByInspectionPartNameAndPartGroupName?{partGroupName}&{inspectionPartName}")
	    public void findInspectionPartLocationNamesByPartGroupNameAndInspectionPartName(
	    			@PathParam("partGroupName") String partGroupName, 
	    			@PathParam("inspectionPartName") String inspectionPartName, 
	    			MethodCallback<List<String>> callback);
	    
	    
	    @GET
	    @Path("DefectDescriptionDao/findPartDefectTypeNames?{partGroupName}&{inspectionPartName}&{inspectionPartLocationName}")
		public void findPartDefectTypeNames( 
    			@PathParam("partGroupName") String partGroupName, 
    			@PathParam("inspectionPartName") String inspectionPartName, 
    			@PathParam("inspectionPartLocationName") String inspectionPartLocationName,  
	  			MethodCallback<List<String>> callback);
		  		
	    @GET
	    @Path("DefectDescriptionDao/findSecondaryPartNames?{partGroupName}&{inspectionPartName}&{inspectionPartLocationName}&{defectTypeName}")
		public void findSecondaryPartNames( 
    			@PathParam("partGroupName") String partGroupName, 
    			@PathParam("inspectionPartName") String inspectionPartName, 
    			@PathParam("inspectionPartLocationName") String inspectionPartLocationName,  
    			@PathParam("defectTypeName") String defectTypeName,  
	  			MethodCallback<List<String>> callback);
		  

}
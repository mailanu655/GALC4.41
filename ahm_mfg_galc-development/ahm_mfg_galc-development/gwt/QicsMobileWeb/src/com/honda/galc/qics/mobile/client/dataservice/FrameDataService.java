package com.honda.galc.qics.mobile.client.dataservice;



import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import com.honda.galc.qics.mobile.shared.entity.Frame;

/**
 * This class defines the method interface to RestWeb.  The interfaces are 
 * used by RestyGWT to produce implementations.
 * 
 * @author vfc01346
 *
 */
public interface FrameDataService  extends RestService {
	
    @GET
    @Path("FrameDao/findBySn?{vin}")
    public void getFrame(@PathParam("vin") String vin, MethodCallback<Frame> callback);

	
}
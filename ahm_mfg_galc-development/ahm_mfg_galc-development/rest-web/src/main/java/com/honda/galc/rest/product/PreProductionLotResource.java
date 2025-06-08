package com.honda.galc.rest.product;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.rest.BaseRestResource;


@Path("preproductionlot")
public class PreProductionLotResource extends BaseRestResource {

	@POST
	@Path("/changeToUnsentByProcess")
	@RolesAllowed("RestUser")
    @Produces(MediaType.TEXT_PLAIN)
	public Integer changeToUnsentByProcess(@QueryParam("processLocation") String processLocation, 
										   @QueryParam("planCode") String planCode,
										   @QueryParam("processPointId") String processPointId) {

		String requestName = "changeToUnsentByProcess";
		try {
			getLogger().info(requestName + " REST request received");

			Integer result = getPreProductionLotDao().changeToUnsentService(processLocation, planCode, processPointId);
			
			getLogger().info(requestName + " REST request completed");

			return result;
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process " +  requestName + " REST request");
		}
		throw new WebApplicationException(Status.BAD_REQUEST);
	}

	public Logger getLogger() {
		return getLogger(this.getClass().getSimpleName());
	}
}

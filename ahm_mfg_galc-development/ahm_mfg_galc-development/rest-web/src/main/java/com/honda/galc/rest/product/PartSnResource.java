package com.honda.galc.rest.product;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.ExtRequiredPartSpecDao;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.entity.product.ExtRequiredPartSpec;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.PartId;
import com.honda.galc.rest.BaseRestResource;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.CommonPartUtility;

/**
 * @author Subu Kathiresan
 * @date Mar 22, 2018
 */
@Path("partserialnumber")
public class PartSnResource extends BaseRestResource {

	@GET
	@Path("/filterByProductIdAndPartName")
	@RolesAllowed("RestUser")
    @Produces(MediaType.APPLICATION_JSON)
	public PartSerialNumber filterByProductIdAndPartName(@QueryParam("vin") String vin, @QueryParam("partName") String partName) {

		String requestName = "filterByProductIdAndPartName";
		try {
			getLogger().info(requestName + " REST request received");
			List<String> partNames = new ArrayList<String>();
			String psn = null;
			partNames.add(partName);
			List<InstalledPart> installedPartList = getInstalledPartDao().findAllByProductIdAndPartNames(vin, partNames);
			PartSerialNumber partSerialNumber = new PartSerialNumber();

			if(!installedPartList.isEmpty()) {
				InstalledPart installedPart = installedPartList.get(0);
				ExtRequiredPartSpec extRequiredPartSpec = getExtReqPartSpec(installedPart);

				// parse part serial number if PARSE_STRATEGY is provided
				if(extRequiredPartSpec != null  && StringUtils.isNotEmpty(extRequiredPartSpec.getParseStrategy())){				
					psn = CommonPartUtility.parsePartSerialNumber(extRequiredPartSpec, installedPart.getPartSerialNumber());
				} else {
					psn = installedPart.getPartSerialNumber();
				}
			}
			getLogger().info("Part Serial Number from REST service " + psn);
			partSerialNumber.setPartSn(psn);
			getLogger().info(requestName + " REST request completed");
			return partSerialNumber;
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process " +  requestName + " REST request");
		}
		throw new WebApplicationException(Status.BAD_REQUEST);
	}

	private ExtRequiredPartSpec getExtReqPartSpec(InstalledPart installedPart) {
		PartId partId = new PartId();
		partId.setPartId(installedPart.getPartId());
		partId.setPartName(installedPart.getPartName());
		return ServiceFactory.getDao(ExtRequiredPartSpecDao.class).findByKey(partId);
	}
	
	public Logger getLogger() {
		return getLogger(this.getClass().getSimpleName());
	}
}

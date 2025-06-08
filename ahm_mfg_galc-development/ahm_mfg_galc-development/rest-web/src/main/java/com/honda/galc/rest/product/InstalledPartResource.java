package com.honda.galc.rest.product;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.honda.galc.dto.DtoUtil;
import com.honda.galc.dto.RestData;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.rest.BaseRestResource;

/**
 * @author Subu Kathiresan
 * @date Oct 04, 2019
 */
@Path("InstalledPart")
public class InstalledPartResource extends BaseRestResource {

	private static final String PART_STATUS = "partStatus";
	@GET
	@Path("/filterByProductIdAndPartName")
	@RolesAllowed("RestUser")
    @Produces(MediaType.APPLICATION_JSON)
	public Map<String, String> filterByProductIdAndPartName(@QueryParam("productId") String productId, @QueryParam("partName") String partName) {

		String requestName = "filterByProductIdAndPartName";
		try {
			getLogger().info(requestName + " REST request received");
			InstalledPart installedPart = null;
			Map<String, String> retPartDetails = new HashMap<String, String>();
			
			List<String> partNames = new ArrayList<String>();
			partNames.add(partName);

			 installedPart = getInstalledPartDao().findById(productId, partName);
			List<Measurement>measurementList = getMeasurementDao().findAllOrderBySequence(productId, partName, true);
			if(installedPart!= null) {
				
				populateMap(installedPart.getId(), retPartDetails);
				populateMap(installedPart, retPartDetails);
				
				if (!measurementList.isEmpty()) {
					for (Measurement measure : measurementList) {
						populateMap(measure, retPartDetails);
					}
				} else	retPartDetails.put("measurementValue", "");
				
				getLogger().info(requestName + " REST request completed");
				return retPartDetails;
			}
		} catch (Exception ex) {
			getLogger().error(ex, "Unable to process " +  requestName + " REST request");
		}
		throw new WebApplicationException(Status.BAD_REQUEST);
	}

	@GET
    @Path("/filterByProductIdAndPartNames")
    @RolesAllowed("RestUser")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Map<String, String>> filterByProductIdAndPartNames(@QueryParam("productId") String productId, @QueryParam("partNames") List<String> partNames) {

        String requestName = "filterByProductIdAndPartNames";
        try {
            getLogger().info(requestName + " REST request received");
            List<Map<String, String>> responseList = new ArrayList<>();
            List<InstalledPart> installedParts = getInstalledPartDao().findAllByProductIdAndPartNames(productId, partNames);
			if (installedParts != null) {
				List<Measurement> allMeasurementList = getMeasurementDao().findAllByProductIdAndPartNames(productId, partNames);
				Map<String, List<Measurement>> partNameMeasurementMap = new HashMap<String, List<Measurement>>();
				if (allMeasurementList != null) {
					for (Measurement measure : allMeasurementList) {
						String partName = measure.getId().getPartName();
						if (partNameMeasurementMap.containsKey(measure.getId().getPartName())) {
							partNameMeasurementMap.get(partName).add(measure);
						} else {
							List<Measurement> thisMeasurementList = new ArrayList<>();
							thisMeasurementList.add(measure);
							partNameMeasurementMap.put(partName, thisMeasurementList);
						}
					}
				}
	
				for (InstalledPart installedPart : installedParts) {
					Map<String, String> retPartDetails = new HashMap<>();
					List<Measurement> measurementList = partNameMeasurementMap.get(installedPart.getPartName());
					populatePartDetails(retPartDetails, installedPart, measurementList);
					responseList.add(retPartDetails);
				}
			}
			getLogger().info(requestName + " REST request completed");
            return responseList;
        } catch (Exception ex) {
            getLogger().error(ex, "Unable to process " + requestName + " REST request");
        }
        throw new WebApplicationException(Status.BAD_REQUEST);
    }

	private void populateMap(Object targetObj, Map<String, String> retPart) {
		for (Field field: DtoUtil.getAllFieldsWithAnnotation(RestData.class, targetObj.getClass())) {
			String strKey = getJsonKey(field);
			Object value = DtoUtil.getFieldValue(field, targetObj);
			if (strKey.equals(PART_STATUS)) {
				value = InstalledPartStatus.getType(Integer.parseInt(value.toString()));
			}
			String strValue = value == null ? "" : StringUtils.trimToEmpty(value.toString());
			getLogger().info("Adding to json: " + strKey + ": " + strValue);
			if (retPart.containsKey(strKey)) {
				String updatedValue = retPart.get(strKey) + "," + strValue;
				retPart.put(strKey, updatedValue);
			} else
				retPart.put(strKey, strValue);
		}
	}
	
	private void populatePartDetails(Map<String, String> retPartDetails, InstalledPart installedPart, List<Measurement> measurementList) {
		populateMap(installedPart.getId(), retPartDetails);
		populateMap(installedPart, retPartDetails);
		if (!measurementList.isEmpty()) {
			for (Measurement measure : measurementList) {
				populateMap(measure, retPartDetails);
			}
		} else {
			retPartDetails.put("measurementValue", "");
		}
	}

	private String getJsonKey(Field field) {
		RestData restData = field.getAnnotation(RestData.class);
		if(!StringUtils.trimToEmpty(restData.key()).equals("")) {
			return restData.key();
		} else {
			return field.getName();
		}
	}
	
	public Logger getLogger() {
		return getLogger(this.getClass().getSimpleName());
	}
}

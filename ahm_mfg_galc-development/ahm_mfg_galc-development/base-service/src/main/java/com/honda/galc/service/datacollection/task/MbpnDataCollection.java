package com.honda.galc.service.datacollection.task;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.service.datacollection.HeadlessDataCollectionContext;


public class MbpnDataCollection extends CollectorTask {
	
	private static String partSerialNumber = ".PART_SERIAL_NUMBER";
	private static String measurement = ".VALUE1";
	
	
	public MbpnDataCollection(HeadlessDataCollectionContext context,
			String processPointId) {
		super(context, processPointId);
	}

	@Override
	public void execute() { // need to save installedparts and measurements
		List<InstalledPart> installedParts = new ArrayList<InstalledPart>();
		List<Measurement> measures = new ArrayList<Measurement>();
		
		try {
			for(Map.Entry<Object, Object> entry : context.entrySet()) {
			    String key = entry.getKey().toString();
			    if(key.contains(partSerialNumber)){
			    	String partName = StringUtils.replace(key, partSerialNumber, "");
			    	getLogger().info("partName: "+ partName);
					Double measurementValue = null;

					InstalledPart installedPart = new InstalledPart();
					installedPart.setProductId(getContextString(TagNames.PRODUCT_ID.name()));
					installedPart.setPartName(partName);
					installedPart.setValidPartSerialNumber(true);
					installedPart.setPartSerialNumber(entry.getValue().toString());
					installedPart.setPassTime(0);
					installedPart.setActualTimestamp(new Timestamp(System
							.currentTimeMillis()));
					installedPart
							.setInstalledPartStatus(InstalledPartStatus.OK);
					installedPart.setPartId("A0000");
					for(Map.Entry<Object, Object> entry1 : context.entrySet()) {
						if( entry1.getKey().toString().contains(measurement)) {
							String measurementName = StringUtils.replace(entry1.getKey().toString(), measurement, "");
							getLogger().info("measurementName: "+ measurementName);
							
							if (measurementName.trim().equalsIgnoreCase(partName.trim())) {
								measurementValue = Double.valueOf(entry1.getValue().toString());

						Measurement measurement = new Measurement(
								getContextString(TagNames.PRODUCT_ID.name()), measurementName.trim()	, 1);
						measurement.setMeasurementValue(measurementValue);
						measurement.setMeasurementStatus(MeasurementStatus.OK);
						measurement.setActualTimestamp(new Timestamp(System
								.currentTimeMillis()));
						measures.add(measurement);
						}
						installedPart.setMeasurements(measures);
						}
					}
					installedParts.add(installedPart);
					}
				}

				saveAllInstalledParts(installedParts);
				
				getLogger().info("Successfully done datacollection");
			
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex, "exception occur in datacollection");
		}
	}

	protected String getContextString(String key){
		String value = (String)context.get(key);
		return  StringUtils.isEmpty(value) ? value : StringUtils.trim(value);
	}
	
}

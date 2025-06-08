package com.honda.galc.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.honda.galc.device.Tag;
import com.honda.galc.device.dataformat.InputData;
import com.honda.galc.entity.product.Measurement;
/**
 * 
 * @author vec15809
 *
 */
public class MeasurementList extends InputData implements Serializable{
	private static final long serialVersionUID = 1L;

	@Tag(name = "MEASUREMENT_LIST")
	private List<Measurement> measurements;

	public List<Measurement> getMeasurements() {
		if(measurements == null)
			measurements = new ArrayList<Measurement>();
		
		return measurements;
	}

	public void setMeasurements(List<Measurement> measurements) {
	
		this.measurements = measurements;
	}
	
}

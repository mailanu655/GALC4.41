package com.honda.galc.device.dataformat;

import java.io.Serializable;
import org.apache.commons.lang.StringUtils;

/**
 * @author Ambica Gawarla
 * @date September 29, 2015
 */
public class BaseScheduleCheckerData extends BaseCheckerData implements Serializable {
	
	private static final long serialVersionUID = 1L;
		
	public BaseScheduleCheckerData() {
		super();
	}

	public BaseScheduleCheckerData(String productionLot, String currentProcessPoint) {
		super(productionLot, currentProcessPoint);
			
	}

}

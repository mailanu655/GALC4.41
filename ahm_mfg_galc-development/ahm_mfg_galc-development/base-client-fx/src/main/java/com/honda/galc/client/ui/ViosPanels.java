package com.honda.galc.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.enumtype.IdEnum;

public enum ViosPanels implements IdEnum<ViosPanels> {
	
	VIOS_PLATFORM_MAINTENANCE(1),
	VIOS_PROCESS_MAINTENANCE(2),
	VIOS_UNIT_NUMBER_CONFIG(3),
	VIOS_PART_CONFIG(4),
	VIOS_MEAS_CONFIG(5),
	ONE_CLICK_APPROVAL(6),
	MFG_MBPN_APPROVAL_PROCESS(7),
	PROD_SPEC_CODE_MAINTAINANCE(8);
	
	private final int id;
	
	private ViosPanels(int id) {
		this.id = id;
	}

	@Override
	public int getId() {
		return id;
	}

	public static List<String> getPanels() {
		List<String> panelList = new ArrayList<String>();
		for(ViosPanels panel : values()) {
			panelList.add(panel.toString());
		}
		return panelList;
	}

}

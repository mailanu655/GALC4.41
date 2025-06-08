package com.honda.galc.device.dataformat;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.device.Tag;

public class TorqueDataList extends InputData {
	
	@Tag(name="1ST_ALARM")
	private int firstAlarm;
	
	@Tag(name="2ND_ALARM")
	private int secondAlarm;
	
	@Tag(name="ENGINE_SN", alt ="PRODUCT_ID")
	private int productId;
	
	private List<Torque> torques = new ArrayList<Torque>();

	public int getFirstAlarm() {
		return firstAlarm;
	}

	public void setFirstAlarm(int firstAlarm) {
		this.firstAlarm = firstAlarm;
	}

	public int getSecondAlarm() {
		return secondAlarm;
	}

	public void setSecondAlarm(int secondAlarm) {
		this.secondAlarm = secondAlarm;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public List<Torque> getTorques() {
		return torques;
	}

	public void setTorques(List<Torque> torques) {
		this.torques = torques;
	}
	
	
	
}

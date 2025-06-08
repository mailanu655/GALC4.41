package com.honda.galc.openprotocol.model;

import org.apache.commons.lang.StringUtils;

public interface ILastTighteningResult {

	double getAngle();

	void setAngle(double angle);

	int getAngleStatus();

	int getTighteningStatus();

	double getTorque();

	void setTorque(double torque);

	int getTorqueStatus();

	void setProductId(String productId);

	String getProductId();

	void setTighteningId(String id);

	String getTighteningId();

}
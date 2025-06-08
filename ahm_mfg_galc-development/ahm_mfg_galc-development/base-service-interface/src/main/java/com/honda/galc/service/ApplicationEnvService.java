package com.honda.galc.service;

public interface ApplicationEnvService extends IService {

	public String getCellName();

	public String getCurrentCellName();
	
	public String getBuildLevel();

	public String getBuildNumber();

	public String getBuildDate();

	public String getBuildType();
}

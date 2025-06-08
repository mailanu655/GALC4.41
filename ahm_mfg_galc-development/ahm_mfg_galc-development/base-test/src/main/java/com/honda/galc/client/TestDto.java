package com.honda.galc.client;

import java.sql.Date;
import java.sql.Timestamp;

import com.honda.galc.dto.DtoTag;
import com.honda.galc.dto.IDto;

public interface TestDto extends IDto{
	
	@DtoTag(name="PRODUCTION_LOT")
	public String getTestString();
	
	@DtoTag(name="INT")
	public int getTestInteger();
	
	@DtoTag(name="DOUBLE")
	public double getTestDouble();
	
	@DtoTag(name="DATE")
	public Date getTestDate();
	
	@DtoTag(name="TIME")
	public Timestamp getTestTimestamp();
	
}

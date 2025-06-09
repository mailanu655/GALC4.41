package com.honda.ahm.lc.vdb.dto;

import java.util.List;

public class DrilldownCountDto {
	
	private String name;
	private String id;
	private List<Object[]> data;
	
	public DrilldownCountDto() {
		super();
	}
	
	public DrilldownCountDto(String name, String id, List<Object[]> data) {
		super();
		this.name = name;
		this.id = id;
		this.data = data;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<Object[]> getData() {
		return data;
	}
	public void setData(List<Object[]> data) {
		this.data = data;
	}

}

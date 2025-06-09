package com.honda.ahm.lc.vdb.dto;

public class ProductCountDto {

	private String name;
	private Integer y;
	private String drilldown;
	private Integer index;
	
	public ProductCountDto() {
		super();
	}
	public ProductCountDto(String name, Integer y, String drilldown, Integer index) {
		super();
		this.name = name;
		this.y = y;
		this.drilldown = drilldown;
		this.index = index;
	}
	
	@Override
	public String toString() {
		String str = getClass().getSimpleName() + "{";
		str = str + "name: " + getName();
        str = str + ", y: " + getY();
        str = str + ", drilldown: " + getDrilldown();
        str = str + ", index: " + getIndex();
        str = str + "}";
        return str;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getY() {
		return y;
	}
	public void setY(Integer y) {
		this.y = y;
	}
	public String getDrilldown() {
		return drilldown;
	}
	public void setDrilldown(String drilldown) {
		this.drilldown = drilldown;
	}
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
	
}

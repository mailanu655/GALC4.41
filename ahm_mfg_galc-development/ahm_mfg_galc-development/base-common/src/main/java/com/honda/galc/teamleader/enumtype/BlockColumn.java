package com.honda.galc.teamleader.enumtype;

public enum BlockColumn implements Column{
	PRODUCT_ID 	("Block ID", "getProductId");
	
	private final String columnName;
	private final String columnGetter;
	
	private BlockColumn(String columnName, String columnGetter) {
		this.columnName = columnName;
		this.columnGetter = columnGetter;
	}
	
	public String getColumnName() {
		return this.columnName;
	}
	
	public String getColumnGetter() {
		return this.columnGetter;
	}
}
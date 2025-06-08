package com.honda.galc.teamleader.enumtype;

public enum HeadColumn implements Column{
	PRODUCT_ID 	("Head ID", "getProductId");
	
	private final String columnName;
	private final String columnGetter;
	
	private HeadColumn(String columnName, String columnGetter) {
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
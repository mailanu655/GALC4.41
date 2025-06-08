package com.honda.galc.teamleader.enumtype;

public enum CrankshaftColumn implements Column{
	PRODUCT_ID 	("Crankshaft ID", "getProductId");
	
	private final String columnName;
	private final String columnGetter;
	
	private CrankshaftColumn(String columnName, String columnGetter) {
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
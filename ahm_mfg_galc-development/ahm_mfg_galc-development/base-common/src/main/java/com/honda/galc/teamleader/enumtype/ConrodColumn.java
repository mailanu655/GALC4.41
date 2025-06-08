package com.honda.galc.teamleader.enumtype;

public enum ConrodColumn implements Column{
	PRODUCT_ID 	("Conrod ID", "getProductId");
	
	private final String columnName;
	private final String columnGetter;
	
	private ConrodColumn(String columnName, String columnGetter) {
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
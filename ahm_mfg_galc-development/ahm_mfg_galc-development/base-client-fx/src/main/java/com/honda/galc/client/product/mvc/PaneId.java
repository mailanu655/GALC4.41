package com.honda.galc.client.product.mvc;

public enum PaneId implements FXViewId {
	PRODUCT_SCAN_PANE					("Product Scan", "com.honda.galc.client.product.pane.GenericProductScanPane"),
	SEARCH_BY_PROCESS_PANE				("Search By Process", "com.honda.galc.client.product.pane.GenericSearchByProcessPane"),
	SEARCH_BY_DUNNAGE_PANE				("Search By Dunnage", "com.honda.galc.client.product.pane.GenericSearchByDunnagePane"),
	SEARCH_BY_TRANSACTIONID_PANE		("Search By Transaction Id", "com.honda.galc.client.product.pane.GenericSearchByTransactionIdPane"),
	SEARCH_BY_TRANSACTION_PANE			("Search By Transaction", "com.honda.galc.client.product.pane.SearchByTransactionIdPane"),
	PROCESS_POINT_SELECT_PANE			("Process Point Selection", "com.honda.galc.client.product.pane.ProcessPointSelectionPane"),
	SEARCH_BY_REPAIR_AREA_PANE			("Search By Repair Area", "com.honda.galc.client.product.pane.SearchByRepairAreaPane"),
	SEARCH_BY_PRODUCT_FILTER_PANE		("Search By Product Filter", "com.honda.galc.client.product.pane.SearchByProductFilterPane");
	
	private String paneLabel;
	private String paneClass;

	private PaneId(String paneLabel, String paneClass) {
		this.paneLabel = paneLabel;
		this.paneClass = paneClass;
	}

	public static PaneId getValueOf(String name) {
		return name == null ? null : PaneId.valueOf(name);
	}

	public static PaneId getPaneId(String panelId) {
		return PaneId.valueOf(panelId);
	}

	public static Class<?> getPaneClass(String paneIdName){
		PaneId paneId = PaneId.valueOf(paneIdName);
		if(paneId == null) return null;
		try {
			return Class.forName(paneId.getPaneClass());
		} catch (ClassNotFoundException e) {
		}
		return null;
	}

	public String getPaneLabel() {
		return this.paneLabel;
	}

	public String getPaneClass() {
		return this.paneClass;
	}
}

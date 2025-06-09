package com.honda.ahm.lc.enums;

public enum GalcDataType {
	SHIPPING_STATUS("SHIPPING_STATUS", "ShippingStatusDao", "com.honda.galc.entity.conf.ShippingStatus"),
	SHIPPING_TRANSACTION("SHIPPING_TRANSACTION","ShippingTransactionDao", "com.honda.galc.entity.product.ShippingTransaction"),
	FRAME_SHIP_CONFIRM("FRAME_SHIP_CONFIRM", "FrameShipConfirmationDao","com.honda.galc.entity.oif.FrameShipConfirmation"),
	INPROCESS("INPROCESS_PRODUCT", "InProcessProductDao", "com.honda.galc.entity.product.InProcessProduct"),
	INREPAIR("INREPAIR_AREA","InRepairAreaDao", "com.honda.galc.entity.conf.InRepairArea"),
	PARK_CHG("PARK_CHANGE", "ParkChangeDao", "com.honda.galc.entity.oif.ParkChange"),
	TRACKING_SERVICE("TRACKING_SERVICE", "TrackingService","com.honda.galc.service.TrackingService"),
	PROCESS_POINT("PROCESS_POINT","ProcessPointDao","com.honda.galc.entity.conf.ProcessPoint"),
	DEPT_SCHEDULE("DEPT_SCHEDULE", "DailyDepartmentScheduleDao","com.honda.galc.entity.product.DailyDepartmentSchedule"),
	PRODUCT_RESULT("PRODUCT_RESULT","ProductResultDao","com.honda.galc.entity.product.ProductResult"),
	INSTALLED_PART("INSTALLED_PART","InstalledPartDao","com.honda.galc.entity.product.InstalledPart"),
	FIF_CODE("FIF_CODE","SalesOrderFifDao", "com.honda.galc.entity.fif.SalesOrderFif"),
	FRAME("FRAME", "FrameDao", "com.honda.galc.dao.product.FrameDao"),
	FRAME_SPEC("FRAME_SPEC", "FrameSpecDao", "com.honda.galc.dao.product.FrameSpecDao"),
	QI_DEFECT("QI_DEFECT", "QiDefectResultDao", "com.honda.galc.dao.qi.QiDefectResultDao"),
	QI_REPAIR_AREA("QI_REPAIR_AREA", "QiRepairAreaSpaceDao", "com.honda.galc.dao.qi.QiRepairAreaSpaceDao");
	
	private String dataType;
	private String dao;
	private String galcPackage;

	private GalcDataType(String dataType, String dao,  String galcPackage) {
		this.dataType = dataType;
		this.dao = dao;

		this.galcPackage = galcPackage;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getDao() {
		return dao;
	}

	public void setDao(String dao) {
		this.dao = dao;
	}

	public String getGalcPackage() {
		return galcPackage;
	}

	public void setGalcPackage(String galcPackage) {
		this.galcPackage = galcPackage;
	}

	GalcDataType getGlacDataType(String dataType) {
		for (GalcDataType type : GalcDataType.values()) {
			if (type.getDataType().equals(dataType)) {
				return type;
			}
		}
		return null;
	}
}

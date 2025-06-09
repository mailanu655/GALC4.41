package com.honda.ahm.lc.vdb.service;

import com.honda.ahm.lc.config.BaseConfig;
import com.honda.ahm.lc.vdb.dao.HistoryDetailsDao;
import com.honda.ahm.lc.vdb.dao.ProductAgeDetailsDao;
import com.honda.ahm.lc.vdb.dao.ProductDetailsDao;
import com.honda.ahm.lc.vdb.dao.ProductHistoryDetailsDao;
import com.honda.ahm.lc.vdb.dao.ProductLastStatusDetailsDao;
import com.honda.ahm.lc.vdb.dao.QiDefectCountDetailsDao;
import com.honda.ahm.lc.vdb.dao.ShiftDetailsDao;
import com.honda.ahm.lc.vdb.dao.TrackingStatusDetailsDao;
import com.honda.ahm.lc.vdb.dao.VinRangeDetailsDao;

public abstract class DataService {
	
	public abstract BaseConfig getConfig();
	
	public abstract ProductDetailsDao getProductDetailsDao();
	
	public abstract HistoryDetailsDao getHistoryDetailsDao();
	
	public abstract ShiftDetailsDao getShiftDetailsDao();
	
	public abstract ProductAgeDetailsDao getProductAgeDetailsDao();
	
	public abstract ProductHistoryDetailsDao getProductHistoryDetailsDao();
	
	public abstract QiDefectCountDetailsDao getQiDefectCountDetailsDao();
	
	public abstract TrackingStatusDetailsDao getTrackingStatusDetailsDao();
	
	public abstract ProductLastStatusDetailsDao getProductLastStatusDetailsDao();
	
	public abstract VinRangeDetailsDao getVinRangeProductDetailsDao();


}

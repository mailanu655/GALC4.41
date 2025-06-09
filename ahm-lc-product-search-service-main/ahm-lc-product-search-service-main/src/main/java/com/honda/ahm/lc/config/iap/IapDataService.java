package com.honda.ahm.lc.config.iap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.honda.ahm.lc.config.BaseConfig;
import com.honda.ahm.lc.config.iap.dao.IapHistoryDetailsDao;
import com.honda.ahm.lc.config.iap.dao.IapProductAgeDetailsDao;
import com.honda.ahm.lc.config.iap.dao.IapProductDetailsDao;
import com.honda.ahm.lc.config.iap.dao.IapProductHistoryDetailsDao;
import com.honda.ahm.lc.config.iap.dao.IapProductLastStatusDetailsDao;
import com.honda.ahm.lc.config.iap.dao.IapQiDefectCountDetailsDao;
import com.honda.ahm.lc.config.iap.dao.IapShiftDetailsDao;
import com.honda.ahm.lc.config.iap.dao.IapTrackingStatusDetailsDao;
import com.honda.ahm.lc.config.iap.dao.IapVinRangeDetailsDao;
import com.honda.ahm.lc.vdb.dao.HistoryDetailsDao;
import com.honda.ahm.lc.vdb.dao.ProductAgeDetailsDao;
import com.honda.ahm.lc.vdb.dao.ProductDetailsDao;
import com.honda.ahm.lc.vdb.dao.ProductHistoryDetailsDao;
import com.honda.ahm.lc.vdb.dao.ProductLastStatusDetailsDao;
import com.honda.ahm.lc.vdb.dao.QiDefectCountDetailsDao;
import com.honda.ahm.lc.vdb.dao.ShiftDetailsDao;
import com.honda.ahm.lc.vdb.dao.TrackingStatusDetailsDao;
import com.honda.ahm.lc.vdb.dao.VinRangeDetailsDao;
import com.honda.ahm.lc.vdb.service.DataService;

@Service(value = "iapDataService")
@EnableTransactionManagement
@Transactional("iapTransactionManager")
@EnableJpaRepositories(
		entityManagerFactoryRef="iapEntityManagerFactory",
		transactionManagerRef = "iapTransactionManager"
		)
public class IapDataService extends DataService {
	
	@Autowired
	private IapConfig iapConfig;
	
	@Autowired
	private IapProductDetailsDao productDetailsDao;
	
	@Autowired
	private IapHistoryDetailsDao historyDetailsDao;
	
	@Autowired
	private IapShiftDetailsDao shiftDetailsDao;
	
	@Autowired
	private IapProductAgeDetailsDao productAgeDetailsDao;
	
	@Autowired
	private IapProductHistoryDetailsDao productHistoryDetailsDao;
	
	@Autowired
	private IapQiDefectCountDetailsDao qiDefectCountDetailsDao;
	
	@Autowired
	private IapTrackingStatusDetailsDao trackingStatusDetailsDao;
	
	@Autowired
	private IapProductLastStatusDetailsDao productLastStatusDetailsDao;
	
	@Autowired
	private IapVinRangeDetailsDao vinRangeDetailsDao;
	
	

	@Override
	public ProductDetailsDao getProductDetailsDao() {
		return productDetailsDao;
	}

	@Override
	public HistoryDetailsDao getHistoryDetailsDao() {
		return historyDetailsDao;
	}

	@Override
	public ShiftDetailsDao getShiftDetailsDao() {
		return shiftDetailsDao;
	}

	@Override
	public ProductAgeDetailsDao getProductAgeDetailsDao() {
		return productAgeDetailsDao;
	}

	@Override
	public ProductHistoryDetailsDao getProductHistoryDetailsDao() {
		return productHistoryDetailsDao;
	}

	@Override
	public QiDefectCountDetailsDao getQiDefectCountDetailsDao() {
		return qiDefectCountDetailsDao;
	}

	@Override
	public TrackingStatusDetailsDao getTrackingStatusDetailsDao() {
		return trackingStatusDetailsDao;
	}

	@Override
	public BaseConfig getConfig() {
		return iapConfig;
	}

	@Override
	public ProductLastStatusDetailsDao getProductLastStatusDetailsDao() {
		return productLastStatusDetailsDao;
	}

	@Override
	public VinRangeDetailsDao getVinRangeProductDetailsDao() {
		return vinRangeDetailsDao;
	}

}

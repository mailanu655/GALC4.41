package com.honda.ahm.lc.config.hcm2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.honda.ahm.lc.config.BaseConfig;
import com.honda.ahm.lc.config.hcm2.dao.Hcm2HistoryDetailsDao;
import com.honda.ahm.lc.config.hcm2.dao.Hcm2ProductAgeDetailsDao;
import com.honda.ahm.lc.config.hcm2.dao.Hcm2ProductDetailsDao;
import com.honda.ahm.lc.config.hcm2.dao.Hcm2ProductHistoryDetailsDao;
import com.honda.ahm.lc.config.hcm2.dao.Hcm2ProductLastStatusDetailsDao;
import com.honda.ahm.lc.config.hcm2.dao.Hcm2QiDefectCountDetailsDao;
import com.honda.ahm.lc.config.hcm2.dao.Hcm2ShiftDetailsDao;
import com.honda.ahm.lc.config.hcm2.dao.Hcm2TrackingStatusDetailsDao;
import com.honda.ahm.lc.config.hcm2.dao.Hcm2VinRangeDetailsDao;
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

@Service(value = "hcm2DataService")
@EnableTransactionManagement
@Transactional("hcm2TransactionManager")
@EnableJpaRepositories(
		entityManagerFactoryRef="hcm2EntityManagerFactory",
		transactionManagerRef = "hcm2TransactionManager"
		)
public class Hcm2DataService extends DataService {
	
	@Autowired
	private Hcm2Config hcm2Config;
	
	@Autowired
	private Hcm2ProductDetailsDao productDetailsDao;
	
	@Autowired
	private Hcm2HistoryDetailsDao historyDetailsDao;
	
	@Autowired
	private Hcm2ShiftDetailsDao shiftDetailsDao;
	
	@Autowired
	private Hcm2ProductAgeDetailsDao productAgeDetailsDao;
	
	@Autowired
	private Hcm2ProductHistoryDetailsDao productHistoryDetailsDao;
	
	@Autowired
	private Hcm2QiDefectCountDetailsDao qiDefectCountDetailsDao;
	
	@Autowired
	private Hcm2TrackingStatusDetailsDao trackingStatusDetailsDao;
	
	@Autowired
	private Hcm2ProductLastStatusDetailsDao productLastStatusDetailsDao;
	
	@Autowired
	private Hcm2VinRangeDetailsDao vinRangeDetailsDao;
	
	

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
		return hcm2Config;
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

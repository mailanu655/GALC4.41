package com.honda.ahm.lc.config.hcm3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.honda.ahm.lc.config.BaseConfig;
import com.honda.ahm.lc.config.hcm3.dao.Hcm3HistoryDetailsDao;
import com.honda.ahm.lc.config.hcm3.dao.Hcm3ProductAgeDetailsDao;
import com.honda.ahm.lc.config.hcm3.dao.Hcm3ProductDetailsDao;
import com.honda.ahm.lc.config.hcm3.dao.Hcm3ProductHistoryDetailsDao;
import com.honda.ahm.lc.config.hcm3.dao.Hcm3ProductLastStatusDetailsDao;
import com.honda.ahm.lc.config.hcm3.dao.Hcm3QiDefectCountDetailsDao;
import com.honda.ahm.lc.config.hcm3.dao.Hcm3ShiftDetailsDao;
import com.honda.ahm.lc.config.hcm3.dao.Hcm3TrackingStatusDetailsDao;
import com.honda.ahm.lc.config.hcm3.dao.Hcm3VinRangeDetailsDao;
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

@Service(value = "hcm3DataService")
@EnableTransactionManagement
@Transactional("hcm3TransactionManager")
@EnableJpaRepositories(
		entityManagerFactoryRef="hcm3EntityManagerFactory",
		transactionManagerRef = "hcm3TransactionManager"
		)
public class Hcm3DataService extends DataService {
	
	@Autowired
	private Hcm3Config hcm3Config;
	
	@Autowired
	private Hcm3ProductDetailsDao productDetailsDao;
	
	@Autowired
	private Hcm3HistoryDetailsDao historyDetailsDao;
	
	@Autowired
	private Hcm3ShiftDetailsDao shiftDetailsDao;
	
	@Autowired
	private Hcm3ProductAgeDetailsDao productAgeDetailsDao;
	
	@Autowired
	private Hcm3ProductHistoryDetailsDao productHistoryDetailsDao;
	
	@Autowired
	private Hcm3QiDefectCountDetailsDao qiDefectCountDetailsDao;
	
	@Autowired
	private Hcm3TrackingStatusDetailsDao trackingStatusDetailsDao;
	
	@Autowired
	private Hcm3ProductLastStatusDetailsDao productLastStatusDetailsDao;
	
	@Autowired
	private Hcm3VinRangeDetailsDao vinRangeDetailsDao;
	
	

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
		return hcm3Config;
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

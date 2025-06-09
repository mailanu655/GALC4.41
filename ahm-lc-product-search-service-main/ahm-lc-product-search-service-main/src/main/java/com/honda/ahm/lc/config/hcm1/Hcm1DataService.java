package com.honda.ahm.lc.config.hcm1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.honda.ahm.lc.config.BaseConfig;
import com.honda.ahm.lc.config.hcm1.dao.Hcm1HistoryDetailsDao;
import com.honda.ahm.lc.config.hcm1.dao.Hcm1ProductAgeDetailsDao;
import com.honda.ahm.lc.config.hcm1.dao.Hcm1ProductDetailsDao;
import com.honda.ahm.lc.config.hcm1.dao.Hcm1ProductHistoryDetailsDao;
import com.honda.ahm.lc.config.hcm1.dao.Hcm1ProductLastStatusDetailsDao;
import com.honda.ahm.lc.config.hcm1.dao.Hcm1QiDefectCountDetailsDao;
import com.honda.ahm.lc.config.hcm1.dao.Hcm1ShiftDetailsDao;
import com.honda.ahm.lc.config.hcm1.dao.Hcm1TrackingStatusDetailsDao;
import com.honda.ahm.lc.config.hcm1.dao.Hcm1VinRangeDetailsDao;
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

@Service(value = "hcm1DataService")
@EnableTransactionManagement
@Transactional("hcm1TransactionManager")
@EnableJpaRepositories(
		entityManagerFactoryRef="hcm1EntityManagerFactory",
		transactionManagerRef = "hcm1TransactionManager"
		)
public class Hcm1DataService extends DataService {
	
	@Autowired
	private Hcm1Config hcm1Config;
	
	@Autowired
	private Hcm1ProductDetailsDao productDetailsDao;
	
	@Autowired
	private Hcm1HistoryDetailsDao historyDetailsDao;
	
	@Autowired
	private Hcm1ShiftDetailsDao shiftDetailsDao;
	
	@Autowired
	private Hcm1ProductAgeDetailsDao productAgeDetailsDao;
	
	@Autowired
	private Hcm1ProductHistoryDetailsDao productHistoryDetailsDao;
	
	@Autowired
	private Hcm1QiDefectCountDetailsDao qiDefectCountDetailsDao;
	
	@Autowired
	private Hcm1TrackingStatusDetailsDao trackingStatusDetailsDao;
	
	
	@Autowired
	private Hcm1ProductLastStatusDetailsDao productLastStatusDetailsDao;
	
	@Autowired
	private Hcm1VinRangeDetailsDao VinRangeDetailsDao;
	

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
		return hcm1Config;
	}

	@Override
	public ProductLastStatusDetailsDao getProductLastStatusDetailsDao() {
		return productLastStatusDetailsDao;
	}

	@Override
	public VinRangeDetailsDao getVinRangeProductDetailsDao() {
		return VinRangeDetailsDao;
	}

}

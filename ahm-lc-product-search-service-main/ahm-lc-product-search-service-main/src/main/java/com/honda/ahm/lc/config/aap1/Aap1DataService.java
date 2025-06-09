package com.honda.ahm.lc.config.aap1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.honda.ahm.lc.config.BaseConfig;
import com.honda.ahm.lc.config.aap1.dao.Aap1HistoryDetailsDao;
import com.honda.ahm.lc.config.aap1.dao.Aap1ProductAgeDetailsDao;
import com.honda.ahm.lc.config.aap1.dao.Aap1ProductDetailsDao;
import com.honda.ahm.lc.config.aap1.dao.Aap1ProductHistoryDetailsDao;
import com.honda.ahm.lc.config.aap1.dao.Aap1ProductLastStatusDetailsDao;
import com.honda.ahm.lc.config.aap1.dao.Aap1QiDefectCountDetailsDao;
import com.honda.ahm.lc.config.aap1.dao.Aap1ShiftDetailsDao;
import com.honda.ahm.lc.config.aap1.dao.Aap1TrackingStatusDetailsDao;
import com.honda.ahm.lc.config.aap1.dao.Aap1VinRangeDetailsDao;
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

@Service(value = "aap1DataService")
@EnableTransactionManagement
@Transactional("aap1TransactionManager")
@EnableJpaRepositories(entityManagerFactoryRef = "aap1EntityManagerFactory", transactionManagerRef = "aap1TransactionManager")
public class Aap1DataService extends DataService {

	@Autowired
	private Aap1Config aap1Config;

	@Autowired
	private Aap1ProductDetailsDao productDetailsDao;

	@Autowired
	private Aap1HistoryDetailsDao historyDetailsDao;

	@Autowired
	private Aap1ShiftDetailsDao shiftDetailsDao;

	@Autowired
	private Aap1ProductAgeDetailsDao productAgeDetailsDao;

	@Autowired
	private Aap1ProductHistoryDetailsDao productHistoryDetailsDao;

	@Autowired
	private Aap1QiDefectCountDetailsDao qiDefectCountDetailsDao;

	@Autowired
	private Aap1TrackingStatusDetailsDao trackingStatusDetailsDao;

	@Autowired
	private Aap1ProductLastStatusDetailsDao productLastStatusDetailsDao;
	
	@Autowired
	private Aap1VinRangeDetailsDao vinRangeProductDetailsDao;

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
		return aap1Config;
	}

	@Override
	public ProductLastStatusDetailsDao getProductLastStatusDetailsDao() {

		return productLastStatusDetailsDao;
	}
   

	@Override
	public VinRangeDetailsDao getVinRangeProductDetailsDao() {
		return vinRangeProductDetailsDao;
	}


	
	

}

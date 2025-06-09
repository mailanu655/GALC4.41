package com.honda.ahm.lc.config.pmc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.honda.ahm.lc.config.BaseConfig;
import com.honda.ahm.lc.config.pmc.dao.PmcHistoryDetailsDao;
import com.honda.ahm.lc.config.pmc.dao.PmcProductAgeDetailsDao;
import com.honda.ahm.lc.config.pmc.dao.PmcProductDetailsDao;
import com.honda.ahm.lc.config.pmc.dao.PmcProductHistoryDetailsDao;
import com.honda.ahm.lc.config.pmc.dao.PmcProductLastStatusDetailsDao;
import com.honda.ahm.lc.config.pmc.dao.PmcQiDefectCountDetailsDao;
import com.honda.ahm.lc.config.pmc.dao.PmcShiftDetailsDao;
import com.honda.ahm.lc.config.pmc.dao.PmcTrackingStatusDetailsDao;
import com.honda.ahm.lc.config.pmc.dao.PmcVinRangeDetailsDao;
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

@Service(value = "pmcDataService")
@EnableTransactionManagement
@Transactional("pmcTransactionManager")
@EnableJpaRepositories(
		entityManagerFactoryRef="pmcEntityManagerFactory",
		transactionManagerRef = "pmcTransactionManager"
		)
public class PmcDataService extends DataService {
	
	@Autowired
	private PmcConfig pmcConfig;
	
	@Autowired
	private PmcProductDetailsDao productDetailsDao;
	
	@Autowired
	private PmcHistoryDetailsDao historyDetailsDao;
	
	@Autowired
	private PmcShiftDetailsDao shiftDetailsDao;
	
	@Autowired
	private PmcProductAgeDetailsDao productAgeDetailsDao;
	
	@Autowired
	private PmcProductHistoryDetailsDao productHistoryDetailsDao;
	
	@Autowired
	private PmcQiDefectCountDetailsDao qiDefectCountDetailsDao;
	
	@Autowired
	private PmcTrackingStatusDetailsDao trackingStatusDetailsDao;
	
	@Autowired
	private PmcProductLastStatusDetailsDao productLastStatusDetailsDao;
	
	@Autowired
	private PmcVinRangeDetailsDao vinRangeDetailsDao;
	
	

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
		return pmcConfig;
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

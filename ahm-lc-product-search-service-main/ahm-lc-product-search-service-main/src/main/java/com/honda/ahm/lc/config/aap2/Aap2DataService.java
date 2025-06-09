package com.honda.ahm.lc.config.aap2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.honda.ahm.lc.config.BaseConfig;
import com.honda.ahm.lc.config.aap2.dao.Aap2HistoryDetailsDao;
import com.honda.ahm.lc.config.aap2.dao.Aap2ProductAgeDetailsDao;
import com.honda.ahm.lc.config.aap2.dao.Aap2ProductDetailsDao;
import com.honda.ahm.lc.config.aap2.dao.Aap2ProductHistoryDetailsDao;
import com.honda.ahm.lc.config.aap2.dao.Aap2ProductLastStatusDetailsDao;
import com.honda.ahm.lc.config.aap2.dao.Aap2QiDefectCountDetailsDao;
import com.honda.ahm.lc.config.aap2.dao.Aap2ShiftDetailsDao;
import com.honda.ahm.lc.config.aap2.dao.Aap2TrackingStatusDetailsDao;
import com.honda.ahm.lc.config.aap2.dao.Aap2VinRangeDetailsDao;
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

@Service(value = "aap2DataService")
@EnableTransactionManagement
@Transactional("aap2TransactionManager")
@EnableJpaRepositories(
		entityManagerFactoryRef="aap2EntityManagerFactory",
		transactionManagerRef = "aap2TransactionManager"
		)
public class Aap2DataService extends DataService {
	
	@Autowired
	private Aap2Config aap2Config;
	
	@Autowired
	private Aap2ProductDetailsDao productDetailsDao;
	
	@Autowired
	private Aap2HistoryDetailsDao historyDetailsDao;
	
	@Autowired
	private Aap2ShiftDetailsDao shiftDetailsDao;
	
	@Autowired
	private Aap2ProductAgeDetailsDao productAgeDetailsDao;
	
	@Autowired
	private Aap2ProductHistoryDetailsDao productHistoryDetailsDao;
	
	@Autowired
	private Aap2QiDefectCountDetailsDao qiDefectCountDetailsDao;
	
	@Autowired
	private Aap2TrackingStatusDetailsDao trackingStatusDetailsDao;
	
	@Autowired
	private Aap2ProductLastStatusDetailsDao productLastStatusDetailsDao;
	
	
	@Autowired
	private Aap2VinRangeDetailsDao vinRangeProductDetailsDao;

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
		return aap2Config;
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

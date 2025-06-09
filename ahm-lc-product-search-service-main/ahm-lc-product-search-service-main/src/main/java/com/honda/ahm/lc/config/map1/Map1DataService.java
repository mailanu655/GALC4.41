package com.honda.ahm.lc.config.map1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.honda.ahm.lc.config.BaseConfig;
import com.honda.ahm.lc.config.map1.dao.Map1HistoryDetailsDao;
import com.honda.ahm.lc.config.map1.dao.Map1ProductAgeDetailsDao;
import com.honda.ahm.lc.config.map1.dao.Map1ProductDetailsDao;
import com.honda.ahm.lc.config.map1.dao.Map1ProductHistoryDetailsDao;
import com.honda.ahm.lc.config.map1.dao.Map1ProductLastStatusDetailsDao;
import com.honda.ahm.lc.config.map1.dao.Map1QiDefectCountDetailsDao;
import com.honda.ahm.lc.config.map1.dao.Map1ShiftDetailsDao;
import com.honda.ahm.lc.config.map1.dao.Map1TrackingStatusDetailsDao;
import com.honda.ahm.lc.config.map1.dao.Map1VinRangeDetailsDao;
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

@Service(value = "map1DataService")
@EnableTransactionManagement
@Transactional("map1TransactionManager")
@EnableJpaRepositories(
		entityManagerFactoryRef="map1EntityManagerFactory",
		transactionManagerRef = "map1TransactionManager"
		)
public class Map1DataService extends DataService {
	
	@Autowired
	private Map1Config map1Config;
	
	@Autowired
	private Map1ProductDetailsDao productDetailsDao;
	
	@Autowired
	private Map1HistoryDetailsDao historyDetailsDao;
	
	@Autowired
	private Map1ShiftDetailsDao shiftDetailsDao;
	
	@Autowired
	private Map1ProductAgeDetailsDao productAgeDetailsDao;
	
	@Autowired
	private Map1ProductHistoryDetailsDao productHistoryDetailsDao;
	
	@Autowired
	private Map1QiDefectCountDetailsDao qiDefectCountDetailsDao;
	
	@Autowired
	private Map1TrackingStatusDetailsDao trackingStatusDetailsDao;
	
	@Autowired
	private Map1ProductLastStatusDetailsDao productLastStatusDetailsDao;
	
	@Autowired
	private Map1VinRangeDetailsDao VinRangeDetailsDao;
	
	

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
		return map1Config;
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

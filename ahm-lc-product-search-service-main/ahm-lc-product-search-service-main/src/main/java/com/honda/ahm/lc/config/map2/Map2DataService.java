package com.honda.ahm.lc.config.map2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.honda.ahm.lc.config.BaseConfig;
import com.honda.ahm.lc.config.map2.dao.Map2HistoryDetailsDao;
import com.honda.ahm.lc.config.map2.dao.Map2ProductAgeDetailsDao;
import com.honda.ahm.lc.config.map2.dao.Map2ProductDetailsDao;
import com.honda.ahm.lc.config.map2.dao.Map2ProductHistoryDetailsDao;
import com.honda.ahm.lc.config.map2.dao.Map2ProductLastStatusDetailsDao;
import com.honda.ahm.lc.config.map2.dao.Map2QiDefectCountDetailsDao;
import com.honda.ahm.lc.config.map2.dao.Map2ShiftDetailsDao;
import com.honda.ahm.lc.config.map2.dao.Map2TrackingStatusDetailsDao;
import com.honda.ahm.lc.config.map2.dao.Map2VinRangeDetailsDao;
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

@Service(value = "map2DataService")
@EnableTransactionManagement
@Transactional("map2TransactionManager")
@EnableJpaRepositories(
		entityManagerFactoryRef="map2EntityManagerFactory",
		transactionManagerRef = "map2TransactionManager"
		)
public class Map2DataService extends DataService {
	
	@Autowired
	private Map2Config map2Config;
	
	@Autowired
	private Map2ProductDetailsDao productDetailsDao;
	
	@Autowired
	private Map2HistoryDetailsDao historyDetailsDao;
	
	@Autowired
	private Map2ShiftDetailsDao shiftDetailsDao;
	
	@Autowired
	private Map2ProductAgeDetailsDao productAgeDetailsDao;
	
	@Autowired
	private Map2ProductHistoryDetailsDao productHistoryDetailsDao;
	
	@Autowired
	private Map2QiDefectCountDetailsDao qiDefectCountDetailsDao;
	
	@Autowired
	private Map2TrackingStatusDetailsDao trackingStatusDetailsDao;
	
	@Autowired
	private Map2ProductLastStatusDetailsDao productLastStatusDetailsDao;
	
	@Autowired
	private Map2VinRangeDetailsDao vinRangeDetailsDao;
	
	

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
		return map2Config;
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

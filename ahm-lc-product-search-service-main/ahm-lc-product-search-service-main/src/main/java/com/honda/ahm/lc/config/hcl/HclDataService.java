package com.honda.ahm.lc.config.hcl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.honda.ahm.lc.config.BaseConfig;
import com.honda.ahm.lc.config.hcl.dao.HclHistoryDetailsDao;
import com.honda.ahm.lc.config.hcl.dao.HclProductAgeDetailsDao;
import com.honda.ahm.lc.config.hcl.dao.HclProductDetailsDao;
import com.honda.ahm.lc.config.hcl.dao.HclProductHistoryDetailsDao;
import com.honda.ahm.lc.config.hcl.dao.HclProductLastStatusDetailsDao;
import com.honda.ahm.lc.config.hcl.dao.HclQiDefectCountDetailsDao;
import com.honda.ahm.lc.config.hcl.dao.HclShiftDetailsDao;
import com.honda.ahm.lc.config.hcl.dao.HclTrackingStatusDetailsDao;
import com.honda.ahm.lc.config.hcl.dao.HclVinRangeDetailsDao;
import com.honda.ahm.lc.config.hcl.dao.HclVinRangeDetailsDao;
import com.honda.ahm.lc.config.hcl.dao.HclVinRangeDetailsDao;
import com.honda.ahm.lc.config.hcl.dao.HclVinRangeDetailsDao;
import com.honda.ahm.lc.config.hcl.dao.HclVinRangeDetailsDao;
import com.honda.ahm.lc.config.hcl.dao.HclVinRangeDetailsDao;
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

@Service(value = "hclDataService")
@EnableTransactionManagement
@Transactional("hclTransactionManager")
@EnableJpaRepositories(
		entityManagerFactoryRef="hclEntityManagerFactory",
		transactionManagerRef = "hclTransactionManager"
		)
public class HclDataService extends DataService {
	
	@Autowired
	private HclConfig hclConfig;
	
	@Autowired
	private HclProductDetailsDao productDetailsDao;
	
	@Autowired
	private HclHistoryDetailsDao historyDetailsDao;
	
	@Autowired
	private HclShiftDetailsDao shiftDetailsDao;
	
	@Autowired
	private HclProductAgeDetailsDao productAgeDetailsDao;
	
	@Autowired
	private HclProductHistoryDetailsDao productHistoryDetailsDao;
	
	@Autowired
	private HclQiDefectCountDetailsDao qiDefectCountDetailsDao;
	
	@Autowired
	private HclTrackingStatusDetailsDao trackingStatusDetailsDao;
	
	@Autowired
	private HclProductLastStatusDetailsDao productLastStatusDetailsDao;
	
	@Autowired
	private HclVinRangeDetailsDao vinRangeProductDetailsDao;
    
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
		return hclConfig;
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

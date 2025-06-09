package com.honda.ahm.lc.config.elp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.honda.ahm.lc.config.BaseConfig;
import com.honda.ahm.lc.config.aap2.dao.Aap2VinRangeDetailsDao;
import com.honda.ahm.lc.config.elp.dao.ElpHistoryDetailsDao;
import com.honda.ahm.lc.config.elp.dao.ElpProductAgeDetailsDao;
import com.honda.ahm.lc.config.elp.dao.ElpProductDetailsDao;
import com.honda.ahm.lc.config.elp.dao.ElpProductHistoryDetailsDao;
import com.honda.ahm.lc.config.elp.dao.ElpProductLastStatusDetailsDao;
import com.honda.ahm.lc.config.elp.dao.ElpQiDefectCountDetailsDao;
import com.honda.ahm.lc.config.elp.dao.ElpShiftDetailsDao;
import com.honda.ahm.lc.config.elp.dao.ElpTrackingStatusDetailsDao;
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

@Service(value = "elpDataService")
@EnableTransactionManagement
@Transactional("elpTransactionManager")
@EnableJpaRepositories(
		entityManagerFactoryRef="elpEntityManagerFactory",
		transactionManagerRef = "elpTransactionManager"
		)
public class ElpDataService extends DataService {
	
	@Autowired
	private ElpConfig elpConfig;
	
	@Autowired
	private ElpProductDetailsDao productDetailsDao;
	
	@Autowired
	private ElpHistoryDetailsDao historyDetailsDao;
	
	@Autowired
	private ElpShiftDetailsDao shiftDetailsDao;
	
	@Autowired
	private ElpProductAgeDetailsDao productAgeDetailsDao;
	
	@Autowired
	private ElpProductHistoryDetailsDao productHistoryDetailsDao;
	
	@Autowired
	private ElpQiDefectCountDetailsDao qiDefectCountDetailsDao;
	
	@Autowired
	private ElpTrackingStatusDetailsDao trackingStatusDetailsDao;
	
	@Autowired
	private ElpProductLastStatusDetailsDao productLastStatusDetailsDao;
	
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
		return elpConfig;
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

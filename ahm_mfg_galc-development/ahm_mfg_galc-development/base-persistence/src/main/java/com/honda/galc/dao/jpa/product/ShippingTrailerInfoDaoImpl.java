package com.honda.galc.dao.jpa.product;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.ShippingQuorumDao;
import com.honda.galc.dao.product.ShippingQuorumDetailDao;
import com.honda.galc.dao.product.ShippingTrailerDao;
import com.honda.galc.dao.product.ShippingTrailerInfoDao;
import com.honda.galc.dao.product.ShippingVanningScheduleDao;
import com.honda.galc.entity.enumtype.ShippingTrailerInfoStatus;
import com.honda.galc.entity.enumtype.TrailerStatus;
import com.honda.galc.entity.product.ShippingQuorum;
import com.honda.galc.entity.product.ShippingQuorumDetail;
import com.honda.galc.entity.product.ShippingTrailerInfo;
import com.honda.galc.entity.product.ShippingVanningSchedule;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>ShippingTrailerInfoDaoImpl Class description</h3>
 * <p> ShippingTrailerInfoDaoImpl description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Jun 1, 2012
 *
 *
 */
public class ShippingTrailerInfoDaoImpl extends BaseDaoImpl<ShippingTrailerInfo,Integer> implements ShippingTrailerInfoDao {

	private static final String FIND_FINISHED_TRAILERS = "SELECT * FROM GALADM.TRAILER_INFO_TBX t WHERE NOT EXISTS " +
		"(SELECT v.TRAILER_ID FROM GALADM.VANNING_SCHEDULE_TBX v WHERE v.TRAILER_ID = t.TRAILER_ID)";
	
	private static final String FIND_SHIPPING_TRAILERS = "SELECT * FROM GALADM.TRAILER_INFO_TBX A " + 
		" WHERE A.STATUS != 4 and A.STATUS != 5 ORDER BY A.TRAILER_ID FOR READ ONLY";

	private static final String FIND_LATEST_TRAILERS =
		"SELECT a.* FROM GALADM.TRAILER_INFO_TBX a, GALADM.TRAILER_TBX b " +
		"WHERE a.TRAILER_ID = " +  
		"	(SELECT MAX(TRAILER_ID) FROM GALADM.TRAILER_INFO_TBX " +
		"		WHERE TRAILER_NUMBER = b.TRAILER_NUMBER)" +
		"ORDER BY TRAILER_NUMBER";
	
	@Autowired
	ShippingTrailerDao shippingTrailerDao;
	
	@Autowired
	ShippingVanningScheduleDao shippingVanningScheduleDao;
	
	@Autowired
	ShippingQuorumDao shippingQuorumDao;
	
	@Autowired
	ShippingQuorumDetailDao shippingQuorumDetailDao;
	
	public List<ShippingTrailerInfo> findAllFinishedTrailers() {
		return findAllByNativeQuery(FIND_FINISHED_TRAILERS, new Parameters());
	}

	public List<ShippingTrailerInfo> findAllShippingTrailers() {
		return findAllByNativeQuery(FIND_SHIPPING_TRAILERS,null);
	}
	
	@Transactional
	public ShippingTrailerInfo changeTrailerNumer(int trailerId, String trailerNumber) {
		ShippingTrailerInfo trailerInfo = findByKey(trailerId);
		if(trailerInfo == null) return null;
		shippingTrailerDao.updateStatus(trailerInfo.getTrailerNumber(), TrailerStatus.AVAILABLE);
		shippingTrailerDao.updateStatus(trailerNumber, TrailerStatus.IN_USE);
		trailerInfo.setTrailerNumber(trailerNumber);
		return save(trailerInfo);
	}

	public ShippingTrailerInfo getLastShippingTrailerInfo() {
		return findFirst(null, new String[]{"trailerId"}, false);
	}

	@Transactional
	public void assignTrailer(String trailerNumber,List<ShippingVanningSchedule> schedules,List<ShippingQuorum> shippingQuorums,ShippingVanningSchedule splitSchedule) {
		ShippingTrailerInfo lastTrailerInfo = getLastShippingTrailerInfo();
		int newTrailerId = lastTrailerInfo == null ? 1 : lastTrailerInfo.getTrailerId() + 1;
		ShippingTrailerInfo trailerInfo = new ShippingTrailerInfo();
		trailerInfo.setTrailerId(newTrailerId);
		trailerInfo.setTrailerNumber(trailerNumber);
		
		int schQty = 0;
		for(ShippingVanningSchedule schedule :schedules) {
			schedule.setTrailerId(newTrailerId);
			schQty += schedule.getSchQty();
		}
		List<ShippingQuorumDetail> shippingQuorumDetails = new ArrayList<ShippingQuorumDetail>();
		
		for(ShippingQuorum quorum : shippingQuorums){
			shippingQuorumDetails.addAll(quorum.getShippingQuorumDetails());
			quorum.setTrailerId(newTrailerId);
			quorum.setShippingQuorumDetails(null);
		}
		
		trailerInfo.setSchQty(schQty);
		
		if(splitSchedule != null) schedules.add(splitSchedule);
		shippingVanningScheduleDao.saveAll(schedules);
		shippingQuorumDetailDao.saveAll(shippingQuorumDetails);
		shippingQuorumDao.saveAll(shippingQuorums);
		
		shippingTrailerDao.updateStatus(trailerNumber, TrailerStatus.IN_USE);
		save(trailerInfo);
		
	}

	@Transactional
	public int deassignTrailer(int trailerId) {
		ShippingTrailerInfo lastTrailerInfo = getLastShippingTrailerInfo();
		if(lastTrailerInfo.getTrailerId() != trailerId || lastTrailerInfo.getStatus()!= ShippingTrailerInfoStatus.WAITING) 
			return 0;
		shippingQuorumDetailDao.deleteAllByTrailerId(trailerId);
		shippingQuorumDao.deleteAllByTrailerId(trailerId);
		shippingVanningScheduleDao.removeTrailerId(trailerId);
		shippingTrailerDao.updateStatus(lastTrailerInfo.getTrailerNumber(), TrailerStatus.AVAILABLE);
		remove(lastTrailerInfo);
		return 1;
	}

	public List<ShippingTrailerInfo> findAllCompleteTrailers() {
		return findAll(Parameters.with("statusId", ShippingTrailerInfoStatus.COMPLETE.getId()),new String[]{"updateTimestamp"});
	}

	public List<ShippingTrailerInfo> findLatestTrailers() {
		return findAllByNativeQuery(FIND_LATEST_TRAILERS, null);
	}
	
}

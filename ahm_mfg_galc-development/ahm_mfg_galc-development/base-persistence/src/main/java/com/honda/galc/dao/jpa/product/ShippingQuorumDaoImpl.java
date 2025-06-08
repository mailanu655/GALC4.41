package com.honda.galc.dao.jpa.product;


import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.ShippingQuorumDao;
import com.honda.galc.dao.product.ShippingQuorumDetailDao;
import com.honda.galc.dao.product.ShippingTrailerInfoDao;
import com.honda.galc.dao.product.ShippingVanningScheduleDao;
import com.honda.galc.entity.enumtype.ShippingQuorumDetailStatus;
import com.honda.galc.entity.enumtype.ShippingQuorumStatus;
import com.honda.galc.entity.product.ShippingQuorum;
import com.honda.galc.entity.product.ShippingQuorumDetail;
import com.honda.galc.entity.product.ShippingQuorumId;
import com.honda.galc.entity.product.ShippingTrailerInfo;
import com.honda.galc.entity.product.ShippingVanningSchedule;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>ShippingQuorumDaoImpl Class description</h3>
 * <p> ShippingQuorumDaoImpl description </p>
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
public class ShippingQuorumDaoImpl extends BaseDaoImpl<ShippingQuorum,ShippingQuorumId> implements ShippingQuorumDao {

	private static final String  FIND_ALL_ACTIVE_QUORUMS =
		"SELECT * FROM GALADM.QUORUM_TBX " +
		"WHERE STATUS != 4 AND STATUS != 5 AND (TRAILER_ID = -1 OR TRAILER_ID = -2 OR EXISTS  " +
			"(SELECT * FROM GALADM.TRAILER_INFO_TBX T WHERE T.TRAILER_ID = TRAILER_ID AND STATUS != 4 AND STATUS != 5))" +
		"ORDER BY QUORUM_DATE ASC, QUORUM_ID ASC WITH UR";

	private static final String  FIND_CURRENT_SHIPPING_QUORUMS =
		"SELECT * FROM GALADM.QUORUM_TBX " +
		"WHERE STATUS IN(0,1,2,6) " +
		"ORDER BY QUORUM_DATE ASC, QUORUM_ID ASC WITH UR";
		
		
	private static final String SHIFT_QUORUM_SEQ = 
		"UPDATE GALADM.QUORUM_TBX SET QUORUM_ID = QUORUM_ID + ?1 WHERE QUORUM_DATE = ?2 AND QUORUM_ID > ?3";
	
	private static final String CHANGE_QUORUM = 
		"UPDATE GALADM.QUORUM_TBX SET QUORUM_DATE = ?1,QUORUM_ID = ?2 " + 
		" WHERE QUORUM_DATE = ?3 AND QUORUM_ID = ?4";
	
	@Autowired
	ShippingQuorumDetailDao shippingQuorumDetailDao;
	
	@Autowired
	ShippingTrailerInfoDao shippingTrailerInfoDao;
	
	@Autowired
	ShippingVanningScheduleDao shippingVanningScheduleDao;
	
	
	@Transactional
	public int deleteAllByTrailerId(int trailerId) {
		return delete(Parameters.with("trailerId", trailerId));
	}

	public List<ShippingQuorum> findAllActiveQuorums() {
		return findAllByNativeQuery(FIND_ALL_ACTIVE_QUORUMS, null);
	}

	@Transactional
	public int updateStatus(Date quorumDate, int quorumId,ShippingQuorumStatus status) {
		return update(Parameters.with("statusId",status.getId()), 
				Parameters.with("id.quorumDate",quorumDate).put("id.quorumId",quorumId));
	}
	
	@Transactional
	public int shiftQuorumSeq(Date quorumDate, int quorumId,int offset) {
		shippingQuorumDetailDao.shiftQuorumSeq(quorumDate, quorumId, offset);
		return executeNativeUpdate(SHIFT_QUORUM_SEQ, Parameters.with("1",offset).put("2",quorumDate).put("3",quorumId));
	}
	
	@Transactional
	public int changeQuorum(Date fromQuorumDate, int fromQuorumId,Date toQuorumDate, int toQuorumId) {
		shippingQuorumDetailDao.changeQuorum(fromQuorumDate, fromQuorumId, toQuorumDate, toQuorumId);
		return executeNativeUpdate(CHANGE_QUORUM, 
				Parameters.with("1",toQuorumDate).put("2",toQuorumId).put("3",fromQuorumDate).put("4",fromQuorumId));
	}

	public ShippingQuorum findLastShippingQuorum() {
		return findFirst(null, new String[]{"id.quorumDate","id.quorumId"}, false);
	}

	@Transactional
	public void updateManualLoadEngines(ShippingQuorum quorum, List<String> eins) {
		List<ShippingQuorum> quorums = new ArrayList<ShippingQuorum>();
		List<ShippingQuorumDetail> quorumDetails = new ArrayList<ShippingQuorumDetail>();
		
		List<ShippingTrailerInfo> trailerInfos = new ArrayList<ShippingTrailerInfo>();
		List<ShippingVanningSchedule> vanningSchedules = new ArrayList<ShippingVanningSchedule>();
		
		// update act qty of vanning schedule and trailer info for deleted engines
		for(ShippingQuorumDetail detail : quorum.getShippingQuorumDetails()){
			if(!StringUtils.isEmpty(detail.getEngineNumber()) && !eins.contains(detail.getEngineNumber())){
				ShippingTrailerInfo trailerInfo = findTrailerInfo(quorum.getTrailerId(), trailerInfos);
				trailerInfo.setActQty(trailerInfo.getActQty() - 1);
				ShippingVanningSchedule schedule = findSchedule(quorum.getTrailerId(), detail.getKdLot(), vanningSchedules); 
				schedule.setActQty(schedule.getActQty() -1);
			}
		}
		
		for(int i =0; i<quorum.getShippingQuorumDetails().size();i++){
			String ein = eins.get(i);
			ShippingQuorumDetail detail = quorum.getShippingQuorumDetails().get(i);
			detail.setEngineNumber(ein);
			detail.setStatus(ShippingQuorumDetailStatus.MANUAL_LOAD);
			
			List<ShippingQuorumDetail> loadedQuorums = getDao(ShippingQuorumDetailDao.class).findAllByEngineNumber(ein);
			boolean isUpdated = false;
			for(ShippingQuorumDetail loadedQuorumDetail : loadedQuorums) {
				if(!quorum.getShippingQuorumDetails().contains(loadedQuorumDetail)) {
					loadedQuorumDetail.setEngineNumber(null);
					loadedQuorumDetail.setStatus(ShippingQuorumDetailStatus.MANUAL_LOAD);
					ShippingQuorum loadedQuorum = findQuorum(loadedQuorumDetail.getId().getQuorumDate(), loadedQuorumDetail.getId().getQuorumId(), quorums); 
					loadedQuorum.setStatus(ShippingQuorumStatus.INCOMPLETE);
					ShippingTrailerInfo trailerInfo = findTrailerInfo(loadedQuorum.getTrailerId(), trailerInfos);
					trailerInfo.setActQty(trailerInfo.getActQty() - 1);
					ShippingVanningSchedule schedule = findScheduleToRemove(loadedQuorum.getTrailerId(), loadedQuorumDetail.getKdLot(), vanningSchedules); 
					schedule.setActQty(schedule.getActQty() -1);
					quorumDetails.add(loadedQuorumDetail);
					isUpdated = true;
				}
			}
			if(isUpdated || loadedQuorums.isEmpty()) {
				ShippingTrailerInfo trailerInfo = findTrailerInfo(quorum.getTrailerId(), trailerInfos);
				trailerInfo.setActQty(trailerInfo.getActQty() + 1);
				ShippingVanningSchedule schedule = findSchedule(quorum.getTrailerId(), detail.getKdLot(), vanningSchedules); 
				schedule.setActQty(schedule.getActQty() + 1);
			}
		}
		
		quorum.setStatus(ShippingQuorumStatus.COMPLETE);
		quorums.add(quorum);
		quorumDetails.addAll(quorum.getShippingQuorumDetails());
		
		shippingQuorumDetailDao.saveAll(quorumDetails);
		saveAll(quorums);
		shippingVanningScheduleDao.saveAll(vanningSchedules);
		shippingTrailerInfoDao.saveAll(trailerInfos);
	}
	
	
	@Transactional
	public void saveWithDetail(ShippingQuorum quorum){
		shippingQuorumDetailDao.saveAll(quorum.getShippingQuorumDetails());
		quorum.setShippingQuorumDetails(null);
		save(quorum);
	}
	
	private ShippingVanningSchedule findSchedule(int trailerId, String kdLot, List<ShippingVanningSchedule> schedules) {
		ShippingVanningSchedule schedule = shippingVanningScheduleDao.findIncompleteSchedule(trailerId, kdLot);
		int index = schedules.indexOf(schedule);
		if(index >=0 ) schedule = schedules.get(index);
		else schedules.add(schedule);
		return schedule; 
	}
	
	private ShippingVanningSchedule findScheduleToRemove(int trailerId, String kdLot, List<ShippingVanningSchedule> schedules) {
		ShippingVanningSchedule schedule = findScheduleToRemove(trailerId, kdLot);
		if(schedule == null ) return schedule;
		int index = schedules.indexOf(schedule);
		if(index >=0 ) schedule = schedules.get(index);
		else schedules.add(schedule);
		return schedule; 
	}
	
	private ShippingVanningSchedule findScheduleToRemove(int trailerId, String kdLot) {
		List<ShippingVanningSchedule> schedules = getDao(ShippingVanningScheduleDao.class).findVanningSchedules(trailerId,kdLot);
		for(int i=schedules.size();i>0;i--) {
			ShippingVanningSchedule schedule = schedules.get(i);
			if(schedule != null && schedule.getActQty() > 0) return schedule;
		}
		return null;
	}
	
	
	private ShippingQuorum findQuorum(Date quorumDate, int quorumId, List<ShippingQuorum> quorums) {
		ShippingQuorum loadedQuorum = new ShippingQuorum(quorumDate,quorumId);
		int index = quorums.indexOf(loadedQuorum);
		if(index <0 ){
			loadedQuorum = findByKey(loadedQuorum.getId());
			quorums.add(loadedQuorum);
		}else loadedQuorum = quorums.get(index);
		return loadedQuorum; 
	}
	
	public ShippingQuorum findCurrentShippingQuorum() {
		return findFirstByNativeQuery(FIND_CURRENT_SHIPPING_QUORUMS,null);
	}
	
	private ShippingTrailerInfo findTrailerInfo(int trailerId, List<ShippingTrailerInfo> trailerInfos) {
		ShippingTrailerInfo trailerInfo = new ShippingTrailerInfo();
		trailerInfo.setTrailerId(trailerId);
		int index = trailerInfos.indexOf(trailerInfo);
		if(index < 0){
			trailerInfo = shippingTrailerInfoDao.findByKey(trailerId);
			trailerInfos.add(trailerInfo);
		}else trailerInfo = trailerInfos.get(index);
		return trailerInfo;
	}

	public List<ShippingQuorum> findAllByTrailerId(int trailerId) {
		return findAll(Parameters.with("trailerId",trailerId), new String[]{"id.quorumDate","id.quorumId"});
	}

	
 
}

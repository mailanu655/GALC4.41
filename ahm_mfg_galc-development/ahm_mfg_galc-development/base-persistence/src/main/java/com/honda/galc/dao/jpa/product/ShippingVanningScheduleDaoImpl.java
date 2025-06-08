package com.honda.galc.dao.jpa.product;


import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.dao.product.ShippingQuorumDao;
import com.honda.galc.dao.product.ShippingQuorumDetailDao;
import com.honda.galc.dao.product.ShippingTrailerInfoDao;
import com.honda.galc.dao.product.ShippingVanningScheduleDao;
import com.honda.galc.entity.enumtype.ShippingQuorumStatus;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.entity.product.ShippingQuorum;
import com.honda.galc.entity.product.ShippingQuorumDetail;
import com.honda.galc.entity.product.ShippingTrailerInfo;
import com.honda.galc.entity.product.ShippingVanningSchedule;
import com.honda.galc.entity.product.ShippingVanningScheduleId;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>ShippingVanningScheduleDaoImpl Class description</h3>
 * <p> ShippingVanningScheduleDaoImpl description </p>
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
public class ShippingVanningScheduleDaoImpl extends BaseDaoImpl<ShippingVanningSchedule,ShippingVanningScheduleId> implements ShippingVanningScheduleDao {
	
	private static final String FIND_ALL_ACTIVE_VANNING_SCHEDULES =
		"SELECT A.PRODUCTION_DATE,A.VANNING_SEQ,A.TRAILER_ID,A.KD_LOT,A.PRODUCTION_LOT,A.YMTO,A.SCH_QTY,A.ACT_QTY,B.TRAILER_NUMBER " +
		" FROM GALADM.VANNING_SCHEDULE_TBX A " + 
		"LEFT OUTER JOIN GALADM.TRAILER_INFO_TBX B on A.TRAILER_ID = B.TRAILER_ID " + 
		"WHERE A.TRAILER_ID is NULL OR A.TRAILER_ID=0 OR (B.STATUS != 4 and B.STATUS != 5) " + 
		" ORDER BY A.PRODUCTION_DATE, A.VANNING_SEQ";
	
	private static final String FIND_ALL_COMPLETE_VANNING_SCHEDULES =
		"SELECT A.* FROM GALADM.VANNING_SCHEDULE_TBX A " + 
		"LEFT OUTER JOIN GALADM.TRAILER_INFO_TBX B on A.TRAILER_ID = B.TRAILER_ID " +
		"WHERE B.STATUS = 4 " +
		" ORDER BY A.PRODUCTION_DATE, A.VANNING_SEQ";
		
	private static final String RESET_TRAILER_ID = 
		"UPDATE GALADM.VANNING_SCHEDULE_TBX SET TRAILER_ID = NULL WHERE TRAILER_ID = ?1";
	
	private static final String FIND_ALL_PARTIAL_LOAD_SCHEDULES =
		"SELECT A.PRODUCTION_DATE,A.VANNING_SEQ,A.TRAILER_ID,A.KD_LOT,A.PRODUCTION_LOT,A.YMTO,A.SCH_QTY,A.ACT_QTY,B.TRAILER_NUMBER " +
		"FROM GALADM.VANNING_SCHEDULE_TBX A " +   
		"LEFT OUTER JOIN GALADM.TRAILER_INFO_TBX B on A.TRAILER_ID = B.TRAILER_ID " + 
		"WHERE (B.STATUS = 4 OR B.STATUS = 5) AND A.ACT_QTY < A.SCH_QTY " +  
		"ORDER BY A.PRODUCTION_DATE, A.VANNING_SEQ ";
	
	private static final String FIND_SCHEDULES = 
		"SELECT * " +  
		"        FROM GALADM.VANNING_SCHEDULE_TBX A " +  
		"        WHERE A.TRAILER_ID = ?1 AND A.KD_LOT = ?2  " +  
		"        ORDER BY A.PRODUCTION_DATE ASC, A.VANNING_SEQ ASC";
	
	@Autowired
	ShippingQuorumDao shippingQuorumDao;
	
	@Autowired
	ShippingQuorumDetailDao shippingQuorumDetailDao;
	
	@Autowired
	ShippingTrailerInfoDao ShippingTrailerInfoDao;
	
	@Autowired
	PreProductionLotDao preProductionLotDao;
	
	@Transactional
	public int deleteAllByProductionLot(String productionLot) {
		
		return delete(Parameters.with("productionLot", productionLot));
	}

	@SuppressWarnings("unchecked")
	public List<ShippingVanningSchedule> findAllActiveVanningSchedules() {
		List<Object[]> results = findResultListByNativeQuery(FIND_ALL_ACTIVE_VANNING_SCHEDULES, null);
		return toVanningSchedules(results);
	}

	@Transactional
	public int removeTrailerId(int trailerId) {
		return executeNativeUpdate(RESET_TRAILER_ID, Parameters.with("1", trailerId));
	}

	public List<ShippingVanningSchedule> findVanningSchedules(int trailerId, String kdLot) {
		return findAllByNativeQuery(FIND_SCHEDULES,Parameters.with("1", trailerId).put("2", kdLot));
	}

	public List<ShippingVanningSchedule> findAllCompleteVanningSchedules() {
		return findAllByNativeQuery(FIND_ALL_COMPLETE_VANNING_SCHEDULES, null);
	}

	@SuppressWarnings("unchecked")
	public List<ShippingVanningSchedule> findAllPartialLoadSchedules() {
		List<Object[]> results = findResultListByNativeQuery(FIND_ALL_PARTIAL_LOAD_SCHEDULES, null);
		return toVanningSchedules(results);
	}
	
	private List<ShippingVanningSchedule> toVanningSchedules(List<Object[]> results) {
		List<ShippingVanningSchedule> schedules = new ArrayList<ShippingVanningSchedule>();
		for(Object[] objects : results) {
			ShippingVanningSchedule schedule = new ShippingVanningSchedule();
			ShippingVanningScheduleId id = new ShippingVanningScheduleId();
			id.setProductionDate((Date)objects[0]);
			id.setVanningSeq((Integer)objects[1]);
			schedule.setId(id);
			schedule.setTrailerId((Integer)objects[2]);
			schedule.setKdLot((String)objects[3]);
			schedule.setProductionLot((String)objects[4]);
			schedule.setYmto((String)objects[5]);
			schedule.setSchQty((Integer)objects[6]);
			schedule.setActQty((Integer)objects[7]);
			schedule.setTrailerNumber((String)objects[8]);
			schedules.add(schedule);
		}
		return schedules;
	}
	
	@Transactional
	public boolean loadEngineNumber(ShippingVanningSchedule schedule,String engineNumber) {
		ShippingTrailerInfo trailerInfo = ShippingTrailerInfoDao.findByKey(schedule.getTrailerId());
		
		for(ShippingQuorum quorum : trailerInfo.getShippingQuorums()) {
			for(ShippingQuorumDetail detail : quorum.getShippingQuorumDetails()) {
				if(StringUtils.isEmpty(detail.getEngineNumber()) && schedule.getYmto().equalsIgnoreCase(detail.getYmto())) {
					detail.setEngineNumber(engineNumber);
					if(quorum.getLoadedCount() == quorum.getActualQuorumSize()){
						shippingQuorumDao.updateStatus(quorum.getId().getQuorumDate(), quorum.getId().getQuorumId(), ShippingQuorumStatus.COMPLETE);
					}
					shippingQuorumDetailDao.save(detail);
					if(schedule.getActQty() < schedule.getSchQty()){
						schedule.setActQty(schedule.getActQty() + 1);
						save(schedule);
					}
					if(trailerInfo.getActQty() < trailerInfo.getSchQty()){
						trailerInfo.setActQty(trailerInfo.getActQty() + 1);
						ShippingTrailerInfoDao.save(trailerInfo);
					}
					return true;
				}
			}
		}
		
		return false;

	}
	
	public ShippingVanningSchedule findIncompleteSchedule(int trailerId, String kdLot){
		List<ShippingVanningSchedule> schedules = findVanningSchedules(trailerId,kdLot);
		for(ShippingVanningSchedule schedule : schedules) {
			if(schedule.getActQty() < schedule.getSchQty()) return schedule;
		}
		return null;
	}
	
	public ShippingVanningSchedule findScheduleToRemove(int trailerId, String kdLot) {
		List<ShippingVanningSchedule> schedules = findVanningSchedules(trailerId,kdLot);
		for(int i=schedules.size()-1;i>0;i--) {
			ShippingVanningSchedule schedule = schedules.get(i);
			if(schedule != null && schedule.getActQty() > 0) return schedule;
		}
		return null;
	}

	
	private  ShippingVanningSchedule findLastVanningSchedule(){
		return findFirst(null, new String[]{"id.productionDate","id.vanningSeq"}, false); 
	}

	@Transactional
	public void syncVanningSchedule() {
		List<ProductionLot> productionLots = preProductionLotDao.findAllNewEngineShippingLots();
		if(productionLots.isEmpty()) return;
		ShippingVanningSchedule lastSchedule = findLastVanningSchedule();
		for(ProductionLot prodLot :productionLots) {
			ShippingVanningSchedule schedule = new ShippingVanningSchedule();
			if(lastSchedule != null && !prodLot.getPlanOffDate().after(lastSchedule.getId().getProductionDate())) {
				schedule = new ShippingVanningSchedule(lastSchedule.getId().getProductionDate(),
						         lastSchedule.getId().getVanningSeq() + ShippingVanningSchedule.VANNING_SEQ_INTERVAL);
			}else {
				schedule = new ShippingVanningSchedule(prodLot.getPlanOffDate(),ShippingVanningSchedule.VANNING_SEQ_INTERVAL);
			}
			schedule.setKdLot(prodLot.getKdLotNumber());
			schedule.setProductionLot(prodLot.getProductionLot());
			schedule.setSchQty(prodLot.getLotSize());
			schedule.setYmto(prodLot.getProductSpecCode());
			save(schedule);
			lastSchedule = schedule;
		}
	}

	private ShippingVanningSchedule findSchedule(String productionLot,String kdLot) {
		return findFirst(Parameters.with("productionLot", productionLot).put("kdLot", kdLot)); 
	}

	@Transactional
	public ShippingVanningSchedule saveSchedule(PreProductionLot preProductionLot) {
		ShippingVanningSchedule oldSchedule = findSchedule(preProductionLot.getProductionLot(),preProductionLot.getKdLot());
		if(oldSchedule != null) return null;
		ShippingVanningSchedule lastSchedule = findLastVanningSchedule();
		
		ProductionLot prodLot = getProductionLot(preProductionLot.getProductionLot());
		if(prodLot == null) return null;
		
		ShippingVanningSchedule schedule = new ShippingVanningSchedule();
		if(lastSchedule != null && !prodLot.getPlanOffDate().after(lastSchedule.getId().getProductionDate())) {
			schedule = new ShippingVanningSchedule(lastSchedule.getId().getProductionDate(),
					         lastSchedule.getId().getVanningSeq() + ShippingVanningSchedule.VANNING_SEQ_INTERVAL);
		}else {
			schedule = new ShippingVanningSchedule(prodLot.getPlanOffDate(),ShippingVanningSchedule.VANNING_SEQ_INTERVAL);
		}
		schedule.setKdLot(prodLot.getKdLotNumber());
		schedule.setProductionLot(prodLot.getProductionLot());
		schedule.setSchQty(prodLot.getLotSize());
		schedule.setYmto(prodLot.getProductSpecCode());
		save(schedule);
		return schedule;
	}
	
	public ProductionLot getProductionLot(String productionLot) {
		return getDao(ProductionLotDao.class).findByKey(productionLot);
	}
	
 
}

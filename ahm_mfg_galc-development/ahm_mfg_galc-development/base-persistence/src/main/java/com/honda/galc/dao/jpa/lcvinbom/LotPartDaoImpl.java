package com.honda.galc.dao.jpa.lcvinbom;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.lcvinbom.LotPartDao;
import com.honda.galc.entity.enumtype.VinBomActiveStatus;
import com.honda.galc.entity.lcvinbom.LotPart;
import com.honda.galc.entity.lcvinbom.LotPartId;
import com.honda.galc.service.Parameters;
import com.honda.galc.util.StringUtil;

public class LotPartDaoImpl extends BaseDaoImpl<LotPart, LotPartId> 
	implements LotPartDao {

	private static final String FIND_PART_NUMBERS_BY_PRODUCTION_LOT_AND_SYSTEM = "SELECT DISTINCT DC_PART_NUMBER FROM LCVINBOM.LOT_PART WHERE PRODUCTION_LOT = ?1 AND LET_SYSTEM_NAME = ?2";

	private static final String FIND_ALL_LOTS_BY_PRODUCTION_LOT_AND_SYSTEM = "SELECT * FROM LCVINBOM.LOT_PART WHERE PRODUCTION_LOT = ?1 AND LET_SYSTEM_NAME = ?2";
	
	private static final String DELETE_LOT_PARTS = "delete from lcvinbom.lot_part "	
			+ "where production_lot in (select production_lot from GALADM.gal143tbx where substr(product_spec_code,1,7) = ?2 and tracking_status not in (@TRACKING_STATUS@)) and let_system_name = ?3";
	
	private static final String INSERT_LOT_PARTS = "insert into lcvinbom.lot_part select distinct production_lot, ?3, ?1, 0, CURRENT TIMESTAMP, null from lcvinbom.lot_part "	
			+ "where production_lot in (select production_lot from GALADM.gal143tbx where substr(product_spec_code,1,7) = ?2 and tracking_status not in (@TRACKING_STATUS@))";
	
	private static final String DELETE_STRAGGLER_LOT_PARTS = "delete from lcvinbom.lot_part "	
			+ "where production_lot in (select b.PRODUCTION_LOT from GALADM.STRAGGLER_TBX a inner join GALADM.gal143tbx b on a.PRODUCT_ID = b.PRODUCT_ID where a.PP_DELAYED_AT = ?4 and substr(b.product_spec_code,1,7) = ?2 and b.tracking_status not in (@TRACKING_STATUS@)) and let_system_name = ?3";
	
	private static final String DELETE_STRAGGLER_LOT_PARTS_WITH_PARTNUMBER = "delete from lcvinbom.lot_part "	
			+ "where production_lot in (select b.PRODUCTION_LOT from GALADM.STRAGGLER_TBX a inner join GALADM.gal143tbx b on a.PRODUCT_ID = b.PRODUCT_ID where a.PP_DELAYED_AT = ?4 and substr(b.product_spec_code,1,7) = ?2 and b.tracking_status not in (@TRACKING_STATUS@)) and let_system_name = ?3 and dc_part_number = ?1";
	
	private static final String INSERT_STRAGGLER_LOT_PARTS = "insert into lcvinbom.lot_part select distinct production_lot, ?3, ?1, 1, CURRENT TIMESTAMP, null from lcvinbom.lot_part "	
			+ "where production_lot in (select b.PRODUCTION_LOT from GALADM.STRAGGLER_TBX a inner join GALADM.gal143tbx b on a.PRODUCT_ID = b.PRODUCT_ID where a.PP_DELAYED_AT = ?4 and substr(b.product_spec_code,1,7) = ?2 and b.tracking_status not in (@TRACKING_STATUS@))";
	
	@Override
	public List<String> getPartNumbersByProductionLotAndSystemName(String productionLot, String systemName) {
		Parameters parameters = Parameters.with("1", productionLot);
		parameters.put("2",systemName);
		return findAllByNativeQuery(FIND_PART_NUMBERS_BY_PRODUCTION_LOT_AND_SYSTEM, parameters, String.class);
	}
	
	@Override
	public List<LotPart> getByProductionLotAndSystemName(String productionLot, String systemName) {
		Parameters parameters = Parameters.with("1", productionLot);
		parameters.put("2",systemName);
		return findAllByNativeQuery(FIND_ALL_LOTS_BY_PRODUCTION_LOT_AND_SYSTEM, parameters);
	}
	
	@Override
	@Transactional
	public void setInterchangableInactive(String letSystemName, String dcPartNumber) {
		Parameters params = Parameters.with("id.letSystemName", letSystemName).put("id.dcPartNumber", dcPartNumber);
		List<LotPart> lotPartList = findAll(params);
		if(lotPartList!=null && !lotPartList.isEmpty()) {
			for(LotPart lotPart: lotPartList) {
				lotPart.setActive(VinBomActiveStatus.DEPRECATED);
				save(lotPart);
			}
		}
	}
	
	@Transactional
	@Override
	public void deleteLotParts(String partNumber, String productSpecCode, List<String> trackingStatuses, String systemName) {
		Parameters parameters = Parameters.with("1", partNumber);
		parameters.put("2",productSpecCode);
		parameters.put("3",systemName);
		String trackingStatusSql = "";
		if(trackingStatuses != null && trackingStatuses.size() > 0){
			trackingStatusSql = StringUtil.toSqlInString(trackingStatuses);
		}
		executeNativeUpdate(DELETE_LOT_PARTS.replace("@TRACKING_STATUS@", trackingStatusSql), parameters);
	}
	
	@Transactional
	@Override
	public void insertLotParts(String partNumber, String productSpecCode, List<String> trackingStatuses, String systemName) {
		Parameters parameters = Parameters.with("1", partNumber);
		parameters.put("2",productSpecCode);
		parameters.put("3",systemName);
		String trackingStatusSql = "";
		if(trackingStatuses != null && trackingStatuses.size() > 0){
			trackingStatusSql = StringUtil.toSqlInString(trackingStatuses);
		}
		executeNativeUpdate(INSERT_LOT_PARTS.replace("@TRACKING_STATUS@", trackingStatusSql), parameters);
	}
	
	@Transactional
	@Override
	public void insertStragglerLotParts(String partNumber, String productSpecCode, List<String> trackingStatuses, String systemName, String processPoint) {
		Parameters parameters = Parameters.with("1", partNumber);
		parameters.put("2",productSpecCode);
		parameters.put("3",systemName);
		parameters.put("4",processPoint);
		String trackingStatusSql = "";
		if(trackingStatuses != null && trackingStatuses.size() > 0){
			trackingStatusSql = StringUtil.toSqlInString(trackingStatuses);
		}
		executeNativeUpdate(INSERT_STRAGGLER_LOT_PARTS.replace("@TRACKING_STATUS@", trackingStatusSql), parameters);
	}
	
	@Transactional
	@Override
	public void deleteStragglerLotParts(String partNumber, String productSpecCode, List<String> trackingStatuses, String systemName, String processPoint) {
		Parameters parameters = Parameters.with("1", partNumber);
		parameters.put("2",productSpecCode);
		parameters.put("3",systemName);
		parameters.put("4",processPoint);
		String trackingStatusSql = "";
		if(trackingStatuses != null && trackingStatuses.size() > 0){
			trackingStatusSql = StringUtil.toSqlInString(trackingStatuses);
		}
		executeNativeUpdate(DELETE_STRAGGLER_LOT_PARTS.replace("@TRACKING_STATUS@", trackingStatusSql), parameters);
	}

	@Transactional
	@Override
	public void deleteStragglerLotPartsWithPartNumber(String partNumber, String productSpecCode, List<String> trackingStatuses, String systemName, String processPoint) {
		Parameters parameters = Parameters.with("1", partNumber);
		parameters.put("2",productSpecCode);
		parameters.put("3",systemName);
		parameters.put("4",processPoint);
		String trackingStatusSql = "";
		if(trackingStatuses != null && trackingStatuses.size() > 0){
			trackingStatusSql = StringUtil.toSqlInString(trackingStatuses);
		}
		executeNativeUpdate(DELETE_STRAGGLER_LOT_PARTS_WITH_PARTNUMBER.replace("@TRACKING_STATUS@", trackingStatusSql), parameters);
	}
}

package com.honda.galc.dao.jpa.product;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.KickoutDao;
import com.honda.galc.dto.KickoutDto;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.enumtype.KickoutStatus;
import com.honda.galc.entity.product.Kickout;
import com.honda.galc.service.Parameters;
import com.honda.galc.util.StringUtil;


public class KickoutDaoImpl extends BaseDaoImpl<Kickout, Long> implements KickoutDao{
	private static final String FIND_ACTIVE_KICKOUTS_BY_PRODUCT_IDS = "select * from KICKOUT_TBX k join KICKOUT_LOCATION_TBX l on k.KICKOUT_ID = l.KICKOUT_ID where k.KICKOUT_STATUS = 1 and k.PRODUCT_ID in ";
	private static final String FIND_ALL_ACTIVE_BY_PRODUCT_ID_AND_PROCESS_POINT = "SELECT " +
			  "k.*" +
			  "FROM galadm.KICKOUT_TBX k " +
			  "inner join KICKOUT_LOCATION_TBX h on k.kickout_id = h.kickout_id " +
			  "inner join gal214tbx p on p.PROCESS_POINT_ID = h.PROCESS_POINT_ID " +
			  "inner join GAL195TBX l on l.LINE_ID=p.LINE_ID " +
			  "inner join GAL128TBX d on d.DIVISION_ID = l.DIVISION_ID " +
			  "WHERE k.PRODUCT_ID = ?1 " +
			  "and h.PROCESS_POINT_ID IS NOT NULL " +
			  "AND h.PROCESS_POINT_ID !='' " +
			  "AND k.KICKOUT_STATUS = 1 " +
			  "and d.SEQUENCE_NUMBER <= " +
			  "( " +
			  "   SELECT " +
			  "   d.SEQUENCE_NUMBER " +
			  "   FROM gal214tbx p1 " +
			  "   inner join GAL195TBX l on l.LINE_ID=p1.LINE_ID " +
			  "   inner join GAL128TBX d on d.DIVISION_ID = l.DIVISION_ID " +
			  "   WHERE p1.PROCESS_POINT_ID = ?2 " +
			  ") " +
			  "and " +
			  "( " +
			  "   l.LINE_SEQUENCE_NUMBER <= " +
			  "   ( " +
			  "      SELECT " +
			  "      l.LINE_SEQUENCE_NUMBER " +
			  "      FROM gal214tbx p2 " +
			  "      inner join GAL195TBX l on l.LINE_ID=p2.LINE_ID " +
			  "      inner join GAL128TBX d on d.DIVISION_ID = l.DIVISION_ID " +
			  "      WHERE p2.PROCESS_POINT_ID = ?2 " +
			  "   ) " +
			  "   or d.SEQUENCE_NUMBER < " +
			  "   ( " +
			  "      SELECT " +
			  "      d.SEQUENCE_NUMBER " +
			  "      FROM gal214tbx p1 " +
			  "      inner join GAL195TBX l on l.LINE_ID=p1.LINE_ID " +
			  "      inner join GAL128TBX d on d.DIVISION_ID = l.DIVISION_ID " +
			  "      WHERE p1.PROCESS_POINT_ID = ?2 " +
			  "   ) " +
			  ") " +
			  "and " +
			  "( " +
			  "   p.SEQUENCE_NUMBER <= CAST(?3 AS INTEGER) or l.LINE_SEQUENCE_NUMBER < " +
			  "   ( " +
			  "      SELECT " +
			  "      l.LINE_SEQUENCE_NUMBER " +
			  "      FROM gal214tbx p2 " +
			  "      inner join GAL195TBX l on l.LINE_ID=p2.LINE_ID " +
			  "      inner join GAL128TBX d on d.DIVISION_ID = l.DIVISION_ID " +
			  "      WHERE p2.PROCESS_POINT_ID = ?2 " +
			  "   ) " +
			  "   or d.SEQUENCE_NUMBER < " +
			  "   ( " +
			  "      SELECT " +
			  "      d.SEQUENCE_NUMBER " +
			  "      FROM gal214tbx p1 " +
			  "      inner join GAL195TBX l on l.LINE_ID=p1.LINE_ID " +
			  "      inner join GAL128TBX d on d.DIVISION_ID = l.DIVISION_ID " +
			  "      WHERE p1.PROCESS_POINT_ID = ?2 " +
			  "   ) " +
			  ") ";
	private static final String FIND_ACTIVE_KICKOUT_INFO_BY_PRODUCT_ID = "select * from GALADM.KICKOUT_TBX kickout join GALADM.KICKOUT_LOCATION_TBX location on kickout.KICKOUT_ID = location.KICKOUT_ID"
			+ " where kickout.PRODUCT_ID = ?1 and kickout.KICKOUT_STATUS = ?2";
	
	private static final String FIND_ACTIVE_KICKOUTS_BY_TRASACTION_ID = "SELECT * FROM GALADM.KICKOUT_TBX k join GALADM.KICKOUT_LOCATION_TBX l on k.KICKOUT_ID = l.KICKOUT_ID where k.KICKOUT_STATUS = 1 " 
			+ " and k.KICKOUT_ID in (SELECT KICKOUT_ID FROM GALADM.QI_DEFECT_RESULT_TBX where DEFECT_TRANSACTION_GROUP_ID = ?1";

	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
	public List<KickoutDto> findProductsWithKickout(List<String> productIdList) {
		if(productIdList == null || productIdList.isEmpty()) {
			return new ArrayList<KickoutDto>();
		}

		StringBuilder sb = new StringBuilder(FIND_ACTIVE_KICKOUTS_BY_PRODUCT_IDS);
		sb.append("(" + StringUtil.toSqlInString(productIdList) + ")");
		return findAllByNativeQuery(sb.toString(), null, KickoutDto.class);
	}
	
	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
	public List<KickoutDto> findProductsWithKickoutAndTransactionId(long transactionId, boolean fixedDefectFlag) {
		
		StringBuilder sb = new StringBuilder(FIND_ACTIVE_KICKOUTS_BY_TRASACTION_ID);
		Parameters parameters = Parameters.with("1",transactionId);
		if(fixedDefectFlag) {
		sb = sb.append(" AND CURRENT_DEFECT_STATUS = "+ DefectStatus.FIXED.getId() + ")");
		}else {
			sb.append(")");
		}
		return findAllByNativeQuery(sb.toString(), parameters, KickoutDto.class);
	}

	public List<Kickout> findAllActiveByProductId(String productId) {
		Parameters params = Parameters.with("productId", productId).put("kickoutStatus", KickoutStatus.OUTSTANDING.getId());
		return findAll(params);
	}
	
	public List<Kickout> findAllActiveByProductIdAndProcessPoint(String productId, String processPointId, int currentProcessSeq) {
		Parameters params = new Parameters();
		params.put("1", productId);
		params.put("2", processPointId);
		params.put("3", currentProcessSeq);

		return findAllByNativeQuery(FIND_ALL_ACTIVE_BY_PRODUCT_ID_AND_PROCESS_POINT, params);
	}
	
	public List<KickoutDto> findActiveKickoutInfoByProductId(String productId) {
		Parameters params = Parameters.with("1", productId).put("2", KickoutStatus.OUTSTANDING.getId());
		List<KickoutDto> kickoutDtoList = findAllByNativeQuery(FIND_ACTIVE_KICKOUT_INFO_BY_PRODUCT_ID, params, KickoutDto.class);
		return kickoutDtoList;
	}
	
	@Transactional
	public int releaseKickout(Kickout kickout) {
		Parameters whereParams = Parameters.with("kickoutId", kickout.getKickoutId());
		Parameters updateParams = Parameters.with("kickoutStatus", kickout.getKickoutStatus())
				.put("releaseUser", kickout.getReleaseUser())
				.put("releaseComment", kickout.getReleaseComment());
		return update(updateParams, whereParams);
	}
}
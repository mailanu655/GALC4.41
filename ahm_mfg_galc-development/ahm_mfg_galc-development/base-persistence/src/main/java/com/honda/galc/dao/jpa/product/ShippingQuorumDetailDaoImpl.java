package com.honda.galc.dao.jpa.product;


import java.sql.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.ShippingQuorumDetailDao;
import com.honda.galc.dto.ShippingQuorumDetailDto;
import com.honda.galc.entity.product.ShippingQuorumDetail;
import com.honda.galc.entity.product.ShippingQuorumDetailId;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>ShippingQuorumDetailDaoImpl Class description</h3>
 * <p> ShippingQuorumDetailDaoImpl description </p>
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
public class ShippingQuorumDetailDaoImpl extends BaseDaoImpl<ShippingQuorumDetail,ShippingQuorumDetailId> implements ShippingQuorumDetailDao {

	private static final String DELETE_BY_TRAILER_ID = "DELETE FROM GALADM.QUORUM_DETAIL_TBX A WHERE EXISTS (SELECT * FROM GALADM.QUORUM_TBX B " +  
		"WHERE B.TRAILER_ID = ? AND A.QUORUM_DATE = B.QUORUM_DATE AND a.QUORUM_ID = B.QUORUM_ID)";
	
	private static final String SHIFT_QUORUM_SEQ = 
		"UPDATE GALADM.QUORUM_DETAIL_TBX SET QUORUM_ID = QUORUM_ID + ?1 WHERE QUORUM_DATE = ?2 AND QUORUM_ID > ?3";
	
	private static final String CHANGE_QUORUM = 
		"UPDATE GALADM.QUORUM_DETAIL_TBX SET QUORUM_DATE = ?1,QUORUM_ID = ?2 " + 
		" WHERE QUORUM_DATE = ?3 AND QUORUM_ID = ?4";
	
	private static final String CHECK_ENGINE_MODELS = 
		"SELECT a.QUORUM_DATE,a.QUORUM_ID,a.QUORUM_SEQ,a.YMTO,a.ENGINE_NUMBER,c.PRODUCT_SPEC_CODE as KD_LOT,a.STATUS,a.CREATE_TIMESTAMP,a.UPDATE_TIMESTAMP " + 
		"FROM GALADM.QUORUM_DETAIL_TBX a,GALADM.QUORUM_TBX b,GALADM.GAL131TBX c " + 
		"WHERE b.TRAILER_ID = ?1 AND a.QUORUM_DATE = b.QUORUM_DATE AND a.QUORUM_ID = b.QUORUM_ID AND a.ENGINE_NUMBER = c.PRODUCT_ID AND a.YMTO <> c.PRODUCT_SPEC_CODE"; 
	
	private static final String ALL_DETAILS =
		"SELECT B.TRAILER_ID,B.TRAILER_ROW,A.QUORUM_SEQ,A.KD_LOT,A.YMTO,A.ENGINE_NUMBER " + 
		"FROM GALADM.QUORUM_DETAIL_TBX A,GALADM.QUORUM_TBX B "+ 
		"WHERE B.TRAILER_ID = ?1 AND A.QUORUM_DATE = B.QUORUM_DATE AND A.QUORUM_ID = B.QUORUM_ID "+
		"ORDER BY TRAILER_ROW,A.QUORUM_SEQ";
	
	private static final String ALL_DETAILS_BY_EIN=
		"SELECT T.TRAILER_NUMBER, T.TRAILER_ID, D.ENGINE_NUMBER,D.QUORUM_SEQ,D.KD_LOT, D.YMTO ,T.STATUS " + 
		"FROM GALADM.QUORUM_DETAIL_TBX d " + 
		"LEFT JOIN GALADM.QUORUM_TBX q ON d.QUORUM_DATE = q.QUORUM_DATE and d.QUORUM_ID = q.QUORUM_ID " +
		"LEFT JOIN GALADM.TRAILER_INFO_TBX t ON t.TRAILER_ID = q.TRAILER_ID " +
		"WHERE d.ENGINE_NUMBER = ?1";	
			
	private static final String ALL_DETAILS_BY_KD_LOT=
		"SELECT T.TRAILER_NUMBER, T.TRAILER_ID, Q.TRAILER_ROW,D.QUORUM_SEQ, D.KD_LOT, D.YMTO ,D.ENGINE_NUMBER  " +
		"FROM GALADM.QUORUM_DETAIL_TBX D, GALADM.QUORUM_TBX Q,GALADM.TRAILER_INFO_TBX T " +
		"WHERE T.TRAILER_ID = Q.TRAILER_ID AND Q.QUORUM_DATE=D.QUORUM_DATE AND Q.QUORUM_ID=D.QUORUM_ID AND D.KD_LOT=?1 " +
		"ORDER BY Q.TRAILER_ID,Q.TRAILER_ROW, D.QUORUM_SEQ";

	@Transactional
	public int deleteAllByTrailerId(int trailerId) {
		return executeNativeUpdate(DELETE_BY_TRAILER_ID, Parameters.with("1", trailerId));
	}

	@Transactional
	public int shiftQuorumSeq(Date quorumDate, int quorumId,int offset) {
		return executeNativeUpdate(SHIFT_QUORUM_SEQ, Parameters.with("1",offset).put("2",quorumDate).put("3",quorumId));
	}

	@Transactional
	public int changeQuorum(Date fromQuorumDate, int fromQuorumId,Date toQuorumDate, int toQuorumId) {
		return executeNativeUpdate(CHANGE_QUORUM, 
				Parameters.with("1",toQuorumDate).put("2",toQuorumId).put("3",fromQuorumDate).put("4",fromQuorumId));
	}

	public List<ShippingQuorumDetail> findAllByEngineNumber(String ein) {
		return findAll(Parameters.with("engineNumber", ein));
	}

	public List<ShippingQuorumDetail> checkEngineModels(int trailerId) {
		return findAllByNativeQuery(CHECK_ENGINE_MODELS, Parameters.with("1", trailerId));
	}

	public List<ShippingQuorumDetailDto> findAllDetails(int trailerId) {
		return findAllByNativeQuery(ALL_DETAILS, Parameters.with("1",trailerId), ShippingQuorumDetailDto.class);
	}

	public List<ShippingQuorumDetailDto> findAllDetailsByEngineNumber(String ein) {
		return findAllByNativeQuery(ALL_DETAILS_BY_EIN, Parameters.with("1",ein), ShippingQuorumDetailDto.class);
	}

	public List<ShippingQuorumDetailDto> findAllDetailsByKdLot(String kdLot) {
		return findAllByNativeQuery(ALL_DETAILS_BY_KD_LOT, Parameters.with("1",kdLot), ShippingQuorumDetailDto.class);
	}
 
}

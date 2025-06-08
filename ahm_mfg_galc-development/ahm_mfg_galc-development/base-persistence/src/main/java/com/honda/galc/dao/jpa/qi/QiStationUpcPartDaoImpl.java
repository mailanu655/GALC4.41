package com.honda.galc.dao.jpa.qi;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiStationUpcPartDao;
import com.honda.galc.entity.qi.QiStationUpcPart;
import com.honda.galc.entity.qi.QiStationUpcPartId;
import com.honda.galc.service.Parameters;
/**
 * 
 * <h3>QiStationUpcPartDaoImpl Class description</h3>
 * <p>
 * QiStationUpcPartDaoImpl contains methods to find all Station UPCPart By ProcessPointId
 *  </p>
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
 * @author LnTInfotech<br>
 *        Oct 25,2016
 * 
 */
public class QiStationUpcPartDaoImpl extends BaseDaoImpl<QiStationUpcPart, QiStationUpcPartId> implements QiStationUpcPartDao{

	private static final String FIND_MBPN_BY_PROCESS_POINT_AND_PRODUCT = "select a.* from QI_STATION_UPC_PART_TBX a, MBPN_PRODUCT_TBX b, MBPN_TBX c " + 
			"where a.MAIN_PART_NO = c.MAIN_NO and b.CURRENT_PRODUCT_SPEC_CODE = c.PRODUCT_SPEC_CODE  and " + 
			"a.PROCESS_POINT_ID = ?1 and b.PRODUCT_ID = ?2";
	
	/**
	 * This method is used to find UPCPart by Processpoint
	 */
	public List<QiStationUpcPart> findAllByProcessPointId(String processPointId) {
		return findAll(Parameters.with("id.processPointId", processPointId));
	}

	/*
	 * this method is used to fetch a count of QiStationUpcPart by processPoint
	 */
	public long countByProcessPoint(String processPointId) {
		return count(Parameters.with("id.processPointId",processPointId));
	}

	/**
	 * This method is used to delete QiStationUpcPart by process point
	 */
	@Transactional
	public int deleteByProcessPoint(String processPointId) {
		return delete(Parameters.with("id.processPointId", processPointId));
	}
	
	public boolean isValidMBPN(String processPointId, String productID) {
		QiStationUpcPart qiStationUpcPart = findFirstByNativeQuery(FIND_MBPN_BY_PROCESS_POINT_AND_PRODUCT, Parameters.with("1", processPointId).put("2", productID));
		return qiStationUpcPart == null? false : true;
	}
}

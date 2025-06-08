package com.honda.galc.dao.jpa.product;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.HoldParmDao;
import com.honda.galc.entity.product.HoldParm;
import com.honda.galc.service.Parameters;
/**
 * 
 * <h3>HoldParmDaoImpl</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> HoldParmDaoImpl description </p>
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
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Jun 28, 2012</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Jun 28, 2012
 */
public class HoldParmDaoImpl extends BaseDaoImpl<HoldParm,Integer>implements HoldParmDao{

	private final String RELEASE_EXPIRED = "update HoldParm e set e.releaseFlag = 1 where e.releaseFlag = 0 and e.stopDate < :stopDate";
	private final String FIND_ACTIVE_BY_PRODUCT_TYPE = "select e from HoldParm e, Qsr q where e.releaseFlag = 0 and e.qsrId = q.id and q.productType = :productType";  
	
	@Transactional
	public HoldParm insert(HoldParm entity) {
		if (entity.getHoldId() == 0) {
			Long max = max("holdId", long.class);
			long maxId = max == null ? 1 : max + 1;
			entity.setHoldId(maxId);
		}		
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		entity.setActualTimestamp(timestamp);
		entity.setCreateTimestamp(timestamp);
		return super.insert(entity);		
	}
	
	public List<HoldParm> findActiveHolds() {
		
		return findAll(Parameters.with("releaseFlag", Integer.valueOf(0)));
	}
	
	public List<HoldParm> findActiveHolds(String productType) {
		return findAllByQuery(FIND_ACTIVE_BY_PRODUCT_TYPE, Parameters.with("productType", productType));
	}	

	public List<HoldParm> findAll(int releaseFlag) {
		return findAll(Parameters.with("releaseFlag", releaseFlag));
	}
	
	public List<HoldParm> findAllByQsrId(int qsrId) {
		return findAll(Parameters.with("qsrId", qsrId));
	}	
	
	@Transactional
	public int releaseExpiredHolds(Date stopDate) {
		return executeUpdate(RELEASE_EXPIRED, Parameters.with("stopDate", stopDate));
	}
}

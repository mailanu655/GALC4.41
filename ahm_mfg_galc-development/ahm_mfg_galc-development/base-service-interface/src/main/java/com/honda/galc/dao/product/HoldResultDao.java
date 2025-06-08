/**
 * 
 */
package com.honda.galc.dao.product;

import java.sql.Timestamp;
import java.util.List;

import com.honda.galc.entity.enumtype.HoldResultType;
import com.honda.galc.entity.product.HoldResult;
import com.honda.galc.entity.product.HoldResultId;
import com.honda.galc.service.IDaoService;
import com.honda.galc.service.common.ProductHoldService;

/**
 * 
 * <h3>HoldResultDao Class description</h3>
 * <p> HoldResultDao description </p>
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
 * Jun 3, 2012
 *
 *
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public interface HoldResultDao extends IDaoService<HoldResult, HoldResultId> {
	
	public List<HoldResult> findAllByProductId(String productId);
	List<HoldResult> findAllByProductAndReleaseFlag(String productId,boolean releaseFlag);
	List<HoldResult> findAllByProductAndReleaseFlag(String productId,boolean releaseFlag, HoldResultType holdType);
	List<HoldResult> findAllByQsrId(int qsrId);
	int releaseProductHolds(String associateId, String associateName, String pager, String phone, String reason, String productId);	
	public List<Object[]> getHoldReasons();
	public List<Object[]> getReleaseReasons();
	List<HoldResult>findAllByHoldReason(String holdReason);
	
	List<HoldResult>findAllHoldByProductIdAndHoldReason(String productId,String holdReason);
	List<HoldResult>findAllByProductHoldTypeAndHoldReason(String productId, HoldResultType holdType,String holdReason);
	
	public List<HoldResult> findAllBySequenceRange(String startSeq,String endSeq, String holdAccessType);
	public List<HoldResult> findAllByDateRange(Timestamp startTime,Timestamp endTime, String holdAccessType);
	public long unreleasedCountByQsr(int qsrId);
	public List<HoldResult> findAllByRange(String productId,Timestamp startTime,Timestamp endTime,String startSeq,String endSeq, String holdAccessType,int qsrId);
	public long findCountByRange(String productId,Timestamp startTime,Timestamp endTime,String startSeq,String endSeq, String holdAccessType,int qsrId);
	public List<HoldResult> findAllByProductIdAndQsr(String productId, int qsrId);
	public long countByQsr(int qsrId);
	public List<HoldResult> findAllByProductionLotRange(String start,String end);
	public List<Integer> findQsrByProductionLotRange(String start,String end);
}

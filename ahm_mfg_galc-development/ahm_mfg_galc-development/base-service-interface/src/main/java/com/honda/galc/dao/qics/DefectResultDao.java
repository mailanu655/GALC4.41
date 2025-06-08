package com.honda.galc.dao.qics;

import java.sql.Timestamp;
import java.sql.Date;
import java.util.List;

import com.honda.galc.dao.qics.vo.AddNewDefectRepairResultRequest;
import com.honda.galc.dao.qics.vo.AddNewDefectResultRequest;
import com.honda.galc.data.ProductType;
import com.honda.galc.dto.DeptDefectResult;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.entity.product.ExceptionalOut;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qics.DefectResult;
import com.honda.galc.entity.qics.DefectResultId;
import com.honda.galc.entity.qics.StationResult;
import com.honda.galc.service.IDaoService;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>DefectResultDao Class description</h3>
 * <p> DefectResultDao description </p>
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
 * Apr 1, 2011
 *
 *
 */
public interface DefectResultDao extends IDaoService<DefectResult, DefectResultId> {

	public List<DefectResult> findAllByProductId(String productId);
	
	
	/**
	 * save defect results, update product status and station result, and create exceptional out record if the product is scrapped
	 * if schedule is null, don't update station result 
	 * @param product
	 * @param defectStatus
	 * @param processPointId
	 * @param defectResults
	 * @param schedule
	 * @param exceptionalOut
	 * @return
	 */
	public StationResult saveAllDefectResults(BaseProduct product,DefectStatus defectStatus,String processPointId,
			List<DefectResult> defectResults,DailyDepartmentSchedule schedule,ExceptionalOut exceptionalOut,
			boolean isReplicateDefectRepairResult, boolean isCreateRepairedDefectTo222);	
	
	public List<DefectResult> findAllDefectsByProductId(String productId);
	public int getLegacyRowCountByProductId(String productId);
	
	public List<DefectResult> findDepartmentDefects(String department, String processPointId, int vinTotal, int dayTotal);
		
	public  int updateGdpDefects(Parameters params);
	
	public int  updateGdpDefectsVQWriteUpDept(Parameters params);
	
	public  int updateGdpDefectsByCurrentProcess(String productId);
	
	public void saveInlineDefects(ProductType productType, List<DefectResult> defectResults);	
	
	public void addNewDefectResult( AddNewDefectResultRequest request );
	
	public void addNewDefectRepairResult( AddNewDefectRepairResultRequest request );
	
	public void setTopDefectsByDepartment(String department,
			String processPointId, int vinTotal, int dayTotal, int howMany);
	
    public List<DefectResult> findAllOutstandingByProductId(String productId);
    public List<DefectResult> findAllDefectDetails(List<DefectResult> defectResults);
	
    public List<Object[]> getAllCoreMQ(Timestamp startTs, Timestamp endTs);  	//query since lastUpdate

    
    /**
	 * find rejection counts of all shifts of all departments
	 * @return
	 */
	public List<DeptDefectResult> findAllRejectionCounts(Date productionDate, String department);
	
	public List<Object[]> findAllCoreMQDefectDataByTimestamp(Timestamp startTs, Timestamp endTs);
	public DefectResult saveDefectResultForHeadlessService(DefectResult defectResult); 
	public List<DefectResult> findAllByPartDefectCombAndProductId(QiDefectResult qiDefectResult ,String productId); 
	
	public void deleteByQiDefectResultId(long qiDefectResultId);
	
	public DefectResult findByQiRepairId(long qiRepairId);
	
	public DefectResult findByQiDefectId(long qiDefectId);
	
	public void updateByQiDefectResultId(long qiDefectResultId, int defectStatus, java.util.Date repairTimestamp, String repairAssociateNo);
	
	public void updateResponsibilityByQiDefectResultId(long qiDefectResultId, String respDept, String respLine, String respZone);


	StationResult saveLotControlResults(BaseProduct product, DefectStatus defectStatus, String processPointId, List<DefectResult> defectResults, DailyDepartmentSchedule schedule, ExceptionalOut exceptionalOut,
			boolean isReplicateDefectRepairResult, boolean isCreateRepairedDefectTo222);
}

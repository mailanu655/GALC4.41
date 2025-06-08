package com.honda.galc.service.pdda;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.honda.galc.constant.RevisionType;
import com.honda.galc.dto.MCPendingProcessDto;
import com.honda.galc.dto.PddaProcess;
import com.honda.galc.dto.PddaSafetyImage;
import com.honda.galc.dto.PddaUnitImage;
import com.honda.galc.dto.UnitOfOperation;
import com.honda.galc.dto.UnitOfOperationDetails;
import com.honda.galc.entity.conf.MCRevision;
import com.honda.galc.entity.pdda.ChangeForm;
import com.honda.galc.entity.pdda.ChangeFormUnit;
import com.honda.galc.service.IService;

/**
 * 
 * <h3>GenericPddaDaoService Class description</h3>
 * <p> GenericPddaDaoService description </p>
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
 * @author Fredrick Yessaian<br>
 * Mar 6, 2014
 *
 *
 */

public interface GenericPddaDaoService extends IService {

	public List<UnitOfOperation> getAllOperationsForProcessPoint(String productId ,String processPoint);
	
	public UnitOfOperationDetails getUnitOfOperationDetails(int maintenanceId);
	
	public List<PddaSafetyImage> getSafetyImages(int maintenanceId) throws SQLException;
	
	public List<PddaUnitImage> getUnitImages(int maintenanceId) throws SQLException;
	
	public List<PddaUnitImage> getUnitCCPImages(String productId ,String processPoint) throws SQLException;
	
	public List<UnitOfOperation> getUnitSafetyImageList(String productId ,String processPoint) throws SQLException;
	
	public Integer getSequenceNumberForOperation(String pp);
	
	public MCRevision createRevisionForChangeForms(List<Integer> changeFormList, String userId, String description, String revType);
	
	public void addMCRecord(ChangeFormUnit cfu, long revId, Map<String, String> modelYrCodeMap, String userId);
	
	public void createMfgCtrlRecords(long revId, Map<String, String> yearCodeMap, String userId);
	
	public List<PddaProcess> getProcessDetailForChangeForm(Integer changeFormId);
	
	public Map<String, String> getModelYearCodeMap(String productType);
	
	public void approveRevision(long revId, String userId);
	
	public RevisionType getRevisionType(int changeFormId);

	public ChangeForm getChangeFormForPddaPlatform(long pddaPlatformId);
	
	public List<MCPendingProcessDto> getPendingProcesses();
	
	public List<MCPendingProcessDto> getPendingProcesses(long revId);
	
	public void performApproval(long revId, String userId);
	
	public void deprecateOperations(int platformId, String userId, long deprRevId);
	
	public void addPlatformChg(long revId, String userId);

	public Map<String, String> getYearDescriptionCodeMap(String productType);

	public void addMCPddaPlatformRecord(String asmProcNo, Long revId, String userId);

	public MCRevision createRevisionForOneClickApproval(List<Integer> list2, String userId, String string,
			String revType);
	
	public void createMfgCtrlRecordsForOneClick(long revId, Map<String, String> yearCodeMap, String userId);
	
}

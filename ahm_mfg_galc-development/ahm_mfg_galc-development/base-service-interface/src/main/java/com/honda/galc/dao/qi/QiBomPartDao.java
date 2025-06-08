package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.dto.qi.QiBomPartDto;
import com.honda.galc.entity.qi.QiBomPart;
import com.honda.galc.entity.qi.QiBomPartId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>QiBomPartDao Class description</h3>
 * <p>
 * QiBomPartDao is used to declare the methods required for the operation on Bom Part table on screen
 * </p>
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
 *        MAY 06, 2016
 * 
 */
public interface QiBomPartDao extends IDaoService<QiBomPart, QiBomPartId> {
	public List<QiBomPartDto> findBomPartsByFilter(String filterValue, String productKind);
	public List<QiBomPartDto> findAssociatedBomPartsByFilter(String filterValue, String productKind);
	public List<QiBomPartDto> findNotAssociatedBomPartsByFilter(String filterValue, String productKind);
	public void mergeBatch(String values);
	public void insertBatch(String values);
}

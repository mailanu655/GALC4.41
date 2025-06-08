package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.entity.qi.QiRepairResultImage;
import com.honda.galc.entity.qi.QiRepairResultImageId;
import com.honda.galc.service.IDaoService;

/**
 * <h3>Class description</h3>
 * 
 * <h4>Description</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Dylan Yang</TD>
 * <TD>October 02, 2020</TD>
 * <TD>1.0</TD>
 * <TD>DY 20201002</TD>
 * <TD>Initial Realease</TD>
 * </TR>
 * 
 * </TABLE>
 * 
 * @author Dylan Yang<br>
 *         October 2, 2020
 */

public interface QiRepairResultImageDao extends IDaoService<QiRepairResultImage, QiRepairResultImageId> {

	List<QiRepairResultImage> findAllByRepairId(long repairId);

}

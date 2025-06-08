package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.dto.qi.QiDefectResultImageDto;
import com.honda.galc.entity.qi.QiDefectResultImage;
import com.honda.galc.entity.qi.QiDefectResultImageId;
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

public interface QiDefectResultImageDao extends IDaoService<QiDefectResultImage, QiDefectResultImageId> {

	List<QiDefectResultImage> findAllByDefectResultId(long defectResultId);

	List<QiDefectResultImageDto> findAllByProductId(String productId);

	List<QiDefectResultImageDto> findAllByApplicationId(String applicationId);

	List<QiDefectResultImageDto> findAllByPartDefectCombination(String searchString);
}

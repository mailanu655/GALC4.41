package com.honda.galc.dao.product.bearing;

import java.util.List;

import com.honda.galc.entity.bearing.BearingMatrixCell;
import com.honda.galc.entity.bearing.BearingMatrixCellId;
import com.honda.galc.service.IDaoService;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>BearingMatrixCellDao</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */
public interface BearingMatrixCellDao extends IDaoService<BearingMatrixCell, BearingMatrixCellId> {

	public List<BearingMatrixCell> findAll(String modelYearCode, String modelCode, String bearingType);

	public List<BearingMatrixCell> findAllByYearModelType(String modelYearCode, String modelCode, String modelType, String bearingType);
	
	public List<BearingMatrixCell> findAllByYearModelTypePosition(String modelYearCode, String modelCode, String modelType, String journalPosition, String bearingType);

	public List<BearingMatrixCell> findAllByBearingNumber(String modelYearCode, String modelCode, String bearingNumber);
}

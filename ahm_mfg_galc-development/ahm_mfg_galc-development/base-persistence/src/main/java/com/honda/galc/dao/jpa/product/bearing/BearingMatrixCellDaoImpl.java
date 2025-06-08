package com.honda.galc.dao.jpa.product.bearing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.bearing.BearingMatrixCellDao;
import com.honda.galc.entity.bearing.BearingMatrixCell;
import com.honda.galc.entity.bearing.BearingMatrixCellId;
import com.honda.galc.service.Parameters;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>BearingMatrixCellDaoImpl</code> is ... .
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
public class BearingMatrixCellDaoImpl extends BaseDaoImpl<BearingMatrixCell, BearingMatrixCellId> implements BearingMatrixCellDao {

	public List<BearingMatrixCell> findAll(String modelYearCode, String modelCode, String bearingType) {
		Parameters params = Parameters.with("id.modelYearCode", modelYearCode).put("id.modelCode", modelCode).put("id.bearingType", bearingType);
		return findAll(params);
	}
	
	public List<BearingMatrixCell> findAllByYearModelType(String modelYearCode, String modelCode, String modelType, String bearingType){
		Parameters params = Parameters.with("id.modelYearCode", modelYearCode).put("id.modelCode", modelCode).put("id.modelTypeCode", modelType).put("id.bearingType", bearingType);
		return findAll(params);
	}
	
	public List<BearingMatrixCell> findAllByYearModelTypePosition(
			String modelYearCode, String modelCode, String modelType,
			String journalPosition, String bearingType) {
		Parameters params = Parameters.with("id.modelYearCode", modelYearCode)
		.put("id.modelCode", modelCode).put("id.modelTypeCode", modelType)
		.put("id.journalPosition", journalPosition).put("id.bearingType", bearingType);
		return findAll(params);
	}

	public List<BearingMatrixCell> findAllByBearingNumber(String modelYearCode, String modelCode, String bearingNumber) {
		Parameters params = Parameters.with("id.modelYearCode", modelYearCode).put("id.modelCode", modelCode).put("upperBearing", bearingNumber);
		List<BearingMatrixCell> upper = findAll(params); 
		params = Parameters.with("id.modelYearCode", modelYearCode).put("id.modelCode", modelCode).put("lowerBearing", bearingNumber);
		List<BearingMatrixCell> lower = findAll(params);
		Set<BearingMatrixCell> all = new HashSet<BearingMatrixCell>();
		if (upper != null) {
			all.addAll(upper);
		}
		if (lower != null) {
			all.addAll(lower);
		}
		return new ArrayList<BearingMatrixCell>(all);
	}

}

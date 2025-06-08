package com.honda.galc.dao.jpa.product.bearing;

import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.bearing.BearingPartDao;
import com.honda.galc.entity.bearing.BearingPart;
import com.honda.galc.service.Parameters;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>BearingPartDaoImpl</code> is ... .
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
public class BearingPartDaoImpl extends BaseDaoImpl<BearingPart, String> implements BearingPartDao {

	private static final String SELECT_BY_YEAR_MODEL = "select bp from BearingPart bp, BearingPartByYearModel bpym where bpym.id.bearingSerialNumber = bp.id and bpym.id.modelYearCode = :modelYearCode and bpym.id.modelCode = :modelCode";
	private static final String SELECT_ALL_IDS_COLORS = "select b.id, b.color from BearingPart b";

	public List<BearingPart> findAllByYearModel(String modelYearCode, String modelCode) {
		Parameters params = Parameters.with("modelYearCode", modelYearCode).put("modelCode", modelCode);
		return findAllByQuery(SELECT_BY_YEAR_MODEL, params);
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getAllIdAndColorRecords() {
		List<Object[]> retList =  findResultListByQuery(SELECT_ALL_IDS_COLORS, null);
		return retList;
	}
}

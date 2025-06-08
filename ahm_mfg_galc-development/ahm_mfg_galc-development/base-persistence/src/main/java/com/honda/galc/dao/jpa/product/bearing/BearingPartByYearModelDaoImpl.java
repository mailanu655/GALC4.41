package com.honda.galc.dao.jpa.product.bearing;

import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.bearing.BearingPartByYearModelDao;
import com.honda.galc.entity.bearing.BearingPartByYearModel;
import com.honda.galc.entity.bearing.BearingPartByYearModelId;
import com.honda.galc.service.Parameters;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>BearingPartByYearModelDaoImpl</code> is ... .
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
 * @created May 13, 2013
 */
public class BearingPartByYearModelDaoImpl extends BaseDaoImpl<BearingPartByYearModel, BearingPartByYearModelId> implements BearingPartByYearModelDao {

	private static final String FIND_DUPLICATED_BEARING_PARTS = 
			"select bym from BearingPartByYearModel bym " 
			+ ",BearingPart bp " 
			+ ",BearingPartByYearModel bym2 " 
			+ "where " 
			+ "bp.id = bym.id.bearingSerialNumber "
			+ "and bym2.id.modelYearCode = bym.id.modelYearCode and bym2.id.modelCode = bym.id.modelCode and bym2.id.bearingSerialNumber = :bearingSerialNumber " 
			+ "and bp.color = :color " 
			+ "and bp.type = :type "
			+ "and bp.id <> :bearingSerialNumber";

	public long selectCountByBearingNumber(String bearingSerialNumber) {
		return count(Parameters.with("id.bearingSerialNumber", bearingSerialNumber));
	}

	public List<BearingPartByYearModel> findPotentialDuplicates(String bearingPartType, String bearingColor, String bearingNumber) {
		Parameters params = Parameters.with("type", bearingPartType).put("color", bearingColor).put("bearingSerialNumber", bearingNumber);
		return findAllByQuery(FIND_DUPLICATED_BEARING_PARTS, params);
	}
}

package com.honda.galc.dao.jpa.qi;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiStationPreviousDefectDao;
import com.honda.galc.entity.qi.QiStationPreviousDefect;
import com.honda.galc.entity.qi.QiStationPreviousDefectId;
import com.honda.galc.service.Parameters;
/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>QiStationPreviousDefectDaoImpl</code> is an implementation class for QiStationPreviousDefectDao interface.
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
 * <TD>L&T Infotech</TD>
 * <TD>1.0.1</TD>
 * <TD>(none)</TD>
 * <TD>Release 1</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.1
 * @author L&T Infotech
 */
public class QiStationPreviousDefectDaoImpl extends BaseDaoImpl<QiStationPreviousDefect,QiStationPreviousDefectId> implements QiStationPreviousDefectDao {
	/*
	 * this method is used to fetch all QiStationPreviousDefect by processPoint
	 */
	public List<QiStationPreviousDefect> findAllByProcessPoint(String processPointId) {
		return findAll(Parameters.with("id.processPointId",processPointId));
	}
	/*
	 * this method is used to fetch a count of QiStationPreviousDefect by processPoint
	 */
	public long countByProcessPoint(String processPointId) {
		return count(Parameters.with("id.processPointId",processPointId));
	}

	/**
	 * This method is used to delete PreviousDefect by process point
	 */
	@Transactional
	public int deleteByProcessPoint(String processPointId) {
		return delete(Parameters.with("id.processPointId", processPointId));
	}
	
}

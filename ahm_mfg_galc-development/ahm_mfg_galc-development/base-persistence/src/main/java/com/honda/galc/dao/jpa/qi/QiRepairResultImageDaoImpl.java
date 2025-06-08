package com.honda.galc.dao.jpa.qi;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiRepairResultImageDao;
import com.honda.galc.entity.qi.QiRepairResultImage;
import com.honda.galc.entity.qi.QiRepairResultImageId;
import com.honda.galc.service.Parameters;

/**
 * 
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


public class QiRepairResultImageDaoImpl extends BaseDaoImpl<QiRepairResultImage, QiRepairResultImageId> implements QiRepairResultImageDao {

	private static final String FIND_ALL_BY_REPAIR_ID = "SELECT * FROM GALADM.QI_REPAIR_RESULT_IMAGE_TBX WHERE REPAIR_ID = ?1";
	
	@Override
	public List<QiRepairResultImage> findAllByRepairId(long repairId) {
		Parameters params = Parameters.with("1", repairId);
		return findAllByNativeQuery(FIND_ALL_BY_REPAIR_ID, params);
	}

	@Override
	@Transactional
	public QiRepairResultImage save(QiRepairResultImage repairResultImage) {
		repairResultImage.setActualTimestamp(getDatabaseTimeStamp());
		return (QiRepairResultImage) super.save(repairResultImage);
	}

	@Override
	@Transactional
	public List<QiRepairResultImage> saveAll(List<QiRepairResultImage> entities) {
		Timestamp ts = getDatabaseTimeStamp();
		for(QiRepairResultImage entity : entities) {
			entity.setActualTimestamp(ts);
		}
		return super.saveAll(entities);
	}

	@Override
	@Transactional
	public QiRepairResultImage insert(QiRepairResultImage repairResultImage) {
		repairResultImage.setActualTimestamp(getDatabaseTimeStamp());
		return (QiRepairResultImage) super.insert(repairResultImage);
	}

	@Override
	@Transactional
	public void insertAll(List<QiRepairResultImage> entities) {
		Timestamp ts = getDatabaseTimeStamp();
		for(QiRepairResultImage entity : entities) {
			entity.setActualTimestamp(ts);
		}
		super.insertAll(entities);
	}
}

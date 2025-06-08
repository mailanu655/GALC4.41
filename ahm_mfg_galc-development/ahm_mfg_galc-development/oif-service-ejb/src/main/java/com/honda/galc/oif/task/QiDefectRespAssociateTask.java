package com.honda.galc.oif.task;

import java.util.List;

import com.honda.galc.dao.mdrs.QiDefectRespAssociateDao;
import com.honda.galc.dao.qi.QiDefectResultDao;
import com.honda.galc.entity.mdrs.QiDefectRespAssociate;
import com.honda.galc.qi.constant.QiConstant;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;

/**
 * <QiDefectRespAssociateTask.java</h3>
 * <h4>Description</h4>
 * <p>
 * This task will assign responsible associate to defects.
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
 * <TR>
 * <TD>vcc01419</TD>
 * <TD>May 15, 2017</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD>
 * </TR>
 *
 * </TABLE>
 * 
 * @version 0.1
 * @author vcc01419
 * @created May 15, 2017
 */
public class QiDefectRespAssociateTask extends OifAbstractTask implements IEventTaskExecutable {

	public QiDefectRespAssociateTask(String name) {
		super(name);
	}

	@Override
	public void execute(Object[] args) {

		try {
			logger.info("Started processing Defect Responsible Associate Interface");

			assignRespAssociateToDefects();

			logger.info("Finished processing Defect Responsible Associate Interface");
		} catch (Exception e) {
			logger.emergency(e, "Unexpected Exception Occurred  while running the defect responsible associate task.");
		}
	}

	private void assignRespAssociateToDefects() {
		QiDefectRespAssociateDao defectRespAssociateDao = ServiceFactory.getDao(QiDefectRespAssociateDao.class);
		QiDefectResultDao defectResultDao = ServiceFactory.getDao(QiDefectResultDao.class);

		List<QiDefectRespAssociate> defectRespAssociateList = defectRespAssociateDao.findAllUnProcessedDefects();

		if (defectRespAssociateList != null && !defectRespAssociateList.isEmpty()) {
			for (QiDefectRespAssociate qiDefectRespAssociate : defectRespAssociateList) {
				int updatedResult = defectResultDao.updateResponsibleAssociateByDefectId(qiDefectRespAssociate.getQfsDefectNo(), qiDefectRespAssociate.getAssociateId(), QiConstant.OIF_TASK);
				if (updatedResult > 0) {
					// update staging table
					qiDefectRespAssociate.setStatus((short) 1);
					defectRespAssociateDao.update(qiDefectRespAssociate);
				}
			}
		}
	}

}

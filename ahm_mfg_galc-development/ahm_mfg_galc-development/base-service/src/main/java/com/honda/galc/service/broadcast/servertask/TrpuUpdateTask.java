package com.honda.galc.service.broadcast.servertask;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.qi.QiDefectResultDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.service.ServiceFactory;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>TrpuUpdateTask</code> is ... .
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
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Vivek Bettada
 * @created Mar 6, 2020
 */
public class TrpuUpdateTask implements IServerTask {

	public DataContainer execute(DataContainer dc) {
		String productId = dc.getString(DataContainerTag.PRODUCT_ID);
		String userId = dc.getString(DataContainerTag.USER_ID);
		String processPointId = dc.getString(DataContainerTag.PROCESS_POINT_ID);

		if (StringUtils.isBlank(productId)) {
			throw new TaskException("ProductId can not be blank.");
		}
		int count = updateTrpuDefects(productId, userId);
		Logger.getLogger().info(" TRPU Defects updated: " + count + " for productId: " + productId + " at processPoint: " + processPointId);
		return dc;
	}
	
	protected int updateTrpuDefects(String productId, String userId) {
		QiDefectResultDao dao = ServiceFactory.getDao(QiDefectResultDao.class);
		int count = dao.updateTrpuDefects(productId, userId);
		return count;
	}
}

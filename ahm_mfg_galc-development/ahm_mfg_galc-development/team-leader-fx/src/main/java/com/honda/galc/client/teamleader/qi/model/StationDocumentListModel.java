package com.honda.galc.client.teamleader.qi.model;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.List;

import com.honda.galc.dao.qi.QiDocumentDao;
import com.honda.galc.entity.qi.QiDocument;

/**
 * 
 * <h3>StationDocumentListModel Class description</h3>
 * <p>
 * StationDocumentListModel description
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
 *
 * </TABLE>
 * 
 * @author Justin Jiang<br>
 *         February 20, 2020
 *
 */

public class StationDocumentListModel extends QiModel {

	public StationDocumentListModel() {
		super();

	}

	public List<QiDocument> findAssignedDocumentByTerminal(String terminal) {
		return getDao(QiDocumentDao.class).findAssignedDocumentByTerminal(terminal);
	}
}

package com.honda.galc.dao.jpa.qi;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiStationDocumentDao;
import com.honda.galc.entity.qi.QiStationDocument;
import com.honda.galc.entity.qi.QiStationDocumentId;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>QiStationDocumentDaoImpl Class description</h3>
 * <p>
 * QiStationDocumentDaoImpl description
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

public class QiStationDocumentDaoImpl extends BaseDaoImpl<QiStationDocument, QiStationDocumentId>
		implements QiStationDocumentDao {

	@Transactional
	public void removeDocumentFromStations(int documentId) {
		delete(Parameters.with("id.documentId", documentId));
	}

	public boolean isDocumentAssigned(int documentId) {
		List<QiStationDocument> stationDocuments = findAll(Parameters.with("id.documentId", documentId));
		if (stationDocuments.size() > 0) {
			return true;
		} else {
			return false;
		}
	}
}

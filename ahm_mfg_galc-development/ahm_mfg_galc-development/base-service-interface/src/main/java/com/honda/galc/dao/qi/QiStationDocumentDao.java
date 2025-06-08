package com.honda.galc.dao.qi;

import com.honda.galc.entity.qi.QiStationDocument;
import com.honda.galc.entity.qi.QiStationDocumentId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>QiStationDocumentDao Class description</h3>
 * <p>
 * QiStationDocumentDao description
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

public interface QiStationDocumentDao extends IDaoService<QiStationDocument, QiStationDocumentId> {

	public void removeDocumentFromStations(int documentId);

	public boolean isDocumentAssigned(int documentId);
}

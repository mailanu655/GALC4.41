package com.honda.galc.dao.qi;

import java.util.List;

import com.honda.galc.entity.qi.QiDocument;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>QiDocumentDao Class description</h3>
 * <p>
 * QiDocumentDao description
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

public interface QiDocumentDao extends IDaoService<QiDocument, String> {
	public boolean isExisting(String name, String link);

	public int deleteById(int docuemntId);

	public List<QiDocument> findByFilter(String filter);

	public List<QiDocument> findAssignedDocumentByStation(String station);

	public List<QiDocument> findAssignedDocumentByTerminal(String terminal);
}

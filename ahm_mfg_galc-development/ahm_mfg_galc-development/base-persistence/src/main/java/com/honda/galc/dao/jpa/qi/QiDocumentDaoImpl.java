package com.honda.galc.dao.jpa.qi;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.qi.QiDocumentDao;
import com.honda.galc.entity.qi.QiDocument;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>QiDocumentDaoImpl Class description</h3>
 * <p>
 * QiDocumentDaoImpl description
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

public class QiDocumentDaoImpl extends BaseDaoImpl<QiDocument, String> implements QiDocumentDao {

	private static final String FIND_DOCUMENT_BY_FILTER = "select e from QiDocument e where "
			+ "(e.documentName like :searchString or e.documentLink like :searchString or "
			+ "e.description like :searchString) order by e.documentName";

	private static final String FIND_ASSIGNED_DOCUMENT_BY_STATION = "select a.* from GALADM.QI_DOCUMENT_TBX a, "
			+ "GALADM.QI_STATION_DOCUMENT_TBX b where a.DOCUMENT_ID=b.DOCUMENT_ID and b.PROCESS_POINT_ID=?1";

	private static final String FIND_DOCUMENT_BY_TERMINAL = "select a.* from GALADM.QI_DOCUMENT_TBX a, "
			+ "GALADM.QI_STATION_DOCUMENT_TBX b, GALADM.GAL242TBX c where c.HOST_NAME = ?1 and "
			+ "c.APPLICATION_ID = b.PROCESS_POINT_ID and b.DOCUMENT_ID = a.DOCUMENT_ID";

	/**
	 * This method is to find all QiDocument by filter
	 * 
	 * @param filterData:
	 *            Filter data for QiDocument.
	 * @return List of QiDocument.
	 */
	public List<QiDocument> findByFilter(String filterData) {
		Parameters params = Parameters.with("searchString", "%" + filterData + "%");
		return findAllByQuery(FIND_DOCUMENT_BY_FILTER, params);
	}

	/**
	 * This method is to find all assigned QiDocument based on QICS station(Process
	 * Point).
	 * 
	 * @param station.
	 * @return List of QiDocument.
	 **/
	public List<QiDocument> findAssignedDocumentByStation(String station) {
		Parameters params = Parameters.with("1", station);
		return findAllByNativeQuery(FIND_ASSIGNED_DOCUMENT_BY_STATION, params);
	}

	@Transactional
	public int deleteById(int docuemntId) {
		return delete(Parameters.with("documentId", docuemntId));
	}

	public List<QiDocument> findAssignedDocumentByTerminal(String terminal) {
		Parameters params = Parameters.with("1", terminal);
		return findAllByNativeQuery(FIND_DOCUMENT_BY_TERMINAL, params);
	}

	public boolean isExisting(String name, String link) {
		Parameters params = Parameters.with("documentName", name).put("documentLink", link);
		List<QiDocument> documents = findAll(params);
		if (documents != null && documents.size() > 0) {
			return true;
		} else {
			return false;
		}
	}
}

package com.honda.galc.client.teamleader.qi.model;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.List;

import com.honda.galc.dao.qi.QiDocumentDao;
import com.honda.galc.dao.qi.QiStationDocumentDao;
import com.honda.galc.entity.qi.QiDocument;

/**
 * 
 * <h3>DocumentMaintenanceModel Class description</h3>
 * <p>
 * DocumentMaintenanceModel description
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

public class DocumentMaintenanceModel extends QiModel {
	
	public QiDocument createDocument(QiDocument document) {
		return (QiDocument) getDao(QiDocumentDao.class).insert(document);
	}
	
	public QiDocument updateDocument(QiDocument qiDocument) {
		return (QiDocument) getDao(QiDocumentDao.class).save(qiDocument);
	}

	public void deleteDocument(int documentId) {
		getDao(QiStationDocumentDao.class).removeDocumentFromStations(documentId);
		getDao(QiDocumentDao.class).deleteById(documentId);
	}
	
	public boolean isDocumentAssigned(int documentId) {
		return getDao(QiStationDocumentDao.class).isDocumentAssigned(documentId);
	}
	
	public List<QiDocument> findDocumentsByFilter(String filterValue){
		return getDao(QiDocumentDao.class).findByFilter(filterValue);
	}
	
	public boolean isDocumentExisting(String name, String link) {
		return getDao(QiDocumentDao.class).isExisting(name, link);
	}
}

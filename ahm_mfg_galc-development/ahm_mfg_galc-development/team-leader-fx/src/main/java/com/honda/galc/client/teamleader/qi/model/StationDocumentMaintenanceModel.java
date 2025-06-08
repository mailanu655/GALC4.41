package com.honda.galc.client.teamleader.qi.model;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.List;

import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.dao.conf.PlantDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.qi.QiDocumentDao;
import com.honda.galc.dao.qi.QiStationDocumentDao;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Plant;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.qi.QiDocument;
import com.honda.galc.entity.qi.QiStationDocument;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>StationDocumentMaintenanceModel Class description</h3>
 * <p>
 * StationDocumentMaintenanceModel description
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

public class StationDocumentMaintenanceModel extends QiModel {

	public StationDocumentMaintenanceModel() {
		super();
	}

	/**
	 * This method is used to find all Division based on Site and Plant.
	 * 
	 * @param siteName,plantName.
	 * @return List of Division.
	 **/
	public List<Division> findById(String siteName, String plantName) {
		return getDao(DivisionDao.class).findById(siteName, plantName);
	}

	/**
	 * This method is used to find all Qics Station(Process Point) based on
	 * Division.
	 * 
	 * @param divisionId.
	 * @return List of Qics station(Process Point).
	 **/
	public List<ProcessPoint> findStationByApplicationComponentDivision(String divisionId) {
		return getDao(ProcessPointDao.class).findAllByApplicationComponentDivision(divisionId);
	}

	public List<QiDocument> findAllDocument() {
		return getDao(QiDocumentDao.class).findAll();
	}

	public List<QiDocument> findAllDocumentByStation(String qicsStation) {
		return getDao(QiDocumentDao.class).findAssignedDocumentByStation(qicsStation);
	}

	/**
	 * This method is used to find all plant by site name
	 * 
	 * @param siteName
	 * @return list of plant
	 */
	public List<Plant> findAllBySite(String siteName) {
		return getDao(PlantDao.class).findAllBySite(siteName);
	}

	public List<QiDocument> findDocumentByFilter(String filter) {
		return getDao(QiDocumentDao.class).findByFilter(filter);
	}

	/**
	 * This method is used to get site name
	 * 
	 * @return
	 */
	public String findSiteName() {
		return PropertyService.getPropertyBean(SystemPropertyBean.class).getSiteName();
	}

	public void createQiStationDocument(QiStationDocument qiStationDocument) {
		getDao(QiStationDocumentDao.class).insert(qiStationDocument);
	}

	/**
	 * This method is used to remove data based on qiStationDocument entity list
	 * 
	 * @param qiStationDocumentList
	 */
	public void deleteQiStationDocuments(List<QiStationDocument> qiStationDocumentList) {
		getDao(QiStationDocumentDao.class).removeAll(qiStationDocumentList);

	}
}

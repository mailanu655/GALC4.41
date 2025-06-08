package com.honda.galc.client.teamleader.qi.model;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.client.teamleader.qi.model.QiModel;
import com.honda.galc.dao.qi.QiBomPartDao;
import com.honda.galc.dao.qi.QiBomQicsPartMappingDao;
import com.honda.galc.dao.qi.QiInspectionPartDao;
import com.honda.galc.dto.qi.QiBomPartDto;
import com.honda.galc.entity.qi.QiBomQicsPartMapping;
import com.honda.galc.entity.qi.QiInspectionPart;
/**
 * 
 * <h3>BomQicsPartAssociationMaintenanceModel Class description</h3>
 * <p>
 * BomQicsPartAssociationMaintenanceModel is used to populate data from BomPart And part tables 
 * And create or delete association between them
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
 * @author LnTInfotech<br>
 *        MAY 06, 2016
 * 
 */
public class BomQicsPartAssociationMaintenanceModel extends QiModel {

	/**
	 * This method is used to filter Bom Part
	 * @param filterString
	 * @return
	 */
	public List<QiBomPartDto> getBomPartsByFilter(String filterString) {
		return getDao(QiBomPartDao.class).findBomPartsByFilter(filterString, getProductKind());
	}

	/**
	 * This method is used to filter Qics Part
	 * @param filterString
	 * @return
	 */
	public List<QiInspectionPart> getActivePartNameByFilter(String filterString) {
		return getDao(QiInspectionPartDao.class).findActivePartNameByFilter(filterString, getProductKind());
	}

	/**
	 * This method is used to filter Associated Bom Part
	 * @param filterString
	 * @return
	 */
	public List<QiBomPartDto> getAssociatedBomPartsByFilter(String filterString) {
		return getDao(QiBomPartDao.class).findAssociatedBomPartsByFilter(filterString, getProductKind());
	}

	/**
	 * This method is used to filter Not Associated Bom Part
	 * @param filterString
	 * @return
	 */
	public List<QiBomPartDto> getNotAssociatedBomPartsByFilter(String filterString) {
		return getDao(QiBomPartDao.class).findNotAssociatedBomPartsByFilter(filterString, getProductKind());
	}

	/**
	 * This method is used to create a Bom Part Association
	 * @param qiBomQicsPartMappingList
	 */
	public void createBomPartAssociation(List<QiBomQicsPartMapping> qiBomQicsPartMappingList) {
		getDao(QiBomQicsPartMappingDao.class).saveAll(qiBomQicsPartMappingList);
	}

	/**
	 * This method is used to delete the Association
	 * @param associatedBomPartList
	 */
	public void deleteBomPartAssociation(List<QiBomQicsPartMapping> associatedBomPartList) {
		getDao(QiBomQicsPartMappingDao.class).removeAll(associatedBomPartList);
	}


	/**
	 * This method is used to get the Bom Part mapping List
	 * @param associatedBomPartList
	 * @param selectedQicsPartsList 
	 * @return
	 */
	public List<QiBomPartDto> getNewBomPartMappingList(List<QiBomPartDto> associatedBomPartList, QiInspectionPart selectedQicsPart) {
		List<QiBomPartDto> tempAssociatedBomPartList = new ArrayList<QiBomPartDto>();
		
		 if(!associatedBomPartList.isEmpty() && selectedQicsPart == null){
			 for (QiBomPartDto associateedBompartDto :associatedBomPartList){
				associateedBompartDto.setInspectionPart(null);
				tempAssociatedBomPartList.add(associateedBompartDto);
			}

		}else if(associatedBomPartList.isEmpty() && null!=selectedQicsPart){
				QiBomPartDto associateedBompartDto = new QiBomPartDto();
				associateedBompartDto.setMainPartNo(null);
				associateedBompartDto.setInspectionPart(selectedQicsPart.getInspectionPartName());
				tempAssociatedBomPartList.add(associateedBompartDto);
			
		}else if(!associatedBomPartList.isEmpty() && null!=selectedQicsPart){
			for (QiBomPartDto associateedBompartDto : associatedBomPartList) {
					 associateedBompartDto.setInspectionPart(selectedQicsPart.getInspectionPartName());
					 tempAssociatedBomPartList.add(associateedBompartDto);
			}
		}
		
		
		return getDao(QiBomQicsPartMappingDao.class).findNewBomPartMappingList(tempAssociatedBomPartList);
	}

}

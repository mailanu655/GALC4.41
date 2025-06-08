package com.honda.galc.dao.qi;
/*
* @author L&T Infotech
*/
import java.util.List;

import com.honda.galc.entity.qi.QiInspectionPart;
import com.honda.galc.service.IDaoService;
public interface QiInspectionPartDao extends IDaoService<QiInspectionPart, String>{
	
	
	public List<QiInspectionPart> findPartsByFilter(String namedQuery,String productKind);
	public List<QiInspectionPart> findPartsByFilter(String filterData,String status,String productKind);
	/**
	 * This method is used to update the 'status' of the Part and 'updateUser'
	 * @param name
	 * @param active
	 * @param updateUser
	 */
	public void updatePartStatus(String name, short active ,String updateUser);
	/**
	 * This method is used to inactivate the Part in Part Location Combination.
	 * @param partName
	 */
	public void inactivatePart(String partName,String userId);
	/**
	 * This method is used to filter data based on Part Name
	 * @param namedQuery
	 * @return
	 */
	public List<QiInspectionPart> findActivePartNameByFilter(String namedQuery, String productKind);
	/**
	 * This method is used to find active and primary Inspection Parts.
	 * @param productKind
	 * @return
	 */
	public List<QiInspectionPart> findActivePrimaryInspectionPartsByProductKind(String productKind);
	/**
	 * This method is used to find filtered active and primary Inspection Parts.
	 * @param filterData
	 * @param productKind
	 * @return
	 */
	public List<QiInspectionPart> findFilteredActivePrimaryInspectionParts(String filterData, String productKind);
	/**
	 * This method is used to find active and secondary Inspection Parts.
	 * @param productKind
	 * @return
	 */
	public List<QiInspectionPart> findActiveSecondaryInspectionPartsByProductKind(String productKind);
	/**
	 * This method is used to find filtered active and secondary Inspection Parts.
	 * @param filterData
	 * @param productKind
	 * @return
	 */
	public List<QiInspectionPart> findFilteredActiveSecondaryInspectionParts(String filterData, String productKind);
	/**
	 * This method is used to find active Inspection Parts.
	 * @param productKind
	 * @return
	 */
	public List<QiInspectionPart> findActiveInspectionPartsByProductKind(String productKind);
	/**
	 * This method is used to find filtered active Inspection Parts.
	 * @param filterData
	 * @param productKind
	 * @return
	 */
	public List<QiInspectionPart> findFilteredActiveInspectionParts(String filterData, String productKind);
	
	/**
	 * This method is used to update Part
	 * @param inspectionPart
	 * @param oldPartName
	 */
	public void updatePart(QiInspectionPart inspectionPart, String oldPartName);
}

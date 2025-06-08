/**
 * 
 */
package com.honda.galc.client.teamleader.qi.mtcmodelimportexport.dto;


import com.honda.galc.entity.qi.QiEntryModel;
import com.honda.galc.entity.qi.QiEntryScreen;
import com.honda.galc.entity.qi.QiTextEntryMenu;
import com.honda.galc.entity.qi.QiTextEntryMenuId;
import com.honda.galc.entity.qi.QiLocalDefectCombination;
import com.honda.galc.entity.qi.QiEntryScreenDefectCombination;
import com.honda.galc.entity.qi.QiEntryScreenDefectCombinationId;
import com.honda.galc.entity.qi.QiEntryScreenId;

/**
 * @author VCC44349
 *
 */
public class QiMtcModelImportExportConfigObjectFactory {

	//create Xml Dto
	public static QiEntryModelTbxXmlDto QiEntryModelTbxXmlDtoInstance(QiEntryModel entity) {
		QiEntryModelTbxXmlDto dto = new QiEntryModelTbxXmlDto();
		dto.setEntryModel(entity.getId().getEntryModel());
		dto.setIsUsed(entity.getId().getIsUsed());
		dto.setEntryModelDescription(entity.getEntryModelDescription());
		dto.setProductType(entity.getProductType());
		return dto;
	}
	// create entity
	public static QiEntryModel getQiEntryModelEntity(QiEntryModelTbxXmlDto dto) {
		QiEntryModel entity = new QiEntryModel();
		entity.getId().setIsUsed(dto.getIsUsed());
		entity.getId().setEntryModel(dto.getEntryModel());
		entity.setEntryModelDescription(dto.getEntryModelDescription());
		entity.setProductType(dto.getProductType());
		entity.setActive(dto.getActive());
		return entity;
	}
	//create Xml Dto
	public static QiEntryScreenTbxXmlDto QiEntryScreenTbxXmlDtoInstance(QiEntryScreen entity) {
		QiEntryScreenTbxXmlDto dto = new QiEntryScreenTbxXmlDto();
		dto.setEntryScreen(entity.getId().getEntryScreen());
		dto.setEntryModel(entity.getId().getEntryModel());
		dto.setIsUsed(entity.getId().getIsUsed());
		dto.setEntryScreenDescription(entity.getEntryScreenDescription());
		dto.setIsImage(entity.getIsImage());
		dto.setImageName(entity.getImageName());
		dto.setActive(entity.getActiveValue());
		dto.setScreenIsUsed(entity.getScreenIsUsed());
		return dto;
	}
	//create entity
	public static QiEntryScreen getQiEntryScreenEntity(QiEntryScreenTbxXmlDto dto) {
		QiEntryScreen entity = new QiEntryScreen();
		QiEntryScreenId id = new QiEntryScreenId();
		id.setEntryScreen(dto.getEntryScreen());
		id.setEntryModel(dto.getEntryModel());
		id.setIsUsed(dto.getIsUsed());
		entity.setId(id);
		entity.setEntryScreenDescription(dto.getEntryScreenDescription());
		entity.setIsImage(dto.getIsImage());
		entity.setProductType(dto.getProductType());
		entity.setImageName(dto.getImageName());
		entity.setActiveValue(dto.getActive());
		entity.setScreenIsUsed(dto.getScreenIsUsed());
		return entity;
	}
	//create Xml Dto
	public static QiTextEntryMenuTbxXmlDto QiTextEntryMenuTbxXmlDtoInstance(QiTextEntryMenu entity) {
		QiTextEntryMenuTbxXmlDto dto = new QiTextEntryMenuTbxXmlDto();
		dto.setEntryScreen(entity.getId().getEntryScreen());
		dto.setEntryModel(entity.getId().getEntryModel());
		dto.setTextEntryMenu(entity.getId().getTextEntryMenu());
		dto.setIsUsed(entity.getId().getIsUsed());
		dto.setTextEntryMenuDesc(entity.getTextEntryMenuDesc());
		return dto;
	}
	//create entity
	public static QiTextEntryMenu getQiTextEntryMenuTbxXmlDtoEntity(QiTextEntryMenuTbxXmlDto dto) {
		QiTextEntryMenu entity = new QiTextEntryMenu();
		QiTextEntryMenuId id = new QiTextEntryMenuId();
		id.setEntryScreen(dto.getEntryScreen());
		id.setTextEntryMenu(dto.getTextEntryMenu());
		id.setEntryModel(dto.getEntryModel());
		id.setIsUsed(dto.getIsUsed());
		entity.setId(id);
		entity.setTextEntryMenuDesc(dto.getTextEntryMenuDesc());
		return entity;
	}
	//create Xml Dto
	public static QiEntryScreenDefectCombinationTbxXmlDto QiEntryScreenDefectCombinationTbxXmlDtoInstance(QiEntryScreenDefectCombination entity) {
		QiEntryScreenDefectCombinationTbxXmlDto dto = new QiEntryScreenDefectCombinationTbxXmlDto();
		dto.setEntryScreen(entity.getId().getEntryScreen());
		dto.setEntryModel(entity.getId().getEntryModel());
		dto.setRegionalDefectCombinationId(entity.getId().getRegionalDefectCombinationId());
		dto.setIsUsed(entity.getId().getIsUsed());  //PRESERVE
		dto.setTextEntryMenu(entity.getTextEntryMenu());
		return dto;
	}
	//create entity
	public static QiEntryScreenDefectCombination getQiEntryScreenDefectCombinationTbxXmlDtoEntity(QiEntryScreenDefectCombinationTbxXmlDto dto) {
		QiEntryScreenDefectCombination entity = new QiEntryScreenDefectCombination();
		
		QiEntryScreenDefectCombinationId id = new QiEntryScreenDefectCombinationId();
		id.setEntryScreen(dto.getEntryScreen());
		id.setRegionalDefectCombinationId(dto.getRegionalDefectCombinationId());
		id.setEntryModel(dto.getEntryModel());
		id.setIsUsed(dto.getIsUsed());
		entity.setId(id);

		entity.setTextEntryMenu(dto.getTextEntryMenu());
		return entity;
	}
	
	//create Xml Dto
	public static QiLocalDefectCombinationTbxXmlDto QiLocalDefectCombinationTbxXmlDtoInstance(QiLocalDefectCombination entity) {
		QiLocalDefectCombinationTbxXmlDto dto = new QiLocalDefectCombinationTbxXmlDto();
		dto.setLocalDefectCombinationId(entity.getLocalDefectCombinationId());
		dto.setRegionalDefectCombinationId(entity.getRegionalDefectCombinationId());
		dto.setEntrySiteName(entity.getEntrySiteName());
		dto.setEntryPlantName(entity.getEntryPlantName());
		dto.setResponsibleLevelId(entity.getResponsibleLevelId());
		dto.setPddaResponsibilityId(0);
		if(entity.getPddaResponsibilityId() != null)  {
			dto.setPddaResponsibilityId(entity.getPddaResponsibilityId());
		}
		dto.setEntryModel(entity.getEntryModel());
		dto.setEntryScreen(entity.getEntryScreen());
		dto.setTextEntryMenu(entity.getTextEntryMenu());
		dto.setRepairMethod(entity.getRepairMethod());
		dto.setRepairMethodTime(entity.getRepairMethodTime());
		dto.setEstimatedTimeToFix(entity.getEstimatedTimeToFix());
		dto.setLocalTheme(entity.getLocalTheme());
		dto.setEngineFiringFlag(entity.getEngineFiringFlag());
		dto.setRepairAreaName(entity.getRepairAreaName());
		dto.setDefectCategoryName(entity.getDefectCategoryName());
		dto.setReportable(entity.getReportable());
		dto.setIsUsed(entity.getIsUsed());
		return dto;
	}
	//create entity
	public static QiLocalDefectCombination getQiLocalDefectCombinationTbxXmlDtoEntity(QiLocalDefectCombinationTbxXmlDto dto) {
		QiLocalDefectCombination entity = new QiLocalDefectCombination();
		entity.setLocalDefectCombinationId(dto.getLocalDefectCombinationId());
		entity.setRegionalDefectCombinationId(dto.getRegionalDefectCombinationId());
		entity.setEntrySiteName(dto.getEntrySiteName());
		entity.setEntryPlantName(dto.getEntrySiteName());
		entity.setResponsibleLevelId(dto.getResponsibleLevelId());
		entity.setPddaResponsibilityId(dto.getPddaResponsibilityId());
		entity.setEntryModel(dto.getEntryModel());
		entity.setEntryScreen(dto.getEntryScreen());
		entity.setTextEntryMenu(dto.getTextEntryMenu());
		entity.setRepairMethod(dto.getRepairMethod());
		entity.setRepairMethodTime(dto.getRepairMethodTime());
		entity.setEstimatedTimeToFix(dto.getEstimatedTimeToFix());
		entity.setLocalTheme(dto.getLocalTheme());
		entity.setEngineFiringFlag(dto.getEngineFiringFlag());
		entity.setRepairAreaName(dto.getRepairAreaName());
		entity.setDefectCategoryName(dto.getDefectCategoryName());
		entity.setReportable(dto.getReportable());
		entity.setIsUsed(dto.getIsUsed());
		return entity;
	}

}

/**
 * 
 */
package com.honda.galc.client.teamleader.qi.stationconfig.clonestation.dto;

import com.honda.galc.entity.qi.QiStationConfiguration;
import com.honda.galc.entity.qi.QiStationConfigurationId;
import com.honda.galc.entity.qi.QiStationEntryDepartment;
import com.honda.galc.entity.qi.QiStationEntryDepartmentId;
import com.honda.galc.entity.qi.QiStationEntryScreen;
import com.honda.galc.entity.qi.QiStationEntryScreenId;
import com.honda.galc.entity.qi.QiStationPreviousDefect;
import com.honda.galc.entity.qi.QiStationPreviousDefectId;
import com.honda.galc.entity.qi.QiStationResponsibility;
import com.honda.galc.entity.qi.QiStationResponsibilityId;
import com.honda.galc.entity.qi.QiStationUpcPart;
import com.honda.galc.entity.qi.QiStationUpcPartId;
import com.honda.galc.entity.qi.QiStationWriteUpDepartment;
import com.honda.galc.entity.qi.QiStationWriteUpDepartmentId;

/**
 * @author VCC44349
 *
 */
public class QiStationConfigObjectFactory {
	
	//create Xml Dto
	public static QiStationConfigurationXmlDto getQiStationConfigurationXmlDtoInstance(QiStationConfiguration entity) {
		QiStationConfigurationXmlDto dto = new QiStationConfigurationXmlDto();
		dto.setProcessPointId(entity.getId().getProcessPointId());
		dto.setPropertyKey(entity.getId().getPropertyKey());
		dto.setValue(entity.getPropertyValue());
		dto.setActive(entity.getActive());
		return dto;
	}
    
	//create entity
	public static QiStationConfiguration getQiStationConfigurationEntity(QiStationConfigurationXmlDto dto,  String newProcessPoint, String userId) {
		QiStationConfiguration entity = new QiStationConfiguration();
		entity.setCreateUser(userId);
		entity.setId(new QiStationConfigurationId());
		entity.getId().setProcessPointId(newProcessPoint);
		entity.getId().setPropertyKey(dto.getPropertyKey());
		entity.setPropertyValue(dto.getValue());
		entity.setActive((short)dto.getActive());
		return entity;
	}
    

	//create Xml Dto
	public static QiStationEntryDepartmentXmlDto getQiStationEntryDepartmentXmlDtoInstance(QiStationEntryDepartment entity) {
		QiStationEntryDepartmentXmlDto dto = new QiStationEntryDepartmentXmlDto();
		dto.setProcessPointId(entity.getId().getProcessPointId());
		dto.setDivisionId(entity.getId().getDivisionId());
		dto.setIsDefault(entity.getIsDefault());
		return dto;
	}   	

	//create entity
	public static QiStationEntryDepartment getQiStationEntryDepartmentEntity(QiStationEntryDepartmentXmlDto dto, String newProcessPoint, String userId) {
		QiStationEntryDepartment entity = new QiStationEntryDepartment();
		entity.setCreateUser(userId);
		entity.setId(new QiStationEntryDepartmentId());
		entity.getId().setProcessPointId(newProcessPoint);
		entity.getId().setDivisionId(dto.getDivisionId());
		entity.setIsDefault((short)dto.getIsDefault());
		return entity;
	}   	

	//create Xml Dto
	public static QiStationEntryScreenXmlDto getQiStationEntryScreenXmlDtoInstance(QiStationEntryScreen entity) {
		QiStationEntryScreenXmlDto dto = new QiStationEntryScreenXmlDto();
		dto.setProcessPointId(entity.getId().getProcessPointId());
		dto.setEntryModel(entity.getId().getEntryModel());
		dto.setDivisionId(entity.getId().getDivisionId());
		dto.setEntryScreen(entity.getEntryScreen());
		dto.setSeq(entity.getId().getSeq());
		return dto;
	}   	

	//create entity
	public static QiStationEntryScreen getQiStationEntryScreenEntity(QiStationEntryScreenXmlDto dto, String newProcessPoint, String userId) {
		QiStationEntryScreen entity = new QiStationEntryScreen();
		entity.setCreateUser(userId);
		entity.setId(new QiStationEntryScreenId());
		entity.getId().setProcessPointId(newProcessPoint);
		entity.getId().setDivisionId(dto.getDivisionId());
		entity.getId().setEntryModel(dto.getEntryModel());
		entity.getId().setSeq((short)dto.getSeq());
		entity.setEntryScreen(dto.getEntryScreen());
		entity.setAllowScan((short)dto.getAllowScan());
		entity.setOrientationAngle((short)dto.getOrientationAngle());
		return entity;
	}   	

	//create Xml Dto
	public static QiStationPreviousDefectXmlDto getQiStationPreviousDefectXmlDtoInstance(QiStationPreviousDefect entity) {
		QiStationPreviousDefectXmlDto dto = new QiStationPreviousDefectXmlDto();
		dto.setProcessPointId(entity.getId().getProcessPointId());
		dto.setEntryDivisionId(entity.getId().getEntryDivisionId());
		dto.setCurrentDefectStatus(entity.getCurrentDefectStatus());
		dto.setOriginalDefectStatus(entity.getOriginalDefectStatus());
		return dto;
	}   	

	//create entity
	public static QiStationPreviousDefect getQiStationPreviousDefectEntity(QiStationPreviousDefectXmlDto dto, String newProcessPoint, String userId) {
		QiStationPreviousDefect entity = new QiStationPreviousDefect();
		entity.setCreateUser(userId);
		entity.setId(new QiStationPreviousDefectId());
		entity.getId().setProcessPointId(newProcessPoint);
		entity.getId().setEntryDivisionId(dto.getEntryDivisionId());
		entity.setCurrentDefectStatus((short)dto.getCurrentDefectStatus());
		entity.setOriginalDefectStatus((short)dto.getOriginalDefectStatus());
		return entity;
	}   	

	//create Xml Dto
	public static QiStationResponsibilityXmlDto getQiStationResponsibilityXmlDtoInstance(QiStationResponsibility entity) {
		QiStationResponsibilityXmlDto dto = new QiStationResponsibilityXmlDto();
		dto.setProcessPointId(entity.getId().getProcessPointId());
		dto.setResponsibleLevelId(entity.getId().getResponsibleLevelId());
		return dto;
	}   	

	//create entity
	public static QiStationResponsibility getQiStationResponsibilityEntity(QiStationResponsibilityXmlDto dto, String newProcessPoint, String userId) {
		QiStationResponsibility entity = new QiStationResponsibility();
		entity.setCreateUser(userId);
		entity.setId(new QiStationResponsibilityId());
		entity.getId().setProcessPointId(newProcessPoint);
		entity.getId().setResponsibleLevelId(dto.getResponsibleLevelId());
		return entity;
	}   	

	//create Xml Dto
	public static QiStationWriteUpDepartmentXmlDto getQiStationWriteUpDepartmentXmlDtoInstance(QiStationWriteUpDepartment entity) {
		QiStationWriteUpDepartmentXmlDto dto = new QiStationWriteUpDepartmentXmlDto();
		dto.setProcessPointId(entity.getId().getProcessPointId());
		dto.setDivisionId(entity.getId().getDivisionId());
		dto.setPlant(entity.getId().getPlant());
		dto.setSite(entity.getId().getSite());
		dto.setColorCode(entity.getId().getColorCode());
		dto.setColorDescription(entity.getColorDescription());
		dto.setIsDefault(entity.getIsDefault());
		return dto;
	}   	

	//create entity
	public static QiStationWriteUpDepartment getQiStationWriteUpDepartmentEntity(QiStationWriteUpDepartmentXmlDto dto, String newProcessPoint, String newPlant, String newSite, String userId) {
		QiStationWriteUpDepartment entity = new QiStationWriteUpDepartment();
		entity.setCreateUser(userId);
		entity.setId(new QiStationWriteUpDepartmentId());
		entity.getId().setProcessPointId(newProcessPoint);
		entity.getId().setDivisionId(dto.getDivisionId());
		entity.getId().setPlant(newPlant);
		entity.getId().setSite(newSite);
		entity.getId().setColorCode(dto.getColorCode());
		entity.setColorDescription(dto.getColorDescription());
		entity.setIsDefault((short)dto.getIsDefault());
		return entity;
	}   	

	//create Xml Dto
	public static QiStationUpcPartXmlDto getQiStationUpcPartXmlDtoInstance(QiStationUpcPart entity) {
		QiStationUpcPartXmlDto dto = new QiStationUpcPartXmlDto();
		dto.setProcessPointId(entity.getId().getProcessPointId());
		dto.setMainPartNo(entity.getId().getMainPartNo());
		return dto;
	}   	

	//create entity
	public static QiStationUpcPart getQiStationUpcPartEntity(QiStationUpcPartXmlDto dto, String newProcessPoint, String userId) {
		QiStationUpcPart entity = new QiStationUpcPart();
		entity.setCreateUser(userId);
		entity.setId(new QiStationUpcPartId());
		entity.getId().setProcessPointId(newProcessPoint);
		entity.getId().setMainPartNo(dto.getMainPartNo());
		return entity;
	}   	

}

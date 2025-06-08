package com.honda.galc.dto.qi;

import java.util.Comparator;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;

import com.honda.galc.dto.DtoTag;
import com.honda.galc.dto.IDto;
import com.honda.galc.entity.qi.QiDepartment;
import com.honda.galc.entity.qi.QiPlant;
import com.honda.galc.entity.qi.QiResponsibleLevel;
import com.honda.galc.entity.qi.QiSite;

/**
 * 
 * <h3>QiStationResponsibilityDto Class description</h3>
 * <p>
 * QiStationResponsibilityDto: DTO class for Limit Responsibility
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
 *        June 13, 2017
 * 
 */
public class QiStationResponsibilityDto implements IDto,Comparator<QiStationResponsibilityDto>{

	private static final long serialVersionUID = 1L;

	@DtoTag(outputName = "RESPONSIBLE_LEVEL_ID")
	private Integer responsibleLevelId;
	
	@DtoTag(outputName = "PROCESS_POINT_ID")
	private String processPointId;
	
	@DtoTag(outputName = "SITE")
	private String site;
	
	@DtoTag(outputName = "PLANT")
	private String plant;
	
	@DtoTag(outputName = "DEPT")
	private String dept;
	
	@DtoTag(outputName = "DEPARTMENT_NAME")
	private String departmentName;

	@DtoTag(outputName = "RESPONSIBLE_LEVEL_NAME")
	private String responsibleLevelName;

	public QiStationResponsibilityDto() {
		super();
	}
	
	public QiStationResponsibilityDto(QiSite qiSite) {
		super();
		this.site = qiSite.getSite();
	}
	
	public QiStationResponsibilityDto(QiPlant qiPlant) {
		super();
		this.site = qiPlant.getId().getSite();
		this.plant = qiPlant.getId().getPlant();
	}
	
	public QiStationResponsibilityDto(QiDepartment qiDepartment) {
		super();
		this.site = qiDepartment.getId().getSite();
		this.plant = qiDepartment.getId().getPlant();
		this.dept = qiDepartment.getId().getDepartment();
		this.departmentName = qiDepartment.getDepartmentName();
	}
	
	public QiStationResponsibilityDto(QiResponsibleLevel qiResponsibleLevel) {
		super();
		this.responsibleLevelId = qiResponsibleLevel.getResponsibleLevelId();
		this.site = qiResponsibleLevel.getSite();
		this.plant = qiResponsibleLevel.getPlant();
		this.dept = qiResponsibleLevel.getDepartment();
		this.responsibleLevelName = qiResponsibleLevel.getResponsibleLevelName();
	}
	
	public Integer getResponsibleLevelId() {
		return responsibleLevelId;
	}

	public void setResponsibleLevelId(Integer responsibleLevelId) {
		this.responsibleLevelId = responsibleLevelId;
	}

	public String getProcessPointId() {
		return StringUtils.trimToEmpty(processPointId);
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	public String getSite() {
		return StringUtils.trimToEmpty(site);
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getPlant() {
		return StringUtils.trimToEmpty(plant);
	}

	public void setPlant(String plant) {
		this.plant = plant;
	}

	public String getDept() {
		return StringUtils.trimToEmpty(dept);
	}

	public void setDept(String department) {
		this.dept = department;
	}

	public String getResponsibleLevelName() {
		return StringUtils.trimToEmpty(responsibleLevelName);
	}

	public void setResponsibleLevelName(String responsibleLevelName) {
		this.responsibleLevelName = responsibleLevelName;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dept == null) ? 0 : dept.hashCode());
		result = prime * result + ((plant == null) ? 0 : plant.hashCode());
		result = prime * result + ((processPointId == null) ? 0 : processPointId.hashCode());
		result = prime * result + ((responsibleLevelId == null) ? 0 : responsibleLevelId.hashCode());
		result = prime * result + ((responsibleLevelName == null) ? 0 : responsibleLevelName.hashCode());
		result = prime * result + ((site == null) ? 0 : site.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QiStationResponsibilityDto other = (QiStationResponsibilityDto) obj;
		if (dept == null) {
			if (other.dept != null)
				return false;
		} else if (!dept.equals(other.dept))
			return false;
		if (plant == null) {
			if (other.plant != null)
				return false;
		} else if (!plant.equals(other.plant))
			return false;
		if (processPointId == null) {
			if (other.processPointId != null)
				return false;
		} else if (!processPointId.equals(other.processPointId))
			return false;
		if (responsibleLevelId == null) {
			if (other.responsibleLevelId != null)
				return false;
		} else if (!responsibleLevelId.equals(other.responsibleLevelId))
			return false;
		if (responsibleLevelName == null) {
			if (other.responsibleLevelName != null)
				return false;
		} else if (!responsibleLevelName.equals(other.responsibleLevelName))
			return false;
		if (site == null) {
			if (other.site != null)
				return false;
		} else if (!site.equals(other.site))
			return false;
		return true;
	}

	public int compare(QiStationResponsibilityDto object1, QiStationResponsibilityDto object2) {
		return new CompareToBuilder().append(object1.getSite(), object2.getSite())
									.append(object1.getPlant(), object2.getPlant())
									.append(object1.getDept(), object2.getDept())
									.append(object1.getResponsibleLevelName(), object2.getResponsibleLevelName()).toComparison();
	}
	
	public String getConcatenatedString(){
		return getProcessPointId()+" "+ getSite()+" "+getPlant()+" "+getDept()+" "+getResponsibleLevelName();
	}
}

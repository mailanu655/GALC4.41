package com.honda.galc.dto.qi;

import com.honda.galc.dto.DtoTag;
import com.honda.galc.dto.IDto;

public class QiPlantDepartmentDto implements IDto {

	private static final long serialVersionUID = 1L;
	
	@DtoTag(name = "SITE")
	private String site;
	@DtoTag(name = "PLANT")
	private String plant;
	@DtoTag(name = "ENTRY_SITE")
	private String entrySite;
	@DtoTag(name = "ENTRY_PLANT")
	private String entryPlant;
	@DtoTag(name = "DEPT")
	private String department;
	@DtoTag(name = "PDDA_DEPT")
	private String pddaDept;
	
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getPlant() {
		return plant;
	}
	public void setPlant(String plant) {
		this.plant = plant;
	}
	public String getEntrySite() {
		return entrySite;
	}
	public void setEntrySite(String entrySite) {
		this.entrySite = entrySite;
	}
	public String getEntryPlant() {
		return entryPlant;
	}
	public void setEntryPlant(String entryPlant) {
		this.entryPlant = entryPlant;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getPddaDept() {
		return pddaDept;
	}
	public void setPddaDept(String pddaDept) {
		this.pddaDept = pddaDept;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((site == null) ? 0 : site.hashCode());
		result = prime * result + ((plant == null) ? 0 : plant.hashCode());
		result = prime * result + ((entrySite == null) ? 0 : entrySite.hashCode());
		result = prime * result + ((entryPlant == null) ? 0 : entryPlant.hashCode());
		result = prime * result + ((department == null) ? 0 : department.hashCode());
		result = prime * result + ((pddaDept == null) ? 0 : pddaDept.hashCode());
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
		QiPlantDepartmentDto other = (QiPlantDepartmentDto) obj;
		if (site == null) {
			if (other.site != null)
				return false;
		} else if (!site.equals(other.site))
			return false;
		if (plant == null) {
			if (other.plant != null)
				return false;
		} else if (!plant.equals(other.plant))
			return false;
		if (entrySite == null) {
			if (other.entrySite != null)
				return false;
		} else if (!entrySite.equals(other.entrySite))
			return false;
		if (entryPlant == null) {
			if (other.entryPlant != null)
				return false;
		} else if (!entryPlant.equals(other.entryPlant))
			return false;
		if (department == null) {
			if (other.department != null)
				return false;
		} else if (!department.equals(other.department))
			return false;
		if (pddaDept == null) {
			if (other.pddaDept != null)
				return false;
		} else if (!pddaDept.equals(other.pddaDept))
			return false;
		return true;
	}
}

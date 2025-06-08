package com.honda.galc.client.teamleader.qi.stationconfig.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.dto.IDto;
import com.honda.galc.dto.qi.QiDepartmentDto;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.qi.QiDepartment;

public class ComboBoxDisplayDto implements IDto, Comparable<ComboBoxDisplayDto> {
	
	public String id = "";
	public String name = "";
	public String description = "";
	
	private ComboBoxDisplayDto()  {
		id = "";
		name = "";
		description = "";
	}
	
	public static ComboBoxDisplayDto getInstance(Division d)  {
		ComboBoxDisplayDto instance = new ComboBoxDisplayDto();
		if(d == null) return null;
		instance.setId(d.getDivisionId());
		instance.setName(d.getDivisionName());
		instance.setDescription(d.getDivisionDescription());
		return instance;
	}
	
	public static ComboBoxDisplayDto getInstance(ProcessPoint pp)  {
		ComboBoxDisplayDto instance = new ComboBoxDisplayDto();
		if(pp == null) return null;
		instance.setId(pp.getProcessPointId());
		instance.setName(pp.getProcessPointName());
		instance.setDescription(pp.getProcessPointDescription());
		return instance;
	}
	
	public static ComboBoxDisplayDto getInstance(QiDepartmentDto qiDept)  {
		ComboBoxDisplayDto instance = new ComboBoxDisplayDto();
		if(qiDept == null) return null;
		instance.setId(qiDept.getDepartment());
		instance.setName(qiDept.getDepartmentName());
		instance.setDescription(qiDept.getDepartmentDescription());
		return instance;
	}
	
	public static List<ComboBoxDisplayDto> getDivisionUniqueList(List<Division> myList)  {
		if(myList == null || myList.isEmpty())  return null;
		Set<Division> newSet = new HashSet<Division>(myList);
		List<ComboBoxDisplayDto> dtoList = new ArrayList<ComboBoxDisplayDto>();
		for(Division d : newSet)  {
			ComboBoxDisplayDto dto = getInstance(d);
			dtoList.add(dto);
		}
		return dtoList;
	}
	
	public static String getComboBoxSelectedId(LoggedComboBox<ComboBoxDisplayDto> whichComboBox) {
		ComboBoxDisplayDto dto = (ComboBoxDisplayDto)whichComboBox.getValue();
		String id = "";
		if(dto != null)  {
			id = dto.getId();
		}
		return id;
	}

   public int compareTo(ComboBoxDisplayDto otherDiv)  {
    	if(otherDiv == null)  return 1;
    	int c1 = id.compareTo(otherDiv.getId());
    	if(c1 == 0)  {
    		int c2 = name.compareTo(otherDiv.getName());
    		if(c2 == 0)  {
    			return description.compareTo(otherDiv.getDescription());
    		}
    		else  return c2;
    	}
    	else  {
    		return c1;
    	}
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(id).append("-").append(name);
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		ComboBoxDisplayDto other = (ComboBoxDisplayDto) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
}

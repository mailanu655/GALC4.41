package com.honda.galc.client.teamleader.qi.stationconfig.dto;

import com.honda.galc.dto.IDto;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.ProcessPoint;

public class ApplicationDetailDto implements IDto, Comparable<ApplicationDetailDto> {
	
	public String id = "";
	public String name = "";
	public String description = "";
	public String windowTitle = "";
	
	private ApplicationDetailDto()  {
		id = "";
		name = "";
		description = "";
		windowTitle = "";
	}
	
	public static ApplicationDetailDto getInstance(Application app)  {
		ApplicationDetailDto instance = new ApplicationDetailDto();
		if(app == null) return null;
		instance.setId(app.getApplicationId());
		instance.setName(app.getApplicationName());
		instance.setDescription(app.getApplicationDescription());
		instance.setWindowTitle(app.getWindowTitle());
		return instance;
	}
	
    public int compareTo(ApplicationDetailDto otherDiv)  {
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

	public String getWindowTitle() {
		return windowTitle;
	}

	public void setWindowTitle(String windowTitle) {
		this.windowTitle = windowTitle;
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
		ApplicationDetailDto other = (ApplicationDetailDto) obj;
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

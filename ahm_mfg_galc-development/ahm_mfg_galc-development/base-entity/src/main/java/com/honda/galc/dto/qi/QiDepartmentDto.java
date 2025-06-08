package com.honda.galc.dto.qi;

import java.util.Comparator;

import javax.persistence.Column;

import org.apache.commons.lang.StringUtils;
import com.honda.galc.dto.DtoTag;
import com.honda.galc.dto.IDto;
/**
 * 
 * <h3>QiDepartmentDto Class description</h3>
 * <p>
 * QiDepartmentDto contains the getter and setter of the Location properties and maps this class with database table and properties with the database its columns .
 * </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * 
 * </TABLE>
 * 
 * @author vcc44349<br>
 * 
 */
public class QiDepartmentDto implements IDto,Comparator<QiDepartmentDto>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@DtoTag(name = "DEPT")
	private String department;
	@DtoTag(name = "DEPT_NAME")
    private String departmentName;
	@DtoTag(name = "DEPT_DESCRIPTION")
    private String departmentDescription;
	
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getDepartmentDescription() {
		return departmentDescription;
	}
	public void setDepartmentDescription(String departmentDescription) {
		this.departmentDescription = departmentDescription;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((department == null) ? 0 : department.hashCode());
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
		QiDepartmentDto other = (QiDepartmentDto) obj;
		if (department == null) {
			if (other.department != null)
				return false;
		} else if (!department.equals(other.department))
			return false;
		return true;
	}
	public int compare(QiDepartmentDto qiDepartmentDto1, QiDepartmentDto qiDepartmentDto2) {
		return qiDepartmentDto1.getDepartment().compareToIgnoreCase(qiDepartmentDto2.getDepartment());
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(department).append("-").append(department);
		return sb.toString();
	}


}

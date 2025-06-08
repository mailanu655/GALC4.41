package com.honda.galc.entity.qics;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.honda.galc.entity.AuditEntry;
 /** * *
 * @version 1 
 * @author Gangadhararao Gadde 
 * @since Aug 02, 2013 
 */ 
@Entity
@Table(name="DEFECT_ACTUAL_PROBLEM_TBX")
public class DefectActualProblem extends AuditEntry {
	
	@EmbeddedId
    private DefectActualProblemId id;

	private static final long serialVersionUID = 1L;

	public DefectActualProblem() {
		super();
	}	

	public DefectActualProblem(DefectActualProblemId id) {
		super();
		this.id = id;
	}

	public DefectActualProblemId getId() {
		return id;
	}

	public void setId(DefectActualProblemId id) {
		this.id = id;
	}
	
	public String toString() {
		return getId().getActualProblemName();
	}


}

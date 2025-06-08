package com.honda.galc.client.teamlead;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.honda.galc.client.dto.ChangeFormDTO;

public class ChangeFormDetails implements Serializable{
	private static final long serialVersionUID = 1L;
	private List<ChangeFormDTO> changeForms;
	private Map<String, List<ChangeFormDTO>> deptCodeWiseChgFrms;
	
	public ChangeFormDetails(List<ChangeFormDTO> changeForms,
			Map<String, List<ChangeFormDTO>> deptCodeWiseChgFrms) {
		this.changeForms = changeForms;
		this.deptCodeWiseChgFrms = deptCodeWiseChgFrms;
	}

	public List<ChangeFormDTO> getChangeForms() {
		return changeForms;
	}

	public void setChangeForms(List<ChangeFormDTO> changeForms) {
		this.changeForms = changeForms;
	}

	public Map<String, List<ChangeFormDTO>> getDeptCodeWiseChgFrms() {
		return deptCodeWiseChgFrms;
	}

	public void setDeptCodeWiseChgFrms(
			Map<String, List<ChangeFormDTO>> deptCodeWiseChgFrms) {
		this.deptCodeWiseChgFrms = deptCodeWiseChgFrms;
	}
}
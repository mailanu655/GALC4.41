package com.honda.galc.client.teamlead;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.honda.galc.client.dto.MCPddaPlatformDTO;

public class PddaPlatformDetails implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<MCPddaPlatformDTO> pddaPlatforms;
	private Map<String, List<MCPddaPlatformDTO>> deptCodeWisePddaPlatforms;
	
	
	public PddaPlatformDetails(List<MCPddaPlatformDTO> pddaPlatforms,
			Map<String, List<MCPddaPlatformDTO>> deptCodeWisePddaPlatforms) {
		super();
		this.pddaPlatforms = pddaPlatforms;
		this.deptCodeWisePddaPlatforms = deptCodeWisePddaPlatforms;
	}
	
	public List<MCPddaPlatformDTO> getPddaPlatforms() {
		return pddaPlatforms;
	}
	public void setPddaPlatforms(List<MCPddaPlatformDTO> pddaPlatforms) {
		this.pddaPlatforms = pddaPlatforms;
	}
	public Map<String, List<MCPddaPlatformDTO>> getDeptCodeWisePddaPlatforms() {
		return deptCodeWisePddaPlatforms;
	}
	public void setDeptCodeWisePddaPlatforms(
			Map<String, List<MCPddaPlatformDTO>> deptCodeWisePddaPlatforms) {
		this.deptCodeWisePddaPlatforms = deptCodeWisePddaPlatforms;
	}

	

}

package com.honda.galc.client.loader.dto;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MfgDataLoadDto {
	private Set<Integer> mass_chg_frm_list = new LinkedHashSet<Integer>();
	private Set<Integer> std_chg_frm_list = new LinkedHashSet<Integer>();
	private Map<Integer, List<PlatformDto>> chgFrm_platform_map = new LinkedHashMap<Integer, List<PlatformDto>>();
	
	public void addMassChangeFormId(int changeFormId, PlatformDto platformDto) {
		mass_chg_frm_list.add(changeFormId);
		addPlatform(changeFormId, platformDto);
	}
	
	public void addStdChangeFormId(int changeFormId, PlatformDto platformDto) {
		std_chg_frm_list.add(changeFormId);
		addPlatform(changeFormId, platformDto);
	}
	
	private void addPlatform(int changeFormId, PlatformDto platformDto) {
		if(chgFrm_platform_map.containsKey(changeFormId)) {
			List<PlatformDto> pltfrmList = chgFrm_platform_map.get(changeFormId);
			if(pltfrmList == null) {
				pltfrmList = new ArrayList<PlatformDto>();
			}
			pltfrmList.add(platformDto);
			chgFrm_platform_map.put(changeFormId, pltfrmList);
		}
		else {
			List<PlatformDto> pltfrmList = new ArrayList<PlatformDto>();
			pltfrmList.add(platformDto);
			chgFrm_platform_map.put(changeFormId, pltfrmList);
		}
	}

	public Set<Integer> getMass_chg_frm_list() {
		return mass_chg_frm_list;
	}

	public Set<Integer> getStd_chg_frm_list() {
		return std_chg_frm_list;
	}

	public Map<Integer, List<PlatformDto>> getChgFrm_platform_map() {
		return chgFrm_platform_map;
	}
}

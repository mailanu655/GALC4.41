package com.honda.galc.handheld.data;

import java.io.Serializable;
import java.util.List;


public class BuildResultContainer implements Serializable{
	private static final long serialVersionUID = 1L;

	List<BuildResultBean> buildResults;

	public List<BuildResultBean> getBuildResults() {
		return buildResults;
	}

	public void setBuildResults(List<BuildResultBean> buildResults) {
		this.buildResults = buildResults;
	}
	
	public BuildResultBean getBuildResult(int index) {
		return buildResults.get(index);
	}
	
	public void setBuildResult(int index, BuildResultBean newResult) {
		buildResults.add(index, newResult);
	}
}

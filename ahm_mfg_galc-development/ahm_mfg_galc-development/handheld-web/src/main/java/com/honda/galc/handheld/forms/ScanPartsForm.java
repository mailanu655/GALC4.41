package com.honda.galc.handheld.forms;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.honda.galc.handheld.data.BuildResultBean;
import com.honda.galc.handheld.data.HandheldConstants;
import com.honda.galc.handheld.plugin.InitializationPlugIn;

public class ScanPartsForm extends ValidatedUserHandheldForm {
	private static final long serialVersionUID = 1L;
	private List <BuildResultBean> results = new ArrayList<BuildResultBean>();
	private String cancelRequested = "";
	
	private HttpServletRequest request;

	public List <BuildResultBean> getResults() {
		return results;
	}
	
	public void setResults(List <BuildResultBean> results) {
		this.results = results;
	}

	public BuildResultBean getEachResult(int index) {
		@SuppressWarnings("unchecked")
		List<BuildResultBean>  snResults = (List<BuildResultBean>)request.getSession().getAttribute("results");
		
		if(snResults != null) results = snResults;
		
		if (results.size() < index + 1) {
			int numberOfNewResults = results.size() + index + 1; 
			for (int i=0;i<numberOfNewResults;i++) 
				results.add(new BuildResultBean());
		}
		return results.get(index);
	}
	
	public void setEachResult(int index, BuildResultBean updatedBean) {
		results.add(index, updatedBean);		
	}

	public String getCancelRequested() {
		return cancelRequested;
	}

	public void setCancelRequested(String cancelRequested) {
		this.cancelRequested = cancelRequested;
	}
	
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.request = request;
	}
	
	private String getUserId() {
		return request == null ? "N/A" : (String)request.getSession().getAttribute(HandheldConstants.USER_ID);
	}
	
	protected void logInfo(String message) {
		InitializationPlugIn.info(getUserId(), message);
	}

}

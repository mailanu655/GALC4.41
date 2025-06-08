package com.honda.galc.client.schedule;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.client.mvc.IModel;
import com.honda.galc.vios.dto.PddaPlatformDto;

public class ScheduleClientModel implements IModel{
	
	private boolean onCut=false;
	
	private List<Integer> cutRowIndices = new ArrayList<Integer>();
	
	private Integer pasteRowIndex=null;
	
	public static final String CUT="Cut";
	
	public static final String PASTE="Paste";
	
	public static final String CANCEL="Cancel";
	
	public static final String CUTPASTE="Cut-Paste";
	
	public PddaPlatformDto pddaPlatform;
	
	public boolean isOnCut() {
		return onCut;
	}


	public void setOnCut(boolean onCut) {
		this.onCut = onCut;
	}


	public List<Integer> getCutRowIndices() {
		return cutRowIndices;
	}


	public void setCutRowIndices(List<Integer> cutRowIndices) {
		this.cutRowIndices = cutRowIndices;
	}


	public Integer getPasteRowIndex() {
		return pasteRowIndex;
	}


	public void setPasteRowIndex(Integer pasteRowIndex) {
		this.pasteRowIndex = pasteRowIndex;
	}


	public PddaPlatformDto getPddaPlatform() {
		return pddaPlatform;
	}


	public void setPddaPlatform(PddaPlatformDto pddaPlatform) {
		this.pddaPlatform = pddaPlatform;
	}


	@Override
	public void reset() {
		this.onCut=false;
		this.cutRowIndices.clear();
		this.pasteRowIndex=null;
		this.pddaPlatform = null;
	}

}

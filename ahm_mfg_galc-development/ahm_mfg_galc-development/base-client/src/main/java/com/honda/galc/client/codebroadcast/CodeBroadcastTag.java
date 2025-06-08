package com.honda.galc.client.codebroadcast;

import java.util.List;

public class CodeBroadcastTag {

	public enum TagType { SPECIAL_TAG, COLOR_TAG, SPECIAL_COLOR, NONE };

	private final TagType tagType;
	private final String tag;
	private final String productSpec;
	private final List<CodeBroadcastCode> codes;
	private final List<CodeBroadcastCode> displayCodes;
	private boolean selected;

	public CodeBroadcastTag(final TagType tagType, final String tag, final String productSpec) {
		this.tagType = tagType;
		this.tag = tag;
		this.productSpec = productSpec;
		this.codes = null;
		this.displayCodes = null;
		this.selected = false;
	}

	public CodeBroadcastTag(final TagType tagType, final String tag, final String productSpec, final List<CodeBroadcastCode> codes) {
		this.tagType = tagType;
		this.tag = tag;
		this.productSpec = productSpec;
		this.codes = codes;
		this.displayCodes = null;
		this.selected = false;
	}

	public CodeBroadcastTag(final TagType tagType, final String tag, final String productSpec, final List<CodeBroadcastCode> codes, final List<CodeBroadcastCode> displayCodes) {
		this.tagType = tagType;
		this.tag = tag;
		this.productSpec = productSpec;
		this.codes = codes;
		this.displayCodes = displayCodes;
		this.selected = false;
	}

	public TagType getTagType() {
		return this.tagType;
	}

	public String getTag() {
		return this.tag;
	}

	public String getProductSpec() {
		return this.productSpec;
	}

	public List<CodeBroadcastCode> getCodes() {
		return this.codes;
	}

	public List<CodeBroadcastCode> getDisplayCodes() {
		return this.displayCodes;
	}

	public boolean isSelected() {
		return this.selected;
	}

	public void select() {
		this.selected = true;
	}

	public void deselect() {
		this.selected = false;
		resetCodes();
	}

	private void resetCodes() {
		if (codes != null) {
			for (CodeBroadcastCode code : codes) {
				code.setConfirmed(false);
			}
		}
	}
}

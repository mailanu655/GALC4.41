package com.honda.galc.client.codebroadcast;

import java.awt.Cursor;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JRadioButton;

import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.conf.PrintAttributeFormat;
import com.honda.galc.entity.product.BuildAttribute;

public class CodeBroadcastSpecialTagController extends CodeBroadcastController {

	private static final String COUNT = "COUNT";

	private List<CodeBroadcastTag> specialTags;

	/*
	 * --------------------------------------------------
	 * Getters/Setters
	 * --------------------------------------------------
	 */
	private CodeBroadcastSpecialTagPanel getSpecialTagPanel() {
		return (CodeBroadcastSpecialTagPanel) getPanel();
	}
	private List<CodeBroadcastTag> getSpecialTags() {
		return this.specialTags;
	}
	private void setSpecialTags(List<CodeBroadcastTag> specialTags) {
		this.specialTags = specialTags;
	}
	protected String[] getStationJobCodes() {
		String[] jobCodes = getPropertyBean().getStationJobCodesBySpecialTag();
		if (jobCodes == null || jobCodes.length <= 0) {
			jobCodes = getPropertyBean().getStationJobCodes();
		}
		return jobCodes;
	}
	protected List<CodeBroadcastCode> getCodes() {
		CodeBroadcastTag specialTag = getSelectedSpecialTag();
		if (specialTag == null) return null;
		return specialTag.getCodes();
	}
	@Override
	protected PrintAttributeFormat[] getStationDisplayFormats() {
		return null;
	}



	/*
	 * --------------------------------------------------
	 * Constructors
	 * --------------------------------------------------
	 */

	public CodeBroadcastSpecialTagController(CodeBroadcastPanel panel) {
		super(panel);
	}



	/*
	 * --------------------------------------------------
	 * Initialization methods
	 * --------------------------------------------------
	 */

	@Override
	protected void initialize() {
		try {
			loadSpecialTags();
			super.initialize();
			getSpecialTagPanel().populateSpecialTagPanel(getSpecialTags());
			CodeBroadcastDeviceListener.getInstance().registerController(this);
			if (!getPanel().isErrorMessage()) requestTrigger();
		} catch (Exception e) {
			getPanel().displayErrorMessage(e.toString(), e);
		}
	}

	@Override
	protected void doConfirm() {
		try {
			stopTimeoutTimer();
			checkMessage();
			Logger.getLogger().info("Confirming codes");
			if (!getPropertyBean().isIgnoreErrorStateCheck() && !isErrorState()) {
				getPanel().displayWarningMessage("Cannot confirm codes: not in error state", true);
				unconfirm();
				restartTimeoutTimer();
				return;
			}
			if (!getPropertyBean().isConfirmConfirmation() || MessageDialog.confirm(getPanel().getMainWindow(), getConfirmationSummary(true, getCodes(), null, null))) {
				getPanel().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				if (broadcastCodes(getCodes(), null, null, true)) {
					Logger.getLogger().info("Broadcasted codes " + getCodesSummary(getCodes()));
				} else {
					Logger.getLogger().info("Failed to broadcast codes " + getCodesSummary(getCodes()));
					unconfirm();
				}
				getPanel().setCursor(Cursor.getDefaultCursor());
			} else {
				restartTimeoutTimer();
			}
		} catch (Exception e) {
			getPanel().setCursor(Cursor.getDefaultCursor());
			getPanel().displayErrorMessage(e.toString(), e);
		}
	}

	private void handleSpecialTagSelection(final CodeBroadcastTag specialTag) {
		stopTimeoutTimer();
		checkMessage();
		getPanel().getProductSpecTextField().setText(specialTag.getProductSpec());
		getPanel().populateDisplayFields(getPanel().getDisplayFields(), specialTag.getDisplayCodes());
		getPanel().populateCodePanel(getPanel().getCodePanel(), getPanel().getCodePanelFields(), getPanel().getConfirmButton(), specialTag.getCodes(), getPropertyBean().isDisplayOnly());
		if (specialTag.getCodes() == null || specialTag.getCodes().isEmpty()) {
			getPanel().getConfirmButton().setEnabled(true);
		}
		Logger.getLogger().info("User selected special tag: " + specialTag.getTag());
		restartTimeoutTimer();
	}

	private void loadSpecialTags() {
		try {
			List<CodeBroadcastTag> specialTags = new ArrayList<CodeBroadcastTag>();
			int specialTagCount = getSpecialTagCount();
			StringBuilder errorBuilder = new StringBuilder();
			for (int i = 1; i <= specialTagCount; i++) {
				String specialTag; {
					String specialTagName = concatWithUnderscore(getPropertyBean().getStationName(), CodeBroadcastTag.TagType.SPECIAL_TAG.name(), String.valueOf(i));
					BuildAttribute specialTagAttribute = getSingleBuildAttribute(specialTagName);
					specialTag = specialTagAttribute.getAttributeValue();
				}
				String productSpec;
				List<CodeBroadcastCode> displayCodes;
				List<CodeBroadcastCode> codes; {
					String productSpecAttributeName = concatWithUnderscore(getPropertyBean().getStationName(), specialTag);
					BuildAttribute productSpecAttribute = getSingleBuildAttribute(productSpecAttributeName);
					productSpec = productSpecAttribute.getAttributeValue();
					if (productSpec == null) {
						getPanel().displayErrorMessage("No product spec defined for " + productSpecAttributeName, true);
					}
					displayCodes = getCodesForProductSpec(productSpec, getPropertyBean().getStationDisplayCodes());
					codes = getCodesForProductSpec(productSpec, getStationJobCodes());
					String result = validateCodesForProductSpec(codes, productSpec);
					if (result != null) {
						if (errorBuilder.length() != 0) {
							errorBuilder.append("\n");
						}
						errorBuilder.append(result);
					}
				}
				CodeBroadcastTag codeBroadcastSpecialTag = new CodeBroadcastTag(CodeBroadcastTag.TagType.SPECIAL_TAG, specialTag, productSpec, codes, displayCodes);
				specialTags.add(codeBroadcastSpecialTag);
			}
			if (errorBuilder.length() != 0) {
				getPanel().displayErrorMessage(errorBuilder.toString(), true);
			}
			setSpecialTags(specialTags);
		} catch (Exception e) {
			getPanel().displayErrorMessage(e.toString(), e);
			setSpecialTags(null);
		}
	}



	/*
	 * --------------------------------------------------
	 * Helper/utility methods
	 * --------------------------------------------------
	 */

	public ItemListener getSpecialTagPanelItemListener(final JRadioButton tagButton, final CodeBroadcastTag specialTag) {
		return (new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					specialTag.select();
					tagButton.setBackground(getPropertyBean().getColorConfirmed());
					handleSpecialTagSelection(specialTag);
				}
				else if (event.getStateChange() == ItemEvent.DESELECTED) {
					specialTag.deselect();
					tagButton.setBackground(getPropertyBean().getColorNeutral());
				}
			}
		});
	}

	private int getSpecialTagCount() {
		String specialTagCountName = concatWithUnderscore(getPropertyBean().getStationName(), CodeBroadcastTag.TagType.SPECIAL_TAG.name(), COUNT);
		BuildAttribute specialTagCountBuildAttribute = getSingleBuildAttribute(specialTagCountName);
		if (specialTagCountBuildAttribute == null)
			return 0;
		int specialTagCount = Integer.valueOf(specialTagCountBuildAttribute.getAttributeValue());
		return specialTagCount;
	}

	private CodeBroadcastTag getSelectedSpecialTag() {
		for (CodeBroadcastTag specialTag : getSpecialTags()) {
			if (specialTag.isSelected()) return specialTag;
		}
		return null;
	}

}

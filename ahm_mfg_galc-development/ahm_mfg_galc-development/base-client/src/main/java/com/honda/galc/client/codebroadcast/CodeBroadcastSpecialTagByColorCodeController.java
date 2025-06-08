package com.honda.galc.client.codebroadcast;

import java.awt.Cursor;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.JRadioButton;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.PrintAttributeFormat;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.service.ServiceFactory;

public class CodeBroadcastSpecialTagByColorCodeController extends CodeBroadcastController {

	private static final String COUNT = "COUNT";
	private static final String MTOC_WILDCARD = "*         ";

	private final boolean showSpecialColors;
	private List<CodeBroadcastTag> specialTags;

	/*
	 * --------------------------------------------------
	 * Getters/Setters
	 * --------------------------------------------------
	 */
	private CodeBroadcastSpecialTagByColorCodePanel getSpecialTagPanel() {
		return (CodeBroadcastSpecialTagByColorCodePanel) getPanel();
	}
	public boolean isShowSpecialColors() {
		return this.showSpecialColors;
	}
	private List<CodeBroadcastTag> getSpecialTags() {
		return this.specialTags;
	}
	private void setSpecialTags(List<CodeBroadcastTag> specialTags) {
		this.specialTags = specialTags;
	}
	protected String[] getStationJobCodes() {
		String[] jobCodes = getPropertyBean().getStationJobCodesByColorCode();
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
	protected List<CodeBroadcastCode> getColorCodes() {
		CodeBroadcastTag colorCodeTag = getSpecialTagPanel().getSelectedColorCode();
		if (colorCodeTag == null) return null;
		return colorCodeTag.getCodes();
	}
	protected List<CodeBroadcastCode> getSpecialColorCodes() {
		if (!isShowSpecialColors()) return null;
		CodeBroadcastTag specialColorTag = getSpecialTagPanel().getSelectedSpecialColor();
		if (specialColorTag == null) return null;
		return specialColorTag.getCodes();
	}
	private List<CodeBroadcastCode> getAllCodes() {
		final List<CodeBroadcastCode> allCodes = new ArrayList<CodeBroadcastCode>();
		final List<CodeBroadcastCode> codes = getCodes();
		final List<CodeBroadcastCode> colorCodes = getColorCodes();
		final List<CodeBroadcastCode> specialColorCodes = getSpecialColorCodes();
		if (codes != null) {
			allCodes.addAll(codes);
		}
		if (colorCodes != null) {
			allCodes.addAll(colorCodes);
		}
		if (specialColorCodes != null) {
			allCodes.addAll(specialColorCodes);
		}
		return allCodes;
	}



	/*
	 * --------------------------------------------------
	 * Constructors
	 * --------------------------------------------------
	 */

	public CodeBroadcastSpecialTagByColorCodeController(CodeBroadcastPanel panel) {
		super(panel);
		this.showSpecialColors = ArrayUtils.isNotEmpty(getPropertyBean().getStationSpecialColorCodes());
	}
	@Override
	protected PrintAttributeFormat[] getStationDisplayFormats() {
		return null;
	}



	/*
	 * --------------------------------------------------
	 * Initialization methods
	 * --------------------------------------------------
	 */

	@Override
	protected void initialize() {
		try {
			setSpecialTags(loadTags(CodeBroadcastTag.TagType.SPECIAL_TAG));
			super.initialize();
			getSpecialTagPanel().populateSpecialTagPanel(getSpecialTags());
			CodeBroadcastDeviceListener.getInstance().registerController(this);
			if (!getPanel().isErrorMessage()) requestTrigger();
		} catch (Exception e) {
			getPanel().displayErrorMessage(e.toString(), e);
		}
	}

	@Override
	protected void initConnections() {
		try {
			super.initConnections();
			getSpecialTagPanel().getColorCodeComboBox().addItemListener(getColorCodeItemListener());
			if (isShowSpecialColors()) {
				getSpecialTagPanel().getSpecialColorComboBox().addItemListener(getSpecialColorItemListener());
			}
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
			final String colorCode; {
				CodeBroadcastTag selectedColorCode = getSpecialTagPanel().getSelectedColorCode();
				if (selectedColorCode.getTagType().equals(CodeBroadcastTag.TagType.COLOR_TAG)) {
					colorCode = getPropertyBean().getExclusionCode();
				} else {
					colorCode = getSpecialTagPanel().getSelectedColorCode().getProductSpec();
				}
			}
			if (!getPropertyBean().isConfirmConfirmation() || MessageDialog.confirm(getPanel().getMainWindow(), getConfirmationSummary(true, getAllCodes(), null, colorCode))) {
				getPanel().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				if (broadcastCodes(getAllCodes(), null, colorCode, true)) {
					Logger.getLogger().info("Broadcasted codes " + getCodesSummary(getAllCodes()) + (colorCode != null ? (", Color Code: " + colorCode) : ""));
				} else {
					Logger.getLogger().info("Failed to broadcast codes " + getCodesSummary(getAllCodes()) + (colorCode != null ? (", Color Code: " + colorCode) : ""));
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
		if (specialTag != null) {
			getPanel().getProductSpecTextField().setText(specialTag.getProductSpec());
			getPanel().populateCodePanel(getPanel().getCodePanel(), getPanel().getCodePanelFields(), getPanel().getConfirmButton(), specialTag.getCodes(), false);
			Logger.getLogger().info("User selected special tag: " + specialTag.getTag());
		}
		restartTimeoutTimer();
	}

	private void handleColorCodeSelection(final CodeBroadcastTag colorCodeTag) {
		stopTimeoutTimer();
		checkMessage();
		if (colorCodeTag != null) {
			if (colorCodeTag.getTagType().equals(CodeBroadcastTag.TagType.COLOR_TAG)) {
				getSpecialTagPanel().getProductSpecColorCodeTextField().setText(getPropertyBean().getExclusionCode());
			} else {
				getSpecialTagPanel().getProductSpecColorCodeTextField().setText(colorCodeTag.getProductSpec());
			}
			getPanel().populateCodePanel(getSpecialTagPanel().getColorCodePanel(), getSpecialTagPanel().getColorCodePanelFields(), getPanel().getConfirmButton(), colorCodeTag.getCodes(), false);
			Logger.getLogger().info("User selected color tag: " + colorCodeTag.getTag());
		}
		restartTimeoutTimer();
	}

	private void handleSpecialColorSelection(final CodeBroadcastTag specialColorTag) {
		stopTimeoutTimer();
		checkMessage();
		if (specialColorTag != null) {
			getSpecialTagPanel().getProductSpecSpecialColorTextField().setText(specialColorTag.getProductSpec());
			getPanel().populateCodePanel(getSpecialTagPanel().getSpecialColorPanel(), getSpecialTagPanel().getSpecialColorPanelFields(), getPanel().getConfirmButton(), specialColorTag.getCodes(), false);
			Logger.getLogger().info("User selected special color tag: " + specialColorTag.getTag());
		}
		restartTimeoutTimer();
	}

	private List<CodeBroadcastTag> loadTags(final CodeBroadcastTag.TagType tagType) {
		try {
			List<CodeBroadcastTag> tags = new ArrayList<CodeBroadcastTag>();
			int tagCount = getTagCount(tagType.name());
			for (int i = 1; i <= tagCount; i++) {
				final String tag; {
					String tagAttributeName = concatWithUnderscore(getPropertyBean().getStationName(), tagType.name(), String.valueOf(i));
					BuildAttribute tagAttribute = getSingleBuildAttribute(tagAttributeName);
					tag = tagAttribute.getAttributeValue();
				}
				final String productSpec, productSpecLookup; {
					String productSpecAttributeName = concatWithUnderscore(getPropertyBean().getStationName(), tag);
					BuildAttribute productSpecAttribute = getSingleBuildAttribute(productSpecAttributeName);
					productSpec = productSpecAttribute.getAttributeValue();
					switch (tagType) {
					case SPECIAL_TAG:
						productSpecLookup = productSpecAttribute.getAttributeValue();
						break;
					case COLOR_TAG:
						productSpecLookup = MTOC_WILDCARD + productSpecAttribute.getAttributeValue();
						break;
					case SPECIAL_COLOR:
						productSpecLookup = MTOC_WILDCARD + productSpecAttribute.getAttributeValue();
						break;
					default:
						productSpecLookup = null;
						break;
					}
					if (productSpecLookup == null) {
						getPanel().displayErrorMessage("No product spec defined for " + productSpecAttributeName, true);
					}
				}
				final List<CodeBroadcastCode> codes; {
					switch (tagType) {
					case SPECIAL_TAG:
						codes = getCodesForProductSpec(productSpecLookup, getStationJobCodes());
						break;
					case COLOR_TAG:
						codes = getCodesForProductSpec(productSpecLookup, getPropertyBean().getStationColorCodes());
						break;
					case SPECIAL_COLOR:
						codes = getCodesForProductSpec(productSpecLookup, getPropertyBean().getStationSpecialColorCodes());
						break;
					default:
						codes = null;
						break;
					}
					final String result = validateCodesForProductSpec(codes, productSpecLookup);
					if (!StringUtils.isEmpty(result)) {
						getPanel().displayErrorMessage(result, true);
					}
				}
				CodeBroadcastTag codeBroadcastTag = new CodeBroadcastTag(tagType, tag, productSpec, codes);
				tags.add(codeBroadcastTag);
			}
			return tags;
		} catch (Exception e) {
			getPanel().displayErrorMessage(e.toString(), e);
			return null;
		}
	}



	/*
	 * --------------------------------------------------
	 * Helper/utility methods
	 * --------------------------------------------------
	 */

	public List<CodeBroadcastTag> getColorTagsAndColorCodes() {
		List<CodeBroadcastTag> colorTagsAndColorCodes = null;
		final List<CodeBroadcastTag> colorTags = loadTags(CodeBroadcastTag.TagType.COLOR_TAG);
		final List<CodeBroadcastTag> colorCodes; {
			final List<String> colorCodeStrings = getRecentColorCodes();
			if (colorCodeStrings != null) {
				colorCodes = new ArrayList<CodeBroadcastTag>();
				for (final String colorCodeString : colorCodeStrings) {
					final String productSpecLookup = MTOC_WILDCARD + colorCodeString;
					List<CodeBroadcastCode> codes; {
						codes = getCodesForProductSpec(productSpecLookup, getPropertyBean().getStationColorCodes());
						final String result = validateCodesForProductSpec(codes, productSpecLookup);
						if (!StringUtils.isEmpty(result)) {
							getPanel().displayErrorMessage(result, true);
						}
					}
					colorCodes.add(new CodeBroadcastTag(CodeBroadcastTag.TagType.NONE, colorCodeString, colorCodeString, codes));
				}
			} else {
				colorCodes = null;
			}
		}
		if (colorTags != null) {
			colorTagsAndColorCodes = new ArrayList<CodeBroadcastTag>();
			colorTagsAndColorCodes.addAll(colorTags);
		}
		if (colorCodes != null) {
			if (colorTagsAndColorCodes == null) {
				colorTagsAndColorCodes = new ArrayList<CodeBroadcastTag>();
			}
			colorTagsAndColorCodes.addAll(colorCodes);
		}
		return colorTagsAndColorCodes;
	}

	public List<CodeBroadcastTag> getSpecialColorTags() {
		List<CodeBroadcastTag> specialColorTags = loadTags(CodeBroadcastTag.TagType.SPECIAL_COLOR);
		return specialColorTags;
	}

	public List<String> getRecentColorCodes() {
		if (ProductType.FRAME.equals(getProductType())) {
			final int lastYear = Calendar.getInstance().get(Calendar.YEAR) - 1;
			final List<String> colorCodes = ServiceFactory.getDao(FrameSpecDao.class).findAllColorCodesAfterModelYearDescription(Integer.toString(lastYear));
			if (colorCodes == null || colorCodes.isEmpty()) return null;

			final char[] colorCodeTerminatingCharacters = getPropertyBean().getColorCodeTerminatingCharacters();
			if (colorCodeTerminatingCharacters != null && colorCodeTerminatingCharacters.length > 0) {
				final List<String> simpleColorCodes = new ArrayList<String>();
				for (String colorCode : colorCodes) {
					final String simpleColorCode = getSimpleColorCode(colorCode, colorCodeTerminatingCharacters);
					if (!simpleColorCodes.contains(simpleColorCode)) {
						simpleColorCodes.add(simpleColorCode);
					}
				}
				return simpleColorCodes;
			}
			return colorCodes;
		}
		return null;
	}

	private String getSimpleColorCode(final String colorCode, final char[] colorCodeTerminatingCharacters) {
		if (colorCode == null) return null;
		int endIndex = colorCode.length();
		while (endIndex > 0) {
			final char testChar = colorCode.charAt(endIndex-1);
			if (Character.isDigit(testChar)) {
				break;
			}
			endIndex--;
		}
		if (endIndex < colorCode.length()) {
			final char endChar = colorCode.charAt(endIndex);
			for (char colorCodeTerminatingCharacter : colorCodeTerminatingCharacters) {
				if (endChar == colorCodeTerminatingCharacter) {
					endIndex++;
					break;
				}
			}
		}
		return colorCode.substring(0, endIndex);
	}

	public ItemListener getSpecialTagItemListener(final JRadioButton tagButton, final CodeBroadcastTag specialTag) {
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

	public ItemListener getColorCodeItemListener() {
		return (new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				final CodeBroadcastTag colorCodeTag = (CodeBroadcastTag) event.getItem();
				if (event.getStateChange() == ItemEvent.SELECTED) {
					colorCodeTag.select();
					handleColorCodeSelection(colorCodeTag);
				}
				else if (event.getStateChange() == ItemEvent.DESELECTED) {
					colorCodeTag.deselect();
				}
			}
		});
	}

	public ItemListener getSpecialColorItemListener() {
		return (new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				final CodeBroadcastTag specialColorTag = (CodeBroadcastTag) event.getItem();
				if (event.getStateChange() == ItemEvent.SELECTED) {
					specialColorTag.select();
					handleSpecialColorSelection(specialColorTag);
				}
				else if (event.getStateChange() == ItemEvent.DESELECTED) {
					specialColorTag.deselect();
				}
			}
		});
	}

	private int getTagCount(final String tagName) {
		String tagCountName = concatWithUnderscore(getPropertyBean().getStationName(), tagName, COUNT);
		BuildAttribute tagCountBuildAttribute = getSingleBuildAttribute(tagCountName);
		if (tagCountBuildAttribute == null)
			return 0;
		int tagCount = Integer.valueOf(tagCountBuildAttribute.getAttributeValue());
		return tagCount;
	}

	private CodeBroadcastTag getSelectedSpecialTag() {
		for (CodeBroadcastTag specialTag : getSpecialTags()) {
			if (specialTag.isSelected()) return specialTag;
		}
		return null;
	}

	@Override
	protected boolean isCodesConfirmed() {
		if (isShowSpecialColors()) {
			return isCodesConfirmed(getCodes()) && isCodesConfirmed(getColorCodes()) && isCodesConfirmed(getSpecialColorCodes());
		}
		return isCodesConfirmed(getCodes()) && isCodesConfirmed(getColorCodes());
	}

	@Override
	protected boolean isCodesConfirmed(List<CodeBroadcastCode> codes) {
		if (codes == null || codes.isEmpty()) return false;
		return super.isCodesConfirmed(codes);
	}

	@Override
	protected void unconfirm() {
		unconfirm(getCodes());
		unconfirm(getColorCodes());
		if (isShowSpecialColors()) {
			unconfirm(getSpecialColorCodes());
		}
	}

}

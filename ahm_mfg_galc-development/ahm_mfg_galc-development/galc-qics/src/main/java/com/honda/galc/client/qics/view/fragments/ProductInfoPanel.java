package com.honda.galc.client.qics.view.fragments;

import static com.honda.galc.client.qics.view.constants.ActionId.CANCEL;
import static com.honda.galc.client.qics.view.constants.ActionId.SCRAP;
import static com.honda.galc.client.qics.view.constants.ActionId.SUBMIT;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.product.view.UiFactory;
import com.honda.galc.client.product.view.UiUtils;
import com.honda.galc.client.qics.controller.QicsController;
import com.honda.galc.client.qics.model.ProductModel;
import com.honda.galc.client.qics.view.constants.ActionId;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.data.ProductType;

/**
 * 
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * UI component with product information.
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */

public class ProductInfoPanel extends ProductNumberPanel {

	private static final long serialVersionUID = 1L;

	private Map<String, JTextField> productInfoTextFields;
	protected SubmitButtonsPanel submitButtonsPanel;

	public ProductInfoPanel(QicsController qicsController) {
		super(qicsController);
	}

	@Override
	protected void initialize() {

		this.productNumberTextField = createProductNumberTextField();
		this.productNumberLabel = createProductNumberLabel();
		this.submitButtonsPanel = new SubmitButtonsPanel(getSubmitButtonCodes());

		this.productInfoTextFields = new LinkedHashMap<String, JTextField>();

		getSubmitButtonsPanel().setLayout(new MigLayout("insets 0", "[130!,fill]", "[50,fill]"));

		setSize(990, 90);
		setLayout(new MigLayout("insets 0", "[70::][max,fill][]5", ""));
		add(getProductNumberLabel(), "alignx right");
		add(getProductNumberTextField(), "");
		add(getSubmitButtonsPanel(), "height 50, wrap");

		JPanel propertyPanel = new JPanel(new MigLayout("insets 0", "[70::][fill]"));
		Map<String, String> propertiesInfo = getProductInfoProperties();
		for (String propertyName : propertiesInfo.keySet()) {
			String label = propertiesInfo.get(propertyName);
			JTextField field = UiFactory.createTextField(Fonts.DIALOG_PLAIN_18, TextFieldState.READ_ONLY);
			getProductInfoTextFields().put(propertyName, field);
			if (StringUtils.isNotBlank(label)) {
				propertyPanel.add(UiFactory.createLabel(label, Fonts.DIALOG_PLAIN_18), "gapleft 10, alignx right");
			}
			propertyPanel.add(field, "width 100::300");
		}
		add(propertyPanel, "span");
	}

	// === factory methods === //
	@Override
	protected JLabel createProductNumberLabel() {
		JLabel label = super.createProductNumberLabel();
		String txt = getQicsController().getProductTypeData().getProductIdLabel();
		if (StringUtils.isNotBlank(txt)) {
			label.setText(txt);
		}
		return label;
	}

	@Override
	protected UpperCaseFieldBean createProductNumberTextField() {
		UpperCaseFieldBean textField = super.createProductNumberTextField();
		TextFieldState.READ_ONLY.setState(textField);
		return textField;
	}

	// === controlling api === //
	public void resetPanel(ProductModel productModel) {
		getProductNumberTextField().setText("");
		UiUtils.setText(getProductInfoTextFields().values(), "");
		if (productModel == null) {
			return;
		}
		getProductNumberTextField().setText(productModel.getProductId() == null ? "" : productModel.getProductId());
		UiUtils.setText(getProductInfoTextFields(), productModel.getProduct());
	}

	// === get/set === //
	protected Map<String, String> getProductInfoProperties() {
		String[] props = getQicsController().getQicsPropertyBean().getProductInfoProperties();
		if (props != null && props.length > 0) {
			return getProductInfoProperties(props);
		}
		if (ProductType.BLOCK.equals(getQicsController().getProductType())) {
			props = new String[] { "dcSerialNumber:DC", "mcSerialNumber:MC" };
		} else if (ProductType.HEAD.equals(getQicsController().getProductType())) {
			props = new String[] { "dcSerialNumber:DC", "mcSerialNumber:MC" };
		} else if (ProductType.FRAME.equals(getQicsController().getProductType())) {
			props = new String[] { "productSpecCode:MTO", "productionLot:Lot", "kdLotNumber:Lot KD" };
		} else if (ProductType.ENGINE.equals(getQicsController().getProductType())) {
			props = new String[] { "productSpecCode:MTO", "productionLot:Lot" };
		}
		return getProductInfoProperties(props);
	}

	protected Map<String, String> getProductInfoProperties(String[] properties) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		if (properties == null || properties.length == 0) {
			return map;
		}
		for (String prop : properties) {
			if (StringUtils.isBlank(prop)) {
				continue;
			}
			String[] couple = prop.split(":");
			if (couple == null || couple.length == 0) {
				continue;
			}
			String name = StringUtils.trim(couple[0]);
			String label = null;
			if (couple.length > 1) {
				label = StringUtils.trim(couple[1]);
			}
			map.put(name, label);
		}
		return map;
	}

	protected Collection<ActionId> getSubmitButtonCodes() {
		ActionId[] actions = null;
		if (getQicsController().isProductTypeScrappable()) {
			actions = new ActionId[] { SCRAP, CANCEL, SUBMIT };
		} else {
			actions = new ActionId[] { CANCEL, SUBMIT };
		}
		return Arrays.asList(actions);
	}

	public SubmitButtonsPanel getSubmitButtonsPanel() {
		return submitButtonsPanel;
	}

	protected Map<String, JTextField> getProductInfoTextFields() {
		return productInfoTextFields;
	}
}

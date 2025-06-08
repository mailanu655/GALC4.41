package com.honda.galc.client.product.view.fragments;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.honda.galc.client.product.config.ProductClientConfig;
import com.honda.galc.client.product.config.PropertyDef;
import com.honda.galc.client.product.view.UiFactory;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.util.BeanUtils;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ProductInfoPanel</code> is ... .
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
public class ProductInfoPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final String FIRST_LEFT_ELEMENT_CONSTRAINTS = "[70,right][grow,fill]";
	private static final String ELEMENT_CONSTRAINTS = "[right][grow,fill]";

	private Map<PropertyDef, JTextField> infoTextFields;

	private JButton cancelButton;
	private JButton doneButton;

	public ProductInfoPanel(ProductTypeData productTypeData) {
		this.infoTextFields = new LinkedHashMap<PropertyDef, JTextField>();
		initView(productTypeData);
		mapActions();
	}

	protected void initView(ProductTypeData productTypeData) {
		this.cancelButton = UiFactory.createButton("Cancel", UiFactory.getInfo().getButtonFont(), true);
		this.doneButton = UiFactory.createButton("Done", UiFactory.getInfo().getButtonFont(), true);
		setLayout(new MigLayout("insets 5", "[grow,fill]"));
		getCancelButton().setName("cancelButton");
		getDoneButton().setName("submitButton");
		add(createFirstRowPanel(productTypeData), "wrap");
		add(createSecondRowPanel(productTypeData));
		setBorder(BorderFactory.createEtchedBorder());
	}

	protected void mapActions() {

	}

	// === factory === //
	protected JPanel createFirstRowPanel(ProductTypeData productTypeData) {
		JPanel panel = createRowPanel(ProductClientConfig.getProductInfoFirstRow(productTypeData), "", "10[150,fill][150,fill]", UiFactory.getInfo());
		panel.add(getCancelButton());
		panel.add(getDoneButton());
		return panel;
	}

	protected JPanel createSecondRowPanel(ProductTypeData productTypeData) {
		JPanel panel = createRowPanel(ProductClientConfig.getProductInfoSecondRow(productTypeData), "", "", UiFactory.getInfoSmall());
		return panel;
	}

	protected JPanel createRowPanel(List<PropertyDef> list, String preConstraint, String postConstraint, UiFactory uiFactory) {

		StringBuffer sb = new StringBuffer();
		JPanel panel = new JPanel();
		for (PropertyDef def : list) {
			if (sb.length() > 0) {
				sb.append(10);
				sb.append(ELEMENT_CONSTRAINTS);
			} else {
				sb.append(FIRST_LEFT_ELEMENT_CONSTRAINTS);
			}
			JTextField field = UiFactory.createTextField(uiFactory.getInputFont(), TextFieldState.READ_ONLY);
			getInfoTextFields().put(def, field);
			panel.add(UiFactory.createLabel(def.getHeader(), uiFactory.getLabelFont()));
			panel.add(field);
		}
		StringBuilder constraints = new StringBuilder();
		if (preConstraint != null) {
			constraints.append(preConstraint);
		}
		constraints.append(sb.toString());
		if (postConstraint != null) {
			constraints.append(postConstraint);
		}
		panel.setLayout(new MigLayout("insets 0", constraints.toString()));
		return panel;
	}

	// ===get/set === //
	public void setInfo(Map<String, ?> model) {
		for (PropertyDef def : getInfoTextFields().keySet()) {
			Object value = BeanUtils.getNestedPropertyValue(model, def.getPropertyPath());
			String str = def.toString(value);
			JTextField field = getInfoTextFields().get(def);
			field.setRequestFocusEnabled(false);
			field.setText(str);
		}
	}

	public JButton getCancelButton() {
		return cancelButton;
	}

	public JButton getDoneButton() {
		return doneButton;
	}

	protected Map<PropertyDef, JTextField> getInfoTextFields() {
		return infoTextFields;
	}
}

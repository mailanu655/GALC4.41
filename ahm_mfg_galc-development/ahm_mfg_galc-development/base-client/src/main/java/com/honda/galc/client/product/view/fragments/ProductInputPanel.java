package com.honda.galc.client.product.view.fragments;

import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.honda.galc.client.product.view.UiFactory;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.entity.product.ProductTypeData;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ProductInputPanel</code> is ... .
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
public class ProductInputPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTextField numberTextField;

	public ProductInputPanel(ProductTypeData productTypeData) {
		initView(productTypeData);
		mapActions();
	}

	protected void initView(ProductTypeData productTypeData) {

		List<ProductNumberDef> list = productTypeData.getProductNumberDefs();
		int length = ProductNumberDef.getMaxLength(list);
		if (length < 1) {
			length = 17;
		}
		String label = productTypeData.getProductIdLabel();
		this.numberTextField = UiFactory.createTextField(length, UiFactory.getIdle().getInputFont(), TextFieldState.EDIT);
		getNumberTextField().setRequestFocusEnabled(true);
		getNumberTextField().setName("inputNumberTextField");
		setLayout(new MigLayout("", "[][grow,fill]"));
		add(UiFactory.createLabel(label, UiFactory.getIdle().getLabelFont()));
		add(getNumberTextField());
		setBorder(BorderFactory.createEtchedBorder());
	}

	protected void mapActions() {

	}

	// === get/set === //
	public JTextField getNumberTextField() {
		return numberTextField;
	}
}

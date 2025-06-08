package com.honda.galc.client.teamleader.hold;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.border.Border;

import com.honda.galc.client.teamleader.hold.config.Config;
import com.honda.galc.client.teamleader.hold.qsr.release.ReleasePanel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.PropertyComboBoxRenderer;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.Division;

import net.miginfocom.swing.MigLayout;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>InputPanel</code> is ... .
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
public class InputPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private HoldPanel parentPanel;
	private JPanel departmentPanel;
	private JPanel dptRadButtonPanel;
	private JRadioButton originDptRadButton;
	private JRadioButton respDptRadButton;
	private ButtonGroup dptRadButtonGroup;
	private LabeledComboBox departmentElement;
	private LabeledComboBox productTypeElement;
	public static final String ORIGIN_DPT_TEXT = "Originating Dpt";
	public static final String RESP_DPT_TEXT = "Responsible Dpt";

	public InputPanel(HoldPanel parentPanel) {
		this.parentPanel = parentPanel;
		initView();
		mapActions();
		initModel();
	}

	protected void initView() {
		setLayout(new MigLayout());
		if (this.parentPanel instanceof ReleasePanel) {
			add(this.getDepartmentPanel());
		} else { 
			add(this.getDepartmentElement());
		}
		add(this.getProductTypeElement());
	}

	protected void mapActions() {
		this.getOriginDptRadButton().addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				getDepartmentComboBox().setSelectedIndex(-1);
			}
		});
		
		this.getRespDptRadButton().addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				getDepartmentComboBox().setSelectedIndex(-1);
			}
		});
		
		getDepartmentComboBox().addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent e) {

				getParentPanel().getMainWindow().clearMessage();
				getParentPanel().getProductPanel().removeData();
				getProductTypeComboBox().removeAllItems();

				Division division = (Division) getDepartmentComboBox().getSelectedItem();
				List<ProductType> productTypes = selectProductTypes(division);

				if (productTypes != null && !productTypes.isEmpty()) {
					getProductTypeComboBox().setModel(new DefaultComboBoxModel<ProductType>(new Vector<ProductType>(productTypes)));
					getProductTypeComboBox().setSelectedIndex(0);
				}
			}
		});

		getProductTypeComboBox().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getParentPanel().getMainWindow().clearMessage();
				getParentPanel().getProductPanel().removeData();
				getParentPanel().setProductPanelColumns();
			}
		});
	}

	@SuppressWarnings("unchecked")
	protected void initModel() {
		List<Division> divisions = selectDivisions();
		getDepartmentComboBox().setModel(new DefaultComboBoxModel<Division>(new Vector<Division>(divisions)));
	}

	protected List<Division> selectDivisions() {
		return Config.getInstance(parentPanel.getApplicationId()).getDivisions();
	}

	protected List<ProductType> selectProductTypes(Division division) {
		return Config.getInstance(parentPanel.getApplicationId()).getProductTypes(division);
	}
	
	private JPanel getDepartmentPanel() {
		if (this.departmentPanel == null) {
			this.departmentPanel = new JPanel(new MigLayout());
			this.departmentPanel.add(this.getDptRadButtonPanel(),"gap right 5");
			this.departmentPanel.add(this.getDepartmentComboBox(),"width 95::");
		}
		return this.departmentPanel;
	}
	
	private JPanel getDptRadButtonPanel() {	
		if (this.dptRadButtonPanel == null) {
			this.dptRadButtonGroup = new ButtonGroup();
			this.dptRadButtonGroup.add(this.getOriginDptRadButton());
			this.dptRadButtonGroup.add(this.getRespDptRadButton());
			this.dptRadButtonGroup.setSelected(this.getOriginDptRadButton().getModel(), true);
			
			this.dptRadButtonPanel = new JPanel(new MigLayout("insets 0"));
			this.dptRadButtonPanel.add(this.getOriginDptRadButton(),"wrap");
			this.dptRadButtonPanel.add(this.getRespDptRadButton());
			this.dptRadButtonPanel.setVisible(true);
		}
		return this.dptRadButtonPanel;
	}

	@SuppressWarnings("unchecked")
	protected LabeledComboBox getDepartmentElement() {
		if (this.departmentElement == null) {
			this.departmentElement = new LabeledComboBox("Originating Dpt");
			this.departmentElement.setSize(190, 50);
			this.departmentElement.getComponent().setRenderer(new PropertyComboBoxRenderer<Division>(Division.class, "divisionName"));
			this.departmentElement.setFont(Fonts.DIALOG_BOLD_12);
		}
		return this.departmentElement;
	}

	@SuppressWarnings("unchecked")
	protected LabeledComboBox getProductTypeElement() {
		if (this.productTypeElement == null) {
			this.productTypeElement = new LabeledComboBox("Product Type");
			this.productTypeElement.setSize(190, 50);
			this.productTypeElement.getComponent().setRenderer(new PropertyComboBoxRenderer<ProductType>(ProductType.class, "productName"));
			this.productTypeElement.setFont(Fonts.DIALOG_BOLD_12);
		}
		return this.productTypeElement;
	}

	// === get/set === //
	public JButton getCommandButton() {
		return null;
	}
	
	public JRadioButton getOriginDptRadButton() {
		if (this.originDptRadButton == null) {
			this.originDptRadButton = new JRadioButton(InputPanel.ORIGIN_DPT_TEXT);
			this.originDptRadButton.setActionCommand(InputPanel.ORIGIN_DPT_TEXT);
			this.originDptRadButton.setBorder(null);
		}
		return this.originDptRadButton;
	}
	
	public JRadioButton getRespDptRadButton() {
		if (this.respDptRadButton == null) {
			this.respDptRadButton = new JRadioButton(InputPanel.RESP_DPT_TEXT);
			this.respDptRadButton.setActionCommand(InputPanel.RESP_DPT_TEXT);
			this.respDptRadButton.setBorder(null);
		}
		return this.respDptRadButton;
	}
	
	@SuppressWarnings("rawtypes")
	public JComboBox getDepartmentComboBox() {
		return getDepartmentElement().getComponent();
	}
	
	public String getSelectedDptRadButtonText() {
		if (this.dptRadButtonGroup != null && this.dptRadButtonGroup.getSelection() != null ) {
			return this.dptRadButtonGroup.getSelection().getActionCommand();
		}
		return null;
	}

	public HoldPanel getParentPanel() {
		return parentPanel;
	}

	@SuppressWarnings("rawtypes")
	public JComboBox getProductTypeComboBox() {
		return getProductTypeElement().getComponent();
	}
}
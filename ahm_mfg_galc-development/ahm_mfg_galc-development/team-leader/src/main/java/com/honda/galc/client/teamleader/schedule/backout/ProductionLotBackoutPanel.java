package com.honda.galc.client.teamleader.schedule.backout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.audio.ClientAudioManager;
import com.honda.galc.client.property.AudioPropertyBean;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.MultiValueObject;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.dto.ProductionLotBackout;
import com.honda.galc.service.property.PropertyService;

public class ProductionLotBackoutPanel extends TabbedPanel {

	private static final long serialVersionUID = 1L;
	private static final String CONTROLLER_CLASS = "CONTROLLER_CLASS";
	private static final Font font = Fonts.DIALOG_PLAIN_16;

	private final ClientAudioManager audioManager;
	private ProductionLotBackoutController controller;
	private JPanel productionLotBackoutPanel;
	private JLabel titleLabel;
	private JLabel lotPrefixLabel;
	private JLabel lotDateLabel;
	private JTextField lotPrefixTextField;
	private JTextField lotDateTextField;
	private JButton deleteButton;
	private ObjectTablePane<MultiValueObject<ProductionLotBackout>> productionLotBackoutTable;

	public ProductionLotBackoutPanel(TabbedMainWindow mainWindow) {
		super("Production Lot Backout", KeyEvent.VK_B, mainWindow);
		this.audioManager = new ClientAudioManager(PropertyService.getPropertyBean(AudioPropertyBean.class));
		controller = createClientController();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() { initComponents(); }
		});
	}

	private ClientAudioManager getAudioManager() { return this.audioManager; }
	public ProductionLotBackoutController getController() { return controller; }
	public void setController(ProductionLotBackoutController controller) { this.controller = controller; }

	public JPanel getPanel() {
		if (this.productionLotBackoutPanel == null) {
			this.productionLotBackoutPanel = new JPanel();
			this.productionLotBackoutPanel.setLayout(new GridBagLayout());
		}
		return this.productionLotBackoutPanel;
	}
	public JLabel getTitleLabel() {
		if (this.titleLabel == null) {
			this.titleLabel = new JLabel(getMainWindow().getProductType().getProductName() + " Production Lot Backout");
			this.titleLabel.setFont(font);
		}
		return this.titleLabel;
	}
	public JLabel getLotPrefixLabel() {
		if (this.lotPrefixLabel == null) {
			this.lotPrefixLabel = new JLabel("Lot Prefix:");
			this.lotPrefixLabel.setFont(font);
			this.lotPrefixLabel.setLabelFor(getLotPrefixTextField());
		}
		return this.lotPrefixLabel;
	}
	public JLabel getLotDateLabel() {
		if (this.lotDateLabel == null) {
			this.lotDateLabel = new JLabel("Lot Date:");
			this.lotDateLabel.setFont(font);
			this.lotDateLabel.setLabelFor(getLotDateTextField());
		}
		return this.lotDateLabel;
	}
	public JTextField getLotPrefixTextField() {
		if (this.lotPrefixTextField == null) {
			this.lotPrefixTextField = new JTextField(20);
			this.lotPrefixTextField.setFont(font);
		}
		return this.lotPrefixTextField;
	}
	public JTextField getLotDateTextField() {
		if (this.lotDateTextField == null) {
			this.lotDateTextField = new JTextField(20);
			this.lotDateTextField.setEnabled(false);
			this.lotDateTextField.setFont(font);
		}
		return this.lotDateTextField;
	}
	public JButton getDeleteButton() {
		if (this.deleteButton == null) {
			this.deleteButton = new JButton("Delete");
			this.deleteButton.setFocusable(false);
			this.deleteButton.setFont(font);
		}
		return this.deleteButton;
	}
	public ObjectTablePane<MultiValueObject<ProductionLotBackout>> getTable() {
		if (this.productionLotBackoutTable == null) {
			ColumnMappings columnMappings = ColumnMappings.with(new String[] { "Table", "Rows", "Info" });
			this.productionLotBackoutTable = new ObjectTablePane<MultiValueObject<ProductionLotBackout>>(columnMappings.get());
			this.productionLotBackoutTable.setAlignment(SwingConstants.CENTER);
			this.productionLotBackoutTable.setFocusable(false);
			this.productionLotBackoutTable.getTable().setBackground(new Color(238, 238, 238));
			this.productionLotBackoutTable.getTable().setFocusable(false);
			this.productionLotBackoutTable.getTable().setFont(font);
			this.productionLotBackoutTable.getTable().setRowSelectionAllowed(false);
			this.productionLotBackoutTable.getTable().setShowGrid(false);
		}
		return this.productionLotBackoutTable;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private ProductionLotBackoutController createClientController() {
		String controllerClz = PropertyService.getProperty(getProcessPointId(), CONTROLLER_CLASS);
		if(StringUtils.isEmpty(controllerClz) || controllerClz.equals(ProductionLotBackoutController.class.getName())) {
			return new ProductionLotBackoutController(this);
		} else {
			try {
				Class<? extends ProductionLotBackoutController> forName = (Class<? extends ProductionLotBackoutController>) Class.forName(controllerClz);
				Class[] parameterTypes = { ProductionLotBackoutPanel.class };
				Object[] parameters = { this };
				Constructor constructor = forName.getConstructor(parameterTypes);
				return (ProductionLotBackoutController)constructor.newInstance(parameters);

			} catch (Exception e) {
				e.printStackTrace();
				getLogger().error(e);
			}
		}
		return null;
	}

	private void initComponents() {
		final Insets insets = new Insets(8, 8, 8, 8);
		final Insets tfLabelInsets = new Insets(8, 8, 8, 2);
		final Insets tfInsets = new Insets(8, 2, 8, 8);
		getPanel().add(getTitleLabel(), makeGridBagConstraints(0, 0, 5, 1, null, null, null, insets, null, 1.0, null));
		getPanel().add(getLotPrefixLabel(), makeGridBagConstraints(0, 1, 1, 1, null, null, null, tfLabelInsets, null, 0.2, null));
		getPanel().add(getLotPrefixTextField(), makeGridBagConstraints(1, 1, 1, 1, null, null, null, tfInsets, null, 0.2, null));
		getPanel().add(getLotDateLabel(), makeGridBagConstraints(2, 1, 1, 1, null, null, null, tfLabelInsets, null, 0.2, null));
		getPanel().add(getLotDateTextField(), makeGridBagConstraints(3, 1, 1, 1, null, null, null, tfInsets, null, 0.2, null));
		getPanel().add(getDeleteButton(), makeGridBagConstraints(4, 1, 1, 1, null, null, null, insets, null, 0.2, null));
		getPanel().add(getTable(), makeGridBagConstraints(0, 2, 5, 1, GridBagConstraints.HORIZONTAL, null, null, insets, null, 1.0, 1.0));
		add(getPanel(), BorderLayout.CENTER);
	}

	private static GridBagConstraints makeGridBagConstraints(Integer gridx, Integer gridy, Integer gridwidth, Integer gridheight, Integer fill, Integer ipadx, Integer ipady, Insets insets, Integer anchor, Double weightx, Double weighty) {
		GridBagConstraints constraints = new GridBagConstraints();
		if (gridx != null) constraints.gridx = gridx;
		if (gridy != null) constraints.gridy = gridy;
		if (gridwidth != null) constraints.gridwidth = gridwidth;
		if (gridheight != null) constraints.gridheight = gridheight;
		if (fill != null) constraints.fill = fill;
		if (ipadx != null) constraints.ipadx = ipadx;
		if (ipady != null) constraints.ipady = ipady;
		if (insets != null) constraints.insets = insets;
		if (anchor != null) constraints.anchor = anchor;
		if (weightx != null) constraints.weightx = weightx;
		if (weighty != null) constraints.weighty = weighty;
		return constraints;
	}

	public void requestFocusOnLotPrefix() {
		if (getLotPrefixTextField() != null) {
			getLotPrefixTextField().requestFocus();
		}
	}

	public void requestFocusOnLotDate() {
		if (getLotDateTextField() != null) {
			getLotDateTextField().requestFocus();
		}
	}

	public void resetComponents() {
		getLotPrefixTextField().setText(null);
		getLotDateTextField().setText(null);
		getLotPrefixTextField().setEnabled(true);
		getLotDateTextField().setEnabled(false);
		getDeleteButton().setEnabled(false);
		clearProductionLotBackoutTable();
		requestFocusOnLotPrefix();
	}

	public void clearProductionLotBackoutTable() {
		List<MultiValueObject<ProductionLotBackout>> clear = new ArrayList<MultiValueObject<ProductionLotBackout>>();
		getTable().reloadData(clear);
	}

	public void clearMessage() {
		getMainWindow().clearStatusMessage();
		getMainWindow().clearMessage();
	}

	public void displayMessage(String message) {
		try { getAudioManager().playNGSound(); } catch (Exception e) { getLogger().error(e); }
		getMainWindow().setMessage(message);
	}

	public void displayErrorMessage(String errorMessage) {
		try { getAudioManager().playNGSound(); } catch (Exception e) { getLogger().error(e); }
		getMainWindow().setErrorMessage(errorMessage);
	}

	public void actionPerformed(ActionEvent e) {}

	@Override
	public void onTabSelected() {}

}

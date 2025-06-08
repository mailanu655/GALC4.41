package com.honda.galc.client.teamleader.bearing;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.product.view.UiFactory;
import com.honda.galc.dao.product.bearing.BearingPartByYearModelDao;
import com.honda.galc.dao.product.bearing.BearingPartDao;
import com.honda.galc.entity.bearing.BearingPart;
import com.honda.galc.entity.bearing.BearingPartByYearModel;
import com.honda.galc.service.ServiceFactory;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>BearingDialog</code> is ... .
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
 * @created May 14, 2013
 */
public class BearingDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private BearingPart bearingPart;
	private String[] bearingTypes;
	private Map<String, Color> bearingColors;

	private JTextField bearingNumberTextField;
	private JComboBox bearingTypeComboBox;
	private JComboBox bearingColorComboBox;

	private JButton cancelButton;
	private JButton submitButton;

	private boolean updated;

	public BearingDialog(String[] bearingTypes, Map<String, Color> bearingColors) {
		setTitle("Update Bearing");
		this.bearingTypes = bearingTypes;
		this.bearingColors = bearingColors;
		initView();
		mapActions();
		initData();
	}

	public BearingDialog(BearingPart bearingPart, String[] bearingTypes, Map<String, Color> bearingColors) {
		setTitle("Update Bearing");
		this.bearingPart = bearingPart;
		this.bearingTypes = bearingTypes;
		this.bearingColors = bearingColors;
		initView();
		mapActions();
		initData();
	}

	protected void initView() {
		setSize(650, 180);
		setLayout(new MigLayout("", "[max,fill][150!,fill][150!,fill]", "30[][]"));
		UiFactory factory = UiFactory.getInputSmall();
		this.bearingNumberTextField = factory.createTextField(30, null);
		this.bearingTypeComboBox = new JComboBox();
		this.bearingColorComboBox = new JComboBox();
		this.cancelButton = new JButton("Cancel");
		this.submitButton = new JButton("Submit");

		if (getBearingPart() != null) {
			getBearingNumberTextField().setEditable(false);
			getSubmitButton().setEnabled(false);
		}

		getBearingTypeComboBox().setFont(getBearingNumberTextField().getFont());
		getBearingColorComboBox().setFont(getBearingNumberTextField().getFont());
		getCancelButton().setFont(getBearingNumberTextField().getFont());
		getSubmitButton().setFont(getBearingNumberTextField().getFont());

		add(factory.createLabel("Bearing Number"));
		add(factory.createLabel("Bearing Type"));
		add(factory.createLabel("Bearing Color"), "wrap");

		add(getBearingNumberTextField());
		add(getBearingTypeComboBox());
		add(getBearingColorComboBox(), "wrap 15");

		add(getCancelButton(), new CC().cell(1, 2));
		add(getSubmitButton(), new CC().cell(2, 2));

	}

	protected void mapActions() {
		getCancelButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		getSubmitButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean processed = false;
				if (getBearingPart() == null) {
					processed = createBearingPart();
				} else {
					processed = updateBearingPart();
				}
				if (processed) {
					dispose();
				}
			}
		});
		ActionListener inputListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (getBearingPart() != null) {
					String bearingType = (String) getBearingTypeComboBox().getSelectedItem();
					String color = (String) getBearingColorComboBox().getSelectedItem();

					if (getBearingPart().getType().equals(bearingType) && getBearingPart().getColor().equals(color)) {
						getSubmitButton().setEnabled(false);
					} else {
						getSubmitButton().setEnabled(true);
					}
				}
			}
		};
		getBearingTypeComboBox().addActionListener(inputListener);
		getBearingColorComboBox().addActionListener(inputListener);
	}

	protected void initData() {
		if (getBearingTypes() != null) {
			getBearingTypeComboBox().setModel(new DefaultComboBoxModel(getBearingTypes()));
		}
		if (getBearingColors() != null) {
			getBearingColorComboBox().setModel(new DefaultComboBoxModel(new Vector<String>(getBearingColors().keySet())));

		}

		if (getBearingPart() != null) {
			getBearingNumberTextField().setText(getBearingPart().getId());
			getBearingTypeComboBox().setSelectedItem(getBearingPart().getType());
			getBearingColorComboBox().setSelectedItem(getBearingPart().getColor());
		}

	}

	// === bl === //
	protected boolean updateBearingPart() {
		String title = "Update Bearing";

		String bearingType = (String) getBearingTypeComboBox().getSelectedItem();
		String color = (String) getBearingColorComboBox().getSelectedItem();
		List<BearingPartByYearModel> list = ServiceFactory.getDao(BearingPartByYearModelDao.class).findPotentialDuplicates(bearingType, color, getBearingPart().getId());
		
		if (list.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (BearingPartByYearModel bym : list) {								
				sb.append("\n").append(bym.getId().getModelYearCode()).append(bym.getId().getModelCode()).append(" : ").append(bym.getId().getBearingSerialNumber());
			}
			String msg = "%s Bearing with color %s is already assigned to: %s. Do you want to continue adding new Bearing with same color?";
			msg = String.format(msg, bearingType, color, sb.toString());
			int response = JOptionPane.showConfirmDialog(this, msg,
					"Confirm", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE);
			if (response == JOptionPane.NO_OPTION) {
				return false;
			} else if (response == JOptionPane.CLOSED_OPTION) {
				return false;
			}
		}

		int retCode = JOptionPane.showConfirmDialog(BearingDialog.this, "Are You sure ?", title, JOptionPane.YES_NO_OPTION);
		if (JOptionPane.YES_OPTION == retCode) {

			getBearingPart().setType(bearingType);
			getBearingPart().setColor(color);
			ServiceFactory.getDao(BearingPartDao.class).update(getBearingPart());
			setUpdated(true);
			return true;
		}
		return false;
	}

	protected boolean createBearingPart() {

		String bearingNumber = getBearingNumberTextField().getText();
		bearingNumber = StringUtils.trim(bearingNumber);
		getBearingNumberTextField().setText(bearingNumber);
		if (StringUtils.isEmpty(bearingNumber)) {
			JOptionPane.showMessageDialog(BearingDialog.this, "Bearing Number can not be empty !", "Title", JOptionPane.ERROR_MESSAGE);
			getBearingNumberTextField().requestFocus();
			return false;
		}
		BearingPart bp = ServiceFactory.getDao(BearingPartDao.class).findByKey(bearingNumber);
		if (bp != null) {
			String msg = String.format("Bearing with Number %s already exists.", bearingNumber);
			JOptionPane.showMessageDialog(this, msg, "Create Bearing", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		String title = "Create Bearing";
		int retCode = JOptionPane.showConfirmDialog(BearingDialog.this, "Are You sure ?", title, JOptionPane.YES_NO_OPTION);

		if (JOptionPane.YES_OPTION == retCode) {
			String bearingType = (String) getBearingTypeComboBox().getSelectedItem();
			String color = (String) getBearingColorComboBox().getSelectedItem();
			BearingPart bearingPart = new BearingPart();
			bearingPart.setId(bearingNumber);
			bearingPart.setType(bearingType);
			bearingPart.setColor(color);

			ServiceFactory.getDao(BearingPartDao.class).update(bearingPart);
			setUpdated(true);
			return true;
		}
		return false;
	}

	// === get/set === //
	public JTextField getBearingNumberTextField() {
		return bearingNumberTextField;
	}

	public JComboBox getBearingTypeComboBox() {
		return bearingTypeComboBox;
	}

	public JComboBox getBearingColorComboBox() {
		return bearingColorComboBox;
	}

	public JButton getCancelButton() {
		return cancelButton;
	}

	public JButton getSubmitButton() {
		return submitButton;
	}

	public BearingPart getBearingPart() {
		return bearingPart;
	}

	public String[] getBearingTypes() {
		return bearingTypes;
	}

	public Map<String, Color> getBearingColors() {
		return bearingColors;
	}

	public boolean isUpdated() {
		return updated;
	}

	public void setUpdated(boolean updated) {
		this.updated = updated;
	}

	protected void setBearingPart(BearingPart bearingPart) {
		this.bearingPart = bearingPart;
	}
}

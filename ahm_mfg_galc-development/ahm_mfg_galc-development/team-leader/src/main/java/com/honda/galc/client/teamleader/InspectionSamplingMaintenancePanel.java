package com.honda.galc.client.teamleader;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import com.honda.galc.client.datacollection.view.NumericFieldBean;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.dao.product.InspectionSamplingDao;
import com.honda.galc.dao.product.RuleDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.InspectionSampling;
import com.honda.galc.entity.product.InspectionSamplingId;
import com.honda.galc.entity.product.Rule;
import com.honda.galc.service.ServiceFactory;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Oct 01, 2019
 */
@SuppressWarnings(value = { "all" })
public class InspectionSamplingMaintenancePanel extends TabbedPanel implements ActionListener, KeyListener, MouseListener {

	private static final long serialVersionUID = 1L;

	private JPanel mainPanel = null;

	private InspectionSamplingTableModel inspectionSamplingTableModel = null;

	private JCheckBox afOffLabelHoldCheckBox = null;

	private JButton addInspectionSamplingButton = null;

	private JButton deleteInspectionSamplingButton = null;

	private JButton updateInspectionSamplingButton = null;

	private JComboBox modelCodeComboBox = null;

	private JComboBox typeCodeComboBox = null;

	private JLabel dimensionSampleSizeSlashLabel = null;

	private JLabel dimensionSamplingSizeLabel = null;

	private JLabel emissionSamplingSlashLabel = null;

	private JLabel emissionSamplingSizeLabel = null;

	private JLabel modelCodeTextLabel = null;

	private JLabel typeCodeTextLabel = null;

	private JLabel noticeLabel = null;

	private NumericFieldBean dimensionSampleSizeDenominatorTextField = null;

	private NumericFieldBean dimensionSampleSizeNumeratorTextField = null;

	private NumericFieldBean emissionSampleSizeDenominatorTextField = null;

	private NumericFieldBean emissionSampleSizeNumeratorTextField = null;

	private TablePane inspectionSamplingTablePane = null;

	private String AF_OFF_LABEL_HOLD_RULE_ID = "AFOffLabelHold";

	private boolean afOFFlabelHoldEnabled = false;

	public InspectionSamplingMaintenancePanel(TabbedMainWindow mainWindow) {
		super("Inspection Sampling Maintenance Panel", KeyEvent.VK_I, mainWindow);
		initialize();
	}

	@Override
	public void onTabSelected() {
		setErrorMessage("");
		Logger.getLogger(getApplicationId()).info("Inspection Sampling Maintenance Panel is selected");
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getAddInspectionSamplingButton())
			addInspectionSampling();
		else if (e.getSource() == getUpdateInspectionSamplingButton())
			updateInspectionSampling();
		else if (e.getSource() == getTypeCodeComboBox())
			selectType();
		else if (e.getSource() == getModelCodeComboBox())
			selectModel();
		else if (e.getSource() == getDeleteInspectionSamplingButton())
			deleteInspectionSampling();
		else if (e.getSource() == getAfOffLabelHoldCheckBox())
			updateAfOffLabelHoldRuleState();
	};

	public void keyPressed(KeyEvent e) {
	};

	public void keyReleased(KeyEvent e) {
		if (e.getSource() == getDimensionSampleSizeDenominatorTextField())
			inputDimesionSampleSizeDenominator();
		else if (e.getSource() == getDimensionSampleSizeNumeratorTextField())
			inputDimensionSampleSizeNumerator();
		else if (e.getSource() == getEmissionSampleSizeDenominatorTextField())
			inputEmissionSampleSizeDenominator();
		else if (e.getSource() == getEmissionSampleSizeNumeratorTextField())
			inputEmissionSampleSizeNumerator();
	};

	public void keyTyped(KeyEvent e) {
	};

	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == getInspectionSamplingTablePane().getTable())
			selectUnselectInspectionSampling();
	};

	public void mouseEntered(MouseEvent e) {
	};

	public void mouseExited(MouseEvent e) {
	};

	public void mousePressed(MouseEvent e) {
	};

	public void mouseReleased(MouseEvent e) {
	};

	private JButton getAddInspectionSamplingButton() {
		if (addInspectionSamplingButton == null) {
			addInspectionSamplingButton = new JButton();
			addInspectionSamplingButton.setName("AddInspectionSamplingButton");
			addInspectionSamplingButton.setText("Add");
			addInspectionSamplingButton.setMaximumSize(new Dimension(57, 25));
			addInspectionSamplingButton.setActionCommand("Add");
			addInspectionSamplingButton.setFont(new Font("dialog", 0, 18));
			addInspectionSamplingButton.setBounds(193, 580, 104, 25);
			addInspectionSamplingButton.setMinimumSize(new Dimension(57, 25));
		}
		return addInspectionSamplingButton;
	}

	private JButton getDeleteInspectionSamplingButton() {
		if (deleteInspectionSamplingButton == null) {
			deleteInspectionSamplingButton = new JButton();
			deleteInspectionSamplingButton.setName("DeleteInspectionSamplingButton");
			deleteInspectionSamplingButton.setText("Delete");
			deleteInspectionSamplingButton.setMaximumSize(new Dimension(71, 25));
			deleteInspectionSamplingButton.setActionCommand("Delete");
			deleteInspectionSamplingButton.setFont(new Font("dialog", 0, 18));
			deleteInspectionSamplingButton.setBounds(752, 580, 104, 25);
			deleteInspectionSamplingButton.setMinimumSize(new Dimension(71, 25));
		}
		return deleteInspectionSamplingButton;
	}

	private JButton getUpdateInspectionSamplingButton() {
		if (updateInspectionSamplingButton == null) {
			updateInspectionSamplingButton = new JButton();
			updateInspectionSamplingButton.setName("UpdateInspectionSamplingButton");
			updateInspectionSamplingButton.setText("Update");
			updateInspectionSamplingButton.setMaximumSize(new Dimension(75, 25));
			updateInspectionSamplingButton.setActionCommand("Update");
			updateInspectionSamplingButton.setFont(new Font("dialog", 0, 18));
			updateInspectionSamplingButton.setBounds(475, 580, 104, 25);
			updateInspectionSamplingButton.setMinimumSize(new Dimension(75, 25));
		}
		return updateInspectionSamplingButton;
	}

	private JComboBox getModelCodeComboBox() {
		if (modelCodeComboBox == null) {
			modelCodeComboBox = new JComboBox();
			modelCodeComboBox.setName("ModelCodeComboBox");
			modelCodeComboBox.setBackground(Color.white);
			modelCodeComboBox.setForeground(Color.black);
			modelCodeComboBox.setFont(new Font("dialog", 0, 14));
			modelCodeComboBox.setBounds(158, 529, 97, 25);
		}
		return modelCodeComboBox;
	}

	private JComboBox getTypeCodeComboBox() {
		if (typeCodeComboBox == null) {
			typeCodeComboBox = new JComboBox();
			typeCodeComboBox.setName("TypeCodeComboBox");
			typeCodeComboBox.setBackground(Color.white);
			typeCodeComboBox.setForeground(Color.black);
			typeCodeComboBox.setFont(new Font("dialog", 0, 14));
			typeCodeComboBox.setBounds(283, 529, 97, 25);
		}
		return typeCodeComboBox;
	}

	private JLabel getDimensionSampleSizeSlashLabel() {
		if (dimensionSampleSizeSlashLabel == null) {
			dimensionSampleSizeSlashLabel = new JLabel();
			dimensionSampleSizeSlashLabel.setName("DimensionSampleSizeSlashLabel");
			dimensionSampleSizeSlashLabel.setText("/");
			dimensionSampleSizeSlashLabel.setMaximumSize(new Dimension(3, 14));
			dimensionSampleSizeSlashLabel.setForeground(new Color(0, 0, 0));
			dimensionSampleSizeSlashLabel.setAlignmentX(0.5F);
			dimensionSampleSizeSlashLabel.setFont(new Font("Arial", 1, 18));
			dimensionSampleSizeSlashLabel.setBounds(797, 532, 6, 30);
			dimensionSampleSizeSlashLabel.setMinimumSize(new Dimension(3, 14));
		}
		return dimensionSampleSizeSlashLabel;
	}

	private JLabel getDimensionSamplingSizeLabel() {
		if (dimensionSamplingSizeLabel == null) {
			dimensionSamplingSizeLabel = new JLabel();
			dimensionSamplingSizeLabel.setName("DimensionSamplingSizeLabel");
			dimensionSamplingSizeLabel.setText("Spec Check Test Size");
			dimensionSamplingSizeLabel.setMaximumSize(new Dimension(143, 14));
			dimensionSamplingSizeLabel.setForeground(new Color(0, 0, 0));
			dimensionSamplingSizeLabel.setFont(new Font("dialog", 0, 18));
			dimensionSamplingSizeLabel.setBounds(719, 501, 250, 28);
			dimensionSamplingSizeLabel.setMinimumSize(new Dimension(143, 14));
		}
		return dimensionSamplingSizeLabel;
	}

	private JLabel getEmissionSamplingSlashLabel() {
		if (emissionSamplingSlashLabel == null) {
			emissionSamplingSlashLabel = new JLabel();
			emissionSamplingSlashLabel.setName("EmissionSamplingSlashLabel");
			emissionSamplingSlashLabel.setText("/");
			emissionSamplingSlashLabel.setMaximumSize(new Dimension(3, 14));
			emissionSamplingSlashLabel.setForeground(new Color(0, 0, 0));
			emissionSamplingSlashLabel.setAlignmentX(0.5F);
			emissionSamplingSlashLabel.setFont(new Font("Arial", 1, 18));
			emissionSamplingSlashLabel.setBounds(527, 532, 10, 30);
			emissionSamplingSlashLabel.setMinimumSize(new Dimension(3, 14));
		}
		return emissionSamplingSlashLabel;
	}

	private JLabel getEmissionSamplingSizeLabel() {
		if (emissionSamplingSizeLabel == null) {
			emissionSamplingSizeLabel = new JLabel();
			emissionSamplingSizeLabel.setName("EmissionSamplingSizeLabel");
			emissionSamplingSizeLabel.setText("Sampling Test Size");
			emissionSamplingSizeLabel.setMaximumSize(new Dimension(135, 14));
			emissionSamplingSizeLabel.setForeground(new Color(0, 0, 0));
			emissionSamplingSizeLabel.setFont(new Font("dialog", 0, 18));
			emissionSamplingSizeLabel.setBounds(440, 501, 211, 28);
			emissionSamplingSizeLabel.setMinimumSize(new Dimension(135, 14));
		}
		return emissionSamplingSizeLabel;
	}

	private JLabel getModelCodeTextLabel() {
		if (modelCodeTextLabel == null) {
			modelCodeTextLabel = new JLabel();
			modelCodeTextLabel.setName("ModelCodeTextLabel");
			modelCodeTextLabel.setText("MODEL");
			modelCodeTextLabel.setMaximumSize(new Dimension(41, 14));
			modelCodeTextLabel.setForeground(new Color(0, 0, 0));
			modelCodeTextLabel.setFont(new Font("dialog", 0, 18));
			modelCodeTextLabel.setBounds(163, 501, 97, 28);
			modelCodeTextLabel.setMinimumSize(new Dimension(41, 14));
		}
		return modelCodeTextLabel;
	}

	private JLabel getNoticeLabel() {
		if (noticeLabel == null) {
			noticeLabel = new JLabel();
			noticeLabel.setName("NoticeLabel");
			noticeLabel.setFont(new Font("dialog", 1, 18));
			noticeLabel.setText("NOTE: If you close without saving, your work will be lost!");
			noticeLabel.setBounds(250, 614, 523, 24);
			noticeLabel.setForeground(Color.black);
		}
		return noticeLabel;
	}

	private JLabel getTypeCodeLabel() {
		if (typeCodeTextLabel == null) {
			typeCodeTextLabel = new JLabel();
			typeCodeTextLabel.setName("TypeCodeTextLabel");
			typeCodeTextLabel.setText("TYPE");
			typeCodeTextLabel.setMaximumSize(new Dimension(29, 14));
			typeCodeTextLabel.setForeground(new Color(0, 0, 0));
			typeCodeTextLabel.setFont(new Font("dialog", 0, 18));
			typeCodeTextLabel.setBounds(288, 501, 100, 28);
			typeCodeTextLabel.setMinimumSize(new Dimension(29, 14));
		}
		return typeCodeTextLabel;
	}

	private JCheckBox getAfOffLabelHoldCheckBox() {
		if (afOffLabelHoldCheckBox == null) {
			afOffLabelHoldCheckBox = new JCheckBox();
			afOffLabelHoldCheckBox.setName("AfOffLabelHoldCheckBox");
			afOffLabelHoldCheckBox.setText("AF-Off Label Hold Enabled");
			afOffLabelHoldCheckBox.setForeground(new Color(0, 0, 0));
			afOffLabelHoldCheckBox.setFont(new Font("dialog", 0, 18));
			afOffLabelHoldCheckBox.setBounds(129, 20, 300, 28);
			afOffLabelHoldCheckBox.setSelected(true);
		}
		return afOffLabelHoldCheckBox;
	}

	private TablePane getInspectionSamplingTablePane() {

		if (inspectionSamplingTablePane == null) {
			inspectionSamplingTablePane = new TablePane("Inspection Sampling Table");
			inspectionSamplingTablePane.setBounds(133, 66, 812, 419);
			inspectionSamplingTablePane.getTable().setSelectionBackground(Color.CYAN);
			inspectionSamplingTablePane.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			inspectionSamplingTableModel = new InspectionSamplingTableModel(inspectionSamplingTablePane.getTable(),new ArrayList<InspectionSampling>());
		}
		return inspectionSamplingTablePane;
	}

	private NumericFieldBean getDimensionSampleSizeDenominatorTextField() {
		if (dimensionSampleSizeDenominatorTextField == null) {
			dimensionSampleSizeDenominatorTextField = new NumericFieldBean();
			dimensionSampleSizeDenominatorTextField.setName("DimensionSampleSizeDenominatorTextField");
			dimensionSampleSizeDenominatorTextField.setHighlighter(new javax.swing.plaf.basic.BasicTextUI.BasicHighlighter());
			dimensionSampleSizeDenominatorTextField.setBackground(Color.white);
			dimensionSampleSizeDenominatorTextField.setCaretColor(Color.black);
			dimensionSampleSizeDenominatorTextField.setForeground(Color.black);
			dimensionSampleSizeDenominatorTextField.setMaximumLength(4);
			dimensionSampleSizeDenominatorTextField.setFont(new Font("dialog", 0, 14));
			dimensionSampleSizeDenominatorTextField.setColor(Color.white);
			dimensionSampleSizeDenominatorTextField.setBounds(805, 532, 68, 25);
		}
		return dimensionSampleSizeDenominatorTextField;
	}

	private NumericFieldBean getDimensionSampleSizeNumeratorTextField() {
		if (dimensionSampleSizeNumeratorTextField == null) {
			dimensionSampleSizeNumeratorTextField = new NumericFieldBean();
			dimensionSampleSizeNumeratorTextField.setName("DimensionSamplingSizeNumeratorTextField");
			dimensionSampleSizeNumeratorTextField.setHighlighter(new javax.swing.plaf.basic.BasicTextUI.BasicHighlighter());
			dimensionSampleSizeNumeratorTextField.setBackground(Color.white);
			dimensionSampleSizeNumeratorTextField.setCaretColor(Color.black);
			dimensionSampleSizeNumeratorTextField.setForeground(Color.black);
			dimensionSampleSizeNumeratorTextField.setMaximumLength(4);
			dimensionSampleSizeNumeratorTextField.setFont(new Font("dialog", 0, 14));
			dimensionSampleSizeNumeratorTextField.setColor(Color.white);
			dimensionSampleSizeNumeratorTextField.setBounds(728, 532, 68, 25);
		}
		return dimensionSampleSizeNumeratorTextField;
	}

	private NumericFieldBean getEmissionSampleSizeDenominatorTextField() {
		if (emissionSampleSizeDenominatorTextField == null) {
			emissionSampleSizeDenominatorTextField = new NumericFieldBean();
			emissionSampleSizeDenominatorTextField.setName("EmissionSampleSizeDenominatorTextField");
			emissionSampleSizeDenominatorTextField.setHighlighter(new javax.swing.plaf.basic.BasicTextUI.BasicHighlighter());
			emissionSampleSizeDenominatorTextField.setBackground(Color.white);
			emissionSampleSizeDenominatorTextField.setCaretColor(Color.black);
			emissionSampleSizeDenominatorTextField.setForeground(Color.black);
			emissionSampleSizeDenominatorTextField.setMaximumLength(4);
			emissionSampleSizeDenominatorTextField.setFont(new Font("dialog", 0, 14));
			emissionSampleSizeDenominatorTextField.setColor(Color.white);
			emissionSampleSizeDenominatorTextField.setBounds(535, 532, 68, 25);
		}
		return emissionSampleSizeDenominatorTextField;
	}

	private NumericFieldBean getEmissionSampleSizeNumeratorTextField() {
		if (emissionSampleSizeNumeratorTextField == null) {
			emissionSampleSizeNumeratorTextField = new NumericFieldBean();
			emissionSampleSizeNumeratorTextField.setName("EmissionSampleSizeNumeratorTextField");
			emissionSampleSizeNumeratorTextField.setHighlighter(new javax.swing.plaf.basic.BasicTextUI.BasicHighlighter());
			emissionSampleSizeNumeratorTextField.setBackground(Color.white);
			emissionSampleSizeNumeratorTextField.setCaretColor(Color.black);
			emissionSampleSizeNumeratorTextField.setForeground(Color.black);
			emissionSampleSizeNumeratorTextField.setMaximumLength(4);
			emissionSampleSizeNumeratorTextField.setFont(new Font("dialog", 0, 14));
			emissionSampleSizeNumeratorTextField.setColor(Color.white);
			emissionSampleSizeNumeratorTextField.setBounds(458, 532, 68, 25);
		}
		return emissionSampleSizeNumeratorTextField;
	}

	private InspectionSamplingTableModel getInspectionSamplingTableModel() {
		return inspectionSamplingTableModel;
	}

	private JPanel getMainPanel() {
		if (mainPanel == null) {
			try {
				mainPanel = new JPanel();
				mainPanel.setName("mainPanel");
				mainPanel.setLayout(null);
				mainPanel.add(getAfOffLabelHoldCheckBox(), getAfOffLabelHoldCheckBox().getName());
				mainPanel.add(getInspectionSamplingTablePane(), getInspectionSamplingTablePane().getName());
				mainPanel.add(getModelCodeComboBox(), getModelCodeComboBox().getName());
				mainPanel.add(getModelCodeTextLabel(), getModelCodeTextLabel().getName());
				mainPanel.add(getTypeCodeLabel(), getTypeCodeLabel().getName());
				mainPanel.add(getTypeCodeComboBox(), getTypeCodeComboBox().getName());
				mainPanel.add(getAddInspectionSamplingButton(), getAddInspectionSamplingButton().getName());
				mainPanel.add(getEmissionSamplingSizeLabel(), getEmissionSamplingSizeLabel().getName());
				mainPanel.add(getEmissionSampleSizeNumeratorTextField(),getEmissionSampleSizeNumeratorTextField().getName());
				mainPanel.add(getEmissionSampleSizeDenominatorTextField(),getEmissionSampleSizeDenominatorTextField().getName());
				mainPanel.add(getEmissionSamplingSlashLabel(), getEmissionSamplingSlashLabel().getName());
				mainPanel.add(getDimensionSampleSizeNumeratorTextField(),getDimensionSampleSizeNumeratorTextField().getName());
				mainPanel.add(getDimensionSampleSizeSlashLabel(), getDimensionSampleSizeSlashLabel().getName());
				mainPanel.add(getDimensionSampleSizeDenominatorTextField(),getDimensionSampleSizeDenominatorTextField().getName());
				mainPanel.add(getDimensionSamplingSizeLabel(), getDimensionSamplingSizeLabel().getName());
				mainPanel.add(getUpdateInspectionSamplingButton(), getUpdateInspectionSamplingButton().getName());
				mainPanel.add(getDeleteInspectionSamplingButton(), getDeleteInspectionSamplingButton().getName());
				mainPanel.add(getNoticeLabel(), getNoticeLabel().getName());
			} catch (Exception e) {
				handleException(e);
			}
		}
		return mainPanel;
	}

	private void initConnections() {
		getInspectionSamplingTablePane().getTable().addMouseListener(this);
		getAfOffLabelHoldCheckBox().addActionListener(this);
		getDimensionSampleSizeDenominatorTextField().addKeyListener(this);
		getDimensionSampleSizeNumeratorTextField().addKeyListener(this);
		getEmissionSampleSizeDenominatorTextField().addKeyListener(this);
		getEmissionSampleSizeNumeratorTextField().addKeyListener(this);
		getAddInspectionSamplingButton().addActionListener(this);
		getUpdateInspectionSamplingButton().addActionListener(this);
		getTypeCodeComboBox().addActionListener(this);
		getModelCodeComboBox().addActionListener(this);
		getDeleteInspectionSamplingButton().addActionListener(this);
	}

	private void initialize() {
		try {
			setLayout(new BorderLayout());
			add(getMainPanel(), BorderLayout.CENTER);
			setName("InspectionSamplingMaintenanceFrame");
			initConnections();
			startFrame();
		} catch (Exception e) {
			handleException(e);
		}
	}

	public void inputDimensionSampleSizeNumerator() {
		setErrorMessage("");
		String dimensionNumeratorText = getDimensionSampleSizeNumeratorTextField().getText();
		if (dimensionNumeratorText.length() != 0) {
			getDimensionSampleSizeDenominatorTextField().setEnabled(true);
		} else {
			getDimensionSampleSizeDenominatorTextField().setText("");
			getDimensionSampleSizeDenominatorTextField().setEnabled(false);
		}
		selectInspectionSamplingTableRows();
	}

	public void selectInspectionSamplingTableRows() {
		resetComponents();
		String rowDataMODEL = new String();
		String rowDataTYPE = new String();
		String selectedMODELItem = (String) getModelCodeComboBox().getSelectedItem();
		String selectedTYPEItem = (String) getTypeCodeComboBox().getSelectedItem();
		int i = 0;
		for (InspectionSampling inspectionSampling : getInspectionSamplingTableModel().getItems()) {
			rowDataMODEL = inspectionSampling.getId().getModelCode();
			rowDataTYPE = inspectionSampling.getId().getModelTypeCode();
			if (rowDataMODEL.equals(selectedMODELItem) && rowDataTYPE.equals(selectedTYPEItem)) {
				getInspectionSamplingTablePane().clearSelection();
				getInspectionSamplingTablePane().getTable().addRowSelectionInterval(i, i);
				getEmissionSampleSizeNumeratorTextField().setEnabled(true);
				getEmissionSampleSizeDenominatorTextField().setEnabled(true);
				getDimensionSampleSizeNumeratorTextField().setEnabled(true);
				getDimensionSampleSizeDenominatorTextField().setEnabled(true);
			}
			i = i + 1;
		}
	}

	public void inputDimesionSampleSizeDenominator() {
		setErrorMessage("");
		if (Integer.parseInt(getDimensionSampleSizeNumeratorTextField().getText()) != 0) {
			if (!(getDimensionSampleSizeDenominatorTextField().getText()).equals("")) {
				if (Integer.parseInt(getDimensionSampleSizeDenominatorTextField().getText()) == 0) {
					getDimensionSampleSizeDenominatorTextField().setText("");
				}
			}
		}
		selectInspectionSamplingTableRows();
	}

	public void inputEmissionSampleSizeNumerator() {
		setErrorMessage("");
		String emissionNumeratorText = getEmissionSampleSizeNumeratorTextField().getText();
		if (emissionNumeratorText.length() != 0) {
			getEmissionSampleSizeDenominatorTextField().setEnabled(true);
		} else {
			getEmissionSampleSizeDenominatorTextField().setText("");
			getEmissionSampleSizeDenominatorTextField().setEnabled(false);
		}
		selectInspectionSamplingTableRows();
	}

	public void inputEmissionSampleSizeDenominator() {
		setErrorMessage("");
		if (Integer.parseInt(getEmissionSampleSizeNumeratorTextField().getText()) != 0) {
			if (!getEmissionSampleSizeDenominatorTextField().getText().equals("")) {
				if (Integer.parseInt(getEmissionSampleSizeDenominatorTextField().getText()) == 0) {
					getEmissionSampleSizeDenominatorTextField().setText("");
				}
			}
		}
		selectInspectionSamplingTableRows();
	}

	public void resetComponents() {
		try {
			String sModel, sType;
			String selectedModel = (String) getModelCodeComboBox().getSelectedItem();
			String selectedType = (String) getTypeCodeComboBox().getSelectedItem();
			String sEmissionNum = getEmissionSampleSizeDenominatorTextField().getText();
			String sDimensionNum = getDimensionSampleSizeDenominatorTextField().getText();
			if ((sEmissionNum.length() == 0) || (sDimensionNum.length() == 0) || (selectedModel == null) || (selectedType == null)) {
				refreshInspectionSamplingTableModel();
				getUpdateInspectionSamplingButton().setEnabled(false);
				getDeleteInspectionSamplingButton().setEnabled(false);
				getAddInspectionSamplingButton().setEnabled(false);
			} else {
				getUpdateInspectionSamplingButton().setEnabled(false);
				getDeleteInspectionSamplingButton().setEnabled(false);
				getAddInspectionSamplingButton().setEnabled(true);
				for (InspectionSampling inspectionSampling : getInspectionSamplingTableModel().getItems()) {
					sModel = inspectionSampling.getId().getModelCode();
					sType = inspectionSampling.getId().getModelTypeCode();
					if (selectedModel.equals(sModel) && selectedType.equals(sType)) {
						getAddInspectionSamplingButton().setEnabled(false);
						getUpdateInspectionSamplingButton().setEnabled(true);
						getDeleteInspectionSamplingButton().setEnabled(true);
						break;
					}
				}
			}
		} catch (Exception e) {
			handleException(e);
		}
	}

	public void resetControls() {
		getAddInspectionSamplingButton().setEnabled(false);
		getUpdateInspectionSamplingButton().setEnabled(false);
		getDeleteInspectionSamplingButton().setEnabled(false);
		getEmissionSampleSizeNumeratorTextField().setText("");
		getEmissionSampleSizeDenominatorTextField().setText("");
		getDimensionSampleSizeNumeratorTextField().setText("");
		getDimensionSampleSizeDenominatorTextField().setText("");
		getEmissionSampleSizeNumeratorTextField().setEnabled(true);
		getEmissionSampleSizeDenominatorTextField().setEnabled(false);
		getDimensionSampleSizeNumeratorTextField().setEnabled(true);
		getDimensionSampleSizeDenominatorTextField().setEnabled(false);
		getModelCodeComboBox().setSelectedIndex(-1);
		getTypeCodeComboBox().setSelectedIndex(-1);
	}

	public void resetScreen(String popupMsg) {
		setErrorMessage("");
		popupMsg+=resetMissingInspectionSamplingData();		
		resetAfOffLabelHoldCheckBox();
		refreshInspectionSamplingTableModel();
		resetModelCodeComboxData();
		resetTypeCodeComboxData();
		resetControls();
		if (popupMsg.trim().length() > 0)
			MessageDialog.showInfo(this, popupMsg);
	}

	public String resetMissingInspectionSamplingData() {
		List<Object[]> missingModelTypeCodeList = getInspectionSamplingDao().findAllMissingInspSamplingModelTypeCodes();
		List<InspectionSampling> missingSamplingList = new ArrayList<InspectionSampling>();
		for (Object[] modelTypeArray : missingModelTypeCodeList) {
			String modelCode = (String) modelTypeArray[0];
			String typeCode = (String) modelTypeArray[1];
			if (modelCode != null && typeCode != null && modelCode.trim().length() > 0 && typeCode.trim().length() > 0) {
				InspectionSamplingId id = new InspectionSamplingId(modelCode, typeCode);
				InspectionSampling sampling = new InspectionSampling(id, 0, 0, 0, 0, 0, 0, 0, 0);
				missingSamplingList.add(sampling);
			}
		}
		if (!missingSamplingList.isEmpty()) {
			getInspectionSamplingDao().saveAll(missingSamplingList);
			return ("\n Inspection Sampling table updated with " + missingSamplingList.size() + " missing New Model Code(s) and Type Code(s)");
		}
		return "";
	}

	public void resetAfOffLabelHoldCheckBox() {
		Rule affOFFLabelHoldRule = ServiceFactory.getDao(RuleDao.class).findByKey(AF_OFF_LABEL_HOLD_RULE_ID);
		if (affOFFLabelHoldRule != null)
			setAfOFFlabelHoldEnabled(affOFFLabelHoldRule.getActiveState() == 1);
		getAfOffLabelHoldCheckBox().setSelected(isAfOFFlabelHoldEnabled());
	}

	public void resetTypeCodeComboxData() {
		getTypeCodeComboBox().removeAllItems();
		List<String> modelTypeCodeList = ServiceFactory.getDao(FrameSpecDao.class).findAllModelTypeCodes(ProductType.FRAME.name());
		for (String modelTypeCode : modelTypeCodeList) {
			if (modelTypeCode != null && modelTypeCode.trim().length() > 0)
				getTypeCodeComboBox().addItem(modelTypeCode.trim());
		}
		getTypeCodeComboBox().setSelectedIndex(-1);
	}

	public void resetModelCodeComboxData() {
		List<String> modelCodeList = ServiceFactory.getDao(FrameSpecDao.class).findAllModelCodes(ProductType.FRAME.name());
		getModelCodeComboBox().removeAllItems();
		for (String modelCode : modelCodeList) {
			if (modelCode != null && modelCode.trim().length() > 0)
				getModelCodeComboBox().addItem(modelCode.trim());
		}
		getModelCodeComboBox().setSelectedIndex(-1);
	}

	public void refreshInspectionSamplingTableModel() {
		List<InspectionSampling> inspectionSamplingList = getInspectionSamplingDao().findAll();
		getInspectionSamplingTableModel().refresh(inspectionSamplingList);
		getInspectionSamplingTableModel().pack();
	}

	protected void handleException(Exception e) {
		if (e == null)
			clearErrorMessage();
		else {
			getLogger().error(e, "Unexpected exception occurs: " + e.getMessage() + " stack trace:" + getStackTrace(e));
			setErrorMessage(e.getMessage());
			e.printStackTrace();
		}
	}

	public void selectModel() {
		setErrorMessage("");
		String rowModel = new String();
		String rowModelType = new String();
		String selectedModelItem = (String) getModelCodeComboBox().getSelectedItem();
		String selectedTypeItem = (String) getTypeCodeComboBox().getSelectedItem();
		String rowEmissionSampleSizeNumerator = new String();
		String rowEmissionSampleSizeDenominator = new String();
		String rowDimensionSamplingSizeNumerator = new String();
		String rowDimensionSampleSizeDenominator = new String();
		boolean inTable = false;
		int i = 0;
		for (InspectionSampling inspectionSampling : getInspectionSamplingTableModel().getItems()) {
			rowModel = inspectionSampling.getId().getModelCode();
			rowModelType = inspectionSampling.getId().getModelTypeCode();
			if (rowModel.equals(selectedModelItem) && rowModelType.equals(selectedTypeItem)) {
				rowEmissionSampleSizeNumerator = inspectionSampling.getEmissionMoleculeMaster().toString();
				rowEmissionSampleSizeDenominator = inspectionSampling.getEmissionDenominatorMaster().toString();
				getEmissionSampleSizeNumeratorTextField().setText(rowEmissionSampleSizeNumerator);
				getEmissionSampleSizeDenominatorTextField().setText(rowEmissionSampleSizeDenominator);
				rowDimensionSamplingSizeNumerator = inspectionSampling.getDimensionMoleculeMaster().toString();
				rowDimensionSampleSizeDenominator = inspectionSampling.getDimensionDenominatorMaster().toString();
				getDimensionSampleSizeNumeratorTextField().setText(rowDimensionSamplingSizeNumerator);
				getDimensionSampleSizeDenominatorTextField().setText(rowDimensionSampleSizeDenominator);
				getInspectionSamplingTablePane().clearSelection();
				getInspectionSamplingTablePane().getTable().addRowSelectionInterval(i, i);
				inTable = true;
				getAddInspectionSamplingButton().setEnabled(false);
				getUpdateInspectionSamplingButton().setEnabled(true);
				getDeleteInspectionSamplingButton().setEnabled(true);
				getEmissionSampleSizeNumeratorTextField().setEnabled(true);
				getEmissionSampleSizeDenominatorTextField().setEnabled(true);
				getDimensionSampleSizeNumeratorTextField().setEnabled(true);
				getDimensionSampleSizeDenominatorTextField().setEnabled(true);
			}
			i = i + 1;
		}
		if (!inTable) {
			getEmissionSampleSizeNumeratorTextField().setText("");
			getEmissionSampleSizeDenominatorTextField().setText("");
			getDimensionSampleSizeNumeratorTextField().setText("");
			getDimensionSampleSizeDenominatorTextField().setText("");
			getAddInspectionSamplingButton().setEnabled(false);
			getUpdateInspectionSamplingButton().setEnabled(false);
			getDeleteInspectionSamplingButton().setEnabled(false);
			getInspectionSamplingTablePane().clearSelection();
			getEmissionSampleSizeNumeratorTextField().setEnabled(true);
			getEmissionSampleSizeDenominatorTextField().setEnabled(false);
			getDimensionSampleSizeNumeratorTextField().setEnabled(true);
			getDimensionSampleSizeDenominatorTextField().setEnabled(false);
		}
		if ((getModelCodeComboBox().getSelectedIndex() == -1) && (getTypeCodeComboBox().getSelectedIndex() == -1)) {
			getAddInspectionSamplingButton().setEnabled(false);
			getUpdateInspectionSamplingButton().setEnabled(false);
			getDeleteInspectionSamplingButton().setEnabled(false);
			getInspectionSamplingTablePane().clearSelection();
			getEmissionSampleSizeNumeratorTextField().setEnabled(false);
			getEmissionSampleSizeDenominatorTextField().setEnabled(false);
			getDimensionSampleSizeNumeratorTextField().setEnabled(false);
			getDimensionSampleSizeDenominatorTextField().setEnabled(false);
		}
		if ((getModelCodeComboBox().getSelectedIndex() == -1) || (getTypeCodeComboBox().getSelectedIndex() == -1)) {
			getEmissionSampleSizeNumeratorTextField().setEnabled(false);
			getEmissionSampleSizeDenominatorTextField().setEnabled(false);
			getDimensionSampleSizeNumeratorTextField().setEnabled(false);
			getDimensionSampleSizeDenominatorTextField().setEnabled(false);
		}
	}

	
	public void selectUnselectInspectionSampling() {
		if (getInspectionSamplingTablePane().getTable().getSelectedRows().length <= 0) {
			resetControls();
		} else {
			InspectionSampling temSelectionItem = getInspectionSamplingTableModel().getSelectedItem();
			getModelCodeComboBox().setSelectedItem(temSelectionItem.getId().getModelCode().trim());
			getTypeCodeComboBox().setSelectedItem(temSelectionItem.getId().getModelTypeCode().trim());
			getEmissionSampleSizeNumeratorTextField().setText(temSelectionItem.getEmissionMoleculeMaster().toString());
			getEmissionSampleSizeDenominatorTextField().setText(temSelectionItem.getEmissionDenominatorMaster().toString());
			getDimensionSampleSizeNumeratorTextField().setText(temSelectionItem.getDimensionMoleculeMaster().toString());
			getDimensionSampleSizeDenominatorTextField().setText(temSelectionItem.getDimensionDenominatorMaster().toString());
			getEmissionSampleSizeNumeratorTextField().setEnabled(true);
			getEmissionSampleSizeDenominatorTextField().setEnabled(true);
			getDimensionSampleSizeNumeratorTextField().setEnabled(true);
			getDimensionSampleSizeDenominatorTextField().setEnabled(true);
			getAddInspectionSamplingButton().setEnabled(false);
			getUpdateInspectionSamplingButton().setEnabled(true);
			getDeleteInspectionSamplingButton().setEnabled(true);
		}
	}

	public void startFrame() {
		try {
			resetScreen("");
		} catch (Exception e) {
			handleException(e);
		}
	}

	public void addInspectionSampling() {
		setErrorMessage("");
		String msg=null;
		String modelCode = (String) getModelCodeComboBox().getSelectedItem();
		String typeCode = (String) getTypeCodeComboBox().getSelectedItem();
		int emissionMoleculeMaster = Integer.parseInt(getEmissionSampleSizeNumeratorTextField().getText());
		int emissionDenominatorMaster = Integer.parseInt(getEmissionSampleSizeDenominatorTextField().getText());
		int dimensionMoleculeMaster = Integer.parseInt(getDimensionSampleSizeNumeratorTextField().getText());
		int dimensionDenominatorMaster = Integer.parseInt(getDimensionSampleSizeDenominatorTextField().getText());
		if (emissionMoleculeMaster > emissionDenominatorMaster) {
			setErrorMessage("Sampling Test Size Denominator is less than Numerator");
			return;
		}
		if (dimensionMoleculeMaster > dimensionDenominatorMaster) {
			setErrorMessage("Spec Check Test Size Denominator is less than Numerator");
			return;
		}
		InspectionSamplingId inspectionSamplingId = new InspectionSamplingId(modelCode, typeCode);
		InspectionSampling inspectionSampling = getInspectionSamplingDao().findByKey(inspectionSamplingId);
		if (inspectionSampling != null) {
			setErrorMessage("Inspection Sampling record already exists");
		} else {
			InspectionSampling sampling = new InspectionSampling(inspectionSamplingId, 0, 0, 0, 0,emissionDenominatorMaster, emissionMoleculeMaster, dimensionDenominatorMaster,dimensionMoleculeMaster);
			getInspectionSamplingDao().save(sampling);
			msg=msg="Successfully inserted the selected Inspection Sampling data for Model="+inspectionSamplingId.getModelCode()+",TypeCode="+inspectionSamplingId.getModelTypeCode();
		}
		getDeleteInspectionSamplingButton().requestFocus();
		resetScreen(msg);
	}

	public void deleteInspectionSampling() {
		setErrorMessage("");
		String msg=null;
		String modelCode = (String) getModelCodeComboBox().getSelectedItem();
		String typeCode = (String) getTypeCodeComboBox().getSelectedItem();
		InspectionSamplingId inspectionSamplingId = new InspectionSamplingId(modelCode, typeCode);
		InspectionSampling inspectionSampling = getInspectionSamplingDao().findByKey(inspectionSamplingId);
		if (inspectionSampling == null) {
			setErrorMessage("Inspection Sampling record already deleted");
		} else {
			getInspectionSamplingDao().remove(inspectionSampling);
			msg="Successfully deleted the selected Inspection Sampling data for Model="+inspectionSamplingId.getModelCode()+",TypeCode="+inspectionSamplingId.getModelTypeCode();
		}
		getDeleteInspectionSamplingButton().requestFocus();
		resetScreen(msg);
	}

	public void updateInspectionSampling() {
		setErrorMessage("");
		String msg=null;
		String modelCode = (String) getModelCodeComboBox().getSelectedItem();
		String typeCode = (String) getTypeCodeComboBox().getSelectedItem();
		int emissionMoleculeMaster = Integer.parseInt(getEmissionSampleSizeNumeratorTextField().getText());
		int emissionDenominatorMaster = Integer.parseInt(getEmissionSampleSizeDenominatorTextField().getText());
		int dimensionMoleculeMaster = Integer.parseInt(getDimensionSampleSizeNumeratorTextField().getText());
		int dimensionDenominatorMaster = Integer.parseInt(getDimensionSampleSizeDenominatorTextField().getText());
		if (emissionMoleculeMaster > emissionDenominatorMaster) {
			setErrorMessage("Sampling Test Size Denominator is less than Numerator");
			return;
		}
		if (dimensionMoleculeMaster > dimensionDenominatorMaster) {
			setErrorMessage("Spec Check Test Size Denominator is less than Numerator");
			return;
		}
		InspectionSamplingId inspectionSamplingId = new InspectionSamplingId(modelCode, typeCode);
		InspectionSampling sampling = new InspectionSampling(inspectionSamplingId, 0, 0, 0, 0,emissionDenominatorMaster, emissionMoleculeMaster, dimensionDenominatorMaster, dimensionMoleculeMaster);
		getInspectionSamplingDao().update(sampling);
		msg="Successfully updated the selected Inspection Sampling data for Model="+inspectionSamplingId.getModelCode()+",TypeCode="+inspectionSamplingId.getModelTypeCode();
		resetScreen(msg);
	}

	public void updateAfOffLabelHoldRuleState() {
		Rule afOffLabelHoldRule = ServiceFactory.getDao(RuleDao.class).findByKey(AF_OFF_LABEL_HOLD_RULE_ID);
		if (afOffLabelHoldRule == null) {
			setErrorMessage("Rule does not exist.Unable to update Label Hold Status");
			getAfOffLabelHoldCheckBox().setSelected(!getAfOffLabelHoldCheckBox().isSelected());
			return;
		} else {
			int newValue = getAfOffLabelHoldCheckBox().isSelected() ? 1 : 0;
			afOffLabelHoldRule.setActiveState(newValue);
			ServiceFactory.getDao(RuleDao.class).save(afOffLabelHoldRule);
			setMessage("Successfully updated the Label Hold Status");
		}
	}
	
	public void selectType() {
		setErrorMessage("");
		String rowModel = new String();
		String rowType = new String();
		String selectedModelItem = (String) getModelCodeComboBox().getSelectedItem();
		String selectedTypeItem = (String) getTypeCodeComboBox().getSelectedItem();
		String rowEmissionSampleSizeNumerator = new String();
		String rowEmissionSampleSizeDenominator = new String();
		String rowDimensionSamplingSizeNumerator = new String();
		String rowDimensionSampleSizeDenominator = new String();
		boolean inTable = false;
		int i = 0;
		for (InspectionSampling inspectionSampling : getInspectionSamplingTableModel().getItems()) {
			rowModel = inspectionSampling.getId().getModelCode();
			rowType = inspectionSampling.getId().getModelTypeCode();
			if (rowModel.equals(selectedModelItem) && rowType.equals(selectedTypeItem)) {
				rowEmissionSampleSizeNumerator = inspectionSampling.getEmissionMoleculeMaster().toString();
				rowEmissionSampleSizeDenominator = inspectionSampling.getEmissionDenominatorMaster().toString();
				getEmissionSampleSizeNumeratorTextField().setText(rowEmissionSampleSizeNumerator);
				getEmissionSampleSizeDenominatorTextField().setText(rowEmissionSampleSizeDenominator);
				rowDimensionSamplingSizeNumerator = inspectionSampling.getDimensionMoleculeMaster().toString();
				rowDimensionSampleSizeDenominator = inspectionSampling.getDimensionDenominatorMaster().toString();
				getDimensionSampleSizeNumeratorTextField().setText(rowDimensionSamplingSizeNumerator);
				getDimensionSampleSizeDenominatorTextField().setText(rowDimensionSampleSizeDenominator);
				getInspectionSamplingTablePane().clearSelection();
				getInspectionSamplingTablePane().getTable().addRowSelectionInterval(i, i);
				inTable = true;
				getAddInspectionSamplingButton().setEnabled(false);
				getUpdateInspectionSamplingButton().setEnabled(true);
				getDeleteInspectionSamplingButton().setEnabled(true);
				getEmissionSampleSizeNumeratorTextField().setEnabled(true);
				getEmissionSampleSizeDenominatorTextField().setEnabled(true);
				getDimensionSampleSizeNumeratorTextField().setEnabled(true);
				getDimensionSampleSizeDenominatorTextField().setEnabled(true);
			}
			i = i + 1;
		}
		if (!inTable) {
			getEmissionSampleSizeNumeratorTextField().setText("");
			getEmissionSampleSizeDenominatorTextField().setText("");
			getDimensionSampleSizeNumeratorTextField().setText("");
			getDimensionSampleSizeDenominatorTextField().setText("");
			getAddInspectionSamplingButton().setEnabled(false);
			getUpdateInspectionSamplingButton().setEnabled(false);
			getDeleteInspectionSamplingButton().setEnabled(false);
			getInspectionSamplingTablePane().clearSelection();
			getEmissionSampleSizeNumeratorTextField().setEnabled(true);
			getEmissionSampleSizeDenominatorTextField().setEnabled(false);
			getDimensionSampleSizeNumeratorTextField().setEnabled(true);
			getDimensionSampleSizeDenominatorTextField().setEnabled(false);
		}
		if ((getModelCodeComboBox().getSelectedIndex() == -1) && (getTypeCodeComboBox().getSelectedIndex() == -1)) {
			getAddInspectionSamplingButton().setEnabled(false);
			getUpdateInspectionSamplingButton().setEnabled(false);
			getDeleteInspectionSamplingButton().setEnabled(false);
			getInspectionSamplingTablePane().clearSelection();
			getEmissionSampleSizeNumeratorTextField().setEnabled(false);
			getEmissionSampleSizeDenominatorTextField().setEnabled(false);
			getDimensionSampleSizeNumeratorTextField().setEnabled(false);
			getDimensionSampleSizeDenominatorTextField().setEnabled(false);
		}

		if ((getModelCodeComboBox().getSelectedIndex() == -1) || (getTypeCodeComboBox().getSelectedIndex() == -1)) {
			getEmissionSampleSizeNumeratorTextField().setEnabled(false);
			getEmissionSampleSizeDenominatorTextField().setEnabled(false);
			getDimensionSampleSizeNumeratorTextField().setEnabled(false);
			getDimensionSampleSizeDenominatorTextField().setEnabled(false);
		}
	}

	protected InspectionSamplingDao getInspectionSamplingDao() {
		return ServiceFactory.getDao(InspectionSamplingDao.class);
	}

	public boolean isAfOFFlabelHoldEnabled() {
		return afOFFlabelHoldEnabled;
	}

	public void setAfOFFlabelHoldEnabled(boolean afOFFlabelHoldEnabled) {
		this.afOFFlabelHoldEnabled = afOFFlabelHoldEnabled;
	}

}

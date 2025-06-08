package com.honda.galc.client.teamleader;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.annotation.AnnotationProcessor;

import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.client.ui.tablemodel.StragglerCodeAssignmentTableModel;
import com.honda.galc.client.utils.ViewUtil;
import com.honda.galc.common.exception.PropertyException;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.CodeDao;
import com.honda.galc.dao.product.StragglerDao;
import com.honda.galc.dto.StragglerCodeAssignmentDto;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.Straggler;
import com.honda.galc.entity.product.StragglerId;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

public class StragglerCodeAssignmentPanel extends TabbedPanel implements ActionListener, TableModelListener {

	private static final long serialVersionUID = 1L;

	private static final Font FONT = Fonts.DIALOG_PLAIN_20;
	private static final Insets INSETS = new Insets(8, 8, 8, 8);
	private static final int TEXT_FIELD_SIZE = 30;
	private static final Dimension COMBO_BOX_DIMENSION;
	static {
		JTextField jTextField = new JTextField(TEXT_FIELD_SIZE);
		jTextField.setFont(FONT);
		JComboBox jComboBox = new JComboBox();
		jComboBox.setFont(FONT);
		COMBO_BOX_DIMENSION = new Dimension((int) jTextField.getPreferredSize().getWidth(), (int) jComboBox.getPreferredSize().getHeight());
	}

	private StragglerDao stragglerDao;
	private CodeDao codeDao;
	private ProcessPointDao processPointDao;
	private final List<String> stragglerCodeTypes;

	private List<StragglerCodeAssignmentDto> stragglers = new ArrayList<StragglerCodeAssignmentDto>();
	private JPanel filterPanel;
	private JComboBox stragglerProcessPointsComboBox;
	private ButtonGroup stragglersButtonGroup;
	private JRadioButton currentStragglersRadioButton;
	private JRadioButton nonCurrentStragglersRadioButton;
	private JRadioButton allStragglersRadioButton;
	private TablePane stragglerTablePane;
	private StragglerCodeAssignmentTableModel stragglerTableModel;

	public StragglerCodeAssignmentPanel(TabbedMainWindow mainWindow) {
		super("Straggler Code Assignment", KeyEvent.VK_S, mainWindow);
		this.stragglerCodeTypes = Arrays.asList(getTerminalCsvProperty("STRAGGLER_CODE_TYPES"));
		this.stragglersButtonGroup = new ButtonGroup();
		this.stragglersButtonGroup.add(getCurrentStragglersRadioButton());
		this.stragglersButtonGroup.add(getNonCurrentStragglersRadioButton());
		this.stragglersButtonGroup.add(getAllStragglersRadioButton());
		AnnotationProcessor.process(this);
	}



	/*
	 * Inherited methods
	 */
	@Override
	public void onTabSelected() {
		if (!this.isInitialized) {
			initComponents();
			this.isInitialized = true;
		}
		loadStragglerTableModelCodes();
		loadStragglers();
	}

	public void actionPerformed(ActionEvent e) {}

	public void tableChanged(TableModelEvent tme) {
		StragglerCodeAssignmentTableModel model = (StragglerCodeAssignmentTableModel) tme.getSource();
		StragglerCodeAssignmentDto dto = model.getSelectedItem();
		if (dto == null) {
			return;
		}
		try {
			Straggler straggler = getStragglerDao().findByKey(new StragglerId(dto.getProductId(), dto.getPpDelayedAt(),dto.getStragglerType()));
			straggler.setCode(StringUtils.substring(dto.getCode(),0,64));
			straggler.setComments(StringUtils.substring(dto.getComments(),0,256));
			getStragglerDao().save(straggler);
			logUserAction(SAVED, straggler);
		} catch (Exception e) {
			model.rollback();
			handleException(e);
		}
	}



	/*
	 * Utility methods
	 */
	protected static JComboBox createJComboBox() {
		JComboBox jComboBox = new JComboBox();
		jComboBox.setBackground(Color.WHITE);
		jComboBox.setFont(FONT);
		jComboBox.setPreferredSize(COMBO_BOX_DIMENSION);
		return jComboBox;
	}

	protected static JLabel createLabelFor(String labelText, Component forComponent) {
		JLabel label = new JLabel(labelText);
		label.setFont(FONT);
		label.setLabelFor(forComponent);
		return label;
	}

	@SuppressWarnings("deprecation")
	private String[] getTerminalCsvProperty(String propertyKey) {
		final String hostName = getMainWindow().getApplicationContext().getTerminal().getHostName();
		String propertyCsv;
		String[] propertyArray;
		try {
			propertyCsv = PropertyService.getProperty(hostName, propertyKey);
		} catch (PropertyException pe) {
			propertyCsv = null;
		}
		if (StringUtils.isNotBlank(propertyCsv)) {
			propertyArray = propertyCsv.split(Delimiter.COMMA);
		} else {
			propertyArray = new String[0];
		}
		return propertyArray;
	}



	/*
	 * Getter methods
	 */
	private List<StragglerCodeAssignmentDto> getStragglers() {
		return this.stragglers;
	}

	private void setStragglers(List<StragglerCodeAssignmentDto> stragglers) {
		this.stragglers = stragglers;
	}

	private StragglerDao getStragglerDao() {
		if (this.stragglerDao == null) {
			this.stragglerDao = ServiceFactory.getDao(StragglerDao.class);
		}
		return this.stragglerDao;
	}

	private CodeDao getCodeDao() {
		if (this.codeDao == null) {
			this.codeDao = ServiceFactory.getDao(CodeDao.class);
		}
		return this.codeDao;
	}

	private ProcessPointDao getProcessPointDao() {
		if (this.processPointDao == null) {
			this.processPointDao = ServiceFactory.getDao(ProcessPointDao.class);
		}
		return this.processPointDao;
	}

	private JPanel getFilterPanel() {
		if (this.filterPanel == null) {
			this.filterPanel = new JPanel(new GridBagLayout());
			ViewUtil.setGridBagConstraints(this.filterPanel, createLabelFor("Process Point:", getStragglerProcessPointsComboBox()), 0, 0, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.filterPanel, getStragglerProcessPointsComboBox(), 1, 0, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.filterPanel, getCurrentStragglersRadioButton(), 2, 0, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.filterPanel, getNonCurrentStragglersRadioButton(), 3, 0, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.filterPanel, getAllStragglersRadioButton(), 4, 0, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
		}
		return this.filterPanel;
	}

	private JComboBox getStragglerProcessPointsComboBox() {
		if (this.stragglerProcessPointsComboBox == null) {
			this.stragglerProcessPointsComboBox = createJComboBox();
			final String[] stragglerProcessPointIds = getTerminalCsvProperty("STRAGGLER_PROCESS_POINTS");
			if (stragglerProcessPointIds != null && stragglerProcessPointIds.length > 0) {
				StragglerProcessPoint[] stragglerProcessPoints = new StragglerProcessPoint[stragglerProcessPointIds.length];
				for (int i = 0; i < stragglerProcessPointIds.length; i++) {
					ProcessPoint processPoint = getProcessPointDao().findById(stragglerProcessPointIds[i]);
					if (processPoint != null) {
						stragglerProcessPoints[i] = new StragglerProcessPoint(stragglerProcessPointIds[i], processPoint.getProcessPointName());
					} else {
						stragglerProcessPoints[i] = new StragglerProcessPoint(stragglerProcessPointIds[i], null);
					}
				}
				final ComboBoxModel stragglerProcessPointsModel = new DefaultComboBoxModel(stragglerProcessPoints);
				this.stragglerProcessPointsComboBox.setModel(stragglerProcessPointsModel);
				if (stragglerProcessPoints.length == 1) {
					this.stragglerProcessPointsComboBox.setSelectedIndex(0);
				} else {
					this.stragglerProcessPointsComboBox.insertItemAt(new StragglerProcessPoint("",null), 0);
					this.stragglerProcessPointsComboBox.setSelectedIndex(-1);
				}
			} else {
				this.stragglerProcessPointsComboBox.insertItemAt(new StragglerProcessPoint("",null), 0);
				this.stragglerProcessPointsComboBox.setSelectedIndex(-1);
			}
			this.stragglerProcessPointsComboBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					loadStragglers();
				}
			});
		}
		return this.stragglerProcessPointsComboBox;
	}

	private JRadioButton getCurrentStragglersRadioButton() {
		if (this.currentStragglersRadioButton == null) {
			this.currentStragglersRadioButton = new JRadioButton("Current Stragglers");
			this.currentStragglersRadioButton.setSelected(true);
			this.currentStragglersRadioButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					loadStragglers();
				}
			});
		}
		return this.currentStragglersRadioButton;
	}

	private JRadioButton getNonCurrentStragglersRadioButton() {
		if (this.nonCurrentStragglersRadioButton == null) {
			this.nonCurrentStragglersRadioButton = new JRadioButton("Non-current Stragglers");
			this.nonCurrentStragglersRadioButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					loadStragglers();
				}
			});
		}
		return this.nonCurrentStragglersRadioButton;
	}

	private JRadioButton getAllStragglersRadioButton() {
		if (this.allStragglersRadioButton == null) {
			this.allStragglersRadioButton = new JRadioButton("All Stragglers");
			this.allStragglersRadioButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					loadStragglers();
				}
			});
		}
		return this.allStragglersRadioButton;
	}

	private TablePane getStragglerTablePane() {
		if (this.stragglerTablePane == null) {
			this.stragglerTablePane = new TablePane(null, ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		}
		return this.stragglerTablePane;
	}

	private StragglerCodeAssignmentTableModel getStragglerTableModel() {
		if (this.stragglerTableModel == null) {
			final boolean reassignCode; {
				final String hostName = getMainWindow().getApplicationContext().getTerminal().getHostName();
				final String reassignCodeString = PropertyService.getProperty(hostName, "REASSIGN_CODE", "FALSE");
				reassignCode = Boolean.valueOf(reassignCodeString);
			}
			this.stragglerTableModel = new StragglerCodeAssignmentTableModel(getStragglerTablePane().getTable(), getStragglers(), reassignCode);
			this.stragglerTableModel.addTableModelListener(this);
		}
		return this.stragglerTableModel;
	}



	/*
	 * Initialization methods
	 */
	protected void initComponents() {
		this.setLayout(new GridLayout(1, 1));
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, getFilterPanel(), getStragglerTablePane());
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(75);
		add(splitPane);
	}



	/*
	 * Data load methods
	 */
	private void loadStragglerTableModelCodes() {
		List<String> codes = getCodeDao().findCodesByCodeTypes(this.stragglerCodeTypes);
		getStragglerTableModel().setCodeComboCell(codes);
	}

	private void loadStragglers() {
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		try {
			final StragglerProcessPoint stragglerProcessPoint = (StragglerProcessPoint) getStragglerProcessPointsComboBox().getSelectedItem();
			if (stragglerProcessPoint == null || StringUtils.isEmpty(stragglerProcessPoint.getProcessPointId())) {
				setStragglers(null);
			} else {
				setStragglers(getStragglerDao().findStragglerCodeAssignmentData(stragglerProcessPoint.getProcessPointId(), getMainWindow().getProductType(), getCurrentStragglersRadioButton().isSelected() || getAllStragglersRadioButton().isSelected(), getNonCurrentStragglersRadioButton().isSelected() || getAllStragglersRadioButton().isSelected()));
			}
		} catch (Exception e) {
			setStragglers(null);
			handleException(e);
		}
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				getStragglerTableModel().refresh(getStragglers());
				setCursor(Cursor.getDefaultCursor());
			}
		});
	}

	private class StragglerProcessPoint {
		private final String processPointId;
		private final String processPointName;
		public StragglerProcessPoint(String processPointId, String processPointName) {
			this.processPointId = processPointId;
			this.processPointName = processPointName;
		}
		public String getProcessPointId() {
			return this.processPointId;
		}
		public String toString() {
			return StringUtils.isEmpty(this.processPointName) ? this.processPointId : this.processPointName + " (" + this.processPointId + ")";
		}
	}
}

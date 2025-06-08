package com.honda.galc.client.teamleader;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.property.TeamRotationPropertyBean;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.IPopupMenu;
import com.honda.galc.client.ui.component.PopupMenuMouseAdapter;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.dao.product.ScheduleDao;
import com.honda.galc.dao.product.TeamRotationDao;
import com.honda.galc.entity.product.TeamRotation;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>TeamRotationPanel is the screen to manage TEAM_ROTATION_TBX</h3>
 * <p>
 * </p>
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
 * 
 * </TABLE>
 * 
 * @author Yang Xin<br>
 *         Oct 10, 2014
 */
public class TeamRotationPanel extends TabbedPanel implements
		TableModelListener, ActionListener {

	private static final String DATE_FORMAT = "yyyy-MM-dd";

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5133283092269561167L;

	/** The Constant logger. */
	private static final Logger logger = Logger.getLogger("TeamRotationPanel");

	/** The Constant CREATE_TEAM_ROTATION. */
	private final static String CREATE_TEAM_ROTATION = "Create";

	/** The Constant DELETE_TEAM_ROTATION. */
	private final static String DELETE_TEAM_ROTATION = "Delete";

	
	/**
	 * The Class LineCellEditor is the cell editor for Line Column in the team rotation table.
	 */
	private class LineCellEditor extends DefaultCellEditor {
		private static final long serialVersionUID = 1L;

		public LineCellEditor() {
			super(new JComboBox());
		}

		@Override
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			@SuppressWarnings("unchecked")
			JComboBox editor = (JComboBox) super
					.getTableCellEditorComponent(table, value, isSelected, row,
							column);
			TeamRotation rotation = teamRotationTableModel.getItem(row);
			if (rotation != null) {
				editor.setModel(new DefaultComboBoxModel(plantLineMap
						.get(StringUtils.trimToEmpty(rotation.getId().getPlantCode()))));
			}
			return editor;
		}
	}

	/**
	 * The Class LocationCellEditor the cell editor for dept/process location Column in the team rotation table.
	 */
	private class LocationCellEditor extends DefaultCellEditor {
		private static final long serialVersionUID = 1L;

		public LocationCellEditor() {
			super(new JComboBox());
		}

		@Override
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			@SuppressWarnings("unchecked")
			JComboBox editor = (JComboBox) super
					.getTableCellEditorComponent(table, value, isSelected, row,
							column);
			
			TeamRotation rotation = teamRotationTableModel.getItem(row);
			if (rotation != null) {
				String key = createLocationKey(StringUtils.trimToEmpty(rotation
						.getId().getPlantCode()),
						StringUtils.trimToEmpty(rotation.getId().getLineNo()));
				editor.setModel(new DefaultComboBoxModel(
						lineLocationMap.get(key)));
			}
			return editor;
		}
	}
	
	/** The property bean. */
	private TeamRotationPropertyBean propertyBean;

	/** The plant list. */
	private Vector<String> plantList;

	private Map<String, Vector<String>> plantLineMap;

	private Map<String, Vector<String>> lineLocationMap;
	
	/** The shift candidates. */
	private String[] shiftCandidates = new String[0];

	/** The team candidates. */
	private String[] teamCandidates = new String[0];

	/** The search panel. */
	private JPanel searchPanel;

	/** The line id label. */
	private JLabel lineIdLabel;

	/** The plant label. */
	private JLabel plantLabel;

	/** The dept label. */
	private JLabel deptLabel;

	/** The start date label. */
	private JLabel startDateLabel;

	/** The end date label. */
	private JLabel endDateLabel;

	/** The line id combo box. */
	private JComboBox lineIdComboBox;

	/** The plant combo box. */
	private JComboBox plantComboBox;

	/** The dept combo box. */
	private JComboBox deptComboBox;

	/** The start date text field. */
	private JTextField startDateTextField;

	/** The end date text field. */
	private JTextField endDateTextField;

	/** The search btn. */
	private JButton searchBtn;

	/** The team rotation panel. */
	private TablePane teamRotationPanel;

	/** The team rotation table model. */
	private TeamRotationTableModel teamRotationTableModel;

	/**
	 * Instantiates a new team rotation panel.
	 * 
	 * @param mainWindow
	 *            the main window
	 */
	public TeamRotationPanel(TabbedMainWindow mainWindow) {
		super("TeamRotation", KeyEvent.VK_T, mainWindow);
		initComponents();
	}

	/**
	 * Inits the UI components.
	 */
	private void initComponents() {
		propertyBean = PropertyService.getPropertyBean(
				TeamRotationPropertyBean.class, this.getApplicationId());
		prepareMetadata();
		createComponents();
		initListeners();
		plantComboBox.getModel().setSelectedItem(null);
		if(!plantList.isEmpty()){
			plantComboBox.getModel().setSelectedItem(plantList.get(0));
		}
		searchTeamRotation();
	}

	/**
	 * Creates the UI components.
	 */
	private void createComponents() {
		setLayout(new BorderLayout());
		// init search panel
		searchPanel = new JPanel();
		searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

		plantLabel = new JLabel("     Plant");
		searchPanel.add(plantLabel);
		plantComboBox = new JComboBox(plantList);
		searchPanel.add(plantComboBox);
		
		lineIdLabel = new JLabel("Line ID");
		searchPanel.add(lineIdLabel);
		lineIdComboBox = new JComboBox();
		searchPanel.add(lineIdComboBox);
		
		deptLabel = new JLabel("     Dept");
		searchPanel.add(deptLabel);
		deptComboBox = new JComboBox();
		searchPanel.add(deptComboBox);

		startDateLabel = new JLabel("     Start Production Date");
		searchPanel.add(startDateLabel);
		SpinnerModel startDateModel = new SpinnerDateModel();
		JSpinner startDateSpinner = new JSpinner(startDateModel);
		JComponent startDateEditor = new JSpinner.DateEditor(startDateSpinner,
				DATE_FORMAT);
		startDateTextField = ((JSpinner.DateEditor) startDateEditor)
				.getTextField();
		startDateTextField.setEditable(false);
		startDateTextField.setBackground(Color.WHITE);
		startDateSpinner.setEditor(startDateEditor);
		searchPanel.add(startDateSpinner);

		endDateLabel = new JLabel("     End Production Date");
		searchPanel.add(endDateLabel);
		SpinnerModel endDateModel = new SpinnerDateModel();
		JSpinner endDateSpinner = new JSpinner(endDateModel);
		JComponent endDateEditor = new JSpinner.DateEditor(endDateSpinner,
				DATE_FORMAT);
		endDateTextField = ((JSpinner.DateEditor) endDateEditor).getTextField();
		endDateTextField.setEditable(false);
		endDateTextField.setBackground(Color.WHITE);
		endDateSpinner.setEditor(endDateEditor);
		searchPanel.add(endDateSpinner);
		searchPanel.add(new JLabel(""));
		searchBtn = new JButton("Search");
		searchPanel.add(searchBtn);

		// init result table
		teamRotationPanel = new TablePane("",
				ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		teamRotationTableModel = new TeamRotationTableModel(teamRotationPanel.getTable(), new ArrayList<TeamRotation>());
		
		JTable table = teamRotationPanel.getTable();
		JComboBox plantComboBox = new JComboBox(plantList);
		JComboBox shiftComboBox = new JComboBox(shiftCandidates);
		JComboBox teamComboBox = new JComboBox(teamCandidates);
		
		DefaultCellEditor cellEditor = new DefaultCellEditor(plantComboBox);
		cellEditor.setClickCountToStart(2);
		table.getColumnModel().getColumn(1).setCellEditor(cellEditor);
		
		cellEditor = new LineCellEditor();
		cellEditor.setClickCountToStart(2);
		table.getColumnModel().getColumn(2).setCellEditor(cellEditor);
		
		cellEditor = new LocationCellEditor();
		cellEditor.setClickCountToStart(2);
		table.getColumnModel().getColumn(3).setCellEditor(cellEditor);
		
		cellEditor = new DefaultCellEditor(shiftComboBox);
		cellEditor.setClickCountToStart(2);
		table.getColumnModel().getColumn(4).setCellEditor(cellEditor);
		
		cellEditor = new DefaultCellEditor(teamComboBox);
		cellEditor.setClickCountToStart(2);
		table.getColumnModel().getColumn(5).setCellEditor(cellEditor);
		
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		renderer.setBackground(Color.green);
		renderer.setHorizontalTextPosition(SwingConstants.CENTER);
		teamRotationPanel.setCellRenderer(renderer);
		teamRotationTableModel.pack();
		this.add(searchPanel, BorderLayout.NORTH);
		this.add(teamRotationPanel, BorderLayout.CENTER);
	}

	/**
	 * Creates the location key using plant and line. this key is used to query <code>lineLocationMap</code>
	 *
	 * @param plant the plant
	 * @param line the line
	 * @return the string
	 */
	public static String createLocationKey(String plant, String line) {
		return plant + "." + line;
	}
	
	/**
	 * Prepare the metadata.
	 */
	private void prepareMetadata() {
		ScheduleDao scheduleDao = ServiceFactory
				.getDao(DailyDepartmentScheduleDao.class);
		Set<String> plants = new HashSet<String>();
		Map<String, Set<String>> lines = new HashMap<String, Set<String>>();
		Map<String, Set<String>> locations = new HashMap<String, Set<String>>();
		
		List<Object[]> processInfos = scheduleDao.getProcessInfo();
		String plant;
		String line;
		String location;
		String locationKey;
		for (Object[] info : processInfos) {
			plant = StringUtils.trimToEmpty((String)info[0]);
			line = StringUtils.trimToEmpty((String)info[1]);
			location = StringUtils.trimToEmpty((String)info[2]);
			plants.add(plant);
			
			if (!lines.containsKey(plant)) {
				lines.put(plant, new HashSet<String>());
			}
			lines.get(plant).add(line);
			locationKey = createLocationKey(plant,line);
			if (!locations.containsKey(locationKey)) {
				locations.put(locationKey, new HashSet<String>());
			}
			locations.get(locationKey).add(location);
		}
		plantList = new Vector<String>(lines.keySet());
		Collections.sort(plantList);
		
		plantLineMap = new HashMap<String, Vector<String>>(lines.size());
		for(Entry<String,Set<String>> entry: lines.entrySet()){
			Vector<String> linesOfPlant = new Vector<String>(entry.getValue());
			Collections.sort(linesOfPlant);
			plantLineMap.put(entry.getKey(), linesOfPlant);
		}
		
		lineLocationMap = new HashMap<String, Vector<String>>(lines.size());
		for(Entry<String,Set<String>> entry: locations.entrySet()){
			Vector<String> locationsOfLine = new Vector<String>(entry.getValue());
			Collections.sort(locationsOfLine);
			lineLocationMap.put(entry.getKey(), locationsOfLine);
		}
		shiftCandidates = propertyBean.getTeamRotationShifts();
		teamCandidates = propertyBean.getTeamRotationTeams();
	}

	/**
	 * Inits the UI Event listeners for UI Components
	 */
	private void initListeners() {
		teamRotationPanel.addMouseListener(createMouseListener());
		teamRotationPanel.getTable().addMouseListener(createMouseListener());
		teamRotationTableModel.addTableModelListener(this);
		plantComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					Vector<String> lines = plantLineMap.get(plantComboBox
							.getSelectedItem());
					lineIdComboBox.setModel(new DefaultComboBoxModel(
							lines));
					deptComboBox.setModel(new DefaultComboBoxModel());
					lineIdComboBox.getModel().setSelectedItem(null);
					if(!lines.isEmpty()){
						lineIdComboBox.getModel().setSelectedItem(lines.get(0));
					}
					
				}
			}
		});
		lineIdComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					String plant = (String)plantComboBox.getSelectedItem();
					String line = (String)lineIdComboBox.getSelectedItem();
					String key = createLocationKey(plant, line);
					Vector<String> locations = lineLocationMap.get(key);
					deptComboBox.setModel(new DefaultComboBoxModel(
							locations));
				}
			}
		});
		searchBtn.addActionListener(this);
	}

	/**
	 * Creates the mouse listener for rotation record table
	 * 
	 * @return the mouse listener
	 */
	private MouseListener createMouseListener() {
		return new PopupMenuMouseAdapter(new IPopupMenu() {
			public void showPopupMenu(MouseEvent e) {
				showOperationPopupMenu(e);
			}

			private void showOperationPopupMenu(MouseEvent e) {
				JPopupMenu popupMenu = new JPopupMenu();
				popupMenu.add(createMenuItem(CREATE_TEAM_ROTATION, true));
				popupMenu.add(createMenuItem(DELETE_TEAM_ROTATION,
						isTeamRotationSelected()));
				popupMenu.show(e.getComponent(), e.getX(), e.getY());
			}

			private boolean isTeamRotationSelected() {
				return teamRotationPanel.getTable().getSelectedRowCount() > 0;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		Object actionSource = e.getSource();

		if (actionSource instanceof JMenuItem) {
			JMenuItem menuItem = (JMenuItem) e.getSource();
			Exception exception = null;
			try {
				if (menuItem.getName().startsWith(CREATE_TEAM_ROTATION)) {
					createTeamRotation();
				} else if (menuItem.getName().startsWith(DELETE_TEAM_ROTATION)) {
					deleteTeamRotation();
				}
			} catch (Exception ex) {
				handleException(exception);
			}
		} else if (actionSource == searchBtn) {
			// search team rotations
			searchTeamRotation();
		}
	}

	/**
	 * Search team rotation.
	 */
	private void searchTeamRotation() {
		searchTeamRotation(true);
	}
	
	/**
	 * Search team rotation.
	 *
	 * @param clearErrorMessage the clear old error message if true
	 */
	private void searchTeamRotation(boolean clearErrorMessage) {
		String selectedLineId = (String)lineIdComboBox.getSelectedItem();
		String selectedPlant = (String)plantComboBox.getSelectedItem();
		String selectedDept = (String)deptComboBox.getSelectedItem();
		String selectedStart = startDateTextField.getText();
		String selectedEnd = endDateTextField.getText();
		if(clearErrorMessage){
			this.clearErrorMessage();
		}
		try {
			//Checkk if the start date is after the end date.
			DateFormat df = new SimpleDateFormat(DATE_FORMAT);
			Date startDate = df.parse(selectedStart);
			Date endDate = df.parse(selectedEnd);
			if (startDate.after(endDate)) {
				this.getMainWindow().setErrorMessage(
						"The start date shouldn't be after the end date.");
				return;
			}
			TeamRotationDao dao = ServiceFactory.getDao(TeamRotationDao.class);
			List<TeamRotation> result = dao.searchRotations(selectedLineId,
					selectedPlant, selectedDept, selectedStart, selectedEnd);
			refreshTeamRotationTable(result);
		} catch (Exception e) {
			this.getLogger().error(e, e.getMessage());
			this.getMainWindow().setErrorMessage(e.getMessage());
		}
	}

	/**
	 * Refresh team rotation table.
	 * 
	 * @param result
	 *            the result
	 */
	private void refreshTeamRotationTable(List<TeamRotation> result) {
		if(teamRotationPanel.getTable().isEditing()){
			teamRotationPanel.getTable().getCellEditor().stopCellEditing();
		}
		teamRotationTableModel.refresh(result);
	}

	/**
	 * Delete a team rotation record.
	 */
	private void deleteTeamRotation() {
		this.clearErrorMessage();
		try {
			List<TeamRotation> teamRotations = teamRotationTableModel
					.getSelectedItems();
			if (teamRotations == null || teamRotations.isEmpty()) {
				return;
			}
			//not allow users to delete the records for the past
			Date today = getToday();
			for (TeamRotation teamRotation : teamRotations) {
				if(today.after(teamRotation.getId().getProductionDate())){
					this.getMainWindow().setErrorMessage("Cannot delete the past record(s).");
					return;
				}
			}
			if (!MessageDialog
					.confirm(this,
							"Are you sure you want to delete the selected rotation record(s)?"))
				return;
			
			for (TeamRotation teamRotation : teamRotations) {
				ServiceFactory.getDao(TeamRotationDao.class).remove(teamRotation);
				logUserAction(REMOVED, teamRotation);
				teamRotationTableModel.remove(teamRotation);
			}
		} catch (Exception e) {
			this.getLogger().error(e, e.getMessage());
			this.getMainWindow().setErrorMessage(e.getMessage());
		}

	}

	/**
	 * Gets the date of today(without hours/minutes/seconds/millisconds)
	 *
	 * @return the today
	 */
	protected Date getToday() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);
		Date today = cal.getTime(); //Date without timestamp
		return today;
	}

	/**
	 * Creates the team rotation.
	 */
	private void createTeamRotation() {
		logger.info("create a team rotation");
		TeamRotationCreationDialog creationDialog = new TeamRotationCreationDialog(
				getMainWindow(),  plantList, plantLineMap, lineLocationMap,
				shiftCandidates, teamCandidates);
		if (!StringUtils.isBlank(creationDialog.getIgnoredRecord())) {
			MessageDialog
					.showError(
							this,
							"The tream rotation data were saved successfully, except for the following duplicate records: \n"
									+ creationDialog.getIgnoredRecord());
		}

		searchTeamRotation();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.honda.galc.client.ui.TabbedPanel#onTabSelected()
	 */
	@Override
	public void onTabSelected() {
		searchTeamRotation();
	}

	/**
	 * Checks if is team rotation exist.
	 * 
	 * @param data
	 *            the data
	 * @return true, if is team rotation exist
	 */
	public boolean isTeamRotationExist(TeamRotation data) {
		if (data == null) {
			return false;
		}
		TeamRotationDao dao = ServiceFactory.getDao(TeamRotationDao.class);
		TeamRotation existing = dao.findByKey(data.getId());
		return existing != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.
	 * TableModelEvent)
	 */
	public void tableChanged(TableModelEvent event) {
		if (event.getSource() == null)
			return;

		if (event.getSource() instanceof TeamRotationTableModel
				&& event.getType() == TableModelEvent.UPDATE) {
			this.clearErrorMessage();
			try {
				TeamRotationTableModel model = (TeamRotationTableModel) event
						.getSource();
				TeamRotation updatedRecord = model.getItem(event.getFirstRow());
				if (updatedRecord == null) {
					return;
				}
				TeamRotation existingRecord = (TeamRotation) updatedRecord
						.clone();
				
				int columnId = event.getColumn();
				String old = model.getCurrentValue().toString();
				boolean showUpdateError = false;
				boolean updateDB = false;

				if (columnId == 1) {
					existingRecord.getId().setPlantCode(old);
					if (!updatedRecord.getId().getPlantCode().equals(old)) {
						if (isTeamRotationExist(updatedRecord)) {
							showUpdateError = true;
						} else {
							updateDB = true;
						}
					}
				} else if (columnId == 2) {
					existingRecord.getId().setLineNo(old);
					if (!updatedRecord.getId().getLineNo().equals(old)) {
						if (isTeamRotationExist(updatedRecord)) {
							showUpdateError = true;
						} else {
							updateDB = true;
						}
					}
				} else if (columnId == 3) {
					existingRecord.getId().setProcessLocation(old);
					if (!updatedRecord.getId().getProcessLocation().equals(old)) {
						if (isTeamRotationExist(updatedRecord)) {
							showUpdateError = true;
						} else {
							updateDB = true;
						}
					}
				} else if (columnId == 4) {
					existingRecord.getId().setShift(old);
					if (!updatedRecord.getId().getShift().equals(old)) {
						if (isTeamRotationExist(updatedRecord)) {
							showUpdateError = true;
						} else {
							updateDB = true;
						}
					}
				} else if (columnId == 5) {
					existingRecord.getId().setTeam(old);
					if (!updatedRecord.getId().getTeam().equals(old)) {
						if (isTeamRotationExist(updatedRecord)) {
							showUpdateError = true;
						} else {
							updateDB = true;
						}
					}

				}
				Date today = getToday();
				if(today.after(updatedRecord.getId().getProductionDate())){
					//not allow users to change the record of the past
					updateDB = false;
					showUpdateError = false;
					this.getMainWindow().setErrorMessage("Cannot change the past record.");
				}
				if (showUpdateError) {
					showUpdateErrorMessage(updatedRecord);
				}
				if (updateDB) {
					TeamRotationDao dao = ServiceFactory
							.getDao(TeamRotationDao.class);
					dao.update(updatedRecord, existingRecord);
					logUserAction(UPDATED, updatedRecord);
				}
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						searchTeamRotation(false);
					}
				});
			} catch (Exception e) {
				this.getLogger().error(e, e.getMessage());
				this.getMainWindow().setErrorMessage(e.getMessage());
			}

		}
	}

	/**
	 * Show the error message.
	 * 
	 * @param updatedRecord
	 *            the updated record
	 */
	protected void showUpdateErrorMessage(TeamRotation updatedRecord) {
		MessageDialog.showError(this,
				"Failed to save the team rotation data as the team rotation record already exists.");
	}

}

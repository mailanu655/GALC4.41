package com.honda.galc.client.teamleader;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.dao.product.TeamRotationDao;
import com.honda.galc.entity.product.TeamRotation;
import com.honda.galc.entity.product.TeamRotationId;
import com.honda.galc.service.ServiceFactory;

/**
 * 
 * <h3>TeamRotationCreationDialog is the popup window for users to create team
 * rotation data</h3>
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
public class TeamRotationCreationDialog extends JDialog implements
		ActionListener {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -9028587122478952233L;

	/** The line id combobox. */
	private JComboBox lineIdComboBox;

	/** The plant combo box. */
	private JComboBox plantComboBox;

	/** The dept combo box. */
	private JComboBox deptComboBox;

	/** The start date text field. */
	private JTextField startDateTextField;

	/** The end date text field. */
	private JTextField endDateTextField;

	/** The shift combo box. */
	private JComboBox shiftComboBox;

	/** The team combo box. */
	private JComboBox teamComboBox;

	/** The save btn. */
	private JButton saveBtn;

	/** The cancel btn. */
	private JButton cancelBtn;

	/** The ignored record. */
	private String ignoredRecord = "";

	/** The plant line map. */
	private Map<String, Vector<String>> plantLineMap;

	/** The line location map. */
	private Map<String, Vector<String>> lineLocationMap;

	/**
	 * Instantiates a new team rotation creation dialog.
	 *
	 * @param owner the owner
	 * @param plantList the plant list
	 * @param plantLineMap the plant line map
	 * @param lineLocationMap the line location map
	 * @param shiftCandidates the shift candidates
	 * @param teamCandidates the team candidates
	 */
	public TeamRotationCreationDialog(JFrame owner, Vector<String> plantList,
			Map<String, Vector<String>> plantLineMap,
			Map<String, Vector<String>> lineLocationMap,
			String[] shiftCandidates, String[] teamCandidates) {
		super(owner, "Create Team Rotation Records", true);
		this.plantLineMap = plantLineMap;
		this.lineLocationMap = lineLocationMap;
		initComponents(plantList, shiftCandidates, teamCandidates);

		initListeners();
		
		plantComboBox.getModel().setSelectedItem(null);
		if(!plantList.isEmpty()){
			plantComboBox.getModel().setSelectedItem(plantList.get(0));
		}
		setLocationRelativeTo(owner);
		setVisible(true);
	}

	/**
	 * Inits the UI components.
	 * 
	 * @param lineIdList
	 *            the line id list
	 * @param plantList
	 *            the plant list
	 * @param deptList
	 *            the dept list
	 * @param shiftCandidates
	 *            the shift candidates
	 * @param teamCandidates
	 *            the team candidates
	 */
	protected void initComponents(Vector<String> plantList,
			String[] shiftCandidates, String[] teamCandidates) {
		setSize(280, 300);
		setLayout(new BorderLayout());
		JPanel formPanel = new JPanel();
		formPanel.setLayout(new GridBagLayout());
		GridBagConstraints titleConstraints = new GridBagConstraints();
		GridBagConstraints contentConstraints = new GridBagConstraints();

		titleConstraints.fill = GridBagConstraints.NONE;
		titleConstraints.ipadx = 10;
		titleConstraints.anchor = GridBagConstraints.EAST;
		titleConstraints.gridx = 0;
		titleConstraints.weightx = 1;
		titleConstraints.insets = new Insets(5, 5, 5, 0);
		contentConstraints.fill = GridBagConstraints.NONE;
		contentConstraints.ipadx = 10;
		contentConstraints.anchor = GridBagConstraints.WEST;
		contentConstraints.gridx = 1;
		contentConstraints.insets = new Insets(0, 5, 5, 5);
		contentConstraints.weightx = 2;

		JLabel plantLabel = new JLabel("Plant");
		titleConstraints.gridy = 0;
		formPanel.add(plantLabel, titleConstraints);
		plantComboBox = new JComboBox(plantList);
		contentConstraints.gridy = 0;
		formPanel.add(plantComboBox, contentConstraints);
		
		JLabel lineIdLabel = new JLabel("Line Id");
		titleConstraints.gridy = 1;
		formPanel.add(lineIdLabel, titleConstraints);
		lineIdComboBox = new JComboBox();
		contentConstraints.gridy = 1;
		formPanel.add(lineIdComboBox, contentConstraints);

		JLabel deptLabel = new JLabel("Dept");
		titleConstraints.gridy = 2;
		formPanel.add(deptLabel, titleConstraints);
		deptComboBox = new JComboBox();
		contentConstraints.gridy = 2;
		formPanel.add(deptComboBox, contentConstraints);

		JLabel startDateLabel = new JLabel("Start Date");
		titleConstraints.gridy = 3;
		formPanel.add(startDateLabel, titleConstraints);
		SpinnerModel startDateModel = new SpinnerDateModel();
		JSpinner startDateSpinner = new JSpinner(startDateModel);
		JComponent startDateEditor = new JSpinner.DateEditor(startDateSpinner,
				"yyyy-MM-dd");
		startDateTextField = ((JSpinner.DateEditor) startDateEditor)
				.getTextField();
		startDateTextField.setEditable(false);
		startDateTextField.setBackground(Color.WHITE);
		startDateSpinner.setEditor(startDateEditor);
		contentConstraints.gridy = 3;
		formPanel.add(startDateSpinner, contentConstraints);

		JLabel endDateLabel = new JLabel("End Date");
		titleConstraints.gridy = 4;
		formPanel.add(endDateLabel, titleConstraints);
		SpinnerModel endDateModel = new SpinnerDateModel();
		JSpinner endDateSpinner = new JSpinner(endDateModel);
		JComponent endDateEditor = new JSpinner.DateEditor(endDateSpinner,
				"yyyy-MM-dd");
		endDateTextField = ((JSpinner.DateEditor) endDateEditor).getTextField();
		endDateTextField.setEditable(false);
		endDateTextField.setBackground(Color.WHITE);
		endDateSpinner.setEditor(endDateEditor);
		contentConstraints.gridy = 4;
		formPanel.add(endDateSpinner, contentConstraints);

		JLabel shiftLabel = new JLabel("Shift");
		titleConstraints.gridy = 5;
		formPanel.add(shiftLabel, titleConstraints);
		shiftComboBox = new JComboBox(shiftCandidates);
		contentConstraints.gridy = 5;
		formPanel.add(shiftComboBox, contentConstraints);

		JLabel teamLabel = new JLabel("Team");
		titleConstraints.gridy = 6;
		formPanel.add(teamLabel, titleConstraints);
		teamComboBox = new JComboBox(teamCandidates);
		contentConstraints.gridy = 6;
		formPanel.add(teamComboBox, contentConstraints);

		this.add(formPanel, BorderLayout.CENTER);

		JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
		saveBtn = new JButton("Save");
		saveBtn.setSize(80, 35);
		btnPanel.add(saveBtn);
		cancelBtn = new JButton("Cancel");
		cancelBtn.setSize(80, 35);
		btnPanel.add(cancelBtn);

		this.add(btnPanel, BorderLayout.SOUTH);
	}

	/**
	 * Inits the listeners.
	 */
	private void initListeners() {
		saveBtn.addActionListener(this);
		cancelBtn.addActionListener(this);
		//add listeners for combobox selection changed
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
					String key = TeamRotationPanel.createLocationKey(plant, line);
					Vector<String> locations = lineLocationMap.get(key);
					deptComboBox.setModel(new DefaultComboBoxModel(
							locations));
				}
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
		if (e.getSource() == saveBtn) {
			// save new team rotation data to database.
			String errorMsg = "";
			String lineId = lineIdComboBox.getSelectedItem().toString();
			String plant = plantComboBox.getSelectedItem().toString();
			String dept = deptComboBox.getSelectedItem().toString();
			String shift = shiftComboBox.getSelectedItem().toString();
			String team = teamComboBox.getSelectedItem().toString();

			// get and validate date information
			DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
			Date startDate = null;
			Date endDate = null;
			try {
				startDate = formater.parse(startDateTextField.getText());
				endDate = formater.parse(endDateTextField.getText());
				if (startDate.after(endDate)) {
					errorMsg += "Please make sure the end date is after the start date!";
				}
			} catch (ParseException ex) {
				errorMsg += "Please make sure the start date and end date have the correct format: 'yyyy-MM-dd'";
			}

			if (!StringUtils.isBlank(errorMsg)) {
				MessageDialog.showError(errorMsg);
				return;
			}

			// create and save records (one recored per day)
			TeamRotationDao dao = ServiceFactory.getDao(TeamRotationDao.class);
			Calendar productionDate = Calendar.getInstance();
			productionDate.setTime(startDate);
			long days = (endDate.getTime() - startDate.getTime())
					/ (24 * 60 * 60 * 1000);
			for (int i = 0; i <= days; i++) {
				TeamRotationId recordId = new TeamRotationId();
				recordId.setLineNo(lineId);
				recordId.setPlantCode(plant);
				recordId.setProcessLocation(dept);
				recordId.setProductionDate(productionDate.getTime());
				recordId.setShift(shift);
				recordId.setTeam(team);
				TeamRotation record = new TeamRotation();
				record.setId(recordId);
				if (dao.findByKey(recordId) == null) {
					// no duplicate record, execute save operation
					dao.save(record);
				} else {
					// no save because there is duplicate record.
					ignoredRecord += record.toDescription() + "\n";
				}

				// move to next day
				productionDate.add(Calendar.DATE, 1);
			}
		}

		dispose();
	}

	/**
	 * Gets the ignored record.
	 * 
	 * @return the ignored record
	 */
	public String getIgnoredRecord() {
		return ignoredRecord;
	}

}

package com.honda.galc.client.teamleader;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import com.honda.galc.client.teamleader.hold.HoldUtils;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.PropertiesMapping;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.report.TableReport;
import com.honda.galc.service.ServiceFactory;

public class ProcessPointDetails extends TabbedPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	private ObjectTablePane<ProcessPoint> procPointTablePane;
	private ArrayList<ProcessPoint> allProcPoints;
	private ArrayList<ProcessPoint> filteredProcPoints;
	private List<Map<String, String>> selectedRecords;
	private LabeledComboBox siteComboBox;
	private LabeledComboBox plantComboBox;
	private LabeledComboBox divisionComboBox;
	private LabeledComboBox lineComboBox;
	private JButton exportButton;
	private JButton refreshButton;
	
	
	public ProcessPointDetails(TabbedMainWindow mainWindow){
		super("Process Details", KeyEvent.VK_Z, mainWindow);
	}
	
	@Override
	public void onTabSelected() {
		try {
			if (isInitialized)	return;
			this.initComponents();
			this.loadData();
			this.addListeners();
			isInitialized = true;
		} catch (Exception e) {
			Logger.getLogger().error(e, "Exception to start ProcessPointDetails.");
			setErrorMessage("Exception to start ProcessPointDetails panel." + e.toString());
		}	
	}
	
	private void loadData() {
		this.allProcPoints = (ArrayList<ProcessPoint>) ServiceFactory.getDao(ProcessPointDao.class).findAll();
		this.filteredProcPoints = this.allProcPoints;
		this.siteComboBox.setModel(this.getSiteNames().toArray(), -1);
		this.plantComboBox.setModel(this.getPlantNames().toArray(), -1);
		this.divisionComboBox.setModel(this.getDivisionNames().toArray(), -1);
		this.lineComboBox.setModel(this.getLineNames().toArray(), -1);
		this.resetFilter();
	}
	
	private void resetFilter() {
		this.procPointTablePane.reloadData(this.allProcPoints);
		this.siteComboBox.getComponent().setSelectedItem("*");
		this.plantComboBox.getComponent().setSelectedItem("*");
		this.divisionComboBox.getComponent().setSelectedItem("*");
		this.lineComboBox.getComponent().setSelectedItem("*");
	}
	
	private TreeSet<String> getSiteNames() {
		TreeSet<String> siteNames = new TreeSet<String>();
		for (ProcessPoint procPoint : this.allProcPoints)
			siteNames.add(procPoint.getSiteName());
		siteNames.add("*");
		return siteNames;
	}
	
	private TreeSet<String> getPlantNames() {
		TreeSet<String> plantNames = new TreeSet<String>();
		for (ProcessPoint procPoint : this.allProcPoints)
			plantNames.add(procPoint.getPlantName());
		plantNames.add("*");
		return plantNames;
	}
	
	private TreeSet<String> getDivisionNames() {
		TreeSet<String> divisionNames = new TreeSet<String>();
		for (ProcessPoint procPoint : this.allProcPoints)
			divisionNames.add(procPoint.getDivisionName());
		divisionNames.add("*");
		return divisionNames;
	}
	
	private TreeSet<String> getLineNames() {
		TreeSet<String> lineNames = new TreeSet<String>();
		for (ProcessPoint procPoint : this.allProcPoints)
			lineNames.add(procPoint.getLineName());
		lineNames.add("*");
		return lineNames;
	}
	
	private void initComponents() {
		this.setLayout(new MigLayout());
		this.add(this.createFilterPanel(),"span, wrap");
		this.add(this.createProcPointDetailsTable(),"growx, span, wrap");
		this.add(this.exportButton = this.createButton("Export Selected"),"al center, wrap");
	}
	
	private JPanel createFilterPanel() {
		JPanel panel = new JPanel(new MigLayout());
		panel.add(this.getSiteComboBox());
		panel.add(this.getPlantComboBox());
		panel.add(this.getDivisionComboBox());
		panel.add(this.getLineComboBox());
		panel.add(this.refreshButton = this.createButton("Refresh"), "gapleft 50, wrap");
		return panel;
	}
	
	private LabeledComboBox getSiteComboBox() {
		if (this.siteComboBox == null)
			this.siteComboBox = this.createComboBox("Site");
		return this.siteComboBox;
	}
	
	private LabeledComboBox getPlantComboBox() {
		if (this.plantComboBox == null)
			this.plantComboBox = this.createComboBox("Plant");
		return this.plantComboBox;
	}
	
	private LabeledComboBox getDivisionComboBox() {
		if (this.divisionComboBox == null)
			this.divisionComboBox = this.createComboBox("Division");
		return this.divisionComboBox;
	}
	
	private LabeledComboBox getLineComboBox() {
		if (this.lineComboBox == null)
			this.lineComboBox = this.createComboBox("Line");
		return this.lineComboBox;
	}
	
	private LabeledComboBox createComboBox(String title) {
		LabeledComboBox comboBox = new LabeledComboBox(title);
		comboBox.setName(title + "ComboBox");
		return comboBox;
	}
	
	private ObjectTablePane<ProcessPoint> createProcPointDetailsTable() {
		if (this.procPointTablePane == null) {
			PropertiesMapping mapping = new PropertiesMapping();
			mapping.put("ID", "processPointId");
			mapping.put("Name", "processPointName");
			mapping.put("Description", "processPointDescription");
			mapping.put("Type", "processPointType");
			mapping.put("Site", "siteName");
			mapping.put("Plant", "plantName");
			mapping.put("Division", "divisionName");
			mapping.put("Line", "lineName");
			this.procPointTablePane = new ObjectTablePane<ProcessPoint>(mapping.get(), true, true);
			this.procPointTablePane.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			this.procPointTablePane.getTable().setName("ProcPointTable");
			this.procPointTablePane.getTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			this.procPointTablePane.setBorder(new TitledBorder("Process Points"));
			this.procPointTablePane.setPreferredSize(new Dimension(9999,9999));
		}
		return this.procPointTablePane;
	}
	
	private JButton createButton(String name) {
		JButton button = new JButton(name);
		button.setName(name + "Button");
		button.setMinimumSize(new Dimension(150, 30));
		return button;
	}
	
	private void filterProcPoints(){
		this.filteredProcPoints = new ArrayList<ProcessPoint>();
		String selectedSite = this.siteComboBox.getComponent().getSelectedItem().toString().trim();
		String selectedPlant = this.plantComboBox.getComponent().getSelectedItem().toString().trim();
		String selectedDivision = this.divisionComboBox.getComponent().getSelectedItem().toString().trim();
		String selectedLine = this.lineComboBox.getComponent().getSelectedItem().toString().trim();
		for (ProcessPoint procPoint : this.allProcPoints) {
			if ((procPoint.getSiteName().trim().equals(selectedSite) || selectedSite.equals("*")) &&
				(procPoint.getPlantName().trim().equals(selectedPlant) || selectedPlant.equals("*")) &&
				(procPoint.getDivisionName().trim().equals(selectedDivision) || selectedDivision.equals("*")) &&
				(procPoint.getLineName().trim().equals(selectedLine) || selectedLine.equals("*")))
				this.filteredProcPoints.add(procPoint);
		}
		this.procPointTablePane.reloadData(this.filteredProcPoints);
	}
	
	private List<Map<String,String>> getSelectedRecords(){
		this.selectedRecords = new ArrayList<Map<String,String>>();
		JTable table = this.procPointTablePane.getTable();
		int[] selectedIdxList = this.procPointTablePane.getTable().getSelectedRows();
		for (int selectedIdx : selectedIdxList) {
			HashMap<String,String> selectedRecord= new HashMap<String,String>();
			selectedRecord.put("ID",			table.getValueAt(selectedIdx, 0).toString());
			selectedRecord.put("Name",			table.getValueAt(selectedIdx, 1).toString());
			selectedRecord.put("Description",	table.getValueAt(selectedIdx, 2).toString());
			selectedRecord.put("Type",			table.getValueAt(selectedIdx, 3).toString());
			selectedRecord.put("Site",			table.getValueAt(selectedIdx, 4).toString());
			selectedRecord.put("Plant",			table.getValueAt(selectedIdx, 5).toString());
			selectedRecord.put("Division",		table.getValueAt(selectedIdx, 6).toString());
			selectedRecord.put("Line",			table.getValueAt(selectedIdx, 7).toString());
			this.selectedRecords.add(selectedRecord);
		}
		return this.selectedRecords;
	}
	
	private void exportRecords() {
		if (this.procPointTablePane.getTable().getSelectedRows().length == 0) {
			MessageDialog.showInfo(this, "No records selected.");
			return;
		}
		String fileName = "ProcessPointDetails_"+ new SimpleDateFormat("yyyy-MM-dd-HHmmss").format(new Date()) + ".xlsx";
		File file = HoldUtils.popupSaveDialog(this, System.getProperty("user.home"), fileName);
		if (file == null || file.getAbsolutePath() == null)	return;
		this.createProcPointDetailsReport().export(file.getAbsolutePath());
	}
	
	private TableReport createProcPointDetailsReport() {
		TableReport report = TableReport.createXlsxTableReport();
		report.setTitle("Process Points");
		report.addColumn("#", "#", 1000);
		report.addColumn("ID", "ID", 3000);
		report.addColumn("Name", "Name", 3000);
		report.addColumn("Description", "Description", 7000);
		report.addColumn("Type", "Type", 3000);
		report.addColumn("Site", "Site", 3000);
		report.addColumn("Plant", "Plant", 3000);
		report.addColumn("Division", "Division", 3000);
		report.addColumn("Line", "Line", 3000);
		List<Map<String, String>> rows = new ArrayList<Map<String, String>>();
		int idx = 0;
		for (Map<String,String> row : this.getSelectedRecords()) {
			idx++;
			row.put("#", Integer.toString(idx));
			rows.add(row);
		}
		report.setData(rows);
		return report;
	}
	
	private void addListeners() {
		this.siteComboBox.getComponent().addActionListener(this);
		this.plantComboBox.getComponent().addActionListener(this);
		this.divisionComboBox.getComponent().addActionListener(this);
		this.lineComboBox.getComponent().addActionListener(this);
		this.exportButton.addActionListener(this);
		this.refreshButton.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(this.siteComboBox.getComponent())) {
			this.plantComboBox.getComponent().setSelectedItem("*");
			this.filterProcPoints();
		} else if (e.getSource().equals(this.plantComboBox.getComponent())) {
			this.divisionComboBox.getComponent().setSelectedItem("*");
			this.filterProcPoints();
		} else if (e.getSource().equals(this.divisionComboBox.getComponent())) {
			this.lineComboBox.getComponent().setSelectedItem("*");
			this.filterProcPoints();
		} else if (e.getSource().equals(this.lineComboBox.getComponent())) {
			this.filterProcPoints();
		} else if (e.getSource().equals(this.exportButton)) {
			this.exportRecords();
		} else if (e.getSource().equals(this.refreshButton)) {
			this.resetFilter();
		}
	}
}

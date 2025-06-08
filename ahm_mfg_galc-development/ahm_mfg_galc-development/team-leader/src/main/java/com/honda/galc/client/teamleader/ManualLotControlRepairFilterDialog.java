package com.honda.galc.client.teamleader;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.component.LabeledListBox;
import com.honda.galc.client.ui.component.ListModel;
import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.dao.conf.LineDao;
import com.honda.galc.dao.conf.ZoneDao;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.Zone;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.service.ServiceFactory;

public class ManualLotControlRepairFilterDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel filterPanel, divisionButtonPanel, lineButtonPanel, zoneButtonPanel, statusButtonPanel;
	private JButton filterButton, divisionAddButton, divisionRemoveButton, lineAddButton, lineRemoveButton, zoneAddButton, zoneRemoveButton, statusAddButton, statusRemoveButton;
	private LabeledListBox divisionList, lineList, zoneList, statusList, selectedDivisionList, selectedLineList, selectedZoneList, selectedStatusList;
	private ManualLotControlRepairController controller;
	private static String DISPLAY_PATTERN = "%s-%s";

	public ManualLotControlRepairFilterDialog(Frame owner,
			ManualLotControlRepairController controller) {
		super(owner, "Filter", true);
		this.controller = controller;
		initComponents();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == divisionAddButton) {
			handleListSelection(divisionList, selectedDivisionList);
		}
		if (e.getSource() == divisionRemoveButton) {
			handleListSelection(selectedDivisionList, divisionList);
		}
		if (e.getSource() == lineAddButton) {
			handleListSelection(lineList, selectedLineList);
		}
		if (e.getSource() == lineRemoveButton) {
			handleListSelection(selectedLineList, lineList);
		}
		if (e.getSource() == zoneAddButton) {
			handleListSelection(zoneList, selectedZoneList);
		}
		if (e.getSource() == zoneRemoveButton) {
			handleListSelection(selectedZoneList, zoneList);
		}
		if (e.getSource() == statusAddButton) {
			handleListSelection(statusList, selectedStatusList);
		}
		if (e.getSource() == statusRemoveButton) {
			handleListSelection(selectedStatusList, statusList);
		}

		if (e.getSource() == filterButton) {
			controller.getView().getSelectedDivisionLabel().setText(getSelectedDivisionId());
			controller.getView().getSelectedLineLabel().setText(getSelectedLineId());
			controller.getView().getSelectedZoneLabel().setText(getSelectedZoneId());
			controller.getView().getSelectedStatusLabel().setText(getSelectedStatusId());
			controller.filterPartResults();
			this.dispose();
		}

	}

	@SuppressWarnings("unchecked")
	private void handleListSelection(LabeledListBox sourceList, LabeledListBox targetList) {
		Object selected = sourceList.getComponent().getSelectedValue();
		if (selected == null) {
			return;
		}
		ListModel<Object> sourceModel = (ListModel<Object>) sourceList.getComponent().getModel();
		ListModel<Object> targetModel = (ListModel<Object>) targetList.getComponent().getModel();
		sourceModel.remove(selected);
		targetModel.add(selected);
		sourceList.setModel(sourceModel, 0);
		targetList.setModel(targetModel, 0);
		targetModel.sort();
	}

	private void initComponents() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
		panel.setSize(850, 700);
		panel.add(getFilterPanel());
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setViewportView(panel);
		add(scrollPane, BorderLayout.CENTER);
		setSize(850, 700);
	}

	private LabeledListBox getDivisionListBox() {
		if (divisionList == null) {
			divisionList = new LabeledListBox("Division", false);
			divisionList.setModel(createDivisionListModel(getDivisionList()), 0);
			divisionList.setSize(100, 80);
			((ListModel<Division>) divisionList.getComponent().getModel()).sort();
		}
		return divisionList;
	}
	
	private LabeledListBox getSelectedDivisionListBox() {
		if (selectedDivisionList == null) {
			selectedDivisionList = new LabeledListBox("Selected Division", false);
			selectedDivisionList.setModel(createDivisionListModel(getDefaultSelectedDivisionList()), 0);
			((ListModel<Division>) selectedDivisionList.getComponent().getModel()).sort();
		}
		return selectedDivisionList;
	}

	private LabeledListBox getLineListBox() {
		if (lineList == null) {
			lineList = new LabeledListBox("Line", false);
			lineList.setModel(createLineListModel(getLineList()), 0);
			lineList.setSize(100, 80);
			((ListModel<Line>) lineList.getComponent().getModel()).sort();
		}

		return lineList;
	}

	private LabeledListBox getSelectedLineListBox() {
		if (selectedLineList == null) {
			selectedLineList = new LabeledListBox("Selected Line", false);
			selectedLineList.setModel(createLineListModel(getDefaultSelectedLineList()), 0);
			((ListModel<Line>) selectedLineList.getComponent().getModel()).sort();
		}

		return selectedLineList;
	}
	
	private LabeledListBox getZoneListBox() {
		if (zoneList == null) {
			zoneList = new LabeledListBox("Zone", false);
			zoneList.setModel(createZoneListModel(getZoneList()), 0);
			zoneList.setSize(100, 80);
			((ListModel<Zone>) zoneList.getComponent().getModel()).sort();
		}

		return zoneList;
	}

	private LabeledListBox getSelectedZoneListBox() {
		if (selectedZoneList == null) {
			selectedZoneList = new LabeledListBox("Selected Zone", false);
			selectedZoneList.setModel(createZoneListModel(getDefaultSelectedZoneList()), 0);
			((ListModel<Zone>) selectedZoneList.getComponent().getModel()).sort();
		}

		return selectedZoneList;
	}
	
	private LabeledListBox getStatusListBox() {
		if (statusList == null) {
			statusList = new LabeledListBox("Status", false);
			statusList.setModel(createStatusListModel(getStatusList()), 0);
			statusList.setSize(100, 80);
			((ListModel<InstalledPartStatus>) statusList.getComponent().getModel()).sort();
		}
		return statusList;
	}
	
	private LabeledListBox getSelectedStatusListBox() {
		if (selectedStatusList == null) {
			selectedStatusList = new LabeledListBox("Selected Status", false);
			selectedStatusList.setModel(createStatusListModel(getDefaultSelectedStatusList()), 0);
			((ListModel<InstalledPartStatus>) selectedStatusList.getComponent().getModel()).sort();
		}

		return selectedStatusList;
	}

	private Component getFilterPanel() {
		if (filterPanel == null) {
			filterPanel = new JPanel();
			filterPanel.setLayout(new GridLayout(5, 1, 20, 10));

			JPanel divisionPanel = new JPanel();
			divisionPanel.setLayout(new GridLayout(1,3, 2, 10));
			divisionPanel.add(getDivisionListBox());
			divisionPanel.add(getDivisionButtonPanel());
			divisionPanel.add(getSelectedDivisionListBox());

			JPanel areaPanel = new JPanel();
			areaPanel.setLayout(new GridLayout(1,3, 2, 10));
			areaPanel.add(getLineListBox());
			areaPanel.add(getLineButtonPanel());
			areaPanel.add(getSelectedLineListBox());
			
			JPanel zonePanel = new JPanel();
			zonePanel.setLayout(new GridLayout(1,3, 2, 10));
			zonePanel.add(getZoneListBox());
			zonePanel.add(getZoneButtonPanel());
			zonePanel.add(getSelectedZoneListBox());
			
			JPanel statusPanel = new JPanel();
			statusPanel.setLayout(new GridLayout(1, 3, 2, 10));
			statusPanel.add(getStatusListBox());
			statusPanel.add(getStatusButtonPanel());
			statusPanel.add(getSelectedStatusListBox());

			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
			buttonPanel.add(getFilterButton());

			filterPanel.add(divisionPanel);
			filterPanel.add(areaPanel);
			filterPanel.add(zonePanel);
			filterPanel.add(statusPanel);
			filterPanel.add(buttonPanel);
			
		}
		return filterPanel;
	}

	private JButton getFilterButton() {
		if (filterButton == null) {
			filterButton = new JButton("OK");
			filterButton.setName("OK");
			filterButton.setSize(50, 20);
			filterButton.addActionListener(this);
		}
		return filterButton;
	}

	private JPanel getLineButtonPanel() {
		if (lineButtonPanel == null) {
			lineButtonPanel = new JPanel();
			lineButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50,50));
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			lineAddButton = new JButton(" >> ");
			lineAddButton.setName("add");
			lineAddButton.addActionListener(this);
			panel.add(lineAddButton);
			panel.add(new JLabel("  "));
			lineRemoveButton = new JButton(" << ");
			lineRemoveButton.setName("remove");
			lineRemoveButton.addActionListener(this);
			panel.add(lineRemoveButton);
			lineButtonPanel.add(panel);
		}
		return lineButtonPanel;
	}

	private JPanel getZoneButtonPanel() {
		if (zoneButtonPanel == null) {
			zoneButtonPanel = new JPanel();
			zoneButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50,50));
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			zoneAddButton = new JButton(" >> ");
			zoneAddButton.setName("add");
			zoneAddButton.addActionListener(this);
			panel.add(zoneAddButton);
			panel.add(new JLabel("  "));
			zoneRemoveButton = new JButton(" << ");
			zoneRemoveButton.setName("remove");
			zoneRemoveButton.addActionListener(this);
			panel.add(zoneRemoveButton);
			zoneButtonPanel.add(panel);
		}
		return zoneButtonPanel;
	}
	
	private JPanel getDivisionButtonPanel() {
		if (divisionButtonPanel == null) {
			divisionButtonPanel = new JPanel();
			divisionButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50,50));
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			divisionAddButton = new JButton(" >> ");
			divisionAddButton.setName("add");
			divisionAddButton.addActionListener(this);
			panel.add(divisionAddButton);
			panel.add(new JLabel("   "));
			divisionRemoveButton = new JButton(" << ");
			divisionRemoveButton.setName("remove");
			divisionRemoveButton.addActionListener(this);
			panel.add(divisionRemoveButton);
			divisionButtonPanel.add(panel);
		}
		return divisionButtonPanel;
	}
	
	private JPanel getStatusButtonPanel() {
		if (statusButtonPanel == null) {
			statusButtonPanel = new JPanel();
			statusButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50,50));
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			statusAddButton = new JButton(" >> ");
			statusAddButton.setName("add");
			statusAddButton.addActionListener(this);
			panel.add(statusAddButton);
			panel.add(new JLabel("   "));
			statusRemoveButton = new JButton(" << ");
			statusRemoveButton.setName("remove");
			statusRemoveButton.addActionListener(this);
			panel.add(statusRemoveButton);
			statusButtonPanel.add(panel);
		}
		return statusButtonPanel;
	}

	@SuppressWarnings("unchecked")
	private List<Division> getDivisionList() {
		List<Division> divisions = controller.getDivisions();
		if (divisions == null || divisions.isEmpty()) {
			DivisionDao divisionDao = ServiceFactory.getDao(DivisionDao.class);
			divisions = divisionDao.findAll();
			controller.setDivisions(divisions);
		}
		return divisions;
	}

	@SuppressWarnings("unchecked")
	private List<Line> getLineList() {
		List<Line> lines = controller.getLines();
		if (lines == null || lines.isEmpty()) {
			LineDao lineDao = ServiceFactory.getDao(LineDao.class);
			lines = lineDao.findAll();
			controller.setLines(lines);
		}
		return lines;
	}
	
	@SuppressWarnings("unchecked")
	private List<Zone> getZoneList() {
		List<Zone> zones = controller.getZones();
		if (zones == null || zones.isEmpty()) {
			ZoneDao zoneDao = ServiceFactory.getDao(ZoneDao.class);
			zones = zoneDao.findAll();
			controller.setZones(zones);
		}
		return zones;
	}
	
	@SuppressWarnings("unchecked")
	private List<InstalledPartStatus> getStatusList() {
		List<InstalledPartStatus> statuses = controller.getStatuses();
		if (statuses == null || statuses.isEmpty()) {
			statuses = Arrays.asList(InstalledPartStatus.values());
			controller.setStatuses(statuses);
		}
		return statuses;
	}

	private List<Division> getDefaultSelectedDivisionList() {
		List<Division> divisionList = new ArrayList<Division>();
		String selectionDivisionString = controller.getView().getSelectedDivisionLabel().getText();
		if (selectionDivisionString != null) {
			String[] tempList = selectionDivisionString.split(",");
			for (String tempId : tempList) {
				Division division = findDivisionById(getDivisionListBox(), tempId);
				if (division != null) {
					removeItem(getDivisionListBox(), division);
					divisionList.add(division);
				}
			}
		}
		return divisionList;
	}
	
	private List<Line> getDefaultSelectedLineList() {
		List<Line> lineList = new ArrayList<Line>();
		String selectionLineString = controller.getView().getSelectedLineLabel().getText();
		if (selectionLineString != null) {
			String[] tempList = selectionLineString.split(",");
			for (String tempId : tempList) {
				Line line = findLineById(getLineListBox(), tempId);
				if (line != null) {
					removeItem(getLineListBox(), line);
					lineList.add(line);
				}
			}
		}
		return lineList;
	}
	
	private List<Zone> getDefaultSelectedZoneList() {
		List<Zone> zoneList = new ArrayList<Zone>();
		String selectionZoneString = controller.getView().getSelectedZoneLabel().getText();
		if (selectionZoneString != null) {
			String[] tempList = selectionZoneString.split(",");
			for (String tempId : tempList) {
				Zone zone = findZoneById(getZoneListBox(), tempId);
				if (zone != null) {
					removeItem(getZoneListBox(), zone);
					zoneList.add(zone);
				}
			}
		}
		return zoneList;
	}
	
	private List<InstalledPartStatus> getDefaultSelectedStatusList() {
		List<InstalledPartStatus> statusList = new ArrayList<InstalledPartStatus>();
		String selectionStatusString = controller.getView().getSelectedStatusLabel().getText();
		if (selectionStatusString != null) {
			String[] tempList = selectionStatusString.split(",");
			for (String tempId : tempList) {
				InstalledPartStatus status = findStatusById(getStatusListBox(), tempId);
				if (status != null) {
					removeItem(getStatusListBox(), status);
					statusList.add(status);
				}
			}
		}
		return statusList;
	}
	
	@SuppressWarnings("unchecked")
	private String getSelectedDivisionId() {
		String selectedDivisionString = "";
		ListModel<Division> model = (ListModel<Division>) selectedDivisionList.getComponent().getModel();
		for (int i = 0; i < model.getSize(); i++) {
			Division div = model.getElementAt(i);
			if (div == null) {
				continue;
			}
			selectedDivisionString = selectedDivisionString + div.getId();			
			if (i < (model.getSize() - 1)) {
				selectedDivisionString = selectedDivisionString + ",";
			}
		}
		return selectedDivisionString;
	}

	@SuppressWarnings("unchecked")
	private String getSelectedLineId() {
		String selectedLineString = "";
		ListModel<Line> model = (ListModel<Line>) selectedLineList.getComponent().getModel();
		for (int i = 0; i < model.getSize(); i++) {
			Line line = model.getElementAt(i);
			selectedLineString = selectedLineString + line.getId();
			if (i < (model.getSize() - 1)) {
				selectedLineString = selectedLineString + ",";
			}
		}
		return selectedLineString;
	}
	
	@SuppressWarnings("unchecked")
	private String getSelectedZoneId() {
		String selectedZoneString = "";
		ListModel<Zone> model = (ListModel<Zone>) selectedZoneList.getComponent().getModel();
		for (int i = 0; i < model.getSize(); i++) {
			Zone zone = model.getElementAt(i);
			selectedZoneString = selectedZoneString + zone.getZoneId();
			if (i < (model.getSize() - 1)) {
				selectedZoneString = selectedZoneString + ",";
			}
		}
		return selectedZoneString;
	}
	
	@SuppressWarnings("unchecked")
	private String getSelectedStatusId() {
		String selectedStatusString = "";
		ListModel<InstalledPartStatus> model = (ListModel<InstalledPartStatus>) selectedStatusList.getComponent().getModel();
		for (int i = 0; i < model.getSize(); i++) {
			InstalledPartStatus status = model.getElementAt(i);
			selectedStatusString = selectedStatusString + status.toString();
			if (i < (model.getSize() - 1)) {
				selectedStatusString = selectedStatusString + ",";
			}
		}
		return selectedStatusString;
	}
	
	// === utility methods === //
	@SuppressWarnings("unchecked")
	protected Division findDivisionById(LabeledListBox listBox, String id) {
		if (listBox == null || id == null) {
			return null;
		}
		ListModel<Division> model = (ListModel<Division>) listBox.getComponent().getModel();
		int size = model.getSize();
		for (int i = 0; i < size; i++) {
			Division o = model.getElementAt(i);
			if (o == null) {
				continue;
			}
			if (o.getId().equals(StringUtils.trim(id))) {
				return o;
			}
		}
		return null;
	}	
	
	@SuppressWarnings("unchecked")
	protected Line findLineById(LabeledListBox listBox, String id) {
		if (listBox == null || id == null) {
			return null;
		}
		ListModel<Line> model = (ListModel<Line>)listBox.getComponent().getModel();
		int size = model.getSize();
		for (int i = 0; i < size; i++) {
			Line o = model.getElementAt(i);
			if (o == null) {
				continue;
			}
			if (o.getId().equals(StringUtils.trim(id))) {
				return o;
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	protected Zone findZoneById(LabeledListBox listBox, String id) {
		if (listBox == null || id == null) {
			return null;
		}
		ListModel<Zone> model = (ListModel<Zone>)listBox.getComponent().getModel();
		int size = model.getSize();
		for (int i = 0; i < size; i++) {
			Zone o = model.getElementAt(i);
			if (o == null) {
				continue;
			}
			if (o.getZoneId().equals(StringUtils.trim(id))) {
				return o;
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	protected InstalledPartStatus findStatusById(LabeledListBox listBox, String id) {
		if (listBox == null || id == null) {
			return null;
		}
		ListModel<InstalledPartStatus> model = (ListModel<InstalledPartStatus>)listBox.getComponent().getModel();
		int size = model.getSize();
		for (int i = 0; i < size; i++) {
			InstalledPartStatus status = model.getElementAt(i);
			if (status == null) {
				continue;
			}
			if (status.toString().equals(StringUtils.trim(id))) {
				return status;
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	protected void removeItem(LabeledListBox listBox, Object item) {
		if (listBox == null || item == null) {
			return;
		}
		ListModel<Object> model = (ListModel<Object>)listBox.getComponent().getModel();
		model.remove(item);
	}
	
	// === utility/factory methods === //
	protected ListModel<Division> createDivisionListModel(List<Division> data) {
		List<Division> list = new ArrayList<Division>(data);
		ListModel<Division> model = new ListModel<Division>(list){
			private static final long serialVersionUID = 1L;
			@Override
			protected String getDisplayObject(Division object) {
				if (object == null) {
					return super.getDisplayObject(object);
				}
				String view = String.format(DISPLAY_PATTERN, object.getDivisionName(), object.getDivisionId());
				return view;
			}
		};
		model.setComparator(new Comparator<Division>() {
			public int compare(Division div1, Division div2) {
				return String.format(DISPLAY_PATTERN, div1.getDivisionName(), div1.getDivisionId()).compareToIgnoreCase(String.format(DISPLAY_PATTERN, div2.getDivisionName(), div2.getDivisionId()));
			}
		});
		return model;
	}
	
	protected ListModel<Line> createLineListModel(List<Line> data) {
		List<Line> list = new ArrayList<Line>(data);
		ListModel<Line> model = new ListModel<Line>(list){
			private static final long serialVersionUID = 1L;
			@Override
			protected String getDisplayObject(Line object) {
				if (object == null) {
					return super.getDisplayObject(object);
				}
				String view = String.format(DISPLAY_PATTERN, object.getLineName(), object.getLineId());
				return view;
			}
		};
		model.setComparator(new Comparator<Line>() {
			public int compare(Line line1, Line line2) {
				return String.format(DISPLAY_PATTERN, line1.getLineName(), line1.getLineId()).compareToIgnoreCase(String.format(DISPLAY_PATTERN, line2.getLineName(), line2.getLineId()));
			}
		});
		return model;
 	}
	
	protected ListModel<Zone> createZoneListModel(List<Zone> data) {
		List<Zone> list = new ArrayList<Zone>(data);
		ListModel<Zone> model = new ListModel<Zone>(list){
			private static final long serialVersionUID = 1L;
			@Override
			protected String getDisplayObject(Zone object) {
				if (object == null) {
					return super.getDisplayObject(object);
				}
				String view = String.format(DISPLAY_PATTERN, object.getZoneDescription(), object.getZoneId());
				return view;
			}
		};
		model.setComparator(new Comparator<Zone>(){
			public int compare(Zone zone1, Zone zone2) {
				return String.format(DISPLAY_PATTERN, zone1.getZoneId(), zone1.getZoneDescription()).compareToIgnoreCase(String.format(DISPLAY_PATTERN, zone2.getZoneId(), zone2.getZoneDescription()));
			}
		});
		return model;
 	}
	
	protected ListModel<InstalledPartStatus> createStatusListModel(List<InstalledPartStatus> data) {
		List<InstalledPartStatus> list = new ArrayList<InstalledPartStatus>(data);
		ListModel<InstalledPartStatus> model = new ListModel<InstalledPartStatus>(list){
			private static final long serialVersionUID = 1L;
			@Override
			protected String getDisplayObject(InstalledPartStatus status) {
				if (status == null) {
					return super.getDisplayObject(status);
				}
				String view = status.toString();
				return view;
			}
		};
		model.setComparator(new Comparator<InstalledPartStatus>() {
			public int compare(InstalledPartStatus status1, InstalledPartStatus status2) {
				return status1.toString().compareToIgnoreCase(status2.toString());
			}
		});
		return model;
	}
}

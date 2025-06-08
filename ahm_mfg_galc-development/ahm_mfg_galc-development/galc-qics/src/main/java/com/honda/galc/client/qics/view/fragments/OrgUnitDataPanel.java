package com.honda.galc.client.qics.view.fragments;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.honda.galc.client.ui.component.ComboBoxModel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.Zone;

/**
 * 
 * <h3>OrgUnitDataPanel Class description</h3>
 * <p> OrgUnitDataPanel description </p>
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
 * @author Jeffray Huang<br>
 * Jun 17, 2011
 *
 *
 */

public class OrgUnitDataPanel extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1916685720537127278L;

	private LabeledComboBox departmentComboBox;
	private LabeledComboBox lineComboBox;
	private LabeledComboBox zoneComboBox;
 	
	public OrgUnitDataPanel() {
		super();
		initialize();
		
		addActionListeners();
		
	}
	
	protected void initialize() {
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		add(getDepartmentComboBox());
		add(getLineComboBox());
		add(getZoneComboBox());
		
	}

	
	public void addActionListeners() {
	      getDepartmentComboBox().getComponent().addActionListener(this);
	      getLineComboBox().getComponent().addActionListener(this);
	      getZoneComboBox().getComponent().addActionListener(this);
	}
	
	
	public void actionPerformed(ActionEvent e) {

		if(e.getSource().equals(getDepartmentComboBox().getComponent())) departmentChanged();
	        
	}

	public void departmentChanged() {
        
		Division division = (Division) getDepartmentComboBox().getComponent().getSelectedItem();
        if(division == null) return;
        getLineComboBox().setModel(new  ComboBoxModel<Line>(division.getLines(),"getLineName"),0);
        getZoneComboBox().setModel(new  ComboBoxModel<Zone>(division.getZones(),"getZoneName"),0);
        
	}
	
	public void loadDepartments(List<Division> divisions) {
		
		getDepartmentComboBox().setModel(new  ComboBoxModel<Division>(divisions,"getDivisionName"),0);
        
	}
	
	
	public void setSelectedDepartment(String department) {

		int count = getDepartmentComboBox().getComponent().getItemCount();
		for(int i = 0; i< count; i++) {
			Division division = (Division)getDepartmentComboBox().getComponent().getItemAt(i);
			if(division.getDivisionId().equalsIgnoreCase(department)) {
				getDepartmentComboBox().getComponent().setSelectedItem(division);
				return;
			}
		}

	}
	
	public void setSelectedLine(String lineName) {
		int count = getLineComboBox().getComponent().getItemCount();
		for(int i = 0; i< count; i++) {
			Line line = (Line)getLineComboBox().getComponent().getItemAt(i);
			if(line.getLineId().equalsIgnoreCase(lineName)) {
				getDepartmentComboBox().getComponent().setSelectedItem(line);
				return;
			}
		}
	}

	public void setSelectedZone(String zoneName) {
		int count = getZoneComboBox().getComponent().getItemCount();
		for(int i = 0; i< count; i++) {
			Zone zone = (Zone)getZoneComboBox().getComponent().getItemAt(i);
			if(zone.getZoneId().equalsIgnoreCase(zoneName)) {
				getDepartmentComboBox().getComponent().setSelectedItem(zone);
				return;
			}
		}
	}
	
	public String getSelectedDepartment() {
		return ((Division)getDepartmentComboBox().getComponent().getSelectedItem()).getDivisionId();
	}
	
	/**
	 * Gets the selected department name.
	 * Use Department Name instead of ID
	 * @return the selected department name
	 */
	public String getSelectedDepartmentName() {
		return ((Division)getDepartmentComboBox().getComponent().getSelectedItem()).getDivisionName();
	}
	
	public String getSelectedLine() {
		return ((Line)getLineComboBox().getComponent().getSelectedItem()).getLineId();
	}
	
	/**
	 * Gets the selected line name.
	 * Use Line Name instead of ID
	 * @return the selected line name
	 */
	public String getSelectedLineName() {
		return ((Line)getLineComboBox().getComponent().getSelectedItem()).getLineName();
	}

	public String getSelectedZone() {
		return ((Zone)getZoneComboBox().getComponent().getSelectedItem()).getZoneId();
	}	
	
	/**
	 * Gets the selected zone name.
	 * Use Zone Name instead of ID
	 * @return the selected zone name
	 */
	public String getSelectedZoneName() {
		return ((Zone)getZoneComboBox().getComponent().getSelectedItem()).getZoneName();
	}

	
	protected LabeledComboBox getDepartmentComboBox() {
		if (departmentComboBox == null) {
            departmentComboBox = new LabeledComboBox("Dept");
            departmentComboBox.setFont(Fonts.DIALOG_PLAIN_18);
            departmentComboBox.setLabelPreferredWidth(60);
            departmentComboBox.getComponent().setPreferredSize(new Dimension(250,20));

        }
        return departmentComboBox;
	}
	
	protected LabeledComboBox getLineComboBox() {
		if (lineComboBox == null) {
			lineComboBox = new LabeledComboBox("Line");
			lineComboBox.setFont(Fonts.DIALOG_PLAIN_18);
			lineComboBox.setLabelPreferredWidth(60);
            lineComboBox.getComponent().setPreferredSize(new Dimension(250,20));

        }
        return lineComboBox;
	}
	
	protected LabeledComboBox getZoneComboBox() {
		if (zoneComboBox == null) {
			zoneComboBox = new LabeledComboBox("Zone");
			zoneComboBox.setFont(Fonts.DIALOG_PLAIN_18);
			zoneComboBox.setLabelPreferredWidth(60);
            zoneComboBox.getComponent().setPreferredSize(new Dimension(250,20));
        }
		return zoneComboBox;
	}
	

}

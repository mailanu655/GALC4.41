package com.honda.galc.client.teamleader;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTextField;
import com.honda.galc.client.ui.component.LabeledComboBox;

import net.miginfocom.swing.MigLayout;

public class DepartmentSelectionPanel extends JPanel{
    
    private static final long serialVersionUID = 1L;
    
    private LabeledComboBox plantComboBox;
    private LabeledComboBox lineComboBox;
    private LabeledComboBox departmentComboBox;

    
    public DepartmentSelectionPanel() {
        super();
        initComponent();
    }
    
    public void initComponent() {
        setLayout(new MigLayout("insets 6 5 6 0"));
        add(getPlantComboBox(),"gapright 5");
        add(getLineComboBox(),"aligny bottom, gapright 5");
        add(getDepartmentComboBox(),"aligny bottom");
    }

	public LabeledComboBox getPlantComboBox() {
	  if (plantComboBox == null)
	      plantComboBox = this.getLabeledComboBox("Plant");
	  return plantComboBox;
	}

	public LabeledComboBox getDepartmentComboBox() {
	  if (departmentComboBox == null)
	      departmentComboBox = this.getLabeledComboBox("Dept.");
	  return departmentComboBox;
	}

	public LabeledComboBox getLineComboBox() {
	  if (lineComboBox == null)
	      lineComboBox = this.getLabeledComboBox("Line");
	  return lineComboBox;
	}

	private LabeledComboBox getLabeledComboBox(String label) {
		LabeledComboBox comboBox = new LabeledComboBox(label,false);
		comboBox.getLabel().setHorizontalAlignment(JTextField.CENTER);
		comboBox.getComponent().setName(label + "ComboBox");
		comboBox.setBorder(null);
		((BorderLayout)comboBox.getLayout()).setVgap(3);
		return comboBox;
	}
    
    public List<String> getSelected(){
    	List<String> list = new ArrayList<String>();
    	list.add(getPlantComboBox().getComponent().getSelectedItem().toString());
    	list.add(getLineComboBox().getComponent().getSelectedItem().toString());
    	list.add(getDepartmentComboBox().getComponent().getSelectedItem().toString());
    	return list;
    }
}

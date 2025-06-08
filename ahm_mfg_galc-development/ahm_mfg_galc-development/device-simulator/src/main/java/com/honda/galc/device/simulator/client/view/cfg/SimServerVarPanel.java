package com.honda.galc.device.simulator.client.view.cfg;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.honda.galc.client.ui.component.SortableTableModel;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.device.simulator.client.view.data.ConfigValueObject;

import java.awt.event.KeyEvent;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.Font;

/**
 * <h3>SimServerVarPanel</h3>
 * <h4> </h4>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Sep. 23, 2008</TD>
 * <TD>Version 0.2</TD>
 * <TD></TD>
 * <TD>Initial version.</TD>
 * </TR>
 * </TABLE>
 */

public class SimServerVarPanel extends SimulatorVarPanel {
	private static final long serialVersionUID = -107623605281952464L;
	private JScrollPane jScrollPaneListTable = null;
	private JTable jTableTags = null;
	List <TagValue> tagValues = new ArrayList<TagValue>();
	private JCheckBox newRspClientId = null;
	private JPanel useRspClientPanel;
	private JTextField rspClientId;
	private JButton addButton;
	private JButton delButton;
	private JPanel buttonPanel;
	protected int selectedRow;
	private JTextArea servNoteTxtArea = null;
	private JPanel rspClientPanel;
	private TagValueTableModel tableModel;

	public SimServerVarPanel() {
		super();
		
		setLayout(new GridBagLayout());
		setName("Simulator Server");
		initialize();
	}

	private void initialize() {
		GridBagConstraints c1 = getConstraint(0, 0, 1);
		add(getRspClientPanel(), c1);
		
		GridBagConstraints c2 = getConstraint(0, 1, 1);
		c2.insets = new Insets(10, 10, 10, 10);
		add(getJScrollPaneListTable(), c2);
		
		GridBagConstraints c3 = getConstraint(0, 3, 1);
		add(getRspButtonPanel(), c3);
		
		getNewRspCheckBox().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actionOnRspClientId(e);
			}
		});
		
		getAddButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actionOnAddButton(e);
			}
		});
		
		getDelButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actionOnDeleteButton(e);
			}
		});
	}
	
	protected void actionOnDeleteButton(ActionEvent e) {
	    
	    tableModel.remove(selectedRow);
	}


	protected void actionOnAddButton(ActionEvent e) {
		
	    tableModel.add(new TagValue("",""));
		
	}

	private JPanel getRspButtonPanel() {
		if(buttonPanel == null)
		{
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new FlowLayout());
			
			buttonPanel.add(getAddButton());
			buttonPanel.add(getDelButton());
		}
		return buttonPanel;
	}

	private JButton getDelButton() {
		if(delButton == null)
		{
			delButton = new JButton();
			delButton.setText("Delete");
			delButton.setPreferredSize(new Dimension(70, 20));
			delButton.setMnemonic(KeyEvent.VK_UNDEFINED);
			delButton.setEnabled(false);
		}
		return delButton;
	}

	private JButton getAddButton() {
		if(addButton == null)
		{
			addButton = new JButton();			
			addButton.setText("Add");
			addButton.setPreferredSize(new Dimension(70, 20));
			addButton.setEnabled(false);
		}
		return addButton;
	}

	protected void actionOnRspClientId(ActionEvent e) {
		boolean b = getNewRspCheckBox().isSelected();
		getRspClientIdTxt().setEditable(b);
		getRspClientIdTxt().setEnabled(b);
		getJTableServerResponseVariable().setEnabled(b);
		getAddButton().setEnabled(b);
		getDelButton().setEnabled(b);
	}

	private JScrollPane getJScrollPaneListTable() {
		if (jScrollPaneListTable == null) {
			jScrollPaneListTable = new JScrollPane();
			jScrollPaneListTable.setPreferredSize(new Dimension(265, 320));
			jScrollPaneListTable.setViewportView(getJTableServerResponseVariable());
		}
		return jScrollPaneListTable;
	}
	
	private JTable getJTableServerResponseVariable() {
		if (jTableTags == null) {
		    jTableTags = new JTable();
			jTableTags.setLocation(new Point(0, 0));
			jTableTags.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			ListSelectionModel rowSM = jTableTags.getSelectionModel();
			rowSM.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					// Ignore extra messages.
					if (e.getValueIsAdjusting()) return;
					ListSelectionModel lsm = (ListSelectionModel) e.getSource();
					if (lsm.isSelectionEmpty()) {
						return;
					} else {
						selectedRow = lsm.getMinSelectionIndex();
					}
				}
			});
			tagValues.add(new TagValue("DATA", "1"));
			tableModel = new TagValueTableModel(tagValues,jTableTags);
			tableModel.pack();
		}
		return jTableTags;
	}
	
	private Component getRspClientPanel() {
		if (useRspClientPanel == null) {
			useRspClientPanel = new JPanel();
			useRspClientPanel.setLayout(new BorderLayout());			
			useRspClientPanel.add(getServNoteTxtArea(), BorderLayout.NORTH);
			useRspClientPanel.add(getNewRspClientPanel(), BorderLayout.SOUTH);
		}
		return useRspClientPanel;
	}
	
	private Component getNewRspClientPanel() {
		if (rspClientPanel == null) {
			rspClientPanel = new JPanel();
			rspClientPanel.setLayout(new FlowLayout());
			rspClientPanel.add(getNewRspCheckBox());
			rspClientPanel.add(getRspClientIdTxt());
		}		
		return rspClientPanel;
	}

	private JTextField getRspClientIdTxt() {
		if(rspClientId == null)
		{
			rspClientId = new JTextField();
			rspClientId.setEditable(false);
			rspClientId.setPreferredSize(new Dimension(150, 26));
			rspClientId.setText("OPCACK");
			
		}
		return rspClientId;
	}

	/**
	 * This method initializes getNewRspCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getNewRspCheckBox() {
		if (newRspClientId == null) {
			newRspClientId = new JCheckBox();
			newRspClientId.setFont(getTextFont());
			newRspClientId.setText("OPC ACK Client ID:");
		}
		return newRspClientId;
	}

	/**
	 * This method initializes servNoteTxtArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getServNoteTxtArea() {
		if (servNoteTxtArea == null) {
			servNoteTxtArea = new JTextArea();
			servNoteTxtArea.setText(" OPC Response Data:\n" +
				" OPCACK is the default data format for Simulator Server response. New\n" +
				" device data can be defined to replace OPCACK. Device data loaded in a\n" +
				" file is going to overwrite device data defined in this page!");
			servNoteTxtArea.setPreferredSize(new Dimension(264, 65));
			servNoteTxtArea.setBackground(new Color(238, 238, 238));
			servNoteTxtArea.setForeground(Color.black);
			servNoteTxtArea.setEnabled(true);
			servNoteTxtArea.setFont(new Font("Dialog", Font.PLAIN, 12));
			servNoteTxtArea.setEditable(false);
			
		}
		return servNoteTxtArea;
	}

	@Override
	protected void fromValueObj(ConfigValueObject vo) {
		DataContainer dc = vo.getResponseDc(); 
		if(dc == null) return;
		
        Iterator<Object> iter = dc.keySet().iterator();
        while(iter.hasNext()) {
			String k = iter.next().toString();
			String v = dc.get(k).toString();
			if(!k.equals(DataContainerTag.CLIENT_ID))
			   tableModel.add(new TagValue(k,v));
		}
		
		getRspClientIdTxt().setText(dc.getClientID());
		getNewRspCheckBox().setSelected(false);
	}

	@Override
	protected void saveToValueObj(ConfigValueObject vo) {
		DataContainer dc = vo.getResponseDc();
		
		dc.setClientID(getRspClientIdTxt().getText());
		
		for(TagValue tagValue : tableModel.getItems()) {
		    if(tagValue.getTag() == null)
		        dc.put(tagValue.getTag(), tagValue.getValue());
		}
	}
	
	
	private class TagValueTableModel extends SortableTableModel<TagValue> {

	    private static final long serialVersionUID = 1L;

        public TagValueTableModel(List<TagValue> tagValues, JTable table){
	        super(tagValues,new String[] {"tag", "value"},table);
	    }
	    
	    public boolean isCellEditable (int row, int column){
            return true;
        }
	    
        public Object getValueAt(int rowIndex, int columnIndex) {
            
            if(rowIndex >= getRowCount()) return null;
            TagValue tagValue = items.get(rowIndex);
            switch(columnIndex) {
                case 0: return tagValue.getTag();
                case 1: return tagValue.getValue();
            }
            return null;
        }
	    
	}
	private class TagValue{
	    
	    String tag;
	    String value;
	    
	    public TagValue(String tag, String value) {
	        this.tag = tag;
	        this.value = value;
	    }
	    
	    public String getTag() {
            return tag;
        }
        public void setTag(String tag) {
            this.tag = tag;
        }
        public String getValue() {
            return value;
        }
        public void setValue(String value) {
            this.value = value;
        }
	}

}

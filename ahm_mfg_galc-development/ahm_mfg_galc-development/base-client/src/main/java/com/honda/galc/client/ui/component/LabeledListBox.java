package com.honda.galc.client.ui.component;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class LabeledListBox extends LabeledComponent<JList> {

	private static final long serialVersionUID = 1L;

	boolean isSelected = false;
	boolean supportUnselect = true;
	public LabeledListBox(String labelName) {
		this(labelName,true);
	}
	
	public LabeledListBox(String labelName,boolean supportUnselect) {
		super(labelName, new JList(),false,true);
		this.supportUnselect = supportUnselect;
		if(supportUnselect) configUnselect();
	}
	private void configUnselect() {
		//		allow mouse click to toggle selection and deselection
		getComponent().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				setItemSelected(true);
			}
		});
		
		getComponent().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (isItemSelected()) {
					//select
					setItemSelected(false);
				} else {
					//unselect
					if (getComponent().isEnabled() && (e.getButton() == MouseEvent.BUTTON1)) {
						getComponent().clearSelection();
					}
				}
			}
		});
	}
	
	public boolean isItemSelected() {
		return isSelected;
	}
	
	private void setItemSelected(boolean b) {
		isSelected = b;
	}
	
	@SuppressWarnings("unchecked")
	public void setModel(ListModel model, int selection) {
		getComponent().setModel(model);
		getComponent().setCellRenderer(model);
		getComponent().setSelectedIndex(selection);
	}
	
//	public T getSelectedItem() {
//		
//		ListModel<T> model = (ListModel<T>)getComponent().getModel();
//		int selectionIndex = getComponent().getSelectedIndex();
//		if(selectionIndex == -1 ) return null;
//		return model.getElementAt(selectionIndex);
//	}
	
}
package com.honda.galc.client.ui.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class ListComboBoxModel implements javax.swing.ComboBoxModel, ActionListener {
	
	protected List data;
	public ListComboBoxModel(List list) {
		this.listeners = new ArrayList();
		this.data = list;
		if(list.size() > 0) {
			//selected = list.get(0);
		}
	}
	
	protected Object selected;
	public void setSelectedItem(Object item) {
		this.selected = item;
	}
	public Object getSelectedItem() {
		return this.selected;
	}

	public Object getElementAt(int index) {
		return data.get(index);
	}
	public int getSize() {
		return data.size();
	}

	protected List listeners;
	public void addListDataListener(ListDataListener l) {
		listeners.add(l);
	}
	public void removeListDataListener(ListDataListener l) {
		this.listeners.remove(l);
	}
	
	public void actionPerformed(ActionEvent evt) {
		if(evt.getActionCommand().equals("update")) {
			this.fireUpdate();
		}
	}

	public void fireUpdate() {
		ListDataEvent le = new ListDataEvent(this,
			ListDataEvent.CONTENTS_CHANGED,
			0,
			data.size());
		for(int i=0; i<listeners.size(); i++) {
			ListDataListener l = (ListDataListener)listeners.get(i);
			l.contentsChanged(le);
		}
	}


}



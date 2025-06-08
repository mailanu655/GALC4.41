package com.honda.galc.client.ui.component;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JList;
import javax.swing.JTable;

public class PopupMenuMouseAdapter extends MouseAdapter {
	
	IPopupMenu poupMenuListner;
	
	public PopupMenuMouseAdapter(IPopupMenu poupMenuListner) {
		this.poupMenuListner = poupMenuListner;
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		showPopupMenu(e);
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		showPopupMenu(e);
	}
	
	protected void showPopupMenu(MouseEvent e) {
		if (e.isPopupTrigger()) {
			resetSelectedItems(e);
			poupMenuListner.showPopupMenu(e);
		}
	}
	
	protected void resetSelectedItems(MouseEvent e) {
		if(e.getSource() instanceof JTable) 
			resetTableSelectedItems(e);
		else if(e.getSource() instanceof JList) 
			resetListSelectedItems(e);
	}
	
	protected void resetTableSelectedItems(MouseEvent e) {
		JTable table = (JTable) e.getSource();
		int rowNumber = table.rowAtPoint(e.getPoint());
		if(rowNumber == -1) return;
		for(int row : table.getSelectedRows())
			if(row == rowNumber) return;
		table.getSelectionModel().setSelectionInterval(rowNumber, rowNumber);
	}
	
	protected void resetListSelectedItems(MouseEvent e) {
		JList list = (JList) e.getSource();
		int rowNumber = list.locationToIndex(e.getPoint());
		if(rowNumber == -1) return;
		for(int row : list.getSelectedIndices()) 
			if(row == rowNumber) return;
		list.setSelectedIndex(rowNumber);
	}
}

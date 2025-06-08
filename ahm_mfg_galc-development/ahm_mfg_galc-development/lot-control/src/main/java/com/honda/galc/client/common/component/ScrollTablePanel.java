package com.honda.galc.client.common.component;

import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

public class ScrollTablePanel extends JScrollPane{
	private static final long serialVersionUID = 1L;
	private JTable table = null;
	private int width = 450;
	private int height = 250;

	public ScrollTablePanel() {
		super();

		initialize();

	}
	
	public ScrollTablePanel(int width, int height) {
		super();
		this.width = width;
		this.height = height;
		
		initialize();
	}
	
	


	public void initialize(){
		setPreferredSize(new Dimension(width, height));
		setViewportView(getTable());
		setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}

	public JTable getTable() {
		if (table == null){
			table = new JTable();
			table.setLocation(new Point(0, 0));
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			table.setPreferredSize(new Dimension(width, height));
		}

		return table;
	}

}

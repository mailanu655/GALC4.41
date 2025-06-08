package com.honda.galc.client.ui.component;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class TablePane extends JScrollPane {
	private static final long serialVersionUID = 1L;
	protected JTable table ;
	private String title;
	private int selectionMode = ListSelectionModel.SINGLE_SELECTION;
	private boolean supportUnselect = false;
	private int selectedRow = Integer.MIN_VALUE;
	
	public TablePane() {
		this(null);
	}
	
	public TablePane(String title) {

		this(title,ListSelectionModel.SINGLE_SELECTION);
	}
	
	public TablePane(String title, JTable table) {
		this(title,ListSelectionModel.SINGLE_SELECTION, table);
	}
	
	public TablePane(String title, int selectionMode, JTable table) {
		this(title,selectionMode, false, table);
	}
	
	public TablePane(String title, int selectionMode, boolean supportUnselect) {
		this(title,selectionMode,supportUnselect, null);
	}
	
	public TablePane(String title, int selectionMode) {
		this(title,selectionMode,false, null);
	}
	
	public TablePane(String title,boolean supportUnselect) {
		this(title,ListSelectionModel.SINGLE_SELECTION,supportUnselect, null);
	}
	
	public TablePane(String title,int selectionMode,boolean supportUnselect, JTable table){
		super();
		this.title = title;
		this.selectionMode = selectionMode;
		this.supportUnselect = supportUnselect;
		this.table = table;
		initComponent();
	}

	protected void initComponent() {
		setName(title);
		setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		setColumnHeaderView(getTable().getTableHeader());
		setViewportView(getTable());
		getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
		if(title != null) this.setBorder(new TitledBorder(title));
	}

	public JTable getTable() {
		if(table == null) {
		    table = new JTable() {
		    	
				private static final long serialVersionUID = 1L;

				public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend)
		        {
					//Always toggle on single selection
//		        	 super.changeSelection(rowIndex, columnIndex, supportUnselect ?!extend : extend, extend);
//		        	if(selectionMode == ListSelectionModel.SINGLE_SELECTION && supportUnselect) 
//		        		super.changeSelection(rowIndex, columnIndex, !extend , extend);
//		        	else super.changeSelection(rowIndex, columnIndex, toggle , extend);
					super.changeSelection(rowIndex, columnIndex, toggle , extend);
		        }

		    };
		    table.setRowSelectionAllowed(true);
		    table.setName(title);
		    table.setSelectionMode(selectionMode);
			JTableHeader header = table.getTableHeader();
			header.setFont(new Font("Dialog", Font.BOLD, 12));
			table.setFont(new Font("Dialog", Font.BOLD, 12));
			table.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					if (!SwingUtilities.isLeftMouseButton(e)) {
						return;
					}
					if(table.getSelectedRowCount() == 1) {
						if(selectedRow == table.getSelectedRow()){
							if(supportUnselect){
								table.clearSelection();
								selectedRow = Integer.MIN_VALUE;
							}
						}else selectedRow = table.getSelectedRow();
							
					}else 
						selectedRow = Integer.MIN_VALUE;
				}
			});
		}
		return table;
	}
	
	public void setTable(JTable table) {
		this.table = table;
	}

	
	public void setTitle(String title) {
		this.title = title;
		this.setName(title);
		if(table != null) table.setName(title);
	}
	
	
	public void makeSelectedItemVisible() {
		
		if(table == null) return;
		int selectionIndex = table.getSelectedRow();
		if(selectionIndex < 0 ) return;
		table.scrollRectToVisible(table.getCellRect(selectionIndex, 0, false));
		
	}
	
	public void addListSelectionListener(ListSelectionListener x) {
		getTable().getSelectionModel().addListSelectionListener(x);
	}
	
	public void removeListSelectionListener(ListSelectionListener x) {
		getTable().getSelectionModel().removeListSelectionListener(x);
	}
	
	public void setPreferredWidth(int width) {
		setPreferredSize(new Dimension(width,getPreferredSize().height));
	}

	public void setPreferredHeight(int height) {
		setPreferredSize(new Dimension(getPreferredSize().width,height));
	}
	
	public void setMaxWidth(int width) {
		setMaximumSize(new Dimension(width,getMaximumSize().height));
	}
	
	public void setMaxHeight(int height) {
		setMaximumSize(new Dimension(getMaximumSize().width,height));
	}
	
	public void scrollToTop() {
		Point point = new Point(0, 0);
		getViewport().setViewPosition(point);
	}

	public void scrollToBottom() {
		getTable().scrollRectToVisible(
				new Rectangle(getTable().getCellRect(getTable().getRowCount() -1, 0, true)));
	}
	
	/**
	 * Scrolls the cell (rowIndex, vColIndex) so that it is visible at the center of viewport.
	 */
	public void scrollToCenter(int rowIndex, int vColIndex) {
	    
	    JViewport viewport = (JViewport)table.getParent();

	    // This rectangle is relative to the table where the
	    // northwest corner of cell (0,0) is always (0,0).
	    Rectangle rect = table.getCellRect(rowIndex, vColIndex, true);

	    // The location of the view relative to the table
	    Rectangle viewRect = viewport.getViewRect();

	    // Translate the cell location so that it is relative
	    // to the view, assuming the northwest corner of the
	    // view is (0,0).
	    rect.setLocation(rect.x-viewRect.x, rect.y-viewRect.y);

	    // Calculate location of rect if it were at the center of view
	    int centerX = (viewRect.width-rect.width)/2;
	    int centerY = (viewRect.height-rect.height)/2;

	    // Fake the location of the cell so that scrollRectToVisible
	    // will move the cell to the center
	    if (rect.x < centerX) {
	        centerX = -centerX;
	    }
	    if (rect.y < centerY) {
	        centerY = -centerY;
	    }
	    rect.translate(centerX, centerY);

	    // Scroll the area into view.
	    viewport.scrollRectToVisible(rect);
	}
	
	public void setPreferredScrollableViewportHeight(int height) {
		int width = table.getPreferredScrollableViewportSize().width;
		table.setPreferredScrollableViewportSize(new Dimension(width,height));
	}
	
	public void setPreferredScrollableViewportWidth(int width) {
		int height = table.getPreferredScrollableViewportSize().height;
		table.setPreferredScrollableViewportSize(new Dimension(width,height));
	}
	
	protected ListSelectionListener[] removeSelectionListener() {
		DefaultListSelectionModel model = (DefaultListSelectionModel) getTable().getSelectionModel();
		ListSelectionListener[] listeners = model.getListSelectionListeners();
		for (ListSelectionListener listener : listeners) {
			model.removeListSelectionListener(listener);
		}
		return listeners;
	}
	
	protected void addSelectionListener(ListSelectionListener[] listeners) {
		if (listeners == null) {
			return;
		}
		ListSelectionModel model = getTable().getSelectionModel();
		for (ListSelectionListener listener : listeners) {
			model.addListSelectionListener(listener);
		}
	}
	
	public void setCellRenderer(TableCellRenderer cellRenderer) {

		TableColumnModel columnModel = getTable().getColumnModel();
		int columnCount = columnModel.getColumnCount();
		for (int i = 0; i < columnCount; i++) {
			TableColumn column = columnModel.getColumn(i);
			column.setCellRenderer(cellRenderer);
		}
	}
	
	public void setCellRenderer(int columnIndex,TableCellRenderer cellRenderer) {
		TableColumnModel columnModel = getTable().getColumnModel();
		TableColumn column = columnModel.getColumn(columnIndex);
		column.setCellRenderer(cellRenderer);
	}

	public String getSelectedString() {
		Object o = getSelectedValue();
		return o == null ? "" : o.toString();
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getSelectedValue() {
		int[] ix = getTable().getSelectedRows();
		if (ix.length == 0) {
			return null;
		}
		return (T) getTable().getValueAt(ix[0], 0);
	}

	public void clearSelection(){
		selectedRow = Integer.MIN_VALUE;
		getTable().clearSelection();
	}

}
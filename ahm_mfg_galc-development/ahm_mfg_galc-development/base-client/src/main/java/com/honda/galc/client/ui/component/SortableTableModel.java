package com.honda.galc.client.ui.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;


public abstract class SortableTableModel<E> extends BaseTableModel<E> implements Comparator<E>{

    
    private static final long serialVersionUID = 1L;

    private final int DESCENDING = -1;
    private final int NOT_SORTED = 0;
    private final int ASCENDING = 1;

    
     
    private MouseListener mouseListener;
    
    
    private List<SortingStatus> sortingStatusList;
    private Map<E,Row> modelMap = new HashMap<E,Row>();
    private TableModelFilter<E> filter = null;
    private List<E> filteredItems = new ArrayList<E>();
    
    public SortableTableModel(List<E> items, String[] columnNames,JTable table) {
        this(items,columnNames);
        setTable(table);
        table.setModel(this);
        pack();
    }
    
    public SortableTableModel(List<E> items, String[] columnNames) {
        super(items, columnNames);
        this.mouseListener = new MouseHandler();
        sortingStatusList = new ArrayList<SortingStatus>();
    }
    
    public void setFilter(TableModelFilter<E> filter) {
    	this.filter = filter;
    }
    
    private void setTable(JTable table) {
        this.table = table;
        JTableHeader tableHeader = table.getTableHeader();
        if(tableHeader == null) return;
        
        for(MouseListener listener :tableHeader.getMouseListeners()) {
        	if(listener instanceof SortableTableModel.MouseHandler) {
            tableHeader.removeMouseListener(listener);
        	}
        }
        tableHeader.addMouseListener(mouseListener);
        tableHeader.setDefaultRenderer(new SortableHeaderRenderer(tableHeader.getDefaultRenderer()));
        tableHeader.setReorderingAllowed(false);
    }
    
    public int getSortingStatus(int column) {
        
       SortingStatus sortingStatus = findSortingStatus(column); 
       return sortingStatus == null ? NOT_SORTED : sortingStatus.direction;
       
    }
    
    private SortingStatus findSortingStatus(int column) {
        for(SortingStatus sortingStatus : sortingStatusList) {
            if(sortingStatus.column == column) return sortingStatus;
        }
        return null;
    }
    
    public boolean isSorting() {
        return sortingStatusList.size() != 0;
    }
    
    private void cancelSorting() {
        sortingStatusList.clear();
        sortingStatusChanged();
    }

    public void setSortingStatus(int column, int status) {
     
        SortingStatus sortingStatus = findSortingStatus(column); 
        if(sortingStatus != null) sortingStatusList.remove(sortingStatus); 
        if (status != NOT_SORTED) {
            sortingStatusList.add(new SortingStatus(column, status));
        }
        sortingStatusChanged();
    }
    
    private void sort() {
    	if (items != null) {
    		modelMap.clear();
    		for(int i = 0; i<getRowCount();i++) 
    			modelMap.put(items.get(i),new Row(i));
    		Collections.sort(this.items,this);
    	}
    }
    
    public void filter() {
    	if (filter != null) {
    		while (filteredItems.size() > 0) {
    			items.add(filteredItems.remove(0));
    		}
    		sort();
    		int i = 0;
    		while (i < items.size()) {
    			if (filter.include(items.get(i))) {
    				i++;
    			} else {
    				filteredItems.add(items.remove(i));
    			}
    		}
    		fireTableDataChanged();
    		if (getTableHeader() != null) {
    			getTableHeader().repaint();
    		}
    	}
    }
    
    @SuppressWarnings("unchecked")
    public int compare(E o1, E o2){
        
        for (SortingStatus sortingStatus : sortingStatusList) {
            
            int comparison = 0;
            // Define null less than everything, except null.
            if (o1 == null && o2 == null) {
                comparison = 0;
            } else if (o1 == null) {
                comparison = -1;
            } else if (o2 == null) {
                comparison = 1;
            } else {
                int col = sortingStatus.column; 
                comparison = getComparator(col)
                         .compare(modelMap.get(o1).getValue(col), modelMap.get(o2).getValue(col));
            }
            if (comparison != 0) {
                return sortingStatus.direction == ASCENDING ? comparison : -comparison;
            }
        }
        return 0;
    }
    
    @SuppressWarnings("unchecked")
    protected Comparator getComparator(int column) {
        Class<?> columnType = getColumnClass(column);
        
        if (Comparable.class.isAssignableFrom(columnType)) {
            return COMPARABLE_COMAPRATOR;
        }
        return LEXICAL_COMPARATOR;
    }
    
    private void sortingStatusChanged() {
        sort();
        fireTableDataChanged();
        if (getTableHeader() != null) {
            getTableHeader().repaint();
        }
    }
    
    private JTableHeader getTableHeader() {
        if(table == null) return null;
        return table.getTableHeader();
    }
    
    
    protected Icon getHeaderRendererIcon(int column, int size) {
        SortingStatus sortingStatus = findSortingStatus(column); 
        if(sortingStatus == null) return null;
        
        return new Arrow(sortingStatus.direction == DESCENDING, size, sortingStatusList.indexOf(sortingStatus));
    }
    
    private class SortableHeaderRenderer implements TableCellRenderer {
        private TableCellRenderer tableCellRenderer;

        public SortableHeaderRenderer(TableCellRenderer tableCellRenderer) {
            this.tableCellRenderer = tableCellRenderer;
        }

        public Component getTableCellRendererComponent(JTable table, 
                                                       Object value,
                                                       boolean isSelected, 
                                                       boolean hasFocus,
                                                       int row, 
                                                       int column) {
            Component c = tableCellRenderer.getTableCellRendererComponent(table, 
                    value, isSelected, hasFocus, row, column);
            if (c instanceof JLabel) {
                JLabel l = (JLabel) c;
                l.setHorizontalTextPosition(JLabel.LEFT);
                int modelColumn = table.convertColumnIndexToModel(column);
                l.setIcon(getHeaderRendererIcon(modelColumn, l.getFont().getSize()));
            }
            return c;
        }
    }
    
    private class MouseHandler extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            int column = getColumn(e);
            if (column == -1) return;
            int status = getSortingStatus(column);
            if (!e.isControlDown()) {
                cancelSorting();
            }
            // Cycle the sorting states through {NOT_SORTED, ASCENDING, DESCENDING} or 
            // {NOT_SORTED, DESCENDING, ASCENDING} depending on whether shift is pressed. 
            status = status + (e.isShiftDown() ? -1 : 1);
            status = (status + 4) % 3 - 1; // signed mod, returning {-1, 0, 1}
            setSortingStatus(column, status);
        }
        
        private int getColumn(MouseEvent e) {
            JTableHeader h = (JTableHeader) e.getSource();
            TableColumnModel columnModel = h.getColumnModel();
            int viewColumn = columnModel.getColumnIndexAtX(e.getX());
            return columnModel.getColumn(viewColumn).getModelIndex();
        }
    }
    

    
    public final Comparator COMPARABLE_COMAPRATOR = new Comparator() {
        @SuppressWarnings("unchecked")
        public int compare(Object o1, Object o2) {
        	if (o1 == null && o2 == null) {
        		return 0;
        	}
        	if (o1 == null) {
        		return -1;
        	}
        	if (o2 == null) {
        		return 1;
        	}
            return ((Comparable) o1).compareTo(o2);
        }
    };
    
    public final Comparator LEXICAL_COMPARATOR = new Comparator() {
        public int compare(Object o1, Object o2) {
        	if (o1 == null && o2 == null) {
        		return 0;
        	}
        	if (o1 == null) {
        		return -1;
        	}
        	if (o2 == null) {
        		return 1;
        	}            
            return o1.toString().compareTo(o2.toString());
        }
    };
    
    private class SortingStatus {
        
        public int column;
        public int direction;

        public SortingStatus(int column, int direction) {
            this.column = column;
            this.direction = direction;
        }
    }
    
    
    private static class Arrow implements Icon {
        private boolean descending;
        private int size;
        private int priority;

        public Arrow(boolean descending, int size, int priority) {
            this.descending = descending;
            this.size = size;
            this.priority = priority;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            Color color = c == null ? Color.GRAY : c.getForeground();             
            // In a compound sort, make each succesive triangle 20% 
            // smaller than the previous one. 
            int dx = (int)(size/2*Math.pow(0.8, priority));
            int dy = descending ? dx : -dx;
            // Align icon (roughly) with font baseline. 
            y = y + 5*size/6 + (descending ? -dy : 0);
            int shift = descending ? 1 : -1;
            g.translate(x, y);

            // Right diagonal. 
            g.setColor(color.darker());
            g.drawLine(dx / 2, dy, 0, 0);
            g.drawLine(dx / 2, dy + shift, 0, shift);
            
            // Left diagonal. 
            g.setColor(color.brighter());
            g.drawLine(dx / 2, dy, dx, 0);
            g.drawLine(dx / 2, dy + shift, dx, shift);
            
            // Horizontal line. 
            if (descending) {
                g.setColor(color.darker().darker());
            } else {
                g.setColor(color.brighter().brighter());
            }
            g.drawLine(dx, 0, 0, 0);

            g.setColor(color);
            g.translate(-x, -y);
        }

        public int getIconWidth() {
            return size;
        }

        public int getIconHeight() {
            return size;
        }
    }
    
    private class Row {
        private Object[] values ;
        
        public Row(int row) {
            values = new Object[getColumnCount()];
            for(int i=0;i<getColumnCount();i++) 
                values[i] = getValueAt(row, i);
        }
        
        public Object getValue(int column) {
            return values[column];
        }
    }


}

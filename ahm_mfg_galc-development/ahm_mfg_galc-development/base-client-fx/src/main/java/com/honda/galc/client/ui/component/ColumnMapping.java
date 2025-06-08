package com.honda.galc.client.ui.component;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 * 
 * @author Suriya Sena  JavaFx Migration
 * 
 */
public class ColumnMapping {
	private String accessor;
	private String format       = "%s";
	private boolean isEditable  = false;
	private String title        = "";
	private Class<?> type       = String.class;
	private boolean  isSortable = true;
	
    private List<ColumnMapping> subColumns; 

    public ColumnMapping(String title) {
		this.title = title;
        this.subColumns = new ArrayList<ColumnMapping>();
	}
	
	public ColumnMapping(String title,String accessor) {
		this(title);
		this.accessor = accessor;
	}

	public ColumnMapping(String title,String accessor, Class<?> type) {
		this(title,accessor);
		this.type = type;
	}

	public ColumnMapping(String title,String accessor, Class<?> type, String format) {
		this(title,accessor);
		this.type = type;
	}
	
	public ColumnMapping(String title,String accessor, Class<?> type,String format, boolean isEditable) {
		this(title,accessor,type,format);
		this.isEditable = isEditable;
	}

	public ColumnMapping(String title, String accessor, Class<?> type,String format, boolean isEditable, boolean isSortable) {
		this(title,accessor,type,format,isEditable);
		this.isSortable = isSortable;
	}

	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public boolean isEditable() {
		return isEditable;
	}

	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}

	public boolean isSortable() {
		return isSortable;
	}

	public void setSortable(boolean isSortable) {
		this.isSortable = isSortable;
	}

	public String getAccessor() {
		return accessor;
	}

	public void setAccessor(String accessor) {
		this.accessor = accessor;
	}

    public List<ColumnMapping> getSubColumns() {
        return subColumns;
    }

    public void addSubColumn(ColumnMapping subColumn) {
        getSubColumns().add(subColumn);
    }
}


package com.honda.galc.client.ui.component;

import java.awt.Color;
import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellEditor;

import com.honda.galc.dao.conf.DCZoneDao;
import com.honda.galc.dto.DCZoneDto;
import com.honda.galc.entity.conf.DCZone;
import com.honda.galc.service.ServiceFactory;

public class CheckBoxCellEditor extends AbstractCellEditor implements TableCellEditor {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JCheckBox checkBox;
	private DCZone zone;
     
    public CheckBoxCellEditor() {
        checkBox = new JCheckBox();
        checkBox.setHorizontalAlignment(SwingConstants.CENTER);
        checkBox.setBackground( Color.white);
    }
     
    @SuppressWarnings("unchecked")
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
       
        if (table.getModel() instanceof BaseTableModel)
        	zone = ((BaseTableModel<DCZoneDto>) table.getModel()).getItem(row).getZone();
        
        if (value instanceof Boolean && zone != null) {
        	Boolean repairable = (Boolean) value;

            try {
            	ServiceFactory.getDao(DCZoneDao.class).remove(zone);
        		zone.getId().setRepairable(!repairable);
                ServiceFactory.getDao(DCZoneDao.class).save(zone);
            } catch (Exception e) {
                zone.getId().setRepairable(repairable);
            }
        }
        
        return null;
    }
    
    public Boolean getCellEditorValue() {
    	if (zone != null) {
        	return zone.getRepairable();
    	}
    	return null;
    }
}
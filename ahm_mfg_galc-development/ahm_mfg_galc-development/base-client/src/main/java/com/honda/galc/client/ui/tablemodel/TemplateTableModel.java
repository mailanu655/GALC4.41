package com.honda.galc.client.ui.tablemodel;

import java.util.List;

import javax.swing.JTable;

import com.honda.galc.client.ui.component.SortableTableModel;
import com.honda.galc.entity.product.Template;

public class TemplateTableModel extends SortableTableModel<Template> {

	/**
	 * @author Fredrick Yessaian
	 * @date Dec 01, 2019
	 *	
	 * Class created to list out TEMPLATE_TBX data
	 */
	private static final long serialVersionUID = -689676657669659203L;

	public TemplateTableModel(List<Template> items, JTable table) {
		super(items, new String[] {"TEMPLATE_NAME","FORM_ID","TEMPLATE_TYPE","TEMPLATE_DESCRIPTION","REVISION_ID"}, table);
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		Template item = getItem(rowIndex);
		
		switch(columnIndex) {
			case 0: return item.getTemplateName();
			case 1: return item.getFormId();
			case 2: return item.getTemplateTypeString();
			case 3: return item.getTemplateDescription();
			case 4: return item.getRevisionId();
		}
		
		return null;
	}
}

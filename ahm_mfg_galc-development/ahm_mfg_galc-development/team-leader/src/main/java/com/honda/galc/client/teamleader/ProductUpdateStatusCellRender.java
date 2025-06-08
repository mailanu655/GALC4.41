package com.honda.galc.client.teamleader;

import java.awt.Color;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.honda.galc.client.datacollection.property.LotControlPropertyBean;
import com.honda.galc.client.datacollection.view.DataCollectionImageManager;
import com.honda.galc.client.property.CommonTlPropertyBean;
import com.honda.galc.constant.InstalledPartShipStatus;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.service.property.PropertyService;
/**
 * 
 * <h3>ProductUpdateStatusCellRender</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>ProductUpdateStatusCellRender description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Ambica Gawarla
 * Aug 21, 2018
 *
 */
public class ProductUpdateStatusCellRender extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;

		JLabel lbl = new JLabel();

	  private ImageIcon okIcon ;
	  private ImageIcon ngIcon ;
	  
	public ProductUpdateStatusCellRender(String okImage, String ngImage){
		 this.okIcon = getImage(okImage);
		 this.ngIcon = getImage(ngImage);
	}
	  
	public Component getTableCellRendererComponent(JTable table, Object value,
		boolean isSelected, boolean hasFocus, int row, int column) {
	
		if(value instanceof Boolean){
			Boolean status = (Boolean)value;
			if(status){
				  lbl.setIcon(okIcon);
				}else {
				  lbl.setIcon(ngIcon);
			}
		}
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			
		return lbl;
	}

	private ImageIcon getImage(String imageName){
		try{
			return new ImageIcon(getClass().getResource(imageName));
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
}

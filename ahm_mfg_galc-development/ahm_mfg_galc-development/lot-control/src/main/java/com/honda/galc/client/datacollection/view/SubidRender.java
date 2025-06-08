package com.honda.galc.client.datacollection.view;

import java.util.Map;

import javax.swing.JTextField;

import com.honda.galc.client.ui.component.ITextFieldRender;
import com.honda.galc.client.ui.component.LengthFieldBean;
import com.honda.galc.client.ui.component.Text;
import com.honda.galc.client.utils.ColorUtil;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.product.Product;
/**
 * 
 * <h3>SubidRender</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> SubidRender description </p>
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
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Jan 25, 2012</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Jan 25, 2012
 */
public class SubidRender implements ITextFieldRender{
	LengthFieldBean field;
	Map<String, String> colorMap = null;

	public SubidRender(Map<String,String> colorMap) {
		super();
		this.colorMap = colorMap;
	}

	public void renderField(Text text) {
		
		if(text.getValue() == null) return;
		if(!field.isVisible()) field.setVisible(true);
		
		this.field.setEditable(false);
		if(Product.SUB_ID_LEFT.equals(text.getValue().trim())){
			this.field.setText("Left Side");
			this.field.setBackground(ColorUtil.getColor(colorMap.get(text.getValue())));
			this.field.setColor(ColorUtil.getColor(colorMap.get(text.getValue())));
		} else if (Product.SUB_ID_RIGHT.equals(text.getValue().trim())){
			this.field.setText("Right Side");
			this.field.setBackground(ColorUtil.getColor(colorMap.get(text.getValue())));	
			this.field.setColor(ColorUtil.getColor(colorMap.get(text.getValue())));
		}
	}
	
	public void renderField(boolean status) {
	}


	public void init() {
		this.field.setEnabled(false);
		this.field.setEditable(false);
		this.field.setVisible(true);
	}

	public void setField(JTextField field) {
		if(field instanceof LengthFieldBean)
			this.field = (LengthFieldBean)field;
		else 
			Logger.getLogger().error("Field type error: SubidRender for LengthFieldBean only."); 
	}
}

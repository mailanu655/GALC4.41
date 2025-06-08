package com.honda.galc.client.teamleader.frame;

import com.honda.galc.client.teamleader.common.SpecMaintPanel;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.FrameSpec;

/**
 * Panel is specific to Frame 
 * @author LTI
 */
public class FrameSpecMaintPanel extends SpecMaintPanel<FrameSpec>{

	private static String screenName = "FRAME MTO";
	
	public FrameSpecMaintPanel(MainWindow mainWindow) {
		super(screenName, mainWindow);
	}
	@Override
	protected Double[] getColumnWidth(){
		return new Double[] {
				0.15, 0.10, 0.10, 0.10, 0.10, 0.15, 0.15
		}; 
	}
	@Override
	protected ColumnMappingList getColumnList() {
		return super.getColumnList().put("Ext Color Code", "extColorCode").put("Int Color Code", "intColorCode");
	}
	
	@Override
	protected String getProductType() {
		return ProductType.FRAME.toString();
	}
}

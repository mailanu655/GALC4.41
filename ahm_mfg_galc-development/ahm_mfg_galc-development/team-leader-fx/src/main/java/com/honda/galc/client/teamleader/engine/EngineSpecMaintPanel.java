package com.honda.galc.client.teamleader.engine;

import com.honda.galc.client.teamleader.common.SpecMaintPanel;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.EngineSpec;
/**
 * Panel is specific to Engine 
 * @author LTI
 */
public class EngineSpecMaintPanel extends SpecMaintPanel<EngineSpec>{

	private static String screenName = "ENGINE MTO";
	
	public EngineSpecMaintPanel(MainWindow mainWindow) {
		super(screenName, mainWindow);
	}
	
	@Override
	protected String getProductType() {
		return ProductType.ENGINE.toString();
	}
}

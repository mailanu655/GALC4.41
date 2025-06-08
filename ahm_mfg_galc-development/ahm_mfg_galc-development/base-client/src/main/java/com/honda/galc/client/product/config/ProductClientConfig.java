package com.honda.galc.client.product.config;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.ProductTypeData;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ProductClientConfig</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */
public class ProductClientConfig {

	// TODO get from properties or other config
	public static List<PropertyDef> getProductInfoFirstRow(ProductTypeData productTypeData) {
		List<PropertyDef> list = new ArrayList<PropertyDef>();
		list.add(new PropertyDef("product.productId", productTypeData.getProductIdLabel()));
		return list;
	}

	public static List<PropertyDef> getProductInfoSecondRow(ProductTypeData productTypeData) {
		List<PropertyDef> list = new ArrayList<PropertyDef>();
		if (ProductType.BLOCK.equals(productTypeData.getProductType())) {
			list.add(new PropertyDef("product.mcSerialNumber", "MCB"));
		} else if (ProductType.HEAD.equals(productTypeData.getProductType())) {
			list.add(new PropertyDef("product.mcSerialNumber", "MCH"));
		} else if (ProductType.ENGINE.equals(productTypeData.getProductType())) {
			list.add(new PropertyDef("product.productionLot", "Prod Lot"));
			list.add(new PropertyDef("product.kdLotNumber", "Kd Lot"));
		}
		list.add(new PropertyDef("product.productSpecCode", "YMTO"));
		return list;
	}

	public static List<PropertyDef> getEngineInfoFirstRow() {
		List<PropertyDef> list = new ArrayList<PropertyDef>();
		list.add(new PropertyDef("product.productId", "EIN"));
		return list;
	}

	public static List<PropertyDef> getEngineInfoSecondRow() {
		List<PropertyDef> list = new ArrayList<PropertyDef>();
		list.add(new PropertyDef("product.productSpecCode", "YMTO"));
		list.add(new PropertyDef("block.mcSerialNumber", "MCB"));
		list.add(new PropertyDef("product.kdLotNumber", "KDL"));
		return list;
	}

}

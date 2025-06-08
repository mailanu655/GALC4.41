package com.honda.galc.client.product;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.dao.qi.QiStationConfigurationDao;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.entity.qi.QiStationConfiguration;
import com.honda.galc.property.QiPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;

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

	public static List<PropertyDef> getProductInfoFirstRow(ProductTypeData productTypeData) {
		List<PropertyDef> list = new ArrayList<PropertyDef>();
		//For diecast product, BLOCK, HEAD, CONROD, CRANKSHAFT, if the property DC_STATION is TRUE, 
		//then the productId will show dc_serial_number, otherwise, it will show mc_serial_number
		String productId = "product.productId";
		if(ProductTypeUtil.isDieCast(productTypeData.getProductType())) {
			boolean isDcStation = PropertyService.getPropertyBean(QiPropertyBean.class,ApplicationContext.getInstance().getProcessPointId()).isDcStation();
			if(isDcStation) {
				productId = "product.dcSerialNumber";
			}else productId = "product.mcSerialNumber";
		}
		list.add(new PropertyDef(productId, productTypeData.getProductIdLabel()));
		return list;
	}

	public static List<PropertyDef> getProductInfoSecondRow(ProductTypeData productTypeData, Boolean showKdLotNumberForFrame) {
		List<PropertyDef> list = new ArrayList<PropertyDef>();
	
		switch(productTypeData.getProductType()){
			case BLOCK:	list.add(new PropertyDef("product.mcSerialNumber", "MCB"));;
						break;
			case HEAD: 	list.add(new PropertyDef("product.mcSerialNumber", "MCH"));
						break;
			case ENGINE:list.add(new PropertyDef("product.productionLot", "Prod Lot"));
						list.add(new PropertyDef("product.kdLotNumber", "Kd Lot"));
						break;
			case FRAME: if(showKdLotNumberForFrame) {
							list.add(new PropertyDef("product.kdLotNumber", "Kd Lot"));
						}
						if(PropertyService.getPropertyBoolean(ApplicationContext.getInstance().getProcessPointId(), "SHOW_ENGINE_NUMBER_FOR_FRAME", false)) {
							list.add(new PropertyDef("product.engineSerialNo", "Engine No"));
						}			
			case MBPN:  if(checkIsUpcStation())list.add(new PropertyDef("product.quantity", "Quantity"));
						break;
		}
		list.add(new PropertyDef("product.productSpecCode", "YMTO"));
		return list;
	}

	public static boolean checkIsUpcStation() {
		return PropertyService.getPropertyBean(QiPropertyBean.class, ApplicationContext.getInstance().getProcessPointId()).isUpcStation();
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

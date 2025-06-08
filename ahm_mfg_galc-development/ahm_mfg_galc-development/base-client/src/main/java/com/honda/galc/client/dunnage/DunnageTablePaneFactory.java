package com.honda.galc.client.dunnage;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.PropertiesMapping;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.DieCast;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.property.DunnagePropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * 
 * @author Paul Chou
 * 
 */
public class DunnageTablePaneFactory {
	public static ObjectTablePane<? extends BaseProduct> createDunnageTablePane(ProductType productType) {
		if (ProductTypeUtil.isInstanceOf(productType, MbpnProduct.class)) {
			ColumnMappings mbpnColumnMappings = ColumnMappings.with("#", "row_reverted").put("Product Id", "productId").put("Spec Code", "currentProductSpecCode").put("Order No", "currentOrderNo");
			return new ObjectTablePane<MbpnProduct>(mbpnColumnMappings.get());
		} else if (ProductTypeUtil.isInstanceOf(productType, DieCast.class)) {
			ColumnMappings columnMappings = ColumnMappings.with("#", "row_reverted").put("Die Cast Number", "dcSerialNumber").put("Machining Number", "mcSerialNumber").put("Model", "modelCode");
			return new ObjectTablePane<DieCast>(columnMappings.get());
		} else {
			ColumnMappings dftColumnMappings = ColumnMappings.with("#", "row_reverted").put("Product Id", "productId").put("Produc Spec", "productSpecCode");
			return new ObjectTablePane<BaseProduct>(dftColumnMappings.get());
		}
	}

	public static ObjectTablePane<Map<String, Object>> createDunnageMaintTablePane(ProductType productType, DunnagePropertyBean property) {
		if (ProductTypeUtil.isInstanceOf(productType, MbpnProduct.class)) {
			PropertiesMapping mbpnColumnMappings = new PropertiesMapping();
			mbpnColumnMappings.put("#", "row_reverted");
			mbpnColumnMappings.put("Product Id", "productId");
			mbpnColumnMappings.put("Spec Code", "currentProductSpecCode");
			mbpnColumnMappings.put("Order No", "currentOrderNo");
			mbpnColumnMappings.put("Container", "containerId");
			mbpnColumnMappings.put("Seq", "trackingSeq");
			mbpnColumnMappings.put("Status", "defectLabel");
			mbpnColumnMappings.put("Hold", "onHoldLabel");
			if (property.getOffProcessPointIds() != null && property.getOffProcessPointIds().length > 0) {
				mbpnColumnMappings.put("Passed OFF", "offedLabel");
			}
			if (!StringUtils.isBlank(property.getShippingProcessPointId())) {
				mbpnColumnMappings.put("Shipped", "shippedLabel");
			}
			if (property.isInsertDunnageContent()) {
				mbpnColumnMappings.put("Matrix(R,C,L)", "matrix");
			}
			return new ObjectTablePane<Map<String, Object>>(mbpnColumnMappings.get(), true, true);
		} else if (ProductTypeUtil.isInstanceOf(productType, DieCast.class)) {
			PropertiesMapping columnMappings = new PropertiesMapping();
			columnMappings.put("#", "row_reverted");
			columnMappings.put("Die Cast Number", "product.dcSerialNumber");
			columnMappings.put("Machining Number", "product.mcSerialNumber");
			columnMappings.put("Machine", "machineId");
			columnMappings.put("Die", "dieId");
			columnMappings.put("Model", "product.modelCode");
			columnMappings.put("Defect", "defectLabel");
			columnMappings.put("Hold", "onHoldLabel");
			if (property.getOffProcessPointIds() != null && property.getOffProcessPointIds().length > 0) {
				String label = getOffLabel(property);
				columnMappings.put(label + " Off", "offedLabel");
				columnMappings.put(label + " Off Date", "offDate");
			}
			if (!StringUtils.isBlank(property.getShippingProcessPointId())) {
				columnMappings.put("Shipped", "shippedLabel");
			}
			if (property.isInsertDunnageContent()) {
				columnMappings.put("Matrix(R,C,L)", "matrix");
			}

			return new ObjectTablePane<Map<String, Object>>(columnMappings.get(), true, true);
		} else {
			PropertiesMapping dftMappings = new PropertiesMapping();
			dftMappings.put("#", "row_reverted");
			dftMappings.put("Product Id", "product.productId");
			dftMappings.put("Produc Spec", "product.productSpecCode");
			dftMappings.put("Defect", "defectLabel");
			dftMappings.put("Hold", "onHoldLabel");
			if (property.getOffProcessPointIds() != null && property.getOffProcessPointIds().length > 0) {
				String label = getOffLabel(property);
				dftMappings.put(label + " Off", "offedLabel");
				dftMappings.put(label + " Off Date", "offDate");
			}
			if (!StringUtils.isBlank(property.getShippingProcessPointId())) {
				dftMappings.put("Shipped", "shippedLabel");
			}
			return new ObjectTablePane<Map<String, Object>>(dftMappings.get(), true, true);
		}
	}

	public static String getOffLabel(DunnagePropertyBean property) {
		String label = property.getOffLabel();
		if (label == null || label.trim().length() == 0) {
			String[] ids = property.getOffProcessPointIds();
			ProcessPoint processPoint = ServiceFactory.getDao(ProcessPointDao.class).findByKey(ids[0]);
			label = processPoint.getDivisionName();
		}
		return label;
	}
}

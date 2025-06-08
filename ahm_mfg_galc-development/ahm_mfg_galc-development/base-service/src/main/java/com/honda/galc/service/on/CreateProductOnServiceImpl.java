package com.honda.galc.service.on;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.data.LineSideContainerValue;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.TagNames;
import com.honda.galc.data.ProductNumberDef.NumberType;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.DieCast;
import com.honda.galc.entity.product.Product;
import com.honda.galc.service.utils.DiecastUtil;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>CreateProductOnServiceImpl</code> is a simple product on service.
 * <ol>
 * <li>It accepts input: productId, productSpecCode or modelCode.</li>
 * <li>Creates product</li>
 * <li>Invokes TrackingService</li>
 * </ol>
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
 * @created Dec 19, 2014
 */
public class CreateProductOnServiceImpl extends ProductOnServiceBase implements CreateProductOnService {

	@Override
	protected void init(Device device) {
		super.init(device);
		context.putAll(device.getInputMap());
	}

	@Override
	protected void processSingleProduct() {

		ProductType productType = getProductType();
		NumberType numberType = getInputNumberType(productType);
		String productIdTag = getFullTag(TagNames.PRODUCT_ID.name());
		String productId = (String) context.get(productIdTag);
		productId = StringUtils.trim(productId);

		try {
			validateProductId(productType, numberType, productId);
			BaseProduct product = assembleProduct(productType, productId);
			saveProduct(productType, product);

			processTask();

			if (getPropertyBean().isAutoTracking()) {
				track();
			} else {
				updateLastPassingProcessPoint(product);
			}
			contextPut(TagNames.DATA_COLLECTION_COMPLETE.name(), LineSideContainerValue.COMPLETE);
		} catch (TaskException te) {
			contextPut(TagNames.DATA_COLLECTION_COMPLETE.name(), LineSideContainerValue.NOT_COMPLETE);
			contextPut(TagNames.ERROR_MESSAGE.name(), te.getMessage());
			getLogger().error(te, te.getMessage(), this.getClass().getSimpleName());
		} catch (Exception e) {
			contextPut(TagNames.DATA_COLLECTION_COMPLETE.name(), LineSideContainerValue.NOT_COMPLETE);
			String msg = String.format("Exception occured when processing product:%s, id:%s, ex:%s ", productType, productId, e.getMessage());
			contextPut(TagNames.ERROR_MESSAGE.name(), msg);
			getLogger().error(e, msg, this.getClass().getSimpleName());
		}
	}

	// === //
	protected void validateProductId(ProductType productType, NumberType numberType, String productId) {
		if (StringUtils.isBlank(productId)) {
			String msg = String.format("Exception: Invalid ProductId: %s %s is blank. ", productType, numberType);
			throw new TaskException(msg);
		}
		List<ProductNumberDef> productNumberDefs = DiecastUtil.findNumberDefs(productType, numberType);
		if (!ProductNumberDef.isNumberValid(productId, productNumberDefs)) {
			String msg = String.format("Exception: Invalid ProductId: %s %s number '%s', number format is invalid. ", productType, numberType, productId);
			throw new TaskException(msg);
		}
		BaseProduct product = ProductTypeUtil.getProductDao(productType).findBySn(productId);
		if (product != null) {
			String msg = String.format("Exception: Invalid ProductId: product %s with id %s already exist in database, ", productType, productId);
			throw new TaskException(msg);
		}
	}

	protected BaseProduct assembleProduct(ProductType productType, String productId) {
		BaseProduct product = ProductTypeUtil.createProduct(productType.name(), productId);
		if (product instanceof Product) {
			assembleProduct((Product) product);
		} else if (product instanceof DieCast) {
			assembleProduct((DieCast) product);
		}
		return product;
	}

	@SuppressWarnings("unchecked")
	protected void saveProduct(ProductType productType, BaseProduct product) {
		ProductDao<BaseProduct> dao = (ProductDao<BaseProduct>) ProductTypeUtil.getProductDao(productType);
		product = dao.save(product);
		this.product = product;
		getLogger().info(String.format("Product:%s, Id:%s has been created at Process Point:%s", productType, productId, getProcessPointId()));
	}

	// === utility === //
	protected NumberType getInputNumberType(ProductType productType) {
		if (ProductTypeUtil.isInstanceOf(productType, DieCast.class)) {
			return NumberType.DC;
		}
		return NumberType.IN;
	}

	protected Product assembleProduct(Product product) {
		String specCode = getTagValue(TagNames.PRODUCT_SPEC_CODE.name());
		if (StringUtils.isNotBlank(specCode)) {
			product.setProductSpecCode(specCode);
		}
		return product;
	}

	protected DieCast assembleProduct(DieCast product) {
		product.setDcSerialNumber(product.getProductId());
		String modelCode = getTagValue(TagNames.MODEL_CODE.name());
		if (StringUtils.isBlank(modelCode)) {
			ProductNumberDef pnd = DiecastUtil.getProductNumberDef(getProductType(), NumberType.DC, product.getDcSerialNumber());
			if (pnd == null) {
				throw new TaskException(String.format("There is no ProductNumberDef defined for number type DC,  product type %s", getProductType()));
			}
			modelCode = pnd.getModel(product.getDcSerialNumber());
		}
		product.setModelCode(modelCode);
		return product;
	}

	protected String getTagValue(String tagName) {
		tagName = getFullTag(tagName);
		String value = (String) context.get(tagName);
		value = StringUtils.trim(value);
		return value;
	}
}

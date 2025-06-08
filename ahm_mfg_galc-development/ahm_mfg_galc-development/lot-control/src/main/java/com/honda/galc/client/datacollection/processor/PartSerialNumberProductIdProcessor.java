package com.honda.galc.client.datacollection.processor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.oif.BomDao;
import com.honda.galc.dao.product.ExpectedProductDao;
import com.honda.galc.dao.product.MbpnProductDao;
import com.honda.galc.dao.product.ProductIdMaskDao;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.entity.fif.Bom;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.BaseProductSpec;
import com.honda.galc.entity.product.ExpectedProduct;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.ProductIdMask;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.CommonPartUtility;

public class PartSerialNumberProductIdProcessor extends ProductIdProcessor {

	final String subAssyProductType;

	public PartSerialNumberProductIdProcessor(ClientContext context) {
		super(context);
		this.subAssyProductType = context.getProperty().getSubAssyProductType();
	}

	protected String getSubAssyProductType() {
		if (StringUtils.isBlank(this.subAssyProductType)) {
			handleException("Error: Property SUB_ASSY_PRODUCT_TYPE is undefined for " + context.getProcessPointId().trim());
		}
		return this.subAssyProductType;
	}

	@Override
	public BaseProductSpec findProductSpec(String productSpecCode) {
		try {
			BaseProductSpec productSpec = ProductTypeUtil.getProductSpecDao(getSubAssyProductType()).findByProductSpecCode(productSpecCode, getSubAssyProductType());
			if (productSpec == null) throw new NullPointerException("null BaseProductSpec for product spec code: " + productSpecCode);
			return productSpec;
		} catch (Exception e) {
			throw new TaskException("Invalid product spec code: " + productSpecCode, e);
		}
	}

	/**
	 * Get a product spec code using the product id and product type.
	 */
	protected String getProductSpecCode(String productId, String productType) {
		try {
			return ProductTypeUtil.getTypeUtil(productType).findProduct(productId).getProductSpecCode();
		} catch (Exception e) {
			handleException("Unable to get product spec code for product id " + productId + " of product type " + productType);
			return null;
		}
	}

	/**
	 * Get a product spec code using PRODUCT_ID_MASK_TBX.
	 */
	@Override
	public String getProductSpecCode(String productId) {
		if (product.getProductId().equals(productId) && !StringUtils.isBlank(product.getProductSpec())) {
			return product.getProductSpec();
		}
		String productSpecCode;
		List<ProductIdMask> productIdMasks = ServiceFactory.getDao(ProductIdMaskDao.class).findAllByProcessPointAndProductType(context.getProcessPointId().trim(), getSubAssyProductType());
		List<ProductIdMask> matchingProductIdMasks = new ArrayList<ProductIdMask>();
		if (productIdMasks != null) {
			for (ProductIdMask productIdMask : productIdMasks) {
				if (CommonPartUtility.verification(productId, productIdMask.getId().getProductIdMask(), PropertyService.getPartMaskWildcardFormat())) {
					matchingProductIdMasks.add(productIdMask);
				}
			}
		}
		{
			ProductIdMask bestMatchingProductIdMask = getBestMatchingProductIdMask(productId, matchingProductIdMasks);
			productSpecCode = (bestMatchingProductIdMask == null ? null : bestMatchingProductIdMask.getProductSpecCode());
		}
		if (productSpecCode == null) {
			handleException("Invalid Part Serial Number: " + productId + ", no matching product id mask.");
		}
		return productSpecCode;
	}

	protected ProductIdMask getBestMatchingProductIdMask(String productId, List<ProductIdMask> matchingProductIdMasks) {
		if (matchingProductIdMasks == null || matchingProductIdMasks.isEmpty()) {
			return null;
		}
		if (matchingProductIdMasks.size() == 1) {
			return matchingProductIdMasks.get(0);
		}
		// check for the expected ProductSpecCode among matching masks
		String expectedProductSpecCode = getProductSpecCode(getExpectedProductId(), context.getFrameSpecs());
		for (ProductIdMask matchingProductIdMask : matchingProductIdMasks) {
			if (expectedProductSpecCode.equals(matchingProductIdMask.getProductSpecCode())) {
				return matchingProductIdMask;
			}
		}
		// choose the best match if the expected ProductSpecCode is not found
		Collections.sort(matchingProductIdMasks, new ProductIdMaskComparator());
		return matchingProductIdMasks.get(matchingProductIdMasks.size()-1);
	}

	protected void confirmProductId(ProductId productId) throws SystemException, TaskException, IOException {
		super.confirmProductId(productId);
		if (ProductTypeUtil.isMbpnProduct(getSubAssyProductType()))
			getOrCreateMbpnProduct(product.getProductId(), product.getProductSpec());
	}

	/** NALC-1574-MAX_PRODUCT_SN_LENGTH is set to 17 ( I cannot add both 17 and length 11)*/
	@Deprecated
	protected void checkProductIdLength() {
		if (product.getProductId().length() <= 0 || product.getProductId().length() > property.getMaxProductSnLength()) {
			handleException("Invalid Part Serial Number: " + product.getProductId() + ", length invalid.");
		}
	}

	@Override
	protected BaseProduct getProductFromServer() {
		BaseProduct aproduct = context.getDbManager().confirmProductOnServer(product.getProductId());
		String productSpecCode = getProductSpecCode(product.getProductId());
		if (aproduct == null && ProductTypeUtil.isMbpnProduct(getSubAssyProductType()))
			aproduct = getOrCreateMbpnProduct(product.getProductId(), productSpecCode);

		product.setProductSpec(productSpecCode);

		return aproduct;
	}

	@Override
	public void checkExpectedProduct(ProductId productId) {
		String expectedProductSpecCode = getProductSpecCode(getExpectedProductId(), context.getProperty().getProductType());

		if (ProductTypeUtil.isMbpnProduct(getSubAssyProductType())) {
			String mbpnProductSpecCode = getProductSpecCode(product.getProductId());
			String plantLocCode = context.getProperty().getPlantLocCode();
			if (StringUtils.isEmpty(plantLocCode) || plantLocCode.length() > 1) {
				throw new TaskException("Invalid PLANT_LOC_CODE: " + plantLocCode);
			}
			List<Bom> boms = ServiceFactory.getDao(BomDao.class).findAllByPartNoAndMtc(mbpnProductSpecCode, plantLocCode, expectedProductSpecCode.substring(0,4), expectedProductSpecCode.substring(4,7));
			if (boms == null || boms.isEmpty()) {
				handleException("Invalid Part Serial Number: " + mbpnProductSpecCode + " does not match expected product spec code " + expectedProductSpecCode);
			}
		}
		else {
			String actualProductSpecCode = getProductSpecCode(product.getProductId());
			if (!actualProductSpecCode.equals(expectedProductSpecCode)) {
				handleException("Invalid Part Serial Number: " + actualProductSpecCode + " does not match expected product spec code " + expectedProductSpecCode);
			}
		}
	}

	protected String getExpectedProductId() {
		String expectedProductId = state.getExpectedProductId();
		if (StringUtils.isEmpty(expectedProductId)) {
			ExpectedProduct expectedProduct = ServiceFactory.getDao(ExpectedProductDao.class).findForProcessPoint(context.getProcessPointId().trim());
			if (expectedProduct != null) {
				expectedProductId = expectedProduct.getProductId();
				if (StringUtils.isEmpty(expectedProductId)) {
					return null;
				}
				state.setExpectedProductId(expectedProductId);
			} else {
				return null;
			}
		}
		return expectedProductId;
	}

	private MbpnProduct getOrCreateMbpnProduct(String productId, String productSpecCode) {
		// create a MBPN_PRODUCT_TBX record for the part serial number iff it does not have a record
		MbpnProductDao mbpnProductDao = ServiceFactory.getDao(MbpnProductDao.class);
		MbpnProduct mbpnProduct = mbpnProductDao.findBySn(productId);
		if (mbpnProduct == null) {
			mbpnProduct = new MbpnProduct();
			mbpnProduct.setProductId(productId);
			mbpnProduct.setCurrentProductSpecCode(productSpecCode);
			mbpnProductDao.save(mbpnProduct);
		} else {
			if (!productSpecCode.equals(mbpnProduct.getCurrentProductSpecCode())) {
				handleException("Error: " + productId + " has CURRENT_PRODUCT_SPEC_CODE " + mbpnProduct.getCurrentProductSpecCode() + " but matching product spec code is " + productSpecCode);
			}
		}
		return mbpnProduct;
	}

	private class ProductIdMaskComparator implements Comparator<ProductIdMask> {

		public int compare(ProductIdMask mask1, ProductIdMask mask2) {
			if (mask1 == null && mask2 == null) return 0;
			if (mask1 == null && mask2 != null) return -1;
			if (mask1 != null && mask2 == null) return 1;
			return rateProductIdMask(mask1) - rateProductIdMask(mask2);
		}

		private int rateProductIdMask(ProductIdMask productIdMask) {
			boolean escape = false;
			int rating = 0;
			char[] maskChars = productIdMask.getId().getProductIdMask().toCharArray();
			for (char c : maskChars) {
				if (!escape) {
					if (c == '\\') { // escape character
						escape = true;
						continue;
					}
					if (c == '*') { // multi-character wildcard
						rating += 0;
						continue;
					}
					if (c == '?' || c == '%') { // single-character wildcard
						rating += 1;
						continue;
					}
					if (c == '^') { // alphanumeric wildcard
						rating += 1;
						continue;
					}
					if (c == '#') { // single-digit wildcard
						rating += 2;
						continue;
					}
				} else {
					escape = false;
				}
				// exact match
				rating += 4;
			}
			return rating;
		}

	}

}

package com.honda.galc.client.teamleader.recovery.frame;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.honda.galc.client.ClientController;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.dao.product.DiecastDao;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.DieCast;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>DataRecoveryController</code> is ...
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
 * <TD>Karol Wozniak</TD>
 * <TD>Jul 11, 2008</TD>
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
public class DataRecoveryController extends ClientController {

	private ProductType productType;
	private BaseProduct product;
	private Map<String, ProductBuildResult> buildResults = new HashMap<String, ProductBuildResult>();

	public DataRecoveryController(MainWindow frame, ProductType productType) {
		super(frame);
		this.productType = productType;
	}

	public void findProduct(String productId, String[] partNames) {
		BaseProduct product = findProduct(productId);
		if (product == null) {
			return;
		}
		setProduct(product);
	}

	public void findProductByDc(String productId) {
		BaseProduct product = findProductByDCNumber(productId);
		if (product == null) {
			return;
		}
		setProduct(product);
	}

	public void findProductByMc(String productId) {
		BaseProduct product = findProductByMCNumber(productId);
		if (product == null) {
			return;
		}
		setProduct(product);
	}

	protected BaseProduct findProduct(String productId) {
		BaseProduct product = ProductTypeUtil.getProductDao(productType).findBySn(productId);
		if (product == null || !productType.equals(product.getProductType())) {
			return null;
		}
		return product;
	}

	protected DieCast findProductByMCNumber(String mcNumber) {
		ProductDao<?> dao = ProductTypeUtil.getProductDao(productType);
		if (dao instanceof DiecastDao) {
			DiecastDao<?> diecastDao = (DiecastDao<?>) dao;
			DieCast product = (DieCast) diecastDao.findByMCSerialNumber(mcNumber);
			if (product == null || !productType.equals(product.getProductType())) {
				return null;
			}
			return product;
		}
		return null;
	}

	protected DieCast findProductByDCNumber(String dcNumber) {
		ProductDao<?> dao = ProductTypeUtil.getProductDao(productType);
		if (dao instanceof DiecastDao) {
			DiecastDao<?> diecastDao = (DiecastDao<?>) dao;
			DieCast product = (DieCast) diecastDao.findByDCSerialNumber(dcNumber);
			if (product == null || !productType.equals(product.getProductType())) {
				return null;
			}
			return product;
		}
		return null;
	}

	public void selectBuildResults(String[] partNames) {
		findProductBuildResults(getProduct().getProductId(), partNames);
	}

	protected void findProductBuildResults(String productId, String[] partNames) {
		Map<String, ProductBuildResult> results = new HashMap<String, ProductBuildResult>();
		for (String partName : partNames) {

			ProductBuildResult result = findProductBuildResult(productId, partName);
			if (result != null) {
				results.put(partName, result);
			}
		}
		setBuildResults(results);
	}

	public ProductBuildResult findProductBuildResult(String productId, String partName) {
		return ProductTypeUtil.getProductBuildResultDao(productType).findById(productId, partName);
	}

	public void updateBuildResults(List<ProductBuildResult> productResults) {
		ProductTypeUtil.getProductBuildResultDao(productType).saveAllResults(productResults);
		for (ProductBuildResult result : productResults) {
			getBuildResults().put(result.getPartName().trim(), result);
		}
	}

	public ProductBuildResult constructProductBuildResult(String partName) {
		ProductBuildResult result = ProductTypeUtil.createBuildResult(productType.name());
		result.setProductId(getProduct().getProductId());
		result.setPartName(partName);
		result.setAssociateNo(getMainWindow().getUserId());
		result.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
		result.setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
		buildResults.put(partName, result);
		return result;
	}

	public void resetModel() {
		setProduct(null);
		if (getBuildResults() != null) {
			getBuildResults().clear();
		}
	}

	public BaseProduct getProduct() {
		return product;
	}

	public void setProduct(BaseProduct product) {
		this.product = product;
	}

	public Map<String, ProductBuildResult> getBuildResults() {
		return buildResults;
	}

	public ProductBuildResult getProductBuildResult(PartDefinition attribute) {
		return buildResults.get(attribute.getName());
	}

	public ProductBuildResult getProductBuildResult(String name) {
		return buildResults.get(name);
	}

	public void setBuildResults(Map<String, ProductBuildResult> buildResults) {
		this.buildResults = buildResults;
	}

	public String getProductName() {
		return getProductType().getProductName();
	}
}

/**
 * 
 */
package com.honda.galc.client.datacollection.processor;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.state.ProductBean;
import com.honda.galc.client.datacollection.strategy.IpuQaTesterXmlDriver;
import com.honda.galc.client.device.ipuqatester.model.UnitInTest;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.ProductSpecCodeDao;
import com.honda.galc.dao.product.SubProductDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.entity.product.Ipu;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.entity.product.ProductSpecCode;
import com.honda.galc.entity.product.ProductSpecCodeId;
import com.honda.galc.entity.product.SubProduct;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.LotControlPartUtil;
import com.honda.galc.util.SortedArrayList;

/**
 * @author Subu Kathiresan
 * @author Gangadhararao Gadde
 * @Date Apr 26, 2012
 *
 */
public class IpuQaTesterSnProcessor extends ProcessorBase implements IIpuQaTesterSnProcessor{

	public static int IPU_PRODUCT_ID_LENGTH = 13;
	public static final String PROCESS_PRODUCT = "Process product:";
	public static final String LOT_CONTROL_RULE_NOT_DEFINED = "LOT_CONTROL_RULE_NOT_DEFINED";
	
	protected SubProductDao _subProductDao = null;
	protected ProductBean _product = null;
	
	public IpuQaTesterSnProcessor(ClientContext context) {
		super(context);
		IpuQaTesterXmlDriver.getInstance(context.getAppContext().getApplicationId());
	}
	
	public void init() {
	}

	public synchronized boolean execute(UnitInTest unitInTest) {

		Logger.getLogger().debug("IpuQaTesterSnProcessor : Enter execute");
		try {
			Logger.getLogger().info(PROCESS_PRODUCT + unitInTest.getProductId());
			if (unitInTest.getProductId().trim().length() == IPU_PRODUCT_ID_LENGTH) {
				createSubProductIfNeeded(unitInTest.getProductId(), unitInTest);
				getProduct().setProductId(unitInTest.getProductId());
				getProduct().setProductSpec(unitInTest.getProductSpecCode());
				loadLotControlRules();
				getController().getFsm().productIdOk(getProduct());
				Logger.getLogger().debug("ProductIdProcessor : Enter execute ok");
				return true;
			} else {
				getController().getFsm().productIdNg(_product, "Invalid Product Idlength", "Invalid ProductId length");
			}
		} catch (TaskException te) {
			Logger.getLogger().error(te.getMessage());
			getProduct().setValidProductId(false);
			getController().getFsm().productIdNg(getProduct(), te.getTaskName(), te.getMessage());
		} catch (Exception e) {
			Logger.getLogger().error(e, e.getMessage());
			getController().getFsm().error(new Message("MSG01", e.getCause().toString()));
		} catch (Throwable t){
			Logger.getLogger().error(t, t.getMessage());
			getController().getFsm().error(new Message("MSG01", t.getCause().toString()));
		} 

		Logger.getLogger().debug("ProductIdProcessor : Exit execute ng");
		return false;
	}
	
	/**
	 * creates the IPU sub product if its not already available
	 * 
	 * @param productId
	 * @param ipuUnit
	 */
	private SubProduct createSubProductIfNeeded(String productId, UnitInTest ipuUnit) {
		SubProduct ipu = new Ipu();
		try {
			if (getSubProductDao().findByKey(productId) == null) {
				ipu.setProductId(productId);
				String[] buildCodeArr = ipuUnit.getBuildCode().split("-");
				String productSpecCode = buildCodeArr[0] + buildCodeArr[1];
				ipu.setProductSpecCode(productSpecCode);
				getSubProductDao().save(ipu);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			Logger.getLogger().error("Unable to create new product for product id: " + productId);
		}
		return ipu;
	}
	
	protected void loadLotControlRules(){
		String productSpec = getProduct().getProductSpec();
		SortedArrayList<LotControlRule> rules = new SortedArrayList<LotControlRule>("getSequenceNumber");
		if(!StringUtils.isBlank(productSpec)) {
			rules.addAll(LotControlPartUtil.getLotControlRuleByProductSpec(findProductSpec(productSpec), context.getAllRules()));
		}
		getController().getFsm().initLotControlRules(productSpec, rules);

		if(rules.size() == 0)
			throw new TaskException("Lot Control Rule for " + productSpec + " is not defined.", LOT_CONTROL_RULE_NOT_DEFINED);
	}

	protected ProductSpec findProductSpec(String productSpec) {
		ProductSpecCodeId  productSpecCodeId = new ProductSpecCodeId();
		productSpecCodeId.setProductType(ProductType.IPU.toString());
		productSpecCodeId.setProductSpecCode(productSpec);
		ProductSpec prodSpec = getProductSpecCodeDao().findByKey(productSpecCodeId);
		if(prodSpec == null) prodSpec = createProductSpec(productSpec);
		return prodSpec;
	}
	
	private ProductSpec createProductSpec(String productSpec){
		ProductSpecCode productSpecCode = new ProductSpecCode();
		ProductSpecCodeId  productSpecCodeId = new ProductSpecCodeId();
		productSpecCodeId.setProductType(ProductType.IPU.toString());
		productSpecCodeId.setProductSpecCode(productSpec);
		productSpecCode.setId(productSpecCodeId);
		productSpecCode.setModelYearCode(productSpec.substring(0,1));
		productSpecCode.setModelTypeCode(productSpec.substring(4,7));
		productSpecCode.setModelCode(productSpec.substring(1,4));
		productSpecCode.setExtColorCode("*");
		productSpecCode.setModelOptionCode("*");
		productSpecCode.setIntColorCode("*");
		return getProductSpecCodeDao().save(productSpecCode);
	}
	
	private ProductSpecCodeDao getProductSpecCodeDao(){
		return ServiceFactory.getDao(ProductSpecCodeDao.class);
	}
	
	public SubProductDao getSubProductDao() {
		if(_subProductDao == null)
			_subProductDao = ServiceFactory.getDao(SubProductDao.class);
		return _subProductDao;
	}
	
	public ProductBean getProduct() {
		if(_product == null)
			_product = new ProductBean();
		return _product;
	}

	public void registerDeviceListener(DeviceListener listener) {
	}
}

package com.honda.galc.client.dc.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.dc.view.PartShipmentInputPane;
import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.product.AbstractProductIdProcessor;
import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.FXOptionPane;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.BuildAttributeDao;
import com.honda.galc.dao.product.PartShipmentProductDao;
import com.honda.galc.dao.product.MbpnDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.device.IDeviceData;

import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.Mbpn;
import com.honda.galc.entity.product.PartShipmentProduct;
import com.honda.galc.property.ProductCheckPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.ProductCheckUtil;
import com.honda.galc.util.check.PartResultData;


public class PartShipmentProcessor extends AbstractProductIdProcessor implements IPartShipmentProcessor{

	protected BaseProduct product;

	public PartShipmentProcessor(ProductController productController) {
		super(productController);
		// TODO Auto-generated constructor stub
	}

	public void init() {
		// TODO Auto-generated method stub

	}

	public boolean execute(ProductId data) {
		// TODO Auto-generated method stub
		return false;
	}

	public IDeviceData processReceived(ProductId deviceData) {
		// TODO Auto-generated method stub
		return null;
	}

	public void processInputNumber(ProductEvent event) {
		if (null!=event) {
			getProductController().getView().getInputPane().getProductIdField().setText((String)event.getTargetObject());
		}
		try{
			removePrefixForFrame();
			product = ProductTypeUtil.findProduct(getProductController().getModel().getProductTypeData().getProductTypeName(), getProductController().getView().getInputPane().getProductId());


			if(product != null  && StringUtils.isNotBlank(product.getProductSpecCode()) && isProductSpecValid()){
				if (!isProductValidForShippingToSite(product)) {
					getProductController().getView().setErrorMessage(product.getProductId() + " is not valid for shipping to " + ((PartShipmentInputPane)getProductController().getView().getInputPane()).getSite(), getProductController().getView().getInputPane().getProductIdField());
					getProductController().getAudioManager().playNGSound();
					return;
				}
				Map<String,Object> messages = check(product);
				if(!messages.isEmpty()){
					StringBuilder messageBuilder = new StringBuilder();
					for (String check : messages.keySet()) {
						Object obj = messages.get(check);
						if(obj != null){
							String msg = getMessage(check,obj);
							messageBuilder.append(msg);
						}else{
							messageBuilder.append(check);
						}

					}

					getProductController().getView().setErrorMessage("FAILED PRODUCT CHECKS: " + messageBuilder.toString(), getProductController().getView().getInputPane().getProductIdField());
					getProductController().getAudioManager().playNGSound();
					return;
				}

				PartShipmentProduct partShipmentProduct = ServiceFactory.getDao(PartShipmentProductDao.class).findByProductId(product.getProductId());
				if(partShipmentProduct != null) {
					String msg ="Part already shipped " + product.getProductId();

					getProductController().getView().setErrorMessage(msg, getProductController().getView().getInputPane().getProductIdField());
					getProductController().getAudioManager().playWarnSound();
					return ;
				}


				getProductController().startProduct(product);

				EventBusUtil.publish(new ProductEvent(product, ProductEventType.PRODUCT_INPUT_OK));

				getProductController().finishProduct();
			}else{
				String errorMsg = "";
				if (product != null) {
					if (StringUtils.isBlank(product.getProductSpecCode())) {
						errorMsg = "invalid Product " + (String) event.getTargetObject() + " empty productspec";
					} else if (!isProductSpecValid()) {
						errorMsg = "invalid Product " + (String) event.getTargetObject() + " productspec does not exist";
					}
				} else {
					errorMsg ="invalid Product " + (String)event.getTargetObject();		
				}		
				getProductController().getView().setErrorMessage(errorMsg, getProductController().getView().getInputPane().getProductIdField());
				getProductController().getAudioManager().playNGSound();
				EventBusUtil.publish(new ProductEvent(product, ProductEventType.PRODUCT_INPUT_NG));
			}
		}catch(Exception e){
			getProductController().getView().setErrorMessage(e.getMessage(), getProductController().getView().getInputPane().getProductIdField());
			getProductController().getAudioManager().playNGSound();
			getProductController().cancel();
			e.printStackTrace();
		}

	}

	private boolean isProductValidForShippingToSite(final BaseProduct product) {
		final String site = ((PartShipmentInputPane)getProductController().getView().getInputPane()).getSite();
		final boolean validateShipping; {
			final Map<String,String> shippingValidationMap = getProductController().getModel().getProperty().getShippingValidation();
			if (shippingValidationMap == null || !shippingValidationMap.containsKey(site)) {
				validateShipping = false;
			} else {
				validateShipping = Boolean.parseBoolean(shippingValidationMap.get(site));
			}
		}
		if (validateShipping) {
			final String siteShippingName = site + "_SHIPPING";
			final BuildAttribute siteShippingAttribute = ServiceFactory.getDao(BuildAttributeDao.class).findById(siteShippingName, product.getProductSpecCode());
			if (siteShippingAttribute == null) {
				FXOptionPane.showMessageDialog(getProductController().getView().getMainWindow().getStage(), "Attribute " + siteShippingName + " is not defined for " + product.getProductSpecCode(), "ERROR", FXOptionPane.Type.ERROR);
				return false;
			}
			if (!siteShippingAttribute.getAttributeValue().equalsIgnoreCase("Y")) {
				return false;
			}
		}
		return true;
	}

	public Map<String,Object> check(BaseProduct product) {
		ProductCheckPropertyBean propertyBean= PropertyService.getPropertyBean(ProductCheckPropertyBean.class, getProductController().getModel().getProcessPointId());
		String[] productInputCheckTypes = propertyBean.getProductInputCheckTypes();
		List<String> types = new ArrayList<String>(Arrays.asList(productInputCheckTypes));
		String[] checkTypes = types.toArray(new String[types.size()]);

		String productCheckProcessPointId = getProductCheckProcessPoint();
		ProcessPoint processPoint = null;
		if(StringUtils.isNotEmpty(productCheckProcessPointId)){
			processPoint = ServiceFactory.getDao(ProcessPointDao.class).findById(productCheckProcessPointId);
		}
		return ProductCheckUtil.check(product, processPoint, checkTypes);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String getMessage(String checkType,Object value){

		if (value instanceof List && ((List) (value)).size() > 0) {
			List<Object> resultList = (List<Object>) value;
			String msg = "";
			for (Object obj : resultList) {
				if (obj instanceof String) {
					String s = (String) obj;
					if (s.trim().length() > 0)
						msg = msg + s + ",";
				}
				if (obj instanceof PartResultData) {
					PartResultData s = (PartResultData) obj;
					msg = msg + "\n" + s.part_Name.trim() + "-" + s.part_Reason + ",";
				}
			}
			return  checkType + " : " + msg;
		} else if (value instanceof Boolean
				&& value.equals(Boolean.TRUE)) {
			return checkType;
		} else if (value instanceof Map
				&& ((Map) (value)).size() > 0) {
			Map<String, Object> resultsMap = (Map<String, Object>) value;
			String msg = "";
			for (String key : resultsMap.keySet()) {
				if (resultsMap.get(key) instanceof Boolean && resultsMap.get(key).equals(Boolean.TRUE)) {
					if (key.trim().length() > 0)
						msg = msg + key + ",";
				}
			}
			return checkType + " : " + msg;
		}
		return checkType;
	}

	private String getProductCheckProcessPoint(){
		String site = ((PartShipmentInputPane)getProductController().getView().getInputPane()).getSite();
		Map<String,String> siteAllowPartialProductShipment = getProductController().getModel().getProperty().getAllowPartialBuild();

		if(!StringUtils.isEmpty(site) && siteAllowPartialProductShipment != null){
			String val = siteAllowPartialProductShipment.get(site);
			if(StringUtils.isNotEmpty(val) && Boolean.parseBoolean(val.trim())){
				Map<String,String> partialCheckPP = getProductController().getModel().getProperty().getPartialCheckPp();
				if(partialCheckPP != null){
					return partialCheckPP.get(product.getLastPassingProcessPointId());
				}else{
					return getProductController().getModel().getProperty().getFinalCheckPp();
				}
			}else{
				return getProductController().getModel().getProperty().getFinalCheckPp();
			}
		}

		return null;
	}
	
	public boolean isProductSpecValid(){
			if(StringUtils.equals(getProductController().getModel().getProductTypeData().getProductTypeName(), ProductType.MBPN.name())) {
				Mbpn mbpnProduct = ServiceFactory.getDao(MbpnDao.class).findByKey(product.getProductSpecCode());
				if(mbpnProduct==null) return false;
			}
		return true;
	}
}

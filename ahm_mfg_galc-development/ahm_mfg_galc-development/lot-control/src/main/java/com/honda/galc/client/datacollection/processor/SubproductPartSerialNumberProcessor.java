package com.honda.galc.client.datacollection.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.commons.lang.StringUtils;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.LotControlConstants;
import com.honda.galc.client.datacollection.view.ErrorDialogManager;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.message.MessageType;
import com.honda.galc.client.common.component.Message;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.product.ProductBuildResultDao;
import com.honda.galc.dao.product.ProductHistoryDao;
import com.honda.galc.data.MbpnDef;
import com.honda.galc.data.ProductType;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.net.Request;
import com.honda.galc.property.SubproductPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.SubproductUtil;

public class SubproductPartSerialNumberProcessor extends
		PartSerialNumberProcessor {
	protected SubproductPropertyBean subProductProperty = null;
	protected SubproductUtil subproductUtil;

	public SubproductPartSerialNumberProcessor(ClientContext context) {
		super(context);
	}
	
	@Override
	protected synchronized boolean confirmPartSerialNumber(PartSerialNumber partnumber) {
		subProductProperty = PropertyService.getPropertyBean(SubproductPropertyBean.class, context.getProcessPointId());
		checkPartSerialNumber(partnumber);
	
		if(null != currentSpec && isVerifyPartSerialNumber()){
			/**
			 * Code to check and validate if the part is Subproduct 
			 */
			LotControlRule rule = getController().getState().getCurrentLotControlRule();
			subproductUtil = new SubproductUtil(partnumber, rule,currentSpec);
			if (subproductUtil.isPartSubproduct()) {
				validateAndMarrySubproduct(partnumber, rule);
			}
		}
		
		if(isCheckDuplicatePart())
			checkDuplicatePart(installedPart.getPartSerialNumber());

		installedPart.setValidPartSerialNumber(true);

		return true;

	}
	
	/**
	 * Validate and confirm the correct Subproduct
	 * Perform ProductCheck for the Subproduct if any
	 * 
	 * @param partnumber
	 * @param rule
	 */
	private void validateAndMarrySubproduct(PartSerialNumber partnumber, LotControlRule rule) {
		BaseProduct subProduct = subproductUtil.findSubproduct();
		String installProcessPoint="";
		
		if (subproductUtil.findSubproduct() == null) {
			handleException("Subproduct SN not found: " + partnumber.getPartSn());			
		}
		
		if (!subproductUtil.isValidSpecCode(rule.getPartName().getSubProductType(), subProduct, getController().getState().getProductSpecCode())) {
			handleException("Spec Code of part does not match expected Spec Code.");
		} else {
			if(null != subproductUtil.getMatchedPartSpec()) {
				currentSpec = subproductUtil.getMatchedPartSpec(); 
				installedPart.setPartId(currentSpec.getId().getPartId());
			}
		}
		
		try {
		if(!subProductProperty.isUseMainNoFromPartSpec())
			installProcessPoint =subProductProperty.getInstallProcessPointMap().get(rule.getPartName().getSubProductType());
		else{
			installProcessPoint =subProductProperty.getInstallProcessPointMap().get(getMainNo(subProduct.getProductSpecCode()));
		}
		}catch(NullPointerException e) {
			handleException("No Install process point map property is configured for process point: " + context.getProcessPointId()
					+ " for product type of " + rule.getPartName().getSubProductType());
		}
		
		if(performSubProductChecks(subProduct,rule.getPartName().getSubProductType())){
		
			List<String> failedProductCheckList = new ArrayList<String>();
			try {
				failedProductCheckList = subproductUtil.performSubProductChecks(rule.getPartName().getSubProductType(), 
						subProduct, installProcessPoint);
				if(failedProductCheckList.size() > 0) {
					StringBuffer msg = new StringBuffer();
					msg.append(subProduct.getProductId() + " failed the following Product Checks : ");
					for (int i = 0; i < failedProductCheckList.size(); i++) {
						msg.append(failedProductCheckList.get(i));
						if (i != failedProductCheckList.size() - 1) {
							msg.append(", ");
						}
					}
					Logger.getLogger().info(msg.toString());
					Message m = new Message(LotControlConstants.FAILED_PRODUCT_CHECKS,msg.toString(),MessageType.EMERGENCY);
					m.setInfo(msg.toString());
					getController().getFsm().error(m);
					
					handleException("Failed Product Checks");
				}
			} catch (Exception e) {
				handleException ("Could not perfrom checks against part.");
			}	
		}

		if(!subProduct.getProductType().name().equals(ProductType.ENGINE.name()))  {
			confirmSubProduct(subProduct, partnumber, rule);

			try {
				subproductUtil.performSubproductTracking(rule.getPartName().getSubProductType(), subProduct, installProcessPoint, context.getProcessPointId());
			} catch (Exception e) {
				Logger.getLogger().info("Could not perform tracking on subproduct.");
			}
		}
	}

	/**
	 * Performs check if the part is already been installed
	 * Validate and confirm if the Subproduct is same as previously installed
	 * Prompt dialog box if different Subproduct to confirm from user
	 * if confirm yes, install subproduct to current product 
	 * Delete the record from previously installed product(if unique part)
	 * @param subProduct 
	 * @param partnumber
	 * @param rule
	 */
	private void confirmSubProduct(BaseProduct subProduct, PartSerialNumber partnumber,
			LotControlRule rule) {
		try {
			ProductBuildResultDao<? extends ProductBuildResult, ?> pbrDao = ProductTypeUtil.getProductBuildResultDao(rule.getPartName().getProductType());
			ProductBuildResult installedPart = pbrDao.findById(getController().getState().getProductId(), rule.getPartNameString().toString());
			if (installedPart != null) {
				if(StringUtils.isNotEmpty(installedPart.getPartSerialNumber()) && !installedPart.getPartSerialNumber().equalsIgnoreCase(partnumber.getPartSn())) {
					//display the confirmation dialog
					String msg=partnumber.getPartSn()+" is different than previous install "+ installedPart.getPartSerialNumber() + ". Want to install this new Part to this Product?";
					Logger.getLogger().info(msg.toString());
					int response = showConfirmDialog(msg, LotControlConstants.DIFFERENT_MBPN_ASSIGNED);					    		
			    	if(response == JOptionPane.NO_OPTION || response == JOptionPane.CLOSED_OPTION) {
			    		Logger.getLogger().info("Part not installed, Product already married to "+installedPart.getPartSerialNumber());
			    		handleException ("Part not installed, Product already married to "+installedPart.getPartSerialNumber());
					}
			    	if(response == JOptionPane.YES_OPTION) {
			    		Logger.getLogger().info("install this new Part to this Product");
			    		//Check if duplicate Part need to be checked
			    		if(isCheckDuplicatePart()){
			    			List<? extends ProductBuildResult> samePartSerials = pbrDao.findAllByPartNameAndSerialNumber(rule.getPartName().getPartName(), partnumber.getPartSn());
			    			for(ProductBuildResult pbr : samePartSerials){
			    				if(!pbr.getProductId().equalsIgnoreCase(getController().getState().getProductId()))	
			    					reassignPart(rule, pbrDao, pbr, subProduct);
			    				}
						}
					}
				}
			//check duplicate part only
			}else if(isCheckDuplicatePart()){
				List<? extends ProductBuildResult> samePartSerials = pbrDao.findAllByPartNameAndSerialNumber(rule.getPartName().getPartName(), partnumber.getPartSn());
				for(ProductBuildResult pbr : samePartSerials){
					if(!pbr.getProductId().equalsIgnoreCase(getController().getState().getProductId())){
						String msg=partnumber.getPartSn()+" is already install to "+ pbr.getProductId() + ". Want to install this Part to this Product?";
						Logger.getLogger().info(msg);
						int response = showConfirmDialog(msg, LotControlConstants.MBPN_ALREADY_ASSIGNED);			    		
			    		if(response == JOptionPane.NO_OPTION || response == JOptionPane.CLOSED_OPTION) {
			    			Logger.getLogger().info("Part not installed, Product already married to "+pbr.getPartSerialNumber());
			    			handleException ("Part not installed, Product already married to "+pbr.getPartSerialNumber());
						}
			    		if(response == JOptionPane.YES_OPTION) { 
			    			Logger.getLogger().info("Reassign Part");
			    			reassignPart(rule, pbrDao, pbr, subProduct);
			    		}
					}
				}
			}
		} catch (Exception e) {
				handleException (e.getMessage());
		}
	}

	private void reassignPart(LotControlRule rule,
			ProductBuildResultDao<? extends ProductBuildResult, ?> pbrDao,
			ProductBuildResult pbr,BaseProduct subProduct) {
		String checkProcessPoints ;
		if(!subProductProperty.isUseMainNoFromPartSpec())
			checkProcessPoints = subProductProperty.getCheckPreviousProcessMap().get(rule.getPartName().getSubProductType());
		else{
			checkProcessPoints =subProductProperty.getCheckPreviousProcessMap().get(getMainNo(subProduct.getProductSpecCode()));
		}
		List<String> processPointList = Arrays.asList(checkProcessPoints.split(Delimiter.COMMA));
		ProductHistoryDao<? extends ProductHistory, ?> historyDao = ProductTypeUtil.getProductHistoryDao(rule.getPartName().getProductType());
		for(String ppid : processPointList) {
			List<? extends ProductHistory> results = historyDao.findAllByProductAndProcessPoint(pbr.getProductId(), ppid);
			//Delete the rule only if it hasn't married to VIN in line
			if(results.isEmpty()){
				Logger.getLogger().info(" Not married to VIN in line - Delete the rule ");
				pbrDao.remove(pbr);
			}
			else
				handleException ("Product Already married to "+pbr.getPartSerialNumber()+" and passed Marriage Process Point: "+ pbr.getProcessPointId());;
		}
	}
	
	private String getMainNo(String spec){
		return MbpnDef.MAIN_NO.getValue(spec);
	}
	
	private int showConfirmDialog(String message, String msgId){
		int response = 0;
		if (getController().getProperty().isShowErrorDialog()) {
			ErrorDialogManager mgr = new ErrorDialogManager();
			String request = mgr.showDialog(context.getFrame(),  message, msgId,
					getController().getProperty());
			if(request.equalsIgnoreCase("YES")) return JOptionPane.YES_OPTION;
			else if(request.equalsIgnoreCase("NO")) return JOptionPane.NO_OPTION;
			else {
				if(StringUtils.isNotEmpty(request)){
					installedPart.setPartSerialNumber("");
					installedPart.setValidPartSerialNumber(false);
					getController().getFsm().partSnNg(installedPart, PART_SN_MESSAGE_ID, message);
					runInSeparateThread(new Request(request)); 
				}  
				return JOptionPane.CLOSED_OPTION;
			}
		}else{
			response = JOptionPane.showConfirmDialog(null, message,msgId, JOptionPane.YES_NO_OPTION);
		}
		return response;
	}
	
	protected void runInSeparateThread(final Request request) {
		Thread t = new Thread() {
			public void run() {
				Logger.getLogger().info("Received Request - "+request.toString());
				getController().received(request);
			}
		};

		t.start();
	}
}

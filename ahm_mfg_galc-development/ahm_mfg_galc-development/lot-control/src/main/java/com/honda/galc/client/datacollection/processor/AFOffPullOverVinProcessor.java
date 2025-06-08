package com.honda.galc.client.datacollection.processor;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.LotControlConstants;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.observer.LotControlAudioManager;
import com.honda.galc.client.datacollection.view.ErrorDialogManager;
import com.honda.galc.client.product.view.UiFactory;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.event.Event;
import com.honda.galc.client.ui.event.EventType;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.TagNames;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.HoldResultType;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.property.ProductCheckPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.common.ProductHoldService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.ProductCheckType;
import com.honda.galc.util.ProductCheckUtil;
/**
 * @author Paul Chou
 * @date Jul.6, 2019
 **/
public class AFOffPullOverVinProcessor extends FrameVinProcessor{
	enum VinScanType{CERT_LABEL,FIC,ALL}

	private static final String confirmMessage3 = "Confirmed request to process product out of sequence. Processing product id [%s] and skipping [%s].";
	private static final String ConfirmMessage1 = "The product id entered is not the expected product id.\nPress Yes to Proceed. This action will identify %s expected VIN(s) as Pullover Units(s)!";
	private static final String ConfirmMessage2 = "The product id entered is not the expected product id.\nPress Yes to log on and proceed or No to return!";
	private List<String> skippedProducts;
	private ProductResultDao productResultDao;
	private InstalledPartDao installedPartDao;
	private ProductHoldService holdService;
	private ProductCheckPropertyBean productCheckBean;
	

	public AFOffPullOverVinProcessor(ClientContext context) {
		super(context);
	}

	protected void processProductIdAheadOfExpectedProductId(ProductId productId, String expectedProductId) {
		skippedProducts = getSkippedProduct(productId, expectedProductId);
		if (showErrorMessageDialog(String.format(ConfirmMessage1, skippedProducts.size()),false))  {
			state.getStateBean().setPullOverProductList(skippedProducts);
			processInputProduct(productId);
		} else {
			if(showErrorMessageDialog(String.format(ConfirmMessage2, skippedProducts.size()), false)) {
				if(login()) {
					if (showErrorMessageDialog(String.format(confirmMessage3, productId.getProductId(), skippedProducts), false))  {
						processInputProduct(productId);
					} else {
						Logger.getLogger().info("Declined request to process invalid product.");
						unexpectedProductReceived(productId, expectedProductId);}
				}
			} else {
				Logger.getLogger().info("Declined request to process invalid product.");
				unexpectedProductReceived(productId, expectedProductId);
			}
		}
	}
	
	protected void processProductIdBehandOfExpectedProductId(ProductId productId, String expectedProductId) {
		if(isProcessPullover() && isPulloverProduct(productId)) {
			processPulloverProduct(productId);
		} else 
			super.processProductIdBehandOfExpectedProductId(productId, expectedProductId);
		
	}


	private boolean isPulloverProduct(ProductId productId) {
		/**
		 * 1. product has 595 pullover history
		 * 2. product does not has build result of AF OFF
		 */
        return !getProductResultDao().findAllByProductAndProcessPoint(productId.getProductId(), context.getFrameLinePropertyBean().getPulloverProcessPointId()).isEmpty();
		
	}

	private boolean isProcessPullover() {
		return !StringUtils.isEmpty(context.getFrameLinePropertyBean().getPulloverProcessPointId());
	}

	private void processPulloverProduct(ProductId productId) {
		if(!property.isResetNextExpectedProduct()){
			DataCollectionController.getInstance(context.getAppContext().getApplicationId()).getState().getStateBean().setResetNextExpected(false);                                                                
		}
		EventBus.publish(new Event(this, productId.getProductId(), EventType.CHANGED));      
		
	}

	private void processInputProduct(ProductId productId) {
		productId.setProductId(productId.getProductId());
		state.setExpectedProductId(productId.getProductId());
		EventBus.publish(new Event(this, productId.getProductId(), EventType.CHANGED));
		
	}


	private List<String> getSkippedProduct(ProductId productId, String expectedProductId) {
		 List<String> incomingProducts = context.getDbManager().getIncomingProducts(DataCollectionController.getInstance().getState());
		 List<String> skippedList = new ArrayList<String>();
		 incomingProducts.add(0, expectedProductId);
		 for(String prodId: incomingProducts) {
			 if(StringUtils.trimToEmpty(prodId).equals(productId.getProductId()))
				 break;
			 skippedList.add(prodId);
		 } 
		return skippedList;
	}

	
	public boolean showColoredConfirmationDialog(final String msg) {
		return showColoredConfirmationDialog(msg, "Confirmation");
	}
	
	public boolean showColoredConfirmationDialog(final String msg, final String title) {
		return showColoredConfirmationDialog(msg, Color.yellow, title);
	}

	private boolean showColoredConfirmationDialog(String message, Color color, String title) {
		JTextArea commentTextArea = new JTextArea(message, 10, 40);
		commentTextArea.setBackground(color);
		commentTextArea.setFont(UiFactory.getInfo().getLabelFont());
		commentTextArea.setLineWrap(true);
		commentTextArea.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(commentTextArea);
		scrollPane.setBackground(color);

		int result = JOptionPane.showConfirmDialog(context.getFrame(), scrollPane, title, JOptionPane.YES_NO_OPTION);
		return JOptionPane.YES_OPTION == result;
	}

	public ProductResultDao getProductResultDao() {
		if(productResultDao == null)
			productResultDao = ServiceFactory.getDao(ProductResultDao.class);
		return productResultDao;
	}
	
	protected void confirmProductId(ProductId productId)
			throws SystemException, TaskException, IOException
	{
        validateVinScanType(productId);

		String vin = context.isRemoveIEnabled() ? context.removeLeadingVinChars(productId.getProductId()) : productId.getProductId();
		if( property.isConfirmSpecCheck() && isSpecCheckHold(vin)) {

			if(MessageDialog.confirm (context.getFrame(), "Was the AF Spec Check process completed for this VIN?")) {
				releaseProduct(vin);
			}

		}
		
		super.confirmProductId(productId);
	}

	private void releaseProduct(String productId) {
		DataContainer dc = new DefaultDataContainer();
		dc.put(TagNames.PRODUCT_ID.name(), productId);
		dc.put(TagNames.PRODUCT_TYPE.name(), context.getProductType().toString());
		dc.put(TagNames.PROCESS_POINT_ID.name(), context.getProcessPointId());
		dc.put(TagNames.HOLD_REASON.name(), getProductCheckPropertyBean().getSpecCheckHoldReason());
		dc.put(TagNames.RELEASE_REASON.name(), getProductCheckPropertyBean().getSpecCheckHoldReason());
		dc.put(TagNames.ASSOCIATE_ID.name(), context.getUserId());
		dc.put(TagNames.HOLD_RESULT_TYPE.name(), getProductCheckBean().getSpecCheckHoldType());
		
		
		getHoldService().qsrRelease(dc);
		
	}

	private boolean isSpecCheckHold(String productId) {
		HoldResultType holdType = HoldResultType.valueOf(getProductCheckBean().getSpecCheckHoldType());
		return getHoldService().isQsrHoldBySpecCheck(productId, holdType, getProductCheckBean().getSpecCheckHoldReason());
	}


	protected boolean executeCheck(BaseProduct product, ProcessPoint processPoint) {
		String[] checkTypes = getProductCheckPropertyBean().getProductNotProcessableCheckTypes();
		if (checkTypes == null || checkTypes.length == 0) {
			return true;
		}
		Map<String, Object> checkResults = ProductCheckUtil.check(product, processPoint, checkTypes);
		if (checkResults == null || checkResults.isEmpty()) {
			return true;
		}
		String message =  "Failed Product Checks: \n" + ProductCheckUtil.formatTxt(checkResults);
		Logger.getLogger().error(message);

		if (getProductCheckPropertyBean().isFailedAckRequired()) {
			// play NG sound
			try {
				LotControlAudioManager.getInstance().playNGSound();
			} catch (Exception e) {
				Logger.getLogger().error(e);
			}

			// show the error acknowledgement dialog
			ErrorDialogManager mgr = new ErrorDialogManager();
			mgr.showDialog(context.getFrame(), "FAILED PRODUCT CHECKS: " +  System.getProperty("line.separator") + formatTxt(checkResults), LotControlConstants.FAILED_PRODUCT_CHECKS_ACK, property);
		}
		handleException(message);
		return false;
	}
	
	public static String formatTxt(Map<String, Object> checkResults) {
		if (checkResults == null || checkResults.isEmpty()) {
			return "";
		}
		int rowCount = 0;
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, Object> entry : checkResults.entrySet()) {
			rowCount++;
			String name = entry.getKey();
			Object result = entry.getValue();
			if (name == null) {
				continue;
			}
			ProductCheckType checkType = ProductCheckType.get(name);
			if (checkType != null) {
				name = checkType.getName();
			}
			sb.append(rowCount).append(".").append(name);
			String formattedResult = ProductCheckUtil.formatTxt(result, System.getProperty("line.separator"));
			if (StringUtils.isNotBlank(formattedResult)) {
				sb.append(System.getProperty("line.separator")).append(formattedResult);
			}
			sb.append("   ").append(System.getProperty("line.separator"));
		}
		return sb.toString();
	}
	
	protected boolean showErrorMessageDialog(String msg, boolean displayOnly) {
		if (displayOnly) {
			MessageDialog.showError(msg);
			return true;
		} else {
			if (property.isShowErrorDialog()) {
				return showColoredConfirmationDialog(msg);
			} else {
				return MessageDialog.confirm(context.getFrame(), msg);
			}
		}
	}

	public ProductHoldService getHoldService() {
		if(holdService == null)
			holdService = ServiceFactory.getService(ProductHoldService.class);
		return holdService;
	}

	public ProductCheckPropertyBean getProductCheckBean() {
		if(productCheckBean == null)
			productCheckBean = PropertyService.getPropertyBean(ProductCheckPropertyBean.class, context.getProcessPointId());
		return productCheckBean;
	}


	
}

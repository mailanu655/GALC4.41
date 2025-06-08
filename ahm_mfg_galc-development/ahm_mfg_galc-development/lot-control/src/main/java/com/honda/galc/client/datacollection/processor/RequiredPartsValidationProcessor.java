package com.honda.galc.client.datacollection.processor;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.RequiredPartDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.ProductTypeCatalog;
import com.honda.galc.data.TagNames;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;

public class RequiredPartsValidationProcessor extends PartSerialNumberProcessor {

	private static final String REQUIRED_PART = "REQUIRED_PART";
		
	public RequiredPartsValidationProcessor(ClientContext context) {
		super(context);
	}

	@Override
	public synchronized boolean execute(PartSerialNumber partnumber) {

		Logger.getLogger()
				.debug("RequiredPartsValidationProcessor : Enter execute");
		try {
			
			List<String> requiredParts = null;

			String productType = PropertyService.getProperty(REQUIRED_PART,
					TagNames.PRODUCT_TYPE.toString());
			
			String processPointId = PropertyService.getProperty(REQUIRED_PART,
					TagNames.PROCESS_POINT_ID.toString());
			
			if (productType == null)
				throw new Exception(
						"Exception occured due to product type data not found in database");

			BaseProduct product = (BaseProduct) ProductTypeUtil.getProductDao(
					productType).findBySn(partnumber.getPartSn());

			if (product == null)
				throw new Exception(
						"Exception occured due to MbpnProduct not found in database");

			requiredParts = ServiceFactory.getDao(RequiredPartDao.class)
					.findMissingRequiredParts(product.getProductSpecCode(),
							StringUtils.isEmpty(processPointId) ? context.getProcessPointId() : processPointId.trim() , 
							ProductTypeCatalog.getProductType(productType),
							partnumber.getPartSn(), "");

			if (requiredParts == null)
				throw new Exception(
						"Exception occured due to RequiredPartsTbx data not found in database");

			if (requiredParts.size() > 0){
				installedPart.setInstalledPartStatus(InstalledPartStatus.NG);
				installedPart.setValidPartSerialNumber(false);
			}
			else{
				installedPart.setInstalledPartStatus(InstalledPartStatus.OK);
				installedPart.setValidPartSerialNumber(true);
			}
			installedPart.setPartSerialNumber(partnumber.getPartSn());
			installedPart.setPartId(getController().getState()
					.getCurrentLotControlRulePartList().get(0).getId()
					.getPartId());
			

			getController().getFsm().partSnOk(installedPart);

			Logger.getLogger()
					.debug("RequiredPartsValidationProcessor:: Exit  execute ok");
			return true;

		} catch (TaskException te) {
			Logger.getLogger().error(te.getMessage());
			installedPart.setValidPartSerialNumber(false);
			getController().getFsm().partSnNg(installedPart,
					PART_SN_MESSAGE_ID, te.getMessage());
		} catch (SystemException se) {
			Logger.getLogger().error(se, se.getMessage());
			installedPart.setValidPartSerialNumber(false);
			getController().getFsm().error(
					new Message(PART_SN_MESSAGE_ID, se.getMessage()));
		} catch (Exception e) {
			Logger.getLogger().error(
					e,
					"ThreadID = " + Thread.currentThread().getName()
							+ " :: execute() : Exception : " + e.toString());
			getController().getFsm()
					.error(new Message("MSG01", e.getMessage()));
		} catch (Throwable t) {
			Logger.getLogger().error(
					t,
					"ThreadID = " + Thread.currentThread().getName()
							+ " :: execute() : Exception : " + t.toString());
			getController().getFsm()
					.error(new Message("MSG01", t.getMessage()));
		}
		Logger.getLogger()
				.debug("RequiredPartsValidationProcessor:: Exit execute ng");
		return false;
	}

}

package com.honda.galc.client.datacollection.strategy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.processor.PartSerialNumberProcessor;
import com.honda.galc.client.datacollection.property.EngineWeightPropertyBean;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.PartCheckType;
import com.honda.galc.util.PartCheckUtil;

/**
 * 
 * @author Wade Pei <br>
 * @date Jan 19, 2014
 */
public class ConRodWeightValidation extends PartSerialNumberProcessor {
	private static List<String> conRodWeightCategories;
	private String productId = null;
	private String category = null;

	public ConRodWeightValidation(ClientContext context) {
		super(context);
	}

	public void init() {
		super.init();
		if (null == conRodWeightCategories || conRodWeightCategories.isEmpty()) {
			synchronized (ConRodWeightValidation.class) {
				EngineWeightPropertyBean engineWeightProperty = PropertyService.getPropertyBean(EngineWeightPropertyBean.class, context.getProcessPointId());
				conRodWeightCategories = Collections.unmodifiableList(Arrays.asList(engineWeightProperty.getConRodWeightCategories()));
			}
		}
	}

	@Override
	protected boolean confirmPartSerialNumber(PartSerialNumber partnumber) {
		List<PartSpec> partList = getController().getState().getCurrentLotControlRulePartList();
		String currProductId = getController().getState().getProductId();
		String partNumber = partnumber == null ? null : partnumber.getPartSn();
		String[] partCheckTypes = new String[] { PartCheckType.PART_NUMBER_NULL_CHECK.name(), PartCheckType.PRODUCT_ID_NULL_CHECK.name() };
		PartCheckUtil.checkWithExceptionalHandling(partNumber, currProductId, partCheckTypes);

		// Check if the category exists.
		if (!conRodWeightCategories.contains(partNumber)) {
			handleException(String.format("Non-exist category %s. Valid categories are %s", partNumber, conRodWeightCategories.toString()));
		}

		if (null == productId || !productId.equals(currProductId)) {
			productId = currProductId;
			category = partNumber;
		} else {
			if (null != category && !category.equals(partNumber)) {
				handleException("Inconsistent category.");
			}
		}
		installedPart.setPartSerialNumber(partNumber);
		installedPart.setPartId(partList.get(0).getId().getPartId());
		installedPart.setInstalledPartStatus(InstalledPartStatus.OK);
		installedPart.setValidPartSerialNumber(true);
		return true;
	}

}

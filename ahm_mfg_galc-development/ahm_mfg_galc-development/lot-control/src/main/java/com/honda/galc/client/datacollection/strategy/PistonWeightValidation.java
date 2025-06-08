package com.honda.galc.client.datacollection.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.processor.PartSerialNumberProcessor;
import com.honda.galc.client.datacollection.property.EngineWeightPropertyBean;
import com.honda.galc.constant.DeviceMessageSeverity;
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
public class PistonWeightValidation extends PartSerialNumberProcessor {
	private String productId = null;
	private static Map<String, List<Double>> pistonWeightCategories;
	private String category = null;

	public PistonWeightValidation(ClientContext context) {
		super(context);
	}

	public void init() {
		super.init();
		if (null == pistonWeightCategories || pistonWeightCategories.isEmpty()) {
			synchronized (PistonWeightValidation.class) {
				EngineWeightPropertyBean engineWeightProperty = PropertyService.getPropertyBean(EngineWeightPropertyBean.class,
						context.getProcessPointId());
				pistonWeightCategories = new HashMap<String, List<Double>>();
				Map<String, String> pistonWeightCategoriesStr = engineWeightProperty.getPistonWeightCategories();
				String[] ranges;
				List<Double> rangeList;
				for (String category : pistonWeightCategoriesStr.keySet()) {
					ranges = pistonWeightCategoriesStr.get(category).split("\\s*,\\s*");
					if (null == ranges || ranges.length < 2) {
						continue;
					}
					rangeList = new ArrayList<Double>(2);
					for (int i = 0; i < 2; i++) {
						rangeList.add(Double.parseDouble(ranges[i]));
					}
					pistonWeightCategories.put(category, rangeList);
				}
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

		List<Double> ranges;
		double value = 0.0d;
		try {
			value = Double.parseDouble(partNumber);
		} catch (NumberFormatException e) {
			handleException("Please input a float value.");
		}
		String errMsgOutOfAllRanges = String.format("Value %.2f is out of the range of all categories.", value);
		if (null == productId || !productId.equals(currProductId) || null == category) {
			productId = currProductId;
			setCategoryByValue(value);
			if (!isValueInRange(value)) {
				handleException(errMsgOutOfAllRanges);
			}
		} else {
			if (!isValueInRange(value)) {
				if (null == category) {
					handleException(errMsgOutOfAllRanges);
				} else {
					ranges = pistonWeightCategories.get(category);
					handleException(String.format("Value %.2f is out of the range of category %s (%.2f-%.2f).", value, category,
							ranges.get(0), ranges.get(1)));
				}
			}
		}
		installedPart.setPartSerialNumber(partNumber);
		installedPart.setPartId(partList.get(0).getId().getPartId());
		installedPart.setInstalledPartStatus(InstalledPartStatus.OK);
		installedPart.setValidPartSerialNumber(true);
		return true;
	}

	private void setCategoryByValue(double value) {
		category = null;
		List<Double> range;
		double min, max;
		for (String category : pistonWeightCategories.keySet()) {
			range = pistonWeightCategories.get(category);
			min = range.get(0);
			max = range.get(1);
			if (Double.compare(min, value) <= 0 && Double.compare(value, max) < 0) {
				this.category = category;
				break;
			}
		}
	}

	private boolean isValueInRange(double value) {
		if (null == category) {
			return false;
		}
		List<Double> range = pistonWeightCategories.get(category);
		double min = range.get(0), max = range.get(1);
		return Double.compare(min, value) <= 0 && Double.compare(value, max) < 0;
	}

}

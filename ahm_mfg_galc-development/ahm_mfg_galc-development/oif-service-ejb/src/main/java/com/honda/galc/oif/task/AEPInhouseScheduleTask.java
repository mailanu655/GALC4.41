package com.honda.galc.oif.task;

import static com.honda.galc.service.ServiceFactory.getDao;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.product.BuildAttributeDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.BuildAttributeId;
import com.honda.galc.oif.dto.GPP631DTO;

public class AEPInhouseScheduleTask extends InHousePriorityPlanGPP631Task<GPP631DTO> {

	BuildAttributeDao buildAttrDao = getDao(BuildAttributeDao.class);
	public AEPInhouseScheduleTask(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see com.honda.galc.oif.task.InHousePriorityPlanGPP631Task#processInHousePriorityPlan()
	 */
	@Override
	protected void processInHousePriorityPlan() {
		super.processInHousePriorityPlan();
		saveAllYmto();
	}
	
	private void saveAllYmto()  {
		if(listOfPlan631 == null || listOfPlan631.isEmpty())  {
			return;
		}
		//process build attributes
		for (GPP631DTO plan631 : listOfPlan631)  {
			String attr = "YMTO";
			String specCode = plan631.getMbpn();
			String attrVal = plan631.getYmto();
			BuildAttributeId bId = new BuildAttributeId(attr, specCode);
			BuildAttribute buildAttr = buildAttrDao.findByKey(bId);
			// if incoming attribute value is blank, nothing to do
			if(StringUtils.isBlank(attrVal))  continue;
			// if build attribute not exist, create new
			if(buildAttr == null)  {
				buildAttr = new BuildAttribute(specCode, attr, attrVal);
				buildAttr.setProductType(ProductType.MBPN.name());
			}
			else  { // build attribute already exists
				String oldAttrVal = buildAttr.getAttributeValue();
				// update if: existing value is blank OR
				// existing value not equal to new value
				if(StringUtils.isBlank(oldAttrVal) || !oldAttrVal.trim().equals(attrVal.trim()))  {
					buildAttr.setAttributeValue(attrVal);
				}
			}
			if(buildAttr != null)  {
			  buildAttrDao.save(buildAttr);
			}
		}
	}
}

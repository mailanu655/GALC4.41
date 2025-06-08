package com.honda.galc.client.teamleader.model;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.client.mvc.AbstractModel;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.LineSideContainerValue;
import com.honda.galc.data.TagNames;
import com.honda.galc.dto.ScrappedProductDto;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.property.QiPropertyBean;
import com.honda.galc.property.UnscrapPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.defect.NaqUnscrapService;
import com.honda.galc.service.defect.UnscrapService;
import com.honda.galc.service.property.PropertyService;

public class UnscrapViewModel extends AbstractModel {
	private List<ScrappedProductDto> scrappedProductDto;

	private DataContainer validateResultDc;
	private DataContainer unscrapResultDc;

	private UnscrapPropertyBean unscrapPropertyBean;

	private String applicationId;

	private String unscrapResultMessage;

	public UnscrapViewModel() {
		super();
	}

	@SuppressWarnings("unchecked")
	public DataContainer validateProductForUnscrap(DefaultDataContainer requestDc) {
		UnscrapService unscrapService = ServiceFactory.getService(UnscrapService.class);
		validateResultDc = unscrapService.validateProductsForUnscrap(requestDc);
		scrappedProductDto = (List<ScrappedProductDto>) validateResultDc.get(TagNames.PRODUCT_INFO_LIST.name());
		return validateResultDc;
	}

	public boolean unscrapProducts(DefaultDataContainer requestDc,List<ScrappedProductDto> scrappedProductDtoList) {

		List<String> scrappedProductList = new ArrayList<String>();
		boolean isSuccessfulUnscrap = true;
		List<String> naqScrappedProductList = new ArrayList<String>();

		for(ScrappedProductDto currentScrappedProductDto : scrappedProductDtoList) {
			if(currentScrappedProductDto.isNaqScrap()) {
				naqScrappedProductList.add(currentScrappedProductDto.getProduct().getProductId());
			} else {
				scrappedProductList.add(currentScrappedProductDto.getProduct().getProductId());
			}
		}

		if(!naqScrappedProductList.isEmpty()) {
			requestDc.put(TagNames.PRODUCT_ID.name(), naqScrappedProductList);
			UnscrapService unscrapService = ServiceFactory.getService(NaqUnscrapService.class);
			unscrapResultDc = unscrapService.unscrapProduct(requestDc);
			if(unscrapResultDc.get(TagNames.REQUEST_RESULT.name()).equals(LineSideContainerValue.NOT_COMPLETE)) {
				isSuccessfulUnscrap = false;
				unscrapResultMessage = unscrapResultDc.getString(TagNames.MESSAGE.name());
			}
		}
		if(!scrappedProductList.isEmpty() && isSuccessfulUnscrap) {
			requestDc.put(TagNames.PRODUCT_ID.name(), scrappedProductList);
			UnscrapService unscrapService = ServiceFactory.getService(UnscrapService.class);
			unscrapResultDc = unscrapService.unscrapProduct(requestDc);

			if(unscrapResultDc.get(TagNames.REQUEST_RESULT.name()).equals(LineSideContainerValue.NOT_COMPLETE)) {
				isSuccessfulUnscrap = false;
				unscrapResultMessage = unscrapResultDc.getString(TagNames.MESSAGE.name());
			}
		}
		return isSuccessfulUnscrap;
	}

	public String getProcessPointName(String processPointId) {
		String processPointName = "";
		ProcessPoint processPoint =  ServiceFactory.getDao(ProcessPointDao.class).findById(processPointId);
		if(processPoint != null) {
			processPointName = processPoint.getProcessPointName();
		}
		return processPointName;
	}

	public int getMaxUnscrapCount() {
		return getUnscrapPropertyBean().getMaxUnscrapCount();
	}

	@Override
	public void reset() {
		validateResultDc.clear();
		unscrapResultDc.clear();
	}

	private UnscrapPropertyBean getUnscrapPropertyBean() {
		if(unscrapPropertyBean == null) {
			unscrapPropertyBean = PropertyService.getPropertyBean(UnscrapPropertyBean.class, applicationId);
		}
		return unscrapPropertyBean;
	}

	public List<ScrappedProductDto> getScrappedProductDto() {
		return this.scrappedProductDto;
	}
	
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getUnscrapResultMessage() {
		return this.unscrapResultMessage;
	}
	
	public boolean isDcStation() {
		return PropertyService.getPropertyBean(QiPropertyBean.class, applicationContext.getProcessPointId()).isDcStation();
	}
}

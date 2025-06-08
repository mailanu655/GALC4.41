package com.honda.galc.handheld.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.Action;

import com.honda.galc.dao.conf.LineDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.PartNameDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.PartName;
import com.honda.galc.handheld.data.HandheldConstants;
import com.honda.galc.handheld.data.HandheldWebPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.ProductCheckUtil;
import com.honda.galc.util.StringUtil;

public abstract class HandheldAction extends Action {
	private HandheldWebPropertyBean handheldPropertyBean;
	private String persistenceErrorMessage;

	public HandheldWebPropertyBean getHandheldPropertyBean() {
		if(handheldPropertyBean == null)
			handheldPropertyBean = PropertyService.getPropertyBean(HandheldWebPropertyBean.class);
		return handheldPropertyBean;
	}

	private String[] getInstalledPartUpdateDivisions(String productType) {
		Map<String, String> divisionMap = getHandheldPropertyBean().getProductTypeInstalledPartUpdateDivisionsMap();
		String[] divisions = null;
		for(String eachProductType : divisionMap.keySet()) {
			if(eachProductType.equals(productType)) {
				divisions = divisionMap.get(eachProductType).split(",");
				break;
			}
		}
		return divisions;
	}

	protected List<String> getInstalledPartUpdateDivisions() {
		Map<String, String> divisionMap = getHandheldPropertyBean().getProductTypeInstalledPartUpdateDivisionsMap();
		List<String> divisions = new ArrayList<String>();
		for(String eachProductType : divisionMap.values())
			divisions.addAll(Arrays.asList(eachProductType.split(",")));
		return divisions;
	}
	
	private String getTrimmedProductDivision(BaseProduct product) {
		if (product.getTrackingStatus() != null) {
			Line line = ServiceFactory.getDao(LineDao.class).findByKey(product.getTrackingStatus());
			if (line != null)
				return line.getDivisionId().trim();
		} else if (product.getLastPassingProcessPointId() != null) {
			ProcessPoint processPoint = ServiceFactory.getDao(ProcessPointDao.class).findById(product.getLastPassingProcessPointId());
			if (processPoint != null)
				return processPoint.getDivisionId().trim();
		}
		return "";
	}

	protected boolean isInstalledPartProductInInstalledPartUpdateDivision(InstalledPart installedPart) {
		PartName partName = ServiceFactory.getDao(PartNameDao.class).findByKey(installedPart.getPartName());
		return isProductInInstalledPartUpdateDivision(ProductTypeUtil.getProductDao(ProductType.getType(partName.getProductType().name())).findByKey(installedPart.getProductId()));
	}

	protected boolean isProductInInstalledPartUpdateDivision(BaseProduct product) {
		if(isProductExemptFromUpdateDivisionCheck(product))
			return true;
		
		String[] divisions = getInstalledPartUpdateDivisions(product.getProductType().name());
		if (divisions == null)
			return false;
	
		String productDivision = getTrimmedProductDivision(product);
		String matchingDivision = null;
		for (String eachDivisionId : divisions) {
			if (eachDivisionId.trim().equals(productDivision)) {
				matchingDivision = eachDivisionId;
				break;
			}
		}
		
		return matchingDivision != null && !ProductCheckUtil.isProductShipped(product.getProductId());
	}

	private boolean isProductExemptFromUpdateDivisionCheck(BaseProduct product) {
		for (String eachProductType : getHandheldPropertyBean().getProductTypesToIgnoreForUpdateDivisionChecks()) {
			if (eachProductType.equals(product.getProductType().name()))
				return true;
		}
		return false;
	}

	public void resetPersistenceErrorMessage() {
		persistenceErrorMessage = null;
	}
	
	public String getPersistenceErrorMessage() {
		if (persistenceErrorMessage == null)
			persistenceErrorMessage = "";
		return persistenceErrorMessage;
	}

	public void setPersistenceErrorMessage(String persistenceErrorMessage) {
		this.persistenceErrorMessage = persistenceErrorMessage;
	}

	protected void appendPersisteceErrorMessage(String message) {
		if(!StringUtil.isNullOrEmpty(getPersistenceErrorMessage()))
			setPersistenceErrorMessage(getPersistenceErrorMessage() +'\n');
		setPersistenceErrorMessage(getPersistenceErrorMessage() + message);
	}

	protected void updateSessionErrorMessages(HttpServletRequest request) {
		if (!StringUtil.isNullOrEmpty(getPersistenceErrorMessage())) {
			request.getSession().setAttribute(HandheldConstants.PERSISTENCE_ERROR_MESSAGE, getPersistenceErrorMessage());
			request.getSession().setAttribute(HandheldConstants.ERROR_SOURCE, formName());
		}
	}
	
	protected String getErrorSource(HttpServletRequest request) {
		return (String)request.getSession().getAttribute(HandheldConstants.ERROR_SOURCE);
	}
	
	protected abstract String formName();
}
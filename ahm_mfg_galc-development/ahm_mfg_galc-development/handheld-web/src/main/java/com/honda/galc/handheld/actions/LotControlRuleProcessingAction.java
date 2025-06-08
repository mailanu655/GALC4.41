package com.honda.galc.handheld.actions;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.transaction.annotation.Transactional;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.MeasurementDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.datacollection.LotControlRuleCache;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.enumtype.PartNameVisibleType;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.BaseProductSpec;
import com.honda.galc.entity.product.BaseProductSpecDao;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.MeasurementSpec;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.handheld.data.HandheldConstants;
import com.honda.galc.handheld.forms.ScanProductIdForm;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.StringUtil;
import org.apache.openjpa.persistence.PersistenceException;

public abstract class LotControlRuleProcessingAction extends ValidatedUserHandheldAction {
	private static final String DEFAULT_PART_ID = "A0000";
	protected BaseProduct product;
	private ProcessPoint processPoint;
	private ProductType productType;
	private ScanProductIdForm scanProductIdForm;

	public LotControlRuleProcessingAction() {
		super();
	}

	public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
		setProductType((ProductType)request.getSession().getAttribute(HandheldConstants.PRODUCT_TYPE));
		return super.execute(mapping, form, request, response);
	}
	
	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}

	public ProcessPoint getProcessPoint() {
		return processPoint;
	}

	public ScanProductIdForm getScanProductIdForm() {
		return scanProductIdForm;
	}

	public void setScanProductIdForm(ScanProductIdForm scanProductIdForm) {
		this.scanProductIdForm = scanProductIdForm;
	}

	public void setProcessPoint(ProcessPoint processPoint) {
		this.processPoint = processPoint;
	}

	public BaseProduct getProduct() {
		return product;
	}

	public void setProduct(BaseProduct product) {
		this.product = product;
	}

	protected List<Measurement> getExistingMeasurementsForRule(
			LotControlRule aRule) {
		return ServiceFactory.getDao(MeasurementDao.class)
				.findAllByProductIdAndPartNames(
						product.getProductId(),
						new ArrayList<String>(Arrays.asList(aRule
								.getPartNameString())));
	}

	protected boolean areAllMeasurementsComplete(List<Measurement> measurements) {
		for (Measurement eachMeasurement : measurements) {
			if (!eachMeasurement.isStatus()) {
				return false;
			}
		}
		return true;
	}

	protected InstalledPart getInstalledPartFor(LotControlRule aRule) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		InstalledPart persistentInstalledPart = getExistingInstalledPartFor(aRule
				.getPartNameString());
		if (persistentInstalledPart == null) {
			persistentInstalledPart = newInstalledPartNamed(aRule.getPartNameString(), now);
		}
		persistentInstalledPart.setAssociateNo(getUserId());
		persistentInstalledPart.setActualTimestamp(now);
		return persistentInstalledPart;
	}

	protected List<MeasurementSpec> getMeasurementSpecsFrom(LotControlRule aRule) {
		for (PartSpec eachPartSpec : aRule.getParts()) {
			if (eachPartSpec.getMeasurementCount() > 0)
				return eachPartSpec.getMeasurementSpecs();
		}
		return new ArrayList<MeasurementSpec>();
	}

	protected List<InstalledPart> getExistingInstalledParts() {
		if (getProduct() == null || getScanProductIdForm().getPartNames().isEmpty())
			return new ArrayList<InstalledPart>();
		return ServiceFactory.getDao(InstalledPartDao.class)
				.findAllByProductIdAndPartNames(getProduct().getProductId(),
						getScanProductIdForm().getPartNames());
	}

	protected InstalledPart getExistingInstalledPartFor(String partName) {
		for (InstalledPart eachInstalledPart : getExistingInstalledParts()) {
			if (eachInstalledPart.getPartName().equals(partName))
				return eachInstalledPart;
		}
		return null;
	}

	protected InstalledPart newInstalledPartNamed(String name, Timestamp now) {
		InstalledPart result = (InstalledPart) ProductTypeUtil.createBuildResult(getProductType().name());
		result.setProductId(getProduct().getProductId());
		result.setPartName(name);
		result.setCreateTimestamp(now);
		result.setProductType(getProductType().name());
		return result;
	}

	protected BaseProduct getProductForProductId(String productId) {
		try {
			return ProductTypeUtil.getProductDao(getProductType()).findByKey(productId);
		} catch (PersistenceException e) {
			//nothing to report just accept the exception and move on
			return null;
		}
	}

	protected BaseProductSpec getCurrentProductSpecCode() {
		return getCurrentProductSpecCode(getProduct());
	}
	
	@SuppressWarnings("unchecked")
	protected BaseProductSpec getCurrentProductSpecCode(BaseProduct product) {
		@SuppressWarnings("rawtypes")
		BaseProductSpecDao productSpecDao = getCurrentProductType();
		return productSpecDao.findByKey(product.getProductSpecCode());
	}

	private BaseProductSpecDao<? extends BaseProductSpec, ?> getCurrentProductType() {
		return ProductTypeUtil.getProductSpecDao(getProductType());
	}
	
	protected List<LotControlRule> getRulesForProductAndStation(ProcessPoint selectedStation) {
		List<LotControlRule> filteredRules = new ArrayList<LotControlRule>();
		for(LotControlRule eachUnfilteredRule : getUnfilteredRulesForProductAndStation(selectedStation)) {
			if (eachUnfilteredRule.getPartName().getPartVisible()==PartNameVisibleType.EDITABLE.getId())
				filteredRules.add(eachUnfilteredRule);
		}
		return filteredRules;
	}

	protected List<LotControlRule> getUnfilteredRulesForProductAndStation(ProcessPoint selectedStation) {
		return LotControlRuleCache.getOrLoadLotControlRule(getCurrentProductSpecCode(), selectedStation.getProcessPointId());
	}

	protected void addMeasurementsToPartForRule(InstalledPart installedPart,
			LotControlRule aRule, boolean shouldMarkAsComplete) {
		installedPart.setInstalledPartStatus(shouldMarkAsComplete
				? InstalledPartStatus.OK
				:InstalledPartStatus.NG);
		for (MeasurementSpec eachMeasurementSpec : getMeasurementSpecsFrom(aRule)) {
			Measurement newMeasurement = new Measurement(getProduct()
					.getProductId(), aRule.getPartNameString(),
					eachMeasurementSpec.getId().getMeasurementSeqNum());
			newMeasurement.setMeasurementStatus(MeasurementStatus.OK);
			newMeasurement.setMeasurementValue(getMedianValueFromSpec(eachMeasurementSpec));
			newMeasurement.setActualTimestamp(installedPart.getActualTimestamp());
			installedPart.getMeasurements().add(newMeasurement);
		}
		logInfo(String.format("Adding measurements for rule: %1s on process: %2s - %3s", aRule.getPartNameString(), getProcessPoint().getProcessPointId(), getProcessPoint().getProcessPointName()));
	}

	private double getMedianValueFromSpec(MeasurementSpec eachMeasurementSpec) {
		return Math.round((((eachMeasurementSpec
				.getMaximumLimit() - eachMeasurementSpec
				.getMinimumLimit()) / 2)
				+ eachMeasurementSpec.getMinimumLimit()) * 100.0) / 100.0;
	}

	protected abstract void compilePersisentResultsForRule(
			LotControlRule aRule, List<InstalledPart> partsToBeRemoved,
			List<InstalledPart> persistentInstalledParts);

	@Transactional
	protected void storeResults() {
		List<InstalledPart> partsToBeRemoved = new ArrayList<InstalledPart>();
		List<InstalledPart> persistentInstalledParts = new ArrayList<InstalledPart>();
		Timestamp now = new Timestamp(System.currentTimeMillis());
		setPersistenceErrorMessage(null);
		//go through the rule for the the receiver and prepare results for each
		for (LotControlRule eachRule : getScanProductIdForm().getRules()) {
			try {
				logInfo(String.format("processing rule: %1s on process: %2s - %3s", eachRule.getPartNameString(), getProcessPoint().getProcessPointId(), getProcessPoint().getProcessPointName()));
				compilePersisentResultsForRule(eachRule, partsToBeRemoved, persistentInstalledParts);
			} catch (TaskException e) {
				appendPersisteceErrorMessage(e.getMessage());
			}
		}
		
		for (InstalledPart eachPart : persistentInstalledParts) {
			eachPart.setActualTimestamp(now);
			eachPart.setInstalledPartReason(InstalledPartStatus.REPAIRED.name());
			if (StringUtil.isNullOrEmpty(eachPart.getPartId()))
				updatePartId(eachPart);
		}
		
		try {
			InstalledPartDao dao = ServiceFactory.getDao(InstalledPartDao.class);
			for(InstalledPart eachPartToBeDeleted : partsToBeRemoved) {
				markInstalledPartAsRemoved(eachPartToBeDeleted);
				persistentInstalledParts.add(eachPartToBeDeleted);
			}
			logInfo(String.format("attempting to save %1d results for %2s on process: %3s - %4s", persistentInstalledParts.size(), getProduct().getProductId(), getProcessPoint().getProcessPointId(), getProcessPoint().getProcessPointName()));
			dao.saveAll(persistentInstalledParts);
		} catch (PersistenceException e) {
			appendPersisteceErrorMessage("Data not saved for " + getProduct().getProductId());			
		}
		if (wasLastSaveSuccessful())
			markVehicleUpdated();
	}

	private void updatePartId(InstalledPart part) {
		LotControlRule currentRule = getRuleForPart(part);

		part.setPartId((currentRule == null || currentRule.getParts().isEmpty())
				? DEFAULT_PART_ID
				: currentRule.getParts().get(0).getId().getPartId());
		//Same as com.honda.galc.client.teamleader.ManualLotControlRepairController.createInstalledPart(PartResult, LotControlRule)
	}
	
	private LotControlRule getRuleForPart(InstalledPart part) {
		for (LotControlRule eachRule : getScanProductIdForm().getRules()) {
			if(eachRule.getPartNameString().equals(part.getPartName()))
				return eachRule;
		}
		//this should never happen since <part> was built off of <getScanProductIdForm().getRules()>
		return null;  
	}

	protected void markInstalledPartAsRemoved(InstalledPart anInstalledPart) {
		removeMeasurementData(anInstalledPart);
		anInstalledPart.setPartSerialNumber(null);
		anInstalledPart.setInstalledPartStatus(InstalledPartStatus.REMOVED);
		anInstalledPart.setActualTimestamp(null);
	}
	
	 protected void removeMeasurementData(InstalledPart anInstalledPart) {
		 for(Measurement measurement : anInstalledPart.getMeasurements()){
			measurement.setMeasurementAngle(0.0);
		  	measurement.setMeasurementValue(0.0);
		  	measurement.setPartSerialNumber(null);
		  	measurement.setMeasurementName(null);
		  	measurement.setMeasurementStringValue(null);
		  	measurement.setActualTimestamp(null);
		  	measurement.setMeasurementStatus(MeasurementStatus.REMOVED);
		 }
	 }

	protected void markVehicleUpdated() {
		getScanProductIdForm().setLastProcessedProductId(getScanProductIdForm().getProductId());
		getScanProductIdForm().setProductId("Updated");
	}

	protected boolean wasLastSaveSuccessful() {
		return getPersistenceErrorMessage() == null || getPersistenceErrorMessage().length() == 0;
	}
	
	protected boolean hasMeasurementSpecsFor(LotControlRule aRule) {
		return !getMeasurementSpecsFrom(aRule).isEmpty();
	}
}
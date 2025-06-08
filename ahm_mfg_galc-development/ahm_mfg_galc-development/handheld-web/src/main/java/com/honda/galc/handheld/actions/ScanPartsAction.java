package com.honda.galc.handheld.actions;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.*;
import com.honda.galc.common.exception.PropertyException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.MbpnProductDao;
import com.honda.galc.dao.product.MeasurementDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.PartSerialNumberScanType;
import com.honda.galc.entity.enumtype.StrategyType;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.handheld.data.BuildResultBean;
import com.honda.galc.handheld.data.HandheldConstants;
import com.honda.galc.handheld.forms.ChooseProcessForm;
import com.honda.galc.handheld.forms.ScanPartsForm;
import com.honda.galc.handheld.forms.ScanProductIdForm;
import com.honda.galc.property.SubproductPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.CommonPartUtility;
import com.honda.galc.util.LotControlPartUtil;
import com.honda.galc.util.SubproductUtil;

public class ScanPartsAction extends LotControlRuleProcessingAction{
	private ScanPartsForm scanPartsForm;
	
	public ScanPartsForm getScanPartsForm() {
		return scanPartsForm;
	}

	public void setScanPartsForm(ScanPartsForm scanPartsForm) {
		this.scanPartsForm = scanPartsForm;
	}

	public ActionForward localExecute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

		if (((ScanPartsForm)form).getCancelRequested().equals(HandheldConstants.CANCEL)) {
			((ScanPartsForm)form).setCancelRequested("");
			return mapping.findForward(HandheldConstants.CANCEL);
		}
		setProduct((BaseProduct)request.getSession().getAttribute(HandheldConstants.PRODUCT));
		setScanProductIdForm((ScanProductIdForm)request.getSession().getAttribute(HandheldConstants.SCAN_PRODUCT_ID_FORM));
		setScanPartsForm((ScanPartsForm)request.getSession().getAttribute(HandheldConstants.SCAN_PARTS_FORM));
		setProcessPoint(((ChooseProcessForm)request.getSession().getAttribute(HandheldConstants.CHOOSE_PROCESS_FORM)).getSelectedProcess());
		storeResults();
		updateSessionErrorMessages(request);
		return mapping.findForward(wasLastSaveSuccessful() ? HandheldConstants.SUCCESS : HandheldConstants.FAILURE);
    }

	protected InstalledPart getPartInstalledOnOtherProduct(LotControlRule rule, String serialNumber) {
		List<InstalledPart> results = ruleCannotUseSameSerialNumberOnOtherParts(rule)
				? findAllPartsWithSerialNumber(serialNumber, rule)
				: findAllPartsWithSerialNumberAndPartName(rule.getPartNameString(), serialNumber);
		
		return results.isEmpty()
			? null
			: results.get(0);
	}

	private List<InstalledPart> findAllPartsWithSerialNumberAndPartName(String partName, String serialNumber) {
		List<InstalledPart> result = ServiceFactory.getDao(InstalledPartDao.class).findAllByPartNameAndSerialNumber(partName, serialNumber);
		if(result.isEmpty())
			return result;
		
		if (result.get(0).getProductId().equals(getProduct().getProductId())) {
			result = new ArrayList<InstalledPart>(result);
			result.remove(0);
		}
		return result;
	}

	private List<InstalledPart> findAllPartsWithSerialNumber(String serialNumber, LotControlRule rule) {
		
		List<InstalledPart> filterResults = new ArrayList<InstalledPart>();
		
		PartSpec partSpec = findPartSpec(rule, serialNumber);
		if(partSpec == null) return filterResults;
		
		List<String> partNames = LotControlPartUtil.getPartNamesOfSamePartNumber(partSpec);
		
		if(!partNames.isEmpty()) {
			List<InstalledPart> results = getDao(InstalledPartDao.class).findAllByPartNameAndSerialNumber(partNames, serialNumber);
			
			for(InstalledPart installedPart : results) {
				if(!installedPart.getPartName().equals(rule.getPartNameString()) || !installedPart.getProductId().equals(getProduct().getProductId()));
				   filterResults.add(installedPart);
			}
			
		}
		
		return filterResults;
		
	}

	private boolean ruleCannotUseSameSerialNumberOnOtherParts(LotControlRule rule) {
		return !StringUtils.isEmpty(rule.getStrategy()) && StrategyType.DUP_PART_SN_VALIDATION == StrategyType.valueOf(rule.getStrategy());
	}

	protected String getParsedSerialNumberFromRawData(String rawData, LotControlRule rule) {
		for (PartSpec eachPartSpec : rule.getParts()) {
			String currentPartSpecParseResult = CommonPartUtility.parsePartSerialNumber(eachPartSpec, rawData);
			if (!currentPartSpecParseResult.equals(rawData))
				return currentPartSpecParseResult;
		}
		return rawData;
	}

	protected void compilePersisentResultsForRule(LotControlRule aRule, List<InstalledPart> partsToBeRemoved,
			List<InstalledPart> persistentInstalledParts) {
			InstalledPart partToBeRemoved;
			InstalledPart persistentInstalledPart = getInstalledPartFor(aRule);
				
			//Find the corresponding BuileResultBean for aRule
			BuildResultBean buildResultBean = getScanPartsBuildResultBeanFor(aRule);
			
			if (hasHTTPClientSerialNumberToRecordFor(buildResultBean)) {
				//if aRule is a Serial Number rule set the serial number
				persistentInstalledPart.setPartSerialNumber(getParsedSerialNumberFromRawData(buildResultBean.getResult(), aRule));
				if (isMBPNRule(aRule))
					updateMBPNStatus(aRule, persistentInstalledPart);
				else					
					persistentInstalledPart.setInstalledPartStatus(InstalledPartStatus.OK);
				logInfo(String.format("Processing scan: %1s for part: %2s on process: %3s - %4s", buildResultBean.getResult(), aRule.getPartNameString(), getProcessPoint().getProcessPointId(), getProcessPoint().getProcessPointName()));
				//if we are stealing the part from another product then
				//get ready to remove it from the original owner
				if(aRule.isUnique()) {
					partToBeRemoved = getPartInstalledOnOtherProduct(aRule, persistentInstalledPart.getPartSerialNumber());
					if (partToBeRemoved != null) {
						if (isInstalledPartProductInInstalledPartUpdateDivision(partToBeRemoved)) {
							partsToBeRemoved.add(partToBeRemoved);
							appendPersisteceErrorMessage(MessageFormat.format("Removing {0} from {1}", partToBeRemoved.getPartSerialNumber(), partToBeRemoved.getProductId()));
						} else {
							String message = MessageFormat.format("{0} already used in shipped product: {1}.", partToBeRemoved.getPartSerialNumber(), partToBeRemoved.getProductId());
							logInfo(message);
							throw new TaskException(message);
						}
					}
				} 
			}
				
			boolean shouldStoreMeasurements = shouldStoreMeasurementsForRuleBaseOnResult(buildResultBean, aRule);
			
			//Get all MeasurementSpecs for eachRule assuming that only one PartSpec has the MeasurementSpec
			if (shouldStoreMeasurements)
				addMeasurementsToPartForRule(persistentInstalledPart, aRule, true);

			if (buildResultBean.isSerialNumberRule() && buildResultBean.isTorqueDataCollector()) {
				boolean valid = isInstalledPartSNValidBasedOnRule(persistentInstalledPart, aRule);
				
				if (valid && !shouldStoreMeasurements) {
					valid = areAllMeasurementsCompleteBasedOnRule(persistentInstalledPart, aRule);
				}
				persistentInstalledPart.setInstalledPartStatus(
					valid
						? InstalledPartStatus.OK
						: InstalledPartStatus.NG);
			}
			//If there is a result or measurements, then store this item otherwise drop it
			if (hasHTTPClientSerialNumberToRecordFor(buildResultBean) || shouldStoreMeasurements)
				persistentInstalledParts.add(persistentInstalledPart);
	}

	private boolean areAllMeasurementsCompleteBasedOnRule(InstalledPart persistentInstalledPart, LotControlRule aRule) {
		List<Measurement> measurements = persistentInstalledPart.getMeasurements();
		if (measurements.isEmpty())
			measurements = ServiceFactory.getDao(MeasurementDao.class).findAllOrderBySequence(persistentInstalledPart.getProductId(),aRule.getPartNameString(), false);
		
		if (aRule.getParts().get(0).getMeasurementCount() != measurements.size())
			return false;
		
		for (Measurement eachMeasurement : measurements) {
			if (eachMeasurement.getMeasurementStatus().getId() != InstalledPartStatus.OK.getId())
				return false;
		}
		return true;
	}

	private boolean isInstalledPartSNValidBasedOnRule(InstalledPart persistentInstalledPart, LotControlRule aRule) {
		return CommonPartUtility.verify(persistentInstalledPart.getResultValue(), aRule.getParts(), PropertyService.getPartMaskWildcardFormat()) != null;
	}

	private void updateMBPNStatus(LotControlRule aRule, InstalledPart persistentInstalledPart) {
		Map<String, Boolean> mbpnStatusMessageMap = getMBPNInstalledPartStatusFor(persistentInstalledPart, aRule);
		for (String eachStatusMessage : mbpnStatusMessageMap.keySet())
			appendPersisteceErrorMessage(eachStatusMessage);

		if(mbpnStatusMessageMap.values().contains(false))
			//should not save this result so exit
			throw new TaskException("Cannot save result for MPBN: " + persistentInstalledPart.getPartSerialNumber());
		else if(!mbpnStatusMessageMap.isEmpty())
			appendPersisteceErrorMessage("MBPN attached with the above errors.");
		
		persistentInstalledPart.setInstalledPartStatus(mbpnStatusMessageMap.isEmpty()
			? InstalledPartStatus.OK
			: InstalledPartStatus.NG);
	}

	private boolean isMBPNRule(LotControlRule aRule) {
		return aRule.getPartName().getSubProductType() == null
				? false
				: aRule.getPartName().getSubProductType().equals(ProductType.MBPN.name());
	}

	/*
	 *Answer a Map with <Key>a log message <Value> a boolean indicating weather change can proceed (false means do not save)
	 */
	private Map<String, Boolean> getMBPNInstalledPartStatusFor(InstalledPart persistentInstalledPart, LotControlRule aRule) {
		HashMap<String, Boolean> testResults = new HashMap<String, Boolean>();
		MbpnProduct mbpn = ServiceFactory.getDao(MbpnProductDao.class).findByKey(persistentInstalledPart.getPartSerialNumber());
		if (mbpn == null) {
			testResults.put("MBPN: " + persistentInstalledPart.getPartSerialNumber() + " does not exist", false);
			return testResults;
		}
		
		PartSpec currentPartSpec = (aRule.getParts() != null && aRule.getParts().size() > 0) ? aRule.getParts().get(0) : null;
		if (currentPartSpec == null) {
			testResults.put("Lot Control rule setup incorret", false);
			return testResults;
		}
		
		if(!doesProductSpecCodeMatchPartSpec(mbpn, currentPartSpec)) {
			testResults.put("MBPN Spec Code " + mbpn.getCurrentProductSpecCode() + " is not valid.", true);
			return testResults;
		}
			
		SubproductPropertyBean subProductProperty;
		try {
			subProductProperty = PropertyService.getPropertyBean(SubproductPropertyBean.class, aRule.getId().getProcessPointId());
		} catch (PropertyException e) {
			testResults.put("Property setup error", false);
			return testResults;
		}
	
		SubproductUtil subproductUtil = new SubproductUtil(new PartSerialNumber(persistentInstalledPart.getPartSerialNumber()), aRule, currentPartSpec);
		for (String eachProcessPointId : subProductProperty.getInstallProcessPointMap().values())
			try {
				for(String eachTestResult : subproductUtil.performSubProductChecks(ProductType.MBPN.getProductName(), mbpn, eachProcessPointId)) {
					testResults.put(eachTestResult, true);
				}
			} catch (Exception e) {
				testResults.put("Unable to perform standard checks", false);
				return testResults;
			}
		return testResults;
	}

	private boolean doesProductSpecCodeMatchPartSpec(MbpnProduct mbpn, PartSpec currentPartSpec) {
		return currentPartSpec.getPartNumber().trim().equals(mbpn.getCurrentProductSpecCode().trim());
	}

	private boolean shouldStoreMeasurementsForRuleBaseOnResult(BuildResultBean buildResultBean, LotControlRule aRule) {
		logInfo("ScanPartsAction.shouldStoreMeasurementsForRuleBaseOnResult(BuildResultBean, LotControlRule)"
				+ " : " + (buildResultBean == null ? "NULL" : buildResultBean.toString())
				+ " : " + (aRule == null ? "NULL" : aRule.getPartNameString()));

		//Allow the possible null pointer exception that will be handled within the calling execute method. 
		return
				buildResultBean.isUpdateTorquesRequested()
				&& hasMeasurementSpecsFor(aRule);
	}

	private boolean isSerialNumberRule(LotControlRule aRule) {
		return !(aRule.getPartMasks().trim().length() == 0)
			&& (aRule.getSerialNumberScanType() == PartSerialNumberScanType.PART 
			|| aRule.getSerialNumberScanType() == PartSerialNumberScanType.PART_MASK);
	}

	protected BuildResultBean getScanPartsBuildResultBeanFor(LotControlRule aRule) {
		return getBuildResultBeanFor(aRule, getScanPartsForm().getResults());
	}

	private BuildResultBean getBuildResultBeanFor(LotControlRule aRule, List<BuildResultBean> buildResults) {
		for (BuildResultBean eachBean : buildResults) {
			if (eachBean.getRule().getPartName().getId().equals(aRule.getPartName().getId()))
				return eachBean;
		}
		return null;
	}

	private boolean hasHTTPClientSerialNumberToRecordFor(BuildResultBean buildResultBean) {
		return buildResultBean != null && buildResultBean.hasResult();
	}
	
	private PartSpec findPartSpec(LotControlRule rule, String partSN) {
		for (PartSpec partSpec : rule.getParts()) {
			if (CommonPartUtility.verification(partSN, partSpec.getPartSerialNumberMask().trim(), PropertyService.getPartMaskWildcardFormat())) {
				return partSpec;
			}
		}
		
		return null;
	}
	
	private boolean passesMaskValidation(LotControlRule rule, String value) {
		for (PartSpec partSpec : rule.getParts()) {
			if (CommonPartUtility.verification(value, partSpec.getPartSerialNumberMask().trim(), PropertyService.getPartMaskWildcardFormat())) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected String formName() {
		return HandheldConstants.SCAN_PARTS_FORM;
	}
}
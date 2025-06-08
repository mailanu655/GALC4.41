package com.honda.galc.handheld.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections.map.HashedMap;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.dao.product.BuildAttributeDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.MbpnProductDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.datacollection.LotControlRuleCache;
import com.honda.galc.device.dataformat.DataCollectionComplete;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.BaseProductSpec;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.handheld.data.HandheldConstants;
import com.honda.galc.handheld.forms.MBPNOnForm;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.on.MbpnProductOnService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.StringUtil;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.TagNames;

public class MBPNOnAction extends ValidatedUserHandheldAction {
	@Override
	ActionForward localExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		MBPNOnForm currentForm = (MBPNOnForm)form; 
		currentForm.setInvalidParentProductId("");
		request.getSession().setAttribute(HandheldConstants.MBPN_ON_FORM, currentForm);

		if (currentForm.getCancelRequested().equals(HandheldConstants.CANCEL)) {
			currentForm.clearUserInput();
			return mapping.findForward(HandheldConstants.CANCEL);
		}

		if (currentForm.isSpecCodeFromProductId()) {
			currentForm.setClearMBPNProductSpecCode(false);
			String statusString = setupMbpnProductIdMasks(mapping, currentForm);
			if (statusString != null)
				return mapping.findForward(statusString);
		} else {
			if (currentForm.isClearMBPNProductSpecCode()) {
				currentForm.setMbpnProductSpecCode(null);
				currentForm.setClearMBPNProductSpecCode(false);
				currentForm.setMbpnProductId(null);
			} else {
				setupMbpnProductIdMasks(mapping, currentForm);
			}
		}
		
		if (StringUtil.isNullOrEmpty(currentForm.getMbpnProductId()))
			return mapping.findForward(HandheldConstants.MORE_USER_DATA_REQUIRED);
		
		MbpnProduct existingMbpn = null;
		try {
			existingMbpn = ServiceFactory.getDao(MbpnProductDao.class).findByKey(currentForm.getMbpnProductId());
		} catch (Exception e) {
			logInfo(e.getMessage());
		}
		if (existingMbpn != null) {
			appendPersisteceErrorMessage("Duplicate MBPN: " + existingMbpn.getProductId() + " already exists");
			updateSessionErrorMessages(request);
			return mapping.findForward(HandheldConstants.FAILURE);
		}
		
		if (!currentForm.isPreparedForMBPNOnProcess()) {
			appendPersisteceErrorMessage("Unable to save MBPN due to spec issue");
			updateSessionErrorMessages(request);
			return mapping.findForward(HandheldConstants.FAILURE);
		}
		
		boolean mbpnOnStatus = runMBPNOn(currentForm, getProductOnDevice(currentForm));
		if (!mbpnOnStatus) {
			updateSessionErrorMessages(request);
			return mapping.findForward(HandheldConstants.FAILURE);
		}
		
		return mapping.findForward(HandheldConstants.SUCCESS);
	}

	private String setupMbpnProductIdMasks(ActionMapping mapping, MBPNOnForm currentForm) {
		if(currentForm.isSpecCodeFromProductId()) {
			BaseProduct parentProduct = null;
			try {
				parentProduct = ServiceFactory.getDao(FrameDao.class).findByKey(currentForm.getProductId());
				if (parentProduct == null) {
					parentProduct = ServiceFactory.getDao(MbpnProductDao.class).findByKey(currentForm.getProductId());
				}
			} catch (Exception e) { 
				logInfo(e.getMessage());
			}
			
			if (parentProduct == null) {
				updateForInvalidParentProduct(currentForm);
				return HandheldConstants.INVALID_PARENT_PRODUCT_ID;
			}
	
			String selectedMBPNPartName = currentForm.getSelectedMBPNPartName();
			LotControlRule selectedRule = getSelectedRule(parentProduct, selectedMBPNPartName);
			if (selectedRule == null ) {
				updateForInvalidParentProduct(currentForm);
				return HandheldConstants.INVALID_PARENT_PRODUCT_ID;
			}
		
			currentForm.setMbpnProductSpecCode(getPartNumberFromRule(selectedRule));
		} 
		if (!StringUtil.isNullOrEmpty(currentForm.getMbpnProductSpecCode()))
			currentForm.setMbpnProductIdMasks(getMasksForProductSpecCode(currentForm.getMbpnProductSpecCode()));
		return null;	
	}

	private void updateForInvalidParentProduct(MBPNOnForm currentForm) {
		currentForm.setInvalidParentProductId(HandheldConstants.INVALID_PARENT_PRODUCT_ID);
		currentForm.setMbpnProductSpecCode(null);
	}

	private boolean runMBPNOn(MBPNOnForm currentForm, Device selectedDevice) {
		DataContainer specs = new DefaultDataContainer();
			specs.put(DataContainerTag.PROCESS_POINT, selectedDevice.getIoProcessPointId());
			specs.put(DataContainerTag.CLIENT_ID, selectedDevice.getClientId());                                                                                                                                
			specs.put(DataContainerTag.PRODUCT_SPEC_CODE, currentForm.getMbpnProductSpecCode());                                                                                                                      
			specs.put(DataContainerTag.PRODUCT_ID, currentForm.getMbpnProductId());
			
		logInfo(String.format("Attempting to save MBPN of type %s with ID: %s with Product Spec Code: %s", currentForm.getSelectedMBPNPartName(), currentForm.getMbpnProductId(), currentForm.getMbpnProductSpecCode()));
		DataContainer result = ServiceFactory.getService(MbpnProductOnService.class).execute(specs);
		boolean success = result.get(TagNames.DATA_COLLECTION_COMPLETE.name()).equals(DataCollectionComplete.OK)
				&& ServiceFactory.getDao(MbpnProductDao.class).findByKey(currentForm.getMbpnProductId()) != null; 

		if (success) {
			logInfo(String.format("Successfully saved: %s with Product Spec Code %s", currentForm.getMbpnProductId(), currentForm.getMbpnProductSpecCode()));
			currentForm.clearUserInput();
		} else {
			logInfo(String.format("Unable to save: %s with Product Spec Code %s", currentForm.getMbpnProductId(), currentForm.getMbpnProductSpecCode()));
			appendPersisteceErrorMessage("Unable to save: " + currentForm.getMbpnProductId() );
		}
		return success;
	}

	private String getPartNumberFromRule(LotControlRule selectedRule) {
		for (PartSpec eachPartSpec : selectedRule.getParts()) {
			if (!StringUtil.isNullOrEmpty(eachPartSpec.getPartNumber()))
					return eachPartSpec.getPartNumber();
		}
		return null;
	}

	private LotControlRule getSelectedRule(BaseProduct parentProduct, String selectedMBPNPartName) {
		String[] mainNumbers = getHandheldPropertyBean().getMbpnMainNumberMap(String[].class).get(selectedMBPNPartName);
		BaseProductSpec productSpec = ProductTypeUtil.getProductSpecDao(parentProduct.getProductType()).findByProductSpecCode(parentProduct.getProductSpecCode(),parentProduct.getProductType().toString());
			
		List<LotControlRule> rules = LotControlRuleCache.getOrLoadLotControlRule(productSpec, getInstallSationForMBPNType(selectedMBPNPartName));
		for (LotControlRule eachRule : rules) {
			for (PartSpec eachPartSpec : eachRule.getParts()) {
				if (!StringUtil.isNullOrEmpty(eachPartSpec.getPartNumber())) {
					for (String eachMainNumber : mainNumbers) {
						if (eachPartSpec.getPartNumber().startsWith(eachMainNumber)) {
							return eachRule;
						}
					}
				}
			}
		}
		return null;
	}

	private String getInstallSationForMBPNType(String selectedMBPNPartName) {
		String installStation = null;
		for (Entry<String, List<String>> eachEntry : getInstallStationMap().entrySet()) {
			if (eachEntry.getValue().contains(selectedMBPNPartName)) {
				installStation = eachEntry.getKey();
				break;
			}
		}
		return installStation;
	}

	private Map<String, List<String>> getInstallStationMap() {
		List<BuildAttribute> attributes = ServiceFactory.getDao(BuildAttributeDao.class).findAllByAttribute("MBPN_CONSUMER_STATION_MAP");
		@SuppressWarnings("unchecked")
		Map<String, List<String>> installStationToMbpnOnStation = new HashedMap();
		for (BuildAttribute eachBuildAttribute : attributes) {
			for(String eachPair : eachBuildAttribute.getAttributeValue().split(",")) {
				String[] pair = eachPair.split("-");
				if (pair.length == 2) {
					String installStation = pair[1];
					List<String> currentOnStationList = installStationToMbpnOnStation.get(installStation);
					if (currentOnStationList == null) {
						currentOnStationList = new ArrayList<String>();
						installStationToMbpnOnStation.put(installStation, currentOnStationList);
					}
					currentOnStationList.add(pair[0]);
				}
			}
		}
		return installStationToMbpnOnStation;
	}
	
	private List<String> getMasksForProductSpecCode(String mbpnProductSpecCode) {
		List<String> result = new ArrayList<String>();
		BuildAttribute attribute = ServiceFactory.getDao(BuildAttributeDao.class).findById(HandheldConstants.PRODUCT_ON_MASK, mbpnProductSpecCode);
		if(attribute != null)
			result.add(attribute.getAttributeValue());
		return result;		
	}

	private Device getProductOnDevice(MBPNOnForm currentForm) {
		List<Device> devices = ServiceFactory.getDao(DeviceDao.class).findAllByProcessPointId(currentForm.getSelectedMBPNPartName());
		for (Device eachDevice : devices) {
			for (DeviceFormat eachDeviceFormat : eachDevice.getDeviceDataFormats()) {
				if (eachDeviceFormat.getTag().equals("PRODUCT_ON_DEVICE") && eachDeviceFormat.getTagValue().equalsIgnoreCase("true")) {
					return eachDevice;
				}
			}
		}
		return null;
	}

	@Override
	protected String formName() {
		return HandheldConstants.MBPN_ON_FORM;
	}
}

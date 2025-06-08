package com.honda.galc.handheld.forms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionMapping;

import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.Mbpn;
import com.honda.galc.handheld.data.HandheldConstants;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.CommonPartUtility;
import com.honda.galc.util.StringUtil;

public class MBPNOnForm extends ValidatedUserHandheldForm {
	private static final long serialVersionUID = 1L;
	private List<ProcessPoint> mbpnOnProcessPoints = null;
	private String selectedMBPNPartName, productId, mbpnProductId, mbpnProductSpecCode, cancelRequested = "", invalidParentProductId = "", mbpnProductIdMasks;
	private boolean specCodeFromProductId = true, clearMBPNProductSpecCode = false;
	private Map<String, List<Mbpn>> processPointSpecCodeMap;

	public MBPNOnForm() {
		super();
	}
	
	public String getMbpnProductSpecCode() {
		return mbpnProductSpecCode;
	}

	public void setMbpnProductSpecCode(String mbpnProductSpecCode) {
		if (mbpnProductSpecCode == null)
			this.mbpnProductSpecCode = null;
		else
			this.mbpnProductSpecCode = mbpnProductSpecCode.trim();
	}

	public List<ProcessPoint> getMbpnOnProcessPoints() {
		if (mbpnOnProcessPoints == null) {
			mbpnOnProcessPoints = new ArrayList<>();
			List<ProcessPoint> processPoints = ServiceFactory.getDao(ProcessPointDao.class).findAllByIds(new ArrayList<>(getHandheldPropertyBean().getMbpnMainNumberMap(String[].class).keySet()));
			for (ProcessPoint eachProcessPoint : processPoints) {
				mbpnOnProcessPoints.add(eachProcessPoint);
			}
		}
			
		return mbpnOnProcessPoints;
	}

	public String getSelectedMBPNPartName() {
		return selectedMBPNPartName;
	}
	
	public void setSelectedMBPNPartName(String selectedMBPNPartName) {
		this.selectedMBPNPartName = selectedMBPNPartName;
	}
	
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	public String getMbpnProductId() {
		return mbpnProductId;
	}
	
	public void setMbpnProductId(String mbpnProductId) {
		this.mbpnProductId = StringUtils.upperCase(mbpnProductId);
	}

	public String getCancelRequested() {
		return cancelRequested;
	}

	public void clearUserInput() {
		setCancelRequested("");
		setProductId(null);
		setMbpnProductSpecCode(null);
		setMbpnProductId(null);
		setSpecCodeFromProductId(true);		
	}
	
	public void setCancelRequested(String cancelRequested) {
		this.cancelRequested = cancelRequested;
	}

	public String getInvalidParentProductId() {
		return invalidParentProductId;
	}

	public void setInvalidParentProductId(String invalidParentProductId) {
		this.invalidParentProductId = invalidParentProductId;
	}
	
	public boolean isInvalidParentProductId() {
		return getInvalidParentProductId().equals(HandheldConstants.INVALID_PARENT_PRODUCT_ID);
	}

	public String getMbpnProductIdMasks() {
		return mbpnProductIdMasks;
	}

	public void setMbpnProductIdMasks(String mbpnProductIdMasks) {
		this.mbpnProductIdMasks = mbpnProductIdMasks;
	}
	
	public void setMbpnProductIdMasks(List<String> masks) {
		String maskString = "";
		for (int i=0;i< masks.size(); i++) {
			maskString += masks.get(i);
			if (i != masks.size() - 1)
				maskString += ',';
		}
		setMbpnProductIdMasks(maskString);
	}

	public boolean isSpecCodeFromProductId() {
		return specCodeFromProductId;
	}

	public void setSpecCodeFromProductId(boolean specCodeFromProductId) {
		this.specCodeFromProductId = specCodeFromProductId;
	}

	public List<Mbpn> getMbpnProductSpecCodes() {
		if (StringUtil.isNullOrEmpty(getSelectedMBPNPartName()))
			return new ArrayList<>();
		return getProcessPointSpecCodeMap().get(getSelectedMBPNPartName());
	}

	public Map<String, List<Mbpn>> getProcessPointSpecCodeMap() {
		if (processPointSpecCodeMap == null) {
			processPointSpecCodeMap = new HashMap<>();
			Map<String, String[]> mainNumberMap = getHandheldPropertyBean().getMbpnMainNumberMap(String[].class);
			@SuppressWarnings("unchecked")
			List<Mbpn> specs = (List<Mbpn>) ProductTypeUtil.getProductSpecDao(ProductTypeUtil.MBPN.toString()).findAllProductSpecCodesOnly(ProductTypeUtil.MBPN.toString());
			
			for (Mbpn eachProductSpec : specs) {
				for (Map.Entry<String, String[]> eachEntry : mainNumberMap.entrySet()) {
					List<Mbpn> currentList = processPointSpecCodeMap.get(eachEntry.getKey());
					if (currentList == null) {
						currentList = new ArrayList<>();
						processPointSpecCodeMap.put(eachEntry.getKey(), currentList);
					}
					for (String eachMainNumber : eachEntry.getValue()) {
						if (eachProductSpec.getMainNo().equals(eachMainNumber))
							currentList.add(eachProductSpec);
					}
				}
			}
		}
		return processPointSpecCodeMap;
	}

	public boolean isClearMBPNProductSpecCode() {
		return clearMBPNProductSpecCode;
	}

	public void setClearMBPNProductSpecCode(boolean clearMBPNProductSpecCode) {
		this.clearMBPNProductSpecCode = clearMBPNProductSpecCode;
	}

	public boolean isPreparedForMBPNOnProcess() {
		return
			(!(StringUtil.isNullOrEmpty(getMbpnProductSpecCode())
				|| StringUtil.isNullOrEmpty(getMbpnProductId())
				|| StringUtil.isNullOrEmpty(getMbpnProductIdMasks())))
			&& verifyMBPNProductIdAgainstMasks();
	}

	private boolean verifyMBPNProductIdAgainstMasks() {
		for (String eachMask : getMbpnProductIdMasks().split(",")) {
			if (verifyMBPNProductIdAgainstMask(eachMask))
				return true;
		}
		return false;
	}

	private boolean verifyMBPNProductIdAgainstMask(String mask) {
		return CommonPartUtility.verification(getMbpnProductId(), mask.trim(), PropertyService.getPartMaskWildcardFormat());
	}
}

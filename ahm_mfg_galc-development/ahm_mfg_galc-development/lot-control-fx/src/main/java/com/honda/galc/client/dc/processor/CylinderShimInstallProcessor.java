package com.honda.galc.client.dc.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.dc.enumtype.ShimInstallPartType;
import com.honda.galc.client.dc.mvc.DataCollectionController;
import com.honda.galc.client.dc.view.CylinderShimInstallAbstractBodyPane.InputFieldType;
import com.honda.galc.client.dc.view.CylinderShimInstallBodyPane;
import com.honda.galc.client.dc.view.CylinderShimInstallView;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.InstalledPart;

import javafx.scene.control.TextField;

/**
 * 
 * @author Wade Pei <br>
 * @date Jul 2, 2014
 */
public class CylinderShimInstallProcessor extends CylinderShimInstallAbstractProcessor<CylinderShimInstallView, CylinderShimInstallBodyPane> {

	public CylinderShimInstallProcessor(DataCollectionController controller, MCOperationRevision operation) {
		super(controller, operation);
	}
	
	/**
	 * This method returns list of parts types based on which fields will be displayed
	 */
	protected List<ShimInstallPartType> populateVisibleInstallPartTypes() {
		List<ShimInstallPartType> shimInstallPartList = new ArrayList<ShimInstallPartType>();
		switch (shimInstallPartType) {
		case BASE_SHIM_ID:
			shimInstallPartList.add(ShimInstallPartType.BASE_SHIM_ID);
			break;
		case BASE_GAP:
			shimInstallPartList.add(ShimInstallPartType.BASE_SHIM_ID);
			shimInstallPartList.add(ShimInstallPartType.BASE_GAP);
			break;
		case ACTUAL_SHIM_ID:
			shimInstallPartList.add(ShimInstallPartType.BASE_SHIM_ID);
			shimInstallPartList.add(ShimInstallPartType.BASE_GAP);
			shimInstallPartList.add(ShimInstallPartType.ACTUAL_SHIM_ID);
			break;
		case FINAL_GAP:
			shimInstallPartList.add(ShimInstallPartType.BASE_SHIM_ID);
			shimInstallPartList.add(ShimInstallPartType.BASE_GAP);
			shimInstallPartList.add(ShimInstallPartType.ACTUAL_SHIM_ID);
			shimInstallPartList.add(ShimInstallPartType.FINAL_GAP);
			break;
		}
		return shimInstallPartList;
	}
	
	protected ConcurrentHashMap<ShimInstallPartType, InstalledPart> getInstalledPartMap() {
		ConcurrentHashMap<ShimInstallPartType, InstalledPart> installedPartMap = new ConcurrentHashMap<ShimInstallPartType, InstalledPart>();
		//Getting installed part
		for(String opName: shimOps) {
			InstalledPart part = getController().getModel().getInstalledPartsMap().get(opName);
			//Adding to the map if Part Installed
			if(part != null && part.isStatusOk()) {
				installedPartMap.put(opShimInstallPartMap.get(opName), part);
			}
		}
		return installedPartMap;
	}
	
	public void loadInstalledParts() {
		//Getting installed parts
		ConcurrentHashMap<ShimInstallPartType, InstalledPart> installedPartMap = getInstalledPartMap();
		//Loading installed parts
		for(ShimInstallPartType shimInstallPartType: visibleShimInstallPartTypes) {
			if(installedPartMap.containsKey(shimInstallPartType)) {
				//Get the part serial number
				InstalledPart part = installedPartMap.get(shimInstallPartType);
				String partSN = part.getPartSerialNumber();
				//Populating corresponding fields
				switch (shimInstallPartType) {
				case BASE_SHIM_ID:
					setFieldValues(getBodyPane().getBaseShimIds(), partSN, InputFieldType.BASE_SHIM_ID);
					break;
				case BASE_GAP:
					String[] partSNs = StringUtils.split(partSN, Delimiter.COLON);
					if(partSNs!=null && partSNs.length == 2) {
						setFieldValues(getBodyPane().getBaseGaps(), partSNs[0], InputFieldType.BASE_GAP);
						setFieldValues(getBodyPane().getNeedShimIds(), partSNs[1], InputFieldType.NEED_SHIM_ID);
					}
					break;
				case ACTUAL_SHIM_ID:
					setFieldValues(getBodyPane().getActShimIds(), partSN, InputFieldType.ACT_SHIM_ID);
					break;
				case FINAL_GAP:
					setFieldValues(getBodyPane().getFinalGaps(), partSN, InputFieldType.FINAL_GAP);
					break;
				default:
					break;
				}
			}
		}
		//Setting Field properties and Default Data
		prepareFieldsAndProperties(installedPartMap);
	}
	
	protected void prepareFieldsAndProperties(ConcurrentHashMap<ShimInstallPartType, InstalledPart> installedPartMap) {
		switch (shimInstallPartType) {
			case BASE_SHIM_ID:
				if(installedPartMap.isEmpty()) {
					// Loading default data
					String defaultPartSN = getProperty().getDefaultBaseShimIds().get(shimType);
					if(StringUtils.isNotBlank(defaultPartSN)) {
						setFieldValues(getBodyPane().getBaseShimIds(), defaultPartSN, InputFieldType.BASE_SHIM_ID);
					}
					getBodyPane().setTextFieldState(getBodyPane().getBaseShimIds(), TextFieldState.EDIT);
				}
				//If BASE_SHIM_ID is installed - show Reject button
				if(isPartInstalled(installedPartMap, false, ShimInstallPartType.BASE_SHIM_ID)) {
					getBodyPane().setFinished(true);
				}
				break;
			case BASE_GAP:
				//If only BASE_SHIM_ID is installed - keep base gap editable / Done
				if(isPartInstalled(installedPartMap, true, ShimInstallPartType.BASE_SHIM_ID)) {
					getBodyPane().setTextFieldState(getBodyPane().getBaseGaps(), TextFieldState.EDIT);
				}
				//If BASE_SHIM_ID, BASE_GAP are installed - show Reject button
				if(isPartInstalled(installedPartMap, false, ShimInstallPartType.BASE_SHIM_ID, ShimInstallPartType.BASE_GAP)) {
					getBodyPane().setFinished(true);
				}
				break;
			case ACTUAL_SHIM_ID:
				//If only BASE_SHIM_ID, BASE_GAP are installed - keep actual shim id editable
				if(isPartInstalled(installedPartMap, true, ShimInstallPartType.BASE_SHIM_ID, ShimInstallPartType.BASE_GAP)) {
					//Populating recommended Actual Shim Ids
					String baseGapPartSN = installedPartMap.get(ShimInstallPartType.BASE_GAP).getPartSerialNumber();
					String[] baseGapPartSNs = StringUtils.split(baseGapPartSN, Delimiter.COLON);
					if(baseGapPartSNs!=null && baseGapPartSNs.length == 2) {
						String[] baseGaps = getPartValues(baseGapPartSNs[0]);
						String[] needShimIds = getPartValues(baseGapPartSNs[1]);
						String[] baseShimIds = getPartValues(installedPartMap.get(ShimInstallPartType.BASE_SHIM_ID).getPartSerialNumber());
						loadRecommendedActShim(baseShimIds, needShimIds, baseGaps);
					}
					getBodyPane().setTextFieldState(getBodyPane().getActShimIds(), TextFieldState.EDIT);
				}
				//if BASE_SHIM_ID, BASE_GAP, and ACTUAL_SHIM_ID are installed - show Reject button
				if(isPartInstalled(installedPartMap, false, ShimInstallPartType.BASE_SHIM_ID,
						ShimInstallPartType.BASE_GAP, ShimInstallPartType.ACTUAL_SHIM_ID)) {
					getBodyPane().setFinished(true);
				}
				break;
			case FINAL_GAP:
				//If only BASE_SHIM_ID, BASE_GAP, and ACTUAL_SHIM_ID are installed - editable / done
				if(isPartInstalled(installedPartMap, true, ShimInstallPartType.BASE_SHIM_ID,
						ShimInstallPartType.BASE_GAP, ShimInstallPartType.ACTUAL_SHIM_ID)) {
					getBodyPane().setTextFieldState(getBodyPane().getFinalGaps(), TextFieldState.EDIT);
				}
				//If all parts are installed - show Reject button
				if(isPartInstalled(installedPartMap, false, ShimInstallPartType.BASE_SHIM_ID, ShimInstallPartType.BASE_GAP, 
						ShimInstallPartType.ACTUAL_SHIM_ID, ShimInstallPartType.FINAL_GAP)) {
					getBodyPane().setFinished(true);
				}
				break;
			default:
				break;
		}
	
		//Set Focus
		if(!getBodyPane().isFinished()) {
			getBodyPane().requestFocusForFirstField();
		}
	}

	private void loadRecommendedActShim(String[] baseShimIds, String[] needShimIds, String[] baseGaps) {
		//Creating installed parts based on Base Gap
		if(null==baseShimIds || null==needShimIds || null==baseGaps
				|| baseShimIds.length != needShimIds.length
				|| needShimIds.length != baseGaps.length) {
			return;
		}

		double[] targetGapRange = getTargetGapRange();
		String[] recommendedActShimIds = new String[baseShimIds.length];
		int baseGap = 0;
		for(int idx=0; idx<baseShimIds.length; idx++) {
			baseGap = Integer.parseInt(baseGaps[idx]);
			if(baseGap >= targetGapRange[0] && baseGap <= targetGapRange[1]) {
				recommendedActShimIds[idx] = baseShimIds[idx];
			}
			else {
				recommendedActShimIds[idx] = needShimIds[idx];
			}
		}
		//Creating Part SN for actual shims
		String recommendedActShimPartSN = StringUtils.join(recommendedActShimIds,',');
		//Loading recommended Actual Shims
		setFieldValues(getBodyPane().getActShimIds(), recommendedActShimPartSN, InputFieldType.ACT_SHIM_ID);
	}
	
	/*
	 * This method returns true if installed parts are exactly equal to shim install part types
	 */
	protected boolean isPartInstalled(ConcurrentHashMap<ShimInstallPartType, InstalledPart> installedPartMap, boolean checkOnlyUnitparts,  ShimInstallPartType... shimInstallPartTypes) {
		if(shimInstallPartTypes!=null) {
			if(installedPartMap.isEmpty()) {
				return false;
			}
			if(checkOnlyUnitparts && shimInstallPartTypes.length != installedPartMap.size()) {
				return false;
			}
			for(ShimInstallPartType shimInstallPartType : shimInstallPartTypes) {
				if(!installedPartMap.containsKey(shimInstallPartType)) {
					return false;
				}
			}
		}
		return true;
	}
	
	@Override
	protected InstalledPart prepareInstalledPart(InstalledPartStatus status) {
		String partSerial = null;
		switch (shimInstallPartType) {
			case BASE_SHIM_ID:
				partSerial = getPartSerial(getBodyPane().getBaseShimIds());
				break;
			case ACTUAL_SHIM_ID:
				partSerial = getPartSerial(getBodyPane().getActShimIds());
				break;
			case BASE_GAP:
				partSerial = getPartSerial(getBodyPane().getBaseGaps(), getBodyPane().getNeedShimIds());
				break;
			case FINAL_GAP:
				partSerial = getPartSerial(getBodyPane().getFinalGaps());
				break;
			default:
				break;
		}
		return createInstalledPart(partSerial, status);
	}
	
	private String getPartSerial(TextField[][]... fieldsArray) {
		List<String> partSerialList = new ArrayList<String>();
		if(fieldsArray!=null) {
			for(TextField[][] fields : fieldsArray) {
				String partSerial = getDelimitedPartSerial(fields);
				if(StringUtils.isNotBlank(partSerial)) {
					partSerialList.add(partSerial);
				}
			}
		}
		return (!partSerialList.isEmpty())?StringUtils.join(partSerialList, Delimiter.COLON):null;
	}

	@Override
	protected boolean isRejectionValid() {
		ConcurrentHashMap<ShimInstallPartType, InstalledPart> installedPartMap = getInstalledPartMap();
		List<ShimInstallPartType> rejectList = new ArrayList<ShimInstallPartType>();
		switch (shimInstallPartType) {
			case BASE_SHIM_ID:
				if(isPartInstalled(installedPartMap, true, ShimInstallPartType.BASE_SHIM_ID)) {
					return true;
				}
				rejectList.add(ShimInstallPartType.BASE_GAP);
				rejectList.add(ShimInstallPartType.ACTUAL_SHIM_ID);
				rejectList.add(ShimInstallPartType.FINAL_GAP);
				break;
			case BASE_GAP:
				if(isPartInstalled(installedPartMap, true, ShimInstallPartType.BASE_SHIM_ID, ShimInstallPartType.BASE_GAP)) {
					return true;
				}
				rejectList.add(ShimInstallPartType.ACTUAL_SHIM_ID);
				rejectList.add(ShimInstallPartType.FINAL_GAP);
				break;
			case ACTUAL_SHIM_ID:
				if(isPartInstalled(installedPartMap, true, ShimInstallPartType.BASE_SHIM_ID,
						ShimInstallPartType.BASE_GAP, ShimInstallPartType.ACTUAL_SHIM_ID)) {
					return true;
				}
				rejectList.add(ShimInstallPartType.FINAL_GAP);
				break;
			case FINAL_GAP:
				if(isPartInstalled(installedPartMap, true, ShimInstallPartType.BASE_SHIM_ID, ShimInstallPartType.BASE_GAP, 
						ShimInstallPartType.ACTUAL_SHIM_ID, ShimInstallPartType.FINAL_GAP)) {
					return true;
				}
				break;
			default:
				return true;
		}
		String errorMsg = "Rejection is not valid. Please contact support!";
		if(!rejectList.isEmpty()) {
			List<String> unitList = new ArrayList<String>();
			Map<ShimInstallPartType,String> ShimInstallPartOpMap = new HashMap<ShimInstallPartType, String>();
			for(String opName: shimOps)
				ShimInstallPartOpMap.put(opShimInstallPartMap.get(opName), opName);
			for(ShimInstallPartType shimInstallPartType: rejectList) {
				String opName = ShimInstallPartOpMap.get(shimInstallPartType);
				if(getController().getModel().isOperationComplete(opName)) {
					MCOperationRevision shimOpRev = getController().getModel().getOperationsMap().get(opName);
					if(shimOpRev!=null) {
						unitList.add(shimOpRev.getUnitNo());
					}
				}
			}
			errorMsg = "Please reject following unit(s) first: "+StringUtils.join(unitList, Delimiter.COMMA);
		}
		getController().displayErrorMessage(errorMsg);
		return false;
	}
}

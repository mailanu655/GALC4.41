package com.honda.galc.client.dc.processor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.audio.ClientAudioManager;
import com.honda.galc.client.dc.enumtype.DataCollectionEventType;
import com.honda.galc.client.dc.enumtype.ShimInstallPartType;
import com.honda.galc.client.dc.event.DataCollectionEvent;
import com.honda.galc.client.dc.mvc.DataCollectionController;
import com.honda.galc.client.dc.mvc.DataCollectionModel;
import com.honda.galc.client.dc.property.CylinderShimPropertyBean;
import com.honda.galc.client.dc.view.CylinderShimInstallAbstractBodyPane;
import com.honda.galc.client.dc.view.CylinderShimInstallAbstractBodyPane.InputFieldType;
import com.honda.galc.client.dc.view.CylinderShimInstallAbstractView;
import com.honda.galc.client.property.AudioPropertyBean;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.dao.conf.MCOperationMeasurementDao;
import com.honda.galc.dao.conf.MCStructureDao;
import com.honda.galc.dao.conf.PrintAttributeFormatDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.MeasurementDao;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.conf.PrintAttributeFormat;
import com.honda.galc.entity.conf.PrintAttributeFormatId;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

import javafx.application.Platform;
import javafx.scene.control.TextField;
import com.honda.galc.entity.conf.MCOperationRevision;


/**
 * 
 * @author Wade Pei <br>
 * @date Mar 27, 2015
 */
public abstract class CylinderShimInstallAbstractProcessor<V extends CylinderShimInstallAbstractView, B extends CylinderShimInstallAbstractBodyPane<V>> extends OperationProcessor {
	protected CylinderShimPropertyBean property;
	protected ClientAudioManager audioManager;
	protected String productId;
	protected String operationName;
	protected String shimType;
	protected ShimInstallPartType shimInstallPartType;
	protected List<String> shimOps;
	protected List<ShimInstallPartType> visibleShimInstallPartTypes = null;
	protected ConcurrentHashMap<String, String> opShimTypeMap = new ConcurrentHashMap<String, String>();
	protected ConcurrentHashMap<String, ShimInstallPartType> opShimInstallPartMap = new ConcurrentHashMap<String, ShimInstallPartType>();
	protected ConcurrentHashMap<String, List<String>> shimTypeOpsMap = new ConcurrentHashMap<String, List<String>>();
	protected V view;

	public CylinderShimInstallAbstractProcessor(DataCollectionController controller, MCOperationRevision operation) {
		super(controller, operation);
		setProperty(PropertyService.getPropertyBean(CylinderShimPropertyBean.class, getApplicationContext().getProcessPointId()));
		setAudioManager(new ClientAudioManager(PropertyService.getPropertyBean(AudioPropertyBean.class, getApplicationContext().getProcessPointId())));
		init();
	}

	public void init() {
		productId = getController().getModel().getProductModel().getProductId();
		operationName = getOperation().getId().getOperationName();
		//set shim type and install part type from properties
		initShimComponents();
		
	}

	private void initShimComponents() {
		populateShimComponents(ShimInstallPartType.BASE_SHIM_ID, getProperty().getBaseShimIds());
		populateShimComponents(ShimInstallPartType.BASE_GAP, getProperty().getBaseGaps());
		populateShimComponents(ShimInstallPartType.ACTUAL_SHIM_ID, getProperty().getActualShimIds());
		populateShimComponents(ShimInstallPartType.FINAL_GAP, getProperty().getFinalGaps());
		shimType = opShimTypeMap.get(operationName);
		shimInstallPartType = opShimInstallPartMap.get(operationName);
		shimOps = shimTypeOpsMap.get(shimType);
		visibleShimInstallPartTypes = populateVisibleInstallPartTypes();
	}

	private void populateShimComponents(ShimInstallPartType shimInstallPart, Map<String, String> shimUnitMap) {
		if(shimUnitMap!=null) {
			for(String shimType: shimUnitMap.keySet()) {
				//shimUnitMap = {shim type: Operation Name}
				String opNameFromCommanName = shimUnitMap.get(shimType);
				for (MCOperationRevision operation:this.getController().getModel().getOperations()) {
					if(operation.getCommonName().equals(StringUtils.trimToEmpty(shimUnitMap.get(shimType)))
							&& this.getOperation().getView().equals(operation.getView())) {
						opNameFromCommanName = operation.getId().getOperationName();
						break;
					}
				}
				opShimTypeMap.put(opNameFromCommanName, shimType);
				opShimInstallPartMap.put(opNameFromCommanName, shimInstallPart);
				//Need all units with respect to shim Type to get installed part data
				List<String> opList = shimTypeOpsMap.get(shimType);
				if(opList==null) {
					opList = new ArrayList<String>();
				}
				opList.add(shimUnitMap.get(shimType));
				shimTypeOpsMap.put(shimType, opList);
			}
		}
	}
	
	//Creating installed part with delimited part serial values
	protected InstalledPart createInstalledPart(String partSerial, InstalledPartStatus status) {
		DataCollectionModel model = getController().getModel();
		MCOperationRevision operation = getOperation();
		InstalledPart installedPart = model.getInstalledPartsMap().get(operation.getId().getOperationName());
		String partName = operation.getId().getOperationName();
		if (installedPart == null) {
			installedPart = new InstalledPart(productId, partName);
		} 
		installedPart.setOperationRevision(operation.getId().getOperationRevision());
		installedPart.setAssociateNo(model.getProductModel().getApplicationContext().getUserId());
		installedPart.setProcessPointId(model.getProductModel().getProcessPointId());
		if (partSerial != null) {
			installedPart.setPartSerialNumber(partSerial);
		} 
		installedPart.setInstalledPartStatus(status);
		
		if (operation.getSelectedPart() == null ) { 
			installedPart.setPartId("");
		} else { 
			installedPart.setPartId(operation.getSelectedPart().getId().getPartId());
			installedPart.setPartRevision(operation.getSelectedPart().getId().getPartRevision());
		}
		
		installedPart.setValidPartSerialNumber(true);
		installedPart.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
		model.getInstalledPartsMap().put(operation.getId().getOperationName(), installedPart);
		return installedPart;
	}

	public void saveData() {
		//Preparing and saving installed part in Model
		prepareInstalledPart(InstalledPartStatus.OK);
		// Call this method to save the part of the structure.
		completeOperation(true);
	}

	public void rejectData() {
		if(isRejectionValid()) {
			getBodyPane().setFinished(false);
			//Preparing and saving installed part in Model
			prepareInstalledPart(InstalledPartStatus.NG);
			//Removing part from model
			getController().getModel().getInstalledPartsMap().remove(operationName);
			DataCollectionEvent dataCollectionEvent = new DataCollectionEvent(DataCollectionEventType.PDDA_REJECT, null);
			dataCollectionEvent.setOperation(getOperation());
			EventBusUtil.publish(dataCollectionEvent);
			//Need to use Platform.runLater for current version of java 1.8,
			//otherwise screen is not refreshing properly
			Platform.runLater(new Runnable() {
			     public void run() {
			    	 getView().resetView(); 
			     }
			});
		}
	}
	
	protected abstract List<ShimInstallPartType> populateVisibleInstallPartTypes();
	
	protected abstract boolean isRejectionValid();

	protected abstract InstalledPart prepareInstalledPart(InstalledPartStatus status);
	
	public String getMainTitle() {
		String shimPartTypeTitle = StringUtils.trimToEmpty(getProperty().getShimTypeTitles().get(shimType));
		String shimInstalledPartTitle = "";
		switch(shimInstallPartType) {
		case FINAL_GAP:
			shimInstalledPartTitle = getProperty().getFinalGapTitle();
			break;
		case ACTUAL_SHIM_ID:
			shimInstalledPartTitle = getProperty().getActualShimTitle();
			break;
		case BASE_GAP:
			shimInstalledPartTitle = getProperty().getBaseGapTitle();
			break;
		case BASE_SHIM_ID:
			shimInstalledPartTitle = getProperty().getBaseShimTitle();
			break;
		}
		return shimPartTypeTitle + " " + StringUtils.trimToEmpty(shimInstalledPartTitle);
	}
	
	public String getSubTitle() {
		double[] targetGapRange = getTargetGapRange(shimType);
		if (null == targetGapRange || targetGapRange.length < 2) {
			return "";
		}
		double averageGap = (targetGapRange[0] + targetGapRange[1]) / 2;
		double fluctuateLevel = (targetGapRange[1] - targetGapRange[0]) / 2;
		return String.format("Target %.0fu +/- %.0fu (%.0fu ~ %.0fu)", averageGap, fluctuateLevel, targetGapRange[0], targetGapRange[1]);
	}

	public double[] getTargetGapRange() {
		return getTargetGapRange(shimType);
	}
	
	public double[] getTargetGapRange(String shimType) {
		double[] targetGapRange = new double[2];
		double minVal = 0.0;
		if(getProperty().getCylinderShimMeasMin().get(shimType)!=null) {
			minVal = Double.parseDouble(StringUtils.trim(getProperty().getCylinderShimMeasMin().get(shimType)));
		}
		double maxVal = 0.0;
		if(getProperty().getCylinderShimMeasMax().get(shimType)!=null) {
			maxVal = Double.parseDouble(StringUtils.trim(getProperty().getCylinderShimMeasMax().get(shimType)));
		}
		targetGapRange[0] = minVal;
		targetGapRange[1] = maxVal;
		return targetGapRange;
	}

	public int getCylinderStartNo() {
		return getCylinderStartNo(shimType);
	}
	
	public int getCylinderStartNo(String shimType) {
		return Integer.parseInt(getProperty().getCylinderStartNos().get(shimType));
	}

	public int getCylinderNum() {
		return getProperty().getCylinderNum();
	}

	public int getTargetGap() {
		return getTargetGap(shimType);
	}
	
	public int getTargetGap(String shimType) {
		double[] targetGapRange = getTargetGapRange(shimType);
		if (null == targetGapRange || targetGapRange.length < 2) {
			return -1;
		}
		double averageGap = (targetGapRange[0] + targetGapRange[1]) / 2;
		return (int) averageGap;
	}

	/**
	 * Get the permitted Base Gap range.
	 * 
	 * @param shimId
	 * @return
	 */
	public double[] getGapRangeByShimId(String shimId) {
		if (StringUtils.isBlank(shimId)) {
			return null;
		}
		double[] gapRange = new double[2];
		int shimSize = findShimSizeById(shimId);
		double[] targetGapRange = getTargetGapRange();
		Integer[] shimRange = getPrintAttributeFormatDao().findLengthRangeByFormId(getProperty().getFormIdForShimIdMapping());
		gapRange[0] = targetGapRange[0] - shimSize + shimRange[0];
		gapRange[1] = targetGapRange[1] - shimSize + shimRange[1];
		gapRange[0] = gapRange[0] < 0 ? 0 : gapRange[0];
		return gapRange;
	}

	public int getTargetGapFluctuateLevel() {
		double[] targetGapRange = getTargetGapRange();
		if (null == targetGapRange || targetGapRange.length < 2) {
			return -1;
		}
		double fluctuateLevel = (targetGapRange[1] - targetGapRange[0]) / 2;
		return (int) fluctuateLevel;
	}

	/**
	 * We reused the GAL258TBX to store the Shim-ID-Size mapping. Shim ID stored in Attribute, and Shim Size stored in
	 * Length.
	 * 
	 * @param shimId
	 * @return
	 */
	public Integer findShimSizeById(String shimId) {
		if (StringUtils.isBlank(shimId)) {
			return -1;
		}
		PrintAttributeFormatId id = new PrintAttributeFormatId();
		id.setFormId(getProperty().getFormIdForShimIdMapping());
		id.setAttribute(shimId.toUpperCase());
		PrintAttributeFormat format = getPrintAttributeFormatDao().findByKey(id);
		if (null != format) {
			return format.getLength();
		} else {
			return -1;
		}
	}

	/**
	 * Find the shim ID whose size is closed to the given shim size.
	 * 
	 * @param shimSize
	 * @return
	 */
	public String findShimIdBySize(int shimSize, int baseShim, String baseShimId) {
		if (shimSize < 0) {
			return "";
		}
		int fluctuateLevel = getTargetGapFluctuateLevel();
		int minValue = shimSize < fluctuateLevel ? 0 : shimSize - fluctuateLevel;
		int maxValue = shimSize + fluctuateLevel;
		List<PrintAttributeFormat> formats = getPrintAttributeFormatDao().findByFormIdAndLengthRange(getProperty().getFormIdForShimIdMapping(),
				minValue, maxValue);
		if (null == formats || formats.isEmpty()) {
			return "";
		}
		if (formats.size() == 1) {
			return formats.get(0).getAttribute();
		}
		PrintAttributeFormat bestFormat = null;
		int minDiff = -1;
		int difference = 0;
		//Difference calculation for tighter gap
		for (PrintAttributeFormat format : formats) {
			if(null != format) {
				difference = Math.abs(shimSize - format.getLength());
		        if(minDiff < 0 || minDiff > difference ) {
		        	minDiff = difference;
		        	bestFormat = format;
		        }
	        }
		}
		//Checking the best format difference
		if(null != bestFormat) {
			if(Math.abs(shimSize - bestFormat.getLength()) != Math.abs(shimSize - baseShim)) {
				return bestFormat.getAttribute();
			}
		}
		//The best format difference is same as base shim id
		return baseShimId;
	}

	public abstract void loadInstalledParts();

	protected void setFieldValues(TextField[][] fields, String partSN, InputFieldType fieldType) {
		if (null == fields || null == partSN || null == fieldType) {
			return;
		}
		String[] values = getPartValues(partSN);
		if (null == values) {
			return;
		}
		for (int i = 0; i < getCylinderNum(); i++) {
			for (int j = 0; j < 2; j++) {
				fields[i][j].setText(values[i * 2 + j]);
				getBodyPane().finishedInput(fields[i][j], fieldType);
			}
		}
	}

	protected String[] getPartValues(String partSN) {
		if (StringUtils.isNotBlank(partSN)) {
			String[] values = partSN.split("\\s*,\\s*");
			if(values != null && values.length >= getCylinderNum() * 2) {
				return values;
			}
		}
		return null;
	}
	
	protected String getDelimitedPartSerial(TextField[][] fields) {
		if (null == fields) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < getCylinderNum(); i++) {
			for (int j = 0; j < 2; j++) {
				sb.append(fields[i][j].getText()).append(",");
			}
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	public MCStructureDao getMCStructureDao() {
		return ServiceFactory.getDao(MCStructureDao.class);
	}

	public MCOperationMeasurementDao getMCOperationMeasurementDao() {
		return ServiceFactory.getDao(MCOperationMeasurementDao.class);
	}

	public PrintAttributeFormatDao getPrintAttributeFormatDao() {
		return ServiceFactory.getDao(PrintAttributeFormatDao.class);
	}

	public InstalledPartDao getInstalledPartDao() {
		return ServiceFactory.getDao(InstalledPartDao.class);
	}

	public MeasurementDao getMeasurementDao() {
		return ServiceFactory.getDao(MeasurementDao.class);
	}

	public ApplicationContext getApplicationContext() {
		return this.getController().getModel().getProductModel().getApplicationContext();
	}

	public CylinderShimPropertyBean getProperty() {
		return property;
	}

	public void setProperty(CylinderShimPropertyBean property) {
		this.property = property;
	}

	public ClientAudioManager getAudioManager() {
		return audioManager;
	}

	public void setAudioManager(ClientAudioManager audioManager) {
		this.audioManager = audioManager;
	}

	public String getProductId() {
		return productId;
	}

	public V getView() {
		return view;
	}

	public void setView(V view) {
		this.view = view;
	}

	protected CylinderShimInstallAbstractBodyPane getBodyPane() {
		return getView().getBodyPane();
	}
	
	public int getFontSize() {
		return getProperty().getFontSize();
	}
	
	public String getShimType() {
		return shimType;
	}
	
	public ShimInstallPartType getShimInstallPartType() {
		return shimInstallPartType;
	}

	public List<ShimInstallPartType> getVisibleShimInstallPartTypes() {
		return visibleShimInstallPartTypes;
	}
	
}

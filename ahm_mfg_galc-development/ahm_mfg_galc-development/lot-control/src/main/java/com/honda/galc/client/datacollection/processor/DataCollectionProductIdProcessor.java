package com.honda.galc.client.datacollection.processor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.state.ProcessPart;
import com.honda.galc.client.datacollection.state.ProcessRefresh;
import com.honda.galc.client.datacollection.state.ProductBean;
import com.honda.galc.client.device.plc.DataCollectionInputData;
import com.honda.galc.client.device.plc.IPlcDataReadyEventProcessor;
import com.honda.galc.client.events.PlcDataCollectionRequest;
import com.honda.galc.client.headless.PlcDataReadyEventProcessorBase;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.dao.product.LotControlRuleDao;
import com.honda.galc.dao.product.MeasurementSpecDao;
import com.honda.galc.device.dataformat.PlcBoolean;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.enumtype.DeviceTagType;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.MeasurementSpec;
import com.honda.galc.entity.product.MeasurementSpecId;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.KeyValue;
import com.honda.galc.util.LotControlPartUtil;
import com.honda.galc.util.SortedArrayList;


public class DataCollectionProductIdProcessor extends PlcDataReadyEventProcessorBase 
implements IPlcDataReadyEventProcessor<PlcDataCollectionRequest> {

	private FrameSpecDao _frameSpecDao = null;
	private LotControlRuleDao _lotControlRuleDao = null;
	private String _terminalId;
	public static final String LOT_CONTROL_RULE_NOT_DEFINED = "LOT_CONTROL_RULE_NOT_DEFINED";
	private SortedArrayList<LotControlRule> rules = null;
	protected ProductBean _product = null;
	public static int PRODUCT_ID_LENGTH = 17;
	private MeasurementSpecDao _measurementSpecDao;
	
	public String getTerminalId() {
		return _terminalId;
	}

	public void setTerminalId(String terminalId) {
		_terminalId = terminalId;
	}

	public synchronized boolean execute(
			PlcDataCollectionRequest plcDataCollectionRequest) {
		getLogger().debug("DataCollectionProductIdProcessor : Enter execute");
		
		try {
			getLogger().debug("productid"+ plcDataCollectionRequest.getProductId());
			if(plcDataCollectionRequest.getProductId().length() == PRODUCT_ID_LENGTH){
				Frame frame = getFrameDao().findByKey(
						plcDataCollectionRequest.getProductId());
				FrameSpec frameSpec = new FrameSpec();
				frameSpec = getFrameSpecDao().findByKey(frame.getProductSpecCode());
				getProduct().setProductId(plcDataCollectionRequest.getProductId());
				getProduct().setProductSpec(frameSpec.getProductSpecCode());
				loadLotControlRules(frameSpec);
				getController().getFsm().productIdOk(getProduct());
				saveParts(plcDataCollectionRequest);
				sendRefresh(plcDataCollectionRequest.getProductId());
				getBean().put("galcDataReady", new StringBuilder("1 "),DeviceTagType.PLC_GALC_DATA_READY);
				Logger.getLogger().debug("DataCollectionProductIdProcessor : Enter execute ok");
				return true;
			} else {
				getController().getFsm().productIdNg(_product, "Invalid Product Idlength", "Invalid ProductId length");
				return false;
			}
		}catch (TaskException te) {
				te.printStackTrace();
				Logger.getLogger().error(te.getMessage());
				return false;
			}
		 catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error("Error processing in data collection process");
			return false;
		} finally {
			getBean().put("eqDataReady", new StringBuilder("0 "));
		}
	}
	
	private void sendRefresh(String productId){
		if (getController().getState() instanceof ProcessRefresh) {
			getController().received(new ProductId(getController().getState().getProductId()));
		}
	}
	
	public DataCollectionController getController() {
		return DataCollectionController.getInstance(getApplicationId().trim());
	}
	
	public ProductBean getProduct() {
		if(_product == null)
			_product = new ProductBean();
		return _product;
	}
	
	public void saveParts(PlcDataCollectionRequest plcDataCollectionRequest) {
		for (int i = 0; i < getController().getState().getLotControlRules().size(); i++) {
			if (getController().getState() instanceof ProcessPart) {
				String partName = getController().getCurrentLotControlRule().getPartName().getPartName().trim();
				DataCollectionInputData result = new DataCollectionInputData();
				getLogger().debug(partName);
				InstalledPart installedPart = new InstalledPart();
				installedPart.setProductId(plcDataCollectionRequest.getProductId());
				installedPart.setPartName( partName);
				installedPart.setPartId("A0000");
				installedPart.setValidPartSerialNumber(true);
				installedPart.setActualTimestamp(new Timestamp(System
					.currentTimeMillis()));
				Double measurementValue = getMeasurementValue(partName, plcDataCollectionRequest);
				if (measurementValue == null) {
					installedPart
						.setInstalledPartStatus(InstalledPartStatus.MISSING);
					installedPart.setPartSerialNumber("-");
					getLogger().debug("Missing part : " + partName);
				} else {
					installedPart.setInstalledPartStatus(InstalledPartStatus.OK);
					List<Measurement> measurementList = new ArrayList<Measurement>();
					Measurement measurement = new Measurement(plcDataCollectionRequest
						.getProductId(), partName, 1);
					measurement.setMeasurementValue(measurementValue);
					PlcBoolean judgement = getJudgementStatus(partName, plcDataCollectionRequest);
					if(judgement!=null){
						if (judgement.getValue()) {
							measurement.setMeasurementStatus(MeasurementStatus.OK);
						} else {
							measurement.setMeasurementStatus(MeasurementStatus.NG);
						}
					}
					
					measurement.setPartSerialNumber("-");
					measurementList.add(measurement);
					installedPart.setMeasurements(measurementList);
					installedPart.setPartSerialNumber("-");
					validateMeasurements(installedPart);
			}
			result.setInstalledPart(installedPart);
			getController().received(result);
		
			}
		}
	}
	
	private PlcBoolean getJudgementStatus(String partName, PlcDataCollectionRequest plcDataCollectionRequest){
		PlcBoolean judgement = null;
		for (KeyValue<String, PlcBoolean> kv : plcDataCollectionRequest
				.getJudgements()) {

			if ((kv.getKey().contains(partName))) {
				judgement = kv.getValue();
				break;
			}
		}
		return judgement;
	}
	
	private Double getMeasurementValue(String partName, PlcDataCollectionRequest plcDataCollectionRequest){
		Double measurementValue = null;
		for(DeviceFormat deviceFormat: getAllDeviceFormats(getApplicationId())){
			String tokens[] = deviceFormat.getTag().split("\\.");
			if ((deviceFormat.getDeviceTagType()== DeviceTagType.PLC_READ_MEASUREMENT)
					&& (tokens[2].equals(partName)) && (tokens[0].equals(plcDataCollectionRequest.getPlcDeviceId())) ){
				for (KeyValue<String, Double> kv : plcDataCollectionRequest
						.getMeasurements()) {

					if (kv.getKey().equalsIgnoreCase(partName)) {
						measurementValue = kv.getValue();
						break;
					}
				}
				break;
			}
		}
		return measurementValue;
	}
	
	private void validateMeasurements(InstalledPart part) {
		for (Measurement measurement : part.getMeasurements()) {
			MeasurementSpecId mSpecId = new MeasurementSpecId(part.getPartName(), part.getPartId(), 1);
			MeasurementSpec mSpec = getMeasurementSpecDao().findByKey(mSpecId);
			if (measurement.getMeasurementValue() > mSpec.getMaximumLimit() || measurement.getMeasurementValue() < mSpec.getMinimumLimit()) {
				measurement.setMeasurementStatus(MeasurementStatus.NG);
			} else {
				measurement.setMeasurementStatus(MeasurementStatus.OK);
			}
		}
	}
	
	private List<DeviceFormat> getAllDeviceFormats(String applicationId){
		return getDeviceFormatDao().findAllByDeviceId(applicationId);
	}
	
	private void loadLotControlRules(ProductSpec frameSpec){
		
		rules = new SortedArrayList<LotControlRule>("getSequenceNumber");
		if(!StringUtils.isBlank(getProduct().getProductSpec())) {
			rules.addAll(getLotControlRulesForProductSpec(frameSpec, getProduct()));
		}
		getController().getFsm().initLotControlRules(getProduct().getProductSpec(), rules);
		if(rules.size() == 0)
			throw new TaskException("Lot Control Rule for " + getProduct().getProductSpec() + " is not defined.", LOT_CONTROL_RULE_NOT_DEFINED);
	}
	
	protected List<LotControlRule> getLotControlRulesForProductSpec(ProductSpec spec, ProductBean product){
		if(!isNeedToReloadRuleForSubId(product)){
			return LotControlPartUtil.getLotControlRuleByProductSpec(spec, getLotControlByPPID());
		} else {
			List<LotControlRule> appliableRules = filterLotControlRules(product.getSubId());
			return LotControlPartUtil.getLotControlRuleByProductSpec(spec, appliableRules);
		}
	}
	
	private boolean isNeedToReloadRuleForSubId(ProductBean product) {
		return !StringUtils.isEmpty(product.getSubId());
	}
	
	private List<LotControlRule> filterLotControlRules(String subId) {
		List<LotControlRule> result = new ArrayList<LotControlRule>();
		for(LotControlRule rule : getLotControlByPPID()){
			if(StringUtils.isEmpty(rule.getSubId()) || subId.equals(rule.getSubId()))
				result.add(rule);
		}
		return result;
	}
	
	private List<LotControlRule> getLotControlByPPID(){
		return getLotControlRuleDao().findAllByProcessPoint(getApplicationId());
	}
	
	public FrameSpecDao getFrameSpecDao() {
		if (_frameSpecDao == null)
			_frameSpecDao = ServiceFactory.getDao(FrameSpecDao.class);
		
		return _frameSpecDao;
	}
	
	public MeasurementSpecDao getMeasurementSpecDao() {
		if (_measurementSpecDao == null) {
			_measurementSpecDao = ServiceFactory.getDao(MeasurementSpecDao.class);
		}
		return _measurementSpecDao;
	}
	
	public LotControlRuleDao getLotControlRuleDao() {
		if (_lotControlRuleDao == null)
			_lotControlRuleDao = ServiceFactory.getDao(LotControlRuleDao.class);
		
		return _lotControlRuleDao;
	}
	
	public void postPlcWrite(boolean writeSucceeded) {
	}

	public void validate() {
	}
	
}

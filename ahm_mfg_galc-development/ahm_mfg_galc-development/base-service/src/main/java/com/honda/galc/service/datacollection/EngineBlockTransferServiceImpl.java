package com.honda.galc.service.datacollection;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.InProcessProductDao;
import com.honda.galc.dao.product.MeasurementDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.InProcessProduct;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.service.EngineBlockTransferService;
import com.honda.galc.service.TrackingService;

/**
 * 
 * 
 * <h3>EngineBlockTransferServiceImpl Class description</h3>
 * <p> EngineBlockTransferServiceImpl description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * May 26, 2014
 *
 *
 */
public class EngineBlockTransferServiceImpl extends IoServiceBase implements EngineBlockTransferService{
	
	private static final String OIL_PRESSURE_SWITCH ="OIL PRESSURE SWITCH";
	private static final String BCAP_RESULT ="B-CAP_RESULT";
	private static final String AE_ON_PROCESS_POINT_ID="AE_ON_PROCESS_POINT_ID";
	private static final String DEFAULT_AEON_PPID = "AE0EN11001";
	
	@Autowired
	InProcessProductDao inProcessProductDao;
	
	@Autowired
	EngineDao engineDao;
	
	@Autowired
	MeasurementDao measurementDao;
	
	@Autowired
	TrackingService trackingService;
	
	@Override
	public DataContainer processData() {
		String ein=(String) getDevice().getInputValue(TagNames.ENGINE_SN.name());		
		boolean aFlag = false;	
		int torqueStatus = 0;
		Engine engine = null;
		try{
			engine = engineDao.findByKey(ein);
			if(engine == null) getLogger().error("Engine with EIN #: " + ein + " does not exist");
			else {
				aFlag = isExpectedEngine(engine);
				List<Measurement> measurements = measurementDao.findAll(ein, OIL_PRESSURE_SWITCH);
				torqueStatus = measurements.isEmpty()? 0 : measurements.get(0).getMeasurementStatusId();
			}
		} catch (TaskException te) {
			getLogger().error(te, "Exception when collect data for ", this.getClass().getSimpleName());
		} catch (Throwable e){
			getLogger().error(e, "Exception when collect data for ", this.getClass().getSimpleName());
		}
		getDevice().setReplyValue(BCAP_RESULT, torqueStatus);
		DataContainer dc = dataCollectionComplete(aFlag);
		
		trackProduct(engine);
		
		return dc;
	}
	
	private boolean isExpectedEngine(Engine engine) {
		if(!engine.getLastPassingProcessPointId().equals(getAEONProcessPointId())){
			getLogger().error("Engine EIN # " + engine.getProductId() + " is in incorrect process :" + engine.getLastPassingProcessPointId());
			return false;
		}
		List<InProcessProduct> items = inProcessProductDao.findByNextProductId(engine.getProductId());
		if(items.isEmpty()) {
			getLogger().warn("There is no engine before ein # " + engine.getProductId());
			return true;
		}else if(!items.get(0).getLastPassingProcessPointId().equals(getProcessPointId())){
			getLogger().error("previous engine : " + items.get(0).getProductId() + " is in process " + items.get(0).getLastPassingProcessPointId());
			return false;
		}
		return true;
	}
	
	private void trackProduct(BaseProduct product) {
		if(product!= null)trackingService.track(product, getProcessPointId());
	}
	
	private String getAEONProcessPointId() {
		return getProperty(AE_ON_PROCESS_POINT_ID,DEFAULT_AEON_PPID);
	}

}

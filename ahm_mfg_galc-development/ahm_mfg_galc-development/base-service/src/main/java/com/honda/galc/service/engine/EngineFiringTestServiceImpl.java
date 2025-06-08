package com.honda.galc.service.engine;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.EngineFiringResultDao;
import com.honda.galc.data.TagNames;
import com.honda.galc.device.dataformat.DataCollectionComplete;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.datacollection.ProductDataCollector;
import com.honda.galc.service.datacollection.work.PreProcess;
import com.honda.galc.service.datacollection.work.ProductStateCheck;

/**
 * 
 * <h3>EngineFiringTestServiceImpl</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> EngineFiringTestServiceImpl description </p>
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
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Nov 6, 2014</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Nov 6, 2014
 */
public class EngineFiringTestServiceImpl extends ProductDataCollector{

	public static final String FIRING_TEST_REQUIRED = "1";
	public static final String FIRING_TEST_NOT_REQUIRED = "2";
	public static final String FIRING_TEST_INVALID_PRODUCT_ID = "3";
	public static final String ENGINE_FIRED_RESULT = "ENGINE_FIRED_RESULT";
	public static final String FIRING_TEST_TYPE = "FIRING_TEST_TYPE";
	public static final String BENCH_NUMBER = "BENCH_NUMBER";
	
	public Device execute(Device device) {
		try {
			start = System.currentTimeMillis();	
			init(device);
			validateProduct();

			if(device.getDeviceFormat(ENGINE_FIRED_RESULT) == null)
				firingTestValidation();
			else 
				collectFireResult();
			
		} catch (TaskException te) {
			handleException(te);
		} catch (Throwable e){
			handleException(e);
		}

		getLogger().debug("context:", context.toString());

		context.prepareReply(device);
		getLogger().info("replyDeviceData:", device.toReplyString());
		getLogger().debug("total process time:" + (System.currentTimeMillis() - start) + " ms.");
		return device;
	}


	private void validateProduct() throws Exception {
		try {
			new PreProcess(context, this).execute();
			if(context.getValidProductId() == null){
				context.put(TagNames.ENGINE_FIRING_FLAG.name(), FIRING_TEST_INVALID_PRODUCT_ID);
				throw new TaskException("Invalid Product:" + context.getProductId());
			}
		} catch (Exception e) {
			context.put(TagNames.ENGINE_FIRING_FLAG.name(), FIRING_TEST_INVALID_PRODUCT_ID);
			throw e;
		}
		
	}
	

	public void firingTestValidation() throws Exception {
		getLogger().info("Start to validate engine firing test for engine:", context.getProductId());
		
		Engine engine = (Engine)context.getProduct();
		context.put(TagNames.ENGINE_FIRING_FLAG.name(), engine.getEngineFiringFlag() ==1 ? FIRING_TEST_REQUIRED : FIRING_TEST_NOT_REQUIRED);
		context.put(TagNames.DATA_COLLECTION_COMPLETE.name(), DataCollectionComplete.NG);
		if(engine.getEngineFiringFlag() == 1){
			new ProductStateCheck(context, this).execute();
		} else {
			context.put(TagNames.DATA_COLLECTION_COMPLETE.name(), DataCollectionComplete.OK);
		    getLogger().info("Firing Test not required.");
		    track();
		}
		
		
	}
	
	public void collectFireResult() {
		getLogger().info("Start to collect engine fired result for engine:", context.getProductId());
		boolean override = (Boolean)context.getTagValue(TagNames.OVERRIDE.name());
		
		ServiceFactory.getDao(EngineFiringResultDao.class).insert(
			context.getProductId(), (String)context.getTagValue(FIRING_TEST_TYPE),
			(String)context.getTagValue(TagNames.ASSOCIATE_ID.name()),
			override ? "OVERRIDE=true":null, (Boolean)context.getTagValue(ENGINE_FIRED_RESULT),
			(Integer)context.getTagValue(BENCH_NUMBER));
		
		if((Boolean)context.getTagValue(ENGINE_FIRED_RESULT))
			ServiceFactory.getDao(EngineDao.class).updateEngineFiringFlag(context.getProductId(), (short)0);
		context.put(TagNames.DATA_COLLECTION_COMPLETE.name(), DataCollectionComplete.OK);
		
		track();
	}

}

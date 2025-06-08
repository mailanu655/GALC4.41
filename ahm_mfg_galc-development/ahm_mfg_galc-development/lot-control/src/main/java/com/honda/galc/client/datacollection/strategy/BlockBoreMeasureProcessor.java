package com.honda.galc.client.datacollection.strategy;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.processor.PartSerialNumberProcessor;
import com.honda.galc.client.datacollection.property.BlockBoreMeasurePropertyBean;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.BlockBuildResultDao;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

public class BlockBoreMeasureProcessor extends PartSerialNumberProcessor {
	
	private BlockBuildResultDao blockBuildResultDao = null;
	private static final String BLOCK_BORE_MEASURE = "BLOCK_BORE_MEASURE";

	public BlockBoreMeasureProcessor(ClientContext context) {
		super(context);
	}

	@Override
	public synchronized boolean execute(PartSerialNumber partnumber) {
		Logger.getLogger().debug("BlockMeasureProcessor:: Enter confirmBoreMeasure");
		try {
			Logger.getLogger().info("Process bore:" + partnumber.getPartSn());
			confirmPartSerialNumber(partnumber);
			installedPart.setValidPartSerialNumber(true);
			getController().getFsm().partSnOk(installedPart);
			setScanCounter(0);
			Logger.getLogger().debug("BlockMeasureProcessor:: Exit confirmBoreMeasure ok");
			return true;
		} catch(TaskException te) {
			Logger.getLogger().error(te.getMessage());
			installedPart.setValidPartSerialNumber(false);
			getController().getFsm().partSnNg(installedPart, BLOCK_BORE_MEASURE, te.getMessage());
		} catch (SystemException se){
			Logger.getLogger().error(se, se.getMessage());
			installedPart.setValidPartSerialNumber(false);
			getController().getFsm().error(new Message(BLOCK_BORE_MEASURE, se.getMessage()));
		} catch (Exception e) {
			Logger.getLogger().error(e, "ThreadID = "+Thread.currentThread().getName()+" :: execute() : Exception : "+e.toString());
			getController().getFsm().error(new Message("MSG01", e.getMessage()));
		} catch (Throwable t){
			Logger.getLogger().error(t, "ThreadID = "+Thread.currentThread().getName()+" :: execute() : Exception : "+t.toString());
			getController().getFsm().error(new Message("MSG01", t.getMessage()));
		}
		setScanCounter(0);
		Logger.getLogger().debug("BlockMeasureProcessor:: Exit confirmBoreMeasure ng");
		return false;
	}
	
	@Override
	protected boolean confirmPartSerialNumber(PartSerialNumber partnumber) {
		BlockBoreMeasurePropertyBean propertyBean = PropertyService.getPropertyBean(BlockBoreMeasurePropertyBean.class, ApplicationContext.getInstance().getProcessPointId());
		checkPartSerialNumber(partnumber);
		DataCollectionState currentState = (DataCollectionState) getController().getState();
		ProductBuildResult bore = getBlockBuildResultDao().findById(
				context.getCurrentViewManager().getCurrentState(context.getProcessPointId()).getProductId(),
				propertyBean.getBoreMeasurePartName());
		String boreMeasure = partnumber.getPartSn();
		Character expected = null, input = null;
		try {
			expected = StringUtils.trimToEmpty(bore.getResultValue()).charAt(currentState.getCurrentPartIndex());
			input = StringUtils.trimToEmpty(boreMeasure).charAt(propertyBean.getBoreMeasureSnPosition());
		} catch(IndexOutOfBoundsException ex) {
			handleException("Expected or scanned value is too small to be processed.");
			return false;
		}
		
		if(
			expected == null 
			|| input == null 
			|| !expected.equals(input)
		) {
			handleException("Expected scanned value is: " + expected + " but scanned is: " + input);
			return false;
		}
		return true;
	}
	
	private BlockBuildResultDao getBlockBuildResultDao() {
		if (blockBuildResultDao == null) {
			blockBuildResultDao = ServiceFactory.getDao(BlockBuildResultDao.class);
		}
		return blockBuildResultDao;
	}
}

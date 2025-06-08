package com.honda.galc.oif.task;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.List;

import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.dao.product.HostMtocDao;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.entity.product.HostMtoc;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public class HostMtocTask extends OifAbstractTask implements IEventTaskExecutable{
	
	private static String OIF_HOST_FRAME_MTOC = "OIF_HOST_FRAME_MTOC";
	
	public HostMtocTask(String name) {
		super(name);
	}
	
	public void execute(Object[] args) {
		
		PropertyService.refreshComponentProperties(OIF_HOST_FRAME_MTOC);
		
		int batchSize = PropertyService.getPropertyInt(OIF_HOST_FRAME_MTOC, "BATCH_SIZE", 500);
		if(batchSize == 0) batchSize = 500;
		
		int sleepMilliseconds = PropertyService.getPropertyInt(OIF_HOST_FRAME_MTOC, "SLEEP_TIME", 1000);
		if(sleepMilliseconds < 10) sleepMilliseconds = 100;
		
		int count = 0;
		
		logger.info("start to update frame spec in batches. batch size is  " + batchSize);
		
		List<HostMtoc> hostMtocs = null;
		do{
			hostMtocs = getDao(HostMtocDao.class).findAll(count,batchSize);
			logger.info("Retrieved " + hostMtocs.size() + " host MTOC data");
			if(!hostMtocs.isEmpty()) {
				logger.info("Starting to load or update next batch MTOC data to FrameSpec table");
				try{
					for(HostMtoc item : hostMtocs){
						getDao(FrameSpecDao.class).save(createFrameSpec(item));
						count++;
					}
					logger.info("Processed " + count + " host MTOC data");
				}catch(Exception e) {
					logger.error(e,"Processed " + count + " host MTOC data. Exception occurs:");
					return;
				}
				
				try {
					Thread.sleep(sleepMilliseconds);
				} catch (InterruptedException e) {
					logger.error(e, " Thread sleep error");
					return;
				}
			}
		}while(!hostMtocs.isEmpty());
		
		logger.info("Totally " + count + " host MTOC data are processed");
		
		
		
	}
		
	private FrameSpec createFrameSpec(HostMtoc item) {
		FrameSpec frameSpec = new FrameSpec();
		frameSpec.setProductSpecCode(item.getId().getProductSpecCode());
		frameSpec.setModelYearCode(item.getId().getModelYearCode());
		frameSpec.setModelCode(item.getId().getModelCode());
		frameSpec.setModelTypeCode(item.getId().getModelTypeCode());
		frameSpec.setModelOptionCode(item.getId().getModelOptionCode());
		frameSpec.setExtColorCode(item.getId().getExtColorCode());
		frameSpec.setIntColorCode(item.getId().getIntColorCode());
		frameSpec.setModelYearDescription(item.getModelYearDescription());
		frameSpec.setExtColorDescription(item.getExtColorDescription());
		frameSpec.setIntColorDescription(item.getIntColorDescription());
		frameSpec.setPlantCodeGpcs(item.getId().getPlantCodeFrame());
		frameSpec.setEnginePlantCode(item.getPlantCodeEngine());
		frameSpec.setEngineMto(item.getEngineMto());
		frameSpec.setSalesModelCode(item.getSalesModelCode());
		frameSpec.setSalesModelTypeCode(item.getSalesModelTypeCode());
		frameSpec.setSalesExtColorCode(item.getSalesExtColorCode());
		frameSpec.setSalesIntColorCode(item.getSalesIntColorCode());
		frameSpec.setPrototypeCode(item.getPrototypeCode());
		frameSpec.setFrameNoPrefix(item.getFrameNoPrefix());
		frameSpec.setSeriesCode(item.getSeriesCode());
		frameSpec.setSeriesDescription(item.getSeriesDescription());
		frameSpec.setGradeCode(item.getGradeCode());
		frameSpec.setBodyAndTransTypeCode(item.getBodyAndTransTypeCode());
		frameSpec.setBodyAndTransTypeDesc(item.getBodyAndTransTypeDesc());
		return frameSpec;
	}
	
	
}

package com.honda.galc.oif.task;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.List;

import com.honda.galc.dao.product.EngineSpecDao;
import com.honda.galc.entity.product.EngineSpec;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;

public class HostMtocEngineTask extends OifAbstractTask implements IEventTaskExecutable{
	private static String OIF_HOST_ENGINE_MTOC = "OIF_HOST_ENGINE_MTOC";
	
	public HostMtocEngineTask(String name) {
		super(name);
	}

	public void execute(Object[] args) {
		
		PropertyService.refreshComponentProperties(OIF_HOST_ENGINE_MTOC);
		
		int batchSize = PropertyService.getPropertyInt(OIF_HOST_ENGINE_MTOC, "BATCH_SIZE", 500);
		if(batchSize == 0) batchSize = 500;
		
		int sleepMilliseconds = PropertyService.getPropertyInt(OIF_HOST_ENGINE_MTOC, "SLEEP_TIME", 1000);
		if(sleepMilliseconds < 10) sleepMilliseconds = 100;
		
		int count = 0;
		
		logger.info("start to update engine spec in batches. batch size is  " + batchSize);
		
		EngineSpecDao dao = getDao(EngineSpecDao.class);
		List<Object[]> hostMtoList = null;
		do{
			
			hostMtoList = dao.findEngineHostMto(count+1,count+batchSize);
			logger.info("Retrieved " + (hostMtoList == null? 0 : hostMtoList.size() + " host MTOC data"));
			if(hostMtoList != null && hostMtoList.size() >0) {
				logger.info("Starting to load or update next batch MTOC data to EngineSpec table");
				try{
					for(Object[] row : hostMtoList){
						getDao(EngineSpecDao.class).save(createEngineSpec(row));
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
		}while(hostMtoList != null && hostMtoList.size() >0);
		
		logger.info("Totally " + count + " host MTOC data are processed");
		
	}
	
	private EngineSpec createEngineSpec(Object[] row) {
		EngineSpec spec = new EngineSpec();
		
		spec.setModelYearCode((String)row[1]);
		spec.setModelCode((String)row[2]);
		spec.setModelTypeCode((String)row[3]);
		spec.setModelOptionCode((String)row[4]);
		spec.setProductSpecCode((String)row[1] + row[2] + row[3] + row[4]);
	    spec.setMissionPlantCode((String)row[13]);
	    spec.setMissionModelCode((String)row[15]);
	    spec.setMissionPrototypeCode((String)row[16]);
	    spec.setMissionModelTypeCode((String)row[18]);
	    spec.setEngineNoPrefix((String)row[6]);
	    spec.setTransmission((String)row[7]);
	    spec.setTransmissionDescription((String) row[8]);
	    spec.setGearShift((String)row[9]);
	    spec.setGearShiftDescription((String)row[10]);
	    spec.setDisplacement((String) row[11]);
	    spec.setDisplacementComment((String)row[12]);
	    spec.setEnginePrototypeCode((String)row[19]);
	    spec.setPlantCode((String)row[0]);
	    spec.setMissionModelYearCode((String) row[14]);
	    spec.setModelYearDescription((String)row[5]);
		
		return spec;
		
	}

}

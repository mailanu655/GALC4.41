package com.honda.galc.oif.task;

import java.util.List;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.oif.BomDao;
import com.honda.galc.dao.product.BuildAttributeDao;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.FrameSpec;

import com.honda.galc.service.ServiceFactory;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;

/**
 * 
 * @author Gangadhararao Gadde
 * @date May 26, 2015
 */
public class PddaPartMaskTask extends OifAbstractTask implements IEventTaskExecutable {


	public PddaPartMaskTask(String name) {
		super(name);
	}

	public void execute(Object[] args)
	{
		try{
			getLogger().info("Started processing PDDA Part Mark Interface");
			processPartMarkRecords();
			getLogger().info("Finished processing PDDA Part Mark Interface");
		}catch(Exception e)
		{
			getLogger().info("Unexpected Exception Occurred  while running the Pdda Park Mark Task :"+ e.getMessage());
			e.printStackTrace();
		}
	}

	private void processPartMarkRecords() {			
		BuildAttributeDao buildAttributeDao=ServiceFactory.getDao(BuildAttributeDao.class);
		FrameSpecDao frameSpecDao=ServiceFactory.getDao(FrameSpecDao.class);
		BomDao bomDao=ServiceFactory.getDao(BomDao.class);
		List<Object[]> bomPapiDataList=bomDao.getAllBomtbxPapidData();
		for (Object[] bomPapiData:bomPapiDataList) 
		{
			List<FrameSpec> frameSpecList=frameSpecDao.findAllByMTOCWildCard(bomPapiData[0]==null?null:bomPapiData[0].toString().trim(), bomPapiData[3]==null?null:bomPapiData[3].toString().trim(), bomPapiData[2]==null?null:bomPapiData[2].toString().trim(), bomPapiData[1]==null?null:bomPapiData[1].toString().trim(), bomPapiData[4]==null?null:bomPapiData[4].toString().trim());
			for(FrameSpec frameSpec:frameSpecList)
			{
				if(buildAttributeDao.findById(bomPapiData[5].toString().trim(), frameSpec.getProductSpecCode().trim())==null)
				{
					BuildAttribute buildAttribute=new BuildAttribute(frameSpec.getProductSpecCode(),bomPapiData[6].toString().trim(),bomPapiData[5].toString().trim());
					buildAttributeDao.save(buildAttribute);
					getLogger().info("Saved build attribute:"+buildAttribute.toString());
				}
			}
		}		
	}

	private Logger getLogger() {
		return Logger.getLogger(componentId);
	}

}
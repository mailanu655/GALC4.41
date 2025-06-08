package com.honda.galc.service.broadcast;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.qics.DefectResultDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.entity.qics.DefectResult;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;


public class MqAssembler implements IMqAssembler {
	protected Logger logger;
		
	public DataContainer execute(DataContainer dc){
		DataContainer returnDC=new DefaultDataContainer();
		logger = Logger.getLogger(dc.getString(DataContainerTag.PROCESS_POINT_ID));
		returnDC=basicExecute(dc);
		return returnDC;
	}
	
	@SuppressWarnings("unchecked")
	public DataContainer basicExecute(DataContainer dc){
		DataContainer returnDC= new DefaultDataContainer();
		List<String> tags = new ArrayList<String>();
		tags=(List<String>) dc.get(DataContainerTag.TAG_LIST);
		for(String tag: tags ){
			try{
				if(dc.get(tag)==null) throw new Exception("Null value for tag: "+tag);
					returnDC.put(tag, dc.get(tag));
				}
			catch(Exception e){
				 logger.error("Wrong Tag name "+ e);
				 return null;
			}
		}
		returnDC.put(DataContainerTag.TAG_LIST, dc.get(DataContainerTag.TAG_LIST));
		
		//RGALCDEV-851:check if there is any outstanding defect->yes->add new tag "HAS_DEFECT"="true"
		if(isCheckDefect(dc.getString(DataContainerTag.PROCESS_POINT_ID))){
			DefectResultDao defectdao=ServiceFactory.getDao(DefectResultDao.class);
			List<DefectResult> defects=defectdao.findAllByProductId(dc.getString(DataContainerTag.PRODUCT_ID));
			if(hasOutstandingDefect(defects)) returnDC.put(DataContainerTag.HAS_DEFECT,"true");
			else returnDC.put(DataContainerTag.HAS_DEFECT,"false");
		}
		return returnDC;
	}
	
	private boolean hasOutstandingDefect(List<DefectResult> defectResults) {
		int count=0;
		for(DefectResult defectResult : defectResults) {
			if(defectResult.isOutstandingStatus()){
				count++;
				break;
			}
		}
		return count>0 ? true: false;
	}
	private boolean isCheckDefect(String processPointID) {
		return PropertyService.getPropertyBoolean(processPointID,"CHECK_DEFECT",false);
	}
}

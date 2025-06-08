package com.honda.galc.service.broadcast;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.LotControlRuleDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.mq.MqInstalledPartInfo;

public class MqInstalledPart extends MqAssembler {
	public DataContainer execute(DataContainer dc){
		DataContainer returnDC=new DefaultDataContainer();
		try{
			returnDC=super.execute(dc);
			List<String> partnames=new ArrayList<String>();
			List<MqInstalledPartInfo> installpart=new ArrayList<MqInstalledPartInfo>();
			LotControlRuleDao dao = ServiceFactory.getDao(LotControlRuleDao.class);
			List<LotControlRule> lotControlRules = dao.findAllForRule(dc.getString(DataContainerTag.PROCESS_POINT_ID));
			for(LotControlRule lcr:lotControlRules){
				String part=lcr.getId().getPartName();
				partnames.add(part);
			}
			InstalledPartDao dao1 = ServiceFactory.getDao(InstalledPartDao.class);
			List<InstalledPart> installParts =dao1.findAllByProductIdAndPartNames(dc.getString(DataContainerTag.PRODUCT_ID), partnames);
						
			for(InstalledPart ip: installParts){
				MqInstalledPartInfo mbd=new MqInstalledPartInfo();
				mbd.setPartName(ip.getId().getPartName());
				mbd.setPartSerialNumber(ip.getPartSerialNumber());
				mbd.setPartStatus(ip.getInstalledPartStatusId().toString());
				mbd.setTimestamp(ip.getActualTimestamp().toString());
				installpart.add(mbd);				
			}
			returnDC.put(DataContainerTag.INSTALLED_PART,installpart);
		}catch(Exception ex){
			if(logger!= null) logger.error(ex,"Exception Occured ");
		}
		return returnDC;
	}
}

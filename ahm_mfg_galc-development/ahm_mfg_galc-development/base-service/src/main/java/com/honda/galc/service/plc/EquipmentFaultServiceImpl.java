package com.honda.galc.service.plc;

import java.util.List;

import com.honda.galc.dao.product.EquipFaultCodeDao;
import com.honda.galc.dao.product.EquipUnitFaultDao;
import com.honda.galc.dao.product.EquipmentGroupDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.entity.product.EquipFaultCode;
import com.honda.galc.entity.product.EquipUnitFault;
import com.honda.galc.entity.product.EquipmentGroup;
import com.honda.galc.service.EquipmentFaultService;
import com.honda.galc.service.datacollection.IoServiceBase;
import com.honda.galc.service.wds.WdsBufferedClient;

import static com.honda.galc.service.ServiceFactory.getDao;

/**
 * 
 * <h3>EquipmentFaultTask Class description</h3>
 * <p> EquipmentFaultTask description </p>
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
 * Dec 20, 2012
 *
 *
 */
public class EquipmentFaultServiceImpl extends IoServiceBase implements EquipmentFaultService{

	@Override
	public DataContainer processData() {

		short unitId = (short)Double.parseDouble((String)getDevice().getInputValue("UNIT_ID"));
        int faultCode = (int)Double.parseDouble((String)getDevice().getInputValue("FAULT_CODE"));
        updateFaultCode(unitId,faultCode);
        outputFaults(unitId);
		return getDevice().toReplyDataContainer(true);
	}
	
	private EquipUnitFault updateFaultCode(short unitId,int faultCode){
        EquipUnitFault unitFault= getDao(EquipUnitFaultDao.class).findByKey(unitId);
        if(unitFault == null) {
            // log configuration error
            getLogger().error("Missing Entry for \"" + unitId + "\" in Unit_Fault table" );
        }else {
            unitFault.setCurrentFaultCode(faultCode);
            getDao(EquipUnitFaultDao.class).update(unitFault);
        }
        return unitFault;
    }
	
	private void outputFaults(short unitId){
		List<EquipmentGroup> equipGroups = getDao(EquipmentGroupDao.class).findAllByUnitId(unitId);
		
		for(EquipmentGroup group : equipGroups) 
			outputFaults(group);
		
	}
	
	private void outputFaults(EquipmentGroup group){
		List<EquipFaultCode> faultCodes = getDao(EquipFaultCodeDao.class).findAllByGroupId(group.getId());
		
		WdsBufferedClient wdsClient = new WdsBufferedClient(getLogger());
		for(int i = 0 ; i< group.getFaultCount(); i++) {
            
            String name = group.getGroupName() + "\\" + "Faults\\item" + (i+1);
            if(i < faultCodes.size()){
                EquipFaultCode faultCode = faultCodes.get(i);
                wdsClient.updateValue(name, faultCode.getFaultDescription());
            }else {
                wdsClient.updateValue(name, "");
            }
        }
        
        int count = Math.min(group.getFaultCount(),faultCodes.size());
        
        wdsClient.updateValue(group.getGroupName() + "\\" + "Faults\\Count" , count);
        wdsClient.updateValue(group.getGroupName() + "\\" + "Faults\\Enable Flag" , count > 0 ? 1: 0);
        
        wdsClient.flush();
	}
	
	
	
}


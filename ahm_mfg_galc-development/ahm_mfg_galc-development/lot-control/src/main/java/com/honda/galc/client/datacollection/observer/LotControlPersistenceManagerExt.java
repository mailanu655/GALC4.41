package com.honda.galc.client.datacollection.observer;

import java.util.Vector;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.property.ProductSequencePropertyBean;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.ReflectionUtils;

/**
 * 
 * <h3>LotControlEnginePersistenceManager</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> LotControlEnginePersistenceManager implements HCM product sequence for Engine Plant </p>
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
 * @author Paul Chou
 * Apr 7, 2010
 *
 */
 /** * *
 * @version 0.2 
 * @author Gangadhararao Gadde 
 * @since Aug 09, 2012 
 */ 
public class LotControlPersistenceManagerExt extends LotControlPartLotPersistenceManager {
	ProductSequencePropertyBean prodSeqProperty;

	public LotControlPersistenceManagerExt(ClientContext context) {
		super(context);
		init();		
	}

	public void init(){
		if(prodSeqProperty==null)
			prodSeqProperty = PropertyService.getPropertyBean(ProductSequencePropertyBean.class, context.getProcessPointId());
	}

	@Override
	public IExpectedProductManager getExpectedProductManger() {
		if(expectedProductManger == null)
			expectedProductManger = createExpectedProductManager(context);
		
		return expectedProductManger;
	}
	
	@Override
	public void saveCompleteData(ProcessProduct state) {
		super.saveCompleteData(state);
		expectedProductManger.updateProductSequence(state);
	}
	
	@SuppressWarnings({ "unchecked"})
	public IExpectedProductManager createExpectedProductManager(ClientContext ct){
		String className = prodSeqProperty.getExpectedProductManager().toString();
		try {
			return (IExpectedProductManager)ReflectionUtils.createInstance(Class.forName(className), new Class<?>[] {ClientContext.class}, ct);
		} catch (Exception e) {
			Logger.getLogger().error(e, "Failed to create ExpectedProductManager class:" + className);
			throw new TaskException("Failed to create ExpectedProductManager class:" + className);
		}
	
	}
	
}

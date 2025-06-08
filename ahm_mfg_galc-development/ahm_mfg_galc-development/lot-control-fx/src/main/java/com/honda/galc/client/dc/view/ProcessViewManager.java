package com.honda.galc.client.dc.view;

import static com.honda.galc.service.ServiceFactory.getService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import com.honda.galc.client.dc.fsm.ProcessPart;
import com.honda.galc.client.dc.fsm.ProcessProduct;
import com.honda.galc.client.dc.fsm.ProcessTorque;
import com.honda.galc.client.dc.mvc.DataCollectionController;
import com.honda.galc.client.dc.property.PDDAPropertyBean;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.dto.PddaSafetyImage;
import com.honda.galc.dto.PddaUnitImage;
import com.honda.galc.entity.conf.MCOperationMeasurement;
import com.honda.galc.entity.conf.MCStructure;
import com.honda.galc.fsm.IState;
import com.honda.galc.service.BroadcastService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.pdda.GenericPddaDaoService;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>ViewManager Class description</h3>
 * <p> ViewManager description </p>
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
 * Feb 25, 2014
 *
 *
 */
public class ProcessViewManager extends ViewManager{
	
	PDDAPropertyBean property = PropertyService.getPropertyBean(PDDAPropertyBean.class);
	private String lastSpecCode = ""; 

	public ProcessViewManager(DataCollectionController dcController) {
		super(dcController);
	}
	
	@Override
	public void productIdOk(ProcessProduct state) {
		
		String ProcessPointID = getController().getModel().getProductModel().getProcessPoint().getProcessPointId();
		String ProductID = getController().getModel().getProductModel().getProductId();
		
		//here, add:1 if ModelChange, or just Login, display CCP
		//      and, if ModelChange, send a 1, not, send a 0
		//getController().getModel().getProductModel().
		
		// TODO use ProductModel.lastProduct when implemented. to check for model change.
		String currentSpecCode = getController().getModel().getProductModel().getProduct().getProductSpecCode();
		
		if (property.isShowCCP() &&  !StringUtils.isBlank(currentSpecCode) && !currentSpecCode.equals(lastSpecCode)){
			GenericPddaDaoService installedPartDao = ServiceFactory.getService(GenericPddaDaoService.class);
	    	List<PddaUnitImage> pddaimage = new ArrayList<PddaUnitImage>();//we do not have CCP data right now
	    	//List<PddaSafetyImage> pddaimage = new ArrayList<PddaSafetyImage>();//use safety image to test
	    	try {
				pddaimage = installedPartDao.getUnitCCPImages(ProductID,ProcessPointID);
	    		//pddaimage = installedPartDao.getSafetyImages(58070344);//for test
				
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			//here, add the broadcast to PLC
			//SendSuccessFlag(ProcessPointID);
		}
		
		lastSpecCode = currentSpecCode;
		
	}

	private void SendSuccessFlag(String ProcessPointID) {
		DataContainer dc = new DefaultDataContainer();
		dc.put("COMPLETE_FLG", "3");
		dc.put("USER_ID", getController().getModel().getProductModel().getApplicationContext().getUserId());
		//dc.put("APPROVER_ID", getModel().getApplicationContext().getApproveId());
		getService(BroadcastService.class).broadcast(ProcessPointID,1, dc);
	}

	
}

package com.honda.galc.client.dc.view;

import java.util.HashMap;
import java.util.Map;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import com.honda.galc.client.dc.fsm.ProcessPart;
import com.honda.galc.client.dc.fsm.ProcessProduct;
import com.honda.galc.client.dc.fsm.ProcessTorque;
import com.honda.galc.client.dc.mvc.DataCollectionController;
import com.honda.galc.entity.conf.MCOperationMeasurement;
import com.honda.galc.entity.conf.MCStructure;
import com.honda.galc.fsm.IState;

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
public class UnitViewManager extends AbstractViewManager{

	private Map<MCStructure,VBox> vboxes = new HashMap<MCStructure,VBox>();
	
	public UnitViewManager(DataCollectionController dcController) {
		super(dcController);
	}
	
	public void productIdOk(ProcessProduct state) {}

	public void completeCollectTorques(ProcessTorque state) {
		// TODO Auto-generated method stub
		
	}

	public void completePartSerialNumber(ProcessPart state) {
		// TODO Auto-generated method stub
		
	}

	public void initPartSn(ProcessPart state) {
		// TODO Auto-generated method stub
		
	}

	public void initTorque(ProcessTorque state) {
		// TODO Auto-generated method stub
		
	}

	public void message(IState<?> state) {
		// TODO Auto-generated method stub
		
	}

	public void notifyError(IState<?> state) {
		// TODO Auto-generated method stub
		
	}

	public void partSnNg(ProcessPart state) {
		// TODO Auto-generated method stub
		
	}

	public void partSnOk(ProcessPart state) {
		// TODO Auto-generated method stub
		
	}

	public void receivedPartSn(ProcessPart state) {
		// TODO Auto-generated method stub
		
	}

	public void resetScreen(IState<?> state) {
		getView().getChildren().clear();
		vboxes.clear();
	}

	public void skipCurrentInput(ProcessTorque state) {
		// TODO Auto-generated method stub
		
	}

	public void skipPart(IState<?> state) {
		// TODO Auto-generated method stub
		
	}

	public void torqueNg(ProcessTorque state) {
		// TODO Auto-generated method stub
		
	}

	public void torqueOk(ProcessTorque state) {
		// TODO Auto-generated method stub
		
	}
	

	


}

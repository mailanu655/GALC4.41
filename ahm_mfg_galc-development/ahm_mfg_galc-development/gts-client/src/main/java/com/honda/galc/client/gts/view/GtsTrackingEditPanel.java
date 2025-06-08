package com.honda.galc.client.gts.view;


import java.util.List;

import javax.swing.Action;

import org.jhotdraw.draw.GridConstrainer;
import org.jhotdraw.draw.action.ToggleGridAction;

import com.honda.galc.client.gts.view.action.CreateLabelAction;
import com.honda.galc.client.gts.view.action.CreateLaneSegmentAction;
import com.honda.galc.client.gts.view.action.CreateShapeAction;
import com.honda.galc.client.gts.view.action.EditColorMapAction;
import com.honda.galc.client.gts.view.action.EditDecisionPointAction;
import com.honda.galc.client.gts.view.action.EditLaneAction;
import com.honda.galc.client.gts.view.action.EditOutlineMapAction;
import com.honda.galc.client.gts.view.action.ShowCarrierAction;
import com.honda.galc.client.ui.DefaultWindow;

/**
 * 
 * 
 * <h3>GtsTrackingEditPanel Class description</h3>
 * <p> GtsTrackingEditPanel description </p>
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
 * Jun 8, 2015
 *
 *
 */
public class GtsTrackingEditPanel extends GtsTrackingPanel{
	
    
    private static final long serialVersionUID = 1L;

    public GtsTrackingEditPanel(DefaultWindow defaultWindow){
           super(defaultWindow);
    }
	
    
    protected void initComponents(){
        GtsDrawing.setEditingMode();
        tool = new GtsEditSelectionTool();
        initOtherData();
        super.initComponents();
        tool.setDrawingActions(getActions());
    }
        
    protected List<Action> getActions(){
        List<Action> actions = super.getActions();
        actions.add(new CreateLabelAction(drawing,tool));
        actions.add(new CreateShapeAction(drawing,tool));
        actions.add(new CreateLaneSegmentAction(drawing,tool));
        actions.add(new EditLaneAction(drawingView));
        actions.add(new EditDecisionPointAction(drawingView));
        actions.add(new EditOutlineMapAction(drawingView));
        actions.add(new EditColorMapAction(drawingView));
        actions.add(new ShowCarrierAction(drawing));
        actions.add(new ToggleGridAction(editor,new GridConstrainer(1,1),  new GridConstrainer(1,1)));
        return actions;
    }
    
    public void fitAll(){
        
    
    }

//    @Override
    protected void initOtherData() {
        
        controller.getModel().initializeDummyCarriers();
        
        // initialize Preference name for view
  //      Preference.init("EDIT_" + controller.getArea().getName());
        
    }
 
 }

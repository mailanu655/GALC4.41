package com.honda.galc.client.gts.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Timer;

import com.honda.galc.client.gts.figure.CarrierFigure;
import com.honda.galc.client.gts.property.GtsClientPropertyBean;
import com.honda.galc.client.gts.view.action.AfonInfoAction;
import com.honda.galc.client.gts.view.action.CarrierInfoAction;
import com.honda.galc.client.gts.view.action.EditColorMapAction;
import com.honda.galc.client.gts.view.action.EditOutlineMapAction;
import com.honda.galc.client.gts.view.action.IndicatorInfoAction;
import com.honda.galc.client.gts.view.action.ManualModeAction;
import com.honda.galc.client.gts.view.action.ManualModeConfirmAction;
import com.honda.galc.client.gts.view.action.MoveStatusAction;
import com.honda.galc.client.gts.view.action.PaintOnHistoryAction;
import com.honda.galc.client.gts.view.action.PreferenceAction;
import com.honda.galc.client.gts.view.action.RefreshIndicatorsAction;
import com.honda.galc.client.gts.view.action.SearchAction;
import com.honda.galc.client.gts.view.action.WeldProductionCountAction;
import com.honda.galc.client.gts.view.action.ZoneInfoAction;
import com.honda.galc.client.ui.DefaultWindow;
import com.honda.galc.entity.enumtype.GtsCarrierDisplayType;

/**
 * 
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>GtsTrackingViewPanel</code> is a JPanel for tracking view client only.
 * This class defines the view client related behaviors such as view client
 * specific menu actions. It also subscribes the tracking server and sets up
 * the GtsDrawingView as the listener to process the notification from the 
 * tracking server. It also sets the view panel background color based on the 
 * user's authority(authorized control or not)
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TR>
 * <TD>Jeffray Huang</TD>
 * <TD>Feb 27, 2008</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Jeffray Huang
 */

public class GtsTrackingViewPanel extends GtsTrackingPanel implements ActionListener{

    
    private static final long serialVersionUID = 1L;
    
    private Timer timer;
    
    private AbstractAction refreshIndicatorsAction; 
    private ManualModeAction manualModeActon;
    private ManualModeConfirmAction manualModeConfirmActon;
    
    private static final String REFRESH_INDICATORS_ACTION = "REFRESH_INDICATORS";
    private static final String EDIT_OUTLINE_MAP_ACTION = "EDIT_OUTLINE_MAP";
    private static final String EDIT_COLOR_MAP_ACTION = "EDIT_COLOR_MAP";
    private static final String SEARCH_ACTION = "SEARCH";
    private static final String WELD_COUNT_ACTION = "WELD_COUNT";
    private static final String PAINT_ON_ACTION = "PAINT_ON_DETAIL";
    private static final String NEAR_AF_ON_ACTION = "NEAR_AF_ON";
    
    
    public GtsTrackingViewPanel(DefaultWindow window) {
        super(window);
    }
    
    protected void initController(){
//        super.initController();
//        
//        // subscribe to the tracking server 
//        controller.subscribe();
    }
    
    protected void initComponents(){
    	initOtherData();
        super.initComponents();
        
        // set the GtsDrawingView as the listener to get the notification 
        // messages from the tracking server
 //       controller.addListener(view);
  
        startClientHeartbeat();
    }
        
    /**
     * if the user is authorized to control, set the background color to light blue
     * otherwise set the background to white 
     */
    
    public void checkUserControllable(){
        super.checkUserControllable();
        if(getDrawingView().getDrawing().getModel().isUserControllable()){
        	if(!getPropertyBean().isAllowManualMode() || controller.getModel().isInManualMode()) {
        		getDrawingView().setBackground(new Color(224,250,250));
        	}else {
        		getDrawingView().setBackground(new Color(255,250,250));
        	}
        	
            if(refreshIndicatorsAction != null) refreshIndicatorsAction.setEnabled(true);
        }else{
        	 getDrawingView().setBackground(Color.white);
            if(refreshIndicatorsAction != null) refreshIndicatorsAction.setEnabled(false);
        }
        
  //      if(manualModeActon != null) manualModeActon.refreshTitle();
    }    
    
    protected List<Action> getActions(){
    	
    	List<Action> actions = super.getActions();
        
    	if(getPropertyBean().isAllowManualMode()) {
    		manualModeActon = new ManualModeAction(drawing);
    		actions.add(manualModeActon);
    		manualModeConfirmActon = new ManualModeConfirmAction(drawing);
    		actions.add(manualModeConfirmActon);
    	}
    	
    	actions.add(new CarrierInfoAction(drawing,GtsCarrierDisplayType.CARRIER,"Information Display"));
        actions.add(new CarrierInfoAction(drawing,GtsCarrierDisplayType.PRODUCT_ID,"Information Display"));
        actions.add(new CarrierInfoAction(drawing,GtsCarrierDisplayType.SHORT_PRODUCT_ID,"Information Display"));
        actions.add(new CarrierInfoAction(drawing,GtsCarrierDisplayType.PROD_LOT,"Information Display"));
        actions.add(new CarrierInfoAction(drawing,GtsCarrierDisplayType.SHORT_PROD_LOT,"Information Display"));
        actions.add(new CarrierInfoAction(drawing,GtsCarrierDisplayType.YMTO,"Information Display"));
        actions.add(new CarrierInfoAction(drawing,GtsCarrierDisplayType.YMTO_COLOR,"Information Display"));
        
        createOptionalActions(actions);
        
        actions.add(new IndicatorInfoAction(drawing));
        actions.add(new MoveStatusAction(drawing));
        actions.add(new ZoneInfoAction(drawing));
        actions.add(new PreferenceAction(getDrawingView()));
           
        if(refreshIndicatorsAction != null)
        	drawing.getModel().refreshPLCIndicators();
 
        return actions;
        
    }    
    
    private void createOptionalActions(List<Action> actions) {
    	String[] optionalActions = getPropertyBean().getOptionalUserActions();
    	for(String action : optionalActions) {
    		if(REFRESH_INDICATORS_ACTION.equalsIgnoreCase(action)) 
    			actions.add(refreshIndicatorsAction = new RefreshIndicatorsAction(drawing));
    		else if(EDIT_OUTLINE_MAP_ACTION.equalsIgnoreCase(action) && 
    				getDrawingView().getDrawing().getModel().isUserControllable())
    			actions.add(new EditOutlineMapAction(getDrawingView()));
    		else if(EDIT_COLOR_MAP_ACTION.equalsIgnoreCase(action) && 
    				getDrawingView().getDrawing().getModel().isUserControllable())
    			actions.add(new EditColorMapAction(getDrawingView()));
    		else if(SEARCH_ACTION.equalsIgnoreCase(action))
    			actions.add(new SearchAction(getDrawingView()));
    		else if(WELD_COUNT_ACTION.equalsIgnoreCase(action))
    			actions.add(new WeldProductionCountAction(drawing));
    		else if(PAINT_ON_ACTION.equalsIgnoreCase(action)) 
				actions.add(new PaintOnHistoryAction(drawing));
    		else if(NEAR_AF_ON_ACTION.equalsIgnoreCase(action)) 
				actions.add(new AfonInfoAction(drawing));
    	}
    }
    
    public void exit(){
        
        stopClientHeartbeat();
        
 //       controller.unsubscribe();
        
        
    }

    /**
     * init the production carriers, moves and indicators
     */
    
//    @Override
    protected void initOtherData() {
    	
      	controller.getModel().initializeLaneCarriers();
      	 //       controller.initProductionData();
      	        
      	        // initialize Preference name for view
      	        Preference.init("VIEW_" + controller.getModel().getArea().getTrackingArea());
      	        
      	        CarrierFigure.setDisplayType(
      	            GtsCarrierDisplayType.valueOf(Preference.getCarrierDisplayType()));
    }
    
    protected void startClientHeartbeat(){
        
        // client heart beat minmum 5000
 //       timer = new Timer(Math.max(5000,PropertyList.getInstance().getClientHeartbeat()),this);
  //      timer.start();
    }
    
    protected void stopClientHeartbeat(){
        
        if(timer != null ) {
            timer.stop();
            timer = null;
        }
    }

    public void actionPerformed(ActionEvent e) {
        
        // timer action
        
 //       controller.getRequestInvoker().sendClientHeartbeat();
        
    }
    
    private GtsClientPropertyBean getPropertyBean() {
    	return controller.getModel().getPropertyBean();
    }
    
}

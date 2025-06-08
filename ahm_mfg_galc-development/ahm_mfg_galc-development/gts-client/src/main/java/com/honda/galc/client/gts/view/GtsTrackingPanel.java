package com.honda.galc.client.gts.view;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import org.jhotdraw.app.action.Actions;
import org.jhotdraw.draw.DefaultDrawingEditor;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.gui.PlacardScrollPaneLayout;

import com.honda.galc.client.ui.ApplicationMainPanel;
import com.honda.galc.client.ui.DefaultWindow;
import com.honda.galc.client.utils.ViewUtil;
import com.honda.galc.common.message.Message;
import com.honda.galc.common.message.MessageType;

/**
 * 
 * 
 * <h3>GtsTrackingView Class description</h3>
 * <p> GtsTrackingView description </p>
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
 * May 27, 2015
 *
 *
 */
public class GtsTrackingPanel extends ApplicationMainPanel{
	
	private static final long serialVersionUID = 1L;
	
	protected GtsDrawing drawing ;
	protected GtsTrackingController controller;
	protected GtsDrawingView drawingView ;
	protected DrawingEditor editor = new DefaultDrawingEditor();
	protected GtsSelectionTool tool =  new GtsViewSelectionTool();
	protected GtsStatusPanel statusPanel;
    
	public GtsTrackingPanel(DefaultWindow window) {
		super(window);
		GtsTrackingModel model = new GtsTrackingModel(window.getApplicationContext());
		controller = new GtsTrackingController(this,model);
		model.loadAll();
		initComponents();
		mapActions();
		loadData();
		subscribe();
	}
	
	

	public GtsDrawingView getDrawingView() {
		return drawingView;
	}



	public void setDrawingView(GtsDrawingView view) {
		this.drawingView = view;
	}



	private void loadData() {
		// TODO Auto-generated method stub
		
	}

	private void mapActions() {
		// TODO Auto-generated method stub
		
	}
	
	public void subscribe() {
		controller.getModel().subscribe();
		window.getApplicationContext().getRequestDispatcher().
			registerNotifcationService("IGtsNotificationService", controller);
	}
	
	public void unsubscribe() {
		controller.getModel().unsubscribe();
	}

	protected void initComponents() {
		setLayout(new BorderLayout());
		drawing = new GtsDrawing(controller);
        drawing.initDrawing();
        drawingView = new GtsDrawingView(controller);
        drawingView.setDrawing(drawing);
        
        // set the default scale factor
        
        drawingView.setScaleFactor(Preference.isCustomizedSize()? Preference.getDrawingScaleFactor() : 1.0);
        
        JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setViewportView(drawingView.getComponent());
		
		scrollPane.setLayout(new PlacardScrollPaneLayout());
        scrollPane.setBorder(new EmptyBorder(0,0,0,0));
        
        
        add(scrollPane,BorderLayout.CENTER);
        
        statusPanel = new GtsStatusPanel(drawingView);
        statusPanel.setUserName(this.getMainWindow().getUserId());
        ViewUtil.setPreferredHeight(statusPanel, 20);
        drawingView.setStatusPanel(statusPanel);
        add(statusPanel,BorderLayout.SOUTH);
//        // status panel
//        statusPanel = getStatusPanel();
//        this.add(statusPanel,JScrollPane.LOWER_LEFT_CORNER);
//        
//        view.setStatusPanel(statusPanel);
//        
        // set the location from preference 
        if(Preference.isCustomizedSize())
        	scrollPane.getViewport().setViewPosition(Preference.getLocation());
        
        editor.add(drawingView);
        editor.setTool(tool);
	}
	
	/**
     * try to maximumly fit all the figures in this panel
     *
     */
    
    public void fitAll(){
        
        // try to fit all the figures in this panel
        
        if(!Preference.isAutoFitAll()) {
        	drawingView.fitAll();
        }
    }
    
    protected List<Action> getActions(){
        List<Action> actions = new ArrayList<Action>();
        return actions;
    } 
    
    public JMenu initializeMenu() {
        JMenu menu = new JMenu();
        menu.setName("Tracking");
        menu.setText("Tracking");
        JMenu submenu = null;
         
        try{
            for (Action a : getActions()) {
                if (a == null) {
                    if (submenu != null) submenu.addSeparator();
                    else menu.addSeparator();
                }else {
                    if (a.getValue(Actions.SUBMENU_KEY) == null) {
                        menu.add(a);
                    }else {
                        String name = (String)a.getValue(Actions.SUBMENU_KEY);
                        if(submenu == null || !submenu.getText().equals(name)){
                            submenu = new JMenu(name);
                            menu.add(submenu);  
                        }
                        submenu.add(a);
                    }    
                }
            }
        
        }catch (Exception ivjExc) {
             handleException(ivjExc);
        }
        return menu;
    }
    
    /**
     * if the user is authorized to control, set the background color to light blue
     * otherwise set the background to white 
     */
    
    public void checkUserControllable(){
        
    }
    
    public void addMessage(Message message) {
    	if(statusPanel != null) statusPanel.addMessage(message);
    }
	
    /**
     *  Handle exception display
     *  display the exception in a message dialog box
     */
    
    public void handleException(Exception e) {
        
        addMessage(new Message(MessageType.EMERGENCY,e.getMessage()));
        
        MessageWindow.showExceptionDialog(getMainWindow(), e.getMessage());

    }
	   
}

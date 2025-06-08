package com.honda.galc.client.gts.view;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.WindowConstants;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ClientMain;
import com.honda.galc.client.ui.DefaultWindow;
import com.honda.galc.entity.conf.Application;

/**
 * 
 * 
 * <h3>GtsDefaultTrackingWindow Class description</h3>
 * <p> GtsDefaultTrackingWindow description </p>
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
public class GtsDefaultTrackingWindow extends DefaultWindow implements ComponentListener{

	private static final long serialVersionUID = 1L;
	
	public GtsDefaultTrackingWindow(ApplicationContext appContext, Application application) {
		super(appContext, application,false);
	}
	
	@Override
	protected void init() {
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setResizable(true);
	    
		super.init();
        
        // add menu 
        addMenu();
  
        // add the component lisetener to handle the windows resize event
        this.addComponentListener(this);
       	
	}
	
	 private void addMenu(){
	     JMenuBar menuBar = this.getRootPane().getJMenuBar();
	     menuBar.add(getTrackingPanel().initializeMenu(),1);
	     menuBar.validate();
	 }
	
	/**
	 * all the tracking clients log the message against same process point id.
	 */
	@Override
	public String getLoggerName(){
			return getApplicationContext().getProcessPointId() + getApplicationPropertyBean().getNewLogSuffix();
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		
	}

	@Override
	public void componentResized(ComponentEvent e) {
		if(!Preference.isCustomizedSize()) getTrackingPanel().fitAll();
	}

	@Override
	public void componentShown(ComponentEvent e) {
		
	}
	
	protected GtsTrackingPanel getTrackingPanel() {
		return (GtsTrackingPanel) getPanel();
	}
	
	@Override
	public void setVisible(boolean flag) {
		getTrackingPanel().checkUserControllable();
		if(this.isVisible() == flag) return;
        
//      check if needs to maximize the window based on the property settings
        if(Preference.isWindowMaximized()){
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
		super.setVisible(flag);
	}
	
	@Override
	protected void switchUser() {
		super.switchUser();
		getTrackingPanel().checkUserControllable();
	}
	
	@Override
	public void cleanUp() {
		getTrackingPanel().unsubscribe();
	}
	
	@Override
	protected void close() {
		ClientMain.getInstance().close(this);
	}
	
	@Override
	protected void exit() {
		close();
	}

}

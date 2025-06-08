package com.honda.galc.client.glassload;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JMenuBar;
import javax.swing.WindowConstants;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ui.DefaultWindow;
import com.honda.galc.entity.conf.Application;

public class GlassLoadWindow extends DefaultWindow implements ComponentListener{
	
	private String title;
	
	public GlassLoadWindow(ApplicationContext appContext, Application application) {
		super(appContext, application,true);
	}
	
	@Override
	protected void init() {
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setResizable(true);

		this.title = getTitle();
    
		super.init();
		
        
        // add menu 
        addMenu();
  
        // add the component lisetener to handle the windows resize event
        this.addComponentListener(this);
       	
	}
	
	 private void addMenu(){
	     JMenuBar menuBar = this.getRootPane().getJMenuBar();
	     menuBar.add(getViewPanel().initializeMenu(),1);
	     menuBar.validate();
	 }
	 
	protected GlassLoadView getViewPanel() {
		return (GlassLoadView) getPanel();
	}
	
	protected void setTitle(GlassLoadController.PlcMode plcMode) {
		this.setTitle(this.title + " <" + plcMode.getModeText() + ">");
	}

	@Override
	public void componentResized(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

}

package com.honda.galc.client.schedule;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ui.DefaultWindow;
import com.honda.galc.entity.conf.Application;
/**
 * 
 * <h3>ScheduleWindow</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ScheduleWindow description </p>
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
 * <TD>P.Chou</TD>
 * <TD>Feb 6, 2013</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Feb 6, 2013
 */
public class ScheduleWindow extends DefaultWindow{
	private static final long serialVersionUID = -5547903550010281218L;
	protected JMenuItem refreshMenu;
	public ScheduleWindow(ApplicationContext appContext, Application application) {
		super(appContext, application);
		
		initialize();
	}
	
	protected void initialize() {
		addWindowListener(new WindowAdapter() {			
			public void windowOpened(WindowEvent e){				
				((ScheduleMainPanel)getPanel()).requestFocusOnProductId();						
			}
		});
	}



	@Override
	protected JMenu createSystemMenu() {
		JMenu systemMenu = super.createSystemMenu();
		getRefreshMenu().addActionListener(this);
		systemMenu.insert(getRefreshMenu(), 1);
		
		return systemMenu;
	}
	
	private JMenuItem getRefreshMenu() {
		if(refreshMenu == null)
			refreshMenu = new JMenuItem("Refresh");
		
		return refreshMenu;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		if(e.getSource() == getRefreshMenu())
			refresh();
	}
	
	private void refresh() {
		((ScheduleMainPanel)getPanel()).getController().refreshLots();
		
	}

	
	

}

package com.honda.galc.client.datacollection.view.action;

import static com.honda.galc.service.ServiceFactory.getService;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ClientMain;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.property.ViewManagerPropertyBean;
import com.honda.galc.client.ui.LoginDialog;
import com.honda.galc.dao.conf.BroadcastDestinationDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.entity.enumtype.DestinationType;
import com.honda.galc.enumtype.LoginStatus;
import com.honda.galc.service.BroadcastService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

public class BaseAFOnAction extends BaseDataCollectionAction {
	
	private static final long serialVersionUID = 1L;
	
	public static final String REFRESH_REQUEST = "cancel";
	private static final String argument = "broadcast";

	public BaseAFOnAction(ClientContext context, String name) {
		super(context, name);
	}

	protected void requestTrigger() {
		List<BroadcastDestination > broadcastDestinations = ServiceFactory.getDao(BroadcastDestinationDao.class).findAllByProcessPointId(context.getProcessPointId());
		for (BroadcastDestination broadcastDestination : broadcastDestinations) {
			if (!broadcastDestination.getDestinationType().equals(DestinationType.DEVICE_WISE)) continue;
			if(broadcastDestination.getArgument().equalsIgnoreCase(argument)){
				DataContainer dataContainer = new DefaultDataContainer();
				getService(BroadcastService.class).broadcast(context.getProcessPointId(), broadcastDestination.getSequenceNumber(), dataContainer);
			}
		}
	}
	
	protected boolean login(JFrame frame){
		LoginDialog loginDialog = new LoginDialog(frame,true, true);
		loginDialog.setLocationRelativeTo(frame);
		loginDialog.setVisible(true);
		if (loginDialog.getLoginStatus() != LoginStatus.OK) return false;
		
		ViewManagerPropertyBean bean = PropertyService.getPropertyBean(ViewManagerPropertyBean.class,ApplicationContext.getInstance().getProcessPointId());
		String authorizationGroup = bean.getAuthorizationGroup();
		if(!ClientMain.getInstance().getAccessControlManager().isAuthorized(authorizationGroup )) {
			JOptionPane.showMessageDialog(null, "Terminating application! \nYou have no access permission of default application of this terminal",
					"Error", JOptionPane.ERROR_MESSAGE);
			
			return false;
		}
		
		return true;
	}

	
}

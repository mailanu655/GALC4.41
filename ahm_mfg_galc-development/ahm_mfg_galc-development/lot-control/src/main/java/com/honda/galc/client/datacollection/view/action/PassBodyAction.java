package com.honda.galc.client.datacollection.view.action;

import static com.honda.galc.service.ServiceFactory.getService;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JOptionPane;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.dao.conf.BroadcastDestinationDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.entity.enumtype.DestinationType;
import com.honda.galc.net.Request;
import com.honda.galc.service.BroadcastService;
import com.honda.galc.service.ServiceFactory;


public class PassBodyAction extends BaseAFOnAction{

	
	private static final long serialVersionUID = 1L;
	private static final String releaseArgument = "release";

	public PassBodyAction(ClientContext context, String name) {
		super(context, name);
	}
	
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		showPassbodyDialog();
	}

	
	private void showPassbodyDialog(){
		int result = JOptionPane.showConfirmDialog(context.getFrame()," Would you like to Pass Body? ", "Confirm Pass Body", JOptionPane.OK_CANCEL_OPTION);
		
		if(result ==  JOptionPane.OK_OPTION){
			broadcast("", 1,0);
		}
		runInSeparateThread(new Request(REFRESH_REQUEST));
			
	}

	protected void broadcast(String productId, Integer unitRelease, Integer error) {
		List<BroadcastDestination > broadcastDestinations = ServiceFactory.getDao(BroadcastDestinationDao.class).findAllByProcessPointId(context.getProcessPointId());
		for (BroadcastDestination broadcastDestination : broadcastDestinations) {
			if (!broadcastDestination.getDestinationType().equals(DestinationType.DEVICE_WISE)) continue;
			if(broadcastDestination.getArgument().equalsIgnoreCase(releaseArgument)){
				DataContainer dataContainer = new DefaultDataContainer();
				dataContainer.put(DataContainerTag.UNIT_RELEASE,unitRelease);
				dataContainer.put(DataContainerTag.IS_ERROR, error);
				getService(BroadcastService.class).broadcast(context.getProcessPointId(), broadcastDestination.getSequenceNumber(), dataContainer);
			}
		}
	}
	
	
	
}

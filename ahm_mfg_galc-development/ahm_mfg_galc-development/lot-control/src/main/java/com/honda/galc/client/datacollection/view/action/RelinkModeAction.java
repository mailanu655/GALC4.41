package com.honda.galc.client.datacollection.view.action;

import java.awt.event.ActionEvent;

import javax.swing.JButton;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.ComponentStatusDao;
import com.honda.galc.entity.conf.ComponentStatus;
import com.honda.galc.entity.enumtype.OperationMode;
import com.honda.galc.net.Request;
import com.honda.galc.service.ServiceFactory;

public class RelinkModeAction extends BaseAFOnAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String RELINK_LABEL = "Relink";

	public RelinkModeAction(ClientContext context, String name) {
		super(context, name);
	}
	
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		JButton button = (JButton)e.getSource();
		if(button.getText().equalsIgnoreCase(RELINK_LABEL)){
			switchRelinkOpMode();
		}else{
			exitRelinkOpMode();
		}
	}

	private void switchRelinkOpMode() {
		ComponentStatus componentStatus = getComponentStatus();
			String operationMode = componentStatus.getStatusValue();
			if(operationMode.equalsIgnoreCase(OperationMode.AUTO_MODE.getName())){
				
				boolean confirm = MessageDialog.confirm(context.getFrame(), " Are you Sure you want to switch to Relink mode?");
				if(confirm){
					Logger.getLogger().info(" updating Operation Mode from Auto mode to Auto Relink mode");
					componentStatus.setStatusValue(OperationMode.AUTO_RELINK_MODE.getName());
					getComponentStatusDao().save(componentStatus);
					logInfo();
					runInSeparateThread(new Request(REFRESH_REQUEST));
					//send request trigger
					requestTrigger();
				}
			}else if(operationMode.equalsIgnoreCase(OperationMode.MANUAL_MODE.getName())){
				boolean confirm = MessageDialog.confirm(context.getFrame(), " Are you Sure you want to switch to Relink mode?");
				if(confirm){
					Logger.getLogger().info(" updating Operation Mode from Manual mode to Manual Relink mode");
					componentStatus.setStatusValue(OperationMode.MANUAL_RELINK_MODE.getName());
					getComponentStatusDao().save(componentStatus);
					
					logInfo();
					runInSeparateThread(new Request(REFRESH_REQUEST));
				}
			}
			
	}
	
	private void exitRelinkOpMode() {
		ComponentStatus componentStatus = getComponentStatus();
		String operationMode = componentStatus.getStatusValue();
		if(operationMode.equalsIgnoreCase(OperationMode.AUTO_RELINK_MODE.getName())){
		
			boolean confirm = MessageDialog.confirm(context.getFrame(), " Are you Sure you want to exit Relink mode?");
			if(confirm){
				Logger.getLogger().info(" updating Operation Mode from Auto mode to Auto Relink mode");
				componentStatus.setStatusValue(OperationMode.AUTO_MODE.getName());
				getComponentStatusDao().save(componentStatus);
				logInfo();
				runInSeparateThread(new Request(REFRESH_REQUEST));
				//send request trigger
				requestTrigger();
			}
		}else if(operationMode.equalsIgnoreCase(OperationMode.MANUAL_RELINK_MODE.getName())){
			boolean confirm = MessageDialog.confirm(context.getFrame(), " Are you Sure you want to exit Relink mode?");
			if(confirm){
				Logger.getLogger().info(" updating Operation Mode from Manual mode to Manual Relink mode");
				componentStatus.setStatusValue(OperationMode.MANUAL_MODE.getName());
				getComponentStatusDao().save(componentStatus);
				
				logInfo();
				runInSeparateThread(new Request(REFRESH_REQUEST));
			}
		}
	}
	
	private ComponentStatusDao getComponentStatusDao(){
		return ServiceFactory.getDao(ComponentStatusDao.class);
	}
	
	private ComponentStatus getComponentStatus(){
		return getComponentStatusDao().findByKey(context.getProcessPointId(), "OPERATION_MODE");
		
	}
	
	
}

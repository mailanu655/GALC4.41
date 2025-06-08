package com.honda.galc.client.datacollection.view.action;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;



import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.ui.ErrorMessageDialog;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.ErrorMessageDialog.ButtonOptions;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.ComponentStatusDao;
import com.honda.galc.entity.conf.ComponentStatus;
import com.honda.galc.entity.enumtype.OperationMode;
import com.honda.galc.net.Request;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

public class SwitchAFOnModeAction extends BaseAFOnAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SwitchAFOnModeAction(ClientContext context, String name) {
		super(context, name);
	}
	
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		switchOpMode();
	}

	private void switchOpMode() {
		ComponentStatus componentStatus = getComponentStatus();
			String operationMode = componentStatus.getStatusValue();
			if(operationMode.equalsIgnoreCase(OperationMode.AUTO_MODE.getName())){
				if(isSecurityEnabled() && !login(context.getFrame())){
					logInfo();
					return;
				}
				boolean confirm = MessageDialog.confirm(context.getFrame(), " Are you Sure you want to switch to Manual mode?");
				if(confirm){
					Logger.getLogger().info(" updating Operation Mode from Auto mode to Manual mode");
					componentStatus.setStatusValue(OperationMode.MANUAL_MODE.getName());
					getComponentStatusDao().save(componentStatus);
					logInfo();
					runInSeparateThread(new Request(REFRESH_REQUEST));
				}
			}else if(operationMode.equalsIgnoreCase(OperationMode.MANUAL_MODE.getName())){
				Logger.getLogger().info(" updating Operation Mode from Manual mode to Auto mode");
				componentStatus.setStatusValue(OperationMode.AUTO_MODE.getName());
				getComponentStatusDao().save(componentStatus);
				
				logInfo();
				runInSeparateThread(new Request(REFRESH_REQUEST));
				//send request trigger
				requestTrigger();
			}else{
				String message = "Processing In RELINK MODE. Please Exit Relink Mode to Switch Mode.";
				Map options = new HashMap<String, String>();
				options.put(ErrorMessageDialog.defaultRefreshLabel, "OK");
				
				final ErrorMessageDialog errorDialog = new ErrorMessageDialog(context.getFrame(), "Switch Mode In Relink", message, "#ffff00", "#000000", "",false, ButtonOptions.REFRESH_ONLY, options);
				errorDialog.setActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						errorDialog.dispose();
					}
					
				});
				errorDialog.setVisible(true);
			}
			
	}
	
	private ComponentStatusDao getComponentStatusDao(){
		return ServiceFactory.getDao(ComponentStatusDao.class);
	}
	
	private ComponentStatus getComponentStatus(){
		return getComponentStatusDao().findByKey(context.getProcessPointId(), "OPERATION_MODE");
	}
	
	private boolean isSecurityEnabled(){
		return PropertyService.getPropertyBoolean(context.getProcessPointId(), "SWITCH_MODE_SECURITY_ENABLED", true);
	}
}

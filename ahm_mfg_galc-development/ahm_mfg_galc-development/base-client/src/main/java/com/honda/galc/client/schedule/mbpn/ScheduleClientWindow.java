/**
 * 
 */
package com.honda.galc.client.schedule.mbpn;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.schedule.DisplayMessageEvent;
import com.honda.galc.client.schedule.ScheduleClient3ListPanel;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.common.message.MessageType;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.service.ServiceFactory;

/**
 * @author Subu Kathiresan
 * @date Jan 17, 2013
 */
public class ScheduleClientWindow extends MainWindow {

	private static final long serialVersionUID = -3691820566619207697L;
	
	private String _lineId = "";
	private ScheduleClientController _controller;
	private ProcessPointDao _processPointDao;

	public ScheduleClientWindow(ApplicationContext appContext, Application application) {
		super(appContext, application, true);
		initialize(appContext);
	}
	
	private void initialize(ApplicationContext appcontext) {
		AnnotationProcessor.process(this);
		ScheduleClient3ListPanel listPanel = new ScheduleClient3ListPanel(applicationContext);
		_controller = new ScheduleClientController(listPanel, applicationContext);
		
		setClientPanel(listPanel);
		this.setSize(800, 600);
		this.pack();
		setInitialFocus(listPanel.getExpectedProductPanel().getProductIdTextField());
	}
	
	public void setInitialFocus(final JTextField productTxtField) {
		SwingUtilities.invokeLater(new Runnable() {
            public void run() { 
            	productTxtField.requestFocusInWindow();
            };
        });
	}
	
	@EventSubscriber(eventClass = DisplayMessageEvent.class)
	public void displayMessage(DisplayMessageEvent displayEvent) {
		if (displayEvent.getMessageType().equals(MessageType.ERROR)){
			setErrorMessage(displayEvent.getmessage());
			_controller.playNGSound();
		} else {
			setMessage(displayEvent.getmessage());
		}
	}
	
	public String getLineId() {
		if (_lineId.equals("")) {
			try {
				ProcessPoint pp = getProcessPointDao().findByKey(getApplicationContext().getProcessPointId());
				_lineId = pp.getLineId();
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		return _lineId;
	}
	
	public ProcessPointDao getProcessPointDao() {
		if (_processPointDao == null)
			_processPointDao = ServiceFactory.getDao(ProcessPointDao.class);
		
		return _processPointDao;
	}
	
	public ScheduleClientController getController() {
		return _controller;
	}
}

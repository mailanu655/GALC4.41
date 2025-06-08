package com.honda.galc.client.schedule;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.PlanStatus;
import com.honda.galc.service.ServiceFactory;
/**
 * @author Subu Kathiresan
 * @date Jan 29, 2013
 */
public class ScheduleClient3ListPanel extends JPanel {

	private static final long serialVersionUID = -7773186067413772574L;
	
	private JPanel _upperPanel;
	private JPanel _middlePanel;
	private JPanel _lowerPanel;
	private JPanel _processedOrderPanel;
	private JPanel _upcomingOrderPanel;
	private JPanel _processedProductPanel;
	private ExpectedProductPanel _expectedProductPanel;
	private ProcessPoint processPoint;
	
	private ApplicationContext _appContext;
	private ProcessPointDao _processPointDao;

	public ScheduleClient3ListPanel(ApplicationContext appContext) {
		super();
		_appContext = appContext;
		initializePanels();
	}

	private void initializePanels() {
		_processedOrderPanel = initializeProcessedOrderPanel();
		_processedProductPanel = initializeProcessedProductPanel();
		_expectedProductPanel = initializeExpectedProductPanel();
		_upcomingOrderPanel = initializeUpcomingOrderPanel();

		_upperPanel = new JPanel();
		_upperPanel.add(_processedOrderPanel);
		_upperPanel.add(_processedProductPanel);

		_middlePanel = new JPanel();
		_middlePanel.add(getExpectedProductPanel());

		_lowerPanel = new JPanel();
		_lowerPanel.add(_upcomingOrderPanel);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(_upperPanel);
		add(_middlePanel, BorderLayout.EAST);
		add(_lowerPanel);
	}

	protected ApplicationContext getAppContext() {
		return _appContext;
	}
	
	public ProcessPointDao getProcessPointDao() {
		if (_processPointDao == null)
			_processPointDao = ServiceFactory.getDao(ProcessPointDao.class);
		
		return _processPointDao;
	}
	
	private ExpectedProductPanel initializeExpectedProductPanel() {
		return new ExpectedProductPanel();
	}

	private JPanel initializeProcessedOrderPanel() {
		return new OrderPanel("Processed Orders", getProcessPoint());
	}

	private JPanel initializeUpcomingOrderPanel() {
		return new UpcomingOrderPanel("Upcoming Orders", getProcessPoint());
	}

	private JPanel initializeProcessedProductPanel() {
		return new ProductPanel("Processed Products", PlanStatus.ASSIGNED, getProcessPoint());
	}

	public ExpectedProductPanel getExpectedProductPanel() {
		return _expectedProductPanel;
	}
	
	public ProcessPoint getProcessPoint() {
		if(processPoint == null)
			processPoint = getProcessPointDao().findByKey(getAppContext().getProcessPointId());
		
		return processPoint;
	}
	
	
}


package com.honda.galc.client.teamleader;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.BaseTableModel;
import com.honda.galc.client.ui.component.IPopupMenu;
import com.honda.galc.client.ui.component.PopupMenuMouseAdapter;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.dao.conf.NotificationDao;
import com.honda.galc.dao.conf.NotificationProviderDao;
import com.honda.galc.dao.conf.NotificationSubscriberDao;
import com.honda.galc.entity.conf.Notification;
import com.honda.galc.entity.conf.NotificationProvider;
import com.honda.galc.entity.conf.NotificationSubscriber;
import com.honda.galc.service.ServiceFactory;
/**
 * 
 * <h3>NotificationPanel Class description</h3>
 * <p> NotificationPanel description </p>
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
 * Jun 25, 2010
 *
 *
 */
public class NotificationPanel extends TabbedPanel implements TableModelListener, ListSelectionListener{

	
	private static final long serialVersionUID = 1L;
	
	private final static String CREATE_NOTIFICATION="Create Notification";
	private final static String UPDATE_NOTIFICATION="Edit Notification";
	private final static String DELETE_NOTIFICATION="Delete Notification";
	private final static String CREATE_NOTIFICATION_PROVIDER="Create Notification Provider";
	private final static String UPDATE_NOTIFICATION_PROVIDER="Edit Notification Provider";
	private final static String DELETE_NOTIFICATION_PROVIDER="Delete Notification Provider";
	private final static String CREATE_NOTIFICATION_SUBSCRIBER="Create Notification Subscriber";
	private final static String UPDATE_NOTIFICATION_SUBSCRIBER="Edit Notification Subscriber";
	private final static String DELETE_NOTIFICATION_SUBSCRIBER="Delete Notification Subscriber";
	
	private TablePane notificationPane;
	
	private TablePane notificationProviderPane;
	
	private TablePane notificationSubscriberPane;
	
	private NotificationTableModel notificationTableModel;
	
	private NotificationProviderTableModel notificationProviderTableModel;
	
	private NotificationSubscriberTableModel notificationSubscriberTableModel;

	public NotificationPanel(){
		super("Notification", KeyEvent.VK_N);
		initComponents();
	}

	
	@Override
	public void onTabSelected() {
	
		if(isInitialized) return;
		
		updateNotificationTableModel(null);
		isInitialized = true;
		
		notificationPane.getTable().getSelectionModel().addListSelectionListener(this);
		
	}
	

	private void initComponents() {
		
		this.setLayout(new BorderLayout());
		
		notificationPane = new TablePane("Notification");
		notificationPane.setPreferredWidth(400);
		notificationProviderPane = new TablePane(" Notification Provider");
		notificationSubscriberPane = new TablePane(" Notification Subscriber");
		
		JPanel rightPanel = new JPanel(new BorderLayout());
		rightPanel.add(notificationProviderPane,BorderLayout.NORTH);
		rightPanel.add(notificationSubscriberPane,BorderLayout.CENTER);
		add(notificationPane,BorderLayout.WEST);
		add(rightPanel,BorderLayout.CENTER);
		
		
		MouseListener notificationMouseListener = createNotificationMouseListener();
		notificationPane.addMouseListener(notificationMouseListener);
		notificationPane.getTable().addMouseListener(notificationMouseListener);
		
		MouseListener notificationProviderMouseListener = createNotificationProviderMouseListener();
		
		notificationProviderPane.addMouseListener(notificationProviderMouseListener);
		notificationProviderPane.getTable().addMouseListener(notificationProviderMouseListener);
		
		MouseListener notificationSubscriberMouseListener = createNotificationSubscriberMouseListener();
		
		notificationSubscriberPane.addMouseListener(notificationSubscriberMouseListener);
		notificationSubscriberPane.getTable().addMouseListener(notificationSubscriberMouseListener);

	}

	private MouseListener createNotificationMouseListener(){
		return new PopupMenuMouseAdapter(new IPopupMenu() {
			public void showPopupMenu(MouseEvent e) {
				showNotificationPopupMenu(e);
			}
		 });  
	}
	
	private void showNotificationPopupMenu(MouseEvent e) {
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(createMenuItem(CREATE_NOTIFICATION,true));
		popupMenu.add(createMenuItem(UPDATE_NOTIFICATION,notificationTableModel.getSelectedItem() != null));
		popupMenu.add(createMenuItem(DELETE_NOTIFICATION,notificationTableModel.getSelectedItem() != null));
		
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
	}

	private MouseListener createNotificationProviderMouseListener(){
		return new PopupMenuMouseAdapter(new IPopupMenu() {
			public void showPopupMenu(MouseEvent e) {
				showNotificationProviderPopupMenu(e);
			}
		 });   
	}
	
	private void showNotificationProviderPopupMenu(MouseEvent e) {
		JPopupMenu popupMenu = new JPopupMenu();
		NotificationProvider selectedProvider = notificationProviderTableModel == null ? null : notificationProviderTableModel.getSelectedItem();
		popupMenu.add(createMenuItem(CREATE_NOTIFICATION_PROVIDER,notificationTableModel.getSelectedItem() != null));
		popupMenu.add(createMenuItem(UPDATE_NOTIFICATION_PROVIDER,notificationTableModel.getSelectedItem() != null && selectedProvider != null));
		popupMenu.add(createMenuItem(DELETE_NOTIFICATION_PROVIDER,selectedProvider != null));
		
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
		
	}
	
	private MouseListener createNotificationSubscriberMouseListener(){
		return new PopupMenuMouseAdapter(new IPopupMenu() {
			public void showPopupMenu(MouseEvent e) {
				showNotificationSubscriberPopupMenu(e);
			}
		 });   
	}
	
	private void showNotificationSubscriberPopupMenu(MouseEvent e) {
		JPopupMenu popupMenu = new JPopupMenu();
		NotificationSubscriber selectedSubscriber = notificationSubscriberTableModel == null ? null : notificationSubscriberTableModel.getSelectedItem();
		popupMenu.add(createMenuItem(CREATE_NOTIFICATION_SUBSCRIBER,notificationTableModel.getSelectedItem() != null));
		popupMenu.add(createMenuItem(UPDATE_NOTIFICATION_SUBSCRIBER,notificationTableModel.getSelectedItem() != null && selectedSubscriber != null));
		popupMenu.add(createMenuItem(DELETE_NOTIFICATION_SUBSCRIBER,selectedSubscriber != null));
		
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
		
	}

	public void actionPerformed(ActionEvent e) {
		JMenuItem menuItem = (JMenuItem)e.getSource();
		Exception exception = null;
		try{	
			if(menuItem != null && !StringUtils.isEmpty(menuItem.getName())) {
				logUserAction("selected menu item: " + menuItem.getName());
			}
			if(menuItem.getName().equals(CREATE_NOTIFICATION)) showNotificationDialog(null);
			else if(menuItem.getName().equals(UPDATE_NOTIFICATION)) showNotificationDialog(notificationTableModel.getSelectedItem());
			else if(menuItem.getName().equals(DELETE_NOTIFICATION)) deleteNotificationItem();
			else if(menuItem.getName().equals(CREATE_NOTIFICATION_PROVIDER)) showNotificationProviderDialog(null);
			else if(menuItem.getName().equals(UPDATE_NOTIFICATION_PROVIDER)) showNotificationProviderDialog(notificationProviderTableModel.getSelectedItem());
			else if(menuItem.getName().equals(DELETE_NOTIFICATION_PROVIDER)) deleteNotificationProviderItem();
			else if(menuItem.getName().equals(CREATE_NOTIFICATION_SUBSCRIBER)) showNotificationSubscriberDialog(null);
			else if(menuItem.getName().equals(UPDATE_NOTIFICATION_SUBSCRIBER)) showNotificationSubscriberDialog(notificationSubscriberTableModel.getSelectedItem());
			else if(menuItem.getName().equals(DELETE_NOTIFICATION_SUBSCRIBER)) deleteNotificationSubscriberItem();
		}catch(Exception ex) {
			exception = ex;
		}
		
		handleException(exception);
	}

	private void showNotificationDialog(Notification notificationProvider) {
		NotificationDialog dialog = new NotificationDialog(getMainWindow());
		dialog.showDialog(notificationProvider);
		if (!dialog.isCanceled()) {
			Notification selectedNotification = dialog.getEnteredNotification();
			updateNotificationTableModel(selectedNotification);
			if (selectedNotification != null) {
				updateProviderTableModel(selectedNotification.getNotificationClass());
				updateSubscriptionTableModel(selectedNotification.getNotificationClass());
			}
		}
	}
	
	private void updateNotificationTableModel(Notification selectedNotification) {
		List<Notification> notifications = 
				ServiceFactory.getDao(NotificationDao.class).findAllOrderByNotificationClass();
		notificationTableModel = new NotificationTableModel(notifications,notificationPane.getTable());
		notificationTableModel.addTableModelListener(this);
		notificationTableModel.selectItem(selectedNotification);
	}
	
	private void deleteNotificationItem() {
		
		Notification item = notificationTableModel.getSelectedItem();
		if(item == null) return;
		List<NotificationProvider> linkedProviders = ServiceFactory.getDao(NotificationProviderDao.class).findAllByNotificationClass(item.getNotificationClass());
		List<NotificationSubscriber> linkedSubscribers = ServiceFactory.getDao(NotificationSubscriberDao.class).findAllByNotificationClass(item.getNotificationClass());
		if (linkedProviders != null && !linkedProviders.isEmpty()) {
			if (linkedSubscribers != null && !linkedSubscribers.isEmpty()) {
				MessageDialog.showInfo(this,"Please remove the corresponding notification providers and notification subscribers first");
				return;
			} else {
				MessageDialog.showInfo(this,"Please remove the corresponding notification providers first");
				return;
			}
		} else if (linkedSubscribers != null && !linkedSubscribers.isEmpty()) {
			MessageDialog.showInfo(this,"Please remove the corresponding notification subscribers first");
			return;
		}
		if(!MessageDialog.confirm(this, "Are you sure that you want to delete ? ")) return;
		ServiceFactory.getDao(NotificationDao.class).remove(item);
		logUserAction(REMOVED, item);
		notificationTableModel.remove(item);
		   
	}

	private void showNotificationProviderDialog(NotificationProvider notificationProvider) {
		Notification notification = notificationTableModel.getSelectedItem();
		if (notification == null) {
			return;
		}
		NotificationProviderDialog dialog = new NotificationProviderDialog(getMainWindow(), notification);
		dialog.showDialog(notificationProvider);
		if (!dialog.isCanceled()) {
			updateProviderTableModel(notification.getNotificationClass());
		}
	}

	private void updateProviderTableModel(String notificationClass) {
		List<NotificationProvider> providers = 
				ServiceFactory.getDao(NotificationProviderDao.class).findAllByNotificationClass(notificationClass);
			notificationProviderTableModel = new NotificationProviderTableModel(providers,notificationProviderPane.getTable());
			notificationProviderTableModel.addTableModelListener(this);
	}

	private void deleteNotificationProviderItem() {
	  
		NotificationProvider item = notificationProviderTableModel.getSelectedItem();
		if(item == null) return;
		if(!MessageDialog.confirm(this, "Are you sure that you want to delete ? ")) return;
		ServiceFactory.getDao(NotificationProviderDao.class).remove(item);
		logUserAction(REMOVED, item);
		notificationProviderTableModel.remove(item);
		
	}
	
	private void showNotificationSubscriberDialog(NotificationSubscriber notificationSubscriber) {
		Notification notification = notificationTableModel.getSelectedItem();
		if (notification == null) {
			return;
		}
		List<NotificationProvider> providers = notificationProviderTableModel.getItems();
		NotificationSubscriberDialog dialog = new NotificationSubscriberDialog(getMainWindow(), notification, providers);
		dialog.showDialog(notificationSubscriber);
		if (!dialog.isCanceled()) {
			updateSubscriptionTableModel(notification.getNotificationClass());
		}
	}

	private void updateSubscriptionTableModel(String notificationClass) {
		List<NotificationSubscriber> subscribers = 
				ServiceFactory.getDao(NotificationSubscriberDao.class).findAllByNotificationClass(notificationClass);
			notificationSubscriberTableModel = new NotificationSubscriberTableModel(subscribers,notificationSubscriberPane.getTable());
			notificationSubscriberTableModel.addTableModelListener(this);
	}


	private void deleteNotificationSubscriberItem() {
	  
		NotificationSubscriber item = notificationSubscriberTableModel.getSelectedItem();
		if(item == null) return;
		if(!MessageDialog.confirm(this, "Are you sure that you want to delete ? ")) return;
		ServiceFactory.getDao(NotificationSubscriberDao.class).remove(item);
		logUserAction(REMOVED, item);
		notificationSubscriberTableModel.remove(item);
		
	}



	private class NotificationTableModel extends BaseTableModel<Notification> {
		
		private static final long serialVersionUID = 1L;


		public NotificationTableModel(List<Notification> items, JTable table) {
			super(items,new String[]{"Notification Class","Description","Client Only"},table);
			pack();
		}
		
		 /**
	     *  set check box for client only column
	     */
	    public Class<?> getColumnClass(int columnIndex) {
	    	return (columnIndex ==2)? Boolean.class : Object.class;
	    }
		
		public Object getValueAt(int rowIndex, int columnIndex) {
	        
			Notification item = getItem(rowIndex);
			
			switch(columnIndex) {
				case 0: return item.getNotificationClass();
				case 1: return item.getDescription();
				case 2: return item.isClientOnly();
			}
			return null;
		}
	}
	
	private class NotificationProviderTableModel extends BaseTableModel<NotificationProvider> {
		
		
		private static final long serialVersionUID = 1L;

		public NotificationProviderTableModel(List<NotificationProvider> items, JTable table) {
			super(items,new String[]{"Notification Class", "Host IP", "Host Port" , "Host Name", "Description"},table);
			pack();
		}
		
		public Object getValueAt(int rowIndex, int columnIndex) {
	        
			NotificationProvider item = getItem(rowIndex);
			
			switch(columnIndex) {
				case 0: return item.getId().getNotificationClass();
				case 1: return item.getId().getHostIp();
				case 2: return item.getId().getHostPort();
				case 3: return item.getHostName();
				case 4: return item.getDescription();
			}
			return null;
		}
	}
	
	private class NotificationSubscriberTableModel extends BaseTableModel<NotificationSubscriber> {
		
		
		private static final long serialVersionUID = 1L;

		public NotificationSubscriberTableModel(List<NotificationSubscriber> items, JTable table) {
			super(items,new String[]{"Notification Class", "Client IP", "Client Port" , "Client Name", "Notification Handler Class", "Provider", "Subscription Type"},table);
			pack();
		}
		
		public Object getValueAt(int rowIndex, int columnIndex) {
	        
			NotificationSubscriber item = getItem(rowIndex);
			
			switch(columnIndex) {
				case 0: return item.getId().getNotificationClass();
				case 1: return item.getClientHostName();
				case 2: return item.getClientPort();
				case 3: return item.getClientName();
				case 4: return item.getNotificationHandlerClass();
				case 5: return item.getId().getProvider();
				case 6: return item.getSubscriptionType();
			}
			return null;
		}
	}	
	
	
	public void tableChanged(TableModelEvent e) {
		Exception exception = null;
		
		if(e.getSource() instanceof NotificationTableModel) {
			NotificationTableModel model =  (NotificationTableModel)e.getSource();
			Notification item = model.getSelectedItem();
			if(item == null) return;
			try{
				ServiceFactory.getDao(NotificationDao.class).update(item);
				logUserAction(UPDATED, item);
			}catch(Exception ex) {
				exception = ex;
				model.rollback();
				getLogger().error("Unable to update Notification. Transaction was rolled back. " + ex.getMessage());
			}
		}else if(e.getSource() instanceof NotificationProviderTableModel){
			NotificationProvider provider = notificationProviderTableModel.getSelectedItem();
			if(provider == null) return;
			try{
				ServiceFactory.getDao(NotificationProviderDao.class).update(provider);
				logUserAction(UPDATED, provider);
			}catch(Exception ex) {
				exception = ex;
				notificationProviderTableModel.rollback();
				getLogger().error("Unable to update NotificationProvider. Transaction was rolled back. " + ex.getMessage());
			}
		}else if(e.getSource() instanceof NotificationSubscriberTableModel){
			NotificationSubscriber subscriber = notificationSubscriberTableModel.getSelectedItem();
			if(subscriber == null) return;
			try{
				ServiceFactory.getDao(NotificationSubscriberDao.class).update(subscriber);
				logUserAction(UPDATED, subscriber);
			}catch(Exception ex) {
				exception = ex;
				notificationSubscriberTableModel.rollback();
				getLogger().error("Unable to update NotificationSubscriber. Transaction was rolled back. " + ex.getMessage());
			}
		}
		
		handleException(exception);
		
	}


	public void valueChanged(ListSelectionEvent e) {
		
		if(e.getValueIsAdjusting()) return;
    	Exception exception = null;
    	try{
	    	if(e.getSource() ==(notificationPane.getTable().getSelectionModel())){
	         	Notification item = notificationTableModel.getSelectedItem();
	         	if(item == null) return;
				updateProviderTableModel(item.getNotificationClass());
				updateSubscriptionTableModel(item.getNotificationClass());
	    	} 				
    	}catch(Exception ex) {
    		exception = ex;
    	}
    	
    	handleException(exception);
    		
	}

}

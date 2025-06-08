package com.honda.galc.client.schedule;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.metal.MetalSplitPaneUI;
import javax.swing.table.DefaultTableCellRenderer;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;

import com.honda.galc.client.ui.ApplicationMainPanel;
import com.honda.galc.client.ui.component.ColorTableCellRenderer;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.IPopupMenu;
import com.honda.galc.client.ui.component.MultiValueObject;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.PopupMenuMouseAdapter;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductStampingSequence;
import com.honda.galc.service.property.PropertyService;
/**
 * <h3>Class description</h3>
 * This panel displays processed, upcoming and onhold lots vertically. 
 * <h4>Description</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Dylan Yang</TD>
 * <TD>Jan 22, 2013</TD>
 * <TD>1.0</TD>
 * <TD>GY 20130122</TD>
 * <TD>Initial Realease</TD>
 * </TR>
 */
/**
 * 
 * @author Gangadhararao Gadde
 * @date March 15 , 2016
 * Product stamping sequence screen changes
 */
public class ScheduleMainPanel extends ApplicationMainPanel implements EventSubscriber<SchedulingEvent>, ListSelectionListener {
	
	private static final long serialVersionUID = 7536387586085340508L;
	private static final String CONTROLLER_CLASS = "CONTROLLER_CLASS";

	private ObjectTablePane<MultiValueObject<BaseProduct>> processedProductPanel;
	
	private ObjectTablePane<MultiValueObject<PreProductionLot>> processedLotPanel;
	private ObjectTablePane<MultiValueObject<PreProductionLot>> upcomingLotPanel;
	private ObjectTablePane<MultiValueObject<ProductStampingSequence>> upcomingProductStampingSeqPanel;
	private ObjectTablePane<MultiValueObject<ProductStampingSequence>> processedProductStampingSeqPanel;
	private ObjectTablePane<MultiValueObject<PreProductionLot>> onHoldLotPanel;
	private CurrentLotPanel currentLotPanel;
	private CurrentLotPanel currentLotPanel1;
	
	private JPanel middlePanel;
	private JSplitPane onHoldSplitPane;
	private JSplitPane processedSplitPane;
	private JSplitPane processedLotProductStampingSeqSplitPane;
	private JSplitPane upComingLotProductStampingSeqSplitPane;

	private ScheduleClientController controller;
	private Timer autoScreenRefreshTimer = null;
	
	public ScheduleMainPanel(ScheduleWindow window) {
		super(window);
		EventBus.subscribe(SchedulingEvent.class, this);
		controller = createClientController();
		initComponents();
		if(controller.getProperties().isAutoRefreshEnabled())
		{
			startAutoScreenRefreshTimer();
		}else
		{
			controller.refreshLots();	
			controller.setInitialized(true);
		}
	}

	@SuppressWarnings("unchecked")
	private ScheduleClientController createClientController() {
		String controllerClz = PropertyService.getProperty(window.getApplicationContext().getProcessPointId(), CONTROLLER_CLASS);
		if(StringUtils.isEmpty(controllerClz) || controllerClz.equals(ScheduleClientController.class.getName()))
			return new ScheduleClientController(this);
		else{
			try {
				Class<? extends ScheduleClientController> forName = (Class<? extends ScheduleClientController>)Class.forName(controllerClz);
				Class[] parameterTypes = {ScheduleMainPanel.class};
				Object[] parameters = {this};
				Constructor constructor = forName.getConstructor(parameterTypes);
				return (ScheduleClientController)constructor.newInstance(parameters);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return null;
			
	}
	
	public void panelRealized() {
		int processedPanelSize = (Integer)controller.getProcessedPanelProperties().get(DefaultScheduleClientProperty.PANEL_SIZE);
		int upcomingPanelSize = (Integer)controller.getUpcomingPanelProperties().get(DefaultScheduleClientProperty.PANEL_SIZE);
		int onHoldPanelSize = (Integer)controller.getOnHoldPanelProperties().get(DefaultScheduleClientProperty.PANEL_SIZE);
		double sum = processedPanelSize + upcomingPanelSize + onHoldPanelSize;
		double div1 = sum == 0.0 ? 0.0 : processedPanelSize / sum;
		double div2 = sum == 0.0 ? 1.0 : (sum - onHoldPanelSize) /sum;
		
		processedSplitPane.setDividerLocation((int) (div1 * this.getHeight()));
		onHoldSplitPane.setDividerLocation((int)(div2*this.getHeight()));

		if (controller.getProperties().isHideOnHoldPanel()) {
			minimizeOnHoldPanel();
		}
	}
	
	private void initComponents() {
		
		processedSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		initializeProcessedLotPanel();
		initializeUpcomingLotPanel();
		if(getController().isProductStampingInfoEnabled())
		{
			processedLotProductStampingSeqSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
			upComingLotProductStampingSeqSplitPane= new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
			initializeUpcomingProductStampingSeqPanel();
			upComingLotProductStampingSeqSplitPane.setLeftComponent(upcomingLotPanel);
			upComingLotProductStampingSeqSplitPane.setRightComponent(upcomingProductStampingSeqPanel);
			processedLotProductStampingSeqSplitPane.setLeftComponent(processedLotPanel);
			processedLotProductStampingSeqSplitPane.setRightComponent(processedProductStampingSeqPanel);
			processedSplitPane.setTopComponent(processedLotProductStampingSeqSplitPane);
		}else
		{
			processedSplitPane.setTopComponent(controller.isProcessedProductOrLot()? processedProductPanel : processedLotPanel);
		}
		currentLotPanel = new CurrentLotPanel(controller.getCurrentPanelProperties(), controller.getBuildAttributeCache(),"Current Lot");
		currentLotPanel1 = new CurrentLotPanel(controller.getCurrentPanelProperties(), controller.getBuildAttributeCache(),"Current Lot1");
		
		initializeCurrentPanel();
		
		initializeMiddlePanel();
		
		processedSplitPane.setBottomComponent(middlePanel);
		processedSplitPane.setOneTouchExpandable(true);
		processedSplitPane.setContinuousLayout(true);
		processedSplitPane.setResizeWeight(0.4);
		processedSplitPane.addPropertyChangeListener(new PropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent arg0) {
				if("dividerLocation".equals(arg0.getPropertyName())) {
					if(processedLotPanel != null)processedLotPanel.scrollToBottom();
					if(processedProductPanel != null)processedProductPanel.scrollToBottom();
					upcomingLotPanel.scrollToTop();
				}
			}
			
		});
		onHoldSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		onHoldSplitPane.setTopComponent(processedSplitPane);
		initializeOnHoldLotPanel();
		onHoldSplitPane.setBottomComponent(onHoldLotPanel);
		onHoldSplitPane.setOneTouchExpandable(true);
		onHoldSplitPane.setContinuousLayout(true);
		
		setLayout(new BorderLayout());
		add(onHoldSplitPane, BorderLayout.CENTER);
		
	}
	
	private void initializeCurrentPanel() {
		if(currentLotPanel.getLotPanel() != null){
			currentLotPanel.getLotPanel().getTable().addMouseListener(
					createProcessedLotMouseListener((String[])controller.getCurrentPanelProperties().
							get(DefaultScheduleClientProperty.POPUP_MENU_ITEMS), currentLotPanel.getLotPanel()));
		}
	}

	private void initializeMiddlePanel() {       

		middlePanel = new  JPanel();

		middlePanel.setLayout(new BorderLayout());
				
		if(getController().isProductStampingInfoEnabled())
		{
			middlePanel.add(currentLotPanel,BorderLayout.NORTH);
			middlePanel.add(upComingLotProductStampingSeqSplitPane, BorderLayout.CENTER);
		}else
		{
			JPanel panel  = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
			panel.add(currentLotPanel);
			if(getController().isMultipleActiveLots()) {
				panel.add(currentLotPanel1);
			}

			middlePanel.add(panel,BorderLayout.NORTH);
			middlePanel.add(upcomingLotPanel, BorderLayout.CENTER);
		}

	}
	
	private void initializeProcessedLotPanel() {
		if(getController().isProductStampingInfoEnabled())
		{
			processedLotPanel = createTablePane(controller.getProcessedPanelProperties()); 
			processedLotPanel.scrollToBottom();			
			processedProductStampingSeqPanel = createProductStampingSeqTablePane(controller.getProcessedProductStampingSeqPanelProperties()); 
			processedProductStampingSeqPanel.scrollToBottom();
		}else
		{
			if(controller.isProcessedProductOrLot()){
				processedProductPanel = createProductTablePane(controller.getProcessedPanelProperties()); 
				processedProductPanel.scrollToBottom();
			}else {
				processedLotPanel = createTablePane(controller.getProcessedPanelProperties()); 
				processedLotPanel.scrollToBottom();
			}
		}
	}

	private void initializeUpcomingLotPanel() {
		upcomingLotPanel = createTablePane(controller.getUpcomingPanelProperties());
		upcomingLotPanel.scrollToTop();
	}
	
	private void initializeUpcomingProductStampingSeqPanel() {
		upcomingProductStampingSeqPanel = createProductStampingSeqTablePane(controller.getUpcomingProductStampingSeqPanelProperties());
		upcomingProductStampingSeqPanel.scrollToTop();
	}

	private void initializeOnHoldLotPanel() {
		onHoldLotPanel = createTablePane(controller.getOnHoldPanelProperties());
	}

	public List<Object> convertList(List<PreProductionLot> list) {
		List<Object> objects = new ArrayList<Object>();
		for(PreProductionLot lot : list) {
			objects.add(lot);
		}
		return objects;
	}
	
	private ObjectTablePane<MultiValueObject<BaseProduct>> createProductTablePane( Map<String, Object> properties){
		ColumnMappings columnMappings = ColumnMappings.with(
				(String[])properties.get(DefaultScheduleClientProperty.COLUMN_HEADINGS));
		
		ObjectTablePane<MultiValueObject<BaseProduct>> pane = new ObjectTablePane<MultiValueObject<BaseProduct>>(
				(String)properties.get(DefaultScheduleClientProperty.PANEL_NAME),columnMappings.get());
		pane.setAlignment(SwingConstants.CENTER);
		pane.getTable().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		pane.getTable().getSelectionModel().addListSelectionListener(this);
		pane.getTable().addMouseListener(
				createProcessedLotMouseListener(
						(String[])properties.get(DefaultScheduleClientProperty.POPUP_MENU_ITEMS), pane));
		pane.getTable().setFont((Font)properties.get(DefaultScheduleClientProperty.FONT));
		pane.getTable().setRowHeight((Integer)properties.get(DefaultScheduleClientProperty.ROW_HEIGHT));
		return pane;
	}	
	
	private ObjectTablePane<MultiValueObject<PreProductionLot>> createTablePane( Map<String, Object> properties){
		ColumnMappings columnMappings = ColumnMappings.with(
				(String[])properties.get(DefaultScheduleClientProperty.COLUMN_HEADINGS));
		
		ObjectTablePane<MultiValueObject<PreProductionLot>> pane = new ObjectTablePane<MultiValueObject<PreProductionLot>>(
				(String)properties.get(DefaultScheduleClientProperty.PANEL_NAME),columnMappings.get());
		pane.setAlignment(SwingConstants.CENTER);
		
		pane.getTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		pane.getTable().getSelectionModel().addListSelectionListener(this);
		
		MouseListener mouseListener = createProcessedLotMouseListener(
				(String[])properties.get(DefaultScheduleClientProperty.POPUP_MENU_ITEMS), pane);
		pane.getTable().addMouseListener(mouseListener);
		pane.addMouseListener(mouseListener);
		
		pane.getTable().setFont((Font)properties.get(DefaultScheduleClientProperty.FONT));
		pane.getTable().setRowHeight((Integer)properties.get(DefaultScheduleClientProperty.ROW_HEIGHT));
		return pane;
	}
	
	private ObjectTablePane<MultiValueObject<ProductStampingSequence>> createProductStampingSeqTablePane( Map<String, Object> properties){
		ColumnMappings columnMappings = ColumnMappings.with(
				(String[])properties.get(DefaultScheduleClientProperty.COLUMN_HEADINGS));
		
		ObjectTablePane<MultiValueObject<ProductStampingSequence>> pane = new ObjectTablePane<MultiValueObject<ProductStampingSequence>>(
				(String)properties.get(DefaultScheduleClientProperty.PANEL_NAME),columnMappings.get());
		pane.setAlignment(SwingConstants.CENTER);
		
		pane.getTable().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		pane.getTable().getSelectionModel().addListSelectionListener(this);
		
		MouseListener mouseListener = createProcessedLotMouseListener(
				(String[])properties.get(DefaultScheduleClientProperty.POPUP_MENU_ITEMS), pane);
		pane.getTable().addMouseListener(mouseListener);
		pane.addMouseListener(mouseListener);
		
		pane.getTable().setFont((Font)properties.get(DefaultScheduleClientProperty.FONT));
		pane.getTable().setRowHeight((Integer)properties.get(DefaultScheduleClientProperty.ROW_HEIGHT));
		return pane;
	}
	
	public ObjectTablePane<MultiValueObject<PreProductionLot>> getProcessedLotPanel() {
		return processedLotPanel;
	}
	
	public ObjectTablePane<MultiValueObject<PreProductionLot>> getUpcomingLotPanel() {
		return upcomingLotPanel;
	}
	
	public ObjectTablePane<MultiValueObject<PreProductionLot>> getOnHoldLotPanel() {
		return onHoldLotPanel;
	}
	
	public CurrentLotPanel getCurrentLotPanel() {
		return currentLotPanel;
	}

	private JPopupMenu createPopupMenu(String[] menuItems, ObjectTablePane<?> tablePane) {
		JPopupMenu menu = new JPopupMenu();
		for(String menuItem : menuItems) {
			JMenuItem item = new JMenuItem(new ScheduleAction(menuItem,tablePane));
			item.setName(menuItem);
			item.setText(menuItem);
			item.setEnabled(canEnableMenuItem(menuItem,tablePane));
			menu.add(item);
		}
		return menu;
	}
	
	private boolean canEnableMenuItem(String menuItem,
			ObjectTablePane<?> tablePane) {
		return controller.canEnableMenuItem(menuItem,tablePane);
	}

	public void minimizeOnHoldPanel() {
		if("Metal".equals(UIManager.getLookAndFeel().getID())) {
			JSplitPane splitPane = (JSplitPane) onHoldLotPanel.getParent();
			MetalSplitPaneUI ui = (MetalSplitPaneUI) splitPane.getUI();
			JButton rightButton = (JButton) ui.getDivider().getComponent(1);

			rightButton.doClick();
		}
	}

	@SuppressWarnings("unchecked")
	public synchronized void onEvent(SchedulingEvent event) {
		if(!(event.getTargetObject() instanceof List)) return;
		
		if(event.getEventType() == SchedulingEventType.PROCESSED_PRODUCT_CHANGED){
			processedProductPanel.reloadData((List<MultiValueObject<BaseProduct>>) event.getTargetObject());
			processedProductPanel.scrollToBottom();
			return;
		}
		
		if(event.getEventType() == SchedulingEventType.UPCOMING_PRODUCT_STAMPING_SEQ_CHANGED){
			upcomingProductStampingSeqPanel.reloadData((List<MultiValueObject<ProductStampingSequence>>) event.getTargetObject());
			return;
		}
		
		if(event.getEventType() == SchedulingEventType.PROCESSED_PRODUCT_STAMPING_SEQ_CHANGED){
			processedProductStampingSeqPanel.reloadData((List<MultiValueObject<ProductStampingSequence>>) event.getTargetObject());
			return;
		}
		
		List<MultiValueObject<PreProductionLot>> preProductionLots = (List<MultiValueObject<PreProductionLot>>) event.getTargetObject();
		switch(event.getEventType()) {
		case PROCESSED_ORDER_CHANGED :
			processedLotPanel.reloadData(preProductionLots);
			processedLotPanel.scrollToBottom();
			break;
		case UPCOMING_ORDER_CHANGED :
			upcomingLotPanel.reloadData(preProductionLots);
			assignUpcomingLotColors();
			break;
		case ON_HOLD_ORDER_CHANGED :
			onHoldLotPanel.reloadData(preProductionLots);
			break;
		case CURRENT_ORDER_CHANGED:
			lotInfoChanged(preProductionLots);
			break;
		default :
		}
	}
	
	public void lotInfoChanged(List<MultiValueObject<PreProductionLot>> preProductionLots) {
		if(!controller.isMultipleActiveLots()) {
			currentLotPanel.lotInfoChanged(preProductionLots);
		}else {
			List<MultiValueObject<PreProductionLot>> previousLots;
			List<MultiValueObject<PreProductionLot>> currentLots;
			MultiValueObject<PreProductionLot> previousLot = preProductionLots.get(0);
			
			previousLots = preProductionLots.stream()
							.filter(item-> item.getKeyObject().isSameKdLot(previousLot.getKeyObject()))
							.collect(Collectors.toList());
			currentLots = preProductionLots.stream()
							.filter(item-> !item.getKeyObject().isSameKdLot(previousLot.getKeyObject()))
							.collect(Collectors.toList());
			currentLotPanel.lotInfoChanged(currentLots);
			currentLotPanel1.lotInfoChanged(previousLots);
			
		}
	}

	public ScheduleClientController getController() {
		return controller;
	}

	public void setController(ScheduleClientController controller) {
		this.controller = controller;
	}
	
	public void assignUpcomingLotColors() {
		int column = (Integer)controller.getUpcomingPanelProperties().get(DefaultScheduleClientProperty.HIGHLIGHT_COLUMN);
		Color color = (Color)controller.getUpcomingPanelProperties().get(DefaultScheduleClientProperty.HIGHLIGHT_COLOR);
		String mask = (String)controller.getUpcomingPanelProperties().get(DefaultScheduleClientProperty.HIGHLIGHT_VALUE);
		if(column < 0 || column >=upcomingLotPanel.getTable().getColumnCount()) return;
		ColorTableCellRenderer renderer = new ColorTableCellRenderer(new DefaultTableCellRenderer(),
				upcomingLotPanel.getTable().getRowCount(),upcomingLotPanel.getTable().getColumnCount());
		for(int row = 0;row<upcomingLotPanel.getTable().getRowCount();row++){
			Object obj = upcomingLotPanel.getTable().getValueAt(row, column);
			if(Pattern.matches(mask, ObjectUtils.toString(obj)))
				renderer.setColor(color, row, column);
		}
		
		upcomingLotPanel.getTable().getColumnModel().getColumn(column).setCellRenderer(renderer);	
		
	}
	private MouseListener createProcessedLotMouseListener(final String[] menuItems,final ObjectTablePane<?> tablePane){
		 return new PopupMenuMouseAdapter(new IPopupMenu() {
			public void showPopupMenu(MouseEvent e) {
				JPopupMenu popupMenu = createPopupMenu(menuItems,tablePane);
				popupMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		 });
	}
	
	public void showPopupMenu1(MouseEvent e, String[] menuItems,
			ObjectTablePane<PreProductionLot> tablePane) {
	}
	

	public void valueChanged(ListSelectionEvent e) {
		if(e.getValueIsAdjusting()) return;
		getMainWindow().clearMessage();
		if(processedLotPanel != null && e.getSource().equals(processedLotPanel.getTable().getSelectionModel())){
			selectionChanged(processedLotPanel);
		}else if(e.getSource().equals(upcomingLotPanel.getTable().getSelectionModel())){
			selectionChanged(upcomingLotPanel);
		}else if(e.getSource().equals(onHoldLotPanel.getTable().getSelectionModel())){
			selectionChanged(onHoldLotPanel);
		}
		
		requestFocusOnProductId();
		
	}
	
	private void selectionChanged(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane) {
		int[] rows = tablePane.getTable().getSelectedRows();
		if(rows.length == 0) return;
		PreProductionLotUtils lotUtils = new PreProductionLotUtils(tablePane.getItems(),
				controller.getProperties().isMoveByKdLot(),controller.getProperties().lockFirstLot());
		int [] selectedRows = lotUtils.parseSelections(rows[0], rows[rows.length -1]);
		if(selectedRows[0] != rows[0] || selectedRows[1] != rows[rows.length -1]){
			tablePane.clearSelection();
			tablePane.getTable().getSelectionModel().setSelectionInterval(selectedRows[0], selectedRows[1]);
		}
	}

	public void requestFocusOnProductId() {
		if(getCurrentLotPanel() != null && getCurrentLotPanel().getProductPane() != null)
		     getCurrentLotPanel().getProductPane().getProductIdTextField().requestFocus();
	}
	
	
	public void startAutoScreenRefreshTimer() {
		TimerTask autoScreenRefreshTask = new TimerTask() {
			public void run() {
				try {
					controller.refreshLots();		
				} catch (Exception ex) {
					ex.printStackTrace();
					getLogger().error(ex);
					getMainWindow().setErrorMessage("An Error occurred refreshing the schedule client screen");
					controller.playNGSound();
				}				
			}
		};
		getAutoScreenRefreshTimer().scheduleAtFixedRate(autoScreenRefreshTask, 1000, controller.getProperties().getAutoRefreshInterval());	
		getLogger().info("Auto Screen Refresh Timer successfully started");
	}

	public Timer getAutoScreenRefreshTimer() {
		if (autoScreenRefreshTimer == null)
			autoScreenRefreshTimer = new Timer();
		return autoScreenRefreshTimer;
	}

}

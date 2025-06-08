package com.honda.galc.client.linesidemonitor.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.UIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.client.datacollection.state.ProcessRefresh;
import com.honda.galc.client.datacollection.view.action.ResetSequenceButtonAction;
import com.honda.galc.client.linesidemonitor.LineSideMonitorData;
import com.honda.galc.client.linesidemonitor.LineSideMonitorTableModel;
import com.honda.galc.client.linesidemonitor.controller.LineSideMonitorController;
import com.honda.galc.client.linesidemonitor.property.LineSideMonitorPropertyBean;
import com.honda.galc.client.ui.ApplicationMainPanel;
import com.honda.galc.client.ui.ApplicationMainWindow;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.component.ColorTableCellRenderer;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.client.ui.event.Event;
import com.honda.galc.client.ui.event.EventType;
import com.honda.galc.client.utils.ViewUtil;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.conf.ComponentPropertyDao;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.conf.ComponentPropertyId;
import com.honda.galc.property.PropertyBeanAttribute;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.KeyValue;

/**
 * 
 * <h3>LineSideMonitorPanel Class description</h3>
 * <p> LineSideMonitorPanel description </p>
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
 * Mar 30, 2011
 *
 *
 */
/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class LineSideMonitorPanel extends ApplicationMainPanel implements ListSelectionListener {

	private static final long serialVersionUID = 1L;

	private LineSideMonitorController controller;
	private TablePane lsmTablePane;
	private LineSideMonitorTableModel lsmTableModel;
	private int highlightedRow = 0;
	private Map<String, String> productIdHighlightMap;
	private List<LineSideMonitorData> lsmData = new ArrayList<LineSideMonitorData>();
	private JPanel infoPane;
	private Map<String, JTextField> infoBoxes;

	private boolean autoAdjust = true;
	private KeyValue<Color,Object> firstColorMap;
	private KeyValue<Color,Object> secondColorMap;
	private static String  beforeOffset=null;

	public LineSideMonitorPanel(MainWindow window) {
		super(window);
		initializePanel();
	}

	public LineSideMonitorPanel(ApplicationMainWindow window) {
		super(window);
		initializePanel();
	}

	public LineSideMonitorPanel(LineSideMonitorWindow window) {
		super(window);
		initializePanel();
	}

	protected void initializePanel() {
		AnnotationProcessor.process(this);
		initializeController();
		initData();
		initComponents();
		initHandlers();
		getMainWindow().setExtendedState(getMainWindow().getExtendedState() | java.awt.Frame.MAXIMIZED_BOTH);
	}

	protected void initializeController() {
		controller = new LineSideMonitorController(getMainWindow());
		try {
			controller.initLastProcessedProduct();
		} catch (TaskException e) {
			e.printStackTrace();
		}
	}

	protected void initData() {

		lsmData = getController().findAllDisplayItems(lsmData);
		if(lsmData.isEmpty()) {
			setErrorMessage("No Data. Waiting for next product... Or check settings" );
			if (getPropertyBean().isAllowDataCollection()) {
				ResetSequenceButtonAction action = new ResetSequenceButtonAction(getLineSideMonitorWindow().getDataCollectionController().getClientContext(), "SET SEQ");
				action.actionPerformed(null);
				if (action.isCancelled()) {
					throw new TaskException("Could not find last processed product");
				}
			}
		}
		if(getPropertyBean().getHighlightRowColor(Color.class) != null) {
			this.productIdHighlightMap = new HashMap<String, String>();
		}

	}

	protected void initComponents() {
		setLayout(new GridLayout());
		add(getPropertyBean().isAllowDataCollection() ? createDataCollectionMonitorPane() : createMonitorPane());
	}

	private JSplitPane createDataCollectionMonitorPane() {
		JSplitPane dataCollectionMonitorPane;
		int orientation = getPropertyBean().isWidescreen() ? JSplitPane.HORIZONTAL_SPLIT : JSplitPane.VERTICAL_SPLIT;
		if (getPropertyBean().isDataCollectionDividerLocationLocked()) {
			dataCollectionMonitorPane = createLockedSplitPane(orientation, createMonitorPane(), createDataCollectionPane(), getPropertyBean().getDataCollectionDividerLocation());
		} else if (getPropertyBean().isDataCollectionDividerLocationPersistent()) {
			try {
				String propertyKey = LineSideMonitorPropertyBean.class.getMethod("getDataCollectionDividerLocation").getAnnotation(PropertyBeanAttribute.class).propertyKey();
				dataCollectionMonitorPane = createPersistentSplitPane(orientation, createMonitorPane(), createDataCollectionPane(), propertyKey);
				dataCollectionMonitorPane.setDividerLocation(getPropertyBean().getDataCollectionDividerLocation());
			} catch (Exception e) {
				throw new TaskException("Unable to create data collection monitor pane", e);
			}
		} else {
			dataCollectionMonitorPane = new JSplitPane(orientation, createMonitorPane(), createDataCollectionPane());
			dataCollectionMonitorPane.setDividerLocation(getPropertyBean().getDataCollectionDividerLocation());
		}
		return dataCollectionMonitorPane;
	}

	private JComponent createMonitorPane() {
		if (getPropertyBean().isInfoVisible()) {
			JSplitPane monitorPane;
			if (getPropertyBean().isInfoDividerLocationLocked()) {
				monitorPane = createLockedSplitPane(JSplitPane.VERTICAL_SPLIT, createInfoPane(), createTablePane(), getPropertyBean().getInfoDividerLocation());
			} else if (getPropertyBean().isInfoDividerLocationPersistent()) {
				try {
					String propertyKey = LineSideMonitorPropertyBean.class.getMethod("getInfoDividerLocation").getAnnotation(PropertyBeanAttribute.class).propertyKey();
					monitorPane = createPersistentSplitPane(JSplitPane.VERTICAL_SPLIT, createInfoPane(), createTablePane(), propertyKey);
					monitorPane.setDividerLocation(getPropertyBean().getInfoDividerLocation());
				} catch (Exception e) {
					throw new TaskException("Unable to create data collection monitor pane", e);
				}
			} else {
				monitorPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, createInfoPane(), createTablePane());
				monitorPane.setDividerLocation(getPropertyBean().getInfoDividerLocation());
			}
			return monitorPane;
		} else {
			return createTablePane();
		}
	}

	private JSplitPane createLockedSplitPane(int orientation, Component componentA, Component componentB, final int dividerLocation) {
		JSplitPane lockedSplitPane = new JSplitPane(orientation, componentA, componentB) {
			private final int lockLocation = dividerLocation;
			@Override
			public int getDividerLocation() {
				return this.lockLocation ;
			}
			@Override
			public int getLastDividerLocation() {
				return this.lockLocation ;
			}
		};
		return lockedSplitPane;
	}

	private JSplitPane createPersistentSplitPane(int orientation, Component componentA, Component componentB, final String propertyKey) {
		JSplitPane persistentSplitPane = new JSplitPane(orientation, componentA, componentB) {
			@Override
			public void setDividerLocation(int location) {
				super.setDividerLocation(location);
				persistProperty(propertyKey, String.valueOf(location));
			}
		};
		return persistentSplitPane;
	}

	private void persistProperty(String propertyKey, String propertyValue) {
		ComponentPropertyDao propertyDao = ServiceFactory.getDao(ComponentPropertyDao.class);
		ComponentPropertyId propertyId = new ComponentPropertyId(getApplicationId(), propertyKey);
		ComponentProperty property = propertyDao.findByKey(propertyId);
		if (property == null) {
			property = new ComponentProperty();
			property.setId(propertyId);
		}
		property.setPropertyValue(propertyValue);
		property.setChangeUserId(ApplicationContext.getInstance().getUserId());
		propertyDao.save(property);
	}

	private void initHandlers() {

		lsmTablePane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener(){
			public void adjustmentValueChanged(AdjustmentEvent e) {
				if(e.getValueIsAdjusting() || !autoAdjust) return;
				adjustHighlightPosition();
				autoAdjust = false;
			}
		});

		lsmTablePane.addListSelectionListener(this);
	}

	protected LineSideMonitorWindow getLineSideMonitorWindow() {

		return (LineSideMonitorWindow) getMainWindow();

	}

	protected LineSideMonitorController getController() {

		return controller;

	}

	protected LineSideMonitorPropertyBean getPropertyBean() {

		return controller.getPropertyBean();

	}

	protected TablePane createTablePane() {
		lsmTablePane = new TablePane();
		String[] columnNames = getController().getColumnNames();
		for (int i = 0; i < columnNames.length; i++){
			String displayName = PropertyService.getProperty(getMainWindow().getApplicationContext().getProcessPointId(), "INFO_DISPLAY_NAME{" + columnNames[i] + "}", columnNames[i]);
			columnNames[i] = displayName;
		}

		if(getController().autoAdjustColumnSize()) 
			lsmTableModel = new LineSideMonitorTableModel(lsmData,columnNames,lsmTablePane.getTable());
		else 
			lsmTableModel = new LineSideMonitorTableModel(lsmData,columnNames,getController().getColumnSizes(),lsmTablePane.getTable());
		lsmTablePane.getTable().setRowHeight(getPropertyBean().getItemHeight());
		if (getPropertyBean().getViewportRowCount() > -1) {
			lsmTablePane.setPreferredScrollableViewportHeight(getPropertyBean().getViewportRowCount() * getPropertyBean().getItemHeight());
		}
		if (!getPropertyBean().isHaveCheckBoxColumn()) {
			lsmTablePane.getTable().setFocusable(false);
			lsmTablePane.getTable().setColumnSelectionAllowed(false);
			lsmTablePane.getTable().setRowSelectionAllowed(false);
		}
		lsmTablePane.getTable().setFont(Fonts.DIALOG_PLAIN(getPropertyBean().getFontSize()));
		lsmTablePane.getTable().getTableHeader().setFont(Fonts.DIALOG_PLAIN(getPropertyBean().getHeaderFontSize()));
		lsmTablePane.getTable().setForeground(getPropertyBean().getForegroundColor());
		lsmTablePane.getTable().setDefaultEditor(Boolean.class, getBooleanCellEditor());
		lsmTableModel.setCheckBoxColumn(getPropertyBean().isHaveCheckBoxColumn());
		assignColors();
		updateNumberColumn();
		lsmTableModel.setAlignment(getTablePaneAlignment());
		lsmTableModel.pack();

		// remove hidden columns from the table model
		TableColumnModel lsmTableColumnModel = lsmTablePane.getTable().getColumnModel();
		List<TableColumn> hiddenColumns = new ArrayList<TableColumn>();
		for (String hiddenColumn : getPropertyBean().getHiddenColumns()) {
			int hiddenColumnIndex = indexOf(columnNames, hiddenColumn);
			if (hiddenColumnIndex > -1) hiddenColumns.add(lsmTableColumnModel.getColumn(hiddenColumnIndex));
		}
		for (TableColumn hiddenColumn : hiddenColumns) {
			lsmTableColumnModel.removeColumn(hiddenColumn);
		}

		return lsmTablePane;
	}

	private int getTablePaneAlignment() {
		final String tablePaneAlignment = getPropertyBean().getTablePaneAlignment();
		if ("CENTER".equalsIgnoreCase(tablePaneAlignment))
			return JLabel.CENTER;
		else if ("LEFT".equalsIgnoreCase(tablePaneAlignment))
			return JLabel.LEFT;
		else if ("RIGHT".equalsIgnoreCase(tablePaneAlignment))
			return JLabel.RIGHT;
		else if ("LEADING".equalsIgnoreCase(tablePaneAlignment))
			return JLabel.LEADING;
		else if ("TRAILING".equalsIgnoreCase(tablePaneAlignment))
			return JLabel.TRAILING;
		throw new IllegalArgumentException(tablePaneAlignment + " is not a supported TABLE_PANE_ALIGNMENT");
	}

	private void updateNumberColumn() {
		int index = getController().getNumberColumnIndex();
		if(index < 0) return;

		for (int row = 0; row < lsmData.size();row++) {
			lsmData.get(row).getValues().set(index, row <= highlightedRow ? "" : row - highlightedRow);
		}
	}

	private int indexOf(String[] strings, String string) {
		if (strings == null || string == null) return -1;
		for (int i = 0; i < strings.length; i++) {
			if (strings[i].equals(string)) return i;
		}
		return -1;
	}

	protected JPanel createInfoPane() {
		infoPane = new JPanel();
		final int baseFont = getPropertyBean().getFontSize();
		infoPane.setLayout(new GridBagLayout());
		infoBoxes = new HashMap<String,JTextField>();
		for (String column : getController().getColumnNames()) {
			String configuration = PropertyService.getProperty(getMainWindow().getApplicationContext().getProcessPointId(), "INFO_CONSTRAINTS{" + column + "}");
			if (configuration == null) continue;
			int fontSize = PropertyService.getPropertyInt(getMainWindow().getApplicationContext().getProcessPointId(), "INFO_FONT_SIZE{" + column + "}", baseFont);
			String displayName = PropertyService.getProperty(getMainWindow().getApplicationContext().getProcessPointId(), "INFO_DISPLAY_NAME{" + column + "}", column);
			JTextField infoBox = makeInfoBox(displayName, fontSize);
			infoBoxes.put(column, infoBox);
			ViewUtil.setGridBagConstraints(infoPane, infoBox, configuration);
		}
		refreshInfo();
		return infoPane;
	}

	private static JTextField makeInfoBox(String label, int fontSize) {
		JTextField infoBox = new JTextField();
		infoBox.setBackground(new Color(238,238,238));
		infoBox.setBorder(new TitledBorder(label));
		infoBox.setEditable(false);
		infoBox.setFont(Fonts.DIALOG_PLAIN(fontSize));
		infoBox.setFocusable(false);
		return infoBox;
	}

	protected JPanel createDataCollectionPane() {
		JPanel dataCollectionPane = getLineSideMonitorWindow().getDataCollectionViewManager().getClientPanel();
		refreshDataCollection();
		return dataCollectionPane;
	}

	@EventSubscriber(eventClass=Event.class)
	public void lastProductChanged(Event event) {
		// handle CHANGED events from the Line Side Monitor controller
		if (event.isEventFromSource(EventType.CHANGED, getController())) {
			refreshData();
		} else if (getPropertyBean().isAllowDataCollection()) {
			// handle CHANGED events from the Data Collection controller
			if (event.isEventFromSource(EventType.CHANGED, getLineSideMonitorWindow().getDataCollectionViewManager())) {
				String before = getController().getLastProcessedProduct();
				beforeOffset = getController().getLastProcessedProduct();
				getController().initLastProcessedProduct();
				String after = getController().getLastProcessedProduct();
				if (!StringUtils.equals(before, after) || getPropertyBean().isUseExpectedAsScan()) {
					refreshData();
				}
			}
			// handle CHANGED events from the Product Id Processor
			else if (event.isEventFromSource(EventType.CHANGED, getLineSideMonitorWindow().getDataCollectionController().getProcessor(ProcessProduct.class))) {
				String productId = (String) event.getTarget();
				refreshData(productId, false);
			}
		}

	}

	/**
	 * Refreshes the Line Side Monitor and Data Collection Panel using the expected product.
	 */
	protected void refreshData() {
		refreshData(null, true);
	}

	/**
	 * Refreshes the Line Side Monitor using the given product id as the current product
	 * or the expected product if the given product id is empty.<br>
	 * Also refreshes the Data Collection Panel iff refreshDataCollection is true.
	 */
	protected void refreshData(final String productId, final boolean refreshDataCollection) {
		boolean countFlag = false;
		int count = 0;

		clearErrorMessage();

		for (LineSideMonitorData lineSideMonitorData : lsmData) {		
			final String lsmdProductId = lineSideMonitorData.getProduct().getProductId();
			if(countFlag) {
				count++;
			}
			if(lsmdProductId.equals(beforeOffset)){
				countFlag= true;					
			}	
			if((getPropertyBean().getHighlightRowOffset() != null) && (!getPropertyBean().getHighlightRowOffset().isEmpty())){
				for(String offsetKey : getPropertyBean().getHighlightRowOffset().keySet()) {				 
					if (!StringUtils.isBlank(getPropertyBean().getHighlightRowOffset().get(offsetKey))) {
						if(count == Integer.parseInt(getPropertyBean().getHighlightRowOffset().get(offsetKey))) {
							lineSideMonitorData.setHighlightRowBackgroundColor(null); 
						}							
					}
				}
			}						
		}

		lsmData = getController().findAllDisplayItems(lsmData, productId);
		if(lsmData.isEmpty()) {
			setErrorMessage("No Data. Waiting for next product... Or check settings" );
		}

		// do display refresh
		assignColors();
		refreshInfo();
		updateNumberColumn();
		lsmTableModel.refresh(lsmData);

		// adjust highlight position
		autoAdjust = true;
		adjustHighlightPosition();	

		// refresh the data collection panel
		if (refreshDataCollection) {
			refreshDataCollection();
		}	
	}

	protected void refreshInfo() {
		if (infoPane != null && infoBoxes != null) {
			if (isValidHighlightedRow()) {
				LineSideMonitorData highlightedData = lsmData.get(highlightedRow);
				for (String attribute : infoBoxes.keySet()) {
					Object value = highlightedData.getValue(attribute);
					infoBoxes.get(attribute).setText(value == null ? "" : value.toString());
				}
				colorInfoPane(highlightedData);
			} else {
				for (String attribute : infoBoxes.keySet()) {
					infoBoxes.get(attribute).setText("");
				}
				colorInfoPane(null);
			}
		}
	}

	private void colorInfoPane(LineSideMonitorData data) {
		final Color backgroundColor, foregroundColor;
		if (data == null) {
			backgroundColor = null;
			foregroundColor = null;
		} else {
			if (getPropertyBean().isAllowCacheMode()) {
				backgroundColor = data.getSpecialBackgroundColor();
				foregroundColor = data.getSpecialForegroundColor();
			} else {
				final LineSideMonitorController.SpecialColorType type = getController().getSpecialColorType(data);
				if (type == LineSideMonitorController.SpecialColorType.NONE) {
					backgroundColor = null;
					foregroundColor = null;
				} else {
					backgroundColor = getController().getSpecialBackgroundColor(type);
					foregroundColor = getController().getSpecialForegroundColor(type);
				}
			}
		}
		infoPane.setBackground(backgroundColor);
		infoPane.setForeground(foregroundColor);
		for (String column : infoBoxes.keySet()) {
			infoBoxes.get(column).setBackground(backgroundColor);
			infoBoxes.get(column).setForeground(foregroundColor);
			((TitledBorder) infoBoxes.get(column).getBorder()).setTitleColor(foregroundColor);
		}
	}

	protected void refreshDataCollection() {
		if (getPropertyBean().isAllowDataCollection()) {
			if (isValidHighlightedRow()) {
				if (getPropertyBean().isUseExpectedAsScan()) {
					String productId = lsmData.get(highlightedRow).getProduct().getProductId();
					final ProductId request = new ProductId(productId);
					if (!getLineSideMonitorWindow().getClientContext().isTrimProductId()) {
						request.setProductIdWithoutTrim(productId);
					}
					Thread t = new Thread() {
						public void run() {
							getLineSideMonitorWindow().getDataCollectionController().getProcessor(ProcessRefresh.class).processReceived(request);
						}
					};

					t.start();
				}
			}
		}
	}

	private boolean isValidHighlightedRow() {
		return (highlightedRow >= 0 && highlightedRow < lsmData.size());
	}


	private LineSideMonitorData findLastProcessedProduct() {

		LineSideMonitorData prevItem = null;
		for(LineSideMonitorData item : lsmData) {

			if(!item.isChecked()) return prevItem;
			else prevItem = item;

		}

		return lsmData.isEmpty() ? null : lsmData.get(lsmData.size() -1);

	}

	private int findIndexOfLastProcessedProduct() {
		int i = 0;
		for(LineSideMonitorData item : lsmData) {
			if(!item.isChecked()) return i - 1;
			i++;
		}
		return i-1;
	}

	private void adjustHighlightPosition() {

		SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
						lsmTablePane.scrollToCenter(highlightedRow, 0);
					}
				}
				);

	}

	public void highlightProductId(final String productId, final String identifier) {
		if (this.productIdHighlightMap != null) {
			final String previousProductId = this.productIdHighlightMap.get(identifier);
			final Color highlightRowBackgroundColor = getPropertyBean().getHighlightRowColor(Color.class).get(identifier);
			final Color highlightRowForegroundColor = new Color(255-highlightRowBackgroundColor.getRed(), 255-highlightRowBackgroundColor.getGreen(), 255-highlightRowBackgroundColor.getBlue());
			this.productIdHighlightMap.put(identifier, productId);

			boolean currentFlag = false;
			boolean previousFlag = false;
			for (LineSideMonitorData lineSideMonitorData : lsmData) {
				final String lsmdProductId = lineSideMonitorData.getProduct().getProductId();
				if (!currentFlag && lsmdProductId.equals(productId)) {
					lineSideMonitorData.setHighlightRowBackgroundColor(highlightRowBackgroundColor);
					lineSideMonitorData.setHighlightRowForegroundColor(highlightRowForegroundColor);
					currentFlag = true;
				} else if (!previousFlag && lsmdProductId.equals(previousProductId)) {
					if (ObjectUtils.equals(highlightRowBackgroundColor, lineSideMonitorData.getHighlightRowBackgroundColor())) {
						lineSideMonitorData.setHighlightRowBackgroundColor(null);
						lineSideMonitorData.setHighlightRowForegroundColor(null);
					}
					previousFlag = true;
				}
				if (currentFlag && previousFlag) {
					break;
				}
			}
			assignColors();
			lsmTableModel.refresh(lsmData);
		}
	}

	private Color colorStringConverter(String offsetKey) {
		String [] RgbValue = getPropertyBean().getHighlightRowColor().get(offsetKey).split(",");					
		int [] RgbInt= new int[RgbValue.length];
		for (int i=0;i<RgbValue.length;i++) {			
			if(RgbValue!=null) {
				RgbInt[i]=Integer.parseInt(RgbValue[i]);						 
			}
		}	
		return new Color(RgbInt[0],RgbInt[1],RgbInt[2]);	
	}

	private void assignColors() {
		final int rowCount = lsmData.size();
		final int columnCount = lsmTableModel.getTable().getColumnModel().getColumnCount();
		final int lastProcessedProductIndex = findIndexOfLastProcessedProduct();		
		highlightedRow = lastProcessedProductIndex + getPropertyBean().getHighlightPosition();

		if ((getPropertyBean().getHighlightRowOffset() != null) && (!getPropertyBean().getHighlightRowOffset().isEmpty())) {		
			for (String offsetKey : getPropertyBean().getHighlightRowOffset().keySet()) {				
				if (!StringUtils.isBlank(getPropertyBean().getHighlightRowOffset().get(offsetKey))) {
					int  highlightedRowAtOffset =  lastProcessedProductIndex + Integer.parseInt(getPropertyBean().getHighlightRowOffset().get(offsetKey))
					+ getPropertyBean().getHighlightPosition();		

					if ((getPropertyBean().getHighlightRowColor()!=null) && (!getPropertyBean().getHighlightRowColor().isEmpty())) {
						if ((getPropertyBean().getHighlightRowColor().containsKey(offsetKey)) && (!StringUtils.isBlank(getPropertyBean().getHighlightRowColor().get(offsetKey)))) {				
							Color highlightedColorAtOffset= colorStringConverter(offsetKey); 									
							setHighlightAtOffset(highlightedRowAtOffset,highlightedColorAtOffset);	
						}								
					}
				}							
			}		
		}

		final Colors colors;
		if (getPropertyBean().isAllowCacheMode()) {
			colors = getCachedColors(rowCount, columnCount);
		} else {
			colors = getUncachedColors(rowCount, columnCount);
		}

		for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
			TableCellRenderer renderer = lsmTablePane.getTable().getColumnModel().getColumn(columnIndex).getCellRenderer();
			if (renderer instanceof ColorTableCellRenderer) {
				renderer =  ((ColorTableCellRenderer)renderer).getTargetCellRenderer();
			} else if (columnIndex == 0 && getPropertyBean().isHaveCheckBoxColumn()) {
				renderer = new BooleanRenderer(getTablePaneAlignment());
			} else {
				renderer = new DefaultTableCellRenderer();
			}
			lsmTablePane.getTable().getColumnModel().getColumn(columnIndex).setCellRenderer(
					new ColorTableCellRenderer(renderer, colors.getBackgroundColors(), colors.getForegroundColors(), rowCount, columnCount));
		}

		boolean aFlag = isDataChanged(getController().getAlarmIndex(lastProcessedProductIndex));
		getController().setAlarm(aFlag);
	}

	private void setHighlightAtOffset(int highlightedRowAtOffset,Color highlightedColorAtOffset){
		boolean currentFlag  =  false;
		for (LineSideMonitorData lineSideMonitorData : lsmData) {			
			final String lsmdProductId = lineSideMonitorData.getProduct().getProductId();					 
			if (highlightedRowAtOffset<lsmData.size()){		
				String productId = lsmData.get(highlightedRowAtOffset).getProduct().getProductId();					
				if (lsmdProductId.equals(productId)) {								    				
					lineSideMonitorData.setHighlightRowBackgroundColor(highlightedColorAtOffset);	      					      						        
					currentFlag = true;
				}			
			}
			if (currentFlag) {
				break;
			}
		}		
	}

	private boolean isDataChanged(int row) {

		if(row <=0 || row >=lsmData.size()) return false;
		int column = getController().findColorChangeColumnIndex();
		return !ObjectUtils.equals(lsmData.get(row -1).getValue(column), lsmData.get(row).getValue(column));

	}

	private Colors getCachedColors(final int rowCount, final int columnCount) {
		Color[][] backgroundColors = new Color[rowCount][columnCount];
		Color[][] foregroundColors = new Color[rowCount][columnCount];
		int secondColorIndex = -1;

		for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
			if (secondColorIndex == -1 && rowIndex != 0) {
				if (!lsmData.get(rowIndex).getBackgroundColor().equals(lsmData.get(0).getBackgroundColor())) {
					secondColorIndex = rowIndex;
				}
			}
			final Color backgroundColor, foregroundColor;
			if (rowIndex == highlightedRow) {
				backgroundColor = getPropertyBean().getHighlightColor();
				foregroundColor = getPropertyBean().getHighlightForegroundColor();
			} else {
				final LineSideMonitorData item = lsmData.get(rowIndex);
				backgroundColor = item.getDominantBackgroundColor();
				foregroundColor = item.getDominantForegroundColor();
			}
			for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
				backgroundColors[rowIndex][columnIndex] = backgroundColor;
				foregroundColors[rowIndex][columnIndex] = foregroundColor;
			}
		}
		return new Colors(backgroundColors, foregroundColors, secondColorIndex);
	}

	private Colors getUncachedColors(final int rowCount, final int columnCount) {
		Color[][] backgroundColors = new Color[rowCount][columnCount];
		Color[][] foregroundColors = new Color[rowCount][columnCount];
		int secondColorIndex = -1;

		final int colorChangeColumnIndex = getController().findColorChangeColumnIndex();
		Object currentColorChangeColumnValue = lsmData.isEmpty()? null :lsmData.get(0).getValue(colorChangeColumnIndex);
		if (firstColorMap == null) {
			firstColorMap = new KeyValue<Color,Object>(getPropertyBean().getBackgroundColor(), currentColorChangeColumnValue);
		} else {
			if (secondColorMap != null && ObjectUtils.equals(currentColorChangeColumnValue, secondColorMap.getValue())) {
				firstColorMap = secondColorMap;
			} else {
				firstColorMap.setValue(currentColorChangeColumnValue);
			}
		}

		Color currentBackgroundColor = firstColorMap.getKey();
		Color currentForegroundColor = getController().getMatchingForegroundColor(currentBackgroundColor);

		int colorGroupIndex = 0;
		for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
			final LineSideMonitorData item = lsmData.get(rowIndex);
			final Object colorChangeColumnValue = item.getValue(colorChangeColumnIndex);
			if (!ObjectUtils.equals(colorChangeColumnValue, currentColorChangeColumnValue)) {
				currentColorChangeColumnValue = colorChangeColumnValue;
				currentBackgroundColor = getController().getAlternateBackgroundColor(currentBackgroundColor);
				currentForegroundColor = getController().getMatchingForegroundColor(currentBackgroundColor);
				colorGroupIndex++;
				if (colorGroupIndex == 1) { 
					secondColorMap = new KeyValue<Color,Object>(currentBackgroundColor, currentColorChangeColumnValue);
					secondColorIndex = rowIndex;
				}
			}

			final Color backgroundColor, foregroundColor;
			if (rowIndex == highlightedRow) {
				backgroundColor = getPropertyBean().getHighlightColor();
				foregroundColor = getPropertyBean().getHighlightForegroundColor();
			} else if (item.getHighlightRowBackgroundColor() != null) {
				backgroundColor = item.getHighlightRowBackgroundColor();
				foregroundColor = item.getHighlightRowForegroundColor();
			} else {
				final LineSideMonitorController.SpecialColorType type = getController().getSpecialColorType(item);
				if (type == LineSideMonitorController.SpecialColorType.NONE) {
					backgroundColor = currentBackgroundColor;
					foregroundColor = currentForegroundColor;
				} else {
					backgroundColor = getController().getSpecialBackgroundColor(type);
					foregroundColor = getController().getSpecialForegroundColor(type);
				}
			}

			for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
				backgroundColors[rowIndex][columnIndex] = backgroundColor;
				foregroundColors[rowIndex][columnIndex] = foregroundColor;
			}
		}
		return new Colors(backgroundColors, foregroundColors, secondColorIndex);
	}


	public void valueChanged(ListSelectionEvent e) {

		if(e.getValueIsAdjusting()) return;

		if(!getPropertyBean().isHaveCheckBoxColumn()) return;
		int index = findIndexOfLastProcessedProduct();
		if(index < 0) index = -1;
		if(index >= lsmData.size()) return;

		LineSideMonitorData nextItem = lsmData.get(index + 1);

		if(StringUtils.isNotBlank(getPropertyBean().getTargetProcessPoint())) {

			getController().updateLastProcessedProduct(nextItem.getProduct().getProductId());

			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					refreshData();
				}});
		}
	}

	private static class Colors
	{
		private final Color[][] backgroundColors;
		private final Color[][] foregroundColors;
		private final int secondColorIndex;
		public Colors(Color[][] backgroundColors, Color[][] foregroundColors, int secondColorIndex) {
			this.backgroundColors = backgroundColors;
			this.foregroundColors = foregroundColors;
			this.secondColorIndex = secondColorIndex;
		}
		public Color[][] getBackgroundColors() {
			return backgroundColors;
		}
		public Color[][] getForegroundColors() {
			return foregroundColors;
		}
		public int getSecondColorIndex() {
			return secondColorIndex;
		}

	}

	private static class BooleanRenderer extends JCheckBox implements TableCellRenderer, UIResource
	{
		private static final long serialVersionUID = 1L;
		private static final Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

		public BooleanRenderer(final int alignment) {
			super();
			setHorizontalAlignment(alignment);
			setBorderPainted(true);
		}

		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			if (isSelected) {
				setForeground(table.getSelectionForeground());
				super.setBackground(table.getSelectionBackground());
			}
			else {
				setForeground(table.getForeground());
				setBackground(table.getBackground());
			}
			setSelected((value != null && ((Boolean)value).booleanValue()));

			if (hasFocus) {
				setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
			} else {
				setBorder(noFocusBorder);
			}

			return this;
		}
	}

	private TableCellEditor getBooleanCellEditor() {
		JCheckBox checkBox = new JCheckBox();
		checkBox.setBorder(new EmptyBorder(1, 1, 1, 1));
		checkBox.setHorizontalAlignment(getTablePaneAlignment());
		checkBox.setBorderPainted(true);
		return new DefaultCellEditor(checkBox);
	}

}

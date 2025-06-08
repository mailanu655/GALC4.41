package com.honda.galc.client.teamleader;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import com.honda.galc.client.schedule.PreProductionLotSeqUtils;
import com.honda.galc.client.schedule.PreProductionLotUtils;
import com.honda.galc.client.schedule.ScheduleClientProperty;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.ComboBoxModel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.IPopupMenu;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.MultiValueObject;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.PopupMenuMouseAdapter;
import com.honda.galc.dao.conf.ComponentPropertyDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.entity.enumtype.PreProductionLotSendStatus;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.ReflectionUtils;

/**
 * 
 * <h3>PreProductionLotSequencePanel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> PreProductionLotSequencePanel description </p>
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
 * @author Paul Chou
 * Jul.18, 2014
 *
 */
public class PreProductionLotSequencePanel extends TabbedPanel implements ListSelectionListener, ActionListener, TableModelListener {
	private static final long serialVersionUID = 1L;
	protected boolean isInitialized = false;
	private LabeledComboBox processPointComboBox;
	private ObjectTablePane<MultiValueObject<PreProductionLot>> preProductionLotPanel;
	private JButton refreshButton;
	private String planCode;
	private ScheduleClientProperty propertyBean;
	private int startX = 10;
	private int startY = 10;
	private int buttonHight = 45;
	private int buttonWidth = 150;

	public static final String[] COLUMN_HEADINGS ={"Production Lot","Start Product Id","KD Lot","Lot Size","Spc","Sequence"};
	public static final String[] METHOD_NAMES ={"getProductionLot","getStartProductId","getKdLotNumber","getLotSize","getProductSpecCode","getSequence"}; 

	public static final String[] POPUP_MENU_ITEMS ={"Move Up","Move Down"};
	
	
	private static final int ROW_HEIGHT = 20; 
	
	private Set<String> planCodeList;

	public PreProductionLotSequencePanel() {
		super("PreProductionLot Sequence", KeyEvent.VK_P);
		init();
	}
	
	private void init() {
		setSize(1000, 750);
		retrievePlanCodes();
		onTabSelected();
	}

	private void retrievePlanCodes() {
		planCodeList = new HashSet<String>();
		
		//plan code from preproduction lot
		planCodeList.addAll(ServiceFactory.getDao(PreProductionLotDao.class).getAllPlanCodes());
		
		//plan code from properties
		planCodeList.addAll(ServiceFactory.getDao(ComponentPropertyDao.class).getAllPlanCodes());
	}

	@Override
	public void onTabSelected() {
		if(isInitialized) return;
		initComponents();
		addListeners();
		isInitialized = true;
		
		showResult();
		
	}

	private void showResult() {
		// TODO Auto-generated method stub
		
	}

	private void addListeners() {
		processPointComboBox.getComponent().addActionListener(this);
		preProductionLotPanel.getTable().getSelectionModel().addListSelectionListener(this);
		refreshButton.addActionListener(this);
		
	}

	private void initComponents() {
		setLayout(null);
		add(createPlanCodeComboBox());
	    add(createPreProductionLotSequencePane());
	    add(createRefreshButton());
		
	}

	private Component createRefreshButton() {
		if(refreshButton == null){
			refreshButton = new JButton("Refresh");
			refreshButton.setFont(Fonts.DIALOG_PLAIN_18);
			refreshButton.setSize(buttonWidth,buttonHight);
			Rectangle bounds = preProductionLotPanel.getBounds();
			refreshButton.setBounds(bounds.x + bounds.width + 10, bounds.y, buttonWidth, buttonHight);
		}
		return refreshButton;
	}

	private ObjectTablePane<MultiValueObject<PreProductionLot>> createPreProductionLotSequencePane() {
		if(preProductionLotPanel == null){
			ColumnMappings columnMappings = ColumnMappings.with(COLUMN_HEADINGS);
			
			preProductionLotPanel = new ObjectTablePane<MultiValueObject<PreProductionLot>>(
					"PreProductionLot Sequence Panel",columnMappings.get());
			preProductionLotPanel.setAlignment(SwingConstants.CENTER);
			
			preProductionLotPanel.getTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			MouseListener mouseListener = createPreProductionLotSeqMouseListener(POPUP_MENU_ITEMS, preProductionLotPanel);
			preProductionLotPanel.getTable().addMouseListener(mouseListener);
			preProductionLotPanel.addMouseListener(mouseListener);
			
			preProductionLotPanel.getTable().setFont(Fonts.DIALOG_PLAIN_18);
			preProductionLotPanel.getTable().setRowHeight(ROW_HEIGHT);
			preProductionLotPanel.setSize(800, 1200);
			Rectangle bounds = processPointComboBox.getBounds();
			preProductionLotPanel.setLocation((int)bounds.getX(), (int)(bounds.getY() + bounds.getHeight() + 5));
			
		}
		return preProductionLotPanel;
	}

	private LabeledComboBox createPlanCodeComboBox() {
		if(processPointComboBox == null){
			processPointComboBox = new LabeledComboBox("Process Point", true);
            ComboBoxModel<String> model = new ComboBoxModel<String>(new ArrayList<String>(planCodeList));
            processPointComboBox.getComponent().setModel(model);
            processPointComboBox.getComponent().setFont(Fonts.DIALOG_PLAIN_18);
            processPointComboBox.setBounds(startX, startY, 150,20);
            processPointComboBox.setSize(550, 50);
            
		}
		return processPointComboBox;
	}

	private MouseListener createPreProductionLotSeqMouseListener(final String[] menuItems,final ObjectTablePane<?> tablePane){
		 return new PopupMenuMouseAdapter(new IPopupMenu() {
			public void showPopupMenu(MouseEvent e) {
				JPopupMenu popupMenu = new JPopupMenu();
				for(String mnName : menuItems)
					popupMenu.add(createMenuItem(mnName, enablePopup(mnName, tablePane)));
				popupMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		 });
	}

	protected boolean enablePopup(String mnName, ObjectTablePane<?> tablePane) {
		int[] selectedItems = tablePane.getTable().getSelectedRows();
	
		if(selectedItems[0] == 0 && mnName.equals(POPUP_MENU_ITEMS[0])) //disable move up first line
			return false;
		
		else if(selectedItems[selectedItems.length -1] == (tablePane.getItems().size() -1) && //disable move down last
				mnName.equals(POPUP_MENU_ITEMS[1])) 
			return false;
		
		return true;
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == processPointComboBox.getComponent()){
			planCode = processPointComboBox.getComponent().getSelectedItem().toString();
			showPlanCodeSequence();
			
		} else if(e.getSource() == refreshButton){
			showPlanCodeSequence();
		}
		
		if(e.getSource() instanceof JMenuItem) {
			try{
				JMenuItem menuItem = (JMenuItem)e.getSource(); 
				if(menuItem.getName().equals(POPUP_MENU_ITEMS[0])) moveUp();
				else if(menuItem.getName().equals(POPUP_MENU_ITEMS[1])) moveDown();
				else if (menuItem.getName().equals(POPUP_MENU_ITEMS[2])) setCurrent();
				
				
			}catch(Exception ex) {
				handleException(ex);
			}
			
		}
	}

	private void moveDown() {
		int[] selectedRows = preProductionLotPanel.getTable().getSelectedRows();
		PreProductionLotSeqUtils lotUtil = new PreProductionLotSeqUtils(preProductionLotPanel.getItems(), getPropertyBean().isMoveByKdLot(),getPropertyBean().lockFirstLot());
		List<PreProductionLot> selectedRowsList = lotUtil.getSelectedRows(selectedRows[0], selectedRows[selectedRows.length -1]);
		getDao(PreProductionLotDao.class).updateAll(lotUtil.moveDown(selectedRows[0], selectedRows[selectedRows.length -1],METHOD_NAMES));
		
		updateTablePane(preProductionLotPanel,lotUtil.getDataList(preProductionLotPanel.getItems(),selectedRowsList));
		
	}

	private void moveUp() {
		int[] selectedRows = preProductionLotPanel.getTable().getSelectedRows();
		PreProductionLotSeqUtils lotUtil = new PreProductionLotSeqUtils(preProductionLotPanel.getItems(), getPropertyBean().isMoveByKdLot(),getPropertyBean().lockFirstLot());
		List<PreProductionLot> selectedRowsList = lotUtil.getSelectedRows(selectedRows[0], selectedRows[selectedRows.length -1]);
		getDao(PreProductionLotDao.class).updateAll(lotUtil.moveUp(selectedRows[0], selectedRows[selectedRows.length -1],METHOD_NAMES));
		
		updateTablePane(preProductionLotPanel,lotUtil.getDataList(preProductionLotPanel.getItems(),selectedRowsList));
		
	}

	private void setCurrent() {

	}

	protected void updateTablePane(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane,
			List<MultiValueObject<PreProductionLot>> selectedLots){
		tablePane.reloadData(tablePane.getItems());
		tablePane.clearSelection();
		tablePane.select(selectedLots);
	}
	
	private void showPlanCodeSequence() {
		List<PreProductionLot> preProductionLots = getDao(PreProductionLotDao.class).findAllByPlanCode(planCode);
		List<MultiValueObject<PreProductionLot>> preProductionLotsMVO = new ArrayList<MultiValueObject<PreProductionLot>>();
		for(PreProductionLot p : preProductionLots){
			
			if(p.getSendStatus() == PreProductionLotSendStatus.WAITING){
				List<Object> values = new ArrayList<Object>();
				for(String methodName : METHOD_NAMES)
					values.add(ReflectionUtils.invoke(p, methodName, new Object[0]));
				preProductionLotsMVO.add(new MultiValueObject<PreProductionLot>(p, values));
			}
		}
		
		preProductionLotPanel.reloadData(preProductionLotsMVO);
		
	}

	public void tableChanged(TableModelEvent arg0) {
		
	}
	
	public ScheduleClientProperty getPropertyBean() {
		if(propertyBean == null)
			propertyBean  = PropertyService.getPropertyBean(ScheduleClientProperty.class, window.getApplication().getApplicationId());
		return propertyBean;
	}

	public void valueChanged(ListSelectionEvent arg0) {
		int[] rows = preProductionLotPanel.getTable().getSelectedRows();
		if(rows.length == 0) return;
		PreProductionLotUtils lotUtils = new PreProductionLotUtils(preProductionLotPanel.getItems(),
				getPropertyBean().isMoveByKdLot(),getPropertyBean().lockFirstLot());
		int [] selectedRows = lotUtils.parseSelections(rows[0], rows[rows.length -1]);
		if(selectedRows[0] != rows[0] || selectedRows[1] != rows[rows.length -1]){
			preProductionLotPanel.clearSelection();
			preProductionLotPanel.getTable().getSelectionModel().setSelectionInterval(selectedRows[0], selectedRows[1]);
		}
		
	}

}

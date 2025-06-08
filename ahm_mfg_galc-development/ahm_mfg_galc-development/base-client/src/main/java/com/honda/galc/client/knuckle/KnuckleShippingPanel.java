package com.honda.galc.client.knuckle;

import static com.honda.galc.service.ServiceFactory.getDao;
import static com.honda.galc.service.ServiceFactory.getService;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import com.honda.galc.client.audio.ClipPlayManager;
import com.honda.galc.client.ui.ApplicationMainPanel;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.client.ui.tablemodel.KeyValueTableModel;
import com.honda.galc.client.ui.tablemodel.KnuckleShippingCartTableModel;
import com.honda.galc.client.ui.tablemodel.SubProductShippingTableModel;
import com.honda.galc.client.utils.ViewUtil;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.RequiredPartDao;
import com.honda.galc.dao.product.SubProductDao;
import com.honda.galc.dao.product.SubProductShippingDao;
import com.honda.galc.dao.product.SubProductShippingDetailDao;
import com.honda.galc.data.BuildAttributeTag;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.BuildAttributeCache;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.BuildAttributeId;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.entity.product.SubProduct;
import com.honda.galc.entity.product.SubProductShipping;
import com.honda.galc.entity.product.SubProductShippingDetail;
import com.honda.galc.entity.product.SubProductShippingId;
import com.honda.galc.service.GenericDaoService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.printing.KnuckleShippingPrintingUtil;
import com.honda.galc.util.KeyValue;

/**
 * 
 * <h3>KnuckleShippingPanel Class description</h3>
 * <p> KnuckleShippingPanel description </p>
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
 * Nov 25, 2010
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
public class KnuckleShippingPanel extends ApplicationMainPanel implements ListSelectionListener, ActionListener, FocusListener {

	private static final long serialVersionUID = 1L;
	
	private static final String LINE_NUMBER_01="01";
	private static final String LINE_NUMBER_02="02";
	private static final String SHIPPING_PLANT = "SHIPPING_PLANT";
	private static final String CART_SIZE = "CART_SIZE";
	private static final int DEFAULT_P1_CART_SIZE = 10;
	private static final int DEFAULT_P2_CART_SIZE = 15;	
	private static final String  CART_SIZE_2= "CART_SIZE_2";
	private static final String  LOAD_LEFT_RIGHT_CART = "LOAD_LEFT_RIGHT_CART";

		
	private TablePane currentKdLotPane;
	private TablePane preProductionLotPane;
	private TablePane caseNumberPane;
	private TablePane ksnTablePane;
	
	private SubProductShippingTableModel upcomingShippingLotTableModel;
	private SubProductShippingTableModel currentShippingKdLotTableModel;
	
	private KnuckleShippingCartTableModel ksnTableModel;
	
	private KeyValueTableModel caseNumberTableModel;
	
	@SuppressWarnings("unchecked")
	private List<KeyValue> caseNumbers = new ArrayList<KeyValue>();
	
	private List<SubProductShipping> shippingSchedules = new ArrayList<SubProductShipping>();

	// last plant 1 knuckle shipping lot
	private SubProductShipping lastShippingLot1;
	
//	 last plant 2 knuckle shipping lot
	private SubProductShipping lastShippingLot2;
	
	// last Knuckle shipping lot 
	
	private SubProductShipping lastShippingLot;
	
	
	private JButton skipButton = new JButton("SKIP");
	
	private JButton refreshButton = new JButton("REFRESH");
	
	
	private int cartSize;
	private String currentPrefix = "";
	//private String currentPrefixLeft = "";
	//private String currentPrefixRight = "";
	
	// the count of schedule quantity up to the current lot
	//private int currentScheduleQuantity = 0;
	
	// schedule quantity for all lots in the same kd lot, usually it is 60. But with new model it can be any number
	private int totalScheduleQuantity = 0;
	
	// current working shipping lot
	private SubProductShipping currentShippingLot = null;
	
	// current shipping lots of the same kd lot
	private List<SubProductShipping> currentProdLots = new ArrayList<SubProductShipping>();
	
	// knuckle list of the current kd lots (left cart)
	private List<SubProductShippingDetail> currentLeftCart = new ArrayList<SubProductShippingDetail>();
	
	//	 knuckle list of the current kd lots (right cart)
	private List<SubProductShippingDetail> currentRightCart = new ArrayList<SubProductShippingDetail>();
	
	private List<SubProductShippingDetail> currentCart = null; 
	//private List<SubProductShippingDetail> currentCart = currentLeftCart; 
	
	private List<SubProductShippingDetail> currentShippingDetails = new ArrayList<SubProductShippingDetail>();
	
	
	private BuildAttributeCache buildAttributeList;
	
	private LabeledTextField knuckleInputField = null;
	private LabeledTextField expectedProductLabel = null;
	
	private boolean canRequireFocus = true;
	
	private KnuckleShippingPrintingUtil printUtil;
	
	public KnuckleShippingPanel(MainWindow window) {
		
		super(window);
		
		//get logon cartSize value as default
	    cartSize = getShippingCartSize();		
		buildAttributeList = new BuildAttributeCache(
				BuildAttributeTag.KNUCKLE_LEFT_SIDE,
				BuildAttributeTag.KNUCKLE_RIGHT_SIDE);
		
		loadShippingSchedule();
		
		createShippingSchedule();
		
		loadNextKdLotData();
		
		updateCurrentShippingLot();
        //nextline		
		//cartSize =getShippingCartSize(); 
		initComponents();
		
		loadCaseNumbers();
		
		showCurrentShippingCase();
		
		refreshExpectedProduct();
		
		knuckleInputField.getComponent().requestFocusInWindow();
		
		addListeners();
	}
	

	
	private void loadCaseNumbers() {

		int count=0;
	
		//cartSize =getShippingCartSize(); 
		 count = (getCurrentShippingKdLotSize()/2 + cartSize - 1)/cartSize ;
		
		caseNumbers.clear();
		for(int i=0;i<count;i++) {
			caseNumbers.add(new KeyValue<String,Integer>(Product.SUB_ID_LEFT,i+1));
			caseNumbers.add(new KeyValue<String,Integer>(Product.SUB_ID_RIGHT,i+1));
		}
		caseNumberTableModel.refresh(caseNumbers);
		caseNumberPane.getTable().getSelectionModel().setSelectionInterval(0, 0);
	}
	
	private int getCurrentShippingKdLotSize() {
		
		int count = 0;
		for(SubProductShipping item : currentProdLots) count += item.getSchQuantity();
		return count;
		
	}
	
	private void refreshShippingSchedules() {
		
		getLogger().info("refresh knuckle shipping schedule");
		
		loadShippingSchedule();
		
		// create shipping schedules when the lot is passed through splash shield 
		createShippingSchedule();
		
	}
	
	private void loadNextKdLotData() {
		
		currentProdLots.clear();
		currentLeftCart.clear();
		currentRightCart.clear();
		currentShippingLot = null;
		totalScheduleQuantity = 0;
		
		SubProductShipping shippingLot = null;
        if(!shippingSchedules.isEmpty()) shippingLot = shippingSchedules.get(0);
        else return;
        
        for(SubProductShipping item : shippingSchedules) {
			if(item.isSameKdLot(shippingLot)) currentProdLots.add(item);
		}
		
        
        // have to filter out the same kd lot number lots (end of day lots)
       String  currentKdLotNumber = null;
        for(SubProductShipping item: currentProdLots) {
        	if(item.getKdLotNumber().equals(currentKdLotNumber)) continue;
        	currentKdLotNumber = item.getKdLotNumber();
        	List<SubProductShippingDetail> shippingDetails = 
        		getDao(SubProductShippingDetailDao.class).findAllByKdLotNumber("KNUCKLE",item.getKdLotNumber());
            
        	// populate the previously loaded knuckles to left and right carts
        	for(SubProductShippingDetail shippingDetail :shippingDetails) {
        		if(SubProduct.SUB_ID_LEFT.equals(shippingDetail.getId().getSubId()))
        			currentLeftCart.add(shippingDetail);
        		else currentRightCart.add(shippingDetail);
        	}
        }
        
      //cartSize =getShippingCartSize();  
        //reset the cart size base on the plant's kd lot value
        if(currentKdLotNumber != null && currentKdLotNumber.substring(5, 6).equals("2")){
        	//cartSize=DEFAULT_P2_CART_SIZE;        	
        	cartSize=getPropertyInt(CART_SIZE_2,DEFAULT_P2_CART_SIZE);
        	getLogger().info("load new knuckle shipping lot " + currentKdLotNumber);
        	getLogger().info("getting a cart size " + cartSize);
        }else  if(currentKdLotNumber != null && currentKdLotNumber.substring(5, 6).equals("1")){
        	//cartSize=DEFAULT_P1_CART_SIZE;   
        	cartSize=getPropertyInt(CART_SIZE,DEFAULT_P1_CART_SIZE);
        	getLogger().info("load new knuckle shipping lot " + currentKdLotNumber);
        	getLogger().info("getting a cart size " + cartSize);
        }else{        	
        	//do nothing
        }
		// get the total schedule quantity for the ko lot
		for(SubProductShipping item: currentProdLots) {
			totalScheduleQuantity += item.getSchQuantity();
		}    
        
		currentCart =  !isCartFull(currentLeftCart) || 
					    (isCartFull(currentRightCart) && currentLeftCart.size() <= currentRightCart.size()) 
				? currentLeftCart: currentRightCart;
		
		shippingSchedules.removeAll(currentProdLots);
	}
	
	private String getCurrentLineNumber() {
		
		if(isAllPlantShipping()){
			return "00";
		}else{		
		if(currentProdLots == null || currentProdLots.isEmpty()) return "0" + getPropertyInt(SHIPPING_PLANT, 0);
		if(currentProdLots != null && currentProdLots.get(0).getKdLotNumber().substring(5, 6).equals("2")){
			return "02";
		}else{
		return currentProdLots.get(0).getLineNumber();
		}
	  }
	}
	

	
	private boolean isPlant1() {	
		return "01".equals(getCurrentLineNumber());	
	}
	
	

	private String getCurrentPartNumberPrefix() {
		
		String side = currentCart == currentLeftCart ? BuildAttributeTag.KNUCKLE_LEFT_SIDE: BuildAttributeTag.KNUCKLE_RIGHT_SIDE;
		return currentShippingLot == null ? "" :getKnucklePrefix(currentShippingLot.getProductSpecCode(),side);
		
	}
	
	
	private void initComponents() {
		
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		add(Box.createVerticalStrut(10));
		add(createKnuckleInputPanel());
		add(Box.createVerticalStrut(10));
		add(createCurrentKdLotPane());
		add(Box.createVerticalStrut(10));
		add(createShippingCasePanel());
		add(Box.createVerticalStrut(10));
		add(createUpcomingProductionKdLotsPanel());
		add(Box.createVerticalStrut(10));
		
	}
	

	private void addListeners() {
		knuckleInputField.getComponent().addActionListener(this);
		knuckleInputField.getComponent().addFocusListener(this);
		preProductionLotPane.getTable().getSelectionModel().addListSelectionListener(this);
		caseNumberPane.getTable().getSelectionModel().addListSelectionListener(this);
		skipButton.addActionListener(this);
		skipButton.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				canRequireFocus = false;
			}
			public void mouseExited(MouseEvent e) {
				canRequireFocus = true;
			}	
			
		});
		
		refreshButton.addActionListener(this);
		refreshButton.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				canRequireFocus = false;
			}
			public void mouseExited(MouseEvent e) {
				canRequireFocus = true;
			}	
			
		});
	}
	
	
	private TablePane createCurrentKdLotPane() {
		currentKdLotPane = new TablePane();
		currentShippingKdLotTableModel = new SubProductShippingTableModel(currentKdLotPane.getTable(),currentProdLots);
//		currentKdLotPane.setPreferredScrollableViewportHeight(100);
		currentKdLotPane.setPreferredHeight(80);
		currentKdLotPane.setMaxHeight(80);
		
		return currentKdLotPane;
	}
	
	private TablePane createCaseNumberTablePane(){
		caseNumberPane = new TablePane();
		caseNumberPane.setPreferredWidth(150);
		caseNumberPane.setMaxWidth(150);
		caseNumberTableModel = new KeyValueTableModel(caseNumbers,"Side","Case Number",caseNumberPane.getTable());
		return caseNumberPane;
	}
	
	private TablePane createKsnPairTablePane() {

		
		ksnTablePane = new TablePane("Shipping Case");
		if(cartSize <= DEFAULT_P1_CART_SIZE) {
			ksnTablePane.getTable().getTableHeader().setFont(Fonts.DIALOG_BOLD(20));
			ksnTablePane.getTable().setFont(Fonts.DIALOG_PLAIN_20);
			ksnTablePane.getTable().setRowHeight(22);
		}else {
			ksnTablePane.getTable().getTableHeader().setFont(Fonts.DIALOG_BOLD(12));
			ksnTablePane.getTable().setFont(Fonts.DIALOG_BOLD(12));
			ksnTablePane.getTable().setRowHeight(18);
		}
	
		
		ksnTableModel = new KnuckleShippingCartTableModel(currentShippingDetails,ksnTablePane.getTable(),cartSize);
		return ksnTablePane;
	}
	
	private Component createUpcomingProductionKdLotsPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(createUpcomingProductionKdLots(),BorderLayout.CENTER);
		
		refreshButton.setFont(new java.awt.Font("dialog", 0, 18));
		
		JPanel refreshPanel = new JPanel();
		refreshPanel.setLayout(new BorderLayout());
		refreshPanel.add(refreshButton,BorderLayout.SOUTH);
		
		panel.add(refreshPanel,BorderLayout.EAST);
		
		ViewUtil.setInsets(refreshPanel, 0, 10, 0, 10);
		
		
		return panel;
	}
	
	private Component createUpcomingProductionKdLots() {
		preProductionLotPane = new TablePane("Pre Production Schedule");
		preProductionLotPane.setPreferredHeight(160);
		preProductionLotPane.setMaxHeight(160);
		
		upcomingShippingLotTableModel = new SubProductShippingTableModel(preProductionLotPane.getTable(),shippingSchedules);
		return preProductionLotPane;
	}
	
	private String getKnucklePrefix(String productSpecCode, String tag) {
		
		String mto = ProductSpec.trimColorCode(productSpecCode);
		BuildAttribute buildAttribute = buildAttributeList.findByKey(new BuildAttributeId(tag,mto)); 
		if(buildAttribute == null) return null;
		return buildAttribute.getAttributeValue()+ProductSpec.extractModelYearCode(mto);

	}
	
	private JPanel createShippingCasePanel() {
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder("Shipping Case"));
		panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));

		panel.add(Box.createHorizontalStrut(100));		
		panel.add(createCaseNumberTablePane());
		panel.add(Box.createHorizontalStrut(10));
		panel.add(createKsnPairTablePane());
		panel.add(Box.createHorizontalStrut(100));
		panel.setMaximumSize(new Dimension(panel.getMaximumSize().width,200));
		
		
		
		return panel;
	}
	
	
	private LabeledTextField createKnuckleInputField() {
		
		knuckleInputField =  new LabeledTextField("KSN:");
		knuckleInputField.setFont(Fonts.DIALOG_PLAIN_33);
		knuckleInputField.setPreferredHeight(40);
		knuckleInputField.setMaxHight(40);
		knuckleInputField.getComponent().setColumns(13);
		return knuckleInputField;
	}
	
	private JPanel createKnuckleInputPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		panel.add(createKnuckleInputField(),BorderLayout.WEST);
		expectedProductLabel = new LabeledTextField("NEXT:");
		expectedProductLabel.getComponent().setEditable(false);
		expectedProductLabel.setFont(new java.awt.Font("dialog", 0, 32));
		expectedProductLabel.getComponent().setBackground(Color.CYAN);
		expectedProductLabel.setPreferredSize(new Dimension(500,expectedProductLabel.getPreferredSize().height));
		expectedProductLabel.setMaximumSize(new Dimension(500,expectedProductLabel.getMaximumSize().height));
		skipButton.setFont(new java.awt.Font("dialog", 0, 18));
		JPanel skipPanel = new JPanel();
		skipPanel.setLayout(new BorderLayout());
		skipPanel.add(skipButton,BorderLayout.SOUTH);
		panel.add(expectedProductLabel,BorderLayout.CENTER);
		panel.add(skipPanel,BorderLayout.EAST);
		ViewUtil.setInsets(panel, 0, 0, 0, 10);
		return panel;
	}
	
	
	private boolean isAllPlantShipping() {
	int plant = getPropertyInt(SHIPPING_PLANT, 0);
		return plant == 0;
	}
	
	private int getShippingCartSize() {
		
		return isPlant1() ? getPropertyInt(CART_SIZE,DEFAULT_P1_CART_SIZE) :
					getPropertyInt(CART_SIZE_2,DEFAULT_P2_CART_SIZE);
		
	}
	
	private String getProcessLocation() {
		return getProperty("PROCESS_LOCATION", "KN");
	}
private int getSwitch() {		
		return getPropertyInt(LOAD_LEFT_RIGHT_CART,2)==2 ? 2:1;
		
	}

	private void createShippingSchedule() {
		
		lastShippingLot1 = getDao(SubProductShippingDao.class).findLastKnuckle(getProcessLocation(),LINE_NUMBER_01);
		lastShippingLot2 = getDao(SubProductShippingDao.class).findLastKnuckle(getProcessLocation(),LINE_NUMBER_02);
		
		lastShippingLot = findLastShippingLot();
		
		if(lastShippingLot != null) getLogger().info("find last knuckle shipping lot " + lastShippingLot.getKdLotNumber());
		
		SubProductShipping shippingLot = null;
		do {
		
			shippingLot = createNextShippingLot();
			if(shippingLot != null) {
				
				getLogger().info("load next knuckle shipping lot " + shippingLot.getKdLotNumber() + " from pre-production table");

				getDao(SubProductShippingDao.class).save(shippingLot);

				
				if(shippingLot.getLineNumber().equals(getCurrentLineNumber())) shippingSchedules.add(shippingLot);
				if(shippingLot.getLineNumber().equals(LINE_NUMBER_01)) lastShippingLot1 = shippingLot;
				if(shippingLot.getLineNumber().equals(LINE_NUMBER_02)) lastShippingLot2 = shippingLot;
				
				lastShippingLot = shippingLot;
			}
		}while(shippingLot != null);
	}
	
	
	private SubProductShipping findLastShippingLot() {
		
		if(lastShippingLot1 == null && lastShippingLot2 == null) return null; 
		else if(lastShippingLot1 == null) return lastShippingLot2;
		else if(lastShippingLot2 == null) return lastShippingLot1;
		PreProductionLot preProductionLot = getDao(PreProductionLotDao.class).findNext(lastShippingLot1.getProductionLot());
		if(preProductionLot == null) return lastShippingLot1;
		
		SubProductShipping item = getDao(SubProductShippingDao.class).findByKey(new SubProductShippingId(preProductionLot.getKdLot(),preProductionLot.getProductionLot()));
		
		if(item == null) return lastShippingLot1;
		else return lastShippingLot2;	
		
	}
	
	private SubProductShipping createNextShippingLot() {
		
		PreProductionLot preProdLot = null;
		int seqNo = 0;
		long currentDate = getCurrentTimeMillis();
		if(lastShippingLot == null){ 
			preProdLot = getDao(PreProductionLotDao.class).findFirstForKnuckleShipping();
			seqNo = 1000;
		}else {
			preProdLot = getDao(PreProductionLotDao.class).findNext(lastShippingLot.getProductionLot());
			if(preProdLot == null) return null;
			
			
			/*if(preProdLot.getProductionLot().substring(4, 6).equals(LINE_NUMBER_01)){
				seqNo =lastShippingLot1 != null && DateUtils.isSameDay(lastShippingLot1.getProductionDate(), currentDate) 
						? lastShippingLot1.getSeqNo() + 1000 : 1000;
			}else{  
				seqNo = lastShippingLot2 != null && DateUtils.isSameDay(lastShippingLot2.getProductionDate(), currentDate) 
				? lastShippingLot2.getSeqNo() + 1000 : 1000;
			}	*/			
			seqNo =lastShippingLot != null && DateUtils.isSameDay(lastShippingLot.getProductionDate(), new Date(currentDate)) 
			? lastShippingLot.getSeqNo() + 1000 : 1000;
			
		}
		
		if(preProdLot != null &&( preProdLot.getSendStatusId() > 0  || preProdLot.isSameKdLot(lastShippingLot.getKdLotNumber())))
		return SubProductShipping.createKnuckleShipping(preProdLot, seqNo, currentDate);
		else  return null;

	}
	
	private long getCurrentTimeMillis() {
		return ServiceFactory.getService(GenericDaoService.class).getCurrentTime();
	}



	private void loadShippingSchedule() {
		
	List<SubProductShipping> shippingLots = getShippingDao().findAllKnuckleShipping(getProcessLocation(),getCurrentLineNumber()
			);
	
	//List<SubProductShipping> shippingLots = getShippingDao().findAllKnuckleShippingLots();
		for(SubProductShipping item : shippingLots) {
			if(!currentProdLots.contains(item) && !shippingSchedules.contains(item)) {
				int detailCount = 0;
				int scanCount = 0;				
				List<SubProductShippingDetail> testDetails = 
					getDao(SubProductShippingDetailDao.class).findAllByKdLotNumber("KNUCKLE",item.getKdLotNumber());	
				for(int i = 0;i < testDetails.size() ; i++) {
					if(item.getProductionLot().equalsIgnoreCase(testDetails.get(i).getProductionLot())){
						detailCount ++;							
						if(testDetails.get(i).getProductId()!=null && testDetails.get(i).getProductId().length()>0 ){
							scanCount ++;
						}						
					}					
				}				
				if(item.getSchQuantity()==detailCount && item.getStatus()< 2){					
						if(scanCount==detailCount){
							item.setStatus(3);
							}else {
							item.setStatus(2);
						}					
					getDao(SubProductShippingDao.class).update(item);
					shippingSchedules.add(item);	
					getLogger().info("load new knuckle shipping lot " + item.getKdLotNumber());
					getLogger().info("load new production lot " + item.getProductionLot());
				}
				else{
				shippingSchedules.add(item);				
				getLogger().info("load new knuckle shipping lot " + item.getKdLotNumber());
				getLogger().info("load new production lot " + item.getProductionLot());
			}
		  }		
		}
	}

	private SubProductShippingDao getShippingDao() {
		return getDao(SubProductShippingDao.class);
	}
	
	public void actionPerformed(ActionEvent e) {

		 try{
       		 if(e.getSource() == knuckleInputField.getComponent()) ksnInputed();
       		 else if(e.getSource() == skipButton) skip();
       		else if(e.getSource() == refreshButton) doRefresh();
       	 }catch(Exception ex) {
           	 handleException(ex);	 
       	 }
	}
	
	private void refreshExpectedProduct() {
		
		int quantity = 0;
		
		for(SubProductShipping item : currentProdLots) {
			quantity += item.getSchQuantity();
		}
		
		currentPrefix = getCurrentPartNumberPrefix();
		
		String subId = currentCart == currentLeftCart ? SubProduct.SUB_ID_LEFT : SubProduct.SUB_ID_RIGHT;
		int actQuantity = currentLeftCart.size() + currentRightCart.size()+ 1;
		expectedProductLabel.getComponent().setText(subId + "  " + currentPrefix + "  " + actQuantity + "/" + quantity);
		
	}
	
	private void doRefresh() {
		
		getLogger().info("start to refresh the production lots manually from user"); 

		currentProdLots.clear();
		shippingSchedules.clear();
		
		loadShippingSchedule();
		
		createShippingSchedule();
		
		upcomingShippingLotTableModel.refresh(shippingSchedules);
    	
		loadNextKdLotData();
		
		updateCurrentShippingLot();
		
		loadCaseNumbers();
		
		showCurrentShippingCase();
		
		refreshExpectedProduct();
		
		currentShippingKdLotTableModel.refresh(currentProdLots);
		
		knuckleInputField.getComponent().requestFocusInWindow();
		
	}


	private void ksnInputed() throws Exception {
		
		String ksn = knuckleInputField.getComponent().getText();
		
		getLogger().info("Knuckle serial number " + ksn + " is scanned");
	    
		boolean isOk = verifyProduct(ksn);
		
		
		knuckleInputField.getComponent().selectAll();
	    
		if(!isOk){
			playSound(false);
			return;
		}
		
		try {
			addKnuckle(ksn);
		}catch(Exception ex) {
			playSound(false);
			throw ex;
		}
		
		// play good sound
		playSound(true);
		knuckleInputField.getComponent().requestFocusInWindow();
		

	}
	
	private void playSound(boolean isOk) {
		try {
			if(isOk)ClipPlayManager.playOkSound();
			else ClipPlayManager.playNgSound();
		} catch (Exception e) {
			getLogger().error(e,"Could not play sound");
		}
	}
	
	private void skip() {
		
		if(currentShippingLot == null) {
			setErrorMessage("No shipping Kd Lot. Could not skip next knuckle");
			return;
		}
		
		addKnuckle(null);
		
		knuckleInputField.getComponent().requestFocusInWindow();
		
	}
	
	private void addKnuckle(String ksn) {
		 
		addKnuckleInputCart(ksn);
		
	 if(isCartFull(currentCart)) {	    	
	    	// 	print when current cart is full or finish current kd lot		 

		 if(isPrintDoc(currentCart)) {
	      printCurrentCart();
		}
	    	if(currentCart == currentRightCart) {
		    	// every right cart full triggers schedule refresh
		    	refreshShippingSchedules();
		    	upcomingShippingLotTableModel.refresh(shippingSchedules);
		    	
		    	getLogger().info("refreshed shipping schedules");
		    	
	    	}
	    	
	    	if(!isCurrentKdLotFullyLoaded()) {
	    		// switch to next cart
	    		
	    		currentCart = currentCart == currentLeftCart ? currentRightCart : currentLeftCart;
	    		
	    		String side = currentCart == currentLeftCart ? "left" : "right";
	    		
	    		getLogger().info("Switched to " + side + " side cart" );
	    	    		
	    		
	    	}else {
	    		
	    		// next kd lot
		   	loadNextKdLotData();
		    	
		    upcomingShippingLotTableModel.refresh(shippingSchedules);
	    	}
	  }
	  //  cartSize =getShippingCartSize(); 
	    
	    updateCurrentShippingLot();
	    
	    loadCaseNumbers();
	    
	    showCurrentShippingCase();
	    
	    refreshExpectedProduct();
	    currentShippingKdLotTableModel.refresh(currentProdLots);
	}
	
	private boolean isCartFull(List<SubProductShippingDetail> cart) {
				
		boolean defaultValue=true;		
		if(getSwitch()==2){
			defaultValue=true;
		}else{
		int index = cart.size() % cartSize;
		   defaultValue= (index == 0 && cart.size() > 0) || cart.size() == totalScheduleQuantity /2;	    
		}
		return defaultValue;	    
	}
	
	private boolean isPrintDoc(List<SubProductShippingDetail> cart) {
	
		int index = cart.size() % cartSize;
		return (index == 0 && cart.size() > 0) || cart.size() == totalScheduleQuantity /2;	 
	   
	}
	
	
	private boolean isCurrentKdLotFullyLoaded() {
		
		return currentLeftCart.size() + currentRightCart.size() >= totalScheduleQuantity;
		
		
	}
	
	private boolean verifyProduct(String ksn) {
		if(StringUtils.isEmpty(ksn)){
	    	setErrorMessage("Knuckle serial number cannot be blank");
	    	return false;
	    }
	    if(ksn.length() != ProductType.KNUCKLE.getProductIdLength()) {
	    	setErrorMessage("Knuckle serial number length is " + ksn.length() + " Should be " + ProductType.KNUCKLE.getProductIdLength());
	    	return false;
	    }
	    
	    if(StringUtils.isEmpty(currentPrefix)) {
	    	setErrorMessage("No production Lot to be shipped");
	    	return false;
	    }
	    if(!SubProduct.getPartNumberPrefix(ksn).equals(currentPrefix)) {
	    	setErrorMessage("Knuckle serial number's part number prefix should be " + currentPrefix);
	    	return false;
	    }
	    
	    // check if the knuckle is loaded already
	    SubProductShippingDetail shippingDetail = getDao(SubProductShippingDetailDao.class).findByProductId(ksn);
	    if(shippingDetail != null) {
	    	setErrorMessage("Knuckle serial number is already loaded to kd lot : " + shippingDetail.getId().getKdLotNumber());
	    	return false;
	    }
	    
	    // check if the knuckle serial is valid
	    
	    
	    SubProduct subProduct = getDao(SubProductDao.class).findByKey(ksn);
	    if(subProduct == null) {
	    	setErrorMessage("KSN " + ksn + " does not exist");
	    	return false;
	    }
	    
	    // check if it is a knuckle product
	    if(!subProduct.getProductType().equals(ProductType.KNUCKLE)) {
	    	setErrorMessage("KSN " + ksn + " is not a knuckle product and is a " + subProduct.getProductType());
	    	return false;
	    }
	    
	    // check if the knuckle has missing required parts
	    List<String> missingRequiredParts = getDao(RequiredPartDao.class).findMissingRequiredParts(
	    		           currentShippingLot.getProductSpecCode(), getApplicationId(), ProductType.KNUCKLE,ksn,subProduct.getSubId());
	    if(!missingRequiredParts.isEmpty()) {
	    	setErrorMessage("Missing required parts : " + missingRequiredParts.toString());
	    	return false;
	    }
	    
	    return true;
	}
	
	private boolean isLeftSide() {
		
		return currentCart == currentLeftCart;
		
	}
	private void addKnuckleInputCart(String ksn) {
		
		if(isLeftSide()) {
			addKnuckleIntoCart(currentLeftCart,ksn,Product.SUB_ID_LEFT);
		}else
			addKnuckleIntoCart(currentRightCart,ksn,Product.SUB_ID_RIGHT);
		
	}

	private void addKnuckleIntoCart(List<SubProductShippingDetail> currentCart, String ksn,String subId) {
		
		int prodSeqNo = 1;
		
		//cartSize =getShippingCartSize(); 
		
		if(!currentCart.isEmpty() && 
			currentCart.get(currentCart.size() - 1).getId().getKdLotNumber().equals(currentShippingLot.getKdLotNumber()) ) {
			prodSeqNo = currentCart.get(currentCart.size() - 1).getId().getProductSeqNo() + 1;
		}
		
		// track the knuckle product to the shipping process
		if(ksn != null) {
			getService(TrackingService.class).track(ProductType.KNUCKLE, ksn, getProcessPointId());
			getLogger().info("Knuckle " + ksn + " is updated to process point : " + getProcessPointId());
		}
		
		// update the ksn to the current shipping lot
		SubProductShippingDetail shippingDetail = new SubProductShippingDetail(currentShippingLot.getKdLotNumber(),currentShippingLot.getProductionLot(),subId,prodSeqNo);
		shippingDetail.setProductId(ksn);
		currentCart.add(shippingDetail);
		getDao(SubProductShippingDetailDao.class).save(shippingDetail);
		
		// update actual quantity of current shipping lot
		if(!StringUtils.isEmpty(ksn)) currentShippingLot.incrementActQuantity();
		updateStatus(currentShippingLot);
		getDao(SubProductShippingDao.class).update(currentShippingLot);
		
		getLogger().info("knuckle " + ksn + " is loaded for kd lot " +currentShippingLot.getKdLotNumber());
  
		clearErrorMessage();
		
	}
	
	private void updateStatus(SubProductShipping shippingLot) {
		
		int status = 0;
		int count = findCountOfShippingDetails(shippingLot);
		
		if(count == 0) return;
		if(count >= shippingLot.getSchQuantity()){
			if(shippingLot.getActQuantity() < shippingLot.getSchQuantity())
				status = SubProductShipping.SHORT_SHIPPED; // short complete
			else status = SubProductShipping.SHIPPED; // complete
		}else if(count > 0) status = SubProductShipping.IN_PROGRESS;
		
		shippingLot.setStatus(status);
		
	}
	
	private int findCountOfShippingDetails(SubProductShipping shippingLot) {
		int count = 0;
		
		for(SubProductShippingDetail item : currentLeftCart) {
			if(item.getProductionLot().equals(shippingLot.getProductionLot())) count++;
		}
		
		for(SubProductShippingDetail item : currentRightCart) {
			if(item.getProductionLot().equals(shippingLot.getProductionLot())) count++;
		}
		return count;
	}

	@SuppressWarnings("unchecked")
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting()) return;
		
		if(e.getSource() == caseNumberPane.getTable().getSelectionModel()) {
			KeyValue<String,Integer> caseNumber = caseNumberTableModel.getSelectedItem();
			
			if(caseNumber == null) return;
			
			showShippingCase(caseNumber.getKey(),caseNumber.getValue() - 1);
		}
	}
	
	
	private void showShippingCase(String side, int number) {
		
		List<SubProductShippingDetail> currentShippingDetails = new ArrayList<SubProductShippingDetail>();
		List<SubProductShippingDetail> selectedCart = 
			Product.SUB_ID_LEFT.equals(side)? currentLeftCart : currentRightCart;

		//cartSize =getShippingCartSize(); 
		
		for(int i = number * cartSize;i < selectedCart.size() ; i++) {
			currentShippingDetails.add(selectedCart.get(i));
		}
		int workingNumber = currentCart ==null ? 1: currentCart.size() / cartSize;
		int cartPosition = currentCart ==null ? 1: currentCart.size() %(cartSize) + 1;
		ksnTableModel.setCurrentKnucklePosition(
				(selectedCart == currentCart && number == workingNumber)
				? cartPosition : 0);
		ksnTablePane.getTable().setRowHeight(cartSize <= DEFAULT_P1_CART_SIZE ?22:18 );
		ksnTableModel.setRowCount(cartSize);
		ksnTableModel.refresh(currentShippingDetails);
		ksnTablePane.setBorder(new TitledBorder("Shipping Case - " + (Product.SUB_ID_LEFT.equals(side)? "LEFT" : "RIGHT")));
		
		
	}
	
	private void showCurrentShippingCase() {
		
		//cartSize =getShippingCartSize(); 
		String side = currentCart == null || currentCart == currentLeftCart ? Product.SUB_ID_LEFT : Product.SUB_ID_RIGHT;
		int currentCaseNumber = currentCart == null  ? 0 : currentCart.size() / cartSize;
		caseNumberTableModel.selectItem(new KeyValue<String,Integer>(side,currentCaseNumber + 1));
		showShippingCase(side,currentCaseNumber);
	   
	}
	
	private void updateCurrentShippingLot() {
		
		int count = 0;
		for(SubProductShipping item : currentProdLots) {			
			count += item.getSchQuantity() /2;
			if(currentCart.size() < count) {
				currentShippingLot = item;
				return;
			}
		}
	}
	
	private void printCurrentCart() {
		
		List<SubProductShippingDetail> details = getCurrentCartShippingDetails();
		
		getPrintingUtil().print(currentProdLots,details);
		
	}
	
	private KnuckleShippingPrintingUtil getPrintingUtil() {
		if(printUtil == null) {
			printUtil = new KnuckleShippingPrintingUtil();
		}
		return printUtil;
	}
	
	
	private List<SubProductShippingDetail> getCurrentCartShippingDetails() {
		
		List<SubProductShippingDetail> details = new ArrayList<SubProductShippingDetail>();

		//cartSize =getShippingCartSize(); 
		int index = ((currentCart.size() -1) / cartSize) * cartSize;
		
		for(int i = index;i<currentCart.size();i++) {
			details.add(currentCart.get(i));
		}
		
		return details;
	}
	
	public void focusGained(FocusEvent e) {
	}

	public void focusLost(FocusEvent e) {
		if(canRequireFocus)
			knuckleInputField.getComponent().requestFocusInWindow();
	}

}

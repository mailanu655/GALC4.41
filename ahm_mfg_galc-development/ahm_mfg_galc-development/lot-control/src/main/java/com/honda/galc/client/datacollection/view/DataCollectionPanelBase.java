package com.honda.galc.client.datacollection.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

import org.bushe.swing.event.annotation.EventSubscriber;

import com.honda.galc.client.common.datacollection.data.AutoFocusMessage;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.property.ViewProperty;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.lotcontrol.ITorqueDevice;
import com.honda.galc.client.ui.StatusMessagePanel;
import com.honda.galc.client.ui.component.LengthFieldBean;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.ProductType;
import com.honda.galc.device.IDevice;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.util.StringUtil;

/**
 * <h3>DataCollectionPanel</h3>
 * <h4>
 * Data Collection Display Panel. 
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Nov.31 2008</TD>
 * <TD>0.1</TD>
 * <TD>Initial Version</TD>
 * <TD></TD>
 * </TR>  
 * </TABLE>
 * @see 
 * @ver 0.1
 * @author Paul Chou
 */
/** * *
 * @version 0.2 
 * @author Gangadhararao Gadde 
 * @since Aug 09, 2012 
 */ 
public abstract class DataCollectionPanelBase extends JPanel{

	private static final long serialVersionUID = 3993392297145045873L;

	// GUI
	protected static final int TOTAL_BUTTON = 5;
	protected int gap = 1;
	protected int maxNumOfPart = 0;
	public int maxNumOfTorque = 0;
	public int separatorGap = 0;

	protected ArrayList<UpperCaseFieldBean> partSNList = new ArrayList<UpperCaseFieldBean>();
	protected ArrayList<JLabel> partLabelList = new ArrayList<JLabel>();
	protected ArrayList<JTextField> torqueValueList = new ArrayList<JTextField>();
	protected ArrayList<JButton> buttonList = new ArrayList<JButton>();
	protected ArrayList<UpperCaseFieldBean> missingRequiredPartList = new ArrayList<UpperCaseFieldBean>();
	protected ArrayList<JLabel> missingRequiredPartLabelList = new ArrayList<JLabel>();
	protected JButton widerButton;
	protected JButton ivjJButtonProductId;
	private JButton testTorqueButton;
	protected JButton removeResultButton;
	protected TablePane repairPartsTable;

	protected JLabel labelExpPidOrProdSpec = null;
	protected JLabel labelAfOnSeqNum = null;
	private JLabel labelTorque = null;
	protected JTextField textFieldExpPIDOrProdSpec = null;
	protected javax.swing.JLabel labelLastPid = null;
	protected javax.swing.JTextField textFieldLastPid = null;
	protected javax.swing.JLabel labelLastEngine = null;
	protected javax.swing.JTextField textFieldLastEngine = null;
	protected javax.swing.JLabel labelLastAfOnSeq = null;
	protected javax.swing.JTextField textFieldLastAfOnSeq = null;
	protected javax.swing.JLabel labelLastMto = null;
	protected javax.swing.JTextField textFieldLastMto = null;
	protected JLabel labelAfOnSeqNumValue = null;

	protected LengthFieldBean textFieldSubId;
	protected List<JButton> cartButtonList;

	protected ViewProperty viewProperty;

	private int mainWindowWidth;
	private int mainWindowHeight;

	protected JLabel prodIdLabel;

	protected UpperCaseFieldBean textFieldProdId;
	protected JLabel labelProductCount = null;
	protected JLabel labelProductCountValue = null;


	private Color initBackgroundColor = ViewControlUtil.VIEW_COLOR_CURRENT;
	private Color initForegroundColor = ViewControlUtil.VIEW_COLOR_INPUT;

	public DataCollectionPanelBase() {
		super();
		init();
	}

	public DataCollectionPanelBase(ViewProperty property, int winWidth, int winHeight) {
		super();
		this.mainWindowWidth = winWidth;
		this.mainWindowHeight = winHeight;

		viewProperty = property;
	}

	protected void init() {
		try {
			maxNumOfPart = viewProperty.getMaxNumberOfPart(); // default 4
			maxNumOfTorque = viewProperty.getMaxNumberOfTorque(); //default 10
			gap = viewProperty.getGap();
			separatorGap = viewProperty.getSeparatorGap();

			getTextFieldProdId();
			getTextFieldExpPidOrProdSpec(); 
			getLabelAfOnSeqNumValue();
			getLabelProductCountValue();
			initComponents();
			this.initPanel();

		} catch (Exception e) {
			handleException(e);
		}		
	}

	protected void initComponents() {
		initPartLabelList();
		initPartSerialNumberList();
		initTorqueValueTextFildList();
		initButtonList();

		if(isPartLotControl())
			initCartButtonList();

	}

	protected boolean isPartLotControl() {
		return getClientContext().isPartLot();
	}

	protected boolean isAfOnSeqNumExist() {
		return getClientContext().isAfOnSeqNumExist();
	}
	
	protected boolean isProductLotCountExist() {
		return getClientContext().isProductLotCountExist();
	}

	protected ClientContext getClientContext() {
		return DataCollectionController.getInstance().getClientContext();
	}

	protected void initCartButtonList() {
		final int CART_LABEL_WIDTH = 200;
		final int BUTTON_X = 2;

		JButton cartButton;
		for (int i = 0; i < maxNumOfPart; i++) {
			Rectangle bounds = getPartLabel(i).getBounds();
			cartButton = new JButton();
			cartButton.setName("CartButton0" + (i +1));
			cartButton.setBounds(BUTTON_X, bounds.y, CART_LABEL_WIDTH, (int)(bounds.height * 0.8));
			cartButton.setVisible(false);
			cartButton.setFont(new java.awt.Font("dialog", 0, viewProperty.getButtonFontSize()));
			cartButton.setText("New/Change Cart");
			getCartButtonList().add(cartButton);
		}		
	}

	public List<JButton> getCartButtonList() {
		if(cartButtonList == null)
			cartButtonList = new ArrayList<JButton>();

		return cartButtonList;
	}

	public JButton getCartButton(int index){
		return cartButtonList.get(index);
	}

	protected javax.swing.JLabel getLabelLastPid() {
		if(labelLastPid == null) {
			labelLastPid = new javax.swing.JLabel();
			if(viewProperty.getLastPidLabelX() == -999) {
			labelLastPid.setBounds(2, viewProperty.getLastPidTextFieldY(), 
						viewProperty.getLastPidTextFieldX() -3, viewProperty.getLastPidTextFieldHeight());
			} else if (viewProperty.getLastPidLabelY() != 0) {
				labelLastPid.setBounds(viewProperty.getLastPidLabelX(), viewProperty.getLastPidLabelY(), 
						viewProperty.getLastPidLabelWidth(), viewProperty.getLastPidTextFieldHeight());
			} else {
				labelLastPid.setBounds(viewProperty.getLastPidLabelX(), viewProperty.getLastPidTextFieldY(), 
						viewProperty.getLastPidLabelWidth(), viewProperty.getLastPidTextFieldHeight());
			}
			labelLastPid.setText("Last "+viewProperty.getProductIdLabel());
			labelLastPid.setComponentOrientation(java.awt.ComponentOrientation.RIGHT_TO_LEFT);
			labelLastPid.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 18));
			labelLastPid.setName("LastProductIdLabel");
		}
		return labelLastPid;
	}

	public javax.swing.JTextField getTextFieldLastPid() {
		if(textFieldLastPid == null) {
			textFieldLastPid = new javax.swing.JTextField();
			textFieldLastPid.setBounds(viewProperty.getLastPidTextFieldX(), viewProperty.getLastPidTextFieldY(),
					viewProperty.getLastPidTextFieldWidth(), viewProperty.getLastPidTextFieldHeight());/* 215, 586, 299, 51*/
			textFieldLastPid.setName("JTextLastPID");
			textFieldLastPid.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 30));
			textFieldLastPid.setEnabled(true);
			if(viewProperty.isLastPidTextFieldUnfocusable())
				textFieldLastPid.setFocusable(false);
			textFieldLastPid.setEditable(false);
			textFieldLastPid.setBackground(Color.white);
		}
		return textFieldLastPid;
	}

	protected javax.swing.JLabel getLabelLastEngine() {
		if(labelLastEngine == null) {
			labelLastEngine = new javax.swing.JLabel();
			labelLastEngine.setBounds(viewProperty.getLastPidLabelX(), viewProperty.getLastPidTextFieldY()+47, 
						viewProperty.getLastPidLabelWidth(), viewProperty.getLastPidTextFieldHeight());
			labelLastEngine.setText("Last EIN:");
			labelLastEngine.setComponentOrientation(java.awt.ComponentOrientation.RIGHT_TO_LEFT);
			labelLastEngine.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 18));
			labelLastEngine.setName("LastEngineLabel");
		}
		return labelLastEngine;
	}

	public javax.swing.JTextField getTextFieldLastEngine() {
		if(textFieldLastEngine == null) {
			textFieldLastEngine = new javax.swing.JTextField();
			textFieldLastEngine.setBounds(viewProperty.getLastPidTextFieldX(), viewProperty.getLastPidTextFieldY()+45,
					viewProperty.getLastPidTextFieldWidth()-50, viewProperty.getLastPidTextFieldHeight());/* 215, 586, 299, 51*/
			textFieldLastEngine.setName("JTextLastEngine");
			textFieldLastEngine.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 30));
			textFieldLastEngine.setEnabled(true);
			textFieldLastEngine.setFocusable(false);
			textFieldLastEngine.setEditable(false);
			textFieldLastEngine.setBackground(Color.white);
		}
		return textFieldLastEngine;
	}
	
	protected javax.swing.JLabel getLabelLastAfOnSeq() {
		if(labelLastAfOnSeq == null) {
			labelLastAfOnSeq = new javax.swing.JLabel(viewProperty.getLastAfOnSeqLabelText());
			labelLastAfOnSeq.setBounds(viewProperty.getLastAfOnSeqLabelX(), viewProperty.getLastPidTextFieldY()-35, 
					viewProperty.getLastAfOnSeqTextFieldWidth(), viewProperty.getLastPidTextFieldHeight()); /* 130, 551, 84 51 */ 
			labelLastAfOnSeq.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
			labelLastAfOnSeq.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, viewProperty.getLastAfOnSeqLabelFontSize()));
			labelLastAfOnSeq.setName("LastAFOnSeqLabel");
		}
		return labelLastAfOnSeq;
	}

	public javax.swing.JTextField getTextFieldLastAfOnSeq() {
		if(textFieldLastAfOnSeq == null) {
			textFieldLastAfOnSeq = new javax.swing.JTextField();
			textFieldLastAfOnSeq.setBounds(viewProperty.getLastAfOnSeqTextFieldX(), viewProperty.getLastPidTextFieldY(), 
					viewProperty.getLastAfOnSeqTextFieldWidth(), viewProperty.getLastPidTextFieldHeight());/* 128, 586, 84, 51*/
			textFieldLastAfOnSeq.setName("JTextLastAFOnSeq");
			textFieldLastAfOnSeq.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, viewProperty.getLastAfOnSeqTextFieldFontSize()));
			textFieldLastAfOnSeq.setEnabled(true);
			textFieldLastAfOnSeq.setFocusable(false);
			textFieldLastAfOnSeq.setEditable(false);
			textFieldLastAfOnSeq.setBackground(Color.white);
		}
		return textFieldLastAfOnSeq;
	}
	
	protected javax.swing.JLabel getLabelLastMto() {
		if(labelLastMto == null) {
			labelLastMto = new javax.swing.JLabel(viewProperty.getLastMtoLabelText());
			labelLastMto.setBounds(viewProperty.getLastMtoLabelX(), viewProperty.getLastPidTextFieldY()-35,
					viewProperty.getLastMtoTextFieldWidth(),viewProperty.getLastPidTextFieldHeight()); /* 7, 551, 120, 51 */
			labelLastMto.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
			labelLastMto.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, viewProperty.getLastMtoLabelFontSize()));
			labelLastMto.setName("LastMTOLabel");
		}
		return labelLastMto;
	}

	public javax.swing.JTextField getTextFieldLastMto() {
		if(textFieldLastMto == null) {
			textFieldLastMto = new javax.swing.JTextField();
			textFieldLastMto.setBounds(viewProperty.getLastMtoTextFieldX(), viewProperty.getLastPidTextFieldY(), 
					viewProperty.getLastMtoTextFieldWidth(), viewProperty.getLastPidTextFieldHeight());/* 5, 586, 120, 51*/
			textFieldLastMto.setName("JTextLastMTO");
			textFieldLastMto.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, viewProperty.getLastMtoTextFieldFontSize()));
			textFieldLastMto.setEnabled(true);
			textFieldLastMto.setFocusable(false);
			textFieldLastMto.setEditable(false);
			textFieldLastMto.setBackground(Color.white);
		}
		return textFieldLastMto;
	}

	protected void initPartLabelList() {
		JLabel partLabel;

		for (int i = 0; i < maxNumOfPart; i++) {
			partLabel = new javax.swing.JLabel();
			partLabel.setName("JLabelPart0" + i);
			partLabel.setFont(new java.awt.Font("dialog", 0, viewProperty.getPartLabelFontSize()));
			partLabel.setText("Part0: " + i);
			repositionPartLabel(partLabel, i, getClientContext().isAfOnSeqNumExist());
			partLabel.setComponentOrientation(java.awt.ComponentOrientation.RIGHT_TO_LEFT);
			partLabel.setForeground(java.awt.Color.black);
			partLabel.setHorizontalAlignment(SwingConstants.RIGHT);
			partLabelList.add(partLabel);
		}		
	}

	public void repositionPartLabel(JLabel partLabel, int position, boolean isAfOnSeqNoExist) {
		repositionPartComponent(partLabel, position, isAfOnSeqNoExist, true);
	}
	
	
	protected void renderProductCount(String label, Long count, Long size) {
		getLabelProductCountValue().setText((Long.valueOf(count)!= 0 && Long.valueOf(size)!= 0)?StringUtil.padLeft(count+"/"+size,5,'0'):"--/--");
		getLabelProductCount().setVisible(true);
		getLabelProductCountValue().setVisible(true);
	}

	protected void initButtonList() {
		int x = viewProperty.getButtonPositionX();//560
		int y = viewProperty.getButtonPositionY();//598
		int gap  = viewProperty.getButtonGap();//10
		int width = viewProperty.getButtonWidth();//100
		int height = viewProperty.getButtonHeight();//25
		int fontSize = viewProperty.getButtonFontSize();//16
		JButton button = null;
		for(int i = 0; i < TOTAL_BUTTON; i++){
			button = new JButton();
			button.setName("Button0" + (i+1));
			button.setBounds(x + (i)*(width + gap), y, width, height);
			button.setFont(new Font("dialog", 0, fontSize));

			button.setVisible(false); 
			buttonList.add(button);
		}
		widerButton = getWiderButton();
	}

	public JLabel getLabelExpPIDOrProdSpec() {
		if (labelExpPidOrProdSpec == null) {
			labelExpPidOrProdSpec = new javax.swing.JLabel();
			labelExpPidOrProdSpec.setName("LabelExpProdSpec");
			labelExpPidOrProdSpec.setFont(new java.awt.Font("dialog", 0, 18));
			labelExpPidOrProdSpec.setText("");
			labelExpPidOrProdSpec.setComponentOrientation(java.awt.ComponentOrientation.RIGHT_TO_LEFT);
			Rectangle refbounds = getLabelExpPidOrProdSpecRefrenceBounds();
			labelExpPidOrProdSpec.setBounds(refbounds.x, refbounds.y + 46, refbounds.width, refbounds.height);
			labelExpPidOrProdSpec.setForeground(java.awt.Color.black);
		}
		return labelExpPidOrProdSpec;
	}

	public JLabel getLabelAfOnSeqNum() {
		if (labelAfOnSeqNum == null) {
			labelAfOnSeqNum = new javax.swing.JLabel();
			labelAfOnSeqNum.setName("LabelAfOnSeq");
			labelAfOnSeqNum.setFont(new java.awt.Font("dialog", 0, viewProperty.getAfOnSeqNumLabelFontSize()));		
			labelAfOnSeqNum.setText("");
			labelAfOnSeqNum.setHorizontalAlignment(SwingConstants.LEFT);
			labelAfOnSeqNum.setComponentOrientation(java.awt.ComponentOrientation.RIGHT_TO_LEFT);
			Rectangle refBounds = getLabelAfOnSeqNoBounds();
			labelAfOnSeqNum.setBounds(refBounds.x, refBounds.y - refBounds.height * 7/10, refBounds.width, refBounds.height * 7/10);
			labelAfOnSeqNum.setForeground(java.awt.Color.black);
			labelAfOnSeqNum.setVisible(false);
		}
		return labelAfOnSeqNum;
	}
	
	public JLabel getLabelProductCount() {
		if (labelProductCount == null) {
			labelProductCount = new javax.swing.JLabel();
			labelProductCount.setName("LabelProductCount");
			labelProductCount.setFont(new java.awt.Font("dialog", 0, viewProperty.getProductCountLabelFontSize()));		
			labelProductCount.setText("");
			labelProductCount.setHorizontalAlignment(SwingConstants.LEFT);
			labelProductCount.setComponentOrientation(java.awt.ComponentOrientation.RIGHT_TO_LEFT);
			Rectangle refBounds = getLabelProductCountBounds();
			labelProductCount.setBounds(refBounds.x, refBounds.y - refBounds.height * 7/10, refBounds.width, refBounds.height * 7/10);
			labelProductCount.setForeground(java.awt.Color.black);
			labelProductCount.setVisible(false);
		}
		return labelProductCount;
	}

	protected Rectangle getLabelAfOnSeqNoBounds(){
		return getLabelAfOnSeqNumValue().getBounds();
	}
	
	protected Rectangle getLabelProductCountBounds(){
		return getLabelProductCountValue().getBounds();
	}
	protected Rectangle getLabelExpPidOrProdSpecRefrenceBounds() {
		if (viewProperty.isProductIdButton()) {
			return getJButtonProductId().getBounds();
		} else {
			return getLabelProdId().getBounds();
		}
	}

	public JLabel getLabelTorque() {
		if (labelTorque == null) {
			labelTorque = new JLabel();
			labelTorque.setName("LabelTorque");
			labelTorque.setFont(new java.awt.Font("dialog", 0, 18));
			labelTorque.setText("Torque:");
			labelTorque.setComponentOrientation(java.awt.ComponentOrientation.RIGHT_TO_LEFT);
			int x = viewProperty.getTorqueStartPositionX() - viewProperty.getTorqueLabelWidth() - 20;
			labelTorque.setBounds(x, viewProperty.getTorqueStartPositionY(), 
					viewProperty.getTorqueLabelWidth(), viewProperty.getTorqueLabelHeight());
			labelTorque.setForeground(java.awt.Color.black);
		}
		return labelTorque;
	}

	protected void initPanel() {
		try {
			setName("DataCollectionView");
			setLayout(null);

			if(viewProperty.isProductIdButton())
				add(getJButtonProductId(), getJButtonProductId().getName());
			else
				add(getLabelProdId(), getLabelProdId().getName());

			add(getTextFieldProdId(), getTextFieldProdId().getName());
			add(getLabelExpPIDOrProdSpec(), getLabelExpPIDOrProdSpec().getName());
			add(getTextFieldExpPidOrProdSpec(), getTextFieldExpPidOrProdSpec().getName());
			add(getLabelAfOnSeqNum(), getLabelAfOnSeqNum().getName());  
			add(getLabelAfOnSeqNumValue(),getLabelAfOnSeqNumValue().getName());
			add(getLabelProductCount(), getLabelProductCount().getName());  
			add(getLabelProductCountValue(),getLabelProductCountValue().getName());
			
			for (int i = 0; i < maxNumOfPart; i++) {
				add(getPartSerialNumber(i), getPartSerialNumber(i).getName());	
				add(getPartLabel(i), getPartLabel(i).getName());
			}			

			add(getLabelTorque(), getLabelTorque().getName());

			for (int i = 0; i < maxNumOfTorque; i++) {
				add(getTorqueValueTextField(i),	getTorqueValueTextField(i).getName());
			}		

			for(int i = 0; i < buttonList.size(); i++){
				add(getButton(i), getButton(i).getName());
			}

			if (getClientContext().getProperty().isShowTestTorque()  && hasTorqueDevices()) {
				add(getTestTorqueButton(), null);
			}

			add(getWiderButton(), getWiderButton().getName());

			add(getLabelLastPid(), null);
			add(getTextFieldLastPid(), null);
			
			if(viewProperty.isShowLastEngine()) {
				add(getLabelLastEngine(), null);
				add(getTextFieldLastEngine(), null);
			}
			
			if (viewProperty.isShowLastAfOnSeqNumber()) {
				add(getLabelLastAfOnSeq(), null);
				add(getTextFieldLastAfOnSeq(), null);	
			}
			
			if(viewProperty.isShowLastMto()) {
				add(getLabelLastMto(), null);
				add(getTextFieldLastMto(), null);	
			}

			if(viewProperty.isShowSubId())
				add(getTextFieldSubId(), getTextFieldSubId().getName());

			if(isPartLotControl()) {
				for(JButton button : cartButtonList){
					add(button, button.getName());
				}
			}
			if(repairPartsTable!= null){
				add(getRepairPartsTable());
			}
			if(removeResultButton != null){
				add(getRemoveResultsButton());
			}

			if(!isAfOnSeqNumExist()) {
				getLabelAfOnSeqNum().setVisible(false);  
				getLabelAfOnSeqNumValue().setVisible(false);
			}
			
			
			if(!isProductLotCountExist()) {
				getLabelProductCount().setVisible(false);  
				getLabelProductCountValue().setVisible(false);
			}
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	public TablePane getRepairPartsTable() {
		return repairPartsTable;
	}

	public JButton getRemoveResultsButton() {
		return removeResultButton;
	}


	protected boolean hasTorqueDevices() {
		boolean hasTorqueDevice = false;
		for (IDevice device : DeviceManager.getInstance().getDevices().values()) {
			if (device instanceof ITorqueDevice) {
				hasTorqueDevice=true;
				break;
			}
		}

		return hasTorqueDevice;
	}

	public javax.swing.JButton getTestTorqueButton() {
		if (testTorqueButton == null) {
			testTorqueButton = new javax.swing.JButton("Test Torque Gun");
			testTorqueButton.setBounds(getButton(2).getX(), getButton(2).getY()-35,
					120, 25);
			testTorqueButton.setName("JTestTorque");
			testTorqueButton.setFont(new Font("dialog", java.awt.Font.PLAIN, 16));
			testTorqueButton.setEnabled(true);

		}
		return testTorqueButton;
	}
	public javax.swing.JTextField getTextFieldExpPidOrProdSpec() {
		if (textFieldExpPIDOrProdSpec == null) {

			textFieldExpPIDOrProdSpec = new javax.swing.JTextField();
			textFieldExpPIDOrProdSpec.setName("TextFieldExpPIDOrProdSpec");
			textFieldExpPIDOrProdSpec.setFont(new java.awt.Font("dialog", 0, 36));
			textFieldExpPIDOrProdSpec.setText("WWWWWWWWWWWWWWWWW");
			Rectangle refbounds = getTextFieldExpPidOrProdSpecRefrenceBounds();
			int fieldWith = viewProperty.isShowProductSubid() ? refbounds.width *7/10 : refbounds.width;
			textFieldExpPIDOrProdSpec.setBounds(refbounds.x, refbounds.y + 46, fieldWith, refbounds.height);
		}
		return textFieldExpPIDOrProdSpec;
	}

	public javax.swing.JLabel getLabelAfOnSeqNumValue() {
		if (labelAfOnSeqNumValue == null) {

			labelAfOnSeqNumValue = new javax.swing.JLabel();
			labelAfOnSeqNumValue.setName("TextFieldAfOnSeqNo");
			labelAfOnSeqNumValue.setFont(new java.awt.Font("dialog", 0, viewProperty.getAfOnSeqNumFontSize()));
			labelAfOnSeqNumValue.setText("-----");
			labelAfOnSeqNumValue.setHorizontalAlignment(SwingConstants.LEFT);
			labelAfOnSeqNumValue.setBounds(viewProperty.getAfOnSeqNumX(), viewProperty.getAfOnSeqNumY(),
					viewProperty.getAfOnSeqNumWidth(), viewProperty.getAfOnSeqNumHeight());
			labelAfOnSeqNumValue.setVisible(false);
		}
		return labelAfOnSeqNumValue;
	}
	
	
	public javax.swing.JLabel getLabelProductCountValue() {
		if (labelProductCountValue == null) {

			labelProductCountValue = new javax.swing.JLabel();
			labelProductCountValue.setName("TextFieldProductCount");
			labelProductCountValue.setFont(new java.awt.Font("dialog", 0, viewProperty.getProductCountValueFontSize()));
			labelProductCountValue.setText("--/--");
			labelProductCountValue.setHorizontalAlignment(SwingConstants.LEFT);
			labelProductCountValue.setBorder(new BevelBorder(BevelBorder.LOWERED));
			labelProductCountValue.setBounds(viewProperty.getProductCountNumX(), viewProperty.getProductCountNumY(),
					viewProperty.getProductCountNumWidth(), viewProperty.getProductCountNumHeight());
			labelProductCountValue.setVisible(false);
		}
		return labelProductCountValue;
	}

	protected Rectangle gettextFieldAfOnSeqNoRefrenceBounds(){
		return getTextFieldExpPidOrProdSpec().getBounds();
	}

	protected Rectangle getTextFieldExpPidOrProdSpecRefrenceBounds() {
		return getTextFieldProdId().getBounds();
	}

	public LengthFieldBean getTextFieldSubId() {
		if (this.textFieldSubId == null) {
			this.textFieldSubId = new LengthFieldBean();
			this.textFieldSubId.setName("TextFieldSubProductId");
			this.textFieldSubId.setFont(new java.awt.Font("dialog", 0, 36));
			this.textFieldSubId.setText("");
			setTextFieldSubIdBounds();
			this.textFieldSubId.setVisible(false);

			Map<String, String> subidColorMap = viewProperty.getSubidColorMap();
			this.textFieldSubId.setRender(new SubidRender(subidColorMap));
		}
		return this.textFieldSubId;
	}

	protected void setTextFieldSubIdBounds() {
		Rectangle refbounds = getTextFieldExpPidOrProdSpec().getBounds();
		Rectangle prodBounds = getTextFieldProdId().getBounds();
		this.textFieldSubId.setBounds(refbounds.x + refbounds.width + 2*gap, refbounds.y, prodBounds.width - refbounds.width - 2*gap, refbounds.height);
	}

	public JButton getButton(int i) {
		return buttonList.get(i);
	}

	public void initTorqueValueTextFildList() {
		int x = viewProperty.getTorqueStartPositionX();//150; //133;
		int gap  = viewProperty.getTorqueFieldGap();
		int width = viewProperty.getTorqueFieldWidth();
		int height = viewProperty.getTorqueFieldHeight();
		String defaultTorque = viewProperty.getDefaultTorqueValue();
		int actualWidth = getActualWidth(width);
		JTextField textFieldTorque = null;

		int maxColumns = getMaxColumns(x, actualWidth, gap);

		for (int i = 0; i < maxNumOfTorque; i++) {
			try {
				int row = i / maxColumns;
				int ix = x + (i % maxColumns) * (actualWidth + gap);
				int iy = getTorquePositionY(row, height, gap);

				textFieldTorque = new javax.swing.JTextField();
				textFieldTorque.setName("JTextFieldTorque0" + i);
				textFieldTorque.setText(defaultTorque);
				textFieldTorque.setComponentOrientation(java.awt.ComponentOrientation.RIGHT_TO_LEFT);
				textFieldTorque.setFont(new java.awt.Font("dialog", 0, viewProperty.getTorqueFontSize()));
				textFieldTorque.setBounds(ix, iy, actualWidth, height);
				textFieldTorque.setEditable(true);
				textFieldTorque.setEnabled(true);
				textFieldTorque.setVisible(false);
				textFieldTorque.addFocusListener(new java.awt.event.FocusAdapter() {
					public void focusGained(FocusEvent e){
						if (!e.isTemporary()){
							Logger.getLogger().check(e.getComponent().getName() + " has gained focus");
						}
					}
				});
				torqueValueList.add(textFieldTorque);
			} catch (java.lang.Throwable e) {
				handleException(e);
			}
		}	
	}

	protected int getActualWidth(int width) {
		//adjust torque field width for show torque status OK/NG
		if(!viewProperty.isShowTorqueAsValue())
			return (int)(width * viewProperty.getTorqueWidthRatio());
		else 
			return width;
	}

	protected abstract int getTorquePositionY(int row, int height, int gap); 
	protected int getMaxColumns(final int x, int width, int gap) {
		return (mainWindowWidth - x)/ (width + gap);
	}

	public void initPartSerialNumberList() {
		for (int i = 0; i < maxNumOfPart; i++)
		{
			UpperCaseFieldBean partSN;
			try {
				partSN = new UpperCaseFieldBean();
				partSN.setName("UppPartSN0" + (i+1));
				partSN.setFont(new java.awt.Font("dialog", 0, viewProperty.getPartSnFontSize()));
				partSN.setColor(ViewControlUtil.VIEW_COLOR_CURRENT);
				partSN.setColumns(30);
				repositionSerialNumber(partSN, i, getClientContext().isAfOnSeqNumExist());
				partSNList.add(partSN);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
	}

	public void repositionSerialNumber(UpperCaseFieldBean partSN, int position, boolean isAfOnSeqNoExist) {
		repositionPartComponent(partSN, position, isAfOnSeqNoExist, false);		
	}

	public JButton getJButtonProductId()
	{
		if (ivjJButtonProductId == null)
		{
			try
			{
				ivjJButtonProductId = new javax.swing.JButton();
				ivjJButtonProductId.setName("JButtonProductId");
				ivjJButtonProductId.setText(viewProperty.getProductIdLabel());
				ivjJButtonProductId.setFont(new java.awt.Font("dialog", 0, 18));
				ivjJButtonProductId.setIconTextGap(40);

				ivjJButtonProductId.setBounds(viewProperty.getProductIdButtonX(), viewProperty.getProductIdButtonY(),
						viewProperty.getProductIdButtonWidth(), viewProperty.getProductIdButtonHeight());


				ivjJButtonProductId.setComponentOrientation(java.awt.ComponentOrientation.RIGHT_TO_LEFT);
				ivjJButtonProductId.setForeground(java.awt.Color.black);
			} catch (java.lang.Throwable ivjExc){
				handleException(ivjExc);
			}
		}
		return ivjJButtonProductId;
	}

	public JLabel getLabelProdId() {
		if (prodIdLabel == null) {
			prodIdLabel = new JLabel();
			prodIdLabel.setText("JLabel");
			prodIdLabel.setName("JLabelEngineSN");
			prodIdLabel.setFont(new java.awt.Font("dialog", 0, viewProperty.getProductIdLabelFontSize()));
			prodIdLabel.setIconTextGap(40);
			prodIdLabel.setBounds(viewProperty.getProductIdLabelX(), viewProperty.getProductIdLabelY(),
					viewProperty.getProductIdLabelWidth(), viewProperty.getProductIdLabelHeight());
			prodIdLabel.setText(viewProperty.getProductIdLabel());
			prodIdLabel.setComponentOrientation(java.awt.ComponentOrientation.RIGHT_TO_LEFT);
			prodIdLabel.setForeground(java.awt.Color.black);
		}
		return prodIdLabel;
	}

	public UpperCaseFieldBean getTextFieldProdId()
	{
		if (textFieldProdId == null) {
			textFieldProdId = new UpperCaseFieldBean();
			textFieldProdId.setName("JTextFieldProductID");
			textFieldProdId.setFont(new java.awt.Font("dialog", 0, viewProperty.getProductIdTextFieldFontSize()));
			textFieldProdId.setText("");
			textFieldProdId.setFixedLength(true);

			//2016-01-13 - BAK - Increase max length by 1 if Remove "I" enabled and product type is Frame			
			int maxLength = viewProperty.getMaxProductSnLength();
			if (getClientContext().isRemoveIEnabled() && getClientContext().getProperty().getProductType().equalsIgnoreCase(ProductType.FRAME.toString())) {
				maxLength = viewProperty.getMaxProductSnLength() + 1;
			}						

			textFieldProdId.setMaximumLength(maxLength);
			textFieldProdId.setColumns(maxLength);
			textFieldProdId.setBounds(viewProperty.getProductIdTextFieldX(), viewProperty.getProductIdTextFieldY(),
					viewProperty.getProductIdTextFieldWidth(), viewProperty.getProductIdTextFieldHeight());
			textFieldProdId.setSelectionColor(new Color(204, 204, 255));
			textFieldProdId.setColor(Color.blue);
			textFieldProdId.setBackground(Color.blue);
		}
		return textFieldProdId;
	}

	public UpperCaseFieldBean getPartSerialNumber(int ic) {
		UpperCaseFieldBean partSN = null;
		try {
			partSN = partSNList.get(ic);

		} catch (java.lang.Throwable e) {
			handleException(e);
		}
		return partSN;
	}

	public JLabel getPartLabel(int ic) {
		JLabel label = null;
		try {
			label = partLabelList.get(ic);

		} catch (java.lang.Throwable t) {
			handleException(t);
		}
		return label;
	}

	public JTextField getTorqueValueTextField(int ic) {
		JTextField texField = null;
		if(ic<torqueValueList.size()){
			try {
				texField = torqueValueList.get(ic);

			} catch (java.lang.Throwable t) {
				handleException(t);
			}
		}
		return texField;
	}

	/**
	 * Called whenever the part throws an exception.
	 * @param exception java.lang.Throwable
	 */
	protected void handleException(java.lang.Throwable exception) {

		/* Uncomment the following lines to print uncaught exceptions to stdout */
		Logger.getLogger().debug(exception, "-- UNCAUGHT EXCEPTION --");
	}

	public int getMaxNumPart() {
		return maxNumOfPart;
	}

	public void setMaxNumPart(int maxNumPart) {
		this.maxNumOfPart = maxNumPart;
	}

	public int getMaxNumTorq() {
		return maxNumOfTorque;
	}

	public void setMaxNumTorq(int maxNumTorq) {
		this.maxNumOfTorque = maxNumTorq;
	}

	public void requestFocusInvokeLater(final JComponent comp) {
		Runnable r = new Runnable() {
			public void run(){
				comp.requestFocus();
			}
		};
		SwingUtilities.invokeLater(r);
	}

	public void showTorque(Measurement bean, JTextField torqueValueTextField, Color color) {
		if(!viewProperty.isShowTorqueAsValue())
			ViewControlUtil.refreshObject(torqueValueTextField, bean.getMeasurementStatus().toString(), color, false);
		else
			ViewControlUtil.refreshObject(torqueValueTextField, Double.toString(bean.getMeasurementValue()), color, false);
	}

	private StatusMessagePanel getStatusMessagePanel() {
		LotControlMainBase main = (LotControlMainBase)this.getRootPane().getParent();
		return main.getStatusMessagePanel();
	}

	public void setProductInputFocused() {
		getTextFieldProdId().requestFocus();

	}

	public void preUpdate(DataCollectionState state){
		getStatusMessagePanel().setErrorMessageArea(state.getMessage() == null ? null : state.getMessage().getDescription());
	}

	public void skipProduct() {
	}

	public void displayMessageDialog(String userMessage) {
		JOptionPane.showMessageDialog(null,userMessage);
	}

	public int getMainWindowWidth() {
		return mainWindowWidth;
	}

	public void setMainWindowWidth(int mainWindowWidth) {
		this.mainWindowWidth = mainWindowWidth;
	}

	public int getMainWindowHeight() {
		return mainWindowHeight;
	}

	public void setMainWindowHeight(int mainWindowHeight) {
		this.mainWindowHeight = mainWindowHeight;
	}

	public String getExpectProductIdLabel() {
		return viewProperty.getExpectProductIdLabel();
	}

	public int getMaxPartTexLength() {
		return viewProperty.getMaxPartTextLength();
	}

	public String getProdSpecLabel() {
		return viewProperty.getProdSpecLabel();
	}

	public String getAfOnSeqNumLabel() {
		return viewProperty.getAfOnSeqNumLabel();
	}
	
	public String getProductCountLabel() {
		return viewProperty.getProductCountLabel();
	}

	public ArrayList<UpperCaseFieldBean> getPartSNList() {
		return partSNList;
	}

	@EventSubscriber(eventClass = AutoFocusMessage.class)
	public void autoFocusOnCurrentField(AutoFocusMessage status) {
		Logger.getLogger().debug(this.getClass().getSimpleName() + " received Event :" + status.getId());
		//check product Id first
		if(isCurrentField(getTextFieldProdId())){

			if(getTextFieldProdId().getBackground() == Color.red)
				ViewControlUtil.setSelection(getTextFieldProdId(), null);

			requestFocusInvokeLater(getTextFieldProdId());
			return;
		}

		//check part serial number 
		for(UpperCaseFieldBean psn : getPartSNList()){
			if(isCurrentField(psn)){

				if(psn.getBackground() == Color.red)
					ViewControlUtil.setSelection(psn, null);

				requestFocusInvokeLater(psn);
				return;
			}
		}

		//last check torque field
		for(JTextField torq : torqueValueList){
			if(isCurrentField(torq))
			{
				requestFocusInvokeLater(torq);
				return;
			}
		}
	}

	private boolean isCurrentField(JTextField c) {
		return c.isVisible() && c.isEnabled() && c.isEditable() && c.isFocusOwner() ;
	}

	public JButton getWiderButton() {
		if(widerButton == null){
			widerButton = new JButton();
			widerButton.setName("widerbutton");
			widerButton.setBounds(getButton(0).getX(), getButton(0).getY(), getButton(0).getWidth()*2, getButton(0).getHeight());
			widerButton.setFont(new Font("dialog", 0, viewProperty.getButtonFontSize()));

			widerButton.setVisible(false); 
		}
		return widerButton;
	}

	public void setWiderButton(JButton widerButton) {
		this.widerButton = widerButton;
	}

	public int getSeparatorGap() {
		return separatorGap;
	}

	public void setSeparatorGap(int separatorGap) {
		this.separatorGap = separatorGap;
	}

	protected void repositionPartComponent(Component component, int position, boolean isAfOnSeqNoExist, boolean isLabel) {
		Rectangle bounds = getTextFieldProdId().getBounds();
		int x = viewProperty.getPartStartPositionX();
		if (x < 0) x = bounds.x;
		int y = viewProperty.getPartStartPositionY();
		boolean boundY = false;
		if (y < 0) {
			y = bounds.y;
			boundY = true;
		}
		if (isLabel) {
				component.setBounds(-10, y + (position +(boundY ? 2 : 0))*(bounds.height + gap) + getSeparatorGap(), x, bounds.height);
		} else {
				component.setBounds(x, y + (position + (boundY ? 2 : 0)) * (bounds.height + gap) + getSeparatorGap(), 
						bounds.width -viewProperty.getStatusLabelWidth() - gap, bounds.height);
		}
	}

	protected void setTestTorqueButtonVisible(boolean visible){
		if(testTorqueButton == null) return ;
		testTorqueButton.setVisible(visible);
	}

	protected void setTestTorqueButtonAction(Action action){
		if(testTorqueButton == null) return;
		testTorqueButton.setAction(action);
	}

	public ArrayList<JButton> getButtonList() {
		return buttonList;
	}

	public void setButtonList(ArrayList<JButton> buttonList) {
		this.buttonList = buttonList;
	}

	public Color getInitBackgroundColor() {
		return initBackgroundColor;
	}

	public void setInitBackgroundColor(Color initBackgroundColor) {
		this.initBackgroundColor = initBackgroundColor;
	}

	public Color getInitForegroundColor() {
		return initForegroundColor;
	}

	public void setInitForegroundColor(Color initForegroundColor) {
		this.initForegroundColor = initForegroundColor;
	}
	
	protected JComponent getCurrentField() {
		if(getTextFieldProdId().isEnabled()) return getTextFieldProdId();
		for(JTextField psn : getPartSNList())
			if(psn.isEnabled()) return psn;

		for(JTextField tv : torqueValueList)
			if(tv.isEnabled()) return tv;

		for(JButton bt : getButtonList())
			if(bt.isEnabled()) return bt;

		return getTextFieldProdId();
	}
}
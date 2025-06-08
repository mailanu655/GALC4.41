package com.honda.galc.client.datacollection.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import javax.swing.plaf.metal.MetalComboBoxEditor;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.common.component.ButtonPanel;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.observer.LotControlAudioManager;
import com.honda.galc.client.datacollection.property.PartLotPropertyBean;
import com.honda.galc.client.ui.component.Constant;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.PartLotDao;
import com.honda.galc.entity.enumtype.PartLotStatus;
import com.honda.galc.entity.product.PartLot;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.CommonPartUtility;

/**
 * 
 * <h3>PartLotControlScreen</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> PartLotControlScreen description </p>
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
 * Dec 15, 2010
 *
 */

public class PartLotControlScreen extends JDialog
implements ActionListener, ListSelectionListener{
	private static final long serialVersionUID = 1L;
	private static final int SAFETY_STOCK_QUANTITY = 999;
	private JLabel panelLabel;
	private ButtonPanel buttonPanel;
	private JPanel contentPanel;
	private List<PartLot> openPartLots;
	private PartLot previousPartLot = null;
	private PartLot currentPartLot;
	private String currentPartName;
	private boolean newPartLot = false;
	private List<String> partLotSerialNumberList;
	private PartLotDao partLotDao;
	private boolean ok;
	private boolean autoSave;
	private PartLotPropertyBean propertyBean;
	
	private PartLotControlPanel partLotCtrPanel;
	private boolean remake;
	private PartLotViewManager viewMgr;
	private Boolean validPartSn = false;
	private boolean validSerialNo = false;
	
	public PartLotControlScreen(JFrame frame) {
		super(frame, true);
		initialize();
	}


	private void initialize() {
		setName(this.getClass().getSimpleName());
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setSize(560, 460);
        setTitle(this.getClass().getSimpleName());
        
		setContentPane(getContentPanel());
		
		initConnections();
		
		propertyBean = PropertyService.getPropertyBean(PartLotPropertyBean.class,
				DataCollectionController.getInstance().getClientContext().getProcessPointId());
		autoSave = propertyBean.isAutoSavePartLot();
	}

	private void initConnections() {
		getPartLotCtrPanel().getFieldSerialNo().getComponent().addActionListener(this);
		getPartLotCtrPanel().getFieldPartNo().getComponent().addActionListener(this);
		getPartLotCtrPanel().getFieldQuantity().getComponent().addActionListener(this);
		getPartLotCtrPanel().getSaftyStock().addActionListener(this);
		getButtonPanel().getOkButton().addActionListener(this);
		getButtonPanel().getCancelButton().addActionListener(this);
		
		getButtonPanel().getOkButton().getInputMap().put(KeyStroke.getKeyStroke("release SPACE"), "ok");
		getButtonPanel().getCancelButton().getInputMap().put(KeyStroke.getKeyStroke("release SPACE"), "cancel");
		getButtonPanel().getOkButton().getActionMap().put("ok", new UserAction(this));
		getButtonPanel().getOkButton().getActionMap().put("cancel",new UserAction(this));
		
		
	}

	private void initScreen(String partName, boolean isRemake) {
		
		newPartLot = false;
		validPartSn = false;
		validSerialNo = false;
		openPartLots = null;
		currentPartLot = null;
		previousPartLot = null;
		partLotSerialNumberList = null;
		
		getPartLotCtrPanel().reset();
		
		currentPartName = partName;
		getPartLotCtrPanel().getFieldPartName().getComponent().setText(partName);
		renderFieldOk(getPartLotCtrPanel().getFieldPartName().getComponent());
		
		if(isRemake)
			initFielPartSnRemake(); 
		else 
			initFieldPartSn();
	}

	private void initFielPartSnRemake() {
		openPartLots = getPartLotDao().findAllPartLotsByStatus(currentPartName, (short)PartLotStatus.SAFTYSTOCK.getId());
		
		partLotSerialNumberList = getPartLotSerialNumberList();
		initComboBoxField(getPartLotCtrPanel().getFieldSerialNo(), partLotSerialNumberList, true);
		getPartLotCtrPanel().getSaftyStock().setSelected(true);
		renderComboBoxFied(getPartLotCtrPanel().getFieldSerialNo());
		
	}

	private void renderComboBoxFied(LabeledComboBox box) {
		final Component editorComp = box.getComponent().getEditor().getEditorComponent();
		getPartLotCtrPanel().initField((JTextField)editorComp);
		MetalComboBoxEditor editor = (MetalComboBoxEditor)box.getComponent().getEditor();
		
		((JTextField)editor.getEditorComponent()).setCaretPosition(0);
		((JTextField)editor.getEditorComponent()).moveCaretPosition(editor.getItem().toString().length());
		
		editorComp.requestFocusInWindow();
	}

	private void initFieldPartSn() {
		openPartLots = getPartLotDao().findAllPartLotsByStatus(currentPartName, (short)PartLotStatus.OPEN.getId());
		currentPartLot = getPartLotDao().findInprogressPartLot(currentPartName);
		previousPartLot = currentPartLot;
		
		partLotSerialNumberList = getPartLotSerialNumberList();
		
		initComboBoxField(getPartLotCtrPanel().getFieldSerialNo(), partLotSerialNumberList, true);
		getPartLotCtrPanel().getSaftyStock().setSelected(false);
		renderComboBoxFied(getPartLotCtrPanel().getFieldSerialNo());
		
	}

	private List<String> getPartLotSerialNumberList() {
		if(partLotSerialNumberList == null || partLotSerialNumberList.size() == 0){
			partLotSerialNumberList = getOpenPartLotSerialNumbers();
			if(currentPartLot != null)
				partLotSerialNumberList.add(0, currentPartLot.getId().getPartSerialNumber());
			partLotSerialNumberList.add(0, "");
			
		}
		return partLotSerialNumberList;
	}
	

	private List<String> getOpenPartLotSerialNumbers() {
		List<String> list = new ArrayList<String>();
		for(PartLot partLot : openPartLots){
			if(!list.contains(partLot.getId().getPartSerialNumber().trim()))//list uniq part serial number
				list.add(partLot.getId().getPartSerialNumber().trim());
		}
		return list;
	}


	public JLabel getPanelLabel() {
		if(panelLabel == null){
			panelLabel = new JLabel("Part Lot Control");
			panelLabel.setPreferredSize(new Dimension(150, 45));
			panelLabel.setFont(PartLotControlPanel.FONT);
		}
		return panelLabel;
	}
	
	public JPanel getContentPanel() {
		if(contentPanel == null){
			contentPanel = new JPanel();
			contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
			contentPanel.add(getPanelLabel());
			contentPanel.add(getPartLotCtrPanel());
			contentPanel.add(getButtonPanel());
			
		}
		return contentPanel;
	}


	public ButtonPanel getButtonPanel() {
		if(buttonPanel == null)
			buttonPanel = new ButtonPanel();
		
		return buttonPanel;
	}

	public void screenOpen(String partName, boolean isRemake, PartLotViewManager manager){
		this.remake = isRemake;
		this.viewMgr = manager;
		initScreen(partName, isRemake);
		this.pack();
		
		getPartLotCtrPanel().getFieldSerialNo().getComponent().getEditor().getEditorComponent().requestFocus();
		this.setVisible(true);
		
	}

	public void actionPerformed(ActionEvent e) {
		Logger.getLogger().debug(e.paramString());
		if(e.getSource() == getPartLotCtrPanel().getFieldSerialNo().getComponent()){
			if(Constant.comboBoxChanged.equals(e.getActionCommand()))
			     processFieldSerialNoChange();
			
		} else if(e.getSource() == getPartLotCtrPanel().getFieldPartNo().getComponent()){
			if(Constant.comboBoxChanged.equals(e.getActionCommand()))
				processFieldPartSnChange();
		
		}else if(e.getSource() == getPartLotCtrPanel().getFieldQuantity().getComponent()){
			processQuantity();
			
		} else if(e.getSource() == getPartLotCtrPanel().getSaftyStock()){
			processSafetyStock();
		}
		
		
		if(e.getSource() == getButtonPanel().getOkButton()){
			
			processOkEvent();
			
			
		} else if(e.getSource() == getButtonPanel().getCancelButton()){
			ok = false;
			dispose();
		}
	}

	private void processQuantity() {
		try{
			String quantityStr = getPartLotCtrPanel().getFieldQuantity().getComponent().getText();
			
			//if(!Character.isDigit(quantityStr.charAt(0)) 
			if(!validatePartLot(quantityStr, propertyBean.getPartLotQuantityPrefix(), "Quantity")){
				
				handleInvalidQuantity();
				return;
			}
			
			int quantity = Integer.parseInt(getNumber(quantityStr));
			
			if(quantity > 0)  {
				
				renderFieldOk(getPartLotCtrPanel().getFieldQuantity().getComponent());
				currentPartLot.setStartingQuantity(quantity);
				setError(null);
				
				if(autoSave)
					processOkEvent();
				else
					getButtonPanel().getOkButton().requestFocus();
				
			} else 
				handleInvalidQuantity();
			
		} catch (Exception ex){
			handleInvalidQuantity();
			
		}
	}
	

	private String getNumber(String str) {
		for(int i = 0; i < str.length(); i++)
			if(Character.isDigit(str.charAt(i)))
				return str.substring(i);
		
		return null;
	} 

	private void handleInvalidQuantity() {
		getPartLotCtrPanel().getFieldQuantity().getComponent().setBackground(Color.red);
		getPartLotCtrPanel().getFieldQuantity().getComponent().setCaretPosition(0);
		getPartLotCtrPanel().getFieldQuantity().getComponent().moveCaretPosition(
				getPartLotCtrPanel().getFieldQuantity().getComponent().getText().length());
		getPartLotCtrPanel().getFieldQuantity().getComponent().requestFocus();
		setError("Invalid quantity.");
	}

	

	private void renderFieldOk(JTextField field) {
		field.setForeground(Color.black);
		field.setBackground(Color.green);
	}
	
	private void processSafetyStock() {
		if(getPartLotCtrPanel().getSaftyStock().isSelected()){
			getPartLotCtrPanel().getFieldQuantity().getComponent().setText("Q" + SAFETY_STOCK_QUANTITY);
			getPartLotCtrPanel().getFieldQuantity().getComponent().setBackground(Color.green);
			
		}
		
	}

	public void valueChanged(ListSelectionEvent e) {
		if(e.getValueIsAdjusting()) return;
		
	}
	
	 private void processOkEvent() {
		 
		 if(!isValidInputs()){
			 LotControlAudioManager.getInstance().playNGSound();
			 return;
		 }
			 
		 
		 ok = true;
		 if(previousPartLot != null && previousPartLot != currentPartLot){
			 previousPartLot.setStatus(previousPartLot.getCurrentQuantity() == 0 ? PartLotStatus.CLOSED : PartLotStatus.OPEN);
			 partLotDao.save(previousPartLot);
		 }
		 
		 if(currentPartLot != null){
			 
			 if (!isSafetyStock()) {
				
				 currentPartLot.setStatus(PartLotStatus.INPROGRESS);
				
				if (newPartLot)
					currentPartLot.setCurrentQuantity(currentPartLot.getStartingQuantity());
			} else {
				if(newPartLot){
					currentPartLot.setStatus(PartLotStatus.SAFTYSTOCK);
					currentPartLot.setStartingQuantity(SAFETY_STOCK_QUANTITY);
					currentPartLot.setCurrentQuantity(SAFETY_STOCK_QUANTITY);
				}
			}
			 
			currentPartLot.setUpdateTimestamp(new Timestamp(System.currentTimeMillis()));
			partLotDao.save(currentPartLot);
		 }
		 
		 dispose();
	}

	private boolean isValidInputs() {
		if(currentPartLot == null){
			BasicComboBoxEditor editor = ((BasicComboBoxEditor)getPartLotCtrPanel().getFieldSerialNo().getComponent().getEditor());
			handleComboBoxNg(editor);
			return false;
		}
		
		if(!validSerialNo){
			setError("Invalid Serial No.");
			return false;
		}
		
		if(validPartSn == null){
			setError("Please select Part No.");
			return false;
		}
		
		if(!validPartSn){
			BasicComboBoxEditor editor = ((BasicComboBoxEditor)getPartLotCtrPanel().getFieldPartNo().getComponent().getEditor());
			handleComboBoxNg(editor);
			return false;
		}
		
		if(!remake && currentPartLot.getStartingQuantity() <= 0){
			handleInvalidQuantity();
			return false;
		}
		
		return true;
		
	}


	private boolean isSafetyStock() {
		return getPartLotCtrPanel().getSaftyStock().isSelected();
	}

	private boolean processFieldSerialNoChange() {
		Logger.getLogger().debug("enter processFieldSerialNoChange.");
		BasicComboBoxEditor editor = ((BasicComboBoxEditor)getPartLotCtrPanel().getFieldSerialNo().getComponent().getEditor());
		String selected = getSelected(editor);
		getPartLotCtrPanel().resetPartNoField();
		getPartLotCtrPanel().resetDataFields();
		
		
		if(!validatePartLot(selected, propertyBean.getPartLotSnPrefix(),"Serial Number")){
			handleComboBoxNg(editor);
			validSerialNo  = false;
			return false;
		} 
		validSerialNo = true;
		renderFieldOk((JTextField)editor.getEditorComponent());
		Logger.getLogger().debug("Serial No ok - field rendered.");
		
		currentPartLot = findCurrentPartLot(selected);
		Logger.getLogger().debug("Current Part Lot:", (currentPartLot == null) ? "null" : currentPartLot.getId().getPartSerialNumber());
		if(currentPartLot == null){
			newPartLot = true;
			currentPartLot = new PartLot(selected, currentPartName);
			
			initComboBoxField(getPartLotCtrPanel().getFieldPartNo(), getPartNoList(selected), true);
			
			
		} else {
			initComboBoxField(getPartLotCtrPanel().getFieldPartNo(), getPartNoList(selected), remake);
			
			if(!remake){
				BasicComboBoxEditor pnEditor = ((BasicComboBoxEditor)getPartLotCtrPanel().getFieldPartNo().getComponent().getEditor());
				if(!validatePartSn(getPartNoList(selected).get(0))){
					handleComboBoxNg(pnEditor);
					validPartSn  = false;
				}else {
					validPartSn = true;
					renderFieldOk((JTextField)getPartLotCtrPanel().getFieldPartNo().getComponent().getEditor().getEditorComponent());
				}
				
				getPartLotCtrPanel().setComponent(getPartLotCtrPanel().getFieldQuantity().getComponent(), "" + currentPartLot.getStartingQuantity(), validPartSn);
				getPartLotCtrPanel().getFieldRemaining().setVisible(true);
				getPartLotCtrPanel().setComponent(getPartLotCtrPanel().getFieldRemaining().getComponent(), "" + currentPartLot.getCurrentQuantity(), validPartSn);
				
				if(validPartSn)
					getButtonPanel().getOkButton().requestFocus();

			} else {
				validPartSn = null;
				getPartLotCtrPanel().getFieldPartNo().getComponent().getEditor().getEditorComponent().requestFocusInWindow();
			}
			
			getPartLotCtrPanel().getSaftyStock().setSelected(currentPartLot.getStatus() == PartLotStatus.SAFTYSTOCK ? true : false);
			FieldControl(true,true,false);
		}
		
		
		return true;
		
	}


	private String getSelected(BasicComboBoxEditor pnEditor) {
		Object pnItem = pnEditor.getItem();
		String pnSelected = pnItem == null ? null : pnItem.toString();
		Logger.getLogger().debug("Selected item:", pnSelected);
		return pnSelected;
	}


	private boolean validatePartLot(String selected, String prefix, String fieldName) {
		boolean result = checkPartLot(selected, prefix);
		
		if(result)
			LotControlAudioManager.getInstance().playOkSound();
		else {
			LotControlAudioManager.getInstance().playNGSound();
			setError(fieldName + ":" + selected + " mismatch with " + prefix);
			Logger.getLogger().info(fieldName + ":" + selected + " mismatch with " + prefix);
		}
		Logger.getLogger().info(fieldName + ":" + selected + " ok.");
		return result;
		
	}


	private boolean checkPartLot(String selected, String prefix) {
		if(StringUtils.isEmpty(selected)) return false;
		if(StringUtils.isEmpty(prefix)) return true;
		
		String[] prefixes = prefix.split(",");
		for(int i = 0; i < prefixes.length; i++){
			if(selected.startsWith(prefixes[i].trim()))
				return true;
		}
		
		return false;
	}
	
	
	public void FieldControl(boolean visible, boolean enabled, boolean editable){
		getPartLotCtrPanel().getFieldPartName().getComponent().setVisible(visible);
		getPartLotCtrPanel().getFieldPartName().getComponent().setEnabled(enabled);
		getPartLotCtrPanel().getFieldPartName().getComponent().setEditable(editable);
		
		getPartLotCtrPanel().getFieldPartNo().getComponent().setVisible(visible);
		getPartLotCtrPanel().getFieldPartNo().getComponent().setEnabled(remake);
		getPartLotCtrPanel().getFieldPartNo().getComponent().setEditable(remake || editable);
		
		getPartLotCtrPanel().getFieldQuantity().getComponent().setVisible(visible);
		getPartLotCtrPanel().getFieldQuantity().getComponent().setEnabled(enabled);
		getPartLotCtrPanel().getFieldQuantity().getComponent().setEditable(editable);
		
		getPartLotCtrPanel().getFieldRemaining().getComponent().setVisible(visible);
		getPartLotCtrPanel().getFieldRemaining().getComponent().setEnabled(enabled);
		getPartLotCtrPanel().getFieldRemaining().getComponent().setEditable(editable);
	}
	
	
	private List<String> getPartNoList(String selected) {
		List<String> list = new ArrayList<String>();
		if(!remake){
			list.add(0, ((currentPartLot == null || currentPartLot.getId() == null || currentPartLot.getId().getPartNumber() == null)? 
					"" : currentPartLot.getId().getPartNumber()));

		} else {
			
			list.add("");
			for(PartLot partlot : openPartLots){
				if(selected.trim().equals(partlot.getId().getPartSerialNumber().trim()))
					list.add(partlot.getId().getPartNumber());
			}
		}
		
		if(list.get(0) == null) list.add(0, "");
		return list;
	}

	private void processFieldPartSnChange() {
		BasicComboBoxEditor editor = ((BasicComboBoxEditor)getPartLotCtrPanel().getFieldPartNo().getComponent().getEditor());
		String selected = getSelected(editor);
		
		if(!newPartLot) selected = currentPartLot.getId().getPartNumber();
		
		if(!validatePartSn(selected)){
			handleComboBoxNg(editor);
			validPartSn  = false;
			return;
		} 
		
		validPartSn = true;
		if(newPartLot)
			currentPartLot.getId().setPartNumber(selected);

		if(remake){
			getPartLotCtrPanel().getFieldQuantity().getComponent().setText("Q" + SAFETY_STOCK_QUANTITY);
			renderFieldOk(getPartLotCtrPanel().getFieldQuantity().getComponent());
			
			if(currentPartLot.getCurrentQuantity() <= 0){
				getPartLotCtrPanel().getFieldRemaining().getComponent().setBackground(Color.red);
			} else {
				getPartLotCtrPanel().getFieldRemaining().setVisible(true);
				getPartLotCtrPanel().getFieldRemaining().getComponent().setText(""+currentPartLot.getCurrentQuantity());
				renderFieldOk(getPartLotCtrPanel().getFieldRemaining().getComponent());
			}
			
			getButtonPanel().getOkButton().requestFocus();
			
		} else{
			getPartLotCtrPanel().getFieldQuantity().getComponent().setEditable(true);
			getPartLotCtrPanel().getFieldQuantity().getComponent().setEnabled(true);
			getPartLotCtrPanel().getFieldQuantity().getComponent().requestFocus();
		}
		
		renderFieldOk((JTextField)editor.getEditorComponent());
	}


	private boolean validatePartSn(String selected) {
		List<PartSpec> parts = DataCollectionController.getInstance().getState().getCurrentLotControlRulePartList();
		Iterator<PartSpec> it = parts.iterator();
		PartSpec part = null;
		List<String> masks = new ArrayList<String>();

		while(it.hasNext()){
			part = it.next();
			if(CommonPartUtility.verification(selected, part.getPartSerialNumberMask(), PropertyService.getPartMaskWildcardFormat()))
			{
				LotControlAudioManager.getInstance().playOkSound();
				setError(null);
				return true;
			}
			masks.add(part.getPartSerialNumberMask());
		}
		LotControlAudioManager.getInstance().playNGSound();
		setError("Part No.: " + selected + " mismatch with " + CommonPartUtility.parsePartMaskDisplay(masks.toString()));
		Logger.getLogger().info("Part serial number:" + selected + " verification failed. Masks:" + masks.toString());
		
		return false;
	}


	private void setError(String msg) {
		viewMgr.setErrorMessage(msg);
	}


	private void handleComboBoxNg(BasicComboBoxEditor editor) {
		((JTextField)editor.getEditorComponent()).setBackground(Color.red);
		((JTextField)editor.getEditorComponent()).setCaretPosition(0);
		((JTextField)editor.getEditorComponent()).moveCaretPosition(editor.getItem().toString().length());
		editor.getEditorComponent().requestFocus();
	}


	private PartLot findCurrentPartLot(String selected) {
		 currentPartLot = getPartLotDao().findInprogressPartLot(currentPartName);
		 
		 if(currentPartLot!= null && isSelectedPartLot(selected, currentPartLot))
			 return currentPartLot;
		 
		 for(PartLot partLot : getOpenPartLots()){
			 if(isSelectedPartLot(selected, partLot))
				 return partLot;
		 }
		 return null;
	 }

	private boolean isSelectedPartLot(String partSn, PartLot partLot) {
		return partSn.equals(partLot.getId().getPartSerialNumber());
	}

	public List<PartLot> getOpenPartLots() {
		return openPartLots;
	}

	public PartLot getCurrentPartLot() {
		return currentPartLot;
	}

	public PartLotDao getPartLotDao() {
		if(partLotDao == null)
			partLotDao = ServiceFactory.getDao(PartLotDao.class);
		
		return partLotDao;
	}
	
	

	public PartLotControlPanel getPartLotCtrPanel() {
		if(partLotCtrPanel == null)
			partLotCtrPanel = new PartLotControlPanel();
		
		return partLotCtrPanel;
	}
	
	public boolean isOk() {
		return ok;
	}

	
	private void initComboBoxField(LabeledComboBox box, List<String> list, boolean enabled) {

		DefaultComboBoxModel comboModel = new DefaultComboBoxModel(list.toArray());
		box.getComponent().setModel(comboModel);
		box.getComponent().setSelectedIndex(0);
		box.getComponent().setEnabled(enabled);
		box.getComponent().setEditable(enabled);
		box.getComponent().setFocusable(enabled);
		
		final Component editorComp = box.getComponent().getEditor().getEditorComponent();
		if(enabled) 
			renderComboBoxFied(box);
		else {
		    renderFieldOk((JTextField)editorComp);	
		}

	}

}

class UserAction extends AbstractAction{
	private static final long serialVersionUID = 1L;
	PartLotControlScreen screen;
	
	public UserAction(PartLotControlScreen screen) {
		super();
		this.screen = screen;
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == screen.getButtonPanel().getOkButton())
			screen.actionPerformed(e);
	}
}


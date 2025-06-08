package com.honda.galc.client.teamleader.reprint;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.knuckle.KnuckleLabelPrintingUtil;
import com.honda.galc.client.teamleader.property.ReprintPropertyBean;
import com.honda.galc.client.ui.ApplicationMainPanel;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LabeledNumberSpinner;
import com.honda.galc.client.utils.ViewUtil;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.dao.conf.BroadcastDestinationDao;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.SubProduct;
import com.honda.galc.service.printing.PrintingUtil;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>ReprintPanel Class description</h3>
 * <p> ReprintPanel description </p>
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
 * Apr 20, 2011
 *
 *
 */

public class ReprintPanel extends ApplicationMainPanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	
	private static final String KSN_STICKER = "KSN_Sticker";
	
	private LabeledComboBox formSelectionBox;
	
	private ProductSelectionPanel productSelectionPanel;
	
	private JPanel productSelectionContainer;
	
	private LabeledComboBox printerSelectionBox;
	
	private LabeledNumberSpinner numberOfCopiesSpinner;
	
	private JButton submitButton = new JButton("Submit");
	
	private JButton selectAllButton = new JButton("Select All");
	
	private JButton clearAllButton = new JButton("ClearAll");
	
	
	private JPanel itemListContainer;
	
	private ReprintPropertyBean reprintPropertyBean;
	
	public ReprintPanel(MainWindow window) {
		super(window);
		
		reprintPropertyBean = PropertyService.getPropertyBean(ReprintPropertyBean.class, getApplicationId());

		initComponents();
		
		mapHandlers();
		
		formSelectionBox.setModel(reprintPropertyBean.getPrintFormList(), 0);
		
	}
	
	private void initComponents() {
		
		JSplitPane splitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, 
				createUpperLeftPanel(), createItemListContainer());
		splitPanel.setDividerLocation(450);
		splitPanel.setContinuousLayout(true);

		this.setLayout(new BorderLayout(0,20));
		add(splitPanel,BorderLayout.CENTER);
		add(createButtonPanel(),BorderLayout.SOUTH);
		ViewUtil.setInsets(this, 20, 20, 20, 20);
		
	}
	
	private JPanel createUpperLeftPanel() {
		
		JPanel leftPanel = new JPanel(new BorderLayout());
		
		leftPanel.add(createFormSelectionBox(),BorderLayout.NORTH);
		leftPanel.add(createProductSelectionContainer(),BorderLayout.CENTER);
		
		JPanel tmpPanel = new JPanel();
		tmpPanel.setLayout(new BoxLayout(tmpPanel,BoxLayout.Y_AXIS));
		tmpPanel.add(createPrinterSelectionBox());
		tmpPanel.add(createNumberOfCopiesSpinner());
		
		leftPanel.add(tmpPanel,BorderLayout.SOUTH);
		
		return leftPanel;
		
	}
	
	private LabeledComboBox createFormSelectionBox() {
		formSelectionBox = new LabeledComboBox("1. Select form to Print");
		formSelectionBox.setFont(Fonts.DIALOG_BOLD_16);
		return formSelectionBox;
	}
	
	private LabeledComboBox createPrinterSelectionBox() {
		
		printerSelectionBox = new LabeledComboBox("4. Select Printer");
		printerSelectionBox.setFont(Fonts.DIALOG_BOLD_16);
		return printerSelectionBox;
		
	}
	
	private LabeledNumberSpinner createNumberOfCopiesSpinner(){
		
		numberOfCopiesSpinner = new LabeledNumberSpinner("5.Number of copies", true,1,10);
		numberOfCopiesSpinner.setFont(Fonts.DIALOG_BOLD_16);
		return numberOfCopiesSpinner;
		
	}
	

	private JPanel createProductSelectionContainer() {
		
		productSelectionContainer = new JPanel(new BorderLayout());
		
		return productSelectionContainer;
		
	}
	
	private JPanel createItemListContainer() {
		
		itemListContainer = new JPanel(new BorderLayout());
		return itemListContainer;
		
	}
	
	private JPanel createButtonPanel() {
		
		JPanel buttonPanel = new JPanel();
		submitButton.setFont(Fonts.DIALOG_BOLD_20);
		selectAllButton.setFont(Fonts.DIALOG_BOLD_20);
		clearAllButton.setFont(Fonts.DIALOG_BOLD_20);
		buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.X_AXIS));
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(submitButton);
		buttonPanel.add(Box.createHorizontalStrut(10));
		buttonPanel.add(selectAllButton);
		buttonPanel.add(Box.createHorizontalStrut(10));
		buttonPanel.add(clearAllButton);
		return buttonPanel;
		
	}
	
	
	private void mapHandlers() {
		formSelectionBox.getComponent().addActionListener(this);
		submitButton.addActionListener(this);
		selectAllButton.addActionListener(this);
		clearAllButton.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		
		clearErrorMessage();
		
		try{
			if(e.getSource()== formSelectionBox.getComponent()) {
				formSelected();
			}else if(e.getSource()== submitButton) {
				submitSelected();
			}else if(e.getSource()== selectAllButton) {
				selectAllSelected();
			}else if(e.getSource()== clearAllButton) {
				clearAllSelected();
			}
		}catch(Exception ex) {
			handleException(ex);
		}
		
	}

	private void formSelected() {
		
		String formId = (String)formSelectionBox.getComponent().getSelectedItem();
		if(StringUtils.isEmpty(formId)) return;
		
		productSelectionContainer.removeAll();
		productSelectionPanel = createProductSelectionPanel(formId);
		productSelectionPanel.setReprintPropertyBean(reprintPropertyBean);
		productSelectionContainer.add(productSelectionPanel,BorderLayout.CENTER);
		
		printerSelectionBox.setModel(getPrinterList(formId), 0);
		
		printerSelectionBox.getComponent().setEnabled(
				printerSelectionBox.getComponent().getItemCount() > 1);
		
		numberOfCopiesSpinner.getComponent().setEnabled(allowMultipleCopies(formId));
		
	}
	
	private String[] getPrinterList(String formId) {
		
		
		List<BroadcastDestination> destinations = getDao(BroadcastDestinationDao.class).findAllByReqestId(formId);
		
		
		
		List<String> printers = new ArrayList<String>();
		for(BroadcastDestination item : destinations) {
			if(!printers.contains(item.getDestinationId()))
				printers.add(item.getDestinationId());
		}
		
		return printers.toArray(new String[]{});
		
	}
	
	private boolean allowMultipleCopies(String formId) {
		
		String[] formIds = reprintPropertyBean.getAllowMultipleCopies();
		for(String item : formIds) {
			if(StringUtils.equals(formId, item)) return true;
		}
		return false;
	
	}
	
	@SuppressWarnings("unchecked")
	private void submitSelected() {
		
		List<? extends BaseProduct> products = productSelectionPanel.getSelectedProducts();
		
		if(products.isEmpty()){
			setErrorMessage("Please select products");
			return;
		}
		
		String formId = (String)formSelectionBox.getComponent().getSelectedItem();
		String printerId = StringUtils.trim((String)printerSelectionBox.getComponent().getSelectedItem());
		if(StringUtils.isEmpty(formId)){
			setErrorMessage("Please select a form");
			return;
		}
		
		if(StringUtils.isEmpty(printerId)){
			setErrorMessage("No printer selected");
			return;
		}
		
		if(KSN_STICKER.equals(formId))
			new KnuckleLabelPrintingUtil().print((List<SubProduct>)products, true,"Primary".equalsIgnoreCase(printerId));
		
		else  {
			getLogger().info("start to print." + " Form Id : " + formId + " Printer : " + printerId);
			PrintingUtil printUtil = new PrintingUtil(printerId,formId);
			printUtil.print(products);
		}
		
	}	
	
	private void selectAllSelected() {
		productSelectionPanel.selectAllProducts();
	}
	
	private void clearAllSelected() {
		productSelectionPanel.clearAllProductSections();
	}
	
	private ProductSelectionPanel createProductSelectionPanel(String formId) {
		
		Map<String,String> panelMap = reprintPropertyBean.getPanelMap();
		String panelClassName = null;
		
		if(panelMap!= null) panelClassName = panelMap.get(formId);
		if(StringUtils.isEmpty(panelClassName)) panelClassName = reprintPropertyBean.getDefaultProductSelectionPanel();
		
		try{
			Class<?>[] constructParamCls = new Class[]{JPanel.class};
			
			Object[] constructParamObj = new Object[]{itemListContainer};
			
			Class<?> panelClass = Class.forName(panelClassName.trim());
			Constructor<?> panelClassConstructor = panelClass.getConstructor(constructParamCls);
			
			return (ProductSelectionPanel)panelClassConstructor.newInstance(constructParamObj);

		} catch (Exception e) {
			e.printStackTrace();
			Throwable throwable = e;
			if(e instanceof InvocationTargetException) throwable = e.getCause();
			String message = "Unable to create form reprint panel due to " + throwable.toString();
			throw new SystemException(message,throwable);
		} 
	}

}

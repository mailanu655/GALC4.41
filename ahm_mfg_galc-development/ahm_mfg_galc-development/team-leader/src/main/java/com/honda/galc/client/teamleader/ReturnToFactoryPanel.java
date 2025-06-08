package com.honda.galc.client.teamleader;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableCellRenderer;

import org.apache.commons.lang.StringUtils;

import net.miginfocom.swing.MigLayout;

import com.honda.galc.client.product.view.UiFactory;
import com.honda.galc.client.teamleader.hold.HoldUtils;
import com.honda.galc.client.teamleader.hold.config.QsrMaintenancePropertyBean;
import com.honda.galc.client.teamleader.hold.qsr.put.dialog.ReturnToFactoryDialog;
import com.honda.galc.client.teamleader.property.HoldReleasePropertyBean;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.ui.component.ManualProductEntryDialog;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.PropertiesMapping;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.dao.product.PurchaseContractDao;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.HoldResultType;
import com.honda.galc.entity.enumtype.QsrStatus;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.entity.product.HoldResult;
import com.honda.galc.entity.product.HoldResultId;
import com.honda.galc.entity.product.ProductResult;
import com.honda.galc.entity.product.PurchaseContract;
import com.honda.galc.entity.product.Qsr;
import com.honda.galc.report.TableReport;
import com.honda.galc.service.ReturnToFactoryService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.ExtensionFileFilter;
import com.honda.galc.util.ProductHoldUtil;

public class ReturnToFactoryPanel extends TabbedPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	private Map<String,Frame> vehicleMap;
	private ObjectTablePane<Frame> vehicleTablePane;
	private LabeledTextField countPanel;
	private ProcessPoint productReturnProcPoint;
	private ArrayList<String> returnToFactoryHistProcs;
	private JPanel manualProdEntryPanel;
	private JButton prodFindButton;
	private JTextField prodEntryField;
	private JPanel importFilePanel;
	private JButton browseButton;
	private String parentFolder;
	private JTextField fileNameField;
	private JButton uploadButton;
	private HoldReleasePropertyBean propertyBean = PropertyService.getPropertyBean(HoldReleasePropertyBean.class, getApplicationId());
	private JPopupMenu popupMenu;
	private FrameDao frameDao;
	private ProductResultDao productResultDao;
	private PurchaseContractDao purchaseContractDao;
	private Division division;
	private ArrayList<PurchaseContract> purchaseContracts;
	private HashMap<String, HashMap<String, ArrayList<String>>> pcYmtocProdMap;
	private enum ProductState{OK, NG};
	
	public ReturnToFactoryPanel(TabbedMainWindow mainWindow){
		super("Return to Factory", KeyEvent.VK_Z, mainWindow);
	}
	
	@Override
	public void onTabSelected() {
		try {
			if (isInitialized)	return;
			this.initComponents();
			this.loadData();
			this.addListeners();
			this.mapActions();
			isInitialized = true;
		} catch (Exception e) {
			getLogger().error(e, "Exception to start ReturnToFactory panel.");
			setErrorMessage("Exception to start ReturnToFactory panel." + e.toString());
		}	
	}
	
	private void initComponents() {
		this.setLayout(new MigLayout());
		this.add(this.getManualProdEntryPanel());
		this.add(this.getImportFilePanel(),"gapleft 20, wrap");
		this.add(this.getVehicleTable(),"span, wrap");
		this.add(this.getCountPanel(),"width 170:170:170, wrap");
	}
	
	private void loadData() {
		this.getMainWindow().clearMessage();
		this.vehicleMap = null;
		if (this.getProductReturnProcPoint() == null) {
			getLogger().error("PRODUCT_RETURN_PROC property is invalid or not set for \"" + this.getApplicationId() + "\".");
			this.setErrorMessage("PRODUCT_RETURN_PROC property is invalid or not set for \"" + this.getApplicationId() + "\".");
			return;
		} else if (this.getReturnToFactoryHistProcs() == null){
			return;
		} else {
			this.vehicleMap = this.getVehicles();
			this.resetPanel();
		}
	}
	
	private void resetPanel() {
		ArrayList<Frame> vehicleList = new ArrayList<Frame>();
		vehicleList.addAll(this.vehicleMap.values());
		this.vehicleTablePane.reloadData(vehicleList);
		this.vehicleTablePane.clearSelection();
		this.setSelectCount();
	}
	
	private JPanel getManualProdEntryPanel() {
		if (this.manualProdEntryPanel == null) {
			this.manualProdEntryPanel = new JPanel(new MigLayout());
			this.manualProdEntryPanel.add(this.getProdFindButton(),"aligny bottom");
			this.manualProdEntryPanel.add(this.getProdEntryField(),"aligny bottom, width 250:300:300");
		}
		return this.manualProdEntryPanel;
	}
	
	private JButton getProdFindButton() {
		if (this.prodFindButton == null) {
			this.prodFindButton = this.createButton("VIN", true);
			this.prodFindButton.setMnemonic(KeyEvent.VK_V);
		}
		return this.prodFindButton;
	}
	
	private JTextField getProdEntryField() {
		if (this.prodEntryField == null) {
			this.prodEntryField = UiFactory.getInput().createTextField(TextFieldState.EDIT);
			this.prodEntryField.setFont(Fonts.DIALOG_BOLD_16);
			this.prodEntryField.setHorizontalAlignment(JTextField.CENTER);
			this.prodEntryField.setPreferredSize(new Dimension(250,32));
			this.prodEntryField.setMargin(new Insets(0,0,0,0));
			this.prodEntryField.setName("ProductEntryField");
		}
		return this.prodEntryField;
	}
	
	public JPanel getImportFilePanel() {
		if (this.importFilePanel == null) {
			this.importFilePanel = new JPanel(new MigLayout());
			this.importFilePanel.add(this.createFileLabel());
			this.importFilePanel.add(this.getFileNameField(),"aligny bottom, width 250:350:350");
			this.importFilePanel.add(this.getBrowseButton(),"aligny bottom");
			this.importFilePanel.add(this.getUploadButton(),"aligny bottom");
		}
		return this.importFilePanel;
	}
	
	protected JLabel createFileLabel() {
		JLabel element = new JLabel("File", JLabel.RIGHT);
		element.setFont(Fonts.DIALOG_BOLD_16);
		return element;
	}

	public JTextField getFileNameField() {
		if (this.fileNameField == null) {
			this.fileNameField = UiFactory.getInput().createTextField(TextFieldState.DISABLED);
			this.fileNameField.setFont(Fonts.DIALOG_PLAIN_16);
			this.fileNameField.setDisabledTextColor(Color.BLACK);
			this.fileNameField.setPreferredSize(new Dimension(250,32));
			this.fileNameField.setMargin(new Insets(0,0,0,0));
			this.fileNameField.setEditable(false);
		}
		return this.fileNameField;
	}

	public JButton getBrowseButton() {
		if (this.browseButton == null) {
			this.browseButton = this.createButton("Browse", true);
			this.browseButton.setMnemonic(KeyEvent.VK_B);
		}
		return this.browseButton;
	}

	public JButton getUploadButton() {
		if (this.uploadButton == null) {
			this.uploadButton = this.createButton("Upload", false);
			this.uploadButton.setMnemonic(KeyEvent.VK_U);
		}
		return this.uploadButton;
	}
	
	private JButton createButton(String title, Boolean enabled) {
		JButton button = new JButton(title);
		String name = (title + "Button").replaceAll("\\s","");
		button.setName(name);
		button.setEnabled(enabled);
		button.setFont(Fonts.DIALOG_BOLD_16);
		return button;
	}
	
	private ObjectTablePane<Frame> getVehicleTable() {
		if (this.vehicleTablePane == null) {
			PropertiesMapping mapping = new PropertiesMapping();
			mapping.put("Product ID", "productId");
			mapping.put("Engine SN", "engineSerialNo");
			mapping.put("Short Vin", "shortVin");
			mapping.put("YMTOC", "productSpecCode");
			mapping.put("Prod Lot", "productionLot");
			mapping.put("KD Lot", "kdLotNumber");
			mapping.put("Last Process", "lastPassingProcessPointId");
			mapping.put("Purchase Contract No", "purchaseContractNumber");
			mapping.put("Last Process Timestamp", "updateTimestamp");
			this.vehicleTablePane = new ObjectTablePane<Frame>(mapping.get(), true, true);
			this.vehicleTablePane.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			this.vehicleTablePane.getTable().setName("VehicleTable");
			this.vehicleTablePane.getTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			for (FocusListener focusListener : this.vehicleTablePane.getTable().getFocusListeners()) {
				this.vehicleTablePane.getTable().removeFocusListener(focusListener);
			}
			this.vehicleTablePane.setBorder(new TitledBorder("Shipped Vehicles"));
			this.vehicleTablePane.setPreferredSize(new Dimension(9999,9999));
			this.vehicleTablePane.getTable().setDefaultRenderer(Object.class, new CellRenderer());
		}
		return this.vehicleTablePane;
	}
	
	private LabeledTextField getCountPanel() {
		if (this.countPanel == null) {
			this.countPanel = new LabeledTextField("Rows selected:");
			this.countPanel.getComponent().setAlignmentX(RIGHT_ALIGNMENT);
			this.countPanel.getComponent().setBorder(null);
			this.countPanel.getComponent().setEditable(false);
			this.countPanel.getLabel().setFont(Fonts.DIALOG_BOLD_14);
			this.countPanel.getComponent().setFont(Fonts.DIALOG_PLAIN_14);
			this.countPanel.setBorder(null);
		}
		return this.countPanel;
	}
	
	private JPopupMenu getPopupMenu() {
		if (this.popupMenu == null) {
			this.popupMenu = new JPopupMenu();
			this.popupMenu.add("Return vehicle(s) to factory").addActionListener(this);
			this.popupMenu.add(new JPopupMenu.Separator());
			this.popupMenu.add("Refresh table").addActionListener(this);
			this.popupMenu.add("Export to file").addActionListener(this);
		}
		return this.popupMenu;
	}
	
	private void addListeners() {
		((TablePane)this.vehicleTablePane).getTable().getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e){
				getMainWindow().clearMessage();
				if (e.getValueIsAdjusting()) return;
				List<Frame> selectedItems = vehicleTablePane.getSelectedItems();
				if (selectedItems.size() == 1)
					getProdEntryField().setText(selectedItems.get(0).getId());
				else
					getProdEntryField().setText("");
				setSelectCount();
            }
		});
		this.prodFindButton.addActionListener(this);
		this.prodEntryField.addActionListener(this);
		this.browseButton.addActionListener(this);
		this.uploadButton.addActionListener(this);
	}
	
	protected void mapActions() {
		MouseListener mouseListener = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				showPopupMenu(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				showPopupMenu(e);
			}

			protected void showPopupMenu(MouseEvent e) {
				if (e.isPopupTrigger()) {
					getMainWindow().clearMessage();
					boolean isSelected = !getVehicleTable().getSelectedItems().isEmpty();
					getPopupMenu().getSubElements()[0].getComponent().setEnabled(isSelected);
					getPopupMenu().getSubElements()[1].getComponent().setEnabled(true);
					getPopupMenu().getSubElements()[2].getComponent().setEnabled(isSelected);
					getPopupMenu().show(e.getComponent(), e.getX(), e.getY());
				}
			}
		};

		this.getVehicleTable().getTable().addMouseListener(mouseListener);
	}

	public void actionPerformed(ActionEvent e) {
		this.getMainWindow().clearMessage();
		if (e.getSource().equals(this.prodFindButton)) this.findVehicle();
		else if (e.getSource().equals(this.prodEntryField)) this.processVehicle();
		else if (e.getSource().equals(this.browseButton)) this.browse();
		else if (e.getSource().equals(this.uploadButton)) this.upload();
		else if (e.getSource().equals(this.getPopupMenu().getSubElements()[0].getComponent())) this.returnVehicles();
		else if (e.getSource().equals(this.getPopupMenu().getSubElements()[1].getComponent())) this.loadData();
		else if (e.getSource().equals(this.getPopupMenu().getSubElements()[2].getComponent())) this.exportRecords();
	}
	
	public void browse() {
		String path = StringUtils.isEmpty(getInputFolderPath()) ? path = "" : getInputFolderPath().trim();
		JFileChooser fc = new JFileChooser(path);
	
		FileFilter filter = new ExtensionFileFilter("csv", "txt");
		fc.setFileFilter(filter);
	
		fc.setAcceptAllFileFilterUsed(false);
		int returnVal = fc.showOpenDialog(this);
	
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			this.getFileNameField().setText(fc.getSelectedFile().getAbsolutePath());
			setInputFolderPath(fc.getSelectedFile().getParentFile().getAbsolutePath());
			this.getUploadButton().setEnabled(true);
		} else {
			return;
		}
	}
	
	private void upload() {
		this.clearSelection();

		List<String> inputNumbers = readInputNumbers(this.getFileNameField().getText());
		this.setSelected(inputNumbers, true);
	}
	
	private void clearSelection() {
		this.getVehicleTable().clearSelection();
	}
	
	protected List<String> readInputNumbers(String fileName) {

		List<String> inputNumbers = new ArrayList<String>();
		File file = new File(fileName);

		FileInputStream fstream = null;
		DataInputStream in = null;
		try {
			fstream = new FileInputStream(file);

			in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				strLine = strLine.trim();
				if (strLine.length() > 0) {
					inputNumbers.add(strLine);
				}
			}
		} catch (Exception e1) {
			getLogger().error(e1);
			throw new RuntimeException(e1);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e1) {
			}
		}
		return inputNumbers;
	}
	
	public String getInputFolderPath() {
		return this.parentFolder;
	}

	public void setInputFolderPath(String parentFolder) {
		this.parentFolder = parentFolder;
	}
	
	private void setSelectCount() {
		int selectedRows = this.getVehicleTable().getTable().getSelectedRowCount();
		int totalRows = this.getVehicleTable().getTable().getRowCount();
		this.countPanel.getComponent().setText("" + selectedRows + " of " + totalRows);
	}
	
	private Map<String,Frame> getVehicles(){
		if (this.vehicleMap == null) {
			this.vehicleMap = new HashMap<String, Frame>();
			String procPoint = this.getProductReturnProcPoint().getProcessPointId();
			List<Frame> vehicles = this.getFrameDao().findAllByLastPassingProcessPointId(procPoint);
			
			for (Frame vehicle : vehicles) {
				String productId = vehicle.getId();
				Timestamp last_proc_ts = getLastProcessPointTime(productId, procPoint);
				vehicle.setUpdateTimestamp(last_proc_ts);
				
				this.vehicleMap.put(vehicle.getProductId(), vehicle);
			}
			
		}
		return vehicleMap;
	}
	
	private Timestamp getLastProcessPointTime(String productId, String procPoint) {
		//get the latest create timestamp from product history table for the frame and process point
		List<ProductResult> productHistory = this.getProductResultDao().findAllByProductAndProcessPoint(productId, procPoint);
		Timestamp process_point_ts = productHistory.get(0).getCreateTimestamp();
		for (ProductResult p : productHistory) {
			if (p.getCreateTimestamp().compareTo(process_point_ts) > 0) {
				process_point_ts = p.getCreateTimestamp();
			}
		}
		return process_point_ts;
	}
	
	public ProcessPoint getProductReturnProcPoint(){
		if (this.productReturnProcPoint == null) {
			if (this.propertyBean.getProductReturnProc() instanceof String) {
				String procPointId = propertyBean.getProductReturnProc();
				this.productReturnProcPoint = ServiceFactory.getDao(ProcessPointDao.class).findByKey(procPointId);
			}
		}
		return this.productReturnProcPoint;
	}
	
	public ArrayList<String> getReturnToFactoryHistProcs(){
		if (this.returnToFactoryHistProcs == null) {
			this.returnToFactoryHistProcs = new ArrayList<String>();
			if(this.propertyBean == null || this.propertyBean.getReturnToFactoryHist() == null || this.propertyBean.getReturnToFactoryHist().length == 0) {
				this.setErrorMessage("Propery RETURN_TO_FACTORY_HIST is not set.");
				return null;
			} else {
				String[] returnToFactoryHist = this.propertyBean.getReturnToFactoryHist();
				for (String procPointId : returnToFactoryHist) {
					ProcessPoint procPoint = ServiceFactory.getDao(ProcessPointDao.class).findByKey(procPointId);
					if (procPoint == null) {
						this.setErrorMessage(procPointId + "in RETURN_TO_FACTORY_HIST is not a valid process point.");
						return null;
					} else if (procPoint.getPassingCountFlag() == 0) {
						this.setErrorMessage("Tracking disabled for process point " + procPointId +".");
						return null;
					}
					this.returnToFactoryHistProcs.add(procPointId);
				}
			}
		}
		return this.returnToFactoryHistProcs;
	}
	
	private void findVehicle() {
		ArrayList<String> selectedProducts = new ArrayList<String>();
		ProductType productType = ProductType.FRAME;
		ManualProductEntryDialog manualProductEntry = new ManualProductEntryDialog(getMainWindow(), productType.toString(), "Manual Product Entry");
		manualProductEntry.setModal(true);
		manualProductEntry.setVisible(true);
		String selectedProduct = manualProductEntry.getResultProductId();
		selectedProducts.add(selectedProduct);
		if (StringUtils.isBlank(selectedProduct)) return;
		this.prodEntryField.requestFocus();
		this.setSelected(selectedProducts, false);
	}
	
	private void processVehicle() {
		ArrayList<String> productIdList = new ArrayList<String>(); 
		productIdList.add(this.prodEntryField.getText());
		if (!productIdList.isEmpty()) this.setSelected(productIdList, false);
	}
	
	private void setSelected(List<String> productIdList, Boolean displayAccepted) {
		if (productIdList.isEmpty()) return;
		ArrayList<String> acceptedVins = new ArrayList<String>();
		ArrayList<String> rejectedVins = new ArrayList<String>();
		ArrayList<String> notFoundVins = new ArrayList<String>();
		JTable table = this.getVehicleTable().getTable();
		for (String productId : productIdList) {
			if (StringUtils.isEmpty(productId)) continue;
			if (!ProductNumberDef.VIN.isNumberValid(productId)) {
				rejectedVins.add(productId);
			} else {
				Boolean notFound = true;
				for (int i = 0; i < this.getVehicleTable().getTable().getRowCount(); i++) {
					if (productId.equals(table.getModel().getValueAt(i,0).toString())) {
						table.addRowSelectionInterval(i, i);
						acceptedVins.add(productId);
						notFound = false;
						continue;
					}
				}
				if (notFound) notFoundVins.add(productId);
			}
		}
		if (!displayAccepted) acceptedVins = new ArrayList<String>();
		this.createReport(acceptedVins, rejectedVins, notFoundVins);
	}
	
	private void createReport(List<String> acceptedVins, List<String> rejectedVins, List<String> notFoundVins) {
		StringBuilder sb = new StringBuilder();
		if (!acceptedVins.isEmpty()) {
			sb.append("\nVINs found:\n");
			for (String acceptedVin : acceptedVins) {
				sb.append(acceptedVin + "\n");
			}
		}
		if (!notFoundVins.isEmpty()) {
			sb.append("\nVINs not found:\n");
			for (String notFoundVin : notFoundVins) {
				sb.append(notFoundVin + "\n");
			}
		}
		if (!rejectedVins.isEmpty()) {
			sb.append("\nVINs rejected:\n");
			for (String rejectedVin : rejectedVins) {
				sb.append(rejectedVin + "\n");
			}
		}
		if (sb.length() > 0 ) this.displayOkDialog("Information", sb.toString());
	}
	
	
	private void displayOkDialog(String title, String message) {
		JOptionPane.showMessageDialog(this, getScrollPane(message), title, JOptionPane.INFORMATION_MESSAGE);
	}
	
	private Integer displayOkCancelDialog(String message) {
		return JOptionPane.showConfirmDialog(this, getScrollPane(message), "Information", JOptionPane.OK_CANCEL_OPTION);
	}
	
	private JScrollPane getScrollPane(String message) {
		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setText(message);
		textArea.setCaretPosition(0);
		JScrollPane scrollPane = new JScrollPane(textArea);	
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(new Dimension(350, 200));
		return scrollPane;
	}
	
	private void returnVehicles() {
		List<Frame> selectedProducts = getVehicleTable().getSelectedItems();
		this.getLogger().info("User " + getUserName() + " attempting to return " + selectedProducts.size() + " VIN(s) to factory - "
				+ selectedProducts.stream().map(Frame::getProductId).collect(Collectors.toList()));
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		final ReturnToFactoryDialog dialog = new ReturnToFactoryDialog(this, "Hold and Return", selectedProducts);
		dialog.setLocationRelativeTo(null);
		dialog.setModal(true);
		dialog.setVisible(true);
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		if (!dialog.getCompleteStatus()) {
			this.getLogger().info("User " + getUserName() + " cancelled transaction.");
			return;
		}
		int retCode = JOptionPane.showConfirmDialog(this, "Are you sure you want return " + this.getSelectedRecords().size() + " product(s)?",
				"Return to Factory", JOptionPane.YES_NO_OPTION);
		if (retCode != JOptionPane.YES_OPTION) {
			this.loadData();
			this.getVehicleTable().setSelectedItems(selectedProducts);
			this.getLogger().info("User " + getUserName() + " cancelled transaction.");
			return;
		}
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		Qsr qsr = null;
		if (dialog.getAddToExisting()) qsr = (Qsr) dialog.getQsr();
		else qsr = assembleQsr(dialog.getRespDpt(), dialog.getReason());
		
		Map<String,List<Frame>> checkedProducts = this.checkProductState(selectedProducts);
		List<Frame> validProducts = checkedProducts.get(ProductState.OK.toString());
		if (!validProducts.isEmpty()) {
			StringBuffer sb = new StringBuffer("Preparing returns for " + validProducts.size() + " product(s):");
			for (Frame validProduct : validProducts) {
				sb.append(	"\nVIN: " + validProduct.getProductId() +
							", Sequence #: " + validProduct.getAfOnSequenceNumber() +
							", YMTOC: " +  validProduct.getProductSpecCode() + 
							", Purchase Contract #: " + validProduct.getPurchaseContractNumber());
			}
			getLogger().info(sb.toString());
			
			List<HoldResult> holdResults = assembleHoldResults(dialog, division, validProducts);
			
			if (!this.updatePurchaseContracts(validProducts)) {
				getLogger().info("Transaction cancelled. Failed to update purchase contracts.");
				this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				return;
			}
			
			for (Frame selectedProduct : validProducts)	selectedProduct.setPurchaseContractNumber(null);
			
			try {
				getLogger().info("Processing returns.");
				ReturnToFactoryService service = ServiceFactory.getService(ReturnToFactoryService.class);
				service.processRetrun(validProducts, purchaseContracts, holdResults, qsr);
			} catch (Exception e) {
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				String errorMsg = "Transaction failed. All changes rolled back. Please try again.\n" + e.getStackTrace().toString();
				getLogger().error(errorMsg);
				setErrorMessage(errorMsg);
				return;
			}
			
			for (Frame selectedProduct : validProducts) {
				for (String procPointId : this.getReturnToFactoryHistProcs()) {
					try{
						getLogger().info("TrackingService called for VIN " + selectedProduct.getProductId() + 
								" and process point " + procPointId + ".");
						ServiceFactory.getService(TrackingService.class).track(selectedProduct, procPointId); 
					} catch (Exception e) {
						getLogger().info("Failed to track VIN " + selectedProduct.getProductId() + 
								" at process point " + procPointId + ".");
						continue;
					}
				}
			}
		}
		
		if (!checkedProducts.get(ProductState.NG.toString()).isEmpty()) {
			List<Frame> skippedProducts = checkedProducts.get(ProductState.NG.toString());
			StringBuilder message = new StringBuilder();
			message.append("The following product(s) were not processed:\n");
			for (Frame skippedProduct : skippedProducts) {
				message.append("\n" + skippedProduct.getId() + " - no longer in \"" + 
				this.getProductReturnProcPoint().getProcessPointName() + "\"");
			}
			getLogger().warn(message.toString());
			this.displayOkDialog("Warning", message.toString());
		}
		this.loadData();
		
		String msg = validProducts.size() + " vehicle(s) returned to factory by user " + getUserName() + 
				" and put on \"At Shipping\" hold.";
		getLogger().info(msg);
		this.setMessage(msg);
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
	private Map<String,List<Frame>> checkProductState(List<Frame> productList) {
		Map<String,List<Frame>> productStateMap = new HashMap<String,List<Frame>>();
		productStateMap.put(ProductState.OK.toString(), new ArrayList<Frame>());
		productStateMap.put(ProductState.NG.toString(), new ArrayList<Frame>());
		
		if (productList == null ||  productList.isEmpty()) return productStateMap;
		
		Map<String, Frame> productMap = productList.stream().collect(Collectors.toMap(Frame::getId, frame -> frame));
		List<String> productIdList = new ArrayList<String>(productMap.keySet());
		Map<String, Frame> updatedFrameMap = 
				this.getFrameDao().findProducts(productIdList, 0, productIdList.size()).stream().collect(
						Collectors.toMap(Frame::getId, frame -> frame)
						);
		for (String productId : productIdList) {
			if (updatedFrameMap.containsKey(productId)) {
				String lastProcPoint = updatedFrameMap.get(productId).getLastPassingProcessPointId();
				if (lastProcPoint.trim().equalsIgnoreCase(propertyBean.getProductReturnProc().trim())) {
					List<Frame> validProducts = productStateMap.get(ProductState.OK.toString());
					validProducts.add(productMap.get(productId));
					productStateMap.put(ProductState.OK.toString(), validProducts);
					continue;
				}
			} 
			List<Frame> invalidProducts = productStateMap.get(ProductState.NG.toString());
			invalidProducts.add(productMap.get(productId));
			productStateMap.put(ProductState.NG.toString(), invalidProducts);
		}
		return productStateMap;
	}
	
	public String getUserName() {
		return super.getUserName();
	}
	
	private void exportRecords() {
		String fileName = "ReturnToFactory_"+ new SimpleDateFormat("yyyy-MM-dd-HHmmss").format(new java.util.Date()) + ".xlsx";
		File file = HoldUtils.popupSaveDialog(this, System.getProperty("user.home"), fileName);
		if (file == null || file.getAbsolutePath() == null)	return;
		this.createProductReport().export(file.getAbsolutePath());
	}
	
	private TableReport createProductReport() {
		TableReport report = TableReport.createXlsxTableReport();
		report.setTitle("Products: ");
		report.addColumn("#", "#", 1000);
		report.addColumn("Product ID", "Product ID", 5500);
		report.addColumn("Engine SN", "Engine SN", 4000);
		report.addColumn("Short VIN", "Short VIN", 3000);
		report.addColumn("YMTOC", "YMTOC", 6000);
		report.addColumn("Prod Lot", "Prod Lot", 6000);
		report.addColumn("KD Lot", "KD Lot", 6000);
		report.addColumn("Last Process", "Last Process", 3000);
		report.addColumn("Last Process Timestamp", "Last Process Timestamp", 4000);
		report.addColumn("Purchase Contract No", "Purchase Contract No", 6000);
		List<Map<String, String>> rows = new ArrayList<Map<String, String>>();
		int idx = 0;
		for (Map<String,String> row : this.getSelectedRecords()) {
			idx++;
			row.put("#", Integer.toString(idx));
			rows.add(row);
		}
		report.setData(rows);
		return report;
	}
	
	private List<Map<String,String>> getSelectedRecords(){
		ArrayList<Map<String,String>> selectedRecords = new ArrayList<Map<String,String>>();
		JTable table = this.getVehicleTable().getTable();
		int[] selectedIdxList = table.getSelectedRows();
		for (int selectedIdx : selectedIdxList) {
			HashMap<String,String> selectedRecord= new HashMap<String,String>();
			Object value;
			selectedRecord.put("Product ID",					table.getValueAt(selectedIdx, 0).toString());
			selectedRecord.put("Engine SN",						(value = table.getValueAt(selectedIdx, 1)) == null ? "" : value.toString());
			selectedRecord.put("Short VIN",						(value = table.getValueAt(selectedIdx, 2)) == null ? "" : value.toString());
			selectedRecord.put("YMTOC",							(value = table.getValueAt(selectedIdx, 3)) == null ? "" : value.toString());
			selectedRecord.put("Prod Lot",						(value = table.getValueAt(selectedIdx, 4)) == null ? "" : value.toString());
			selectedRecord.put("KD Lot",						(value = table.getValueAt(selectedIdx, 5)) == null ? "" : value.toString());
			selectedRecord.put("Last Process",					(value = table.getValueAt(selectedIdx, 6)) == null ? "" : value.toString());
			selectedRecord.put("Last Process Timestamp",		(value = table.getValueAt(selectedIdx, 7)) == null ? "" : value.toString());
			selectedRecord.put("Purchase Contract No",			(value = table.getValueAt(selectedIdx, 8)) == null ? "" : value.toString());
			selectedRecords.add(selectedRecord);
		}
		return selectedRecords;
	}
	
	protected List<HoldResult> assembleHoldResults(ReturnToFactoryDialog dialog, Division division, List<Frame> productList) {
		List<HoldResult> holdResults = new ArrayList<HoldResult>();
		Date productionDate = null;
		
		for (Frame product : productList) {
			HoldResult holdResult = new HoldResult();
			HoldResultId holdResultId = new HoldResultId();
			
			holdResultId.setProductId(product.getProductId());
			holdResultId.setActualTimestamp(new Timestamp(System.currentTimeMillis()));
			holdResultId.setHoldType(HoldResultType.HOLD_AT_SHIPPING.getId());
			holdResult.setId(holdResultId);
			holdResult.setReleaseFlag((short)0);
			holdResult.setReleasePermission(PropertyService.getPropertyBean(QsrMaintenancePropertyBean.class, this.getApplicationId()).getHoldBy());
			holdResult.setHoldAssociateName(dialog.getName());
			holdResult.setHoldReason((String) dialog.getReason());
			holdResult.setHoldAssociateNo(dialog.getId());
			holdResult.setHoldAssociateName(dialog.getName());
			holdResult.setHoldAssociatePhone(dialog.getPhone());
			holdResult.setProductionLot(product.getProductionLot());
			holdResult.setProductSpecCode(product.getProductSpecCode());
			holdResult.setProductionDate(productionDate);
			holdResults.add(holdResult);
		}
		return holdResults;
	}
	
	protected Qsr assembleQsr(String departmentId, String holdReason) {
		Qsr qsr = new Qsr();
		qsr.setProcessLocation(productReturnProcPoint.getDivisionId());
		qsr.setProductType(ProductType.FRAME.name());
		qsr.setDescription(holdReason);
		qsr.setStatus(QsrStatus.ACTIVE.getIntValue());
		qsr.setHoldAccessType(ProductHoldUtil.getDefaultHoldAccessType(this.getApplicationId()));
		qsr.setResponsibleDepartment(departmentId);
		qsr.setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
		return qsr;
	}
	
	private Boolean updatePurchaseContracts(List<Frame> products) {
		StringBuilder sb = new StringBuilder();
		new HashMap<String,ArrayList<String>>();
		this.pcYmtocProdMap = new HashMap<String, HashMap<String, ArrayList<String>>>();
		this.purchaseContracts = new ArrayList<PurchaseContract>();
		String missingValueVins = "";
		String missingRecordVins = "";
		String invShipUnitsVins = "";
		for (Frame product : products) {
			FrameSpec prodSpec = ServiceFactory.getDao(FrameSpecDao.class).findByKey(product.getProductSpecCode());
			if (prodSpec == null) {
				String errorMsg = "Frame spec code " + product.getProductSpecCode() + " does not exist.";
				getLogger().error(errorMsg);
				this.getMainWindow().setErrorMessage(errorMsg);
				return false;
			}
			
			String pcNo = product.getPurchaseContractNumber();
			if (StringUtils.isBlank(pcNo)){
				getLogger().warn("Product " + product.getId() + " missing purchase contract number");
				missingValueVins = missingValueVins.concat(product.getId() + "\n");
				continue;
			}
			this.addToPcYmtocProdMap(product);
		}
		ArrayList<String> rejectedPcRecords = updateValidPcRecords();
		if (!rejectedPcRecords.isEmpty()) {
			sb.append("\nReturned units exceed shipped for the following purchase Contracts:\n");
			sb.append(rejectedPcRecords);
		}
		if (!StringUtils.isEmpty(missingValueVins)) {
			sb.append("\nPurchase contract number missing for the following VINs:\n");
			sb.append(missingValueVins);
		}
		if (!StringUtils.isEmpty(missingRecordVins)) {
			sb.append("\nNo valid purchase contract records found for the following VINs:\n");
			sb.append(missingRecordVins);
		}
		if (!StringUtils.isEmpty(invShipUnitsVins)) {
			sb.append("\nShip unit values can't be decremented for purchase contracts\n"
					+ "associated with the following VINs:\n");
			sb.append(invShipUnitsVins);
		}
		
		if (sb.length() != 0) {
			getLogger().warn(sb.toString());
			sb.append("\nWould you like to continue returning selected vehicles?\n"
					+ "You will need to adjust purchase contract values manually.");
			if (this.displayOkCancelDialog(sb.toString()) == JOptionPane.CANCEL_OPTION) {
				getLogger().info("User " + getUserName() + " cancelled transaction.");
				this.purchaseContracts = new ArrayList<PurchaseContract>();
				return false;
			}
		}
		return true;
	}
	
	private void addToPcYmtocProdMap(Frame product) {
		HashMap<String, ArrayList<String>> ymtocProdMap = new HashMap<String, ArrayList<String>>();
		ArrayList<String> prodIdList = new ArrayList<String>();	
		if (this.pcYmtocProdMap.containsKey(product.getPurchaseContractNumber())) {
			ymtocProdMap = pcYmtocProdMap.get(product.getPurchaseContractNumber());
			if (ymtocProdMap.containsKey(product.getProductSpecCode())) {
				prodIdList = ymtocProdMap.get(product.getProductSpecCode());
			}
			prodIdList.add(product.getId());
			ymtocProdMap.put(product.getProductSpecCode(),prodIdList);
		} else {
			prodIdList.add(product.getId());
			ymtocProdMap.put(product.getProductSpecCode(),prodIdList);
			this.pcYmtocProdMap.put(product.getPurchaseContractNumber(), ymtocProdMap);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ArrayList<String> updateValidPcRecords() {
		ArrayList<String> rejectedPcNoList = new ArrayList<String>();
		for (Map.Entry pcYmtoc : this.pcYmtocProdMap.entrySet()) {
			String pcNo = (String)pcYmtoc.getKey();
			HashMap<String, ArrayList<String>> ymtocProdMap = (HashMap<String, ArrayList<String>>)pcYmtoc.getValue();
			for (Map.Entry ymtocProd : ymtocProdMap.entrySet()) {
				FrameSpec frameSpec = ServiceFactory.getDao(FrameSpecDao.class).findByKey((String)ymtocProd.getKey());
				ArrayList<String> prodIds = (ArrayList<String>)ymtocProd.getValue();
				ArrayList<PurchaseContract> pcRecords = this.getPurchaseContracts(pcNo, frameSpec);
				int shipCount = 0;
				for (PurchaseContract pcRecord : pcRecords) {
					shipCount = shipCount + pcRecord.getShipUnit();
				}
				if (shipCount < prodIds.size()) {
					rejectedPcNoList.add(pcNo);
				} else {
					this.updatePcRecords(pcRecords, prodIds.size());
				}
			}
		}
		return rejectedPcNoList;
	}
	
	private void updatePcRecords(ArrayList<PurchaseContract> pcRecords, int returnCount) {
		for (int i = pcRecords.size()-1; i >= 0; i--) {
			if (returnCount ==  0) return;
			PurchaseContract pcRecord = pcRecords.get(i);
			String originalShipCount = pcRecord.getShipUnit() + "";
			if (pcRecord.getShipUnit() <= returnCount) {
				returnCount = returnCount-pcRecord.getShipUnit();
				pcRecord.setShipUnit(0);
			} else {
				pcRecord.setShipUnit(pcRecord.getShipUnit()-returnCount);
				returnCount = 0;
			}
			this.purchaseContracts.add(pcRecord);
			this.getLogger().info("Purchase contract record " + pcRecord.getPurchaseContractId() + 
					" for number "  + pcRecord.getPurchaseContractNumber() + " updated." +
					" Ship unit count decreased from " + originalShipCount + " to " + pcRecord.getShipUnit() + ".");
		}
	}
	
	private ArrayList<PurchaseContract> getPurchaseContracts(String pcNo, FrameSpec prodSpec) {
		List<PurchaseContract> pContracts = 
			this.getPurchaseContractDao().findAllBySalesMtc(prodSpec.getSalesModelCode(), prodSpec.getSalesModelTypeCode(), prodSpec.getSalesExtColorCode());
		
		ArrayList<PurchaseContract> filteredList = new ArrayList<PurchaseContract>();
		for (PurchaseContract purchaseContract : pContracts) {
			if (purchaseContract.getPurchaseContractNumber().equals(pcNo) &&
				purchaseContract.getShipUnit() > 0) {
				filteredList.add(purchaseContract);
			}
		}
		return filteredList;
	}
	
	private PurchaseContractDao getPurchaseContractDao() {
		if (this.purchaseContractDao == null)
			this.purchaseContractDao = ServiceFactory.getDao(PurchaseContractDao.class);
		return this.purchaseContractDao;
	}
	
	private FrameDao getFrameDao() {
		if (this.frameDao == null)
			this.frameDao = ServiceFactory.getDao(FrameDao.class);
		return this.frameDao;
	}
	
	private ProductResultDao getProductResultDao() {
		if (this.productResultDao == null) {
			this.productResultDao = ServiceFactory.getDao(ProductResultDao.class);
		}
		return this.productResultDao;
	}
	
	public Division getDivision() {
		if (this.division == null) {
			String divisionId = this.getProductReturnProcPoint().getDivisionId();
			this.division = ServiceFactory.getDao(DivisionDao.class).findByDivisionId(divisionId);
		}
		return this.division;
	}
	
	public class CellRenderer extends DefaultTableCellRenderer {    
		private static final long serialVersionUID = 1L;
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			String cell = (String)table.getValueAt(row, 7);
			if(StringUtils.isBlank(cell)) {       
				if (isSelected)	this.setBackground(new Color(250, 215, 160));
				else this.setBackground(new Color(252, 243, 207));
				this.setForeground(table.getForeground());
			} else if (isSelected) {
	            this.setBackground(table.getSelectionBackground());
	            this.setForeground(table.getSelectionForeground());
			} else {
				this.setBackground(table.getBackground());
				this.setForeground(table.getForeground());
			}
			return this;
		}
	}
}
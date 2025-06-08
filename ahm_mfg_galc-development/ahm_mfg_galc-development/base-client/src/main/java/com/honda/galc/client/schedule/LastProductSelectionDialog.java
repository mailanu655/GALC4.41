package com.honda.galc.client.schedule;

import static com.honda.galc.service.ServiceFactory.getService;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientMain;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.ei.EiDevice;
import com.honda.galc.enumtype.LoginStatus;
import com.honda.galc.client.ui.ApplicationMainPanel;
import com.honda.galc.client.ui.LoginDialog;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.MultiValueObject;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.ProductStampingSequenceDao;
import com.honda.galc.dao.product.SubProductDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.ProductTypeCatalog;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductStampingSequence;
import com.honda.galc.entity.product.SubProduct;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.on.WeldOnService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.SortedArrayList;

/**
 * 
 * <h3>LastLotSelectionScreen</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> LastLotSelectionScreen description </p>
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
 * <TR> 
 * <TD>P.Chou</TD>
 * <TD>Feb 6, 2013</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD>
 * </TR>
 * 
 * </TABLE>
 * 
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Feb 6, 2013
 */
public class LastProductSelectionDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = -644844823470723064L;
	private ObjectTablePane<MultiValueObject<?>> productTablePanel;
	private JPanel contentPanel;
	private JPanel buttonPanel;
	private ProductType productType;
	private JButton cancelPlcButton;
	private JButton sentToPlcButton;
	private JButton sendToOnButton;
	private ScheduleClientProperty property;

	private Logger logger;

	public LastProductSelectionDialog(JFrame frame, ScheduleClientProperty property) {
		super(frame, true);
		this.property = property;
		initialize();
	}

	private void initialize() {
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setSize(560, 460);
		setTitle("Last Lot Selection");
		setLocationRelativeTo(getOwner());
		productType = ProductTypeCatalog.getProductType(property.getProductType());
		setContentPane(getContentPanel());
		getSendToPlcButton().addActionListener(this);
		getCancelPlcButton().addActionListener(this);
		getSendToOnButton().addActionListener(this);
	}

	private void loadProductData(List<MultiValueObject<PreProductionLot>> selectedItems) {
		getProductTablePanel().reloadData(prepareProductData(selectedItems));
		getProductTablePanel().clearSelection();

	}

	@SuppressWarnings("unchecked")
	private List<MultiValueObject<?>> prepareProductData(List<MultiValueObject<PreProductionLot>> selectedItems) {
		List<MultiValueObject<?>> productIdData = new ArrayList<MultiValueObject<?>>();
		for (MultiValueObject<PreProductionLot> lot : selectedItems) {

			List<? extends BaseProduct> allInLot = 
				ProductTypeUtil.getProductDao(productType).findAllByProductionLot(lot.getKeyObject().getProductionLot());

			productIdData.addAll((productType == ProductType.KNUCKLE) ? 
				sortKnuckles((List<SubProduct>)allInLot) : sortProducts((List<BaseProduct>)allInLot));
		}

		return productIdData;
	}

	private List<MultiValueObject<?>> sortKnuckles(List<SubProduct> allInLot) {
		List<String> leftKnuckles = new ArrayList<String>();
		List<String> rightKnuckles = new ArrayList<String>();
		List<MultiValueObject<?>> sortedList = new ArrayList<MultiValueObject<?>>();

		for (SubProduct subProd : allInLot) {
			if (subProd.getSubId().equals(SubProduct.SUB_ID_LEFT))
				leftKnuckles.add(subProd.getProductId());
			else
				rightKnuckles.add(subProd.getProductId());
		}

		Collections.sort(leftKnuckles);
		Collections.sort(rightKnuckles);

		int count = Math.max(leftKnuckles.size(), rightKnuckles.size());
		for (int i = 0; i < count; i++) {
			try {
				String left = leftKnuckles.get(i);
				String right = rightKnuckles.get(i);
				sortedList.add(new MultiValueObject<BaseProduct>(StringUtils.isEmpty(left)? "" : left, 
						StringUtils.isEmpty(right)? "" : right));
			} catch (Exception e) {
				getLogger().error("Exception to find Knuckle on index:" + i);
			}
		}
		return sortedList;
	}

	private List<MultiValueObject<?>> sortProducts(List<BaseProduct> allInLot) {
		if (allInLot != null && !allInLot.isEmpty()) {
			List<ProductStampingSequence> productStampingSequences = getProductStampingSequencesForLot(allInLot);
			if (productStampingSequences != null)
				return sortProductsByStampingSequenceNumber(productStampingSequences);
			else
				return sortProductsByProductId(allInLot);
		}
		return new ArrayList<MultiValueObject<?>>();
	}

	private List<MultiValueObject<?>> sortProductsByStampingSequenceNumber(List<ProductStampingSequence> productStampingSequences) {
		List<MultiValueObject<?>> sortedList = new ArrayList<MultiValueObject<?>>();
		Collections.sort(productStampingSequences, new Comparator<ProductStampingSequence>() {
			@Override
			public int compare(ProductStampingSequence productStampingSequence1, ProductStampingSequence productStampingSequence2) {
				return (productStampingSequence1.getStampingSequenceNumber() - productStampingSequence2.getStampingSequenceNumber());
			}
		});

		for (ProductStampingSequence productStampingSequence : productStampingSequences)
			sortedList.add(new MultiValueObject<String>(productStampingSequence.getId().getProductID()));

		return sortedList;
	}

	private List<ProductStampingSequence> getProductStampingSequencesForLot(List<BaseProduct> allInLot) {
		List<ProductStampingSequence> productStampingSequences = new ArrayList<ProductStampingSequence>();
		ProductStampingSequenceDao productStampingSequenceDao = ServiceFactory.getDao(ProductStampingSequenceDao.class);
		for (BaseProduct product : allInLot) {
			ProductStampingSequence productStampingSequence = productStampingSequenceDao.findById(product.getProductionLot(),product.getProductId());
			if (productStampingSequence == null || productStampingSequence.getStampingSequenceNumber() == null) return null; // if a product does not have a stamping sequence number, return null (note that STAMPING_SEQUENCE_NO in GAL216TBX is nullable)
			productStampingSequences.add(productStampingSequence);
		}
		return productStampingSequences;
	}

	private List<MultiValueObject<?>> sortProductsByProductId(List<BaseProduct> allInLot) {
		List<MultiValueObject<?>> sortedList = new ArrayList<MultiValueObject<?>>();
		SortedArrayList<BaseProduct> prodList = new SortedArrayList<BaseProduct>("getProductId");
		prodList.addAll(allInLot);

		for (BaseProduct prod : prodList)
			sortedList.add(new MultiValueObject<String>(prod.getProductId()));

		return sortedList;
	}

	public JPanel getContentPanel() {
		if (contentPanel == null) {
			contentPanel = new JPanel();
			contentPanel.setLayout(new BorderLayout());
			contentPanel.add(getProductTablePanel(), BorderLayout.CENTER);
			contentPanel.add(getButtonPanel(), BorderLayout.SOUTH);
		}
		return contentPanel;
	}

	private Component getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			if (ProductType.FRAME.equals(getProductType())) {
				buttonPanel.add(getSendToOnButton());
			}
			buttonPanel.add(getSendToPlcButton());
			buttonPanel.add(getCancelPlcButton());
		}
		return buttonPanel;
	}

	public JButton getCancelPlcButton() {
		if (cancelPlcButton == null) {
			cancelPlcButton = new JButton("Cancel");
		}
		return cancelPlcButton;
	}

	private JButton getSendToPlcButton() {
		if (sentToPlcButton == null) {
			sentToPlcButton = new JButton("Send to PLC");
		}

		return sentToPlcButton;
	}

	public ObjectTablePane<MultiValueObject<?>> getProductTablePanel() {
		if (productTablePanel == null) {
			String[] heading =  productType == ProductType.KNUCKLE ? 
					new String[]{"Left Knuckle","Right Knuckle"} : new String[]{"Product Id"};
			ColumnMappings columnMappings = ColumnMappings.with(heading);
			productTablePanel = new ObjectTablePane<MultiValueObject<?>>("", columnMappings.get());
			productTablePanel.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			productTablePanel.getTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					if (getSendToOnButton() != null) {
						getSendToOnButton().setEnabled(productTablePanel.getTable().getSelectedRowCount() > 0);
					}
				}
			});
		}
		return productTablePanel;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getSendToPlcButton()) {
			if (!login()) {
				return;
			}
			Logger.getLogger().info("User:" + ClientMain.getInstance().getAccessControlManager().getUserName() + " logged in to write:", getProductTablePanel().getSelectedItem().getValues().toString(), " to PLC.");
			if (getProductTablePanel().getSelectedItem() == null) {
				getLogger().warn("No Product seleted, can not sent to PLC.");
				return;
			}
			LastProductSelectionDialog.sendToPlc((JFrame) this.getOwner(), property.getLastLotDeviceId(), property.getProductType(), getProductTablePanel().getSelectedItem().getValues());
		} else if (e.getSource() == getSendToOnButton()) {
			if (!login()) {
				return;
			}
			Logger.getLogger().info("User:" + ClientMain.getInstance().getAccessControlManager().getUserName() + " logged in to send ", getProductTablePanel().getSelectedItem().getValues().toString(), " to ON process point.");
			sendToOn();
		} else if (e.getSource() == getCancelPlcButton()) {
			this.dispose();
		}
	}

	private boolean login() {
		if(LoginDialog.login() != LoginStatus.OK) return false;

		if (!ClientMain.getInstance().getAccessControlManager().isAuthorized(property.getAuthorizationGroup())) {
			JOptionPane.showMessageDialog(null, "You have no access permission to execute this action.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	public static void sendToPlc(JFrame mainWindow, String deviceId, List<MultiValueObject<BaseProduct>> products) {
		if (products == null || products.isEmpty()) return;

		BaseProduct product = products.get(0).getKeyObject();

		if (product.getProductType().equals(ProductType.KNUCKLE)) {
			if (products.size() < 2) {
				MessageDialog.showError(mainWindow, "Please select both left and right knuckles");
				return;
			} else {
				SubProduct product1 = (SubProduct) products.get(0).getKeyObject();
				SubProduct product2 = (SubProduct) products.get(1).getKeyObject();
				String productIdLeft = null;
				String productIdRight = null;
				if(product1.getSubId().equals(SubProduct.SUB_ID_LEFT)) productIdLeft = product1.getProductId();
				else productIdRight = product1.getProductId();
				if(product2.getSubId().equals(SubProduct.SUB_ID_RIGHT)) productIdRight = product2.getProductId();
				else productIdLeft = product2.getProductId();
				if (productIdLeft == null || productIdRight == null) {
					MessageDialog.showError(mainWindow, "Please select both left and right knuckles");
					return;
				}
				sendToPlc(mainWindow,deviceId,ProductType.KNUCKLE.getProductName(), 
						Arrays.asList(new Object[]{productIdLeft,productIdRight}));
			}
		} else
			sendToPlc(mainWindow,deviceId,product.getProductType().getProductName(), 
					Arrays.asList(new Object[]{product.getProductId()}));

	}

	public static void sendToPlc(JFrame mainWindow, String deviceId, String productTypeName, List<Object> productIds) {
		EiDevice eiDevice = DeviceManager.getInstance().getEiDevice();
		if (eiDevice == null) {
			Logger.getLogger().info("Failed to write Last Product to PLC because EI Device is not configured.");
			MessageDialog.showError(mainWindow, "Failed to write Last Product to PLC because EI Device is not configured.");
			return;
		}

		if (StringUtils.isEmpty(deviceId)) {
			MessageDialog.showError(mainWindow, "Error: configuration error - LAST_LOT_DEVICE_ID is not defined.");
			return;
		}

		boolean confirm = MessageDialog.confirm(mainWindow, getLotWriteInfo(productTypeName, productIds));
		if(!confirm) return;

		try {
			eiDevice.syncSend(deviceId, createData(deviceId, productIds));
			Logger.getLogger().info("Sent product ids " + productIds + " to PLC successfully");
			MessageDialog.showInfo(mainWindow, "Sent product ids " + productIds + " to PLC successfully");
		} catch (Exception e) {
			Logger.getLogger().error(e, "Failed to write to PLC:" + getLotWriteInfo(productTypeName, productIds));
			MessageDialog.showError(mainWindow, "Failed to write Last Product to PLC :" + e.toString());
		}
	}

	private static String getLotWriteInfo(String productTypeName, List<Object> productIds) {
		boolean isPair = productIds.size() >= 2;
		StringBuilder sb = new StringBuilder();
		sb.append("The ").append(productTypeName);
		sb.append(isPair ? " Pair " : " ");
		sb.append(productIds.get(0));
		if(isPair) sb.append(" & ").append(productIds.get(1));
		sb.append(isPair? " will be sent to the PLC as the last pair processed.":
			" will be sent to the PLC as the last product processed.");
		return sb.toString();
	}

	protected void sendToOn() {
		MultiValueObject<?> item = getProductTablePanel().getSelectedItem();
		if (item == null || item.getValues() == null || item.getValues().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please make a selection", "Send to ON", JOptionPane.WARNING_MESSAGE);
			return;
		}
		List<Object> values = item.getValues();
		String productId = (String) values.get(0);
		String onProcessPointId = getProperty().getOnProcessPoint();
		if (StringUtils.isBlank(onProcessPointId)) {
			JOptionPane.showMessageDialog(this, "ON Process Point is not defined.", "Send to ON", JOptionPane.WARNING_MESSAGE);
			return;
		}

		BaseProduct product = ProductTypeUtil.getProductDao(getProductType()).findByKey(productId);
		if (product == null) {
			String msg = String.format("Product %s does not exist for %s", getProductType(), productId);
			JOptionPane.showMessageDialog(this, msg, "Send to ON", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		String msg = "Are you sure you want to send product to ON ?";
		List<?> historyList = ProductTypeUtil.getProductHistoryDao(getProductType()).findAllByProductAndProcessPoint(productId, onProcessPointId);
		if (historyList != null && historyList.size() > 0) {
			msg = "Product has already ON record. Are you sure you want to reprocess it ?";
		} 

		int retCode = JOptionPane.showConfirmDialog(this, msg, "Send to ON", JOptionPane.YES_NO_OPTION);
		if (retCode != JOptionPane.YES_OPTION) {
			return;
		}
		
		try {
			WeldOnService onService = getService(WeldOnService.class);
			onService.processProduct(product, onProcessPointId);
		} catch (Exception e) {
			Logger.getLogger().error(e, "Failed to send product", productId, " to ON ", onProcessPointId);
			MessageDialog.showError(this, "Failed to send product to ON, " + e.toString());
		}
		refresh();
	}

	private static DataContainer createData(String deviceId, List<Object> productIds) {
		DataContainer data = new DefaultDataContainer();
		if (productIds.size() == 1) {
			data.put(TagNames.PRODUCT_ID.name(), productIds.get(0));
		} else {
			data.put(SubProduct.SUB_ID_LEFT + Delimiter.DOT + TagNames.PRODUCT_ID.name(), productIds.get(0));
			data.put(SubProduct.SUB_ID_RIGHT + Delimiter.DOT + TagNames.PRODUCT_ID.name(), productIds.get(1));
		}
		data.setClientID(deviceId);
		Logger.getLogger().info("write last product to ", deviceId + " : " + data.toString());

		return data;
	}

	public SubProductDao getSubProductDao() {
		return ServiceFactory.getDao(SubProductDao.class);
	}

	public void open(List<MultiValueObject<PreProductionLot>> selectedItems) {
		loadProductData(selectedItems);
		this.pack();
		this.setVisible(true);

	}

	private Logger getLogger() {
		if (logger == null)
			logger = Logger.getLogger();
		return logger;
	}

	protected JButton getSendToOnButton() {
		if (sendToOnButton == null) {
			sendToOnButton = new JButton("Send to On");
			sendToOnButton.setEnabled(false);
		}
		return sendToOnButton;
	}

	protected ProductType getProductType() {
		return productType;
	}

	protected ScheduleClientProperty getProperty() {
		return property;
	}
	
	protected void refresh() {
		Window window = getOwner();
		if (window instanceof ScheduleWindow) {
			ScheduleWindow frame = (ScheduleWindow) window;
			ApplicationMainPanel panel = frame.getPanel();
			((ScheduleMainPanel)panel).getController().refreshLots();
		}
	}
}

package com.honda.galc.client.collector;

import java.awt.Color;
import java.awt.Component;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventTopicSubscriber;

import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.LineSideContainerValue;
import com.honda.galc.data.TagNames;
import com.honda.galc.dto.ProductCheckDto;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.net.Request;
import com.honda.galc.notification.service.IProductPassedNotification;
import com.honda.galc.service.ServiceFactory;

public class QualityAssuranceCollectorClientController extends ProductStateCollectorClientController implements IProductPassedNotification, ListSelectionListener {
    private Device device;
	private int index, size;
   	private boolean autoCleanUpEnabled;
   	private String[] productIds;
   	private InstalledPartStatus[] productStatus;

	public QualityAssuranceCollectorClientController() {
		super();
	}
	
	public QualityAssuranceCollectorClientController(ProductStateCollectorMain collectorMain) {
		super(collectorMain);
	}
	
	void init(){
		super.init();
		getMainScreen().getCollectorPanel().getProductStatePane().getTable().getSelectionModel().addListSelectionListener(this);
		AnnotationProcessor.process(this);
	
		size = getProperty().isAutoCleanUpEnabled();
		autoCleanUpEnabled = size > 0;
		if (autoCleanUpEnabled) {
			productIds = new String[size];
			productStatus = new InstalledPartStatus[size];
		}
	}
	
	private void autoCleanUp(String productId, InstalledPartStatus status) {
		if (!isProductAlreadyOnAutoUpdateList(productId)) {
			if (productStatus[index] == InstalledPartStatus.NG) {
				ListIterator<ProductCheckDto> iterator = productCheckList.listIterator(productCheckList.size());
				while (iterator.hasPrevious()) {
					if(iterator.previous().getProductId() == productIds[index]) {
						iterator.remove();
						break;
					}
				}
			}
			productStatus[index] = status;
			productIds[index] = productId;
			index = (index+1) % size;		
		}
	}

	protected void productIdReceived(DataContainer dc) {
		try {
			String productId = getProductIdField().getText();
			reCheckProductList();
			
			result = invokeDataCollectionService(prepareDc(dc, productId));
			InstalledPartStatus currentStatus = getResultStatus(result);
			if (currentStatus == InstalledPartStatus.NG) {
				playNgAlarm(true);
			} else {
				removeProductOk(productId);
			}
			renderProductStateCheckResult(productId, result.get(TagNames.DATA_COLLECTION_COMPLETE.name()));
			if (autoCleanUpEnabled) autoCleanUp(productId, getResultStatus(result));
			if (productCheckList.isEmpty() && currentStatus == InstalledPartStatus.OK) playNgAlarm(false);

		} catch (Exception e) {
			getProductIdField().setStatus(false);
			getMainScreen().getStatusMessagePanel().setErrorMessageArea("Exception " + getExceptionMessage(e));
			getLogger().error(e, " Exception on invoking data collection service.");
			return;
		}
		processServiceResponse();
		
	}
		
	protected ProductCheckDto setProductStateCheckDto(Map<Object, Object> resultproductCheck, String productId) {
		ProductCheckDto productCheckDto = new ProductCheckDto();
		String status = getProductCheckStatus(resultproductCheck);
		productCheckDto.setProductId(productId);
		productCheckDto.setStatus(status);
		productCheckDto.setReason(getReasonForProductStateCheck(resultproductCheck));
		productCheckDto.setDate(getCurrentDate());
		productCheckDto.setAfOnSeqNo(getAfOnSeqNo(productId));
		showStatus(status);
		return productCheckDto;
	}
		
	private String getAfOnSeqNo (String productId) {
		FrameDao frameDao = ServiceFactory.getDao(FrameDao.class);
		Frame frame = frameDao.findByKey(productId);
		if (frame == null || frame.getAfOnSequenceNumber() == null)  return "";
		return frame.getAfOnSequenceNumber().toString();
	}
	
	private void productIdReceivedFromNotification(String productId) {
		getProductIdField().setText(productId);
		productIdReceived(getClientDevice().toRequestDataContainer());
	}

	protected synchronized void renderProductStateCheckResult(final String productId,final Object object) {
		Runnable r = new Runnable() {
			public void run()
			{

				doResultGUIUpdate(productId, object);
			}
		};

		SwingUtilities.invokeLater(r);
	}

	private void doResultGUIUpdate(String productId, Object status) {
		ProductCheckDto resultDto = setProductStateCheckDto(result, productId);
		if (!isProductAlreadyOnList(productId) && getResultStatus(result) == InstalledPartStatus.NG && 
				LineSideContainerValue.COMPLETE.equals(status)) {
			productCheckList.add(0, resultDto);
			lastNgProduct = productId;
		}
		
		if (productCheckList.size() > getProperty().getMaxNumberOfRows()) 
			productCheckList.remove(getProperty().getMaxNumberOfRows());
		
		getMainScreen().getCollectorPanel().getSplitPane().setVisible(true);
		getMainScreen().getCollectorPanel().getProductStatePane().reloadData(productCheckList);
		getMainScreen().getCollectorPanel().getProductStatePane().getTable().setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			public Component getTableCellRendererComponent(JTable table,Object value,boolean isSelected, boolean hasFocus,int row,int column) {
				Component c = super.getTableCellRendererComponent(table,value, isSelected,hasFocus,row,column);
				String status = value != null ? (String)value : "";
				if (status.equals(InstalledPartStatus.NG.name())) 
					c.setForeground(Color.red);	
				else 
					c.setForeground(Color.black);
				
				return c;
			}
		});
	}

	private void reCheckProductList() {
		if (!productCheckList.isEmpty()) {
			
			Iterator<ProductCheckDto> iterator = productCheckList.iterator();
			while (iterator.hasNext()) {
				String productId = iterator.next().getProductId();
				result = invokeDataCollectionService(prepareDc(productId));
				if(getResultStatus(result) == InstalledPartStatus.OK)
					iterator.remove();
			}
			
			reloadProductCheckList();
		}
	}

	private void reloadProductCheckList() {
		Runnable r = new Runnable() {
			public void run()
			{
				getMainScreen().getCollectorPanel().getSplitPane().setVisible(true);
				getMainScreen().getCollectorPanel().getProductStatePane().reloadData(productCheckList);
			}
		};

		SwingUtilities.invokeLater(r);
	}
	
	private InstalledPartStatus getResultStatus(Map<Object, Object> result) {
		for(Object key : result.keySet()) {
			if(result.get(key).toString().equalsIgnoreCase("false"))
				return InstalledPartStatus.NG;
		}
		return InstalledPartStatus.OK;
	}

	private void productIdReceivedFromSelection(String productId) {
		getProductIdField().setText(productId);
		getClientDevice().getDeviceFormat(TagNames.PRODUCT_ID.name()).setValue(productId);
		
		try {
			
			result = invokeDataCollectionService(prepareDc(productId));
			if (getResultStatus(result) == InstalledPartStatus.OK) removeProductOk(productId);
			
			renderProductStateCheckResult(productId,result.get(TagNames.DATA_COLLECTION_COMPLETE.name()));
		} catch (Exception e) {
			getProductIdField().setStatus(false);
			getMainScreen().getStatusMessagePanel().setErrorMessageArea("Exception " + getExceptionMessage(e));
			getLogger().error(e, " Exception on invoking data collection service.");
			return;
		}
		processServiceResponse();
	}
	
	private boolean isProductAlreadyOnList(String productId) {
		boolean match = false;
		for (ProductCheckDto prod : productCheckList) {
			if (prod.getProductId().equals(productId)) {
				match = true;
				break;
			}
		}
		return match;
	}

	private boolean isProductAlreadyOnAutoUpdateList(String productId) {
		boolean match = false;
		for (String s : productIds) {
			if (productId.equals(s)) {
				match = true;
				break;
			}
		}
		return match;
	}
  
	private void removeProductOk(String productId) {
		Iterator<ProductCheckDto> iterator = productCheckList.iterator();
		while (iterator.hasNext()) {
			if (iterator.next().getProductId().equals(productId)) {
				iterator.remove();
				break;
			}
		}
	}
	
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting())
			return;
		ProductCheckDto selectedItem = getMainScreen().getCollectorPanel().getProductStatePane().getSelectedItem();
				
		playNgAlarm(false);
		resetScreen();
				
		if(selectedItem != null)
		    productIdReceivedFromSelection(selectedItem.getProductId());
		else
			clearResultPanel();
		
		lastNgProduct = null;
	}
	
	private void clearResultPanel() {
		getMainScreen().getCollectorPanel().getResultPane().setText("");
		getMainScreen().getCollectorPanel().getResultPane().setCaretPosition(0);
	}

	protected ProductStateCollectorMain getMainScreen() {
		return (ProductStateCollectorMain) this.main;
	}
	
	public void execute(String processPointId, String productId) {
		// Received Product ID from IProductPassedNotification notification service
		resetScreen();
		productIdReceivedFromNotification(productId);
	}

	@EventTopicSubscriber(topic="IProductPassedNotification")
	public void onProductPassedEvent(String event, Request request) {
		try {
			request.invoke(this);
		} catch (Exception e) {
			e.printStackTrace();
			getLogger().error(e, " on IProductPassedNotification");
		} 
	}

	public Device getClientDevice() {
		if(device == null)
			 device =getDevice(StringUtils.trimToEmpty(getProperty().getProductStateClientId()));
		
		return device;
			
	}
	
	private DataContainer prepareDc(String productId) {
		return prepareDc(getClientDevice().toRequestDataContainer(), productId);
	}
	
	private DataContainer prepareDc(DataContainer dc, String productId) {
		if(StringUtils.isEmpty(dc.getString(TagNames.PROCESS_POINT_ID.name())))
			dc.put(TagNames.PROCESS_POINT_ID.name(), getMainScreen().getApplicationContext().getProcessPointId());
		dc.put(TagNames.PRODUCT_ID.name(),productId);
		return dc;
	}
}

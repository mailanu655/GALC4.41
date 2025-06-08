package com.honda.galc.client.collector;

import java.awt.Color;
import java.awt.Component;

import java.awt.Font;
import java.awt.FontMetrics;

import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.audio.ClientAudioManager;
import com.honda.galc.client.audio.LightAlarmManager;
import com.honda.galc.client.property.AudioPropertyBean;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.TagNames;
import com.honda.galc.dto.ProductCheckDto;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.service.property.PropertyService;

public class ProductStateCollectorClientController extends CollectorClientController {
	protected List<ProductCheckDto> productCheckList;
	private static ClientAudioManager audioManager = null;
	private static LightAlarmManager visualManager = null;
	AudioPropertyBean property;
	private static final String DATE_FORMAT = "MM/dd/yyyy HH:mm:ss a";
	protected String lastNgProduct; 
	
	public ProductStateCollectorClientController() {
		super();
	}
	
	public ProductStateCollectorClientController(ProductStateCollectorMain collectorMain) {
		super(collectorMain);
	}
	
	void init(){
		super.init();
		productCheckList = new ArrayList<ProductCheckDto>();
		lastNgProduct = "";
		initAlarm();
	}

	protected void initAlarm() {
		property = PropertyService.getPropertyBean(AudioPropertyBean.class,
				ApplicationContext.getInstance().getProcessPointId());
		if (property.getSoundAlarmProductStateClient().equalsIgnoreCase("Y"))
			audioManager = new ClientAudioManager(property);
		if (property.getLightAlarm().equalsIgnoreCase("Y"))
			visualManager = new LightAlarmManager();
	}
	
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		if (e.getSource() == getCancelButton())
			playNgAlarm(false);
	}
	
	protected void playNgAlarm(boolean alarm) {
		if (alarm) {
			if (audioManager != null) audioManager.playNoGoodSound();
			if (visualManager != null) visualManager.raiseAlarm(true);
		} else {
			if (audioManager != null) audioManager.playGoodSound();
			if (visualManager != null) visualManager.raiseAlarm(false);
		}
	}
	
	protected void productIdReceivedFromTerminal(String productId) {
		getLogger().info("Received product:", productId);

		Device device = getDevice(StringUtils.trimToEmpty(getProperty().getProductStateClientId()));
		device.getDeviceFormat(TagNames.PRODUCT_ID.name()).setValue(productId);
		
		productIdReceived(device.toRequestDataContainer());
	}
	
	protected void productIdReceived(DataContainer dc) {
		try {
			String productId = getProductIdField().getText();
			if(StringUtils.isEmpty(dc.getString(TagNames.PROCESS_POINT_ID.name())))
				dc.put(TagNames.PROCESS_POINT_ID.name(), getMainScreen().getApplicationContext().getProcessPointId());
		
			result = invokeDataCollectionService(dc);
			if (getProductCheckStatus(result).equals(InstalledPartStatus.NG.name())) playNgAlarm(true);	
			renderProductStateCheckResult(productId);
		} catch (Exception e) {
			getProductIdField().setStatus(false);
			getMainScreen().getStatusMessagePanel().setErrorMessageArea("Exception " + getExceptionMessage(e));
			getLogger().error(e, " Exception on invoking data collection service.");
			return;
		}
		processServiceResponse();
	}

	@SuppressWarnings("serial")
	protected void renderProductStateCheckResult(String productId) {
		productCheckList.add(0, setProductStateCheckDto(result, productId));
		if(productCheckList.size() > getProperty().getMaxNumberOfRows()) productCheckList.remove(getProperty().getMaxNumberOfRows());
		getMainScreen().getCollectorPanel().getSplitPane().setVisible(true);
		getMainScreen().getCollectorPanel().getProductStatePane().reloadData(productCheckList);
		
		getMainScreen().getCollectorPanel().getProductStatePane().getTable().setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			public Component getTableCellRendererComponent(JTable table,Object value,boolean isSelected, boolean hasFocus,int row,int column) {
				Component c = super.getTableCellRendererComponent(table,value, isSelected,hasFocus,row,column);				
				
				Font customFont = getFont().deriveFont((float) getProperty().getListFontSize());
				c.setFont(customFont);
				
				// Set table row height to match font height.
				FontMetrics metrics = getFontMetrics(customFont);
				int customRowHeight = metrics.getHeight();				
				getMainScreen().getCollectorPanel().getProductStatePane().getTable().setRowHeight(customRowHeight);
				
				if(row==getProperty().getHighlightRow()) c.setBackground(getProperty().getHighlightRowColor());
				else c.setBackground(Color.white);
				
				String status = value != null ? (String)value : "";				
				if (status.equals(InstalledPartStatus.NG.name())) 
					c.setForeground(Color.red);	
				else if(status.equals(InstalledPartStatus.OK.name()))
					c.setForeground(Color.green);
				else
					c.setForeground(Color.black);			
				
				return c;
			}
		});
	}
	
	protected ProductCheckDto setProductStateCheckDto(Map<Object, Object> resultproductCheck, String productId) {
		ProductCheckDto productCheckDto = new ProductCheckDto();
		String status = getProductCheckStatus(resultproductCheck);
		productCheckDto.setProductId(productId);
		productCheckDto.setStatus(status);
		productCheckDto.setReason(getReasonForProductStateCheck(resultproductCheck));
		productCheckDto.setDate(getCurrentDate());
		showStatus(status);
		return productCheckDto;
	}
	
	protected String getProductCheckStatus(Map<Object, Object> resultproductCheck) {
		for (Object key : resultproductCheck.keySet()) {
			if(resultproductCheck.get(key).toString().equalsIgnoreCase("false"))
				return InstalledPartStatus.NG.name();
		}
		return InstalledPartStatus.OK.name();
	}
	
	protected String getCurrentDate() {
		DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		Date date = new Date();
		return dateFormat.format(date);
	}
	
	protected String getReasonForProductStateCheck(Map<Object, Object> resultproductCheck) {
		String reason = "";
		for (Object key : resultproductCheck.keySet()) {
			if(resultproductCheck.get(key).toString().equalsIgnoreCase("false"))
				reason += resultproductCheck.get(key).toString().equalsIgnoreCase("false") ? key + ", " : "";
		}
		return reason.replaceAll(", $", "").replaceAll("_", " ");
	}
	
	protected void renderHtmlResult() {
		getMainScreen().getCollectorPanel().getResultPane().setVisible(true);
		if(getProperty().isShowOnlyNgState()) {
			if(getProductCheckStatus(result).equals(InstalledPartStatus.NG.name())){
				showHtmlResult();
				lastNgProduct =main.getCollectorPanel().getProductPane().getProductIdField().getText();
			}
			else if(getProperty().isCheckLastNgProduct()){
				if(!StringUtils.isEmpty(lastNgProduct)){
					if(checkPreviousNgProductStatus(lastNgProduct)) {
						getMainScreen().getCollectorPanel().getResultPane().setText("");
						lastNgProduct = "";
					}
				}
			}
		} else
			showHtmlResult();
	}
	
	
	/**
	 * method that will check if last NG Product has been fixed or not. If fixed clear the error message.
	 * 
	 * @param productId
	 * @return	boolean 
	 */
	private boolean checkPreviousNgProductStatus(String productId){
		getLogger().info("Last NG product:", productId);

		Device device = getDevice(StringUtils.trimToEmpty(getProperty().getProductStateClientId()));
		device.getDeviceFormat(TagNames.PRODUCT_ID.name()).setValue(productId);

		DataContainer dc = device.toRequestDataContainer();
		Map<Object, Object> result1 = invokeDataCollectionService(dc);		
		String status= getProductCheckStatus(result1);
		
		return status.equals(InstalledPartStatus.OK.name()) ? true : false;
	}
	
	private void showHtmlResult() {
		StringBuilder sb = new StringBuilder();
		for(IRender<?> render : renders)
			sb.append(render.render());
		
		getMainScreen().getCollectorPanel().getResultPane().setText(sb.toString());
		getMainScreen().getCollectorPanel().getResultPane().setCaretPosition(0);
		getLogger().debug("html contents:" + sb.toString());
	}
	
	protected void showStatus(String status) {
		getMainScreen().getCollectorPanel().getProductPane().getStatusText().setText(status);
		getMainScreen().getCollectorPanel().getProductPane().getStatusText().setBackground(status.equals(InstalledPartStatus.OK.name()) ? Color.green : Color.red);
		resetFlag = true;
		if(!StringUtils.equals(status, InstalledPartStatus.OK.name())){
			if(!getProperty().isAutoRefreshNg()) resetFlag = false;
		}
	}
	
	public ProductStateCollectorClientPropertyBean getProperty() {
		return (ProductStateCollectorClientPropertyBean)getMainScreen().getProperty();
	}
	
	protected ProductStateCollectorMain getMainScreen() {
		return (ProductStateCollectorMain) this.main;
	}
}

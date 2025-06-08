package com.honda.galc.client.qi.precheck;


import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.product.NavigateTabEvent;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.qi.base.AbstractQiProcessView;
import com.honda.galc.client.qi.base.QiProcessModel;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.utils.QiProgressBar;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.qi.QiStationConfigurationDao;
import com.honda.galc.entity.enumtype.QiEntryStationConfigurationSettings;
import com.honda.galc.entity.qi.QiStationConfiguration;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.ProductCheckType;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.util.Duration;

/**
 * 
 * <h3>PreCheckView Class description</h3>
 * <p> PreCheckView description </p>
 *   
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
 * @author L&T Infotech<br>
 * Nov 26, 2016
 *
 */

public class PreCheckView extends AbstractQiProcessView<QiProcessModel, PreCheckController>{	

	private WebView webView;
	private static Timeline timer;
	private volatile boolean reset = true;
	
	public PreCheckView(MainWindow mainWindow) {
		super(ViewId.PRE_CHECK,mainWindow);
	}

	/**
	 * This method is used to reload view
	 */

	@Override
	public void reload() {
		
		Map<String, Object> productPreWarnResults = getController().submitPreWarnCheckProductState();
		
		Map<String, Object> productPreCheckResults = getController().submitPreCheckProductState();

		reloadProductCheckData(productPreCheckResults,productPreWarnResults); 
		getController().setMultiLine();

	}

	public void stopClock() {
		if(timer != null){
			timer.stop();
			
		}
	}
	
	public void startClock(int redirectTime) {
		stopClock();
	    timer =  new Timeline(new KeyFrame(Duration.millis(redirectTime),new EventHandler<ActionEvent>() {
		  public void handle(ActionEvent event) {
		  };
		}));
	    
	    timer.setOnFinished(new EventHandler<ActionEvent>(){
	    	public void handle(ActionEvent event) {
	    		NavigateTabEvent nav = new NavigateTabEvent();
	    		nav.setNavigateToTab(ViewId.DEFECT_ENTRY.getViewLabel());
	    		nav.setNextTab(true);
	    		nav.setSwitchProcessTab(true);
				EventBusUtil.publish(nav);	    		
	    	}	
	    });
		timer.setCycleCount(1);
		timer.play();
	}
	
	public void reloadProductCheckData(Map<String, Object> productPreCheckResults, Map<String, Object> productPreWarnResults) {

		StringBuffer htmlContent = new StringBuffer();
		boolean isOK = false;
		int redirectTime = getRedirectTime();

		if ((productPreCheckResults == null || productPreCheckResults.isEmpty()) && (productPreWarnResults == null || productPreWarnResults.isEmpty()) ) {
			
			htmlContent.append("<div><p style='color: #ffffff; background-color: green'>Pre Check:OK</p></div>");
			isOK = true;
		}
		else{
			if (productPreWarnResults != null && !productPreWarnResults.isEmpty()) {
				htmlContent.append("<div style='padding:2px; background-color:FF8000; font-size: 17px; font-weight: bold; font-family: dialog;'>Pre Check - Warning</div>");
				htmlContent.append(generateProductPreCheckContent(productPreWarnResults));
			}
			if (productPreCheckResults != null && !productPreCheckResults.isEmpty()) {
				String message = generateProductPreCheckContent(productPreCheckResults);

				htmlContent.append("<div style='padding:2px; background-color:RED; font-size: 17px; font-weight: bold; font-family: dialog;'>Pre Check - Failure</div>");
				htmlContent.append(message);
			}
			try  {
				getController().getAudioManager().playNGSound();
			}
			catch(Exception ex)  {
				getLogger().warn(ex, "Precheck Unable to plag NG sound");
			}
		}		
		
		setWebView(htmlContent.toString());
		if(reset && redirectTime >= 0 && isOK)  {
			reset = false;
			startClock(redirectTime);
		}
		
	}
	
	private int getRedirectTime() {
		int redirectTime = -1;
		
		try {
			
			QiStationConfiguration redirectConfigEntry = 
					ServiceFactory.getDao(QiStationConfigurationDao.class).findValueByProcessPointAndPropKey(getProcessPointId(), QiEntryStationConfigurationSettings.REDIRECT_TO_DEFECT_ENTRY.getSettingsName());		
			String redirectTimeValue = QiEntryStationConfigurationSettings.REDIRECT_TO_DEFECT_ENTRY.getDefaultPropertyValue();
			redirectTime = Integer.parseInt(redirectTimeValue)*1000;
			if(redirectConfigEntry != null && redirectConfigEntry.getPropertyValue() != null)  {				
				Double redirectTimeDouble = Double.parseDouble(redirectConfigEntry.getPropertyValue())*1000;
				redirectTime = redirectTimeDouble.intValue();
			}
		
		} catch(Exception ex) {
			Logger.getLogger().error(ex.getMessage());
		}
			
		return redirectTime;
	}

	private String generateProductPreCheckContent(Map<String, Object> checkResults) {
		Logger.getLogger().check("Product Pre Check Results: " + checkResults);
		return generateProductCheckContent(checkResults, "#c7c7c7");
	}        


	/**
	 * This method is used to display the data into table format
	 */
	private void generateProductCheckContent(String message, Collection<Object> result, StringBuilder content) {
		Iterator<Object> i = result.iterator();
		int count = 0;
		while (i.hasNext()) {
			count++;
			Object name = i.next();
			String color = "#ededed";
			if (count % 2 == 0) {
				color = "#ffffff";
			}
			content.append("<tr style='font-size: 13px; background: ").append(color).append("'><td>&nbsp;</td>").append("<td width='10%' style='text-align: right;'>").append(count)
			.append(")</td>").append("<td style=\"word-break: break-word;\">").append(name).append("</td></tr>");

		}
	}

	/**
	 * This method is used to append the style and property to the table data
	 */
	private StringBuilder generateProductCheckContent(String message, Object result, int rowCount, StringBuilder content, String color) {
		if (rowCount % 2 == 0) {
			color = "#bababa";
		}
		// Added to change the color only when we have Lot Change for RGALCDEV-8480 
		if(StringUtils.equalsIgnoreCase(message, "LOT CHANGE")) {
			color = "#ffff00";
		}
		content.append("<tr style='background: ").append(color).append(";'><td width='10%' style='text-align: right;'>").append(rowCount).append(".</td><td colspan='2'>").append(
				message).append("</td></tr>");
		return content;
	}

	/**
	 * This method is used to generate product PreCheck content
	 */
	@SuppressWarnings("unchecked")
	private String generateProductCheckContent(Map<String, Object> mProductCheckResults, String color) {

		StringBuilder content = new StringBuilder();

		int rowCount = 0;
		content.append("<table width='100%' border='0' cellspacing='0' cellpadding='2' style='font-size: 15px; font-weight: bold; font-family: dialog; '>");
		for (Map.Entry<String, Object> entry : mProductCheckResults.entrySet()) {
			rowCount++;
			String message = entry.getKey();
			Object value = entry.getValue();

			if (message == null || value == null) {
				continue;
			}

			try {
				ProductCheckType checkType =  ProductCheckType.valueOf(message);
				message = checkType.getName();
			} catch (IllegalArgumentException e) {
				handleException(e);
			}

			content = generateProductCheckContent(message, value, rowCount, content, color);

			Collection<Object> collectionValue = null;
			if (value instanceof Collection) {
				collectionValue = (Collection<Object>) value;
			} else if (value.getClass().isArray()) {
				Object[] arrayValue = (Object[]) value;
				collectionValue = Arrays.asList(arrayValue);
			} else if (value instanceof Map) {
				@SuppressWarnings("rawtypes")
				Map mapValue = (Map) value;
				collectionValue = mapValue.entrySet();
			}

			if (collectionValue != null) {
				generateProductCheckContent(message, collectionValue, content);				
			}
		}
		content.append("</table>");
		return content.toString();
	}

	@Override
	public void start() {
		reload();

	}

	@Override
	public void initView() {
		reset = true;
	}	

	private void setWebView(String htmlContent){
		ScrollPane outerScrollPane = new ScrollPane();
		VBox innerPane = new VBox();
		webView = new WebView();
		BorderPane root = new BorderPane(webView);
		
		QiProgressBar progressBarUtility = QiProgressBar.getInstance("Loading PreCheck View.", "Loading PreCheck View.",
				getModel().getProductId(),getStage(),true);;
		try {
			progressBarUtility.showMe();	
			innerPane.getChildren().add(root);
			innerPane.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth());
			outerScrollPane.setContent(innerPane);
			webView.getEngine().loadContent(htmlContent);
		}
		finally {
			if(progressBarUtility != null)  {
				progressBarUtility.closeMe();
			}
		}
		this.setCenter(outerScrollPane);
	}
}  
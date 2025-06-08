package com.honda.galc.client.product;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.bridge.mdrs.MdrsStateBean;
import com.honda.galc.client.product.mvc.AbstractProductClientPane;
import com.honda.galc.client.product.mvc.BulkProductClientPane;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dao.conf.TerminalDao;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.conf.Terminal;
import com.honda.galc.property.ProductPropertyBean;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class BulkProductClientFrame extends MainWindow {
	
	protected static final int STATUS_PANEL_HIGHT = 75;
	private AbstractProductClientPane productPanel;
	
	public BulkProductClientFrame(ApplicationContext appContext, Application application) {
		super(appContext, application, true);
		createProductPanel(null);
		init();
	}

	public BulkProductClientFrame(ApplicationContext appContext, Application application,ProductPropertyBean productPropertyBean) {
		super(appContext, application, true);
		createProductPanel(productPropertyBean);
		init();
	}
	
	protected void init() {
		String iconUrl = "/resource/images/common/honda.gif";
		try {
			Image frameIcon = new Image(iconUrl);
			ClientMainFx.getInstance().getStage().getIcons().add(frameIcon);
		} catch (Exception e) {
			getLogger().error(e, "Failed to load frame icon from url:'" + iconUrl + "'");
		}
		
		productPanel.openProcessInfoDialog();
		productPanel.openMainMenuDialog();


		if (null != System.getProperty("javaws.secure.properties.trainingmode")
				&& System.getProperty("javaws.secure.properties.trainingmode").equals("true"))
			productPanel.getController().getModel().setTrainingMode(true);

		if (productPanel.getController().getModel().isTrainingMode() ) displayTrainingModeMsg();
		else registerClientIpaddress();

		// call MDRS REST service
		SystemPropertyBean systemPropertyBean =  PropertyService.getPropertyBean(SystemPropertyBean.class);
		
		if(systemPropertyBean.isMdrsServiceAvailable()){
			MdrsStateBean.getUserTrainingData(applicationContext.getUserId());
		}
		
		productPanel.getController().toIdle();
		//after login,broadcast to PLC to ask for product
		if (productPanel.getController().getModel().getProperty().isBroadcastAfterLogin() && 
				!productPanel.getController().getModel().isTrainingMode()) { 
			productPanel.getController().getModel().broadcastAfterlogin();
		}
	}
	
	private void displayTrainingModeMsg() {
		Label trainingModel = UiFactory.createLabel("trainingModel", "TRAINING MODE!!!(Collected data Won't Be Saved.)");
		trainingModel.setMaxWidth(Double.MAX_VALUE);
		trainingModel.setStyle("-fx-font-size: 24; -fx-font-weight: bolder; -fx-text-fill: white; -fx-background-color:blue");
		trainingModel.setAlignment(Pos.CENTER);
		
		FadeTransition ft = new FadeTransition(Duration.millis(500), trainingModel);
        ft.setFromValue(1.0);
        ft.setToValue(0.3);
        ft.setCycleCount(10);
        ft.setAutoReverse(true);
        
        ft.play();

        statusMessagePane.setBottom(trainingModel);
	}
	
	private void registerClientIpaddress() {
		ApplicationContext defaultAppContext = ClientMainFx.getInstance().getApplicationContext();
		SystemPropertyBean systemPropertyBean =  PropertyService.getPropertyBean(SystemPropertyBean.class);
		
		if (systemPropertyBean.isAutoRegisterIpaddr()) {
		   String ipAddress = defaultAppContext.getLocalHostIp();
		   String hostname = defaultAppContext.getHostName();
		
		   TerminalDao terminalDao = ServiceFactory.getDao(TerminalDao.class);
		   Terminal terminal  = terminalDao.findByKey(hostname);
		   terminal.setIpAddress(ipAddress);
           terminalDao.save(terminal);
		
		   getLogger().info(String.format("Terminal %s ipaddress updated to %s." ,hostname,ipAddress));
		}
	}
	
	protected AbstractProductClientPane createProductPanel(ProductPropertyBean productPropertyBean) {
		productPanel = new BulkProductClientPane(this,productPropertyBean);
		setClientPane(productPanel);
		return productPanel;
	}
	
	protected AbstractProductClientPane getProductPanel() {
		return (AbstractProductClientPane) clientPane;
	}
}

package com.honda.galc.client.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * @author is08925
 *
 */

/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class TabbedMainWindow extends MainWindow{

	private static final long serialVersionUID = 1L;
	private final boolean allowCommonMessage;
	private  int width, height;
	private JTabbedPane tabbedPane;
	
	private Map<Component, String> messageCache;
	private Map<Component, String> errorMessageCache;

	public TabbedMainWindow(ApplicationContext appContext,Application application) {
		super(appContext,application,true);
		this.allowCommonMessage = PropertyService.getPropertyBoolean(application.getApplicationId(), "ALLOW_COMMON_MESSAGE", false);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.width = PropertyService.getPropertyInt(application.getApplicationId(), "MAIN_WINDOW_WIDTH", screenSize.width);
		this.height = PropertyService.getPropertyInt(application.getApplicationId(), "MAIN_WINDOW_HEIGHT", screenSize.height);
		setSize(new Dimension(this.width,this.height));
		init();
	}
	
	protected void init() {
		if (!isAllowCommonMessage()) {
			this.messageCache = new HashMap<Component, String>();
			this.errorMessageCache = new HashMap<Component, String>();
		}
		this.setClientPanel(createTabbedContentPanel(loadPanelConfigs()));
	}
	
    protected List<String> loadPanelConfigs() {
    	List<String> panelIds = new ArrayList<String>();
    	String propertyPanels = getApplicationProperty("PANELS");
    	if(!StringUtils.isEmpty(propertyPanels)) {
    		String[] items = propertyPanels.split(Delimiter.COMMA);
    		for(String str : items) {
    			panelIds.add(str);
    		}
    	}
    	if(panelIds.isEmpty()) panelIds.add("BLANK");
    	return panelIds;
    }
    
 	protected Component createTabbedContentPanel(List<String> panelIds) {
		this.tabbedPane = new JTabbedPane();
		if (panelIds == null || panelIds.isEmpty()) {
			return getTabbedPane();
		}

		for (int tabIx = 0; tabIx < panelIds.size(); tabIx++) {
			String panelId = panelIds.get(tabIx);
			TabbedPanel panel = TabbedPanel.createTabbedPanel(panelId, this);
			getTabbedPane().addTab(panel.getScreenName(), panel);
			getTabbedPane().setMnemonicAt(tabIx, panel.getKeyEvent());
			if (panel.getMainWindow() == null) {
				panel.setMainWindow(this);
			}
			if (tabIx == 0) {
				panel.onTabSelected();
			}
		}

		getTabbedPane().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				Component component = getTabbedPane().getSelectedComponent();
				if (component instanceof TabbedPanel) {
					TabbedPanel selectedPanel = (TabbedPanel) component;
					selectedPanel.onTabSelected();
				}
				if (!isAllowCommonMessage()) {
					String message = getErrorMessageCache().get(component);
					if (message != null) {
						setErrorMessage(message);
						return;
					}
					message = getMessageCache().get(component);
					if (message != null) {
						setMessage(message);
						return;
					}
					clearMessage();
				}
			}
		});
		if (getTabbedPane().getTabCount() > 0) {
			getTabbedPane().setSelectedIndex(0);
		}
		return getTabbedPane();
	} 	
 	
	@Override
	public void clearMessage() {
		super.clearMessage();
		if (!isAllowCommonMessage()) {
			getMessageCache().remove(getTabbedPane().getSelectedComponent());
			getErrorMessageCache().remove(getTabbedPane().getSelectedComponent());
		}
	}
 	
	@Override
	public void setMessage(String message) {
		super.setMessage(message);
		if (!isAllowCommonMessage()) {
			getMessageCache().put(getTabbedPane().getSelectedComponent(), message);
		}
	}
 	
	@Override
	public void setErrorMessage(String message) {
		super.setErrorMessage(message);
		if (!isAllowCommonMessage()) {
			getErrorMessageCache().put(getTabbedPane().getSelectedComponent(), message);
		}
	}

 	
 	@SuppressWarnings("unchecked")
	public <T extends TabbedPanel> T getTabbedPanel(Class<T> clazz) {
 		for(int i =0;i<tabbedPane.getTabCount();i++){
 			Component component = tabbedPane.getComponentAt(i);
 			if(component.getClass().isInstance(clazz)) return (T) component;
 		}
 		return null;
 	}
 
 	protected boolean isAllowCommonMessage() {
 		return this.allowCommonMessage;
 	}

	protected JTabbedPane getTabbedPane() {
		return tabbedPane;
	}
	
	private Map<Component, String> getMessageCache() {
		return messageCache;
	}

	private Map<Component, String> getErrorMessageCache() {
		return errorMessageCache;
	}	
}

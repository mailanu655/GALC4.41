package com.honda.galc.client.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.entity.conf.Application;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

/**
 * 
 * @author is08925
 *
 * @author Suriya Sena
 * 
 * Jan 25 2014  JavaFx Migration
 */

/**
 * 
 *    
 * @author Gangadhararao Gadde
 * @since Nov 9, 2017
 * Simulation changes
 */
public class TabbedMainWindow extends MainWindow{

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	
	private TabPane tabbedPane;  // FIXME rename
	
	
	// TODO should this map be of pane or tab
	private Map<Tab, String> messageCache;
	private Map<Tab, String> errorMessageCache;

	public TabbedMainWindow(ApplicationContext appContext,Application application) {
		super(appContext,application,true);
		init();
	}
	
	protected void init() {
		this.messageCache = new HashMap<Tab, String>();
		this.errorMessageCache = new HashMap<Tab, String>();
		this.setClientPane(createTabbedContentPanel(loadPanelConfigs()));
		this.setTop(getTopPanel());
	}
	
	protected Node getTopPanel() {
		return super.menuBar;
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
    
 	protected TabPane createTabbedContentPanel(List<String> panelIds) {
		this.tabbedPane = createTabPane();

		if (panelIds == null || panelIds.isEmpty()) {
			return getTabPane();
		}

		for (int tabIx = 0; tabIx < panelIds.size(); tabIx++) {
			String panelId = panelIds.get(tabIx);
			final ApplicationMainPane panel = TabbedPanel.createTabbedPanel(panelId, this);
			final ITabbedPanel iPanel = (ITabbedPanel)panel;
			Tab tab = createTab(panel, iPanel);
			getTabPane().getTabs().add(tab);
			// TODO add Mnemonic to node. 
			//getTabPane().setMnemonicAt(tabIx, panel.getKeyEvent());
			if (panel.getMainWindow() == null) {
				panel.setMainWindow(this);
			}
			if (tabIx == 0) {
				iPanel.onTabSelected();
			}
			
			tab.setOnSelectionChanged(new EventHandler<Event>() {

				@Override
				public void handle(Event arg0) {
					if(((Tab)arg0.getSource()).isSelected()) {
						clearMessage();
						iPanel.onTabSelected();
                        Logger.getLogger().check(iPanel.getScreenName() + " tabbed panel is selected");  
 					}
					
				}
			});
			
		}
		
		

	
		// TODO
//		getTabPane().addChangeListener(new ChangeListener() {
//			public void stateChanged(ChangeEvent evt) {
//				Component component = getTabPane().getSelectedComponent();
//				if (component instanceof TabbedPanel) {
//					TabbedPanel selectedPanel = (TabbedPanel) component;
//					selectedPanel.onTabSelected();
//				}
//				String message = getErrorMessageCache().get(component);
//				if (message != null) {
//					setErrorMessage(message);
//					return;
//				}
//				message = getMessageCache().get(component);
//				if (message != null) {
//					setMessage(message);
//					return;
//				}
//				clearMessage();
//			}
//		});
		if (getTabPane().getTabs().size() > 0) {
			getTabPane().getSelectionModel().selectFirst();
		}
		return getTabPane();
 	}
 	
 	protected TabPane createTabPane() {
 		TabPane tabPane= new TabPane();
 		tabPane.setSide(Side.TOP);
		return tabPane;
 	}
 	
 	protected Tab createTab(ApplicationMainPane panel, ITabbedPanel iPanel) {
 		Tab tab = new Tab();
		tab.setClosable(false);
		tab.setContent(panel);
		tab.setText(iPanel.getScreenName());
		return tab;
 	}
 	
	@Override
	public void clearMessage() {
		super.clearMessage();
		getMessageCache().remove(getSelectedTab());
		getErrorMessageCache().remove(getSelectedTab());
	}
 	
	@Override
	public void setMessage(String message) {
		super.setMessage(message);
		getMessageCache().put(getSelectedTab(), message);
	}
 	
	@Override
	public void setErrorMessage(String message) {
		super.setErrorMessage(message);
		getErrorMessageCache().put(getSelectedTab(), message);
	}

 //TODO	
 	@SuppressWarnings("unchecked")
//	public <T extends TabbedPanel> T getTabbedPanel(Class<T> clazz) {
// 		for(int i =0;i<tabbedPane.getTabCount();i++){
// 			Component component = tabbedPane.getComponentAt(i);
// 			if(component.getClass().isInstance(clazz)) return (T) component;
// 		}
// 		return null;
// 	}

	protected TabPane getTabPane() {
		return tabbedPane;
	}
	
	private Tab getSelectedTab() {
		return getTabPane().getSelectionModel().getSelectedItem();
	}
	
	private Map<Tab, String> getMessageCache() {
		return messageCache;
	}

	private Map<Tab, String> getErrorMessageCache() {
		return errorMessageCache;
	}	
}

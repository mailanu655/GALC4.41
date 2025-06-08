package com.honda.galc.client.product.entry;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.client.product.mvc.PaneId;
import com.honda.galc.client.product.pane.AbstractGenericEntryPane;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.util.ReflectionUtils;
import com.honda.galc.util.StringUtil;

import javafx.scene.control.Tab;

public class GenericProductEntryTabPane extends AbstractProductEntryPane {
	GenericProductInputPaneController controller;

	List<AbstractGenericEntryPane> paneList= new ArrayList<AbstractGenericEntryPane>();

	public GenericProductEntryTabPane(TabbedPanel parentView) {
		super(parentView);
		init();
	}

	private void init() {
		this.controller = new GenericProductInputPaneController(this);
		String[] panels = controller.getPanels();
		if(panels == null || panels.length == 0) {
			return;
		}
		loadPanels(panels);
	}

	private void loadPanels(String... panels) {
		if(panels == null) {
			return;
		}
		for(String panel : panels) {
			if(StringUtil.isNullOrEmpty(panel)) {
				continue;
			}
			AbstractGenericEntryPane pane = createPanel(panel);
			if(pane == null) {
				continue;
			}
			paneList.add(pane);
		}
		if(paneList.isEmpty()) {
			return;
		}
		for(AbstractGenericEntryPane pane : paneList) {
			Tab tab = new Tab(pane.getPaneLabel());
			tab.setContent(pane);
			getTabs().add(tab);
		}
		setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
	}
	
	private AbstractGenericEntryPane createPanel(String panelName) {
		panelName = panelName.trim();
		Class<?> panelClass = PaneId.getPaneClass(panelName);
		if(panelClass == null) {
			parentView.getMainWindow().setErrorMessage("Pane class does not exist : " + panelClass);
		return null;
		} else {
			if(AbstractGenericEntryPane.class.isAssignableFrom(panelClass)) {
				parentView.getLogger().info("Creating entry pane : " + panelClass.getSimpleName());
				return (AbstractGenericEntryPane)ReflectionUtils
						.createInstance(panelClass,new Class<?>[] {AbstractProductEntryPane.class},this);
				} else {
					parentView.getMainWindow().setErrorMessage("Pane class cannot be created : " + panelName);
			}
		}
		return null;
	}
	
	public String getSelectedTabId() {
		return getSelectionModel().getSelectedItem().getContent().getId();
		
	}
}

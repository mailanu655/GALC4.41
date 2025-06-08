package com.honda.galc.client.dc.view;

import java.util.List;

import javafx.application.Platform;
import javafx.scene.control.Control;

import org.tbee.javafx.scene.layout.MigPane;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.dc.mvc.DataCollectionController;
import com.honda.galc.client.dc.processor.CylinderShimInstallAbstractProcessor;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.event.ProductStartedEvent;
import com.honda.galc.client.ui.event.UnitNavigatorEvent;
import com.honda.galc.client.ui.event.UnitNavigatorEventType;
import com.honda.galc.entity.conf.MCOperationRevision;

/**
 * 
 * @author Wade Pei <br>
 * @param <B>
 * @date Jul 2, 2014
 */
public abstract class CylinderShimInstallAbstractView<B extends CylinderShimInstallAbstractBodyPane, P extends CylinderShimInstallAbstractProcessor> extends OperationView {
	
	protected MigPane headPane;
	protected B bodyPane;

	public CylinderShimInstallAbstractView(P processor) {
		super(processor);
		EventBusUtil.register(this);
	}
	
	/**
	 * Handle product started event.
	 *
	 * @param event the event
	 */
	@Subscribe
	public void handleProductStartedEvent(ProductStartedEvent event){
		//Reset the view when a new product id is entered;
		resetView();
	}

	/**
	 * Handle the unit navigator event.
	 *
	 * @param event the event
	 */
	@Subscribe
	public void handleUnitNavigatorEvent(UnitNavigatorEvent event) {
		getLogger().debug("UnitNavigatorWidget.handleEvent recvd : " + event.toString());
        List<MCOperationRevision> structures = this.getProcessor().getController().getModel().getOperations();
        int index = structures.indexOf(this.getProcessor().getOperation());
 		if (event.getType().equals(UnitNavigatorEventType.SELECTED) && index == event.getIndex()) {
			//refresh the view when the work unit is selected.
			refreshView();
		}
	}
	
	/* (non-Javadoc)
	 * @see com.honda.galc.client.dc.view.AbstractDataCollectionWidget#refreshView()
	 */
	@Override
	public void refreshView(){
		// The following lines are used to fix the layout bug of MigLayout in JDK1.8 environment.
		Platform.runLater(new Runnable() {
			public void run() {
				// The Head pane is optional.
				if(null!=headPane){
					headPane.layout();
				}
				bodyPane.layout();
				setFocus();
			}
		});
		setFocus();
	}

	@Override
	public void initComponents() {
		getProcessor().setView(this);
		headPane = createHeadPane();
		bodyPane = createBodyPane();
		if(null!=headPane){
			this.setTop(headPane);
		}
		this.setCenter(bodyPane);
		getProcessor().loadInstalledParts();
		refreshView();
	}

	protected abstract MigPane createHeadPane();

	protected abstract B createBodyPane();

	public void resetView() {
		getProcessor().init();
		getBodyPane().init();
		getProcessor().loadInstalledParts();
		setFocus();
	}

	protected void setFocus() {
		Control focused = getBodyPane().getFocusedComponent();
		if (null != focused && !focused.equals(getController().getFocusComponent())) {
			getController().setFocusComponent(focused);
		}
		getController().requestFocus();
	}

	@Override
	public P getProcessor() {
		return (P)super.getProcessor();
	}

	public DataCollectionController getController() {
		return getProcessor().getController();
	}

	public B getBodyPane() {
		return bodyPane;
	}
}

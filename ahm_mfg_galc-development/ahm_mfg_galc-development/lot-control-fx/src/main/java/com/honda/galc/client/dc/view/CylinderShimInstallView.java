package com.honda.galc.client.dc.view;

import org.tbee.javafx.scene.layout.MigPane;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.dc.processor.CylinderShimInstallProcessor;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.event.KeypadEvent;
import com.honda.galc.client.utils.UiFactory;

/**
 * 
 * @author Wade Pei <br>
 * @date Jul 2, 2014
 */
public class CylinderShimInstallView extends CylinderShimInstallAbstractView<CylinderShimInstallBodyPane, CylinderShimInstallProcessor> {

	public CylinderShimInstallView(CylinderShimInstallProcessor processor) {
		super(processor);
		cleanup();
	}
	
	private void cleanup() {
		for (Object  o : EventBusUtil.findListenersOfType(this.getClass())) {
			if ( o != this ) {
				EventBusUtil.unregister(o);
			}
		}
	}
	
	protected MigPane createHeadPane() {
		int FONT_SIZE = getProcessor().getFontSize();
		MigPane pane = new MigPane("insets 14 5 5 5", "[center,grow,fill]", "[]5[]");
		pane.add(UiFactory.createLabel("MainTitle", getProcessor().getMainTitle(), Fonts.SS_DIALOG_BOLD((int)(FONT_SIZE*1.2))), "wrap");
		pane.add(UiFactory.createLabel("SubTitle", getProcessor().getSubTitle(), Fonts.SS_DIALOG_PLAIN((int)(FONT_SIZE*0.9))));
		return pane;
	}

	protected CylinderShimInstallBodyPane createBodyPane() {
		return new CylinderShimInstallBodyPane(getProcessor().getCylinderNum(), getProcessor().getCylinderStartNo(), this);
	}
	
	@Subscribe
	public void handle(KeypadEvent event) {
	
		switch (event.getEventType()) {
		    // Dont let the user reject an unit via the clicker
			case KEY_COMPLETE:
				if(getBodyPane().getDoneBtn()!=null && 
					getBodyPane().getDoneBtn().getText().equals(getBodyPane().DONE) &&
					getBodyPane().getDoneBtn().isDisabled() == false) {
					getBodyPane().getDoneBtn().fire();
				}
				break;
			default:
				break;
		}
	}
	
}

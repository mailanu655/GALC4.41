/**
 * 
 */
package com.honda.galc.client.qi.base;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.qi.homescreen.HomeScreenView;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.util.MultiLineHelper;

/**
 * @author VCC44349
 *
 */
public abstract class AbstractQiDefectController<M extends AbstractQiDefectProcessModel, V extends AbstractQiProcessView<?, ?>> extends AbstractQiProcessController<M, V> {
 
	public static final String PREVIOUS_LINE_INVALID_MSG = "Tracking status got updated by another process and is invalid for this process. Please cancel this operation.";
	
	public AbstractQiDefectController(M model, V view) {
		super(model, view);
	}

	protected void publishProductPreviousLineInvalidEvent() {
		EventBusUtil.publishAndWait(new StatusMessageEvent(PREVIOUS_LINE_INVALID_MSG, StatusMessageEventType.ERROR));
		EventBusUtil.publishAndWait(new ProductEvent(StringUtils.EMPTY, ProductEventType.PRODUCT_PREVIOUS_LINE_INVALID));
	}
	
	public boolean isFrameQicsEngineSource() {
		return getModel().getProperty().isQicsEngineSource() &&
				getModel().getProductType().equals(ProductType.FRAME.toString());
	}
}
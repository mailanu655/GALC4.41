package com.honda.galc.client.teamleader;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ui.StatusMessagePanel;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.Application;

public class ManualLotControlRepairSubView extends TabbedMainWindow {
	private static final long serialVersionUID = 1L;
	private ManualLotControlRepairPanel contentPanel;
	private ProductType productType;
	protected ManualLotControlRepairPanel parentView;
	private StatusMessagePanel statusMessagePane = null;
	
	public ManualLotControlRepairSubView(ApplicationContext appContext, Application application) throws SystemException {
		super(appContext, application);	
		initialize();
	}
	
	public ManualLotControlRepairSubView(ApplicationContext appContext, Application application,ProductType productType, ManualLotControlRepairPanel parentView) throws SystemException {
		super(appContext, application);
		this.productType = productType;
		this.parentView = parentView;
		initialize();
	}

	private void initialize() {
		
		try {
			setTitle("SubProduct View: "+productType.getProductName());
			setLayout(new BorderLayout());
			JScrollPane pane = new JScrollPane(getViewContentPanel());
			add(pane, BorderLayout.CENTER);
            add(getStatusMessagePanel(), BorderLayout.SOUTH);

			
			
		} catch (Exception e) {
			Logger.getLogger().error(e, "Exception to start ManualLotControlRepairSubView");
		}
	}

	public ManualLotControlRepairPanel getViewContentPanel() {
		if(contentPanel == null){
			contentPanel = new ManualLotControlRepairPanel(this,productType,parentView);
		}
		
		return contentPanel;
	}
	
	@Override
    public StatusMessagePanel getStatusMessagePanel() {
        if (statusMessagePane == null) {
        	statusMessagePane = new StatusMessagePanel();
        }
        return statusMessagePane;
    }
    
    @Override
    public void setErrorMessage(String msg) {
    	getStatusMessagePanel().setErrorMessageArea(msg);	
	}
	
	@Override
	protected void exit() {
		setDefaultCloseOperation(javax.swing.
		           WindowConstants.DISPOSE_ON_CLOSE);
	}
	@Override
	protected void close() {
		setDefaultCloseOperation(javax.swing.
		           WindowConstants.DISPOSE_ON_CLOSE);
	}	
	
}

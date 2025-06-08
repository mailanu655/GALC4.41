package com.honda.galc.client.qics.view.action;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;

import com.honda.galc.client.events.ProductInspectionComplete;
import com.honda.galc.client.qics.view.constants.ActionId;
import com.honda.galc.client.qics.view.screen.DefectScanTextPanel;
import com.honda.galc.client.qics.view.screen.QicsPanel;

public class ScanTextInputAction extends AbstractPanelAction {

	private static final long serialVersionUID = 1L;	
	private boolean reset;			
	private Map<String, AbstractPanelAction> actionsMap = new HashMap<String, AbstractPanelAction>();


	public ScanTextInputAction(QicsPanel qicsPanel) {
		super(qicsPanel);
		createActionsMap();
	}

	protected void createActionsMap() {

		getActionsMap().put("Direct Pass", ActionId.DIRECT_PASS.createAction(getQicsPanel()));
		getActionsMap().put("Repair All", ActionId.REPAIR_ALL.createAction(getQicsPanel()));		
		getActionsMap().put("Cancel", ActionId.CANCEL.createAction(getQicsPanel()));
		getActionsMap().put("Done", ActionId.SUBMIT.createAction(getQicsPanel()));
		getActionsMap().put("Scrap", ActionId.SCRAP.createAction(getQicsPanel()));
	}


	@Override
	public void execute(ActionEvent e) {
		try {
			String productId = getQicsFrame().getMainPanel().getProductInfoPanel().getProductNumberTextField().getText();
			getQicsFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			getQicsFrame().clearMessage();
			getQicsFrame().clearStatusMessage();
			reset = false;
			String text = getQicsPanel().getDefectScanInputPane().getEntryTextField().getText().trim();
			getQicsFrame().getLogger().info("Entry TextField Scanned value: "+text);
			if (text.length() == 0) {
				getQicsFrame().setMessage("Invalid Scan value");
				getQicsFrame().getLogger().info("Invalid Scan value");
				return;
			}

			AbstractPanelAction action =getActionClass(text);
			if (action==null)
			{			
				action= new AcceptScanTextAction(getQicsPanel());	           

			}
			getQicsFrame().getLogger().info("Defect Scan Action class: "+action.getClass().getSimpleName());
			
			if (action.getClass().getSimpleName().equals(SubmitAction.class.getSimpleName()) 
					&& getQicsController().getProductModel().getNewDefects().size() == 0
					&& getQicsController().getProductModel().getUpdatedDefects().size() == 0){
				getQicsFrame().setErrorMessage("No Defects entered or updated.Perform Direct Pass.");
				getQicsFrame().getLogger().info("No Defects entered or updated.Perform Direct Pass");
				
				return;
			}
			if (action.getClass().getSimpleName().equals(DirectPassAction.class.getSimpleName()) )
			{
				if(getQicsController().getProductModel().getNewDefects().size() != 0)
				{
					getQicsFrame().setErrorMessage("New Defects entered.Cannot perform Direct Pass.");
					getQicsFrame().getLogger().info("New Defects entered.Cannot perform Direct Pass.");
					
					return;
				}
				if(getQicsController().getProductModel().getUpdatedDefects().size() != 0)
				{
					getQicsFrame().setErrorMessage("Defects updated.Cannot perform Direct Pass");
					getQicsFrame().getLogger().info("Defects updated.Cannot perform Direct Pass");
					return;
				}
			}

			action.execute(e);
			if (action.getClass().getSimpleName().equals(DirectPassAction.class.getSimpleName())
					||action.getClass().getSimpleName().equals(SubmitAction.class.getSimpleName())
					||action.getClass().getSimpleName().equals(ScrapAction.class.getSimpleName()))
			{
				EventBus.publish(new ProductInspectionComplete(StringUtils.trimToEmpty(productId)));
			}

		} finally {
			getQicsFrame().setCursor(
					Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			getQicsPanel().getDefectScanInputPane().resetEntryTextField();
			if (reset) {
				getQicsPanel().getDefectScanInputPane().resetPanel();
			}
		}
	}

	private AbstractPanelAction getActionClass(String text) {
		for (Map.Entry<String, AbstractPanelAction> entry : getActionsMap().entrySet())
		{
			if(entry.getKey().equalsIgnoreCase(text))
			{
				return (AbstractPanelAction)entry.getValue();
			}
		}
		return null;
	}

	@Override
	protected DefectScanTextPanel getQicsPanel() {
		return (DefectScanTextPanel) super.getQicsPanel();
	}

	public Map<String, AbstractPanelAction> getActionsMap() {
		return actionsMap;
	}

	public void setActionsMap(Map<String, AbstractPanelAction> actionsMap) {
		this.actionsMap = actionsMap;
	}
}

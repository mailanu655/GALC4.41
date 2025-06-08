package com.honda.galc.client.qics.view.screen;

import com.honda.galc.client.qics.view.constants.QicsViewId;
import com.honda.galc.client.qics.view.frame.QicsFrame;

/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>PreCheckResultsPanel</code> is ...
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TR>
 * <TD>Karol Wozniak</TD>
 * <TD>Sep 23, 2009</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */
public class PreCheckResultsPanel extends CheckResultsPanel {

	private static final long serialVersionUID = 1L;

	public PreCheckResultsPanel(QicsFrame frame) {
		super(frame);
	}
	
	@Override
	public QicsViewId getQicsViewId() {
		return QicsViewId.PRE_CHECK_RESULTS;
	}
	
	@Override
	public void startPanel() {

		if (getQicsController().isRefreshProductPreCheckResults()) {
			getQicsController().submitPreCheckProductState();
			getQicsController().setRefreshProductPreCheckResults(false);			
		}
		
		boolean emptyPreCheckResults = getQicsController().getProductPreCheckResults() == null || getQicsController().getProductPreCheckResults().isEmpty();
		boolean emptyWarnCheckResults = getQicsController().getProductWarnCheckResults() == null || getQicsController().getProductWarnCheckResults().isEmpty();
		if (emptyPreCheckResults && emptyWarnCheckResults) {
			getQicsFrame().getMainPanel().removePanel(QicsViewId.PRE_CHECK_RESULTS);
			return;
		} 
		getCheckResultsPane().setProductPreCheckResults(getQicsController().getProductPreCheckResults());
		getCheckResultsPane().setProductWarnCheckResults(getQicsController().getProductWarnCheckResults());
		getCheckResultsPane().reloadPreCheckData();
		
		setButtonsState();
		//check if current engine is not in Cure Complete status, then disable "Direct Pass" button and other configured tabs.
		if(getQicsController().getProductPreCheckResults() != null && !getQicsController().getProductPreCheckResults().isEmpty()){
			String preCheckResult = getQicsController().getProductPreCheckResults().values().toString();
			if(preCheckResult.contains("is not in Cure Complete status") || preCheckResult.contains("has no status information")){
				disableButtonAndPanels();
			}
		}
		
		getQicsController().setRefreshProductPreCheckResults(true);
	}
	
	public void disableButtonAndPanels(){
		//disable "Direct Pass" button
		getQicsFrame().getMainPanel().getSubmitButtonsPanel().setButtonsEnabled(false);
		getQicsFrame().getMainPanel().getSubmitButtonsPanel().getCancelButton().setEnabled(true);
		setFocus(getQicsFrame().getMainPanel().getSubmitButtonsPanel().getCancelButton());
		//disable other tabs 
		int count = getQicsFrame().getMainPanel().getTabbedPane().getTabCount();
		if(count >1){
			for(int i=1; i < count; i++){
				getQicsFrame().getMainPanel().getTabbedPane().setEnabledAt(i, false);
			}
		}
	}
}

package com.honda.galc.client.qics.view.screen;

import com.honda.galc.client.qics.view.constants.QicsViewId;
import com.honda.galc.client.qics.view.fragments.CheckResultsPane;
import com.honda.galc.client.qics.view.frame.QicsFrame;

/**
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>ProductCheckResultPanel</code> is ...
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
 * <TD>Apr 17, 2008</TD>
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
public class CheckResultsPanel extends QicsPanel {

	private static final long serialVersionUID = 1L;

	private CheckResultsPane checkResultsPane;

	public CheckResultsPanel(QicsFrame frame) {
		super(frame);
		initialize();
	}

	@Override
	public QicsViewId getQicsViewId() {
		return QicsViewId.CHECK_RESULTS;
	}

	@Override
	public void setButtonsState() {
		getQicsFrame().getMainPanel().setButtonsState();
	}

	protected void initialize() {
		setLayout(null);
		setSize(getTabPaneWidth(), getTabPaneHeight());
		checkResultsPane = createCheckResultsPane();
		add(getCheckResultsPane());
		mapActions();
	}

	@Override
	public void startPanel() {

		if (getQicsController().isRefreshProductCheckResults()) {
			getQicsController().submitWarnCheckProductState();
			getQicsController().submitItemCheckProductState();
//			if (getQicsController().isErrorMsgExists()) {
//				getQicsFrame().setErrorMessage(getQicsController().getErrorMessageId());
//				return;
//			}
		}

		if (getQicsController().getProductItemCheckResults().isEmpty() && getQicsController().getProductWarnCheckResults().isEmpty() && getClientConfig().isDunnageRequired()) {
			getQicsFrame().getMainPanel().displayDunnagePanel();
			return;
		} 

		getCheckResultsPane().setProductItemCheckResults(getQicsController().getProductItemCheckResults());
		getCheckResultsPane().setProductWarnCheckResults(getQicsController().getProductWarnCheckResults());
		getCheckResultsPane().reloadProductCheckData();
		
		setButtonsState();
		getQicsController().setRefreshProductCheckResults(true);

		if (!getQicsController().getProductItemCheckResults().isEmpty()) {
			if (getClientConfig().isOffProcessPointIdDefined()) 
				getQicsFrame().setErrorMessage("Product will not be processed OFF");
			else
				getQicsFrame().setErrorMessage("Outstanding items exist");
		}
	}

	public CheckResultsPane getCheckResultsPane() {
		return checkResultsPane;
	}

	// === factory methods === //
	protected CheckResultsPane createCheckResultsPane() {
		CheckResultsPane pane = new CheckResultsPane();
		pane.setSize(1000, 510);
		pane.setLocation(0, 0);
		return pane;
	}
}

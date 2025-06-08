package com.honda.galc.client.qics.view.screen;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;

import com.honda.galc.client.qics.view.constants.QicsViewId;
import com.honda.galc.client.qics.view.frame.QicsFrame;
import com.honda.galc.client.teamleader.recovery.frame.DataRecoveryController;
import com.honda.galc.client.teamleader.recovery.frame.ObjectFactory;
import com.honda.galc.client.teamleader.recovery.frame.PartDataPanel;
import com.honda.galc.client.teamleader.recovery.frame.ProductRecoveryConfig;
import com.honda.galc.client.teamleader.recovery.frame.ProductRecoveryConfigProvider;
import com.honda.galc.data.ProductType;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>RecoveryPanel</code> is ...
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TD>Mar 24, 2009</TD>
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
public class RecoveryPanel extends QicsPanel {

	private static final long serialVersionUID = 1L;

	private JPanel dataPanelContainer;
	private PartDataPanel dataPanel;

	public RecoveryPanel(QicsFrame frame) {
		super(frame);
		initialize();
	}

	@Override
	public QicsViewId getQicsViewId() {
		return QicsViewId.DC_RECOVERY;
	}

	protected void initialize() {
		this.dataPanelContainer = new JPanel(new MigLayout("insets 0 0 0 0", "[990!]"));
		setLayout(new GridLayout());
		JScrollPane panel = new JScrollPane();
		panel.setViewportView(getDataPanelContainer());
		panel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		panel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		add(panel);
		renderProductPanel();
	}

	protected void renderProductPanel() {
		if (getDataPanel() != null) {
			getDataPanelContainer().remove(getDataPanel());
		}
		this.dataPanel = createProductDataPanel();
		getDataPanelContainer().add(getDataPanel());
	}

	@Override
	public void startPanel() {
		getDataPanel().getController().setProduct(getQicsController().getProductModel().getProduct());
		getDataPanel().getController().selectBuildResults(dataPanel.getPartNames());
		getDataPanel().setInputMode();
		setButtonsState();
	}

	@Override
	public void setButtonsState() {
		super.setButtonsState();
		getQicsFrame().getMainPanel().setButtonsState();
	}

	protected PartDataPanel createProductDataPanel() {
		ProductType productType = getQicsFrame().getProductType();
		DataRecoveryController controller = new DataRecoveryController(getQicsFrame(), productType);

		String modelCode = null;
		if (getProductModel() != null && getProductModel().getProduct() != null) {
			modelCode = getProductModel().getProduct().getModelCode();
		}
		ProductRecoveryConfig recoveryConfig = ProductRecoveryConfigProvider.createProductRecoveryConfig(getClientConfig().getProcessPoint(), productType, modelCode);
		return ObjectFactory.createProductDataPanel(recoveryConfig, controller);
	}

	@Override
	public void startProduct() {
		super.startProduct();
		renderProductPanel();
	}

	protected PartDataPanel getDataPanel() {
		return dataPanel;
	}

	protected JPanel getDataPanelContainer() {
		return dataPanelContainer;
	}
}

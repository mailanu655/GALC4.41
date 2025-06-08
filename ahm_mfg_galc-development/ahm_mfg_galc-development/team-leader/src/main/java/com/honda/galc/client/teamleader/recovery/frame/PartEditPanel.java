package com.honda.galc.client.teamleader.recovery.frame;

import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientMain;
import com.honda.galc.client.ui.LoginDialog;
import com.honda.galc.client.ui.component.FontSizeHandler;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.ProductBuildResultDao;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.enumtype.LoginStatus;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * 
 * <h3>PartEditPanel Class description</h3>
 * <p>
 * PartEditPanel description
 * </p>
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
 * 
 * </TABLE>
 * 
 * @author Jeffray Huang<br>
 *         Dec 16, 2011
 * 
 * 
 */

public class PartEditPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private static final List<String> PASSWORD_PROTECTED_PARTS = Arrays.asList(PropertyService.getProperty(ClientMain.getInstance().getApplicationContext().getProcessPointId(), "PASSWORD_PROTECTED_PARTS", "").replaceAll("_", " ").split(","));
	private static final String AUTHORIZATION_GROUPS = PropertyService.getProperty(ClientMain.getInstance().getApplicationContext().getProcessPointId(), "AUTHORIZATION_GROUP", "");

	private String authorizedUser;
	
	private String labelText;
	private JLabel label;
	private JButton button;

	private PartDefinition partDefinition;
	private PartDefinition[] multiPartDefinitions;
	private boolean renderMultiPartLabels;
	private int multiPartRowCount;

	private PartView partView;
	private MultiPartView multiPartView;

	boolean partEditable;
	boolean multiPartEditable;
	boolean unique;



	private PartDataPanel parentPanel;

	protected enum ActionId {
		TOGGLE, EDIT, SAVE;
		public String getLabel() {
			return String.format("%s%s", name().substring(0, 1), name().substring(1).toLowerCase());
		}

		public String getCommand() {
			return name();
		}
	};

	public PartEditPanel(PartDataPanel parentPanel, PartDefinition partDefinition) {
		this.parentPanel = parentPanel;
		this.labelText = partDefinition.getLabel();
		this.partEditable = partDefinition.isEditable();
		this.partDefinition = StringUtils.isBlank(partDefinition.getName()) ? null : partDefinition;
		this.multiPartEditable = partDefinition.isMultiPartEditable();
		this.multiPartRowCount = partDefinition.getRowCount() < 1 ? 1 : partDefinition.getRowCount();
		this.multiPartDefinitions = partDefinition.getMultiParts() != null ? partDefinition.getMultiParts() : null;
		this.unique = partDefinition.isUnique();
		setBorder(BorderFactory.createEtchedBorder());
		setLayout(createLayout());
		initView();
		mapActions();
		setIdleMode();
	}
	
	// === ui construction api === //
	protected void initView() {
		if (StringUtils.isBlank(getLabelText()) && getPartDefinition() != null) {
			setLabelText(getPartDefinition().getLabel());
		}
		label = new JLabel(getLabelText());
		button = new JButton();

		getLabel().addComponentListener(new FontSizeHandler(0.6f));
		getButton().addComponentListener(new FontSizeHandler(0.6f));
		add(getLabel(), new CC().height("max").alignX("left"));
		if (getPartDefinition() != null && StringUtils.isNotBlank(getPartDefinition().getName())) {
			partView = new PartView(getPartDefinition(), getPartDefinition().isStatus(), getPartDefinition().isValue(), Utils.getStatusFieldWidth());
			add(getPartView());
		}

		if (isMultiPart()) {
			multiPartView = new MultiPartView(getMultiPartRowCount(), isRenderMultiPartLabels(), getMultiPartDefinitions());
			add(getMultiPartView());
		}
		if (isPartEditable() || isMultiPartEditable()) {
			add(getButton(), new CC().height("max"));
		}
		setBorder(BorderFactory.createEtchedBorder());
	}

	protected LayoutManager createLayout() {
		int labelColWidth = 205;
		int buttonColWidth = Utils.getButtonWidth();
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("20[%s!]10", labelColWidth));

		if (getPartDefinition() != null) {
			if (getPartDefinition().isStatusOnly() && isMultiPart()) {
				int statusWidth = Utils.getStatusFieldWidth();
				sb.append("[").append(statusWidth).append("!").append("]");
			} else {
				sb.append(String.format("[max,fill]"));
			}
		}
		if (isMultiPart()) {
			sb.append(String.format("[max,fill]"));
		}

		sb.append(String.format("10[%s!,fill]15", buttonColWidth));
		MigLayout layout = new MigLayout("insets 5 , gap 0", sb.toString());
		return layout;
	}

	// === listeners === //
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (ActionId.TOGGLE.getCommand().equals(command)) {
			toggle();
		} else if (ActionId.EDIT.getCommand().equals(command)) {
			edit();
		} else if (ActionId.SAVE.getCommand().equals(command)) {
			save();
		}
	}

	protected void mapActions() {
		getButton().addActionListener(this);
	}

	protected void toggle() {
		if (PASSWORD_PROTECTED_PARTS.contains(getPartView().getPartDefinition().getName()) && !login()) return;
		List<ProductBuildResult> buildResults = new ArrayList<ProductBuildResult>();
		if (getPartView() != null && isPartEditable()) {
			ProductBuildResult result = toggleProductBuildResultData(getPartView());
			buildResults.add(result);
		}
		if (getMultiPartView() != null && isMultiPartEditable()) {
			for (PartView partView : getMultiPartView().getPartViews()) {
				if (partView.getPartDefinition().isEditable()) {
					ProductBuildResult result = toggleProductBuildResultData(partView);
					buildResults.add(result);
				}
			}
		}
		getController().updateBuildResults(buildResults);
		setReadOnlyMode();
	}

	protected void edit() {
		PartView partView = getPartView();
		if (partView != null && PASSWORD_PROTECTED_PARTS.contains(partView.getPartDefinition().getName()) && !login()) return;
		setEditMode();
		getButton().setText(ActionId.SAVE.getLabel());
		getButton().setActionCommand(ActionId.SAVE.getCommand());
		JComponent comp = getFirstFocusableComponent();
		if (comp != null) {
			comp.requestFocus();
		}
	}
	
	private boolean login() {
		authorizedUser = null;
		LoginStatus loginStatus = LoginDialog.login(null, true, true, false);

		if (loginStatus != LoginStatus.OK) return false;
		
		if (AUTHORIZATION_GROUPS.length() > 0) {
			if (ClientMain.getInstance().getAccessControlManager().isAuthorized(AUTHORIZATION_GROUPS)) {
				authorizedUser = ClientMain.getInstance().getAccessControlManager().getUserName();
				Logger.getLogger().info("User:" + authorizedUser + " logged in.");
				return true;
			}
			
			JOptionPane.showMessageDialog(null, "Terminating application! \nYou have no access permission of default application of this terminal",
					"Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		return true;
	}

	protected void save() {
		List<ProductBuildResult> buildResults = new ArrayList<ProductBuildResult>();
		String validationMsg = validateInput();
		if (StringUtils.isNotBlank(validationMsg)) {
			JOptionPane.showMessageDialog(this, validationMsg, "Save Build Results", JOptionPane.ERROR_MESSAGE);
			JTextField tf = getFirstTextFieldInState(TextFieldState.ERROR);
			if (tf != null) {
				tf.requestFocus();
			}
			return;
		}

		if (getPartView() != null && isPartEditable()) {
			ProductBuildResult result = setProductBuildResultData(getPartView());
			buildResults.add(result);
		}
		if (getMultiPartView() != null && isMultiPartEditable()) {
			for (PartView partView : getMultiPartView().getPartViews()) {
				if (partView.getPartDefinition().isEditable()) {
					ProductBuildResult result = setProductBuildResultData(partView);
					buildResults.add(result);
				}
			}
		}
		getController().updateBuildResults(buildResults);

		setReadOnlyMode();
		getButton().setText(ActionId.EDIT.getLabel());
		getButton().setActionCommand(ActionId.EDIT.getCommand());
	}

	protected String validateInput() {
		StringBuilder msgs = new StringBuilder();

		if (getPartView() != null && isPartEditable()) {
			String msg = getPartView().validateInput();
			if (StringUtils.isNotBlank(msg)) {
				msgs.append(msg);
			}
			
			if(isUnique()){
				msg = checkDuplicatedPart();
				if (StringUtils.isNotBlank(msg)) msgs.append(msg);

			}
		}

		if (getMultiPartView() != null && isMultiPartEditable()) {
			for (PartView partView : getMultiPartView().getPartViews()) {
				String msg = partView.validateInput();
				if (StringUtils.isNotBlank(msg)) {
					if (msgs.length() > 0) {
						msgs.append("\n");
					}
					msgs.append(msg);
				}
			}
		}
		return msgs.toString();
	}

	private String checkDuplicatedPart() {
		ProductBuildResultDao buildResultDao = ProductTypeUtil.getProductBuildResultDao(getParentPanel().getController().getProductType());
		ProductBuildResult result = setProductBuildResultData(getPartView());
		
		//Exit if the Status is NG and the string is empty
		if (result.getInstalledPartStatus() == InstalledPartStatus.NG && result.getPartSerialNumber().length() == 0) {
		return "";
		}
		
		List<ProductBuildResult> buildResults = buildResultDao.findAllByPartNameAndSerialNumber(result.getPartName(), result.getPartSerialNumber());
		
		String dupMsg="";
		if (buildResults != null && buildResults.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (ProductBuildResult productBuildResult : buildResults) {
				if (productBuildResult.getProductId().equals(result.getProductId())) {
					continue;
				}
				if (sb.length() > 0) {
					sb.append(", ");
				}
				sb.append(productBuildResult.getProductId());
			}
			if (sb.length() > 0) {
				 dupMsg = String.format(" Part %s: %s is already associated with product: %s", result.getPartName(), result.getPartSerialNumber(), sb.toString());
				
			}
		}
		
		return dupMsg;
	}

	protected ProductBuildResult toggleProductBuildResultData(PartView partView) {
		ProductBuildResult result = getController().getProductBuildResult(partView.getPartDefinition());
		if (result == null) {
			result = getController().constructProductBuildResult(partView.getPartDefinition().getName());
		}
		if (result.getInstalledPartStatusId() != null && result.isStatusOk()) {
			result.setInstalledPartStatusId(InstalledPartStatus.NG.getId());
		} else {
			result.setInstalledPartStatusId(InstalledPartStatus.OK.getId());
		}
		return result;
	}

	protected ProductBuildResult setProductBuildResultData(PartView partView) {

		ProductBuildResult result = getController().getProductBuildResult(partView.getPartDefinition());
		if (result == null) {
			result = getController().constructProductBuildResult(partView.getPartDefinition().getName());
		}

		if (partView.isRenderStatus()) {
			Object selectedItem = partView.getStatusInput().getSelectedItem();
			result.setInstalledPartStatusId(BuildAttributeStatus.OK.equals(selectedItem) ? InstalledPartStatus.OK.getId() : InstalledPartStatus.NG.getId());
		} else {
			result.setInstalledPartStatusId(InstalledPartStatus.OK.getId());
		}

		if (partView.isRenderValue()) {
			StringBuilder sb = new StringBuilder();
			if (partView.getValueFields().size() == 1) {
				sb.append(partView.getValueFields().get(0).getText().trim());
			} else {
				for (JTextField field : partView.getValueFields()) {
					if (sb.length() > 0) {
						sb.append(partView.getPartDefinition().getDelimiter());
					}
					sb.append(field.getText().trim());
				}
			}
			result.setResultValue(sb.toString());
		}
		return result;
	}

	// === state control === //
	public void setIdleMode() {
		if (getPartView() != null) {
			getPartView().setIdleMode();
		}
		if (getMultiPartView() != null) {
			getMultiPartView().setIdleMode();
		}
		initButtonState();
		getButton().setEnabled(false);
	}

	public void setReadOnlyMode() {
		setIdleMode();
		if (getPartDefinition() != null) {
			getPartView().setBuildResult(getController().getProductBuildResult(getPartView().getPartDefinition()));
			getPartView().setReadOnlyMode();

		}
		if (getMultiPartView() != null) {
			for (PartView view : getMultiPartView().getPartViews()) {
				view.setBuildResult(getController().getProductBuildResult(view.getPartDefinition()));
			}
			getMultiPartView().setReadOnlyMode();
		}
		initButtonState();
		getButton().setEnabled(true);
	}

	public void setEditMode() {
		if (getPartDefinition() != null && isPartEditable()) {
			getPartView().setEditMode();
		}
		if (getMultiPartView() != null && isMultiPartEditable()) {
			getMultiPartView().setEditMode();
		}
	}

	protected void initButtonState() {
		if (isStatusOnly() && !isMultiPartEditable()) {
			getButton().setText(ActionId.TOGGLE.getLabel());
			getButton().setActionCommand(ActionId.TOGGLE.getCommand());
		} else {
			getButton().setText(ActionId.EDIT.getLabel());
			getButton().setActionCommand(ActionId.EDIT.getCommand());
		}
	}

	// === config control === //
	protected JComponent getFirstFocusableComponent() {
		JComponent comp = null;
		if (getPartView() != null) {
			comp = getPartView().getFirstFocusableComponent();
			if (comp != null) {
				return comp;
			}
		}
		if (getMultiPartView() != null) {
			comp = getMultiPartView().getFirstFocusableComponent();
			return comp;
		}
		return comp;
	}

	protected JTextField getFirstTextFieldInState(TextFieldState state) {
		if (state == null) {
			return null;
		}
		JTextField comp = null;
		if (getPartView() != null) {
			comp = getPartView().getFirstTextFieldInState(state);
			if (comp != null) {
				return comp;
			}
		}
		if (getMultiPartView() != null) {
			comp = getMultiPartView().getFirstTextFieldInState(state);
			return comp;
		}
		return comp;
	}

	protected boolean isMultiPart() {
		return getMultiPartDefinitions() != null && getMultiPartDefinitions().length > 0;
	}

	protected boolean isStatusOnly() {
		if (getPartDefinition() == null && (getMultiPartDefinitions() == null || getMultiPartDefinitions().length == 0)) {
			return false;
		}
		if (getPartDefinition() != null && isPartEditable() && !getPartDefinition().isStatusOnly()) {
			return false;
		}
		if (isMultiPartEditable() && getMultiPartDefinitions() != null) {
			for (PartDefinition pd : getMultiPartDefinitions()) {
				if (pd != null && !pd.isStatusOnly()) {
					return false;
				}
			}
		}
		return true;
	}

	// === get/set === //
	protected DataRecoveryController getController() {
		return getParentPanel().getController();
	}

	public List<String> getPartNames() {
		List<String> partNames = new ArrayList<String>();
		if (getPartDefinition() != null) {
			partNames.add(getPartDefinition().getName());
		}
		if (getMultiPartDefinitions() == null) {
			return partNames;
		}
		for (PartDefinition partDef : getMultiPartDefinitions()) {
			partNames.add(partDef.getName());
		}
		return partNames;
	}

	protected String getLabelText() {
		return labelText;
	}

	protected void setLabelText(String labelText) {
		this.labelText = labelText;
	}

	protected JButton getButton() {
		return button;
	}

	protected PartDefinition getPartDefinition() {
		return partDefinition;
	}

	protected PartDefinition[] getMultiPartDefinitions() {
		return multiPartDefinitions;
	}

	protected int getMultiPartRowCount() {
		return multiPartRowCount;
	}

	protected PartView getPartView() {
		return partView;
	}

	protected MultiPartView getMultiPartView() {
		return multiPartView;
	}

	protected boolean isPartEditable() {
		return partEditable;
	}

	protected boolean isMultiPartEditable() {
		return multiPartEditable;
	}

	protected boolean isRenderMultiPartLabels() {
		return renderMultiPartLabels;
	}

	protected JLabel getLabel() {
		return label;
	}

	protected PartDataPanel getParentPanel() {
		return parentPanel;
	}
	
	public boolean isUnique() {
		return unique;
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}
}

package com.honda.galc.client.teamleader.fx.dataRecovery;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.ProductBuildResult;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;

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
 * @author L&T Infotech<br>
 *         July 15, 2017
 * 
 */

public class PartEditPanel extends MigPane implements EventHandler<ActionEvent> {
	
	private String labelText;
	private LoggedLabel label;
	private LoggedButton button;
	
	private PartDefinition partDefinition;
	private PartDefinition[] multiPartDefinitions;
	private boolean renderMultiPartLabels;
	private int multiPartRowCount;

	private PartView partView;
	private MultiPartView multiPartView;

	boolean partEditable;
	boolean multiPartEditable;

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
			createLayout();
			initView();
			mapActions();
			setIdleMode();
		}
		
		protected void initView() {
			if (StringUtils.isBlank(getLabelText()) && getPartDefinition() != null) {
				setLabelText(getPartDefinition().getLabel());
			}
			label = new LoggedLabel(getLabelText(),getLabelText());
			label.setPrefSize(Utils.getScreenWidth() * 0.10 , Utils.getScreenHeight() * 0.05);
			Utils.setFontSize(getLabel());
			
			button = Utils.createButton(null, this);
			
			add(getLabel(), new CC().alignX("left").minWidth((Utils.getScreenWidth() * 0.15)+"px").gap("4"));
			if (getPartDefinition() != null && StringUtils.isNotBlank(getPartDefinition().getName())) {
				partView = new PartView(getPartDefinition(), getPartDefinition().isStatus(), getPartDefinition().isValue(), 20);
				if (isMultiPart()) {
				add(getPartView(),  new CC().minWidth((Utils.getScreenWidth() * 0.12)+"px"));
				}
				if (!isMultiPart()) {
					add(getPartView(),  new CC().minWidth((Utils.getScreenWidth() * 0.72)+"px"));
				}
			}

			if (isMultiPart()) {
				multiPartView = new MultiPartView(getMultiPartRowCount(), isRenderMultiPartLabels(), getMultiPartDefinitions());
				if (getPartDefinition() != null && StringUtils.isNotBlank(getPartDefinition().getName())) {
					add(getMultiPartView(), new CC().minWidth((Utils.getScreenWidth() * 0.60)+"px").
							minHeight((Utils.getScreenWidth() * multiPartRowCount *0.037)+"px"));
				}
				else{
					add(getMultiPartView(),  new CC().minWidth((Utils.getScreenWidth() * 0.72)+"px")
							.minHeight((Utils.getScreenWidth() * multiPartRowCount *0.035)+"px"));
				}
			}
			if (isPartEditable() || isMultiPartEditable()) {
				add(getButton(),new CC().minWidth((Utils.getScreenWidth() * 0.11)+"px"));
					
			}
			this.setStyle("-fx-border-color: lightGray"); 
		}

		protected void createLayout() {
			this.setLayoutConstraints(new LC().insets("5"));
			this.setPrefWidth(Utils.getScreenWidth() - 20);
			this.setMinHeight((Utils.getScreenWidth() * multiPartRowCount *0.042));
		}

		// === listeners === //
		public void actionPerformed(ActionEvent e) {
			String command = ((LoggedButton)e.getSource()).getText();
			if (ActionId.TOGGLE.getCommand().equals(command)) {
				toggle();
			} else if (ActionId.EDIT.getCommand().equals(command)) {
				edit();
			} else if (ActionId.SAVE.getCommand().equals(command)) {
				save();
			}
		}

		protected void mapActions() {
			getButton().setOnAction(this);
		}

		protected void toggle() {
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
			setEditMode();
			getButton().setText(ActionId.SAVE.getLabel());
			Node node = getFirstFocusableComponent();
			if (node != null) {
				node.requestFocus();
			}
		}

		protected void save() {
			List<ProductBuildResult> buildResults = new ArrayList<ProductBuildResult>();
			String validationMsg = validateInput();
			if (StringUtils.isNotBlank(validationMsg)) {
				MessageDialog.showError(validationMsg);
				LoggedTextField tf = getFirstTextFieldInState(TextFieldState.ERROR);
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
		}

		protected String validateInput() {
			StringBuilder msgs = new StringBuilder();

			if (getPartView() != null && isPartEditable()) {
				String msg = getPartView().validateInput();
				if (StringUtils.isNotBlank(msg)) {
					msgs.append(msg);
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
				Object selectedItem = partView.getStatusInput().getSelectionModel().getSelectedItem();
				result.setInstalledPartStatusId(BuildAttributeStatus.OK.toString().equals(selectedItem.toString()) ? InstalledPartStatus.OK.getId() : InstalledPartStatus.NG.getId());
			} else {
				result.setInstalledPartStatusId(InstalledPartStatus.OK.getId());
			}

			if (partView.isRenderValue()) {
				StringBuilder sb = new StringBuilder();
				if (partView.getValueFields().size() == 1) {
					sb.append(partView.getValueFields().get(0).getText().trim());
				} else {
					for (LoggedTextField field : partView.getValueFields()) {
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
			getButton().setDisable(true);
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
			getButton().setDisable(false);
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
				getButton().setOnAction(this);
			} else {
				getButton().setText(ActionId.EDIT.getLabel());
				getButton().setOnAction(this);
			}
		}


		protected LoggedTextField getFirstTextFieldInState(TextFieldState state) {
			if (state == null) {
				return null;
			}
			LoggedTextField comp = null;
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
		
		@Override
		public void handle(ActionEvent actionEvent) {
			String command = ((LoggedButton)actionEvent.getSource()).getText();
			if (ActionId.TOGGLE.getCommand().equalsIgnoreCase(command)) {
				toggle();
			} else if (ActionId.EDIT.getCommand().equalsIgnoreCase(command)) {
				edit();
			} else if (ActionId.SAVE.getCommand().equalsIgnoreCase(command)) {
				save();
			}
		}
		
		
		// === config control === //
		protected Node getFirstFocusableComponent() {
			Node comp = null;
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

		protected LoggedButton getButton() {
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

		protected LoggedLabel getLabel() {
			return label;
		}

		protected PartDataPanel getParentPanel() {
			return parentPanel;
		}

		
}

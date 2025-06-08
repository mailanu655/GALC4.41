package com.honda.galc.client.qi.homescreen;

import java.util.List;

import com.honda.galc.client.qi.base.QiFxDialog;
import com.honda.galc.client.ui.component.FxDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.qi.QiEntryModelDao;
import com.honda.galc.entity.qi.QiEntryModel;
import com.honda.galc.entity.qi.QiEntryModelId;
import com.honda.galc.service.ServiceFactory;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

public class RefreshCacheDialog extends QiFxDialog<HomeScreenModel> implements EventHandler<ActionEvent> {
	private final String productType;
	private boolean cancelled;
	private QiEntryModel entryModel;
	private LoggedLabel entryModelLbl;
	private LoggedComboBox<QiEntryModel> entryModelCmbBx;
	private LoggedButton refreshCacheBtn;
	private LoggedButton cancelBtn;
	protected final static String CSS_PATH = "/resource/css/QiMainCss.css";

	public RefreshCacheDialog(String title, HomeScreenModel model, String applicationId, String productType) {
		super(title, applicationId, model);
		this.productType = productType;
		this.getScene().getStylesheets().add(CSS_PATH);
		initComponents();
	}

	private void initComponents() {
		HBox entryModelBox = new HBox(5);
		entryModelBox.setAlignment(Pos.CENTER);
		HBox.setHgrow(getEntryModelLbl(), Priority.ALWAYS);
		HBox.setHgrow(getEntryModelCmbBx(), Priority.ALWAYS);
		entryModelBox.getChildren().addAll(getEntryModelLbl(), getEntryModelCmbBx());

		HBox buttonBox = new HBox(5);
		buttonBox.setAlignment(Pos.CENTER);
		HBox.setHgrow(getRefreshCacheBtn(), Priority.ALWAYS);
		HBox.setHgrow(getCancelBtn(), Priority.ALWAYS);
		buttonBox.getChildren().addAll(getRefreshCacheBtn(), getCancelBtn());

		VBox mainBox = new VBox();
		mainBox.setAlignment(Pos.CENTER);
		mainBox.setPadding(new Insets(5, 5, 5, 5));
		mainBox.getChildren().addAll(entryModelBox, buttonBox);
		((BorderPane) this.getScene().getRoot()).setCenter(mainBox);
	}

	@Override
	public void showDialog() {
		this.cancelled = false;
		this.entryModel = null;
		getRefreshCacheBtn().setDisable(true);
		loadEntryModelData();
		super.showDialog();
	}

	private void refreshCache() {
		this.entryModel = (QiEntryModel) entryModelCmbBx.getSelectionModel().getSelectedItem();
		closeDialog();
	}

	private void cancel() {
		this.cancelled = true;
		closeDialog();
	}

	private void closeDialog() {
		Stage stage = (Stage) this.getScene().getWindow();
		stage.close();
	}

	public boolean isCancelled() {
		return this.cancelled;
	}

	public QiEntryModel getEntryModel() {
		return this.entryModel;
	}

	private void loadEntryModelData() {
		getEntryModelCmbBx().getItems().clear();
		QiEntryModelDao qiEntryModelDao = ServiceFactory.getDao(QiEntryModelDao.class);
		List<QiEntryModel> qiEntryModels = qiEntryModelDao.findAllByProductType(productType);
		getEntryModelCmbBx().getItems().addAll(qiEntryModels);
		QiEntryModel allEntryModels = new QiEntryModel(new QiEntryModelId(Delimiter.ASTERISK, (short) 0), "ALL ENTRY MODELS", productType, (short) 0);
		getEntryModelCmbBx().getItems().add(0, allEntryModels);
	}

	private LoggedLabel getEntryModelLbl() {
		if (entryModelLbl == null) {
			entryModelLbl = UiFactory.createLabel("entryModelLbl", "Entry Model");
			entryModelLbl.setPadding(new Insets(0, 0, 0, 0));
			entryModelLbl.getStyleClass().add("display-label");
		}
		return entryModelLbl;
	}

	private LoggedComboBox<QiEntryModel> getEntryModelCmbBx() {
		if (entryModelCmbBx == null) {
			entryModelCmbBx = new LoggedComboBox<QiEntryModel>("entryModelCmbBx");
			entryModelCmbBx.setOnAction(event -> refreshCacheBtn.setDisable(false));
			entryModelCmbBx.setButtonCell(createEntryModelCell());
			entryModelCmbBx.setCellFactory(new Callback<ListView<QiEntryModel>, ListCell<QiEntryModel>>() {
				@Override
				public ListCell<QiEntryModel> call(ListView<QiEntryModel> listView) {
					return createEntryModelCell();
				}
			});
		}
		return entryModelCmbBx;
	}
	
	private ListCell<QiEntryModel> createEntryModelCell() {
		return new ListCell<QiEntryModel>() {
			@Override public void updateItem(QiEntryModel item, boolean empty) {
				super.updateItem(item, empty);
				setText(item == null ? null : item.getId().getEntryModel());
			}
		};
	}

	private LoggedButton getRefreshCacheBtn() {
		if (refreshCacheBtn == null) {
			refreshCacheBtn = createBtn("Refresh Cache", (actionEvent) -> refreshCache());
			refreshCacheBtn.setPadding(new Insets(15, 15, 15, 15));
		}
		return refreshCacheBtn;
	}

	private LoggedButton getCancelBtn() {
		if (cancelBtn == null) {
			cancelBtn = createBtn("Cancel", (actionEvent) -> cancel());
			cancelBtn.setPadding(new Insets(15, 15, 15, 15));
		}
		return cancelBtn;
	}

	@Override
	public void handle(ActionEvent event) {
		// TODO Auto-generated method stub
	}
}

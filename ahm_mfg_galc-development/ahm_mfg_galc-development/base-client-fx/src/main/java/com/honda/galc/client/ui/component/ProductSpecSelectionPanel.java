package com.honda.galc.client.ui.component;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;

import com.honda.galc.client.data.ProductSpecData;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.product.EngineSpec;
import com.honda.galc.property.ApplicationPropertyBean;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>ProductSpecSelectionPanel Class description</h3>
 * <p>
 * ProductSpecSelectionPanel description
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
 * <TR>
 * <TD>Janak Bhalla & Alok Ghode</TD>
 * <TD>March 05, 2015</TD>
 * <TD>1.0</TD>
 * <TD>GY 20150305</TD>
 * <TD>Initial Realease</TD>
 * </TR>
 * </TABLE>
 * 
 */
public class ProductSpecSelectionPanel extends BorderPane {

	@FXML
	private ListView<String> modelTypeCodeList;

	@FXML
	private ListView<String> modelIntColorCodeList;

	@FXML
	private ListView<String> modelExtColorCodeList;

	@FXML
	private ListView<String> modelYearCodeList;

	@FXML
	private ListView<String> modelOptionCodeList;

	@FXML
	private Label modelTypeCodeLabel;

	@FXML
	private ListView<String> modelCodeList;

	@FXML
	private Label modelIntColorCodeLabel;

	@FXML
	private Label modelYearCodeLabel;

	@FXML
	private Label modelExtColorCodeLabel;

	@FXML
	private Label modelCodeLabel;

	@FXML
	private Label modelOptionCodeLabel;

	private ProductSpecData productSpecData = null;

	private static final String resourcePath = "/resource/%s.fxml";

	public ProductSpecSelectionPanel() {
		super();

		EventBusUtil.register(this);
		initComponent();
		addSelectionListeners();
	}

	public ProductSpecSelectionPanel(String productType) {
		this();
		productSpecData = new ProductSpecData(productType);
		modelYearCodeList.getItems()
				.addAll(productSpecData.getModelYearCodes());

	}

	public void initComponent() {
		URL url = getViewURL();
		FXMLLoader fxmlLoader = new FXMLLoader(url);
		fxmlLoader.setController(this);

		try {
			setCenter((Node) fxmlLoader.load());
		} catch (IOException exception) {
			exception.printStackTrace();
			throw new RuntimeException(exception);
		}

	}

	private void addSelectionListeners() {

		modelYearCodeList.getSelectionModel().getSelectedIndices()
				.addListener(new ListChangeListener<Integer>() {
					@Override
					public void onChanged(
							javafx.collections.ListChangeListener.Change<? extends Integer> arg0) {
						modelYearCodeChanged();

					}
				});

		modelCodeList.getSelectionModel().getSelectedIndices()
				.addListener(new ListChangeListener<Integer>() {
					@Override
					public void onChanged(
							javafx.collections.ListChangeListener.Change<? extends Integer> arg0) {
						modelCodeChanged();

					}
				});

		modelTypeCodeList.getSelectionModel().getSelectedIndices()
				.addListener(new ListChangeListener<Integer>() {
					@Override
					public void onChanged(
							javafx.collections.ListChangeListener.Change<? extends Integer> arg0) {
						modelTypeCodeChanged();

					}
				});

		modelOptionCodeList.getSelectionModel().getSelectedIndices()
				.addListener(new ListChangeListener<Integer>() {
					@Override
					public void onChanged(
							javafx.collections.ListChangeListener.Change<? extends Integer> arg0) {
						modelOptionCodeChanged();

					}
				});

		modelExtColorCodeList.getSelectionModel().getSelectedIndices()
				.addListener(new ListChangeListener<Integer>() {
					@Override
					public void onChanged(
							javafx.collections.ListChangeListener.Change<? extends Integer> arg0) {
						modelExtColorCodeChanged();

					}
				});

		modelIntColorCodeList.getSelectionModel().getSelectedIndices()
				.addListener(new ListChangeListener<Integer>() {
					@Override
					public void onChanged(
							javafx.collections.ListChangeListener.Change<? extends Integer> arg0) {
						modelIntColorCodeChanged();

					}
				});

	}

	public void modelYearCodeChanged() {
		String modelYearCode = (String) modelYearCodeList.getSelectionModel()
				.getSelectedItem();
		if (modelYearCode != null) {
			Logger.getLogger().info(
					"ModelYearCode " + modelYearCode + " is selected");
		}
		modelCodeList.getItems().clear();
		if (modelYearCode == null) {
			modelCodeList.getSelectionModel().clearSelection();
		} else {
			modelCodeList.getItems().addAll(
					productSpecData.getModelCodes(modelYearCode));
			modelCodeList.getSelectionModel().selectIndices(0);
		}

	}

	public void modelCodeChanged() {
		String modelYearCode = (String) modelYearCodeList.getSelectionModel()
				.getSelectedItem();
		String modelCode = (String) modelCodeList.getSelectionModel()
				.getSelectedItem();
		if (modelCode != null)
			Logger.getLogger().info("ModelCode " + modelCode + " is selected");
		modelTypeCodeList.getItems().clear();
		if (modelCode == null) {
			modelTypeCodeList.getSelectionModel().clearSelection();
		} else {
			modelTypeCodeList.getItems()
					.addAll(productSpecData.getModelTypeCodes(modelYearCode,
							modelCode));
			modelTypeCodeList.getSelectionModel().selectIndices(0);
		}

	}

	public void modelTypeCodeChanged() {
		String modelYearCode = (String) modelYearCodeList.getSelectionModel()
				.getSelectedItem();
		String modelCode = (String) modelCodeList.getSelectionModel()
				.getSelectedItem();
		ObservableList<String> modelTypeCodes = modelTypeCodeList
				.getSelectionModel().getSelectedItems();
		if (modelTypeCodes.size() > 0)
			Logger.getLogger().info(
					"ModelTypeCode " + modelTypeCodes.toString()
							+ " is selected");
		modelOptionCodeList.getItems().clear();
		if (modelTypeCodes.size() == 0) {
			modelOptionCodeList.getSelectionModel().clearSelection();
		} else {
			modelOptionCodeList.getItems().addAll(
					productSpecData.getModelOptionCodes(modelYearCode,
							modelCode, modelTypeCodes.toArray()));
			modelOptionCodeList.getSelectionModel().selectIndices(0);
		}
	}

	public void modelOptionCodeChanged() {

		String modelYearCode = (String) modelYearCodeList.getSelectionModel()
				.getSelectedItem();
		String modelCode = (String) modelCodeList.getSelectionModel()
				.getSelectedItem();
		ObservableList<String> modelTypeCodes = modelTypeCodeList
				.getSelectionModel().getSelectedItems();
		ObservableList<String> modelOptionCodes = modelOptionCodeList
				.getSelectionModel().getSelectedItems();
		modelExtColorCodeList.getItems().clear();
		if (modelOptionCodes.size() == 0) {
			modelExtColorCodeList.getSelectionModel().clearSelection();
		}

		else {
			modelExtColorCodeList.getItems().addAll(
					productSpecData.getModelExtColorCodes(modelYearCode,
							modelCode, modelTypeCodes.toArray(),
							modelOptionCodes.toArray()));
			modelExtColorCodeList.getSelectionModel().selectIndices(0);
		}
	}

	public void modelExtColorCodeChanged() {
		String modelYearCode = (String) modelYearCodeList.getSelectionModel()
				.getSelectedItem();
		String modelCode = (String) modelCodeList.getSelectionModel()
				.getSelectedItem();
		ObservableList<String> modelTypeCodes = modelTypeCodeList
				.getSelectionModel().getSelectedItems();
		ObservableList<String> modelOptionCodes = modelOptionCodeList
				.getSelectionModel().getSelectedItems();
		ObservableList<String> exitColorCodes = modelExtColorCodeList
				.getSelectionModel().getSelectedItems();
		modelIntColorCodeList.getItems().clear();
		if (exitColorCodes.size() == 0) {
			modelIntColorCodeList.getSelectionModel().clearSelection();
		} else {
			modelIntColorCodeList.getItems().addAll(
					productSpecData.getModelIntColorCodes(modelYearCode,
							modelCode, modelTypeCodes.toArray(),
							modelOptionCodes.toArray(),
							exitColorCodes.toArray()));
			modelIntColorCodeList.getSelectionModel().selectIndices(0);
		}
	}

	public void modelIntColorCodeChanged() {

	}

	public String getSelectedModelYearCode() {
		return (String) modelYearCodeList.getSelectionModel().getSelectedItem();
	}

	public String getSelectedModelCode() {
		return (String) modelCodeList.getSelectionModel().getSelectedItem();
	}

	public String getSelectedModelTypeCode() {
		return (String) modelTypeCodeList.getSelectionModel().getSelectedItem();
	}

	public String getSelectedModelOptionCode() {
		return (String) modelOptionCodeList.getSelectionModel()
				.getSelectedItem();
	}

	public String getSelectedExtColorCode() {
		return (String) modelExtColorCodeList.getSelectionModel()
				.getSelectedItem();
	}

	public String getSelectedIntColorCode() {
		return (String) modelIntColorCodeList.getSelectionModel()
				.getSelectedItem();
	}

	public ObservableList<String> getSelectedModelTypeCodes() {
		return modelTypeCodeList.getSelectionModel().getSelectedItems();
	}

	public ObservableList<String> getSelectedModelOptionCodes() {
		return modelOptionCodeList.getSelectionModel().getSelectedItems();
	}

	public ObservableList<String> getSelectedExtColorCodes() {
		return modelExtColorCodeList.getSelectionModel().getSelectedItems();
	}

	public ObservableList<String> getSelectedIntColorCodes() {
		return modelIntColorCodeList.getSelectionModel().getSelectedItems();
	}

	public List<EngineSpec> getSelectedEngineSpecData() {
		String modelYearCode = (String) modelYearCodeList.getSelectionModel()
				.getSelectedItem();
		String modelCode = (String) modelCodeList.getSelectionModel()
				.getSelectedItem();
		ObservableList<String> modelTypeCodes = modelTypeCodeList
				.getSelectionModel().getSelectedItems();
		ObservableList<String> modelOptionCodes = modelOptionCodeList
				.getSelectionModel().getSelectedItems();
		return productSpecData.getProductSpecData(modelYearCode, modelCode,
				modelTypeCodes.toArray(), modelOptionCodes.toArray());
	}

	public boolean isProductSpecSelected() {
		return getSelectedIntColorCodes().size() != 0;
	}

	public List<String> buildSelectedProductSpecCodes() {
		return productSpecData == null ? new ArrayList<String>()
				: productSpecData.getProductSpecData(
						getSelectedModelYearCode(), getSelectedModelCode(),
						getSelectedModelTypeCodes().toArray(),
						getSelectedModelOptionCodes().toArray(),
						getSelectedExtColorCodes().toArray(),
						getSelectedIntColorCodes().toArray());
	}

	public ApplicationPropertyBean getApplicationPropertyBean(
			String applicationId) {
		return PropertyService.getPropertyBean(ApplicationPropertyBean.class,
				applicationId);
	}

	public String getViewPath() {
		String fxmlFilepath = String.format(resourcePath, this.getClass()
				.getName().replace(".", "/"));
		return fxmlFilepath;
	}

	private URL getViewURL() {
		return getClass().getResource(getViewPath());
	}

}

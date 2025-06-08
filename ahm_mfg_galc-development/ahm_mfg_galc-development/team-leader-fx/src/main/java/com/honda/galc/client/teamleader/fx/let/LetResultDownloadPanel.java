package com.honda.galc.client.teamleader.fx.let;

import static com.honda.galc.client.teamleader.fx.let.LetResultDownloadModel.ATTR_EXP_VAL;
import static com.honda.galc.client.teamleader.fx.let.LetResultDownloadModel.ATTR_VAL;
import static com.honda.galc.client.teamleader.fx.let.LetResultDownloadModel.EXPECTED_VALUE;
import static com.honda.galc.client.teamleader.fx.let.LetResultDownloadModel.FAULT_CODE_DESC;
import static com.honda.galc.client.teamleader.fx.let.LetResultDownloadModel.FAULT_CODE_VAL;
import static com.honda.galc.client.teamleader.fx.let.LetResultDownloadModel.HIGH_LIMIT;
import static com.honda.galc.client.teamleader.fx.let.LetResultDownloadModel.LOW_LIMIT;
import static com.honda.galc.client.teamleader.fx.let.LetResultDownloadModel.PARAM_HI_LIMIT;
import static com.honda.galc.client.teamleader.fx.let.LetResultDownloadModel.PARAM_LO_LIMIT;
import static com.honda.galc.client.teamleader.fx.let.LetResultDownloadModel.PARAM_UNIT;
import static com.honda.galc.client.teamleader.fx.let.LetResultDownloadModel.PARAM_VAL;
import static com.honda.galc.client.teamleader.fx.let.LetResultDownloadModel.SHORT_DESC;
import static com.honda.galc.client.teamleader.fx.let.LetResultDownloadModel.UNIT;
import static com.honda.galc.client.teamleader.fx.let.LetResultDownloadModel.VALUE;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.data.ProductSpecData;
import com.honda.galc.client.teamleader.fx.let.LetResultDownload.DownloadStatus;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.CacheTablePane;
import com.honda.galc.client.ui.component.ColumnMapping;
import com.honda.galc.client.ui.component.DynamicTablePane;
import com.honda.galc.client.ui.component.FXOptionPane;
import com.honda.galc.client.ui.component.FXOptionPane.Response;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.cache.PersistentCache;
import com.honda.galc.entity.product.LetInspectionProgram;
import com.honda.galc.util.KeyValue;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>LetResultDownloadPanel</code> is ... .
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
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 * @created Mar 1, 2019
 */
public class LetResultDownloadPanel extends TabbedPanel {

    public static final String DOWNLOADS = "Downloads";
    public static final String RESULTS = "Results";
    public static final String JOB_RESULTS = RESULTS + "-Job %s";

    private static int LET_PGM_MAX_LENGTH = 75;
    private static int PRODUCT_ID_MAX_LENGTH = 17;

    private static String FONT_STYLE = "-fx-font-weight: bold; -fx-font-size: 11px;";
    public static final String INIT_MSG = "This screen will not reflect any changes made manually through GALC. \n" 
            + "Only results directly uploaded from LET device will be displayed.\n"
            + "If you have questions please contact your local GALC support member for details.";

    private Integer jobCounter;

    private TextField productId;
    private TextField programFilter;

    private DatePicker startDatePicker;
    private DatePicker endDatePicker;

    private ComboBox<String> yearCode;
    private ComboBox<String> modelCode;
    private ComboBox<String> typeCode;
    private ComboBox<String> optionCode;

    private TextField lineNo;
    private ComboBox<KeyValue<String, String>> production;

    private ComboBox<Integer> pageSize;

    private Button resetButton;
    private Button submitButton;
    private Button clearButton;

    private DynamicTablePane<LetInspectionProgram> letProgramTable;

    private DynamicTablePane<LetResultDownload> downloadJobTable;
    private CacheTablePane resultTable;

    private ContextMenu downloadJobTableContextMenu;
    private MenuItem viewMenuItem;
    private MenuItem exportMenuItem;
    private MenuItem deleteMenuItem;
    private MenuItem cancelMenuItem;

    private TabPane tabbedPane;
    private GridPane leftPanel;

    private List<ColumnMapping> resultTableStaticColumnMappings;

    private LetResultDownloadController controller;
    
    private boolean initMessageDisplayed;

    public LetResultDownloadPanel(MainWindow mainWindow) {
        super("Let Result Download", KeyEvent.VK_D, mainWindow);
        this.resultTableStaticColumnMappings = createResultTableStaticColumnMappings();
        this.jobCounter = 0;
        this.controller = new LetResultDownloadController(this);

        initUi();
        initData();
        mapHandlers();
    }

    @Override
    public void onTabSelected() {
        if (!isInitMessageDisplayed()) {
            String msg = INIT_MSG;
            Text text = new Text(msg);
            text.setId("fxoption-pane");
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    FXOptionPane.showMessageDialog(getMainWindow().getStage(), text, "", FXOptionPane.Type.INFORMATION);
                    setInitMessageDisplayed(true);
                }
            });
        }
    }

    protected void initUi() {
        this.tabbedPane = new TabPane();
        this.tabbedPane.setSide(Side.TOP);

        this.productId = createProductIdField("ProductId");
        this.programFilter = createProgramFilter();
        this.startDatePicker = createDatePicker();
        this.endDatePicker = createDatePicker();
        this.yearCode = createComboBox();
        this.modelCode = createComboBox();
        this.typeCode = createComboBox();
        this.optionCode = createComboBox();
        this.lineNo = createLineNo();
        this.production = createProductionComboBox();
        this.pageSize = createComboBox(100, true);
        this.letProgramTable = createProgramTable();

        this.downloadJobTable = createDownloadJobTable();
        this.downloadJobTableContextMenu = createDonwloadJobTableContextMenu();

        this.resultTable = createResultTable(null);

        this.resetButton = createButton("Reset", "resetButton", 220);
        this.submitButton = createButton("Download", "submitButton", 220);
        this.clearButton = createButton("Delete Downloads", "clearButton", 220);

        this.leftPanel = createLeftPanel();
        setLeft(getLeftPanel());
        setMargin(getLeftPanel(), new Insets(10, 0, 10, 0));

        Tab downloadTab = new Tab();
        downloadTab.setClosable(false);
        downloadTab.setContent(getDownloadJobTable());
        downloadTab.setText(DOWNLOADS);
        downloadTab.setStyle(FONT_STYLE);
        Tab resultTab = new Tab();
        resultTab.setClosable(false);
        resultTab.setContent(getResultTable());
        resultTab.setText(RESULTS);
        resultTab.setStyle(FONT_STYLE);

        getTabbedPane().getTabs().add(downloadTab);
        getTabbedPane().getTabs().add(resultTab);
        getTabbedPane().setStyle("-fx-padding: 10 10 10 10;");

        getTabbedPane().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
                if (t1.getContent().equals(getDownloadJobTable())) {
                    getClearButton().setText("Delete Downloads");
                    getPageSize().setDisable(true);
                } else if (t1.getContent().equals(getResultTable())) {
                    getClearButton().setText("Clear Results");
                    getPageSize().setDisable(false);
                } else {
                    getClearButton().setText("Clear");
                }

            }
        });
        setCenter(getTabbedPane());
    }

    protected void initData() {
        getController().loadinitData();
        getLetProgramTable().setItems(getPrograms());
        getYearCode().getItems().addAll(getSpecData().getModelYearCodes());
        getProduction().getItems().addAll(getProductionData());
        getPageSize().getItems().addAll(Arrays.asList(getController().getPropertyBean().getResultPageSizes()));
        getPageSize().setValue(getController().getPropertyBean().getDefaultResultPageSize());
    }

    // === event handlers === //
    protected void mapHandlers() {
        getResetButton().setOnAction(this);
        getClearButton().setOnAction(this);
        getSubmitButton().setOnAction(this);
        getYearCode().setOnAction(this);
        getModelCode().setOnAction(this);
        getTypeCode().setOnAction(this);
        getOptionCode().setOnAction(this);
        getPageSize().setOnAction(this);

        getDownloadJobTable().getTable().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.SECONDARY) {
                    LetResultDownload selectedItem = getDownloadJobTable().getSelectedItem();
                    if (selectedItem == null) {
                        return;
                    }

                    for (MenuItem item : getDownloadJobTableContextMenu().getItems()) {
                        item.setDisable(true);
                    }

                    DownloadStatus status = selectedItem.getStatus();
                    if (status.isViewable()) {
                        getViewMenuItem().setDisable(false);
                    }
                    if (status.isExportable()) {
                        getExportMenuItem().setDisable(false);
                    }
                    if (status.isDeletable()) {
                        getDeleteMenuItem().setDisable(false);
                    }
                    if (status.isCancelable()) {
                        getCancelMenuItem().setDisable(false);
                    }
                    getDownloadJobTableContextMenu().show(getDownloadJobTable(), event.getScreenX(), event.getScreenY());
                } else if (getDownloadJobTableContextMenu().isShowing()) {
                    getDownloadJobTableContextMenu().hide();
                }
            }
        });

        getProgramFilter().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                filterPrograms(oldValue, newValue);
            }
        });
    }

    @Override
    public void handle(ActionEvent event) {
        getMainWindow().getScene().setCursor(Cursor.WAIT);
        disableSource(event, true);
        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    handleEvent(event);
                                } finally {
                                    disableSource(event, false);
                                    getMainWindow().getScene().setCursor(Cursor.DEFAULT);
                                }
                            }
                        });
                        return null;
                    }
                };
            }
        };
        service.start();
    }

    public void handleEvent(ActionEvent event) {

        if (event.getSource().equals(getSubmitButton())) {
            submitDownloadJob();
        } else if (event.getSource().equals(getResetButton())) {
            reset();
        } else if (event.getSource().equals(getClearButton())) {
            clearTable();
        } else if (event.getSource().equals(getYearCode())) {
            yearCodeSelected();
        } else if (event.getSource().equals(getModelCode())) {
            modelCodeSelected();
        } else if (event.getSource().equals(getTypeCode())) {
            typeCodeSelected();
        } else if (event.getSource() instanceof MenuItem) {
            if (getViewMenuItem().equals(event.getSource())) {
                viewDownload();
            } else if (getExportMenuItem().equals(event.getSource())) {
                exportDownload();
            } else if (getDeleteMenuItem().equals(event.getSource())) {
                deleteDownload();
            } else if (getCancelMenuItem().equals(event.getSource())) {
                cancelDownload();
            }
        } else if (event.getSource().equals(getPageSize())) {
            changePageSize();
        }
    }

    protected void disableSource(ActionEvent event, boolean disable) {
        if (event == null) {
            return;
        }
        Object target = event.getSource();
        if (target instanceof Node) {
            Node node = (Node) target;
            node.setDisable(disable);
        }
    }

    protected void clearTable() {
        Tab tab = getTabbedPane().getSelectionModel().getSelectedItem();
        if (tab.getContent().equals(getDownloadJobTable())) {
            deleteCompletedDownloads();
        } else if (tab.getContent().equals(getResultTable())) {
            clearResultTable();
        }
    }

    protected void deleteCompletedDownloads() {
        List<LetResultDownload> downloads = getDownloadJobTable().getItems();
        if (downloads.isEmpty()) {
            return;
        }
        Response response = FXOptionPane.showConfirmDialog(getMainWindow().getStage(), "Are you sure you want to delete completed downloads?", "", FXOptionPane.Type.CONFIRM);
        if (!Response.YES.equals(response)) {
            return;
        }
        clearResultTable();
        getDownloadJobTable().getTable().scrollToColumnIndex(0);
        List<LetResultDownload> completedDownloads = new ArrayList<LetResultDownload>();
        for (LetResultDownload download : downloads) {
            if (download.getStatus().isDeletable()) {
                getController().deleteDownload(download);
                completedDownloads.add(download);
            }
        }
        getDownloadJobTable().removeItems(completedDownloads);
    }

    protected void clearResultTable() {
        getResultTable().getTable().scrollToColumnIndex(0);
        getResultTable().clearData();
        removeDynamicColumns();
        getResultTable().resizeColumns();
        getTabbedPane().getTabs().get(1).setText(RESULTS);
        getController().setDisplayedDownload(null);
        if (Logger.getLogger().isInfoEnabled()) {
            String msg = "LetDownload Cache - In Memory:" + getCache().getCache().getMemoryStoreSize();
            msg = msg + ",On Disk:" + getCache().getCache().getDiskStoreSize();
            Logger.getLogger().info(msg);
        }
        getCache().flush();
        if (Logger.getLogger().isInfoEnabled()) {
            String msg = "LetDownload Cache - In Memory:" + getCache().getCache().getMemoryStoreSize();
            msg = msg + ",On Disk:" + getCache().getCache().getDiskStoreSize();
            Logger.getLogger().info(msg);
        }
    }

    protected void removeDynamicColumns() {
        int columnCount = getResultTable().getTable().getColumns().size();
        List<TableColumn<Object, ?>> dynamicColumns = getResultTable().getTable().getColumns().subList(getResultTableStaticColumnMappings().size(), columnCount);
        getResultTable().removeColumns(dynamicColumns);
    }

    protected void reset() {
        getProduction().getSelectionModel().clearSelection();
        getLineNo().clear();
        getOptionCode().getSelectionModel().clearSelection();
        getTypeCode().getSelectionModel().clearSelection();
        getModelCode().getSelectionModel().clearSelection();
        getYearCode().getSelectionModel().clearSelection();
        getEndDatePicker().setValue(null);
        getEndDatePicker().getEditor().clear();
        getStartDatePicker().setValue(null);
        getStartDatePicker().getEditor().clear();
        getProductId().clear();
        getProgramFilter().clear();
    }

    protected void yearCodeSelected() {
        getModelCode().getItems().clear();
        String yearCode = getYearCode().getValue();
        if (StringUtils.isBlank(yearCode)) {
            return;
        }
        getModelCode().getItems().addAll(getSpecData().getModelCodes(yearCode));
    }

    protected void modelCodeSelected() {
        getTypeCode().getItems().clear();
        String yearCode = getYearCode().getValue();
        String modelCode = getModelCode().getValue();
        if (StringUtils.isBlank(modelCode)) {
            return;
        }
        getTypeCode().getItems().addAll(getSpecData().getModelTypeCodes(yearCode, modelCode));
    }

    protected void typeCodeSelected() {
        getOptionCode().getItems().clear();
        String yearCode = getYearCode().getValue();
        String modelCode = getModelCode().getValue();
        String typeCode = getTypeCode().getValue();
        if (StringUtils.isBlank(typeCode)) {
            return;
        }
        getOptionCode().getItems().addAll(getSpecData().getModelOptionCodes(yearCode, modelCode, new String[] { typeCode }));
    }

    protected void filterPrograms(String oldValue, String newValue) {
        String filter = getProgramFilter().getText();
        filter = StringUtils.trimToEmpty(filter);

        getLetProgramTable().clearData();
        List<LetInspectionProgram> filtered = new ArrayList<LetInspectionProgram>();
        if (StringUtils.isBlank(filter)) {
            filtered.addAll(getPrograms());
        } else {
            for (LetInspectionProgram pgm : getPrograms()) {
                if (StringUtils.containsIgnoreCase(pgm.getInspectionPgmName(), filter)) {
                    filtered.add(pgm);
                }
            }
        }
        getLetProgramTable().setItems(filtered);
    }

    protected void submitDownloadJob() {
        getController().submitDownloadJob();
    }

    protected void viewDownload() {
        LetResultDownload download = getDownloadJobTable().getSelectedItem();
        if (download == null) {
            return;
        }
        if (download.getDataSize() == 0) {
            FXOptionPane.showMessageDialog(getMainWindow().getStage(), "There is no data to view!", "", FXOptionPane.Type.WARNING);
            return;
        }
        setResultTableData(download);
        getTabbedPane().getSelectionModel().select(1);
        getTabbedPane().getTabs().get(1).setText(String.format(JOB_RESULTS, download.getJobId()));
        getController().setDisplayedDownload(download);
        if (Logger.getLogger().isInfoEnabled()) {
            String msg = "Let Cache - In Memory:" + getCache().getCache().getMemoryStoreSize();
            msg = msg + ",On Disk:" + getCache().getCache().getDiskStoreSize();
            Logger.getLogger().info(msg);
        }
    }

    protected void setResultTableData(LetResultDownload download) {
        clearResultTable();
        getResultTable().addColumns(getResultTableDynamicColumnMappings(download));
        getResultTable().setItems(download.getKeys());
        getResultTable().resizeColumns(getResultTableStaticColumnMappings().size(), 10);
        // REMARK : for wide tables, the last column sometimes is not visible
        // workaround - adjust width of the last column
        int lastIx = getResultTable().getTable().getColumns().size() - 1;
        int count = getResultTable().getTable().getColumns().size();
        getResultTable().getTable().getColumns().get(lastIx).setPrefWidth(count * 2.5);
    }

    protected void deleteDownload() {
        LetResultDownload download = getDownloadJobTable().getSelectedItem();
        if (download == null) {
            return;
        }
        Response response = FXOptionPane.showConfirmDialog(getMainWindow().getStage(), "Are you sure you want to remove download?", "", FXOptionPane.Type.CONFIRM);
        if (!Response.YES.equals(response)) {
            return;
        }
        if (download.equals(getController().getDisplayedDownload())) {
            clearResultTable();
        }
        if (DownloadStatus.RUNNING.equals(download.getStatus())) {
            FXOptionPane.showMessageDialog(getMainWindow().getStage(), "Please first cancel download and then delete!", "", FXOptionPane.Type.WARNING);
            return;
        }
        getController().deleteDownload(download);
        getDownloadJobTable().removeItem(download);
    }

    protected void cancelDownload() {
        LetResultDownload download = getDownloadJobTable().getSelectedItem();
        if (download == null) {
            return;
        }
        Response response = FXOptionPane.showConfirmDialog(getMainWindow().getStage(), "Are you sure you want to cancel download?", "", FXOptionPane.Type.CONFIRM);
        if (!Response.YES.equals(response)) {
            return;
        }
        synchronized (download.getStatus()) {
            if (!download.getStatus().isCancelable()) {
                return;
            }
            if (DownloadStatus.QUEUED.equals(download.getStatus())) {
                download.setStatus(DownloadStatus.CANCELED);
            } else {
                download.setStatus(DownloadStatus.CANCEL_PEND);
            }
        }
  //      getDownloadJobTable().getTable().refresh();
    }

    protected void changePageSize() {
        if (getPageSize().getValue() != null) {
            getResultTable().setPageSize(getPageSize().getValue());
            getResultTable().resetPagination();
        }
    }

    // === export file === //
    protected void exportDownload() {
        LetResultDownload download = getDownloadJobTable().getSelectedItem();
        if (download.getDataSize() == 0) {
            FXOptionPane.showMessageDialog(getMainWindow().getStage(), "There is no data to export!", "", FXOptionPane.Type.WARNING);
            return;
        }
        getController().exportDownload(download);
    }

    // === ui factory methods === //
    protected GridPane createLeftPanel() {
        int rowIx = 0;
        GridPane pane = new GridPane();
        pane.setVgap(5);
        pane.setHgap(5);
        pane.setPadding(new Insets(5, 5, 5, 5));
        pane.setPrefWidth(400);
        pane.getColumnConstraints().addAll(createColumnConstraints(20), createColumnConstraints2(30), createColumnConstraints(20), createColumnConstraints2(30));

        Label title = createTitleLabel();
        pane.add(title, 0, rowIx++);
        GridPane.setColumnSpan(title, 4);

        pane.add(createLabel("VIN :"), 0, rowIx);
        pane.add(getProductId(), 1, rowIx++);
        GridPane.setColumnSpan(getProductId(), 3);

        pane.add(createLabel("Start Date :"), 0, rowIx);
        pane.add(getStartDatePicker(), 1, rowIx);

        pane.add(createLabel("End Date :"), 2, rowIx);
        pane.add(getEndDatePicker(), 3, rowIx++);

        pane.add(createLabel("Year :"), 0, rowIx);
        pane.add(getYearCode(), 1, rowIx);

        pane.add(createLabel("Model :"), 2, rowIx);
        pane.add(getModelCode(), 3, rowIx++);

        pane.add(createLabel("Type :"), 0, rowIx);
        pane.add(getTypeCode(), 1, rowIx);

        pane.add(createLabel("Option:"), 2, rowIx);
        pane.add(getOptionCode(), 3, rowIx++);

        pane.add(createLabel("LineNo:"), 0, rowIx);
        pane.add(getLineNo(), 1, rowIx);

        pane.add(createLabel("Production:"), 2, rowIx);
        pane.add(getProduction(), 3, rowIx++);

        pane.add(getResetButton(), 0, rowIx);
        GridPane.setColumnSpan(getResetButton(), 2);
        pane.add(getSubmitButton(), 2, rowIx++);
        GridPane.setColumnSpan(getSubmitButton(), 2);

        pane.add(createLabel("Pgm Filter:"), 0, rowIx);
        pane.add(getProgramFilter(), 1, rowIx++);
        GridPane.setColumnSpan(getProgramFilter(), 3);
        GridPane.setColumnSpan(getLetProgramTable(), 4);

        pane.add(getLetProgramTable(), 0, rowIx);
        for (int i = 0; i <= rowIx; i++) {
            pane.getRowConstraints().add(createRowConstraints());
        }
        pane.getRowConstraints().get(pane.getRowConstraints().size() - 1).setPrefHeight(2000d);
        rowIx++;

        pane.add(createLabel("Page Size:"), 0, rowIx);
        pane.add(getPageSize(), 1, rowIx);
        pane.add(getClearButton(), 2, rowIx);
        GridPane.setColumnSpan(getClearButton(), 2);
        return pane;
    }

    protected DynamicTablePane<LetInspectionProgram> createProgramTable() {
        ColumnMapping mapping = new ColumnMapping("Inspection Program", "inspectionPgmName");
        mapping.setSortable(true);
        List<ColumnMapping> columnMappings = new ArrayList<ColumnMapping>();
        columnMappings.add(mapping);
        DynamicTablePane<LetInspectionProgram> table = new DynamicTablePane<LetInspectionProgram>(columnMappings);
        table.getTable().getColumns().get(0).setPrefWidth(375d);
        table.getTable().setStyle(FONT_STYLE);
        return table;
    }

    protected DynamicTablePane<LetResultDownload> createDownloadJobTable() {
        List<ColumnMapping> columnMappings = getDownloadJobTableColumns();
        DynamicTablePane<LetResultDownload> table = new DynamicTablePane<LetResultDownload>(columnMappings, true);
        table.getTable().setStyle(FONT_STYLE);
        return table;
    }

    protected CacheTablePane createResultTable(LetResultDownload download) {
        List<ColumnMapping> columnMappings = getResultTableStaticColumnMappings();
        int pageSize = getController().getPropertyBean().getDefaultResultPageSize();
        CacheTablePane table = new CacheTablePane(columnMappings, getCache(), pageSize, false, true);
        table.getTable().setStyle(FONT_STYLE);
        table.getPagination().setStyle(FONT_STYLE);
        return table;
    }

    protected Label createTitleLabel() {
        Label title = new Label("Let Inspection Result Downolad");
        title.setStyle(String.format("-fx-font-weight: bold; -fx-font-size: %dpx; -fx-alignment: center; -fx-padding: 10;", 20));
        return title;
    }

    protected TextField createProductIdField(String id) {
        TextField element = UiFactory.createTextField(id, PRODUCT_ID_MAX_LENGTH, FONT_STYLE, null, Pos.CENTER_LEFT, true);
        element.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (element.getText().length() > PRODUCT_ID_MAX_LENGTH) {
                    String s = element.getText().substring(0, PRODUCT_ID_MAX_LENGTH);
                    element.setText(s);
                }
            }
        });
        return element;
    }

    protected TextField createProgramFilter() {
        TextField element = UiFactory.createTextField("programFilter", LET_PGM_MAX_LENGTH, FONT_STYLE, null, Pos.CENTER_LEFT, true);
        element.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (element.getText().length() > LET_PGM_MAX_LENGTH) {
                    String s = element.getText().substring(0, LET_PGM_MAX_LENGTH);
                    element.setText(s);
                }
            }
        });
        return element;
    }

    protected TextField createLineNo() {
        TextField element = UiFactory.createTextField("lineNo", 1, FONT_STYLE, null, Pos.CENTER_LEFT, false);
        element.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                int maxLength = 1;
                if (element.getText().length() > maxLength) {
                    String s = element.getText().substring(0, maxLength);
                    element.setText(s);
                }
            }
        });
        return element;
    }

    protected ComboBox<KeyValue<String, String>> createProductionComboBox() {
        ComboBox<KeyValue<String, String>> element = createComboBox();
        StringConverter<KeyValue<String, String>> converter = new StringConverter<KeyValue<String, String>>() {
            @Override
            public String toString(KeyValue<String, String> object) {
                if (object == null) {
                    return "";
                } else if (StringUtils.isBlank(object.getKey())) {
                    return "";
                } else {
                    return object.getKey() + ":" + object.getValue();
                }
            }

            @Override
            public KeyValue<String, String> fromString(String string) {
                return null;
            }
        };
        element.setConverter(converter);
        return element;
    }

    protected ContextMenu createDonwloadJobTableContextMenu() {
        ContextMenu cm = new ContextMenu();
        cm.setStyle(FONT_STYLE);
        this.viewMenuItem = new MenuItem("View");
        this.exportMenuItem = new MenuItem("Export");
        this.deleteMenuItem = new MenuItem("Delete");
        this.cancelMenuItem = new MenuItem("Cancel");
        getViewMenuItem().setId("view");
        getExportMenuItem().setId("export");
        getDeleteMenuItem().setId("delete");
        getCancelMenuItem().setId("cancel");

        cm.getItems().add(getViewMenuItem());
        cm.getItems().add(getExportMenuItem());
        cm.getItems().add(new SeparatorMenuItem());
        cm.getItems().add(getDeleteMenuItem());
        cm.getItems().add(getCancelMenuItem());
        for (MenuItem item : cm.getItems()) {
            item.setOnAction(this);
        }
        return cm;
    }

    // === table columns === //
    protected List<ColumnMapping> getDownloadJobTableColumns() {

        List<ColumnMapping> columns = new ArrayList<ColumnMapping>();
        columns.add(createColumnMapping("#", "$rowid"));
        columns.add(createColumnMapping("Job Id", "jobId"));
        columns.add(createColumnMapping("Program", "program.inspectionPgmName"));
        columns.add(createColumnMapping("Start DateTime", "startDateTxt"));
        columns.add(createColumnMapping("End DateTime", "endDateTxt"));
        columns.add(createColumnMapping("VIN", "productId"));

        columns.add(createColumnMapping("Year", "spec.modelYearCode"));
        columns.add(createColumnMapping("Model", "spec.modelCode"));
        columns.add(createColumnMapping("Type", "spec.modelTypeCode"));
        columns.add(createColumnMapping("Option", "spec.modelOptionCode"));
        columns.add(createColumnMapping("LineNo", "lineNo"));
        columns.add(createColumnMapping("Production", "production"));

        ColumnMapping pd = createColumnMapping("Progress", "progress");
        pd.setFormat("%s%%");
        columns.add(pd);

        columns.add(createColumnMapping("Downloaded", "totalCount"));

        columns.add(createColumnMapping("Job Status", "status"));
        columns.add(createColumnMapping("Download Started", "downloadStartedTxt"));
        columns.add(createColumnMapping("Download Finished", "downloadFinishedTxt"));

        return columns;
    }

    protected List<ColumnMapping> getResultTableColumnMappings(LetResultDownload download) {
        List<ColumnMapping> columns = new ArrayList<ColumnMapping>();
        columns.addAll(getResultTableStaticColumnMappings());
        columns.addAll(getResultTableDynamicColumnMappings(download));
        return columns;
    }

    protected List<ColumnMapping> getResultTableDynamicColumnMappings(LetResultDownload download) {
        List<ColumnMapping> columns = new ArrayList<ColumnMapping>();
        columns.addAll(createAttrColumns(download));
        columns.addAll(createParamColumns(download));
        columns.addAll(createFaultCodeColumns(download));
        columns.add(createColumnMapping("", "SPACER"));
        return columns;
    }

    protected List<ColumnMapping> createResultTableStaticColumnMappings() {
        List<ColumnMapping> columns = new ArrayList<ColumnMapping>();
        columns.add(createColumnMapping("#", "$rowid"));
        columns.add(createColumnMapping("MSG_ID", "MESSAGE_ID"));
        columns.add(createColumnMapping("VIN", "PRODUCT_ID"));
        columns.add(createColumnMapping("Year", "MODEL_YEAR_CODE"));
        columns.add(createColumnMapping("Model", "MODEL_CODE"));
        columns.add(createColumnMapping("Type", "MODEL_TYPE_CODE"));
        columns.add(createColumnMapping("Option", "MODEL_OPTION_CODE"));
        columns.add(createColumnMapping("TestProcess", "TestProcess"));
        columns.add(createColumnMapping("StepFile", "SEQ_STEP_FILE"));
        columns.add(createColumnMapping("EndTimestamp", "TEST_END_TIME"));
        columns.add(createColumnMapping("Process", "PROCESS"));
        columns.add(createColumnMapping("Cal", "CAL"));
        columns.add(createColumnMapping("Cell", "CELL"));
        columns.add(createColumnMapping("Status", "TEST_STATUS"));
        columns.add(createColumnMapping("Production", "PRODUCTION"));
        return columns;
    }

    protected List<ColumnMapping> createAttrColumns(LetResultDownload download) {
        List<ColumnMapping> columns = new ArrayList<ColumnMapping>();
        if (download == null || download.getAttrNames() == null || download.getAttrNames().isEmpty()) {
            return columns;
        }
        for (String name : download.getAttrNames()) {
            String title = name;
            String prefix = getController().getModel().getAttrPrefix(name);
            ColumnMapping cm = createColumnMapping(title, title);
            cm.setSortable(false);
            cm.addSubColumn(createColumnMapping(VALUE, prefix + ATTR_VAL));
            cm.addSubColumn(createColumnMapping(EXPECTED_VALUE, prefix + ATTR_EXP_VAL));
            columns.add(cm);
        }
        return columns;
    }

    protected List<ColumnMapping> createParamColumns(LetResultDownload download) {
        List<ColumnMapping> columns = new ArrayList<ColumnMapping>();
        if (download == null || download.getParamNames() == null || download.getParamNames().isEmpty()) {
            return columns;
        }
        for (String name : download.getParamNames()) {
            String title = name;
            String prefix = getController().getModel().getParamPrefix(name);
            ColumnMapping cm = createColumnMapping(title, title);
            cm.setSortable(false);
            cm.addSubColumn(createColumnMapping(VALUE, prefix + PARAM_VAL));
            cm.addSubColumn(createColumnMapping(LOW_LIMIT, prefix + PARAM_LO_LIMIT));
            cm.addSubColumn(createColumnMapping(HIGH_LIMIT, prefix + PARAM_HI_LIMIT));
            cm.addSubColumn(createColumnMapping(UNIT, prefix + PARAM_UNIT));
            columns.add(cm);
        }
        return columns;
    }

    protected List<ColumnMapping> createFaultCodeColumns(LetResultDownload download) {
        List<ColumnMapping> columns = new ArrayList<ColumnMapping>();
        if (download == null || download.getFaultCodeNames() == null || download.getFaultCodeNames().isEmpty()) {
            return columns;
        }
        for (String name : download.getFaultCodeNames()) {
            String title = name;
            String prefix = getController().getModel().getFaultCodePrefix(name);
            ColumnMapping cm = createColumnMapping(title, title);
            cm.setSortable(false);
            cm.addSubColumn(createColumnMapping(VALUE, prefix + FAULT_CODE_VAL));
            cm.addSubColumn(createColumnMapping(SHORT_DESC, prefix + FAULT_CODE_DESC));
            columns.add(cm);
        }
        return columns;
    }

    protected ColumnMapping createColumnMapping(String title, String propertyName) {
        ColumnMapping mapping = new ColumnMapping(title, propertyName);
        mapping.setSortable(true);
        mapping.setFormat(null);
        return mapping;
    }

    protected Label createLabel(String txt) {
        Label element = UiFactory.createLabel(txt, txt, 12);
        GridPane.setHalignment(element, HPos.RIGHT);
        return element;
    }

    protected DatePicker createDatePicker() {
        DatePicker element = new DatePicker();
        element.setStyle(FONT_STYLE);
        return element;
    }

    protected <T> ComboBox<T> createComboBox() {
        return createComboBox(300d, false);
    }

    protected <T> ComboBox<T> createComboBox(double width, boolean disabled) {
        ComboBox<T> element = new ComboBox<T>();
        element.setStyle(FONT_STYLE);
        element.setPrefWidth(width);
        element.setDisable(disabled);
        return element;
    }

    protected Button createButton(String text, String id) {
        Button element = UiFactory.createButton(text, id);
        element.setPrefWidth(165);
        return element;
    }

    protected Button createButton(String text, String id, int prefWidth) {
        Button element = UiFactory.createButton(text, id);
        element.setPrefWidth(prefWidth);
        return element;
    }

    protected ColumnConstraints createColumnConstraints(double percentage) {
        ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(percentage);
        return cc;
    }

    protected ColumnConstraints createColumnConstraints2(double percentage) {
        ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(percentage);
        cc.setHgrow(Priority.ALWAYS);
        return cc;
    }

    protected RowConstraints createRowConstraints() {
        RowConstraints rc = new RowConstraints();
        return rc;
    }

    // === get/set === //
    public TextField getProductId() {
        return productId;
    }

    public DatePicker getStartDatePicker() {
        return startDatePicker;
    }

    public DatePicker getEndDatePicker() {
        return endDatePicker;
    }

    public ComboBox<String> getYearCode() {
        return yearCode;
    }

    public ComboBox<String> getTypeCode() {
        return typeCode;
    }

    public ComboBox<String> getOptionCode() {
        return optionCode;
    }

    protected ComboBox<String> getModelCode() {
        return modelCode;
    }

    protected DynamicTablePane<LetInspectionProgram> getLetProgramTable() {
        return letProgramTable;
    }

    protected CacheTablePane getResultTable() {
        return resultTable;
    }

    protected Button getClearButton() {
        return clearButton;
    }

    protected Button getSubmitButton() {
        return submitButton;
    }

    protected Button getResetButton() {
        return resetButton;
    }

    protected ProductSpecData getSpecData() {
        return getController().getSpecData();
    }

    protected List<KeyValue<String, String>> getProductionData() {
        return getController().getProductionData();
    }

    protected List<LetInspectionProgram> getPrograms() {
        return getController().getPrograms();
    }

    protected TextField getProgramFilter() {
        return programFilter;
    }

    protected GridPane getLeftPanel() {
        return leftPanel;
    }

    protected TabPane getTabbedPane() {
        return tabbedPane;
    }

    protected DynamicTablePane<LetResultDownload> getDownloadJobTable() {
        return downloadJobTable;
    }

    public ContextMenu getDownloadJobTableContextMenu() {
        return downloadJobTableContextMenu;
    }

    protected synchronized int getNewJobId() {
        jobCounter++;
        return jobCounter;
    }

    protected MenuItem getViewMenuItem() {
        return viewMenuItem;
    }

    protected MenuItem getExportMenuItem() {
        return exportMenuItem;
    }

    protected MenuItem getDeleteMenuItem() {
        return deleteMenuItem;
    }

    protected MenuItem getCancelMenuItem() {
        return cancelMenuItem;
    }

    public TextField getLineNo() {
        return lineNo;
    }

    public ComboBox<KeyValue<String, String>> getProduction() {
        return production;
    }

    protected LetResultDownloadController getController() {
        return controller;
    }

    protected PersistentCache getCache() {
        return getController().getCache();
    }

    protected ComboBox<Integer> getPageSize() {
        return pageSize;
    }

    protected List<ColumnMapping> getResultTableStaticColumnMappings() {
        return resultTableStaticColumnMappings;
    }

    protected boolean isInitMessageDisplayed() {
        return initMessageDisplayed;
    }

    protected void setInitMessageDisplayed(boolean initMessageDisplayed) {
        this.initMessageDisplayed = initMessageDisplayed;
    }
}

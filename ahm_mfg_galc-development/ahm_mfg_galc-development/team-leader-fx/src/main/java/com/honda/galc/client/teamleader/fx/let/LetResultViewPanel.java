package com.honda.galc.client.teamleader.fx.let;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.ColumnMapping;
import com.honda.galc.client.ui.component.DynamicTablePane;
import com.honda.galc.client.ui.component.FXOptionPane;
import com.honda.galc.client.ui.component.ManualProductEntryDialog;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.XmlTreePanel;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.enumtype.LetInspectionStatus;
import com.honda.galc.entity.product.LetPassCriteriaProgram;
import com.honda.galc.entity.product.LetProgramCategory;
import com.honda.galc.entity.product.LetProgramCategoryId;
import com.honda.galc.entity.product.ProductTypeData;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
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
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import javafx.util.Callback;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>LetResultViewPanel</code> is ... .
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
 * @created Aug 7, 2019
 */
public class LetResultViewPanel extends TabbedPanel {

    public static final String RESULTS = "Results Table ";
    public static final String TREE_VIEW = "Results Tree";

    public static final int LET_PGM_MAX_LENGTH = 75;
    public static final int PRODUCT_ID_MAX_LENGTH = 17;

    public static final String FONT_STYLE = "-fx-font-weight: bold; -fx-font-size: 11px;";
    public static final String INIT_MSG = "This screen will not reflect any changes made manually through GALC. \n" 
            + "Only results directly uploaded from LET device will be displayed.\n"
            + "If you have questions please contact your local GALC support member for details.";

    private Button vinButton;
    private TextField productIdInput;
    private Button resetButton;
    private Button submitButton;

    private TextField productIdDisplay;
    private TextField productSpecDisplay;

    private TextField programNameFilter;
    private DynamicTablePane<LetPassCriteriaProgram> programTable;

    private TextField treeViewFilter;
    private Button collapseAllButton;
    private Button expandAllButton;
    private Button exportButton;

    private DynamicTablePane<Map<String, Object>> resultTable;
    private XmlTreePanel resultTree;

    private ContextMenu resultTreeContextMenu;
    private MenuItem expandMenuItem;
    private MenuItem collapseMenuItem;
    private MenuItem copyMenuItem;
    private MenuItem copyBranchMenuItem;

    private GridPane leftPanel;
    private TabPane tabbedPane;

    private LetResultViewController controller;

    private boolean initMessageDisplayed;

    public LetResultViewPanel(MainWindow mainWindow) {
        super("Let Result View", KeyEvent.VK_V, mainWindow);
        init();
    }

    protected void init() {
        this.controller = new LetResultViewController(this);
        initUi();
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

        this.vinButton = createButton("VIN", "vinButton", 70);
        this.productIdInput = createProductIdField("ProductId");
        this.resetButton = createButton("Reset", "resetButton", 220);
        this.submitButton = createButton("Search", "submitButton", 220);

        this.productIdDisplay = UiFactory.createTextField("ProductIdDisplay", FONT_STYLE, TextFieldState.DISABLED);
        this.productSpecDisplay = UiFactory.createTextField("ProductSpecDisplay", FONT_STYLE, TextFieldState.DISABLED);

        this.programNameFilter = createTestFilter();
        this.programTable = createProgramTable();

        this.treeViewFilter = createTreeViewFilter();
        this.collapseAllButton = createButton("-", "collapseAllButton", 55);
        this.expandAllButton = createButton("+", "expandAllButton", 55);
        this.exportButton = createButton("Export", "exportButton", 110);

        this.resultTable = createResultTable();
        this.resultTree = createResultTree();

        this.resultTreeContextMenu = createResultTreeContextMenu();

        this.leftPanel = createLeftPanel();
        setLeft(getLeftPanel());
        setMargin(getLeftPanel(), new Insets(10, 0, 10, 0));

        Tab resultTab = new Tab();
        resultTab.setClosable(false);
        BorderPane bp = new BorderPane();

        bp.setCenter(getResultTable());
        resultTab.setContent(bp);
        resultTab.setText(RESULTS);
        resultTab.setStyle(FONT_STYLE);

        Tab treeViewTab = new Tab();
        treeViewTab.setClosable(false);
        treeViewTab.setContent(getResultTree());
        treeViewTab.setText(TREE_VIEW);
        treeViewTab.setStyle(FONT_STYLE);

        getTabbedPane().getTabs().add(resultTab);
        getTabbedPane().getTabs().add(treeViewTab);
        getTabbedPane().setStyle("-fx-padding: 10 10 10 10;");

        setCenter(getTabbedPane());
    }

    // === factory methods === //
    protected GridPane createLeftPanel() {
        int rowIx = 0;
        GridPane pane = new GridPane();
        pane.setVgap(5);
        pane.setHgap(5);
        pane.setPadding(new Insets(5, 5, 0, 5));
        pane.setPrefWidth(400);
        pane.getColumnConstraints().addAll(createColumnConstraints(20), createColumnConstraints2(30), createColumnConstraints(20), createColumnConstraints2(30));

        Label title = createTitleLabel();
        pane.add(title, 0, rowIx++);
        GridPane.setColumnSpan(title, 4);

        pane.add(getVinButton(), 0, rowIx);
        pane.add(getProductIdInput(), 1, rowIx++);
        GridPane.setColumnSpan(getProductIdInput(), 3);

        pane.add(getResetButton(), 0, rowIx);
        GridPane.setColumnSpan(getResetButton(), 2);
        pane.add(getSubmitButton(), 2, rowIx++);
        GridPane.setColumnSpan(getSubmitButton(), 2);

        pane.add(getProductIdDisplay(), 0, rowIx);
        GridPane.setColumnSpan(getProductIdDisplay(), 2);
        pane.add(getProductSpecDisplay(), 2, rowIx++);
        GridPane.setColumnSpan(getProductSpecDisplay(), 2);

        pane.add(createLabel("Pgm Filter:"), 0, rowIx);
        pane.add(getProgramNameFilter(), 1, rowIx++);
        GridPane.setColumnSpan(getProgramNameFilter(), 3);
        GridPane.setColumnSpan(getProgramTable(), 4);

        pane.add(getProgramTable(), 0, rowIx++);

        pane.add(createLabel("Tree Search:"), 0, rowIx);
        pane.add(getTreeViewFilter(), 1, rowIx);
        GridPane.setColumnSpan(getTreeViewFilter(), 2);
        HBox box = new HBox();
        box.getChildren().add(getCollapseAllButton());
        box.getChildren().add(getExpandAllButton());
        pane.add(box, 3, rowIx++);

        pane.add(getExportButton(), 3, rowIx);

        for (int i = 0; i <= rowIx; i++) {
            pane.getRowConstraints().add(createRowConstraints());
        }
        pane.getRowConstraints().get(pane.getRowConstraints().size() - 3).setPrefHeight(2000d);
        return pane;
    }

    protected Label createTitleLabel() {
        Label title = new Label("Let Inspection Result View");
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

    protected TextField createTestFilter() {
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

    protected TextField createTreeViewFilter() {
        TextField element = UiFactory.createTextField("treeViewFilter", LET_PGM_MAX_LENGTH, FONT_STYLE, null, Pos.CENTER_LEFT, true);
        return element;
    }

    protected ContextMenu createResultTreeContextMenu() {
        ContextMenu cm = new ContextMenu();
        cm.setStyle(FONT_STYLE);
        this.expandMenuItem = new MenuItem("Expand");
        this.collapseMenuItem = new MenuItem("Collapse");
        this.copyMenuItem = new MenuItem("Copy Item");
        this.copyBranchMenuItem = new MenuItem("Copy Branch");

        getExpandMenuItem().setId("expand");
        getCollapseMenuItem().setId("collapse");
        getCopyMenuItem().setId("copyItem");
        getCopyBranchMenuItem().setId("copyBranch");
        cm.getItems().add(getExpandMenuItem());
        cm.getItems().add(getCollapseMenuItem());
        cm.getItems().add(new SeparatorMenuItem());
        cm.getItems().add(getCopyMenuItem());
        cm.getItems().add(getCopyBranchMenuItem());
        for (MenuItem item : cm.getItems()) {
            item.setOnAction(this);
        }
        return cm;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected DynamicTablePane<LetPassCriteriaProgram> createProgramTable() {
        List<ColumnMapping> columnMappings = new ArrayList<ColumnMapping>();
        columnMappings.add(getController().createColumnMapping("Inspection Program", "criteriaPgmName", true));
        columnMappings.add(getController().createColumnMapping("Type", "", true));
        DynamicTablePane<LetPassCriteriaProgram> table = new DynamicTablePane<LetPassCriteriaProgram>(columnMappings) {

            @Override
            protected TableColumn<LetPassCriteriaProgram, Object> createColumn(ColumnMapping mapping) {
                TableColumn<LetPassCriteriaProgram, Object> column = super.createColumn(mapping);
                if ("Type".equals(mapping.getTitle())) {
                    column.setCellFactory(createProgramTableCellFactory());
                }
                return column;
            }

        };
        table.getTable().getColumns().get(0).setPrefWidth(275d);
        table.getTable().getColumns().get(1).setPrefWidth(100d);

        Comparator comp = new Comparator<LetPassCriteriaProgram>() {
            @Override
            public int compare(LetPassCriteriaProgram o1, LetPassCriteriaProgram o2) {
                LetProgramCategoryId id1 = new LetProgramCategoryId(o1.getInspectionDeviceType(), o1.getCriteriaPgmAttr());
                LetProgramCategoryId id2 = new LetProgramCategoryId(o2.getInspectionDeviceType(), o2.getCriteriaPgmAttr());
                LetProgramCategory cat1 = getModel().getProgramCategoryIx().get(id1);
                LetProgramCategory cat2 = getModel().getProgramCategoryIx().get(id2);
                String catName1 = cat1 == null ? "" : cat1.getPgmCategoryName();
                String catName2 = cat2 == null ? "" : cat2.getPgmCategoryName();
                return catName1.compareTo(catName2);
            }

        };
        table.getTable().getColumns().get(1).setComparator(comp);
        table.getTable().setStyle(FONT_STYLE);
        return table;
    }

    protected Callback<TableColumn<LetPassCriteriaProgram, Object>, TableCell<LetPassCriteriaProgram, Object>> createProgramTableCellFactory() {
        Callback<TableColumn<LetPassCriteriaProgram, Object>, TableCell<LetPassCriteriaProgram, Object>> cellFactory = new Callback<TableColumn<LetPassCriteriaProgram, Object>, TableCell<LetPassCriteriaProgram, Object>>() {
            @Override
            public TableCell<LetPassCriteriaProgram, Object> call(TableColumn<LetPassCriteriaProgram, Object> param) {

                TableCell<LetPassCriteriaProgram, Object> tableCell = new TableCell<LetPassCriteriaProgram, Object>() {
                    @Override
                    protected void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty);
                        if (isEmpty() || getItem() == null) {
                            setText(null);
                            setGraphic(null);
                            setStyle("");
                        } else {
                            if (item instanceof LetPassCriteriaProgram) {
                                LetPassCriteriaProgram lpsp = (LetPassCriteriaProgram) item;
                                LetProgramCategoryId id = new LetProgramCategoryId(lpsp.getInspectionDeviceType(), lpsp.getCriteriaPgmAttr());
                                LetProgramCategory category = getModel().getProgramCategoryIx().get(id);
                                if (category != null) {
                                    setText(category.getPgmCategoryName());
                                    setStyle("-fx-background-color: " + category.getBgColor() + ";");
                                } else {
                                    setText("");
                                    setStyle("");
                                }
                            } else {
                                setStyle("");
                            }
                        }
                    }
                };
                return tableCell;
            }
        };
        return cellFactory;
    }

    protected Callback<TableColumn<Map<String, Object>, Object>, TableCell<Map<String, Object>, Object>> createResultTableCellFactory() {
        Callback<TableColumn<Map<String, Object>, Object>, TableCell<Map<String, Object>, Object>> cellFactory = new Callback<TableColumn<Map<String, Object>, Object>, TableCell<Map<String, Object>, Object>>() {
            @Override
            public TableCell<Map<String, Object>, Object> call(TableColumn<Map<String, Object>, Object> param) {

                TableCell<Map<String, Object>, Object> tableCell = new TableCell<Map<String, Object>, Object>() {
                    @Override
                    protected void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty);
                        if (isEmpty() || getItem() == null) {
                            setText(null);
                            setGraphic(null);
                            setStyle("");
                        } else {
                            setText(getItem().toString());
                            if (LetInspectionStatus.Fail.getStringValue().equals(item) || LetInspectionStatus.PASS_TERMINATED.getStringValue().equals(item)) {
                                setStyle("-fx-background-color: red;");
                            } else {
                                setStyle("");
                            }
                        }
                    }
                };

                return tableCell;
            }
        };
        return cellFactory;
    }

    protected Callback<TableView<Map<String, Object>>, TableRow<Map<String, Object>>> createResultTableRowFactory() {
        Callback<TableView<Map<String, Object>>, TableRow<Map<String, Object>>> factory = null;
        factory = new Callback<TableView<Map<String, Object>>, TableRow<Map<String, Object>>>() {
            @Override
            public TableRow<Map<String, Object>> call(TableView<Map<String, Object>> param) {
                TableRow<Map<String, Object>> tableRow = new TableRow<Map<String, Object>>() {
                    @Override
                    protected void updateItem(Map<String, Object> item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!isEmpty() && getItem() != null && LetResultViewController.SEPARATOR.equals(item.get(LetResultViewController.TYPE))) {
                            setStyle("-fx-background-color: wheat;-fx-font-weight: bold; -fx-font-size: 12px;");
                        } else {
                            setStyle("");
                        }
                    }
                };
                return tableRow;
            }
        };
        return factory;
    }

    protected DynamicTablePane<Map<String, Object>> createResultTable() {
        DynamicTablePane<Map<String, Object>> table = new DynamicTablePane<Map<String, Object>>(getModel().getDefaultColumnMappings()) {
            @Override
            protected TableColumn<Map<String, Object>, Object> createColumn(ColumnMapping mapping) {
                TableColumn<Map<String, Object>, Object> tableColumn = super.createColumn(mapping);
                tableColumn.setCellFactory(createResultTableCellFactory());
                return tableColumn;
            }
        };
        table.getTable().setRowFactory(createResultTableRowFactory());
        table.getTable().setStyle(FONT_STYLE);
        return table;
    }

    protected String computeColor(String value) {

        String treeFilter = getTreeViewFilter().getText();
        if (StringUtils.isBlank(treeFilter)) {
            return "";
        }
        if (StringUtils.containsIgnoreCase(value, treeFilter)) {
            return "-fx-background: yellow;";
        }
        return "";
    }

    protected XmlTreePanel createResultTree() {
        XmlTreePanel tree = new XmlTreePanel();
        tree.getTreeView().setStyle(FONT_STYLE);
        tree.getTreeView().setCellFactory(v -> new TreeCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                styleProperty().unbind();
                if (empty) {
                    setText("");
                    setStyle("");
                } else {
                    setText(item);
                    styleProperty().bind(Bindings.createStringBinding(() -> computeColor(item)));
                }
            }
        });
        tree.getTreeView().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        return tree;
    }

    protected Button createButton(String text, String id, int prefWidth) {
        Button element = UiFactory.createButton(text, id);
        element.setPrefWidth(prefWidth);
        return element;
    }

    protected Label createLabel(String txt) {
        Label element = UiFactory.createLabel(txt, txt, 12);
        GridPane.setHalignment(element, HPos.RIGHT);
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

    // === event handlers === //
    protected void mapHandlers() {
        getVinButton().setOnAction(this);
        getResetButton().setOnAction(this);
        getSubmitButton().setOnAction(this);
        getProductIdInput().setOnAction(this);
        getTreeViewFilter().setOnAction(this);
        getCollapseAllButton().setOnAction(this);
        getExpandAllButton().setOnAction(this);
        getExportButton().setOnAction(this);

        getProgramTable().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<LetPassCriteriaProgram>() {
            @Override
            public void changed(ObservableValue<? extends LetPassCriteriaProgram> ov, LetPassCriteriaProgram oldVal, LetPassCriteriaProgram newVal) {
                if (newVal == null) {
                    getController().resetProgram();
                    getTreeViewFilter().setText("");
                    return;
                }
                getController().processSelectedProgram(newVal);
                getTreeViewFilter().setText("\"" + newVal.getCriteriaPgmName() + "\"");
            }
        });

        getProgramNameFilter().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                getController().filterPrograms(newValue);
            }
        });

        getTreeViewFilter().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                searchTreeView(newValue);
            }
        });

        getResultTree().getTreeView().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.SECONDARY) {
                    List<TreeItem<String>> selectedItems = getResultTree().getTreeView().getSelectionModel().getSelectedItems();
                    if (selectedItems == null || selectedItems.isEmpty()) {
                        return;
                    }
                    getResultTreeContextMenu().show(getResultTree(), event.getScreenX(), event.getScreenY());
                } else if (getResultTreeContextMenu().isShowing()) {
                    getResultTreeContextMenu().hide();
                }
            }
        });
    }

    @Override
    public void handle(ActionEvent event) {
        clearErrorMessage();
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

    protected void searchTreeView() {
        String text = getTreeViewFilter().getText();
        searchTreeView(text);
    }

    protected void searchTreeView(String text) {
        getResultTree().collapseAll();
        expandRootItem();
        if (StringUtils.isBlank(text)) {
            return;
        }
        List<TreeItem<String>> foundItems = new ArrayList<TreeItem<String>>();
        foundItems.addAll(getResultTree().searchItems(text));
        if (foundItems == null || foundItems.isEmpty()) {
            return;
        }
        LetPassCriteriaProgram selectedProgram = getProgramTable().getSelectedItem();
        boolean expand = false;
        if (selectedProgram != null && text.equals("\"" + selectedProgram.getCriteriaPgmName() + "\"")) {
            expand = true;
        }
        boolean single = foundItems.size() == 1;
        for (TreeItem<String> item : foundItems) {
            getResultTree().expandToRoot(item);
            if (single || expand) {
                getResultTree().expandBranch(item);

            }
        }
        TreeItem<String> firstFoundItem = foundItems.get(0);
        int ix = getResultTree().getIndex(firstFoundItem);
        if (ix > 20) {
            ix--;
            getResultTree().getTreeView().scrollTo(ix);
        } else {
            getResultTree().getTreeView().scrollTo(0);
        }

    }

    public void handleEvent(ActionEvent event) {

        if (event.getSource().equals(getVinButton())) {
            displayVinLookupDialog();
        }

        if (event.getSource().equals(getProductIdInput())) {
            getController().processProductId();
        }
        if (event.getSource().equals(getSubmitButton())) {
            getController().processProductId();
        } else if (event.getSource().equals(getProgramNameFilter())) {
            getController().filterPrograms();
        }

        else if (event.getSource().equals(getTreeViewFilter())) {
            searchTreeView();
        } else if (event.getSource().equals(getResetButton())) {
            getController().reset();
        } else if (event.getSource().equals(getCollapseAllButton())) {
            collapseAll();
        } else if (event.getSource().equals(getExpandAllButton())) {
            expandAll();
        } else if (event.getSource().equals(getExportButton())) {
            getController().exportResults();
        } else if (event.getSource() instanceof MenuItem) {
            if (getExpandMenuItem().equals(event.getSource())) {
                expandSelected();
            } else if (getCollapseMenuItem().equals(event.getSource())) {
                collapseSelected();
            } else if (getCopyMenuItem().equals(event.getSource())) {
                copyToClipboard();
            } else if (getCopyBranchMenuItem().equals(event.getSource())) {
                copyBranchToClipboard();
            }
        }
    }

    public void displayVinLookupDialog() {
        ProductTypeData productTypeData = getModel().getProductTypeData();
        if (productTypeData == null) {
            String msg = "Can not process product lookup as ProductTypeData is not configured for product type: " + getModel().getProductType() + ".";
            FXOptionPane.showMessageDialog(getMainWindow().getStage(), msg, "", FXOptionPane.Type.WARNING);
            return;
        }

        ManualProductEntryDialog manualProductEntryDialog = new ManualProductEntryDialog("Manual Product Entry Dialog", productTypeData, getMainWindow().getApplicationContext().getApplicationId());
        manualProductEntryDialog.showDialog();
        String productId = manualProductEntryDialog.getResultProductId();
        if (!StringUtils.isEmpty(productId)) {
            getProductIdInput().setText(productId);
        }
    }

    public void expandAll() {
        getResultTree().expandAll();
    }

    public void expandRootItem() {
        getResultTree().expandItem(getResultTree().getTreeView().getRoot());
    }
    
    public void expandSelected() {
        List<TreeItem<String>> selectedItems = getResultTree().getTreeView().getSelectionModel().getSelectedItems();
        if (selectedItems == null || selectedItems.isEmpty()) {
            return;
        } else {
            for (TreeItem<String> item : selectedItems) {
                getResultTree().expandBranch(item);
            }
        }
    }

    public void collapseAll() {
        getResultTree().collapseAll();
    }

    public void collapseSelected() {
        List<TreeItem<String>> selectedItems = getResultTree().getTreeView().getSelectionModel().getSelectedItems();
        if (selectedItems == null || selectedItems.isEmpty()) {
            return;
        } else {
            for (TreeItem<String> item : selectedItems) {
                getResultTree().collapseBranch(item);
            }
        }
    }

    protected void copyToClipboard() {
        List<TreeItem<String>> selectedItems = getResultTree().getTreeView().getSelectionModel().getSelectedItems();
        if (selectedItems == null || selectedItems.isEmpty()) {
            return;
        } else {
            StringBuilder sb = new StringBuilder();
            for (TreeItem<String> item : selectedItems) {
                sb.append(item.getValue());
                sb.append(System.lineSeparator());
            }

            if (sb.length() > 0) {
                final Clipboard clipboard = Clipboard.getSystemClipboard();
                final ClipboardContent content = new ClipboardContent();
                content.putString(sb.toString());
                clipboard.setContent(content);
            }
        }
    }

    protected void copyBranchToClipboard() {
        List<TreeItem<String>> selectedItems = getResultTree().getTreeView().getSelectionModel().getSelectedItems();
        if (selectedItems == null || selectedItems.isEmpty()) {
            return;
        } else {
            StringBuilder sb = new StringBuilder();
            for (TreeItem<String> item : selectedItems) {
                toString(sb, item, "");
            }

            if (sb.length() > 0) {
                final Clipboard clipboard = Clipboard.getSystemClipboard();
                final ClipboardContent content = new ClipboardContent();
                content.putString(sb.toString());
                clipboard.setContent(content);
            }
        }
    }

    protected void toString(StringBuilder sb, TreeItem<String> item, String indent) {
        if (indent == null) {
            indent = "";
        }
        sb.append(indent).append(item.getValue()).append(System.lineSeparator());
        if (item.getChildren().isEmpty()) {
            return;
        }
        for (TreeItem<String> i : item.getChildren()) {
            toString(sb, i, indent + "\t");
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

    // === get/set === //
    protected TextField getProductIdInput() {
        return productIdInput;
    }

    protected TextField getProgramNameFilter() {
        return programNameFilter;
    }

    protected Button getResetButton() {
        return resetButton;
    }

    protected Button getSubmitButton() {
        return submitButton;
    }

    protected DynamicTablePane<LetPassCriteriaProgram> getProgramTable() {
        return programTable;
    }

    protected GridPane getLeftPanel() {
        return leftPanel;
    }

    protected DynamicTablePane<Map<String, Object>> getResultTable() {
        return resultTable;
    }

    protected TabPane getTabbedPane() {
        return tabbedPane;
    }

    protected XmlTreePanel getResultTree() {
        return resultTree;
    }

    protected TextField getTreeViewFilter() {
        return treeViewFilter;
    }

    protected Button getExpandAllButton() {
        return expandAllButton;
    }

    protected Button getCollapseAllButton() {
        return collapseAllButton;
    }

    protected MenuItem getExpandMenuItem() {
        return expandMenuItem;
    }

    protected MenuItem getCollapseMenuItem() {
        return collapseMenuItem;
    }

    protected MenuItem getCopyMenuItem() {
        return copyMenuItem;
    }

    protected ContextMenu getResultTreeContextMenu() {
        return resultTreeContextMenu;
    }

    protected MenuItem getCopyBranchMenuItem() {
        return copyBranchMenuItem;
    }

    protected Button getExportButton() {
        return exportButton;
    }

    protected LetResultViewController getController() {
        return controller;
    }

    protected LetResultViewModel getModel() {
        return getController().getModel();
    }

    @Override
    protected void setErrorMessage(String errorMessage) {
        super.setErrorMessage(errorMessage);
    }

    protected Button getVinButton() {
        return vinButton;
    }

    protected TextField getProductIdDisplay() {
        return productIdDisplay;
    }

    protected TextField getProductSpecDisplay() {
        return productSpecDisplay;
    }

    protected boolean isInitMessageDisplayed() {
        return initMessageDisplayed;
    }

    protected void setInitMessageDisplayed(boolean initMessageDisplayed) {
        this.initMessageDisplayed = initMessageDisplayed;
    }
}

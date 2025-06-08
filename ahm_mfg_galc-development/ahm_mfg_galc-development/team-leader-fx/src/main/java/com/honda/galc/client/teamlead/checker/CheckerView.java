/**
 * 
 */
package com.honda.galc.client.teamlead.checker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.honda.galc.checkers.Checkers;
import com.honda.galc.client.ui.ApplicationMainPane;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.AutoCompleteTextField;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.ui.component.LoggedTableCell;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dto.IDto;
import com.honda.galc.util.KeyValue;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.util.Callback;


/**
 * @author vfc01862
 *
 */
public abstract class CheckerView<T extends IDto> extends ApplicationMainPane {
	
	private TitledPane searchTitledPane;
	private TitledPane detailsTitledPane;
	
	protected AutoCompleteTextField searchTextField;
	private List<String> autoCompleteList;
	protected LoggedButton searchBtn;
	protected LoggedButton addBtn;
	protected LoggedButton duplicateBtn;
	protected LoggedButton deleteBtn;
	protected LoggedComboBox<KeyValue<String, String>> checkerComboBox;
	protected CheckerConfigModel configModel;
	protected ObjectTablePane<T> checkerDetailTablePane;
	
	public abstract Pane createSearchText();
	public abstract Pane createDetailsTablePane();
	public abstract void duplicateChecker();
	public abstract void addChecker();
	public abstract void doSearch();
	public abstract void deleteChecker(ActionEvent actionEvent);
	public abstract void editChecker(ActionEvent actionEvent);
	public abstract void searchTextChanged();
	public abstract  void autoCompleteSearch();
	
	public CheckerView(CheckerConfigModel configModel,MainWindow window) {
		super(window);
		this.configModel = configModel;
		initComponents();
	}
	
	public void initComponents() {
		ScrollPane pane = new ScrollPane(createCheckerViewPanel());
		this.setCenter(pane);
		this.addTableListener();
		this.handleSearchAction();
		this.handleAddDuplicateAction();
		doSearch();
	}
	
	public HBox createCheckerViewPanel() {
		HBox hBox = new HBox();
		Rectangle2D parentBounds = Screen.getPrimary().getVisualBounds();
		double scrollPaneWidth = parentBounds.getWidth();
		double scrollPaneHeight = parentBounds.getHeight();
		
		searchTitledPane = createTitiledPane("Search Criteria", 300, scrollPaneHeight);
		detailsTitledPane = createTitiledPane("Checker Configuration", scrollPaneWidth - 300, scrollPaneHeight);
		searchTitledPane.setContent(createSearchText());
		detailsTitledPane.setContent(createDetailsPane());
		hBox.getChildren().addAll(searchTitledPane, detailsTitledPane);
		
		return hBox;
	}
	
	public Pane createDetailsPane() { 
		VBox detailsTable  = new VBox();
		detailsTable.getChildren().addAll(createDetailsTablePane(), createButton());
		return detailsTable;
	}

	private TitledPane createTitiledPane(String title, double width, double height) {
		TitledPane titledPane = new TitledPane();
		titledPane.setText(title);
		titledPane.setFont(Font.font("", FontWeight.BOLD, 14));
		titledPane.setPrefSize(width,height);
		titledPane.setCollapsible(false);
		return titledPane;
	}
	
	protected AutoCompleteTextField createAutoCompleteTextField(String id, int width, String promptText) {
		AutoCompleteTextField searchTextField= new AutoCompleteTextField(id);
		searchTextField.setMaxSuggestions(5);
		searchTextField.setMaxWidth(width);
		searchTextField.setMinWidth(width);
		searchTextField.setPromptText(promptText);
		searchTextField.setStyle("-fx-prompt-text-fill: black; -fx-font-size: 12px; -fx-background-color: #F7DFB5;");
		return searchTextField;
	}
	
	public HBox createSearchBtn() {
		HBox searchBtnHBox = new HBox();
		searchBtn = createBtn(CheckerConstants.SEARCH);
		searchBtnHBox.getChildren().add(searchBtn);
		searchBtnHBox.setPrefSize(250,20);
		searchBtnHBox.setPadding(new Insets(10,0,0,15));
		HBox.setHgrow(searchBtnHBox, Priority.ALWAYS);
		searchBtnHBox.setAlignment(Pos.BOTTOM_CENTER);
		return searchBtnHBox;
	}
	
	public HBox createSearchTextField(String label) {
		HBox hBox = new HBox();
		Label searchLbl = new Label(label);
		searchLbl.setPadding(new Insets(5,0,5,0));
		searchLbl.setStyle("-fx-font-weight: bold ;");
		searchTextField = createAutoCompleteTextField("searchTextField", 162, "");
		hBox.setPadding(new Insets(10,0,0,0));
		hBox.getChildren().addAll(searchLbl, searchTextField);
		return hBox;
	}
	
	protected ObjectTablePane<T> createCheckerDetailTablePane(ColumnMappingList columnMappingList,Double[] columnWidths) { 
		
		Rectangle2D parentBounds = Screen.getPrimary().getVisualBounds();
		double scrollPaneWidth = parentBounds.getWidth();
		double scrollPaneHeight = parentBounds.getHeight();
		
		checkerDetailTablePane = new ObjectTablePane<T>(columnMappingList,columnWidths);
		LoggedTableColumn<T, Boolean> slNoCol = new LoggedTableColumn<T, Boolean>();
		createSerialNumber(slNoCol);
		checkerDetailTablePane.getTable().getColumns().add(0, slNoCol);
		checkerDetailTablePane.getTable().getColumns().get(0).setText("Sl No");
		checkerDetailTablePane.getTable().getColumns().get(0).setResizable(true);
		checkerDetailTablePane.getTable().getColumns().get(0).setMaxWidth(45);
		checkerDetailTablePane.getTable().getColumns().get(0).setMinWidth(45);
		checkerDetailTablePane.setConstrainedResize(false);
		checkerDetailTablePane.getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		checkerDetailTablePane.setMinHeight(scrollPaneHeight / 1.46);
		checkerDetailTablePane.setMaxHeight(scrollPaneHeight / 1.46);
		checkerDetailTablePane.setMinWidth(scrollPaneWidth - 300);
		checkerDetailTablePane.setMaxWidth(scrollPaneWidth - 300);
		return checkerDetailTablePane;
	}
	
	protected HBox createButton() {
		HBox hb = new HBox();
		hb.setSpacing(10);
		hb.setPadding(new Insets(5));
		addBtn = createBtn(CheckerConstants.ADD);
		duplicateBtn = createBtn(CheckerConstants.DUPLICATE);
		deleteBtn = createBtn(CheckerConstants.DELETE);
		hb.getChildren().addAll(addBtn, deleteBtn, duplicateBtn);
		return hb;
	}
	
	protected HBox createCheckerComboBox() {
		HBox checkerHBox = new HBox();
		Label checkerLbl = new Label("Checker ");
		checkerLbl.setPadding(new Insets(7,48,5,0));
		checkerLbl.setStyle("-fx-font-weight: bold ;");
		checkerComboBox = new LoggedComboBox<KeyValue<String, String>>("checkerComboBox");
		checkerComboBox.setMinHeight(20.0);
		checkerComboBox.setMinWidth(165.0);
		checkerComboBox.setPrefWidth(165);
		checkerComboBox.setStyle("-fx-font-size: 11pt; -fx-font-family: arial;");
		checkerHBox.setPadding(new Insets(10,0,0,0));
		checkerHBox.getChildren().addAll(checkerLbl, checkerComboBox);
		return checkerHBox;
	}
	
	public LoggedButton createBtn(String text) {
		LoggedButton btn = UiFactory.createButton(text, text);
		btn.defaultButtonProperty().bind(btn.focusedProperty());
		btn.setStyle("-fx-pref-height: 5px; -fx-pref-width: 100px; -fx-font-size : 11pt;");
		btn.getStyleClass().add("table-button");
		return btn;
	}
	
	@SuppressWarnings("unchecked")
	protected void loadCheckerComboBox(List<Checkers> checkers, KeyValue<String, String> keyValue) {
		checkerComboBox.getItems().clear();
		checkerComboBox.setPromptText(CheckerConstants.SELECT);
		checkerComboBox.getItems().add(getKeyValue("", "ALL"));
		Collections.sort(checkers, new Comparator<Checkers>() {
            @Override
            public int compare(Checkers o1, Checkers o2) {
                return o1.name().toString().compareTo(o2.name().toString());
            }
        });
		for (Checkers c : checkers) {
			checkerComboBox.getItems().add(getKeyValue(c.getCheckerClass().getName(), c.toString().replace("_", " ")));
		}
	}
	
	protected void addTableListener(){
		this.checkerDetailTablePane.getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<T>() {
			public void changed(ObservableValue<? extends T> arg0, T arg1, T arg2) {
				addContextMenuItems();
			}
		});
	}
	
	public void addContextMenuItems() {
		List<T> appCheckerList = this.checkerDetailTablePane.getTable().getSelectionModel().getSelectedItems();
		if(this.checkerDetailTablePane.getTable().getContextMenu() != null)
			this.checkerDetailTablePane.getTable().getContextMenu().getItems().clear();
		if(appCheckerList != null && appCheckerList.size() > 0 ) {
			MenuItem update = new MenuItem("Update");
			update.setOnAction(new EventHandler<ActionEvent>() {
			    @Override
			    public void handle(ActionEvent event) {
			    	editChecker(event);
			    }
			});
			ContextMenu rowMenu = new ContextMenu();
			rowMenu.getItems().add(update);
			this.checkerDetailTablePane.getTable().contextMenuProperty().bind(
					Bindings.when(Bindings.isNotNull(this.checkerDetailTablePane.getTable().itemsProperty()))
							.then(rowMenu).otherwise((ContextMenu) null));
		}
	}
	
	protected T getSelectedCheckerForEdit() {
		List<T> appCheckerDtos = checkerDetailTablePane.getTable().getSelectionModel().getSelectedItems();
		if(appCheckerDtos.size() > 1) {
			MessageDialog.showInfo(window.getStage(), "Please Select One Checker");
			return null;
		}
		return appCheckerDtos.get(0);
	}	
	
	protected void handleSearchAction(){
		searchBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                doSearch();
            }
        });
		searchTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				autoCompleteSearch();
			}
		});	
		searchTextField.textProperty().addListener(new ChangeListener<String>() { 
            @Override 
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
            	searchTextChanged();
            } 
        }); 
	}
	
	protected void handleAddDuplicateAction(){
		duplicateBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
            	duplicateChecker();
            }
        });
		addBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
            	addChecker();
            }
        });
		deleteBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
            	deleteChecker(actionEvent);
            }
        });
	}
	
	protected KeyValue<String, String> getKeyValue(String key, final String value) {
		KeyValue<String, String> kv = new KeyValue<String, String>(key, value) {
			private static final long serialVersionUID = 1L;

			@Override
			public String toString() {
				return value;
			}
		};
		return kv;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void createSerialNumber(LoggedTableColumn rowIndex){
		rowIndex.setCellFactory(new Callback<LoggedTableColumn, LoggedTableCell>() {
			public LoggedTableCell call(LoggedTableColumn p) {
				return new LoggedTableCell() {
					@Override
					public void updateItem(Object item, boolean empty) {
						super.updateItem(item, empty);
						setText(empty ? null : getIndex() + 1 + "");
					}
				};
			}
		});
	}
	
	public TitledPane getSearchTitledPane() {
		return searchTitledPane;
	}

	public void setSearchTitledPane(TitledPane searchTitledPane) {
		this.searchTitledPane = searchTitledPane;
	}

	public TitledPane getDetailsTitledPane() {
		return detailsTitledPane;
	}

	public void setDetailsTitledPane(TitledPane detailsTitledPane) {
		this.detailsTitledPane = detailsTitledPane;
	}
	public List<String> getAutoCompleteList() {
		if(autoCompleteList == null)
			autoCompleteList = new ArrayList<String>();
		return autoCompleteList;
	}
	public void setAutoCompleteList(List<String> autoCompleteList) {
		this.autoCompleteList = autoCompleteList;
	}
	public ObjectTablePane<T> getCheckerDetailTablePane() {
		return checkerDetailTablePane;
	}
	
	public ToggleButton createToggleButton() {
		final ToggleButton toggleButton = new ToggleButton(CheckerConstants.ON);
		toggleButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(toggleButton.isSelected()) {
					toggleButton.setText(CheckerConstants.ON);
					toggleButton.setGraphic(createCircleSymbol(Color.GREEN));
				} else {
					toggleButton.setText(CheckerConstants.OFF);
					toggleButton.setGraphic(createCircleSymbol(Color.RED));
				}
			}
		});

		toggleButton.setSelected(true);
		toggleButton.setStyle("-fx-background-insets: 0 0 -1 0,0,1; "
				+ "-fx-background-radius: 5,5,4; -fx-padding: 5 0 5 0; "
				+ "-fx-font-size: 14px; -fx-font-weight: bold; "
				+ "-fx-border-width: 1px; -fx-border-color: grey");
		toggleButton.setMinSize(50, 30);
		toggleButton.setMaxSize(50, 30);
		return toggleButton;
	}
	private Circle createCircleSymbol(Color color) {
		Circle circle = new Circle();
		circle.setRadius(5);
		circle.setFill(color);
		return circle;
	}
}

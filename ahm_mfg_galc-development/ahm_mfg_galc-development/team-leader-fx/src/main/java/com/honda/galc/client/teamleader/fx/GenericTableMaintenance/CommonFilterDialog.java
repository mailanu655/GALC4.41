package com.honda.galc.client.teamleader.fx.GenericTableMaintenance;


import static com.honda.galc.service.ServiceFactory.getService;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Set;
import javafx.util.StringConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.ui.component.FXOptionPane;
import com.honda.galc.client.ui.component.FxDialog;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.AuditEntry;
import com.honda.galc.service.GenericDaoService;
import com.honda.galc.service.Parameters;

/*   
 * @author Gangadhararao Gadde , Subu kathiresan, Raj Salethaiyan
 * 
 *
 *
 */
public class CommonFilterDialog extends FxDialog implements EventHandler<javafx.event.ActionEvent>  {

	private static final long serialVersionUID = 1L;

	private GenericTableMaintenancePanel parentPanel = null;
	private String runQueryDisplay = "Run Query";
	private String cancelDisplay = "   Cancel   ";
	private String clearDisplay = "     Clear    ";
	private String findDisplay = "Find";
	private String moveRigtBtnTxt = ">>";
	private String moveLeftBtnTxt = "<<";
	private String inStr = "IN";
	private String notinStr = "NOT IN";
	private String dataTypeDate = "DATE";
	private String dateTypeTimeStamp = "TIMESTAMP";	
	private String leftTableViewId = "LEFTJLIST";
	private String rightTableViewId = "RIGHTJLIST";
	private String searchFieldName = "SEARCHNAME";
	private String freeFormTxtName = "FREEFORMNAME";
	private String whereString;
	private String selectedFilter;
	private String _filterName;
	private List filterValueList = new ArrayList();
	private GridPane mainPanel;
	private VBox filterListVbox;
	private GridPane buttonContentPanel;
	private GridPane filterValuesPane;
	private GridPane searchContentPanel;
	private VBox moveBtnVbox;
	private Button runQueryButton;
	private Button cancelButton;
	private Button findButton;
	private Button clearButton;
	private Button moveRightBtn;
	private Button moveLeftBtn;
	private DatePicker calendarDatePicker;
	private TextField calendarTimeTxtField;
	private Label calendarLabel;
	private Label calendarTimeLabel;
	private Label selectedFilterName = UiFactory.createLabel("selectedFilterName", "");
	private TextField searchField;
	private TextField fiterTxtFld;
	private TextField formTextField;
	private ScrollPane filterListScrollPane;
	private TableView leftFilterValueTableView;
	private ArrayList<TextField> filterTxtFields = new ArrayList<TextField>();
	private ArrayList<Label> filterLabels = new ArrayList<Label>();
	private NotifyParent notifyParent = new NotifyParent();
	private boolean isTimeStamp;
	private TableView rightFilterValueTableView = UiFactory.createTableView();	
	private String observerAction=null;
	private Hashtable<String, GenericTableMaintFilter> filtersMap = new Hashtable<String, GenericTableMaintFilter>();
	private ArrayList<GenericTableMaintFilter> filtersList = new ArrayList<GenericTableMaintFilter>();
	private String applicationID=null;




	public CommonFilterDialog(GenericTableMaintenancePanel parentPanel, String applicationID, String filterName) {
		super("", ClientMainFx.getInstance().getStage());
		setResizable(true);
		filtersList = TableMaintenanaceProperties.getFilters(applicationID);
		buildFilterMap();
		this._filterName = filterName;
		System.out.println("Number of filters retrieved: " + filtersList.size());
		this.setParentPanel(parentPanel);
		notifyParent.addObserver(parentPanel);
		this.applicationID=applicationID;
		initialize();
	}

	public void buildFilterMap(){

		for(GenericTableMaintFilter filter: filtersList) {
			filtersMap.put(filter.getId(), filter);
		}
	}

	private void initialize() {

		try {
			Logger.getLogger().info("### CommonFilter: Initializing Filter window screen ###");
			mainPanel = new GridPane();
			mainPanel.setHgap(10);
			mainPanel.setVgap(10);
			String userMessage = "Please enter the filter criteria ";
			Label usermsgLbl = UiFactory.createLabel("usermsgLbl");
			usermsgLbl.setText(userMessage);
			usermsgLbl.setFont(Font.font("Italic", FontWeight.NORMAL, 17));
			mainPanel.add(usermsgLbl, 0,0);
			mainPanel.add(getFilterListScrollPane(), 0,1);
			mainPanel.add(getButtonPane(), 1,1);
			mainPanel.add(getFilterValuesPane(),0,2);
			mainPanel.add(getSearchPane(), 0,3);
			VBox vbox=new VBox(50);
			vbox.setFillWidth(true);
			vbox.setPrefWidth(900);
			vbox.setPrefHeight(600);
			vbox.getChildren().add(mainPanel);
			((BorderPane)this.getScene().getRoot()).setCenter(vbox);
			this.showDialog();	
			
			Logger.getLogger().info("### CommonFilter: Filter window screen has been initialized ###");
		} catch (Exception exp) {
			Logger.getLogger().info("### CommonFilter: Exception during Filter screen initialization ###");
			handleException(exp);
		}

	}

	private ScrollPane getFilterListScrollPane() {
		try {
			if (filterListScrollPane == null) {
				filterListScrollPane = new ScrollPane();
				filterListScrollPane.setMaxWidth(600);
				filterListScrollPane.setMaxHeight(300);
				if (filterListVbox == null)
					filterListVbox = new VBox();
				filterListVbox.setSpacing(10);				
				java.util.Calendar cal = java.util.Calendar.getInstance();
				SimpleDateFormat sdfdate = new SimpleDateFormat("yyyy-MM-dd");
				String date = sdfdate.format(cal.getTime());
				SimpleDateFormat sdftime = new SimpleDateFormat("HH:mm:ss");
				String time = sdftime.format(cal.getTime());
				for(GenericTableMaintFilter filter: filtersList) {
					int i=0;
					String key = filter.getId().toString();
					GenericTableMaintFilter fltr = filtersMap.get(key);
					Label filterNameLabel = UiFactory.createLabel("filterNameLabel");
					fiterTxtFld = UiFactory.createTextField("fiterTxtFld");
					fiterTxtFld.setEditable(false);
					fiterTxtFld.setStyle("-fx-background-color: white;-fx-border-insets: 0;-fx-border-width: 2px;-fx-border-color: lightgray ");
					filterNameLabel.setStyle("-fx-border-insets: 0;-fx-border-width: 2px;-fx-border-color: lightgray ");;				
					filterNameLabel.setText("  " + (fltr.getLabel()));
					fiterTxtFld.setText(fltr.getDefaultValue());
					if (fiterTxtFld.getText().length() == 0 && fltr.getDataType().equalsIgnoreCase(dataTypeDate)) {
						fiterTxtFld.setText(date);
					}
					if (fiterTxtFld.getText().length() == 0 && fltr.getDataType().equalsIgnoreCase(dateTypeTimeStamp)) {
						fiterTxtFld.setText(date + " " + time);
					}

					if (fiterTxtFld.getText().length() == 0) {
						fiterTxtFld.setStyle("-fx-background-color: white;-fx-border-insets: 0;-fx-border-width: 2px;-fx-border-color: lightgray ");
					}				
					GridPane filterRow = new GridPane();
					filterRow.setHgap(10);
					Label paramLbl = UiFactory.createLabel("paramLbl");
					paramLbl.setText(fltr.getParamType());
					filterRow.add(filterNameLabel,0,0);
					filterRow.add(paramLbl,1,0);
					filterRow.add(fiterTxtFld,2,0);				
					filterNameLabel.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, new EventHandler<javafx.scene.input.MouseEvent>() {
						public void handle(javafx.scene.input.MouseEvent e) {
							CommonFilterDialog.this.mouseClicked(e);
						}
					});
					filterNameLabel.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_ENTERED, new EventHandler<javafx.scene.input.MouseEvent>() {
						public void handle(javafx.scene.input.MouseEvent e) {
							CommonFilterDialog.this.mouseEntered(e);
						}
					});				
					filterNameLabel.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_EXITED, new EventHandler<javafx.scene.input.MouseEvent>() {
						public void handle(javafx.scene.input.MouseEvent e) {
							CommonFilterDialog.this.mouseExited(e);
						}
					});
					filterNameLabel.setId(key);
					fiterTxtFld.setId(key);
					filterTxtFields.add(fiterTxtFld);
					filterLabels.add(filterNameLabel);
					filterListVbox.getChildren().add(filterRow);
					if(i==1) break;
					i++;
				}
				//filterListVbox.setStyle("-fx-background-color: white;");;
				filterListVbox.setVgrow(filterListScrollPane, Priority.ALWAYS);
				filterListVbox.setFillWidth(true);
				filterListScrollPane.setContent(filterListVbox);
				filterListScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
				filterListScrollPane.setStyle("-fx-border-insets: 0;-fx-border-width: 2px;-fx-border-color: lightgray ");;
			}
		} catch (Exception exp) {
			Logger.getLogger().info("### CommonFilter: Exception while fetching list of table columns ###");
			handleException(exp);
		}
		return filterListScrollPane;
	}

	private GridPane getButtonPane() {
		try {
			if (buttonContentPanel == null) {
				buttonContentPanel = new GridPane();
				buttonContentPanel.setPrefWidth(300);
				runQueryButton = UiFactory.createButton(runQueryDisplay);
				cancelButton = UiFactory.createButton(cancelDisplay);
				clearButton = UiFactory.createButton(clearDisplay);
				clearButton.setVisible(true);
				runQueryButton.setId("RUN_QUERY");
				runQueryButton.setOnAction(this);
				buttonContentPanel.add(runQueryButton, 1,1);
				cancelButton.setId("CANCEL");
				cancelButton.setOnAction(this);
				buttonContentPanel.add(cancelButton, 1,2);
				clearButton.setId("CLEAR");
				clearButton.setOnAction(this);
				buttonContentPanel.add(clearButton, 1,3);
			}
		} catch (Exception exp) {
			Logger.getLogger().info("### CommonFilter: Exception while building button content panel ###");
			handleException(exp);
		}
		return buttonContentPanel;
	}

	private GridPane getFilterValuesPane() {
		try {
			if (leftFilterValueTableView == null) {
				leftFilterValueTableView = UiFactory.createTableView();
				if (filterValuesPane == null)
					filterValuesPane = new GridPane();
				filterValuesPane.setPrefHeight(300);
				filterValuesPane.setHgap(10);
				filterValuesPane.setVgap(10);
				formTextField = UiFactory.createTextField("freeFormTxtName");
				formTextField.setVisible(false);
				formTextField.setOnKeyPressed( new EventHandler<KeyEvent>()   
						{   
					public void handle( KeyEvent e )   
					{   
						if (e.getCode().equals(KeyCode.ENTER))
							moveValuesRight();
					}   
						} );
				filterValuesPane.add(formTextField, 0,0);
				calendarLabel=UiFactory.createLabel("calendarDate", "Date(yyyy-mm-dd)");
				calendarLabel.setVisible(false);
			    calendarTimeLabel=UiFactory.createLabel("calendarTimeLabel", "Time(HH:mm:ss)");
			    calendarTimeLabel.setVisible(false);
				calendarDatePicker = getCalendarDatePicker();								
				filterValuesPane.add(calendarLabel, 1,0);
				filterValuesPane.add(calendarDatePicker, 2,0);	
				filterValuesPane.add(calendarTimeLabel, 3,0);
				calendarTimeTxtField=getCalendarTimeTextField();				
				filterValuesPane.add(calendarTimeTxtField, 4,0);
				filterValuesPane.add(selectedFilterName, 1,1);
				moveBtnVbox = new VBox();				
				moveRightBtn = UiFactory.createButton(moveRigtBtnTxt);
				moveLeftBtn = UiFactory.createButton(moveLeftBtnTxt);
				moveRightBtn.setDisable(true);
				moveLeftBtn.setDisable(true);
				moveRightBtn.setOnAction(this);
				moveLeftBtn.setOnAction(this);
				moveBtnVbox.getChildren().add(moveRightBtn);
				moveBtnVbox.getChildren().add(moveLeftBtn);
				moveBtnVbox.setSpacing(10);
				leftFilterValueTableView.setStyle("-fx-border-insets: 0;-fx-border-width: 2px;-fx-border-color: lightgray ");
				leftFilterValueTableView.getColumns().clear(); 
				leftFilterValueTableView.getItems().clear(); 
				TableColumn<AuditEntry,String> column = UiFactory.createTableColumn(AuditEntry.class, String.class);
				column.setPrefWidth(260);
				column.setCellValueFactory(new PropertyValueFactory<AuditEntry,String>("columnName"));
				leftFilterValueTableView.getColumns().add(column);				
				filterValuesPane.add(leftFilterValueTableView,0 ,2);
				filterValuesPane.add(moveBtnVbox, 1,2);
				VBox  vbox=new VBox();
				vbox.getChildren().add(rightFilterValueTableView);				
				rightFilterValueTableView.setId(rightTableViewId);
				rightFilterValueTableView.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, new EventHandler<javafx.scene.input.MouseEvent>() {
					public void handle(javafx.scene.input.MouseEvent e) {
						CommonFilterDialog.this.mouseClicked(e);
					}
				});
				rightFilterValueTableView.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_ENTERED, new EventHandler<javafx.scene.input.MouseEvent>() {
					public void handle(javafx.scene.input.MouseEvent e) {
						CommonFilterDialog.this.mouseEntered(e);
					}
				});

				rightFilterValueTableView.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_EXITED, new EventHandler<javafx.scene.input.MouseEvent>() {
					public void handle(javafx.scene.input.MouseEvent e) {
						CommonFilterDialog.this.mouseExited(e);
					}
				});
				TableColumn<AuditEntry,String> column2 = UiFactory.createTableColumn(AuditEntry.class, String.class);
				column2.setPrefWidth(275);
				column2.setCellValueFactory(new PropertyValueFactory<AuditEntry,String>("columnName"));
				rightFilterValueTableView.getColumns().add(column2);
				filterValuesPane.add(rightFilterValueTableView,2,2);
			}
		} catch (Exception exp) {
			Logger.getLogger().info("### CommonFilter: Exception while building column value panel ###");
			handleException(exp);
		}
		return filterValuesPane;
	}

	private TextField getCalendarTimeTextField() {
		if(calendarTimeTxtField==null)
		{
			calendarTimeTxtField = UiFactory.createTextField("calendarTimeTxtField", "");
			calendarTimeTxtField.setVisible(false);
			calendarTimeTxtField.setOnKeyPressed( new EventHandler<KeyEvent>()   
					{   
				public void handle( KeyEvent e )   
				{   
					if (e.getCode().equals(KeyCode.ENTER))
					{
						if (isTimeStamp ) {
							if (calendarDatePicker.getValue() != null&& !calendarTimeTxtField.getText().trim().equals("HH:mm:ss")&& calendarTimeTxtField.getText().trim().length()>0) {
								CommonFilterDialog.this.formTextField.setText(calendarDatePicker.getValue()+ " "+ calendarTimeTxtField.getText());
							} else
								formTextField.setText("");
						}
						if (formTextField.getText().length() > 0) {
							CommonFilterDialog.this.moveValuesRight();
						}
					}

				}   
					} );
		}
		return calendarTimeTxtField;
	}

	private GridPane getSearchPane() {
		try {
			if (searchContentPanel == null) {
				searchContentPanel = new GridPane();
				searchContentPanel.setHgap(10);
				searchField = UiFactory.createTextField("searchField");
				searchField.setText("");
				searchField.setPrefWidth(150);
				searchField.setStyle("-fx-background-color: white;-fx-border-insets: 0;-fx-border-width: 2px;-fx-border-color: lightgray ");
				searchField.setTooltip(new Tooltip("Enter search value here"));
				searchField.setId(searchFieldName);
				searchField.setOnKeyPressed( new EventHandler<KeyEvent>()   
						{   
					public void handle( KeyEvent e )   
					{   
						if (e.getCode().equals(KeyCode.ENTER))
							doFind();
					}   
						} );
				searchContentPanel.add(searchField, 0,0);
				findButton = UiFactory.createButton(findDisplay);
				findButton.setOnAction(this);
				searchContentPanel.add(findButton,1,0);
			}
		} catch (Exception exp) {
			Logger.getLogger().info("### commonFilter: Exception while building search content pane ###");
			handleException(exp);
		}
		return searchContentPanel;
	}

	private void handleException(Exception exception) {
		exception.printStackTrace();
	}

	public void mouseClicked(MouseEvent event) {
		searchField.setText("");
		String seletedFilterName = ((Control)(event.getSource())).getId();
		System.out.println("Clicked Param " + seletedFilterName);
		if (seletedFilterName.equals(leftTableViewId)) {
			if (event.getClickCount() == 2) {
				moveValuesRight();
			}
		} else if (seletedFilterName.equals(rightTableViewId)) {
			if (event.getClickCount() == 2) {
				moveValuesLeft();
			}
		} else {
			selectedFilter = seletedFilterName;
			moveRightBtn.setDisable(false);
			this.formTextField.setText("");
			GenericTableMaintFilter fltr = this.filtersMap.get(seletedFilterName);
			selectedFilterName.setText(fltr.getLabel());
			if (fltr.isFreeFormTextAllowed()) {
				this.formTextField.setVisible(true);
				this.formTextField.requestFocus();
				if (fltr.getDataType().toString().equalsIgnoreCase(dataTypeDate)|| fltr.getDataType().toString().equalsIgnoreCase(dateTypeTimeStamp)) {
					hideDisplayCalendar(true);
					formTextField.setEditable(false);
					if (fltr.getDataType().toString().equalsIgnoreCase(dateTypeTimeStamp)) {
						this.isTimeStamp = true;
					} else {
						this.isTimeStamp = false;
					}
				} else {
					hideDisplayCalendar(false);
					this.formTextField.setEditable(true);
					this.isTimeStamp = false;
				}
			} else {
				this.formTextField.setVisible(false);
				hideDisplayCalendar(false);
				this.isTimeStamp = false;
			}

			String str = null;
			for (TextField jtxt : filterTxtFields) {
				if (jtxt != null && jtxt.getId().trim().equalsIgnoreCase(seletedFilterName)) {
					if (jtxt.getText().length() > 0)
						jtxt.setStyle("-fx-background-color: green;");
					jtxt.setStyle("-fx-border-width: 2px;-fx-border-color: blue;");
					rightFilterValueTableView.getItems().clear();
					Iterator itr = getIndividualValues(jtxt.getText().toString()).iterator();
					while (itr.hasNext()) {
						str = itr.next().toString();
						if (str != null && str.length() > 0)
							rightFilterValueTableView.getItems().add( new GenericMaintenanceTableDto(str));
					}
				} else {
					if (jtxt.getText().length() > 0) {
						jtxt.setStyle("-fx-background-color: white;");
						jtxt.setStyle("-fx-border-insets: 0;-fx-border-width: 2px;-fx-border-color: lightgray ");;
					} else
						jtxt.setStyle("-fx-border-insets: 0;-fx-border-width: 2px;-fx-border-color: lightgray ");;
				}
			}
			filterValueList = fetchFilterValues(this.filtersMap.get(seletedFilterName));
			buildFilterScrollPane(this.filtersMap.get(seletedFilterName), filterValueList);
			if (this.rightFilterValueTableView.getItems().size()== 0) {
				System.out.println("Right size is Zero"+ rightFilterValueTableView.getItems().size());
				moveLeftBtn.setDisable(true);
			} else if (rightFilterValueTableView.getItems().size() > 0) {
				System.out.println("Right size is non-zero "+ rightFilterValueTableView.getItems().size());
				moveLeftBtn.setDisable(false);
			}
		}
	}
	
	public void hideDisplayCalendar(boolean visibleBooleanVal)
	{
		calendarDatePicker.setVisible(visibleBooleanVal);
		calendarLabel.setVisible(visibleBooleanVal);
		calendarTimeLabel.setVisible(visibleBooleanVal);
		calendarTimeTxtField.setVisible(visibleBooleanVal);
	}

	public void mouseEntered(MouseEvent e) {
		if (!(e.getSource().equals(leftFilterValueTableView)|| e.getSource().equals(rightTableViewId))) {
			this.getScene().setCursor(javafx.scene.Cursor.CLOSED_HAND);
		}
	}

	public void mouseExited(MouseEvent e) {
		if (!(e.getSource().equals(leftFilterValueTableView)|| e.getSource().equals(rightTableViewId))) {
			this.getScene().setCursor(javafx.scene.Cursor.DEFAULT);
		}
	}

	public ArrayList<String> getFilterNames() {
		ArrayList<String> filterNames = new ArrayList<String>();
		try {
			Enumeration keyEnum = this.filtersMap.keys();
			while (keyEnum.hasMoreElements()) {
				GenericTableMaintFilter fltr = filtersMap.get(keyEnum.nextElement());
				filterNames.add(fltr.getLabel());
			}

		} catch (Exception exp) {
			Logger.getLogger().info("### CommonFilter.getColumnNames(String columnName) : Exception while fetching column value list ###");
			handleException(exp);
		}
		return filterNames;
	}

	public ArrayList<String> fetchFilterValues(GenericTableMaintFilter fltr) {
		ArrayList<String> filterValues = new ArrayList<String>();
		try {
			if (fltr.getFilterValueSource().equals(FilterValueSource.DBCOLUMN)) {
				filterValues = getSelectionListFromDbColumn(fltr, null);
			} else if (fltr.getFilterValueSource().equals(FilterValueSource.DBMULTICOLUMN)) {
				System.out.println("Fetch list of values from multiple DB column");
			} else if (fltr.getFilterValueSource().equals(FilterValueSource.FREEFORM)) {
				System.out.println("Provided list of values");
				filterValues = getIndividualValues(fltr.getValues());
			} else if (fltr.getFilterValueSource().equals(FilterValueSource.CUSTOMBUILDER)) {
				Class obj = Class.forName(fltr.getValues());
				ICustomWhereClauseBuilder objCustomWhere = (ICustomWhereClauseBuilder) obj.newInstance();
				filterValues = objCustomWhere.getDisplayList();
			}

		} catch (Exception exp) {
			Logger.getLogger().info("### CommonFilter.getColumnValues(String columnName) : Exception while fetching column value list ###");
			handleException(exp);
		}
		return filterValues;
	}

	private ArrayList<String> getSelectionListFromDbColumn(GenericTableMaintFilter fltr, String likeValue) throws Exception {
		ArrayList<String> values = new ArrayList<String>();
		StringBuilder query = new StringBuilder();
		String columnName = fltr.getValues();
		if (columnName == null || columnName.trim().equals(""))
			return values;
		String entityName = columnName.substring(0, columnName.indexOf('.'));
		columnName = columnName.substring(columnName.indexOf('.') + 1);
		query.append("select distinct e." + columnName + " from " + entityName +" e");
		List resultList =null;
		
		if (likeValue != null) {
			String[] commaSeparatedLikeValues = likeValue.split(",");
			if(commaSeparatedLikeValues.length > 1)
			{
				
				query.append(" where e." + columnName + " IN (:commaSeparatedLikeValues)");					
				resultList = getService(GenericDaoService.class).executeGenericSelectQuery(query.toString(),Parameters.with("commaSeparatedLikeValues", Arrays.asList(commaSeparatedLikeValues)),null);
			}
			else
			{
				if(fltr.getDataType().equalsIgnoreCase(String.class.getName())||fltr.getDataType().equalsIgnoreCase(Timestamp.class.getSimpleName()))
				{
					query.append(" where lower(e." + columnName + ") like " + "'%" + likeValue.toLowerCase() + "%'");
				}else{
					query.append(" where e." + columnName + " = "  + likeValue.toLowerCase() ); 
				}
				resultList = getService(GenericDaoService.class).executeGenericSelectQuery(query.toString(),null,getParentPanel().getGenericTableMaintenancePropertyBean().getMaxRowCount());
			}
		}else
			resultList = getService(GenericDaoService.class).executeGenericSelectQuery(query.toString(),null,getParentPanel().getGenericTableMaintenancePropertyBean().getMaxRowCount());
				
		for (Object val: resultList) {
			if(val!=null)
				values.add(val.toString().trim());
		}
		return values;
	}

	public void buildFilterScrollPane(GenericTableMaintFilter fltr, List columnValList) {
		try {
			leftFilterValueTableView.setId(leftTableViewId);
			leftFilterValueTableView.setStyle("-fx-border-insets: 0;-fx-border-width: 2px;-fx-border-color: lightgray ");;
			leftFilterValueTableView.getItems().clear();
			if (fltr.isNullAllowed()) {
				leftFilterValueTableView.getItems().add("NULL");
			}
			ArrayList<GenericMaintenanceTableDto> list=new ArrayList<GenericMaintenanceTableDto>();
			for(Object value:columnValList)
			{
				list.add(new GenericMaintenanceTableDto(value.toString()));
			}
			leftFilterValueTableView.getItems().addAll(FXCollections.observableArrayList(list));
			if (columnValList.size() >= 200) {
				leftFilterValueTableView.getItems().add("...");
			}
			if (fltr.getParamType().equalsIgnoreCase(inStr)|| fltr.getParamType().equalsIgnoreCase(notinStr))
				leftFilterValueTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			else
				leftFilterValueTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			leftFilterValueTableView.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent e) {
					CommonFilterDialog.this.mouseClicked(e);
				}
			});
			leftFilterValueTableView.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_ENTERED, new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent e) {
					CommonFilterDialog.this.mouseEntered(e);
				}
			});
			leftFilterValueTableView.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_EXITED, new EventHandler<javafx.scene.input.MouseEvent>() {
				public void handle(javafx.scene.input.MouseEvent e) {
					CommonFilterDialog.this.mouseExited(e);
				}
			});
		} catch (Exception exp) {
			Logger.getLogger().info("### CommonFilter.buildColValueScrollPane(ArrayList<String> columnValueList1) : Exception while building Column value scroll pane ###");
			handleException(exp);
		}

	}

	public boolean setSelectedValues() {
		boolean isReadyForWhere = false;
		try {
			for (TextField jtf : filterTxtFields) {
				if (jtf != null && (!isNullOrEmpty(jtf.getText()))) {
					this.filtersMap.get(jtf.getId()).setSelectedValues(getIndividualValues(jtf.getText()));
				}
			}
			Enumeration keys = this.filtersMap.keys();
			String key = null;
			while (keys.hasMoreElements()) {
				key = keys.nextElement().toString();
				if (!(this.filtersMap.get(key).getSelectedValues() == null || this.filtersMap.get(key).getSelectedValues().size() ==0 )) {				
					isReadyForWhere = true;
					break;
				}
			}

		} catch (Exception exp) {
			Logger.getLogger().info("### CommonFilter.getDataHash(String columnName) : Exception while fetching column value list ###");
			handleException(exp);
		}
		return isReadyForWhere;
	}

	public void doFind() {
		try {
			ArrayList<String> searchResult = new ArrayList<String>();
			if (trimSafe(searchField.getText()).length() == 0) {
				FXOptionPane.showMessageDialog(null,"Please enter a find text","");
				this.searchField.requestFocus();
			} else {
				searchResult = getSelectionListFromDbColumn(this.filtersMap.get(selectedFilter), (trimSafe(searchField.getText())));
				if (searchResult.size() == 0) {
					FXOptionPane.showMessageDialog(null,"No data found that matches '" + trimSafe(searchField.getText()) + "'","");
					this.searchField.requestFocus();
				} else
					buildFilterScrollPane(this.filtersMap.get(selectedFilter), searchResult);
			}
		} catch (Exception exp) {
			Logger.getLogger().info("### CommonFilter.doFind() : Exception while trying to do a find ###");
			handleException(exp);
		}
	}

	public ArrayList<String> getIndividualValues(String withComa) {
		ArrayList<String> returnList = new ArrayList<String>();
		try {
			String[] splitArray = null;
			splitArray = withComa.split(";");
			for (int i = 0; i < splitArray.length; i++) {
				returnList.add(splitArray[i]);
			}
		} catch (Exception exp) {
			Logger.getLogger().info("### CommonFilter.getIndValues(String withComa) : Exception while fetching individual values ###");
			handleException(exp);
		}
		return returnList;
	}

	private DatePicker getCalendarDatePicker() {
		 final String pattern = "yyyy-MM-dd";
		if (calendarDatePicker == null) {
			 calendarDatePicker = new DatePicker();			 
			 StringConverter converter = new StringConverter<LocalDate>() {
				   DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
		            @Override
		            public String toString(LocalDate date) {
		                if (date != null) {
		                    return dateFormatter.format(date);
		                } else {
		                    return "";
		                }
		            }
		            @Override
		            public LocalDate fromString(String string) {
		                if (string != null && !string.isEmpty()) {
		                    return LocalDate.parse(string, dateFormatter);
		                } else {
		                    return null;
		                }
		            }
		        };  		        
		        calendarDatePicker.setConverter(converter);
		        calendarDatePicker.setPromptText(pattern.toLowerCase());	
		        calendarDatePicker.setVisible(false);
		}
		return calendarDatePicker;
	}




	public void moveValuesRight() {
		boolean isMultiSelect = false;
		if (this.filtersMap.get(selectedFilter).getParamType().equalsIgnoreCase(inStr)	|| this.filtersMap.get(selectedFilter).getParamType().equalsIgnoreCase(notinStr))
			isMultiSelect = true;
		ObservableList<Integer> selectedIx = this.leftFilterValueTableView.getSelectionModel().getSelectedIndices();
		if (!isMultiSelect && selectedIx.size() > 0	&& this.formTextField.getText().trim().length() > 0) {
			FXOptionPane.showMessageDialog(null,"Please use either free form text or column value, not both.","");
			this.leftFilterValueTableView.getSelectionModel().clearSelection();
			return;
		}
		Set<String> hset = new HashSet<String>();
		if (!isMultiSelect && (selectedIx.size() > 0 || this.formTextField.getText().trim().length() > 0)) {
			this.rightFilterValueTableView.getItems().clear();
		}
		if (this.formTextField.getText().length() > 0) {
			if (this.filtersMap.get(selectedFilter).getDataType().equalsIgnoreCase(dateTypeTimeStamp)) {
				String date = this.formTextField.getText().trim().split(" ")[0];
				String time = this.formTextField.getText().trim().split(" ")[1];
				this.formTextField.setText(date + " " + time);
			}
			hset.add(this.formTextField.getText().trim());
		}
		for (int i = 0; i < this.rightFilterValueTableView.getItems().size(); i++) {
			hset.add(((GenericMaintenanceTableDto)this.rightFilterValueTableView.getItems().get(i)).getColumnName());
		}
		this.rightFilterValueTableView.getItems().clear();
		for (int j = 0; j < selectedIx.size(); j++) {
			Object value = leftFilterValueTableView.getItems().get(selectedIx.get(j));
			if (!value.toString().equals("..."))
				hset.add(((GenericMaintenanceTableDto)value).getColumnName());
		}
		ArrayList<GenericMaintenanceTableDto> list=new ArrayList<GenericMaintenanceTableDto>();
		for(String value:hset)
		{
			list.add(new GenericMaintenanceTableDto(value));
		}
		rightFilterValueTableView.getItems().addAll(FXCollections.observableArrayList(list));
		for (TextField jtxt : filterTxtFields) {
			if (jtxt != null && jtxt.getId().trim().equalsIgnoreCase(selectedFilter)) {
				jtxt.setText("");
				for (int i = 0; i < this.rightFilterValueTableView.getItems().size(); i++) {
					if (jtxt.getText().length() > 0)
						jtxt.setText(jtxt.getText()+ ";"+ (((GenericMaintenanceTableDto)this.rightFilterValueTableView.getItems().get(i)).getColumnName()));
					else
						jtxt.setText((((GenericMaintenanceTableDto)this.rightFilterValueTableView.getItems().get(i)).getColumnName()));
				}
				if (jtxt.getText().length() > 0) {
					jtxt.setStyle("-fx-background-color: green;");
				}
			}
		}

		if (this.rightFilterValueTableView.getItems().size() == 0) {
			this.moveLeftBtn.setDisable(true);
		} else
			this.moveLeftBtn.setDisable(false);
	}

	public void moveValuesLeft() {
		ObservableList<Integer> selectedIx = this.rightFilterValueTableView.getSelectionModel().getSelectedIndices();
		ArrayList lst = new ArrayList();
		for (int j = 0; j < selectedIx.size(); j++) {
			Object sel = rightFilterValueTableView.getItems().get(selectedIx.get(j));
			lst.add(sel);
		}
		Iterator itr = lst.iterator();
		while (itr.hasNext()) {
			this.rightFilterValueTableView.getItems().remove(itr.next());
		}

		if (this.rightFilterValueTableView.getItems().size() == 0) {
			this.moveLeftBtn.setDisable(true);
		} else
			this.moveLeftBtn.setDisable(false);
		for (TextField jtxt : filterTxtFields) {
			if (jtxt != null && jtxt.getId().trim().equalsIgnoreCase(selectedFilter)) {
				jtxt.setText("");
				for (int i = 0; i < this.rightFilterValueTableView.getItems().size(); i++) {
					jtxt.setText(jtxt.getText()+ ";" + ((GenericMaintenanceTableDto)this.rightFilterValueTableView.getItems().get(i)).getColumnName());
				}
				if (jtxt.getText().length() == 0) {
					jtxt.setStyle("-fx-background-color: white;");;
				}
			}
		}
	}

	private void setParentPanel(GenericTableMaintenancePanel parentPanel) {
		this.parentPanel = parentPanel;
	}

	public GenericTableMaintenancePanel getParentPanel() {
		return parentPanel;
	}

	public class NotifyParent extends Observable {
		public void runClicked(CommonFilterDialog commonFilter) {
			try {
				setChanged();
				notifyObservers(commonFilter);
			} catch (Exception exp) {
				Logger.getLogger().info("### NotifyParent.runClicked(): Exception while notifying parent screen after 'Run Query' ###");
				handleException(exp);
			}

		}
	}

	public String getWhereString() {
		return whereString;
	}

	public void setWhereString(String whereString) {
		this.whereString = whereString;
	}

	public void handle(ActionEvent event) {


		if (event.getSource().equals(runQueryButton)) {
			setSelectedValues();
			WhereClauseGenerator whereClause = new WhereClauseGenerator(this.filtersMap);
			String where = whereClause.getWhereStringBuff().toString();
			if(where.length()<6)
				this.setWhereString("");
			else
				this.setWhereString(where);
			this.setObserverAction(this._filterName);
			notifyParent.runClicked(this);
			this.close();
		}
		else if (event.getSource().equals(cancelButton)) {
			this.setObserverAction("CANCEL");
			notifyParent.runClicked(this);
			this.close();
		}
		else if (event.getSource().equals(findButton)) {
			doFind();
		}
		else if (event.getSource().equals(clearButton)) {
			this.selectedFilterName.setText("");
			this.searchField.setText("");
			this.formTextField.setText("");
		}
		else if (event.getSource().equals(moveRightBtn)) {
			System.out.println("Move right button clicked");
			moveValuesRight();
		}
		else if (event.getSource().equals(moveLeftBtn)) {
			System.out.println("Move left button clicked");
			moveValuesLeft();
		}
	}
	
	public String getFilterName() {
		return _filterName;
	}

	public void setFilterName(String name) {
		_filterName = name;
	}
	public String getObserverAction() {
		return observerAction;
	}

	public void setObserverAction(String observerAction) {
		this.observerAction = observerAction;
	}
	public static boolean isNullOrEmpty(String str){
		return str == null || str.trim().length() == 0 ? true:false;
	}
	
	public static String trimSafe(String str) {
		return str == null ? null : str.trim();
	}
}
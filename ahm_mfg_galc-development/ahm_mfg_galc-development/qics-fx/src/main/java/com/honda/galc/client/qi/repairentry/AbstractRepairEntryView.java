package com.honda.galc.client.qi.repairentry;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.qi.base.AbstractQiProcessController;
import com.honda.galc.client.qi.base.AbstractQiProcessView;
import com.honda.galc.client.qi.base.QiProcessModel;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.FxDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.util.DefectImageUtil;
import com.honda.galc.client.utils.QiProgressBar;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.MediaUtils;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dto.qi.QiRepairResultDto;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.enumtype.KickoutStatus;
import com.honda.galc.entity.enumtype.QiEntryStationConfigurationSettings;
import com.honda.galc.entity.qi.QiDefectResultImage;
import com.honda.galc.entity.qi.QiImage;
import com.honda.galc.entity.qi.QiRepairResultImage;
import com.honda.galc.entity.qi.QiStationConfiguration;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Screen;
import javafx.util.Callback;

public abstract class AbstractRepairEntryView<M extends QiProcessModel,C extends AbstractQiProcessController<?,?>> extends AbstractQiProcessView<RepairEntryModel, AbstractRepairEntryController<?,?>> {

	protected Hyperlink hyperlink;
	private static final double BUTTON_PREF_WIDTH = 250;
	private static final double BUTTON_PREF_HEIGHT = 100;
	protected static final String DEFECT_ACTUAL_PROBLEM = "Defect is the Actual Problem";
	protected static final String ADD_REPAIR_METHOD_FOR_ACTUAL_PROBLEM = "Repair Method";
	protected static final String SCRAP = "Scrap";
	protected static final String ASSIGN_ACTUAL_PROBLEM = "Assign the Actual Problem to Defect";
	protected static final String NO_PROBLEM_FOUND = "No Problem Found";
	protected static final String SET_ACTUAL_PROBLEM_NOT_FIXED = "Set Actual Problem To Not Fixed";
	protected static final String DELETE_ACTUAL_PROBLEM = "Delete Actual Problem";
	protected static final String ADD_NEW_DEFECTS = "Add New Defects";
	protected static final String UPDATE_REPAIR_AREA = "Update Repair Area";
	protected static final String REAL_PROBLEM_DEF_CATG = "REAL PROBLEM";
	protected static final String SYMPTOM_DEF_CATG = "SYMPTOM";

	private static List<QiRepairResultDto> parentCachedDefectList;
	protected LoggedButton defectActualProblemButton;
	protected LoggedButton addRepairMethodButton;
	protected LoggedButton scrapButton;
	protected LoggedButton assignActualProblemButton;
	protected LoggedButton noProblemFoundButton;
	protected LoggedButton setActualProblemToNotFixedButton;
	protected LoggedButton deleteActualProblemButton;
	protected LoggedButton addNewDefectButton;
	protected LoggedButton updateRepairAreaButton;
	protected LoggedButton uploadImageButton;
	protected TitledPane repairOptionsPane;
	protected TreeTableView<QiRepairResultDto> treeTablePane;
	protected boolean isProductScraped;
	protected LoggedLabel plantLabel;
	protected LoggedLabel parkingRepairAreaLabel;
	protected LoggedLabel rowLabel;
	protected LoggedLabel spaceLabel;
	QiProgressBar qiProgressBar = null;

	public AbstractRepairEntryView(ViewId viewId, MainWindow window) {
		super(viewId, window);
	}

	// **********Creating main table pane ***************//

	protected Node getMainTablePane() {

		HBox mainTablePane = new HBox();
		mainTablePane.setPrefHeight(getScreenHeight() * 0.40);
		mainTablePane.setPadding(new Insets(10, 10, 0, 10));
		mainTablePane.getChildren().addAll(createTreeTablePane());
		mainTablePane.fillHeightProperty();
		mainTablePane.setAlignment(Pos.CENTER);
		return mainTablePane;
	}
	
	@SuppressWarnings("unchecked")
	protected TreeTableView<QiRepairResultDto> createTreeTablePane() {
		// Create tables
		final TreeItem<QiRepairResultDto> root = new TreeItem<QiRepairResultDto>(new QiRepairResultDto());
		treeTablePane = new TreeTableView<QiRepairResultDto>(root);


		// ********** Initialized tree table columns ****************//
		TreeTableColumn<QiRepairResultDto, String> defDescColumn = new TreeTableColumn<QiRepairResultDto, String>("Defect Description");
		defDescColumn.setPrefWidth(getScreenWidth() * 0.23);
		defDescColumn.setCellValueFactory(
				new Callback<CellDataFeatures<QiRepairResultDto, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<QiRepairResultDto, String> param) {
						return new ReadOnlyStringWrapper(param.getValue().getValue().getDefectDesc());
					}
				});
		
		TreeTableColumn<QiRepairResultDto, String> responsibleDeptColumn = new TreeTableColumn<QiRepairResultDto, String>("Resp Dept");
		responsibleDeptColumn.setPrefWidth(getScreenWidth() * 0.10);
		responsibleDeptColumn.setCellValueFactory(
				new Callback<CellDataFeatures<QiRepairResultDto, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<QiRepairResultDto, String> param) {
						return new ReadOnlyStringWrapper(param.getValue().getValue().getResponsibleDept());
					}
				});

		TreeTableColumn<QiRepairResultDto, String> responsibleL1Column = new TreeTableColumn<QiRepairResultDto, String>("Resp Level1");
		responsibleL1Column.setPrefWidth(getScreenWidth() * 0.07);
		responsibleL1Column.setCellValueFactory(
				new Callback<CellDataFeatures<QiRepairResultDto, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<QiRepairResultDto, String> param) {
						return new ReadOnlyStringWrapper(param.getValue().getValue().getResponsibleLevel1());
					}
				});

		TreeTableColumn<QiRepairResultDto, String> responsibleL2Column = new TreeTableColumn<QiRepairResultDto, String>("Resp Level2");
		responsibleL2Column.setPrefWidth(getScreenWidth() * 0.05);
		responsibleL2Column.setCellValueFactory(
				new Callback<CellDataFeatures<QiRepairResultDto, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<QiRepairResultDto, String> param) {
						return new ReadOnlyStringWrapper(param.getValue().getValue().getResponsibleLevel2());
					}
				});

		TreeTableColumn<QiRepairResultDto, String> responsibleL3Column = new TreeTableColumn<QiRepairResultDto, String>("Resp Level3");
		responsibleL3Column.setPrefWidth(getScreenWidth() * 0.05);
		responsibleL3Column.setCellValueFactory(
				new Callback<CellDataFeatures<QiRepairResultDto, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<QiRepairResultDto, String> param) {
						return new ReadOnlyStringWrapper(param.getValue().getValue().getResponsibleLevel3());
					}
				});

		TreeTableColumn<QiRepairResultDto, String> fixedColumn = new TreeTableColumn<QiRepairResultDto, String>("Fixed");
		fixedColumn.setPrefWidth(getScreenWidth() * 0.05);
		fixedColumn.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<QiRepairResultDto, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<QiRepairResultDto, String> param) {
						String fixedStatus = param.getValue().getValue().getCurrentDefectStatus() == DefectStatus.FIXED.getId() ? "Y" : "N";
						return new ReadOnlyStringWrapper(fixedStatus);
					}

				});

		TreeTableColumn<QiRepairResultDto, String> defCategoryColumn = new TreeTableColumn<QiRepairResultDto, String>("Defect Category");
		defCategoryColumn.setPrefWidth(getScreenWidth() * 0.10);
		defCategoryColumn.setCellValueFactory(
				new Callback<CellDataFeatures<QiRepairResultDto, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<QiRepairResultDto, String> param) {
						String defectCategory = param.getValue().getValue().getDefectCategoryName();
						return new ReadOnlyStringWrapper(defectCategory != null ? defectCategory.toUpperCase() : null);
					}
				});

		TreeTableColumn<QiRepairResultDto, String> entryDeptColumn = new TreeTableColumn<QiRepairResultDto, String>("Entry Dept");
		entryDeptColumn.setPrefWidth(getScreenWidth() * 0.04);
		entryDeptColumn.setCellValueFactory(
				new Callback<CellDataFeatures<QiRepairResultDto, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<QiRepairResultDto, String> param) {
						return new ReadOnlyStringWrapper(param.getValue().getValue().getEntryDept());
					}
				});

		TreeTableColumn<QiRepairResultDto, String> entryDeptNameColumn = new TreeTableColumn<QiRepairResultDto, String>("Dept Name");
		entryDeptNameColumn.setPrefWidth(getScreenWidth() * 0.08);
		entryDeptNameColumn.setCellValueFactory(
				new Callback<CellDataFeatures<QiRepairResultDto, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<QiRepairResultDto, String> param) {
						return new ReadOnlyStringWrapper(param.getValue().getValue().getDivisionName());
					}
				});

		TreeTableColumn<QiRepairResultDto, String> processPointIdColumn = new TreeTableColumn<QiRepairResultDto, String>("Process Point Id");
		processPointIdColumn.setPrefWidth(getScreenWidth() * 0.08);
		processPointIdColumn.setCellValueFactory(
				new Callback<CellDataFeatures<QiRepairResultDto, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<QiRepairResultDto, String> param) {
						return new ReadOnlyStringWrapper(getController().getProcessPointIdColumnData(param.getValue().getValue()));
					}
				});
		
		TreeTableColumn<QiRepairResultDto, String> processPointNameColumn = new TreeTableColumn<QiRepairResultDto, String>("Process Point Name");
		processPointNameColumn.setPrefWidth(getScreenWidth() * 0.08);
		processPointNameColumn.setCellValueFactory(
				new Callback<CellDataFeatures<QiRepairResultDto, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<QiRepairResultDto, String> param) {
						return new ReadOnlyStringWrapper(getController().getProcessPointName(param.getValue().getValue().getApplicationId()));
					}
				});
		
		TreeTableColumn<QiRepairResultDto, String> kickoutLocationColumn = new TreeTableColumn<QiRepairResultDto, String>("Kickout Location");
		kickoutLocationColumn.setPrefWidth(getScreenWidth() * 0.08);
		kickoutLocationColumn.setCellValueFactory(
				new Callback<CellDataFeatures<QiRepairResultDto, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<QiRepairResultDto, String> param) {
						boolean koOutstanding = param.getValue().getValue().getKickoutStatus() == KickoutStatus.OUTSTANDING.getId();
						return koOutstanding ? new ReadOnlyStringWrapper(param.getValue().getValue().getKickoutProcessPointName()) : new ReadOnlyStringWrapper("");
					}
				});
		
		TreeTableColumn<QiRepairResultDto, Date> repairDateColumn = new TreeTableColumn<QiRepairResultDto, Date>("Repair Date");
		repairDateColumn.setPrefWidth(getScreenWidth() * 0.11);
		repairDateColumn
		.setCellValueFactory(new Callback<CellDataFeatures<QiRepairResultDto, Date>, ObservableValue<Date>>() {
			@Override
			public ObservableValue<Date> call(CellDataFeatures<QiRepairResultDto, Date> param) {
				// Repair time stamp will be shown for completely fixed  defect only
				return new ReadOnlyObjectWrapper<Date>(param.getValue().getValue().getRepairTimestamp());
			}
		});

		TreeTableColumn<QiRepairResultDto, String> repairRelatedColumn = new TreeTableColumn<QiRepairResultDto, String>("Repair Related");
		repairRelatedColumn.setPrefWidth(getScreenWidth() * 0.06);
		repairRelatedColumn.setCellValueFactory(
				new Callback<TreeTableColumn.CellDataFeatures<QiRepairResultDto, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<QiRepairResultDto, String> param) {
						String fixedStatus = param.getValue().getValue().getIsRepairRelated() == 0 ? "N" : "Y";
						return new ReadOnlyStringWrapper(fixedStatus);
					}

				});

		TreeTableColumn<QiRepairResultDto, String> imageNameColumn = new TreeTableColumn<QiRepairResultDto, String>("Image Name");
		imageNameColumn.setPrefWidth(getScreenWidth() * 0.10);
		imageNameColumn.setCellValueFactory(
				new Callback<CellDataFeatures<QiRepairResultDto, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<QiRepairResultDto, String> param) {
						return new ReadOnlyStringWrapper(param.getValue().getValue().getImageName());
					}
				});
		addCellFactoryForImage(imageNameColumn);

		TreeTableColumn<QiRepairResultDto, Date> entryTimestampColumn = new TreeTableColumn<QiRepairResultDto, Date>("Entry Timestamp");
		entryTimestampColumn.setPrefWidth(getScreenWidth() * 0.13);
		entryTimestampColumn
		.setCellValueFactory(new Callback<CellDataFeatures<QiRepairResultDto, Date>, ObservableValue<Date>>() {
			@Override
			public ObservableValue<Date> call(CellDataFeatures<QiRepairResultDto, Date> param) {
				return new ReadOnlyObjectWrapper<Date>(param.getValue().getValue().getActualTimestamp());
			}
		});
		
				
		TreeTableColumn<QiRepairResultDto, TextFlow> imageVideoColumn = new TreeTableColumn<QiRepairResultDto, TextFlow>("Image/Video");
		imageVideoColumn.setPrefWidth(getScreenWidth() * 0.15);
		imageVideoColumn.setCellValueFactory(
			new Callback<CellDataFeatures<QiRepairResultDto, TextFlow>, ObservableValue<TextFlow>>() {
				@Override
				public ObservableValue<TextFlow> call(CellDataFeatures<QiRepairResultDto, TextFlow> param) {
					return new ReadOnlyObjectWrapper<TextFlow>(buildTextFlow(param.getValue().getValue()));
				}
		});
		
		

		TreeTableColumn<QiRepairResultDto, String> createUserColumn = new TreeTableColumn<QiRepairResultDto, String>("Create User");
		createUserColumn.setPrefWidth(getScreenWidth() * 0.10);
		createUserColumn
		.setCellValueFactory(new Callback<CellDataFeatures<QiRepairResultDto, String>, ObservableValue<String>>() {
			@Override	
		    public ObservableValue<String> call(CellDataFeatures<QiRepairResultDto, String> param) {
				return new ReadOnlyStringWrapper(param.getValue().getValue().getCreateUser());

		}
		});

		TreeTableColumn<QiRepairResultDto, String> upateUserColumn = new TreeTableColumn<QiRepairResultDto, String>("Update User");
		upateUserColumn.setPrefWidth(getScreenWidth() * 0.10);
		upateUserColumn
		.setCellValueFactory(new Callback<CellDataFeatures<QiRepairResultDto, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<QiRepairResultDto, String> param) {
				return new ReadOnlyStringWrapper(param.getValue().getValue().getUpdateUser());

		}
		});
		
		treeTablePane.getColumns().setAll(defDescColumn, responsibleDeptColumn, responsibleL1Column,responsibleL2Column,responsibleL3Column,fixedColumn, defCategoryColumn, entryDeptColumn,entryDeptNameColumn,
				processPointIdColumn, processPointNameColumn, kickoutLocationColumn, repairDateColumn, repairRelatedColumn, imageNameColumn, entryTimestampColumn, imageVideoColumn, createUserColumn, upateUserColumn);
		treeTablePane.setShowRoot(false);
		addRowFactoryForScrap();

		//Check if able to apply multi-select repairs
		if(getController().getMultiSelectReapirsConfiguration() && getController().getMultiSelectRepairsFieldsPreCheck().compareTo("") != 0) {
			treeTablePane.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			getController().getMultiSelectRepairsPreCheckMethods();
			addMultiSelectControlKeyEvent(); //CTRL key PRESS and RELEASE event
		}
		return treeTablePane;
	}
	
	private TextFlow buildTextFlow(QiRepairResultDto dto) {
		TextFlow tf = new TextFlow();
		if(dto.getRepairId() == 0) {
			List<QiDefectResultImage> images = dto.getDefectResultImages();
			for(QiDefectResultImage image : images) {
				tf.getChildren().add(createHyperlink(image.getId().getImageUrl()));
			}
			tf.setPrefHeight((1 + images.size() / 5) * 25);	
		} else {
			List<QiRepairResultImage> images = dto.getRepairResultImages();
			for(QiRepairResultImage image : images) {
				tf.getChildren().add(createHyperlink(image.getId().getImageUrl()));
			}
			tf.setPrefHeight((1 + images.size() / 5) * 25);	
		}
		return tf;
	}

	private Hyperlink createHyperlink(String imageUrl) {
		Hyperlink hyperlink = new Hyperlink(getShortImageName(imageUrl));
		hyperlink.setTooltip(new Tooltip(getPathAndImageName(imageUrl)));
		hyperlink.setFont(Font.font("Arial", 14));
		hyperlink.setTextFill(Color.INDIGO);
		hyperlink.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				MediaUtils.showMedia(imageUrl);
			}
		});
		return hyperlink;
	}
	
	private String getShortImageName(String imageUrl) {
		int index = imageUrl.lastIndexOf("/");
		if(index > 0 && index < imageUrl.length() - 1) {
			String filename = imageUrl.substring(index + 1, imageUrl.length());
			return filename.length() > 6 ? filename.substring(filename.length() - 6) : filename;
		} else {
			return imageUrl;
		}
	}
	
	private String getPathAndImageName(String imageUrl) {
		int index = imageUrl.lastIndexOf("/");
		if(index > 0 && index < imageUrl.length() - 1) {
			int index2 = imageUrl.lastIndexOf("/", index - 1);
			return index2 > 0 ? imageUrl.substring(index2 + 1, imageUrl.length()) : imageUrl.substring(index + 1, imageUrl.length());
		} else {
			return imageUrl;
		}
	}
	

	
	/**
	 * Setting hyper link on image name column
	 * 
	 * @param imageNameColumn
	 */
	private void addCellFactoryForImage(TreeTableColumn<QiRepairResultDto, String> imageNameColumn) {

		imageNameColumn.setCellFactory(
				new Callback<TreeTableColumn<QiRepairResultDto, String>, TreeTableCell<QiRepairResultDto, String>>() {
					@Override
					public TreeTableCell<QiRepairResultDto, String> call(TreeTableColumn<QiRepairResultDto, String> param) {

						TreeTableCell<QiRepairResultDto, String> cell = new TreeTableCell<QiRepairResultDto, String>() {
							@Override
							protected void updateItem(String item, boolean empty) {
								super.updateItem(item, empty);
								hyperlink = new Hyperlink(item);
								hyperlink.setFont(Font.font("Arial", 14));
								hyperlink.setTextFill(Color.INDIGO);
								setGraphic(hyperlink);
								addHyperLinkAction(this.getTreeTableRow().getItem());
							}
						};
						return cell;
					}
				});
	}
	
	/**
	 * Row factory which will disable particular row if defect is scraped
	 */
	private void addRowFactoryForScrap() {

		treeTablePane.setRowFactory(new Callback<TreeTableView<QiRepairResultDto>, TreeTableRow<QiRepairResultDto>>() {
			@Override
			public TreeTableRow<QiRepairResultDto> call(TreeTableView<QiRepairResultDto> param) {
				final TreeTableRow<QiRepairResultDto> row = new TreeTableRow<QiRepairResultDto>() {
					@Override
					protected void updateItem(QiRepairResultDto item, boolean empty) {
						super.updateItem(item, empty);
						if (item != null && !empty && !isSelected()) {
							changeCellDataFont(item, this);
						}
					}
				};
				
				row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<Event>() {
					@Override
					public void handle(Event event) {
						MouseEvent  mouseEvent = (MouseEvent) event;

						final int index = row.getIndex();
						if (index >= 0 && treeTablePane.getSelectionModel().isSelected(index)) {
							if(mouseEvent.isSecondaryButtonDown()==false) {
								treeTablePane.getSelectionModel().clearSelection(index);
							}

							getController().disableAllButtons();
							if (row.getItem() != null && !row.isEmpty()) {
								changeCellDataFont(row.getItem(), row);
							}
							event.consume();
						} else {
							row.setStyle("-fx-text-background-color: #4B0082; -fx-font-weight: bold; -fx-font-size: 15px;");
						}
					}
				});
				return row;
			}
		});
	}
	
	/**
	 * This method will be used to change row data font based on condition.
	 * 
	 * @param item
	 */
	protected void changeCellDataFont(QiRepairResultDto item, TreeTableRow<QiRepairResultDto> row ) {
		
		if(item.getInspectionPartName().contains(AbstractRepairEntryController.NO_PROBLEM_FOUND) || item.getDefectTypeName().contains(AbstractRepairEntryController.NO_PROBLEM_FOUND)) {
			MenuItem menuItem = new MenuItem("Add Repair Method for No Problem Found");
			ContextMenu contextMenu = new ContextMenu(menuItem);

			menuItem.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					getController().setNoProblemFound(true);
					getController().addRepairMethods();
				}
			});
			row.setContextMenu(contextMenu);
		} else {
			row.setContextMenu(null);
		}

		if (item.getCurrentDefectStatus() == DefectStatus.NON_REPAIRABLE.getId()) {
			row.setStyle("-fx-text-background-color: black;");
			//Enabled Scrapped defect row ONLY when the property "Assign Actual Problem After Scrap" is "Yes"
			if(getController().getAssignActualProblemAfterScrapConfiguration())
				row.setDisable(false);
			else row.setDisable(true);
			
			row.setOpacity(.5);
		} else if (item.getCurrentDefectStatus() == DefectStatus.FIXED.getId()) {
			row.setStyle("-fx-text-background-color: black;");
			row.setDisable(false);
			row.setOpacity(1);
		} else {
			row.setStyle("-fx-text-background-color: red;");
			row.setDisable(false);
			row.setOpacity(1);
		}
	}

	/**
	 * Adding action listener for image hyper link
	 * 
	 * @param qiRepairResultDto
	 */
	protected void addHyperLinkAction(final QiRepairResultDto qiRepairResultDto) {

		hyperlink.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				if(qiRepairResultDto.getImageData() == null || qiRepairResultDto.getImageData().length <= 0) {
					//Get image data for bulk processing
					QiImage image = getController().getImageByImagename(qiRepairResultDto.getImageName());
					if(image != null) 
						qiRepairResultDto.setImageData(image.getImageData());
				}
				
				if (qiRepairResultDto.getImageData() != null && qiRepairResultDto.getImageData().length > 0) {
					DialogPane dialogPane = new DialogPane();
					dialogPane.setPrefSize(getScreenHeight() * 0.70, getScreenHeight() * 0.70);
					dialogPane.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
					ImageView defectImageView = new ImageView();
					defectImageView.setFitHeight(getScreenHeight() * 0.70);
					defectImageView.setFitWidth(getScreenHeight() * 0.70);
					defectImageView.setImage(new Image(new ByteArrayInputStream(qiRepairResultDto.getImageData())));
					dialogPane.getChildren().add(defectImageView);
					double resizePercent = ((getScreenHeight()*0.70)-500)/5;
					int xPoint = (int) (qiRepairResultDto.getPointX()+((qiRepairResultDto.getPointX()*resizePercent)/100));
					int yPoint = (int) (qiRepairResultDto.getPointY()+((qiRepairResultDto.getPointY()*resizePercent)/100));

					String colorCode = getModel().findColorCodeByWriteupDeptAndProcessPointId(qiRepairResultDto.getWriteUpDept());

					dialogPane = (DialogPane) DefectImageUtil.drawSymbol(xPoint, yPoint,
							DefectStatus.getType(qiRepairResultDto.getCurrentDefectStatus()).getName(), colorCode,
							qiRepairResultDto.getDefectDesc(), dialogPane);

					FxDialog fxDialog = new FxDialog("Image Viewer", ClientMainFx.getInstance().getStage(getController().getApplicationId()), dialogPane);
					fxDialog.show();
				} else {
					MessageDialog.showInfo(ClientMainFx.getInstance().getStage(getController().getApplicationId()), "No Image Data Present.");
				}
			}
		});
	}
	
	/**
	 * Add CTRL key PRESS event and RELEASE event
	 */
	private void addMultiSelectControlKeyEvent() {	
		treeTablePane.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if( treeTablePane.getSelectionModel().getSelectedItem() != null) {
					final int selectedIndex = treeTablePane.getSelectionModel().getSelectedIndex();					
					QiRepairResultDto selectedItem = treeTablePane.getSelectionModel().getSelectedItem().getValue();				
					if(selectedIndex >= 0 && (event.isControlDown() || event.isShiftDown())) {	
						List<Field> preCheckFields = getController().getPreCheckFields();
						if((preCheckFields == null || preCheckFields.size() == 0) && getController().getInvalidColumnNames().compareTo("") != 0) {
							EventBusUtil.publish(new StatusMessageEvent("Invalid Column name :" + getController().getInvalidColumnNames() + ".", 
									StatusMessageEventType.ERROR));	
							Logger.getLogger().warn("Invalid Column name :" + getController().getInvalidColumnNames() + ".");
						}
						boolean isChildDefect = selectedItem.getRepairId() != 0 ? true : false;
						boolean isNotFixed = selectedItem.getCurrentDefectStatus() == DefectStatus.NOT_FIXED.getId() ? true : false;
						treeTablePane.setRowFactory(new Callback<TreeTableView<QiRepairResultDto>, TreeTableRow<QiRepairResultDto>>() {
							@Override
							public TreeTableRow<QiRepairResultDto> call(TreeTableView<QiRepairResultDto> param) {
								final TreeTableRow<QiRepairResultDto> row = new TreeTableRow<QiRepairResultDto>() {
									@Override
									protected void updateItem(QiRepairResultDto item, boolean empty) {
										super.updateItem(item, empty);
										boolean enableRow = false;
		
										if(preCheckFields != null && preCheckFields.size() > 0){
											if(isNotFixed) {
												if (item != null && !empty && !isSelected() && this.getIndex() != selectedIndex 
														&& item.getCurrentDefectStatus() == DefectStatus.NOT_FIXED.getId() 
														//isChildDefect = (repair_id != 0) = true => actual problem
														//isChildDefect = (repair_id != 0) = false => main defect
														&& (isChildDefect == (item.getRepairId() != 0))) {
													enableRow = isEnableMultiSelectReapirs(item, selectedItem, preCheckFields);
												}
											}
										}
										if(this.getIndex() == selectedIndex && item.getCurrentDefectStatus() == DefectStatus.NOT_FIXED.getId()) {
											enableRow = true;
										}
										this.setDisable(!enableRow);
										if(!enableRow) {
											this.setStyle("-fx-text-background-color: grey;");
										}else {
											this.setStyle("-fx-text-background-color: red;");
										}
									}
								};
								//allow shift-key range-select only for main defects
								if(event.isShiftDown())  {
									row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
										@Override
										public void handle(MouseEvent event) {
											if (event.isPrimaryButtonDown() && event.isShiftDown()) {
												final int index = row.getIndex();
												if (index >= 0) {
													treeTablePane.getSelectionModel().select(index);
													event.consume();
												} 												
											}
										}
									});
								}
							return row;
							}
						});
					}
				}
			}				
		});		
		
		treeTablePane.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override 
			public void handle(KeyEvent event) {
				treeTablePane.setRowFactory(new Callback<TreeTableView<QiRepairResultDto>, TreeTableRow<QiRepairResultDto>>() {
					@Override
					public TreeTableRow<QiRepairResultDto> call(TreeTableView<QiRepairResultDto> param) {
						final TreeTableRow<QiRepairResultDto> row = new TreeTableRow<QiRepairResultDto>() {
							@Override
							protected void updateItem(QiRepairResultDto item, boolean empty) {
								super.updateItem(item, empty);
								if (item != null && !empty && !isSelected()) {
									changeCellDataFont(item, this);
								}
							}
						};
						//Mouse press event after release CTRL key
						row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
							@Override
							public void handle(MouseEvent event) {
								if (event.isPrimaryButtonDown()) {
									final int index = row.getIndex();
									ObservableList<TreeItem<QiRepairResultDto>> selectedItems = FXCollections.observableArrayList();
									selectedItems.addAll(treeTablePane.getSelectionModel().getSelectedItems());
									selectedItems.removeIf(item -> item == null); //Remove null items		
									if(selectedItems != null && selectedItems.size() > 1) {
										treeTablePane.getSelectionModel().clearAndSelect(index);
										row.setStyle("-fx-text-background-color: #4B0082; -fx-font-weight: bold; -fx-font-size: 15px;");
									}else {
										if (index >= 0 && treeTablePane.getSelectionModel().isSelected(index)) {
											treeTablePane.getSelectionModel().clearSelection(index);
											getController().disableAllButtons();
											if (row.getItem() != null && !row.isEmpty()) {
												changeCellDataFont(row.getItem(), row);
											}
											event.consume();
										} else {
											row.setStyle("-fx-text-background-color: #4B0082; -fx-font-weight: bold; -fx-font-size: 15px;");
										}
									}
								}
							}
						});
					return row;
					}
				});
			}
		});
	}
	
	/**
	 * This method is to check if all configured field values of the current row is same as the select row
	 * @param checkItem
	 * @param selectedItem
	 * @param preCheckFields
	 * @return
	 */
	private boolean isEnableMultiSelectReapirs(QiRepairResultDto checkItem, QiRepairResultDto selectedItem, List<Field> preCheckFields) {
		boolean isEnable = true;
		for(Field field : preCheckFields) {
			try {
				field.setAccessible(true);
				String valueOfCurrent = field.get(checkItem).toString();
				String valueOfLast = field.get(selectedItem).toString();
				if(valueOfCurrent.compareTo(valueOfLast) != 0 ) {
					return false;
				}
			}catch(Exception e) {
				EventBusUtil.publish(new StatusMessageEvent(" Field name :" + field.getName() + " cannot find in QiRepairResultDto.java class. ",
						StatusMessageEventType.ERROR));
				Logger.getLogger().warn(" Field name :" + field.getName() + " cannot find in QiRepairResultDto.java class. ");
				return false;
			}
		}
		return isEnable;		
	}

	protected GridPane getExistingProductAssignment() {
		GridPane existingProductAssignmentGrid = createGrid();

		LoggedLabel productIsParkedLabel = new LoggedLabel("parkedLabel","[------------------------------------------------------------------------------------------"
				+ "- Product is currently parked at ----------------------------------------------------------------------------------]");

		existingProductAssignmentGrid.add(productIsParkedLabel, 0, 0 , 4, 1);

		plantLabel = new LoggedLabel("plantLabel");
		parkingRepairAreaLabel = new LoggedLabel("parkingRepairAreaLabel");
		rowLabel = new LoggedLabel("rowLabel");
		spaceLabel = new LoggedLabel("spaceLabel");

		plantLabel.setPadding(new Insets(0, 0, 0, 30));
		if(getScreenWidth()< 1024){
			productIsParkedLabel.setPadding(new Insets(0, 0, 0, 50));
			plantLabel.setPadding(new Insets(0, 0, 0, 70));
		}

		existingProductAssignmentGrid.setPadding(new Insets(0));

		existingProductAssignmentGrid.add(plantLabel, 0, 1);
		existingProductAssignmentGrid.add(parkingRepairAreaLabel, 1, 1);
		existingProductAssignmentGrid.add(rowLabel, 2, 1);
		existingProductAssignmentGrid.add(spaceLabel, 3, 1);

		existingProductAssignmentGrid.getColumnConstraints().addAll(createColumnConstraints(HPos.LEFT, 250), createColumnConstraints(HPos.LEFT, 250), createColumnConstraints(HPos.LEFT, 250), createColumnConstraints(HPos.LEFT, 250));
		existingProductAssignmentGrid.setStyle(String.format("-fx-font-size: %dpx;", (int)(0.01 * getScreenWidth())));;
		return existingProductAssignmentGrid;
	}

	/** This method is used to return gridPane template
	 * 
	 * @return
	 */
	private GridPane createGrid() {
		GridPane gridpane = new GridPane();
		gridpane.setHgap(10);
		gridpane.setVgap(5);
		gridpane.setPadding(new Insets( 0, 10, 10, 0));
		gridpane.setAlignment(Pos.CENTER);
		return gridpane;
	}

	/**
	 * This method is used to create ColumnConstraints
	 * 
	 * @param hpos
	 * @param width
	 * @return columnConstraint
	 */
	protected ColumnConstraints createColumnConstraints(HPos hpos, double width) {
		ColumnConstraints column = new ColumnConstraints();
		column.setHalignment(hpos);
		column.setPrefWidth(width);
		return column;
	}

	public double getScreenWidth() {
		return Screen.getPrimary().getVisualBounds().getWidth();
	}

	public double getScreenHeight() {
		return Screen.getPrimary().getVisualBounds().getHeight();
	}

	protected LoggedButton createButton(String text) {

		LoggedButton button = createBtn(text, getController());
		button.setDisable(true);
		button.setPrefSize(BUTTON_PREF_WIDTH, BUTTON_PREF_HEIGHT);
		button.setWrapText(true);
		button.setTextAlignment(TextAlignment.CENTER);
		button.setTooltip(new Tooltip(text));
		return button;
	}

	/**
	 * This method will be used to create repair options panel.
	 * 
	 * @return
	 */
	protected Node createRepairOptionsPanel() {

		VBox mainPanel = new VBox();
		mainPanel.setPrefHeight(getScreenHeight() * 0.28);
		mainPanel.setPadding(new Insets(10));

		// Adding button panel to main panel
		initializeAllButtons();
		repairOptionsPane = new TitledPane("Repair Options", getButtonPanel());

		mainPanel.getChildren().addAll(repairOptionsPane);
		mainPanel.setAlignment(Pos.CENTER);
		return mainPanel;
	}

	protected Node getButtonPanel() {

		HBox firstHorizontalButtonContainer = new HBox();
		firstHorizontalButtonContainer.setSpacing(25);
		firstHorizontalButtonContainer.setAlignment(Pos.CENTER);
		firstHorizontalButtonContainer.getChildren().addAll(defectActualProblemButton, addRepairMethodButton,
				deleteActualProblemButton, scrapButton);
		QiStationConfiguration qiStationConfiguration =getModel().getStationConfigForRepairArea(ClientMainFx.getInstance().getApplicationContext().getProcessPointId(),
				QiEntryStationConfigurationSettings.UPDATE_REPAIR_AREA.getSettingsName()); 

		if (qiStationConfiguration != null) {
			if (StringUtils.trimToEmpty(qiStationConfiguration.getPropertyValue()).equalsIgnoreCase(QiConstant.YES)) {
				firstHorizontalButtonContainer.getChildren().add(updateRepairAreaButton);
			}}
		HBox secondHorizontalButtonContainer = new HBox();
		secondHorizontalButtonContainer.setSpacing(25);
		secondHorizontalButtonContainer.setAlignment(Pos.CENTER);
		secondHorizontalButtonContainer.getChildren().addAll(assignActualProblemButton, noProblemFoundButton,
				setActualProblemToNotFixedButton, addNewDefectButton, uploadImageButton);

		VBox buttonContainer = new VBox();
		buttonContainer.setSpacing(5);
		buttonContainer.setAlignment(Pos.CENTER);
		buttonContainer.getChildren().addAll(firstHorizontalButtonContainer, secondHorizontalButtonContainer);

		return buttonContainer;
	}

	/**
	 * This method will be used to initialize all the buttons in disabled state.
	 * 
	 */
	protected void initializeAllButtons() {

		defectActualProblemButton = createButton(DEFECT_ACTUAL_PROBLEM);
		addRepairMethodButton = createButton(ADD_REPAIR_METHOD_FOR_ACTUAL_PROBLEM);
		scrapButton = createButton(SCRAP);

		assignActualProblemButton = createButton(ASSIGN_ACTUAL_PROBLEM);
		noProblemFoundButton = createButton(NO_PROBLEM_FOUND);
		setActualProblemToNotFixedButton = createButton(SET_ACTUAL_PROBLEM_NOT_FIXED);

		deleteActualProblemButton = createButton(DELETE_ACTUAL_PROBLEM);
		addNewDefectButton = createButton(ADD_NEW_DEFECTS);
		addNewDefectButton.setDisable(false);
		updateRepairAreaButton =  createButton(UPDATE_REPAIR_AREA);
		uploadImageButton =  createButton(QiConstant.UPLOAD_IMAGE_VIDEO);
	}

	/**
	 * @return the defectActualProblemBtn
	 */
	public LoggedButton getDefectActualProblemBtn() {
		return defectActualProblemButton;
	}

	/**
	 * @return the addRepairMethodBtn
	 */
	public LoggedButton getAddRepairMethodBtn() {
		return addRepairMethodButton;
	}

	/**
	 * @return the scrapBtn
	 */
	public LoggedButton getScrapBtn() {
		return scrapButton;
	}

	/**
	 * @return the assignActualProblemBtn
	 */
	public LoggedButton getAssignActualProblemBtn() {
		return assignActualProblemButton;
	}

	/**
	 * @return the noProblemFoundBtn
	 */
	public LoggedButton getNoProblemFoundBtn() {
		return noProblemFoundButton;
	}

	/**
	 * @return the setActualProblemToNotFixedBtn
	 */
	public LoggedButton getSetActualProblemToNotFixedBtn() {
		return setActualProblemToNotFixedButton;
	}

	/**
	 * @return the deleteActualProblemBtn
	 */
	public LoggedButton getDeleteActualProblemBtn() {
		return deleteActualProblemButton;
	}

	/**
	 * @return the treeTablePane
	 */
	public TreeTableView<QiRepairResultDto> getTreeTablePane() {
		return treeTablePane;
	}

	public LoggedButton getAddNewDefectBtn() {
		return addNewDefectButton;
	}

	public TitledPane getRepairOptionsPane() {
		return repairOptionsPane;
	}

	public boolean isProductScraped() {
		return isProductScraped;
	}

	public void setProductScraped(boolean isProductScraped) {
		this.isProductScraped = isProductScraped;
	}

	public LoggedButton getUpdateRepairAreaButton() {
		return updateRepairAreaButton;
	}
	
	public LoggedButton getUploadImageButton() {
		return uploadImageButton;
	}

	public LoggedLabel getPlantLabel() {
		return plantLabel;
	}

	public LoggedLabel getParkingRepairAreaLabel() {
		return parkingRepairAreaLabel;
	}

	public LoggedLabel getRowLabel() {
		return rowLabel;
	}

	public LoggedLabel getSpaceLabel() {
		return spaceLabel;
	}

	public void onTabSelected() {
		Logger.getLogger().check("Repair Entry panel Selected");
	}

	public static List<QiRepairResultDto> getParentCachedDefectList() {
		return parentCachedDefectList;
	}

	public static void setParentCachedDefectList(List<QiRepairResultDto> parentCachedDefectList) {
		AbstractRepairEntryView.parentCachedDefectList = parentCachedDefectList;
	}

}

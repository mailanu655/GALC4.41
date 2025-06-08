package com.honda.galc.client.product.pane;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.honda.galc.util.ProductSpecUtil;
import org.apache.commons.lang.StringUtils;
import org.tbee.javafx.scene.layout.MigPane;
import com.honda.galc.data.ProductType;
import com.honda.galc.client.product.ProductClientConfig;
import com.honda.galc.client.product.PropertyDef;
import com.honda.galc.client.product.action.ProductActionId;
import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTableCell;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.utils.StyleUtil;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.BeanUtils;
import com.sun.glass.ui.Screen;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.util.Callback;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ProductInfoPanel</code> is ... .
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
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
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
public class ProductInfoPane extends MigPane {
	private static final String FIRST_LEFT_ELEMENT_CONSTRAINTS = "[140,right][grow,fill]";
	private static final String ELEMENT_CONSTRAINTS = "[right][grow,fill]";

	private Map<PropertyDef, TextField> infoTextFields;

	private boolean showSequenceNumber;   //Property that is checked whether to show # or not
	private boolean showKdLotNumberForFrame;
	private boolean showLargeProductIdField;
	private Label sequenceLabel;

	private ProductController productController;
	
	private MigPane infoPane;
	private Pane firstPane;
	private MigPane largeIdPane;

	private List<Region> controls = new ArrayList<Region>();

	public ProductInfoPane(ProductController productController) {
		super("insets 5", "[grow,fill]");
		this.productController = productController;
		this.infoTextFields = new LinkedHashMap<PropertyDef, TextField>();
		this.showSequenceNumber = getProductController().getModel().getProperty().isShowProductSequence();
		this.showKdLotNumberForFrame = getProductController().getModel().getProperty().isShowKdLotNumberForFrame();
		this.showLargeProductIdField = getProductController().getModel().getProperty().isShowLargeProductIdField();
		initView(productController.getProductTypeData());
	}


	protected ProductController getProductController() {
		return productController;
	}


	protected void setProductController(ProductController productController) {
		this.productController = productController;
	}
	
	protected void initView(ProductTypeData productTypeData) {
		if (this.showLargeProductIdField) {
			this.add(this.createInfoPane(productTypeData));
		} else { 
			add(getFirstRowPanel(productTypeData), "wrap");
			add(getSecondRowPanel(productTypeData));
		}
		// set Etched border
		setStyle("-fx-border-color: white, grey; -fx-border-width: 2, 1; -fx-border-insets: 0, 0 1 1 0");
	}
	
	//This adds the skip button 
	public void setProductButtons(ProductActionId[] actionIds) {
		setProductButtons(actionIds, true);
	}
	public void setProductButtons(ProductActionId[] actionIds, final boolean canTakeFocus) {
		removeRegions();
		for(ProductActionId actionId : actionIds) {
			if(actionId != null) {
				final Button button;
				button = UiFactory.createButton(actionId.getActionName(),
						isDefaultButtonStyleAction(actionId)
							? StyleUtil.getDefaultBtnStyle(15, "10 10 10 10")
							: StyleUtil.getBtnStyle(15, "10 10 10 10"),
						true);
				button.setOnAction(actionId.createProductAction(getProductController()));
				if (actionId.equals(ProductActionId.DIRECTPASS)) { 
					Platform.runLater( new Runnable() {
						@Override
						public void run() {
							button.defaultButtonProperty().bind(button.focusedProperty());//Switch from SPACE key to ENTER key binding to Direct Pass button
							if (canTakeFocus)
								button.requestFocus();//Default focus to Direct Pass button in NAQ
						}
					});
				}
				controls.add(button);
				firstPane.getChildren().add(button);
			}
		}
		if (showSequenceNumber && !this.showLargeProductIdField) {   						 //checks whether property is turned on.
			HBox hbox = new HBox();
			Label label = new Label("Sequence #");  		 //Creates top label					
			double fontSize = Math.ceil(Screen.getMainScreen().getWidth()*0.01);					
			label.setStyle("-fx-font-size: " + fontSize + "pt; -fx-font-weight: bold;");  
			hbox.getChildren().add(label); 					 //Adds label to Hbox to format 
			HBox.setHgrow(label, Priority.ALWAYS);
			hbox.setAlignment(Pos.CENTER);
			controls.add(hbox);
			firstPane.getChildren().add(hbox);
		}
	}

	private boolean isDefaultButtonStyleAction(ProductActionId actionId) {
		return actionId.equals(ProductActionId.DIRECTPASS)||
			actionId.equals(ProductActionId.KEYBOARD)||
			actionId.equals(ProductActionId.PRODUCT_CHECK_DONE)||
			actionId.equals(ProductActionId.SUBMIT)||
			actionId.equals(ProductActionId.SEND_FINAL)||
			actionId.equals(ProductActionId.CANCEL)||
			actionId.equals(ProductActionId.UPDATE_REPAIR_AREA)||
		    actionId.equals(ProductActionId.CANCEL_DIRECT_PASS) ||
		    actionId.equals(ProductActionId.CANCEL_DONE);
	}
	
	private void removeRegions() {
		for(Region control : controls) {
			firstPane.getChildren().remove(control);
		}
		controls.clear();
	}

	protected void mapActions() {

	}
	
	protected MigPane createInfoPane(ProductTypeData productTypeData) {
		this.infoPane = new MigPane("Insets 0");
		infoPane.add(getLargeProductIdPanel(productTypeData),"spanx,grow,push,al right");
		return this.infoPane;
	}
	
	protected MigPane getLargeProductIdPanel(ProductTypeData productTypeData) {
		if (this.largeIdPane == null) {
			this.largeIdPane = new MigPane("fill");
			this.largeIdPane.add(getFirstRowPanel(productTypeData), "width 525:n:725, grow, wrap, al right");
			this.largeIdPane.add(getSecondRowPanel(productTypeData), "width 525:n:725, al right");
		}
		return this.largeIdPane;
	}

	// === factory === //
	protected Pane getFirstRowPanel(ProductTypeData productTypeData) {
		if (this.firstPane == null) {
			if (this.showLargeProductIdField) {
				this.firstPane = createRowPanel(ProductClientConfig.getProductInfoFirstRow(productTypeData), "", showSequenceNumber ? "[][fill][grow,fill]" : "push [170,fill][170,fill]", UiFactory.getInfoLarge());
			} else {
				this.firstPane = createRowPanel(ProductClientConfig.getProductInfoFirstRow(productTypeData), "", "10[fill][150,fill]", UiFactory.getInfo());
			}
			for(Region control : controls)
				firstPane.getChildren().add(control);
		}
		return this.firstPane;
	}

	protected Pane getSecondRowPanel(ProductTypeData productTypeData) {
		Pane secondPane = new Pane();
		if (this.showLargeProductIdField) {
			secondPane = createRowPanel(ProductClientConfig.getProductInfoSecondRow(productTypeData, showKdLotNumberForFrame), "", showKdLotNumberForFrame ?  "[][grow,fill][][grow,fill]": "push [][fill]", UiFactory.getInfoSmall());
		} else {
			secondPane = createRowPanel(ProductClientConfig.getProductInfoSecondRow(productTypeData, showKdLotNumberForFrame), "", showSequenceNumber ? "10[fill][fill]" : "", UiFactory.getInfoSmall());
		}
		if (showSequenceNumber) {
			HBox hbox = new HBox();
			if (this.showLargeProductIdField) {   						 //checks whether property is turned on.
				Label label = new Label("SEQ: ");  		 //Creates top label					
				double fontSize = Math.ceil(Screen.getMainScreen().getWidth()*0.01);					
				label.setStyle("-fx-font-size: " + fontSize + "pt; -fx-font-weight: bold;");  
				hbox.getChildren().add(label); 					 //Adds label to Hbox to format 
				HBox.setHgrow(label, Priority.ALWAYS);
				hbox.setAlignment(Pos.CENTER);
			}
			Button button = UiFactory.createButton("", StyleUtil.getBtnStyle(15, "10 10 10 10"), true);
			button.setVisible(false);
			this.firstPane.getChildren().add(button);
			hbox.getChildren().add(this.getSequenceLabel());
			HBox.setHgrow(this.getSequenceLabel(), Priority.ALWAYS);
			hbox.setAlignment(Pos.CENTER_RIGHT);
			if (this.showLargeProductIdField)
				this.firstPane.getChildren().add(hbox);
			else 
				secondPane.getChildren().add(hbox);
		}
		return secondPane;
	}

	@SuppressWarnings("unused")
	protected Pane createRowPanel(List<PropertyDef> list, String preConstraint, String postConstraint, UiFactory uiFactory) {
		
		StringBuffer sb = new StringBuffer();
		if (!this.showLargeProductIdField){
			for (PropertyDef def : list) {
				if (sb.length() > 0) {
					sb.append(10);
					sb.append(ELEMENT_CONSTRAINTS);
				}  else {
					sb.append(FIRST_LEFT_ELEMENT_CONSTRAINTS);
				}
			}
		}

		StringBuilder constraints = new StringBuilder();
		if (preConstraint != null) {
			constraints.append(preConstraint);
		}
		constraints.append(sb.toString());
		if (postConstraint != null) {
			constraints.append(postConstraint);
		}

		Pane pane = new MigPane("insets 0", constraints.toString());
		//Hey these are the textfields creation point MCM
		for (PropertyDef def : list) {
			TextField field = UiFactory.createTextField("propertyDef-" + Integer.toString(list.indexOf(def)), UiFactory.getInputFont(), TextFieldState.READ_ONLY, def.getHeader());
			getInfoTextFields().put(def, field);
			if (this.showLargeProductIdField) {
				field.setPadding(new Insets(0,0,0,0));
				if (def.getPropertyPath().equals("product.productId")){
					LoggedLabel label = UiFactory.createLabel(def.getHeader(), def.getHeader(), UiFactory.getLabelFont());
					this.infoPane.add(label,"width 50:n:n");
					this.infoPane.add(field,"width 850:n:950");
				} else if (def.getPropertyPath().equals("product.engineSerialNo")) {
					continue;
				}else {
					LoggedLabel label = UiFactory.createLabel(def.getHeader(), def.getHeader(), UiFactory.getLabelFont());
					if (def.getPropertyPath().equals("product.productSpecCode")) {
						field.setMinWidth(220);
					}
					pane.getChildren().add(UiFactory.createLabel(def.getHeader(), def.getHeader(), UiFactory.getLabelFont()));
					pane.getChildren().add(field);
				}
			} else {
				pane.getChildren().add(UiFactory.createLabel(def.getHeader(), def.getHeader(), UiFactory.getLabelFont()));
				pane.getChildren().add(field);
			}
		}
		return pane;
	}

	/**
	 *  This is used to create the serial number dynamically on the TableView's '#' column
	 * @param rowIndex
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void createSerialNumber(LoggedTableColumn rowIndex){
		rowIndex.setCellFactory( new Callback<LoggedTableColumn, LoggedTableCell>()
				{
			public LoggedTableCell call(LoggedTableColumn p)
			{
				return new LoggedTableCell()
				{
					@Override
					public void updateItem( Object item, boolean empty )
					{
						super.updateItem( item, empty );
						setText( empty ? null : getIndex() + 1 + "" );
					}
				};
			}
		});
	}

	// ===get/set === //
	//Pulls VIN and YMTO label and value to display on screen  MCM
	public void setInfo(Map<String, ?> model) {
		for (PropertyDef def : getInfoTextFields().keySet()) {
			Object value = BeanUtils.getNestedPropertyValue(model, def.getPropertyPath());
			String str = def.toString(value);
			TextField field = getInfoTextFields().get(def);
			if(StringUtils.equalsIgnoreCase(def.getHeader(), "YMTO")) {
				if (getProductController().getModel().getProductType().equals(ProductType.FRAME.toString()) || getProductController().getModel().getProductType().equals(ProductType.ENGINE.toString())) {
					str = ProductSpecUtil.extractModelYearCode(str)+ProductSpecUtil.extractModelCode(str)+ Delimiter.HYPHEN +ProductSpecUtil.extractModelTypeCode(str)+ Delimiter.HYPHEN +ProductSpecUtil.extractModelOptionCode(str)+ 
							Delimiter.HYPHEN +ProductSpecUtil.extractExtColorCode(str)+ Delimiter.HYPHEN +ProductSpecUtil.extractIntColorCode(str);
				}
			}
			field.setText(str);
			if (showSequenceNumber && str != null && def.getHeader().equals("VIN")){   //if it meets all these conditions change "NA" to Sequence number from Frame table
				if (getProductController().getModel().getProductType().equals("FRAME")) {
					Frame frame = ServiceFactory.getDao(FrameDao.class).findByKey(str);//Look up frame object by supplied VIN
					Integer sequence = frame.getAfOnSequenceNumber();				   //
					
					//BAK - 2015-12-21 - added if statement to reset sequence to N/A when no sequence exists
					if (sequence != null) {
						sequenceLabel.setText(sequence.toString());  // Change to sequence number to one that matches the DB value
					}
					else {
						sequenceLabel.setText("N/A"); // Change to sequence number N/A because there is no sequence for the current product
					}
						
				}
			}
		}
	}

	protected Map<PropertyDef, TextField> getInfoTextFields() {
		return infoTextFields;
	}

	public Label getSequenceLabel() {
		if (this.sequenceLabel == null) {
			double fontSize = Math.ceil(Screen.getMainScreen().getWidth()*0.02);
			this.sequenceLabel = new Label("NA");
			this.sequenceLabel.setStyle("-fx-font-size: " + fontSize + "pt; -fx-font-weight: bold;");
		}
		return this.sequenceLabel;
	}

	public boolean isShowSequenceNumber() {
		return showSequenceNumber;
	}
}

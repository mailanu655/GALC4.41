package com.honda.galc.client.loader;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;

import com.honda.galc.client.mvc.AbstractTabbedView;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.utils.UiFactory;


public class MfgDataLoaderPartMaskPanel extends AbstractTabbedView<MfgDataLoaderPartMaskModel, MfgDataLoaderPartMaskController>{
	
	private LoggedButton fileChooserButton;
	private LoggedButton loadDataButton;
	private LoggedButton resetButton;
	private Text filePath;
	private TextArea templateSelectedText;
	private ProgressBar progressBar;
	
	public MfgDataLoaderPartMaskPanel(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW, mainWindow);
	}
	
	@Override
	public void initView() {
		Rectangle2D parentBounds = Screen.getPrimary().getVisualBounds();
		double scrollPaneWidth = parentBounds.getWidth() - 5;
		double scrollPaneHeight = parentBounds.getHeight() - 141;
		
		VBox partMaskBox = new VBox();
		partMaskBox.getChildren().addAll(createTitiledPane("Mfg Maintainance Control Data Load", createDetailsPanel(), scrollPaneWidth, scrollPaneHeight/2-100, 12, false), 
				createProgressBox(scrollPaneWidth, scrollPaneHeight));
		
		this.setCenter(partMaskBox);
	}
	
	private VBox createDetailsPanel() {
		VBox detailPanel = new VBox();
		Label loadPartMaskLbl =  UiFactory.createLabel("loadPartMaskLbl", "Please Select Spreadsheet To Load Part Masks",Fonts.SS_DIALOG_BOLD(16),TextAlignment.CENTER);;
		
		Rectangle2D parentBounds = Screen.getPrimary().getVisualBounds();
		double scrollPaneWidth = parentBounds.getWidth() - 5;
		double scrollPaneHeight = parentBounds.getHeight() - 141;
		detailPanel.setPrefSize(scrollPaneWidth,scrollPaneHeight);
		detailPanel.setAlignment(Pos.TOP_CENTER);
		detailPanel.getChildren().addAll(loadPartMaskLbl, createButton());
		
		return detailPanel;
	}
	
	private VBox createProgressBox(double scrollPaneWidth, double scrollPaneHeight) {
		VBox progressBox = new VBox();
		progressBar = new ProgressBar();
        progressBar.setPrefWidth(scrollPaneWidth/4);
        progressBar.setVisible(false);
        templateSelectedText = new TextArea();
        templateSelectedText.setVisible(false);
        templateSelectedText.setEditable(false);
        templateSelectedText.setMaxHeight(scrollPaneHeight);
        templateSelectedText.setMinHeight(scrollPaneHeight/1.4);
        templateSelectedText.setMaxWidth(scrollPaneWidth);
        
        progressBox = new VBox();
        progressBox.getChildren().addAll(progressBar, templateSelectedText);
        progressBox.setAlignment(Pos.CENTER);
        return progressBox;
	}
	
	private VBox createButton() {
		VBox btnBox = new VBox();
		HBox selectBox = new HBox();
		Label selectFileLbl =  UiFactory.createLabel("selectFileLbl", "Select File",Fonts.SS_DIALOG_BOLD(16),TextAlignment.LEFT);
		fileChooserButton = createBtn("Select Template ", getController());
		filePath = new Text();
		fileChooserButton.setPadding(new Insets(10));
		selectBox.getChildren().addAll(selectFileLbl, filePath, fileChooserButton);
		selectBox.setAlignment(Pos.CENTER);
		selectBox.setPadding(new Insets(10));
		selectBox.setSpacing(20);
		
		HBox loadBox = new HBox();
		resetButton = createBtn("Reset", getController());
		resetButton.setVisible(false);
		loadDataButton = createBtn("Load Data ", getController());
		loadBox.getChildren().addAll(loadDataButton, resetButton);
		loadDataButton.setVisible(false);
		loadBox.setAlignment(Pos.CENTER);
		loadBox.setPadding(new Insets(10));
		btnBox.getChildren().addAll(selectBox, loadBox);
		
		return btnBox;
	}
	
	public LoggedButton createBtn(String text,EventHandler<ActionEvent> handler) {
		LoggedButton btn = UiFactory.createButton(text, text);
		btn.defaultButtonProperty().bind(btn.focusedProperty());
		btn.setOnAction(handler);
		btn.getStyleClass().add("table-button");
		return btn;
	}
	
	@Override
	public void onTabSelected() {
		
	}

	@Override
	public void reload() {
		
	}
	
	private TitledPane createTitiledPane(String title,Node content, double width, double height, int font, boolean collapsible) {
		TitledPane titledPane = new TitledPane();
		titledPane.setText(title);
		titledPane.setFont(Font.font("", FontWeight.BOLD, font));
		titledPane.setContent(content);
		titledPane.setPrefSize(width,height);
		titledPane.setCollapsible(collapsible);
		return titledPane;
	}
	
	@Override
	public void start() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public String getScreenName() {
		return "Data Load Part Mask";
	}

	public LoggedButton getFileChooserButton() {
		return fileChooserButton;
	}

	public void setFileChooserButton(LoggedButton fileChooserButton) {
		this.fileChooserButton = fileChooserButton;
	}

	public LoggedButton getLoadDataButton() {
		return loadDataButton;
	}

	public void setLoadDataButton(LoggedButton loadDataButton) {
		this.loadDataButton = loadDataButton;
	}

	public TextArea getTemplateSelectedText() {
		return templateSelectedText;
	}

	public void setTemplateSelectedText(TextArea templateSelectedText) {
		this.templateSelectedText = templateSelectedText;
	}

	public ProgressBar getProgressBar() {
		return progressBar;
	}

	public void setProgressBar(ProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	public LoggedButton getResetButton() {
		return resetButton;
	}

	public void setResetButton(LoggedButton resetButton) {
		this.resetButton = resetButton;
	}

	public Text getFilePath() {
		return filePath;
	}

	public void setFilePath(Text filePath) {
		this.filePath = filePath;
	}
	
}

package com.honda.galc.client.teamleader.qi.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.ui.component.FxDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTextArea;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dao.qi.QiBomPartDao;

import javafx.scene.control.TextField;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import com.honda.galc.entity.qi.QiBomPart;
import com.honda.galc.entity.qi.QiBomPartId;

public class QiBomNewItemDialog extends FxDialog {

    private TextField dcPartNo;
    private TextField modelCode;
    private TextField mainPartNo;
    private TextField dcPartName;
    private LoggedButton okBtn;
	private LoggedButton cancelBtn;
	private String buttonClickedname;
	QiBomPart newPart = new QiBomPart();
	private TableView<QiBomPart> tableView = new TableView<>();
	
    public QiBomNewItemDialog() {
        super("Add Record to BOM");
        initComponents();
    }

    private void initComponents() {
    	   VBox vbox = new VBox(10);
    	    vbox.setPadding(new Insets(10));
    	    dcPartNo = new TextField();
    	    modelCode = new TextField();
    	    mainPartNo = new TextField();
    	    dcPartName = new TextField();
    	    vbox.getChildren().addAll(
   	        new Label("DC_PART_NO:"), dcPartNo,
   	        new Label("MODEL_CODE:"), modelCode,
   	        new Label("MAIN_PART_NO:"), mainPartNo,
   	        new Label("DC_PART_NAME:"), dcPartName,
   	        createButtonBox()
    	    );
    	    BorderPane borderPane = new BorderPane(vbox);
    	    Scene scene = new Scene(borderPane);
    	    this.setScene(scene);    
    }

    private HBox createButtonBox() {
        HBox hbox = new HBox(10);
        hbox.setAlignment(Pos.CENTER_RIGHT);
        
		okBtn = createBtn("Ok");
		cancelBtn = createBtn(QiConstant.CANCEL);
		hbox.getChildren().addAll(okBtn, cancelBtn);
		hbox.setAlignment(Pos.CENTER);
		hbox.setPadding(new Insets(0, 10, 10, 10));
		hbox.setSpacing(10);
        return hbox;
    }
    private LoggedButton createBtn(String text)
	{
		LoggedButton btn = UiFactory.createButton(text, text);
		btn.defaultButtonProperty().bind(btn.focusedProperty());
		btn.setOnAction(handler);
		btn.getStyleClass().add("popup-btn");
		return btn;
	}
    public String getButtonClickedname() {
		return StringUtils.trimToEmpty(buttonClickedname);
	}

	private EventHandler<ActionEvent> handler = new EventHandler<ActionEvent>() {

		public void handle(ActionEvent event) {
			Object source = event.getSource();
			if (source instanceof LoggedButton) {
				LoggedButton btn = (LoggedButton) source;
				buttonClickedname = btn.getText();
				if(getButtonClickedname().equalsIgnoreCase("Ok"))
				{
						newPart.setId(new QiBomPartId());
					    newPart.setMainPartNo(mainPartNo.getText());
					    newPart.setDcPartName(dcPartName.getText());
					    newPart.setCreateTimestamp(Timestamp.valueOf(LocalDateTime.now()));
					    newPart.setUpdateTimestamp(Timestamp.valueOf(LocalDateTime.now()));
					    ObservableList<QiBomPart> data = FXCollections.observableArrayList();
					    data.add(newPart);
					    tableView.setItems(data);
					    
					    QiBomPart newBomPart = new QiBomPart();
						QiBomPartId newId = new QiBomPartId();
						newId.setDcPartNo(dcPartNo.getText());
						newId.setModelCode(modelCode.getText());
						newId.setProductKind(QiConstant.AUTOMOBILE);
						newBomPart.setId(newId);
						newBomPart.setDcPartName(dcPartName.getText());
						newBomPart.setCreateTimestamp(Timestamp.valueOf(LocalDateTime.now()));
						newBomPart.setMainPartNo(mainPartNo.getText());
						newBomPart.setUpdateTimestamp(Timestamp.valueOf(LocalDateTime.now()));
						getDao(QiBomPartDao.class).save(newBomPart);
						
						Stage stage = (Stage) btn.getScene().getWindow();
						stage.close();

				}
				else
				{
					Stage stage = (Stage) btn.getScene().getWindow();
					stage.close();
				}
			}
		}
	};

	public boolean showBomNewItemDialog(Object object) {
		initComponents();
		showDialog();
		if(getButtonClickedname().equalsIgnoreCase("Ok"))
			return true;
		else
			return false;
	}

}
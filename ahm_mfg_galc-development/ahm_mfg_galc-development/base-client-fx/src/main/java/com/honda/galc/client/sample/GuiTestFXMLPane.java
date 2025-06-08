package com.honda.galc.client.sample;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.ui.ApplicationMainPane;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.MessageDialog;

public class GuiTestFXMLPane  extends ApplicationMainPane  {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnSetError"
    private Button btnSetError; // Value injected by FXMLLoader

    @FXML // fx:id="btnSetStatus"
    private Button btnSetStatus; // Value injected by FXMLLoader

    @FXML // fx:id="btnClearException"
    private Button btnClearException; // Value injected by FXMLLoader

    @FXML // fx:id="btnClearStatus"
    private Button btnClearStatus; // Value injected by FXMLLoader

    @FXML // fx:id="btnClearMessage"
    private Button btnClearMessage; // Value injected by FXMLLoader

    @FXML // fx:id="btnSetMessage"
    private Button btnSetMessage; // Value injected by FXMLLoader

    @FXML // fx:id="btnSetMessageDialog"
    private Button btnSetMessageDialog; // Value injected by FXMLLoader

    @FXML // fx:id="btnShowError1"
    private Button btnShowError1; // Value injected by FXMLLoader

    @FXML // fx:id="btnShowError0"
    private Button btnShowError0; // Value injected by FXMLLoader

    @FXML // fx:id="btnLongData"
    private ToggleButton btnLongData; // Value injected by FXMLLoader

    @FXML // fx:id="btnConfirm1"
    private Button btnConfirm1; // Value injected by FXMLLoader

    @FXML // fx:id="btnShowError5"
    private Button btnShowError5; // Value injected by FXMLLoader

    @FXML // fx:id="btnSetException"
    private Button btnSetException; // Value injected by FXMLLoader

    @FXML // fx:id="btnShowError4"
    private Button btnShowError4; // Value injected by FXMLLoader

    @FXML // fx:id="btnShowError3"
    private Button btnShowError3; // Value injected by FXMLLoader

    @FXML // fx:id="btnConfirm2"
    private Button btnConfirm2; // Value injected by FXMLLoader

    @FXML // fx:id="btnShowScroll4"
    private Button btnShowScroll4; // Value injected by FXMLLoader

    @FXML // fx:id="btnShowScroll5"
    private Button btnShowScroll5; // Value injected by FXMLLoader

    @FXML // fx:id="btnShowScroll2"
    private Button btnShowScroll2; // Value injected by FXMLLoader

    @FXML // fx:id="btnShowScroll3"
    private Button btnShowScroll3; // Value injected by FXMLLoader

    @FXML // fx:id="btnShowScroll1"
    private Button btnShowScroll1; // Value injected by FXMLLoader

    @FXML // fx:id="btnShowInfo4"
    private Button btnShowInfo4; // Value injected by FXMLLoader

    @FXML // fx:id="btnShowError4a"
    private Button btnShowError4a; // Value injected by FXMLLoader

    @FXML // fx:id="btnShowInfo3"
    private Button btnShowInfo3; // Value injected by FXMLLoader

    @FXML // fx:id="btnShowInfo2"
    private Button btnShowInfo2; // Value injected by FXMLLoader

    @FXML // fx:id="btnShowInfo1"
    private Button btnShowInfo1; // Value injected by FXMLLoader
    
    
    private Stage parentStage = ClientMainFx.getInstance().getStage();

    
    public GuiTestFXMLPane(MainWindow window) {
		super(window, true);
		btnLongData.setSelected(true);
	}

    @FXML
    void setMessage(ActionEvent event) {
   	   getMainWindow().getStatusMessagePane().setMessage(formatData("MESSAGE"));
    }

    @FXML
    void clearMessage(ActionEvent event) {
       getMainWindow().getStatusMessagePane().setMessage(null);
    }

    @FXML
    void setMessageDialog(ActionEvent event) {
    	getMainWindow().getStatusMessagePane().setMessage(formatData("DIALOG"));      	
    	getMainWindow().getStatusMessagePane().displayMessageDialog();
    }
    
    @FXML
    void setStatus(ActionEvent event) {
    	getMainWindow().getStatusMessagePane().setStatusMessage(formatData("STATUS"));
    }

    @FXML
    void clearStatus(ActionEvent event) {
    	getMainWindow().getStatusMessagePane().setStatusMessage(null);
    }

    @FXML
    void setError(ActionEvent event) {
    	getMainWindow().getStatusMessagePane().setErrorMessageArea(formatData("ERROR"));
    }

    @FXML
    void setException(ActionEvent event) {
    	getMainWindow().getStatusMessagePane().setErrorMessageArea(formatData("EXCEPTION"));
    }

    @FXML
    void clearException(ActionEvent event) {
    	getMainWindow().getStatusMessagePane().setErrorMessageArea(null);
    }
    
    @FXML
    void longData(ActionEvent event) {
    	 if (btnLongData.isSelected() ) {
    		 btnLongData.setText("Long Data");
    	 } else {
    		 btnLongData.setText("Short Data");
    	 }
    }
    
    @FXML
    void showError0(ActionEvent event) {
        MessageDialog.showError("TEST :  static void showError(String message)");
    }

    @FXML
    void showError1(ActionEvent event) {
        MessageDialog.showError(parentStage,"TEST : static void showError(Stage parent,String message)");
    }

    @FXML
    void showError3(ActionEvent event) {
    	MessageDialog.showError(parentStage,"TEST : static void showError(Stage parent,String message,String title)","TEST : Title");
    }

    @FXML
    void btnShowError4(ActionEvent event) {
        MessageDialog.showError(parentStage,"TEST : static void showError(Stage parent,String message,int countPerLine)",3);
    }
    

    @FXML
    void showError4a(ActionEvent event) {
    	 MessageDialog.showError(parentStage,"TEST : static void showError(Stage parent,String message,String title,int columnCount)","TEST : Title",3);
    }
        

    @FXML
    void showInfo1(ActionEvent event) {
        MessageDialog.showInfo(parentStage,"TEST : showInfo(Stage parent,String message)");
    }

    @FXML
    void showInfo3(ActionEvent event) {
        MessageDialog.showInfo(parentStage,"TEST : MessageDialog.showInfo(parent, message, countPerLine)", 3);
    }

    @FXML
    void showInfo2(ActionEvent event) {
        MessageDialog.showInfo(parentStage,"TEST : static void showInfo(Stage parent,String message,String title)", "TEST : Title");
    }

    @FXML
    void showError5(ActionEvent event) {
       MessageDialog.showError(parentStage, "TEST : static void showError(Stage parent,String message,String title,int columnCount)", "TITLE : Title", 5);
    }

    @FXML
    void showInfo4(ActionEvent event) {
        MessageDialog.showInfo(parentStage, "static void showInfo(Stage parent,String message,String title,int columnCount)", "TITLE : Title", 5);
    }

    @FXML
    void showScroll1(ActionEvent event) {
        MessageDialog.showScrollingInfo(parentStage, "showScrollingInfo(Stage parent,String message)");
    }

    @FXML
    void showScroll2(ActionEvent event) {
    	MessageDialog.showScrollingInfo(parentStage,"TEST : showScrollingInfo(Stage parent,String message,int column)", 5);
    }

    @FXML
    void showScroll3(ActionEvent event) {
   	    MessageDialog.showScrollingInfo(parentStage,"TEST : MessageDialog.showScrollingInfo(parent, message, title)", "TITLE ");
    }

    @FXML
    void showScroll4(ActionEvent event) {
   	    MessageDialog.showScrollingInfo(parentStage,"TEST :static void showScrollingInfo(Stage parent,String message,int row, int column)",5,5);
    }
    

    @FXML
    void showScroll5(ActionEvent event) {
   	    MessageDialog.showScrollingInfo(parentStage,"TEST :   static void showScrollingInfo(Stage parent,String message,String title,int row, int column) ","TEST : Title",5, 5);
    }

    @FXML
    void confirm1(ActionEvent event) {
    	MessageDialog.confirm(parentStage, "confirm(Stage parent, String message)");
    }

    @FXML
    void confirm2(ActionEvent event) {
    	MessageDialog.confirm(parentStage, "confirm(Stage parent, String message)",false);
    }   
    
    

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnSetError != null : "fx:id=\"btnSetError\" was not injected: check your FXML file 'GuiTestFXMLPane.fxml'.";
        assert btnSetStatus != null : "fx:id=\"btnSetStatus\" was not injected: check your FXML file 'GuiTestFXMLPane.fxml'.";
        assert btnClearException != null : "fx:id=\"btnClearException\" was not injected: check your FXML file 'GuiTestFXMLPane.fxml'.";
        assert btnClearStatus != null : "fx:id=\"btnClearStatus\" was not injected: check your FXML file 'GuiTestFXMLPane.fxml'.";
        assert btnClearMessage != null : "fx:id=\"btnClearMessage\" was not injected: check your FXML file 'GuiTestFXMLPane.fxml'.";
        assert btnSetMessage != null : "fx:id=\"btnSetMessage\" was not injected: check your FXML file 'GuiTestFXMLPane.fxml'.";
        assert btnSetMessageDialog != null : "fx:id=\"btnSetMessageDialog\" was not injected: check your FXML file 'GuiTestFXMLPane.fxml'.";
//        assert btnShowError2 != null : "fx:id=\"btnShowError2\" was not injected: check your FXML file 'GuiTestFXMLPane.fxml'.";
        assert btnShowError1 != null : "fx:id=\"btnShowError1\" was not injected: check your FXML file 'GuiTestFXMLPane.fxml'.";
        assert btnLongData != null : "fx:id=\"btnLongData\" was not injected: check your FXML file 'GuiTestFXMLPane.fxml'.";
        assert btnConfirm1 != null : "fx:id=\"btnConfirm1\" was not injected: check your FXML file 'GuiTestFXMLPane.fxml'.";
        assert btnShowError5 != null : "fx:id=\"btnShowError5\" was not injected: check your FXML file 'GuiTestFXMLPane.fxml'.";
        assert btnSetException != null : "fx:id=\"btnSetException\" was not injected: check your FXML file 'GuiTestFXMLPane.fxml'.";
        assert btnShowError4 != null : "fx:id=\"btnShowError4\" was not injected: check your FXML file 'GuiTestFXMLPane.fxml'.";
        assert btnShowError3 != null : "fx:id=\"btnShowError3\" was not injected: check your FXML file 'GuiTestFXMLPane.fxml'.";
        assert btnConfirm2 != null : "fx:id=\"btnConfirm2\" was not injected: check your FXML file 'GuiTestFXMLPane.fxml'.";
        assert btnShowScroll4 != null : "fx:id=\"btnShowScroll4\" was not injected: check your FXML file 'GuiTestFXMLPane.fxml'.";
        assert btnShowScroll5 != null : "fx:id=\"btnShowScroll5\" was not injected: check your FXML file 'GuiTestFXMLPane.fxml'.";
        assert btnShowScroll2 != null : "fx:id=\"btnShowScroll2\" was not injected: check your FXML file 'GuiTestFXMLPane.fxml'.";
        assert btnShowScroll3 != null : "fx:id=\"btnShowScroll3\" was not injected: check your FXML file 'GuiTestFXMLPane.fxml'.";
        assert btnShowScroll1 != null : "fx:id=\"btnShowScroll1\" was not injected: check your FXML file 'GuiTestFXMLPane.fxml'.";
        assert btnShowInfo4 != null : "fx:id=\"btnShowInfo4\" was not injected: check your FXML file 'GuiTestFXMLPane.fxml'.";
        assert btnShowInfo3 != null : "fx:id=\"btnShowInfo3\" was not injected: check your FXML file 'GuiTestFXMLPane.fxml'.";
        assert btnShowInfo2 != null : "fx:id=\"btnShowInfo2\" was not injected: check your FXML file 'GuiTestFXMLPane.fxml'.";
        assert btnShowInfo1 != null : "fx:id=\"btnShowInfo1\" was not injected: check your FXML file 'GuiTestFXMLPane.fxml'.";   
  }
    
    private String formatData(String message) {
    	String data;
    	
    	if (btnLongData.isSelected()) {
    	   data = String.format("%40s%s_%tc_%2$s%40s",' ',message, System.currentTimeMillis(),' ').replace(' ', '#');
    	} else {
    	   data = String.format("%5s%s_%tc_%2$s%5s",' ',message, System.currentTimeMillis(),' ').replace(' ', '#');
    	}
    	
    	return data;
    }
    
}
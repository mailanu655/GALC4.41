package com.honda.galc.client.utils;

import java.util.Stack;
import java.util.function.Function;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.util.GalcStation;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class QiProgressBar extends Stage {
	
	private String labelText;
	private String processName;
	private Label statusLabel;
	private String productId = "";
	private ProgressBar progressBar;
	private VBox progressBox;
	Label lblWait, lblProc;
	private double myWidth = 0.0D, myHeight = 0.0D;
	private static QiProgressBar _instance = null;
	private Stack<LabelText> messageStack;
	private int refCount = 0;
	private StringProperty labelTextProp = new SimpleStringProperty();
	
	private static class LabelText  {
		String procName = "";
		String lblTxt = "";
		String myProductId = "";
		
		public LabelText(String newProcName, String newLblTxt, String newProductId)  {
			procName = newProcName;
			lblTxt = newLblTxt;
			myProductId = newProductId;
		}		
	}
	
	public QiProgressBar(String newLabelText,String newProcessName,String newProductId, Stage owner,boolean isModal) {
		super();

		setDisplayData(newLabelText, newProcessName, newProductId);
		
		messageStack = new Stack<LabelText>();
		refCount = 1;
		
		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		this.myWidth = screenBounds.getWidth()/4;
		this.myHeight = screenBounds.getHeight()/4;
		
		lblWait = new Label("Please wait...");
		lblProc = new Label();
		lblWait.getStyleClass().add("display-label-blue");
		lblProc.getStyleClass().add("display-label-blue");
		//lblWait.setPadding(new Insets(0,50,0,0));
		lblProc.textProperty().bind(labelTextProp);
		HBox labelBox = new HBox();
		labelBox.setSpacing(10);
		labelBox.setAlignment(Pos.CENTER);
		labelBox.getChildren().addAll(lblWait, lblProc);
		
		progressBar = new ProgressBar(0.25D);
		progressBar.setPrefWidth(myWidth/1.25);
        progressBox = new VBox();
        progressBox.getChildren().addAll(labelBox,progressBar);
        progressBox.setAlignment(Pos.CENTER);
        progressBox.setStyle("-fx-text-background-color: #D3D3D3;");
    	getDescriptionLabel();
		
		StackPane myPane = new StackPane();
		myPane.setPrefSize(myWidth, myHeight/3.0);
		myPane.getChildren().add(progressBox);
		
		Scene myScene = new Scene(myPane);
		myScene.setFill(Color.LIGHTGRAY);
		myScene.getStylesheets().add(QiConstant.CSS_PATH);
		setScene(myScene);
		
		initModality(isModal ?Modality.WINDOW_MODAL : Modality.NONE);
		initOwner(owner);
		setResizable(false);
		sizeToScene();
		setOpacity(0.8D);
		centerOnScreen();
		        
		this.setOnCloseRequest( new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
			    close();	
			}
		});
	}
	
	public static QiProgressBar getInstance(String newLabelText,String newProcessName,String newProductId, Stage owner, boolean isModal) {
		if(_instance != null)  {
			_instance.increaseRefCount();
			if(_instance.getRefCount() > 1)  _instance.pushMessage();
			_instance.setDisplayData(newLabelText, newProcessName, newProductId);
		}
		else  {
			_instance = new QiProgressBar(newLabelText,newProcessName,newProductId,owner,isModal);
		}
		return _instance;
	}
	
	public void setDisplayData(String labelText1, String processName1, String productId1) {
		this.processName = processName1;
		this.labelText = labelText1;
		if(StringUtils.isEmpty(labelText1))  this.labelText = this.processName;
		this.productId = productId1;
		labelTextProp.set(labelText1);
	}
	
	private void pushMessage()  {
		getMessageStack().push(new QiProgressBar.LabelText(labelText, processName, productId));		
	}
	
	public void initializeProgressBarStage(Stage owner) {}
	
	public void showMe() {
		startLog();
		if(!isShowing())  {
			this.show();
		}
	}
	
	public void closeMe() {
		endLog();
		if(this.isShowing())  {
			close();
		}
		if(getRefCount() == 1)  {
			getProgressBar().setProgress(100.0);
			setRefCount(0);
			messageStack.clear();
			setDisplayData("", "", "");
		}
		else  {
			decreaseRefCount();
			QiProgressBar.LabelText currentLabelText = messageStack.pop();
			if(currentLabelText != null)  {
				setDisplayData(currentLabelText.lblTxt, currentLabelText.procName, currentLabelText.myProductId);
			}
		}
	}
	
	public void initializeProgressBar(Function<Void,Void> myTask) {
		try {
			Task<Void> mainTask = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					
					showMe();
					Void a = null;
					myTask.apply(a);
					return null;
				}
			};

			mainTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
				public void handle(WorkerStateEvent t) {
					closeMe();
				}
			});
			progressBar.progressProperty().bind(mainTask.progressProperty());

			new Thread(mainTask).start();
		} finally  {
			closeMe();
		}
	}

	public void startLog() {
		try {

			Logger.getLogger().check("[(Start Time )"
					+"(Process Name: "+this.processName+")"
					+"(Asset Name: "+GalcStation.getHostName()+")"
					+"(User Id: "+ApplicationContext.getInstance().getUserId()+")" 
					+"(Terminal Name: "+ApplicationContext.getInstance().getHostName()+")"
					+"(Product Id: "+productId
					+")]");
		} catch (Exception e) {
			Logger.getLogger().error("Error while starting progress bar.");
		}
	}

	public void endLog() {
		try {
			Logger.getLogger().check("[(End Time)"
					+"(Process Name: "+this.processName+")"
					+"(Asset Name: "+GalcStation.getHostName()+")"
					+"(User Id: "+ApplicationContext.getInstance().getUserId()+")" 
					+"(Terminal Name: "+ApplicationContext.getInstance().getHostName()+")"
					+"(Product Id: "+productId
					+")]");

		} catch (Exception e) {
			Logger.getLogger().error("Error while stoping progress bar.");
		}
	}

	public Label getDescriptionLabel() {
		if(statusLabel == null) {
			statusLabel = UiFactory.createLabel("descriptionLabel", labelText);
		}
		return statusLabel;
	}
	
	public VBox getProgressBox() {
		return progressBox;
	}

	public ProgressBar getProgressBar() {
		return progressBar;
	}

	public void setProgressBar(ProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	public void updateMessage(String message) {
		statusLabel.setText(message);
	}

	public String getProductId() {
		return productId;
	}

	public int getRefCount() {
		return refCount;
	}

	public void setRefCount(int refCount) {
		this.refCount = refCount;
	}
	
	public int increaseRefCount() {
		return ++refCount;
	}

	public int decreaseRefCount() {
		return --refCount;
	}

	public Stack<LabelText> getMessageStack() {
		return messageStack;
	}

	public void setMessageStack(Stack<LabelText> messageStack) {
		this.messageStack = messageStack;
	}


}
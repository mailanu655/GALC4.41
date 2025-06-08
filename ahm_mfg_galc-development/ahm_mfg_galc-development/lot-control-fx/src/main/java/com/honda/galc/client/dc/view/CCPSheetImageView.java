package com.honda.galc.client.dc.view;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import com.honda.galc.client.audio.ClientAudioManager;
import com.honda.galc.client.product.mvc.ProductModel;
import com.honda.galc.client.property.AudioPropertyBean;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dto.PddaUnitImage;
import com.honda.galc.service.property.PropertyService;

/*
 * This class is a rewrite of the widget without the Pagination control to avoid the lag between page transitions.
 * The javafx.scene.control.Pagination control is particularly slow on a Wyse terminal when a page is transitioned from one image to another. 
 * author Suriya Sena
 */


public class CCPSheetImageView implements EventHandler<ActionEvent> {

	private static CCPSheetImageView instance= new CCPSheetImageView();
	
	private static final int MAX_HEIGHT =460;
	
	private static final String confirmImageUrl = "/resource/images/common/confirm.png";
	private static final String leftImageUrl = "/resource/images/common/left.png";
	private static final String rightImageUrl = "/resource/images/common/right.png";
	private static final String emptyImageUrl = "/resource/images/common/empty.png";
	

	private Image[] imageList; 
	private boolean pageView[];
	private boolean isModelChange;
	private ImageView currentImage;
	private Button leftButton;
	private Button rightButton;
	private Button confirmButton;
	private int  currentImageIndex=0;
	private Dialog dialog;
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	private ScheduledFuture<?> alertHandler; 
	private Runnable alert;
	private static final int INTIAL_DELAY_SECS=5;
	private static final int INTERVAL_SECS=2;
	private List<String> imageNameLst;
	
	static class Dialog extends Stage {
		public Dialog(Stage owner, Scene scene) {
			setTitle("CCP Sheet Image");
			initStyle(StageStyle.UNDECORATED);
			initModality(Modality.NONE);
			initOwner(owner);
			setResizable(false);
			setScene(scene);
		}

		public void showDialog() {
			sizeToScene();
			// centerOnScreen();
			setY(1);
			setX(160);
			showAndWait();
		}
	}
	
	
	private CCPSheetImageView() {
	   final ClientAudioManager audioManager = new ClientAudioManager(PropertyService.getPropertyBean(AudioPropertyBean.class));
	   alert = new Runnable() {
		       public void run() {
		    	   audioManager.playUrgeSound();
		       }
	   };
	}
	

	public static void showImage(Stage parent,List<PddaUnitImage> pddaimage,ProductModel productmodel) {
		instance.show(parent, pddaimage, productmodel);
	}
	
	private Scene initScene(ProductModel productmodel) {
		VBox mainVbox = new VBox();
		mainVbox.setPadding(new Insets(10));
		mainVbox.setSpacing(5);
		mainVbox.setStyle("-fx-border-style: solid; -fx-border-color: gray;-fx-border-width: 3px;  -fx-border-radius: 2px;");

		currentImageIndex = 0;
		currentImage = new ImageView();
		
		showImage(currentImageIndex);
		
		HBox buttonPane = new HBox(10);
		buttonPane.setAlignment(Pos.CENTER);
		
		buttonPane.setPadding(new Insets(10));

		ImageView leftImageView;
		leftImageView = new ImageView(leftImageUrl);
		leftButton = UiFactory.createButton(null, "CCPImgLeftBtn");
		leftButton.setGraphic(leftImageView);
		leftButton.setOnAction(this);
		leftButton.setMaxHeight(Double.MAX_VALUE);
		leftButton.setDisable(pageView.length<=1);
	
		ImageView rightImageView;
		rightImageView = new ImageView(rightImageUrl);
		rightButton = UiFactory.createButton(null, "CCPImgRightBtn");
		rightButton.setGraphic(rightImageView);
		rightButton.setOnAction(this);
		rightButton.setMaxHeight(Double.MAX_VALUE);
		rightButton.setDisable(pageView.length<=1);
		
		ImageView confirmImageView;
		confirmImageView = new ImageView(confirmImageUrl);
		confirmButton = UiFactory.createButton("Confirm", "CCPImgConfirmBtn");
		confirmButton.setGraphic(confirmImageView);
		setConfirmButtonProperties();
		confirmButton.setOnAction(this);
		confirmButton.setStyle("-fx-font-size: 24;");
		confirmButton.setMaxWidth(Double.MAX_VALUE);
		
		buttonPane.getChildren().addAll(leftButton,confirmButton,rightButton);
		
		if (isModelChange) {
			Label modelChangeLabel = UiFactory.createLabel("modelChangeLabel", "MODEL CHANGE!");
			modelChangeLabel.setMaxWidth(Double.MAX_VALUE);
			modelChangeLabel.setStyle("-fx-font-size: 48; -fx-font-weight: bolder; -fx-text-fill: white; -fx-background-color:red");
			modelChangeLabel.setAlignment(Pos.CENTER);
			
			FadeTransition ft = new FadeTransition(Duration.millis(500), modelChangeLabel);
	        ft.setFromValue(1.0);
	        ft.setToValue(0.3);
	        ft.setCycleCount(Animation.INDEFINITE);
	        ft.setAutoReverse(true);
	        
	        ft.play();
	        
			mainVbox.getChildren().addAll(getHeaderInfo(productmodel),currentImage,modelChangeLabel, buttonPane);
		} else {
			mainVbox.getChildren().addAll(getHeaderInfo(productmodel),currentImage, buttonPane);
		}
		
		Scene scene = new Scene(mainVbox);
		return scene;
	}
	
	private HBox getHeaderInfo(ProductModel productmodel) {
		
		UiFactory uiFactory = UiFactory.getInfo();
		String ProductIdLabeltxt = productmodel.getProductTypeData().getProductIdLabel();
		String ProductSpecCodeLabeltxt="YMTO";
		String style = uiFactory.getLabelFont()+";-fx-background-color: white;-fx-border-color: gray;-fx-border-width: 3px;  -fx-border-radius: 2px;-fx-padding: 5px;";
		
		HBox pane = new HBox();
		pane.setSpacing(15);
		
		HBox ProductIDpane = new HBox();
		ProductIDpane.setSpacing(5);
		HBox ProductSpecpane = new HBox();
		ProductSpecpane.setSpacing(5);
		pane.setAlignment(Pos.CENTER);
		ProductSpecpane.setAlignment(Pos.CENTER);
		ProductIDpane.setAlignment(Pos.CENTER);
		
		
		pane.setPadding(new Insets(10));
		ProductIDpane.getChildren().add(UiFactory.createLabel("productIdLabel", ProductIdLabeltxt, uiFactory.getLabelFont()));
		ProductIDpane.getChildren().add(UiFactory.createLabel("productModelLabel", productmodel.getProductId(),style));

		ProductSpecpane.getChildren().add(UiFactory.createLabel("ProductSpecCodeLabeltxt", ProductSpecCodeLabeltxt, uiFactory.getLabelFont()));
		ProductSpecpane.getChildren().add(UiFactory.createLabel("productModelProduct", productmodel.getProduct().getProductSpecCode(),style));
		pane.getChildren().addAll(ProductIDpane,ProductSpecpane);
		
		return pane;

	}

	
	public void handle(ActionEvent event) {
       if (event.getSource().equals(rightButton)) {
    	   currentImageIndex = clamp(currentImageIndex+1);
    	   Logger.getLogger().check("CCP Image Right button Clicked");
       } else if (event.getSource().equals(leftButton)) {
    	   currentImageIndex = clamp(currentImageIndex-1);
    	   Logger.getLogger().check("CCP Image Left button Clicked");
       } else if (event.getSource().equals(confirmButton)) {
    	   cancelAlarm();
    	   Logger.getLogger().check("CCP Image Confirm button Clicked");
    	   dialog.close();
       }
       
       showImage(clamp(currentImageIndex));
	   setConfirmButtonProperties();

       if (allPagesViewed()) {
    	   rightButton.setEffect(addDropShadow());
    	   leftButton.setEffect(addDropShadow());
       }
	} 
	
	private void setConfirmButtonProperties() {
		if ( allPagesViewed()) {
			confirmButton.getGraphic().setEffect(null);
			confirmButton.setEffect(addDropShadow());
    		confirmButton.setDisable(false);
    		Logger.getLogger().check("Confirm button enabled");
		} else {
			confirmButton.getGraphic().setEffect(new SepiaTone());
			confirmButton.setEffect(new GaussianBlur());
    		confirmButton.setDisable(true);
    		Logger.getLogger().check("Confirm button disabled");
		}
	}
	
	private int clamp(int index) {
		if (index >= imageList.length) {
			index = imageList.length - 1;
		} else if (index < 0) {
			index = 0;
		}
		return index;
	}
	
	private void showImage(int i) {
		currentImage.setImage(imageList[i]);
		pageView[i]=true;
		
		currentImage.setFitHeight(MAX_HEIGHT);
		currentImage.setPreserveRatio(true);
		currentImage.setSmooth(false);
		currentImage.setCache(false);
		Logger.getLogger().check("Image " + imageNameLst.get(i).trim() + " is shown");
	}
	
	private boolean allPagesViewed() {
		for (int i= 0; i < pageView.length; i++) {
			if (pageView[i] == false) {
				return false;
			}
		}
		return true;
	}
	
	private void show(Stage parentStage,List<PddaUnitImage> pddaImage,ProductModel productmodel) {
		loadImageView(pddaImage);
		
		this.isModelChange = productmodel.isModelChanged(productmodel.getProduct().getProductSpecCode());;
		
		Scene scene = initScene(productmodel);
		showImage(0);
		
		dialog = new Dialog(parentStage, scene);
		scheduleAlarm();
		dialog.showDialog();
	}
	
	private Effect addDropShadow() {
		 DropShadow dropShadow = new DropShadow();
		 dropShadow.setRadius(20.0);
		 dropShadow.setOffsetX(2.0);
		 dropShadow.setOffsetY(2.0);
		 dropShadow.setColor(Color.LAWNGREEN);
		 return dropShadow;
	}
	

	private void loadImageView( List<PddaUnitImage> pddaImage) {
		if (pddaImage == null) {
		  pageView = new boolean[1];
		  imageList = new Image[1];
		  imageList[0] = new Image(emptyImageUrl);
		  imageNameLst = new ArrayList<String>(1);
		  imageNameLst.add("emptyImage");
		} else {
		  imageList = new Image[pddaImage.size()];
		  pageView = new boolean[pddaImage.size()];
		  imageNameLst = new ArrayList<String>(pddaImage.size());;
		  for (int i = 0; i < pddaImage.size(); i++) {
		 	 imageList[i] = new Image(new ByteArrayInputStream(pddaImage.get(i).getImage()));
		 	 imageNameLst.add(pddaImage.get(i).getUnitImage());
		  }
		}
	}
	
    private void cancelAlarm() {
    	alertHandler.cancel(true);
    }
    
	private void scheduleAlarm() {
		alertHandler =scheduler.scheduleAtFixedRate(alert,INTIAL_DELAY_SECS, INTERVAL_SECS, TimeUnit.SECONDS);
	}
}

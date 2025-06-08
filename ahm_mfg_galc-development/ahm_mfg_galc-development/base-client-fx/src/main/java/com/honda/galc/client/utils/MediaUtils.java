package com.honda.galc.client.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.common.logging.Logger;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * <h3>Class description</h3>
 * 
 * <h4>Description</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * </TABLE>
 * @version 1.0
 * @author Dylan Yang
 * @see
 *
 */
public class MediaUtils {
        
	public static void showMedia(String url) {
		String lowercaseUrl = url.toLowerCase();
		if(lowercaseUrl.endsWith("mp4") || lowercaseUrl.endsWith("mov")) {
			MediaUtils.playVideo(url);
		} else if(lowercaseUrl.endsWith("jpg") || lowercaseUrl.endsWith("png") || lowercaseUrl.endsWith("gif") || lowercaseUrl.endsWith("bmp")) {
			MediaUtils.showImage(url);
		}
	}
	
    public static void playVideo(String url) {
    	
        Media media;
		try {
			media = new Media(url);
	        MediaPlayer player = new MediaPlayer(media);
	        MediaView mediaView = new MediaView(player);
       
	        DoubleProperty width = mediaView.fitWidthProperty();
	        DoubleProperty height = mediaView.fitHeightProperty();
	        width.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
	        height.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));
	        mediaView.setPreserveRatio(true);

			VBox vbox = new VBox(mediaView);
			VBox.setMargin(mediaView, new Insets(10,10,10,10));
			vbox.setAlignment(Pos.BASELINE_CENTER);
			vbox.setPrefSize(800, 500);
			Scene scene = new Scene(vbox);

	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.DECORATED);;

	        stage.setScene(scene);
	        stage.show();
	        player.play();
		} catch (Exception e) {
			e.printStackTrace();
		} 
    }

    public static void showImage(String url) {
    	
		try {
			Image image = new Image(url);
	        ImageView imageView = new ImageView(image);
	        
			VBox vbox = new VBox(imageView);
			VBox.setMargin(imageView, new Insets(10,10,10,10));
			vbox.setAlignment(Pos.BASELINE_CENTER);
			
			vbox.setPrefSize(image.getWidth(), image.getHeight());
			Scene scene = new Scene(vbox);

	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.DECORATED);;

	        stage.setScene(scene);
	        stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		} 
    }
    
	public static File browseMediaFile() {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Media Files", "*.jpg", "*.png", "*.gif", "*.bmp", "*.mp4", "*.mov");
		fileChooser.getExtensionFilters().add(extFilter);
		return fileChooser.showOpenDialog(null);
	}
	
	public static byte[] loadImageData(File file) {
		Logger.getLogger().debug("Loading image from file " + file.getName());
		
		BufferedInputStream inputStream = null;
		byte[] imageData = null;
		try {
			inputStream = new BufferedInputStream(new FileInputStream(file));
			long length = file.length();
			imageData = new byte[(int) length];
			inputStream.read(imageData);
			inputStream.close();
		} catch (Exception e) {
			Logger.getLogger().error("Unable to load image from file. " + e.getMessage());
			MessageDialog.showError("Unable to read image. Please verify file format");
			imageData = null;
		} finally {
			if(inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
				}
			}
		}
		Logger.getLogger().debug("Finished loading image.");
		return imageData;
	}

}

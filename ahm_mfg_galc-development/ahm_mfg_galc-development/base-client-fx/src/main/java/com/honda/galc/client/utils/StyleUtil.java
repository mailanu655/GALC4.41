package com.honda.galc.client.utils;

import javafx.scene.image.ImageView;

/**
 * @author Subu Kathiresan
 * @date Jan 9, 2015
 */
public class StyleUtil {

	public static double cmdBtnWidth;
	public static double cmdBtnHeight;
	public static double cmdBtnImageWidth;
	public static double cmdBtnImageHeight;
	
	public static double getCmdBtnWidth() {
		return cmdBtnWidth;
	}

	public static void setCmdBtnWidth(double cmdBtnWidth) {
		StyleUtil.cmdBtnWidth = cmdBtnWidth;
	}
	
	public static double getCmdBtnHeight() {
		return cmdBtnHeight;
	}

	public static void setCmdBtnHeight(double cmdBtnHeight) {
		StyleUtil.cmdBtnHeight = cmdBtnHeight;
	}
	
	public static double getCmdBtnImageWidth() {
		return cmdBtnImageWidth;
	}

	public static void setCmdBtnImageWidth(double cmdBtnImageWidth) {
		StyleUtil.cmdBtnImageWidth = cmdBtnImageWidth;
	}
	
	public static double getCmdBtnImageHeight() {
		return cmdBtnImageHeight;
	}

	public static void setCmdBtnImageHeight(double cmdBtnImageHeight) {
		StyleUtil.cmdBtnImageHeight = cmdBtnImageHeight;
	}
	
	public static String getDeviceBoxStyle() {
		return "-fx-background-color: #b2c8d5; " +
			    "-fx-border-color: #2e8b57; " +
			    "-fx-border-width: 2px; " +
				"-fx-padding: 15; " +
				"-fx-border-radius: 5 5 5 5; " +
				"-fx-background-radius: 5 5 5 5; " +
				"-fx-effect: dropshadow(three-pass-box, derive(cadetblue, -20%), 10, 0, 4, 4);  " +
				"-fx-spacing: 10;";
	}
	
	public static String getStatusIndicatorStyle() {
		return "-fx-background-color: #fff5f5; " +
			    "-fx-border-color: #af8383; " +
			    "-fx-border-width: 2px; " +
				"-fx-padding: 15; " +
				"-fx-border-radius: 5 5 5 5; " +
				"-fx-background-radius: 5 5 5 5; " +
				"-fx-effect: dropshadow(three-pass-box, derive(cadetblue, -20%), 10, 0, 4, 4);  " +
				"-fx-spacing: 10;";
	}
	
	public static String getBtnStyle() {
		return getBtnStyle(20, "5 10 5 10");
	}
	
	public static String getBtnStyle(int fontSize, String padding) {
		return getBtnStyle(fontSize, padding, "0,1,4,5", "9,8,5,4");
	}
	
	public static String getDefaultBtnStyle(int fontSize, String padding) {
		return getDefaultBtnStyle(fontSize, padding, "0,1,4,5", "9,8,5,4");
	}
	public static String getBtnStyle(int fontSize, String padding, String insets, String radius) {
		return  "-fx-background-color: " +
				"linear-gradient(#686868 0%, #232723 25%, #373837 75%, #757575 100%)," +
				"linear-gradient(#020b02, #3a3a3a)," +
				"linear-gradient(#b9b9b9 0%, #c2c2c2 20%, #afafaf 80%, #c8c8c8 100%)," +
				"linear-gradient(#f5f5f5 0%, #dbdbdb 50%, #cacaca 51%, #d7d7d7 100%);" +
				"-fx-background-insets: " + insets + ";" +
				"-fx-background-radius: " + radius + ";" +
				"-fx-padding: " + padding + ";" +
				"-fx-font-family: 'Helvetica';" +
				"-fx-font-size: " + fontSize + "px;" +
				"-fx-font-weight: bold;" +
				"-fx-text-fill: #333333;" +
				"-fx-effect: dropshadow(three-pass-box , rgba(1,100,100,0.2) , 1, 0.0 , 0 , 1);";	
	}
	
	public static String getDefaultBtnStyle(int fontSize, String padding, String insets, String radius) {
		return  "-fx-background-insets: " + insets + ";" +
				"-fx-background-radius: " + radius + ";" +
				"-fx-padding: " + padding + ";" +
				"-fx-base:#c8c8c8;"+
				"-fx-font-family: 'Helvetica';" +
				"-fx-font-size: " + fontSize + "px;" +
				"-fx-font-weight: bold;" +
				"-fx-text-fill: #333333;" +
				"-fx-effect: dropshadow(three-pass-box , rgba(1,100,100,0.2) , 1, 0.0 , 0 , 1);";	
	}

	public static String getCompleteBtnStyle(int fontSize) {
		return  "-fx-background-color: darkslategray;" +
				"-fx-text-fill: white;" +
				"-fx-font-family: 'Helvetica';" +
				"-fx-font-size: " + fontSize + "px;" +
				"-fx-font-weight: bold;" +
				"-fx-effect: dropshadow(three-pass-box , rgba(1,100,100,0.2) , 1, 0.0 , 0 , 1);";	
		
	}
	
	public static ImageView normalizeImage(ImageView imageView, double normalizedImageSize) {
		if (imageView.getImage() != null && ( imageView.getImage().getWidth() != normalizedImageSize || imageView.getImage().getWidth() != normalizedImageSize)) {
			imageView.setPreserveRatio(true);
			imageView.setFitHeight(normalizedImageSize);
			imageView.setFitWidth(normalizedImageSize);
		}
		return imageView;
	}
}

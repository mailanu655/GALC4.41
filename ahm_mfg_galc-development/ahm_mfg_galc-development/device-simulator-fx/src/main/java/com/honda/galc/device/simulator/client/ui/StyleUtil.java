package com.honda.galc.device.simulator.client.ui;

/**
 * @author Subu Kathiresan
 * @date Jan 9, 2015
 */
public class StyleUtil {

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
		return  "-fx-background-color: " +
				"linear-gradient(#686868 0%, #232723 25%, #373837 75%, #757575 100%)," +
				"linear-gradient(#020b02, #3a3a3a)," +
				"linear-gradient(#b9b9b9 0%, #c2c2c2 20%, #afafaf 80%, #c8c8c8 100%)," +
				"linear-gradient(#f5f5f5 0%, #dbdbdb 50%, #cacaca 51%, #d7d7d7 100%);" +
				"-fx-background-insets: 0,1,4,5;" +
				"-fx-background-radius: 9,8,5,4;" +
				"-fx-padding: 15 30 15 30;" +
				"-fx-font-family: 'Helvetica';" +
				"-fx-font-size: 20px;" +
				"-fx-font-weight: bold;" +
				"-fx-text-fill: #333333;" +
				"-fx-effect: dropshadow(three-pass-box , rgba(1,100,100,0.2) , 1, 0.0 , 0 , 1);";	
	}
}

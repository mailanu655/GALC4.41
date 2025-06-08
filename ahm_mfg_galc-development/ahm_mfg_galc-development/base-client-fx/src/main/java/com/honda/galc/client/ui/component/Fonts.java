package com.honda.galc.client.ui.component;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * 
 * <h3>Fonts Class description</h3>
 * <p> Fonts description </p>
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
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Nov 22, 2010
 *
 *
 */
/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class Fonts {
	
	public static final Font DIALOG_BOLD_12 = DIALOG_BOLD(12); 
	public static final Font DIALOG_BOLD_14 = DIALOG_BOLD(14);
	public static final Font DIALOG_BOLD_15 = DIALOG_BOLD(15);
	public static final Font DIALOG_BOLD_16 = DIALOG_BOLD(16);
	public static final Font DIALOG_BOLD_20 = DIALOG_BOLD(20);
	public static final Font DIALOG_BOLD_22 = DIALOG_BOLD(22);
	public static final Font DIALOG_BOLD_24 = DIALOG_BOLD(24);
	public static final Font DIALOG_BOLD_26 = DIALOG_BOLD(26);

	public static final Font DIALOG_PLAIN_14 = DIALOG_PLAIN(14);
	public static final Font DIALOG_PLAIN_16 = DIALOG_PLAIN(16);
	public static final Font DIALOG_PLAIN_18 = DIALOG_PLAIN(18);
	public static final Font DIALOG_PLAIN_20 = DIALOG_PLAIN(20);
	public static final Font DIALOG_PLAIN_22 = DIALOG_PLAIN(22);
	public static final Font DIALOG_PLAIN_24 = DIALOG_PLAIN(24);	
	public static final Font DIALOG_PLAIN_26 = DIALOG_PLAIN(26);
	public static final Font DIALOG_PLAIN_28 = DIALOG_PLAIN(28);
	public static final Font DIALOG_PLAIN_30 = DIALOG_PLAIN(30);
	public static final Font DIALOG_PLAIN_33 = DIALOG_PLAIN(33);
	public static final Font DIALOG_PLAIN_36 = DIALOG_PLAIN(36);
	public static final Font DIALOG_PLAIN_45 = DIALOG_PLAIN(45);
	public static final Font DIALOG_PLAIN_50 = DIALOG_PLAIN(50);
	public static final Font DIALOG_PLAIN_60 = DIALOG_PLAIN(60);
	public static final Font DIALOG_PLAIN_70 = DIALOG_PLAIN(150);
	
	public static Font DIALOG_BOLD(int fontSize) {
		return Font.font("Dialog", FontWeight.BOLD, fontSize);
	}
	
	public static Font DIALOG_PLAIN(int fontSize) {
		return Font.font("Dialog", FontWeight.NORMAL, fontSize);
	}
	
	public static Font FONT_PLAIN(String fontType,int fontSize) {
		return Font.font(fontType, FontWeight.NORMAL, fontSize);
	}
	
	public static Font FONT_BOLD(String fontType,int fontSize) {
		return Font.font(fontType, FontWeight.BOLD, fontSize);
	}
	
	public static String SS_DIALOG_BOLD(int fontSize) {
		return SS_FONT_BOLD("Dialog", fontSize);
	}
	
	public static String SS_DIALOG_PLAIN(int fontSize) {
		return SS_FONT_PLAIN("Dialog", fontSize);
	}
	
	public static String SS_FONT_BOLD(String fontFamily,int fontSize) {
		return SS_FONT(fontFamily, FontWeight.BOLD, fontSize);
	}
	public static String SS_FONT_PLAIN(String fontFamily,int fontSize) {
		return SS_FONT(fontFamily, FontWeight.NORMAL, fontSize);
	}
	
	public static String SS_FONT(String fontFamily,FontWeight fontWeight,int fontSize) {
		StringBuilder builder = new StringBuilder();
		builder.append("-fx-font-family: ").append(fontFamily).append(";");
		builder.append("-fx-font-weight: ").append(fontWeight).append(";");
		builder.append("-fx-font-size: ").append(fontSize).append("px;");
		return builder.toString();
	}
	
}

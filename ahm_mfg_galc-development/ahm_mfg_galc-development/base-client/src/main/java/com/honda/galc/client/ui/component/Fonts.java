package com.honda.galc.client.ui.component;

import java.awt.Font;
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
	
	public static final Font DIALOG_BOLD_12 = new Font("Dialog", Font.BOLD, 12);
	public static final Font DIALOG_BOLD_14 = new Font("Dialog", Font.BOLD, 14);
	public static final Font DIALOG_BOLD_15 = new Font("Dialog", Font.BOLD, 15);
	public static final Font DIALOG_BOLD_16 = new Font("Dialog", Font.BOLD, 16);
	public static final Font DIALOG_BOLD_20 = new Font("Dialog", Font.BOLD, 20);
	public static final Font DIALOG_BOLD_22 = new Font("Dialog", Font.BOLD, 22);
	public static final Font DIALOG_BOLD_24 = new Font("Dialog", Font.BOLD, 24);
	public static final Font DIALOG_BOLD_26 = new Font("Dialog", Font.BOLD, 26);

	public static final Font DIALOG_PLAIN_14 = new Font("Dialog", Font.PLAIN, 14);
	public static final Font DIALOG_PLAIN_16 = new Font("Dialog", Font.PLAIN, 16);
	public static final Font DIALOG_PLAIN_18 = new Font("Dialog", Font.PLAIN, 18);
	public static final Font DIALOG_PLAIN_20 = new Font("Dialog", Font.PLAIN, 18);
	public static final Font DIALOG_PLAIN_22 = new Font("Dialog", Font.PLAIN, 22);
	public static final Font DIALOG_PLAIN_24 = new Font("Dialog", Font.PLAIN, 24);	
	public static final Font DIALOG_PLAIN_26 = new Font("Dialog", Font.PLAIN, 26);
	public static final Font DIALOG_PLAIN_28 = new Font("Dialog", Font.PLAIN, 28);
	public static final Font DIALOG_PLAIN_30 = new Font("Dialog", Font.PLAIN, 30);
	public static final Font DIALOG_PLAIN_33 = new Font("Dialog", Font.PLAIN, 33);
	public static final Font DIALOG_PLAIN_36 = new Font("Dialog", Font.PLAIN, 36);
	public static final Font DIALOG_PLAIN_45 = new Font("Dialog", Font.PLAIN, 45);
	public static final Font DIALOG_PLAIN_50 = new Font("Dialog", Font.PLAIN, 50);
	public static final Font DIALOG_PLAIN_60 = new Font("Dialog", Font.PLAIN, 60);
	public static final Font DIALOG_PLAIN_70 = new Font("Dialog", Font.PLAIN, 70);
	
	public static Font DIALOG_BOLD(int fontSize) {
		return new Font("Dialog", Font.BOLD, fontSize);
	}
	
	public static Font DIALOG_PLAIN(int fontSize) {
		return new Font("Dialog", Font.PLAIN, fontSize);
	}
	
	public static Font FONT_PLAIN(String fontType,int fontSize) {
		return new Font(fontType, Font.PLAIN, fontSize);
	}
	
	public static Font FONT_BOLD(String fontType,int fontSize) {
		return new Font(fontType, Font.BOLD, fontSize);
	}
	
}

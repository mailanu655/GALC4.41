package com.honda.galc.client.ui;

import static com.honda.galc.common.logging.Logger.getLogger;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import com.honda.galc.client.ui.component.FXOptionPane;
import com.honda.galc.client.utils.UiFactory;
/**
 * 
 * <h3>MessageDialog Class description</h3>
 * <p> MessageDialog description </p>
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
 * Apr 22, 2010
 *
 * @author Suriya Sena JavaFx Migration
 * Feb 19, 2014
 */

public class MessageDialog {
	
	   private static final int CHAR_COUNT = 50;
	   private static final int COLUMN = 80;
	   private static final int ROW = 30;
    	
	   
	   public static void showError(String message) {
		   showError(null,message);
	   }
	   
	   public static void showError(Stage parent,String message){
	        showError(parent,message,CHAR_COUNT);
	   }
	   
	   public static void showError(Stage parent,String message,String title){
	        showError(parent,message,title,CHAR_COUNT);
	   }
	   
	   public static void showError(Stage parent,String message,int countPerLine){
		   showError(parent,message,"Error Occured!",countPerLine);
	   }
	   
	   public static void showInfo(Stage parent,String message){
	        showInfo(parent,message,CHAR_COUNT);
	   }
	   public static void showInfo(Stage parent,String message,String title){
	        showInfo(parent,message,title,CHAR_COUNT);
	   }
	   
	   public static void showInfo(Stage parent,String message,int countPerLine){
		   showInfo(parent,message,"Information",countPerLine);
	   }
	   
	   public static void showError(Stage parent,String message,String title,int columnCount) {
		   FXOptionPane.showMessageDialog(parent,UiFactory.createLabel("showError", getFormattedText(message,columnCount)), title, FXOptionPane.Type.ERROR);
	   }
	   
	   public static void showInfo(Stage parent,String message,String title,int columnCount) {
		   FXOptionPane.showMessageDialog(parent,UiFactory.createLabel("showInfo", getFormattedText(message,columnCount)),title, FXOptionPane.Type.INFORMATION);
	   }
	   
	   public static void showScrollingInfo(Stage parent,String message){
		   showScrollingInfo(parent,message,"Information");
	   }
	   
	   public static void showScrollingInfo(Stage parent,String message,int column){
		   showScrollingInfo(parent,message,"Information",ROW,column);
	   }
	   
	   public static void showScrollingInfo(Stage parent,String message,String title){
		   showScrollingInfo(parent,message,title,ROW,COLUMN);
	   }
	   
	   public static void showScrollingInfo(Stage parent,String message,int row, int column){
		   showScrollingInfo(parent,message,"Information",row,column);
	   }
	   
	   
//	   public static String showInputDialog(String title,String label,int maxLength, boolean isUpperCase) {
//		    LengthFieldBean lengthFieldBean = ( isUpperCase ? new UpperCaseFieldBean() : new LengthFieldBean());
//		    lengthFieldBean.setMaximumLength(maxLength);
//		   
//		    // ensure lengthFieldBean has focus
//		    lengthFieldBean.addAncestorListener( new AncestorListener() {
//				
//				
//				public void ancestorRemoved(AncestorEvent event) {
//				}
//				
//				
//				public void ancestorMoved(AncestorEvent event) {
//				}
//				
//				
//				public void ancestorAdded(AncestorEvent event) {
//					JComponent component =  event.getComponent();
//					component.requestFocusInWindow();
//					component.removeAncestorListener(this);
//				}
//			});
//
//
//			Object[] ob = {new JLabel(label),lengthFieldBean}; 
//			
//			int result = JOptionPane.showConfirmDialog(null, ob, title, JOptionPane.OK_CANCEL_OPTION);
//			
//			return (result == JOptionPane.OK_OPTION ? lengthFieldBean.getText() : null);
//	   }
	   
	   
	   public static void showScrollingInfo(Stage parent,String message,String title,int row, int column) {
		   TextArea textArea = UiFactory.createTextArea();
		   textArea.setPrefColumnCount(column);
		   textArea.setPrefRowCount(row);
		   textArea.setText(message);
		   textArea.setEditable(false);
	      
		   FXOptionPane.showMessageDialog(parent, textArea,title,FXOptionPane.Type.INFORMATION);
	   }
	    
	    
	   private static String getFormattedText(String str, int count){
	        if(str == null) return "";
	        int length = Math.max(calculateCount(str),count);
	        String lineArray []  = str.split("\n");
	        StringBuilder buf = new StringBuilder();
	        for (String line : lineArray) {
	        	appendHtmlText(buf,line,length);
	        }  
	        return buf.toString();
	   }
	   
	   private static int calculateCount(String str) {
		   int count = 0;
		   String wordArray[] = str.split("[ \n]");
	       for (String word : wordArray) {
	    	   int len = word.length();
	    	   if(len > count) {
	    		   count = len;
	    	   }
	       }
	       
	       return count;
	   }
	   
	   private static void appendHtmlText(StringBuilder buffer, String str,int count) {
		   	String wordArray[] = str.split(" ");
		   	
	        int k = 0;
	        for(String word : wordArray ){
	            if(k + word.length() >= count && k > 0) {
	                buffer.append("\n");
	                k = 0;
	            }
	            buffer.append(word).append(" ");
	            k += word.length();
	        }
	        
	        if(k>0) {
	        	buffer.append("\n");
	        }
	   }
	   
	   
	   /**
	    * this confirm dialog will not have a default option
	    * button selected, which will force the user to 
	    * acknowledge the dialog and not dispose it
	    * using a scanner or by pressing the Enter key
	    * 
	    * @param frame
	    * @param message
	    * @return
	    */
	   public static boolean confirm(Stage parent, String message) {
		   return confirm(parent,message,true);
	   }
	   
	   
	   /**
	    * Option dialog which allows us to choose which option button
	    * has the focus when shown
	    * 
	    * @param parent
	    * @param message
	    * @param selectYes
	    * @return
	    */
	   public static boolean confirm(Stage parent, String message, boolean selectYes) {
		   getLogger().check(message);
		   FXOptionPane.Response response = FXOptionPane.showConfirmDialog(parent, message, "Confirmation",FXOptionPane.Type.QUESTION);
           getLogger().check(String.format("'%s' dialog was presented and the user chose '%s'", message,response.name()));
           return (response == FXOptionPane.Response.YES ? true : false);
	   }
	   /**
	    * Option dialog which allows us to choose which option button
	    * has the focus when shown
	    * 
	    * @param parent
	    * @param message
	    * @param selectYes
	    * @return
	    */
	   public static boolean confirmNoSelected(Stage parent, String message) {
		   getLogger().check(message);
		   FXOptionPane.Response response = FXOptionPane.showConfirmDialogWithNoSelected(parent, message, "Confirmation",FXOptionPane.Type.QUESTION);
           getLogger().check(String.format("'%s' dialog was presented and the user chose '%s'", message,response.name()));
           return (response == FXOptionPane.Response.YES ? true : false);
	   }
	   

	   
	   /**
		 * Option dialog which allows us to choose which option button has the focus
		 * when shown with provision to enter comments.
		 * 
		 * @param parent
		 * @param message
		 * @param commentName
		 * @param isCommentMandatory
		 * @return
		 */
		public static boolean confirmWithComment(Stage parent, String message, String commentName, boolean isCommentMandatory, String errorMessage) {
			getLogger().check(message);
			FXOptionPane.Response response = FXOptionPane.showConfirmDialogWithComment(parent, message, "Confirmation",
					FXOptionPane.Type.QUESTION, commentName, isCommentMandatory, errorMessage);
			getLogger().check(String.format("'%s' dialog was presented and the user chose '%s'", message, response.name()));
			return (response == FXOptionPane.Response.YES ? true : false);
		}
		public static boolean confirmWithOptionalComment(Stage parent, String message, String commentName, boolean isCommentMandatory, String errorMessage,boolean isScrapReasonRequired) {
			getLogger().check(message);
			FXOptionPane.Response response=null;
						
			if(isScrapReasonRequired) {
				response = FXOptionPane.showConfirmDialogWithComment(parent, message, "Confirmation",
						FXOptionPane.Type.QUESTION, commentName, isCommentMandatory, errorMessage);
			} else {
				response = FXOptionPane.showConfirmDialog(parent, message, "Confirmation",
						FXOptionPane.Type.QUESTION);
			}
			
			
			getLogger().check(String.format("'%s' dialog was presented and the user chose '%s'", message, response.name()));
			return (response == FXOptionPane.Response.YES ? true : false);
		}

		   public static String confirmWithCancel(Stage parent, String message, boolean selectYes) {
			   getLogger().check(message);
			   FXOptionPane.Response response = FXOptionPane.showConfirmDialog(parent, message, "Confirmation",FXOptionPane.Type.QUESTION);
	           getLogger().check(String.format("'%s' dialog was presented and the user chose '%s'", message,response.name()));
	           return response.name();
		   }

	
}

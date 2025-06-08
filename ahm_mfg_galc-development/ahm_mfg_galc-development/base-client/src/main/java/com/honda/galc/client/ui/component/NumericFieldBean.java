package com.honda.galc.client.ui.component;


import javax.swing.text.*;
/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * TextField to input numbers without decimal point.
 * <h4>Usage and Example</h4>
 * 
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Y.Kawada</TD>
 * <TD>(Nov/02/2001 10:04:35)</TD>
 * <TD>1.0.1</TD>
 * <TD>(none)</TD>
 * <TD>Modify Javadoc</TD>
 * </TR>
 * </TABLE>
 * @see LengthFieldBean
 * @author M.Satoh
 * @version 1.0
 */
/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class NumericFieldBean extends LengthFieldBean {
  /**
  * ???????????????????????????
  * @see PlainDocument
  */
  protected class NumericCaseDocument extends LengthControlDocument {
		private static final long serialVersionUID = 1L;
	/**
	* ????????????????????????
	* @param offs - ????????????????????????? >= 0??????????????????????????
	* @param str - ????????
	* @param a - ??????????????????????????? null
	* @exception BadLocationException - ???????????????????????????????
	*/
	public void insertString(int offs,String str,AttributeSet a) throws BadLocationException{
	  String oldString = getText(0,getLength());
	  String newString = oldString.substring(0,offs) + str + oldString.substring(offs);
	  try{
			for(int i = 0; i <= str.length() - 1; i++){
				if (Character.isDigit(str.charAt(i)) == false) return;
			}	
			super.insertString(offs,str,a);
	  }
	  catch(BadLocationException e){
	  }

	}
  }
  /**
  * ????????? NumericCaseDocument ???????
  * @return NumericCaseDocument ???????????????
  */
  protected Document createDefaultModel() {
		return new NumericCaseDocument();
  }
/**
 * Set only numeric text without decimal point.
 * 
 * 
 * @param t - String - new string to be set to the text field.
 * @exception NumberFormatException  - The specified string is not numeric.
 * @exception IndexOutOfBoundsException - There is some disintegrity withn the specified string.
 */

	public void setText(String t) {
		String text;
		text = t;
		try{
			for(int i = 0; i <= text.length() - 1; i++){
				if (Character.isDigit(text.charAt(i)) == false) return;
			}	
			super.setText(text);
		}
		catch(NumberFormatException ne){
		} 				
		catch(IndexOutOfBoundsException e){
		}
	}
}

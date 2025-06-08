package com.honda.galc.client.ui.component;

//import javax.swing.text.*;
import com.honda.galc.common.logging.Logger;

/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class UpperCaseFieldBean extends LengthFieldBean {

	private static final long serialVersionUID = 1L;
	
	public UpperCaseFieldBean(String id) {
		super(id);
		//textProperty().addListener(new ChangeListener<String>() {
	        //@Override 
	        //public void changed(ObservableValue observableValue, String oldValue, String newValue) {                
	            //System.out.println("Changed.");  
	        //	if (null != newValue) newValue = newValue.toUpperCase();
	        //}    
	    //});
		
	}
	
	public UpperCaseFieldBean(String id, ITextFieldRender fieldRender) {
		super(id, fieldRender);
	}

    @Override public void replaceText(int start, int end, String text) {
        // If the replaced text would end up being invalid, then simply
        // ignore this call!
        if (null!= text) {
            super.replaceText(start, end, text.toUpperCase());
        }
    }
 
    @Override public void replaceSelection(String text) {
        if (null!= text) {
            super.replaceSelection(text.toUpperCase());
        }
    }
/*	protected class UpperCaseDocument extends LengthControlDocument {

		private static final long serialVersionUID = 1L;

		public void insertString(int offs,String str,AttributeSet a) throws BadLocationException{
			try{
				super.insertString(offs,str.toUpperCase(),a);
			}
			catch(BadLocationException e){
				Logger.getLogger().warn(e, this.getClass().getSimpleName() + "::insertString() exception.");
			}
		}
	}
*/	
	//protected Document createDefaultModel() {
	//	return new UpperCaseDocument();
	//}
	
	/**
	 * Set the specified text to the text field, after converting lower case characters to upper case characters if the text contains lower case characters.
	 * @param t - new string to be set to the text field.  
	 * @exception IndexOutOfBoundsException - There is some disintegrity within the specified string.
	 */
	public void settext(String t) {
		String text;
		text = t == null ? null: t.toUpperCase();
		try{
			super.setText(text);

		}catch(IndexOutOfBoundsException e){
			Logger.getLogger().warn(e, this.getClass().getSimpleName() + "::setText() exception.");
		}
	}
}

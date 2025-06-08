package com.honda.galc.client.mvc;


/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ProcessView</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Jeffray Huang
 */
public interface IView <M extends IModel,C extends IController<?,?>> {
	
	public C getController();
	
	public M getModel();
	
	/**
     * Prepare the view by creating all visual components.
     * 
     * This method is called after Model initialization
     * 
     */
    void prepare();
    
    /**
     * Handle custom tasks to do the first time after creation of the view.
     * 
     * For example : you could start the show animation of the view.
     */
    void start();
    
    /**
     * Handle custom tasks to do at each rendering of the view (re-display).
     * 
     * For example : play from start the start animation.
     */
    void reload();
    
    /**
     * Handle custom tasks to do when the view is closed or hidden.
     * 
     * For example : you could start the hide animation of the view.
     */
    void hide();

}

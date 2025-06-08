package com.honda.galc.client.gts.view.action;


import javax.swing.AbstractAction;
import javax.swing.Action;

import com.honda.galc.client.gts.view.GtsDrawingView;

/**
 * 
 * 
 * <h3>EditLaneAction Class description</h3>
 * <p> EditLaneAction description </p>
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
 * Jun 10, 2015
 *
 *
 */
public class EditLaneAction extends AbstractAction{
   
    private static final long serialVersionUID = 1L;
    
    public static final int EDIT_NAME = 0;
    public static final int EDIT_FONT = 1;
    public static final int EDIT_TEXT_COLOR = 2;
    public static final int EDIT_FILL_COLOR = 3;
    private GtsDrawingView view;
    
    public EditLaneAction(GtsDrawingView view){
        this.view = view;
        this.putValue(Action.NAME, getName());
    }
    
    private String getName(){
        return "Edit Lanes";
    }

    public void actionPerformed(java.awt.event.ActionEvent e) {
        EditLaneDialog dialog = new EditLaneDialog(view);
        dialog.setVisible(true);
    }
}

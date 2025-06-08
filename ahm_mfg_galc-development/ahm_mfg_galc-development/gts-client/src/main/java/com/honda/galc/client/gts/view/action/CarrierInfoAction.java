package com.honda.galc.client.gts.view.action;


import javax.swing.AbstractAction;
import javax.swing.Action;
import org.jhotdraw.app.action.Actions;

import com.honda.galc.client.gts.view.GtsDrawing;
import com.honda.galc.entity.enumtype.GtsCarrierDisplayType;

/**
 * 
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>CarrierInfoAction</code> is ...
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TD>Jeffray Huang</TD>
 * <TD>Mar 16, 2008</TD>
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

public class CarrierInfoAction extends AbstractAction{
    
    
    private static final long serialVersionUID = 1L;
    
    private GtsDrawing drawing;
    private GtsCarrierDisplayType displayType;
    
    public CarrierInfoAction(GtsDrawing drawing,GtsCarrierDisplayType displayType){
        this.drawing= drawing;
        this.displayType = displayType;
        this.putValue(Action.NAME, displayType.name());
    }

    
    public CarrierInfoAction(GtsDrawing drawing,GtsCarrierDisplayType displayType,String submenuKey){
        this(drawing,displayType);
        if(submenuKey != null && submenuKey.length() != 0)
            this.putValue(Actions.SUBMENU_KEY, submenuKey);
	}

    public void actionPerformed(java.awt.event.ActionEvent e) {
        drawing.refreshCarriers(displayType);
	}
}

package com.honda.galc.client.gts.view;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import org.jhotdraw.draw.DelegationSelectionTool;

/**
 * 
 * 
 * <h3>GtsSelectionTool Class description</h3>
 * <p> GtsSelectionTool description </p>
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
 * Jun 17, 2015
 *
 *
 */
public class GtsSelectionTool extends DelegationSelectionTool {
    
    protected MouseEvent currentMouseEvent;
    
    /**
     * Hook method which can be overriden by subclasses to provide
     * specialised behaviour in the event of a popup trigger.
     */
    
    @Override
    protected void handlePopupMenu(MouseEvent evt) {
        
        this.currentMouseEvent = evt;
        
        super.handlePopupMenu(evt);
        
    }


    public MouseEvent getCurrentMouseEvent() {
        return currentMouseEvent;
    }

    public Point2D.Double getMousePoint(){
    	Point point = currentMouseEvent != null ?
    			currentMouseEvent.getPoint() : MouseInfo.getPointerInfo().getLocation();
         return this.viewToDrawing(point);
    }
    
    public void setCurrentMouseEvent(MouseEvent currentMouseEvent) {
        this.currentMouseEvent = currentMouseEvent;
    }
    
    
}

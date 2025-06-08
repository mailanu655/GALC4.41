package com.honda.galc.client.gts.view.action;


import javax.swing.AbstractAction;
import javax.swing.Action;

import com.honda.galc.client.gts.figure.ShapeFigure;
import com.honda.galc.client.gts.view.GtsDrawing;
import com.honda.galc.client.gts.view.GtsSelectionTool;
import com.honda.galc.entity.gts.GtsShape;

/**
 * 
 * 
 * <h3>CreateShapeAction Class description</h3>
 * <p> CreateShapeAction description </p>
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
public class CreateShapeAction extends AbstractAction{
    
    
    private static final long serialVersionUID = 1L;

    private GtsDrawing drawing;
    private GtsSelectionTool tool;

    public CreateShapeAction(GtsDrawing drawing,GtsSelectionTool tool){
        
        this.drawing = drawing;
        this.tool = tool;
        this.putValue(Action.NAME, getName());
    }
    
    private String getName(){
         return "Create new Shape";
    }
    
    
    
    public void actionPerformed(java.awt.event.ActionEvent e) {
        
    	GtsShape shape = drawing.getModel().createShape((int)tool.getMousePoint().getX(),(int)tool.getMousePoint().getY());
        if(shape == null) return;
        drawing.add(new ShapeFigure(shape));
        
    }
}

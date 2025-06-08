package com.honda.galc.client.gts.view.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import com.honda.galc.client.gts.figure.LabelFigure;
import com.honda.galc.client.gts.view.GtsDrawing;
import com.honda.galc.client.gts.view.GtsSelectionTool;
import com.honda.galc.entity.gts.GtsLabel;

/**
 * 
 * 
 * <h3>CreateLabelAction Class description</h3>
 * <p> CreateLabelAction description </p>
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
 * Jun 11, 2015
 *
 *
 */
public class CreateLabelAction extends AbstractAction{
   
    private static final long serialVersionUID = 1L;
 
    private GtsDrawing drawing;
    
    private GtsSelectionTool tool;
    
    public CreateLabelAction(GtsDrawing drawing,GtsSelectionTool tool){
        this.drawing = drawing;
        this.tool = tool;
        this.putValue(Action.NAME, getName());
//      this.setEnabled(false);
    }
    
    private String getName(){
         return "Create New Label";
    }
    
    
    
    public void actionPerformed(ActionEvent e) {
        
       	GtsLabel label = drawing.getModel().createLabel(
       			(int)tool.getMousePoint().getX(),(int)tool.getMousePoint().getY());
        if(label == null) return;

   //      drawing.add(new LabelFigure(label));
    }
  
   
}

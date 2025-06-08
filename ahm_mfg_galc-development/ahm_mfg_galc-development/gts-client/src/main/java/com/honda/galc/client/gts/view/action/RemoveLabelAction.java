package com.honda.galc.client.gts.view.action;


import javax.swing.AbstractAction;
import javax.swing.Action;

import com.honda.galc.client.gts.figure.LabelFigure;

/**
 * 
 * 
 * <h3>RemoveLabelAction Class description</h3>
 * <p> RemoveLabelAction description </p>
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
public class RemoveLabelAction extends AbstractAction{
    
    private static final long serialVersionUID = 1L;
    
    private LabelFigure figure;
    public RemoveLabelAction(LabelFigure figure){
        this.figure = figure;
        this.putValue(Action.NAME, getName());
    }
    
    private String getName(){
         return "Remove Label";
    }
    
    public void actionPerformed(java.awt.event.ActionEvent e) {
    	figure.getDrawing().getModel().removeLabel(figure.getLabel());
    }
    
}

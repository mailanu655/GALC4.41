package com.honda.galc.client.gts.figure;

import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.geom.AffineTransform;
import java.util.Collection;
import java.util.LinkedList;


import org.jhotdraw.draw.AbstractCompositeFigure;
import org.jhotdraw.draw.Handle;

import com.honda.galc.client.gts.view.GtsDrawing;
import com.honda.galc.client.gts.view.GtsDrawingView;
import com.honda.galc.client.gts.view.GtsTrackingController;
import com.honda.galc.client.gts.view.GtsTrackingModel;

/**
 * 
 * 
 * <h3>GtsAbstractFigure Class description</h3>
 * <p> GtsAbstractFigure description </p>
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
 * May 11, 2015
 *
 *
 */
public class GtsAbstractFigure extends AbstractCompositeFigure{

    
    private static final long serialVersionUID = 1L;

    public Collection<Handle> createHandles(int detailLevel) {
        if(GtsDrawing.isEditingMode()) 
            return super.createHandles(detailLevel); 
        else return new LinkedList<Handle>();
    }
    
    
    /**
     * Transforms the figure.
     */
    public void transform(AffineTransform tx) {

        if(GtsDrawing.isEditingMode()){
            double x = tx.getTranslateX();
            double y = tx.getTranslateY();
            super.transform(AffineTransform.getTranslateInstance(x, y));
        }
        
    }
    
    public GtsDrawing getDrawing(){
        return (GtsDrawing)super.getDrawing();
    }
    
    public GtsDrawingView getView(){
        Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().
                               getPermanentFocusOwner();
        if (focusOwner != null && focusOwner instanceof GtsDrawingView) {
                return (GtsDrawingView)focusOwner;
        }else return null;
    }
    
    public GtsTrackingController getController() {
    	return getDrawing().getController();
    }
    
    public GtsTrackingModel getModel() {
    	return getDrawing().getModel();
    }
}

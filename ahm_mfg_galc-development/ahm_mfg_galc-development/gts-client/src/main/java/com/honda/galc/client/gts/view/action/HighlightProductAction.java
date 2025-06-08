package com.honda.galc.client.gts.view.action;

import java.awt.Color;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JColorChooser;

import com.honda.galc.client.gts.figure.CarrierFigure;
import com.honda.galc.client.gts.view.GtsDrawing;
import com.honda.galc.client.gts.view.HighlightCondition;
import com.honda.galc.client.gts.view.HighlightCondition.Type;

public class HighlightProductAction extends AbstractAction{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/*
     * the position of the carrier to be added 
     * BEFORE,AFTER the selected carrier
     * HEAD,TAIL of the lane of the selected carrier
     */
    private CarrierFigure figure;
    private Type type;
    public HighlightProductAction(CarrierFigure figure,Type type){
        this.figure = figure;
        this.type = type;
        this.putValue(Action.NAME, getName());
//      this.setEnabled(false);
    }
    
    private String getName(){
        
        switch (type) {
            case TYPE_PROD_LOT:
                return "Highlight Production Lot";
            case TYPE_MTO:
                return "Highlight YMTO";
            case TYPE_NONE:
                return "Remove Highlighted Colors";
        }
        
        return "Highlight ???";
    }
    
    public void actionPerformed(java.awt.event.ActionEvent e) {
        
        if(type == Type.TYPE_NONE){
            removeHighlightedColors();
            return;
        }
        
        Color chosenColor = ColorChooser.selectColor(figure.getDrawing().getController().getWindow(), Color.GREEN);
        if (chosenColor != null) {
            changeAttribute(chosenColor);
        }
        
    }
    
    
    private void changeAttribute(Color color){
    	
        switch(type){
            case TYPE_PROD_LOT:
                CarrierFigure.addHightlightCondition(type, figure.getCarrier().getProductionLot(), color);
                break;
            case TYPE_MTO:
                CarrierFigure.addHightlightCondition(type, figure.getCarrier().getProductSpec(), color);
                break;
        }
        
        ((GtsDrawing)figure.getDrawing()).highlightCarriers();
    }
    
    private void removeHighlightedColors(){
        
        figure.removeHighlightedColors();
        
        ((GtsDrawing)figure.getDrawing()).highlightCarriers();
        
    }

}

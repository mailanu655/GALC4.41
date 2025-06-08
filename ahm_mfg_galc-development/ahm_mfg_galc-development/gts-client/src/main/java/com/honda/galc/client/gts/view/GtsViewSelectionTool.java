package com.honda.galc.client.gts.view;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.Tool;

import com.honda.galc.client.gts.figure.CarrierFigure;

public class GtsViewSelectionTool extends GtsSelectionTool implements ActionListener {
    
	private final static int clickInterval = (Integer)Toolkit.getDefaultToolkit().
		        getDesktopProperty("awt.multiClickInterval");
	
	MouseEvent lastEvent;
	Timer timer = new Timer(clickInterval/2,this);
	
    @Override
    public Tool createAreaTracker(){
        return new GtsAreaTracker();
    }
    
    @Override
    public Tool createDragTracker(Figure figure){
    	
    	if(figure instanceof CarrierFigure)
    		return new GtsCarrierFigureDragTracker(figure);
    	else return super.createDragTracker(figure);
    }
    
    public void mouseClicked(MouseEvent evt) {
    	if (evt.getClickCount() > 2) return;
    	lastEvent = evt;
    	if(evt.getClickCount() == 1) {
    		timer.restart();
    	}else {
    		timer.stop();
    		super.mouseClicked(evt);
    	}
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		timer.stop();
		//filter right click
		if(SwingUtilities.isRightMouseButton(lastEvent)) return;
		super.handleDoubleClick(lastEvent);
	}	   
}
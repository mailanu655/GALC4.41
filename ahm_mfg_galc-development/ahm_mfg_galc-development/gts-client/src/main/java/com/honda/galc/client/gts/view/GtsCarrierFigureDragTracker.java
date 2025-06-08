package com.honda.galc.client.gts.view;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.util.Collection;

import javax.swing.SwingUtilities;

import org.jhotdraw.draw.DragTracker;
import org.jhotdraw.draw.Figure;

import com.honda.galc.client.gts.figure.CarrierFigure;
import com.honda.galc.client.gts.figure.LaneSegmentFigure;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.entity.gts.GtsLane;
import com.honda.galc.util.KeyValue;

public class GtsCarrierFigureDragTracker extends DragTracker {
	
	private CarrierFigure carrierFigure;
	private boolean isDragging = false; 
	
    public GtsCarrierFigureDragTracker(Figure figure) {
		super(figure);
		this.carrierFigure = (CarrierFigure) figure;
	}

    
    //  implements java.awt.event.MouseMotionListener
    public void mouseDragged(MouseEvent e) {
    //	super.mouseDragged(e);
    
    	if(!SwingUtilities.isLeftMouseButton(e) || !getModel().isControllAllowed() ) return;
    	
    	if(getModel().isExitGateOpen(carrierFigure.getLane().getLaneName())) {
			MessageDialog.showError("Source Lane - " + carrierFigure.getLane().getLaneName() + " is open");
	    	return;
    	}
    	
    	Image image = this.carrierFigure.getImage();
    	Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(image, new Point(0,0), "carrier");
    	getView().setCursor(cursor);
    	isDragging = true;
     }

    //     implements java.awt.event.MouseListener
    public void mousePressed(MouseEvent e) {
    	
    	
    }

//     implements java.awt.event.MouseListener
    public void mouseReleased(MouseEvent e) {
    	
    	if(!isDragging || !SwingUtilities.isLeftMouseButton(e) ) return;
    	
    	KeyValue<GtsLane,Integer> destLane = getDestnationPosition(e.getPoint());
    	
    	
    	if(destLane != null) {
    		boolean flag = true;
        	if(getModel().isInManualMode() && Preference.isConfirmMessage()) {
    	
    			flag = MessageDialog.confirm(null, "Are you sure to move carrier " + carrierFigure.getCarrierNumber() + 
    				        " From lane " + carrierFigure.getLane().getLaneName() + " to lane " + destLane.getKey().getLaneName() + "?");
    	    }
    	
        	if(flag) {
        		String srcLaneId = carrierFigure.getCarrier().getId().getLaneId();
        		String laneName = destLane.getKey().getLaneName();
        		GtsLane lane = getModel().findLane(laneName);
        		
        		if(!lane.getLaneId().equalsIgnoreCase(srcLaneId) &&lane.isFull()) {
        			MessageDialog.showError("Cannot move the carrier - destionation lane " + laneName + " is full");
        			return;
        		}
        		
        		getModel().removeCarrierByUser(carrierFigure.getCarrier(), carrierFigure.getCarrier().getPosition() + 1);
        		getModel().addCarrierByUser(laneName,destLane.getValue(),carrierFigure.getCarrierNumber());
        	}
    	}
 		
    	getView().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
    
    KeyValue<GtsLane,Integer> getDestnationPosition(Point p) {
    	Figure fig = getView().findFigure(p);
    	if(fig != null && fig instanceof CarrierFigure) {

    		GtsLane lane = ((CarrierFigure)fig).getCarrier().getLane();
    		if(getModel().isExitGateOpen(lane.getLaneName())) {
				MessageDialog.showError("Destination Lane - " + lane.getLaneName() + " is open");
				return null;
    		}
    		int pos = ((CarrierFigure)fig).getCarrier().getPosition();
    		return new KeyValue<GtsLane,Integer>(lane,pos + 1);
    	}else {
    		Dimension cursorSize = Toolkit.getDefaultToolkit().getBestCursorSize(0, 0);
    		Rectangle rect = new Rectangle(cursorSize);
    		rect.translate(p.x, p.y + 10);
    	
    		Collection<Figure> figures = getView().findFigures(rect);
    		
    		LaneSegmentFigure laneSegmentFigure = findLaneSegmentFigure(figures);
    		
    		if(laneSegmentFigure != null) {
    			GtsLane lane = laneSegmentFigure.getLaneSegment().getLane();
    			if(getModel().isExitGateOpen(lane.getLaneName())) {
    				MessageDialog.showError("Destination Lane - " + lane.getLaneName() + " is open");
    				return null;
    			}else {
    				int size = lane.getLaneCarriers().size();
    				String srcLane = carrierFigure.getLane().getLaneName();
    				if(!lane.getLaneName().equalsIgnoreCase(srcLane) && lane.getLaneCapacity() == size) {
    					MessageDialog.showError("Destination Lane - " + lane.getLaneName() + " is full. Size = " + size);
    					return null;
    				}else {
    					if(lane.getLaneName().equalsIgnoreCase(srcLane)) size = size -1;
    					return new KeyValue<GtsLane,Integer>(lane,size + 1);
    				}
    			}
    		}
    	}
    	
    	return null;
    }
    
    
    
    
    
    private LaneSegmentFigure findLaneSegmentFigure(Collection<Figure> figures) {
    	for(Figure figure : figures) {
    		if(figure instanceof LaneSegmentFigure) return (LaneSegmentFigure)figure;
    	}
    	return null;
    }
    
    public GtsDrawingView getView() {
    	return (GtsDrawingView)super.getView();
    }
    
    public GtsTrackingModel getModel() {
    	return getView().getController().getModel();
    }
    
    public void mouseMoved(MouseEvent evt) {
     
        updateCursor(editor.findView((Container) evt.getSource()), new Point(evt.getX(), evt.getY()));
    }
}

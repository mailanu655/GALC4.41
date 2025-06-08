/*
 * @(#)BezierOutlineHandle.java  1.0  April 14, 2007
 *
 * Copyright (c) 2007 Werner Randelshofer
 * Staldenmattweg 2, CH-6405 Immensee, Switzerland
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Werner Randelshofer. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Werner Randelshofer.
 */

package com.honda.galc.client.gts.view.action;


import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import org.jhotdraw.draw.AbstractHandle;
import org.jhotdraw.draw.BezierFigure;
import org.jhotdraw.draw.LineDecoration;
import org.jhotdraw.geom.BezierPath;


/**
 * Draws the outlines of a BezierFigure to make adjustment easier.
 *
 * @author Werner Randelshofer
 * @version 1.0 April 14, 2007 Created.
 */
public class BezierDecorationHandle extends AbstractHandle {
    private final static Color HANDLE_COLOR = new Color(0x00a8ff);
    
    private LineDecoration decoration;
    // decoration positon 0 = start, 1 = end
    private int position = 0;
    /** Creates a new instance. */
    
    public BezierDecorationHandle(BezierFigure owner,LineDecoration deco) {
        super(owner);
        this.decoration = deco;
    }
    
    public BezierDecorationHandle(BezierFigure owner,LineDecoration deco,int position) {
        super(owner);
        this.decoration = deco;
        this.position = position;
    }
    
    public BezierFigure getOwner() {
        return (BezierFigure) super.getOwner();
    }
    
    protected Rectangle basicGetBounds() {
        return view.drawingToView(getOwner().getDrawingArea());
    }
    @Override public boolean contains(Point p) {
        return false;
    }
    
    public void trackStart(Point anchor, int modifiersEx) {
    }
    
    public void trackStep(Point anchor, Point lead, int modifiersEx) {
    }
    
    public void trackEnd(Point anchor, Point lead, int modifiersEx) {
    }
    
    @Override public void draw(Graphics2D g) {
        Point2D.Double p1,p2;
        BezierPath bounds = getOwner().getBezierPath();
        if (position == 0) {
            p1 = bounds.getPointOnPath(1.0, 1.0);
            p2 = bounds.getPointOnPath(0.95, 1.0);
        }else {
            p1 = bounds.getPointOnPath(0, 1.0);
            p2 = bounds.getPointOnPath(0.05, 1.0);
        } 
 
        AffineTransform tm = view.getDrawingToViewTransform();
        
        g.setColor(HANDLE_COLOR);
        
        decoration.draw(g, getOwner(), (Point2D.Double)tm.transform(p1, p1),
                                       (Point2D.Double)tm.transform(p2,p2));
     }
}

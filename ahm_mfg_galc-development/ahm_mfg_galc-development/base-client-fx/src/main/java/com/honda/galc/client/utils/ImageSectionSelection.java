package com.honda.galc.client.utils;

import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class ImageSectionSelection {

	Rectangle rectangle;
	public ImageSectionSelection(Rectangle rectangle) {
		this.rectangle = rectangle;
	}

	public boolean checkIntersects(Shape node) {
		boolean inside = false;
		if (node.intersects(rectangle.getX(), rectangle.getY(), 1, 1))
			inside = checkCoordinateInsidePolygon(node);
		return inside;
	}

	/**
	 * This method check clicked coordinate present inside Polygon or outside.
	 * @param node the Shape (Polygon)
	 * @return true if click inside Polygon else return false 
	 */
	private boolean checkCoordinateInsidePolygon(Shape node) {
		double rectX = getDoubleValue(rectangle.getX());
		double rectY = getDoubleValue(rectangle.getY());

		if (node.contains(rectX, rectY))
			return true;

		if (rectY > getDoubleValue(node.getLayoutBounds().getMaxY()))
			return false;
		
		if(isPointOutsidePolygon(node))
			return false;
		
		if(isPointInsidePolygon(node, true))
			return true;
		else
			return isPointInsidePolygon(node, false);

	}
	
	private boolean isPointOutsidePolygon(Shape node) {
		
		double minX = getDoubleValue(node.getLayoutBounds().getMinX());
		double maxX = getDoubleValue(node.getLayoutBounds().getMaxX());
		double maxY = getDoubleValue(node.getLayoutBounds().getMaxY());
		double minY = getDoubleValue(node.getLayoutBounds().getMinY());
		
		int inOutCounter = 0;
		double rectX = getDoubleValue(rectangle.getX());
		double rectY = getDoubleValue(rectangle.getY());
		for (double xVal = minX; xVal <= maxX; xVal++) {
			if (xVal == rectX) {
				if (inOutCounter == 0)
					return true;
			}
			if (node.contains(xVal, rectY))
				inOutCounter ++;
		}

		inOutCounter = 0;
		for (double xVal = maxX; xVal >= minX; xVal--) {
			if (xVal == rectX) {
				if (inOutCounter == 0)
					return true;
			}
			if (node.contains(xVal, rectY))
				inOutCounter ++;
		}

		inOutCounter = 0;
		for (double yVal = minY; yVal <= maxY; yVal++) {
			if (yVal == rectY) {
				if (inOutCounter == 0)
					return true;
			}
			if (node.contains(rectX, yVal))
				inOutCounter ++;
		}

		inOutCounter = 0;
		for (double yVal = maxY; yVal >= minY; yVal--) {
			if (yVal == rectY) {
			 	if (inOutCounter == 0)
					return true;
			}
			if (node.contains(rectX, yVal))
				inOutCounter ++;
		}
		return false;
	}

	private boolean isPointInsidePolygon(Shape node, boolean checkXPoint) {
		
		double rectX = getDoubleValue(rectangle.getX());
		double rectY = getDoubleValue(rectangle.getY());
		
		boolean shapeFound = false;
		boolean inEvenCount = false;
		boolean inOddCount = false;
		boolean onLine = false;
		int inOutCounter = 0;
		double minPoint = 0;
		double maxPoint = 0;
		
		if(checkXPoint) {
			minPoint = getDoubleValue(node.getLayoutBounds().getMinX());
			maxPoint = getDoubleValue(node.getLayoutBounds().getMaxX());
		}else {
			minPoint = getDoubleValue(node.getLayoutBounds().getMinY());
			maxPoint = getDoubleValue(node.getLayoutBounds().getMaxY());
		}
		
		for (double point = minPoint; point <= maxPoint; point++) {
			
			if (checkXPoint) {
				if (node.contains(point, rectY))
					onLine = true;
				else {
					if (onLine) {
						inOutCounter++;
						onLine = false;
					}
				}
				if (point == rectX) {
					shapeFound = true;
					if (inOutCounter % 2 == 0)
						inEvenCount = true;
					else
						inOddCount = true;
				}

			} else {

				if (node.contains(rectX, point))
					onLine = true;
				else {
					if (onLine) {
						inOutCounter++;
						onLine = false;
					}
				}
				if (point == rectY) {
					shapeFound = true;
					if (inOutCounter % 2 == 0)
						inEvenCount = true;
					else
						inOddCount = true;
				}
			}
		}

		if (shapeFound) {
			if (inOutCounter % 2 == 0) {
				if (inEvenCount)
					return false;
				else
					return true;
			} else {
				if (inOddCount)
					return true;
				else
					return false;
			}
		}
		return shapeFound;
	}
	
	private double getDoubleValue(double value) {
		long longVal = (long) value;
		return (double) longVal;
	}
}

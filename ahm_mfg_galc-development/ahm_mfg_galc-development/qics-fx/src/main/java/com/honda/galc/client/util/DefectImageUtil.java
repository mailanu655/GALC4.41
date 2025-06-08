package com.honda.galc.client.util;

import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import com.honda.galc.entity.enumtype.DefectStatus;


/**
 * <h3>Class DefectImageUtility</h3>
 * <p>
 * <code>DefectImageUtility</code>
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TR>
 * <TD>L&T Infotech</TD>
 * <TD>17/11/2016</TD>
 * <TD>1.0.0</TD>
 * <TD>(none)</TD>
 * <TD>Release 2</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.0
 * @author L&T Infotech
 */
public class DefectImageUtil {

	/**
	 * This method is used to draw Fixed/Not Fixed/Non-Reparable symbol
	 * 
	 * @param xPoint
	 * @param yPoint
	 * @param defectStatus
	 * @param colorCode
	 * @param partDefectCombDesc
	 * @param pane
	 */
	public static Pane drawSymbol(int xPoint, int yPoint, String defectStatus, String colorCode, String partDefectCombDesc, Pane pane) {
		Tooltip t = new Tooltip(partDefectCombDesc);
		if (defectStatus.equalsIgnoreCase(DefectStatus.FIXED.getName())) {
			drawFixedSymbol(xPoint, yPoint, colorCode, t, pane);
		} else {
			int x1 = xPoint + 3;
			int y1 = yPoint + 3;
			int x2 = xPoint - 3;
			int y2 = yPoint - 3;
			Line line1 = new Line();
			Line line2 = new Line();
			Line line3 = new Line();
			Line line4 = new Line();

			if (defectStatus.equalsIgnoreCase(DefectStatus.NON_REPAIRABLE.getName())) {
				drawNonRepairableSymbol(colorCode, t, x1, y1, x2, y2, line1, line2, line3, line4, pane);
			} else {
				drawNotFixedSymbol(colorCode, t, x1, y1, x2, y2, line1, line2, pane);
			}
		}
		return pane;

	}

	/**
	 * This method is used to draw fixed symbol
	 * 
	 * @param xPoint
	 * @param yPoint
	 * @param colorCode
	 * @param t
	 * @param pane
	 */
	private static void drawFixedSymbol(int xPoint, int yPoint, String colorCode, Tooltip t, Pane pane) {
		Circle circle = new Circle();
		circle.setRadius(4.5);
		circle.setLayoutX(xPoint);
		circle.setLayoutY(yPoint);
		if (null != colorCode)
			circle.setFill(Color.web(colorCode.trim()));

		Tooltip.install(circle, t);
		pane.getChildren().add(circle);
	}

	/**
	 * This method is used to draw non-reparable symbol
	 * 
	 * @param colorCode
	 * @param t
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param line1
	 * @param line2
	 * @param line3
	 * @param line4
	 * @param pane
	 */
	private static void drawNonRepairableSymbol(String colorCode, Tooltip t, int x1, int y1, int x2, int y2, Line line1,
			Line line2, Line line3, Line line4, Pane pane) {
		line1.setStartX(x1);
		line1.setStartY(y1);
		line1.setEndX(x2);
		line1.setEndY(y1);
		line1.setStrokeWidth(3);
		pane.getChildren().add(line1);

		line2.setStartX(x2);
		line2.setStartY(y1);
		line2.setEndX(x2);
		line2.setEndY(y2);
		line2.setStrokeWidth(3);
		pane.getChildren().add(line2);

		line3.setStartX(x2);
		line3.setStartY(y2);
		line3.setEndX(x1);
		line3.setEndY(y2);
		line3.setStrokeWidth(3);
		pane.getChildren().add(line3);

		line4.setStartX(x1);
		line4.setStartY(y2);
		line4.setEndX(x1);
		line4.setEndY(y1);
		line4.setStrokeWidth(3);
		pane.getChildren().add(line4);

		if (null != colorCode) {
			line1.setStroke(Color.web(colorCode.trim()));
			line2.setStroke(Color.web(colorCode.trim()));
			line3.setStroke(Color.web(colorCode.trim()));
			line4.setStroke(Color.web(colorCode.trim()));
		}
		Tooltip.install(line1, t);
		Tooltip.install(line2, t);
		Tooltip.install(line3, t);
		Tooltip.install(line4, t);
	}

	/**
	 * This method is used to draw Not Fixed Symbol
	 * 
	 * @param colorCode
	 * @param t
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param line1
	 * @param line2
	 * @param pane
	 */
	private static void drawNotFixedSymbol(String colorCode, Tooltip t, int x1, int y1, int x2, int y2, Line line1,
			Line line2, Pane pane) {
		line1.setStartX(x1);
		line1.setStartY(y1);
		line1.setEndX(x2);
		line1.setEndY(y2);
		line1.setStrokeWidth(3);
		pane.getChildren().add(line1);
		line2.setStartX(x2);
		line2.setStartY(y1);
		line2.setEndX(x1);
		line2.setEndY(y2);
		line2.setStrokeWidth(3);
		pane.getChildren().add(line2);
		if (null != colorCode) {
			line1.setStroke(Color.web(colorCode.trim()));
			line2.setStroke(Color.web(colorCode.trim()));
		}
		Tooltip.install(line1, t);
		Tooltip.install(line2, t);
	}
	
	/**
	 * This method is used to draw a symbol for defects in current session
	 * @param xPoint
	 * @param yPoint
	 * @param colorCode
	 * @param tooltip
	 * @param pane
	 */
	public static void drawCurrentSessionSymbol(int xPoint, int yPoint, String colorCode, String tooltip, Pane pane) {
		Arc arc = new Arc();
        arc.setCenterX(xPoint);
        arc.setCenterY(yPoint-5);
        arc.setRadiusX(10);
        arc.setRadiusY(10);
        arc.setLength(65);
        arc.setStartAngle(240);
        arc.setType(ArcType.ROUND);
        if (null != colorCode)
        	arc.setFill(Color.web(colorCode.trim()));
        
        Tooltip.install(arc, new Tooltip(tooltip));
		pane.getChildren().add(arc);
	}
}

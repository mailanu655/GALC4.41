package com.honda.galc.client.ui.component;

import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>FontSizeHandler</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 * @created Jun 18, 2013
 */
public class FontSizeHandler extends ComponentAdapter {

	private float heightFillFactor = 0.9f;
	private float maxBoldSize = 17;

	public FontSizeHandler() {
	}

	public FontSizeHandler(float heighFillFactor) {
		this.heightFillFactor = heighFillFactor;
	}

	@Override
	public void componentResized(ComponentEvent e) {

		if (!(e.getSource() instanceof Component)) {
			return;
		}
		Component comp = (Component) e.getSource();
		Font font = createFont(comp);
		comp.setFont(font);
	}

	public Font createFont(Component comp) {
		float height = comp.getHeight();
		float pointHeight = pixelToPoint(getHeightFillFactor() * height);
		Font font = comp.getFont().deriveFont(pointHeight);
		FontMetrics fm = comp.getGraphics().getFontMetrics(font);
		float ascent = fm.getAscent();
		float size = pixelToPoint(ascent);
		if (size > getMaxBoldSize() && !font.isPlain()) {
			font = font.deriveFont(Font.PLAIN);
		} else if (size <= getMaxBoldSize() && !font.isBold()) {
			font = font.deriveFont(Font.BOLD);
		}
		font = font.deriveFont(size);
		return font;
	}

	protected float pixelToPoint(float height) {
		float res = Toolkit.getDefaultToolkit().getScreenResolution();
		float size = 72f * height / res;
		return size;
	}

	public float getMaxBoldSize() {
		return maxBoldSize;
	}

	public void setMaxBoldSize(float maxBoldSize) {
		this.maxBoldSize = maxBoldSize;
	}

	public float getHeightFillFactor() {
		return heightFillFactor;
	}

	public void setHeightFillFactor(float heightFillFactor) {
		this.heightFillFactor = heightFillFactor;
	}
}

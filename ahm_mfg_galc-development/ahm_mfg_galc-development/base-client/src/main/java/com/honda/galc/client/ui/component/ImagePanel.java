package com.honda.galc.client.ui.component;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * <h3>Class description</h3>
 * Panel for displaying images
 * 
 * <h4>Description</h4>
 * <p>
 * </p>
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * <TR>
 * <TD>Dylan Yang</TD>
 * <TD>Sep. 12, 2017</TD>
 * <TD>1.0</TD>
 * <TD>20170912</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * @see 
 * @ver 1.0
 * @author Dylan Yang
 */
public class ImagePanel extends JPanel {
	private static final long serialVersionUID = -5290531413174548603L;

	protected BufferedImage image;
	protected int imageHeight = 0;
	protected int imageWidth = 0;
	protected int newHeight;
	protected int newWidth;
	protected int originX = 0;
	protected int originY = 0;
	
	public ImagePanel() {
		super();
	}
	
	public ImagePanel(BufferedImage image) {
		setImage(image);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		calculateSize();
		if(image != null) {
			g.drawImage(image, originX, originY, newWidth, newHeight, this);
		}
	}
	
	protected void calculateSize() {
		int panelHeight = getHeight();
		int panelWidth = getWidth();
		newHeight = panelHeight;
		newWidth = panelWidth;
		
		if(imageHeight == 0 || imageWidth == 0 || panelHeight == 0 || panelWidth == 0) {
			return;
		}
		
		if(((float) imageWidth / imageHeight) > ((float) panelWidth / panelHeight)) {
			newHeight = imageHeight * panelWidth / imageWidth;
			newWidth = panelWidth;
			originX = 0;
			originY = (panelHeight - newHeight) / 2;
		} else {
			newHeight = panelHeight;
			newWidth = imageWidth * panelHeight / imageHeight;
			originX = (panelWidth - newWidth) / 2;
			originY = 0;
		}
	}
	
	public void setImage(BufferedImage image) {
		this.image = image;
		if(image != null) {
			imageHeight = image.getHeight();
			imageWidth = image.getWidth();
		}
	}
	
	public void showImage(BufferedImage image) {
		setImage(image);
		repaint();
	}
}

package com.honda.galc.client.datacollection.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.honda.galc.client.datacollection.property.ImageViewPropertyBean;
import com.honda.galc.client.ui.component.ImagePanel;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.DataCollectionImageDao;
import com.honda.galc.entity.product.DataCollectionImage;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.service.ServiceFactory;

/**
 * <h3>Class description</h3>
 * Panel to display image and data collection items
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
 * <TD>Mar. 20, 2018</TD>
 * <TD>1.0</TD>
 * <TD>20180320</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 */
public class ImagePartViewPanel extends JPanel {
	private static final long serialVersionUID = -7509031846616013663L;
	
	private List<JPanel> snPanels;
	private List<JPanel> torquePanels;
	private JPanel dcPanel;
	private JScrollPane scrollPane;
	private ImageViewPropertyBean property;
	private ImagePanel imagePanel;
	private int torquesPerLine;
	private Logger logger = Logger.getLogger();
	private JPanel torqueLabelPanel = new JPanel();
	
	public ImagePartViewPanel() {
		super();
	}
	
	public ImagePartViewPanel(ImageViewPropertyBean property, List<JPanel> snPanels, List<JPanel> torquePanels) {
		this.property = property;
		this.snPanels = snPanels;
		this.torquePanels = torquePanels;
		initializeComponents();
	}
	
	private void initializeComponents() {
		torquesPerLine = property.getTorquesPerLine();
		setLayout(new BorderLayout());
		
		dcPanel =  new JPanel();
		dcPanel.setLayout(new GridBagLayout());

		scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getViewport().setView(dcPanel);
		scrollPane.setBorder(null);
		scrollPane.setPreferredSize(new Dimension(property.getTorqueFieldWidth(), property.getTorqueFieldHeight() + 25));

		add(scrollPane, BorderLayout.NORTH);
		add(getImagePanel(), BorderLayout.CENTER);
		setViewPosition(0, 10);
	}
	
	
	public void setViewPosition(int x, int y) {
		scrollPane.getViewport().setViewPosition(new Point(x, y));
	}
	
	public void showPanel(JPanel panel) {
		int y = panel.getY();
		setViewPosition(0, y >= 10 ? y - 10 : 0);
		repaint();
	}
	
	public void configureDcPanel(List<LotControlRule> lotControlRules) {
		logger.debug("Start to configure ImagePartView panel.");
		LotControlRule rule;
		int y = 0;
		int x = 0;
		int torqueSeq = 0;
		GridBagLayout layout = (GridBagLayout) dcPanel.getLayout();
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(20, 5, 30, 55);
		c.gridwidth = torquesPerLine;
		for (int i = 0; ((i < property.getMaxNumberOfPart()) && (i < lotControlRules.size())); i++) {
			rule = lotControlRules.get(i);
			if (rule != null && (rule.getSerialNumberScanFlag() == 1 || rule.isDateScan())) {
				c.gridy = y++;
				c.gridx = 0;
				addPanel(snPanels.get(i), layout, c);
			}
		}
			
		c.gridwidth = 1;
		c.insets = new Insets(10, 15, 10, 5);
		c.anchor = GridBagConstraints.WEST;
		
		c.gridx = x++;
		c.gridy = y;
		
		addPanel(this.torqueLabelPanel, layout, c);
		
		for (int i = 0; ((i < property.getMaxNumberOfPart()) && (i < lotControlRules.size())); i++) {
			rule = lotControlRules.get(i);
			for (int j = 0; j < rule.getParts().get(0).getMeasurementCount(); j++) {
				c.gridx = x++;
				c.gridy = y;
				addPanel(torquePanels.get(torqueSeq++), layout, c);
				if(x >= torquesPerLine) {
					x = 0;
					y++;
				}
			}
		}
		logger.debug("Finished to configure ImagePartView panel.");
	}
	
	protected JPanel createTorqueLabelPanel(String labelText) {
		JLabel seqLabel = new JLabel(labelText);
		seqLabel.setFont(new java.awt.Font("dialog", java.awt.Font.BOLD, 16));
		this.torqueLabelPanel.removeAll();
		this.torqueLabelPanel.add(seqLabel);
		return this.torqueLabelPanel;
	}
	
	public void showImage(int imageId) {
		BufferedImage image = null;
		if(imageId > 0) {
			logger.info("Preparing to show image: " + imageId);
			DataCollectionImage dcImage = ServiceFactory.getDao(DataCollectionImageDao.class).findByKey(imageId);
			if(dcImage != null) {
				try {
					image = ImageIO.read(new ByteArrayInputStream(dcImage.getImageData()));
				} catch (IOException e) {
					logger.error("Unable to read image. imageId = " + imageId);
				}
			}
		}
		getImagePanel().showImage(image);
	}

	protected ImagePanel getImagePanel() {
		if(imagePanel == null) {
			imagePanel = new ImagePanel();
		}
		return imagePanel;
	}
	
	private void addPanel(JPanel panel, GridBagLayout layout, GridBagConstraints c) {
		layout.setConstraints(panel, c);
		dcPanel.add(panel);
		panel.setVisible(true);
	}
	
	public void reset() {
		for(Component comp : dcPanel.getComponents()) {
			dcPanel.remove(comp);
		}
		showImage(0);
		repaint();
	}
}

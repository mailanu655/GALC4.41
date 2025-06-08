package com.honda.galc.client.teamleader.qics.image;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Collections;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.EtchedBorder;

import com.honda.galc.client.teamleader.qics.screen.QicsMaintenanceTabbedPanel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.ImageBean;
import com.honda.galc.client.ui.component.PropertyComboBoxRenderer;
import com.honda.galc.client.utils.ComboBoxUtils;
import com.honda.galc.entity.qics.Image;
import com.honda.galc.util.PropertyComparator;

/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>AbstractImagePanel</code> is ...
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
 * <TD>Karol Wozniak</TD>
 * <TD>Dec 19, 2008</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */
public abstract class AbstractImagePanel extends QicsMaintenanceTabbedPanel {

	private static final long serialVersionUID = 1L;

	private ImageBean image;

	private JLabel imagesLabel;
	private JComboBox imageComboBox;
	private JPanel imagesPanel;
	private ImageModel imageModel;
	private JButton newImageButton;
	
	public AbstractImagePanel(String screenName, int keyEvent) {
		super(screenName, keyEvent);
	}
	
	protected void init() {
		setLayout(null);

		setImageModel(new ImageModel());
		// === create ui fragments === //
		createComponents();
		mapActions();
		mapListeners();

		// == init data === //
		initData();
	}

	protected void createComponents() {
		this.image = createImageBean();
		this.imagesLabel = createImagesLabel();
		this.imageComboBox = createImageComboBox();
		this.newImageButton =  createNewImageButton();
		this.imagesPanel = createImagesPanel();
		add(getImageBean());
		add(getImagesPanel());
	}

	protected void initData() {
		reloadImages();
	}

	public void reloadImages() {
		List<Image> images = getController().selectImages();
		Collections.sort(images, new PropertyComparator<Image>(Image.class, "imageName"));
		getClientModel().setImages(images);
		ComboBoxUtils.loadComboBox(getImageComboBox(), getClientModel().getImages());
	}

	// ============= factory methods for ui components
	protected JLabel createImagesLabel() {
		JLabel component = createLabel("Images");
		return component;
	}

	protected JLabel createLabel(String text) {
		JLabel component = new JLabel(text, JLabel.RIGHT);
		component.setFont(Fonts.DIALOG_PLAIN_20);
		return component;
	}

	protected JPanel createImagesPanel() {
		JPanel panel = new JPanel(new BorderLayout(20,0));
		Component base = getImageBean();
		panel.setSize(495, 50);
		panel.setLocation(base.getX() + base.getWidth() + 5, base.getY());
		panel.setBorder(new EtchedBorder());

		getImagesLabel().setSize(100, panel.getHeight() - 10);

		panel.add(getImagesLabel(),BorderLayout.WEST);
		panel.add(getImageComboBox(),BorderLayout.CENTER);
		panel.add(getNewImageButton(),BorderLayout.EAST);

		return panel;
	}

	protected JComboBox createImageComboBox() {
		JComboBox comboBox = new JComboBox();
		ListCellRenderer tableCellRenderer = new PropertyComboBoxRenderer<Image>(Image.class, "imageName");
		comboBox.setRenderer(tableCellRenderer);
		comboBox.setFont(Fonts.DIALOG_PLAIN_20);
		return comboBox;
	}


	protected ImageBean createImageBean() {
		ImageBean image = new ImageBean();
		image.setBorder(new EtchedBorder());
		image.setSize(500, 530);
		image.setLocation(getLeftMargin(), getTopMargin() + 10);
		return image;
	}
	
	protected JButton createNewImageButton() {
		JButton button = new JButton("New");
		button.setEnabled(true);
		button.setFont(Fonts.DIALOG_PLAIN_20);
		return button;
	}
	
	protected Image loadImageData(Image image) {
		if(image.getImageData() != null) return image;
		Image newImage = getController().selectImage(image.getImageName());
		ComboBoxUtils.loadComboBox(getImageComboBox(), getClientModel().getImages());
		getImageComboBox().setSelectedItem(image);
		return newImage;
	}

	
	protected int getLeftMargin() {
		return 10;
	}
	
	protected int getTopMargin() {
		return 10;
	}
	
	protected JLabel getImagesLabel() {
		return imagesLabel;
	}

	public ImageBean getImageBean() {
		return image;
	}

	public JPanel getImagesPanel() {
		return imagesPanel;
	}

	public JComboBox getImageComboBox() {
		return imageComboBox;
	}
	
	public JButton getNewImageButton() {
		return newImageButton;
	}
	

	// === action/handlers mappings === //
	protected void mapActions() {

	}

	protected void mapListeners() {
		getImageComboBox().addActionListener(this);
	}

	// === controlling api === //
	public void setButtonsState() {
	}

	public void setImageModel(ImageModel imageModel) {
		this.imageModel = imageModel;
	}

	public ImageModel getImageModel() {
		return imageModel;
	}
	
}

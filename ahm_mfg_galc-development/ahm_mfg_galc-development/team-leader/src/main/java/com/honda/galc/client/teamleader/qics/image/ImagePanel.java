package com.honda.galc.client.teamleader.qics.image;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qics.frame.QicsMaintenanceFrame;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LimitedLengthPlainDocument;
import com.honda.galc.client.ui.component.UpperCaseDocument;
import com.honda.galc.entity.qics.DefectGroup;
import com.honda.galc.entity.qics.Image;

/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>ImagePanel</code> is ...
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
public class ImagePanel extends AbstractImagePanel {

	private static final long serialVersionUID = 1L;

	private JPanel imageFormPanel;

	private JLabel imageNameLabel;
	private JLabel imageFileNameLabel;
	private JLabel descriptionLabel;
	private JTextField imageNameTextField;
	private JTextField imageFileNameTextField;
	private JTextField descriptionTextField;

	private JButton deleteImageButton;
	private JButton updateImageButton;
	
	
	private Integer defaultWidthValue=500;
	private Integer defaultHeightValue=500;
	
	private boolean isCreate = false;
	private boolean isUpdate = false;
	
	public ImagePanel(QicsMaintenanceFrame mainWindow) {
		super("Image",KeyEvent.VK_I);
		setMainWindow(mainWindow);
	}

	@Override
	protected void createComponents() {
		super.createComponents();

		deleteImageButton = createDeleteImageButton();
		updateImageButton = createUpdateImageButton();
		
		imageNameTextField = createImageNameTextField();
		imageFileNameTextField = createImageFileNameTextField();
		imageNameLabel = createImageNameLabel();
		imageFileNameLabel = createImageFileNameLabel();

		descriptionLabel = createDescriptionLabel();
		descriptionTextField = createDescriptionTextField();

		imageFormPanel = createImageFormPanel();

		add(getImageFormPanel());
	}

	// ============= factory methods for ui components

	protected JTextField createImageNameTextField() {
		JTextField field = new JTextField();
		field.setFont(Fonts.DIALOG_BOLD_16);
		field.setDocument(new UpperCaseDocument(getMaxImageNameLength()));
		return field;
	}

	protected JTextField createImageFileNameTextField() {
		JTextField field = new JTextField();
		field.setFont(Fonts.DIALOG_BOLD_16);
		field.setEditable(false);
		field.setDocument(new LimitedLengthPlainDocument(getMaxImageFileNameLength()));
		return field;
	}

	protected JPanel createImageFormPanel() {
		JPanel panel = new JPanel();
		panel.setSize(495, 240);
		int inputElementRows = 3;
		Component base = getImagesPanel();
		panel.setLocation(base.getX(), base.getY() + base.getHeight() + 210);
		panel.setBorder(new EtchedBorder());
		// panel.setLayout(new GridLayout(2, 1, 5, 5));
		panel.setLayout(null);

		int width = panel.getWidth() - 10;
		int height = panel.getHeight() / (inputElementRows + 1) - 7;

		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weighty = 1;
		c.fill = 1;
		inputPanel.add(getImageNameLabel(), c);

		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.weighty = 1;
		c.weightx = 1;
		c.fill = 1;
		c.insets = new Insets(0, 5, 2, 2);
		inputPanel.add(getImageNameTextField(), c);

		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weighty = 1;
		c.fill = 1;
		inputPanel.add(getDescriptionLabel(), c);

		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.weighty = 1;
		c.weightx = 1;
		c.fill = 1;
		c.insets = new Insets(2, 5, 0, 2);
		inputPanel.add(getDescriptionTextField(), c);

		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weighty = 1;
		c.fill = 1;
		inputPanel.add(getImageFileNameLabel(), c);

		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.weighty = 1;
		c.weightx = 1;
		c.fill = 1;
		c.insets = new Insets(2, 5, 0, 2);
		inputPanel.add(getImageFileNameTextField(), c);

		inputPanel.setSize(width, height * 3);
		inputPanel.setLocation(5, 10);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 3, 5, 5));
		buttonPanel.add(getDeleteImageButton());
		buttonPanel.add(getUpdateImageButton());
		buttonPanel.setSize(width, height);
		buttonPanel.setLocation(inputPanel.getX(), inputPanel.getY() + inputPanel.getHeight() + 10);

		panel.add(inputPanel);
		panel.add(buttonPanel);
		return panel;
	}

	protected JLabel createImageNameLabel() {
		JLabel component = createLabel("Image Name");
		component.setFont(Fonts.DIALOG_BOLD_16);
		return component;
	}

	protected JLabel createImageFileNameLabel() {
		JLabel component = new JLabel("File Name", JLabel.CENTER);
		component.setFont(Fonts.DIALOG_BOLD_16);
		component.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		return component;
	}

	protected JLabel createDescriptionLabel() {
		JLabel component = createLabel("Description");
		return component;
	}

	protected JTextField createDescriptionTextField() {
		JTextField field = new JTextField();
		field.setFont(Fonts.DIALOG_BOLD_16);
		field.setDocument(new LimitedLengthPlainDocument(getMaxDescriptionLength()));
		return field;
	}

	protected JButton createDeleteImageButton() {
		JButton button = new JButton("Delete");
		button.setEnabled(false);
		button.setFont(Fonts.DIALOG_PLAIN_20);
		return button;
	}
	


	protected JButton createUpdateImageButton() {
		JButton button = new JButton("Update");
		button.setEnabled(false);
		button.setFont(Fonts.DIALOG_PLAIN_20);
		return button;
	}

	protected JButton createCreateImageButton() {
		JButton button = new JButton("Create");
		button.setEnabled(false);
		button.setFont(Fonts.DIALOG_PLAIN_20);
		return button;
	}

	// === action/handlers mappings === //
	protected void mapActions() {
		getDeleteImageButton().addActionListener(this);
		getUpdateImageButton().addActionListener(this);
		getNewImageButton().addActionListener(this);
	}

	@Override
	protected void mapListeners() {

		getImageComboBox().addActionListener(this);

		getImageFileNameLabel().addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if(getImageFileNameLabel().isEnabled())
				selectImageFile();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				getMainWindow().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				getImageFileNameLabel().setBorder(new EtchedBorder());
				getImageFileNameLabel().setBackground(Color.LIGHT_GRAY);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				getMainWindow().setDefaultCursor();
				getImageFileNameLabel().setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
			}
		});

		getImageNameTextField().addKeyListener(new KeyAdapter(){
				public void keyReleased(KeyEvent e) {
					if(!isCreate) isUpdate = true;
					setButtonsState();
				}
			}
		);
		
		getDescriptionTextField().addKeyListener(new KeyAdapter(){
			public void keyReleased(KeyEvent e) {
				if(!isCreate) isUpdate = true;
				setButtonsState();
			}
		}
	);
	}

	// === controlling api === //
	public void setButtonsState() {
		
		getUpdateImageButton().setText(isCreate ? "Create":"Update");
		getUpdateImageButton().setEnabled(isCreate || isUpdate);
		getDeleteImageButton().setEnabled(!isCreate);
	}

	// === get/set === //
	public JButton getDeleteImageButton() {
		return deleteImageButton;
	}

	public JButton getUpdateImageButton() {
		return updateImageButton;
	}

	public JTextField getImageNameTextField() {
		return imageNameTextField;
	}

	public JTextField getImageFileNameTextField() {
		return imageFileNameTextField;
	}

	public JLabel getImageFileNameLabel() {
		return imageFileNameLabel;
	}

	public JLabel getImageNameLabel() {
		return imageNameLabel;
	}

	public JPanel getImageFormPanel() {
		return imageFormPanel;
	}

	protected int getMaxImageFileNameLength() {
		return 255;
	}

	protected int getMaxImageNameLength() {
		return 20;
	}

	protected int getMaxImageFileSize() {
		return 1024 * 1024;
	}

	protected int getMaxDescriptionLength() {
		return 30;
	}

	public JTextField getDescriptionTextField() {
		return descriptionTextField;
	}

	public JLabel getDescriptionLabel() {
		return descriptionLabel;
	}

	@Override
	public void deselected(ListSelectionModel model) {
		
	}

	@Override
	public void selected(ListSelectionModel model) {
		
	}

	@Override
	public void onTabSelected() {
		if(!isInitialized){
			init();
			isInitialized = true;
		}
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == getImageComboBox()) imageSelected();
		if(e.getSource() == getDeleteImageButton()) deleteImage();
		if(e.getSource() == getUpdateImageButton()) saveImage();
		if(e.getSource() == getNewImageButton()) newImage();
	}
	
	private void imageSelected() {
		setErrorMessage("");
		Image image = (Image) getImageComboBox().getSelectedItem();
		getImageBean().clearImage();
		if (image == null) return;
		Image newImage = loadImageData(image);

		getImageModel().setFormImage(newImage);
		getImageNameTextField().setText(newImage.getImageName());
		getDescriptionTextField().setText(newImage.getImageDescriptionLong());
		getImageFileNameTextField().setText(newImage.getBitmapFileName());
		
		isCreate = false;
		isUpdate = false;
		setButtonsState();
		try {
			getImageBean().loadImage(newImage.getImageData());
		} catch (Exception ex) {
			setErrorMessage("Failed to load image: " + newImage.getImageName());
		}
	}
	
	private void deleteImage() {
		Image image = (Image)getImageComboBox().getSelectedItem();
		if (image == null) return;

		List<DefectGroup> defectGroups = getController().selectDefectGroups(image.getImageName());
		if (defectGroups.size() > 0) {
			JOptionPane.showMessageDialog(getMainWindow(), "This image is associated with Defect Groups. Please disassociate image before it can be deleted:");
			return;
		}

		if (getImageModel().getImageSections().size() > 0) {
			JOptionPane.showMessageDialog(getMainWindow(), "Please remove all sections before this image can be deleted:" + image.getImageName());
			return;
		}

		if (getImageModel().isModelUpdated()) {
			JOptionPane.showMessageDialog(getMainWindow(), "Please save image sections before continue with this action.");
			return;
		}

		int returnCode = JOptionPane.showConfirmDialog(getMainWindow(), "Are you sure you want to delete this image ?", "Delete Image", JOptionPane.YES_NO_OPTION);
		if (returnCode != JOptionPane.YES_OPTION) 	return;

		getController().deleteImage(image);
		reloadImages();
	}
	
	private void saveImage() {
		Image image = getImageModel().getFormImage();
		if (image == null) return;

		String imageName = getImageNameTextField().getText();
		String description = getDescriptionTextField().getText();
		String fileName = getImageFileNameTextField().getText();

		if (StringUtils.isEmpty(imageName)) {
			JOptionPane.showMessageDialog(getMainWindow(), "Image Name can not be empty");
			return;
		}

		if (StringUtils.isEmpty(fileName)) {
			JOptionPane.showMessageDialog(getMainWindow(), "You must select an image before");
			return;
		}
		
		if (imageNameExists(image.getBitmapFileName(),imageName)) return;

		image=scale(image);
		image.setImageName(imageName);
		image.setImageDescriptionLong(description);

		Image newImage = getController().updateImage(image);
		reloadImages();
		isCreate = false;
		isUpdate = false;
		setButtonsState();
		if (newImage != null) {
			getImageComboBox().setSelectedItem(newImage);
			JOptionPane.showMessageDialog(getMainWindow(), "Image " + image.getImageName() + " saved successfully");
		}	
	}
	
	private boolean bitmapFileNameExists(String bitmapFileName) {
		for (Image i : getClientModel().getImages()) {
			if(i.getBitmapFileName().equalsIgnoreCase(bitmapFileName)) 
				return true;
		}
		return false;
	}
	
	private boolean imageNameExists(String bitmapFileName,String imageName) {
		for (Image i : getClientModel().getImages()) {
			if(i.getImageName().equalsIgnoreCase(imageName) && !i.getBitmapFileName().equalsIgnoreCase(bitmapFileName)){
				JOptionPane.showMessageDialog(getMainWindow(), "Duplicate Image Name:" + imageName);
				return true;
			}
		}
		return false;
	}
	
	private void selectImageFile() {

		// TODO remove init path
		JFileChooser fc = new JFileChooser("");
		fc.addChoosableFileFilter(new JpgFilter());
		fc.setAcceptAllFileFilterUsed(false);

		int returnVal = fc.showDialog(getMainWindow(), "Select");

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			String fileName = file.getName();
			
			Image image = new Image();
			image.setBitmapFileName(file.getName());

			try {
				BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
				long length = file.length();
				byte[] bytes = new byte[(int) length];

				in.read(bytes);

				image.setImageData(bytes);
				image=scale(image);
				getImageFileNameTextField().setText(image.getBitmapFileName());

				in.close();
				getImageModel().setFormImage(image);

				getImageBean().loadImage(image.getImageData());
				setFormState();
				
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "Failed to load file: " + fileName, "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	public Image scale(Image imageToScale) {
		try {
			ByteArrayInputStream byteInput = new ByteArrayInputStream(imageToScale.getImageData());
			ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();

			BufferedImage inputBufferedImage = ImageIO.read(byteInput);
			Integer defaultWidth = getMainWindow().getApplicationProperty("DEFAULT_IMAGE_WIDTH")!=null?Integer.parseInt(getMainWindow().getApplicationProperty("DEFAULT_IMAGE_WIDTH")):defaultWidthValue;
			Integer defaultHeight = getMainWindow().getApplicationProperty("DEFAULT_IMAGE_HEIGHT")!=null?Integer.parseInt(getMainWindow().getApplicationProperty("DEFAULT_IMAGE_HEIGHT")):defaultHeightValue;
			
			java.awt.Image scaledImage = inputBufferedImage.getScaledInstance(defaultWidth,defaultHeight, java.awt.Image.SCALE_SMOOTH);
			BufferedImage outputBufferedImage = new BufferedImage(defaultWidth,defaultHeight, BufferedImage.TYPE_INT_RGB);
			outputBufferedImage.getGraphics().drawImage(scaledImage, 0, 0,new Color(0, 0, 0), null);

			ImageIO.write(outputBufferedImage, "jpg", byteOutput);
			imageToScale.setImageData(byteOutput.toByteArray());			

		} catch (Exception e) {
             getLogger().error("An error Occurred scaling the image");
             e.printStackTrace();
		}
		return imageToScale;
		
	}
	
	private void newImage() {
		isCreate = true;
		getImageBean().clearImage();
		clearInputs();		
		setFormState();
		getImageModel().setFormImage(new Image());
	}
	
	private void clearInputs(){
		getImageNameTextField().setText("");
		getDescriptionTextField().setText("");	
		getImageFileNameTextField().setText("");		
	}
	
	private void setFormState(){
		isUpdate = !isCreate;
		getUpdateImageButton().setText(isCreate?"Update":"Create");
		getUpdateImageButton().setEnabled(true);
		setButtonsState();
	}

}

package com.honda.galc.client.teamleader;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.ImagePanel;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.DataCollectionImageDao;
import com.honda.galc.dao.product.PartSpecDao;
import com.honda.galc.entity.product.DataCollectionImage;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.ExtensionFileFilter;

/**
 * <h3>Class description</h3>
 * Data collection image maintenance panel
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
public class DataCollectionImageMaintenancePanel extends TabbedPanel implements ListSelectionListener {
	private static final long serialVersionUID = 3641365159066130931L;
	
	private static final String[] IMAGE_LIST_HEADINGS = {"Image ID", "Image Name", "Image Description", "Active"};
	private static final String[] IMAGE_LIST_GETTERS = {"getId", "getImageName", "getImageDescription", "isActive"};
	private static final int MAX_IMAGE_SIZE = 5000000;
	
	private ImagePanel imagePanel;
	private JPanel rightPanel;
	private ObjectTablePane<DataCollectionImage> imageList;
	private JTextField filenameTextField;
	private JTextField nameTextField;
	private JTextField descriptionTextField;
	private JTextArea informationTextArea;
	private JCheckBox activeCheckBox;
	private JButton filenameButton;
	private JButton newButton;
	private JButton updateButton;
	private JButton deleteButton;
	private boolean addMode = false;
	private Logger logger = Logger.getLogger();
	
	public ImagePanel getImagePanel() {
		return imagePanel;
	}

	public void setImagePanel(ImagePanel imagePanel) {
		this.imagePanel = imagePanel;
	}

	public JPanel getRightPanel() {
		return rightPanel;
	}

	public void setRightPanel(JPanel rightPanel) {
		this.rightPanel = rightPanel;
	}

	public DataCollectionImageMaintenancePanel(TabbedMainWindow mainWindow) {
		super("DCImageMaintenance", KeyEvent.VK_G, mainWindow);
		init();
	}
	
	public void init() {
		initComponents();
		loadImages();
		refreshScreen();
	}
	
	protected void initComponents() {
		setLayout(new BorderLayout());
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setContinuousLayout(true);
		splitPane.setResizeWeight(1.0);
		splitPane.setDividerLocation(1.0);

		splitPane.setLeftComponent(createImagePanel());
		splitPane.setRightComponent(createRightPanel());
		
		add(splitPane, BorderLayout.CENTER);
		addListeners();
	}
	
	protected void loadImages() {
		List<DataCollectionImage> images = ServiceFactory.getDao(DataCollectionImageDao.class).findAllWithoutImageData();
		imageList.getItems().addAll(images);
	}

	protected ImagePanel createImagePanel() {
		imagePanel = new ImagePanel();
		return imagePanel;
	}
	
	protected JPanel createRightPanel() {
		rightPanel = new JPanel();
		rightPanel.setLayout(new GridLayout(2, 1));
		rightPanel.add(createImageListPane());
		rightPanel.add(createInfoPanel());
		return rightPanel;
	}
	
	private JPanel createInfoPanel() {
		
		JPanel infoPanel = new JPanel();
		Font textFont = new java.awt.Font("dialog", 0, 18);
		
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		infoPanel.setLayout(layout);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 5, 5, 5);

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		JLabel nameLabel = new JLabel("Image Name"); 
		nameLabel.setFont(textFont);
		addGridBagComponent(infoPanel, nameLabel, layout, c);
	
		c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		nameTextField = new JTextField(); 
		nameTextField.setFont(textFont);
		nameTextField.setPreferredSize(new Dimension(300, 35));
		addGridBagComponent(infoPanel, nameTextField, layout, c);
		
		// Image name
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		JLabel descriptionLabel = new JLabel("Description");
		descriptionLabel.setFont(textFont);
		addGridBagComponent(infoPanel, descriptionLabel, layout, c);

		c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		descriptionTextField = new JTextField();
		descriptionTextField.setFont(textFont);
		descriptionTextField.setPreferredSize(new Dimension(300, 35));
		addGridBagComponent(infoPanel, descriptionTextField, layout, c);
		
		// Image description
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		JLabel informationLabel = new JLabel("Information");
		informationLabel.setFont(textFont);
		addGridBagComponent(infoPanel, informationLabel, layout, c);

		c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 3;
		informationTextArea = new JTextArea();
		informationTextArea.setFont(textFont);
		informationTextArea.setLineWrap(true);
		informationTextArea.setPreferredSize(new Dimension(300, 80));
		JScrollPane scrollPane = new JScrollPane(informationTextArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		addGridBagComponent(infoPanel, scrollPane, layout, c);
		
		// Image information
		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 1;
		c.gridheight = 1;
		filenameButton = new JButton("File Name");
		filenameButton.setFont(textFont);
		addGridBagComponent(infoPanel, filenameButton, layout, c);

		c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		filenameTextField = new JTextField();
		filenameTextField.setFont(textFont);
		filenameTextField.setPreferredSize(new Dimension(300, 35));
		addGridBagComponent(infoPanel, filenameTextField, layout, c);

		// Active CheckBox
		c.gridx = 0;
		c.gridy = 6;
		c.gridwidth = 1;
		
		activeCheckBox = new JCheckBox("Active");
		activeCheckBox.setFont(textFont);
		addGridBagComponent(infoPanel, activeCheckBox, layout, c);
		
		// New button
		c.gridx = 1;
		c.gridwidth = 2;
		newButton = new JButton("New");
		newButton.setFont(new java.awt.Font("dialog", 0, 20));
		newButton.setPreferredSize(new Dimension(110,40));
		addGridBagComponent(infoPanel, newButton, layout, c);
		
		// Update button
		c.gridx = 3;
		updateButton = new JButton("Update");
		updateButton.setFont(new java.awt.Font("dialog", 0, 20));
		updateButton.setPreferredSize(new Dimension(110, 40));
		addGridBagComponent(infoPanel, updateButton, layout, c);

		// Delete button
		c.gridx = 5;
		deleteButton = new JButton("Delete");
		deleteButton.setFont(new java.awt.Font("dialog", 0, 20));
		deleteButton.setPreferredSize(new Dimension(110, 40));
		addGridBagComponent(infoPanel, deleteButton, layout, c);
		
		return infoPanel;
	}
	
	protected void addGridBagComponent(JPanel panel, JComponent comp, GridBagLayout layout, GridBagConstraints c) {
		JComponent component = comp == null ? new JLabel("") : comp;
		layout.setConstraints(component, c);
		panel.add(component);
	}
	
	private ObjectTablePane<DataCollectionImage> createImageListPane() {
		imageList = new ObjectTablePane<DataCollectionImage>();
		ColumnMappings columnMappings = ColumnMappings.with(IMAGE_LIST_HEADINGS, IMAGE_LIST_GETTERS);
		imageList = new ObjectTablePane<DataCollectionImage> ("Image", columnMappings.get(), false, true);
		imageList.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);	
		
		return imageList;
	}
	
	private void addListeners() {
		imageList.addListSelectionListener(this);
		filenameButton.addActionListener(this);
		deleteButton.addActionListener(this);
		newButton.addActionListener(this);
		updateButton.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent event) {
		if(event.getSource().equals(newButton)) {
			newButtonAction();
		} else if(event.getSource().equals(deleteButton)) {
			deleteButtonAction();
		} else if(event.getSource().equals(filenameButton)) {
			filenameButtonAction();
		} else if(event.getSource().equals(updateButton)) {
			updateButtonAction();
		}
	}
	
	private void newButtonAction() {
		imageList.clearSelection();
		addMode = true;
		refreshScreen();
		activeCheckBox.setSelected(true);
	}
	
	private void showImage(byte[] imageData) {
		if(imageData == null) {
			imagePanel.showImage(null);
		} else {
			try {
				imagePanel.showImage(ImageIO.read(new ByteArrayInputStream(imageData)));
			} catch (IOException e) {
				logger.error("Unable to load image");
				e.printStackTrace();
			}
		}
	}
	
	private byte[] loadImageData(String filename) {
		logger.debug("Loading image from file " + filename);
		if(StringUtils.isEmpty(filename)) { 
			MessageDialog.showError("Please enter a valid file name");
			return null;
		}
		
		BufferedInputStream inputStream = null;
		File file = new File(filename);
		byte[] imageData = null;
		try {
			inputStream = new BufferedInputStream(new FileInputStream(file));
			long length = file.length();
			imageData = new byte[(int) length];
			inputStream.read(imageData);
			inputStream.close();
		} catch (Exception e) {
			logger.error("Unable to load image from file. " + e.getMessage());
			MessageDialog.showError("Unable to read image. Please verify file format");
			imageData = null;
		} finally {
			if(inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
				}
			}
		}
		logger.debug("Finished loading image.");
		return imageData == null ? null : scale(imageData, filename.toLowerCase().endsWith(".bmp"));
	}
	
	private void filenameButtonAction() {
		JFileChooser chooser = new JFileChooser();
	    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	    chooser.setCurrentDirectory(new File("/"));
	    chooser.setFileFilter(new ExtensionFileFilter("jpg", "png", "gif", "bmp"));
	    chooser.setDialogTitle("Please select image file");
	    int returnVal = chooser.showOpenDialog(this);
        if(returnVal != JFileChooser.APPROVE_OPTION) {
        	return;
        }
		String filename = chooser.getSelectedFile().getAbsolutePath();
		if(filename != null) {
			filenameTextField.setText(filename);
		}
	}

	private void updateButtonAction() {
		if(addMode) {
			if(!saveImage()) {
				return;
			}
			addMode = false;
			refreshScreen();
		} else {
			DataCollectionImage image = imageList.getSelectedItem();
			if(image != null) {
				String filename = StringUtils.trimToEmpty(filenameTextField.getText());
				if(!StringUtils.isEmpty(filename)) {
					byte[] imageData = loadImageData(filename);
					if(imageData != null) {
						image.setImageData(imageData);
					} else {
						MessageDialog.showError("Unable to load image from file " + filename);
						return;
					}
				}

				image.setImageName(StringUtils.trimToEmpty(nameTextField.getText()));
				image.setImageDescription(StringUtils.trimToEmpty(descriptionTextField.getText()));
				image.setInformation(StringUtils.trimToEmpty(informationTextArea.getText()));
				image.setActive(activeCheckBox.isSelected());
				ServiceFactory.getDao(DataCollectionImageDao.class).update(image);
				logger.info(String.format("Image %s was updated", image.toString()));
				showImageAndInfo(image);
				imageList.repaint();
			}
		}
	}
	
	private boolean saveImage() {
		boolean result = false;
		logger.debug("Saving image...");
		String filename = StringUtils.trimToEmpty(filenameTextField.getText());
		String imageName = StringUtils.trimToEmpty(nameTextField.getText());
		if(StringUtils.isEmpty(filename)) {
			MessageDialog.showInfo(this, "Please enter or select a file name");
			return result;
		} else if(StringUtils.isEmpty(imageName)) {
			MessageDialog.showInfo(this, "Please enter the image name");
			return result;
		}
		
		byte[] imageData = loadImageData(filename);
		if(imageData != null) {
			DataCollectionImage image = new DataCollectionImage();
			image.setImageName(imageName);
			image.setImageDescription(StringUtils.trimToEmpty(descriptionTextField.getText()));
			image.setInformation(StringUtils.trimToEmpty(informationTextArea.getText()));
			image.setImageData(imageData);
			image.setActive(activeCheckBox.isSelected());
			DataCollectionImage newImage = ServiceFactory.getDao(DataCollectionImageDao.class).save(image);
			logger.info(String.format("Image %s was saved", image.toString()));
			if(newImage != null) {
				imageList.getItems().add(newImage);
				imageList.refresh();
				imageList.select(newImage);
				imageList.scrollToBottom();
				showImageAndInfo(newImage);
				result = true;
			} else {
				MessageDialog.showError("Unable to save image to datbase. Check log for more details");
			}
		} else {
			logger.error("Unable to load image from file " + filename);
			MessageDialog.showError("Unable to load image from file " + filename);
		}
		return result;
	}
	
	@Override
	public void onTabSelected() {
	}

	public void valueChanged(ListSelectionEvent event) {
		DataCollectionImage image = imageList.getSelectedItem();
		if(image != null && image.getImageData() == null) {
			DataCollectionImage tempImage = ServiceFactory.getDao(DataCollectionImageDao.class).findByKey(image.getImageId());
			image.setInformation(tempImage.getInformation());
			image.setImageData(tempImage.getImageData());
		}
		refreshScreen();
	}
	
	private void showImageAndInfo(DataCollectionImage image) {
		if(image == null) {
			nameTextField.setText("");
			descriptionTextField.setText("");
			informationTextArea.setText("");
			activeCheckBox.setSelected(false);
			showImage(null);
		} else {
			nameTextField.setText(image.getImageName());
			descriptionTextField.setText(image.getImageDescription());
			informationTextArea.setText(image.getInformation());
			activeCheckBox.setSelected(image.isActive());
			filenameTextField.setText("");
			showImage(image.getImageData());
		}
	}
	
	private void deleteButtonAction() {
		if(addMode) {	//Cancel adding new image
			addMode = false;
			refreshScreen();
			return;
		} 
		
		DataCollectionImage image = imageList.getSelectedItem();
		if(image != null) {
			long number = ServiceFactory.getDao(PartSpecDao.class).findNumberOfPartsByImageId(image.getImageId());
			if(number > 0) {
				MessageDialog.showError(String.format("This image is used by %5d parts. It cannot be deleted", number));
				return;
			}
			
			if(MessageDialog.confirm(getMainWindow(), "Are you sure to delete this image from database?")) {
				ServiceFactory.getDao(DataCollectionImageDao.class).remove(image);
				imageList.getItems().remove(image);
				imageList.clearSelection();
				imageList.repaint();
				imageList.refresh();
				logger.info(String.format("Image %s was deleted.", image.toString()));
			}
		}
	}
	
	private void refreshScreen() {
		if(addMode) {
			updateButton.setText("Save");
			deleteButton.setText("Cancel");
		} else {
			updateButton.setText("Update");
			deleteButton.setText("Delete");
		}

		DataCollectionImage image = imageList.getSelectedItem();
		refreshWidgets(image != null || addMode);
		showImageAndInfo(image);
	}
	
	private void refreshWidgets(boolean enable) {
		nameTextField.setEnabled(enable);
		descriptionTextField.setEnabled(enable);
		informationTextArea.setEnabled(enable);
		filenameButton.setEnabled(enable);
		filenameTextField.setEnabled(enable);
		updateButton.setEnabled(enable);
		deleteButton.setEnabled(enable);
		activeCheckBox.setEnabled(enable);
		imageList.repaint();
	}
	
	public byte[] scale(byte[] imageData, boolean isBmp) {
		byte[] result = null;
		result = isBmp ? scaleImage(imageData, 1) : imageData;
		
		while(result.length > MAX_IMAGE_SIZE) {
			result = scaleImage(result, 0.9);
		}
		return result;
	} 
	
	public byte[] scaleImage(byte[] imageData, double ratio) {
		byte[] result = null;
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData);
			BufferedImage inputBufferedImage = ImageIO.read(inputStream);
			inputStream.close();

			int newWidth = (int) (inputBufferedImage.getWidth() * ratio);
			int newHeight = (int) (inputBufferedImage.getHeight() * ratio);
			
			Image newImage = inputBufferedImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			BufferedImage outputBufferedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
			outputBufferedImage.getGraphics().drawImage(newImage, 0, 0, new Color(0, 0, 0), null);
			ImageIO.write(outputBufferedImage, "jpg", outputStream);
			result = outputStream.toByteArray();
			outputStream.close();

		} catch (Exception e) {
             logger.error("Unable to rescale the image");
             e.printStackTrace();
		}
		return result;
	}
}

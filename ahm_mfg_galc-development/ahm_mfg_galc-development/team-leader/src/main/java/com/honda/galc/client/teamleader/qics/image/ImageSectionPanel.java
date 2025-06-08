package com.honda.galc.client.teamleader.qics.image;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;

import com.honda.galc.client.teamleader.qics.frame.QicsMaintenanceFrame;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.ImageBean;
import com.honda.galc.client.ui.component.PropertyComboBoxRenderer;
import com.honda.galc.client.ui.component.ImageBean.PartPolygon;
import com.honda.galc.client.utils.ComboBoxUtils;
import com.honda.galc.entity.qics.Image;
import com.honda.galc.entity.qics.ImageSection;
import com.honda.galc.entity.qics.ImageSectionPoint;
import com.honda.galc.entity.qics.InspectionPartDescription;
import com.honda.galc.entity.qics.PartGroup;
import com.honda.galc.util.PropertyComparator;

/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>ImageSectionPanel</code> is ...
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
 * <TD>Sep 15, 2008</TD>
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
public class ImageSectionPanel extends AbstractImagePanel {

	private static final long serialVersionUID = 1L;
	
	private long imagesSelectedTime;

	private JButton clearButton;
	private JButton drawButton;

	private JLabel partGroupLabel;
	private JLabel partLabel;
	private JLabel partLocationLabel;
	private JComboBox partGroupComboBox;
	private JComboBox partComboBox;
	private JComboBox partLocationComboBox;

	private JPanel selectionPanel;

	private JButton updateButton;
	private JButton removeButton;
	private JButton saveButton;

	public ImageSectionPanel(QicsMaintenanceFrame mainWindow) {
		super("Image Section",KeyEvent.VK_S);
		setMainWindow(mainWindow);
	}

	@Override
	protected void createComponents() {

		super.createComponents();
		partGroupLabel = createPartGroupLabel();
		partLabel = createPartLabel();
		partLocationLabel = createPartLocationLabel();

		partGroupComboBox = createPartGroup();
		partComboBox = createPart();
		partLocationComboBox = createPartLocation();

		updateButton = createUpdateButton();
		removeButton = createRemoveButton();
		saveButton = createSaveButton();

		selectionPanel = createSelectionPanel();

		clearButton = createClearButton();
		drawButton = createDrawButton();

		add(getClearButton());
		add(getDrawButton());
		add(getSelectionPanel());

	}

	@Override
	protected void initData() {
		super.initData();

		List<PartGroup> partGroups = getClientModel().getPartGroups();
		ComboBoxUtils.loadComboBox(getPartGroup(), partGroups);

	}
	
	@Override
	public void onTabSelected() {
		if(!isInitialized){
			init();
			isInitialized = true;
		}
			
		if (getClientModel().isImagesUpdated(getImagesSelectedTime())) {
			Image selected = (Image) getImageComboBox().getSelectedItem();
			List<Image> images = new ArrayList<Image>(getClientModel().getImages());
			Collections.sort(images, new PropertyComparator<Image>(Image.class, "imageName"));
			ComboBoxUtils.loadComboBox(getImageComboBox(), getClientModel().getImages());
			setImagesSelectedTime(getClientModel().getImagesUpdateTime());
			if (selected != null) {
				getImageComboBox().setSelectedItem(selected);
			}
		}
	}

	// ============= factory methods for ui components

	protected JLabel createPartGroupLabel() {
		JLabel component = createLabel("Part Group");
		return component;
	}

	protected JLabel createPartLabel() {
		JLabel component = createLabel("Part");
		return component;
	}

	protected JLabel createPartLocationLabel() {
		JLabel component = createLabel("Location");
		return component;
	}

	protected JPanel createSelectionPanel() {

		Component base = getImagesPanel();
		JPanel panel = new JPanel();
		panel.setSize(450, 210);
		int rowCount = 3;
		int elementHeight = panel.getHeight() / (rowCount + 1);

		panel.setLocation(base.getX() + 45, base.getY() + base.getHeight() + 240);

		panel.setBorder(new EtchedBorder());
		panel.setLayout(null);
		JPanel labelPanel = new JPanel();
		labelPanel.setLayout(new GridLayout(rowCount, 1, 5, 5));
		labelPanel.add(getPartGroupLabel());
		labelPanel.add(getPartLabel());
		labelPanel.add(getPartLocationLabel());
		labelPanel.setSize(100, elementHeight * rowCount - 20);
		labelPanel.setLocation(5, 10);

		JPanel comboPanel = new JPanel();
		comboPanel.setLayout(new GridLayout(rowCount, 1, 5, 5));
		comboPanel.add(getPartGroup());
		comboPanel.add(getPart());
		comboPanel.add(getPartLocation());
		comboPanel.setSize(panel.getWidth() - labelPanel.getWidth() - 20, labelPanel.getHeight());
		comboPanel.setLocation(labelPanel.getX() + labelPanel.getWidth() + 10, labelPanel.getY());

		JPanel actionButtonsPanel = new JPanel();
		actionButtonsPanel.setLayout(new GridLayout(1, 3, 5, 5));
		actionButtonsPanel.add(getUpdateButton());
		actionButtonsPanel.add(getRemoveButton());
		actionButtonsPanel.add(getSaveButton());
		actionButtonsPanel.setSize(comboPanel.getX() + comboPanel.getWidth() - labelPanel.getX(), elementHeight - 10);
		actionButtonsPanel.setLocation(labelPanel.getX(), comboPanel.getY() + comboPanel.getHeight() + 10);
		panel.add(labelPanel);
		panel.add(comboPanel);
		panel.add(actionButtonsPanel);
		return panel;
	}

	protected JComboBox createPartGroup() {
		JComboBox comboBox = new JComboBox();
		ListCellRenderer tableCellRenderer = new PropertyComboBoxRenderer<PartGroup>(PartGroup.class, "partGroupName");
		comboBox.setRenderer(tableCellRenderer);
		comboBox.setFont(Fonts.DIALOG_PLAIN_20);
		return comboBox;
	}

	protected JComboBox createPart() {
		JComboBox comboBox = new JComboBox();
		comboBox.setFont(Fonts.DIALOG_PLAIN_20);
		return comboBox;
	}

	protected JComboBox createPartLocation() {
		JComboBox comboBox = new JComboBox();
		ListCellRenderer tableCellRenderer = new PropertyComboBoxRenderer<InspectionPartDescription>(InspectionPartDescription.class, "inspectionPartLocationName");
		comboBox.setRenderer(tableCellRenderer);
		comboBox.setFont(Fonts.DIALOG_PLAIN_20);
		return comboBox;
	}

	protected ImageBean createImageBean() {
		ImageBean image = new ImageBean();
		image.setBorder(new EtchedBorder());
		image.setSize(500, 500);
		image.setLocation(getLeftMargin(), getTopMargin() + 10);
		return image;
	}

	protected JButton createClearButton() {
		JButton button = new JButton();
		button.setRolloverIcon(null);
		button.setText("");
		button.setIcon(new ImageIcon(getClass().getResource("/resource/com/honda/galc/client/qics/view/pointer.gif")));
		button.setSize(40, 40);
		Component base = getImageBean();
		button.setLocation(base.getX() + base.getWidth() + 5, getSelectionPanel().getY());
		button.setEnabled(false);
		button.setToolTipText("Clear");
		return button;
	}

	protected JButton createDrawButton() {
		JButton button = new JButton();
		button.setRolloverIcon(null);
		button.setText("");
		button.setIcon(new ImageIcon(getClass().getResource("/resource/com/honda/galc/client/qics/view/shape.gif")));
		button.setSize(40, 40);
		Component base = getClearButton();
		button.setLocation(base.getX(), base.getY() + base.getHeight() + 5);
		button.setEnabled(false);
		button.setToolTipText("Part Outline");
		return button;
	}

	protected JButton createDeleteImageButton() {
		JButton button = new JButton();
		button.setEnabled(false);
		button.setFont(Fonts.DIALOG_PLAIN_20);
		return button;
	}

	protected JButton createUpdateImageButton() {
		JButton button = new JButton();
		button.setEnabled(false);
		button.setFont(Fonts.DIALOG_PLAIN_20);
		return button;
	}

	protected JButton createCreateImageButton() {
		JButton button = new JButton();
		button.setEnabled(false);
		button.setFont(Fonts.DIALOG_PLAIN_20);
		return button;
	}

	protected JButton createUpdateButton() {
		JButton button = new JButton("Update");
		button.setEnabled(false);
		button.setFont(Fonts.DIALOG_PLAIN_20);
		return button;
	}

	protected JButton createRemoveButton() {
		JButton button = new JButton("Remove");
		button.setEnabled(false);
		button.setFont(Fonts.DIALOG_PLAIN_20);
		return button;
	}

	protected JButton createSaveButton() {
		JButton button = new JButton("Save");
		button.setEnabled(false);
		button.setFont(Fonts.DIALOG_PLAIN_20);
		return button;
	}

	// === get/set === //
	public JButton getClearButton() {
		return clearButton;
	}

	public JButton getDrawButton() {
		return drawButton;
	}

	public JPanel getSelectionPanel() {
		return selectionPanel;
	}

	public JComboBox getPartLocation() {
		return partLocationComboBox;
	}

	public JLabel getPartLocationLabel() {
		return partLocationLabel;
	}

	public JComboBox getPart() {
		return partComboBox;
	}

	public JComboBox getPartGroup() {
		return partGroupComboBox;
	}

	public JLabel getPartGroupLabel() {
		return partGroupLabel;
	}

	public JLabel getPartLabel() {
		return partLabel;
	}

	public JButton getSaveButton() {
		return saveButton;
	}

	public JButton getUpdateButton() {
		return updateButton;
	}

	public JButton getRemoveButton() {
		return removeButton;
	}

	// === action/handlers mappings === //
	protected void mapActions() {
		getClearButton().addActionListener(this);

		getDrawButton().addActionListener(this);
		getRemoveButton().addActionListener(this);
		getUpdateButton().addActionListener(this);
		getSaveButton().addActionListener(this);
	}

	protected void mapListeners() {
		getImageComboBox().addActionListener(this);
		getImageBean().addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				int mouseX = e.getX();
				int mouseY = e.getY();
				getImageBean().setMouseCursorMove(mouseX, mouseY);
			}
		});
		getImageBean().addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e) {
				imageMouseClicked(e);
			}	
		});
		getPartGroup().addActionListener(this);
		getPart().addActionListener(this);
		getPartLocation().addActionListener(this);
	}

	// === controlling api === //
	public void setButtonsState() {
		boolean polygonSelected = getImageBean().getSelectedPolygon() != null;
		boolean modelUpdated = getImageModel().isModelUpdated();
		boolean selectionChanged = false;
		if (polygonSelected) {
			ImageSection data = getImageBean().getSelectedPolygon().info;
			InspectionPartDescription description = (InspectionPartDescription) getPartLocation().getSelectedItem();
			if (description != null && description.getDescriptionId() != data.getDescriptionId()) {
				selectionChanged = true;
			}
		}
		getRemoveButton().setEnabled(polygonSelected);
		getUpdateButton().setEnabled(polygonSelected && selectionChanged);
		getSaveButton().setEnabled(modelUpdated);
	}

	public long getImagesSelectedTime() {
		return imagesSelectedTime;
	}

	public void setImagesSelectedTime(long imagesSelectedTime) {
		this.imagesSelectedTime = imagesSelectedTime;
	}
	
	private void imageMouseClicked(MouseEvent e){
		if ( getPartLocation().getSelectedItem() == null) {
			String msg = null;
			if (getPartLocation().getItemCount() == 0) {
				msg = "Selected Part Group does not have associated Part Descriptions";
			} else {
				msg = "Please select Part and Location"; 
			}
			JOptionPane.showMessageDialog(this, msg);
			return;
		}		
		
		if(getImageBean().processMouseClicked(e.getX(), e.getY())) {
			Image image = (Image) getImageComboBox().getSelectedItem();
			InspectionPartDescription inspectionPartDescription = (InspectionPartDescription) getPartLocation().getSelectedItem();
			int descriptionId = inspectionPartDescription.getDescriptionId();
			String imageName = image.getImageName();
			ImageSection newImageSection = insertNewPolygonData(descriptionId, imageName);
			getImageModel().getCreatedImageSections().add(newImageSection);
			PartPolygon galcMagic = getImageBean().getVectorPolygon();
			for (int i = 0; i < galcMagic.npoints; i++) {
				ImageSectionPoint point = new ImageSectionPoint();
				point.setImageSectionId(newImageSection.getImageSectionId());
				point.setPointSequenceNo(i);
				point.setPointX(galcMagic.xpoints[i]);
				point.setPointY(galcMagic.ypoints[i]);
				// imageSectionPoints.add(point);
				newImageSection.getImageSectionPoints().add(point);
			}

			ImageSection data = new ImageSection();
			data.setImageSectionId(newImageSection.getImageSectionId());
			data.setImageName(newImageSection.getImageName());
			data.setPartKindFlagValue(newImageSection.getPartKindFlagValue());
			data.setDescriptionId(newImageSection.getDescriptionId());
			data.setOverlayNo(newImageSection.getOverlayNo());
			getImageBean().insertNewPolygonData(data);
			update(getGraphics());
		}

		if (getImageBean().getSelectedPolygon() != null) {
			ImageSection data = getImageBean().getSelectedPolygon().info;
			int descriptionId = data.getDescriptionId();
			InspectionPartDescription inspectionPartDescription = getController().selectInspectionPartDescription(descriptionId);
			if (inspectionPartDescription != null) {
				for (PartGroup partGroup : getClientModel().getPartGroups()) {
					if (partGroup.getPartGroupName().equals(inspectionPartDescription.getPartGroupName()))
						getPartGroup().setSelectedItem(partGroup);
				}
				getPart().setSelectedItem(inspectionPartDescription.getInspectionPartName());
				getPartLocation().setSelectedItem(inspectionPartDescription);
			}
		}
		setButtonsState();
		
	}
	
	private ImageSection insertNewPolygonData(int descriptionID, String imageName) {

		int maxID = -1;
		int maxOverlay = -1;
		List<ImageSection> imageSections = new ArrayList<ImageSection>();
		imageSections.addAll(getImageModel().getImageSections());
		imageSections.addAll(getImageModel().getCreatedImageSections());
		for (int i = 0; i < imageSections.size(); i++) {

			ImageSection imageSection = imageSections.get(i);
			if (i == 0) {
				maxID = imageSection.getImageSectionId();
				maxOverlay = imageSection.getOverlayNo();
			}
			
			if ((maxID < imageSection.getImageSectionId()))
				maxID = imageSection.getImageSectionId();

			if ((maxOverlay < imageSection.getOverlayNo()))
				maxOverlay = imageSection.getOverlayNo();
		}

		ImageSection newImageSection = new ImageSection();
		newImageSection.setImageSectionId(maxID + 1);
		newImageSection.setImageName(imageName);
		newImageSection.setDescriptionId(descriptionID);
		newImageSection.setOverlayNo(maxOverlay + 1);

		newImageSection.setPartKindFlag(false);
		return newImageSection;
	}

	@Override
	public void deselected(ListSelectionModel model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void selected(ListSelectionModel model) {
		// TODO Auto-generated method stub
		
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == getImageComboBox()) imageSelected();
		else if(e.getSource() == getClearButton()) clearButtonClicked();
		else if(e.getSource() == getDrawButton()) drawButtonClicked();
		else if(e.getSource() == getPartGroup()) partGroupSelected();
		else if(e.getSource() == getPart()) partSelected();
		else if(e.getSource() == getPartLocation()) partLocationSelected();
		else if(e.getSource() == getUpdateButton()) updateButtonClicked();
		else if(e.getSource() == getRemoveButton()) removeButtonClicked();
		else if(e.getSource() == getSaveButton()) saveButtonClicked();
	}

	private void updateButtonClicked() {
		if (getImageBean().getFunctionMode() != ImageBean.POINTER_MODE) return;
		if (getImageBean().getSelectedPolygon() == null)return;
		ImageSection data = getImageBean().getSelectedPolygon().info;
		int imageSectionId = data.getId();
		InspectionPartDescription inspectionPartDescription = (InspectionPartDescription) getPartLocation().getSelectedItem();
		data.setDescriptionId(inspectionPartDescription.getDescriptionId());
		getImageModel().updateImageSection(imageSectionId, inspectionPartDescription.getDescriptionId());
		getImageBean().updateDescriptionID(imageSectionId, inspectionPartDescription.getDescriptionId());
		setButtonsState();
	}
	
	private void clearButtonClicked() {
		getImageBean().setFunctionMode(ImageBean.POINTER_MODE);
	}
	
	private void removeButtonClicked() {
		if (getImageBean().getFunctionMode() != ImageBean.POINTER_MODE) return;
		if (getImageBean().getSelectedPolygon() == null)return;
		int imageSectionId = getImageBean().getSelectedPolygon().info.getId();
		getImageModel().removeImageSection(imageSectionId);
		getImageBean().runDEL();
		setButtonsState();
	}
	
	private void saveButtonClicked() {
		List<ImageSection> createdImageSections = getImageModel().getCreatedImageSections();
		List<ImageSection> udatedImageSections = getImageModel().getUpdatedImageSections();
		List<ImageSection> deletedImageSections = getImageModel().getDeletedImageSections();
		getController().saveImageSections(createdImageSections, udatedImageSections, deletedImageSections);

		Image image = (Image) getImageComboBox().getSelectedItem();
		getImageComboBox().setSelectedIndex(-1);
		getImageComboBox().setSelectedItem(image);
		setButtonsState();
		getRemoveButton().setEnabled(false);
	}
	
	private void partGroupSelected() {
		getPart().removeAllItems();
		PartGroup partGroup = (PartGroup)getPartGroup().getSelectedItem();
		if (partGroup == null) {
			return;
		}
		InspectionPartDescription criteria = new InspectionPartDescription();
		criteria.setPartGroupName(partGroup.getPartGroupName());
		List<InspectionPartDescription> parts = getController().selectInspectionPartDescriptions(criteria);
		
		if (parts == null || parts.isEmpty()) {
			getImageModel().setInspectionPartDescriptions(new ArrayList<InspectionPartDescription>());
			return;
		}
		getImageModel().setInspectionPartDescriptions(parts);
		List<String> partNames = new ArrayList<String>();
		for (InspectionPartDescription part : parts) {
			String partName = part.getInspectionPartName().trim();
			if (!partNames.contains(partName)) {
				partNames.add(partName);
			}
		}
		ComboBoxUtils.loadComboBox(getPart(), partNames);	
	}
	
	private void partSelected() {
		getPartLocation().removeAllItems();
		String partName = (String) getPart().getSelectedItem();
		if (partName == null) {
			return;
		}
		partName = partName.trim();
		List<InspectionPartDescription> parts = new ArrayList<InspectionPartDescription>();
		if (getImageModel().getInspectionPartDescriptions() != null) {
			for (InspectionPartDescription part : getImageModel().getInspectionPartDescriptions()) {
				if (part == null) continue;
				if (partName.equals(part.getInspectionPartName())) parts.add(part);
			}
		}
		if (parts == null || parts.isEmpty()) {
			return;
		}
		ComboBoxUtils.loadComboBox(getPartLocation(), parts);
	}
	
	private void partLocationSelected() {
		setButtonsState();
	}
	
	
	private void drawButtonClicked() {
		getImageBean().setFunctionMode(ImageBean.SHAPE_MODE);
	}
	
	public void imageSelected() {
		// Image section tab error message not getting refreshed automatically
		clearErrorMessage();
		Image image = (Image)getImageComboBox().getSelectedItem();
		getImageBean().setDataPolygons(new ArrayList<PartPolygon>());
		getImageBean().clearImage();
		getImageModel().reset();
		setButtonsState();
		if (image == null) {
			getClearButton().setEnabled(false);
			getDrawButton().setEnabled(false);
			return;
		}
		Image loadedImage = loadImageData(image);
		getImageModel().setFormImage(loadedImage);
		try {
			if(loadedImage.getImageData() != null)
				getImageBean().loadImage(loadedImage.getImageData());
			else getImageBean().loadImage(loadedImage.getBitmapFileName());
		} catch (Exception ex) {
			getClearButton().setEnabled(false);
			getDrawButton().setEnabled(false);
			setErrorMessage("Failed to load image: " + image.getImageName());
			return;
		}

		List<ImageSection> imageSections = getController().selectImageSections(image.getImageName());
		if (imageSections == null || imageSections.isEmpty()) {
			getImageModel().setImageSections(new ArrayList<ImageSection>());
			getClearButton().setEnabled(true);
			getDrawButton().setEnabled(true);
			return;
		}

		getImageModel().setImageSections(imageSections);

		for (ImageSection imageSection : imageSections) {
			if (imageSection == null) {
				continue;
			}
			List<ImageSectionPoint> points = imageSection.getImageSectionPoints();
			if (points == null || points.isEmpty()) {
				continue;
			}
			int[] xs = new int[points.size()];
			int[] ys = new int[points.size()];
			int ix = 0;
			for (ImageSectionPoint point : points) {
				if (point == null) {
					continue;
				}
				xs[ix] = point.getPointX();
				ys[ix] = point.getPointY();
				ix++;
			}
			ImageSection data = new ImageSection();
			data.setDescriptionId(imageSection.getDescriptionId());
			data.setImageName(imageSection.getImageName());
			data.setImageSectionId(imageSection.getImageSectionId());
			data.setOverlayNo(imageSection.getOverlayNo());
			data.setPartKindFlag(imageSection.getPartKindFlag());
			getImageBean().addPolygonData(xs, ys, data, null);
		}
		getClearButton().setEnabled(true);
		getDrawButton().setEnabled(true);

	}
	
	
}

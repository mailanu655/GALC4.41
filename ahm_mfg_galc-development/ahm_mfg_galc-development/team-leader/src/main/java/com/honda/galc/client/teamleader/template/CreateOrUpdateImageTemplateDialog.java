package com.honda.galc.client.teamleader.template;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.common.ImageUtils;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.ImagePanel;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.TemplateDao;
import com.honda.galc.entity.product.Template;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.ExtensionFileFilter;

/**
 * @author Fredrick Yessaian
 * @date Dec 01, 2019
 * 
 *       This screen class creates new or update existing template (TEMPLATE_TBX)
 *       through Team leader screens.
 */

@SuppressWarnings(value = { "all" })
public class CreateOrUpdateImageTemplateDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = -3082959520462181310L;
	private final String browseButton = "PickAFile";
	private final String saveButton = "saveButton";
	private final String cancelButton = "cancelButton";
	private String filenameTextField = null;
	private byte[] loadedImageByte;
	private static final int MAX_IMAGE_SIZE = 10000000;
	private Template newTemplate = null;
	private Logger logger = Logger.getLogger();
	private DocumentFilter filter = new TemplateFilter();
	private ModeOfOperation typeOfOperation = null;
	private String templateHeading = null;
	private Template selectedTemplate = null;
	private static final int TEMPLATE_NAME_LENGTH = 40;
	private static final int FORM_ID_LENGTH = 32;
	private static final int DESCRIPTION_LENGTH = 128;
	private JTextField tempNameTx = null;
	private JTextField formIdTx = null;
	private JLabel revIdLbl = null;
	private TemplateDao templateDao = null;
	private ImagePanel image = null;
	private JTextField tempDescTx = null;
	private Boolean templateImageAltered = false;
	private JButton browsefileBtn = null;
	
	protected enum ModeOfOperation {
		CREATE, UPDATE
	}

	public CreateOrUpdateImageTemplateDialog(ModeOfOperation operation, Template template) {
		this.typeOfOperation = operation;
		this.setSelectedTemplate(template);
		this.setTemplateLabel();
		this.initialize();
	}

	private void initialize() {
		this.setLayout(new BorderLayout(10, 10));
		this.add(createPageStart(), BorderLayout.PAGE_START);
		this.add(createDetailsPanel(), BorderLayout.CENTER);
		this.add(createPageEnd(), BorderLayout.PAGE_END);

		this.setResizable(false);
		this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize((int) screenSize.getWidth() - 150, (int) screenSize.getHeight() - 150);
		this.setModal(true);

		if (this.typeOfOperation.equals(ModeOfOperation.UPDATE)) {
			preLoadDataForUpdate(this.getSelectedTemplate());
		} else {
			((AbstractDocument) tempNameTx.getDocument()).setDocumentFilter(filter);
			((AbstractDocument) formIdTx.getDocument()).setDocumentFilter(filter);
		}

		this.setVisible(true);
	}

	private JPanel createPageStart() {
		JPanel startPanel = new JPanel(new GridBagLayout());
		JLabel startLbl = new JLabel(getTemplateName());
		GridBagConstraints constraint = new GridBagConstraints();
		constraint.anchor = GridBagConstraints.CENTER;
		constraint.insets = new Insets(10, 0, 10, 0);
		startLbl.setFont(new Font("Arial", Font.BOLD, 25));
		startPanel.add(startLbl, constraint);
		return startPanel;
	}

	private JPanel createDetailsPanel() {
		JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 0));
		centerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		centerPanel.add(createTemplatePanel(), 0);
		centerPanel.add(createImagePanel(), 1);
		return centerPanel;
	}

	private JPanel createTemplatePanel() {

		Font font = new Font("Arial", 0, 25);

		JPanel templatePanel = new JPanel(new GridBagLayout());
		templatePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

		JLabel tempNameLbl = new JLabel("Template Name : ");
		tempNameLbl.setFont(font);
		GridBagConstraints lblConstraint1 = new GridBagConstraints();
		lblConstraint1.anchor = GridBagConstraints.LINE_END;
		lblConstraint1.insets = new Insets(15, 0, 25, 5);
		lblConstraint1.gridx = 0;
		lblConstraint1.gridy = 0;
		tempNameLbl.setForeground(Color.RED);
		templatePanel.add(tempNameLbl, lblConstraint1);

		tempNameTx = new JTextField(25);
		tempNameTx.setName("TemplateTxtBox");
		tempNameTx.setFont(font);
		GridBagConstraints txtConstraint1 = new GridBagConstraints();
		txtConstraint1.anchor = GridBagConstraints.LINE_START;
		txtConstraint1.insets = new Insets(15, 0, 25, 5);
		txtConstraint1.gridx = 1;
		txtConstraint1.gridy = 0;
		tempNameTx.addActionListener(this);
		tempNameTx.setMinimumSize(new Dimension(300, 25));
		templatePanel.add(tempNameTx, txtConstraint1);

		JLabel formIdLbl = new JLabel("Form Id : ");
		formIdLbl.setFont(font);
		GridBagConstraints lblConstraint2 = new GridBagConstraints();
		lblConstraint2.anchor = GridBagConstraints.LINE_END;
		lblConstraint2.insets = new Insets(15, 0, 25, 5);
		lblConstraint2.gridx = 0;
		lblConstraint2.gridy = 1;
		templatePanel.add(formIdLbl, lblConstraint2);

		formIdTx = new JTextField(25);
		formIdTx.setFont(font);
		GridBagConstraints txtConstraint2 = new GridBagConstraints();
		txtConstraint2.anchor = GridBagConstraints.LINE_START;
		txtConstraint2.insets = new Insets(15, 0, 25, 5);
		txtConstraint2.gridx = 1;
		txtConstraint2.gridy = 1;
		formIdTx.setMinimumSize(new Dimension(300, 25));
		templatePanel.add(formIdTx, txtConstraint2);

		JLabel tempTypeLbl = new JLabel("Template Type : ");
		tempTypeLbl.setFont(font);
		GridBagConstraints lblConstraint3 = new GridBagConstraints();
		lblConstraint3.anchor = GridBagConstraints.LINE_END;
		lblConstraint3.insets = new Insets(15, 0, 25, 5);
		lblConstraint3.gridx = 0;
		lblConstraint3.gridy = 2;
		templatePanel.add(tempTypeLbl, lblConstraint3);

		JComboBox tempTypeCmb = new JComboBox();
		JLabel tempType = new JLabel(" IMAGE ");
		tempType.setFont(font);
		GridBagConstraints txtConstraint3 = new GridBagConstraints();
		txtConstraint3.anchor = GridBagConstraints.LINE_START;
		txtConstraint3.insets = new Insets(15, 0, 25, 5);
		txtConstraint3.gridx = 1;
		txtConstraint3.gridy = 2;
		templatePanel.add(tempType, txtConstraint3);

		JLabel tempDesLbl = new JLabel("Template Description : ");
		tempDesLbl.setFont(font);
		GridBagConstraints lblConstraint4 = new GridBagConstraints();
		lblConstraint4.anchor = GridBagConstraints.LINE_END;
		lblConstraint4.insets = new Insets(15, 0, 25, 5);
		lblConstraint4.gridx = 0;
		lblConstraint4.gridy = 3;
		templatePanel.add(tempDesLbl, lblConstraint4);

		tempDescTx = new JTextField(25);
		tempDescTx.setFont(font);
		GridBagConstraints txtConstraint4 = new GridBagConstraints();
		txtConstraint4.anchor = GridBagConstraints.LINE_START;
		txtConstraint4.insets = new Insets(15, 0, 25, 5);
		txtConstraint4.gridx = 1;
		txtConstraint4.gridy = 3;
		tempDescTx.setMinimumSize(new Dimension(315, 25));
		templatePanel.add(tempDescTx, txtConstraint4);

		JLabel revisionIdLbl = new JLabel("Revision Id : ");
		revisionIdLbl.setFont(font);
		GridBagConstraints lblConstraint5 = new GridBagConstraints();
		lblConstraint5.anchor = GridBagConstraints.LINE_END;
		lblConstraint5.insets = new Insets(15, 0, 25, 5);
		lblConstraint5.gridx = 0;
		lblConstraint5.gridy = 4;
		templatePanel.add(revisionIdLbl, lblConstraint5);

		revIdLbl = new JLabel();
		revIdLbl.setText("1");
		revIdLbl.setName("revIdLbl");
		revIdLbl.setFont(font);
		GridBagConstraints txtConstraint5 = new GridBagConstraints();
		txtConstraint5.anchor = GridBagConstraints.LINE_START;
		txtConstraint5.insets = new Insets(15, 0, 25, 5);
		txtConstraint5.gridx = 1;
		txtConstraint5.gridy = 4;
		templatePanel.add(revIdLbl, txtConstraint5);

		return templatePanel;
	}

	private JPanel createImagePanel() {
		JPanel imagePanel = new JPanel(new BorderLayout(5, 5));

		GridBagConstraints imageGridbagConstraint = new GridBagConstraints();
		imagePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
		imageGridbagConstraint.gridx = 0;
		imageGridbagConstraint.gridy = 0;
		imageGridbagConstraint.anchor = GridBagConstraints.CENTER;
		imageGridbagConstraint.insets = new Insets(0, 0, 0, 0);
		image = new ImagePanel();
		image.setSize(100, 100);

		JPanel browsePanel = new JPanel();
		browsePanel.setLayout(new GridBagLayout());
		browsePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

		String browseBtnText = "Pick Your Image File";
		if (this.typeOfOperation.equals(ModeOfOperation.UPDATE))
			browseBtnText = "Update with new image";

		browsefileBtn = new JButton(browseBtnText);
		browsefileBtn.setFont(new Font("Arial", Font.BOLD, 20));
		browsefileBtn.setName(browseButton);
		browsefileBtn.addActionListener(this);
		GridBagConstraints constraint = new GridBagConstraints();
		constraint.anchor = GridBagConstraints.CENTER;
		constraint.insets = new Insets(10, 0, 10, 0);
		browsePanel.add(browsefileBtn, constraint);

		imagePanel.add(image, BorderLayout.CENTER);
		imagePanel.add(browsePanel, BorderLayout.PAGE_END);

		return imagePanel;
	}

	private JPanel createPageEnd() {

		Font font = new Font("Arial", 0, 18);

		JPanel pageEndPanel = new JPanel(new GridBagLayout());

		JButton saveBtn = new JButton(
				"           " + ModeOfOperation.valueOf(typeOfOperation.toString()) + "            ");
		saveBtn.setName(saveButton);
		saveBtn.setFont(font);
		GridBagConstraints saveGrid = new GridBagConstraints();
		saveGrid.anchor = GridBagConstraints.CENTER;
		saveGrid.gridx = 0;
		saveGrid.gridy = 0;
		saveGrid.insets = new Insets(10, 0, 10, 10);
		saveBtn.addActionListener(this);

		JButton cancelBtn = new JButton("            Cancel           ");
		cancelBtn.setName(cancelButton);
		cancelBtn.setFont(font);
		GridBagConstraints cancelGrid = new GridBagConstraints();
		cancelGrid.anchor = GridBagConstraints.CENTER;
		cancelGrid.gridx = 1;
		cancelGrid.gridy = 0;
		cancelGrid.insets = new Insets(10, 10, 10, 0);
		cancelBtn.addActionListener(this);

		pageEndPanel.add(saveBtn, saveGrid);
		pageEndPanel.add(cancelBtn, cancelGrid);
		return pageEndPanel;
	}

	private String getTemplateName() {
		return templateHeading;
	}

	private void setTemplateLabel() {

		if (this.typeOfOperation.equals(ModeOfOperation.UPDATE)) {
			if (getSelectedTemplate().getFormId() != null && getSelectedTemplate().getFormId().trim().length() > 0)
				this.templateHeading = "Update Template : " + getSelectedTemplate().getTemplateName()
						+ " , with Form Id : " + getSelectedTemplate().getFormId();
			else
				this.templateHeading = "Update Template : " + getSelectedTemplate().getTemplateName();
		} else {
			this.templateHeading = "Create A New Template";
		}
	}

	private Template getSelectedTemplate() {
		return selectedTemplate;
	}

	private void setSelectedTemplate(Template selectedTemplate) {
		this.selectedTemplate = selectedTemplate;
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() instanceof JButton) {
			JButton btnName = (JButton) e.getSource();

			if (btnName.getName().equalsIgnoreCase(browseButton)) {
				pickAFile();
			}else if (btnName.getName().equalsIgnoreCase(cancelButton)) {
				this.dispose();
				setVisible(false);
			}else if (btnName.getName().equalsIgnoreCase(saveButton)) {
				boolean result = saveTemplateTbx();
				if (result) {
					this.dispose();
					setVisible(false);
				}

			}
		}

	}

	private boolean saveTemplateTbx() {
		if (typeOfOperation.equals(ModeOfOperation.CREATE)) {

			try {
				if (this.tempNameTx.getText().trim().length() == 0) {
					MessageDialog.showError(this, "Template Name should not be empty");
					this.tempNameTx.requestFocusInWindow();
					return false;
				}
				if (this.tempNameTx.getText().trim().length() > TEMPLATE_NAME_LENGTH || this.formIdTx.getText().trim().length() > FORM_ID_LENGTH
						|| this.tempDescTx.getText().trim().length() > DESCRIPTION_LENGTH) {
					MessageDialog.showError(this,
							"Make sure Template Name should not exceed "+TEMPLATE_NAME_LENGTH+" , Form Id "+FORM_ID_LENGTH+" and Template Description "+DESCRIPTION_LENGTH+" characters");

					if (this.tempNameTx.getText().trim().length() > TEMPLATE_NAME_LENGTH) {
						this.tempNameTx.selectAll();
						this.tempNameTx.requestFocusInWindow();
					}

					if (this.formIdTx.getText().trim().length() > FORM_ID_LENGTH) {
						this.formIdTx.selectAll();
						this.formIdTx.requestFocusInWindow();
					}

					if (this.tempDescTx.getText().trim().length() > DESCRIPTION_LENGTH) {
						this.tempDescTx.selectAll();
						this.tempDescTx.requestFocusInWindow();
					}

					return false;
				}
				if (this.getNewTemplate() == null) {
					MessageDialog.showError(this, "Please upload the Image file");
					browsefileBtn.requestFocusInWindow();
					return false;
				}
				Template verifyTemplateName = getTemplateDao()
						.findTemplateByTemplateName(this.tempNameTx.getText().trim());
				if (verifyTemplateName != null) {
					MessageDialog.showError(this, "Given Template Name " + this.tempNameTx.getText().trim() + " is already exist, please change the template name");
					this.tempNameTx.selectAll();
					this.tempNameTx.requestFocusInWindow();
					return false;
				}
				this.getNewTemplate().setTemplateName(this.tempNameTx.getText().trim());
				this.getNewTemplate().setFormId(this.formIdTx.getText().trim());
				this.getNewTemplate().setTemplateDescription(this.tempDescTx.getText().trim());
				this.getNewTemplate().setTemplateTypeString("IMAGE");
				this.getNewTemplate().setRevisionId(1);
				getTemplateDao().insert(this.getNewTemplate());
				return true;
			} catch (Exception e) {
				MessageDialog.showError(this, "Database Insert failed");
				logger.error("Exception while inserting TEMPLATE_TBX : " + e.getMessage());
				e.printStackTrace();
				return false;
			}

		} else {

			if (this.formIdTx.getText().trim().length() > FORM_ID_LENGTH || this.tempDescTx.getText().trim().length() > DESCRIPTION_LENGTH) {
				MessageDialog.showError("Make sure Form Id should not exceed " + FORM_ID_LENGTH + " and Template Descripton should not exceed " + DESCRIPTION_LENGTH + " characters");
				
				if (this.formIdTx.getText().trim().length() > FORM_ID_LENGTH) {
					this.formIdTx.selectAll();
					this.formIdTx.requestFocusInWindow();
				}

				if (this.tempDescTx.getText().trim().length() > DESCRIPTION_LENGTH) {
					this.tempDescTx.selectAll();
					this.tempDescTx.requestFocusInWindow();
				}
				
				return false;
			}

			if (isTemplateImageAltered() || !StringUtils.equals(this.formIdTx.getText().trim(), this.getSelectedTemplate().getFormId().trim())  
					|| !StringUtils.equals(this.tempDescTx.getText().trim(),this.getSelectedTemplate().getTemplateDescription().trim())) { 
				try {
					this.getSelectedTemplate().setFormId(this.formIdTx.getText().trim());
					this.getSelectedTemplate().setTemplateDescription(this.tempDescTx.getText().trim());
					this.getSelectedTemplate().setRevisionId(this.getSelectedTemplate().getRevisionId() + 1);
					getTemplateDao().update(this.getSelectedTemplate());
					return true;
				} catch (Exception e) {
					MessageDialog.showError(this, "Database update failed : " + e.getMessage());
					logger.error("Exception while updating TEMPLATE_TBX : " + e.getMessage());
					e.printStackTrace();
					return false;
				}

			} else {
				MessageDialog.showError("Nothing has been altered");
				return false;
			}
		}

	}

	private void pickAFile() {

		UIManager.put("FileChooser.readOnly", Boolean.TRUE);
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setCurrentDirectory(new File("/"));
		chooser.setMultiSelectionEnabled(false);
		chooser.setFileFilter(new ExtensionFileFilter("jpg", "png", "gif", "bmp"));
		chooser.setDialogTitle("Select the image");
		int returnVal = chooser.showOpenDialog(this);
		if (returnVal != JFileChooser.APPROVE_OPTION) {
			return;
		}
		String filename = chooser.getSelectedFile().getAbsolutePath();
		if (filename != null) {

			try {

				loadedImageByte = loadImageData(filename);

				if (typeOfOperation.equals(ModeOfOperation.CREATE)) {
					newTemplate = new Template();
					newTemplate.setTemplateData(loadedImageByte);
					loadImageInPanel(newTemplate.getTemplateDataBytes());
				} else {
					getSelectedTemplate().setTemplateData(loadedImageByte);
					loadImageInPanel(getSelectedTemplate().getTemplateDataBytes());
					setTemplateImageAltered(true);
				}

			} catch (Exception e) {
				logger.error("Exception occured while loading the Image file : " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	private byte[] loadImageData(String filename) {
		logger.debug("Loading image from file " + filename);
		if (StringUtils.isEmpty(filename)) {
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
			logger.debug("Finished loading image.");
			return imageData == null ? null
					: ImageUtils.scale(imageData, filename.toLowerCase().endsWith(".bmp"), MAX_IMAGE_SIZE, 0.9);
			
		} catch (Exception e) {
			logger.error("Unable to load image from file. " + e.getMessage());
			MessageDialog.showError("Unable to read image. Please verify file format");
			return imageData = null;
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
				}
			}
		}
	}

	private void preLoadDataForUpdate(Template template) {
		((AbstractDocument) formIdTx.getDocument()).setDocumentFilter(filter);
		this.tempNameTx.setText(template.getTemplateName());
		this.tempNameTx.setEditable(false);
		this.formIdTx.setText(template.getFormId());
		this.revIdLbl.setText(String.valueOf(template.getRevisionId()));
		this.tempDescTx.setText(template.getTemplateDescription());
		this.loadSelectedImage(template);
	}

	private void loadSelectedImage(Template template) {
		Template temp = getTemplateDao().findTemplateByTemplateName(template.getTemplateName().trim());
		if (temp.getTemplateDataBytes() != null) {
			loadImageInPanel(temp.getTemplateDataBytes());
			this.getSelectedTemplate().setTemplateData(temp.getTemplateDataBytes());
		} else
			image.showImage(null);
	}

	private void loadImageInPanel(byte[] imageBytes) {
		try {
			image.showImage(ImageIO.read(new ByteArrayInputStream(imageBytes)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private TemplateDao getTemplateDao() {
		templateDao = ServiceFactory.getDao(TemplateDao.class);
		return templateDao;
	}

	private Boolean isTemplateImageAltered() {
		return templateImageAltered;
	}

	private void setTemplateImageAltered(Boolean templateAltered) {
		this.templateImageAltered = templateAltered;
	}

	private Template getNewTemplate() {
		return newTemplate;
	}

	private void setNewTemplate(Template newTemplate) {
		this.newTemplate = newTemplate;
	}

}

/*
 * This filter class ensures only letters in upper case, numerics and underscore
 * ( _ ) is allowed through filter.
 */

class TemplateFilter extends DocumentFilter {

	public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
			throws BadLocationException {

		int asciiIntVal = (int) text.charAt(length);
		if ((asciiIntVal >= 65 && asciiIntVal <= 90) || (asciiIntVal == 95) || (asciiIntVal >= 97 && asciiIntVal <= 122)
				|| (asciiIntVal >= 48 && asciiIntVal <= 57))
			fb.replace(offset, length, text.toUpperCase(), attrs);

	}

}

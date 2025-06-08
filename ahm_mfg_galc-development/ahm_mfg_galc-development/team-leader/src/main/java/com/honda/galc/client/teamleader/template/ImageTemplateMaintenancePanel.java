package com.honda.galc.client.teamleader.template;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.IPopupMenu;
import com.honda.galc.client.ui.component.ImagePanel;
import com.honda.galc.client.ui.component.PopupMenuMouseAdapter;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.client.ui.tablemodel.TemplateTableModel;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.TemplateDao;
import com.honda.galc.entity.product.Template;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.SortedArrayList;

/**
 * @author Fredrick Yessaian
 * @date Dec 01, 2019
 * 
 *       This screen class created to maintain TEMPLATE_TBX (for Images) through
 *       Team leader screens.
 */

@SuppressWarnings(value = { "all" })
public class ImageTemplateMaintenancePanel extends TabbedPanel implements ActionListener, ListSelectionListener {

	private JButton loadButton = null;
	private JButton createButton = null;
	private List<Template> templateLst = new SortedArrayList<Template>();
	private TablePane resultTable = null;
	private TemplateTableModel templateTableModel = null;
	private TemplateDao templateDao = null;
	private JTextField searchTemplateTxt = null;
	private JTextField searchFormTxt = null;
	private JButton searchBtn = null;
	private JButton clearTxtBtn = null;
	private ImagePanel imagePanel = null;
	private final JPanel resultPanel = null;
	private final String MODIFY_TEMPLATE = "Modify Template";
	private Operation lastOperation = Operation.NONE;
	private JButton clearResultBtn = null;
	private final String CLEAR_TXT_BTN_NAME = "clearTxtBtn";
	private final String SEARCH_BTN_NAME = "searchBtn";
	private final String LOAD_ALL_BTN_NAME = "loadAllButton";
	private final String CREATE_NEW_BTN_NAME = "createNewButton";
	private final String CLEAR_RST_BTN_NAME = "clearResultBtn";
	private final String RESULT_TBL_NAME = "resultTable";
	
	private Logger logger = Logger.getLogger();

	protected enum Operation {
		NONE, LIST_ALL, SEARCH
	}

	public ImageTemplateMaintenancePanel(TabbedMainWindow mainWindow) {
		super("IMAGE Template(s) Maintenance Screen", KeyEvent.VK_T, mainWindow);
	}

	public void onTabSelected() {
		this.initiate();
	}

	private void initiate() {
		this.setLayout(new BorderLayout(10, 10));
		this.add(createFunctionalPanel(), BorderLayout.PAGE_START);
		this.add(createResultPanel(), BorderLayout.CENTER);
		this.add(createPageEndPanel(), BorderLayout.PAGE_END);
	}

	private JPanel createFunctionalPanel() {

		JPanel functionalPanel = new JPanel();
		functionalPanel.setLayout(new GridLayout(1, 2, 0, 0));

		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new GridBagLayout());
		searchPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

		GridBagConstraints label1Constraint = new GridBagConstraints();
		label1Constraint.gridx = 0;
		label1Constraint.gridy = 0;
		label1Constraint.anchor = GridBagConstraints.LINE_END;
		label1Constraint.insets = new Insets(10, 0, 10, 0);
		searchPanel.add(new JLabel("TEMPLATE NAME   : "), label1Constraint);

		GridBagConstraints label2Constraint = new GridBagConstraints();
		label2Constraint.gridx = 1;
		label2Constraint.gridy = 0;
		label2Constraint.gridheight = 2;
		label2Constraint.anchor = GridBagConstraints.LINE_END;
		label2Constraint.insets = new Insets(0, 20, 0, 20);
		searchPanel.add(new JLabel("%"), label2Constraint);

		GridBagConstraints txtBx1Constraint = new GridBagConstraints();
		txtBx1Constraint.gridx = 2;
		txtBx1Constraint.gridy = 0;
		txtBx1Constraint.insets = new Insets(10, 0, 0, 0);
		searchTemplateTxt = new JTextField(25);
		searchTemplateTxt.setFont(new Font("Arial", Font.BOLD, 20));
		searchTemplateTxt.setMinimumSize(new Dimension(300, 25));
		searchPanel.add(searchTemplateTxt, txtBx1Constraint);

		GridBagConstraints label3Constraint = new GridBagConstraints();
		label3Constraint.gridx = 3;
		label3Constraint.gridy = 0;
		label3Constraint.gridheight = 2;
		label3Constraint.anchor = GridBagConstraints.LINE_START;
		label3Constraint.insets = new Insets(0, 20, 0, 20);
		searchPanel.add(new JLabel("%"), label3Constraint);

		GridBagConstraints Btn1Constraint = new GridBagConstraints();
		Btn1Constraint.gridx = 4;
		Btn1Constraint.gridy = 0;
		searchBtn = new JButton("    Search    ");
		searchBtn.setName(SEARCH_BTN_NAME);
		searchBtn.addActionListener(this);
		searchPanel.add(searchBtn, Btn1Constraint);

		GridBagConstraints label4Constraint = new GridBagConstraints();
		label4Constraint.gridx = 0;
		label4Constraint.gridy = 1;
		label4Constraint.anchor = GridBagConstraints.LINE_END;
		label4Constraint.insets = new Insets(10, 0, 10, 0);
		searchPanel.add(new JLabel("FORM ID   : "), label4Constraint);

		GridBagConstraints txtBx2Constraint = new GridBagConstraints();
		txtBx2Constraint.gridx = 2;
		txtBx2Constraint.gridy = 1;
		txtBx2Constraint.insets = new Insets(10, 0, 10, 0);
		searchFormTxt = new JTextField(25);
		searchFormTxt.setFont(new Font("Arial", Font.BOLD, 20));
		searchFormTxt.setMinimumSize(new Dimension(300, 25));
		searchPanel.add(searchFormTxt, txtBx2Constraint);

		GridBagConstraints Btn2Constraint = new GridBagConstraints();
		Btn2Constraint.gridx = 4;
		Btn2Constraint.gridy = 1;
		clearTxtBtn = new JButton("  Clear Text  ");
		clearTxtBtn.setName(CLEAR_TXT_BTN_NAME);
		clearTxtBtn.addActionListener(this);

		searchPanel.add(clearTxtBtn, Btn2Constraint);

		JPanel functionalButtonPanel = new JPanel();
		functionalButtonPanel.setLayout(new GridBagLayout());
		functionalButtonPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

		GridBagConstraints contraints = new GridBagConstraints();
		contraints.anchor = GridBagConstraints.CENTER;
		contraints.weightx = 0.1;
		contraints.gridx = 0;
		contraints.gridy = 0;

		functionalButtonPanel.add(createLoadButton(), contraints, 0);

		GridBagConstraints contraints2 = new GridBagConstraints();
		contraints2.anchor = GridBagConstraints.CENTER;
		contraints2.weightx = 0.1;
		contraints2.gridx = 1;
		contraints2.gridy = 0;

		functionalButtonPanel.add(createTemplateButton(), contraints2, 1);

		functionalPanel.add(searchPanel, 0);
		functionalPanel.add(functionalButtonPanel, 1);
		return functionalPanel;
	}

	private JPanel createResultPanel() {
		final JPanel resultPanel = new JPanel(new GridLayout(1, 2, 10, 0));
		resultTable = new TablePane("TEMPLATE(S)", true);
		resultTable.setName("resultTable");
		templateTableModel = new TemplateTableModel(templateLst, resultTable.getTable());

		resultTable.addListSelectionListener(this);

		resultTable.getTable().addMouseListener(createMouseListener());

		imagePanel = new ImagePanel();
		imagePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));

		resultPanel.add(resultTable, 0);
		resultPanel.add(imagePanel, 1);

		return resultPanel;
	}

	private MouseListener createMouseListener() {
		return new PopupMenuMouseAdapter(new IPopupMenu() {
			public void showPopupMenu(MouseEvent e) {
				addPopupMenu(e);
			}
		});
	}

	private void addPopupMenu(MouseEvent e) {
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(createMenuItem(MODIFY_TEMPLATE, true));
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
	}

	private Component createImageDisplayPanel() {
		ImagePanel imagePanel = new ImagePanel();
		imagePanel.setSize(300, 500);
		imagePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
		return imagePanel;
	}

	private JButton createLoadButton() {
		loadButton = new JButton("           Load All Available Templates           ");
		loadButton.setName(LOAD_ALL_BTN_NAME);
		loadButton.addActionListener(this);
		return loadButton;
	}

	private JButton createTemplateButton() {
		createButton = new JButton("           Create New Template           ");
		createButton.setName(CREATE_NEW_BTN_NAME);
		createButton.addActionListener(this);
		return createButton;
	}

	private JPanel createPageEndPanel() {
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridBagLayout());
		bottomPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		clearResultBtn = new JButton("Clear Results");
		clearResultBtn.setName(CLEAR_RST_BTN_NAME);
		clearResultBtn.addActionListener(this);
		GridBagConstraints constraint = new GridBagConstraints();
		constraint.anchor = GridBagConstraints.CENTER;
		constraint.insets = new Insets(10, 0, 10, 0);
		bottomPanel.add(clearResultBtn, constraint);

		return bottomPanel;
	}

	private TemplateDao getTemplateDao() {
		templateDao = ServiceFactory.getDao(TemplateDao.class);
		return templateDao;
	}

	private void populateSearchResult() {

		if (searchTemplateTxt.getText().trim().length() == 0 && searchFormTxt.getText().trim().length() == 0) {
			MessageDialog.showError(this, "Please enter Template Name or Form Id or both to get the result.");
			return;
		}

		templateLst = null;
		templateLst = getTemplateDao().searchTemplates(searchTemplateTxt.getText().trim(),
				searchFormTxt.getText().trim());
		if (templateLst != null && templateLst.size() == 0) {
			MessageDialog.showInfo(this, "No template(s) found, tune your search", "Template Search");
			return;
		}

		templateTableModel.refresh(null);
		templateTableModel.refresh(templateLst);
		setLastOperation(Operation.SEARCH);
	}

	private void loadSelectedImage(Template template) {

		if (template == null)
			return;

		Template temp = getTemplateDao().findTemplateByTemplateName(template.getTemplateName().trim());
		try {
			if (temp.getTemplateDataBytes() != null)
				imagePanel.showImage(ImageIO.read(new ByteArrayInputStream(temp.getTemplateDataBytes())));
			else {
				imagePanel.showImage(null);
				MessageDialog.showInfo(this, "No Image attached with this entry");
			}

		} catch (IOException e) {
			logger.error("Exception at loadSelectedImage : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() instanceof JMenuItem) {

			JMenuItem menuItem = (JMenuItem) e.getSource();
			if (menuItem.getName().equalsIgnoreCase(MODIFY_TEMPLATE))
				modifyTemplate(templateTableModel.getSelectedItem());
		}
		if (e.getSource() instanceof JButton) {

			JButton button = (JButton) e.getSource();
			if (button.getName().equalsIgnoreCase("loadAllButton")) {
				this.populateListAll();
			}else if (button.getName().equalsIgnoreCase("clearResultBtn")) {
				this.clearSerchText();
			}else if (button.getName().equalsIgnoreCase("clearTxtBtn")) {
				this.clearSerchText();
			}else if (button.getName().equalsIgnoreCase("searchBtn")) {
				this.populateSearchResult();
			}else if (button.getName().equalsIgnoreCase("createNewButton")) {
				this.callCreateTemplate();
			}
		}
	}

	private void callCreateTemplate() {
		new CreateOrUpdateImageTemplateDialog(CreateOrUpdateImageTemplateDialog.ModeOfOperation.CREATE, null);
		this.refreshResults();
	}

	private void clearSerchText() {
		this.templateTableModel.refresh(null);
		this.templateTableModel.selectItem(null);
		this.imagePanel.showImage(null);
		this.searchFormTxt.setText(null);
		this.searchTemplateTxt.setText(null);
		this.setLastOperation(Operation.NONE);
	}

	private void populateListAll() {
		this.templateLst.clear();
		this.templateLst = getTemplateDao().findAllTemplates();
		this.templateTableModel.refresh(templateLst);
		this.templateTableModel.selectItem(null);
		this.setLastOperation(Operation.LIST_ALL);
	}

	private void refreshResults() {
		
		Template selectedOne = this.templateTableModel.getSelectedItem();
		
		if (getLastOperation().equals(Operation.SEARCH)) {

			if (searchTemplateTxt.getText().trim().length() == 0 && searchFormTxt.getText().trim().length() == 0) {
				return;
			}
			
			templateLst = null;
			templateTableModel.refresh(null);
			templateTableModel.selectItem(null);
			this.imagePanel.showImage(null);
			templateLst = getTemplateDao().searchTemplates(searchTemplateTxt.getText().trim(),
					searchFormTxt.getText().trim());
			if (templateLst != null && templateLst.size() == 0) {
				return;
			}
			
			templateTableModel.refresh(templateLst);
			this.setLastOperation(Operation.SEARCH);

		} else if (getLastOperation().equals(Operation.LIST_ALL)) {
			this.populateListAll();
		}
		
		if(selectedOne != null) {
			templateTableModel.selectItem(selectedOne);
			this.loadSelectedImage(selectedOne);
		}		
	}

	private void modifyTemplate(Template selectedItem) {
		new CreateOrUpdateImageTemplateDialog(CreateOrUpdateImageTemplateDialog.ModeOfOperation.UPDATE, selectedItem);
		this.refreshResults();
	}

	private Operation getLastOperation() {
		return lastOperation;
	}

	private void setLastOperation(Operation operation) {
		this.lastOperation = operation;
	}

	public void valueChanged(ListSelectionEvent e) {
		this.loadSelectedImage(templateTableModel.getSelectedItem());
	}
}
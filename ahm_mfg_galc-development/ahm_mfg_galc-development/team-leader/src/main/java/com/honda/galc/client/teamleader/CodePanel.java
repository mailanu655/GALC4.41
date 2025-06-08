package com.honda.galc.client.teamleader;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.annotation.AnnotationProcessor;

import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.IPopupMenu;
import com.honda.galc.client.ui.component.PopupMenuMouseAdapter;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.client.ui.tablemodel.CodeTableModel;
import com.honda.galc.common.exception.PropertyException;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.product.CodeDao;
import com.honda.galc.entity.product.Code;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

public class CodePanel extends TabbedPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	private static final String CREATE_CODE = "Create Code";
	private static final String EDIT_CODE = "Edit Code";
	private static final String DELETE_CODE = "Delete Code";
	private static final Font FONT = Fonts.DIALOG_PLAIN_20;

	private CodeDialog codeDialog;
	private CodeDao codeDao;
	private final List<String> codeTypesAllowed;

	private List<Code> codes = new ArrayList<Code>();
	private TablePane codeTablePane;
	private CodeTableModel codeTableModel;

	@SuppressWarnings("deprecation")
	public CodePanel(TabbedMainWindow mainWindow) {
		super("Code Maint", KeyEvent.VK_C, mainWindow);
		{
			final String hostName = getMainWindow().getApplicationContext().getTerminal().getHostName();
			String codeTypesAllowedCsv;
			String[] codeTypesAllowedArray;
			try {
				codeTypesAllowedCsv = PropertyService.getProperty(hostName, "CODE_TYPES_ALLOWED");
			} catch (PropertyException pe) {
				codeTypesAllowedCsv = null;
			}
			if (StringUtils.isNotBlank(codeTypesAllowedCsv)) {
				codeTypesAllowedArray = codeTypesAllowedCsv.split(Delimiter.COMMA);
			} else {
				codeTypesAllowedArray = new String[0];
			}
			Arrays.sort(codeTypesAllowedArray);
			this.codeTypesAllowed = Arrays.asList(codeTypesAllowedArray);
		}
		AnnotationProcessor.process(this);
	}



	/*
	 * Inherited methods
	 */
	@Override
	public void onTabSelected() {
		if (!this.isInitialized) {
			initComponents();
			addListeners();
			this.isInitialized = true;
		}
		refreshCodes();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JMenuItem) {
			Exception exception = null;
			try {
				JMenuItem menuItem = (JMenuItem)e.getSource();
				logUserAction("selected menu item: " + menuItem.getName());
				if (menuItem.getName().equals(CREATE_CODE)) createCode();
				else if (menuItem.getName().equals(EDIT_CODE)) editCode();
				else if (menuItem.getName().equals(DELETE_CODE)) deleteCode();

			} catch(Exception ex) {
				exception = ex;
			}
			handleException(exception);
		}
	}



	/*
	 * Utility methods
	 */
	@Override
	protected JMenuItem createMenuItem(final String name, final boolean enabled) {
		JMenuItem menuItem = super.createMenuItem(name, enabled);
		menuItem.setFont(FONT);
		return menuItem;
	}



	/*
	 * Getter methods
	 */
	private CodeDialog getCodeDialog() {
		if (this.codeDialog == null) {
			this.codeDialog = new CodeDialog(getMainWindow(), this.codeTypesAllowed);
		}
		return this.codeDialog;
	}

	private CodeDao getCodeDao() {
		if (this.codeDao == null) {
			this.codeDao = ServiceFactory.getDao(CodeDao.class);
		}
		return this.codeDao;
	}

	private TablePane getCodeTablePane() {
		if (this.codeTablePane == null) {
			this.codeTablePane = new TablePane(null, ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			this.codeTableModel = new CodeTableModel(this.codeTablePane.getTable(), this.codes);
		}
		return this.codeTablePane;
	}



	/*
	 * Initialization methods
	 */
	protected void initComponents() {
		this.setLayout(new GridLayout(1, 1));
		add(getCodeTablePane());
	}

	private void addListeners() {
		getCodeTablePane().getTable().addMouseListener(createCodeMouseListener());
		getCodeTablePane().addMouseListener(createCodeMouseListener());
	}

	private MouseListener createCodeMouseListener(){
		return new PopupMenuMouseAdapter(new IPopupMenu() {
			public void showPopupMenu(MouseEvent e) {
				showCodePopupMenu(e);
			}
		});  
	}

	private void showCodePopupMenu(MouseEvent e) {
		int rowCount = this.codeTablePane.getTable().getSelectedRowCount();
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(createMenuItem(CREATE_CODE, true));
		popupMenu.add(createMenuItem(EDIT_CODE, rowCount == 1));
		popupMenu.add(createMenuItem(DELETE_CODE, rowCount > 0));
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
	}



	/*
	 * Data load methods
	 */
	private void refreshCodes() {
		this.codes = getCodeDao().findByCodeTypes(this.codeTypesAllowed);
		this.codeTableModel.refresh(this.codes);
	}



	/*
	 * Data edit methods
	 */
	private void createCode() {
		getCodeDialog().showDialog(null, true);
		if (!getCodeDialog().isCanceled()) {
			refreshCodes();
		}
	}

	private void editCode() {
		getCodeDialog().showDialog(getSelectedCode(), false);
		if (!getCodeDialog().isCanceled()) {
			refreshCodes();
		}
	}

	private void deleteCode() {
		if (MessageDialog.confirm(getMainWindow(), "Delete the selected rows?")) {
			List<Code> selectedCodes = getSelectedCodes();
			for (Code code : selectedCodes) {
				getCodeDao().remove(code);
				logUserAction(REMOVED, code);
			}
			refreshCodes();
		}
	}

	private Code getSelectedCode() {
		return this.codeTableModel.getSelectedItem();
	}

	private List<Code> getSelectedCodes() {
		return this.codeTableModel.getSelectedItems();
	}
}

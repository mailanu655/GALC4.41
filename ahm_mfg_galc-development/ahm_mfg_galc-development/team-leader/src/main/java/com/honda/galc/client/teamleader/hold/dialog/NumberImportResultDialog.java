package com.honda.galc.client.teamleader.hold.dialog;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.hold.HoldUtils;
import com.honda.galc.client.ui.ApplicationMainPanel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.PropertiesMapping;
import com.honda.galc.report.TableReport;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>NumberImportResultDialog</code> is ...
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
 * <TD>Karol Wozniak</TD>
 * <TD>Mar 29, 2010</TD>
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
public class NumberImportResultDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private ApplicationMainPanel parentPanel;
	private JLabel messageLabel;
	private ObjectTablePane<Map<String, String>> resultPanel;

	private String filePath;
	private String message;
	private List<Map<String, String>> data;

	private JButton exportButton;
	private JButton cancelButton;
	private JButton submitButton;

	boolean submit;

	private JPopupMenu productPopupMenu;

	public NumberImportResultDialog(ApplicationMainPanel parentPanel, String title, String message, List<Map<String, String>> data, String filePath) {
		super(parentPanel.getMainWindow(), title, true);
		this.parentPanel = parentPanel;
		this.filePath = filePath;
		this.message = message;
		this.data = data;
		setSize(710, 590);
		initialize();
	}

	protected void initialize() {
		setLayout(null);
		messageLabel = createMessageLabel();
		resultPanel = createResultPanel();
		exportButton = createExportButton();
		cancelButton = createCancelButton();
		submitButton = createSubmitButton();

		productPopupMenu = createProductPopupMenu();

		add(getMessageLabel());
		add(getResultPanel());
		add(getExportButton());
		add(getCancelButton());
		add(getSubmitButton());

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);

		mapActions();

		getResultPanel().reloadData(getData());
	}

	protected JLabel createMessageLabel() {
		JLabel component = new JLabel(getMessage(), JLabel.CENTER);
		component.setFont(Fonts.DIALOG_BOLD_14);
		component.setSize(695, 30);
		component.setLocation(5, 5);
		return component;
	}

	protected ObjectTablePane<Map<String, String>> createResultPanel() {

		PropertiesMapping mapping = new PropertiesMapping();
		mapping.put("#", "row");
		mapping.put("Input Number", "number");
		mapping.put("Comment", "comment");
		ObjectTablePane<Map<String, String>> panel = new ObjectTablePane<Map<String, String>>(mapping.get(), true, true);
		panel.getTable().setName("resultPanel");

		Component base = getMessageLabel();

		panel.setSize(695, 450);
		panel.setLocation(base.getX(), base.getY() + base.getHeight() + 5);

		return panel;
	}
	
	protected JButton createExportButton() {
		JButton element = new JButton();
		Component base = getResultPanel();
		element.setSize(150, 50);
		element.setLocation(base.getX(), base.getY() + base.getHeight() + 5);
		element.setFont(Fonts.DIALOG_BOLD_16);
		element.setText("Export");
		element.setMnemonic(KeyEvent.VK_E);
		return element;
	}

	protected JButton createCancelButton() {
		JButton element = new JButton();
		Component base = getResultPanel();
		element.setSize(150, 50);
		element.setLocation(393, base.getY() + base.getHeight() + 5);
		element.setFont(Fonts.DIALOG_BOLD_16);
		element.setText("Cancel");
		element.setMnemonic(KeyEvent.VK_C);
		return element;
	}

	protected JButton createSubmitButton() {
		JButton element = new JButton();
		Component base = getCancelButton();
		element.setSize(base.getWidth(), base.getHeight());
		element.setLocation(base.getX() + base.getWidth() + 5, base.getY());
		element.setFont(Fonts.DIALOG_BOLD_16);
		element.setText("OK");
		element.setMnemonic(KeyEvent.VK_S);
		return element;
	}

	protected JPopupMenu createProductPopupMenu() {
		JPopupMenu popup = new JPopupMenu();

		// TODO
		JMenuItem menuItem = new JMenuItem("Export Selected");
		// menuItem.addActionListener(new
		// ExportInvalidDataAction(getParentPanel(), this, true));
		menuItem.setActionCommand("selected");
		popup.add(menuItem);

		menuItem = new JMenuItem("Export All");
		// menuItem.addActionListener(new
		// ExportInvalidDataAction(getParentPanel(), this, false));
		popup.add(menuItem);

		return popup;
	}
	
	public TableReport getReport() {
		TableReport report = TableReport.createXlsxTableReport();
		if (report != null) {
			return report;
		}
		return report;
	}
	
	private Component getView(){
		return this;
	}
	
	private TableReport createRejectedIDsReport() {
		TableReport report = TableReport.createXlsxTableReportPoi();
		report.setTitle("Rejected Product IDs");
		report.addColumn("#", "#", 1000);
		report.addColumn("number", "Input Number", 7000);
		report.addColumn("comment", "Comment", 3000);
		List<Map<String, String>> rows = new ArrayList<Map<String, String>>();
		int idx = 0;
		for (Map<String,String> row : this.getData()) {
			idx++;
			row.put("#", Integer.toString(idx));
			rows.add(row);
		}
		report.setData(rows);
		return report;
	}

	// === action mappings ===//
	private void mapActions() {
		getCancelButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		getExportButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String defaultPath = new File(filePath).getParent();
				if (StringUtils.isBlank(defaultPath) || !(new File(defaultPath).exists()))
					defaultPath = System.getProperty("user.home");
				String fileName = "RejectedProductIDs_"+ new SimpleDateFormat("yyyy-MM-dd-HHmmss").format(new Date()) + ".xlsx";
				File file = HoldUtils.popupSaveDialog(getView(), defaultPath, fileName);
				if (file == null || file.getAbsolutePath() == null)	return;
				createRejectedIDsReport().export(file.getAbsolutePath());
			}
		});

		getSubmitButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setSubmit(true);
				dispose();
			}
		});

		MouseListener mouseListener = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				showPopupMenu(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				showPopupMenu(e);
			}

			protected void showPopupMenu(MouseEvent e) {
				if (e.isPopupTrigger()) {
					boolean productsSelected = getResultPanel().getSelectedValue() != null;
					boolean productsListed = getResultPanel().getTable().getRowCount() > 0;
					getProductPopupMenu().getSubElements()[0].getComponent().setEnabled(productsSelected);
					getProductPopupMenu().getSubElements()[1].getComponent().setEnabled(productsListed);
					getProductPopupMenu().show(e.getComponent(), e.getX(), e.getY());
				}
			}
		};

		getResultPanel().addMouseListener(mouseListener);
		getResultPanel().getTable().addMouseListener(mouseListener);
	}

	public ObjectTablePane<Map<String, String>> getResultPanel() {
		return resultPanel;
	}

	public List<Map<String, String>> getData() {
		return data;
	}

	public String getMessage() {
		return message;
	}

	public JLabel getMessageLabel() {
		return messageLabel;
	}

	public ApplicationMainPanel getParentPanel() {
		return parentPanel;
	}
	
	public JButton getExportButton() {
		return exportButton;
	}

	public JButton getCancelButton() {
		return cancelButton;
	}

	public JButton getSubmitButton() {
		return submitButton;
	}

	public boolean isSubmit() {
		return submit;
	}

	protected void setSubmit(boolean submit) {
		this.submit = submit;
	}

	public JPopupMenu getProductPopupMenu() {
		return productPopupMenu;
	}
}

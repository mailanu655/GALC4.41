package com.honda.galc.client.qics.view.dialog;

import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;

import com.honda.galc.client.qics.view.fragments.CheckResultsPane;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.ObjectTablePane;

/**
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * Popup dialog to present user with product check results.
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
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
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
public class ProductCheckResultsDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel contentPanel;
	private ObjectTablePane<String> tablePane;
	private JButton okButton;
	private JLabel messageLabel;

	private CheckResultsPane scrollPane;

	Map<String, Object> productCheckResults;
	Map<String, Object> productWarnCheckResults;

	public ProductCheckResultsDialog(Frame owner) {
		super(owner);
		setModal(true);
		initialize();
	}

	public ProductCheckResultsDialog(Frame owner, Map<String, Object> productCheckResults) {
		super(owner);
		setProductCheckResults(productCheckResults);
		setModal(true);
		initialize();
	}

	private void initialize() {
		setLayout(null);
		setSize(500, 370);
		setContentPane(getContentPanel());
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		mapActions();

		getRootPane().setDefaultButton(getOkButton());
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				getOkButton().requestFocusInWindow();
			}
		});
	}

	private JPanel getContentPanel() {
		if (contentPanel == null) {
			contentPanel = new JPanel();
			contentPanel.setLayout(null);
			contentPanel.add(getMessageLabel());
			contentPanel.add(getScrollPane());
			contentPanel.add(getOkButton());
		}
		return contentPanel;
	}

	public JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new CheckResultsPane();
			scrollPane.setSize(getWidth() - 16, getHeight() - 110);
			scrollPane.setLocation(5, getMessageLabel().getY() + 30);
			scrollPane.reloadProductCheckData();
		}
		return scrollPane;
	}

	public ObjectTablePane<String> getTablePane() {
		if (tablePane == null) {
			tablePane = new ObjectTablePane<String>("Part Name");
			tablePane.setSize(getWidth() - 16, getHeight() - 110);
			tablePane.setLocation(new Point(5, 30));
			DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
			renderer.setHorizontalAlignment(SwingConstants.CENTER);
			tablePane.getTable().getColumnModel().getColumn(0).setCellRenderer(renderer);
			tablePane.getTable().setFont(Fonts.DIALOG_BOLD_12);
		}
		return tablePane;
	}

	public JButton getOkButton() {
		if (okButton == null) {
			int width = 100;
			int height = 30;
			okButton = new JButton();
			okButton.setText("Ok");
			okButton.setFont(Fonts.DIALOG_PLAIN_18);
			okButton.setMnemonic(KeyEvent.VK_O);
			okButton.setSize(width, height);
			int x = getWidth() / 2 - width / 2;
			int y = getTablePane().getY() + getTablePane().getHeight() + 10;
			okButton.setLocation(x, y);
		}
		return okButton;
	}

	private void mapActions() {
		getOkButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ProductCheckResultsDialog.this.dispose();
			}
		});
	}

	protected JLabel getMessageLabel() {
		if (messageLabel == null) {
			messageLabel = new JLabel();
			messageLabel.setSize(getWidth() - 20, 30);
			messageLabel.setLocation(10, 0);
		}
		return messageLabel;
	}

	public void setMessage(String message) {
		getMessageLabel().setText(message);
	}

	public Map<String, Object> getProductCheckResults() {
		return productCheckResults;
	}

	public void setProductCheckResults(Map<String, Object> productCheckResults) {
		this.productCheckResults = productCheckResults;
	}

	public Map<String, Object> getProductWarnCheckResults() {
		return productWarnCheckResults;
	}

	public void setProductWarnCheckResults(Map<String, Object> productWarnCheckResults) {
		this.productWarnCheckResults = productWarnCheckResults;
	}
}

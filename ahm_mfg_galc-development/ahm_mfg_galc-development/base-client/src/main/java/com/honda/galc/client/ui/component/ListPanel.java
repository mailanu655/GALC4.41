package com.honda.galc.client.ui.component;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * Container for list.
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
public class ListPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel title;
	private JScrollPane scrollPane;
	private JList list;
	private boolean valueChanged;

	public ListPanel(String title) {
		getTitle().setText(title);
		initialize();

	}

	protected void initialize() {
		setLayout(new GridBagLayout());
		add(getTitle(), getLabelConstraints());
		add(getScrollPane(), getScrollPanelConstraints());
		getList().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				setValueChanged(true);
			}
		});
		getList().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (isValueChanged()) {
					setValueChanged(false);
				} else {
					if (getList().isEnabled()) {
						getList().clearSelection();
					}
				}
			}
		});
	}

	public JLabel getTitle() {
		if (title == null) {
			title = new JLabel();
		}
		return title;
	}

	public JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane(getList());
			scrollPane.setPreferredSize(new Dimension(100, 150));
			scrollPane.setAlignmentX(LEFT_ALIGNMENT);
		}
		return scrollPane;
	}

	public JList getList() {
		if (list == null) {
			list = new JList(new DefaultListModel());
			list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			list.setLayoutOrientation(JList.VERTICAL);
			list.setVisibleRowCount(-1);
			list.setSize(100, 200);
		}
		return list;
	}

	protected GridBagConstraints getLabelConstraints() {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		// constraints.insets = new Insets(5, 5, 5, 5);
		constraints.weighty = 0;
		return constraints;
	}

	protected GridBagConstraints getScrollPanelConstraints() {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		// getComponentCount()
		// constraints.gridy = 1;
		constraints.gridy = getComponentCount();
		// constraints.insets = new Insets(5, 0, 5, 5);
		constraints.weighty = 1;
		constraints.weightx = 1;
		constraints.fill = GridBagConstraints.BOTH;
		return constraints;
	}

	public void addData(List<String> data) {
		if (data == null) {
			return;
		}
		DefaultListModel dataModel = (DefaultListModel) getList().getModel();
		for (String element : data) {
			dataModel.addElement(element);
		}
	}

	public void removeData() {
		DefaultListModel dataModel = (DefaultListModel) getList().getModel();
		dataModel.clear();
	}

	public boolean isValueChanged() {
		return valueChanged;
	}

	public void setValueChanged(boolean valueChanged) {
		this.valueChanged = valueChanged;
	}
}

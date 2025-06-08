package com.honda.galc.client.ui.component;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import org.bushe.swing.event.EventBus;

import com.honda.galc.client.ui.event.ParentPartNameSelectionEvent;
import com.honda.galc.client.ui.event.SelectionEvent;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.PartNameDao;
import com.honda.galc.entity.product.PartName;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.SortedArrayList;

public class ParentPartNameSelectionPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private FilteredLabeledComboBox partNameComboBox;
	private String productType;

	public ParentPartNameSelectionPanel(String productType) {
		super();
		this.productType = productType;
		initComponent();
		addActionListeners();
	}

	public void initComponent() {
		LayoutManager layout = new GridBagLayout();
		setLayout(layout);
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 0.1;
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(getParentPartNameComboBox(), c);
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		initParentPart();
	}

	public FilteredLabeledComboBox getParentPartNameComboBox() {
		if (partNameComboBox == null) {
			partNameComboBox = new FilteredLabeledComboBox("Part Name");
			partNameComboBox.getComponent().setName("PartNameComboBox");
		}
		return partNameComboBox;
	}

	public void initParentPart() {
		List<PartName> Parts = ServiceFactory.getDao(PartNameDao.class).findAll();
		SortedArrayList<PartName> sortedParts = new SortedArrayList<PartName>(Parts);
		getParentPartNameComboBox().setModel(new ComboBoxModel<PartName>(sortedParts, "getPartName"));
		getParentPartNameComboBox().setSelectedIndex(-1);
	}

	public void addActionListeners() {
		getParentPartNameComboBox().getComponent().addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(getParentPartNameComboBox().getComponent()))
			parentPartNameChanged();
	}

	public SortedArrayList<PartName> loadParentPartName() {
		List<PartName> parentPartName = ServiceFactory.getDao(PartNameDao.class).findAll();
		return new SortedArrayList<PartName>(parentPartName);
	}

	public String getParentPartName() {
		PartName partName = getSelectedPartName();
		if (partName == null)
			return null;
		return partName.getPartName();
	}

	public boolean isParentPartNameSelected() {
		return getParentPartName() != null;
	}

	private void parentPartNameChanged() {
		if (getParentPartNameComboBox().getComponent().getSelectedItem() != null)
			Logger.getLogger()
					.info(getParentPartNameComboBox().getComponent().getSelectedItem().toString() + " is selected");
		EventBus.publish(new ParentPartNameSelectionEvent(this, SelectionEvent.PARENT_PART_SELECTED));
	}

	public PartName getSelectedPartName() {
		return (PartName) getParentPartNameComboBox().getComponent().getSelectedItem();
	}

	public String getProductType() {
		return this.productType;
	}
}
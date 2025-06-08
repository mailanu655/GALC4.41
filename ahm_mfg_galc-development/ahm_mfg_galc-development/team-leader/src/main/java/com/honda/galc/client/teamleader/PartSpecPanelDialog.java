package com.honda.galc.client.teamleader;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;

import com.honda.galc.client.product.view.UiFactory;
import com.honda.galc.client.teamleader.model.MeasurementSpecCopyTableModel;
import com.honda.galc.client.teamleader.model.PartIdSelection;
import com.honda.galc.client.teamleader.model.PartIdSelectionTableModel;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.dao.product.MeasurementSpecDao;
import com.honda.galc.dao.product.PartSpecDao;
import com.honda.galc.entity.product.MeasurementSpec;
import com.honda.galc.entity.product.MeasurementSpecId;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.entity.product.PartSpecId;
import com.honda.galc.service.ServiceFactory;

public class PartSpecPanelDialog extends JDialog  {

	private static final long serialVersionUID = 1L;

	private JPanel buttonPanel;
	private JButton saveButton;
	private JButton cancelButton;
	private TablePane partSpecPanel = new TablePane("Part Id Selection");
	private TablePane measurementSpecPanel = new TablePane("Measurement Spec");
	private MeasurementSpecCopyTableModel measurementSpecTableModel = new MeasurementSpecCopyTableModel(
			measurementSpecPanel.getTable(), null);
	private PartIdSelectionTableModel partIdSelectionTableModel = new PartIdSelectionTableModel(
			partSpecPanel.getTable(), null);

	private String partName;
	private String partId;
	private List<MeasurementSpec> measSpecs;
	
	public PartSpecPanelDialog(Frame owner, String partId, String partName,
			List<MeasurementSpec> measSpecs) {
		super(owner, "Copy Measurement Spec");
		this.partId = partId;
		this.partName = partName;
		this.measSpecs = measSpecs;
		
		setSize(600, 600);
		initComponents();
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	private void initComponents() {
		JPanel panel = new JPanel(new BorderLayout());

		JPanel labelPanel = new JPanel();
		labelPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

		String labelString = "Current PartId to Copy From: " + partId;
		JLabel copyfromLabel = UiFactory.getInfo().createLabel(labelString, SwingConstants.CENTER);
		labelPanel.add(copyfromLabel);

		JPanel boxPanel = new JPanel();
		boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));

		JSplitPane splitPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, getMeasSpecPanel(),
				getPartIdSelectionPanel());
		splitPanel.setDividerLocation(150);

		boxPanel.add(splitPanel);

		panel.add(labelPanel, BorderLayout.NORTH);
		panel.add(boxPanel, BorderLayout.CENTER);

		panel.add(getButtonPanel(), BorderLayout.SOUTH);

		add(panel, BorderLayout.CENTER);
		
		loadData();

	}

	private Component getPartIdSelectionPanel() {
		return partSpecPanel;
	}

	private Component getMeasSpecPanel() {
		return measurementSpecPanel;
	}


	private Component getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 10));
			buttonPanel.add(getSaveButton());
			buttonPanel.add(getCancelButton());
		}
		return buttonPanel;
	}

	public JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton("Cancel");
			cancelButton.setName("Cancel");
		}
		return cancelButton;
	}

	public JButton getSaveButton() {
		if (saveButton == null) {
			saveButton = new JButton("Save");
			saveButton.setName("Save");
		}
		return saveButton;
	}

	public void copyMeasurementSpecs() {
		List<PartIdSelection> partIdList = partIdSelectionTableModel.getItems();
		
		for (PartIdSelection partIdSelection : partIdList) {
			if (partIdSelection.isApply()) {
				PartSpecId partSpecId = new PartSpecId();
				partSpecId.setPartName(partName);
				partSpecId.setPartId(partIdSelection.getPartId());
				PartSpec partSpec = ServiceFactory.getDao(PartSpecDao.class).findByKey(partSpecId);

				if (partSpec != null) {

					// If there are current measurement specs for the part Id
					// they system will remove the current measurement specs and
					// copy the new measurement specs
					List<MeasurementSpec> oldMeasSpecs = partSpec.getMeasurementSpecs();
					if (oldMeasSpecs != null && oldMeasSpecs.size() > 0) {
						for (MeasurementSpec measSpec : oldMeasSpecs) {
							ServiceFactory.getDao(MeasurementSpecDao.class).remove(measSpec);
						}
					}
					List<MeasurementSpec> measSpecsCopy = new ArrayList<MeasurementSpec>();
					for(MeasurementSpec measSpec: measSpecs){
						MeasurementSpecId measurementSpecId = new MeasurementSpecId();
						measurementSpecId.setPartId(partIdSelection.getPartId());
						measurementSpecId.setPartName(partName);
						measurementSpecId.setMeasurementSeqNum(measSpec.getId().getMeasurementSeqNum());
						
						MeasurementSpec measurementSpec = new MeasurementSpec();
						measurementSpec.setId(measurementSpecId);
						measurementSpec.setMinimumLimit(measSpec.getMinimumLimit());
						measurementSpec.setMaximumLimit(measSpec.getMaximumLimit());
						measurementSpec.setMaxAttempts(measSpec.getMaxAttempts());
						
						ServiceFactory.getDao(MeasurementSpecDao.class).save(measurementSpec);
						
						measSpecsCopy.add(measurementSpec);
						
					}
					partSpec.setMeasurementSpecs(measSpecsCopy);
					partSpec.setMeasurementCount(measSpecsCopy.size());
					
					ServiceFactory.getDao(PartSpecDao.class).save(partSpec);
				}
			}
		}
	}

	private void loadData() {
		List<PartSpec> partSpecs = ServiceFactory.getDao(PartSpecDao.class).findAllByPartName(this.partName);
		List<PartIdSelection> partIdList = new ArrayList<PartIdSelection>();
		for (PartSpec partSpec : partSpecs) {
			if (!partSpec.getId().getPartId().equalsIgnoreCase(partId)) {
				PartIdSelection partId = new PartIdSelection();
				partId.setPartId(partSpec.getId().getPartId());
				partId.setDescription(partSpec.getPartDescription());
				partId.setPartNumber(partSpec.getPartNumber());
				partId.setPartMark(partSpec.getPartMark());
				partId.setPartMask(partSpec.getPartSerialNumberMask());
				partId.setApply(false);

				partIdList.add(partId);
			}
		}
		partIdSelectionTableModel.refresh(partIdList);

		measurementSpecTableModel.refresh(measSpecs);
	}
	
}

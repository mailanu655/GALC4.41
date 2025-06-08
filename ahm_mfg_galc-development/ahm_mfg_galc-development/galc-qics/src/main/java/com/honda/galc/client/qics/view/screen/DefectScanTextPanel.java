package com.honda.galc.client.qics.view.screen;




import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import com.honda.galc.client.qics.view.action.ScanTextInputAction;
import com.honda.galc.client.qics.view.constants.QicsViewId;
import com.honda.galc.client.qics.view.frame.QicsFrame;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.entity.qics.DefectResult;





public class DefectScanTextPanel extends QicsPanel {

	private static final long serialVersionUID = 1L;

	private ObjectTablePane<DefectResult> defectsPane;
	private DefectScanTextInputPanel defectScanInputPane;

	public DefectScanTextPanel(QicsFrame frame) {
		super(frame);
		initialize();
	}



	public QicsViewId getQicsViewId() {
		return QicsViewId.DEFECT_SCAN_TEXT;
	}

	protected void initialize() {
		setLayout(null);
		setSize(getTabPaneWidth(), getTabPaneHeight());
		defectScanInputPane = createDefectScanInputPane();
		defectsPane = createDefectsPane();

		add(getDefectScanInputPane());
		add(getDefectsPane());

		mapActions();



	}

	public void setFocus(final JComponent component) {
		if (component == null) {
			return;
		}
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				component.requestFocusInWindow();
			}
		});
	}


	@Override
	public void startPanel() {
		getDefectScanInputPane().resetPanel();
		resetDefectTable();
		setFocus(getDefectScanInputPane().getEntryTextField());	
	}


	public DefectScanTextInputPanel getDefectScanInputPane() {
		return defectScanInputPane;
	}

	protected ObjectTablePane<DefectResult> createDefectsPane() {
		int width = 1000;
		int height = 400;

		ColumnMappings columnMappings = 
			ColumnMappings.with("Status", "defectStatus")
			.put("Defect", "defectTypeName").put("Location", "inspectionPartLocationName").put("Resp. Dept.", "responsibleDept")
			.put("Entry Station", "entryDept");



		ObjectTablePane<DefectResult> pane = new ObjectTablePane<DefectResult>(columnMappings.get(),true);

		pane.setSize(width, height);
		pane.setTitle("Mbpn Product");
		pane.setLocation(0, createDefectScanInputPane().getY() + createDefectScanInputPane().getHeight());
		pane.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		setColumnSize(pane);
		return pane;
	}

	protected void setColumnSize(ObjectTablePane<DefectResult> pane) {
		Map<String, Integer> columnWidths = new HashMap<String, Integer>();
		columnWidths.put("Status", 200);
		columnWidths.put("Defect", 200);
		columnWidths.put("Location", 200);
		columnWidths.put("Resp. Dept.", 200);
		columnWidths.put("Entry Station", 200);		
	}


	public DefectScanTextInputPanel createDefectScanInputPane() {
		DefectScanTextInputPanel panel = new DefectScanTextInputPanel(1000, 120);
		panel.setLocation(0, 0);
		return panel;
	}


	@Override
	protected void mapActions() {

		ScanTextInputAction entryAction = new ScanTextInputAction(this) ;
		defectScanInputPane.getEntryTextField().setAction(entryAction);
	}


	public ObjectTablePane<DefectResult> getDefectsPane() {
		return defectsPane;
	}


	public void setDispayPane(ObjectTablePane<DefectResult> dispayPane) {
		this.defectsPane = dispayPane;
	}


	public void setInputPane(DefectScanTextInputPanel inputPane) {
		this.defectScanInputPane = inputPane;
	}

	public void resetDefectTable() {

		Vector<DefectResult> totalDefect = new Vector<DefectResult>();

		if (getProductModel().getExistingDefects() != null && getProductModel().getExistingDefects().size() > 0) {
			totalDefect.addAll(getProductModel().getExistingDefects());
		}

		if (getProductModel().getNewDefects() != null && getProductModel().getNewDefects().size() > 0) {
			totalDefect.addAll(getProductModel().getNewDefects());
		}
		getDefectsPane().removeData();
		getDefectsPane().reloadData(totalDefect);

	}




}

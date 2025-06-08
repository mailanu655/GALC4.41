package com.honda.galc.client.teamleader.bearing;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.bearing.BearingMatrixCell;
import com.honda.galc.entity.bearing.BearingMatrixCellId;
import com.honda.galc.entity.bearing.BearingPart;
import com.honda.galc.entity.bearing.BearingPartType;
import com.honda.galc.entity.bearing.BearingType;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>BearingMatrixPanel</code> is ... .
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
 * @created May 17, 2013
 */
public class BearingMatrixPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final Color IDLE_COLOR = new Color(240, 240, 240);

	private BearingMatrixMaintenancePanel parentPanel;

	private BearingType bearingType;
	private List<String> columnValues;
	private List<String> rowValues;

	// === ix by column and row values === //
	// === Map<RowValue, Map<ColValue, JTextField >> === //
	private Map<String, Map<String, JTextField>> upperBearingPartFieldIx;
	private Map<String, Map<String, JTextField>> lowerBearingPartFieldIx;

	// === ix by field === //
	private Map<JTextField, BearingPart> upperBearingPartIx;
	private Map<JTextField, BearingPart> lowerBearingPartIx;
	private Map<JTextField, BearingMatrixCell> bearingMatrixCellIx;

	public BearingMatrixPanel(BearingMatrixMaintenancePanel parentPanel, BearingType bearingType, String[] columnValues, String[] rowValues) {
		this.parentPanel = parentPanel;
		this.bearingType = bearingType;
		this.columnValues = new ArrayList<String>();
		this.rowValues = new ArrayList<String>();

		this.upperBearingPartFieldIx = new HashMap<String, Map<String, JTextField>>();
		this.lowerBearingPartFieldIx = new HashMap<String, Map<String, JTextField>>();

		this.upperBearingPartIx = new HashMap<JTextField, BearingPart>();
		this.lowerBearingPartIx = new HashMap<JTextField, BearingPart>();
		this.bearingMatrixCellIx = new HashMap<JTextField, BearingMatrixCell>();

		if (columnValues != null) {
			getColumnValues().addAll(Arrays.asList(columnValues));
		}

		if (rowValues != null) {
			getRowValues().addAll(Arrays.asList(rowValues));
		}
		initView();
	}

	protected void initView() {
		StringBuilder sb = new StringBuilder("[50!,fill]");
		for (int i = 1; i < getColumnValues().size(); i++) {
			sb.append("[max,fill]");
		}
		setLayout(new MigLayout("insets 0, gap 0", sb.toString()));
		createColumnHeader();
		createRows();
	}

	// === bl api === //
	public void loadData(List<BearingPart> bearingParts, List<BearingMatrixCell> matrixCells) {
		clearData();
		if (matrixCells == null || matrixCells.isEmpty()) {
			return;
		}
		for (BearingMatrixCell bmc : matrixCells) {

			String rowValue = bmc.getId().getRowMeasurement();
			String columnValue = bmc.getId().getColumnMeasurement();

			JTextField lower = getLowerBearingPartField(rowValue, columnValue);
			JTextField upper = getUpperBearingPartField(rowValue, columnValue);
			setBearingMatrixCell(lower, bmc);
			setBearingMatrixCell(upper, bmc);

			if (lower != null) {
				setLowerBearingPart(lower, null);
			}
			if (upper != null) {
				setUpperBearingPart(upper, null);
			}

			if (lower != null && StringUtils.isNotBlank(bmc.getLowerBearing())) {
				BearingPart ixLower = new BearingPart();
				ixLower.setId(bmc.getLowerBearing());
				BearingPart bpLower = bearingParts.get(bearingParts.indexOf(ixLower));
				setLowerBearingPart(lower, bpLower);
			}

			if (upper != null && StringUtils.isNotBlank(bmc.getUpperBearing())) {
				BearingPart ixUpper = new BearingPart();
				ixUpper.setId(bmc.getUpperBearing());
				BearingPart bpUpper = bearingParts.get(bearingParts.indexOf(ixUpper));
				setUpperBearingPart(upper, bpUpper);
			}
		}
	}

	public void clearData() {
		for (String rowValue : getRowValues()) {
			for (String columnValue : getColumnValues()) {
				JTextField lower = getLowerBearingPartField(rowValue, columnValue);
				JTextField upper = getUpperBearingPartField(rowValue, columnValue);
				setBearingMatrixCell(lower, null);
				setBearingMatrixCell(upper, null);
				setLowerBearingPart(lower, null);
				setUpperBearingPart(upper, null);
			}
		}
	}

	public boolean isUpdated() {
		for (JTextField field : getLowerBearingPartFields()) {
			if (isLowerBearingPartUpdated(getBearingMatrixCell(field), getLowerBearingPart(field))) {
				return true;
			}
		}
		for (JTextField field : getUpperBearingPartFields()) {
			if (isUpperBearingPartUpdated(getBearingMatrixCell(field), getUpperBearingPart(field))) {
				return true;
			}
		}
		return false;
	}

	public List<BearingMatrixCell> getUpdatedBearingMatrixCells() {
		List<BearingMatrixCell> list = new ArrayList<BearingMatrixCell>();

		Map<?, ?> map = (Map<?, ?>) getParentPanel().getYearModelComboBox().getSelectedItem();
		String modelYearCode = (String) map.get("modelYearCode");
		String modelCode = (String) map.get("modelCode");
		String modelTypeCode = (String) getParentPanel().getModelTypeComboBox().getSelectedItem();
		String journalPosition = (String) getParentPanel().getJournalPositionComboBox().getSelectedItem();

		for (String rowValue : getRowValues()) {
			for (String columnValue : getColumnValues()) {
				JTextField lower = getLowerBearingPartField(rowValue, columnValue);
				JTextField upper = getUpperBearingPartField(rowValue, columnValue);
				BearingMatrixCell bmc = getBearingMatrixCell(lower);
				if (bmc == null) {
					bmc = getBearingMatrixCell(upper);
				}
				if (bmc == null) {
					bmc = new BearingMatrixCell();
					BearingMatrixCellId id = new BearingMatrixCellId();
					id.setModelYearCode(modelYearCode);
					id.setModelCode(modelCode);
					id.setModelTypeCode(modelTypeCode);
					id.setJournalPosition(journalPosition);
					id.setBearingType(getBearingType().name());
					id.setColumnMeasurement(columnValue);
					id.setRowMeasurement(rowValue);
					bmc.setId(id);
				}

				BearingPart bpLower = getLowerBearingPart(lower);
				if (isLowerBearingPartUpdated(bmc, bpLower)) {
					bmc.setLowerBearing(bpLower.getId());
					if (!list.contains(bmc)) {
						list.add(bmc);
					}
				}
				BearingPart bpUpper = getUpperBearingPart(upper);
				if (isUpperBearingPartUpdated(bmc, bpUpper)) {
					bmc.setUpperBearing(bpUpper.getId());
					if (!list.contains(bmc)) {
						list.add(bmc);
					}
				}
			}
		}
		return list;
	}

	public List<JTextField> toList(Map<String, Map<String, JTextField>> ix) {
		List<JTextField> list = new ArrayList<JTextField>();
		if (ix == null || ix.isEmpty()) {
			return list;
		}
		for (String rowValue : ix.keySet()) {
			Map<String, JTextField> row = ix.get(rowValue);
			list.addAll(row.values());
		}
		return list;
	}

	protected boolean isLowerBearingPartUpdated(BearingMatrixCell bmc, BearingPart bp) {
		if (bmc == null && bp == null) {
			return false;
		}
		if (bmc == null && bp != null) {
			return true;
		}
		if (bmc != null && bp == null) {
			return false;
		}
		return !StringUtils.equals(bmc.getLowerBearing(), bp.getId());
	}

	protected boolean isUpperBearingPartUpdated(BearingMatrixCell bmc, BearingPart bp) {
		if (bmc == null && bp == null) {
			return false;
		}
		if (bmc == null && bp != null) {
			return true;
		}
		if (bmc != null && bp == null) {
			return false;
		}
		return !StringUtils.equals(bmc.getUpperBearing(), bp.getId());
	}

	protected boolean isComplete() {
		for (JTextField field : getLowerBearingPartFields()) {
			if (getLowerBearingPart(field) == null) {
				return false;
			}
		}

		for (JTextField field : getUpperBearingPartFields()) {
			if (getUpperBearingPart(field) == null) {
				return false;
			}
		}
		return true;
	}

	protected void setUpperBearingPart(JTextField field, BearingPart item) {
		getUpperBearingPartIx().put(field, item);
		if (item == null) {
			field.setBackground(getIdleColor());
			field.setToolTipText("");
		} else {
			field.setBackground(getColor(item.getColor()));
			field.setForeground(getForegroundColor(field.getBackground()));
			field.setToolTipText(item.getId());
		}

		if (isUpperBearingPartUpdated(getBearingMatrixCell(field), item)) {
			field.setText("*");
		} else {
			field.setText("");
		}
	}

	protected void setLowerBearingPart(JTextField field, BearingPart item) {
		getLowerBearingPartIx().put(field, item);
		if (item == null) {
			field.setBackground(getIdleColor());
			field.setToolTipText("");
		} else {
			field.setBackground(getColor(item.getColor()));
			field.setForeground(getForegroundColor(field.getBackground()));
			field.setToolTipText(item.getId());
		}

		if (isLowerBearingPartUpdated(getBearingMatrixCell(field), item)) {
			field.setText("*");
		} else {
			field.setText("");
		}
	}

	// === factory methods === //
	protected void createColumnHeader() {
		JTextField h00 = createColumnValueField(null);
		add(h00, new CC().width("max"));
		for (int i = 0; i < getColumnValues().size(); i++) {
			CC cc = new CC().width("max");
			if (i == getColumnValues().size() - 1) {
				cc.wrap();
			}
			JTextField field = createColumnValueField(getColumnValues().get(i).toString());
			add(field, cc);
		}
	}

	protected void createRows() {
		for (int j = 0; j < getRowValues().size(); j++) {
			String rowValue = getRowValues().get(j);
			JPanel p = new JPanel(new MigLayout("insets 0,gap 0", "[max,fill][max,fill]"));
			p.add(createColumnValueField(rowValue), new CC().width("max").height("max").spanY(2));
			p.add(createColumnValueField(BearingPartType.Lower.name().substring(0, 1)), new CC().width("max").height("max").wrap());
			p.add(createColumnValueField(BearingPartType.Upper.name().substring(0, 1)), new CC().width("max").height("max"));

			add(p);
			for (int i = 0; i < getColumnValues().size(); i++) {
				CC cc = new CC().width("max").height("max");
				if (i == getColumnValues().size() - 1) {
					cc.wrap();
				}
				String columnValue = getColumnValues().get(i);
				JPanel panel = new JPanel(new GridLayout(2, 1));
				JTextField lowerField = createBearingPartField();
				JTextField upperField = createBearingPartField();
				panel.add(lowerField);
				panel.add(upperField);

				putUpperBearingPartField(rowValue, columnValue, upperField);
				putLowerBearingPartField(rowValue, columnValue, lowerField);
				add(panel, cc);
			}
		}
	}

	protected JTextField createRowValueField(String text) {
		JTextField field = new JTextField();
		if (text != null) {
			field.setText(text);
		}
		field.setEditable(false);
		field.setHorizontalAlignment(JTextField.CENTER);
		field.setFont(field.getFont().deriveFont(Font.BOLD));
		return field;
	}

	protected JTextField createColumnValueField(String text) {
		JTextField field = new JTextField();
		if (text != null) {
			field.setText(text);
		}
		field.setEditable(false);
		field.setHorizontalAlignment(JTextField.CENTER);
		field.setFont(field.getFont().deriveFont(Font.BOLD));
		return field;
	}

	protected JTextField createBearingPartField() {
		JTextField field = new JTextField();
		field.setEditable(false);
		field.setBackground(getIdleColor());
		return field;
	}

	// === get/set === //
	protected List<String> getColumnValues() {
		return columnValues;
	}

	protected List<String> getRowValues() {
		return rowValues;
	}

	protected BearingType getBearingType() {
		return bearingType;
	}

	protected BearingMatrixMaintenancePanel getParentPanel() {
		return parentPanel;
	}

	// === get === //
	protected Map<String, Map<String, JTextField>> getUpperBearingPartFieldIx() {
		return upperBearingPartFieldIx;
	}

	protected JTextField getUpperBearingPartField(String rowValue, String columnValue) {
		Map<String, JTextField> row = getUpperBearingPartFieldIx().get(rowValue);
		if (row == null) {
			return null;
		}
		JTextField item = row.get(columnValue);
		return item;
	}

	protected void putUpperBearingPartField(String rowValue, String columnValue, JTextField item) {
		Map<String, JTextField> row = getUpperBearingPartFieldIx().get(rowValue);
		if (row == null) {
			row = new HashMap<String, JTextField>();
			getUpperBearingPartFieldIx().put(rowValue, row);
		}
		row.put(columnValue, item);
	}

	protected Map<String, Map<String, JTextField>> getLowerBearingPartFieldIx() {
		return lowerBearingPartFieldIx;
	}

	public List<JTextField> getUpperBearingPartFields() {
		return toList(getUpperBearingPartFieldIx());
	}

	public List<JTextField> getLowerBearingPartFields() {
		return toList(getLowerBearingPartFieldIx());
	}

	public List<JTextField> getBearingPartFields() {
		List<JTextField> list = new ArrayList<JTextField>();
		list.addAll(getLowerBearingPartFields());
		list.addAll(getUpperBearingPartFields());
		return list;
	}

	protected JTextField getLowerBearingPartField(String rowValue, String columnValue) {
		Map<String, JTextField> row = getLowerBearingPartFieldIx().get(rowValue);
		if (row == null) {
			return null;
		}
		JTextField item = row.get(columnValue);
		return item;
	}

	protected void putLowerBearingPartField(String rowValue, String columnValue, JTextField item) {
		Map<String, JTextField> row = getLowerBearingPartFieldIx().get(rowValue);
		if (row == null) {
			row = new HashMap<String, JTextField>();
			getLowerBearingPartFieldIx().put(rowValue, row);
		}
		row.put(columnValue, item);
	}

	protected Map<JTextField, BearingPart> getUpperBearingPartIx() {
		return upperBearingPartIx;
	}

	protected BearingPart getUpperBearingPart(JTextField field) {
		return getUpperBearingPartIx().get(field);
	}

	protected Map<JTextField, BearingPart> getLowerBearingPartIx() {
		return lowerBearingPartIx;
	}

	protected BearingPart getLowerBearingPart(JTextField field) {
		return getLowerBearingPartIx().get(field);
	}

	protected Map<JTextField, BearingMatrixCell> getBearingMatrixCellIx() {
		return bearingMatrixCellIx;
	}

	protected BearingMatrixCell getBearingMatrixCell(JTextField field) {
		return getBearingMatrixCellIx().get(field);
	}

	protected void setBearingMatrixCell(JTextField field, BearingMatrixCell item) {
		getBearingMatrixCellIx().put(field, item);
	}

	protected Color getIdleColor() {
		return IDLE_COLOR;
	}

	public Color getColor(String colorName) {
		Color color = getParentPanel().getBearingColor(colorName);
		if (color == null) {
			color = getIdleColor();
		}
		return color;
	}

	protected Color getForegroundColor(Color color) {
		if (Color.BLACK.equals(color) || Color.BLUE.equals(color)) {
			return Color.WHITE;
		}
		return Color.BLACK;
	}
}

package com.honda.galc.report;

import java.text.Format;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.honda.galc.util.BeanUtils;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>TableReport</code> is ... .
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
 */
public abstract class TableReport {

	// === model === //
	private String fileName;
	private String title;
	private String reportName;
	private List<?> data;

	private Map<String, String> columnPropertyMapping;
	private Map<String, String> columnHeaderMapping;
	private Map<String, Integer> columnWidthMapping;
	private Map<String, Format> columnFormatMapping;

	public TableReport() {
		this.columnPropertyMapping = new LinkedHashMap<String, String>();
		this.columnHeaderMapping = new HashMap<String, String>();
		this.columnWidthMapping = new HashMap<String, Integer>();
		this.columnFormatMapping = new HashMap<String, Format>();
	}

	// === factory api === //
	public static TableReport createXlsxTableReport() {
		return createXlsxTableReportPoi();
	}

	public static TableReport createXlsxTableReportPoi() {
		XlsxTableReportPoi report = new XlsxTableReportPoi();
		return report;
	}

	// === abstract api === //
	public abstract void export(String fileName);

	// === publicv api === //
	public void addColumn(String propertyName, String header) {
		addColumn(propertyName, propertyName, header);
	}

	public void addColumn(String propertyName, String header, Integer width) {
		addColumn(propertyName, propertyName, header, width);
	}

	public void addColumn(String propertyName, String header, Integer width, Format format) {
		addColumn(propertyName, propertyName, header, width, format);
	}

	// === utility api === //
	protected void addColumn(String id, String propertyName, String header) {
		getColumnPropertyMapping().put(id, propertyName);
		getColumnHeaderMapping().put(id, header);
	}

	protected void addColumn(String id, String propertyName, String header, Integer width) {
		getColumnPropertyMapping().put(id, propertyName);
		getColumnHeaderMapping().put(id, header);
		getColumnWidthMapping().put(id, width);
	}

	protected void addColumn(String id, String propertyName, String header, Integer width, Format format) {
		getColumnPropertyMapping().put(id, propertyName);
		getColumnHeaderMapping().put(id, header);
		getColumnWidthMapping().put(id, width);
		getColumnFormatMapping().put(id, format);
	}

	protected List<Map<String, ?>> prepareData(List<?> data) {
		List<Map<String, ?>> reportData = new ArrayList<Map<String, ?>>();
		if (data == null || data.isEmpty()) {
			return reportData;
		}

		for (Object element : data) {
			Map<String, Object> row = new HashMap<String, Object>();
			for (String columnId : getColumnPropertyMapping().keySet()) {
				String propertyName = getColumnPropertyMapping().get(columnId);
				Object value = BeanUtils.getNestedPropertyValue(element, propertyName);
				Format format = getColumnFormatMapping().get(columnId);
				if (format != null && value != null) {
					value = format.format(value);
				} else {
					if (value instanceof String) {
						value = ((String) value).trim();
					}
				}
				row.put(columnId, value);
			}
			reportData.add(row);
		}
		return reportData;
	}

	// === get/set === //
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public List<?> getData() {
		return data;
	}

	public void setData(List<?> data) {
		this.data = data;
	}

	protected Map<String, String> getColumnPropertyMapping() {
		return columnPropertyMapping;
	}

	protected Map<String, String> getColumnHeaderMapping() {
		return columnHeaderMapping;
	}

	protected Map<String, Integer> getColumnWidthMapping() {
		return columnWidthMapping;
	}

	protected Map<String, Format> getColumnFormatMapping() {
		return columnFormatMapping;
	}
}

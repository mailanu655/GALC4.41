package com.honda.galc.report;

import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>XlsTableReportPoi</code> is ... .
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
public class XlsxTableReportPoi extends TableReport {

	// === public api === //
	@Override
	public void export(String fileName) {
		export(createWorkbook(), fileName);
	}

	// === xls utility api === //
	protected void export(Workbook workBook, String fileName) {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(fileName);
			workBook.write(out);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (out != null) {
					out.close();
					out = null;
				}
			} catch (Exception ex) {
			}
		}
	}

	protected Workbook createWorkbook() {
		String headerLine = getTitle();
		String sheetName = getReportName();
		List<?> data = getData();
		Workbook wb = createNewWorkbook();
		Sheet sheet = wb.createSheet();

		if (sheetName != null && sheetName.trim().length() > 0) {
			wb.setSheetName(0, sheetName);
		}
		int startRowIx = 1;
		int startColIx = 1;
		int rowIx = startRowIx;
		createHeaderLine(wb, sheet, startColIx, rowIx, headerLine);
		rowIx++;
		rowIx++;
		createTableHeader(wb, sheet, startColIx, rowIx);
		createTableData(wb, sheet, startColIx, sheet.getLastRowNum() + 1, data);
		return wb;
	}
	
	protected Workbook createNewWorkbook() {
		return new XSSFWorkbook();
	}

	// === xls protected api === //
	protected void createHeaderLine(Workbook wb, Sheet sheet, int colIx, int rowIx, String headerLine) {
		Font f = wb.createFont();
		f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		f.setFontHeightInPoints((short) 12);
		CellStyle style = wb.createCellStyle();
		style.setFont(f);
		Row row = sheet.createRow(rowIx++);
		Cell cell = row.createCell(colIx);
		cell.setCellStyle(style);
		cell.setCellValue(headerLine);
	}

	protected void createTableHeader(Workbook wb, Sheet sheet, int colIx, int rowIx) {
		Font f = wb.createFont();
		CellStyle style = wb.createCellStyle();
		f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(f);
		style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		Row row = sheet.createRow(rowIx++);
		for (String columnId : getColumnPropertyMapping().keySet()) {
			Cell cell = row.createCell(colIx++);
			cell.setCellStyle(style);
			String columnHeader = getColumnHeaderMapping().get(columnId);
			columnHeader = columnHeader == null ? columnId : columnHeader;
			cell.setCellValue(columnHeader);
			Integer width = getColumnWidthMapping().get(columnId);

			if (width != null && width > 0) {
				sheet.setColumnWidth(cell.getColumnIndex(), width);
			}
		}
	}

	protected void createTableData(Workbook wb, Sheet sheet, int colIx, int rowIx, List<?> data) {

		Row row = null;
		Cell cell = null;

		CellStyle dataStyle = wb.createCellStyle();
		Font f = wb.createFont();
		dataStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
		dataStyle.setFont(f);

		int startColIx = colIx;
		if (data == null) {
			return;
		}

		List<Map<String, ?>> reportData = prepareData(data);

		for (Map<String, ?> element : reportData) {
			row = sheet.createRow(rowIx++);
			colIx = startColIx;
			for (String columnId : getColumnPropertyMapping().keySet()) {
				cell = row.createCell(colIx++);
				Object value = element.get(columnId);
				String stringValue = value == null ? "" : value.toString().trim();
				cell.setCellValue(stringValue);
			}
		}
	}
}

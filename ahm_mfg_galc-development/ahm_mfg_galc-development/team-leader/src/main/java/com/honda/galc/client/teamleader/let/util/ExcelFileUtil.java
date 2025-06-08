package com.honda.galc.client.teamleader.let.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.filechooser.FileFilter;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.honda.galc.util.ExtensionFileFilter;

/**
 * <h3>Class description</h3>
 * <p>
 * <code>ExcelFileUtil</code> class contains  Excel utility methods.
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
 * @author vcc01419
 */
public class ExcelFileUtil {

	private static final String FILE_EXTENSION = ".xls";
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");

	/**
	 * This method will be used to write data to excel file.
	 * 
	 * @param jTable
	 * @param fileName
	 * @return flag
	 */
	public static boolean writeDataToExcelFile(JTable jTable, String fileName) {
		File file = new File(fileName + DATE_FORMAT.format(new Date()) + FILE_EXTENSION);

		JFileChooser fileChooser = new JFileChooser();
		FileFilter filter = new ExtensionFileFilter("Excel Files", "xls", "xlsx");
		fileChooser.setFileFilter(filter);
		fileChooser.addChoosableFileFilter(filter);
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setSelectedFile(file);
		int rtnBtn = fileChooser.showSaveDialog(jTable);
		if (rtnBtn == JFileChooser.APPROVE_OPTION) {
			exportData(fileChooser.getSelectedFile().getPath(), jTable);
			return true;
		}
		return false;
	}

	private static void exportData(String path, JTable jTable) {
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		Row row = sheet.createRow(1);
		Row headerRow = sheet.createRow(0);
		
		Font font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		CellStyle style = workbook.createCellStyle();
		style.setFont(font);
		style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		// For each column write column name
		for (int header = 0; header < jTable.getColumnCount(); header++) {
			Cell headerCell = headerRow.createCell(header);
			headerCell.setCellStyle(style);
			headerCell.setCellValue((String) jTable.getColumnModel().getColumn(header).getHeaderValue());
		}

		for (int rowCounter = 0; rowCounter < jTable.getRowCount(); rowCounter++) {
			for (int columnCounter = 0; columnCounter < jTable.getColumnCount(); columnCounter++) {
				Object value = jTable.getValueAt(rowCounter, columnCounter);
				String stringValue = value == null ? "" : value.toString().trim();
				row.createCell(columnCounter).setCellValue(stringValue);
			}
			// Set the row to the next one in the sequence
			row = sheet.createRow((rowCounter + 2));
		}

		path = path.contains(FILE_EXTENSION) ? path : path + FILE_EXTENSION;
		FileOutputStream outStream = null;
		try {
			outStream = new FileOutputStream(path);
			workbook.write(outStream);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (outStream != null)
					outStream.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
}

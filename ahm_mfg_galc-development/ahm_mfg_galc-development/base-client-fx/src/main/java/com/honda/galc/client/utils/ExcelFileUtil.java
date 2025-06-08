package com.honda.galc.client.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.stage.FileChooser;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * 
 * <h3>ExcelFileUtil</h3> <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * ExcelFileUtil description
 * All Excel utility methods is written here and shall be used across all applications
 * </p>
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
 * 
 * </TABLE>
 * 
 * @author LnT Infotech Oct 13, 2016
 * 
 */
public class ExcelFileUtil {

	/**
	 * This method is used to load excel file
	 */
	public static File loadExcelFile() {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel Files","*.xls","*.xlsx");
		fileChooser.getExtensionFilters().add(extFilter);
		File file=fileChooser.showOpenDialog(null);
		return file;
	}

	/**
	 * This method is used to read data from file
	 * @param workbook
	 * @param sheetData
	 */
	public static void readDataFromExcelFile(Workbook workbook, List<Object[]> sheetData)
			throws IOException {
		Sheet sheet = workbook.getSheetAt(0);
		int count = 0;
		for (Row row : sheet) {
			if(count!=0){
				List<String> data = new ArrayList<String>();
				for (Cell cell : row) {
					data.add(new DataFormatter().formatCellValue(cell).toUpperCase());
				}
				if(data.size()>0)
					sheetData.add(data.toArray());
			}
			count++;
		}
	}
}

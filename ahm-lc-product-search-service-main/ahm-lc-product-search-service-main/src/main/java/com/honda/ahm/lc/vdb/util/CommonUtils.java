package com.honda.ahm.lc.vdb.util;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONException;
import org.json.JSONObject;

import com.honda.ahm.lc.vdb.dto.LotDetailsDto;
import com.honda.ahm.lc.vdb.entity.HistoryDetails;
import com.honda.ahm.lc.vdb.entity.IProductDetails;
import com.honda.ahm.lc.vdb.entity.ProductDetails;
import com.honda.ahm.lc.vdb.entity.VinRangeProductDetails;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

public class CommonUtils {

	private static Logger logger = LogManager.getLogger(CommonUtils.class);

	public static List<LotDetailsDto> convertToLotDetailsDto(List<? extends IProductDetails> productDetailsList) {
		List<LotDetailsDto> lotDetailsList = new ArrayList<>();
		Map<String, List<IProductDetails>> lotGroupMap = productDetailsList.parallelStream()
				.collect(Collectors.groupingBy(IProductDetails::getUniqueKey));

		lotGroupMap.keySet().parallelStream().forEach(keySet -> {
			String[] strSplit = keySet.split(Constants.SEPARATOR);
			String kdLotNumber = StringUtils.trimToEmpty(strSplit[0]);
			String productionLot = StringUtils.trimToEmpty(strSplit[1]);
			List<? extends IProductDetails> productList = lotGroupMap.get(keySet);
			LotDetailsDto dto = new LotDetailsDto(kdLotNumber, productionLot, productList.size(), productList);
			synchronized (lotDetailsList) { // Ensure thread safety when adding to the shared list
				lotDetailsList.add(dto);
			}
		});

		return lotDetailsList;

	}

	public static Integer getShippedIntVal(String isShippedStr) {
		Integer isShipped = 0;
		switch (isShippedStr) {
		case Constants.FALSE:
			isShipped = 0;
			break;
		case Constants.TRUE:
			isShipped = 1;
			break;
		case Constants.BOTH:
			isShipped = 2;
			break;
		}
		return isShipped;
	}

	public static String getValueFromJson(JSONObject json, String key) {
		String arg = StringUtils.EMPTY;
		try {
			arg = StringUtils.trimToEmpty(json.getString(key));
			if (arg.equalsIgnoreCase("null")) {
				arg = StringUtils.EMPTY;
			}
		} catch (JSONException e) {
			// No value found in JSON...so skip
		}
		return StringUtils.trimToNull(arg);
	}

	public static String getCommaSeparatedIntegersFromJson(JSONObject json, String key) {
		String arg = StringUtils.EMPTY;
		try {
			arg = StringUtils.trimToEmpty(json.getString(key));

			if (arg.equalsIgnoreCase("null")) {
				return StringUtils.EMPTY;
			}

			String[] items = arg.split(",\\s*");

			for (String item : items) {
				Integer.parseInt(item);
			}

		} catch (JSONException e) {
			return StringUtils.EMPTY;
		} catch (NumberFormatException e) {

			return StringUtils.EMPTY;
		}

		return StringUtils.trimToNull(arg);
	}

	public static Integer getIntValueFromJson(JSONObject json, String key) {
		Integer arg = 0;
		try {
			arg = json.getInt(key);
		} catch (JSONException e) {
			return arg;
		}
		return arg;
	}

	public static byte[] downloadFile(List<HistoryDetails> productList, String fileType) throws Exception {
		// Define the required column sequence
		List<String> columnOrder = Arrays.asList("kdLotNumber", "productionLot", "id", "engineSerialNo", "engineMto",
				"afOnSequenceNumber", "productSpecCode", "destination", "lastProcessPoint", "actualTimestamp",
				"trackingStatus", "trackingStatusDate", "parkingLocation");

		if (fileType.equalsIgnoreCase("excel")) {
			try (Workbook workbook = new XSSFWorkbook()) {
				Sheet sheet = workbook.createSheet("Product List");
				int[] columnWidths = { 7000, 7000, 5000, 5000, 3000, 7000, 2000, 8000, 8000, 8000, 8000, 8000, 5000,
						8000 };
				for (int i = 0; i < columnWidths.length; i++) {
					sheet.setColumnWidth(i, columnWidths[i]);
				}

				CellStyle headerStyle = workbook.createCellStyle();
				headerStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
				headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

				Row headerRow = sheet.createRow(0);
				for (int i = 0; i < columnOrder.size(); i++) {
					Cell cell = headerRow.createCell(i);
					cell.setCellStyle(headerStyle);
					cell.setCellValue(getColumnName(columnOrder.get(i)));
				}

				AtomicInteger rowIdx = new AtomicInteger(1);
				for (HistoryDetails history : productList) {
					Row row = sheet.createRow(rowIdx.getAndIncrement());
					for (int i = 0; i < columnOrder.size(); i++) {
						Cell cell = row.createCell(i);
						Object value = getFieldValue(history, columnOrder.get(i));
						setCellValue(cell, value);
					}
				}

				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				workbook.write(outputStream);
				return outputStream.toByteArray();
			}
		} else {
			Document document = new Document(PageSize.A3, 30, 30, 30, 50);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);
			writer.setPageEvent(new FooterHandler());
			document.open();

			PdfPTable table = new PdfPTable(columnOrder.size());
			table.setWidthPercentage(100);
			Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
			BaseColor headerBackgroundColor = new BaseColor(255, 255, 204);

			for (String column : columnOrder) {
				PdfPCell headerCell = new PdfPCell(new Phrase(getColumnName(column), headerFont));
				headerCell.setBackgroundColor(headerBackgroundColor);
				headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
				headerCell.setBorderWidth(1f);
				table.addCell(headerCell);
			}

			Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
			for (HistoryDetails history : productList) {
				for (String column : columnOrder) {
					Object value = getFieldValue(history, column);
					table.addCell(createDataCell(value != null ? value.toString() : "", dataFont));
				}
			}

			document.add(table);
			document.close();
			return outputStream.toByteArray();
		}
	}

	private static Object getFieldValue(HistoryDetails history, String fieldName) {
		try {
			Class<?> currentClass = history.getClass();
			while (currentClass != null) {
				try {
					Field field = currentClass.getDeclaredField(fieldName);
					field.setAccessible(true);
					return field.get(history);
				} catch (NoSuchFieldException ignored) {
				}
				currentClass = currentClass.getSuperclass();
			}
		} catch (IllegalAccessException e) {
			return null;
		}
		return null;
	}

	private static void setCellValue(Cell cell, Object value) {
		if (value instanceof String) {
			cell.setCellValue((String) value);
		} else if (value instanceof Number) {
			cell.setCellValue(((Number) value).doubleValue());
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else if (value instanceof java.util.Date) {
			String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value);
			cell.setCellValue(formattedDate);
		} else {
			cell.setCellValue(value != null ? value.toString() : "");
		}
	}

	/*
	 * public static Field[] getAllFields(Class<?> clazz) { List<Field> fieldsList =
	 * new ArrayList<>();
	 * 
	 * while (clazz != null) { // Traverse class hierarchy for (Field field :
	 * clazz.getDeclaredFields()) { fieldsList.add(field); } clazz =
	 * clazz.getSuperclass(); // Move to superclass }
	 * 
	 * return fieldsList.toArray(new Field[0]); // Convert list to array }
	 */

	public static byte[] downloadFile(String fileType, List<ProductDetails> productList) throws Exception {
		List<String> columnOrder = Arrays.asList("kdLotNumber", "productionLot", "id", "engineSerialNo", "engineMto",
				"afOnSequenceNumber", "productSpecCode", "destination", "lastProcessPoint", "actualTimestamp",
				"trackingStatus", "trackingStatusDate", "parkingLocation");
		if (fileType.equalsIgnoreCase("excel")) {
			try (Workbook workbook = new XSSFWorkbook()) {
				Sheet sheet = workbook.createSheet("Product List");

				int[] columnWidths = { 7000, 7000, 5000, 5000, 3000, 7000, 2000, 8000, 8000, 8000, 8000, 8000, 5000,
						8000 };
				for (int i = 0; i < columnWidths.length; i++) {
					sheet.setColumnWidth(i, columnWidths[i]);
				}

				CellStyle headerStyle = workbook.createCellStyle();
				headerStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
				headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

				Row headerRow = sheet.createRow(0);

				AtomicInteger headerIdx = new AtomicInteger(0);
				columnOrder.forEach(fieldName -> {
					Cell cell = headerRow.createCell(headerIdx.getAndIncrement());
					cell.setCellStyle(headerStyle);
					cell.setCellValue(getColumnName(fieldName));
				});

				AtomicInteger rowIdx = new AtomicInteger(1);
				productList.forEach(product -> {
					Row row = sheet.createRow(rowIdx.getAndIncrement());
					AtomicInteger colIdx = new AtomicInteger(0);
					columnOrder.forEach(fieldName -> {
						try {
							Object value = getFieldValue(product, fieldName);
							Cell cell = row.createCell(colIdx.getAndIncrement());

							if (value instanceof String) {
								cell.setCellValue((String) value);
							} else if (value instanceof Number) {
								cell.setCellValue(((Number) value).doubleValue());
							} else if (value instanceof Boolean) {
								cell.setCellValue((Boolean) value);
							} else if (value instanceof java.util.Date) {
								String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value);
								cell.setCellValue(formattedDate);
							} else if (value != null) {
								cell.setCellValue(value.toString());
							} else {
								cell.setCellValue("");
							}
						} catch (Exception e) {
							logger.info(e.getMessage());
						}
					});
				});

				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				workbook.write(outputStream);
				return outputStream.toByteArray();
			}
		} else {
			Document document = new Document(PageSize.A3, 30, 30, 30, 50);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);
			writer.setPageEvent(new FooterHandler());
			document.open();

			Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
			Paragraph title = new Paragraph("LC Product Search", titleFont);
			title.setAlignment(Element.ALIGN_CENTER);
			document.add(title);
			document.add(new Chunk(new LineSeparator(1, 100, BaseColor.BLACK, Element.ALIGN_CENTER, -1)));

			PdfPTable table = new PdfPTable(columnOrder.size());
			table.setWidthPercentage(100);
			Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
			BaseColor headerBackgroundColor = new BaseColor(255, 255, 204);

			columnOrder.forEach(fieldName -> {
				PdfPCell headerCell = new PdfPCell(new Phrase(getColumnName(fieldName), headerFont));
				headerCell.setBackgroundColor(headerBackgroundColor);
				headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
				headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				headerCell.setBorderWidth(1f);
				headerCell.setBorderColor(BaseColor.BLACK);
				table.addCell(headerCell);
			});

			Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);

			productList.forEach(product -> {
				columnOrder.forEach(fieldName -> {
					try {
						Object value = getFieldValue(product, fieldName);
						table.addCell(createDataCell(value != null ? value.toString() : "", dataFont));
					} catch (Exception e) {
						logger.info(e.getMessage());
					}
				});
			});

			document.add(table);
			document.close();
			return outputStream.toByteArray();
		}
	}

	private static Object getFieldValue(Object obj, String fieldName) {
		try {
			Field field = getAllFields(obj.getClass()).stream().filter(f -> f.getName().equals(fieldName)).findFirst()
					.orElse(null);
			if (field != null) {
				field.setAccessible(true);
				return field.get(obj);
			}
		} catch (IllegalAccessException e) {
			logger.info(e.getMessage());
		}
		return null;
	}

	private static List<Field> getAllFields(Class<?> clazz) {
		List<Field> fields = new ArrayList<>();
		while (clazz != null) {
			fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
			clazz = clazz.getSuperclass();
		}
		return fields;
	}

	public static byte[] downloadFileVinRange(String fileType, List<VinRangeProductDetails> productList)
			throws Exception {
		List<String> columnOrder = Arrays.asList("kdLotNumber", "productionLot", "id", "engineSerialNo",
				 "engineMto", "afOnSequenceNumber", "productSpecCode", "destination",
				"lastProcessPoint", "actualTimestamp", "trackingStatus", "trackingStatusDate", "parkingLocation");

		if (fileType.equalsIgnoreCase("excel")) {
			try (Workbook workbook = new XSSFWorkbook()) {
				Sheet sheet = workbook.createSheet("Product List");

				int[] columnWidths = { 7000, 7000, 5000, 5000, 3000, 7000, 2000, 8000, 8000, 8000, 8000, 8000, 8000 };
				for (int i = 0; i < columnWidths.length; i++) {
					sheet.setColumnWidth(i, columnWidths[i]);
				}

				// Style for header cells
				CellStyle headerStyle = workbook.createCellStyle();
				headerStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
				headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

				// Create header row
				Row headerRow = sheet.createRow(0);
				for (int i = 0; i < columnOrder.size(); i++) {
					Cell cell = headerRow.createCell(i);
					cell.setCellStyle(headerStyle);
					String fieldName = columnOrder.get(i);
					cell.setCellValue(getColumnName(fieldName));
				}

				// Create data rows
				AtomicInteger rowIdx = new AtomicInteger(1);
				for (VinRangeProductDetails history : productList) {
					Row row = sheet.createRow(rowIdx.getAndIncrement());
					for (int i = 0; i < columnOrder.size(); i++) {
						String fieldName = columnOrder.get(i);
						Object value = getFieldValue(history, fieldName);
						Cell cell = row.createCell(i);
						setCellValue(cell, value);
					}
				}

				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				workbook.write(outputStream);
				return outputStream.toByteArray();
			}
		} else {
			// PDF creation logic
			Document document = new Document(PageSize.A3, 30, 30, 30, 50);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);
			writer.setPageEvent(new FooterHandler());
			document.open();

			Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
			Paragraph title = new Paragraph("LC Product Search", titleFont);
			title.setAlignment(Element.ALIGN_CENTER);
			document.add(title);

			document.add(new Chunk(new LineSeparator(1, 100, BaseColor.BLACK, Element.ALIGN_CENTER, -1)));

			PdfPTable table = new PdfPTable(columnOrder.size());
			table.setWidthPercentage(100);
			Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
			BaseColor headerBackgroundColor = new BaseColor(255, 255, 204);

			// Set header row values
			for (String column : columnOrder) {
				PdfPCell headerCell = new PdfPCell(new Phrase(getColumnName(column), headerFont));
				headerCell.setBackgroundColor(headerBackgroundColor);
				headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
				headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				headerCell.setBorderWidth(1f);
				headerCell.setBorderColor(BaseColor.BLACK);
				table.addCell(headerCell);
			}

			Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);

			// Add data rows
			for (VinRangeProductDetails history : productList) {
				for (String column : columnOrder) {
					Object value = getFieldValue(history, column);
					table.addCell(createDataCell(value != null ? value.toString() : "", dataFont));
				}
			}

			document.add(table);
			document.close();
			return outputStream.toByteArray();
		}
	}

	private static Object getFieldValue(VinRangeProductDetails history, String fieldName) {
		try {
			Field field = VinRangeProductDetails.class.getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(history);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			logger.info("Error accessing field: " + fieldName + " - " + e.getMessage());
			return null;
		}
	}

	private static PdfPCell createDataCell(String content, Font font) {
		PdfPCell cell = new PdfPCell(new Phrase(content, font));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setPadding(5f);
		return cell;
	}

	public static class FooterHandler extends PdfPageEventHelper {
		Font footerFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.GRAY);

		@Override
		public void onEndPage(PdfWriter writer, Document document) {
			PdfPTable footerTable = new PdfPTable(2);
			try {
				footerTable.setWidthPercentage(100);
				footerTable.setTotalWidth(527);
				footerTable.setLockedWidth(true);
				footerTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

				// Add created date to footer
				String createdDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
				PdfPCell dateCell = new PdfPCell(new Phrase("Created on: " + createdDate, footerFont));
				dateCell.setHorizontalAlignment(Element.ALIGN_LEFT);
				dateCell.setBorder(Rectangle.NO_BORDER);

				// Add page number to footer
				PdfPCell pageNumberCell = new PdfPCell(new Phrase("Page " + writer.getPageNumber(), footerFont));
				pageNumberCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pageNumberCell.setBorder(Rectangle.NO_BORDER);

				footerTable.addCell(dateCell);
				footerTable.addCell(pageNumberCell);

				// Write footer to the document
				PdfContentByte cb = writer.getDirectContent();
				footerTable.writeSelectedRows(0, -1, 34, 50, cb);
			} catch (Exception e) {
				logger.info(e.getMessage());
			}
		}
	}

	private static String getColumnName(String column) {

		if (column.equalsIgnoreCase("kdLotNumber")) {
			return "kd_lot_number";
		} else if (column.equalsIgnoreCase("productionLot")) {
			return "production_lot";
		} else if (column.equalsIgnoreCase("engineSerialNo")) {
			return "engine_serial_no";
		} else if (column.equalsIgnoreCase("missionSerialNo")) {
			return "mission_serial_no";
		} else if (column.equalsIgnoreCase("engineMto")) {
			return "engine_mto";
		} else if (column.equalsIgnoreCase("afOnSequenceNumber")) {
			return "af_seq_no";
		} else if (column.equalsIgnoreCase("productSpecCode")) {
			return "product_spec_code";
		} else if (column.equalsIgnoreCase("destination")) {
			return "destination";
		} else if (column.equalsIgnoreCase("lastProcessPoint")) {
			return "last_process_point";
		} else if (column.equalsIgnoreCase("actualTimestamp")) {
			return "last_process_date";
		} else if (column.equalsIgnoreCase("trackingStatus")) {
			return "tracking_status";
		} else if (column.equalsIgnoreCase("trackingStatusDate")) {
			return "tracking_Status_Date";
		} else if (column.equalsIgnoreCase("parkingLocation")) {
			return "parking_location";
		} else if (column.equalsIgnoreCase("id")) {
			return "product_id";
		} else if (column.equalsIgnoreCase("product_id")) {
			return "product_id";
		} else {
			return null;
		}
	}

}

package com.honda.galc.client.utils;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EmbeddedId;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.honda.galc.checkers.ReactionType;
import com.honda.galc.common.exception.InvalidExcelFileException;
import com.honda.galc.common.exception.InvalidSheetException;
import com.honda.galc.common.exception.ReflectionException;
import com.honda.galc.common.logging.LogRecord;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.PartType;
import com.honda.galc.dto.ExcelSheetColumn;
import com.honda.galc.entity.conf.MCViosMasterOperation;
import com.honda.galc.entity.conf.MCViosMasterOperationChecker;
import com.honda.galc.entity.conf.MCViosMasterOperationMeasurement;
import com.honda.galc.entity.conf.MCViosMasterOperationMeasurementChecker;
import com.honda.galc.entity.conf.MCViosMasterOperationPart;
import com.honda.galc.entity.conf.MCViosMasterOperationPartChecker;
import com.honda.galc.entity.conf.MCViosMasterPlatform;
import com.honda.galc.entity.conf.MCViosMasterProcess;
import com.honda.galc.entity.enumtype.PartCheck;
import com.honda.galc.util.ReflectionUtils;

/**
 * <h3>ExcelFileReader Class description</h3>
 * <p>
 * ExcelFileReader is a Utility class to read Excel Sheet. This class is used to
 * convert the content of Excel sheet into List of Java Objects.
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
 * @author Hemant Kumar<br>
 *         March 28, 2019
 */
public class ExcelFileReader {

	private File excelFile;
	private Map<String, Integer> columnMap;
	
	public static final String FILE_EXTENSION = ".xls";

	public static final String UNDERSCORE = "_";

	public static final String SET = "set";

	public static final String GET = "get";

	public static final String VALUE = "Value";

	public ExcelFileReader(File excelFile) {
		super();
		this.excelFile = excelFile;
		this.columnMap = new HashMap<String, Integer>();
	}

	public ExcelFileReader() {
		super();

	}

	
	
	public <T extends Object> List<T> getExcelContent(Class<T> clazz) {
		List<T> dataList = new ArrayList<T>();
		FileInputStream fstream = null;
		Workbook workbook;
		try {
			fstream = new FileInputStream(excelFile);
			if (excelFile.getName().contains(FILE_EXTENSION)) {
				workbook = new HSSFWorkbook(fstream);
			} else {
				fstream.close();
				throw new InvalidExcelFileException("Please upload only excel(xls) file.");
			}
			Sheet sheet = workbook.getSheetAt(0);
			if (sheet != null) {
				Row firstRow = sheet.getRow(0);
				if (firstRow != null) {
					for (int i = 0; i < firstRow.getLastCellNum(); i++) {
						columnMap.put(new DataFormatter().formatCellValue(firstRow.getCell(i)).toUpperCase(), i);
					}
					for (int i = 1; i <= sheet.getLastRowNum(); i++) {
						T entity = ReflectionUtils.createInstance(clazz);
						Row row = sheet.getRow(i);
						for (Field field : entity.getClass().getDeclaredFields()) {
							if(field.isAnnotationPresent(EmbeddedId.class)) {
								//Embedded Id found! Search in Id as well.
								Object embeddedId = ReflectionUtils.createInstance(field.getType());
								for (Field idField : embeddedId.getClass().getDeclaredFields()) {
									setFieldInObject(idField, embeddedId, row);
								}
								//set embedded id in entity
								setParameterInObject(field, entity, embeddedId);
							} else {
								setFieldInObject(field, entity, row);
							}
						}
						dataList.add(entity);
					}
				}
			}
		} catch (Exception e) {
			if (e instanceof InvalidExcelFileException) {
				throw new InvalidExcelFileException(e.getMessage());
			}
			if (e instanceof SecurityException || e instanceof NoSuchMethodException
					|| e instanceof IllegalArgumentException || e instanceof InstantiationException
					|| e instanceof IllegalAccessException || e instanceof InvocationTargetException
					|| e instanceof ReflectionException) {
				Logger.getLogger().error(e, new LogRecord("An exception occured while parsing Excel Sheet."));
			} else {
				throw new InvalidSheetException("The content in the sheet is invalid", e);
			}
		}
		return dataList;
	}

	

	@SuppressWarnings("rawtypes")
	public static Object toObject(Class clazz, String value) {

		if (Boolean.class == clazz || Boolean.TYPE == clazz)
			return Boolean.parseBoolean(value);
		else if (Byte.class == clazz || Byte.TYPE == clazz)
			return Byte.parseByte(value);
		else if (Short.class == clazz || Short.TYPE == clazz)
			return Short.parseShort(value);
		else if (Integer.class == clazz || Integer.TYPE == clazz)
			return Integer.parseInt(value);
		else if (Long.class == clazz || Long.TYPE == clazz)
			return Long.parseLong(value);
		else if (Float.class == clazz || Float.TYPE == clazz)
			return Float.parseFloat(value);
		else if (Double.class == clazz || Double.TYPE == clazz)
			return Double.parseDouble(value);
		else if (BigDecimal.class == clazz)
			return new BigDecimal(value);
		else if (String.class == clazz)
			return value;
		else if (ReactionType.class == clazz)
			return ReactionType.valueOf(value);
		else if (PartType.class == clazz)
			return PartType.valueOf(value);
		else if (PartCheck.class == clazz)
			return StringUtils.isEmpty(value)? PartCheck.DEFAULT : PartCheck.valueOf(value.toUpperCase());
		else
			return clazz.cast(value);
	}

	private static String getSetterMethodName(String attributeName) {
		return SET + StringUtils.capitalize(attributeName);
	}

	private static void setParameterInObject(Field field, Object obj, Object parameter) {
		String methodName = getSetterMethodName(field.getName());
		if (field.getDeclaringClass() == obj.getClass()) {
			ReflectionUtils.invoke(obj, methodName, parameter);
		}
	}
	
	private void setFieldInObject(Field field, Object obj, Row row) {
		if (field.isAnnotationPresent(ExcelSheetColumn.class)) {
			ExcelSheetColumn excelAnnotation = field.getAnnotation(ExcelSheetColumn.class);
			String colName = excelAnnotation.name().toUpperCase();
			int index = columnMap.get(colName);
			row.getCell(index);
			Object parameter = null;
			try {
				parameter = toObject(field.getType(),
						new DataFormatter().formatCellValue(row.getCell(index)));
			} catch (NumberFormatException e) {
				parameter = 0;
			}
			setParameterInObject(field, obj, parameter);
		}
	}



}

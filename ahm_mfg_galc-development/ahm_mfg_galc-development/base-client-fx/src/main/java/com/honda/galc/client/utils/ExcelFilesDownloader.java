package com.honda.galc.client.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.persistence.EmbeddedId;
import java.lang.reflect.Method;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;

import com.honda.galc.checkers.ReactionType;
import com.honda.galc.common.logging.LogRecord;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.PartType;
import com.honda.galc.dao.conf.MCViosMasterMBPNMatrixDataDao;
import com.honda.galc.dao.conf.MCViosMasterOperationCheckerDao;
import com.honda.galc.dao.conf.MCViosMasterOperationDao;
import com.honda.galc.dao.conf.MCViosMasterOperationMeasurementCheckerDao;
import com.honda.galc.dao.conf.MCViosMasterOperationMeasurementDao;
import com.honda.galc.dao.conf.MCViosMasterOperationPartCheckerDao;
import com.honda.galc.dao.conf.MCViosMasterOperationPartDao;
import com.honda.galc.dao.conf.MCViosMasterPlatformDao;
import com.honda.galc.dao.conf.MCViosMasterProcessDao;
import com.honda.galc.dto.ExcelSheetColumn;
import com.honda.galc.entity.conf.MCViosMasterMBPNMatrixData;
import com.honda.galc.entity.conf.MCViosMasterOperation;
import com.honda.galc.entity.conf.MCViosMasterOperationChecker;
import com.honda.galc.entity.conf.MCViosMasterOperationMeasurement;
import com.honda.galc.entity.conf.MCViosMasterOperationMeasurementChecker;
import com.honda.galc.entity.conf.MCViosMasterOperationPart;
import com.honda.galc.entity.conf.MCViosMasterOperationPartChecker;
import com.honda.galc.entity.conf.MCViosMasterPlatform;
import com.honda.galc.entity.conf.MCViosMasterProcess;
import com.honda.galc.entity.enumtype.PartCheck;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.ReflectionUtils;

/**
 * <h3>ExcelFilesDownloader Class description</h3>
 * <p>
 * ExcelFilesDownloader is a Utility class to download Excel Sheet. This class
 * is used to convert the entities to Excel sheets and download that files.
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
 * @author Shambhu Singh<br>
 *         April 25, 2019
 */
public class ExcelFilesDownloader {

	public static final String UNDERSCORE = "_";

	public static final String SET = "set";

	public static final String GET = "get";

	public static final String VALUE = "Value";

	private File excelFile;

	public ExcelFilesDownloader(File excelFile) {
		super();
		this.excelFile = excelFile;
	}

	/**
	 * @param file
	 * @param daoMap
	 * @param platformId
	 */
	@SuppressWarnings({ "rawtypes", "unused", "deprecation", "unchecked" })
	public String downloadAllExcelFiles(File file, String viosPlatformId) {
		try {
			ExcelFileReader exlReader = new ExcelFileReader();
			Map<Class, Class> daoMap = null;
			Map<Class, Class> primaryKeyMap = null;
			java.io.FileOutputStream fileOut = new java.io.FileOutputStream(file);
			ZipOutputStream zip = new ZipOutputStream(fileOut);
			int fileCount = 1;
			int fileBlankCount = 1;
			boolean isAllBlank = false;

			 List<MCViosMasterPlatform> mvPlatformList = ServiceFactory.getDao(MCViosMasterPlatformDao.class)
					.findAllData(viosPlatformId);
			List<MCViosMasterProcess> mcProcessList = ServiceFactory.getDao(MCViosMasterProcessDao.class)
					.findAllData(viosPlatformId);

			List<MCViosMasterOperation> mcOperationList = ServiceFactory.getDao(MCViosMasterOperationDao.class)
					.findAllData(viosPlatformId);
			List<MCViosMasterOperationChecker> mcopsCheckerList = ServiceFactory
					.getDao(MCViosMasterOperationCheckerDao.class).findAllData(viosPlatformId);
			List<MCViosMasterOperationPart> mcopsPartList = ServiceFactory.getDao(MCViosMasterOperationPartDao.class)
					.findAllData(viosPlatformId);
			List<MCViosMasterOperationPartChecker> mcOpspartCheckerList = ServiceFactory
					.getDao(MCViosMasterOperationPartCheckerDao.class).findAllData(viosPlatformId);
			List<MCViosMasterOperationMeasurement> mcOpsMeasList = ServiceFactory
					.getDao(MCViosMasterOperationMeasurementDao.class).findAllData(viosPlatformId);
			List<MCViosMasterOperationMeasurementChecker> mcOpsMeasCheckerList = ServiceFactory
					.getDao(MCViosMasterOperationMeasurementCheckerDao.class).findAllData(viosPlatformId);

			List<MCViosMasterMBPNMatrixData> mbpnMasterdataList =
					ServiceFactory.getService(MCViosMasterMBPNMatrixDataDao.class).findAllData(viosPlatformId, StringUtils.EMPTY);
			List allEntityList = new ArrayList<>();
			allEntityList.add(mvPlatformList);
		allEntityList.add(mcProcessList);
			allEntityList.add(mcOperationList);
			allEntityList.add(mcopsCheckerList);
			allEntityList.add(mcopsPartList);
			allEntityList.add(mcOpspartCheckerList);
			allEntityList.add(mcOpsMeasList);
			allEntityList.add(mcOpsMeasCheckerList);
			allEntityList.add(mbpnMasterdataList);
			

			List listObject = null;
			String fileName = null;
			for (int counter = 0; counter < allEntityList.size(); counter++) {
				try {
					listObject = (List) allEntityList.get(counter);
					if (listObject.size() > 0) {
						fileName = listObject.get(0).getClass().getSimpleName() + ExcelFileReader.FILE_EXTENSION;
						HSSFWorkbook workbook = new HSSFWorkbook();
						HSSFSheet worksheet = workbook.createSheet(viosPlatformId);
						int count = 0;
						Object entityObject = listObject.get(0);
						Set<Field> fieldList = getMatchingField(entityObject.getClass());
						List<String> colList = getColumnName(entityObject.getClass());
						if (count == 0 && listObject.size() > 0) {
							HSSFRow row1 = worksheet.createRow((short) count);
							int cellCount = 0;
							for (String column : colList) {
								HSSFCell cellA1 = row1.createCell((short) cellCount);
								Field fieldColumn = getFieldFromColumn(column, fieldList);
								cellA1.setCellValue(fieldColumn.getAnnotation(ExcelSheetColumn.class).name());
								cellCount++;
							}
							count++;
						}
						
						for (Object obj : listObject) {
							Object entityId = getEntityIdObject(obj);
							try {
								HSSFRow row1 = worksheet.createRow((short) count);
								int cellCount = 0;
								for (String column : colList) {
									Field field = getFieldFromColumn(column, fieldList);
									HSSFCell cellA1 = row1.createCell((short) cellCount);
									Object returnValue = fetchDataFromEntity(field, obj, entityId);
									if (returnValue.getClass() == Short.class) {
										cellA1.setCellValue(String.valueOf(returnValue));
									} else if (returnValue.getClass() == BigDecimal.class) {
										cellA1.setCellValue(String.valueOf(returnValue));
									} else {
										cellA1.setCellValue("" + returnValue);
									}
									cellCount = cellCount + 1;
								}
								count = count + 1;
							} catch (Exception e) {
								e.printStackTrace();
								Logger.getLogger().error(e, new LogRecord("An exception occured in for loop."));
							}
						}
						
						
						fileCount++;
						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						if (!isRowEmpty(workbook.getSheetAt(0).getRow(0))) {
							workbook.write(bos);
							zip.putNextEntry(new ZipEntry(new File(fileName).getName()));
						}
						bos.writeTo(zip);
					}
				} catch (Exception e) {
					e.printStackTrace();
					Logger.getLogger().error(e, new LogRecord("An exception occured in for loop."));
				}
			}
			zip.closeEntry();
			zip.flush();
			zip.close();

		} catch (Exception e) {
			e.printStackTrace();
			Logger.getLogger().error(e, new LogRecord("An exception occured while download Excel Sheet."));
		}
		return null;
	}

	/**
	 * @param filevcc9
	 * @param clazz
	 */
	@SuppressWarnings({ "rawtypes", "unused", "deprecation" })
	public void downloadExcelFile(File file, Class clazz) {
		try {
			ExcelFileReader exlReader = new ExcelFileReader();
			java.io.FileOutputStream fileOut = new java.io.FileOutputStream(file);
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet worksheet = workbook.createSheet(clazz.getSimpleName());
			int count = 0;
			@SuppressWarnings("unchecked")
			Set<Field> fieldList = getMatchingField(clazz);
			for (int i = 0; i < 1; i++) {
				HSSFRow row1 = worksheet.createRow((short) count);
				int cellCount = 0;
				List<String> colList = getColumnName(clazz);
				for (String column : colList) {
					Field field = getFieldFromColumn(column, fieldList);
					HSSFCell cellA1 = row1.createCell((short) cellCount);
					ExcelSheetColumn excelAnnotation = field.getAnnotation(ExcelSheetColumn.class);
					String colName = excelAnnotation.name();
					cellA1.setCellValue(colName);
					cellCount++;
				}
				count = count + 1;
			}
			workbook.write(fileOut);
			fileOut.flush();
			fileOut.close();

		} catch (Exception e) {
			e.printStackTrace();
			Logger.getLogger().error(e, new LogRecord("An exception occured while download Excel Sheet."));
		}
	}

	public static boolean isRowEmpty(Row row) {
		boolean isEmpty = true;
		DataFormatter dataFormatter = new DataFormatter();
		if (row != null) {
			for (Cell cell : row) {
				if (dataFormatter.formatCellValue(cell).trim().length() > 0) {
					isEmpty = false;
					break;
				}
			}
		}
		return isEmpty;
	}

	/**
	 * @param attributeName
	 * @param clazz
	 * @param obj
	 * @return
	 */
	public static Object fetchDataFromEntity(Field field, Object obj, Object entityId) {
		Method getMethod = null;
		Object parameter = null;
		Object actualObject = null;
		try {
			if (entityId != null) {
				if (field.getDeclaringClass() == entityId.getClass()) {
					actualObject = entityId;
				}
			}
			if (field.getDeclaringClass() == obj.getClass()) {
				actualObject = obj;
			}
			if (field.getType() == Short.TYPE) {
				getMethod = actualObject.getClass().getMethod(GET + StringUtils.capitalize(field.getName()) + VALUE,
						new Class[] {});
			} else if (field.getType() == Integer.TYPE) {
				getMethod = actualObject.getClass().getMethod(GET + StringUtils.capitalize(field.getName()),
						new Class[] {});
			} else if (field.getType() == Double.TYPE) {
				getMethod = actualObject.getClass().getMethod(GET + StringUtils.capitalize(field.getName()),
						new Class[] {});
			} else {
				getMethod = actualObject.getClass().getMethod(GET + StringUtils.capitalize(field.getName()),
						new Class[] {});
			}
			Object returnValue = (Object) getMethod.invoke(actualObject, new Object[] {});
			if (returnValue instanceof ReactionType) {
				parameter = returnValue.toString();
			} else if (returnValue instanceof PartType) {
				parameter = returnValue;
			} else if (returnValue instanceof PartCheck) {
				parameter = returnValue;
			} else {
				parameter = ExcelFileReader.toObject(field.getType(), returnValue.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			Logger.getLogger().error(e, new LogRecord(e.getMessage()));
		}
		return parameter;
	}

	public List<String> getColumnName(@SuppressWarnings("rawtypes") Class clazz) {
		List<String> colNames = new ArrayList<String>();
		if (clazz == MCViosMasterPlatform.class) {
			colNames.add("Plant");
			colNames.add("Department");
			colNames.add("Model_Year");
			colNames.add("Production_Rate");
			colNames.add("Line_No");
			colNames.add("Vehicle_Model_Code");
			colNames.add("Status");

		} else if (clazz == MCViosMasterProcess.class) {
			colNames.add("Process_Point_Id");
			colNames.add("Process_No");
			colNames.add("Process_Seq_No");

		} else if (clazz == MCViosMasterOperation.class) {
			colNames.add("Unit_No");
			colNames.add("Common_Name");
			colNames.add("View");
			colNames.add("Processor");
			colNames.add("Op_Check");

		} else if (clazz == MCViosMasterOperationChecker.class) {
			colNames.add("Unit_No");
			colNames.add("CheckPoint");
			colNames.add("CheckSequence");
			colNames.add("CheckName");
			colNames.add("Checker");
			colNames.add("ReactionType");

		} else if (clazz == MCViosMasterOperationPart.class) {
			colNames.add("Unit");
			colNames.add("Part No");
			colNames.add("Part Mask");
			colNames.add("Part Check");
		} else if (clazz == MCViosMasterOperationPartChecker.class) {
			colNames.add("Unit No");
			colNames.add("Part No");
			colNames.add("CheckPoint");
			colNames.add("CheckSeq");
			colNames.add("CheckName");
			colNames.add("Checker");
			colNames.add("ReactionType");

		} else if (clazz == MCViosMasterOperationMeasurement.class) {
			colNames.add("Unit");
			colNames.add("Num bolts"); // setOpMeasSeqNum
			colNames.add("Min (nm)");
			colNames.add("Max (nm)");
			colNames.add("DeviceMsg"); // setDeviceMsg
			colNames.add("DeviceId"); // setDeviceId
		} else if (clazz == MCViosMasterOperationMeasurementChecker.class) {
			colNames.add("Unit No");
			colNames.add("CheckPoint");
			colNames.add("CheckSeq");
			colNames.add("CheckName");
			colNames.add("Checker");
			colNames.add("ReactionType");
		} else if (clazz == MCViosMasterMBPNMatrixData.class) {
			colNames.add("PDDA Process Number");
			colNames.add("MBPN Mask");
			colNames.add("MTC Model");
			colNames.add("MTC Types");
		}
		return colNames;
	}

	/**
	 * @param klass
	 * @param clazz
	 * @return
	 */

	@SuppressWarnings("rawtypes")
	public Set getMatchingField(@SuppressWarnings("rawtypes") Class clazz) {
		Set<Field> fieldList = new HashSet<Field>();
		for (Field field : clazz.getDeclaredFields()) {

			if (field.isAnnotationPresent(EmbeddedId.class)) {
				Object embeddedId = ReflectionUtils.createInstance(field.getType());
				for (Field idField : embeddedId.getClass().getDeclaredFields()) {
					if (idField.isAnnotationPresent(ExcelSheetColumn.class)) {
						fieldList.add(idField);
					}
				}
			} else {
				if (field.isAnnotationPresent(ExcelSheetColumn.class)) {
					fieldList.add(field);
				}
			}
		}
		return fieldList;
	}

	public Field getFieldFromColumn(String column, Set<Field> fieldList) {
		for (Field field : fieldList) {
			ExcelSheetColumn excelAnnotation = field.getAnnotation(ExcelSheetColumn.class);
			String colName = excelAnnotation.name();
			if (colName.trim().equals(column.trim())) {
				return field;
			}
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public Object getEntityIdObject(@SuppressWarnings("rawtypes") Object obj) {
		Method getMethod = null;
		Object returnValue = null;
		for (Field field : obj.getClass().getDeclaredFields()) {
			if (field.isAnnotationPresent(EmbeddedId.class)) {
				try {
					getMethod = obj.getClass().getMethod(GET + StringUtils.capitalize(field.getName()), new Class[] {});
					returnValue = (Object) getMethod.invoke(obj, new Object[] {});
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return returnValue;
	}

}

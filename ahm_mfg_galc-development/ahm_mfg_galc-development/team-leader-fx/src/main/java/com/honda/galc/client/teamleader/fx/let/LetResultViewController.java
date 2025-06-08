package com.honda.galc.client.teamleader.fx.let;

import java.awt.Desktop;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.honda.galc.client.ui.component.ColumnMapping;
import com.honda.galc.client.ui.component.FXOptionPane;
import com.honda.galc.client.ui.component.FXOptionPane.Response;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.utils.FileUtility;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.enumtype.LetInspectionStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.LetMessage;
import com.honda.galc.entity.product.LetPassCriteriaProgram;
import com.honda.galc.letxml.model.FaultCode;
import com.honda.galc.letxml.model.Process;
import com.honda.galc.letxml.model.ReTest;
import com.honda.galc.letxml.model.Test;
import com.honda.galc.letxml.model.TestAttrib;
import com.honda.galc.letxml.model.TestParam;
import com.honda.galc.letxml.model.UnitInTest;
import com.honda.galc.report.TableReport;
import com.honda.galc.report.XlsxTableReportPoi;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.BeanUtils;
import com.honda.galc.util.StringUtil;

import javafx.stage.FileChooser.ExtensionFilter;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>LetResultViewController</code> is ... .
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
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 * @created Aug 7, 2019
 */
public class LetResultViewController {

    public static final String UNIT_TYPE = "UNIT";
    public static final String PROCESS_TYPE = "PROCESS";
    public static final String TEST_TYPE = "TEST";

    public static final String PARAMETERS = "PARAMETERS";
    public static final String ATTRIBUTES = "ATTRIBUTES";
    public static final String FAULT_CODES = "FAULT CODES";
    public static final String SEPARATOR = "SEPARATOR";

    public static final String TYPE = "type";

    public static final String ITEM_HEADER = "Item";
    public static final String TEST_HEADER = "Test";
    public static final String RE_TEST_HEADER = "ReTest";

    public static final String LABEL_IX = "labelIx";
    public static final String PROPERTY = "property";
    public static final String FIRST_COLUMN_KEY = "rowKey";
    public static final String VALUE = "value";

    private LetResultViewPanel view;
    private LetResultViewModel model;

    public LetResultViewController(LetResultViewPanel view) {
        this.view = view;
        init();
    }

    // === init === //

    protected void init() {
        this.model = new LetResultViewModel();
        getModel().getDefaultColumnMappings().add(createColumnMapping(ITEM_HEADER, LABEL_IX, false));
        getModel().getDefaultColumnMappings().add(createColumnMapping(TEST_HEADER, PROPERTY, false));
    }

    // === action implementation //
    protected void processProductId() {

        String productId = getView().getProductIdInput().getText();
        resetProduct();
        productId = StringUtils.trim(productId);
        if (StringUtils.isBlank(productId)) {
            getView().getProductIdInput().requestFocus();
            return;
        }
        Logger.getLogger().info("Submitted product for processing:" + productId);
        getView().getProductIdInput().setText(productId);
        List<LetMessage> messages = getModel().selectMessages(productId);

        if (messages == null || messages.isEmpty()) {
            getView().setErrorMessage("No data found");
            getView().getProductIdInput().requestFocus();
            Logger.getLogger().info("No valid let messages found for product:" + productId);
            return;
        }

        Map<Long, LetMessage> msgIx = new LinkedHashMap<Long, LetMessage>();
        for (LetMessage lm : messages) {
            if (lm.getXmlMessageBody() != null) {
                msgIx.put(lm.getMessageId(), lm);
            }
        }

        Logger.getLogger().info("Found " + msgIx.size() + " messages for product:" + productId);
        Map<Long, UnitInTest> unitIx = new LinkedHashMap<Long, UnitInTest>();
        for (LetMessage msg : msgIx.values()) {
            UnitInTest uit = getModel().convert(msg.getXmlMessageBody());
            if (uit != null) {
                unitIx.put(msg.getMessageId(), uit);
            }
        }

        List<Map.Entry<Long, UnitInTest>> list = new ArrayList<Map.Entry<Long, UnitInTest>>(unitIx.entrySet());
        Comparator<Map.Entry<Long, UnitInTest>> comp = new Comparator<Map.Entry<Long, UnitInTest>>() {
            @Override
            public int compare(Entry<Long, UnitInTest> o1, Entry<Long, UnitInTest> o2) {
                String ts1 = getLastProcessEndTime(o1.getValue());
                String ts2 = getLastProcessEndTime(o2.getValue());
                int result = BeanUtils.safeCompare(ts1, ts2);
                return result;
            }
        };
        Collections.sort(list, comp);
        for (Map.Entry<Long, UnitInTest> entry : list) {
            getModel().getUnitInTestIx().put(entry.getKey(), entry.getValue());
            getModel().getMessageIx().put(entry.getKey(), msgIx.get(entry.getKey()));
        }

        getModel().getProductPrograms().addAll(getProductPrograms(getModel().getUnitInTests()));

        getView().getProgramTable().setItems(getModel().getProductPrograms());
        filterPrograms();

        loadTreeView();

        TextFieldState.GOOD_READ_ONLY.setState(getView().getProductIdDisplay());
        TextFieldState.GOOD_READ_ONLY.setState(getView().getProductSpecDisplay());
        getView().getProductIdDisplay().setText(productId);
        BaseProduct product = ProductTypeUtil.getProductDao(getModel().getProductType()).findBySn(productId);
        if (product != null) {
            getView().getProductSpecDisplay().setText(product.getProductSpecCode());
        }
    }

    protected void processSelectedProgram(LetPassCriteriaProgram program) {

        resetProgram();
        getView().getResultTable().removeColumns();

        String testName = program.getCriteriaPgmName();

        if (StringUtils.isBlank(testName) || getModel().getUnitInTests() == null || getModel().getUnitInTests().isEmpty()) {
            return;
        }

        String terminatingTs = getTerminatingTimestamp(testName);

        List<ColumnMapping> columnMappings = new ArrayList<ColumnMapping>();
        columnMappings.add(createColumnMapping(ITEM_HEADER, FIRST_COLUMN_KEY, false));

        Map<String, Map<String, Object>> unitData = new LinkedHashMap<String, Map<String, Object>>();
        Map<String, Map<String, Object>> paramData = new LinkedHashMap<String, Map<String, Object>>();
        Map<String, Map<String, Object>> attrData = new LinkedHashMap<String, Map<String, Object>>();
        Map<String, Map<String, Object>> faultData = new LinkedHashMap<String, Map<String, Object>>();

        putSeparator(unitData, UNIT_TYPE);

        putSeparator(paramData, PARAMETERS);
        putSeparator(attrData, ATTRIBUTES);
        putSeparator(faultData, FAULT_CODES);

        int unitIntestCtr = 0;

        for (Long msgId : getModel().getUnitInTestIx().keySet()) {
            UnitInTest uit = getModel().getUnitInTestIx().get(msgId);
            LetMessage msg = getModel().getMessageIx().get(msgId);
            unitIntestCtr++;

            String columnKey = VALUE + "-" + unitIntestCtr;
            String reTestColumnKey = VALUE + "-" + unitIntestCtr + "-rt";
            for (Process process : uit.getProcesses()) {
                for (Test test : process.getTests()) {
                    if (testName.equals(test.getId())) {

                        // === unit data === //
                        putItem(unitData, "Total Status", columnKey, uit.getTotalStatusName());
                        putItem(unitData, "Production", columnKey, uit.getProduction());
                        putItem(unitData, "Message Id", columnKey, msg.getMessageId());
                        putItem(unitData, "Message Status", columnKey, msg.getTotalStatus());

                        // === process data === //
                        putSeparator(unitData, PROCESS_TYPE);
                        putItem(unitData, "Process", columnKey, process.getId());
                        putItem(unitData, "Process Status", columnKey, process.getStatusName());
                        putItem(unitData, "Process Start", columnKey, process.getStartTime());
                        putItem(unitData, "Process Finish", columnKey, process.getFinishTime());
                        putItem(unitData, "Base release Ver.", columnKey, process.getSoftwareVersion());

                        // === test data === //
                        boolean reTestFlag = test instanceof ReTest;
                        if (terminatingTs != null && LetInspectionStatus.Pass.getStringValue().equals(test.getStatusName())) {
                            if (test.getTestEndTime().compareTo(terminatingTs) < 1) {
                                test.setStatus(LetInspectionStatus.PASS_TERMINATED.getStringValue());
                            }
                        }
                        String testColumnKey = reTestFlag ? reTestColumnKey : columnKey;
                        putSeparator(unitData, TEST_TYPE);
                        putItem(unitData, "Test Status", testColumnKey, test.getStatusName());
                        putItem(unitData, "Test Start", testColumnKey, test.getTestTime());
                        putItem(unitData, "Test Finish", testColumnKey, test.getTestEndTime());
                        putItem(unitData, "InCycleRetestNum", testColumnKey, test.getInCycleRetestNum());

                        putItem(paramData, PARAMETERS, testColumnKey, "Val  [Unit, Low, High]");
                        putItem(attrData, ATTRIBUTES, testColumnKey, "Val  [Expected]");

                        if (reTestFlag) {
                            columnMappings.add(createColumnMapping(RE_TEST_HEADER + "-" + unitIntestCtr, testColumnKey, false));

                            ReTest reTest = (ReTest) test;

                            for (TestParam item : reTest.getReTestParams()) {
                                putItem(paramData, item.getParam(), testColumnKey, formatItem(item));
                            }
                            for (TestAttrib item : reTest.getReTestAttribs()) {
                                putItem(attrData, item.getAtt(), testColumnKey, formatItem(item));

                            }
                            for (FaultCode item : reTest.getReFaultCode()) {
                                putItem(faultData, item.getFaultCode(), testColumnKey, item.getShortDesc());
                            }
                        } else {
                            columnMappings.add(createColumnMapping(TEST_HEADER + "-" + unitIntestCtr, testColumnKey, false));
                            for (TestParam item : test.getTestParams()) {
                                putItem(paramData, item.getParam(), testColumnKey, formatItem(item));
                            }
                            for (TestAttrib item : test.getTestAttribs()) {
                                putItem(attrData, item.getAtt(), testColumnKey, formatItem(item));
                            }
                            for (FaultCode item : test.getFaultCodes()) {
                                putItem(faultData, item.getFaultCode(), testColumnKey, item.getShortDesc());
                            }
                        }
                    }
                }
            }
        }

        List<Map<String, Object>> tableData = new ArrayList<Map<String, Object>>();
        tableData.addAll(unitData.values());
        tableData.addAll(paramData.values());
        tableData.addAll(attrData.values());
        tableData.addAll(faultData.values());

        getModel().getProgramColumnMappings().addAll(columnMappings);
        getView().getResultTable().addColumns(getModel().getProgramColumnMappings());
        getView().getResultTable().setItems(tableData);
    }

    protected void exportResults() {
        if (getView().getResultTable().getTable().getItems().size() == 0) {
            FXOptionPane.showMessageDialog(getView().getMainWindow().getStage(), "There is no data to export!", "", FXOptionPane.Type.WARNING);
            return;
        }
        LetPassCriteriaProgram program = getView().getProgramTable().getSelectedItem();
        String programName = program.getCriteriaPgmName();
        String productId = getView().getProductIdInput().getText();
        String fileName = generateLetResultExportFileName(programName, productId, "xlsx");
        String title = "Program: " + programName + "    VIN: " + productId;
        String reportName = programName;

        File file = FileUtility.popupSaveDialog(getView(), fileName, new ExtensionFilter("Excel Files xls, xlsx", "*.xls", "*.xlsx"));
        if (file == null || file.getAbsolutePath() == null) {
            return;
        }

        List<Map<String, Object>> data = getView().getResultTable().getItems();

        TableReport report = new XlsxTableReportPoi() {
            @Override
            protected void createTableData(Workbook wb, Sheet sheet, int colIx, int rowIx, List<?> data) {
                Row row = null;
                Cell cell = null;
                Font separatorFont = wb.createFont();
                CellStyle separatorStyle = wb.createCellStyle();
                separatorFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                separatorStyle.setFont(separatorFont);
                separatorStyle.setFillForegroundColor((short) 47);
                separatorStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

                int startColIx = colIx;
                if (data == null) {
                    return;
                }

                List<Map<String, ?>> reportData = prepareData(data);

                for (Map<String, ?> element : reportData) {
                    row = sheet.createRow(rowIx++);
                    colIx = startColIx;
                    boolean separator = SEPARATOR.equals(element.get(TYPE));
                    for (String columnId : getColumnPropertyMapping().keySet()) {
                        cell = row.createCell(colIx++);
                        Object value = element.get(columnId);
                        String stringValue = value == null ? "" : value.toString().trim();
                        cell.setCellValue(stringValue);
                        if (separator) {
                            cell.setCellStyle(separatorStyle);
                        }
                    }
                }
            }

            @SuppressWarnings("unchecked")
            @Override
            protected List<Map<String, ?>> prepareData(List<?> data) {
                return (List<Map<String, ?>>) data;
            }
        };
        for (ColumnMapping cm : getModel().getProgramColumnMappings()) {
            report.addColumn(cm.getAccessor(), cm.getTitle(), 5500);

        }

        report.setData(data);
        report.setFileName(file.getAbsolutePath());
        report.setTitle(title);
        report.setReportName(reportName);

        report.export(report.getFileName());
        
        if (Desktop.isDesktopSupported()) {
            Response response = FXOptionPane.showConfirmDialog(getView().getMainWindow().getStage(), "Do you want to open download folder?", "", FXOptionPane.Type.CONFIRM);
            if (Response.YES.equals(response)) {
                try {
                    Desktop.getDesktop().open(file.getParentFile());
                } catch (Exception e) {
                    String msg = "Failed to open download directory ";
                    Logger.getLogger().error(e, msg + ":" + file);
                    FXOptionPane.showMessageDialog(getView().getMainWindow().getStage(), msg + "!", "", FXOptionPane.Type.WARNING);
 
                }
            }
        }
    }

    protected String generateLetResultExportFileName(String programName, String productId, String ext) {
        String fileName = "LetResults-";
        fileName = fileName + programName.replaceAll("[^a-zA-Z0-9_\\-\\.]", "_");
        if (StringUtils.isNotBlank(productId)) {
            fileName = fileName + "-" + productId;
        }
        fileName = fileName + "-" + StringUtil.dateToString(new Date(), "yyyyMMddHHmmss");
        fileName = fileName + "." + ext;
        return fileName;
    }

    protected List<LetPassCriteriaProgram> getProductPrograms(Collection<UnitInTest> unitInTests) {
        Map<String, LetPassCriteriaProgram> map = new TreeMap<String, LetPassCriteriaProgram>();
        for (UnitInTest uit : unitInTests) {
            for (Process process : uit.getProcesses()) {
                for (Test test : process.getTests()) {
                    if (map.containsKey(test.getId())) {
                        continue;
                    }
                    LetPassCriteriaProgram prg = getModel().getProgramIx().get(test.getId());
                    if (prg == null) {
                        prg = new LetPassCriteriaProgram();
                        prg.setCriteriaPgmName(test.getId());
                    }
                    map.put(prg.getCriteriaPgmName(), prg);
                }
            }
        }
        return new ArrayList<LetPassCriteriaProgram>(map.values());
    }

    protected String getLastProcessEndTime(UnitInTest uit) {
        String lastEndTs = null;
        if (uit == null || uit.getProcesses() == null) {
            return null;
        }
        for (Process process : uit.getProcesses()) {
            if (lastEndTs == null || (BeanUtils.safeCompare(lastEndTs, process.getFinishTime()) < 0)) {
                lastEndTs = process.getFinishTime();
            }
        }
        return lastEndTs;
    }

    protected String getTerminatingTimestamp(String programName) {
        List<String> trigPrograms = getModel().getConnectedProgramIx().get(programName);
        if (trigPrograms == null || trigPrograms.isEmpty()) {
            return null;
        }
        String latestTs = null;
        for (UnitInTest uit : getModel().getUnitInTestIx().values()) {
            for (Process process : uit.getProcesses()) {
                for (Test test : process.getTests()) {
                    if (trigPrograms.contains(test.getId())) {
                        if (latestTs == null || latestTs.compareTo(test.getTestEndTime()) < 1) {
                            latestTs = test.getTestEndTime();
                        }
                    }
                }
            }
        }
        return latestTs;
    }

    protected void filterPrograms() {
        String filter = getView().getProgramNameFilter().getText();
        filterPrograms(filter);
    }

    protected void filterPrograms(String filter) {
        filter = StringUtils.trimToEmpty(filter);
        getView().getProgramTable().clearData();
        List<LetPassCriteriaProgram> filtered = new ArrayList<LetPassCriteriaProgram>();
        if (StringUtils.isBlank(filter)) {
            filtered.addAll(getModel().getProductPrograms());
        } else {
            for (LetPassCriteriaProgram lip : getModel().getProductPrograms()) {
                if (StringUtils.containsIgnoreCase(lip.getCriteriaPgmName(), filter)) {
                    filtered.add(lip);
                }
            }
        }
        getView().getProgramTable().setItems(filtered);
    }

    protected void loadTreeView() {
        StringBuilder xml = new StringBuilder();
        xml.append("<UNIT_IN_TESTS>");
        if (getModel().getMessages().size() > 0) {
            for (LetMessage msg : getModel().getMessages()) {
                xml.append(msg.getXmlMessageBody());
            }
        }
        xml.append("</UNIT_IN_TESTS>");
        getView().getResultTree().setData(xml.toString());
        getView().expandRootItem();
    }

    protected ColumnMapping createColumnMapping(String title, String propertyName) {
        ColumnMapping mapping = new ColumnMapping(title, propertyName);
        mapping.setSortable(true);
        mapping.setFormat(null);
        return mapping;
    }

    protected ColumnMapping createColumnMapping(String title, String propertyName, boolean sortable) {
        ColumnMapping mapping = new ColumnMapping(title, propertyName);
        mapping.setSortable(sortable);
        mapping.setFormat(null);
        return mapping;
    }

    protected Map<String, Object> putSeparator(Map<String, Map<String, Object>> data, String rowKey) {
        Map<String, Object> map = data.get(rowKey);
        if (map == null) {
            map = new HashMap<String, Object>();
            map.put(FIRST_COLUMN_KEY, rowKey);
            map.put(TYPE, SEPARATOR);
            data.put(rowKey, map);
        }
        return map;
    }

    protected Map<String, Object> putItem(Map<String, Map<String, Object>> data, String rowKey, String columnKey, Object value) {
        Map<String, Object> map = data.get(rowKey);
        if (map == null) {
            map = new HashMap<String, Object>();
            map.put(FIRST_COLUMN_KEY, rowKey);
            data.put(rowKey, map);
        }
        map.put(columnKey, value);
        return map;
    }

    protected String formatItem(TestParam item) {
        if (item == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        String txt = StringUtils.trimToEmpty(item.getVal());
        if (StringUtils.isNotBlank(item.getUnit())) {
            sb.append(item.getUnit());
        }
        if (StringUtils.isNotBlank(item.getLoLimit())) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(item.getLoLimit());
        }
        if (StringUtils.isNotBlank(item.getHiLimit())) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(item.getHiLimit());
        }
        if (sb.length() > 0) {
            txt = txt + "   [" + sb.toString() + "]";
        }
        return txt;
    }

    protected String formatItem(TestAttrib item) {
        if (item == null) {
            return "";
        }
        String txt = StringUtils.trimToEmpty(item.getVal());
        if (StringUtils.isNotBlank(item.getExpectedVal())) {
            txt = txt + "   [" + item.getExpectedVal() + "]";
        }
        return txt;
    }

    // === reset state === //
    protected void reset() {
        resetProduct();
        getView().getTreeViewFilter().clear();
        getView().getProgramNameFilter().clear();
        getView().getTabbedPane().getSelectionModel().select(0);
    }

    protected void resetProduct() {

        resetProgram();
        getView().getResultTree().clearData();
        getView().getProgramTable().clearData();

        TextFieldState.DISABLED.setState(getView().getProductSpecDisplay());
        TextFieldState.DISABLED.setState(getView().getProductIdDisplay());

        getView().getProductSpecDisplay().clear();
        getView().getProductIdDisplay().clear();
        getView().getProductIdInput().clear();

        getModel().getProductPrograms().clear();
        getModel().getUnitInTestIx().clear();
        getModel().getMessageIx().clear();
    }

    protected void resetProgram() {
        getView().getResultTable().clearData();
        getView().getResultTable().removeColumns();
        getView().getResultTable().addColumns(getModel().getDefaultColumnMappings());
        getModel().getProgramColumnMappings().clear();
    }

    // === get/set === //
    protected LetResultViewPanel getView() {
        return view;
    }

    protected void setView(LetResultViewPanel view) {
        this.view = view;
    }

    protected LetResultViewModel getModel() {
        return model;
    }

    protected void setModel(LetResultViewModel model) {
        this.model = model;
    }
}

package com.honda.galc.client.teamleader.fx.let;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.data.ProductSpecData;
import com.honda.galc.client.teamleader.fx.let.LetResultDownload.DownloadStatus;
import com.honda.galc.client.ui.component.ColumnMapping;
import com.honda.galc.client.ui.component.FXOptionPane;
import com.honda.galc.client.ui.component.FXOptionPane.Response;
import com.honda.galc.client.utils.FileUtility;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.data.cache.PersistentCache;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.entity.product.LetInspectionProgram;
import com.honda.galc.property.LetPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.KeyValue;
import com.honda.galc.util.QueueProcessor;
import com.honda.galc.util.StringUtil;

import javafx.stage.FileChooser.ExtensionFilter;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>LetResultDownloadController</code> is ... .
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
 * @created Mar 1, 2019
 */
public class LetResultDownloadController {

    private LetResultDownloadPanel view;
    private LetResultDownloadModel model;

    private QueueProcessor<LetResultDownload> downloader;

    private LetResultDownload displayedDownload;

    public LetResultDownloadController(LetResultDownloadPanel view) {
        this.view = view;
        this.model = new LetResultDownloadModel();
        this.downloader = new QueueProcessor<LetResultDownload>(100) {
            @Override
            public void processItem(LetResultDownload item) {
                selectData(item);
            }
        };
        getDownloader().start();
    }

    protected void loadinitData() {
        getModel().loadInitData();
    }

    // === event handlers === //
    protected void submitDownloadJob() {
        if (!isInputValid()) {
            return;
        }
        LetResultDownload download = createDownloadJob();
        LetResultDownload existingDownload = null;
        for (LetResultDownload item : getView().getDownloadJobTable().getItems()) {
            if (item.isSelectionCriteriaTheSame(download)) {
                existingDownload = item;
            }
        }
        if (existingDownload != null) {
            if (DownloadStatus.QUEUED.equals(existingDownload.getStatus())) {
                FXOptionPane.showMessageDialog(getView().getMainWindow().getStage(), "The same job is already in the Queue !", "", FXOptionPane.Type.WARNING);
                return;
            }
            if (existingDownload.getStatus().isRunning()) {
                FXOptionPane.showMessageDialog(getView().getMainWindow().getStage(), "The same job is already running !", "", FXOptionPane.Type.WARNING);
                return;
            }
            if (DownloadStatus.DONE.equals(existingDownload.getStatus())) {
                Response response = FXOptionPane.showConfirmDialog(getView().getMainWindow().getStage(), "The same job is already done.\nAre you sure you want to run it again ?", "",
                        FXOptionPane.Type.CONFIRM);
                if (!Response.YES.equals(response)) {
                    return;
                }
            }
        }
        download.setStatus(DownloadStatus.QUEUED);
        getView().getDownloadJobTable().addItem(download);
        getDownloader().enqueue(download);
        getView().getTabbedPane().getSelectionModel().select(0);
    }

    protected boolean isInputValid() {
        String productId = getView().getProductId().getText();
        LetInspectionProgram program = getView().getLetProgramTable().getSelectedItem();
        LocalDate startDate = getView().getStartDatePicker().getValue();
        LocalDate endDate = getView().getEndDatePicker().getValue();
        if (program == null) {
            FXOptionPane.showMessageDialog(getView().getMainWindow().getStage(), "Please select a Program !", "", FXOptionPane.Type.WARNING);
            return false;
        }
        if (StringUtils.isBlank(productId) && (startDate == null || endDate == null)) {
            FXOptionPane.showMessageDialog(getView().getMainWindow().getStage(), "Start Date and End Date are required if VIN is not specified !", "", FXOptionPane.Type.WARNING);
            return false;
        }
        if (startDate != null && endDate != null) {
            if (endDate.isBefore(startDate)) {
                FXOptionPane.showMessageDialog(getView().getMainWindow().getStage(), "End Date is before Start Date !", "", FXOptionPane.Type.WARNING);
                return false;
            }
            if (StringUtils.isBlank(productId)) {
                int totalDays = (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;
                if (totalDays > getMaxDownloadRangeDate()) {
                    FXOptionPane.showMessageDialog(getView().getMainWindow().getStage(), "Maximum number of days to download is " + getMaxDownloadRangeDate() + " !", "", FXOptionPane.Type.WARNING);
                    return false;
                }
            }
        }
        return true;
    }

    protected LetResultDownload createDownloadJob() {

        String productId = getView().getProductId().getText();
        LetInspectionProgram program = getView().getLetProgramTable().getSelectedItem();
        LocalDate startLocalDate = getView().getStartDatePicker().getValue();
        LocalDate endLocalDate = getView().getEndDatePicker().getValue();
        Timestamp startDate = null;
        Timestamp endDate = null;
        if (startLocalDate != null) {
            startDate = Timestamp.valueOf(startLocalDate.atTime(getPropertyBean().getLetDownloadStartTime(), 0));
        }
        if (endLocalDate != null) {
            endLocalDate = endLocalDate.plusDays(1);
            endDate = Timestamp.valueOf(endLocalDate.atTime(getPropertyBean().getLetDownloadStartTime(), 0));
        }

        String yearCode = getView().getYearCode().getValue();
        String modelCode = getView().getModelCode().getValue();
        String typeCode = getView().getTypeCode().getValue();
        String optionCode = getView().getOptionCode().getValue();
        String specCode = StringUtils.defaultString(yearCode, "*");
        specCode = specCode + StringUtils.defaultString(modelCode, "*");
        specCode = specCode + StringUtils.defaultString(typeCode, "*");
        specCode = specCode + StringUtils.defaultString(optionCode, "*");
        FrameSpec spec = new FrameSpec();
        spec.setModelYearCode(yearCode);
        spec.setModelCode(modelCode);
        spec.setModelTypeCode(typeCode);
        spec.setModelOptionCode(optionCode);
        spec.setProductSpecCode(specCode);

        String lineNo = getView().getLineNo().getText();
        String production = null;
        KeyValue<String, String> item = getView().getProduction().getValue();
        if (item != null) {
            production = item.getValue();
        }

        LetResultDownload download = new LetResultDownload() {
            @Override
            public void setStatus(DownloadStatus status) {
                super.setStatus(status);
                Logger.getLogger().info(toString());
                getView().getDownloadJobTable().getTable().refresh();
            }

            @Override
            public void setProgress(int progress) {
                super.setProgress(progress);
                Logger.getLogger().info(toString());
                getView().getDownloadJobTable().getTable().refresh();
            }
        };
        download.setJobId(getView().getNewJobId());
        download.setProgram(program);
        download.setProductId(productId);
        download.setStartDate(startDate);
        download.setEndDate(endDate);
        download.setSpec(spec);
        download.setLineNo(lineNo);
        download.setProduction(production);
        return download;
    }

    protected void deleteDownload(LetResultDownload download) {
        if (download == null) {
            return;
        }
        for (Object key : download.getKeys()) {
            getCache().remove(key);
        }
        download.getAttrNames().clear();
        download.getParamNames().clear();
        download.getKeys().clear();
    }

    // === data api === //
    protected void selectData(LetResultDownload download) {
        if (download == null) {
            return;
        }
        download.setDownloadStarted(new Timestamp(System.currentTimeMillis()));
        try {
            synchronized (download.getStatus()) {
                if (!download.getStatus().isRunnable()) {
                    if (download.getStatus().isCancelPending()) {
                        download.setStatus(DownloadStatus.CANCELED);
                    }
                    return;
                }
                download.setStatus(DownloadStatus.RUNNING);
            }
            List<Long> messageIds = getModel().selectMessageIds(download);

            if (!getModel().isRunningStatusValid(download)) {
                return;
            }
            getModel().selectResults(download, messageIds);

            if (!getModel().isRunningStatusValid(download)) {
                return;
            }
            download.setDownloadFinished(new Timestamp(System.currentTimeMillis()));
            download.setProgress(100);
            download.setStatus(DownloadStatus.DONE);
        } catch (Exception e) {
            download.setStatus(DownloadStatus.FAILED);
            Logger.getLogger().error(e, "Failed to execute download:" + download);
        }
    }

    // === export data === //
    protected void exportDownload(LetResultDownload download) {
        if (download == null) {
            return;
        }
        String fileName = generateFileName(download);
        File file = FileUtility.popupSaveDialog(getView(), fileName, new ExtensionFilter("CSV Files", "*.csv"));
        if (file == null || file.getAbsolutePath() == null) {
            return;
        }
        List<ColumnMapping> tableColumns = getView().getResultTableColumnMappings(download);
        List<ColumnMapping> exportColumns = new ArrayList<ColumnMapping>();
        List<String> excludeColumnFilter = new ArrayList<String>(Arrays.asList(new String[] { "#", "MSG_ID", "key", "" }));
        for (ColumnMapping cm : tableColumns) {
            if (cm == null || excludeColumnFilter.contains(cm.getTitle())) {
                continue;
            }
            exportColumns.add(cm);
        }
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file));
            bw.write(getModel().generateFileMainHeader());
            bw.write("\n");
            bw.write(getModel().generateFileMainLine(download));
            bw.write("\n\n");
            bw.write(getModel().generateFileTableHeader(exportColumns));
            bw.write("\n\n");

            List<Object> list = new ArrayList<Object>();
            for (int i = 0; i < download.getDataSize(); i++) {
                Object key = download.getKeys().get(i);
                Map<?, ?> map = getCache().get(key, Map.class);
                list.clear();
                for (ColumnMapping cm : exportColumns) {
                    list.addAll(getColumnValues(cm, map));
                }
                bw.write(StringUtils.join(list, Delimiter.COMMA));
                bw.write("\n");
            }
            bw.close();
            bw = null;
            if (Desktop.isDesktopSupported()) {
                Response response = FXOptionPane.showConfirmDialog(getView().getMainWindow().getStage(), "Do you want to open download folder?", "", FXOptionPane.Type.CONFIRM);
                if (Response.YES.equals(response)) {
                    Desktop.getDesktop().open(file.getParentFile());
                }
            }

        } catch (Exception e) {
            String msg = "Failed to export data to a file ";
            Logger.getLogger().error(e, msg + ":" + file);
            FXOptionPane.showMessageDialog(getView().getMainWindow().getStage(), msg + "!", "", FXOptionPane.Type.WARNING);
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (Exception e) {
                Logger.getLogger().error(e);
            }
        }
    }

    protected String generateFileName(LetResultDownload download) {
        String fileName = "LetResults-";
        fileName = fileName + download.getProgram().getInspectionPgmName().replaceAll("[^a-zA-Z0-9_\\-\\.]", "_");
        if (StringUtils.isNotBlank(download.getProductId())) {
            fileName = fileName + "-" + download.getProductId();
        }
        fileName = fileName + "-" + StringUtil.dateToString(new Date(), "yyyyMMddHHmmss");
        fileName = fileName + ".csv";
        return fileName;
    }

    protected List<Object> getColumnValues(ColumnMapping cm, Map<?, ?> data) {
        List<Object> list = new ArrayList<Object>();
        if (cm.getSubColumns() == null || cm.getSubColumns().isEmpty()) {
            Object value = data.get(cm.getAccessor());
            if (value == null) {
                list.add("");
            } else {
                String str = value.toString();
                str = StringUtils.trim(str);
                list.add(StringUtil.csvCommaCheckAddQuotes(str));
            }
        } else {
            for (ColumnMapping subCm : cm.getSubColumns()) {
                list.addAll(getColumnValues(subCm, data));
            }
        }
        return list;
    }

    // === get/set === //
    protected ProductSpecData getSpecData() {
        return getModel().getSpecData();
    }

    protected List<LetInspectionProgram> getPrograms() {
        return getModel().getPrograms();
    }

    protected LetResultDownloadPanel getView() {
        return view;
    }

    protected LetResultDownloadModel getModel() {
        return model;
    }

    protected List<KeyValue<String, String>> getProductionData() {
        List<KeyValue<String, String>> list = new ArrayList<KeyValue<String, String>>();
        list.add(new KeyValue<String, String>("MassPro", "0"));
        list.add(new KeyValue<String, String>("Event", "1"));
        return list;
    }

    public QueueProcessor<LetResultDownload> getDownloader() {
        return downloader;
    }

    protected int getDownloadDateRange() {
        String property = getPropertyBean().getDownloadDateRange();
        int range = 31;
        try {
            range = Integer.parseInt(property);
        } catch (Exception e) {
            Logger.getLogger().error("Failed to parse integer getDownloadDateRange():" + property);
        }
        return range;
    }

    protected int getMaxDownloadRangeDate() {
        return getDownloadDateRange();
    }

    protected LetPropertyBean getPropertyBean() {
        return PropertyService.getPropertyBean(LetPropertyBean.class, getView().getApplicationId());
    }

    protected PersistentCache getCache() {
        return getModel().getCache();
    }

    protected LetResultDownload getDisplayedDownload() {
        return displayedDownload;
    }

    protected void setDisplayedDownload(LetResultDownload displayedDownload) {
        this.displayedDownload = displayedDownload;
    }
}

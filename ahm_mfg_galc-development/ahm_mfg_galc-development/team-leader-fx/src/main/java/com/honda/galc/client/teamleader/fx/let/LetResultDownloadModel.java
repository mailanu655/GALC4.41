package com.honda.galc.client.teamleader.fx.let;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Timestamp;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.data.ProductSpecData;
import com.honda.galc.client.teamleader.fx.let.LetResultDownload.DownloadStatus;
import com.honda.galc.client.ui.component.ColumnMapping;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.product.LetInspectionProgramDao;
import com.honda.galc.dao.product.LetMessageDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.cache.CacheFactory;
import com.honda.galc.data.cache.PersistentCache;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.entity.product.LetInspectionProgram;
import com.honda.galc.letxml.model.FaultCode;
import com.honda.galc.letxml.model.TestAttrib;
import com.honda.galc.letxml.model.TestParam;
import com.honda.galc.util.StringUtil;
import com.thoughtworks.xstream.XStream;

import net.sf.ehcache.Cache;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>LetResultDownloadModel</code> is ... .
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
public class LetResultDownloadModel {

    public static final String ATTR_PREFIX = "att_";
    public static final String PARAM_PREFIX = "param_";
    public static final String FAULT_CODE_PREFIX = "fault_code_";

    public static final String VALUE = "Value";
    public static final String EXPECTED_VALUE = "Expected";
    public static final String LOW_LIMIT = "LoLimit";
    public static final String HIGH_LIMIT = "HiLimit";
    public static final String UNIT = "Unit";
    public static final String SHORT_DESC = "ShortDesc";

    public static final String ATTR_VAL = "_val";
    public static final String ATTR_EXP_VAL = "_expectedVal";

    public static final String PARAM_VAL = "_val";
    public static final String PARAM_LO_LIMIT = "_loLimit";
    public static final String PARAM_HI_LIMIT = "_hiLimit";
    public static final String PARAM_UNIT = "_unit";

    public static final String FAULT_CODE_VAL = "_val";
    public static final String FAULT_CODE_DESC = "_shortDesc";

    private static final String FAULT_CODE_VALUE = "X";

    private static final int SELECT_ID_PROGRESS = 20;

    private XStream xmlParamConverter;
    private XStream xmlAttribConverter;
    private XStream xmlFaultCodeConverter;

    private ProductSpecData specData;
    private List<LetInspectionProgram> programs;
    private PersistentCache cache;

    public LetResultDownloadModel() {
        this.programs = new ArrayList<LetInspectionProgram>();
        this.xmlParamConverter = new XStream();
        this.xmlAttribConverter = new XStream();
        this.xmlFaultCodeConverter = new XStream();
        this.cache = createCache("LetDownload");
        getXmlParamConverter().processAnnotations(TestParam.class);
        getXmlAttribConverter().processAnnotations(TestAttrib.class);
        getXmlFaultCodeConverter().processAnnotations(FaultCode.class);
    }

    public void loadInitData() {
        getPrograms().clear();
        getPrograms().addAll(getLetInspectionProgramDao().findAllLetInspPgmOrderByPgmName());
        setSpecData(new ProductSpecData(ProductType.FRAME.name()));
    }

    // === data selectors === //
    protected List<Long> selectMessageIds(LetResultDownload download) {
        String programName = download.getProgram().getInspectionPgmName();
        String productId = download.getProductId();
        FrameSpec spec = download.getSpec();
        Timestamp startDate = download.getStartDate();
        Timestamp endDate = download.getEndDate();
        String lineNo = download.getLineNo();
        String production = download.getProduction();

        List<Long> messageIds = new ArrayList<Long>();

        if (!isRunningStatusValid(download)) {
            return messageIds;
        }

        if (StringUtils.isNotBlank(productId)) {
            messageIds = getLetMessageDao().findAllMessageIds(programName, productId, spec, startDate, endDate, lineNo, production);
            download.setProgress(SELECT_ID_PROGRESS);
        } else {
            int daysInBatch = 1;
            int ctr = 0;
            int totalDays = (int) ChronoUnit.DAYS.between(startDate.toLocalDateTime(), endDate.toLocalDateTime());

            Timestamp runEndDate = new Timestamp(startDate.getTime());
            Timestamp runStartDate = new Timestamp(runEndDate.getTime());
            while (runEndDate.before(endDate)) {
                ctr++;
                runStartDate = new Timestamp(runEndDate.getTime());
                runEndDate = new Timestamp(runStartDate.getTime() + daysInBatch * 24 * 60 * 60 * 1000);
                if (runEndDate.after(endDate)) {
                    runEndDate = new Timestamp(endDate.getTime());
                }
                if (!isRunningStatusValid(download)) {
                    return messageIds;
                }
                List<Long> batch = getLetMessageDao().findAllMessageIds(programName, productId, spec, runStartDate, runEndDate, lineNo, production);
                messageIds.addAll(batch);
                int percentage = 0;
                if (totalDays != 0) {
                    percentage = SELECT_ID_PROGRESS * ctr / totalDays;
                }
                download.setProgress(percentage);
            }
        }
        return messageIds;
    }

    protected void selectResults(LetResultDownload download, List<Long> messageIds) {
        int batchSize = 100;
        String programName = download.getProgram().getInspectionPgmName();
        if (!isRunningStatusValid(download)) {
            return;
        }
        int startIx = 0;
        int toIx = Math.min(startIx + batchSize, messageIds.size());
        while (startIx < messageIds.size()) {
            List<Long> batchIds = new ArrayList<Long>(messageIds.subList(startIx, toIx));
            if (!isRunningStatusValid(download)) {
                return;
            }
            List<Map<String, Object>> batch = getLetMessageDao().findAllProgramResultData(programName, batchIds);
            if (batch != null && !batch.isEmpty()) {
                processBatch(download, batch, startIx);
                int percentage = SELECT_ID_PROGRESS + (100 - SELECT_ID_PROGRESS) * download.getDataSize() / messageIds.size();
                download.setProgress(percentage);
                batch.clear();
            }
            startIx = toIx;
            toIx = Math.min(startIx + batchSize, messageIds.size());
            batchIds.clear();
        }
    }

    protected void processBatch(LetResultDownload download, List<Map<String, Object>> data, int startIx) {
        if (data == null || data.isEmpty()) {
            return;
        }
        for (Map<String, Object> row : data) {
            convertAttrs(download, row);
            convertParams(download, row);
            convertFaultCodes(download, row);
            UUID key = UUID.randomUUID();
            download.getKeys().add(key);
            getCache().put(key, row);
        }
    }

    protected void convertAttrs(LetResultDownload download, Map<String, Object> row) {
        Object object = row.get("ATTRS");
        String xml = "";
        if (object instanceof String) {
            xml = (String) object;
        }
        if (StringUtils.isBlank(xml)) {
            return;
        }
        xml = "<list>" + xml + "</list>";
        Object collection = convertXml(getXmlAttribConverter(), xml);
        if (collection instanceof List<?>) {
            List<?> list = (List<?>) collection;
            for (Object obj : list) {
                TestAttrib element = (TestAttrib) obj;
                convertToMap(download, element, row);
            }
        }
    }

    protected void convertParams(LetResultDownload download, Map<String, Object> row) {
        Object object = row.get("PARAMS");
        String xml = "";
        if (object instanceof String) {
            xml = (String) object;
        }
        if (StringUtils.isBlank(xml)) {
            return;
        }
        xml = "<list>" + xml + "</list>";
        Object collection = convertXml(getXmlParamConverter(), xml);
        if (collection instanceof List<?>) {
            List<?> list = (List<?>) collection;
            for (Object obj : list) {
                TestParam element = (TestParam) obj;
                convertToMap(download, element, row);
            }
        }
    }

    protected void convertFaultCodes(LetResultDownload download, Map<String, Object> row) {
        Object object = row.get("FAULT_CODES");
        String xml = "";
        if (object instanceof String) {
            xml = (String) object;
        }
        if (StringUtils.isBlank(xml)) {
            return;
        }
        xml = "<list>" + xml + "</list>";
        Object collection = convertXml(getXmlFaultCodeConverter(), xml);
        if (collection instanceof List<?>) {
            List<?> list = (List<?>) collection;
            for (Object obj : list) {
                FaultCode element = (FaultCode) obj;
                convertToMap(download, element, row);
            }
        }
    }

    protected void convertToMap(LetResultDownload download, TestAttrib element, Map<String, Object> row) {
        if (element == null || row == null) {
            return;
        }
        String prefix = getAttrPrefix(element.getAtt());
        row.put(prefix + ATTR_VAL, element.getVal());
        row.put(prefix + ATTR_EXP_VAL, element.getExpectedVal());
        if (!download.getAttrNames().contains(element.getAtt())) {
            download.getAttrNames().add(element.getAtt());
        }
    }

    protected void convertToMap(LetResultDownload download, TestParam element, Map<String, Object> row) {
        if (element == null || row == null) {
            return;
        }
        String prefix = getParamPrefix(element.getParam());
        row.put(prefix + PARAM_VAL, element.getVal());
        row.put(prefix + PARAM_LO_LIMIT, element.getLoLimit());
        row.put(prefix + PARAM_HI_LIMIT, element.getHiLimit());
        row.put(prefix + PARAM_UNIT, element.getUnit());
        if (!download.getParamNames().contains(element.getParam())) {
            download.getParamNames().add(element.getParam());
        }
    }

    protected void convertToMap(LetResultDownload download, FaultCode element, Map<String, Object> row) {
        if (element == null || row == null) {
            return;
        }
        String prefix = getFaultCodePrefix(element.getFaultCode());
        row.put(prefix + FAULT_CODE_VAL, FAULT_CODE_VALUE);
        row.put(prefix + FAULT_CODE_DESC, element.getShortDesc());
        if (!download.getFaultCodeNames().contains(element.getFaultCode())) {
            download.getFaultCodeNames().add(element.getFaultCode());
        }
    }

    protected String getAttrPrefix(String name) {
        String prefix = ATTR_PREFIX + name.replace(".", "_");
        return prefix;
    }

    protected String getParamPrefix(String name) {
        String prefix = PARAM_PREFIX + name.replace(".", "_");
        return prefix;
    }

    protected String getFaultCodePrefix(String name) {
        String prefix = FAULT_CODE_PREFIX + name.replace(".", "_");
        return prefix;
    }

    protected synchronized boolean isRunningStatusValid(LetResultDownload download) {
        if (download.getStatus().isRunning()) {
            return true;
        }
        if (download.getStatus().isCancelPending()) {
            download.setStatus(DownloadStatus.CANCELED);
        }
        return false;
    }

    // === convert xml === //
    protected Object convertXml(XStream xs, String xml) {
        if (xs == null || StringUtils.isBlank(xml)) {
            return null;
        }
        Object obj = null;
        obj = xs.fromXML(xml);
        return obj;
    }

    // === factory methods === //
    protected PersistentCache createCache(String cacheName) {
        if (StringUtils.isEmpty(CacheFactory.getPath())) {
            CacheFactory.setPath(System.getProperty("user.dir") + System.getProperty("file.separator") + "ehcache");
        }
        PersistentCache persistentCache = new PersistentCache(cacheName);

        Cache cache = new Cache(cacheName, 1000, // max elements in memory
                MemoryStoreEvictionPolicy.LRU, // memory store eviction policy
                true, // overflowToDisk
                CacheFactory.getPath(), // disk store path
                true, // eternal
                0, // time to live in seconds
                0, // time to idle in seconds
                false, // persist the cache to disk between JVM restarts
                36000, // disk expiry thread interval in seconds
                null, // registered event listeners
                null, // bootstrap cache loader
                0, // max elements on disk
                0, // disk spool buffer size in Mb
                true); // clear on flush

        CacheFactory.addCache(cache);
        return persistentCache;
    }

    // === data export === //
    protected String generateFileMainHeader() {
        return "DownloadTimestamp,VIN,LineNo,Year,Model,Type,Option,StartDate,EndDate,Production,Program";
    }

    protected String generateFileMainLine(LetResultDownload download) {
        LetInspectionProgram program = download.getProgram();
        List<Object> list = new ArrayList<Object>();
        list.add(download.getDownloadStartedTxt());
        list.add(StringUtils.trimToEmpty(download.getProductId()));
        list.add(StringUtils.trimToEmpty(download.getLineNo()));
        if (download.getSpec() != null) {
            list.add(StringUtils.trimToEmpty(download.getSpec().getModelYearCode()));
            list.add(StringUtils.trimToEmpty(download.getSpec().getModelCode()));
            list.add(StringUtils.trimToEmpty(download.getSpec().getModelTypeCode()));
            list.add(StringUtils.trimToEmpty(download.getSpec().getModelOptionCode()));
        } else {
            list.add("");
            list.add("");
            list.add("");
            list.add("");
        }
        list.add(download.getStartDateTxt());
        list.add(download.getEndDateTxt());
        list.add(StringUtils.trimToEmpty(download.getProduction()));
        list.add(StringUtil.csvCommaCheckAddQuotes(program.getInspectionPgmName()));
        String str = StringUtils.join(list, Delimiter.COMMA);
        return str;
    }

    protected String generateFileTableHeader(List<ColumnMapping> columns) {
        List<String> list = new ArrayList<String>();
        for (ColumnMapping cm : columns) {
            list.add(StringUtil.csvCommaCheckAddQuotes(cm.getTitle()));
            if (cm.getSubColumns() == null || cm.getSubColumns().isEmpty()) {
                continue;
            }
            for (int i = 1; i < cm.getSubColumns().size(); i++) {
                list.add(cm.getSubColumns().get(i).getTitle());
            }
        }
        String str = StringUtils.join(list, Delimiter.COMMA);
        return str;
    }

    // === get/set === //
    protected LetInspectionProgramDao getLetInspectionProgramDao() {
        return getDao(LetInspectionProgramDao.class);
    }

    protected List<LetInspectionProgram> getPrograms() {
        return programs;
    }

    protected ProductSpecData getSpecData() {
        return specData;
    }

    protected PersistentCache getCache() {
        return cache;
    }

    protected LetMessageDao getLetMessageDao() {
        return getDao(LetMessageDao.class);
    }

    protected XStream getXmlAttribConverter() {
        return xmlAttribConverter;
    }

    protected XStream getXmlParamConverter() {
        return xmlParamConverter;
    }

    protected XStream getXmlFaultCodeConverter() {
        return xmlFaultCodeConverter;
    }

    protected void setSpecData(ProductSpecData specData) {
        this.specData = specData;
    }
}

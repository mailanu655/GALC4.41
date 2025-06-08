package com.honda.galc.client.teamleader.fx.let;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.entity.product.LetInspectionProgram;
import com.honda.galc.util.BeanUtils;
import com.honda.galc.util.StringUtil;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>LetResultDownload</code> is ... .
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
public class LetResultDownload {

    public enum DownloadStatus {

        QUEUED, RUNNING, DONE, FAILED, INVALID_INPUT, CANCEL_PEND, CANCELED;

        public boolean isViewable() {
            return this.equals(DONE);
        }

        public boolean isExportable() {
            return this.equals(DONE);
        }

        public boolean isDeletable() {
            return this.equals(DONE) || this.equals(FAILED) || this.equals(INVALID_INPUT) || this.equals(CANCELED);
        }

        public boolean isCancelable() {
            return this.equals(QUEUED) || isRunning();
        }

        public boolean isRunnable() {
            return this.equals(QUEUED);
        }

        public boolean isRunning() {
            return this.equals(RUNNING);
        }

        public boolean isCancelPending() {
            return this.equals(CANCEL_PEND);
        }
    }

    private int jobId;
    private LetInspectionProgram program;
    private String productId;
    private Timestamp startDate;
    private Timestamp endDate;
    private FrameSpec spec;
    private String lineNo;
    private String production;

    private Timestamp downloadStarted;
    private Timestamp downloadFinished;
    private DownloadStatus status;
    private int progress;

    private List<String> attrNames;
    private List<String> paramNames;
    private List<String> faultCodeNames;

    private List<Object> keys;

    public LetResultDownload() {
        this.attrNames = new ArrayList<String>();
        this.paramNames = new ArrayList<String>();
        this.faultCodeNames = new ArrayList<String>();
        this.keys = new ArrayList<Object>();
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public String getStartDateTxt() {
        return format(getStartDate());
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public String getEndDateTxt() {
        return format(getEndDate());
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public FrameSpec getSpec() {
        return spec;
    }

    public void setSpec(FrameSpec spec) {
        this.spec = spec;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public List<String> getAttrNames() {
        return attrNames;
    }

    public List<String> getParamNames() {
        return paramNames;
    }

    public List<String> getFaultCodeNames() {
        return faultCodeNames;
    }

    public DownloadStatus getStatus() {
        return status;
    }

    public void setStatus(DownloadStatus status) {
        this.status = status;
    }

    public int getTotalCount() {
        return getDataSize();
    }

    public String getProduction() {
        return production;
    }

    public void setProduction(String production) {
        this.production = production;
    }

    public Timestamp getDownloadStarted() {
        return downloadStarted;
    }

    public void setDownloadStarted(Timestamp downloadStarted) {
        this.downloadStarted = downloadStarted;
    }

    public Timestamp getDownloadFinished() {
        return downloadFinished;
    }

    public String getDownloadFinishedTxt() {
        return format(getDownloadFinished());
    }

    public String getDownloadStartedTxt() {
        return format(getDownloadStarted());
    }

    public void setDownloadFinished(Timestamp downloadFinished) {
        this.downloadFinished = downloadFinished;
    }

    public LetInspectionProgram getProgram() {
        return program;
    }

    public void setProgram(LetInspectionProgram program) {
        this.program = program;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public boolean isSelectionCriteriaTheSame(LetResultDownload other) {
        if (other == null) {
            return false;
        }
        if (this == other) {
            return true;
        }
        if (!BeanUtils.safeEquals(getProgram(), other.getProgram())) {
            return false;
        }
        if (!BeanUtils.safeEquals(getProductId(), other.getProductId())) {
            return false;
        }
        if (!BeanUtils.safeEquals(getStartDate(), other.getStartDate())) {
            return false;
        }
        if (!BeanUtils.safeEquals(getEndDate(), other.getEndDate())) {
            return false;
        }
        if (!BeanUtils.safeEquals(getProduction(), other.getProduction())) {
            return false;
        }
        if (!BeanUtils.safeEquals(getSpec(), other.getSpec())) {
            return false;
        }
        if (!BeanUtils.safeEquals(getLineNo(), other.getLineNo())) {
            return false;
        }
        if (!BeanUtils.safeEquals(getProduction(), other.getProduction())) {
            return false;
        }
        return true;
    }

    public int getDataSize() {
        if (getKeys() == null) {
            return 0;
        } else {
            return getKeys().size();
        }
    }

    public List<Object> getKeys() {
        return keys;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    protected String format(Timestamp date) {
        if (date == null) {
            return "";
        }
        return StringUtil.dateToString(date, "yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LetResultDownload").append("{");
        sb.append("jobId:").append(getJobId()).append(",");
        sb.append("program:").append(getProgram()).append(",");
        sb.append("status:").append(getStatus()).append(",");
        sb.append("progress:").append(getProgress()).append(",");
        sb.append("downloaded:").append(getTotalCount()).append(",");
        sb.append("downloadStarted:").append(getDownloadStarted()).append(",");
        sb.append("downloadFinished:").append(getDownloadFinished()).append(",");
        if (StringUtils.isNotBlank(getProductId())) {
            sb.append("productId:").append(getProductId()).append(",");
        }
        if (getStartDate() != null) {
            sb.append("startDate:").append(getStartDate()).append(",");
        }
        if (getEndDate() != null) {
            sb.append("endDate:").append(getEndDate()).append(",");
        }
        sb.append("spec:").append(getSpec()).append(",");
        if (StringUtils.isNotBlank(getLineNo())) {
            sb.append("lineNo:").append(getLineNo()).append(",");
        }
        if (StringUtils.isNotBlank(getProduction())) {
            sb.append("production:").append(getProduction());
        }
        sb.append("}");
        return sb.toString();
    }
}

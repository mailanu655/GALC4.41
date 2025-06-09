package com.honda.mfg.stamp.conveyor.domain;

import java.sql.Timestamp;
import java.util.Date;

/**
 * User: VCC30690
 * Date: 10/11/11
 */
public class AuditErrorLogFinderCriteria {

    private String nodeId;
    private String source;
    private Integer severity;
    private String messageText;
    private Timestamp beginTimestamp;
    private Timestamp endTimestamp;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getSeverity() {
        return severity;
    }

    public void setSeverity(Integer severity) {
        this.severity = severity;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Timestamp getBeginTimestamp() {
        return beginTimestamp;
    }

    public void setBeginTimestamp(Timestamp beginTimestamp) {
        this.beginTimestamp = beginTimestamp;
    }

    public Date getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(Timestamp endTimestamp) {
        this.endTimestamp = endTimestamp;
    }
}

package com.honda.mfg.stamp.conveyor.domain;

import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Component
@Configurable
public class AuditErrorLogDataOnDemand {

	private Random rnd = new java.security.SecureRandom();

	private List<AuditErrorLog> data;

	public AuditErrorLog getNewTransientAuditErrorLog(int index) {
        com.honda.mfg.stamp.conveyor.domain.AuditErrorLog obj = new com.honda.mfg.stamp.conveyor.domain.AuditErrorLog();
        setNodeId(obj, index);
        setSource(obj, index);
        setSeverity(obj, index);
        setMessageText(obj, index);
        setLogTimestamp(obj, index);
        return obj;
    }

	public void setNodeId(AuditErrorLog obj, int index) {
        java.lang.String NodeId = "NodeId_" + index;
        obj.setNodeId(NodeId);
    }

	public void setSource(AuditErrorLog obj, int index) {
        java.lang.String Source = "Source_" + index;
        obj.setSource(Source);
    }

	public void setSeverity(AuditErrorLog obj, int index) {
        java.lang.Integer Severity = new Integer(index);
        obj.setSeverity(Severity);
    }

	public void setMessageText(AuditErrorLog obj, int index) {
        java.lang.String MessageText = "MessageText_" + index;
        obj.setMessageText(MessageText);
    }

	public void setLogTimestamp(AuditErrorLog obj, int index) {
        java.sql.Timestamp LogTimestamp = null;
        obj.setLogTimestamp(LogTimestamp);
    }

	public AuditErrorLog getSpecificAuditErrorLog(int index) {
        init();
        if (index < 0) index = 0;
        if (index > (data.size() - 1)) index = data.size() - 1;
        AuditErrorLog obj = data.get(index);
        return AuditErrorLog.findAuditErrorLog(obj.getId());
    }

	public AuditErrorLog getRandomAuditErrorLog() {
        init();
        AuditErrorLog obj = data.get(rnd.nextInt(data.size()));
        return AuditErrorLog.findAuditErrorLog(obj.getId());
    }

	public boolean modifyAuditErrorLog(AuditErrorLog obj) {
        return false;
    }

	public void init() {
        data = com.honda.mfg.stamp.conveyor.domain.AuditErrorLog.findAuditErrorLogEntries(0, 10);
        if (data == null) throw new IllegalStateException("Find entries implementation for 'AuditErrorLog' illegally returned null");
        if (!data.isEmpty()) {
            return;
        }
        
        data = new java.util.ArrayList<com.honda.mfg.stamp.conveyor.domain.AuditErrorLog>();
        for (int i = 0; i < 10; i++) {
            com.honda.mfg.stamp.conveyor.domain.AuditErrorLog obj = getNewTransientAuditErrorLog(i);
            obj.persist();
            obj.flush();
            data.add(obj);
        }
    }
}

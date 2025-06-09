package com.honda.mfg.stamp.conveyor.domain;

import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.PartProductionVolume;
import com.honda.mfg.stamp.conveyor.domain.enums.Press;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: vcc30690
 * Date: 2/1/12
 * Time: 11:01 AM
 * To change this template use File | Settings | File Templates.
 */
@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Transactional
public class AuditErrorLogFinderTest {

    Calendar c;
    Timestamp timestamp;
    @Test
    public void successfullyGetErrorLogsForCriteria() {
        loadAuditErrorLogTable ();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);

        Timestamp beginTimestamp = new Timestamp(c.getTimeInMillis());

         c.add(Calendar.DATE, 2);
        Timestamp afterTimestamp = new Timestamp(c.getTimeInMillis());

        AuditErrorLogFinderCriteria auditErrorLogFinderCriteria = new AuditErrorLogFinderCriteria();
        auditErrorLogFinderCriteria.setBeginTimestamp(beginTimestamp);
        auditErrorLogFinderCriteria.setEndTimestamp(afterTimestamp);
        auditErrorLogFinderCriteria.setMessageText("message");
        auditErrorLogFinderCriteria.setNodeId("node");
        auditErrorLogFinderCriteria.setSeverity(1);
        auditErrorLogFinderCriteria.setSource("source");

        List<AuditErrorLog> auditErrorLogs = AuditErrorLog.findAuditErrorLogByCriteria(auditErrorLogFinderCriteria,1,1) ;

        assertEquals(1,auditErrorLogs.size());
        assertEquals("message", auditErrorLogs.get(0).getMessageText());
        assertEquals("node", auditErrorLogs.get(0).getNodeId());
        assertEquals(1,auditErrorLogs.get(0).getSeverity().intValue());
        assertEquals("source", auditErrorLogs.get(0).getSource());
        assertEquals(timestamp,auditErrorLogs.get(0).getLogTimestamp());

    }

    private void loadAuditErrorLogTable() {
         Calendar c = Calendar.getInstance();
        timestamp = new Timestamp(c.getTimeInMillis());
        AuditErrorLog errorLog = new AuditErrorLog();
        errorLog.setLogTimestamp(timestamp);
        errorLog.setMessageText("message");
        errorLog.setNodeId("node");
        errorLog.setSeverity(1);
        errorLog.setSource("source");

        errorLog.persist();

    }

}

package com.honda.mfg.stamp.conveyor.helper;

import com.honda.mfg.stamp.conveyor.domain.*;
import com.honda.mfg.stamp.conveyor.domain.enums.PartProductionVolume;
import com.honda.mfg.stamp.conveyor.domain.enums.StopArea;
import com.honda.mfg.stamp.conveyor.domain.enums.StopAvailability;
import com.honda.mfg.stamp.conveyor.domain.enums.StopType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.print.attribute.standard.Severity;
import java.sql.Timestamp;
import java.util.Calendar;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: Ambica Gawarla
 * Date: 1/2/14
 * Time: 9:38 AM
 * To change this template use File | Settings | File Templates.
 */

@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Transactional
public class AbstractHelperImplTest {

    @Test
    public void successfullySaveToAuditLog() {
        loadToAuditLogTable();
        AbstractTestHelperImpl helperImpl = new AbstractTestHelperImpl();
        helperImpl.saveToAuditLog("Node2", " test message", "testsource");
    }

    @Test
    public void successfullyGetParmValue() {
        loadTestParmData();
        AbstractTestHelperImpl helperImpl = new AbstractTestHelperImpl();
        int x = helperImpl.getParmValue("deliveryCarrierReleaseCount");
        assertNotNull(x);
    }

    @Test
    public void successfullyGetEmptyDie() {
        loadDieTable();
        AbstractTestHelperImpl helperImpl = new AbstractTestHelperImpl();
        Die x = helperImpl.getEmptyDie();
        assertNotNull(x);
    }

    @Test
    public void successfullyGenerateAlarm() {
        loadAlarmEventTable();
        AbstractTestHelperImpl helperImpl = new AbstractTestHelperImpl();
        helperImpl.generateAlarm(100, 101);
    }

    @Test
    public void successfullyPopulateCarrier() {
        loadStopTable();
        loadDieTable();

        CarrierMes carrier = new CarrierMes();
        carrier.setCarrierNumber(101);
        carrier.setDieNumber(999);
        carrier.setQuantity(1);
        carrier.setCurrentLocation(1201L);
        carrier.setDestination(1201L);
        carrier.setBuffer(new Integer(1));
        carrier.setOriginationLocation(0);
        carrier.setStatus(0);
        carrier.setProductionRunNumber(100);

        CarrierMes carrier1 = new CarrierMes();
        carrier1.setCarrierNumber(101);
        carrier1.setDieNumber(999);
        carrier1.setQuantity(1);
        carrier1.setCurrentLocation(1201L);
        carrier1.setDestination(1201L);
        carrier1.setBuffer(new Integer(1));
        carrier1.setProductionRunNumber(100);


        AbstractTestHelperImpl helperImpl = new AbstractTestHelperImpl();
        assertNotNull(helperImpl.populateCarrier(carrier));
        assertNotNull(helperImpl.populateCarrier(carrier1));
    }



    public void loadTestParmData() {
        ParmSetting setting = new ParmSetting();
        setting.setFieldname("deliveryCarrierReleaseCount");
        setting.setFieldvalue("4");
        setting.setDescription("deliveryCarrierReleaseCount");
        setting.setUpdatedby("test");
        setting.setUpdatetstp(new Timestamp(System.currentTimeMillis()));

        setting.persist();

        ParmSetting setting1 = new ParmSetting();
        setting1.setFieldname("fulfillmentCarrierInspectionStop");
        setting1.setFieldvalue("1300");
        setting1.setDescription("fulfillmentCarrierInspectionStopt");
        setting1.setUpdatedby("test");
        setting1.setUpdatetstp(new Timestamp(System.currentTimeMillis()));

        setting1.persist();
    }

     void loadStopTable() {
        Stop stop = new Stop();
        stop.setId(1201L);
        stop.setName("ST12-1");
        stop.setStopType(StopType.NO_ACTION);
        stop.setStopArea(StopArea.ROW);

        stop.persist();
    }

    public void loadDieTable() {
        Die leftDie = new Die();
        leftDie.setId(101l);
        leftDie.setDescription("left_die_101");
        leftDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

        Die rightDie = new Die();
        rightDie.setId(102l);
        rightDie.setDescription("right_die_102");
        rightDie.setPartProductionVolume(PartProductionVolume.MEDIUM_VOLUME);

        Die someDie = new Die();
        someDie.setId(999l);
        someDie.setDescription("empty_die_999");
        someDie.setPartProductionVolume(PartProductionVolume.EMPTY);

        leftDie.persist();
        rightDie.persist();
        someDie.persist();
    }

    public void loadAlarmEventTable() {
        AlarmEvent event = new AlarmEvent();
        event.setAlarmNumber(1);
        event.setLocation(1201);

        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, -1);

        Timestamp timestamp = new Timestamp(c.getTimeInMillis());
        event.setEventTimestamp(timestamp);
        //event.setCleared(false);
        //event.setClearedBy("none");
        // event.setNotified(false);
        event.persist();

    }

    public void loadToAuditLogTable() {
        AuditErrorLog log = new AuditErrorLog();
        log.setLogTimestamp(new Timestamp(System.currentTimeMillis()));
        log.setMessageText(" error");
        log.setNodeId(" NODE1");
        log.setSeverity(Severity.ERROR.getValue());
        log.setSource("test");
        log.persist();

    }

    private class AbstractTestHelperImpl extends AbstractHelperImpl {

    }
}

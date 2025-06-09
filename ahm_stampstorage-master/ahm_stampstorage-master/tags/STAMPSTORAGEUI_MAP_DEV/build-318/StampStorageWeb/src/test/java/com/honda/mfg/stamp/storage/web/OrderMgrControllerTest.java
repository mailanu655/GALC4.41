package com.honda.mfg.stamp.storage.web;

import com.honda.mfg.stamp.conveyor.domain.OrderMgr;
import com.honda.mfg.stamp.conveyor.domain.Stop;
import com.honda.mfg.stamp.conveyor.domain.enums.StopArea;
import com.honda.mfg.stamp.conveyor.domain.enums.StopAvailability;
import com.honda.mfg.stamp.conveyor.domain.enums.StopType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: Ambica Gawarla
 * Date: 3/19/12
 * Time: 10:12 AM
 * To change this template use File | Settings | File Templates.
 */
@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Transactional
public class OrderMgrControllerTest {

    @Test
    public void successfullyTestOrderMgrController() {
        loadTestData();
        OrderMgrController controller = new OrderMgrController();
        assertNotNull(controller.populateStops());
        assertNotNull(controller.populateOrderMgrs());
    }

    public void loadTestData() {

        Stop stop1 = new Stop(1300L);
        stop1.setName("stop1");
        stop1.setStopType(StopType.NO_ACTION);
        stop1.setStopArea(StopArea.UNDEFINED);

        Stop stop2 = new Stop(1301L);
        stop2.setName("stop2");
        stop2.setStopType(StopType.NO_ACTION);
        stop2.setStopArea(StopArea.UNDEFINED);

        Stop stop3 = new Stop(700L);
        stop3.setName("stop3");
        stop3.setStopType(StopType.NO_ACTION);
        stop3.setStopArea(StopArea.UNDEFINED);

        Stop stop4 = new Stop(1201L);
        stop4.setName("row");
        stop4.setStopType(StopType.NO_ACTION);
        stop4.setStopArea(StopArea.UNDEFINED);

        Stop stop5 = new Stop(708L);
        stop5.setName("kd");
        stop5.setStopType(StopType.NO_ACTION);
        stop5.setStopArea(StopArea.KD_LINE);

        stop1.persist();
        stop2.persist();
        stop3.persist();
        stop4.persist();
        stop5.persist();

        OrderMgr orderMgr = new OrderMgr();
        orderMgr.setId(1L);
        orderMgr.setLineName("line1");
        orderMgr.setMaxDeliveryCapacity(new Integer(12));
        orderMgr.setDeliveryStop(stop3);
        orderMgr.setLeftConsumptionExit(stop3);
        orderMgr.setLeftConsumptionStop(stop3);
        orderMgr.setRightConsumptionExit(stop3);
        orderMgr.setRightConsumptionStop(stop3);
        orderMgr.persist();
    }
}

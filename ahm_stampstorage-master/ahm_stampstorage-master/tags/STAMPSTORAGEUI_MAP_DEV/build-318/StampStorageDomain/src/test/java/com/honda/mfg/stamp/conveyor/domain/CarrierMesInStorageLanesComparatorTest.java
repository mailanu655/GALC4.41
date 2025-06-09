package com.honda.mfg.stamp.conveyor.domain;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.fail;

/**
 * User: Jeffrey M Lutz
 * Date: 6/19/11
 */
public class CarrierMesInStorageLanesComparatorTest {
    private static final Long DESTINATION_ONE = 1L;
    private static final Long DESTINATION_TWO = 2L;
    private long before = System.currentTimeMillis();
    private long after = before + 1000;
    private Date beforeDate = new Date(before);
    private Date afterDate = new Date(after);

    @Test
    public void successfullyTestDifferentDestinations() {
        List<CarrierMes> carriers = new ArrayList<CarrierMes>();
        for (int i = 10; i > 0; i--) {
            carriers.add(createCarrier(new Long(i)));
        }
        if (isAscendingOrder(carriers)) {
            fail();
        }
        Collections.sort(carriers, new CarrierMesInStorageLanesComparator());
        if (!isAscendingOrder(carriers)) {
            fail();
        }
    }

    @Test
    public void successfullyTestSameDestinationsAndDifferentUpdateDates() {
        List<CarrierMes> carriers = new ArrayList<CarrierMes>();
        carriers.add(createCarrier(DESTINATION_ONE, afterDate));
        carriers.add(createCarrier(DESTINATION_ONE, beforeDate));
        if (isAscendingOrder(carriers)) {
            fail();
        }
        Collections.sort(carriers, new CarrierMesInStorageLanesComparator());
        if (!isAscendingOrder(carriers)) {
            fail();
        }
    }

    @Test
    public void successfullyTestSameDestinationsAndDifferentUpdateDates2() {
        List<CarrierMes> carriers = getCarriersForDestination(DESTINATION_ONE);
        if (isAscendingOrder(carriers)) {
            fail();
        }
        Collections.sort(carriers, new CarrierMesInStorageLanesComparator());
        if (!isAscendingOrder(carriers)) {
            fail();
        }
    }

    @Test
    public void successfullyTestSameDestinationsWithDifferentUpdateDate() {
        List<CarrierMes> carriers = getCarriersForDestination(DESTINATION_ONE);
        if (isAscendingOrder(carriers)) {
            fail();
        }
        Collections.sort(carriers, new CarrierMesInStorageLanesComparator());
        if (!isAscendingOrder(carriers)) {
            fail();
        }
    }

    @Test
    public void successfullyTestSameDestinationsWithDifferentUpdateDateAndBuffer() {
        List<CarrierMes> carriers = getCarriersForDestination(DESTINATION_ONE);
        if (isAscendingOrder(carriers)) {
            fail();
        }
        Collections.sort(carriers, new CarrierMesInStorageLanesComparator());
        if (!isAscendingOrder(carriers)) {
            fail();
        }
    }


    @Test
    public void successfullyTestDifferentDestinationsWithDifferentUpdateDateAndBuffer() {
        List<CarrierMes> carriers = getCarriersForDestination(DESTINATION_TWO);
        carriers.addAll(getCarriersForDestination(DESTINATION_ONE));
        if (isAscendingOrder(carriers)) {
            fail();
        }
        Collections.sort(carriers, new CarrierMesInStorageLanesComparator());
        if (!isAscendingOrder(carriers)) {
            fail();
        }
    }

    private List<CarrierMes> getCarriersForDestination(Long destination) {
        List<CarrierMes> carriers = new ArrayList<CarrierMes>();
        for (int i = 0; i < 10; i++) {
            carriers.add(createCarrier(destination, i % 2 == 0 ? beforeDate : afterDate));
        }
        carriers.add(createCarrier(destination, afterDate, 1));
        carriers.add(createCarrier(destination, beforeDate, 1));
        return carriers;
    }

    private boolean isAscendingOrder(List<CarrierMes> carriers) {
        Long prev = -1L;
        Integer prevBuffer = 0;
        Date prevDate = null;
        for (CarrierMes carrier : carriers) {
            Long cur = carrier.getDestination();
            Date curDate = carrier.getUpdateDate();
            Integer curBuffer = carrier.getBuffer();
            if (prev == -1L) {
                prev = cur;
                prevBuffer = curBuffer;
                prevDate = curDate;
                continue;
            }
            long curMS = curDate.getTime();
            long prevMS = prevDate.getTime();
            if (cur < prev && curBuffer == prevBuffer) {
                return false;
            } else if (cur == prev && curMS < prevMS && curBuffer == prevBuffer) {
                return false;
            }
            prev = cur;
            prevDate = curDate;
        }
        return true;
    }

    private CarrierMes createCarrier(Long destination) {
        return createCarrier(destination, new Date());
    }

    private CarrierMes createCarrier(Long destination, Date updateDate) {
        return createCarrier(destination, updateDate, 0);
    }

    private CarrierMes createCarrier(Long destination, Date updateDate, int buffer) {
        CarrierMes c = new CarrierMes();
        c.setDestination(destination);
        c.setUpdateDate(updateDate);
        c.setBuffer(buffer);
        return c;
    }
}

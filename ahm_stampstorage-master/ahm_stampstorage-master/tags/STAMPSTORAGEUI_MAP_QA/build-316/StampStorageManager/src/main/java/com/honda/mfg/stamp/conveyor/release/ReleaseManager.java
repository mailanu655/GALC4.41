package com.honda.mfg.stamp.conveyor.release;

import com.honda.mfg.stamp.conveyor.domain.Stop;

/**
 * Created by IntelliJ IDEA.
 * User: vcc30690
 * Date: 1/16/12
 * Time: 10:53 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ReleaseManager {
/**
 * If Carrier is at head of row (buffer=1), and no other carriers releasing from this area, Publish an update message.
 * @param carrierNumber
 * @param destination
 * @param source
 * @param releaseManager
 */
   void releaseCarrier(Integer carrierNumber, Stop destination, String source, boolean releaseManager);
}

package com.honda.mfg.stamp.conveyor.messages;

import org.junit.Test;
import static junit.framework.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: Ambica Gawarla
 * Date: 2/13/12
 * Time: 12:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class StaleDataMessageTest {

     @Test
    public void successfullyCreateValidMessage() {
      StaleDataMessage staleDataMessage = new StaleDataMessage(true);
      assertEquals(true, staleDataMessage.isStale());
    }
}

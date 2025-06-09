package com.honda.mfg.stamp.conveyor.messages;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: Ambica Gawarla
 * Date: 2/13/12
 * Time: 11:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class RecalculateDestinationMessageTest {

    @Test
    public void successfullyCreateValidMessage() {
        RecalculateDestinationMessage recalculateDestinationMessage = new RecalculateDestinationMessage(true);
        assertEquals(true, recalculateDestinationMessage.isRecalc());
    }
}

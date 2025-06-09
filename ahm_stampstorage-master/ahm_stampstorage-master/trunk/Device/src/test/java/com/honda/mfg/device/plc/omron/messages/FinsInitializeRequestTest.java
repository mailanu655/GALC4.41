package com.honda.mfg.device.plc.omron.messages;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * User: Jeffrey M Lutz
 * Date: 4/10/11
 */
public class FinsInitializeRequestTest {

    @Test
    public void successfullyExercise() {
        FinsInitializeRequest request =
                new FinsInitializeRequest();
        assertEquals(request.getMessageRequest().hashCode(), request.hashCode());
        assertEquals(true, request.equals(new FinsInitializeRequest()));
        assertEquals(new FinsInitializeRequest().getMessageRequest(), request.getMessageRequest());
    }
}

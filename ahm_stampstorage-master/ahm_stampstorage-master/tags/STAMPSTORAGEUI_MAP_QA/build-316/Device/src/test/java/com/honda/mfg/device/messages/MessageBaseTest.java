package com.honda.mfg.device.messages;

import com.honda.mfg.device.plc.omron.exceptions.FinsRequestException;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;

/**
 * User: Jeffrey M Lutz
 * Date: 4/7/11
 */
public class MessageBaseTest {


    @Test
    public void successfullyReturnAsChars() {

        long number = 123456;
        int numberOfCharacters = 6;

        MessageBase msgBase = new MessageBase();

        char[] charSequence = msgBase.asChars(number, numberOfCharacters);
        assertNotNull(charSequence);

        long number1 = 12345678;
        char[] charSequence1 = msgBase.asChars(number1, 8);
        assertNotNull(charSequence1);
    }

    @Test(expected = FinsRequestException.class)
    public void throwsExceptionAttemptingToConvertNumberTooLarge() {
        long number = 1234567890;
        int numberOfCharacters = 1;

        MessageBase msgBase = new MessageBase();
        char[] charSequence = msgBase.asChars(number, numberOfCharacters);


    }


}

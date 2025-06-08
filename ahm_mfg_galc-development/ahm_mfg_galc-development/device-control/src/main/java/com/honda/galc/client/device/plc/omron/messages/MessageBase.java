package com.honda.galc.client.device.plc.omron.messages;

import com.honda.galc.client.device.plc.omron.exceptions.FinsRequestException;

/**
 * User: Jeffrey M Lutz
 * Date: Nov 9, 2010
 */
public class MessageBase {

    protected char[] asChars(long number, int numberOfCharacters) {
        assertNumberIsNotTooLarge(number, numberOfCharacters);
        long remainder = number;
        char[] lengthChars = new char[numberOfCharacters];
        for (int i = 0; i < lengthChars.length; i++) {
            lengthChars[numberOfCharacters - 1 - i] = (char) (remainder % 256);
            remainder = remainder / 256;
        }
        return lengthChars;
    }

    protected void assertNumberIsNotTooLarge(long number, int numberOfCharacters) {
        long maxIntegerForNumberOfCharacters = (long) (Math.pow(2, (8 * numberOfCharacters)) - 1);
        if (numberOfCharacters == 8) {
            maxIntegerForNumberOfCharacters = (long) ((Math.pow(2, (8 * numberOfCharacters)) - 1) / 2);
        }
        if (number > maxIntegerForNumberOfCharacters) {
            throw new FinsRequestException(number + " cannot be represented using " + numberOfCharacters + " hexadecimal characters");
        }
    }
}

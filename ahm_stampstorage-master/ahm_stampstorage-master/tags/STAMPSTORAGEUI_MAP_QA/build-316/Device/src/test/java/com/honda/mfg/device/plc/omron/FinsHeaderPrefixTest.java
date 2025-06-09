package com.honda.mfg.device.plc.omron;

import com.honda.mfg.device.plc.omron.exceptions.FinsHeaderException;
import org.junit.Assert;
import org.junit.Test;

/**
 * User: Jeffrey M Lutz
 * Date: Sep 16, 2010
 * Time: 10:25:51 AM
 */
public class FinsHeaderPrefixTest {


    @Test
    public void successfullyCreatesFinsHeaderPrefixWhenPassedValidMessageLength() {
        FinsHeaderPrefix header = new FinsHeaderPrefix(2000);
        String messageLengthString = new String(new char[]{0x00, 0x00, 0x07, 0xD0});
        Assert.assertEquals("Wrong fins prefix", "FINS" + messageLengthString, header.getHeader());
    }

    @Test
    public void successfullyCreatesFinsHeaderPrefixWhenPassedValidMessageHeader() {
        String h = new String(new char[]{0x00, 0x00, 0x07, 0xD0});
        FinsHeaderPrefix header = new FinsHeaderPrefix("FINS" + h);
        String messageLengthString = "FINS" + h;
        Assert.assertEquals("Wrong fins prefix", messageLengthString, header.getHeader());
    }

    @Test(expected = FinsHeaderException.class)
    public void throwExceptionCreatingFinsPrefixHeaderWithTooLargeDecimalLength() {
        new FinsHeaderPrefix(2001);
    }

    @Test(expected = FinsHeaderException.class)
    public void checksThatHeaderPrefixStartsWithFINS() {
        String h = new String(new char[]{0x00, 0x00, 0x07, 0xD0});
        FinsHeaderPrefix header = new FinsHeaderPrefix("DOGS" + h);
    }

    @Test(expected = FinsHeaderException.class)
    public void checksThatHeaderPrefixHasExpectedLength() {
        new FinsHeaderPrefix("FINS12345");
    }

    @Test(expected = FinsHeaderException.class)
    public void checksThatMessageLengthIsWithinRange() {
        String messageLengthString = new String(new char[]{0x00, 0x00, 0x07, 0xD1});
        new FinsHeaderPrefix("FINS" + messageLengthString);
    }

    @Test
    public void succesfullyCreatesFinsHeaderPrefix() {
        String messageLengthString = new String(new char[]{0x00, 0x00, 0x07, 0xD0});
        FinsHeaderPrefix headerPrefix = new FinsHeaderPrefix("FINS" + new String(messageLengthString));
        Assert.assertNotNull("Did not really create a fins header prefix object", headerPrefix);
        Assert.assertEquals("Fins Header Prefix does not have expected messages length", 2000, headerPrefix.getMessageLength());
    }
}

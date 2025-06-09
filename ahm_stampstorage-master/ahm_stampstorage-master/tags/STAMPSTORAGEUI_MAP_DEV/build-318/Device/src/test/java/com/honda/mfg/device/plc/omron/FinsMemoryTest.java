package com.honda.mfg.device.plc.omron;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * User: Jeffrey M Lutz
 * Date: Sep 13, 2010
 * Time: 11:07:38 AM
 */
public class FinsMemoryTest {
    private int SMALLEST_START_ADDRESS = 0;
    private int LARGEST_START_ADDRESS = 65535;
    private int SMALLEST_LEGAL_DATA_LENGTH = 0;
    private int LARGEST_LEGAL_DATA_LENGTH = 256;


    @Test
    public void createsMemoryWithBeginningLocationHasSmallestLegalValue() {
        FinsMemory memory = new FinsMemory(FinsMemory.BANK.DM, SMALLEST_START_ADDRESS, SMALLEST_LEGAL_DATA_LENGTH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionWhenBeginningLocationIsTooSmall() {
        FinsMemory memory = new FinsMemory(FinsMemory.BANK.DM, SMALLEST_START_ADDRESS - 1, SMALLEST_LEGAL_DATA_LENGTH);
    }

    @Test
    public void createsMemoryWithBeginningLocationLargestLegalValue() {
        FinsMemory memory = new FinsMemory(FinsMemory.BANK.DM, LARGEST_START_ADDRESS, SMALLEST_LEGAL_DATA_LENGTH);
        String expectedStringRepresentation = "MEMORY:  DM:65535[0]";
        assertNotNull(memory.toString());
        assertEquals(expectedStringRepresentation, memory.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionWhenBeginningLocationIsTooLarge() {
        FinsMemory memory = new FinsMemory(FinsMemory.BANK.DM, LARGEST_START_ADDRESS + 1, SMALLEST_LEGAL_DATA_LENGTH);
    }

    @Test
    public void createsMemoryWithSizeHasSmallestLegalValue() {
        FinsMemory memory = new FinsMemory(FinsMemory.BANK.DM, SMALLEST_START_ADDRESS, SMALLEST_LEGAL_DATA_LENGTH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionWhenSizeIsTooSmall() {
        FinsMemory memory = new FinsMemory(FinsMemory.BANK.DM, SMALLEST_START_ADDRESS, SMALLEST_LEGAL_DATA_LENGTH - 1);
    }

    @Test
    public void createsMemoryWithSizeLargestLegalValue() {
        FinsMemory memory = new FinsMemory(FinsMemory.BANK.DM, LARGEST_START_ADDRESS, LARGEST_LEGAL_DATA_LENGTH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionWhenSizeIsTooLarge() {
        FinsMemory memory = new FinsMemory(FinsMemory.BANK.DM, LARGEST_START_ADDRESS, LARGEST_LEGAL_DATA_LENGTH + 1);
    }


}

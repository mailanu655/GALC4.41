package com.honda.mfg.device.rfid.etherip;

import com.honda.mfg.device.rfid.etherip.messages.RfidCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * User: Jeffrey M Lutz
 * Date: Nov 16, 2010
 * Time: 12:46:44 PM
 */
public class RfidResponseParser {
    private static final Logger LOG = LoggerFactory.getLogger(RfidResponseParser.class);

    private static final int COMMAND_LOCATION = 3;
    private static final int READ_TAG_VALUE_START_POS = 12;
    private static final int READ_TAG_ID_ERROR_MESSAGE_LENGTH = 14;
    private static final int TAG_ID_SECONDARY_LENGTH_POSITION = 11;
    private static final int MONTH_POSITION = 6;
    private static final int DAY_POSITION = 7;
    private static final int HOURS_POSITION = 8;
    private static final int MINUTES_POSITION = 9;
    private static final int SECONDS_POSITION = 10;

    private static final String DATE_FORMAT = "MM/dd/yyyy HH:mm:ss";
    private static final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

    public RfidCommand getRfidCommand(String response) {
        int code = response.charAt(COMMAND_LOCATION);
        for (RfidCommand c : RfidCommand.values()) {
            if (code == c.getCode()) {
                return c;
            }
        }
        return RfidCommand.UNKNOWN;
    }

    public int getInstanceCounter(String response) {
        return response.charAt(4);
    }

    public int getNodeId(String response) {
        return response.charAt(5);
    }

    public String getTagValue(String response) {
        if (!getRfidCommand(response).equals(RfidCommand.READ_TAG)) {
            return null;
        }
        if (response.length() < READ_TAG_VALUE_START_POS) {
            return null;
        }
        return response.substring(READ_TAG_VALUE_START_POS);
    }

    public String getTagIdString(String response) {
        /**     +-> Length (in words)
         *      |  +-> Literal (0xAA)
         *      |  |  +-> Command (0x7)
         *      |  |  |  +-> Instance Counter (hex)
         *      |  |  |  |  +-> Node ID (hex)
         *      |  |  |  |  |  +-> Month (hex)
         *      |  |  |  |  |  |  +-> Day (hex)
         *      |  |  |  |  |  |  |  +-> Hours (hex)
         *      |  |  |  |  |  |  |  |  +-> Minutes (hex)
         *      |  |  |  |  |  |  |  |  |  +-> Seconds (hex)
         *      |  |  |  |  |  |  |  |  |  |  +-> Additional Length (in words)
         *      |  |  |  |  |  |  |  |  |  |  |  +--+-> Tag ID #1
         *      |  |  |  |  |  |  |  |  |  |  |  |  |  +--+-> Tag ID #2
         * [00 07 AA 07 03 01 01 03 16 04 14 02 E0 04 AC 93 ]
         *         +-+--------------------------+-> Read Tag Id error response "Tag Not Found"
         * [00 07 FF FF 03 01 01 03 16 04 14 01 07 00 ]
         * 	 Node 1 	Tag Not Found
         */
        Boolean isError = response.length() < READ_TAG_ID_ERROR_MESSAGE_LENGTH || getRfidCommand(response).equals(RfidCommand.ERROR);
        if (isError) {
            return null;
        }
        return response.substring(READ_TAG_VALUE_START_POS);
    }

    public Date parseDate(String response) {

        Date date = null;
        String dateStr = null;
        try {
            dateStr = getDateString(response);
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            LOG.warn("Attempt to parse invalid date.  Not of form: " + DATE_FORMAT + " --> Actual date: " + dateStr);
        } catch (StringIndexOutOfBoundsException e) {
            LOG.warn("Attempt to parse invalid date.  Not of form: " + DATE_FORMAT + " --> Actual date: " + dateStr);
        }
        return date;
    }

    private String getDateString(String response) {
        Calendar c = new GregorianCalendar();

        int month = response.charAt(MONTH_POSITION);
        int day = response.charAt(DAY_POSITION);
        int hours = response.charAt(HOURS_POSITION);
        int min = response.charAt(MINUTES_POSITION);
        int secs = response.charAt(SECONDS_POSITION);
        int year = c.get(Calendar.YEAR);

        StringBuilder sb = new StringBuilder();
        sb.append(month);
        sb.append("/");
        sb.append(day);
        sb.append("/");
        sb.append(year);
        sb.append(" ");
        sb.append(hours);
        sb.append(":");
        sb.append(min);
        sb.append(":");
        sb.append(secs);
        return sb.toString();
    }

    public boolean validLength(String response) {
        return !(response == null || response.length() < TAG_ID_SECONDARY_LENGTH_POSITION);
    }
}

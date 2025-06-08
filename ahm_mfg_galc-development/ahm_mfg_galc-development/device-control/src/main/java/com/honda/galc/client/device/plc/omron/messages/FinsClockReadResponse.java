package com.honda.galc.client.device.plc.omron.messages;

/**
 * User: Jeffrey M Lutz
 * Date: Sep 23, 2010
 * Time: 10:33:12 AM
 */
public class FinsClockReadResponse extends FinsResponseBase {

    public FinsClockReadResponse(int destinationNode, int sourceNode, int serviceId, StringBuilder data) {
        super(destinationNode, sourceNode, serviceId, FinsCommand.CLOCK_READ, data);
    }

    public String toString() {
        return clockReading();
    }

    public String clockReading() {
        StringBuilder string = getData();
        StringBuilder hexStr = new StringBuilder();
        for (int i = 0; string != null && i < string.length(); i++) {
            if (i == 1) {
                hexStr.append("-");
            }
            if (i == 2) {
                hexStr.append("-");
            }
            if (i == 3) {
                hexStr.append(" ");
            }
            if (i == 4) {
                hexStr.append(":");
            }
            if (i == 5) {
                hexStr.append(":");
            }
            if (i == 6) {
                hexStr.append("_");
            }
            String hexVal = Integer.toHexString(string.charAt(i));
            if (hexVal.length() < 2) {
                hexStr.append("0");
            }
            hexStr.append(hexVal);
        }
        return hexStr.toString();
    }
}

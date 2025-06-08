/**
 * 
 */
package com.honda.galc.util;

import java.text.MessageFormat;

/**
 * @author Subu Kathiresan
 * Nov 30, 2011
 */
public class TimeTicks {

    private static final String FORMAT_PATTERN = "{0,choice,0#|1#1 day, |1<{0,number,integer} days, }"
            											+ "{1,number,integer}:{2,number,00}:{3,number,00}.{4,number,00}";

    private long _value = -1;
    
    public TimeTicks() {
    }

    public TimeTicks(long value) {
    	_value = value;
    }

    public TimeTicks(TimeTicks other) {
        _value = other.getValue();
    }

    public Object clone() {
        return new TimeTicks(_value);
    }

    public String toString() {
        return toString(FORMAT_PATTERN);
    }

    public String toString(String pattern) {
        long days, hours, minutes, seconds, mseconds;
        long ticks = getValue();

        days = ticks/86400000;
        ticks %= 86400000;

        hours = ticks/3600000;
        ticks %= 3600000;

        minutes = ticks/60000;
        ticks %= 60000;

        seconds = ticks/1000;
        ticks %= 1000;
        
        mseconds = ticks;
        
        Long[] values = new Long[5];
        values[0] = new Long(days);
        values[1] = new Long(hours);
        values[2] = new Long(minutes);
        values[3] = new Long(seconds);
        values[4] = new Long(mseconds);

        return MessageFormat.format(pattern, (Object[]) values);
    }
   
    public long getValue() {
    	return _value;
    }
    
    public void setValue(long value) {
    	_value = value;
    }
    
    public static void main(String[] args) {
    	TimeTicks tTicks = new TimeTicks(999999999999999999L);
    	System.out.println(tTicks.getValue() + " millisecs equals " + tTicks.toString());
    	
    	tTicks = new TimeTicks(360L);
    	System.out.println(tTicks.getValue() + " millisecs equals " + tTicks.toString());
    	
    	tTicks = new TimeTicks(1360L);
    	System.out.println(tTicks.getValue() + " millisecs equals " + tTicks.toString());
    	
    	tTicks = new TimeTicks(65360L);
    	System.out.println(tTicks.getValue() + " millisecs equals " + tTicks.toString());
    	
    	tTicks = new TimeTicks(3800360L);
    	System.out.println(tTicks.getValue() + " millisecs equals " + tTicks.toString());
    	
    	tTicks = new TimeTicks(3232824360L);
    	System.out.println(tTicks.getValue() + " millisecs equals " + tTicks.toString());
    }
}



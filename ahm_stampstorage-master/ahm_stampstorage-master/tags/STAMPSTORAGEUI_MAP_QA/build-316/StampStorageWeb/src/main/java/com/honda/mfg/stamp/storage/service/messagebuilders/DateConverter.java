package com.honda.mfg.stamp.storage.service.messagebuilders;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * User: vcc30690
 * Date: 3/22/11
 */
public class DateConverter extends AbstractSingleValueConverter {

    private static DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    //private boolean toJson;

    public boolean canConvert(Class clazz) {
        return clazz.equals(Date.class);
    }

    @Override
    public Object fromString(String str) {
        GregorianCalendar calendar = new GregorianCalendar();
        try {
            calendar.setTime(formatter.parse(str));
        } catch (ParseException e) {
            throw new ConversionException(e.getMessage(), e);
        }
        return new Date(calendar.getTimeInMillis());
    }

    public String toString(Object obj) {
        return obj == null ? null : formatter.format((Date) obj);
    }

}


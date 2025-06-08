package com.honda.galc.client.qics.util;

import java.util.StringTokenizer;
import java.util.Vector;


public class StringTrim
{
    /**
     * StringTrim constructor.
     */
    public StringTrim()
    {
        super();
    }

    /**
     * A class name is received, and a package name is removed and returned.
     *
     * @param aClassName java.lang.String Class name
     * @return java.lang.String Formatted string
     */
    public static String getClassName(String aClassName)
    {

        String result = null;
        StringTokenizer st = null;
        Vector vToken = null;

        try
        {
            //separate the string dilimited by "."
            st = new StringTokenizer(aClassName, ".");
            vToken = new Vector();
            if (st.hasMoreTokens())
            {
                while (st.hasMoreTokens())
                {
                    vToken.addElement(st.nextToken());
                }
                result = (String) vToken.lastElement();
            }
        }
        catch (Exception ex)
        {
        }

        if ((result.substring(result.length() - 4, result.length())).equals("Bean"))
        {
            result = result.substring(0, result.length() - 4);
        }

        return result;

    }

    /**
     * A class name and a method name are received and corresponding SQLID is returned.
     *
     * @param aClassName java.lang.String Class name
     * @param aSQLName   java.lang.String SQL ID
     * @return java.lang.String Formatted data
     */
    public static String getSQLId(String aClassName, String aSQLName)
    {

        String sqlId = null;
        String className = "com.honda.global.galc.client.qics.data.QICSDataTag";

        try
        {
            sqlId = (String) (Class.forName(className).getDeclaredField(getClassName(aClassName) + aSQLName)).get(new Object());
        }
        catch (NoSuchFieldException ex)
        {
            try
            {
                sqlId = (String) (Class.forName(className).getDeclaredField(getClassName("QicsTask") + aSQLName)).get(new Object());
            } catch (Exception e)
            {
            }
        }
        catch (java.lang.IllegalAccessException ex)
        {
        }
        catch (ClassNotFoundException ex)
        {
        }

        return sqlId;
    }

    /**
     * This method trims spaces in both ends with in a string.
     * It doesn't trim spaces if the first charactor of the string is a space.
     *
     * @param aValue java.lang.String Target string
     * @return java.lang.String Formatted data
     */
    public String trimSpace(String aValue)
    {

        String value = null;

        if (aValue != null && aValue.length() > 0 && (aValue.charAt(0)) != ' ')
        {
            value = aValue.trim();
        } else
        {
            value = aValue;
        }

        return value;
    }
}

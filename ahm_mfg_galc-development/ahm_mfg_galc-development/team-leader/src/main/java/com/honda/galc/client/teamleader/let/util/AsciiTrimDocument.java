package com.honda.galc.client.teamleader.let.util;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;


/**
 * 
 * @author Gangadhararao Gadde
 * @date Aug 26, 2013
 */
public class AsciiTrimDocument extends PlainDocument {
    private static final char MIN_CHAR = ' ';
    private static final char MAX_CHAR = '~';
    private int limit;
    private int scanLimit;

    /**
     * Constructs an ASCII document.
     *
     * @param limit the limit length of the document
     */
    public AsciiTrimDocument(int typeLimit, int scanLimit) {
        limit = typeLimit;
        this.scanLimit = scanLimit;
    }

    /**
     * @see javax.swing.text.Document#insertString(int, java.lang.String, javax.swing.text.AttributeSet)
     */
    public void insertString(int offs, String str, AttributeSet a)
            throws BadLocationException {

        // Input character is trim.
        if (str.length() == scanLimit) {

            if (limit < scanLimit) {
                str = str.substring(0, limit);
            }
        }

        // Check the limit.
        if (getLength() + str.length() > limit) {
            return;
        }

        // Check if the inserting character is an ASCII character.
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) < MIN_CHAR || MAX_CHAR < str.charAt(i)) {
                return;
            }
        }

        super.insertString(offs, str, a);
    }
}
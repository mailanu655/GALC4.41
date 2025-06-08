package com.honda.galc.client.teamleader.let.util;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;


/**
 * 
 * @author Gangadhararao Gadde
 * @date Aug 26, 2013
 */
public class AsciiLimitedCharDocument extends PlainDocument {

    private static final char MIN_CHAR_NUM = '0';
    private static final char MAX_CHAR_NUM = '9';
    private static final char MIN_CHAR = 'A';
    private static final char MAX_CHAR = 'Z';
    private static final String PERMIT_MARKS = "_";
    private int limit;

    /**
     * Constructs an ASCII document.
     *
     * @param limit the limit length of the document
     */
    public AsciiLimitedCharDocument(int limit) {
        this.limit = limit;
    }

    /**
     * @see javax.swing.text.Document#insertString(int, java.lang.String, javax.swing.text.AttributeSet)
     */
    public void insertString(int offs, String str, AttributeSet a)
            throws BadLocationException {
        final String upperStr = str == null ? null : str.toUpperCase();
        // Check the limit.
        if (getLength() + upperStr.length() > limit) {
            return;
        }

        boolean isValidCharacter = true;
        for (int i = 0; i < upperStr.length(); i++) {
            char c = upperStr.charAt(i);
            if ((c < MIN_CHAR || MAX_CHAR < c)
                    && (c < MIN_CHAR_NUM || MAX_CHAR_NUM < c)) {

                if (PERMIT_MARKS.indexOf(c) == -1) {
                    return;
                }
            }
        }

        super.insertString(offs, upperStr, a);
    }

    /**
     * main() method for test.
     */
    public static void main(String[] args) throws Exception {
        JTextField field = new JTextField();
        field.setDocument(new AsciiLimitedCharDocument(10));

        JFrame frame = new JFrame();
        frame.setContentPane(field);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
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
public class AsciiDocument extends PlainDocument {
    private static final char MIN_CHAR = ' ';
    private static final char MAX_CHAR = '~';
    private int limit;

    /**
     * Constructs an ASCII document.
     *
     * @param limit the limit length of the document
     */
    public AsciiDocument(int limit) {
        this.limit = limit;
    }

    /**
     * @see javax.swing.text.Document#insertString(int, java.lang.String, javax.swing.text.AttributeSet)
     */
    public void insertString(int offs, String str, AttributeSet a)
            throws BadLocationException {
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

    /**
     * main() method for test.
     */
    public static void main(String[] args) throws Exception {
        JTextField field = new JTextField();
        field.setDocument(new AsciiDocument(10));

        JFrame frame = new JFrame();
        frame.setContentPane(field);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
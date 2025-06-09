package com.honda.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;

/**
 * User: Jeffrey M Lutz
 * Date: Sep 9, 2010
 * Time: 1:52:57 PM
 */
public interface StreamPair {

    public BufferedReader in();

    public BufferedWriter out();

}

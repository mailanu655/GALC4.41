package org.svenson.tokenize;

import org.svenson.util.ExceptionWrapper;

import java.io.*;

public class InputStreamSource
        implements JSONCharacterSource {
    private Reader reader;

    private int index;

    private boolean close;

    /**
     * Creates an input stream source from the given input stream which must deliver UTF-8 encoded data
     *
     * @param inputStream input stream
     * @param close       if <code>true</code>, the input stream is closed when reaching the end
     */
    public InputStreamSource(InputStream inputStream, boolean close) {
        try {
            this.reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw ExceptionWrapper.wrap(e);
        }
        this.close = close;
    }

    public int getIndex() {
        return index;
    }

    public int nextChar() {
        try {
            int result = reader.read();
            index++;
            return (char) result;
        } catch (IOException e) {
            throw ExceptionWrapper.wrap(e);
        }
    }

    public void destroy() {
        if (close) {
            try {
                reader.close();
            } catch (IOException e) {
                throw ExceptionWrapper.wrap(e);
            }
        }
    }
}

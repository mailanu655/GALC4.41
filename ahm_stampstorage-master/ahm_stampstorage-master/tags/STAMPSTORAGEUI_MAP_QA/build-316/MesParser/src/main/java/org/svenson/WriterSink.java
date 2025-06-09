package org.svenson;

import org.svenson.util.ExceptionWrapper;

import java.io.IOException;
import java.io.Writer;

public class WriterSink implements JSONCharacterSink {
    private Writer writer;

    public WriterSink(Writer writer) {
        this.writer = writer;
    }

    public void append(String token) {
        try {
            writer.write(token);
        } catch (IOException e) {
            throw ExceptionWrapper.wrap(e);
        }
    }

    public void append(char c) {
        try {
            writer.write(c);
        } catch (IOException e) {
            throw ExceptionWrapper.wrap(e);
        }
    }

    public void append(Object o) {
        append(o.toString());
    }

}

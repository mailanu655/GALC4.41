package com.honda.galc.net;

import java.io.Serializable;

public class Response implements Serializable{

    private static final long serialVersionUID = 1L;
    
    
    private Object value;

    private Throwable exception;
    
    
    public Response(Object value) {
        this.value = value;
    }
    
    public Response(Throwable exception) {
        this.exception = exception;
    }
    
    public Object getValue(){
        return value;
    }
    
    public Throwable getException() {
        return this.exception;
    }
    
    public boolean hasException() {
        return this.exception != null;
    }
    
}

package com.honda.galc.rest.json.v2;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

public class V2RestWebApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(JacksonDateFormatProvider.class);
        return classes;
     }

}

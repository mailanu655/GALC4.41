package com.honda.galc.device.simulator.client.ui;

import java.io.IOException;
import java.io.InputStream;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

public class ClasspathResolver implements EntityResolver {

    public InputSource resolveEntity(String publicId, String systemId)
        throws IOException {

    	systemId = systemId.replaceAll("^file:///", "");
        InputStream stream = getClass().getClassLoader().getResourceAsStream(systemId);
        if (stream == null) {
            return null;
        } else {
            return new InputSource(stream);
        }
    }
}

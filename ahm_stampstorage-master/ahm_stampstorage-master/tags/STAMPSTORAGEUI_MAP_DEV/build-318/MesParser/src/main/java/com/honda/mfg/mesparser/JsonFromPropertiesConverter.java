package com.honda.mfg.mesparser;

import java.util.*;

/**
 * User: Jeffrey M Lutz
 * Date: 5/20/11
 */
public class JsonFromPropertiesConverter extends JsonConverterBase {
    private static final String HEADER = "{\"GeneralMessage\":{";
    private static final String FOOTER = "}}";
    private static final String NEST_START = "{";
    private static final String NEST_END = "}";

    private StringBuilder json;
    private Properties p = new Properties();
    private Set<String> processedRoots = new HashSet<String>();

    public JsonFromPropertiesConverter(Properties p) {
        this.p = p;
        json = new StringBuilder();
        json.append(HEADER);
        parseMessage();
        json.append(FOOTER);
    }

    private JsonFromPropertiesConverter(Properties p, String rootKey) {
        this.p = propertiesWithRemovedRootKey(p, rootKey);
        json = new StringBuilder();
        json.append(NEST_START);
        parseMessage();
        json.append(NEST_END);
    }

    public String getJson() {
        return json.toString();
    }

    private Properties propertiesWithRemovedRootKey(Properties p, String rootKey) {
        Properties pp = new Properties();
        for (Iterator it = p.keySet().iterator(); it.hasNext(); ) {
            String key = (String) it.next();
            String val = p.getProperty(key);
            if (key.startsWith(rootKey)) {
                String cleanKey = key.substring(rootKey.length() + 1);
                pp.setProperty(cleanKey, val);
            }
        }
        return pp;
    }

    private void parseMessage() {
//        json.append(HEADER);
        SortedSet sortedSet = new TreeSet(p.keySet());
        for (Iterator it = sortedSet.iterator(); it.hasNext(); ) {
            String key = (String) it.next();
            String val = p.getProperty(key);
            if (isKeyNested(key)) {
                String rootKey = parseRootKey(key);
                if (processedRoots.contains(rootKey)) {
                    continue;
                }
                processedRoots.add(rootKey);
                JsonFromPropertiesConverter c = new JsonFromPropertiesConverter(p, rootKey);
                val = c.getJson();
                json.append("\"").append(rootKey).append("\":").append(val);
                if (anyUnprocessedRoots()) {
                    json.append(",");
                }
            } else {
                json.append("\"").append(key).append("\":").append("\"").append(val).append("\"");
                if (it.hasNext()) {
                    json.append(",");
                }
            }
        }
//        json.append(FOOTER);
    }

    private boolean anyUnprocessedRoots() {
        for (Iterator it = p.keySet().iterator(); it.hasNext(); ) {
            String rootKey = this.parseRootKey((String) it.next());
            if (!processedRoots.contains(rootKey)) {
                return true;
            }
        }
        return false;
    }

    private String parseRootKey(String key) {
        String retVal;
        if (isKeyNested(key)) {
            retVal = key.substring(0, key.indexOf(SEPARATOR_CHAR));
        } else {
            retVal = key;
        }
        return retVal;
    }

    private boolean isKeyNested(String key) {
        return key != null && key.length() > 0 && key.indexOf(SEPARATOR_CHAR) > -1;
    }
}


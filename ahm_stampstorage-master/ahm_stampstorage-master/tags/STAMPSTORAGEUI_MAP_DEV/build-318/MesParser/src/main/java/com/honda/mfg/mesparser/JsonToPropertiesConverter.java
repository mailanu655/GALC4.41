package com.honda.mfg.mesparser;

import org.svenson.JSONParser;

import java.util.Map;
import java.util.Properties;

/**
 * User: Jeffrey M Lutz
 * Date: 5/20/11
 */
public class JsonToPropertiesConverter extends JsonConverterBase {
    private Properties properties;

    public JsonToPropertiesConverter(String json) {
        this.properties = parseJsonWithGeneralMessage(json);
    }

    private String removeGeneralMessageWrapper(String json) {
        if (json == null || json.length() < 1) {
            return json;
        }
        int openIndex = json.indexOf("{") + 1;
        int closeIndex = json.lastIndexOf("}");
        String unbracketed = json.substring(openIndex, closeIndex);
        openIndex = unbracketed.indexOf("{");
        return openIndex > -1 ? unbracketed.substring(openIndex) : unbracketed;
    }

    private Properties parseJsonWithGeneralMessage(String json) {
        return parseJson(removeGeneralMessageWrapper(json));
    }

    private Properties parseJson(String json) {
        Properties props = new Properties();
        Object o = JSONParser.defaultJSONParser().parse(json);
        if (o instanceof Map) {
            Map map = (Map) o;
            for (Object key : map.keySet()) {
                String keyStr = key.toString().trim();
                String valStr = map.get(key).toString();
                valStr = valStr.trim();
                props.put(keyStr, valStr);
            }
        }
        return props;
    }

    public Properties getProperties() {
        return this.properties;
    }
}


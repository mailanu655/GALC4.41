package com.honda.ahm.lc.live.map.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>AppController</code> is ... .
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 * @created Apr 1, 2021
 */

@Configuration
@Controller
@RequestMapping("")
public class AppController {

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    @ConfigurationProperties(prefix = "client")
    public Map<String, Object> getClientConfig() {
        return new HashMap<String, Object>();
    }

    // === config mapping === //
    @GetMapping(path = "config")
    public @ResponseBody Map<String, Object> getConfigMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.putAll(getClientConfig());
        return map;
    }

    @GetMapping(path = "info", produces = "text/javascript")
    public @ResponseBody String getConfigInfo() throws JsonProcessingException {
        String info = getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(getConfigMap());
        return info;
    }

    @GetMapping(path = "assets/js/config.js", produces = "text/javascript")
    public @ResponseBody String getConfigFile() throws JsonProcessingException {
        String str = getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(getConfigMap());
        str = "var config = " + str + ";\n";
        str = str + "(function(window) { \n   window.__config = config; \n}(this));";
        return str;
    }

    // === navigation mapping === //
    @GetMapping(path = "home")
    public String home(Model model) {
        return "forward:/index.html";
    }

    // === get/set === //
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}

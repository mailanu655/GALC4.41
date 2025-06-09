package com.honda.ahm.lc.rest;

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
        return getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(getConfigMap());
    }

    @GetMapping(path = "assets/js/config.js", produces = "text/javascript")
    public @ResponseBody String getConfigFile() throws JsonProcessingException {
        String str = getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(getConfigMap());
        str = "var config = " + str + ";\n";
        str = str + "(function(window) { \n   window.__env = config; \n}(this));";
        return str;
    }

    // === navigation mapping === //
    @GetMapping(path = "")
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

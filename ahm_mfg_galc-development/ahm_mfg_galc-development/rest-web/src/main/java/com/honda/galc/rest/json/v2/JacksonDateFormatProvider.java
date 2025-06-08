package com.honda.galc.rest.json.v2;

import javax.ws.rs.Produces;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;

@Provider
@Produces("application/json")
public class JacksonDateFormatProvider implements ContextResolver<ObjectMapper> {
	final ObjectMapper objectMapper;
	
	public JacksonDateFormatProvider() {
		objectMapper = new ObjectMapper();
		objectMapper.setDateFormat(new StdDateFormat()); // Solution 1: accepts multiple date formats
        //objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")); //Solution 2: accepts only one date format
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
	}

	@Override
    public ObjectMapper getContext(Class<?> type) {
		return objectMapper;
    }
}
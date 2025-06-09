package com.honda.mfg.stamp.storage.web;

import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.springframework.format.FormatterRegistry;

/**
 * Created by IntelliJ IDEA. User: Ambica Gawarla Date: 3/19/12 Time: 10:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationConversionServiceFactoryBeanTest {

	@Test
	public void successfullyTestApplicationConversionServiceFactoryBean() {
		ApplicationConversionServiceFactoryBean applicationConversionServiceFactoryBean = new ApplicationConversionServiceFactoryBean();
		applicationConversionServiceFactoryBean.setConverters(null);
		applicationConversionServiceFactoryBean.setEmbeddedValueResolver(null);
		FormatterRegistry formatterRegistry = mock(FormatterRegistry.class);
		// TODO need to verify with MvcConfig
		// applicationConversionServiceFactoryBean.installFormatters(formatterRegistry);
		applicationConversionServiceFactoryBean.installLabelConverters(formatterRegistry);
	}
}

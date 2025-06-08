package com.honda.galc.data;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.StaticApplicationContext;

public class ApplicationContextProvider {
	
	private static ApplicationContext ctx ;
	private static StaticApplicationContext staticContext = new StaticApplicationContext();
	
	public static ApplicationContext loadFromClassPathXml(String file) {
	    if(ctx == null)ctx = new ClassPathXmlApplicationContext(new String[] {file});
        return ctx;
	}
	
	public static ApplicationContext getApplicationContext() {
	    return ctx;
	}
	
	public static void setApplicationContext(ApplicationContext context) {
		if(context != null) ctx = context;
	}
	
	public static Object getBean(String beanName) {
		if(ctx == null) {
		    System.out.println("application context is null");
		}
		if(ctx != null && ctx.containsBean(beanName))return ctx.getBean(beanName);
		else return staticContext.getBean(beanName);
	}
	
	public static boolean containsBean(String beanName) {
	    return ctx.containsBean(beanName) || staticContext.containsBean(beanName);
	}
		
	@SuppressWarnings("unchecked")
    public static <T> T RegisterBean(String beanName,Class<T> beanClass){
	    staticContext.registerSingleton(beanName, beanClass);
	    return (T) staticContext.getBean(beanName);
	}
}

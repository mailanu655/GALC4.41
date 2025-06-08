package com.honda.galc.net;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>Request</code> is an object which contains the request <br>
 * information for client and server communication<br>
 * Request contains the method signature of a target object
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
 * <TD>Jeffray Huang</TD>
 * <TD>Feb 20, 2008</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Jeffray Huang
 */

/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class Request implements Serializable{
    
    private static final long serialVersionUID = -3111950279925678812L;
    
    public static final String CREATE_SESSION="createSession";
    public static final String DESTROY_SESSION="destorySession";
    
    
    private String targetClass;
    private String command;
    private Object[] params = null;
    private Class<?>[] types = null;
    // request issuer's source ip address
    private String ip ="UNKNOWN";
    // request issuer's receiving socket port number
    private int port;
    // request issuer's hostname
    private String hostName = "UNKNOWN";
    
    public Request(String command){
        this.command = command;
    }
    
    public Request(String target,String command,Object[] params) {
        this(command,params);
        this.targetClass = target;
    }
    
    public Request(String command,Object[] params){
        
        this(command);

        this.params= params;
    }
    
    public Request(String command,Object[] params, Class<?>[] types){
        
        this(command,params);
        
        this.types = types;
    }
    
    public String getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(String targetClass) {
        this.targetClass = targetClass;
    }

    public String getCommand(){
        return command;
    }
    
    public Object[] getParams(){
        return params;
    }
    
    public int getParamSize(){
    	if(params == null) return 0;
    	else return params.length;
    }
    
    public Class<?>[] getTypes(){
        
        if(types != null) return types;
        else return getObjectTypes();
    }
    
    
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
    
    public String getHostName(){
        return hostName;
    }
    
    public void setHostName(String name){
        
        if(name != null) hostName = name;
        
    }
    
    public boolean isCreateSessionMethod() {
    	return CREATE_SESSION.equals(command);
    }
    
    public boolean isDestroySessionMethod() {
    	return DESTROY_SESSION.equals(command);
    }
    
    /**
     * invoke the method based on the request details
     * @param obj - caller of the method
     * @return
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    
    public Object invoke(Object obj) throws SecurityException,NoSuchMethodException,
                                              IllegalArgumentException, IllegalAccessException, 
                                              InvocationTargetException{
        
        Method method = getMethod(obj,command, getTypes());
        return method.invoke(obj, params);
    }
    /**
     * get method based on the method signature
     * if the method with the exact method signatures does not exist
     * find the method its parameter's class is the super class of the current parameter object
     * throws NoSuchMethodException when no method exists
     * @param obj
     * @param command
     * @param types
     * @return
     * @throws NoSuchMethodException
     */
    
    private Method getMethod(Object obj,String command,Class<?>[] types)throws NoSuchMethodException{
        try{
            return obj.getClass().getMethod(command, types);
        }catch(NoSuchMethodException e){
            Method[] methods = obj.getClass().getMethods();
            for(Method method:methods){
                if(!method.getName().equals(command)) continue;
                Class<?>[] parameterTypes = method.getParameterTypes();
                if(parameterTypes.length !=types.length)continue;
                boolean usable = true;
                for (int i=0; i<parameterTypes.length; i++){
                    Class<?> parameterType = parameterTypes[i];
                    if(!isAssignable(types[i],parameterType)){
                        usable = false;
                        break;
                    }
                }
                if(usable) return method;
            }
            throw e;
        }
    }
    
    private boolean isAssignable(Class<?> objectType, Class<?> paramType) {
    	if(objectType == null || paramType.isAssignableFrom(objectType)) return true; 
    	return paramType.isPrimitive() ? autobox(paramType).isAssignableFrom(objectType) : false;
    }
    
    private Class<?>[] getObjectTypes(){
        Class<?> types[] = new Class[getParamSize()];
        for (int i = 0;i < getParamSize(); i++){
             types[i] = getObjectType(params[i]);
        };
        return types;
    }
    
    private Class<?> getObjectType(Object o){
        if(o == null) return null;
        return o.getClass();
    }
    
    /**
     * This method emulates server J5SE argument autoboxing.
     * @param arg The the argument class to test for boxing. If the argument
     * <i>type</i> is primitive, it will be substituted with the corresponding
     * primitive <i>class</i> representation.
     * @return The corresponding class matching the primitive type, or the
     * original argument class, if it is not primitive.
     */
    public static Class<?> autobox(Class<?> arg) {
       return arg.isPrimitive() ?
          arg == Boolean.TYPE   ? Boolean.class   :
          arg == Byte.TYPE      ? Byte.class      :
          arg == Character.TYPE ? Character.class :
          arg == Short.TYPE     ? Short.class     :
          arg == Integer.TYPE   ? Integer.class   :
          arg == Long.TYPE      ? Long.class      :
          arg == Float.TYPE     ? Float.class     : Double.class : arg;
    }
    
    public String toString(){
        
        StringBuilder buf = new StringBuilder(); 
        if(targetClass != null){
            buf.append(targetClass);
            buf.append(".");
        }
        buf.append(command + "(");
        
        for (int i = 0; i< getParamSize();i++){
    		buf.append(params[i] == null ? "null":params[i].toString());
    		if(i<params.length-1) buf.append(", ");
    	}
        buf.append(")");
        return buf.toString();
    }
}

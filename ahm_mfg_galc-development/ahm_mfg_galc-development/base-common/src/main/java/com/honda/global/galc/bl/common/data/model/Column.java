package com.honda.global.galc.bl.common.data.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;


public class Column implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private String name;
    private ColumnType type;
    private String method;
    private int size;
    private boolean isKey = false;
    private boolean isIdentityKey = false;
    private boolean isNullable = false;
    private boolean isInsertable = true;
    private boolean isUpdatable = true;
    
    public Column(String name,ColumnType type,String method,int size, boolean isKey,boolean isNullable){
        this(name,type,method,isKey);
        this.size = size;
        this.isNullable = isNullable;
        if(this.isKey == true)this.isNullable = false;
    }
    
    public Column(String name,ColumnType type,String method,int size, boolean isKey){
        this(name,type,method,isKey);
        this.size = size;
    }
    
    /**
     * constructor - size = 0, isKey = false, isNullable = true
     * @param name
     * @param type
     * @param method
     */
    
    public Column(String name,ColumnType type,String method){
        this(name,type,method,0,false);
    }
    
    /**
     * constructor - isKey = false, isNullable = true;
     * @param name
     * @param type
     * @param method
     * @param size
     */
    public Column(String name,ColumnType type,String method,int size){
        this(name,type,method,size,false);
    }
    
    public Column(String name,ColumnType type,String method,boolean isKey){
        this.name = name;
        this.type = type;
        this.method = method;
        this.isKey = isKey;
        if(isKey == true) this.isNullable = false;
    }
    
    public Column(String name,ColumnType type,String method,boolean isKey,boolean isIdentityKey){
        this(name,type,method,isKey);
        if(isKey) this.isIdentityKey = isIdentityKey;
    }
    
    public Column(String name,ColumnType type,String method,boolean isKey,boolean isIdentityKey,boolean isNullable){
        this(name,type,method,isKey);
        if(isKey) this.isIdentityKey = isIdentityKey;
        if(!isKey) this.isNullable = isNullable;
    }
 

    public boolean isKey() {
        return isKey;
    }

    public void setKey(boolean isKey) {
        this.isKey = isKey;
    }

    public boolean isNullable() {
        return isNullable;
    }




    public void setNullable(boolean isNullable) {
        this.isNullable = isNullable;
    }

    public boolean isInsertable() {
        return isInsertable;
    }

    public void setInsertable(boolean isInsertable) {
        this.isInsertable = isInsertable;
    }

    public boolean isUpdatable() {
        return isUpdatable;
    }

    public void setUpdatable(boolean isUpdatable) {
        this.isUpdatable = isUpdatable;
    }


    public String getMethod() {
        return method;
    }




    public void setMethod(String method) {
        this.method = method;
    }




    public String getName() {
        return name;
    }




    public void setName(String name) {
        this.name = name;
    }




    public int getSize() {
        return size;
    }




    public void setSize(int size) {
        this.size = size;
    }

    public boolean isIdentityKey() {
        return isIdentityKey;
    }

    public void setIdentityKey(boolean isIndentityKey) {
        this.isIdentityKey = isIndentityKey;
    }


    public ColumnType getType() {
        return type;
    }




    public void setType(ColumnType type) {
        this.type = type;
    }
    
    
    public static enum ColumnType{
        
        SmallInt(int.class,Integer.class,Types.SMALLINT,"Int"),
        BigInt(long.class,Long.class,Types.BIGINT,"Long"),
        Integer(int.class,Integer.class,Types.INTEGER,"Int"),
        Float(float.class,Float.class,Types.FLOAT,"Float"),
        Double(double.class,Double.class,Types.DOUBLE,"Double"),
        Char(String.class,Types.CHAR,"String"),
        String(String.class,Types.VARCHAR,"String"),
        Date(Date.class,Types.DATE,"Date"),
        Time(Time.class,Types.TIME,"Time"),
        Timestamp(Timestamp.class,Types.TIMESTAMP,"Timestamp"),
        Bytes(byte[].class,Types.BLOB,"Bytes"),
        Boolean(boolean.class,Boolean.class,Types.BOOLEAN, "Boolean");
        
        private Class primaryType;
        private Class secondaryType;
        private int columnType;
        private String methodName;
        
        ColumnType(Class type,int columnType,String methodName){
            this(type,type,columnType,methodName);
        }
        
        ColumnType(Class type1,Class type2,int columnType,String methodName){
            primaryType = type1;
            secondaryType = type2;
            this.columnType = columnType;
            this.methodName = methodName;
        }
        
        public Class getType(){
            return primaryType;
        }
        
        public Class getType(boolean isNullable){
            if(isNullable) return secondaryType;
            else return primaryType;
        }
        
        public String getMethodName(){
            
            return methodName;
        }
        
        
        public int getColumnType(){
            return columnType;
        }
        
        public String getMethodName(Object obj,boolean isNullable){
            if(obj == null && isNullable) return "String";
            else return methodName;
        }
        
        
        
        public static ColumnType convert(int num){
            if(num <0 || num >=values().length) return null;
            return values()[num];
       }
    }


 


 

}

package com.honda.galc.test.db;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.honda.galc.data.ApplicationContextProvider;
import com.honda.galc.entity.product.BlockLoad;


public class QueryHelper {
    
    public static QueryHelper queryHelper;
    
    
    private Properties galc_properties = new Properties();
    private Properties properties = new Properties();
    private final String GALC_PROPERTY_FILE_NAME = "/resource/com/honda/global/galc/system/database/SQLProperties.properties";
    private final String PROPERTY_FILE_NAME = "/sql/SQLProperties.properties";
    
    @PersistenceContext
	protected EntityManager entityManager;
	
    public static QueryHelper getInstance() {
        
        return (QueryHelper) ApplicationContextProvider.getBean("QueryHelper");
        
    }
    
    public QueryHelper(){
        
        // load sql statments from file
        loadSqlProperties();
    }
 
    private void loadSqlProperties() {
        
        InputStream inStream1 = this.getClass().getResourceAsStream(GALC_PROPERTY_FILE_NAME);
        InputStream inStream2 = this.getClass().getResourceAsStream(PROPERTY_FILE_NAME);
        try {
            galc_properties.load(inStream1);
            properties.load(inStream2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    private String getSqlStatement(String sqlId) {

        String sql = (String)galc_properties.get(sqlId);
        
        if(sql == null) return (String) properties.get(sqlId);
        else return sql;
        
    }
    
    private String prepareSqlStatement(String sqlId,Vector<Object> params){
        
        SimpleSQLMaker sqlMaker = new SimpleSQLMaker();
        
        try {
            sqlMaker.runMakeSql(getSqlStatement(sqlId), params);
        } catch (IllegalSQLRequestException e) {
            e.printStackTrace();
            
            return "";
        }
        
        return sqlMaker.getSqlStatement();
        
    }
    
    /**
     * query based on sqlId in SqlProperties file
     * @param sqlId
     * @return a list 
     */
    
    @SuppressWarnings("unchecked")
	public List query(String sqlId) {
        
        return entityManager.createNativeQuery(this.getSqlStatement(sqlId)).getResultList();
    }
    
    /**
     * query based on sqlId in SqlProperties file
     * @param sqlId
     * @return a single row 
     */
    
    public Object querySingleResult(String sqlId) {
        
        return entityManager.createNativeQuery(this.getSqlStatement(sqlId)).getSingleResult();
        
    }
    
    /**
     * query based on sqlId in SqlProperties file
     * @param sqlId
     * @Param params parameters for the SQL statement
     * @return a list 
     */
    
    public List<?> query(String sqlId,Vector<Object> params) {
        
        return entityManager.createNativeQuery(prepareSqlStatement(sqlId, params)).getResultList();
        
    }
    
    /**
     * query based on sqlId in SqlProperties file
     * @param sqlId
     * @Param params parameters for the SQL statement
     * @return a single row 
     */
    
    public Object querySingleResult(String sqlId,Vector<Object> params) {
        
        return entityManager.createNativeQuery(prepareSqlStatement(sqlId, params)).getSingleResult();
        
    }
    
    /**
     * query based on sqlId in SqlProperties file
     * @param sqlId
     * @Param className class name of the result object
     * @return a list of objects with className
     */

    public List<?> query(String sqlId, Class<?> className) {
        
        return entityManager.createNativeQuery(this.getSqlStatement(sqlId),className).getResultList();
    }
    
    /**
     * update based on sqlId in SqlProperties file
     * @param sqlId
     * @return
     */
    
    public int executeUpdate(String sqlId)
    {
    	return executeUpdate(entityManager.createNativeQuery(this.getSqlStatement(sqlId)));
    }
    
    /**
     * update based on sqlId in SqlProperties file
     * @param sqlId
     * @Param params parameters for the SQL statement
     * @return
     */
    
    public int executeUpdate(String sqlId,Vector<Object> params) {

    	return executeUpdate(entityManager.createNativeQuery(this.prepareSqlStatement(sqlId, params)));
    }

    public int executeUpdate(String sqlId, Class<?> className) {

    	return executeUpdate(entityManager.createNativeQuery(this.getSqlStatement(sqlId),className));
    }
    
    public int executeUpdate(Query query)
    {
    	int result = query.executeUpdate();
    	return result;
    	
    }
   
    public <E> E querySingleResult(String sqlId, Class<E> className) {
        
        Object obj= entityManager.createNativeQuery(this.getSqlStatement(sqlId),className).getSingleResult();
        try{
            return className.cast(obj);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    public List<?> query(String sqlId,String mapping) {
        
        return entityManager.createNativeQuery(this.getSqlStatement(sqlId),mapping).getResultList();
        
    }
    
    /**
     * get last loaded block at AE block load station
     * @return
     */
    
    public BlockLoad getLastLoadedBlock() {
        
        return querySingleResult("AEONSEL5105",BlockLoad.class);
        
    }
    
}

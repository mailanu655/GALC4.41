package com.honda.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;


/**
 * PropertyDao : Database access class to read properties and map them to 
 * Property objects.  
 *  
 * @author      Suriya Sena
 * Date         3/17/2016
 */


public class PropertyDao extends AbstractDao {

		private static final String queryByComponent = 
			"select component_id,property_key,property_value,description from galadm.gal489tbx " +
			"where component_id = ? " +
			"for read only ";
		
		
		
		public List<Property> getProperty(String component) {
			List<Property> results;
		    List<Object> args = new ArrayList<Object>();
		    args.add(component);
            
			results = jdbcTemplate.query(queryByComponent,args.toArray(),
			        new RowMapper<Property>() {
			            public Property mapRow(ResultSet rs, int rowNum) throws SQLException {
			            	
			            	Property property = new Property();
			            	property.setComponentId(rs.getString("component_id"));
			            	property.setKey(rs.getString("property_key"));
			            	property.setValue(rs.getString("property_value"));
			            	property.setDescription(rs.getString("description"));
			            	
			                return property;
			            }
			        });
			
			return results;
		}
		
}

	


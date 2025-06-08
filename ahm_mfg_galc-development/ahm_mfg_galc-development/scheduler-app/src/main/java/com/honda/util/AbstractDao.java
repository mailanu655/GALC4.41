package com.honda.util;
import org.springframework.jdbc.core.JdbcTemplate;


/**
 * AbstractDao : Not much to say about this   
 *  
 * @author      Suriya Sena
 * Date         3/17/2016
 */

public class AbstractDao {
	protected JdbcTemplate jdbcTemplate;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}	
}
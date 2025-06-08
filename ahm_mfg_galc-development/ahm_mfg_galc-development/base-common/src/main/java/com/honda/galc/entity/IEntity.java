package com.honda.galc.entity;

/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public interface IEntity {
	public Object getId();
	public String toString();
	public IEntity deepCopy();
}

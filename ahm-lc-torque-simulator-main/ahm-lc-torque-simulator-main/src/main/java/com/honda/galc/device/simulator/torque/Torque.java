package com.honda.galc.device.simulator.torque;

import java.util.ArrayList;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * @author Subu Kathiresan
 * @date June 29, 2009
 */
public class Torque {
	
	@XStreamAlias("id")
	@XStreamAsAttribute
	private int _id = 1;
	
	@XStreamAlias("force")
	@XStreamAsAttribute
	private int _force = 0;

	@XStreamAlias("partName")
	@XStreamAsAttribute
	private String _partName = "";
	
	@XStreamAlias("seq")
	@XStreamAsAttribute
	private int _sequence = 1;
	
	@XStreamImplicit(itemFieldName="Field")
	private ArrayList<OPMessageField> _fields = new ArrayList<OPMessageField>();

    /** Need to allow bean to be created via reflection */
    public Torque() {}
    
	/**
	 * @param fields the fields to set
	 */
	public void setFields(ArrayList<OPMessageField> fields) {
		_fields = fields;
	}

	/**
	 * @return the fields
	 */
	public ArrayList<OPMessageField> getFields() {
		return _fields;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		_id = id;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return _id;
	}
	
	public int getForce() {
		return _force;
	}

	public void setForce(int force) {
		_force = force;
	}

	/**
	 * @param seq the sequence to set
	 */
	public void setSequence(int seq) {
		_sequence = seq;
	}

	/**
	 * @return the sequence
	 */
	public int getSequence() {
		return _sequence;
	}

	/**
	 * @param partName the partName to set
	 */
	public void setPartName(String partName) {
		_partName = partName;
	}

	/**
	 * @return the partName
	 */
	public String getPartName() {
		return _partName;
	}
}

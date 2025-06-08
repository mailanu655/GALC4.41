package com.honda.galc.entity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.honda.galc.util.ToStringUtil;

/**
 * 
 * 
 * <h3>AbstractEntity Class description</h3>
 * <p> AbstractEntity description </p>
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
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Oct 1, 2014
 *
 *
 */
public abstract class AbstractEntity implements IEntity,Serializable{
	
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return ToStringUtil.generateToString(this, true);
	}

	protected String toString(Object... objects ) {

		String str = getClass().getSimpleName() + "(";
		boolean isFirst = true;
		for(Object item : objects) {
			if(!isFirst) str += ",";
			else isFirst = false;
			str += item == null ? "null" : item.toString();
		}
		str +=")";
		return str;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(this == obj) return true;
		if(!this.getClass().equals(obj.getClass())) return false;

		if(getId() == null) return false;

		return getId().equals(((AbstractEntity)obj).getId());
	} 

	public IEntity deepCopy() {
		ObjectOutputStream out=null;
		ObjectInputStream in=null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			out = new ObjectOutputStream(bos);
			out.writeObject(this);
			out.flush();
			in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
			return (IEntity) in.readObject();
		}
		catch(Exception e) {
			e.printStackTrace();
		}finally{
			if(out != null) {
				try {
					out.close();
				} catch(IOException e) {
					e.printStackTrace();
				}
				out = null;
			}
			if(in != null) {
				try {
					in.close();
				} catch(IOException e) {
					e.printStackTrace();
				}
				in = null;
			}
		}
		return null;
	}
}

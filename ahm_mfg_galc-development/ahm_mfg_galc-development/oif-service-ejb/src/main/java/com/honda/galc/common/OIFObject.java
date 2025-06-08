package com.honda.galc.common;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.oif.task.OifAbstractTask;

/**
 * Insert the type's description here.
 * Creation date: (1/22/02 1:15:47 PM)
 * @author: Administrator
 */
public class OIFObject
{
	protected Logger logger;

    private OifAbstractTask parentTask = null;

	public OIFObject()
	{
	}

	/**
	 * Insert the method's description here.
	 * Creation date: (1/22/02 3:14:37 PM)
	 */
	public OIFObject(OifAbstractTask aTask)
	{
		parentTask = aTask;
		logger = Logger.getLogger(aTask.getName());
	}
	
	/**
	 * Insert the method's description here.
	 * Creation date: (1/22/02 3:13:07 PM)
	 */
	public OifAbstractTask getParentHandler()
	{
	    return parentTask;
	}

}

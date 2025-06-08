/**
 * 
 */
package com.honda.galc.client.datacollection.control;

import com.honda.galc.client.datacollection.fsm.IDataCollectionFsm;
import com.honda.galc.client.datacollection.processor.IDataCollectionTaskProcessor;
import com.honda.galc.device.IDeviceData;

/**
 * @author Subu Kathiresan
 * @date Feb 21, 2012
 */
public class DataCollectionItem {

	private int _key;
	private Object _item;
	private IDataCollectionTaskProcessor<IDeviceData> _taskProcessor;
	private IDataCollectionFsm _fsm;
	
	public DataCollectionItem(int key, Object item) {
		_key = key;
		_item = item;
	}
	
	public DataCollectionItem(int key, Object item, IDataCollectionFsm fsm) {
		this(key, item);
		_fsm = fsm;
	}
	
	public DataCollectionItem(int key, Object item, IDataCollectionFsm fsm, IDataCollectionTaskProcessor<IDeviceData> taskProcessor) {
		this(key, item, fsm);
		_taskProcessor = taskProcessor;
	}

	public int getKey() {
		return _key;
	}
	
	public void setKey(int key) {
		_key = key;
	}
	
	public Object getItem() {
		return _item;
	}
	
	public void setItem(Object item) {
		_item = item;
	}
	
	public IDataCollectionFsm getFsm() {
		return _fsm;
	}
	
	public void setFsm(IDataCollectionFsm fsm) {
		_fsm = fsm;
	}

	
	public IDataCollectionTaskProcessor<IDeviceData> getTaskProcessor() {
		return _taskProcessor;
	}
	
	public void setTaskProcessor(IDataCollectionTaskProcessor<IDeviceData> taskProcessor) {
		_taskProcessor = taskProcessor;
	}
	
	
	@Override
	public int hashCode() {
		return _key;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataCollectionItem other = (DataCollectionItem) obj;
		if (_key != other._key)
			return false;
		return true;
	}
}

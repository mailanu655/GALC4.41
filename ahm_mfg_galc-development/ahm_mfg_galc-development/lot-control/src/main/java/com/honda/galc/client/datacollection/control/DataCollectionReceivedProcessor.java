/**
 * 
 */
package com.honda.galc.client.datacollection.control;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;

import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.datacollection.fsm.FSMContext;
import com.honda.galc.client.datacollection.fsm.IDataCollectionFsm;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.net.Request;

/**
 * @author Subu Kathiresan
 * @date Feb 21, 2012
 */
public class DataCollectionReceivedProcessor extends Thread {

	// if processing of a data collection received item takes
	// more than the wait period, its probably because of an error 
	// condition and such processes should be replaced by 
	// fire-and-forget processes
	private static final int INVOKE_AND_WAIT_PERIOD = 5000;	
	private volatile SynchronousQueue<DataCollectionItem> _sendQueue;
	private volatile BlockingQueue<DataCollectionItem> _receiveQueue;
	private volatile boolean _active = false;
	private volatile FSMContext _fsmContext = null;
	
	public DataCollectionReceivedProcessor(FSMContext fsmContext) {
		_fsmContext = fsmContext;
		_sendQueue = new SynchronousQueue<DataCollectionItem>(true);
		_receiveQueue = new LinkedBlockingQueue<DataCollectionItem>();
		_active = true;
        setDaemon(true);
	}
		
	public DataCollectionItem getProcessResult(DataCollectionItem item) {
        long endTime = System.currentTimeMillis() + INVOKE_AND_WAIT_PERIOD;
		while (System.currentTimeMillis() < endTime) {
			try {
				_sendQueue.remove(item);
				return item;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		_sendQueue.remove(item);
		Logger.getLogger().warn("waited too long to process " + item.getItem().toString());
		return null;
	}

	public Object processReceived(DataCollectionItem di) {
		_receiveQueue.add(di);
		DataCollectionItem result = getProcessResult(di);
		return (result != null)? result.getItem() : null;
	}

	public void run() {
		while (_active) {
			try {
				final DataCollectionItem  di = _receiveQueue.take();
				Runnable worker = new Runnable() {
					public void run() {
						synchronized (_fsmContext) {
							Object obj = di.getItem();
							Logger.getLogger().debug("processReceived " + obj.toString());
							if (obj instanceof IDeviceData) {
								di.setItem(di.getTaskProcessor().processReceived((IDeviceData) obj));
							} else if (obj instanceof Request) {
								di.setItem(processRequest((Request) obj, di.getFsm()));
							} else {
								Logger.getLogger().error("Received unsupported data: " + obj.getClass().getSimpleName());
								di.setItem(null);
							}
							_sendQueue.offer(di);
						}
					}
				};
				new Thread(worker).start();
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	private Object processRequest(Request request, IDataCollectionFsm fsm) {
		try {
			return request.invoke(fsm);
		} catch (Exception e) {
			fsm.message(new Message(this.getClass().getSimpleName(), e.getCause().toString()));
		}
		return null;
	}
}

package com.honda.galc.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 
 * <h3>QueueProcessor Class description</h3>
 * <p> QueueProcessor abstract class is a thread which allows the application 
 * to enqueue a specfic object with template T into a blocking queue. 
 * The thread dequeue each object and process the item using abstract method processItem.
 * Subclass of this is responsibe to implement processItem method </p>
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
 * Mar 4, 2010
 *
 */
public abstract class QueueProcessor<T> extends Thread{
    
    protected BlockingQueue<T> queue;
    
    protected boolean isRunning = true;
    
    public QueueProcessor() {
        this.setDaemon(true);
        initQueue();
    }
    
    public QueueProcessor(int capacity) {
    	this.setDaemon(true);
        initQueue(capacity);
    }
    
    protected void initQueue(){
        queue = new LinkedBlockingQueue<T>();
    }
    
    protected void initQueue( int capacity) {
    	if(capacity <=0) initQueue();
    	else queue = new LinkedBlockingQueue<T>(capacity);
    }
    
    public boolean enqueue(T item){
        return queue.offer(item);
    }
    
    public void run() {
        T item;
        
        while(isRunning) {
            try {
                item = (T) queue.take();
                processItem(item);

            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                isRunning = false;
            }
        }
    }
    
    public void stopRunning(){
        isRunning = false;
    }
    
    public abstract void processItem(T item);
}

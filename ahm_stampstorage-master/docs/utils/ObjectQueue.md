# ObjectQueue

## Purpose
The ObjectQueue class provides a generic queue implementation for storing and processing Object instances in the StampStorage system. It notifies registered observers when new objects are added to the queue, enabling asynchronous event-driven processing. This class serves as a foundation for message and event handling throughout the application.

## Logic/Functionality
- Extends Observable to support the observer pattern
- Implements Runnable to operate in its own thread
- Implements Observer to receive objects from other Observable sources
- Maintains a Vector-based storage for queued objects
- Provides methods for adding, retrieving, and peeking at objects in the queue
- Notifies registered observers when new objects are added
- Supports thread synchronization for concurrent access
- Allows for graceful shutdown of the processing thread

## Flow
1. The class is instantiated, creating a Vector for object storage
2. A dedicated thread is started to process objects in the queue
3. Objects are added to the queue using the postObject method
4. When an object is added, the processing thread is notified
5. The processing thread wakes up and calls processEvent
6. The processEvent method notifies all registered observers of each object in the queue
7. Observers process the objects according to their implementation
8. The queue continues to accept and process objects until continueProcessing is set to false

## Key Elements
- **Vector Storage**: Uses a Vector to store queued objects
- **Observer Pattern**: Extends Observable and notifies observers of new objects
- **Thread Management**: Runs in its own thread and handles synchronization
- **Queue Operations**: Methods for adding, retrieving, and examining objects
- **Graceful Shutdown**: Support for stopping the processing thread
- **Configurable Capacity**: Initial capacity and increment size can be configured

## Usage
The ObjectQueue is typically created and used as an event queue:

```java
// Example of creating an ObjectQueue
ObjectQueue queue = new ObjectQueue("EventQueue");

// Example of adding an observer
Observer eventHandler = new EventHandler();
queue.addListener(eventHandler);

// Example of posting an object to the queue
Event event = new Event("SomeEvent");
queue.postObject(event);
```

It can also be used as an Observer itself:

```java
// Example of using ObjectQueue as an Observer
Observable eventSource = new EventSource();
ObjectQueue queue = new ObjectQueue();
eventSource.addObserver(queue);
```

## Debugging and Production Support

### Common Issues
1. **Thread Synchronization**: Issues with concurrent access to the queue
2. **Resource Leaks**: Failure to properly shut down the queue thread
3. **Observer Management**: Problems with adding or removing observers
4. **Queue Capacity**: Vector capacity issues with large numbers of objects
5. **Notification Failures**: Observers not being properly notified of new objects

### Debugging Steps
1. **Check Thread State**: Verify that the queue thread is running and responsive
2. **Inspect Queue Contents**: Examine the contents of the queue to ensure objects are being added correctly
3. **Review Observer Registration**: Check that observers are properly registered with the queue
4. **Monitor Resource Usage**: Watch for memory leaks or excessive thread creation
5. **Analyze Notification Flow**: Trace the flow of notifications to ensure observers are being notified
6. **Examine Synchronization**: Look for potential synchronization issues or deadlocks

### Resolution
1. **Thread Management Improvements**: Enhance thread management to prevent leaks and ensure proper shutdown
2. **Synchronization Fixes**: Correct synchronization issues to prevent concurrent access problems
3. **Observer Handling Enhancements**: Improve observer registration and notification
4. **Capacity Optimization**: Tune Vector capacity settings for better performance
5. **Error Handling**: Add better error handling during object processing

### Monitoring
1. **Queue Size**: Track the number of objects in the queue
2. **Processing Rate**: Measure how quickly objects are processed
3. **Observer Count**: Monitor the number of registered observers
4. **Thread State**: Track the state of the queue thread
5. **Memory Usage**: Monitor memory usage related to queued objects
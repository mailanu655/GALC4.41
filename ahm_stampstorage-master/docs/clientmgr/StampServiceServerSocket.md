# StampServiceServerSocket

## Purpose
The StampServiceServerSocket class provides a server socket implementation that accepts and manages client connections for the StampStorage system. It handles the creation and management of socket connections, delegates client communication to individual socket connection handlers, and supports active/passive role switching for high availability.

## Logic/Functionality
- Creates and manages a server socket that listens for client connections
- Accepts incoming client connections and creates StampServiceSocketConnection instances to handle them
- Supports configuration through properties files for port, timeouts, and connection limits
- Implements ServiceRoleObserver to respond to service role changes (active/passive)
- Manages socket lifecycle including initialization, connection acceptance, and graceful shutdown
- Uses a CountDownLatch mechanism to control when the server starts accepting connections
- Maintains a map of active client connections for management purposes

## Flow
1. The server socket is initialized with configuration from a properties file
2. It waits at a CountDownLatch gate until the service is set to active mode
3. Once active, it begins accepting client connections on the configured port
4. For each accepted connection, it creates a StampServiceSocketConnection instance
5. The connection is configured with a CommandProcessor as its message sink
6. The connection is started in its own thread to handle client communication
7. The server continues accepting connections until stopped or switched to passive mode
8. When switched to passive mode, it closes existing connections and waits to be reactivated

## Key Elements
- **Server Socket Management**: Creates and manages the server socket for accepting connections
- **Connection Handling**: Creates and manages StampServiceSocketConnection instances for client communication
- **Role-Based Operation**: Supports active/passive role switching for high availability
- **Configuration**: Loads and applies configuration from properties files
- **Error Handling**: Comprehensive error handling for socket exceptions and timeouts
- **Connection Tracking**: Maintains a map of active connections for management

## Usage
The StampServiceServerSocket is typically instantiated and started by the main application:

```java
// Example of creating and starting the server socket
String configFileName = "stamp-service-socket.properties";
StampServiceServerSocket serverSocket = new StampServiceServerSocket(configFileName);
serverSocket.init();
serverSocket.setCarrierManager(carrierManager);
serverSocket.start();
```

It can also be run directly for testing purposes:

```java
// Example of running the server socket directly
String configFileName = args[0];
StampServiceServerSocket serverSo = new StampServiceServerSocket(configFileName);
serverSo.init();
serverSo.enableMaxTimeOuts(true);
ExecutorService threadService = Executors.newSingleThreadExecutor();
threadService.execute(serverSo);
```

## Debugging and Production Support

### Common Issues
1. **Port Binding Failures**: The server socket may fail to bind to the configured port if it's already in use
2. **Connection Limits**: If the maximum number of connections is reached, new connections will be rejected
3. **Timeout Handling**: Improper timeout settings can cause connections to be terminated prematurely
4. **Role Switching Issues**: Problems with role switching can cause the server to remain in an incorrect state
5. **Resource Leaks**: Failure to properly close sockets can lead to resource leaks

### Debugging Steps
1. **Check Port Availability**: Verify that the configured port is available and not in use by another process
2. **Review Connection Limits**: Ensure the maximum connection limit is appropriate for the expected load
3. **Examine Timeout Settings**: Check that timeout settings are appropriate for the network environment
4. **Monitor Role Switching**: Verify that role switching is functioning correctly by checking logs
5. **Inspect Resource Usage**: Monitor system resources to detect potential leaks
6. **Review Error Logs**: Check logs for exceptions or errors related to socket operations

### Resolution
1. **Port Configuration**: Change the port configuration if conflicts are detected
2. **Connection Limit Adjustments**: Increase the maximum connection limit if legitimate connections are being rejected
3. **Timeout Tuning**: Adjust timeout settings based on network performance and reliability
4. **Role Switching Fixes**: Correct issues with role switching by ensuring proper observer notification
5. **Resource Cleanup**: Ensure all resources are properly closed when no longer needed
6. **Error Handling Improvements**: Enhance error handling to better recover from exceptional conditions

### Monitoring
1. **Connection Statistics**: Track the number of active connections and connection attempts
2. **Timeout Occurrences**: Monitor how often connections time out
3. **Role Switch Events**: Log and track role switching events
4. **Resource Usage**: Monitor memory and file descriptor usage
5. **Error Rates**: Track the frequency of socket-related errors
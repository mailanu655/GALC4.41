# ServiceConnectionManager Technical Documentation

## Purpose
The `ServiceConnectionManager` class is responsible for managing connections to external services in the StampStorage system. It handles service role discovery, connection state management, and provides information about the current connection status. This class is crucial for maintaining reliable communication with external systems such as MES (Manufacturing Execution System) or PLC (Programmable Logic Controller) devices.

## Logic/Functionality
The service connection manager provides several key functionalities:

1. **Service Role Management**: Discovers and manages service roles for external systems
2. **Connection State Management**: Tracks and updates the connection state to external systems
3. **Connection Event Publishing**: Publishes events related to connection state changes
4. **Connection Retry Logic**: Implements retry mechanisms for failed connections
5. **Service Role Selection**: Selects appropriate service roles based on configuration

## Flow
The `ServiceConnectionManager` operates in the following workflow:

1. The class is instantiated by Spring and initialized with configuration properties
2. The manager discovers available service roles from the configuration
3. The manager attempts to establish connections to external systems
4. The manager monitors connection status and publishes connection events
5. Other components subscribe to connection events to react to connection state changes
6. The manager provides connection status information to other components

## Key Elements
- **ServiceRole**: Represents a role for an external service, including IP address and port
- **updateServiceRoles**: Updates the available service roles from the configuration
- **getServiceRole**: Retrieves the current service role
- **isConnected/isDisconnected**: Checks the current connection status
- **setConnected/setDisconnected**: Updates the connection status
- **publishConnectionEvent**: Publishes events related to connection state changes
- **retryConnection**: Implements retry logic for failed connections

## Usage
The `ServiceConnectionManager` is used in the following scenarios:

1. **Connection Status Checking**: When checking if external systems are connected
   ```java
   @Autowired
   private ServiceConnectionManager serviceConnectionManager;
   
   public boolean isExternalSystemConnected() {
       return serviceConnectionManager.isConnected();
   }
   ```

2. **Service Role Retrieval**: When retrieving information about external service endpoints
   ```java
   ServiceRole role = serviceConnectionManager.getServiceRole();
   String ipAddress = role.getIp();
   int port = role.getPort();
   ```

3. **Connection Event Handling**: When subscribing to connection events
   ```java
   @EventListener
   public void handleConnectionEvent(ConnectionEvent event) {
       if (event.isConnected()) {
           // Handle connected state
       } else {
           // Handle disconnected state
       }
   }
   ```

4. **Service Role Update**: When updating service roles
   ```java
   serviceConnectionManager.updateServiceRoles();
   ```

## Database Tables
The `ServiceConnectionManager` does not directly interact with database tables. However, it may indirectly affect the following tables through connection state changes:

1. **ALARM_EVENT**: Connection state changes may trigger alarm events
2. **AUDIT_ERROR_LOG**: Connection errors may be logged in the audit error log

## Debugging and Production Support

### Common Issues
1. **Connection Failures**: Failures to connect to external systems
2. **Service Role Discovery Failures**: Failures to discover available service roles
3. **Connection State Inconsistencies**: Connection state not properly updated or reported
4. **Connection Event Publication Failures**: Failures to publish connection events
5. **Connection Retry Failures**: Failures in the connection retry mechanism
6. **Service Role Selection Errors**: Errors in selecting appropriate service roles
7. **Configuration Issues**: Incorrect configuration for service roles

### Debugging Steps
1. **Connection Failures**:
   - Check the external system availability
   - Verify the IP address and port configuration
   - Check for network connectivity issues
   - Review logs for connection attempt errors
   - Test with known working IP addresses and ports

2. **Service Role Discovery Failures**:
   - Check the configuration for service roles
   - Verify the service role discovery mechanism
   - Check for configuration file access issues
   - Review logs for service role discovery errors
   - Test with known working configurations

3. **Connection State Inconsistencies**:
   - Check the implementation of connection state management
   - Verify the connection state update mechanism
   - Check for race conditions in connection state updates
   - Review logs for connection state changes
   - Test connection state updates with controlled scenarios

4. **Connection Event Publication Failures**:
   - Check the event publication mechanism
   - Verify the event subscriber configuration
   - Check for event handling issues
   - Review logs for event publication errors
   - Test event publication with controlled scenarios

5. **Connection Retry Failures**:
   - Check the implementation of the retry mechanism
   - Verify the retry configuration
   - Check for issues with retry timing
   - Review logs for retry attempt errors
   - Test retry mechanism with controlled failures

### Resolution
1. **Connection Failures**:
   - Fix connection logic to handle network issues
   - Update IP address and port configuration
   - Implement connection timeout handling
   - Enhance logging for connection attempts
   - Add fallback connection mechanisms

2. **Service Role Discovery Failures**:
   - Fix service role discovery logic
   - Update configuration for service roles
   - Implement configuration validation
   - Enhance logging for service role discovery
   - Add default service roles for error cases

3. **Connection State Inconsistencies**:
   - Fix connection state management logic
   - Implement thread-safe connection state updates
   - Add connection state validation
   - Enhance logging for connection state changes
   - Implement connection state recovery mechanisms

4. **Connection Event Publication Failures**:
   - Fix event publication mechanism
   - Update event subscriber configuration
   - Implement event handling error recovery
   - Enhance logging for event publication
   - Add event publication retry mechanisms

5. **Connection Retry Failures**:
   - Fix retry mechanism logic
   - Update retry configuration
   - Implement adaptive retry timing
   - Enhance logging for retry attempts
   - Add retry attempt limit and fallback mechanisms

6. **Service Role Selection Errors**:
   - Fix service role selection logic
   - Implement service role validation
   - Add fallback service role selection
   - Enhance logging for service role selection
   - Implement service role prioritization

7. **Configuration Issues**:
   - Fix configuration loading logic
   - Implement configuration validation
   - Add default configurations for error cases
   - Enhance logging for configuration loading
   - Implement configuration reload mechanisms

### Monitoring
1. **Connection Status**: Monitor the connection status to external systems
   - Log connection status changes with timestamps
   - Track connection uptime and downtime
   - Alert on prolonged disconnections
   - Monitor connection attempt success rates

2. **Service Role Changes**: Monitor changes to service roles
   - Log service role updates with details
   - Track service role selection changes
   - Alert on frequent service role changes
   - Monitor service role availability

3. **Connection Events**: Monitor connection event publication
   - Log connection events with details
   - Track event publication success rates
   - Alert on event publication failures
   - Monitor event subscriber responses

4. **Connection Retries**: Monitor connection retry attempts
   - Log retry attempts with details
   - Track retry success rates
   - Alert on excessive retry attempts
   - Monitor retry timing and backoff

5. **Configuration Changes**: Monitor configuration changes
   - Log configuration updates with details
   - Track configuration validation results
   - Alert on configuration validation failures
   - Monitor configuration reload attempts

6. **External System Availability**: Monitor the availability of external systems
   - Log external system status checks
   - Track external system response times
   - Alert on external system unavailability
   - Monitor external system error rates

7. **Network Connectivity**: Monitor network connectivity to external systems
   - Log network connectivity checks
   - Track network latency and packet loss
   - Alert on network connectivity issues
   - Monitor network performance metrics

Implement logging and metrics collection for these aspects to enable proactive monitoring and issue detection.
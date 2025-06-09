# storage.properties Technical Documentation

## Purpose
The storage.properties file defines critical configuration parameters for the StampStorage application's service connectivity, authentication, and operational behavior. It serves as a central repository for service device connection information, security credentials, and application-specific settings that control how the application interacts with external systems and authenticates users.

## Logic/Configuration
The file contains key-value pairs that define:

1. **Service Device Configuration**: IP address, port, and watchdog interval for the service device
2. **Operational Parameters**: Settings that control application behavior, such as pause time
3. **Authentication Configuration**: LDAP connection parameters for user authentication
4. **Role Configuration**: Role prefix for application-specific authorization

The file uses property placeholders (${filter.xxx}) for service device settings, allowing environment-specific values to be injected during the build process. This enables deployment to different environments without code changes.

## Flow
1. During application startup, Spring loads this properties file
2. The property values are used to configure various application components:
   - Service device connection parameters configure the connection to the service device
   - Pause time controls throttling of service device communication
   - Authentication parameters configure LDAP-based user authentication
   - Role prefix defines the application-specific role structure

## Key Elements
- **Service Device IP** (line 2): The IP address of the service device
- **Service Device Port** (line 3): The port number for service device communication
- **Watchdog Interval** (line 4): The interval for connection health checks
- **Pause Time** (line 7): Time to pause between service device communications
- **LDAP Provider URL** (line 10): The URL for LDAP authentication
- **Principal Security** (line 11): The LDAP principal for authentication
- **Security Credentials** (line 12): The password for LDAP authentication
- **Role Prefix** (line 13): The prefix for application-specific roles

## Usage
This file is used:
- During application startup to configure service connectivity
- When authenticating users through LDAP
- When authorizing users based on roles
- When controlling communication throttling with the service device
- When deploying to different environments (development, testing, production)

## Debugging and Production Support

### Common Issues
1. **Service Connection Failures**: Unable to connect to the service device
2. **Authentication Problems**: LDAP authentication failures
3. **Authorization Issues**: Incorrect role configuration
4. **Communication Throttling**: Inappropriate pause time causing performance issues
5. **Property Resolution Failures**: Filter properties not being resolved correctly

### Debugging Steps
1. **Service Connection Failures**:
   - Verify service device IP and port are correct
   - Check network connectivity between application and service device
   - Review application logs for connection error messages
   - Test connectivity using network tools (ping, telnet)
   - Verify watchdog interval is appropriate for the network environment

2. **Authentication Problems**:
   - Verify LDAP provider URL is correct and accessible
   - Check principal security and credentials
   - Test LDAP connectivity using external tools
   - Review application logs for authentication failures
   - Verify LDAP server configuration and availability

3. **Authorization Issues**:
   - Check role prefix configuration
   - Verify user roles in LDAP directory
   - Review application logs for authorization failures
   - Test with users having different roles
   - Verify role mapping in application security configuration

4. **Communication Throttling**:
   - Review pause time setting for appropriateness
   - Monitor communication patterns with service device
   - Check for timeout or performance issues related to throttling
   - Test with different pause time values
   - Review application logs for communication-related issues

5. **Property Resolution Failures**:
   - Check filter files for the correct property definitions
   - Verify the build process is correctly applying filters
   - Look for unresolved ${filter.xxx} values in the deployed properties file
   - Review application logs for property resolution errors
   - Test with hardcoded values to isolate filter issues

### Resolution
1. **Service Connection Failures**:
   - Update service device IP and port with correct values
   - Resolve network connectivity issues
   - Adjust watchdog interval based on network conditions
   - Implement connection retry logic
   - Add better error handling for connection failures

2. **Authentication Problems**:
   - Update LDAP provider URL, principal, and credentials
   - Verify LDAP server configuration
   - Implement authentication fallback mechanisms
   - Add detailed logging for authentication process
   - Consider alternative authentication methods if LDAP is unstable

3. **Authorization Issues**:
   - Correct role prefix configuration
   - Update role mapping in application
   - Ensure users have appropriate roles in LDAP
   - Implement role caching for performance
   - Add role debugging capabilities

4. **Communication Throttling**:
   - Adjust pause time based on system performance
   - Implement adaptive throttling based on load
   - Monitor and log communication patterns
   - Balance throughput with system stability
   - Consider asynchronous communication patterns

5. **Property Resolution Failures**:
   - Update filter files with correct property values
   - Fix the build process to properly apply filters
   - Add validation for critical properties during startup
   - Implement default values for missing properties
   - Add detailed logging for property resolution

### Monitoring
1. **Service Connectivity**: Monitor connection status and failures
2. **Authentication Activity**: Track authentication successes and failures
3. **Authorization Checks**: Monitor role-based access control decisions
4. **Communication Performance**: Track service device communication timing
5. **Property Values**: Validate property resolution during startup
6. **Security Events**: Monitor for suspicious authentication attempts
7. **Resource Usage**: Track connection and thread usage related to service communication

## Additional Notes
The file contains sensitive security credentials (line 12) in plain text, which is a potential security risk. In a production environment, these credentials should be encrypted or stored in a secure credential store.

The LDAP configuration (lines 10-13) indicates the application uses Active Directory for authentication, as evidenced by the DC=mfg,DC=am,DC=mds,DC=honda,DC=com domain components in the provider URL.

The ohcvRolePrefix setting (line 13) suggests the application uses a role-based access control system with roles prefixed with "APP-OHCV". This is important for understanding how authorization works in the application.

The pauseTimeSec setting (line 7) controls the throttling of service device communication. This is a critical performance tuning parameter that balances system responsiveness with the risk of overwhelming the service device with too many requests.

The watchdogIntervalSec setting (line 4) determines how frequently the application checks the health of the connection to the service device. This is important for detecting and recovering from connection failures in a timely manner.
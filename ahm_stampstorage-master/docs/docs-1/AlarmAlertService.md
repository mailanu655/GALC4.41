# AlarmAlertService Technical Documentation

## Purpose
The `AlarmAlertService` interface defines a service responsible for monitoring and notifying contacts about alarm events in the StampStorage system. It serves as a critical component in the system's alerting mechanism, ensuring that appropriate personnel are notified when alarm conditions occur.

## Logic/Functionality
- Provides a `run()` method that executes the alarm notification process
- Allows setting a `ServiceRoleWrapper` to determine if the service should operate in active or passive mode
- When implemented, it checks for alarms that need notification and sends alerts to configured contacts

## Flow
1. The service is periodically executed (likely by a scheduler)
2. It checks if it's in passive mode (via ServiceRoleWrapper) - if so, it doesn't process alarms
3. When active, it retrieves alarms that need notification
4. For each alarm, it notifies configured contacts via email or pager
5. It also handles PLC notifications for alarms that require it

## Key Elements
- `run()`: The main method that executes the alarm notification process
- `setServiceRoleWrapper()`: Method to set the service role (active/passive) wrapper

## Usage
This service is used in production environments to ensure that system operators and maintenance personnel are promptly notified of alarm conditions. It's typically:
- Configured in the Spring application context
- Scheduled to run at regular intervals
- Connected to email/paging systems for notifications

## Debugging and Production Support

### Common Issues
1. Alarm notifications not being sent
2. Duplicate alarm notifications
3. Notifications being sent in passive mode
4. Missing notifications for specific alarm types
5. Delays in notification delivery

### Debugging Steps
1. Check logs for "AlarmAlertServiceImpl#run()" entries to verify the service is running
2. Verify if the service is in passive mode (look for "Passive mode...not running" log entries)
3. Check the database for alarms with notification status
4. Verify email server configuration and connectivity
5. Check if contacts are properly configured for each alarm type

### Resolution
- For notification failures: Verify email server settings and connectivity
- For duplicate notifications: Check if multiple instances of the service are running
- For passive mode issues: Verify the ServiceRoleWrapper configuration
- For missing notifications: Ensure alarm definitions have proper contact associations
- For notification delays: Check system load and email server performance

### Monitoring
- Monitor log files for exceptions in the AlarmAlertServiceImpl
- Set up alerts for email server connectivity issues
- Track alarm notification success rates
- Monitor the number of unacknowledged alarms in the system
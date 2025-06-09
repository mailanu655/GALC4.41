# AlarmServiceHelper Technical Documentation

## Purpose
The `AlarmServiceHelper` interface extends the `Helper` interface and defines a set of methods for managing alarm-related operations in the StampStorage system. It serves as a critical component for alarm retrieval, notification, and interaction with PLCs regarding alarm conditions.

## Logic/Functionality
- Provides methods to retrieve different types of alarms (expired, requiring notification, etc.)
- Defines functionality for alerting contacts about alarm events
- Offers methods for PLC notification about alarms
- Enables filtering alarms by type

## Flow
The AlarmServiceHelper is used by various services in the system to:
1. Retrieve alarms that need attention (expired, requiring notification)
2. Send notifications to appropriate contacts based on alarm configuration
3. Notify PLCs about alarm conditions
4. Filter alarms by type for specific processing needs

## Key Elements
- `getExpiredAlarms()`: Retrieves alarms that have exceeded their configured lifetime
- `getAlarmsToNotify()`: Retrieves alarms that need notification to contacts
- `alertContacts()`: Sends notifications to configured contacts for a specific alarm
- `getUnclearedAlarmsByType()`: Filters alarms by type for specific processing
- `getAlarmsToNotifyPlc()`: Retrieves alarms that need PLC notification
- `alertPlc()`: Sends notifications to PLCs about alarm conditions

## Usage
This helper interface is used throughout the StampStorage system to:
- Support alarm archiving services in identifying expired alarms
- Enable alarm notification services to send alerts to contacts
- Facilitate PLC communication regarding alarm conditions
- Allow filtering of alarms for specific processing needs
- It's typically injected into services that need alarm-related functionality

## Debugging and Production Support

### Common Issues
1. Missing or incorrect alarm retrieval
2. Notification failures to contacts
3. PLC communication issues for alarms
4. Performance problems with large numbers of alarms
5. Incorrect filtering of alarms by type

### Debugging Steps
1. Verify database queries for alarm retrieval are returning expected results
2. Check contact configuration for alarms requiring notification
3. Verify PLC connectivity and message format for PLC notifications
4. Monitor query performance for alarm retrieval methods
5. Validate alarm type definitions and filtering logic

### Resolution
- For retrieval issues: Check database queries and alarm status definitions
- For notification failures: Verify contact information and notification service configuration
- For PLC issues: Check connectivity, message format, and PLC configuration
- For performance problems: Optimize queries and consider indexing strategies
- For filtering issues: Validate alarm type definitions and filtering logic

### Monitoring
- Track the number of alarms in each category (expired, requiring notification, etc.)
- Monitor notification success rates
- Track PLC communication success for alarm notifications
- Set up alerts for unusual patterns in alarm activity
- Monitor query performance for alarm retrieval methods
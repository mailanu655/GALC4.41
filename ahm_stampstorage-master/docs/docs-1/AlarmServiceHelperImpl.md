# AlarmServiceHelperImpl Technical Documentation

## Purpose
The `AlarmServiceHelperImpl` class implements the `AlarmServiceHelper` interface and provides concrete implementations for alarm-related operations in the StampStorage system. It handles alarm retrieval, contact notification, and PLC communication for alarm conditions.

## Logic/Functionality
- Extends `AbstractHelperImpl` to inherit common helper functionality
- Implements methods to retrieve different types of alarms (expired, requiring notification, etc.)
- Provides logic for alerting contacts about alarm events via email and pager
- Implements PLC notification for alarms
- Handles alarm filtering by type

## Flow
1. For expired alarms:
   - Retrieves current uncleared alarms
   - Checks each alarm's auto-archive time setting
   - Determines if the alarm has exceeded its configured lifetime
   - Returns a list of expired alarms

2. For contact notification:
   - Retrieves alarms that need notification
   - For each alarm, finds associated contacts
   - Sends email or pager notifications based on contact type
   - Updates the alarm's notification status

3. For PLC notification:
   - Retrieves alarms that need PLC notification
   - Creates PLC alarm messages
   - Publishes messages to the event bus for delivery to PLCs

## Key Elements
- `alertingService`: Service used to send email and pager notifications
- `getExpiredAlarms()`: Implements logic to identify expired alarms
- `getAlarmsToNotify()`: Retrieves alarms requiring contact notification
- `alertContacts()`: Sends notifications to contacts and updates alarm status
- `getAlarmsToNotifyPlc()`: Retrieves alarms requiring PLC notification
- `alertPlc()`: Creates and publishes PLC alarm messages
- Event bus integration for PLC communication

## Usage
This implementation is used throughout the StampStorage system to:
- Support alarm archiving by identifying expired alarms
- Enable contact notification for alarm conditions
- Facilitate PLC communication about alarms
- Filter alarms by type for specific processing
- It's typically injected into services that need alarm-related functionality

## Debugging and Production Support

### Common Issues
1. Incorrect identification of expired alarms
2. Contact notification failures
3. PLC communication issues
4. Performance problems with large alarm volumes
5. Database query errors during alarm retrieval

### Debugging Steps
1. For expired alarm issues:
   - Check auto-archive time settings in alarm definitions
   - Verify timestamp comparison logic
   - Validate database queries for alarm retrieval

2. For notification issues:
   - Check contact configuration in the database
   - Verify email/pager address formats
   - Check alerting service configuration
   - Look for exceptions during notification attempts

3. For PLC issues:
   - Verify event bus configuration
   - Check PLC message format
   - Monitor event publication success

### Resolution
- For expired alarm issues: Review auto-archive settings and time calculation logic
- For notification failures: Verify contact information and alerting service configuration
- For PLC issues: Check event bus configuration and message format
- For performance problems: Optimize queries and consider indexing strategies
- For database errors: Check connectivity and schema integrity

### Monitoring
- Track the number of alarms in each category (expired, requiring notification, etc.)
- Monitor notification success rates
- Track PLC message publication success
- Set up alerts for exceptions during alarm processing
- Monitor query performance for alarm retrieval methods
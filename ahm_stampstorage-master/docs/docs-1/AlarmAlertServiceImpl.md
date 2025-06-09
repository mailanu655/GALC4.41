# AlarmAlertServiceImpl Technical Documentation

## Purpose
The `AlarmAlertServiceImpl` class implements the `AlarmAlertService` interface and serves as the concrete implementation for monitoring and notifying contacts about alarm events in the StampStorage system. It handles both human notifications (email/pager) and PLC notifications for system alarms.

## Logic/Functionality
- Implements the `run()` method to execute the alarm notification process
- Uses `AlarmServiceHelper` to retrieve alarms that need notification
- Sends notifications to configured contacts for each alarm
- Handles PLC notifications for alarms that require it
- Respects active/passive mode configuration via ServiceRoleWrapper

## Flow
1. When `run()` is called, it first checks if the service is in passive mode
2. If active, it retrieves alarms that need notification using `alarmServiceHelper.getAlarmsToNotify()`
3. For each alarm, it calls `alarmServiceHelper.alertContacts()` to send notifications
4. It then retrieves alarms that need PLC notification using `alarmServiceHelper.getAlarmsToNotifyPlc()`
5. For each PLC alarm, it calls `alarmServiceHelper.alertPlc()` to send notifications to PLCs

## Key Elements
- `alarmServiceHelper`: Helper class that provides methods for alarm retrieval and notification
- `serviceRoleWrapper`: Determines if the service is in active or passive mode
- `run()`: Main method that executes the alarm notification process
- Exception handling to ensure service stability

## Usage
This implementation is used in production environments to:
- Periodically check for alarms that need notification
- Send email and pager alerts to configured contacts
- Notify PLCs about alarm conditions
- It's typically scheduled to run at regular intervals via a scheduler

## Debugging and Production Support

### Common Issues
1. Exceptions during alarm notification process
2. Service running in passive mode when it should be active
3. Missing notifications for specific alarms
4. PLC notifications not being sent
5. Performance issues with large numbers of alarms

### Debugging Steps
1. Check logs for "AlarmAlertServiceImpl#run()" entries to verify execution
2. Look for "Passive mode...not running" to determine if service is in passive mode
3. Check for exceptions in the logs during the notification process
4. Verify that `alarmServiceHelper.getAlarmsToNotify()` is returning expected alarms
5. Check PLC connectivity if PLC notifications are failing

### Resolution
- For exceptions: Analyze the stack trace and fix underlying issues
- For passive mode issues: Check ServiceRoleWrapper configuration
- For missing notifications: Verify alarm configurations and contact associations
- For PLC notification issues: Check PLC connectivity and message format
- For performance issues: Consider batching notifications or optimizing database queries

### Monitoring
- Monitor log files for exceptions in the AlarmAlertServiceImpl
- Track the number of alarms being processed in each run
- Monitor notification success rates
- Set up alerts for repeated exceptions
- Track PLC notification success rates
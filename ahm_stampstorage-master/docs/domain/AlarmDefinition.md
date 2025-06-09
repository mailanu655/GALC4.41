# AlarmDefinition Technical Documentation

## Purpose
AlarmDefinition.java defines the core properties and behaviors of alarms in the stamp storage system. It represents the template for alarm events, containing information about alarm severity, notification requirements, and descriptions.

## Logic/Functionality
- Implements a JPA entity mapped to the ALARMS_DEFINITION_TBX table
- Stores metadata about alarms including severity, notification category, and auto-archive settings
- Provides methods to retrieve notification contacts through the AlarmContact relationship
- Includes finder methods to locate alarms by number, location, and other criteria
- Supports filtering of alarms by active status

## Flow
1. AlarmDefinition entities are created during system configuration to define possible alarm conditions
2. When an alarm condition is detected, the system references the corresponding AlarmDefinition
3. The AlarmDefinition provides information on how to handle the alarm (severity, notification, etc.)
4. AlarmEvent entities are created based on AlarmDefinition templates when actual alarms occur

## Key Elements
- `alarmNumber`: Unique identifier for the alarm type
- `location`: Integer representing the physical location where the alarm can occur
- `name` and `description`: Human-readable information about the alarm
- `notificationCategory`: Categorization of the alarm for notification purposes
- `severity`: Enumerated value indicating the alarm's importance
- `autoArchiveTimeInMinutes`: Time after which the alarm can be automatically archived
- `notificationRequired` and `qpcNotificationRequired`: Flags indicating notification requirements
- `active`: Flag indicating whether the alarm definition is currently in use

## Usage
```java
// Find an alarm definition by number and location
AlarmDefinition alarm = AlarmDefinition.findAlarmByAlarmNumberAndLocation(123, 456);

// Check if notification is required
if (alarm.getNotificationRequired()) {
    // Get contacts for notification
    String notification = alarm.getNotification();
    // Send notification
}

// Create a new alarm definition
AlarmDefinition newAlarm = new AlarmDefinition();
newAlarm.setAlarmNumber(789);
newAlarm.setLocation(101);
newAlarm.setName("New Alarm");
newAlarm.setDescription("Description of new alarm condition");
newAlarm.setSeverity(SEVERITY.THREE);
newAlarm.setNotificationRequired(true);
newAlarm.setActive(true);
newAlarm.persist();
```

## Debugging and Production Support

### Common Issues
1. Missing alarm definitions for critical system conditions
2. Incorrect severity levels causing inappropriate response to alarms
3. Notification configuration issues (missing contacts or incorrect notification settings)
4. Performance issues when querying large numbers of alarm definitions
5. Inconsistent alarm numbering across locations

### Debugging Steps
1. Verify alarm definition exists:
   ```java
   AlarmDefinition.findAlarmByAlarmNumberAndLocation(alarmNumber, location)
   ```
2. Check notification configuration:
   ```java
   alarm.getNotificationRequired() && !alarm.getNotification().isEmpty()
   ```
3. Examine active status:
   ```sql
   SELECT * FROM ALARMS_DEFINITION_TBX WHERE ACTIVE = 1
   ```
4. Review auto-archive settings:
   ```java
   alarm.getAutoArchiveTime()
   ```

### Resolution
- For missing definitions: Add appropriate AlarmDefinition entries
- For notification issues: Update notification settings and verify contact information
- For severity issues: Adjust severity levels based on operational impact
- For performance: Add appropriate database indexes on frequently queried fields

### Monitoring
- Monitor the count of active alarm definitions
- Track changes to critical alarm definitions
- Audit notification settings periodically
- Review alarm definition usage patterns to identify potential improvements
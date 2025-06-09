# AlarmEvent Technical Documentation

## Purpose
AlarmEvent.java represents an occurrence of an alarm condition in the stamp storage system. It captures the details of when an alarm was triggered, its status (cleared or not), and who cleared it if applicable. This entity serves as the runtime record of alarm instances that occur during system operation.

## Logic/Functionality
- Implements a JPA entity mapped to the ALARM_EVENT_TBX table
- Records alarm occurrences with timestamps and status information
- Provides methods to find and manage alarm events
- Supports querying for uncleared alarms that require notification
- Links to AlarmDefinition to provide context about the alarm type

## Flow
1. When an alarm condition is detected in the system, an AlarmEvent is created
2. The AlarmEvent references the corresponding AlarmDefinition by alarm number and location
3. The system uses AlarmEvent queries to find uncleared alarms for notification and display
4. When an alarm is addressed, the AlarmEvent is updated with clearing information
5. Archived alarms are moved to AlarmEventArchive for historical record-keeping

## Key Elements
- `location`: Integer representing the physical location where the alarm occurred
- `alarmNumber`: Identifier linking to the corresponding AlarmDefinition
- `eventTimestamp`: When the alarm was triggered
- `cleared`: Boolean indicating if the alarm has been addressed
- `clearedBy`: User who addressed the alarm
- `clearedTimestamp`: When the alarm was cleared
- `notified`: Boolean indicating if notifications have been sent for this alarm

## Usage
```java
// Create a new alarm event
AlarmEvent event = new AlarmEvent();
event.setLocation(123);
event.setAlarmNumber(456);
event.setEventTimestamp(new Timestamp(System.currentTimeMillis()));
event.setCleared(false);
event.setNotified(false);
event.persist();

// Find uncleared alarms that need notification
List<AlarmEvent> alarmsToNotify = AlarmEvent.findCurrentUnClearedAlarmsToNotify(100);
for (AlarmEvent alarm : alarmsToNotify) {
    // Send notification
    alarm.setNotified(true);
    alarm.merge();
}

// Clear an alarm
AlarmEvent alarm = AlarmEvent.findCurrent_Alarm(alarmId);
alarm.setCleared(true);
alarm.setClearedBy("username");
alarm.setClearedTimestamp(new Timestamp(System.currentTimeMillis()));
alarm.merge();
```

## Debugging and Production Support

### Common Issues
1. High volume of uncleared alarms causing system performance degradation
2. Missing alarm definitions for triggered alarms
3. Notification failures for critical alarms
4. Inconsistent alarm clearing practices
5. Duplicate alarm events for the same condition

### Debugging Steps
1. Check for uncleared alarms:
   ```java
   List<AlarmEvent> uncleared = AlarmEvent.findCurrentUnClearedAlarms(1000);
   System.out.println("Uncleared alarm count: " + uncleared.size());
   ```

2. Verify alarm definition exists:
   ```java
   AlarmEvent alarm = AlarmEvent.findCurrent_Alarm(alarmId);
   AlarmDefinition def = alarm.getAlarm();
   if (def == null || def.getId() == null) {
       System.out.println("Missing alarm definition for alarm: " + alarm.getAlarmNumber());
   }
   ```

3. Check notification status:
   ```java
   List<AlarmEvent> notificationFailed = AlarmEvent.findCurrentUnClearedAlarmsToNotify(100);
   System.out.println("Alarms pending notification: " + notificationFailed.size());
   ```

4. Examine alarm clearing patterns:
   ```sql
   SELECT CLEARED_BY, COUNT(*) FROM ALARM_EVENT_TBX GROUP BY CLEARED_BY;
   ```

### Resolution
- For high volume of uncleared alarms: Implement auto-clearing based on business rules
- For missing definitions: Add appropriate AlarmDefinition entries
- For notification failures: Verify notification service is functioning and contacts are configured
- For inconsistent clearing: Provide training or implement automated clearing processes
- For duplicate events: Implement deduplication logic in alarm creation process

### Monitoring
- Track the count of uncleared alarms over time
- Monitor the time between alarm triggering and clearing
- Audit notification success rates
- Review alarm patterns to identify recurring issues
- Set up alerts for alarms that remain uncleared beyond a threshold time
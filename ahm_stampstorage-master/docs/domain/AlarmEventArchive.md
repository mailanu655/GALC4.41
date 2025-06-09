# AlarmEventArchive Technical Documentation

## Purpose
AlarmEventArchive.java represents the historical record of alarm events that have been archived from the active alarm system. It serves as a long-term storage mechanism for alarm data that is no longer active but needs to be retained for historical analysis, auditing, and reporting purposes.

## Logic/Functionality
- Implements a JPA entity mapped to the ALARM_EVENT_ARCHIVE_TBX table
- Stores archived alarm events with complete history including creation and clearing information
- Adds archival metadata (timestamp and user who archived)
- Provides methods to query and retrieve historical alarm data
- Maintains a reference to the original AlarmEvent ID for traceability

## Flow
1. When an AlarmEvent is no longer needed in the active system (typically after being cleared and a certain time has passed), it is archived
2. The AlarmEventArchive entity is created with all data from the original AlarmEvent
3. Additional archival metadata is added (archival timestamp and user)
4. The original AlarmEvent may be deleted from the active system
5. The archived data remains available for historical reporting and analysis

## Key Elements
- `alarmEventId`: Reference to the original AlarmEvent ID
- `location`: Integer representing the physical location where the alarm occurred
- `alarmNumber`: Identifier linking to the corresponding AlarmDefinition
- `eventTimestamp`: When the alarm was triggered
- `cleared`: Boolean indicating if the alarm was addressed before archiving
- `clearedBy`: User who addressed the alarm
- `clearedTimestamp`: When the alarm was cleared
- `archivalTimestamp`: When the alarm was archived
- `archivedBy`: User or process that performed the archiving

## Usage
```java
// Archive an alarm event
AlarmEvent event = AlarmEvent.findCurrent_Alarm(alarmId);
if (event != null && event.getCleared()) {
    AlarmEventArchive archive = new AlarmEventArchive();
    archive.setAlarmEventId(event.getId());
    archive.setLocation(event.getLocation());
    archive.setAlarmNumber(event.getAlarmNumber());
    archive.setEventTimestamp(event.getEventTimestamp());
    archive.setCleared(event.getCleared());
    archive.setClearedBy(event.getClearedBy());
    archive.setClearedTimestamp(event.getClearedTimestamp());
    archive.setArchivedTimestamp(new Timestamp(System.currentTimeMillis()));
    archive.setArchivedBy("archival_process");
    archive.persist();
    
    // Optionally remove the original event
    event.remove();
}

// Query archived alarms
List<AlarmEventArchive> archivedAlarms = AlarmEventArchive.findArchivedAlarmEntries(0, 100);
for (AlarmEventArchive archive : archivedAlarms) {
    // Process archived alarm data
    System.out.println("Archived alarm: " + archive.getAlarmNumber() + 
                       " at location: " + archive.getLocation() + 
                       " archived on: " + archive.getArchivedTimestamp());
}
```

## Debugging and Production Support

### Common Issues
1. Missing data during the archival process
2. Performance issues with large archive tables
3. Inconsistent archival policies leading to premature or delayed archiving
4. Difficulty correlating archived alarms with their definitions
5. Storage growth issues due to high alarm volume

### Debugging Steps
1. Verify complete data transfer during archival:
   ```java
   AlarmEvent event = AlarmEvent.findCurrent_Alarm(alarmId);
   AlarmEventArchive archive = AlarmEventArchive.findArchivedAlarm(archiveId);
   
   if (!event.getAlarmNumber().equals(archive.getAlarmNumber()) ||
       !event.getLocation().equals(archive.getLocation())) {
       System.out.println("Data mismatch during archival");
   }
   ```

2. Check archive table size and growth:
   ```java
   long archiveCount = AlarmEventArchive.countArchivedAlarms();
   System.out.println("Archive size: " + archiveCount);
   ```

3. Verify archival criteria:
   ```java
   // Check if alarms are being archived according to policy
   List<AlarmEvent> clearedAlarms = AlarmEvent.findCurrentUnClearedAlarms(1000);
   for (AlarmEvent alarm : clearedAlarms) {
       if (alarm.getCleared() && 
           (System.currentTimeMillis() - alarm.getClearedTimestamp().getTime()) > 
           (7 * 24 * 60 * 60 * 1000)) { // 7 days
           System.out.println("Alarm should be archived: " + alarm.getId());
       }
   }
   ```

### Resolution
- For missing data: Implement transaction management to ensure complete data transfer
- For performance issues: Add appropriate indexes to the archive table and implement data partitioning
- For inconsistent archival: Create a scheduled job with clear archival criteria
- For correlation difficulties: Ensure AlarmDefinition data is preserved or referenced
- For storage growth: Implement data retention policies and periodic purging of very old archives

### Monitoring
- Track archive table size and growth rate
- Monitor archival job performance and success rates
- Audit the completeness of archived data
- Set up alerts for archival process failures
- Periodically validate that archived data meets retention requirements
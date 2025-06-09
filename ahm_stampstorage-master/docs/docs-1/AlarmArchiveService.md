# AlarmArchiveService Technical Documentation

## Purpose
The `AlarmArchiveService` interface defines a service responsible for archiving alarm events in the StampStorage system. It provides functionality to move alarms from an active state to an archived state, either automatically based on configured timeouts or manually by user action.

## Logic/Functionality
- Provides a `run()` method that executes the automatic alarm archiving process
- Offers an `archiveAlarm()` method for manual archiving of specific alarms
- Allows setting a `ServiceRoleWrapper` to determine if the service should operate in active or passive mode

## Flow
1. The service is periodically executed (likely by a scheduler)
2. It checks if it's in passive mode (via ServiceRoleWrapper) - if so, it doesn't process alarms
3. When active, it retrieves expired alarms that need to be archived
4. For each expired alarm, it archives the alarm with the system as the source
5. Manual archiving can also be triggered by user action through the `archiveAlarm()` method

## Key Elements
- `run()`: The main method that executes the automatic alarm archiving process
- `archiveAlarm(Long id, String user)`: Method to manually archive a specific alarm
- `setServiceRoleWrapper()`: Method to set the service role (active/passive) wrapper

## Usage
This service is used in production environments to:
- Automatically clean up old alarms that are no longer relevant
- Allow users to manually archive alarms that have been addressed
- Maintain a historical record of all alarms while keeping the active alarm list manageable
- It's typically scheduled to run at regular intervals via a scheduler

## Debugging and Production Support

### Common Issues
1. Alarms not being archived automatically
2. Archived alarms still appearing in active alarm lists
3. Service running in passive mode when it should be active
4. Performance issues with large numbers of alarms
5. Incorrect archiving of alarms that should remain active

### Debugging Steps
1. Check logs for "AlarmArchiveServiceImpl#run()" entries to verify execution
2. Look for "Passive mode...not running" to determine if service is in passive mode
3. Verify that `alarmServiceHelper.getExpiredAlarms()` is returning expected alarms
4. Check database tables for alarm status and archiving timestamps
5. Verify auto-archive time settings in alarm definitions

### Resolution
- For alarms not being archived: Check auto-archive time settings and service execution
- For archived alarms in active lists: Verify query filters in UI components
- For passive mode issues: Check ServiceRoleWrapper configuration
- For performance issues: Consider batching archive operations or optimizing database queries
- For incorrect archiving: Review alarm definition configurations

### Monitoring
- Monitor log files for exceptions in the AlarmArchiveServiceImpl
- Track the number of alarms being archived in each run
- Monitor database growth of archived alarm tables
- Set up alerts for unusual spikes in alarm archiving activity
- Periodically review archive settings for different alarm types
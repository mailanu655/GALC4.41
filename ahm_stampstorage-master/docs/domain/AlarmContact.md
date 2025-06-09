# AlarmContact Technical Documentation

## Purpose
AlarmContact.java defines the relationship between alarm definitions and contacts in the stamp storage system. It serves as a mapping entity that associates specific alarms with the contacts who should be notified when those alarms are triggered, along with the type of contact method to be used.

## Logic/Functionality
- Implements a JPA entity mapped to the ALARM_CONTACT_TBX table
- Maintains a many-to-one relationship with AlarmDefinition and Contact entities
- Provides CRUD operations through standard JPA EntityManager methods
- Includes finder methods to retrieve alarm contacts by various criteria

## Flow
1. When an alarm is configured, AlarmContact entities are created to link the alarm to specific contacts
2. When an alarm is triggered, the system can query AlarmContact to determine who should be notified
3. The contact type (email, pager, etc.) determines how the notification is sent

## Key Elements
- `alarm`: Reference to an AlarmDefinition entity
- `contact`: Reference to a Contact entity
- `contactType`: Enumerated value indicating how the contact should be notified
- Finder methods like `findAlarmContactByAlarmAndContact` and `findAlarmContactsByAlarm`

## Usage
```java
// Find all contacts for a specific alarm
AlarmDefinition alarm = AlarmDefinition.findAlarmDefinition(alarmId);
List<AlarmContact> contacts = AlarmContact.findAlarmContactsByAlarm(alarm);

// Create a new alarm contact
AlarmContact alarmContact = new AlarmContact();
alarmContact.setAlarm(alarm);
alarmContact.setContact(contact);
alarmContact.setContactType(ContactType.EMAIL);
alarmContact.persist();
```

## Debugging and Production Support

### Common Issues
1. Missing contacts for alarms, resulting in notifications not being sent
2. Incorrect contact type configuration
3. Database constraint violations when creating or updating alarm contacts

### Debugging Steps
1. Verify alarm and contact relationships:
   ```java
   AlarmContact.findAlarmContactsByAlarm(alarm)
   ```
2. Check if contacts exist for critical alarms:
   ```sql
   SELECT * FROM ALARM_CONTACT_TBX WHERE ALARM = ?
   ```
3. Examine logs for persistence errors related to AlarmContact entities

### Resolution
- For missing contacts: Add appropriate AlarmContact entries for critical alarms
- For incorrect contact types: Update the contactType field with the correct enum value
- For database issues: Check for referential integrity constraints and ensure both alarm and contact exist

### Monitoring
- Monitor the count of alarm contacts to ensure critical alarms have associated contacts
- Periodically audit alarm contacts to verify notification paths are correctly configured
- Log notification failures to identify potential issues with alarm contact configuration
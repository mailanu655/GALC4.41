# AuditErrorLog Technical Documentation

## Purpose
AuditErrorLog.java represents the system's error logging mechanism, providing a structured way to record, store, and query system errors and events. It serves as a critical component for system monitoring, troubleshooting, and maintaining an audit trail of system behavior, particularly for error conditions.

## Logic/Functionality
- Implements a JPA entity mapped to the AUDIT_ERROR_LOG_TBX table
- Provides methods to record system errors with detailed context information
- Supports sophisticated querying capabilities through finder criteria
- Categorizes errors by severity, source, and node
- Includes timestamp information for chronological tracking
- Offers static utility methods for simplified logging

## Flow
1. When an error or notable event occurs in the system, an AuditErrorLog entry is created
2. The entry captures contextual information including the source, severity, and message
3. The log entry is persisted to the database for future reference
4. System administrators and support personnel can query the logs using finder criteria
5. Log entries can be analyzed to identify patterns, troubleshoot issues, or audit system behavior

## Key Elements
- `nodeId`: Identifier for the system node where the error occurred
- `source`: Component or module that generated the error
- `severity`: Integer representing the error severity (uses SEVERITY enum)
- `messageText`: Detailed description of the error or event
- `logTimestamp`: When the error was recorded
- `AuditErrorLogFinderCriteria`: Companion class for sophisticated querying

## Usage
```java
// Simple logging of an error
AuditErrorLog.save("NODE1", "Database connection failed", "DatabaseService");

// Creating and persisting a custom log entry
AuditErrorLog errorLog = new AuditErrorLog();
errorLog.setNodeId("STORAGE_NODE_5");
errorLog.setSource("CarrierService");
errorLog.setSeverity(SEVERITY.FOUR.type());
errorLog.setMessageText("Failed to move carrier #1234 to destination");
errorLog.setLogTimestamp(new Timestamp(System.currentTimeMillis()));
errorLog.persist();

// Querying logs with finder criteria
AuditErrorLogFinderCriteria criteria = new AuditErrorLogFinderCriteria();
criteria.setNodeId("STORAGE_NODE_5");
criteria.setSeverity(SEVERITY.FOUR.type());
criteria.setBeginTimestamp(startTime);
criteria.setEndTimestamp(endTime);

List<AuditErrorLog> errors = AuditErrorLog.findAuditErrorLogByCriteria(criteria, 1, 100);
for (AuditErrorLog error : errors) {
    System.out.println(error.getLogTimestamp() + ": " + error.getMessageText());
}
```

## Debugging and Production Support

### Common Issues
1. Excessive logging causing database growth and performance issues
2. Missing critical error information in log entries
3. Inconsistent severity levels making filtering difficult
4. Timestamp inconsistencies across distributed system components
5. Inadequate querying performance for large log volumes
6. Log retention policies not properly implemented

### Debugging Steps
1. Check log volume and growth:
   ```java
   long logCount = AuditErrorLog.countAuditErrorLogs();
   System.out.println("Total log entries: " + logCount);
   
   // Check recent entries
   AuditErrorLogFinderCriteria criteria = new AuditErrorLogFinderCriteria();
   criteria.setBeginTimestamp(new Timestamp(System.currentTimeMillis() - 24*60*60*1000)); // Last 24 hours
   Long recentCount = AuditErrorLog.findAuditErrorLogCount(criteria);
   System.out.println("Entries in last 24 hours: " + recentCount);
   ```

2. Verify log content completeness:
   ```java
   AuditErrorLogFinderCriteria criteria = new AuditErrorLogFinderCriteria();
   criteria.setSource("CriticalComponent");
   List<AuditErrorLog> logs = AuditErrorLog.findAuditErrorLogByCriteria(criteria, 1, 10);
   
   for (AuditErrorLog log : logs) {
       if (log.getMessageText() == null || log.getMessageText().isEmpty()) {
           System.out.println("Empty message text in log ID: " + log.getId());
       }
   }
   ```

3. Analyze severity distribution:
   ```java
   for (int severity = 1; severity <= 5; severity++) {
       AuditErrorLogFinderCriteria criteria = new AuditErrorLogFinderCriteria();
       criteria.setSeverity(severity);
       Long count = AuditErrorLog.findAuditErrorLogCount(criteria);
       System.out.println("Severity " + severity + " count: " + count);
   }
   ```

### Resolution
- For excessive logging: Implement log level filtering at the source and periodic archiving
- For missing information: Enhance logging templates and enforce required fields
- For inconsistent severity: Establish clear severity guidelines and review logging practices
- For timestamp issues: Implement synchronized time services across system components
- For query performance: Add appropriate indexes and implement log partitioning
- For retention issues: Create automated purging jobs based on configurable retention policies

### Monitoring
- Track log volume by severity, source, and node
- Set up alerts for unusual spikes in error frequency
- Monitor database space used by the log table
- Periodically review high-severity errors
- Validate that log retention policies are functioning correctly
- Check for patterns that might indicate systemic issues
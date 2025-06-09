# AuditErrorLogFinderCriteria Technical Documentation

## Purpose
AuditErrorLogFinderCriteria.java provides a structured query criteria model for searching and filtering AuditErrorLog entries. It enables sophisticated, multi-parameter searches of the error log database without requiring complex SQL queries to be written directly in application code.

## Logic/Functionality
- Serves as a criteria container for filtering AuditErrorLog entries
- Provides fields that map to AuditErrorLog properties for query construction
- Supports partial matching for text fields
- Enables date range filtering through begin and end timestamps
- Works in conjunction with AuditErrorLog finder methods to build dynamic queries

## Flow
1. An instance of AuditErrorLogFinderCriteria is created
2. Search parameters are set on the criteria object based on user input or system requirements
3. The criteria object is passed to AuditErrorLog finder methods
4. The finder methods construct and execute a dynamic query based on the provided criteria
5. Results matching the criteria are returned to the caller

## Key Elements
- `nodeId`: Filter by system node identifier (supports partial matching)
- `source`: Filter by error source component (supports partial matching)
- `severity`: Filter by error severity level (exact match)
- `messageText`: Filter by error message content (supports partial matching)
- `beginTimestamp`: Filter for errors occurring on or after this timestamp
- `endTimestamp`: Filter for errors occurring on or before this timestamp

## Usage
```java
// Create criteria for searching critical database errors in the last hour
AuditErrorLogFinderCriteria criteria = new AuditErrorLogFinderCriteria();
criteria.setSource("Database");
criteria.setSeverity(SEVERITY.ONE.type()); // Critical severity
criteria.setMessageText("connection");      // Partial match for connection-related errors

// Set time range for the last hour
Timestamp endTime = new Timestamp(System.currentTimeMillis());
Timestamp beginTime = new Timestamp(System.currentTimeMillis() - 3600000); // 1 hour ago
criteria.setBeginTimestamp(beginTime);
criteria.setEndTimestamp(endTime);

// Execute search with pagination (page 1, 20 results per page)
List<AuditErrorLog> criticalDbErrors = AuditErrorLog.findAuditErrorLogByCriteria(criteria, 1, 20);

// Get total count matching criteria (for pagination)
Long totalMatches = AuditErrorLog.findAuditErrorLogCount(criteria);

System.out.println("Found " + criticalDbErrors.size() + " of " + totalMatches + " total matching errors");
```

## Debugging and Production Support

### Common Issues
1. Inefficient queries due to too many wildcard searches
2. Timestamp range issues due to timezone inconsistencies
3. No results returned when criteria are too restrictive
4. Too many results returned when criteria are too broad
5. Performance degradation with large log volumes
6. Inconsistent behavior with null criteria values

### Debugging Steps
1. Verify criteria construction:
   ```java
   AuditErrorLogFinderCriteria criteria = new AuditErrorLogFinderCriteria();
   criteria.setNodeId("NODE1");
   criteria.setSeverity(SEVERITY.THREE.type());
   
   System.out.println("Node ID filter: " + criteria.getNodeId());
   System.out.println("Severity filter: " + criteria.getSeverity());
   ```

2. Check timestamp range validity:
   ```java
   AuditErrorLogFinderCriteria criteria = new AuditErrorLogFinderCriteria();
   criteria.setBeginTimestamp(beginTime);
   criteria.setEndTimestamp(endTime);
   
   if (criteria.getBeginTimestamp() != null && 
       criteria.getEndTimestamp() != null &&
       criteria.getBeginTimestamp().after(criteria.getEndTimestamp())) {
       System.out.println("Invalid time range: begin time is after end time");
   }
   ```

3. Test with minimal criteria to isolate issues:
   ```java
   // Test with just one criterion
   AuditErrorLogFinderCriteria criteria = new AuditErrorLogFinderCriteria();
   criteria.setNodeId("NODE1");
   
   List<AuditErrorLog> results = AuditErrorLog.findAuditErrorLogByCriteria(criteria, 1, 10);
   System.out.println("Results with only nodeId filter: " + results.size());
   
   // Clear and try another single criterion
   criteria = new AuditErrorLogFinderCriteria();
   criteria.setSeverity(SEVERITY.ONE.type());
   
   results = AuditErrorLog.findAuditErrorLogByCriteria(criteria, 1, 10);
   System.out.println("Results with only severity filter: " + results.size());
   ```

### Resolution
- For inefficient queries: Limit wildcard searches and add database indexes
- For timestamp issues: Standardize on UTC for storage and convert to local time for display
- For restrictive criteria: Implement a step-by-step approach, starting with broader criteria
- For too many results: Add pagination and sorting to manage large result sets
- For performance issues: Optimize database queries and consider denormalization
- For null handling: Implement consistent null handling in query construction

### Monitoring
- Track query execution times for different criteria combinations
- Monitor result set sizes to identify potential performance issues
- Log criteria that consistently return zero results
- Periodically review query patterns to identify optimization opportunities
- Set up alerts for queries exceeding performance thresholds
- Validate that timestamp-based queries are working correctly across timezone changes
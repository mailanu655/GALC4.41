# StorageConfig Technical Documentation

## Purpose
`StorageConfig` is a configuration class that defines constants used throughout the stamp storage system, particularly for identifying the source of operations in audit logs, event tracking, and user attribution. These constants provide a standardized way to identify which component or process initiated an action in the system.

## Logic/Functionality
The class provides a set of string constants that identify different components or operations in the system:

- **Manual Operations**:
  - `OHCV_APP_MANUAL_ORDER`: Identifies manual order operations
  - `OHCV_APP_MANUAL_EMPTY`: Identifies manual empty carrier operations

- **Automated Operations**:
  - `OHCV_APP_AUTO_EMPTY`: Identifies automatic empty carrier operations
  - `OHCV_APP_RECIRC`: Identifies recirculation operations
  - `OHCV_APP_RESEQUENCE`: Identifies resequencing operations

- **Process Operations**:
  - `OHCV_APP_STOREIN`: Identifies store-in operations
  - `OHCV_APP_STOREOUT`: Identifies store-out operations
  - `OHCV_APP_FULFILLMENT`: Identifies fulfillment operations
  - `OHCV_APP_BACKORDER_PROCESSOR`: Identifies back order processor operations
  - `OHCV_APP_STORAGE_PROCESSOR`: Identifies storage processor operations

- **Alarm Operations**:
  - `OHCV_APP_ALARM_ARCHIVE`: Identifies alarm archiving operations
  - `OHCV_APP_ALARM_RESET`: Identifies alarm reset operations

- **General**:
  - `OHCV_APP`: General application identifier

## Flow
1. Components use these constants when logging operations or setting source fields
2. The constants provide consistent identification across the system
3. Audit logs and event records include these identifiers to track the origin of actions
4. Analysis and troubleshooting use these identifiers to filter and categorize system events

## Key Elements
- Private constructor to prevent instantiation (utility class pattern)
- String constants for operation identification
- Naming convention with "OHCV-APP" prefix
- Final class to prevent extension

## Usage
```java
// Example: Set the source of a carrier operation
carrier.setSource(StorageConfig.OHCV_APP_STOREIN);

// Example: Log an operation with its source
logger.info("Operation completed by " + StorageConfig.OHCV_APP_MANUAL_ORDER);

// Example: Archive an alarm with user attribution
helper.archiveAlarm(alarmId, StorageConfig.OHCV_APP_ALARM_RESET);

// Example: Set the source in an audit log entry
auditLog.setSource(StorageConfig.OHCV_APP_STORAGE_PROCESSOR);
```

## Debugging and Production Support

### Common Issues
1. **Inconsistent Usage**: Constants may be used inconsistently across the system
2. **Missing Constants**: New operations may not have corresponding constants
3. **Constant Duplication**: Similar constants may be defined elsewhere
4. **Ambiguous Attribution**: Some operations may be attributed to the wrong source

### Debugging Steps
1. Search for string literals that should use these constants
   ```java
   // Search for hardcoded strings like "OHCV-APP-STOREIN" in the codebase
   ```
2. Check for inconsistent usage patterns
   ```java
   // Verify that similar operations use the same constant
   // For example, all store-in operations should use OHCV_APP_STOREIN
   ```
3. Verify that all operations have appropriate constants
   ```java
   // Review audit logs to identify operations without proper source attribution
   ```
4. Check for duplicate or similar constants in other classes

### Resolution
- Enforce consistent usage of constants
- Add new constants for new operations
- Refactor duplicate constants to use StorageConfig
- Provide documentation on when to use each constant
- Consider using an enum instead of string constants for better type safety

### Monitoring
- Audit logs for inconsistent source identifiers
- Track usage patterns of different operation types
- Monitor the distribution of operation sources to identify unusual patterns
- Review audit logs periodically to ensure proper attribution
- Consider adding validation for source fields to ensure they use valid constants
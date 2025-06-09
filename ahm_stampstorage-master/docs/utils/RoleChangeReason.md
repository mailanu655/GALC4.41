# RoleChangeReason

## Purpose
The RoleChangeReason enum defines a set of standardized reasons for service role changes in the StampStorage system. It provides a type-safe way to represent and communicate why a service has changed its role (e.g., from active to passive or vice versa), enabling consistent logging and reporting of role change events.

## Logic/Functionality
- Defines an enumeration of possible reasons for service role changes
- Associates each reason with a descriptive message
- Provides a method to retrieve the message for a given reason
- Enables type-safe representation of role change reasons

## Flow
1. When a service role change occurs, a RoleChangeReason is specified
2. The reason is used to document why the change occurred
3. The associated message can be retrieved for logging or reporting
4. This provides consistent terminology and explanation for role changes

## Key Elements
- **Enum Values**: Predefined constants representing different role change reasons
- **Message Field**: A string message associated with each reason
- **Constructor**: Sets the message for each enum value
- **getMessage Method**: Retrieves the message for a given reason

## Usage
The RoleChangeReason enum is typically used when logging or reporting service role changes:

```java
// Example of using RoleChangeReason
RoleChangeReason reason = RoleChangeReason.MISSED_PING;
String message = reason.getMessage();
log.info("Service role changed to passive due to: " + message);
```

It can also be used in conditional logic:

```java
// Example of conditional logic based on role change reason
if (roleChangeReason == RoleChangeReason.FORCE_START) {
    // Handle forced start scenario
} else if (roleChangeReason == RoleChangeReason.MISSED_PING) {
    // Handle missed ping scenario
}
```

## Debugging and Production Support

### Common Issues
1. **Missing Reasons**: New role change scenarios might not have corresponding enum values
2. **Inconsistent Usage**: Different parts of the system might use different reasons for the same scenario
3. **Unclear Messages**: The messages associated with reasons might not be clear or descriptive enough
4. **Enum Evolution**: As the system evolves, the enum might need to be updated to include new reasons

### Debugging Steps
1. **Review Role Change Logs**: Examine logs to see which reasons are being used and in what contexts
2. **Check Reason Coverage**: Verify that all possible role change scenarios have corresponding enum values
3. **Assess Message Clarity**: Evaluate whether the messages clearly explain the reasons for role changes
4. **Analyze Usage Patterns**: Look for inconsistencies in how reasons are assigned to role changes

### Resolution
1. **Add New Reasons**: Extend the enum with new values for additional role change scenarios
2. **Standardize Usage**: Ensure consistent usage of reasons across the system
3. **Improve Messages**: Enhance the clarity and descriptiveness of messages
4. **Document Reasons**: Provide comprehensive documentation of when each reason should be used

### Monitoring
1. **Reason Distribution**: Track the frequency of different role change reasons
2. **Temporal Patterns**: Look for patterns in when specific reasons occur
3. **Correlation Analysis**: Correlate role change reasons with other system events
4. **Anomaly Detection**: Identify unusual patterns in role change reasons that might indicate problems
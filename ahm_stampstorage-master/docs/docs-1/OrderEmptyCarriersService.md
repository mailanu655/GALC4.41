# OrderEmptyCarriersService Technical Documentation

## Purpose
The `OrderEmptyCarriersService` interface extends the `StampStorageService` interface and defines the contract for services responsible for ordering empty carriers in the StampStorage system. It provides functionality to ensure that specific areas maintain an adequate supply of empty carriers.

## Logic/Functionality
- Extends the base `StampStorageService` interface
- Defines a `run()` method that executes the empty carrier ordering process
- Provides a method to set the service role wrapper for active/passive mode control
- Serves as a contract for implementations that handle different areas of the system

## Flow
The OrderEmptyCarriersService is designed to be executed periodically to:
1. Check if specific areas have enough empty carriers
2. If an area is underfilled, order more empty carriers to that area
3. Respect active/passive mode configuration to prevent duplicate operations

## Key Elements
- `run()`: The main method that executes the empty carrier ordering process
- `setServiceRoleWrapper()`: Method to set the service role (active/passive) wrapper

## Usage
This interface is implemented by services that:
- Monitor empty carrier levels in specific areas
- Order more empty carriers when levels are low
- Maintain appropriate carrier distribution throughout the system
- It's typically scheduled to run at regular intervals via a scheduler

## Debugging and Production Support

### Common Issues
1. Empty carrier levels not being maintained properly
2. Service running in passive mode when it should be active
3. Conflicts between multiple ordering services
4. Configuration issues with thresholds and counts
5. Performance issues during high activity periods

### Debugging Steps
1. Verify the service is running by checking logs for execution entries
2. Check if the service is in passive mode
3. Verify threshold configurations for determining underfilled status
4. Check carrier count configurations for release operations
5. Monitor for conflicts with other carrier movement operations

### Resolution
- For level maintenance issues: Check threshold configurations and release logic
- For passive mode issues: Verify ServiceRoleWrapper configuration
- For conflicts: Implement coordination between services
- For configuration issues: Verify parameter values in the database
- For performance issues: Optimize operations and consider scheduling adjustments

### Monitoring
- Track empty carrier levels in different areas
- Monitor carrier ordering operations
- Set up alerts for areas consistently below thresholds
- Track service execution frequency
- Monitor for unusual patterns in carrier distribution
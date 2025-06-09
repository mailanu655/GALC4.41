# EmptyCarriersServiceHelper Technical Documentation

## Purpose
The `EmptyCarriersServiceHelper` interface extends the `Helper` interface and defines methods for managing empty carriers in the StampStorage system. It provides functionality to track, count, and manage the movement of empty carriers between different areas of the storage system.

## Logic/Functionality
- Provides methods to count carriers in different storage areas (old weld line, empty area, B area)
- Offers functionality to retrieve carriers for movement to empty storage areas
- Defines methods to get delivery stops for different areas
- Provides utility methods to check order status and carrier release conditions
- Enables verification of carrier positions and lane occupancy

## Flow
The EmptyCarriersServiceHelper is used by various services to:
1. Monitor carrier counts in different storage areas
2. Identify carriers that need to be moved to empty storage areas
3. Determine appropriate delivery stops for empty carriers
4. Verify conditions for releasing carriers
5. Check order processing status to avoid conflicts

## Key Elements
- Methods for counting carriers in different areas:
  - `getCarrierCountForOldWeldLineEmptyArea()`
  - `getCarrierCountForEmptyArea()`
  - `getCarrierCountForBAreaEmptyStorage()`
- Methods for retrieving delivery stops:
  - `getOldWeldLineEmptyCarrierDeliveryStop()`
  - `getEmptyCarrierDeliveryStop()`
  - `getBAreaEmptyCarrierDeliveryStop()`
- Methods for carrier management:
  - `getCarriersToMoveToEmptyStorageArea()`
  - `canReleaseCarrier()`
  - `getCarrierCountInLane()`
- Status check methods:
  - `activeOrderExistsForOrderMgr()` (deprecated)
  - `anyOrderInProcess()`
  - `anyCarrierSetToReleaseFromRows()`

## Usage
This helper interface is used throughout the StampStorage system to:
- Support empty carrier management services
- Enable automated movement of empty carriers between storage areas
- Maintain appropriate carrier levels in different areas
- Prevent conflicts between carrier movements and active orders
- It's typically injected into services that manage empty carrier movement

## Debugging and Production Support

### Common Issues
1. Incorrect carrier counts in storage areas
2. Empty carriers not being moved when needed
3. Conflicts between carrier movements and active orders
4. Missing or incorrect delivery stop configurations
5. Performance issues with carrier queries

### Debugging Steps
1. Verify carrier count methods are returning accurate numbers
2. Check delivery stop configurations in the database
3. Verify order status checks are functioning correctly
4. Check carrier release conditions
5. Monitor database queries for performance issues

### Resolution
- For count discrepancies: Verify database queries and carrier status definitions
- For movement issues: Check delivery stop configurations and release conditions
- For conflicts: Verify order status checks and carrier release logic
- For missing stops: Check stop configurations in the database
- For performance issues: Optimize queries and consider indexing strategies

### Monitoring
- Track carrier counts in different storage areas
- Monitor empty carrier movement operations
- Set up alerts for unusual patterns in carrier distribution
- Track carrier release success rates
- Monitor query performance for carrier retrieval methods
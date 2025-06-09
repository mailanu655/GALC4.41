# EmptyCarriersServiceHelperImpl Technical Documentation

## Purpose
The `EmptyCarriersServiceHelperImpl` class implements the `EmptyCarriersServiceHelper` interface and provides concrete implementations for managing empty carriers in the StampStorage system. It handles counting, tracking, and facilitating the movement of empty carriers between different areas of the storage system.

## Logic/Functionality
- Extends `AbstractHelperImpl` to inherit common helper functionality
- Implements methods to count carriers in different storage areas
- Provides functionality to retrieve carriers for movement to empty storage areas
- Implements methods to get delivery stops for different areas
- Offers utility methods to check order status and carrier release conditions
- Handles verification of carrier positions and lane occupancy

## Flow
1. For carrier counting:
   - Queries the database for carriers at specific locations with empty die numbers
   - Logs carrier counts for monitoring and debugging
   - Returns counts for decision-making by carrier management services

2. For delivery stop retrieval:
   - Queries the database for stops with specific types and areas
   - Returns the appropriate stop for carrier delivery operations
   - Handles cases where stops might not be configured

3. For carrier movement:
   - Identifies carriers at specific locations
   - Verifies they are not already in storage rows
   - Creates a list of carriers eligible for movement
   - Limits the list based on requested count

4. For status checking:
   - Queries for active orders and carrier release operations
   - Determines if carrier movements might conflict with other operations
   - Provides status information for decision-making

## Key Elements
- Database queries for carrier and stop information
- Logging for monitoring and debugging
- Status checking logic to prevent conflicts
- Carrier selection logic for movement operations
- Row verification to ensure carriers are in expected locations

## Usage
This implementation is used throughout the StampStorage system to:
- Support empty carrier management services
- Enable automated movement of empty carriers between storage areas
- Maintain appropriate carrier levels in different areas
- Prevent conflicts between carrier movements and active orders
- It's typically injected into services that manage empty carrier movement

## Debugging and Production Support

### Common Issues
1. Incorrect carrier counts due to database query issues
2. Missing or misconfigured delivery stops
3. Carriers being incorrectly identified as in rows
4. Performance issues with carrier queries
5. Conflicts between carrier movements and other operations

### Debugging Steps
1. Check log entries for carrier counts in different areas
2. Verify stop configurations in the database
3. Examine the `isCarrierInRow()` method logic for correctness
4. Monitor database query performance
5. Check order status and carrier release queries

### Resolution
- For count discrepancies: Verify database queries and carrier status definitions
- For stop issues: Check stop type and area configurations in the database
- For row identification problems: Review the `isCarrierInRow()` method logic
- For performance issues: Optimize queries and consider indexing strategies
- For conflicts: Verify order status checks and carrier release logic

### Monitoring
- Track log entries for carrier counts in different areas
- Monitor for "STOP Missing" messages indicating configuration issues
- Track carrier movement operations
- Set up alerts for unusual patterns in carrier distribution
- Monitor query performance for carrier retrieval methods
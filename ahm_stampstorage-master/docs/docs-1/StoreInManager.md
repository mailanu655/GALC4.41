# StoreInManager Technical Documentation

## Purpose
`StoreInManager` is an interface that defines the contract for storing carriers in the stamp storage system. It provides methods for determining the appropriate storage location for carriers based on business rules. This interface is a key component in the carrier routing logic, ensuring carriers are stored in optimal locations based on their characteristics and system conditions.

## Logic/Functionality
The interface declares two primary methods:

### General Storage
- `store(Carrier carrier)`: Determines the appropriate storage row for a carrier based on general storage rules. This method applies the full set of business rules to find the optimal storage location for any carrier.

### Area-Specific Storage
- `subStore(Carrier carrier, StorageArea area)`: Determines the appropriate storage row for a carrier within a specific storage area. This method applies a subset of business rules constrained to the specified area, allowing for more targeted storage decisions.

Both methods are expected to implement complex business logic to determine the optimal storage location based on factors such as:
- Carrier die number
- Current storage utilization
- Lane capacities
- Carrier status
- Storage area characteristics
- Mixing rules for different die numbers
- Priority rules for different storage areas

## Flow
1. Components that need to store carriers depend on the StoreInManager interface
2. A concrete implementation is injected or provided
3. When a carrier needs to be stored, the appropriate method is called:
   - `store(Carrier)` for general storage decisions
   - `subStore(Carrier, StorageArea)` for area-specific storage decisions
4. The implementation applies business rules to determine the optimal storage location
5. The selected storage row is returned to the caller for further processing

## Key Elements
- Method declarations for storage operations
- No implementation details (as it's an interface)
- Serves as a contract for implementations
- Support for both general and area-specific storage decisions

## Usage
```java
// Example: Component with StoreInManager dependency
public class CarrierStorageProcessor {
    private StoreInManager storeInManager;
    
    public CarrierStorageProcessor(StoreInManager storeInManager) {
        this.storeInManager = storeInManager;
    }
    
    // Store a carrier using general rules
    public StorageRow processCarrier(Carrier carrier) {
        return storeInManager.store(carrier);
    }
    
    // Store a carrier in a specific area
    public StorageRow processCarrierForArea(Carrier carrier, StorageArea area) {
        return storeInManager.subStore(carrier, area);
    }
    
    // Process a carrier based on its characteristics
    public StorageRow routeCarrier(Carrier carrier) {
        // Determine if area-specific storage is needed
        if (needsSpecificArea(carrier)) {
            StorageArea targetArea = determineTargetArea(carrier);
            return storeInManager.subStore(carrier, targetArea);
        } else {
            return storeInManager.store(carrier);
        }
    }
    
    private boolean needsSpecificArea(Carrier carrier) {
        // Logic to determine if carrier needs specific area
        return false;
    }
    
    private StorageArea determineTargetArea(Carrier carrier) {
        // Logic to determine target area
        return StorageArea.A_AREA;
    }
}
```

## Debugging and Production Support

### Common Issues
1. **No Suitable Storage Location**: The implementation may not find a suitable storage location for a carrier
2. **Suboptimal Storage Decisions**: The business rules may lead to suboptimal storage decisions
3. **Area Constraints**: Specific storage areas may have constraints that prevent storage
4. **Rule Complexity**: The complex business rules may be difficult to understand and debug
5. **Performance Issues**: Finding the optimal storage location may be computationally intensive

### Debugging Steps
1. Log carrier properties when storing
   ```java
   logger.debug("Storing carrier: number={}, dieId={}, status={}",
       carrier.getCarrierNumber(),
       carrier.getDie() != null ? carrier.getDie().getId() : "null",
       carrier.getCarrierStatus());
   ```
2. Track the decision-making process
   ```java
   logger.debug("Evaluating storage rules for carrier {}", carrier.getCarrierNumber());
   // Log each rule evaluation
   logger.debug("Rule 1 result: {}", rule1Result);
   logger.debug("Rule 2 result: {}", rule2Result);
   // Log the final decision
   logger.debug("Selected storage row: {}", selectedRow != null ? selectedRow.getId() : "none");
   ```
3. Monitor area-specific constraints
   ```java
   logger.debug("Checking constraints for area {}", area);
   // Log constraint evaluations
   logger.debug("Area capacity: {}, current utilization: {}", areaCapacity, areaUtilization);
   ```
4. Verify the selected storage row is appropriate
   ```java
   if (selectedRow != null) {
       logger.debug("Selected row {}: capacity={}, current={}, dieNumbers={}",
           selectedRow.getId(), selectedRow.getCapacity(),
           selectedRow.getCurrentCarrierCount(),
           selectedRow.getDieNumbersForAllCarriers());
   } else {
       logger.warn("No suitable storage row found for carrier {}", carrier.getCarrierNumber());
   }
   ```

### Resolution
- Implement fallback mechanisms for when no suitable storage location is found
- Add detailed logging of the decision-making process
- Optimize the rule evaluation for performance
- Consider adding priority levels to storage rules
- Implement monitoring of storage decisions to identify patterns
- Periodically review and refine the business rules based on operational data

### Monitoring
- Track storage decision metrics (success rate, time to decide)
- Monitor storage utilization across areas
- Log storage rule application details
- Alert on persistent storage decision failures
- Track carrier routing patterns to identify potential improvements
- Monitor the distribution of carriers across storage areas
- Track the frequency of area-specific storage decisions
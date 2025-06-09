# StoreOutManager Technical Documentation

## Purpose
`StoreOutManager` is an interface that defines the contract for retrieving carriers from the stamp storage system based on die requirements. It provides a method for determining the appropriate storage row from which to retrieve a carrier containing parts produced by a specific die. This interface is a key component in the fulfillment process, ensuring that the right carriers are selected to meet production needs.

## Logic/Functionality
The interface declares a single method:

### Carrier Retrieval
- `retrieve(Die die)`: Determines the appropriate storage row from which to retrieve a carrier containing parts produced by the specified die. This method applies business rules to find the optimal carrier to fulfill the die requirement.

This method is expected to implement complex business logic to determine the optimal carrier to retrieve based on factors such as:
- Die number matching
- Carrier position in storage rows
- Carrier status (e.g., shippable, on hold)
- Storage row characteristics
- FIFO (First-In-First-Out) principles
- Blocking carriers that may need to be moved
- Priority rules for different storage areas

## Flow
1. Components that need to retrieve carriers depend on the StoreOutManager interface
2. A concrete implementation is injected or provided
3. When parts from a specific die are needed, the `retrieve(Die)` method is called
4. The implementation applies business rules to determine the optimal storage row
5. The selected storage row is returned to the caller for further processing
6. The caller typically releases the carrier from the selected row

## Key Elements
- Method declaration for carrier retrieval
- No implementation details (as it's an interface)
- Serves as a contract for implementations
- Focus on die-based carrier selection

## Usage
```java
// Example: Component with StoreOutManager dependency
public class CarrierRetrievalProcessor {
    private StoreOutManager storeOutManager;
    private StorageState storageState;
    
    public CarrierRetrievalProcessor(StoreOutManager storeOutManager, StorageState storageState) {
        this.storeOutManager = storeOutManager;
        this.storageState = storageState;
    }
    
    // Retrieve a carrier for a specific die
    public Carrier retrieveCarrierForDie(Die die) {
        // Find the appropriate row
        StorageRow row = storeOutManager.retrieve(die);
        
        if (row != null) {
            // Release the carrier from the row
            return storageState.releaseCarrierFromLane(row);
        } else {
            // No suitable carrier found
            return null;
        }
    }
    
    // Process a fulfillment request
    public boolean fulfillRequest(Long dieNumber, Integer quantity) {
        Die die = Die.findDie(dieNumber);
        if (die == null) {
            return false;
        }
        
        Carrier carrier = retrieveCarrierForDie(die);
        if (carrier == null) {
            return false;
        }
        
        // Check if carrier has sufficient quantity
        return carrier.getQuantity() >= quantity;
    }
}
```

## Debugging and Production Support

### Common Issues
1. **No Suitable Carrier**: The implementation may not find a suitable carrier for a die
2. **Blocked Carriers**: The optimal carrier may be blocked by other carriers
3. **Carrier Status Issues**: Carriers may be on hold or have other status issues
4. **FIFO Violations**: The implementation may not properly follow FIFO principles
5. **Performance Issues**: Finding the optimal carrier may be computationally intensive

### Debugging Steps
1. Log die properties when retrieving
   ```java
   logger.debug("Retrieving carrier for die: id={}, number={}, active={}",
       die.getId(), die.getDieNumber(), die.getActive());
   ```
2. Track the decision-making process
   ```java
   logger.debug("Evaluating retrieval rules for die {}", die.getId());
   // Log each rule evaluation
   logger.debug("Rule 1 result: {}", rule1Result);
   logger.debug("Rule 2 result: {}", rule2Result);
   // Log the final decision
   logger.debug("Selected storage row: {}", selectedRow != null ? selectedRow.getId() : "none");
   ```
3. Monitor carrier status and blocking
   ```java
   for (StorageRow row : candidateRows) {
       Carrier headCarrier = row.getCarrierAtRowOut();
       logger.debug("Row {} head carrier: number={}, dieId={}, status={}",
           row.getId(),
           headCarrier != null ? headCarrier.getCarrierNumber() : "null",
           headCarrier != null && headCarrier.getDie() != null ? headCarrier.getDie().getId() : "null",
           headCarrier != null ? headCarrier.getCarrierStatus() : "null");
       
       int blockingCount = row.getCountOfCarriersBlockingDieNumberAtLaneOutEnd(die.getId());
       logger.debug("Blocking carriers count: {}", blockingCount);
   }
   ```
4. Verify FIFO principles
   ```java
   for (StorageRow row : rowsWithDie) {
       List<Carrier> carriers = row.getCarriersWithDieNumber(die.getId());
       if (carriers != null && !carriers.isEmpty()) {
           Carrier oldestCarrier = carriers.get(0);
           logger.debug("Oldest carrier for die {} in row {}: number={}, timestamp={}",
               die.getId(), row.getId(), oldestCarrier.getCarrierNumber(),
               oldestCarrier.getStampingProductionRunTimestamp());
       }
   }
   ```

### Resolution
- Implement fallback mechanisms for when no suitable carrier is found
- Add detailed logging of the decision-making process
- Optimize the rule evaluation for performance
- Consider adding priority levels to retrieval rules
- Implement monitoring of retrieval decisions to identify patterns
- Periodically review and refine the business rules based on operational data
- Add back order tracking for unfulfilled die requirements

### Monitoring
- Track retrieval decision metrics (success rate, time to decide)
- Monitor carrier availability across dies
- Log retrieval rule application details
- Alert on persistent retrieval failures
- Track carrier retrieval patterns to identify potential improvements
- Monitor the distribution of carriers across storage areas
- Track back orders for dies with no available carriers
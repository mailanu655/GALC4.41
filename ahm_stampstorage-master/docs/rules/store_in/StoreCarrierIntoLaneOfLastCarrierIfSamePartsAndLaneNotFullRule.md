# StoreCarrierIntoLaneOfLastCarrierIfSamePartsAndLaneNotFullRule Technical Documentation

## Purpose
The `StoreCarrierIntoLaneOfLastCarrierIfSamePartsAndLaneNotFullRule` class implements the `StoreInRule` interface and is responsible for determining if a carrier can be stored in the same lane as the last carrier with the same die and production run number. This rule promotes grouping of similar carriers in the same storage lane.

## Logic/Functionality
- Extends the `StoreInRuleBase` abstract class
- Implements logic to check if a carrier can be stored in the same lane as the last carrier with the same die
- Considers both the die number and production run number when making the decision
- Checks if the lane has physical space available and is not blocked
- Passes control to the next rule in the chain if this rule cannot be applied

## Flow
1. The rule receives a carrier to be stored
2. It retrieves the storage row associated with the carrier's die from the storage state context
3. It checks if the lane has physical space available and is not blocked
4. It retrieves the last carrier stored in the lane
5. It compares the die and production run number of the incoming carrier with the last stored carrier
6. If they match, the rule returns the lane for storing the carrier
7. If they don't match or any conditions fail, the rule passes control to the next rule in the chain

## Key Elements
- **computeLane()**: The main method that implements the rule logic
- **getStorageStateContext().getRow(carrier.getDie())**: Retrieves the storage row associated with the carrier's die
- **lane.isPhysicalSpaceAvailable()**: Checks if the lane has space for additional carriers
- **lane.isBlocked()**: Checks if the lane is blocked
- **lane.getCarrierAtLaneIn()**: Retrieves the last carrier stored in the lane
- **lastStoredCarrier.getProductionRunNo().equals(carrier.getProductionRunNo())**: Compares production run numbers
- **carrier.getDie().equals(lastStoredCarrier.getDie())**: Compares die numbers

## Usage
This class is part of the store-in rule chain and is typically the first rule evaluated:

```java
// Example of how this rule would be configured in a rule chain
StorageStateContext storageStateContext = new StorageStateContextImpl();
StoreInRule nextRule = new SomeOtherStoreInRule(storageStateContext, null);
StoreInRule rule = new StoreCarrierIntoLaneOfLastCarrierIfSamePartsAndLaneNotFullRule(storageStateContext, nextRule);

// The rule is used as part of a rule chain
Carrier carrier = new Carrier();
// Set carrier properties
StorageRow row = rule.processRule(carrier);
if (row != null) {
    // Store the carrier in the selected row
}
```

## Debugging and Production Support

### Common Issues
1. **Rule not firing when expected**: The rule may not select a lane when it should
2. **Incorrect lane selection**: The rule may select an inappropriate lane
3. **NullPointerException**: May occur if the carrier, die, or last stored carrier is null
4. **Production run number mismatch**: Carriers with different production runs may not be grouped correctly
5. **Lane capacity issues**: The rule may not correctly check lane capacity

### Debugging Steps
1. **For rule not firing when expected**:
   - Check if the carrier has a valid die and production run number
   - Verify that a storage row exists for the carrier's die
   - Check if the lane has physical space available and is not blocked
   - Examine the last carrier in the lane to ensure it has the same die and production run

2. **For incorrect lane selection**:
   - Review the logic in `computeLane()` to ensure it's making correct decisions
   - Verify that the storage state context is providing the correct row for the die
   - Check if the lane's properties (space available, blocked status) are correct

3. **For NullPointerException**:
   - Add null checks for the carrier, die, and last stored carrier
   - Verify that the storage state context is properly initialized
   - Check if the lane exists in the storage system

4. **For production run number mismatch**:
   - Verify that production run numbers are being correctly set on carriers
   - Check if the comparison logic is working as expected
   - Examine the actual values of production run numbers

### Resolution
1. **For rule not firing when expected**:
   - Add additional logging to track the rule's decision process
   - Enhance error handling to provide better feedback
   - Consider relaxing the matching criteria if appropriate

2. **For incorrect lane selection**:
   - Update the lane selection logic
   - Add validation to ensure correct lane selection
   - Enhance logging to track lane selection decisions

3. **For NullPointerException**:
   - Implement robust null checking
   - Add defensive programming techniques
   - Enhance error handling for null references

4. **For production run number mismatch**:
   - Review and update the production run number comparison logic
   - Add validation to ensure correct production run number handling
   - Consider adding flexibility in the matching criteria

### Monitoring
1. **Track rule application**: Monitor how often this rule is successfully applied
2. **Alert on high failure rates**: Set up alerts for when the rule frequently fails to apply
3. **Monitor lane utilization**: Track how effectively lanes are being utilized
4. **Log rule decisions**: Maintain detailed logs of rule decisions for troubleshooting
5. **Monitor carrier grouping**: Track how effectively carriers with the same die and production run are being grouped
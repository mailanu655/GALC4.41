# CarrierStatusMessage Technical Documentation

## Purpose
The `CarrierStatusMessage` class serves as a data transfer object (DTO) that encapsulates information about a carrier's status within the stamp storage system. It implements the `Message` interface and is responsible for converting raw message data into a structured `Carrier` object that can be used by the system.

## Logic/Functionality
- Stores carrier-related information such as carrier number, quantity, die number, location, destination, status, etc.
- Provides a method `getCarrier()` that constructs a `Carrier` object from the stored data
- Includes getters and setters for all carrier attributes
- Handles date formatting and parsing for production run timestamps
- Provides utility methods to retrieve location information as `Stop` objects

## Flow
1. The class receives raw data from external systems (likely PLC or MES)
2. Data is stored in string fields within the message object
3. When `getCarrier()` is called, it constructs a complete `Carrier` object by:
   - Converting string values to appropriate types (Integer, Long)
   - Looking up related objects (Die, Stop) from the database
   - Setting all carrier properties
4. The constructed `Carrier` object is then used by other components in the system for decision-making

## Key Elements
- **messageType**: Always set to `DeviceMessageType.CARRIER_STATUS`
- **getCarrier()**: The most critical method that transforms raw data into a domain object
- **getCurrentLocationAsStop()** and **getDestinationAsStop()**: Helper methods that convert location IDs to Stop objects
- **Date formatting**: Uses `SimpleDateFormat` to parse production run timestamps

## Usage
This class is primarily used in message processing components that receive carrier status updates from external systems:

```java
// Example of processing a carrier status message
CarrierStatusMessage message = new CarrierStatusMessage();
message.setCarrierNumber("1001");
message.setQuantity("50");
message.setDieNumber("123");
message.setCurrentLocation("456");
message.setDestination("789");
message.setStatus("1");
// Set other properties...

// Convert to domain object
Carrier carrier = message.getCarrier();
// Use carrier object for business logic
```

## Debugging and Production Support

### Common Issues
1. **ParseException in timestamp conversion**: The code attempts to parse production run timestamps but may fail if the format doesn't match "yyyy-mm-dd HH:mm:ss.sss"
2. **NullPointerException**: May occur if required fields are missing when constructing a Carrier
3. **NumberFormatException**: Can happen when converting string values to numeric types
4. **Invalid Stop or Die references**: If the system can't find a Stop or Die with the given ID

### Debugging Steps
1. **For timestamp parsing issues**:
   - Check the log for ParseException stack traces
   - Verify the format of incoming timestamp data
   - Ensure the SimpleDateFormat pattern matches the actual data format

2. **For NullPointerException**:
   - Add logging before each potential null reference
   - Check if carrier number, die number, or location fields are null
   - Verify that the message contains all required fields

3. **For NumberFormatException**:
   - Add logging to capture the actual string values before conversion
   - Ensure that numeric fields contain valid numbers
   - Add validation before conversion attempts

4. **For invalid references**:
   - Check if Stop.findStop() or Die.findDie() returns null
   - Verify that the IDs exist in the database
   - Add validation to handle cases where objects can't be found

### Resolution
1. **For timestamp parsing issues**:
   - Implement more robust error handling around the date parsing
   - Consider using a more flexible date parsing approach
   - Add a fallback to current date if parsing fails

2. **For null references**:
   - Add null checks before accessing potentially null objects
   - Implement defensive programming techniques
   - Return a default or empty carrier when data is incomplete

3. **For data conversion issues**:
   - Add validation and sanitization of input data
   - Implement try-catch blocks around conversion operations
   - Provide default values when conversion fails

### Monitoring
1. **Log carrier creation events**: Add logging when carriers are created from messages
2. **Track message processing metrics**: Monitor the rate and success of message processing
3. **Alert on high error rates**: Set up alerts for frequent exceptions in message processing
4. **Monitor database connectivity**: Ensure that Stop and Die lookups are functioning properly
5. **Validate data integrity**: Periodically check that carrier data is consistent and complete
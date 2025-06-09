# MesMessageBuilder Technical Documentation

## Purpose
The `MesMessageBuilder` class serves as a bridge between the StampStorage system's internal message format and the MES (Manufacturing Execution System) message format. It converts internal device messages into the GeneralMessage format required for communication with the MES system. This class is essential for ensuring proper message formatting and compatibility between the StampStorage system and the MES.

## Logic/Functionality
The class implements the following key functionality:

1. **Message Conversion**: Converts internal Message objects to GeneralMessage objects for MES communication.
2. **JSON Serialization**: Uses JSonResponseParser to serialize internal messages to JSON format.
3. **Logging**: Provides trace-level logging of built MES messages for debugging purposes.
4. **Type Safety**: Uses Java generics to ensure type safety during message conversion.

## Flow
The `MesMessageBuilder` class interacts with the messaging system in the following way:

1. An instance is created by a service or controller that needs to communicate with the MES
2. The `buildMesMessage` method is called with an internal device message and its class
3. The method uses JSonResponseParser to convert the device message to a JSON string
4. The JSON string is wrapped in a GeneralMessage object
5. The resulting GeneralMessage is returned for transmission to the MES
6. The entire process is logged at the trace level for debugging

## Key Elements
- **buildMesMessage Method**: The core method that converts internal messages to MES messages.
- **JSonResponseParser Usage**: Leverages JSonResponseParser for JSON serialization.
- **Logging**: Uses SLF4J for trace-level logging of message content.
- **GeneralMessage Creation**: Creates GeneralMessage instances with JSON content.
- **Class Parameter**: Uses a Class parameter to specify the type of message being converted.

## Usage
The `MesMessageBuilder` class is used in the following scenarios:

1. **MES Communication**: When the StampStorage system needs to send messages to the MES.
2. **Message Format Conversion**: When internal message formats need to be converted to MES-compatible formats.
3. **Service Layer Integration**: When services need to prepare messages for MES transmission.
4. **Controller Layer Integration**: When controllers need to send responses to MES requests.

Example of converting an internal message to a MES message:
```java
MesMessageBuilder builder = new MesMessageBuilder();
CarrierStatusMessage statusMessage = new CarrierStatusMessage();
statusMessage.setCarrierId(12345);
statusMessage.setStatus("ACTIVE");
GeneralMessage mesMessage = builder.buildMesMessage(statusMessage, CarrierStatusMessage.class);
// mesMessage can now be sent to the MES
```

## Debugging and Production Support

### Common Issues
1. **Message Format Incompatibility**: Issues with message formats not being compatible with MES expectations.
2. **JSON Serialization Errors**: Problems with converting complex objects to JSON.
3. **Class Type Mismatches**: Issues with incorrect class types being provided to the buildMesMessage method.
4. **Missing Message Fields**: Problems with required fields not being included in messages.
5. **Message Size Limitations**: Issues with messages exceeding size limits.
6. **Character Encoding Problems**: Issues with special characters in messages.
7. **Performance Bottlenecks**: Inefficiencies in message conversion for high-volume scenarios.

### Debugging Steps
1. **Message Format Incompatibility**:
   - Check the format of messages being sent to the MES
   - Verify that the JSON structure matches MES expectations
   - Add logging to capture the exact message format
   - Test with known good message formats
   - Compare with MES documentation

2. **JSON Serialization Errors**:
   - Check for complex objects that may cause serialization issues
   - Verify that all objects in the message are serializable
   - Add logging to capture serialization exceptions
   - Test with simplified message structures
   - Check for circular references

3. **Class Type Mismatches**:
   - Verify that the correct class type is provided to buildMesMessage
   - Check for class casting issues
   - Add logging to capture class types
   - Test with explicit class types
   - Check for inheritance issues

4. **Missing Message Fields**:
   - Verify that all required fields are set in messages
   - Check for null fields that should have values
   - Add validation for required fields
   - Test with complete message objects
   - Compare with MES field requirements

### Resolution
1. **Message Format Incompatibility**:
   - Standardize message formats according to MES requirements:
     ```java
     public GeneralMessage buildMesMessage(Message deviceMessage, Class clazz) {
         // Add validation for message format
         validateMessageFormat(deviceMessage);
         
         GeneralMessage mesMessage;
         JSonResponseParser parser = new JSonResponseParser();
         String msg = parser.toJson(deviceMessage, clazz);
         mesMessage = new GeneralMessage(msg);
         LOG.trace("Built mes message: " + mesMessage.getMessage());
         return mesMessage;
     }
     
     private void validateMessageFormat(Message deviceMessage) {
         // Implement validation logic
     }
     ```
   - Document required message formats
   - Implement format validation
   - Add error handling for format issues

2. **JSON Serialization Errors**:
   - Add error handling for serialization issues:
     ```java
     public GeneralMessage buildMesMessage(Message deviceMessage, Class clazz) {
         GeneralMessage mesMessage;
         JSonResponseParser parser = new JSonResponseParser();
         try {
             String msg = parser.toJson(deviceMessage, clazz);
             mesMessage = new GeneralMessage(msg);
             LOG.trace("Built mes message: " + mesMessage.getMessage());
             return mesMessage;
         } catch (Exception e) {
             LOG.error("Failed to serialize message: " + deviceMessage, e);
             throw new MessageBuildException("Failed to build MES message", e);
         }
     }
     ```
   - Implement custom exception types
   - Add retry logic for transient issues
   - Consider simplified message formats for problematic cases

3. **Class Type Mismatches**:
   - Add type checking and validation:
     ```java
     public <T extends Message> GeneralMessage buildMesMessage(T deviceMessage, Class<T> clazz) {
         if (!clazz.isInstance(deviceMessage)) {
             throw new IllegalArgumentException("Message type does not match class: " + 
                 deviceMessage.getClass().getName() + " is not an instance of " + clazz.getName());
         }
         
         GeneralMessage mesMessage;
         JSonResponseParser parser = new JSonResponseParser();
         String msg = parser.toJson(deviceMessage, clazz);
         mesMessage = new GeneralMessage(msg);
         LOG.trace("Built mes message: " + mesMessage.getMessage());
         return mesMessage;
     }
     ```
   - Use generics to enforce type safety
   - Add validation for class types
   - Implement error handling for type mismatches

4. **Missing Message Fields**:
   - Add validation for required fields:
     ```java
     public <T extends Message> GeneralMessage buildMesMessage(T deviceMessage, Class<T> clazz) {
         validateRequiredFields(deviceMessage);
         
         GeneralMessage mesMessage;
         JSonResponseParser parser = new JSonResponseParser();
         String msg = parser.toJson(deviceMessage, clazz);
         mesMessage = new GeneralMessage(msg);
         LOG.trace("Built mes message: " + mesMessage.getMessage());
         return mesMessage;
     }
     
     private void validateRequiredFields(Message deviceMessage) {
         // Implement validation logic for required fields
     }
     ```
   - Document required fields for each message type
   - Implement field validation
   - Add error handling for missing fields

5. **Message Size Limitations**:
   - Add size checking:
     ```java
     public <T extends Message> GeneralMessage buildMesMessage(T deviceMessage, Class<T> clazz) {
         GeneralMessage mesMessage;
         JSonResponseParser parser = new JSonResponseParser();
         String msg = parser.toJson(deviceMessage, clazz);
         
         if (msg.length() > MAX_MESSAGE_SIZE) {
             LOG.warn("Message exceeds size limit: " + msg.length() + " > " + MAX_MESSAGE_SIZE);
             // Handle oversized message
         }
         
         mesMessage = new GeneralMessage(msg);
         LOG.trace("Built mes message: " + mesMessage.getMessage());
         return mesMessage;
     }
     ```
   - Define maximum message size limits
   - Implement size checking
   - Add handling for oversized messages

6. **Character Encoding Problems**:
   - Standardize character encoding:
     ```java
     public <T extends Message> GeneralMessage buildMesMessage(T deviceMessage, Class<T> clazz) {
         GeneralMessage mesMessage;
         JSonResponseParser parser = new JSonResponseParser();
         String msg = parser.toJson(deviceMessage, clazz);
         
         // Ensure proper encoding
         msg = new String(msg.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
         
         mesMessage = new GeneralMessage(msg);
         LOG.trace("Built mes message: " + mesMessage.getMessage());
         return mesMessage;
     }
     ```
   - Specify character encoding explicitly
   - Add validation for special characters
   - Implement character escaping if needed

7. **Performance Bottlenecks**:
   - Optimize message building for high-volume scenarios:
     ```java
     public <T extends Message> GeneralMessage buildMesMessage(T deviceMessage, Class<T> clazz) {
         // Use cached parser for better performance
         JSonResponseParser parser = getParserFromCache();
         
         String msg = parser.toJson(deviceMessage, clazz);
         GeneralMessage mesMessage = new GeneralMessage(msg);
         
         // Only log if trace is enabled to avoid string concatenation overhead
         if (LOG.isTraceEnabled()) {
             LOG.trace("Built mes message: " + mesMessage.getMessage());
         }
         
         return mesMessage;
     }
     
     private JSonResponseParser getParserFromCache() {
         // Implement parser caching
     }
     ```
   - Implement parser caching
   - Optimize logging
   - Consider batch processing for multiple messages

### Monitoring
1. **Message Building Success Rate**: Monitor success rate of message building
   - Track successful vs. failed message builds
   - Alert on high failure rates
   - Monitor trends in failure rates
   - Capture sample messages that cause failures

2. **Message Size Distribution**: Monitor message size distribution
   - Track message size statistics
   - Alert on unusually large messages
   - Monitor trends in message sizes
   - Set thresholds for acceptable message sizes

3. **Message Building Performance**: Monitor performance of message building
   - Track time spent in message building operations
   - Look for performance degradation
   - Monitor CPU and memory usage during message building
   - Set thresholds for acceptable message building times

4. **Message Type Distribution**: Monitor distribution of message types
   - Track frequency of different message types
   - Look for unusual patterns in message type distribution
   - Monitor trends in message type usage
   - Alert on unexpected message types

5. **Error Patterns**: Monitor for patterns in message building errors
   - Track error types and frequencies
   - Look for correlations between errors and message types
   - Monitor trends in error rates
   - Alert on recurring error patterns

Implement logging and metrics collection for these aspects to enable proactive monitoring and issue detection.
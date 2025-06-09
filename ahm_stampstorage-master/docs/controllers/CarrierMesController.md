# CarrierMesController Technical Documentation

## Purpose
The `CarrierMesController` manages web requests related to carrier MES (Manufacturing Execution System) data in the StampStorage system. It handles CRUD operations for CarrierMes records, provides specialized views for moving carriers, and facilitates the management of carrier information as it relates to the MES system. This controller serves as a bridge between the web interface and the underlying carrier MES data, allowing users to view and manipulate carrier information in the MES context.

## Logic/Functionality
The controller implements several key functionalities:

1. **CRUD Operations**: Create, read, update, and delete operations for CarrierMes records
2. **Moving Carriers View**: Specialized view for carriers that are currently moving
3. **Pagination Support**: Handles pagination for carrier MES lists
4. **Form Handling**: Processes form submissions for carrier MES operations
5. **Date Formatting**: Formats date fields for display in the UI
6. **Carrier Conversion**: Converts between CarrierMes and Carrier domain objects

## Flow
The controller operates in the following workflow:

1. User accesses carrier MES pages through the web interface
2. Controller handles the request and retrieves appropriate carrier MES data
3. Controller prepares the model with carrier MES information
4. View renders the carrier MES information to the user
5. User may perform CRUD operations on carrier MES records
6. Controller processes these operations and updates the database accordingly

## Key Elements
- **list**: Displays a list of carrier MES records with pagination support
- **moving**: Displays a list of carriers that are currently moving
- **show**: Displays details of a specific carrier MES record
- **update/updateForm**: Updates an existing carrier MES record
- **delete**: Deletes a carrier MES record
- **addDateTimeFormatPatterns**: Adds date formatting patterns to the model
- **getCarrier**: Converts a CarrierMes object to a Carrier domain object
- **pause**: Utility method to pause execution for a specified time

## Usage
The `CarrierMesController` is accessed through the following URL patterns:

1. **List Carrier MES**: `/carriermes` (GET)
   - Displays a list of carrier MES records
   - Supports pagination with `page` and `size` parameters

2. **List Moving Carriers**: `/carriermes?moving` (GET)
   - Displays a list of carriers that are currently moving

3. **Show Carrier MES**: `/carriermes/{id}` (GET)
   - Displays details of a specific carrier MES record
   - Requires carrier MES ID as a path variable

4. **Create Carrier MES**: `/carriermes?form` (GET), `/carriermes` (POST)
   - GET request displays the creation form
   - POST request processes the form submission and creates the record

5. **Update Carrier MES**: `/carriermes/{id}?form` (GET), `/carriermes` (PUT)
   - GET request displays the update form for a specific record
   - PUT request processes the form submission and updates the record

6. **Delete Carrier MES**: `/carriermes/{id}` (DELETE)
   - Deletes a specific carrier MES record
   - Requires carrier MES ID as a path variable

## Database Tables
The `CarrierMesController` interacts with the following database tables:

1. **CARRIER_MES**: Stores carrier MES records including:
   - ID: Primary key
   - CARRIER_NUMBER: The carrier number
   - DIE_NUMBER: The die number
   - QUANTITY: The quantity of parts
   - CURRENT_LOCATION: The current location of the carrier
   - DESTINATION: The destination of the carrier
   - ORIGINATION_LOCATION: The origination location of the carrier
   - STATUS: The status of the carrier
   - BUFFER: Buffer status
   - PRODUCTION_RUN_NUMBER: The production run number
   - PRODUCTION_RUN_DATE: The production run date
   - MAINTENANCE_BITS: Maintenance bits for the carrier

## Debugging and Production Support

### Common Issues
1. **Binding Errors**: Problems with binding form data to CarrierMes objects
2. **Pagination Problems**: Issues with pagination of carrier MES lists
3. **Date Formatting Errors**: Problems with date formatting for display
4. **Carrier Conversion Errors**: Issues with converting between CarrierMes and Carrier objects
5. **Moving Carriers Query Performance**: Performance issues with the moving carriers query
6. **Null Reference Exceptions**: Null references in carrier MES data
7. **Concurrent Modification Issues**: Problems with concurrent modifications to carrier MES records

### Debugging Steps
1. **Binding Errors**:
   - Check the form submission data
   - Verify the CarrierMes object structure
   - Check for missing or invalid form fields
   - Review logs for binding-related errors
   - Test with valid form submissions

2. **Pagination Problems**:
   - Check the pagination parameters in the `list` method
   - Verify the calculation of page numbers and sizes
   - Check for off-by-one errors in pagination
   - Review logs for pagination-related errors
   - Test with different page and size values

3. **Date Formatting Errors**:
   - Check the implementation of `addDateTimeFormatPatterns` method
   - Verify the date format patterns
   - Check for locale-specific issues
   - Review logs for date formatting errors
   - Test with different date values and locales

4. **Carrier Conversion Errors**:
   - Check the implementation of `getCarrier` method
   - Verify the mapping between CarrierMes and Carrier fields
   - Check for missing or null fields during conversion
   - Review logs for conversion errors
   - Test with different CarrierMes objects

5. **Moving Carriers Query Performance**:
   - Check the implementation of `findMovingCarriers` method
   - Verify the query for moving carriers
   - Check for missing indexes on relevant fields
   - Review logs for query performance issues
   - Test with different numbers of moving carriers

### Resolution
1. **Binding Errors**:
   - Fix form binding logic in create and update methods
   - Update CarrierMes object structure if needed
   - Add validation for form fields
   - Enhance logging for binding operations
   - Add user-friendly error messages for binding failures

2. **Pagination Problems**:
   - Fix pagination logic in the `list` method
   - Correct page number and size calculations
   - Fix off-by-one errors in pagination
   - Enhance logging for pagination operations
   - Add validation for pagination parameters

3. **Date Formatting Errors**:
   - Fix date formatting logic in `addDateTimeFormatPatterns`
   - Update date format patterns if needed
   - Add locale-specific handling
   - Enhance logging for date formatting operations
   - Add fallback date formats for error cases

4. **Carrier Conversion Errors**:
   - Fix conversion logic in `getCarrier`
   - Update field mapping between CarrierMes and Carrier
   - Add null checks for fields during conversion
   - Enhance logging for conversion operations
   - Add error handling for conversion failures

5. **Moving Carriers Query Performance**:
   - Optimize the query in `findMovingCarriers`
   - Add indexes on relevant fields
   - Implement query caching if appropriate
   - Enhance logging for query performance
   - Add pagination for large result sets

6. **Null Reference Exceptions**:
   - Add null checks in carrier MES data handling
   - Implement default values for null fields
   - Enhance logging for null reference detection
   - Add error handling for null reference scenarios
   - Implement data validation before processing

7. **Concurrent Modification Issues**:
   - Implement optimistic locking for carrier MES records
   - Add version fields for concurrency control
   - Enhance logging for concurrent modification detection
   - Add error handling for concurrent modification scenarios
   - Implement retry mechanisms for failed operations

### Monitoring
1. **CRUD Operations**: Monitor CRUD operation performance and success rates
   - Log CRUD operations with carrier numbers
   - Track operation response times
   - Alert on slow operations
   - Monitor operation patterns and frequencies

2. **Moving Carriers Query**: Monitor moving carriers query performance
   - Log query execution times
   - Track query result sizes
   - Alert on slow queries
   - Monitor query patterns and frequencies

3. **Pagination Usage**: Monitor pagination usage patterns
   - Log pagination parameters and result counts
   - Track pagination navigation patterns
   - Alert on unusual pagination patterns
   - Monitor pagination performance

4. **Date Formatting**: Monitor date formatting operations
   - Log date formatting with patterns and locales
   - Track formatting errors
   - Alert on high formatting error rates
   - Monitor formatting patterns

5. **Carrier Conversion**: Monitor carrier conversion operations
   - Log conversion operations with carrier numbers
   - Track conversion errors
   - Alert on high conversion error rates
   - Monitor conversion patterns

6. **Error Rates**: Monitor error rates for carrier MES operations
   - Log errors with details
   - Track error frequencies and patterns
   - Alert on high error rates
   - Monitor error resolution times

7. **User Activity**: Monitor user activity patterns
   - Log user actions on carrier MES
   - Track user session durations
   - Alert on unusual user activity
   - Monitor user access patterns

Implement logging and metrics collection for these aspects to enable proactive monitoring and issue detection.
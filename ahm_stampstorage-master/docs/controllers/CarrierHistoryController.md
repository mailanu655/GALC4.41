# CarrierHistoryController Technical Documentation

## Purpose
The `CarrierHistoryController` manages web requests related to carrier history in the StampStorage system. It handles CRUD operations for carrier history records, provides search functionality based on various criteria, and manages the display of carrier history information in the web interface. This controller is essential for tracking and analyzing the historical movement and status changes of carriers throughout the system.

## Logic/Functionality
The controller implements several key functionalities:

1. **CRUD Operations**: Create, read, update, and delete operations for carrier history records
2. **Search Functionality**: Search for carrier history records based on criteria such as carrier number
3. **Cookie Management**: Stores and retrieves search criteria in cookies for persistent user experience
4. **Pagination Support**: Handles pagination for carrier history lists
5. **Form Handling**: Processes form submissions for carrier history operations

## Flow
The controller operates in the following workflow:

1. User accesses carrier history pages through the web interface
2. Controller handles the request and retrieves appropriate carrier history data
3. Controller prepares the model with carrier history information
4. View renders the carrier history information to the user
5. User may perform search operations or CRUD operations on carrier history records
6. Controller processes these operations and updates the database accordingly

## Key Elements
- **list**: Displays a list of carrier history records, optionally filtered by criteria
- **show**: Displays details of a specific carrier history record
- **create/createForm**: Creates a new carrier history record
- **update/updateForm**: Updates an existing carrier history record
- **delete**: Deletes a carrier history record
- **findCarrierHistoryByCriteria**: Searches for carrier history records based on criteria
- **getSavedCarrierHistoryFinderCriteria**: Retrieves saved search criteria from cookies
- **saveCarrierHistoryFinderCriteria**: Saves search criteria to cookies

## Usage
The `CarrierHistoryController` is accessed through the following URL patterns:

1. **List Carrier History**: `/carrierhistories` (GET)
   - Displays a list of carrier history records
   - Supports pagination with `page` and `size` parameters
   - May use saved search criteria from cookies

2. **Show Carrier History**: `/carrierhistories/{id}` (GET)
   - Displays details of a specific carrier history record
   - Requires carrier history ID as a path variable

3. **Create Carrier History**: `/carrierhistories?form` (GET), `/carrierhistories` (POST)
   - GET request displays the creation form
   - POST request processes the form submission and creates the record

4. **Update Carrier History**: `/carrierhistories/{id}?form` (GET), `/carrierhistories` (PUT)
   - GET request displays the update form for a specific record
   - PUT request processes the form submission and updates the record

5. **Delete Carrier History**: `/carrierhistories/{id}` (DELETE)
   - Deletes a specific carrier history record
   - Requires carrier history ID as a path variable

6. **Search Carrier History**: `/carrierhistories?find=ByCarrierNumber` (GET)
   - Searches for carrier history records by carrier number
   - Saves search criteria to cookies for future use

## Database Tables
The `CarrierHistoryController` interacts with the following database tables:

1. **CARRIER_HISTORY**: Stores carrier history records including:
   - ID: Primary key
   - CARRIER_NUMBER: The carrier number
   - CURRENT_LOCATION: The current location of the carrier
   - DESTINATION: The destination of the carrier
   - BUFFER: Buffer status
   - TIMESTAMP: Timestamp of the history record
   - SOURCE: Source of the history record

## Debugging and Production Support

### Common Issues
1. **Search Criteria Cookie Issues**: Problems with storing or retrieving search criteria from cookies
2. **Pagination Problems**: Issues with pagination of carrier history lists
3. **Form Validation Errors**: Problems with form validation for carrier history operations
4. **Database Query Performance**: Performance issues with carrier history queries
5. **Null Reference Exceptions**: Null references in carrier history data
6. **Cookie Size Limitations**: Exceeding cookie size limitations with search criteria
7. **Concurrent Modification Issues**: Problems with concurrent modifications to carrier history records

### Debugging Steps
1. **Search Criteria Cookie Issues**:
   - Check the implementation of `getSavedCarrierHistoryFinderCriteria` and `saveCarrierHistoryFinderCriteria` methods
   - Verify cookie names and values
   - Check for cookie expiration issues
   - Review logs for cookie-related errors
   - Test with known cookie values

2. **Pagination Problems**:
   - Check the pagination parameters in the `list` method
   - Verify the calculation of page numbers and sizes
   - Check for off-by-one errors in pagination
   - Review logs for pagination-related errors
   - Test with different page and size values

3. **Form Validation Errors**:
   - Check the form validation logic in create and update methods
   - Verify the binding result handling
   - Check for missing validation annotations
   - Review logs for validation-related errors
   - Test with valid and invalid form submissions

4. **Database Query Performance**:
   - Check the query implementation in the `findCarrierHistoryByCarrierNumber` method
   - Verify the use of pagination in database queries
   - Check for missing indexes on search fields
   - Review logs for query performance issues
   - Test with different search criteria and result sizes

### Resolution
1. **Search Criteria Cookie Issues**:
   - Fix cookie handling logic in `getSavedCarrierHistoryFinderCriteria` and `saveCarrierHistoryFinderCriteria`
   - Update cookie names and values if needed
   - Implement proper cookie expiration handling
   - Enhance logging for cookie operations
   - Add error handling for cookie-related issues

2. **Pagination Problems**:
   - Fix pagination logic in the `list` method
   - Correct page number and size calculations
   - Fix off-by-one errors in pagination
   - Enhance logging for pagination operations
   - Add validation for pagination parameters

3. **Form Validation Errors**:
   - Fix form validation logic in create and update methods
   - Improve binding result handling
   - Add missing validation annotations
   - Enhance logging for validation operations
   - Add user-friendly error messages for validation failures

4. **Database Query Performance**:
   - Optimize query implementation in the `findCarrierHistoryByCarrierNumber` method
   - Ensure proper use of pagination in database queries
   - Add missing indexes on search fields
   - Enhance logging for query performance
   - Implement query caching if appropriate

5. **Null Reference Exceptions**:
   - Add null checks in carrier history data handling
   - Implement default values for null fields
   - Enhance logging for null reference detection
   - Add error handling for null reference scenarios
   - Implement data validation before processing

6. **Cookie Size Limitations**:
   - Optimize search criteria storage in cookies
   - Implement cookie size checking
   - Split large criteria into multiple cookies if needed
   - Enhance logging for cookie size issues
   - Add fallback mechanisms for oversized cookies

7. **Concurrent Modification Issues**:
   - Implement optimistic locking for carrier history records
   - Add version fields for concurrency control
   - Enhance logging for concurrent modification detection
   - Add error handling for concurrent modification scenarios
   - Implement retry mechanisms for failed operations

### Monitoring
1. **Search Operations**: Monitor search operation performance and success rates
   - Log search operations with criteria and result counts
   - Track search operation response times
   - Alert on slow search operations
   - Monitor search criteria patterns

2. **Pagination Usage**: Monitor pagination usage patterns
   - Log pagination parameters and result counts
   - Track pagination navigation patterns
   - Alert on unusual pagination patterns
   - Monitor pagination performance

3. **Form Submissions**: Monitor form submission success rates
   - Log form submissions with validation results
   - Track form submission errors
   - Alert on high form submission error rates
   - Monitor form submission patterns

4. **Database Query Performance**: Monitor database query performance
   - Log query execution times
   - Track query result sizes
   - Alert on slow queries
   - Monitor query patterns and frequencies

5. **Cookie Usage**: Monitor cookie usage patterns
   - Log cookie operations with sizes
   - Track cookie expiration and renewal
   - Alert on cookie size issues
   - Monitor cookie usage patterns

6. **Error Rates**: Monitor error rates for carrier history operations
   - Log errors with details
   - Track error frequencies and patterns
   - Alert on high error rates
   - Monitor error resolution times

7. **User Activity**: Monitor user activity patterns
   - Log user actions on carrier history
   - Track user session durations
   - Alert on unusual user activity
   - Monitor user access patterns

Implement logging and metrics collection for these aspects to enable proactive monitoring and issue detection.
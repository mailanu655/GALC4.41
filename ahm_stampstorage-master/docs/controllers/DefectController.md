# DefectController Technical Documentation

## Purpose
The `DefectController` manages web requests related to defect tracking in the StampStorage system. It handles CRUD operations for Defect records, which represent quality issues identified in carriers or parts. This controller is essential for quality control processes, allowing users to record, track, and manage defects throughout the manufacturing process.

## Logic/Functionality
The controller implements several key functionalities:

1. **CRUD Operations**: Create, read, update, and delete operations for Defect records
2. **Pagination Support**: Handles pagination for defect lists
3. **Form Handling**: Processes form submissions for defect operations
4. **Inspection Integration**: Special handling for defects identified during inspection
5. **Image Display**: Displays die images for visual reference during defect recording
6. **Date Formatting**: Formats date fields for display in the UI
7. **Redirection Logic**: Intelligent redirection based on carrier location and inspection context

## Flow
The controller operates in the following workflow:

1. User accesses defect pages through the web interface, often from carrier inspection screens
2. Controller handles the request and retrieves appropriate defect and carrier data
3. Controller prepares the model with defect information and related data (e.g., die images)
4. View renders the defect information to the user
5. User may perform CRUD operations on defect records
6. Controller processes these operations and updates the database accordingly
7. Controller redirects the user back to the appropriate screen based on context

## Key Elements
- **create/createForm**: Creates a new defect record, with special handling for inspection context
- **createDefectFormForInspection**: Creates a defect form specifically for inspection context
- **show**: Displays details of a specific defect record
- **list**: Displays a list of defect records with pagination support
- **update/updateForm**: Updates an existing defect record
- **delete**: Deletes a defect record
- **addDateTimeFormatPatterns**: Adds date formatting patterns to the model
- **populateDEFECT_TYPEs**: Populates the model with defect types
- **populateREWORK_METHODs**: Populates the model with rework methods

## Usage
The `DefectController` is accessed through the following URL patterns:

1. **List Defects**: `/defects` (GET)
   - Displays a list of defect records
   - Supports pagination with `page` and `size` parameters

2. **Show Defect**: `/defects/{id}` (GET)
   - Displays details of a specific defect record
   - Requires defect ID as a path variable

3. **Create Defect**: `/defects?form` (GET), `/defects` (POST)
   - GET request displays the creation form
   - POST request processes the form submission and creates the record
   - Special parameter `detailedinventory` controls redirection after creation

4. **Create Defect for Inspection**: `/defects?create=/{carriernumber}/{detailedinventory}` (GET)
   - Displays a defect creation form for a specific carrier during inspection
   - Includes die image and carrier information

5. **Update Defect**: `/defects/{id}/{detailedinventory}?form` (GET), `/defects` (PUT)
   - GET request displays the update form for a specific record
   - PUT request processes the form submission and updates the record
   - Parameter `detailedinventory` controls redirection after update

6. **Delete Defect**: `/defects/{id}/{detailedinventory}` (DELETE)
   - Deletes a specific defect record
   - Requires defect ID as a path variable
   - Parameter `detailedinventory` controls redirection after deletion

## Database Tables
The `DefectController` interacts with the following database tables:

1. **DEFECT**: Stores defect records including:
   - ID: Primary key
   - CARRIER_NUMBER: The carrier number associated with the defect
   - PRODUCTION_RUN_NO: The production run number
   - DEFECT_TYPE: The type of defect (enum value)
   - REWORK_METHOD: The method used for rework (enum value)
   - X_AREA: X coordinate of the defect area
   - Y_AREA: Y coordinate of the defect area
   - DEFECT_TIMESTAMP: Timestamp when the defect was recorded
   - SOURCE: Source of the defect record (typically user ID)
   - NOTE: Additional notes about the defect

2. **CARRIER_MES**: Referenced to find carrier information
   - CARRIER_NUMBER: The carrier number (used to link with DEFECT.CARRIER_NUMBER)
   - CURRENT_LOCATION: The current location of the carrier
   - DIE_NUMBER: The die number associated with the carrier
   - PRODUCTION_RUN_NUMBER: The production run number

3. **DIE**: Referenced for die information and images
   - ID: Primary key (referenced by CARRIER_MES.DIE_NUMBER)
   - DESCRIPTION: Description of the die
   - IMAGE_FILE_NAME: Filename of the die image

## Debugging and Production Support

### Common Issues
1. **Image Display Issues**: Problems with displaying die images for defect recording
2. **Redirection Logic Failures**: Issues with redirection after defect operations
3. **Form Validation Errors**: Problems with form validation for defect operations
4. **Carrier Not Found**: Issues with finding carriers by carrier number
5. **Date Formatting Errors**: Problems with date formatting for display
6. **Empty Notes for "OTHER" Defect Types**: Missing notes for defects of type "OTHER"
7. **Concurrent Modification Issues**: Problems with concurrent modifications to defect records

### Debugging Steps
1. **Image Display Issues**:
   - Check the image path construction in the controller
   - Verify the die image filename retrieval
   - Check for missing or invalid image files
   - Review logs for image-related errors
   - Test with known valid die images

2. **Redirection Logic Failures**:
   - Check the redirection logic in create, update, and delete methods
   - Verify the handling of the `detailedinventory` parameter
   - Check for missing or invalid location information
   - Review logs for redirection-related errors
   - Test with different carrier locations and detailed inventory settings

3. **Form Validation Errors**:
   - Check the form validation logic in create and update methods
   - Verify the binding result handling
   - Check for missing validation annotations
   - Review logs for validation-related errors
   - Test with valid and invalid form submissions

4. **Carrier Not Found**:
   - Check the carrier lookup logic
   - Verify the handling of invalid carrier numbers
   - Check for error handling when carriers are not found
   - Review logs for carrier lookup errors
   - Test with valid and invalid carrier numbers

5. **Date Formatting Errors**:
   - Check the implementation of `addDateTimeFormatPatterns` method
   - Verify the date format patterns
   - Check for locale-specific issues
   - Review logs for date formatting errors
   - Test with different date values and locales

### Resolution
1. **Image Display Issues**:
   - Fix image path construction in the controller
   - Improve die image filename retrieval
   - Add fallback images for missing or invalid files
   - Enhance logging for image operations
   - Add user-friendly messages for image display failures

2. **Redirection Logic Failures**:
   - Fix redirection logic in create, update, and delete methods
   - Improve handling of the `detailedinventory` parameter
   - Add validation for location information
   - Enhance logging for redirection operations
   - Add fallback redirection for error cases

3. **Form Validation Errors**:
   - Fix form validation logic in create and update methods
   - Improve binding result handling
   - Add missing validation annotations
   - Enhance logging for validation operations
   - Add user-friendly error messages for validation failures

4. **Carrier Not Found**:
   - Improve carrier lookup logic
   - Add validation for carrier numbers
   - Implement user-friendly error messages for missing carriers
   - Enhance logging for carrier lookup operations
   - Add suggestions for similar carrier numbers

5. **Date Formatting Errors**:
   - Fix date formatting logic in `addDateTimeFormatPatterns`
   - Update date format patterns if needed
   - Add locale-specific handling
   - Enhance logging for date formatting operations
   - Add fallback date formats for error cases

6. **Empty Notes for "OTHER" Defect Types**:
   - Add validation for notes when defect type is "OTHER"
   - Implement user-friendly error messages for missing notes
   - Enhance logging for note validation
   - Add note suggestions for common "OTHER" defect scenarios
   - Implement note templates for different defect types

7. **Concurrent Modification Issues**:
   - Implement optimistic locking for defect records
   - Add version fields for concurrency control
   - Enhance logging for concurrent modification detection
   - Add error handling for concurrent modification scenarios
   - Implement retry mechanisms for failed operations

### Monitoring
1. **Defect Creation**: Monitor defect creation operations
   - Log defect creation with carrier numbers and defect types
   - Track defect creation response times
   - Alert on high defect creation rates
   - Monitor defect creation patterns by user and location

2. **Image Display**: Monitor image display operations
   - Log image display with die IDs and image paths
   - Track image display failures
   - Alert on high image display failure rates
   - Monitor image access patterns

3. **Redirection**: Monitor redirection operations
   - Log redirections with source and destination URLs
   - Track redirection failures
   - Alert on redirection loops or failures
   - Monitor redirection patterns

4. **Form Submissions**: Monitor form submission success rates
   - Log form submissions with validation results
   - Track form submission errors
   - Alert on high form submission error rates
   - Monitor form submission patterns

5. **Carrier Lookup**: Monitor carrier lookup operations
   - Log carrier lookups with carrier numbers
   - Track carrier lookup failures
   - Alert on high carrier lookup failure rates
   - Monitor carrier lookup patterns

6. **Error Rates**: Monitor error rates for defect operations
   - Log errors with details
   - Track error frequencies and patterns
   - Alert on high error rates
   - Monitor error resolution times

7. **User Activity**: Monitor user activity patterns
   - Log user actions on defects
   - Track user session durations
   - Alert on unusual user activity
   - Monitor user access patterns

Implement logging and metrics collection for these aspects to enable proactive monitoring and issue detection.
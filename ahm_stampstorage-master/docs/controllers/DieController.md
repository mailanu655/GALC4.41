# DieController Technical Documentation

## Purpose
The `DieController` manages web requests related to die information in the StampStorage system. It handles CRUD operations for Die records, which represent the stamping dies used in the manufacturing process. This controller is essential for maintaining the die database, which is a critical component for carrier tracking, defect recording, and production planning.

## Logic/Functionality
The controller implements several key functionalities:

1. **CRUD Operations**: Create, read, update, and delete operations for Die records
2. **Pagination Support**: Handles pagination for die lists
3. **Form Handling**: Processes form submissions for die operations
4. **Exception Handling**: Handles JPA system exceptions, particularly for duplicate die IDs
5. **User Tracking**: Logs user actions for auditing purposes
6. **Model Population**: Populates the model with die data and part production volumes

## Flow
The controller operates in the following workflow:

1. User accesses die pages through the web interface
2. Controller handles the request and retrieves appropriate die data
3. Controller prepares the model with die information
4. View renders the die information to the user
5. User may perform CRUD operations on die records
6. Controller processes these operations, logs the user action, and updates the database accordingly
7. In case of exceptions (e.g., duplicate die IDs), controller redirects to an exception page

## Key Elements
- **create/createForm**: Creates a new die record with exception handling for duplicates
- **show**: Displays details of a specific die record
- **list**: Displays a list of die records with pagination support
- **update/updateForm**: Updates an existing die record
- **delete**: Deletes a die record
- **populateDies**: Populates the model with die data
- **populatePartProductionVolumes**: Populates the model with part production volume options

## Usage
The `DieController` is accessed through the following URL patterns:

1. **List Dies**: `/dies` (GET)
   - Displays a list of die records
   - Supports pagination with `page` and `size` parameters

2. **Show Die**: `/dies/{id}` (GET)
   - Displays details of a specific die record
   - Requires die ID as a path variable

3. **Create Die**: `/dies?form` (GET), `/dies` (POST)
   - GET request displays the creation form
   - POST request processes the form submission and creates the record
   - Handles exceptions for duplicate die IDs

4. **Update Die**: `/dies/{id}?form` (GET), `/dies` (PUT)
   - GET request displays the update form for a specific record
   - PUT request processes the form submission and updates the record

5. **Delete Die**: `/dies/{id}` (DELETE)
   - Deletes a specific die record
   - Requires die ID as a path variable
   - Logs the user who performed the deletion

6. **Die Exception**: `/dies/exception` (GET)
   - Displays an exception page for die operations
   - Used primarily for duplicate die ID errors

## Database Tables
The `DieController` interacts with the following database tables:

1. **DIE**: Stores die records including:
   - ID: Primary key
   - DESCRIPTION: Description of the die
   - PART_PRODUCTION_VOLUME: Enum value indicating production volume (HIGH_VOLUME, MEDIUM_VOLUME, LOW_VOLUME)
   - BACKGROUND_COLOR: Background color for UI display
   - TEXT_COLOR: Text color for UI display
   - IMAGE_FILE_NAME: Filename of the die image
   - ACTIVE: Flag indicating if the die is active

2. **CARRIER_MES**: Referenced by carriers that use the die
   - DIE_NUMBER: Foreign key referencing DIE.ID

3. **MODEL**: Referenced by models that use the die
   - LEFT_DIE_ID: Foreign key referencing DIE.ID
   - RIGHT_DIE_ID: Foreign key referencing DIE.ID

## Debugging and Production Support

### Common Issues
1. **Duplicate Die ID Exceptions**: Problems with creating dies with duplicate IDs
2. **Image File Handling**: Issues with die image filenames
3. **Form Validation Errors**: Problems with form validation for die operations
4. **Pagination Problems**: Issues with pagination of die lists
5. **User Tracking Failures**: Problems with logging user actions
6. **Referential Integrity Violations**: Issues with deleting dies that are referenced by carriers or models
7. **Color Format Validation**: Problems with validating color formats

### Debugging Steps
1. **Duplicate Die ID Exceptions**:
   - Check the exception handling in the `create` method
   - Verify the redirection to the exception page
   - Check for proper error messages
   - Review logs for duplicate ID errors
   - Test with known duplicate IDs

2. **Image File Handling**:
   - Check the handling of image filenames
   - Verify the storage and retrieval of image files
   - Check for missing or invalid image files
   - Review logs for image-related errors
   - Test with different image filenames

3. **Form Validation Errors**:
   - Check the form validation logic in create and update methods
   - Verify the binding result handling
   - Check for missing validation annotations
   - Review logs for validation-related errors
   - Test with valid and invalid form submissions

4. **Pagination Problems**:
   - Check the pagination parameters in the `list` method
   - Verify the calculation of page numbers and sizes
   - Check for off-by-one errors in pagination
   - Review logs for pagination-related errors
   - Test with different page and size values

5. **User Tracking Failures**:
   - Check the user tracking logic in create, update, and delete methods
   - Verify the retrieval of user information from the request
   - Check for logging of user actions
   - Review logs for user tracking errors
   - Test with different user contexts

### Resolution
1. **Duplicate Die ID Exceptions**:
   - Improve exception handling in the `create` method
   - Enhance the exception page with more details
   - Add validation for die IDs before submission
   - Enhance logging for duplicate ID errors
   - Implement suggestions for alternative IDs

2. **Image File Handling**:
   - Improve handling of image filenames
   - Add validation for image file existence
   - Implement default images for missing files
   - Enhance logging for image operations
   - Add image upload functionality if needed

3. **Form Validation Errors**:
   - Fix form validation logic in create and update methods
   - Improve binding result handling
   - Add missing validation annotations
   - Enhance logging for validation operations
   - Add user-friendly error messages for validation failures

4. **Pagination Problems**:
   - Fix pagination logic in the `list` method
   - Correct page number and size calculations
   - Fix off-by-one errors in pagination
   - Enhance logging for pagination operations
   - Add validation for pagination parameters

5. **User Tracking Failures**:
   - Improve user tracking logic in create, update, and delete methods
   - Enhance user information retrieval from the request
   - Add fallback mechanisms for missing user information
   - Enhance logging for user tracking operations
   - Implement audit trail functionality

6. **Referential Integrity Violations**:
   - Add checks for references before deleting dies
   - Implement user-friendly error messages for reference violations
   - Add options for handling references (e.g., cascade, restrict)
   - Enhance logging for reference violation detection
   - Implement reference cleanup mechanisms

7. **Color Format Validation**:
   - Add validation for color formats
   - Implement color pickers in the UI
   - Add default colors for invalid formats
   - Enhance logging for color validation
   - Implement color suggestions for common use cases

### Monitoring
1. **Die Operations**: Monitor die operation performance and success rates
   - Log die operations with die IDs
   - Track operation response times
   - Alert on slow operations
   - Monitor operation patterns and frequencies

2. **Exception Rates**: Monitor exception rates for die operations
   - Log exceptions with details
   - Track exception frequencies and patterns
   - Alert on high exception rates
   - Monitor exception resolution times

3. **User Actions**: Monitor user actions on dies
   - Log user actions with user IDs and die IDs
   - Track user action patterns
   - Alert on unusual user actions
   - Monitor user action frequencies

4. **Pagination Usage**: Monitor pagination usage patterns
   - Log pagination parameters and result counts
   - Track pagination navigation patterns
   - Alert on unusual pagination patterns
   - Monitor pagination performance

5. **Form Submissions**: Monitor form submission success rates
   - Log form submissions with validation results
   - Track form submission errors
   - Alert on high form submission error rates
   - Monitor form submission patterns

6. **Reference Integrity**: Monitor reference integrity for dies
   - Log reference checks with die IDs
   - Track reference violation attempts
   - Alert on reference violation patterns
   - Monitor reference cleanup operations

7. **Image Usage**: Monitor image usage for dies
   - Log image access with die IDs
   - Track image access patterns
   - Alert on missing or invalid images
   - Monitor image update operations

Implement logging and metrics collection for these aspects to enable proactive monitoring and issue detection.
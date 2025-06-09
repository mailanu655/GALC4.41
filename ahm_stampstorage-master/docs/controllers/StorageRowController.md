# StorageRowController Technical Documentation

## Purpose
The `StorageRowController` class manages the StorageRow entities in the StampStorage system. It provides functionality for creating, reading, updating, and deleting StorageRow records, which represent physical storage rows in the conveyor system. This controller is essential for configuring the storage layout of the system, enabling administrators to define and manage the storage rows that carriers can be stored in.

## Logic/Functionality
The class implements the following key functionality:

1. **StorageRow Creation**: Allows administrators to create new StorageRow records with details such as row name, capacity, and storage area.
2. **StorageRow Retrieval**: Provides methods to retrieve individual StorageRow records or lists of StorageRows.
3. **StorageRow Update**: Enables administrators to update existing StorageRow records, with special handling to preserve certain fields.
4. **StorageRow Deletion**: Allows administrators to delete StorageRow records.
5. **StorageRow Population**: Provides methods to populate StorageRow data for use in forms and views.
6. **Row Update Notification**: Notifies the carrier management service when a row is updated to ensure the system's storage state is consistent.

## Flow
The `StorageRowController` class interacts with the system in the following way:

1. **StorageRow Creation**:
   - User accesses the StorageRow creation form via the `/storagerows?form` endpoint (GET)
   - Controller displays the form for entering StorageRow data
   - User submits the form with StorageRow data via the `/storagerows` endpoint (POST)
   - Controller validates the data
   - Controller creates a new StorageRow record
   - Controller redirects the user to the newly created StorageRow's detail page

2. **StorageRow Retrieval**:
   - User accesses a specific StorageRow via the `/storagerows/{id}` endpoint (GET)
   - Controller retrieves the StorageRow from the database
   - Controller displays the StorageRow details to the user

   - User accesses the list of StorageRows via the `/storagerows` endpoint (GET)
   - Controller retrieves StorageRows from the database, with pagination if specified
   - Controller displays the list of StorageRows to the user

3. **StorageRow Update**:
   - User accesses the StorageRow update form via the `/storagerows/{id}?form` endpoint (GET)
   - Controller retrieves the StorageRow from the database
   - Controller displays the form with the StorageRow data
   - User submits the form with updated StorageRow data via the `/storagerows` endpoint (PUT)
   - Controller validates the data
   - Controller updates the StorageRow record, preserving certain fields
   - Controller notifies the carrier management service of the row update
   - Controller redirects the user to the updated StorageRow's detail page

4. **StorageRow Deletion**:
   - User submits a delete request via the `/storagerows/{id}` endpoint (DELETE)
   - Controller deletes the StorageRow from the database
   - Controller redirects the user to the StorageRow list page

## Key Elements
- **StorageRow**: A domain class that represents a physical storage row in the conveyor system, containing properties such as row name, capacity, and storage area.
- **Stop**: A domain class that represents a physical location in the conveyor system, referenced by the StorageRow class.
- **StorageArea**: An enumeration that represents the area of a storage row, such as C_HIGH or A_AREA.
- **StopAvailability**: An enumeration that represents the availability of a stop, such as AVAILABLE or UNAVAILABLE.
- **CarrierManagementServiceProxy**: An autowired service that provides methods for managing carriers and storage, including updating rows.
- **Validation**: The controller validates StorageRow data to ensure it meets the required format and constraints.
- **Pagination**: The controller supports pagination for the StorageRow list to handle large numbers of StorageRows efficiently.
- **Dependencies**: The controller checks for dependencies, such as the existence of stops, before allowing StorageRow creation.

## Usage
The `StorageRowController` class is used in the following scenarios:

1. **Storage Configuration**: Administrators can create, view, update, and delete StorageRows to configure the storage layout of the conveyor system.
2. **Capacity Management**: Administrators can define the capacity of StorageRows to control how many carriers can be stored in each row.
3. **Area Organization**: Administrators can organize StorageRows into areas to facilitate efficient storage and retrieval of carriers.

Example URL patterns:
- `/storagerows?form` (GET) - Display the StorageRow creation form
- `/storagerows` (POST) - Create a new StorageRow
- `/storagerows/{id}` (GET) - Display a specific StorageRow
- `/storagerows` (GET) - Display the list of StorageRows
- `/storagerows/{id}?form` (GET) - Display the StorageRow update form
- `/storagerows` (PUT) - Update an existing StorageRow
- `/storagerows/{id}` (DELETE) - Delete a StorageRow

## Database Tables
The `StorageRowController` interacts with the following database tables:

1. **STORAGE_ROW**: Stores StorageRow information with the following columns:
   - **ID**: The unique identifier for the StorageRow (primary key)
   - **VERSION**: Version number for optimistic locking
   - **ROW_NAME**: The name of the storage row
   - **CAPACITY**: The capacity of the storage row
   - **STORAGE_AREA**: The area of the storage row (enumeration value)
   - **STOP_ID**: The ID of the stop associated with the storage row (foreign key to STOP)

2. **STOP**: Referenced by the STORAGE_ROW table, stores stop information with the following columns:
   - **ID**: The unique identifier for the stop (primary key)
   - **VERSION**: Version number for optimistic locking
   - **NAME**: The name of the stop
   - **STOP_TYPE**: The type of the stop (enumeration value)
   - **STOP_AREA**: The area of the stop (enumeration value)
   - **STOP_AVAILABILITY**: The availability of the stop (enumeration value)
   - **CAPACITY**: The capacity of the stop

The controller primarily reads from and writes to the STORAGE_ROW table, and reads from the STOP table.

## Debugging and Production Support

### Common Issues

1. **Stop Dependency Issues**:
   - **Symptom**: StorageRow creation fails due to missing stop dependencies.
   - **Cause**: The stops referenced by the StorageRow do not exist in the database.
   - **Impact**: Administrators cannot create StorageRows with non-existent stops, potentially affecting storage configuration.

2. **Row Update Notification Issues**:
   - **Symptom**: Row updates do not propagate to the carrier management service.
   - **Cause**: Issues with the row update notification logic or service calls.
   - **Impact**: The system's storage state may be inconsistent, potentially affecting carrier routing and storage.

3. **Validation Issues**:
   - **Symptom**: StorageRow creation or update fails with validation errors.
   - **Cause**: The StorageRow data does not meet the required format or constraints.
   - **Impact**: Administrators cannot create or update StorageRows, potentially affecting storage configuration.

4. **Pagination Issues**:
   - **Symptom**: The StorageRow list does not display correctly, or pagination does not work as expected.
   - **Cause**: Issues with pagination logic or parameters.
   - **Impact**: Administrators may not be able to view all StorageRows, potentially affecting storage configuration.

5. **Field Preservation Issues**:
   - **Symptom**: Certain fields are not preserved during StorageRow updates.
   - **Cause**: Issues with the field preservation logic in the update method.
   - **Impact**: StorageRows may have incorrect data after updates, potentially affecting storage configuration.

### Debugging Steps

1. **Stop Dependency Issues**:
   - Enable DEBUG level logging for the `StorageRowController` class.
   - Check the `createForm` method to verify that it correctly checks for stop dependencies.
   - Verify that the dependencies list is correctly populated and displayed.
   - Check for exceptions in the application logs related to stop dependencies.
   - Consider implementing additional validation to provide more specific information about missing stops.

2. **Row Update Notification Issues**:
   - Enable DEBUG level logging for the `StorageRowController` class and the `CarrierManagementServiceProxy`.
   - Check the `update` method to verify that it correctly calls the `updateRow` method on the `CarrierManagementServiceProxy`.
   - Verify that the `updateRow` method is correctly implemented in the `CarrierManagementServiceProxy`.
   - Check for exceptions in the application logs related to row update notifications.
   - Consider implementing additional validation to ensure that row updates are correctly propagated.

3. **Validation Issues**:
   - Enable DEBUG level logging for the `StorageRowController` class.
   - Check the methods that handle StorageRow creation and update, such as `create` and `update`.
   - Verify that the StorageRow data is correctly validated and that appropriate error messages are displayed.
   - Check for exceptions in the application logs related to validation.
   - Consider implementing additional validation for StorageRow data.

4. **Pagination Issues**:
   - Enable DEBUG level logging for the `StorageRowController` class.
   - Check the methods that handle pagination, such as `list`.
   - Verify that pagination parameters are correctly processed and applied.
   - Check for exceptions in the application logs related to pagination.
   - Consider implementing additional validation for pagination parameters.

5. **Field Preservation Issues**:
   - Enable DEBUG level logging for the `StorageRowController` class.
   - Check the `update` method to verify that it correctly preserves certain fields, such as capacity and storage area.
   - Verify that the `merge` method is correctly called on the StorageRow.
   - Check for exceptions in the application logs related to field preservation.
   - Consider implementing additional validation to ensure that fields are correctly preserved.

### Resolution

1. **Stop Dependency Issues**:
   - Implement more robust dependency checking with proper error handling.
   - Add more informative error messages to help administrators identify missing stops.
   - Consider implementing a more user-friendly interface for creating and selecting stops.
   - Add logging to track stop dependency issues and identify patterns.
   - Consider implementing a mechanism to create missing stops automatically.

2. **Row Update Notification Issues**:
   - Implement more robust notification logic with proper error handling.
   - Add validation to ensure that row updates are correctly propagated to the carrier management service.
   - Consider implementing a more reliable mechanism for row update notifications.
   - Add logging to track row update notifications and identify issues.
   - Consider implementing a notification helper to centralize and standardize notification logic.

3. **Validation Issues**:
   - Implement more robust validation logic with proper error handling.
   - Add more informative error messages to help administrators identify and correct validation issues.
   - Consider implementing client-side validation to catch issues before form submission.
   - Add logging to track validation failures and identify patterns.
   - Consider implementing a validation helper to centralize and standardize validation logic.

4. **Pagination Issues**:
   - Implement more robust pagination logic with proper error handling.
   - Add validation for pagination parameters to ensure they are within acceptable ranges.
   - Consider implementing a more user-friendly pagination interface.
   - Add logging to track pagination operations and identify issues.
   - Consider implementing a pagination helper to centralize and standardize pagination logic.

5. **Field Preservation Issues**:
   - Implement more robust field preservation logic with proper error handling.
   - Add validation to ensure that fields are correctly preserved during updates.
   - Consider implementing a more reliable mechanism for field preservation.
   - Add logging to track field preservation operations and identify issues.
   - Consider implementing a field preservation helper to centralize and standardize preservation logic.

### Monitoring

1. **Stop Dependency Monitoring**:
   - Monitor stop dependency operations to detect issues with stop selection and dependency checking.
   - Track the distribution of stops across StorageRows to identify potential issues.
   - Set up alerts for unusual stop dependency patterns that may indicate issues.

2. **Row Update Notification Monitoring**:
   - Monitor row update notification operations to detect issues with notification logic.
   - Track the frequency and timing of row update notifications to identify potential issues.
   - Set up alerts for failed notifications that may indicate issues.

3. **Validation Monitoring**:
   - Monitor validation operations to detect issues with data validation.
   - Track the distribution of validation failures to identify potential issues.
   - Set up alerts for unusual validation patterns that may indicate issues.

4. **Pagination Monitoring**:
   - Monitor pagination operations to detect issues with pagination logic.
   - Track the distribution of page sizes and page numbers to identify potential issues.
   - Set up alerts for unusual pagination patterns that may indicate issues.

5. **Field Preservation Monitoring**:
   - Monitor field preservation operations to detect issues with preservation logic.
   - Track the distribution of field values before and after updates to identify potential issues.
   - Set up alerts for unusual field value changes that may indicate issues.

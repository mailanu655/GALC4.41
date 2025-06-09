# GroupHoldController Technical Documentation

## Purpose
The `GroupHoldController` class manages group hold operations in the StampStorage system. It provides functionality for applying holds to groups of carriers based on various criteria such as storage row, production run number, production run date, and carrier status. This controller is essential for quality control and production management, enabling administrators to place holds on groups of carriers that may have quality issues or require inspection.

## Logic/Functionality
The class implements the following key functionality:

1. **Group Hold Criteria Form**: Allows administrators to specify criteria for selecting carriers to place on hold.
2. **Carrier Selection**: Retrieves carriers that match the specified criteria.
3. **Defect Management**: Enables administrators to add, update, and delete defects associated with carriers on hold.
4. **Group Hold Application**: Applies the specified hold status to all carriers that match the criteria.
5. **Cookie-Based State Management**: Uses cookies to maintain state between requests, storing criteria and defect information.

## Flow
The `GroupHoldController` class interacts with the system in the following way:

1. **Group Hold Criteria Form**:
   - User accesses the group hold criteria form via the `/groupholds?ByRowAndProductionRunNoAndProductionRunDate` endpoint (GET)
   - Controller retrieves storage rows, production run numbers, carrier statuses, and robot (press) information
   - Controller displays the form for entering group hold criteria
   - User submits the form with criteria via the `/groupholds?find=ByRowAndProductionRunNoAndProductionRunDate` endpoint (GET)
   - Controller validates the criteria
   - Controller saves the criteria in cookies
   - Controller retrieves carriers that match the criteria
   - Controller displays the list of carriers to the user

2. **Carrier List**:
   - User accesses the carrier list via the `/groupholds` endpoint (GET)
   - Controller retrieves the criteria from cookies
   - Controller retrieves carriers that match the criteria
   - Controller displays the list of carriers to the user, along with any associated defects

3. **Defect Management**:
   - User accesses the defect creation form via the `/groupholds?form` endpoint (GET)
   - Controller displays the form for entering defect information
   - User submits the form with defect information via the `/groupholds` endpoint (POST)
   - Controller validates the defect information
   - Controller saves the defect in cookies
   - Controller displays the updated carrier list with the new defect

   - User accesses the defect update form via the `/groupholds/{id}?form` endpoint (GET)
   - Controller retrieves the defect from cookies
   - Controller displays the form with the defect information
   - User submits the form with updated defect information via the `/groupholds` endpoint (PUT)
   - Controller validates the updated defect information
   - Controller updates the defect in cookies
   - Controller displays the updated carrier list with the updated defect

   - User submits a defect delete request via the `/groupholds/{id}` endpoint (DELETE)
   - Controller removes the defect from cookies
   - Controller displays the updated carrier list without the deleted defect

4. **Group Hold Application**:
   - User submits a group hold application request via the `/groupholds?find=submitgrouphold` endpoint (GET)
   - Controller validates that defects are provided if required by the hold status
   - Controller retrieves carriers that match the criteria
   - Controller applies the specified hold status to all carriers
   - Controller creates defect records in the database for each carrier if defects are provided
   - Controller clears the defect cookies
   - Controller redirects the user to the lanes view or carriers view

## Key Elements
- **GroupHoldFinderCriteria**: A domain class that represents the criteria for selecting carriers, containing properties such as row, production run number, production run date, carrier status, and robot (press).
- **Defect**: A domain class that represents a defect associated with a carrier, containing properties such as defect type, rework method, X and Y coordinates, and notes.
- **CarrierManagementService**: An autowired service that provides methods for managing carriers and retrieving carrier information.
- **CarrierManagementServiceProxy**: An autowired service that provides proxy methods for carrier management operations, such as applying bulk carrier status updates.
- **Cookie-Based State Management**: The controller uses cookies to maintain state between requests, storing criteria and defect information.
- **Validation**: The controller validates criteria and defect information to ensure it meets the required format and constraints.
- **Logging**: The controller logs user actions for auditing purposes.

## Usage
The `GroupHoldController` class is used in the following scenarios:

1. **Quality Control**: Administrators can place holds on groups of carriers that may have quality issues, requiring inspection or rework.
2. **Production Management**: Administrators can manage the status of carriers in the production process, ensuring that only carriers with acceptable quality proceed.
3. **Defect Tracking**: Administrators can record defects associated with carriers, providing information for quality analysis and improvement.

Example URL patterns:
- `/groupholds?ByRowAndProductionRunNoAndProductionRunDate` (GET) - Display the group hold criteria form
- `/groupholds?find=ByRowAndProductionRunNoAndProductionRunDate` (GET) - Retrieve carriers that match the criteria
- `/groupholds` (GET) - Display the list of carriers that match the criteria
- `/groupholds?form` (GET) - Display the defect creation form
- `/groupholds` (POST) - Create a new defect
- `/groupholds/{id}?form` (GET) - Display the defect update form
- `/groupholds` (PUT) - Update an existing defect
- `/groupholds/{id}` (DELETE) - Delete a defect
- `/groupholds?find=submitgrouphold` (GET) - Apply the specified hold status to all carriers that match the criteria

## Database Tables
The `GroupHoldController` interacts with the following database tables:

1. **CARRIER_MES**: Stores carrier information with the following columns:
   - **ID**: The unique identifier for the carrier (primary key)
   - **VERSION**: Version number for optimistic locking
   - **CARRIER_NUMBER**: The number of the carrier
   - **DIE_NUMBER**: The number of the die
   - **QUANTITY**: The quantity of parts in the carrier
   - **CURRENT_LOCATION**: The ID of the current location of the carrier (foreign key to STOP)
   - **DESTINATION**: The ID of the destination of the carrier (foreign key to STOP)
   - **ORIGINATION_LOCATION**: The ID of the origination location of the carrier
   - **STATUS**: The status of the carrier (enumeration value)
   - **BUFFER**: Whether the carrier is in a buffer position
   - **PRODUCTION_RUN_NUMBER**: The production run number of the carrier
   - **PRODUCTION_RUN_DATE**: The production run date of the carrier
   - **MAINTENANCE_BITS**: The maintenance bits of the carrier

2. **DEFECT**: Stores defect information with the following columns:
   - **ID**: The unique identifier for the defect (primary key)
   - **VERSION**: Version number for optimistic locking
   - **CARRIER_NUMBER**: The number of the carrier associated with the defect
   - **PRODUCTION_RUN_NO**: The production run number associated with the defect
   - **DEFECT_TYPE**: The type of the defect (enumeration value)
   - **REWORK_METHOD**: The method for reworking the defect (enumeration value)
   - **X_AREA**: The X coordinate of the defect
   - **Y_AREA**: The Y coordinate of the defect
   - **DEFECT_TIMESTAMP**: The timestamp when the defect was recorded
   - **SOURCE**: The source of the defect information
   - **NOTE**: Additional notes about the defect

3. **STORAGE_ROW**: Referenced by the controller, stores storage row information with the following columns:
   - **ID**: The unique identifier for the storage row (primary key)
   - **VERSION**: Version number for optimistic locking
   - **ROW_NAME**: The name of the storage row
   - **CAPACITY**: The capacity of the storage row
   - **STOP_ID**: The ID of the associated stop (foreign key to STOP)
   - **STORAGE_AREA**: The area of the storage row (enumeration value)

4. **STOP**: Referenced by the CARRIER_MES and STORAGE_ROW tables, stores stop information with the following columns:
   - **ID**: The unique identifier for the stop (primary key)
   - **VERSION**: Version number for optimistic locking
   - **NAME**: The name of the stop
   - **STOP_TYPE**: The type of the stop (enumeration value)
   - **STOP_AREA**: The area of the stop (enumeration value)
   - **STOP_AVAILABILITY**: The availability of the stop (enumeration value)
   - **CAPACITY**: The capacity of the stop

5. **DIE**: Referenced by the controller, stores die information with the following columns:
   - **ID**: The unique identifier for the die (primary key)
   - **VERSION**: Version number for optimistic locking
   - **DESCRIPTION**: The description of the die
   - **PART_PRODUCTION_VOLUME**: The production volume of the die (enumeration value)
   - **ACTIVE**: Whether the die is active
   - **BACKGROUND_COLOR**: The background color for the die in the UI
   - **TEXT_COLOR**: The text color for the die in the UI
   - **IMAGE_FILE_NAME**: The name of the image file for the die

The controller primarily reads from the CARRIER_MES, STORAGE_ROW, STOP, and DIE tables, and writes to the CARRIER_MES and DEFECT tables.

## Debugging and Production Support

### Common Issues

1. **Cookie Management Issues**:
   - **Symptom**: Criteria or defect information is lost between requests, or incorrect information is displayed.
   - **Cause**: Issues with cookie creation, retrieval, or parsing.
   - **Impact**: Administrators cannot effectively manage group holds, potentially affecting quality control and production management.

2. **Criteria Validation Issues**:
   - **Symptom**: Group hold criteria validation fails or produces unexpected results.
   - **Cause**: Issues with criteria validation logic or constraints.
   - **Impact**: Administrators cannot specify the desired criteria, potentially affecting the selection of carriers for holds.

3. **Carrier Selection Issues**:
   - **Symptom**: The wrong carriers are selected based on the criteria, or no carriers are selected when some should be.
   - **Cause**: Issues with the carrier selection logic or database queries.
   - **Impact**: Administrators cannot place holds on the desired carriers, potentially affecting quality control and production management.

4. **Defect Management Issues**:
   - **Symptom**: Defects cannot be created, updated, or deleted, or defect information is incorrect.
   - **Cause**: Issues with defect management logic or cookie handling.
   - **Impact**: Administrators cannot record defect information, potentially affecting quality analysis and improvement.

5. **Group Hold Application Issues**:
   - **Symptom**: Group hold application fails or produces unexpected results.
   - **Cause**: Issues with the group hold application logic or service communication.
   - **Impact**: Administrators cannot apply holds to carriers, potentially affecting quality control and production management.

### Debugging Steps

1. **Cookie Management Issues**:
   - Enable DEBUG level logging for the `GroupHoldController` class.
   - Check the methods that handle cookie creation, retrieval, and parsing, such as `getSavedCarrierHistoryFinderCriteria`, `saveCarrierHistoryFinderCriteria`, `getDefectList`, `saveDefect`, `updateDefect`, `deleteDefect`, and `clearAllDefects`.
   - Verify that cookies are correctly created, retrieved, and parsed.
   - Check for exceptions in the application logs related to cookie handling.
   - Consider implementing additional validation for cookie data.

2. **Criteria Validation Issues**:
   - Enable DEBUG level logging for the `GroupHoldController` class.
   - Check the methods that handle criteria validation, such as `findCarrierByCriteria`.
   - Verify that criteria are correctly validated and that appropriate error messages are displayed.
   - Check for exceptions in the application logs related to criteria validation.
   - Consider implementing additional validation for criteria.

3. **Carrier Selection Issues**:
   - Enable DEBUG level logging for the `GroupHoldController` class and the `CarrierManagementService` class.
   - Check the methods that handle carrier selection, such as `findCarrierByCriteria` and `carrierlist`.
   - Verify that the correct database queries are executed and that the results are correctly processed.
   - Check for exceptions in the application logs related to carrier selection.
   - Consider implementing additional validation for carrier selection.

4. **Defect Management Issues**:
   - Enable DEBUG level logging for the `GroupHoldController` class.
   - Check the methods that handle defect management, such as `create`, `update`, `delete`, `saveDefect`, `updateDefect`, `deleteDefect`, and `getDefect`.
   - Verify that defect information is correctly created, updated, and deleted in cookies.
   - Check for exceptions in the application logs related to defect management.
   - Consider implementing additional validation for defect information.

5. **Group Hold Application Issues**:
   - Enable DEBUG level logging for the `GroupHoldController` class and the `CarrierManagementServiceProxy` class.
   - Check the methods that handle group hold application, such as `submitGroupHold`.
   - Verify that the correct service methods are called and that the results are correctly processed.
   - Check for exceptions in the application logs related to group hold application.
   - Consider implementing additional validation for group hold application.

### Resolution

1. **Cookie Management Issues**:
   - Implement more robust cookie handling with proper error handling.
   - Add more informative error messages to help administrators identify and resolve cookie issues.
   - Consider implementing a more reliable mechanism for maintaining state between requests, such as server-side session storage.
   - Add logging to track cookie operations and identify issues.
   - Consider implementing a cookie helper to centralize and standardize cookie handling.

2. **Criteria Validation Issues**:
   - Implement more robust criteria validation with proper error handling.
   - Add more informative error messages to help administrators identify and correct validation issues.
   - Consider implementing client-side validation to catch issues before form submission.
   - Add logging to track validation failures and identify patterns.
   - Consider implementing a validation helper to centralize and standardize validation logic.

3. **Carrier Selection Issues**:
   - Implement more robust carrier selection logic with proper error handling.
   - Add more informative error messages to help administrators identify and resolve carrier selection issues.
   - Consider implementing more efficient database queries for carrier selection.
   - Add logging to track carrier selection operations and identify issues.
   - Consider implementing a carrier selection helper to centralize and standardize selection logic.

4. **Defect Management Issues**:
   - Implement more robust defect management with proper error handling.
   - Add more informative error messages to help administrators identify and resolve defect management issues.
   - Consider implementing a more reliable mechanism for storing defect information, such as server-side session storage.
   - Add logging to track defect management operations and identify issues.
   - Consider implementing a defect management helper to centralize and standardize defect handling.

5. **Group Hold Application Issues**:
   - Implement more robust group hold application with proper error handling.
   - Add more informative error messages to help administrators identify and resolve group hold application issues.
   - Consider implementing a more reliable mechanism for applying group holds, with better error recovery.
   - Add logging to track group hold application operations and identify issues.
   - Consider implementing a group hold application helper to centralize and standardize application logic.

### Monitoring

1. **Cookie Management Monitoring**:
   - Monitor cookie operations to detect issues with cookie handling.
   - Track the size and complexity of cookie data to identify potential issues.
   - Set up alerts for unusual cookie patterns that may indicate issues.

2. **Criteria Validation Monitoring**:
   - Monitor criteria validation operations to detect issues with validation logic.
   - Track the distribution of validation failures to identify potential issues.
   - Set up alerts for unusual validation patterns that may indicate issues.

3. **Carrier Selection Monitoring**:
   - Monitor carrier selection operations to detect issues with selection logic.
   - Track the distribution of carrier selection results to identify potential issues.
   - Set up alerts for unusual selection patterns that may indicate issues.

4. **Defect Management Monitoring**:
   - Monitor defect management operations to detect issues with defect handling.
   - Track the distribution of defect operations to identify potential issues.
   - Set up alerts for unusual defect patterns that may indicate issues.

5. **Group Hold Application Monitoring**:
   - Monitor group hold application operations to detect issues with application logic.
   - Track the distribution of group hold applications to identify potential issues.
   - Set up alerts for unusual application patterns that may indicate issues.

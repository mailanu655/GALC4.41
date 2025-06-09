# LanesController Technical Documentation

## Purpose
The `LanesController` class manages the visualization and manipulation of storage lanes in the StampStorage system. It provides functionality for viewing lane inventory, managing carrier positions within lanes, releasing carriers from lanes, and monitoring the overall storage state. This controller is essential for the operational management of the storage system, enabling administrators to visualize and control the movement of carriers through the storage lanes.

## Logic/Functionality
The class implements the following key functionality:

1. **Lane Inventory Visualization**: Displays the current state of storage lanes, including carriers present in each lane.
2. **Lane Overview**: Provides a high-level overview of all storage lanes, grouped by storage area.
3. **Storage State Monitoring**: Displays the current storage state and allows for manual refresh of the storage state.
4. **Lane Resequencing**: Allows administrators to change the sequence of carriers within a lane.
5. **Manual Carrier Release**: Enables administrators to manually release carriers from lanes, either individually or in groups.
6. **Empty Carrier Release**: Provides functionality to release empty carriers from specified storage areas.

## Flow
The `LanesController` class interacts with the system in the following way:

1. **Lane Inventory Visualization**:
   - User accesses the lane inventory view via the `/lanes` endpoint (GET)
   - Controller retrieves storage rows from the database, grouped by storage area
   - Controller filters carriers in each lane to determine their status (in lane, moving out, queued to move out, moving in)
   - Controller displays the lane inventory view with carriers organized by lane and status

2. **Lane Overview**:
   - User accesses the lane overview via the `/lanes?overview` endpoint (GET)
   - Controller retrieves storage rows from the database, grouped by storage area
   - Controller retrieves active models from the database
   - Controller displays the lane overview with lanes organized by storage area

3. **Storage State Monitoring**:
   - User accesses the storage state view via the `/lanes?storageState` endpoint (GET)
   - Controller retrieves storage rows from the database, grouped by storage area
   - Controller retrieves active models from the database
   - Controller displays the storage state view with lanes organized by storage area
   - User can refresh the storage state via the `/lanes?storageState=refresh` endpoint (GET)
   - Controller calls the `resetStorageState` method on the `CarrierManagementServiceProxy`
   - Controller redirects to the storage state view

4. **Lane Resequencing**:
   - User accesses the lane resequencing form via the `/lanes?find=ByLane&form` endpoint (GET)
   - Controller retrieves all storage rows from the database
   - Controller displays the lane selection form
   - User selects a lane and submits the form via the `/lanes?find=ByLane` endpoint (GET)
   - Controller retrieves carriers in the selected lane
   - Controller displays the lane resequencing form with carriers in the lane
   - User resequences carriers and submits the form via the `/lanes` endpoint (PUT)
   - Controller validates the resequenced carriers
   - Controller calls the `saveCarriersInToRow` method on the `CarrierManagementServiceProxy`
   - Controller displays the updated lane resequencing form

5. **Manual Carrier Release**:
   - User accesses the manual carrier release form via the `/lanes?ByRow` endpoint (GET)
   - Controller retrieves all storage rows and valid destination stops
   - Controller displays the manual carrier release form
   - User selects a row, count, and destination and submits the form via the `/lanes?find=ByRow` endpoint (GET)
   - Controller calls the `releaseCarriers` method on the `CarrierManagementServiceProxy`
   - Controller redirects to the lane inventory view

6. **Empty Carrier Release**:
   - User accesses the empty carrier release form via the `/lanes?ByEmpty` endpoint (GET)
   - Controller displays the empty carrier release form
   - User selects a storage area and submits the form via the `/lanes?find=ByEmpty` endpoint (GET)
   - Controller calls the `releaseEmptyCarriersFromRows` method on the `CarrierManagementServiceProxy`
   - Controller redirects to the lane inventory view

## Key Elements
- **StorageRow**: A domain class that represents a storage row, containing properties such as row name, capacity, and associated stop.
- **Carrier**: A domain class that represents a carrier, containing properties such as carrier number, die, quantity, and status.
- **CarrierManagementService**: An autowired service that provides methods for managing carriers and retrieving carrier information.
- **CarrierManagementServiceProxy**: An autowired service that provides proxy methods for carrier management operations, such as saving carriers to rows and releasing carriers.
- **BitInfo**: A domain class that represents maintenance bit information for carriers.
- **Model**: A domain class that represents a model, containing properties such as name and description.
- **DieInventory**: A domain class that represents inventory information for a die, containing properties such as die name and quantity.

## Usage
The `LanesController` class is used in the following scenarios:

1. **Inventory Management**: Administrators can view the current state of storage lanes and monitor carrier positions.
2. **Lane Resequencing**: Administrators can change the sequence of carriers within a lane to optimize storage or prepare for carrier release.
3. **Manual Carrier Release**: Administrators can manually release carriers from lanes to fulfill orders or manage inventory.
4. **Empty Carrier Release**: Administrators can release empty carriers from storage areas to free up space.
5. **Storage State Monitoring**: Administrators can monitor the overall storage state and refresh it if necessary.

Example URL patterns:
- `/lanes` (GET) - Display the lane inventory view
- `/lanes?overview` (GET) - Display the lane overview
- `/lanes?storageState` (GET) - Display the storage state view
- `/lanes?storageState=refresh` (GET) - Refresh the storage state
- `/lanes?find=ByLane&form` (GET) - Display the lane selection form for resequencing
- `/lanes?find=ByLane` (GET) - Display the lane resequencing form for a selected lane
- `/lanes` (PUT) - Resequence carriers in a lane
- `/lanes?ByRow` (GET) - Display the manual carrier release form
- `/lanes?find=ByRow` (GET) - Release carriers from a row
- `/lanes?ByEmpty` (GET) - Display the empty carrier release form
- `/lanes?find=ByEmpty` (GET) - Release empty carriers from a storage area
- `/lanes?inventory` (GET) - Display the inventory view
- `/lanes?inventoryMonitorView` (GET) - Display the inventory monitor view

## Database Tables
The `LanesController` interacts with the following database tables:

1. **STORAGE_ROW**: Stores storage row information with the following columns:
   - **ID**: The unique identifier for the storage row (primary key)
   - **VERSION**: Version number for optimistic locking
   - **ROW_NAME**: The name of the storage row
   - **CAPACITY**: The capacity of the storage row
   - **STOP_ID**: The ID of the associated stop (foreign key to STOP)
   - **STORAGE_AREA**: The area of the storage row (enumeration value)

2. **CARRIER_MES**: Stores carrier information with the following columns:
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

3. **CARRIER_RELEASE**: Stores carrier release information with the following columns:
   - **ID**: The unique identifier for the carrier release (primary key)
   - **VERSION**: Version number for optimistic locking
   - **SOURCE**: The source of the release
   - **DESTINATION**: The ID of the destination of the release (foreign key to STOP)

4. **STOP**: Stores stop information with the following columns:
   - **ID**: The unique identifier for the stop (primary key)
   - **VERSION**: Version number for optimistic locking
   - **NAME**: The name of the stop
   - **STOP_TYPE**: The type of the stop (enumeration value)
   - **STOP_AREA**: The area of the stop (enumeration value)
   - **STOP_AVAILABILITY**: The availability of the stop (enumeration value)
   - **CAPACITY**: The capacity of the stop

5. **DIE**: Stores die information with the following columns:
   - **ID**: The unique identifier for the die (primary key)
   - **VERSION**: Version number for optimistic locking
   - **DESCRIPTION**: The description of the die
   - **PART_PRODUCTION_VOLUME**: The production volume of the die (enumeration value)
   - **ACTIVE**: Whether the die is active
   - **BACKGROUND_COLOR**: The background color for the die in the UI
   - **TEXT_COLOR**: The text color for the die in the UI
   - **IMAGE_FILE_NAME**: The name of the image file for the die

6. **MODEL**: Stores model information with the following columns:
   - **ID**: The unique identifier for the model (primary key)
   - **VERSION**: Version number for optimistic locking
   - **NAME**: The name of the model
   - **DESCRIPTION**: The description of the model
   - **ACTIVE**: Whether the model is active
   - **LEFT_DIE**: The ID of the left die for the model (foreign key to DIE)
   - **RIGHT_DIE**: The ID of the right die for the model (foreign key to DIE)

7. **PARM_SETTING**: Stores parameter setting information with the following columns:
   - **ID**: The unique identifier for the parameter setting (primary key)
   - **VERSION**: Version number for optimistic locking
   - **FIELD_NAME**: The name of the parameter
   - **FIELD_VALUE**: The value of the parameter
   - **DESCRIPTION**: The description of the parameter
   - **UPDATED_BY**: The user who last updated the parameter
   - **UPDATE_TSTP**: The timestamp of the last update

The controller primarily reads from these tables to retrieve information about storage rows, carriers, and related entities. It also indirectly writes to the CARRIER_MES and CARRIER_RELEASE tables through the CarrierManagementServiceProxy when performing operations such as resequencing carriers and releasing carriers.

## Debugging and Production Support

### Common Issues

1. **Lane Visualization Issues**:
   - **Symptom**: Lanes are not displayed correctly, or carriers are shown in incorrect positions.
   - **Cause**: Issues with retrieving storage row or carrier information from the database, or with filtering carriers by lane.
   - **Impact**: Administrators cannot accurately visualize the current state of storage lanes, potentially affecting inventory management decisions.

2. **Lane Resequencing Issues**:
   - **Symptom**: Carrier resequencing fails or produces unexpected results.
   - **Cause**: Issues with validating resequenced carriers, or with saving carriers to rows through the CarrierManagementServiceProxy.
   - **Impact**: Administrators cannot effectively manage carrier positions within lanes, potentially affecting storage optimization and carrier release operations.

3. **Carrier Release Issues**:
   - **Symptom**: Manual carrier release or empty carrier release fails or produces unexpected results.
   - **Cause**: Issues with validating release parameters, or with releasing carriers through the CarrierManagementServiceProxy.
   - **Impact**: Administrators cannot effectively release carriers from lanes, potentially affecting order fulfillment and inventory management.

4. **Storage State Refresh Issues**:
   - **Symptom**: Storage state refresh fails or produces unexpected results.
   - **Cause**: Issues with resetting the storage state through the CarrierManagementServiceProxy.
   - **Impact**: Administrators cannot effectively monitor and manage the overall storage state, potentially affecting system reliability.

5. **Inventory View Issues**:
   - **Symptom**: Inventory views (inventory, inventoryMonitorView) are not displayed correctly, or inventory information is inaccurate.
   - **Cause**: Issues with retrieving and processing inventory information from the database.
   - **Impact**: Administrators cannot accurately monitor inventory levels, potentially affecting inventory management decisions.

### Debugging Steps

1. **Lane Visualization Issues**:
   - Enable DEBUG level logging for the `LanesController` class.
   - Check the methods that handle lane visualization, such as `listLanes` and `listOverviewRows`.
   - Verify that storage rows are correctly retrieved from the database and grouped by storage area.
   - Verify that carriers are correctly filtered by lane and status.
   - Check for exceptions in the application logs related to lane visualization.
   - Consider implementing additional validation for lane visualization.

2. **Lane Resequencing Issues**:
   - Enable DEBUG level logging for the `LanesController` class and the `CarrierManagementServiceProxy` class.
   - Check the methods that handle lane resequencing, such as `findLaneToResequence`, `update`, `insertCarrier`, and `removeCarrier`.
   - Verify that resequenced carriers are correctly validated.
   - Verify that carriers are correctly saved to rows through the CarrierManagementServiceProxy.
   - Check for exceptions in the application logs related to lane resequencing.
   - Consider implementing additional validation for lane resequencing.

3. **Carrier Release Issues**:
   - Enable DEBUG level logging for the `LanesController` class and the `CarrierManagementServiceProxy` class.
   - Check the methods that handle carrier release, such as `manuallyOrderFulfillment` and `manuallyReleaseEmpty`.
   - Verify that release parameters are correctly validated.
   - Verify that carriers are correctly released through the CarrierManagementServiceProxy.
   - Check for exceptions in the application logs related to carrier release.
   - Consider implementing additional validation for carrier release.

4. **Storage State Refresh Issues**:
   - Enable DEBUG level logging for the `LanesController` class and the `CarrierManagementServiceProxy` class.
   - Check the method that handles storage state refresh, `refreshStorageState`.
   - Verify that the storage state is correctly reset through the CarrierManagementServiceProxy.
   - Check for exceptions in the application logs related to storage state refresh.
   - Consider implementing additional validation for storage state refresh.

5. **Inventory View Issues**:
   - Enable DEBUG level logging for the `LanesController` class.
   - Check the methods that handle inventory views, such as `getInventory` and `getInventoryMonitorView`.
   - Verify that inventory information is correctly retrieved and processed from the database.
   - Check for exceptions in the application logs related to inventory views.
   - Consider implementing additional validation for inventory views.

### Resolution

1. **Lane Visualization Issues**:
   - Implement more robust lane visualization with proper error handling.
   - Add more informative error messages to help administrators identify and resolve lane visualization issues.
   - Consider implementing a more efficient mechanism for retrieving and processing lane information.
   - Add logging to track lane visualization operations and identify issues.
   - Consider implementing a lane visualization helper to centralize and standardize visualization logic.

2. **Lane Resequencing Issues**:
   - Implement more robust lane resequencing with proper error handling.
   - Add more informative error messages to help administrators identify and resolve lane resequencing issues.
   - Consider implementing a more reliable mechanism for validating and saving resequenced carriers.
   - Add logging to track lane resequencing operations and identify issues.
   - Consider implementing a lane resequencing helper to centralize and standardize resequencing logic.

3. **Carrier Release Issues**:
   - Implement more robust carrier release with proper error handling.
   - Add more informative error messages to help administrators identify and resolve carrier release issues.
   - Consider implementing a more reliable mechanism for validating and executing carrier release operations.
   - Add logging to track carrier release operations and identify issues.
   - Consider implementing a carrier release helper to centralize and standardize release logic.

4. **Storage State Refresh Issues**:
   - Implement more robust storage state refresh with proper error handling.
   - Add more informative error messages to help administrators identify and resolve storage state refresh issues.
   - Consider implementing a more reliable mechanism for resetting the storage state.
   - Add logging to track storage state refresh operations and identify issues.
   - Consider implementing a storage state refresh helper to centralize and standardize refresh logic.

5. **Inventory View Issues**:
   - Implement more robust inventory views with proper error handling.
   - Add more informative error messages to help administrators identify and resolve inventory view issues.
   - Consider implementing a more efficient mechanism for retrieving and processing inventory information.
   - Add logging to track inventory view operations and identify issues.
   - Consider implementing an inventory view helper to centralize and standardize view logic.

### Monitoring

1. **Lane Visualization Monitoring**:
   - Monitor lane visualization operations to detect issues with lane display.
   - Track the distribution of lane visualization requests to identify potential issues.
   - Set up alerts for unusual lane visualization patterns that may indicate issues.

2. **Lane Resequencing Monitoring**:
   - Monitor lane resequencing operations to detect issues with carrier positioning.
   - Track the distribution of lane resequencing requests to identify potential issues.
   - Set up alerts for unusual lane resequencing patterns that may indicate issues.

3. **Carrier Release Monitoring**:
   - Monitor carrier release operations to detect issues with carrier movement.
   - Track the distribution of carrier release requests to identify potential issues.
   - Set up alerts for unusual carrier release patterns that may indicate issues.

4. **Storage State Refresh Monitoring**:
   - Monitor storage state refresh operations to detect issues with state management.
   - Track the distribution of storage state refresh requests to identify potential issues.
   - Set up alerts for unusual storage state refresh patterns that may indicate issues.

5. **Inventory View Monitoring**:
   - Monitor inventory view operations to detect issues with inventory display.
   - Track the distribution of inventory view requests to identify potential issues.
   - Set up alerts for unusual inventory view patterns that may indicate issues.
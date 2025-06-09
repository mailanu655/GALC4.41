# WeldOrderController Technical Documentation

## Purpose
The `WeldOrderController` class manages the WeldOrder entities in the StampStorage system. It provides functionality for creating, reading, updating, and deleting WeldOrder records, which represent orders for carriers to be delivered to weld lines. This controller is essential for managing the flow of carriers to weld lines, enabling operators to create and manage orders for specific models and quantities of carriers.

## Logic/Functionality
The class implements the following key functionality:

1. **WeldOrder Creation**: Allows operators to create new WeldOrder records with details such as model, quantities, and order status.
2. **WeldOrder Retrieval**: Provides methods to retrieve individual WeldOrder records or lists of WeldOrders, with filtering by order status and order manager.
3. **WeldOrder Update**: Enables operators to update existing WeldOrder records, with special handling to preserve certain fields.
4. **WeldOrder Deletion**: Allows operators to delete WeldOrder records.
5. **WeldOrder Fulfillment Management**: Provides methods to manage the fulfillment of WeldOrders, including removing carriers from orders.
6. **WeldOrder Status Management**: Provides methods to manage the status of WeldOrders, including manually completing or canceling orders.
7. **Role-Based Access Control**: Implements role-based access control to restrict certain operations to specific user roles.

## Flow
The `WeldOrderController` class interacts with the system in the following way:

1. **WeldOrder Creation**:
   - User accesses the WeldOrder creation form via the `/weldorders?form` endpoint (GET)
   - Controller displays the form for entering WeldOrder data
   - User submits the form with WeldOrder data via the `/weldorders` endpoint (POST)
   - Controller validates the data
   - Controller creates a new WeldOrder record with initial values for delivered and consumed quantities
   - Controller redirects the user to the WeldOrder list page

2. **WeldOrder Retrieval**:
   - User accesses a specific WeldOrder via the `/weldorders/{id}` endpoint (GET)
   - Controller retrieves the WeldOrder from the database
   - Controller retrieves the OrderFulfillments for the WeldOrder
   - Controller displays the WeldOrder details and OrderFulfillments to the user

   - User accesses the list of WeldOrders via the `/weldorders` endpoint (GET)
   - Controller retrieves WeldOrders from the database, filtered by the user's role if applicable
   - Controller displays the list of WeldOrders to the user

3. **WeldOrder Update**:
   - User accesses the WeldOrder update form via the `/weldorders/{id}?form` endpoint (GET)
   - Controller retrieves the WeldOrder from the database
   - Controller determines if the user can update the order status and delivery status
   - Controller displays the form with the WeldOrder data and available status options
   - User submits the form with updated WeldOrder data via the `/weldorders` endpoint (PUT)
   - Controller validates the data
   - Controller updates the WeldOrder record, preserving certain fields
   - Controller redirects the user to the WeldOrder list page

4. **WeldOrder Deletion**:
   - User submits a delete request via the `/weldorders/{id}` endpoint (DELETE)
   - Controller deletes the WeldOrder from the database
   - Controller redirects the user to the WeldOrder list page

5. **WeldOrder Fulfillment Management**:
   - User submits a request to remove a carrier from an order via the `/weldorders?orderfulfillment` endpoint (GET)
   - Controller retrieves the WeldOrder and OrderFulfillment from the database
   - Controller updates the delivered quantity of the WeldOrder
   - Controller removes the OrderFulfillment
   - Controller displays the updated WeldOrder details

6. **WeldOrder Status Management**:
   - User submits a request to manually complete an order via the `/weldorders?find=ByManualComplete` endpoint (GET)
   - Controller retrieves the WeldOrder from the database
   - Controller updates the order status and delivery status to ManuallyCompleted
   - Controller refreshes the storage state
   - Controller redirects the user to the WeldOrder list page

   - User submits a request to cancel an order via the `/weldorders?find=ByCancel` endpoint (GET)
   - Controller retrieves the WeldOrder from the database
   - Controller updates the order status and delivery status to Cancelled
   - Controller redirects the user to the WeldOrder list page

## Key Elements
- **WeldOrder**: A domain class that represents an order for carriers to be delivered to a weld line, containing properties such as model, quantities, and order status.
- **OrderFulfillment**: A domain class that represents the fulfillment of a WeldOrder with a specific carrier, containing properties such as carrier number and quantity.
- **OrderMgr**: A domain class that represents a weld line manager, referenced by the WeldOrder class.
- **Model**: A domain class that represents a car model, referenced by the WeldOrder class.
- **OrderStatus**: An enumeration that represents the status of a WeldOrder, such as Initialized, InProcess, or Completed.
- **CarrierManagementServiceProxy**: An autowired service that provides methods for managing carriers and storage, including refreshing the storage state.
- **Role-Based Access Control**: The controller implements role-based access control to restrict certain operations to specific user roles, such as ROLE_APP-OHCVD-MAP-WEOP1.
- **Validation**: The controller validates WeldOrder data to ensure it meets the required format and constraints.
- **Logging**: The controller logs important events, such as order updates, to facilitate debugging and auditing.

## Usage
The `WeldOrderController` class is used in the following scenarios:

1. **Order Creation**: Operators can create new WeldOrders to request carriers to be delivered to weld lines.
2. **Order Management**: Operators can view, update, and delete WeldOrders to manage the flow of carriers to weld lines.
3. **Order Fulfillment**: The system tracks the fulfillment of WeldOrders with specific carriers.
4. **Order Status Management**: Operators can manage the status of WeldOrders, including manually completing or canceling orders.

Example URL patterns:
- `/weldorders?form` (GET) - Display the WeldOrder creation form
- `/weldorders` (POST) - Create a new WeldOrder
- `/weldorders/{id}` (GET) - Display a specific WeldOrder
- `/weldorders` (GET) - Display the list of WeldOrders
- `/weldorders/{id}?form` (GET) - Display the WeldOrder update form
- `/weldorders` (PUT) - Update an existing WeldOrder
- `/weldorders/{id}` (DELETE) - Delete a WeldOrder
- `/weldorders?orderfulfillment` (GET) - Remove a carrier from an order
- `/weldorders?find=ByManualComplete` (GET) - Manually complete an order
- `/weldorders?find=ByCancel` (GET) - Cancel an order

## Database Tables
The `WeldOrderController` interacts with the following database tables:

1. **WELD_ORDER**: Stores WeldOrder information with the following columns:
   - **ID**: The unique identifier for the WeldOrder (primary key)
   - **VERSION**: Version number for optimistic locking
   - **ORDER_SEQUENCE**: The sequence number of the order
   - **MODEL_ID**: The ID of the model (foreign key to MODEL)
   - **ORDER_MGR_ID**: The ID of the order manager (foreign key to ORDER_MGR)
   - **LEFT_QUANTITY**: The quantity of left-hand parts requested
   - **RIGHT_QUANTITY**: The quantity of right-hand parts requested
   - **LEFT_DELIVERED_QUANTITY**: The quantity of left-hand parts delivered
   - **RIGHT_DELIVERED_QUANTITY**: The quantity of right-hand parts delivered
   - **LEFT_CONSUMED_QUANTITY**: The quantity of left-hand parts consumed
   - **RIGHT_CONSUMED_QUANTITY**: The quantity of right-hand parts consumed
   - **LEFT_QUEUED_QTY**: The quantity of left-hand parts queued
   - **RIGHT_QUEUED_QTY**: The quantity of right-hand parts queued
   - **ORDER_STATUS**: The status of the order (enumeration value)
   - **DELIVERY_STATUS**: The status of the delivery (enumeration value)
   - **CREATED_BY**: The user who created the order
   - **CREATED_DATE**: The date the order was created
   - **COMMENTS**: Comments about the order
   - **LEFT_DELIVERY_COMMENTS**: Comments about left-hand part delivery
   - **RIGHT_DELIVERY_COMMENTS**: Comments about right-hand part delivery
   - **LEFT_FULFILLMENT_COMMENTS**: Comments about left-hand part fulfillment
   - **RIGHT_FULFILLMENT_COMMENTS**: Comments about right-hand part fulfillment

2. **ORDER_FULFILLMENT**: Stores OrderFulfillment information with the following columns:
   - **WELD_ORDER_ID**: The ID of the weld order (part of composite primary key, foreign key to WELD_ORDER)
   - **CARRIER_NUMBER**: The number of the carrier (part of composite primary key)
   - **RELEASE_CYCLE**: The release cycle (part of composite primary key)
   - **QUANTITY**: The quantity of parts in the carrier
   - **CARRIER_FULFILLMENT_STATUS**: The status of the carrier fulfillment (enumeration value)

3. **MODEL**: Referenced by the WELD_ORDER table, stores model information with the following columns:
   - **ID**: The unique identifier for the model (primary key)
   - **VERSION**: Version number for optimistic locking
   - **NAME**: The name of the model
   - **DESCRIPTION**: The description of the model
   - **LEFT_DIE_ID**: The ID of the left-hand die (foreign key to DIE)
   - **RIGHT_DIE_ID**: The ID of the right-hand die (foreign key to DIE)
   - **ACTIVE**: Whether the model is active

4. **ORDER_MGR**: Referenced by the WELD_ORDER table, stores order manager information with the following columns:
   - **ID**: The unique identifier for the order manager (primary key)
   - **VERSION**: Version number for optimistic locking
   - **LINE_NAME**: The name of the weld line
   - **LEFT_QUEUE_STOP_ID**: The ID of the left-hand queue stop (foreign key to STOP)
   - **RIGHT_QUEUE_STOP_ID**: The ID of the right-hand queue stop (foreign key to STOP)

The controller primarily reads from and writes to the WELD_ORDER and ORDER_FULFILLMENT tables, and reads from the MODEL and ORDER_MGR tables.

## Debugging and Production Support

### Common Issues

1. **Role-Based Access Control Issues**:
   - **Symptom**: Users cannot access certain WeldOrder operations despite having the appropriate role.
   - **Cause**: Issues with role-based access control logic or role assignments.
   - **Impact**: Users cannot perform necessary operations, potentially affecting weld line operations.

2. **Order Status Management Issues**:
   - **Symptom**: WeldOrder status updates do not work as expected, or status constraints are not enforced.
   - **Cause**: Issues with order status management logic or constraints.
   - **Impact**: WeldOrders may have incorrect statuses, potentially affecting weld line operations.

3. **Order Fulfillment Issues**:
   - **Symptom**: OrderFulfillment operations do not work as expected, or delivered quantities are not correctly updated.
   - **Cause**: Issues with order fulfillment logic or quantity calculations.
   - **Impact**: WeldOrders may have incorrect fulfillment information, potentially affecting weld line operations.

4. **Storage State Refresh Issues**:
   - **Symptom**: Storage state is not correctly refreshed after manual order completion.
   - **Cause**: Issues with storage state refresh logic or service calls.
   - **Impact**: Storage state may be inconsistent, potentially affecting carrier routing and storage.

5. **Validation Issues**:
   - **Symptom**: WeldOrder creation or update fails with validation errors.
   - **Cause**: The WeldOrder data does not meet the required format or constraints.
   - **Impact**: Operators cannot create or update WeldOrders, potentially affecting weld line operations.

### Debugging Steps

1. **Role-Based Access Control Issues**:
   - Enable DEBUG level logging for the `WeldOrderController` class.
   - Check the methods that implement role-based access control, such as `getWeldorders`, `getWeldordersDelivering`, and `getWeldordersPending`.
   - Verify that the security context is correctly accessed and that the user's authorities are correctly retrieved.
   - Check for exceptions in the application logs related to security context access.
   - Consider implementing additional logging to track role-based access control decisions.

2. **Order Status Management Issues**:
   - Enable DEBUG level logging for the `WeldOrderController` class.
   - Check the methods that manage order status, such as `update`, `manuallyCompleteWeldOrder`, and `cancelWeldOrder`.
   - Verify that order status constraints are correctly enforced, such as preventing status changes when certain conditions are not met.
   - Check for exceptions in the application logs related to order status management.
   - Consider implementing additional validation for order status changes.

3. **Order Fulfillment Issues**:
   - Enable DEBUG level logging for the `WeldOrderController` class.
   - Check the methods that manage order fulfillment, such as `updateOrderFulfillment`.
   - Verify that delivered quantities are correctly calculated and updated.
   - Check for exceptions in the application logs related to order fulfillment.
   - Consider implementing additional validation for order fulfillment operations.

4. **Storage State Refresh Issues**:
   - Enable DEBUG level logging for the `WeldOrderController` class and the `CarrierManagementServiceProxy`.
   - Check the methods that refresh the storage state, such as `manuallyCompleteWeldOrder`.
   - Verify that the `refreshStorageState` method is correctly called on the `CarrierManagementServiceProxy`.
   - Check for exceptions in the application logs related to storage state refresh.
   - Consider implementing additional validation to ensure that the storage state is correctly refreshed.

5. **Validation Issues**:
   - Enable DEBUG level logging for the `WeldOrderController` class.
   - Check the methods that handle WeldOrder creation and update, such as `create` and `update`.
   - Verify that the WeldOrder data is correctly validated and that appropriate error messages are displayed.
   - Check for exceptions in the application logs related to validation.
   - Consider implementing additional validation for WeldOrder data.

### Resolution

1. **Role-Based Access Control Issues**:
   - Implement more robust role-based access control with proper error handling.
   - Add more informative error messages to help users understand access control decisions.
   - Consider implementing a more user-friendly interface for role-based access control.
   - Add logging to track role-based access control decisions and identify issues.
   - Consider implementing a role-based access control helper to centralize and standardize access control logic.

2. **Order Status Management Issues**:
   - Implement more robust order status management with proper error handling.
   - Add more informative error messages to help operators understand status constraints.
   - Consider implementing a more user-friendly interface for order status management.
   - Add logging to track order status changes and identify issues.
   - Consider implementing an order status management helper to centralize and standardize status logic.

3. **Order Fulfillment Issues**:
   - Implement more robust order fulfillment logic with proper error handling.
   - Add more informative error messages to help operators understand fulfillment operations.
   - Consider implementing a more user-friendly interface for order fulfillment.
   - Add logging to track order fulfillment operations and identify issues.
   - Consider implementing an order fulfillment helper to centralize and standardize fulfillment logic.

4. **Storage State Refresh Issues**:
   - Implement more robust storage state refresh logic with proper error handling.
   - Add validation to ensure that the storage state is correctly refreshed.
   - Consider implementing a more reliable mechanism for refreshing the storage state.
   - Add logging to track storage state refresh operations and identify issues.
   - Consider implementing a storage state refresh helper to centralize and standardize refresh logic.

5. **Validation Issues**:
   - Implement more robust validation logic with proper error handling.
   - Add more informative error messages to help operators identify and correct validation issues.
   - Consider implementing client-side validation to catch issues before form submission.
   - Add logging to track validation failures and identify patterns.
   - Consider implementing a validation helper to centralize and standardize validation logic.

### Monitoring

1. **Role-Based Access Control Monitoring**:
   - Monitor role-based access control decisions to detect issues with access control logic.
   - Track the distribution of user roles and access patterns to identify potential issues.
   - Set up alerts for unusual access patterns that may indicate issues.

2. **Order Status Monitoring**:
   - Monitor order status changes to detect issues with status management logic.
   - Track the distribution of order statuses to identify potential issues.
   - Set up alerts for unusual status patterns that may indicate issues.

3. **Order Fulfillment Monitoring**:
   - Monitor order fulfillment operations to detect issues with fulfillment logic.
   - Track the distribution of fulfilled orders to identify potential issues.
   - Set up alerts for unusual fulfillment patterns that may indicate issues.

4. **Storage State Refresh Monitoring**:
   - Monitor storage state refresh operations to detect issues with refresh logic.
   - Track the frequency and timing of refresh operations to identify potential issues.
   - Set up alerts for failed refresh operations that may indicate issues.

5. **Validation Monitoring**:
   - Monitor validation operations to detect issues with data validation.
   - Track the distribution of validation failures to identify potential issues.
   - Set up alerts for unusual validation patterns that may indicate issues.
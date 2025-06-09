# Process Points in the Sales Interface Service

## Overview

Process points are unique identifiers used in Honda's GALC (Global Assembly Line Control) system to track vehicles as they move through different stages of the sales and distribution process. The `AH-processpoint-config.pdf` file contains the configuration of these process points, which are essential for the Sales Interface Service to properly track vehicle status changes.

## What Are Process Points?

Process points are represented by alphanumeric codes (e.g., `AAH1RE1P00101`) that serve as markers for specific stages in a vehicle's journey from production to dealer delivery. Each process point:

1. Has a unique ID
2. Corresponds to a specific status in the vehicle lifecycle
3. Is used to update the GALC system when a vehicle reaches that stage
4. Enables tracking and reporting on vehicle movement

## Process Point Structure

The process point IDs follow a specific pattern:
- First 3 characters: Typically indicate the facility or system (e.g., `AAH` for American Honda)
- Middle characters: Indicate the process type and location
- Last characters: Sequence numbers for the specific process

## Key Process Points

Based on the application.properties file and code analysis, the following process points are defined in the system:

| Status Type | Process Point ID | Description | Purpose |
|-------------|------------------|-------------|---------|
| AH-RCVD | AAH1RE1P00101 | American Honda Receiving | Marks when a vehicle is received at American Honda facility |
| DLR-ASGN | AAH1SC1P00101 | Dealer Assignment | Marks when a vehicle is assigned to a dealer (FTZ Release) |
| AH-SHIP | AAH1DC1P00101 | American Honda Shipping | Marks when a vehicle is shipped from American Honda (FTZ Confirmation) |
| AH-RTN | AVQ1FR1P00101 | Factory Return | Marks when a vehicle is returned to the factory |
| PPO-ON | AAH1PP1P00101 | Post-Production Option ON | Marks when a vehicle enters post-production options installation |
| PPO-OFF | AAH1PP1P00102 | Post-Production Option OFF | Marks when a vehicle completes post-production options installation |
| ON-TRN | AAH1SC1P00103 | Loaded to Train | Marks when a vehicle is loaded onto a train for transport |
| SHIPPER | AAH1SC1P00102 | Shipper | Marks when a vehicle is assigned to a shipping carrier |
| DLR-RCPT | AAH1DL1P00102 | Dealer Receipt | Marks when a vehicle is received by a dealer |
| DLR-RTN | AAH1DL1P00101 | Dealer Return | Marks when a vehicle is returned by a dealer |
| AF-OFF | AAF1OF1PQ0111 | Assembly Off | Marks when a vehicle completes assembly |

## How Process Points Are Used

### 1. Status Tracking

When a vehicle changes status, the corresponding process point is used to update the GALC system:

```java
// Example from AhReceiveMessageHandler.java
shippingStatusService.trackProduct(galcUrl, 
    propertyUtil.getProcessPoint(StatusEnum.AH_RCVD.getType()), 
    statusVehicle.getVin());
```

### 2. Vehicle Lifecycle Management

Process points help maintain an accurate record of where each vehicle is in the sales process:

```java
// Status progression example
if (shippingStatus.getStatus() == StatusEnum.VQ_SHIP.getStatus()) {
    // Update to next status
    shippingStatus.setStatus(StatusEnum.AH_RCVD.getStatus());
    // Track with appropriate process point
    shippingStatusService.trackProduct(galcUrl, 
        propertyUtil.getProcessPoint(StatusEnum.AH_RCVD.getType()), 
        statusVehicle.getVin());
}
```

### 3. Data Integration

Process points are sent to the GALC system via REST API calls:

```java
// From BaseGalcService.java
public void trackProduct(String galcUrl, String processPointId, String productId) {
    // Create JSON payload with process point
    JSONObject jsonObject3 = new JSONObject();
    jsonObject3.put("java.lang.String", processPointId);
    
    // Send to GALC system
    getRestTemplate().postForLocation(
        getExternalSystemUrl(galcUrl, GalcDataType.TRACKING_SERVICE.getDao(), "track"),
        jsonObjects.toString());
}
```

## Process Point Flow

The process points follow a logical sequence that represents the vehicle's journey:

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│  AF-OFF     │────>│  VQ-SHIP    │────>│  AH-RCVD    │────>│  DLR-ASGN   │
│ AAF1OF1PQ0111│     │ (No PP ID)  │     │ AAH1RE1P00101│     │ AAH1SC1P00101│
└─────────────┘     └─────────────┘     └─────────────┘     └─────────────┘
      │                                                             │
      │                                                             ▼
      │                                                     ┌─────────────┐
      │                                                     │  AH-SHIP    │
      │                                                     │ AAH1DC1P00101│
      │                                                     └─────────────┘
      │                                                             │
      │                                                             ▼
      │                                                     ┌─────────────┐
      │                                                     │  SHIPPER    │
      │                                                     │ AAH1SC1P00102│
      │                                                     └─────────────┘
      │                                                             │
      │                                                             ▼
      │                                                     ┌─────────────┐
      │                                                     │  ON-TRN     │
      │                                                     │ AAH1SC1P00103│
      │                                                     └─────────────┘
      │                                                             │
      │                                                             ▼
      │                                                     ┌─────────────┐
      │                                                     │  DLR-RCPT   │
      │                                                     │ AAH1DL1P00102│
      │                                                     └─────────────┘
      │                                                             │
      ▼                                                             ▼
┌─────────────┐                                            ┌─────────────┐
│  AH-RTN     │<───────────────────────────────────────────│  DLR-RTN    │
│ AVQ1FR1P00101│                                            │ AAH1DL1P00101│
└─────────────┘                                            └─────────────┘
```

Alternative paths include:

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│  AH-RCVD    │────>│  PPO-ON     │────>│  PPO-OFF    │────> Continue normal flow
│ AAH1RE1P00101│     │ AAH1PP1P00101│     │ AAH1PP1P00102│
└─────────────┘     └─────────────┘     └─────────────┘
```

```
┌─────────────┐     ┌─────────────┐
│  Any Status │────>│  AH-PCHG    │────> Continue normal flow
│             │     │ (Park Change)│
└─────────────┘     └─────────────┘
```

## Special Process Points

### Factory Return (AH-RTN)

The Factory Return process point (AVQ1FR1P00101) is special because it:
- Has a negative status value (-1) indicating it's outside the normal flow
- Can be triggered from multiple points in the process
- Requires additional handling for NAQ (Non-Acceptable Quality) updates:

```java
// From FactoryReturnMessageHandler.java
if (propertyUtil.updateNaqEnable()) {
    naqDefectAndParkingService.updateNaqDefect(
        galcUrl,
        statusVehicle.getVin(),
        propertyUtil.getPropertyByDefectName(),
        propertyUtil.getPropertyByRepairName(),
        propertyUtil.getProcessPoint(StatusEnum.AH_RTN.getType()),
        propertyUtil.getBackoutPartList()
    );
}
```

### Post-Production Options (PPO-ON/PPO-OFF)

These process points track vehicles that require additional options to be installed after the main production:
- PPO-ON (AAH1PP1P00101): Marks entry into the post-production options area
- PPO-OFF (AAH1PP1P00102): Marks completion of post-production options installation

## Integration with Other Systems

The process points are primarily used to integrate with the GALC system:

1. **GALC Integration**: Process points are sent to GALC via REST API calls to update vehicle status
2. **YMS Integration**: Status updates from YMS trigger the use of process points to update GALC
3. **Reporting**: Process points enable accurate reporting on vehicle location and status

## Debugging Process Point Issues

If there are issues with process points not being recognized or status updates failing, check:

1. **Configuration**: Verify the process point IDs in application.properties match those expected by GALC
2. **Logs**: Look for tracking errors in the application logs
3. **GALC System**: Check if the GALC system is accepting the process point updates
4. **Status Flow**: Ensure the vehicle is in an appropriate status to receive the new process point

### Example Debugging Commands

```bash
# Check if process points are being sent correctly
grep "Tracking completed" /var/logs/application/lc-product-service.log

# Look for tracking errors
grep "Error Tracking" /var/logs/application/lc-product-service.log

# Check specific process point usage
grep "AAH1RE1P00101" /var/logs/application/lc-product-service.log
```

## Summary

Process points are a critical component of the Sales Interface Service, enabling accurate tracking of vehicles through the sales and distribution process. The `AH-processpoint-config.pdf` file contains the configuration of these process points, which are referenced in the application.properties file and used throughout the codebase to update vehicle status in the GALC system.

Understanding these process points is essential for:
1. Troubleshooting status update issues
2. Tracking vehicles through the sales process
3. Ensuring proper integration between YMS and GALC
4. Maintaining accurate vehicle status records
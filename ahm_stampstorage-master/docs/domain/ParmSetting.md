# ParmSetting Technical Documentation

## Purpose
ParmSetting.java defines an entity that represents system configuration parameters in the stamping system. This class provides a flexible way to store and retrieve key-value pairs for various system settings, allowing for runtime configuration without code changes. It serves as a centralized repository for application parameters, including descriptions and audit information about when and by whom settings were last modified.

## Logic/Functionality
- Stores configuration parameters as key-value pairs
- Provides descriptive information for each parameter
- Tracks who updated each parameter and when
- Implements JPA entity functionality for database persistence
- Offers static finder methods to retrieve parameters by name or ID
- Supports CRUD operations for parameter management

## Flow
1. System parameters are defined and stored in the database as ParmSetting entities
2. Application components retrieve parameter values by field name
3. Parameters can be updated through the application interface
4. Changes to parameters are tracked with user and timestamp information
5. The system uses parameter values to control various behaviors and configurations

## Key Elements
- `fieldname`: The name/key of the parameter
- `fieldvalue`: The value of the parameter
- `description`: A human-readable description of the parameter's purpose
- `updatedby`: The user who last updated the parameter
- `updatetstp`: The timestamp when the parameter was last updated
- Static finder methods for retrieving parameters
- Standard JPA entity methods for persistence operations

## Usage
```java
// Find a parameter by field name
ParmSetting maxRetryParam = ParmSetting.findParmSettingsByFieldName("MAX_RETRY_COUNT");
System.out.println("Parameter: " + maxRetryParam.getFieldname());
System.out.println("Value: " + maxRetryParam.getFieldvalue());
System.out.println("Description: " + maxRetryParam.getDescription());
System.out.println("Last updated by: " + maxRetryParam.getUpdatedby());
System.out.println("Last updated on: " + maxRetryParam.getUpdatetstp());

// Convert parameter value to appropriate type
int maxRetryCount = Integer.parseInt(maxRetryParam.getFieldvalue());
System.out.println("Max retry count: " + maxRetryCount);

// Find all parameters
List<ParmSetting> allParams = ParmSetting.findAllParmSettings();
System.out.println("Total parameters: " + allParams.size());

// Create a new parameter
ParmSetting newParam = new ParmSetting();
newParam.setFieldname("NOTIFICATION_ENABLED");
newParam.setFieldvalue("true");
newParam.setDescription("Enable or disable system notifications");
newParam.setUpdatedby("admin");
newParam.setUpdatetstp(new Timestamp(System.currentTimeMillis()));
newParam.persist();

// Update an existing parameter
ParmSetting timeoutParam = ParmSetting.findParmSettingsByFieldName("CONNECTION_TIMEOUT");
timeoutParam.setFieldvalue("30000");
timeoutParam.setUpdatedby("admin");
timeoutParam.setUpdatetstp(new Timestamp(System.currentTimeMillis()));
timeoutParam.merge();

// Delete a parameter
ParmSetting obsoleteParam = ParmSetting.findParmSettingsByFieldName("OBSOLETE_SETTING");
if (obsoleteParam != null) {
    obsoleteParam.remove();
}

// Find parameters by pagination
List<ParmSetting> pagedParams = ParmSetting.findParmSettingEntries(0, 10);
System.out.println("First 10 parameters:");
for (ParmSetting param : pagedParams) {
    System.out.println("  " + param.getFieldname() + ": " + param.getFieldvalue());
}
```

## Debugging and Production Support

### Common Issues
1. Missing critical parameters
2. Invalid parameter values
3. Inconsistent parameter types
4. Duplicate parameter names
5. Stale parameter values
6. Unauthorized parameter changes
7. Performance issues with parameter retrieval

### Debugging Steps
1. Verify critical parameters exist:
   ```java
   // Check for required parameters
   System.out.println("Checking for required parameters:");
   
   String[] requiredParams = {
       "CONNECTION_TIMEOUT",
       "MAX_RETRY_COUNT",
       "NOTIFICATION_ENABLED",
       "DEFAULT_STORAGE_AREA",
       "ALARM_CHECK_INTERVAL",
       "MAX_CARRIER_COUNT"
   };
   
   for (String paramName : requiredParams) {
       try {
           ParmSetting param = ParmSetting.findParmSettingsByFieldName(paramName);
           if (param != null) {
               System.out.println("  " + paramName + ": " + param.getFieldvalue());
           } else {
               System.out.println("  WARNING: Required parameter not found: " + paramName);
           }
       } catch (Exception e) {
           System.out.println("  ERROR checking parameter " + paramName + ": " + e.getMessage());
       }
   }
   ```

2. Validate parameter values:
   ```java
   // Validate parameter values
   System.out.println("Validating parameter values:");
   
   // Define parameter validators
   Map<String, Predicate<String>> validators = new HashMap<>();
   validators.put("CONNECTION_TIMEOUT", value -> {
       try {
           int timeout = Integer.parseInt(value);
           return timeout > 0 && timeout <= 300000; // 0-5 minutes
       } catch (NumberFormatException e) {
           return false;
       }
   });
   
   validators.put("MAX_RETRY_COUNT", value -> {
       try {
           int count = Integer.parseInt(value);
           return count >= 0 && count <= 10;
       } catch (NumberFormatException e) {
           return false;
       }
   });
   
   validators.put("NOTIFICATION_ENABLED", value -> 
       value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false"));
   
   // Validate all parameters with defined validators
   for (Map.Entry<String, Predicate<String>> entry : validators.entrySet()) {
       String paramName = entry.getKey();
       Predicate<String> validator = entry.getValue();
       
       try {
           ParmSetting param = ParmSetting.findParmSettingsByFieldName(paramName);
           if (param != null) {
               boolean valid = validator.test(param.getFieldvalue());
               System.out.println("  " + paramName + ": " + param.getFieldvalue() + 
                                 " - " + (valid ? "Valid" : "INVALID"));
               
               if (!valid) {
                   System.out.println("    WARNING: Parameter has invalid value");
               }
           } else {
               System.out.println("  " + paramName + ": Not found");
           }
       } catch (Exception e) {
           System.out.println("  ERROR validating " + paramName + ": " + e.getMessage());
       }
   }
   ```

3. Check for duplicate parameters:
   ```java
   // Check for duplicate parameter names
   System.out.println("Checking for duplicate parameter names:");
   
   List<ParmSetting> allParams = ParmSetting.findAllParmSettings();
   Map<String, List<ParmSetting>> paramsByName = new HashMap<>();
   
   for (ParmSetting param : allParams) {
       String fieldName = param.getFieldname();
       if (!paramsByName.containsKey(fieldName)) {
           paramsByName.put(fieldName, new ArrayList<>());
       }
       paramsByName.get(fieldName).add(param);
   }
   
   boolean hasDuplicates = false;
   for (Map.Entry<String, List<ParmSetting>> entry : paramsByName.entrySet()) {
       if (entry.getValue().size() > 1) {
           hasDuplicates = true;
           System.out.println("  WARNING: Duplicate parameter found: " + entry.getKey());
           System.out.println("    Count: " + entry.getValue().size());
           
           for (ParmSetting param : entry.getValue()) {
               System.out.println("    ID: " + param.getId() + 
                                 ", Value: " + param.getFieldvalue() + 
                                 ", Updated by: " + param.getUpdatedby() + 
                                 ", Updated on: " + param.getUpdatetstp());
           }
       }
   }
   
   if (!hasDuplicates) {
       System.out.println("  No duplicate parameters found");
   }
   ```

4. Check for stale parameters:
   ```java
   // Check for stale parameters
   System.out.println("Checking for stale parameters:");
   
   // Define the threshold for stale parameters (e.g., 180 days)
   long staleThresholdMillis = 180L * 24 * 60 * 60 * 1000;
   Timestamp now = new Timestamp(System.currentTimeMillis());
   
   for (ParmSetting param : ParmSetting.findAllParmSettings()) {
       Timestamp lastUpdate = param.getUpdatetstp();
       if (lastUpdate != null) {
           long ageMillis = now.getTime() - lastUpdate.getTime();
           if (ageMillis > staleThresholdMillis) {
               System.out.println("  WARNING: Stale parameter found: " + param.getFieldname());
               System.out.println("    Last updated: " + lastUpdate);
               System.out.println("    Age: " + (ageMillis / (24 * 60 * 60 * 1000)) + " days");
           }
       } else {
           System.out.println("  WARNING: Parameter has null update timestamp: " + 
                             param.getFieldname());
       }
   }
   ```

5. Check for unauthorized changes:
   ```java
   // Check for unauthorized parameter changes
   System.out.println("Checking for unauthorized parameter changes:");
   
   // Define authorized users for parameter updates
   Set<String> authorizedUsers = new HashSet<>(Arrays.asList(
       "admin", "system", "supervisor", "maintainer"
   ));
   
   for (ParmSetting param : ParmSetting.findAllParmSettings()) {
       String updatedBy = param.getUpdatedby();
       if (updatedBy != null && !authorizedUsers.contains(updatedBy.toLowerCase())) {
           System.out.println("  WARNING: Parameter updated by unauthorized user: " + 
                             param.getFieldname());
           System.out.println("    Updated by: " + updatedBy);
           System.out.println("    Updated on: " + param.getUpdatetstp());
       }
   }
   ```

6. Test parameter retrieval performance:
   ```java
   // Test parameter retrieval performance
   System.out.println("Testing parameter retrieval performance:");
   
   // Test findAllParmSettings
   long startTime = System.currentTimeMillis();
   List<ParmSetting> allParamsPerf = ParmSetting.findAllParmSettings();
   long endTime = System.currentTimeMillis();
   System.out.println("  findAllParmSettings: " + (endTime - startTime) + 
                     "ms for " + allParamsPerf.size() + " parameters");
   
   // Test findParmSettingsByFieldName for a few parameters
   String[] testParams = {
       "CONNECTION_TIMEOUT",
       "MAX_RETRY_COUNT",
       "NOTIFICATION_ENABLED"
   };
   
   for (String paramName : testParams) {
       startTime = System.currentTimeMillis();
       try {
           ParmSetting param = ParmSetting.findParmSettingsByFieldName(paramName);
           endTime = System.currentTimeMillis();
           
           System.out.println("  findParmSettingsByFieldName(" + paramName + "): " + 
                             (endTime - startTime) + "ms, found: " + 
                             (param != null ? "Yes" : "No"));
       } catch (Exception e) {
           endTime = System.currentTimeMillis();
           System.out.println("  findParmSettingsByFieldName(" + paramName + "): " + 
                             (endTime - startTime) + "ms, ERROR: " + e.getMessage());
       }
   }
   
   // Test findParmSetting by ID
   if (!allParamsPerf.isEmpty()) {
       ParmSetting sampleParam = allParamsPerf.get(0);
       startTime = System.currentTimeMillis();
       ParmSetting foundParam = ParmSetting.findParmSetting(sampleParam.getId());
       endTime = System.currentTimeMillis();
       
       System.out.println("  findParmSetting(ID): " + (endTime - startTime) + 
                         "ms, found: " + (foundParam != null ? "Yes" : "No"));
   }
   ```

7. Check for inconsistent parameter types:
   ```java
   // Check for inconsistent parameter types
   System.out.println("Checking for inconsistent parameter types:");
   
   // Define expected parameter types
   Map<String, String> expectedTypes = new HashMap<>();
   expectedTypes.put("CONNECTION_TIMEOUT", "integer");
   expectedTypes.put("MAX_RETRY_COUNT", "integer");
   expectedTypes.put("NOTIFICATION_ENABLED", "boolean");
   expectedTypes.put("DEFAULT_STORAGE_AREA", "string");
   expectedTypes.put("ALARM_CHECK_INTERVAL", "integer");
   expectedTypes.put("MAX_CARRIER_COUNT", "integer");
   
   for (Map.Entry<String, String> entry : expectedTypes.entrySet()) {
       String paramName = entry.getKey();
       String expectedType = entry.getValue();
       
       try {
           ParmSetting param = ParmSetting.findParmSettingsByFieldName(paramName);
           if (param != null) {
               String value = param.getFieldvalue();
               boolean typeValid = false;
               
               switch (expectedType) {
                   case "integer":
                       try {
                           Integer.parseInt(value);
                           typeValid = true;
                       } catch (NumberFormatException e) {
                           typeValid = false;
                       }
                       break;
                   case "boolean":
                       typeValid = value.equalsIgnoreCase("true") || 
                                  value.equalsIgnoreCase("false");
                       break;
                   case "string":
                       typeValid = true; // All values are valid strings
                       break;
                   default:
                       typeValid = false;
               }
               
               System.out.println("  " + paramName + ": " + value + 
                                 " - Expected type: " + expectedType + 
                                 ", Valid: " + (typeValid ? "Yes" : "No"));
               
               if (!typeValid) {
                   System.out.println("    WARNING: Parameter has inconsistent type");
               }
           } else {
               System.out.println("  " + paramName + ": Not found");
           }
       } catch (Exception e) {
           System.out.println("  ERROR checking " + paramName + ": " + e.getMessage());
       }
   }
   ```

### Resolution
- For missing parameters: Add the required parameters with appropriate values
- For invalid values: Update parameters with valid values
- For inconsistent types: Correct parameter values to match expected types
- For duplicate parameters: Remove duplicate entries and keep only one
- For stale parameters: Review and update parameters as needed
- For unauthorized changes: Implement proper access controls
- For performance issues: Consider caching frequently used parameters

### Monitoring
- Track parameter changes with timestamps and user information
- Monitor for unauthorized parameter updates
- Track parameter retrieval performance
- Monitor for missing critical parameters
- Set up alerts for invalid parameter values
- Track parameter usage patterns
- Monitor for duplicate parameter entries
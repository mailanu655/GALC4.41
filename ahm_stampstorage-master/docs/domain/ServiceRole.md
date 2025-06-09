# ServiceRole Technical Documentation

## Purpose
ServiceRole.java defines an entity that represents service roles in the stamping system's distributed architecture. This class manages information about service instances, their network locations, and their operational status. It supports high availability through primary/backup designations and failover mechanisms, enabling the system to maintain service continuity even when individual service instances fail.

## Logic/Functionality
- Tracks service instances and their network locations (IP address, port, hostname)
- Manages service role designations (primary vs. backup)
- Supports failover ordering for high availability
- Tracks the current active status of services
- Implements JPA entity functionality for database persistence
- Provides methods to find and manage service role records
- Supports querying for services by various criteria

## Flow
1. Service instances register themselves in the system with a specific role
2. The system designates primary and backup roles based on configuration
3. Service health is monitored and active status is updated
4. When a primary service fails, the system can identify backup services based on failover order
5. Services can query for other services to establish communication

## Key Elements
- `serviceName`: The name of the service
- `ip`: The IP address where the service is running
- `port`: The port number the service is listening on
- `hostName`: The hostname of the server running the service
- `designatedPrimary`: Whether this service instance is designated as primary
- `currentActive`: Whether this service instance is currently active
- `failoverOrder`: The order in which this service should be activated during failover
- Static finder methods for retrieving service role records
- Standard JPA entity methods for persistence operations

## Usage
```java
// Find a ServiceRole by ID
ServiceRole role = ServiceRole.findServiceRole(1L);
System.out.println("Service: " + role.getServiceName());
System.out.println("IP: " + role.getIp());
System.out.println("Port: " + role.getPort());
System.out.println("Hostname: " + role.getHostName());
System.out.println("Designated Primary: " + role.getDesignatedPrimary());
System.out.println("Current Active: " + role.getCurrentActive());
System.out.println("Failover Order: " + role.getFailoverOrder());

// Find all ServiceRoles
List<ServiceRole> allRoles = ServiceRole.findAllServiceRoles();
System.out.println("Total service roles: " + allRoles.size());

// Find ServiceRoles by service name
List<ServiceRole> storageRoles = ServiceRole.findServiceRolesByServiceNameEquals("StorageService")
                                           .getResultList();
System.out.println("Storage service roles: " + storageRoles.size());
for (ServiceRole storageRole : storageRoles) {
    System.out.println("  " + storageRole.getIp() + ":" + storageRole.getPort() + 
                      " (Active: " + storageRole.getCurrentActive() + ")");
}

// Find active ServiceRoles
List<ServiceRole> activeRoles = ServiceRole.findServiceRolesByCurrentActiveNot(false)
                                          .getResultList();
System.out.println("Active service roles: " + activeRoles.size());
for (ServiceRole activeRole : activeRoles) {
    System.out.println("  " + activeRole.getServiceName() + " at " + 
                      activeRole.getIp() + ":" + activeRole.getPort());
}

// Find ServiceRoles by hostname
List<ServiceRole> hostRoles = ServiceRole.findServiceRolesByHostNameEquals("server1.example.com")
                                        .getResultList();
System.out.println("Service roles on server1: " + hostRoles.size());

// Find ServiceRoles by IP and port
List<ServiceRole> ipPortRoles = ServiceRole.findServiceRolesByIpEqualsAndPort("192.168.1.10", 8080)
                                          .getResultList();
System.out.println("Service roles at 192.168.1.10:8080: " + ipPortRoles.size());

// Find ServiceRoles ordered by failover order
List<ServiceRole> orderedRoles = ServiceRole.findAllServiceRolesOrderByFailoverOrder();
System.out.println("Service roles ordered by failover:");
for (ServiceRole orderedRole : orderedRoles) {
    System.out.println("  Order " + orderedRole.getFailoverOrder() + ": " + 
                      orderedRole.getServiceName() + " at " + 
                      orderedRole.getIp() + ":" + orderedRole.getPort());
}

// Create a new ServiceRole
ServiceRole newRole = new ServiceRole();
newRole.setServiceName("NewService");
newRole.setIp("192.168.1.20");
newRole.setPort(9090);
newRole.setHostName("server2.example.com");
newRole.setDesignatedPrimary(false);
newRole.setCurrentActive(true);
newRole.setFailoverOrder(3);
newRole.persist();

// Update an existing ServiceRole
ServiceRole existingRole = ServiceRole.findServiceRolesByServiceNameEquals("StorageService")
                                     .getResultList().get(0);
existingRole.setCurrentActive(false);
existingRole.merge();

// Delete a ServiceRole
ServiceRole obsoleteRole = ServiceRole.findServiceRole(2L);
if (obsoleteRole != null) {
    obsoleteRole.remove();
}
```

## Debugging and Production Support

### Common Issues
1. Service role inconsistencies (multiple active primaries)
2. Inactive services not being detected
3. Failover not occurring properly
4. Network connectivity issues between services
5. Incorrect service role configurations
6. Stale service role data
7. Missing service roles

### Debugging Steps
1. Check for service role inconsistencies:
   ```java
   // Check for service role inconsistencies
   System.out.println("Checking for service role inconsistencies:");
   
   Map<String, List<ServiceRole>> rolesByService = new HashMap<>();
   
   for (ServiceRole role : ServiceRole.findAllServiceRoles()) {
       String serviceName = role.getServiceName();
       if (!rolesByService.containsKey(serviceName)) {
           rolesByService.put(serviceName, new ArrayList<>());
       }
       rolesByService.get(serviceName).add(role);
   }
   
   for (Map.Entry<String, List<ServiceRole>> entry : rolesByService.entrySet()) {
       String serviceName = entry.getKey();
       List<ServiceRole> roles = entry.getValue();
       
       System.out.println("Service: " + serviceName + " (" + roles.size() + " instances)");
       
       // Check for multiple active instances
       int activeCount = 0;
       for (ServiceRole role : roles) {
           if (role.getCurrentActive() != null && role.getCurrentActive()) {
               activeCount++;
           }
       }
       
       if (activeCount == 0) {
           System.out.println("  WARNING: No active instances for " + serviceName);
       } else if (activeCount > 1) {
           System.out.println("  WARNING: Multiple active instances for " + serviceName + 
                             " (" + activeCount + ")");
           
           for (ServiceRole role : roles) {
               if (role.getCurrentActive() != null && role.getCurrentActive()) {
                   System.out.println("    Active: " + role.getIp() + ":" + role.getPort() + 
                                     " on " + role.getHostName() + 
                                     " (Designated Primary: " + role.getDesignatedPrimary() + ")");
               }
           }
       }
       
       // Check for multiple designated primaries
       int primaryCount = 0;
       for (ServiceRole role : roles) {
           if (role.getDesignatedPrimary() != null && role.getDesignatedPrimary()) {
               primaryCount++;
           }
       }
       
       if (primaryCount == 0) {
           System.out.println("  WARNING: No designated primary for " + serviceName);
       } else if (primaryCount > 1) {
           System.out.println("  WARNING: Multiple designated primaries for " + serviceName + 
                             " (" + primaryCount + ")");
           
           for (ServiceRole role : roles) {
               if (role.getDesignatedPrimary() != null && role.getDesignatedPrimary()) {
                   System.out.println("    Primary: " + role.getIp() + ":" + role.getPort() + 
                                     " on " + role.getHostName() + 
                                     " (Currently Active: " + role.getCurrentActive() + ")");
               }
           }
       }
       
       // Check for primary not being active
       boolean primaryNotActive = false;
       for (ServiceRole role : roles) {
           if (role.getDesignatedPrimary() != null && role.getDesignatedPrimary() && 
               (role.getCurrentActive() == null || !role.getCurrentActive())) {
               primaryNotActive = true;
               System.out.println("  WARNING: Designated primary is not active for " + 
                                 serviceName);
               System.out.println("    Primary: " + role.getIp() + ":" + role.getPort() + 
                                 " on " + role.getHostName());
           }
       }
       
       // Check for non-primary being active when primary exists
       if (!primaryNotActive) {
           boolean hasPrimary = false;
           boolean nonPrimaryActive = false;
           ServiceRole primary = null;
           
           for (ServiceRole role : roles) {
               if (role.getDesignatedPrimary() != null && role.getDesignatedPrimary()) {
                   hasPrimary = true;
                   primary = role;
               } else if (role.getCurrentActive() != null && role.getCurrentActive()) {
                   nonPrimaryActive = true;
               }
           }
           
           if (hasPrimary && nonPrimaryActive && primary.getCurrentActive()) {
               System.out.println("  WARNING: Non-primary instance active when primary is also active for " + 
                                 serviceName);
               
               for (ServiceRole role : roles) {
                   if (role.getCurrentActive() != null && role.getCurrentActive()) {
                       System.out.println("    Active: " + role.getIp() + ":" + role.getPort() + 
                                         " on " + role.getHostName() + 
                                         " (Designated Primary: " + role.getDesignatedPrimary() + ")");
                   }
               }
           }
       }
   }
   ```

2. Check for failover configuration:
   ```java
   // Check failover configuration
   System.out.println("Checking failover configuration:");
   
   for (String serviceName : rolesByService.keySet()) {
       List<ServiceRole> roles = rolesByService.get(serviceName);
       
       // Check failover order
       Map<Integer, List<ServiceRole>> rolesByFailoverOrder = new HashMap<>();
       
       for (ServiceRole role : roles) {
           int failoverOrder = role.getFailoverOrder();
           if (!rolesByFailoverOrder.containsKey(failoverOrder)) {
               rolesByFailoverOrder.put(failoverOrder, new ArrayList<>());
           }
           rolesByFailoverOrder.get(failoverOrder).add(role);
       }
       
       System.out.println("Service: " + serviceName);
       
       // Check for missing failover orders
       List<Integer> orders = new ArrayList<>(rolesByFailoverOrder.keySet());
       Collections.sort(orders);
       
       if (orders.isEmpty()) {
           System.out.println("  WARNING: No failover orders defined");
       } else {
           boolean hasMissingOrders = false;
           for (int i = 1; i < orders.get(orders.size() - 1); i++) {
               if (!orders.contains(i)) {
                   hasMissingOrders = true;
                   System.out.println("  WARNING: Missing failover order " + i);
               }
           }
           
           if (!hasMissingOrders) {
               System.out.println("  Failover orders are sequential");
           }
       }
       
       // Check for duplicate failover orders
       for (Map.Entry<Integer, List<ServiceRole>> orderEntry : rolesByFailoverOrder.entrySet()) {
           if (orderEntry.getValue().size() > 1) {
               System.out.println("  WARNING: Multiple instances with failover order " + 
                                 orderEntry.getKey() + " (" + orderEntry.getValue().size() + ")");
               
               for (ServiceRole role : orderEntry.getValue()) {
                   System.out.println("    Instance: " + role.getIp() + ":" + role.getPort() + 
                                     " on " + role.getHostName());
               }
           }
       }
   }
   ```

3. Check for network connectivity:
   ```java
   // Check for network connectivity issues
   System.out.println("Checking for network connectivity issues:");
   
   for (ServiceRole role : ServiceRole.findAllServiceRoles()) {
       if (role.getCurrentActive() != null && role.getCurrentActive()) {
           String ip = role.getIp();
           int port = role.getPort();
           
           System.out.println("  Testing connectivity to " + role.getServiceName() + 
                             " at " + ip + ":" + port);
           
           // Simulate connectivity test
           boolean reachable = testConnectivity(ip, port);
           
           if (!reachable) {
               System.out.println("    WARNING: Service marked as active but not reachable");
           } else {
               System.out.println("    Service is reachable");
           }
       }
   }
   
   // Simulated connectivity test method
   private static boolean testConnectivity(String ip, int port) {
       // In a real implementation, this would attempt to connect to the service
       // For this example, we'll just return a simulated result
       try {
           // Simulate network test with Socket
           // Socket socket = new Socket(ip, port);
           // socket.close();
           // return true;
           
           // For documentation purposes, just return true
           return true;
       } catch (Exception e) {
           return false;
       }
   }
   ```

4. Check for stale service data:
   ```java
   // Check for stale service data
   System.out.println("Checking for stale service data:");
   
   // In a real implementation, this would check service heartbeats or timestamps
   // For this example, we'll just check if services are properly configured
   
   for (ServiceRole role : ServiceRole.findAllServiceRoles()) {
       System.out.println("  Service: " + role.getServiceName() + 
                         " at " + role.getIp() + ":" + role.getPort());
       
       // Check for null or empty values
       if (role.getIp() == null || role.getIp().trim().isEmpty()) {
           System.out.println("    WARNING: Missing IP address");
       }
       
       if (role.getPort() <= 0 || role.getPort() > 65535) {
           System.out.println("    WARNING: Invalid port number: " + role.getPort());
       }
       
       if (role.getHostName() == null || role.getHostName().trim().isEmpty()) {
           System.out.println("    WARNING: Missing hostname");
       }
       
       if (role.getDesignatedPrimary() == null) {
           System.out.println("    WARNING: Designated primary flag is null");
       }
       
       if (role.getCurrentActive() == null) {
           System.out.println("    WARNING: Current active flag is null");
       }
   }
   ```

5. Test service role queries:
   ```java
   // Test service role queries
   System.out.println("Testing service role queries:");
   
   // Test findAllServiceRoles
   long startTime = System.currentTimeMillis();
   List<ServiceRole> allRolesPerf = ServiceRole.findAllServiceRoles();
   long endTime = System.currentTimeMillis();
   System.out.println("  findAllServiceRoles: " + (endTime - startTime) + 
                     "ms for " + allRolesPerf.size() + " roles");
   
   // Test findServiceRolesByServiceNameEquals
   Set<String> serviceNames = new HashSet<>();
   for (ServiceRole role : allRolesPerf) {
       if (role.getServiceName() != null) {
           serviceNames.add(role.getServiceName());
       }
   }
   
   for (String serviceName : serviceNames) {
       startTime = System.currentTimeMillis();
       List<ServiceRole> roles = ServiceRole.findServiceRolesByServiceNameEquals(serviceName)
                                           .getResultList();
       endTime = System.currentTimeMillis();
       
       System.out.println("  findServiceRolesByServiceNameEquals for " + serviceName + 
                         ": " + (endTime - startTime) + "ms, found: " + roles.size());
   }
   
   // Test findServiceRolesByCurrentActiveNot
   startTime = System.currentTimeMillis();
   List<ServiceRole> inactiveRoles = ServiceRole.findServiceRolesByCurrentActiveNot(true)
                                               .getResultList();
   endTime = System.currentTimeMillis();
   System.out.println("  findServiceRolesByCurrentActiveNot(true): " + 
                     (endTime - startTime) + "ms, found: " + inactiveRoles.size());
   
   // Test findServiceRole by ID
   if (!allRolesPerf.isEmpty()) {
       ServiceRole sampleRole = allRolesPerf.get(0);
       startTime = System.currentTimeMillis();
       ServiceRole foundRole = ServiceRole.findServiceRole(sampleRole.getId());
       endTime = System.currentTimeMillis();
       
       System.out.println("  findServiceRole(ID): " + (endTime - startTime) + 
                         "ms, found: " + (foundRole != null ? "Yes" : "No"));
   }
   ```

6. Check for missing service roles:
   ```java
   // Check for missing service roles
   System.out.println("Checking for missing service roles:");
   
   // Define expected services
   Set<String> expectedServices = new HashSet<>(Arrays.asList(
       "StorageService",
       "DeviceService",
       "UIService",
       "NotificationService"
   ));
   
   // Check which expected services are missing
   Set<String> foundServices = new HashSet<>();
   for (ServiceRole role : ServiceRole.findAllServiceRoles()) {
       if (role.getServiceName() != null) {
           foundServices.add(role.getServiceName());
       }
   }
   
   for (String expectedService : expectedServices) {
       if (!foundServices.contains(expectedService)) {
           System.out.println("  WARNING: Missing expected service: " + expectedService);
       }
   }
   
   // Check for unexpected services
   for (String foundService : foundServices) {
       if (!expectedServices.contains(foundService)) {
           System.out.println("  INFO: Found unexpected service: " + foundService);
       }
   }
   ```

7. Check for incorrect service role configurations:
   ```java
   // Check for incorrect service role configurations
   System.out.println("Checking for incorrect service role configurations:");
   
   for (ServiceRole role : ServiceRole.findAllServiceRoles()) {
       System.out.println("  Service: " + role.getServiceName() + 
                         " at " + role.getIp() + ":" + role.getPort());
       
       // Check for localhost in production
       if ("127.0.0.1".equals(role.getIp()) || "localhost".equals(role.getHostName())) {
           System.out.println("    WARNING: Service using localhost in production");
       }
       
       // Check for common port conflicts
       int port = role.getPort();
       if (port == 80 || port == 443 || port == 22 || port == 21 || port == 25) {
           System.out.println("    WARNING: Service using well-known port: " + port);
       }
       
       // Check for services on same IP:port
       for (ServiceRole otherRole : ServiceRole.findAllServiceRoles()) {
           if (!role.equals(otherRole) && 
               role.getIp().equals(otherRole.getIp()) && 
               role.getPort() == otherRole.getPort()) {
               
               System.out.println("    WARNING: Port conflict with " + otherRole.getServiceName() + 
                                 " at " + otherRole.getIp() + ":" + otherRole.getPort());
           }
       }
   }
   ```

### Resolution
- For service role inconsistencies: Update service roles to ensure only one active primary per service
- For inactive services: Restart services or update their status
- For failover issues: Correct failover order and ensure proper configuration
- For network connectivity issues: Resolve network problems or update service locations
- For incorrect configurations: Update service role configurations with correct values
- For stale data: Refresh service role data or implement heartbeat mechanism
- For missing service roles: Add required service roles

### Monitoring
- Monitor service health and active status
- Track service role changes
- Monitor for multiple active primaries
- Track failover events
- Monitor network connectivity between services
- Set up alerts for service role inconsistencies
- Track service availability metrics
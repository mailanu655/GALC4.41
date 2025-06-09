# Configuration Files Documentation

This section contains technical documentation for the configuration files used in the StampStorage application. These files define how the application is structured, how it connects to external systems, and how it processes requests.

## Core Configuration Files

| File | Description |
|------|-------------|
| [applicationContext.xml](applicationContext.xml.md) | Main Spring application context configuration that defines core beans, database connectivity, and transaction management |
| [database.properties](database.properties.md) | Database connection parameters for different environments |
| [storage.properties](storage.properties.md) | Service device connection parameters, authentication settings, and operational parameters |
| [webmvc-config.xml](webmvc-config.xml.md) | Spring MVC configuration for web controllers, view resolution, and static resources |

## Service and Device Configuration

| File | Description |
|------|-------------|
| [mes-device.xml](mes-device.xml.md) | Configuration for connecting to the real MES device in production |
| [mes-device-mock.xml](mes-device-mock.xml.md) | Mock implementation of the MES device for development and testing |
| [storage-service.xml](storage-service.xml.md) | Core service components for carrier management and device communication |

## Configuration Relationships

The configuration files have dependencies and relationships that are important to understand:

1. **applicationContext.xml** imports other configuration files:
   - Imports database.properties for database connectivity
   - Imports storage.properties for service parameters
   - Imports storage-service.xml for service components
   - Imports webmvc-config.xml for web framework configuration

2. **storage-service.xml** imports device configuration:
   - Imports either mes-device.xml (production) or mes-device-mock.xml (development/testing)

3. **Property placeholders** are used throughout the configuration files:
   - ${filter.xxx} placeholders in database.properties and storage.properties
   - These are resolved during the build process with environment-specific values

## Configuration Best Practices

When working with these configuration files, follow these best practices:

1. **Environment-specific configuration**:
   - Use filter properties for environment-specific values
   - Keep sensitive information (passwords, credentials) in secure locations
   - Use different configurations for development, testing, and production

2. **Troubleshooting configuration issues**:
   - Check for unresolved property placeholders
   - Verify bean dependencies and initialization order
   - Review application logs for configuration-related errors
   - Test with simplified configurations to isolate issues

3. **Configuration changes**:
   - Document all configuration changes
   - Test configuration changes in development before deploying to production
   - Consider the impact of configuration changes on other components
   - Maintain backward compatibility when possible

## Common Configuration Tasks

| Task | Configuration Files |
|------|---------------------|
| Change database connection | database.properties |
| Modify service device connection | storage.properties, mes-device.xml |
| Update authentication settings | storage.properties |
| Add new web controllers | webmvc-config.xml |
| Configure static resources | webmvc-config.xml |
| Modify service components | storage-service.xml |
| Switch between real and mock devices | storage-service.xml |
# Contact Technical Documentation

## Purpose
Contact.java defines an entity that represents contact information for individuals who need to be notified about alarms and system events in the stamp storage system. This entity stores essential contact details such as names, email addresses, and pager numbers, which are used by the alarm notification system to alert appropriate personnel when issues arise.

## Logic/Functionality
- Represents a contact entity in the system
- Maps to the CONTACT_TBX database table
- Stores contact name, email address, and pager number
- Provides JPA entity mapping for database persistence
- Implements standard CRUD operations for contact management
- Supports transaction management for database operations
- Includes methods for finding and retrieving contacts

## Flow
1. Contacts are created and stored in the database during system setup or configuration
2. When an alarm is configured, contacts are associated with specific alarm definitions
3. When an alarm is triggered, the system retrieves the associated contacts
4. The system uses the contact information to send notifications via email or pager
5. Contacts can be updated or removed as personnel changes occur

## Key Elements
- `id`: The unique identifier for the contact (primary key)
- `contactName`: The name of the contact person
- `email`: The email address for the contact
- `pagerNo`: The pager number for the contact
- JPA entity mapping annotations for database persistence
- Standard CRUD methods (persist, remove, merge, etc.)
- Static finder methods for retrieving contacts

## Usage
```java
// Create a new contact
Contact contact = new Contact();
contact.setContactName("John Smith");
contact.setEmail("john.smith@example.com");
contact.setPagerNo("555-1234");
contact.persist();

// Find a contact by ID
Long contactId = 1L;
Contact foundContact = Contact.findContact(contactId);
if (foundContact != null) {
    System.out.println("Found contact: " + foundContact.getContactName());
    System.out.println("Email: " + foundContact.getEmail());
    System.out.println("Pager: " + foundContact.getPagerNo());
}

// Update a contact
foundContact.setEmail("john.smith.updated@example.com");
foundContact.merge();

// Find all contacts
List<Contact> allContacts = Contact.findAllContacts();
System.out.println("Found " + allContacts.size() + " contacts");

// Find contacts with pagination
int page = 1;
int pageSize = 10;
List<Contact> pagedContacts = Contact.findContactEntries((page - 1) * pageSize, pageSize);
System.out.println("Page " + page + " contains " + pagedContacts.size() + " contacts");

// Remove a contact
foundContact.remove();
```

## Debugging and Production Support

### Common Issues
1. Missing contact information leading to failed notifications
2. Duplicate contacts in the system
3. Outdated contact information
4. Contacts not properly associated with alarm definitions
5. Performance issues when retrieving contacts
6. Null or invalid email addresses and pager numbers
7. Transaction-related issues during contact management operations

### Debugging Steps
1. Verify contact data:
   ```java
   // Check a specific contact
   Long contactId = 1L;
   Contact contact = Contact.findContact(contactId);
   
   if (contact != null) {
       System.out.println("Contact ID: " + contact.getId());
       System.out.println("Name: " + contact.getContactName());
       System.out.println("Email: " + contact.getEmail());
       System.out.println("Pager: " + contact.getPagerNo());
       System.out.println("Version: " + contact.getVersion());
   } else {
       System.out.println("Contact not found with ID: " + contactId);
   }
   ```

2. Check for contacts with missing information:
   ```java
   // Find contacts with missing email or pager
   String sql = "SELECT o FROM Contact o WHERE o.email IS NULL OR o.email = '' OR o.pagerNo IS NULL OR o.pagerNo = ''";
   Query q = entityManager().createQuery(sql, Contact.class);
   
   List<Contact> incompleteContacts = q.getResultList();
   System.out.println("Found " + incompleteContacts.size() + " contacts with missing information");
   
   for (Contact incompleteContact : incompleteContacts) {
       System.out.println("  Contact ID: " + incompleteContact.getId());
       System.out.println("  Name: " + incompleteContact.getContactName());
       System.out.println("  Email: " + incompleteContact.getEmail());
       System.out.println("  Pager: " + incompleteContact.getPagerNo());
   }
   ```

3. Check for potential duplicate contacts:
   ```java
   // Find potential duplicate contacts by name
   String sql = "SELECT o.contactName, COUNT(o) FROM Contact o GROUP BY o.contactName HAVING COUNT(o) > 1";
   Query q = entityManager().createQuery(sql);
   
   List<Object[]> duplicateNames = q.getResultList();
   System.out.println("Found " + duplicateNames.size() + " potentially duplicate contact names");
   
   for (Object[] result : duplicateNames) {
       String name = (String) result[0];
       Long count = (Long) result[1];
       System.out.println("  Name: " + name + ", Count: " + count);
       
       // Find the contacts with this name
       String detailSql = "SELECT o FROM Contact o WHERE o.contactName = :name";
       Query detailQuery = entityManager().createQuery(detailSql, Contact.class);
       detailQuery.setParameter("name", name);
       
       List<Contact> contacts = detailQuery.getResultList();
       for (Contact contact : contacts) {
           System.out.println("    ID: " + contact.getId() + 
                              ", Email: " + contact.getEmail() + 
                              ", Pager: " + contact.getPagerNo());
       }
   }
   ```

4. Check for contacts associated with alarms:
   ```java
   // Find contacts associated with alarms
   String sql = "SELECT ac.contact, COUNT(ac) FROM AlarmContact ac GROUP BY ac.contact";
   Query q = entityManager().createQuery(sql);
   
   List<Object[]> contactUsage = q.getResultList();
   System.out.println("Found " + contactUsage.size() + " contacts associated with alarms");
   
   for (Object[] result : contactUsage) {
       Contact contact = (Contact) result[0];
       Long count = (Long) result[1];
       System.out.println("  Contact: " + contact.getContactName() + 
                          ", Associated with " + count + " alarms");
   }
   ```

5. Test contact persistence:
   ```java
   // Test creating and persisting a new contact
   try {
       Contact testContact = new Contact();
       testContact.setContactName("Test Contact " + System.currentTimeMillis());
       testContact.setEmail("test" + System.currentTimeMillis() + "@example.com");
       testContact.setPagerNo("555-" + (1000 + (int)(Math.random() * 9000)));
       
       System.out.println("Persisting test contact: " + testContact.getContactName());
       testContact.persist();
       System.out.println("Contact persisted with ID: " + testContact.getId());
       
       // Verify the contact was persisted
       Contact verifyContact = Contact.findContact(testContact.getId());
       if (verifyContact != null) {
           System.out.println("Contact verified in database");
           
           // Clean up the test contact
           verifyContact.remove();
           System.out.println("Test contact removed");
       } else {
           System.out.println("Failed to verify contact in database");
       }
   } catch (Exception e) {
       System.out.println("Error testing contact persistence: " + e.getMessage());
       e.printStackTrace();
   }
   ```

### Resolution
- For missing contact information: Implement validation to ensure required fields are provided
- For duplicate contacts: Implement uniqueness constraints and validation
- For outdated information: Implement a regular review process for contact information
- For association issues: Verify alarm-contact associations and implement proper relationship management
- For performance issues: Optimize queries and implement indexing
- For invalid data: Implement data validation and sanitization
- For transaction issues: Ensure proper transaction management and error handling

### Monitoring
- Track the number of contacts in the system
- Monitor for contacts with missing or invalid information
- Track contact usage in alarm notifications
- Monitor for failed notifications due to contact issues
- Set up regular validation of contact information
- Track changes to contact information
- Monitor for potential duplicate contacts
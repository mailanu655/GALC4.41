# StampingImage Technical Documentation

## Purpose
StampingImage.java defines an entity that manages image data for the stamping system. This class provides functionality to store, retrieve, and manage binary image data in the database, allowing the system to maintain visual representations of dies, carriers, and other components. It serves as a repository for images that can be displayed in the user interface or used for reporting purposes.

## Logic/Functionality
- Stores binary image data in the database
- Uses the image name as a unique identifier
- Implements JPA entity functionality for database persistence
- Provides methods to find and manage image records
- Supports retrieving all image names for listing purposes
- Handles large binary data through LOB annotations

## Flow
1. Images are uploaded to the system and stored as StampingImage entities
2. The system can retrieve images by name when needed for display
3. The UI can request a list of available image names
4. Images can be updated or removed as needed

## Key Elements
- `imageName`: The unique name/identifier for the image
- `imageBytes`: The binary data of the image (stored as a BLOB)
- Static finder methods for retrieving image records
- Standard JPA entity methods for persistence operations

## Usage
```java
// Find an image by name
StampingImage image = StampingImage.findStampingImage("die123.png");
if (image != null) {
    byte[] imageData = image.getImageBytes();
    System.out.println("Image found: " + image.getImageName());
    System.out.println("Image size: " + imageData.length + " bytes");
} else {
    System.out.println("Image not found");
}

// Get all image names
List<String> imageNames = StampingImage.findImageNames();
System.out.println("Available images:");
for (String name : imageNames) {
    System.out.println("  " + name);
}

// Find all images
List<StampingImage> allImages = StampingImage.findAllStampingImages();
System.out.println("Total images: " + allImages.size());

// Create a new image
StampingImage newImage = new StampingImage();
newImage.setImageName("new_die.png");
// Load image data from file
byte[] fileData = Files.readAllBytes(Paths.get("/path/to/new_die.png"));
newImage.setImageBytes(fileData);
newImage.persist();

// Update an existing image
StampingImage existingImage = StampingImage.findStampingImage("die123.png");
if (existingImage != null) {
    // Load updated image data
    byte[] updatedData = Files.readAllBytes(Paths.get("/path/to/updated_die123.png"));
    existingImage.setImageBytes(updatedData);
    existingImage.merge();
}

// Delete an image
StampingImage obsoleteImage = StampingImage.findStampingImage("old_die.png");
if (obsoleteImage != null) {
    obsoleteImage.remove();
}

// Serve an image to a client
@RequestMapping(value = "/images/{imageName}", method = RequestMethod.GET)
public ResponseEntity<byte[]> getImage(@PathVariable String imageName) {
    StampingImage image = StampingImage.findStampingImage(imageName);
    if (image != null) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG); // Adjust based on image type
        return new ResponseEntity<>(image.getImageBytes(), headers, HttpStatus.OK);
    } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
```

## Debugging and Production Support

### Common Issues
1. Missing or corrupted image data
2. Large image sizes causing performance issues
3. Duplicate image names
4. Memory issues when handling large images
5. Database storage limitations
6. Image format compatibility issues
7. Slow image retrieval performance

### Debugging Steps
1. Check for missing or corrupted images:
   ```java
   // Check for missing or corrupted images
   System.out.println("Checking for missing or corrupted images:");
   
   List<StampingImage> allImages = StampingImage.findAllStampingImages();
   System.out.println("Total images: " + allImages.size());
   
   for (StampingImage image : allImages) {
       String imageName = image.getImageName();
       byte[] imageData = image.getImageBytes();
       
       System.out.println("  Image: " + imageName);
       
       if (imageData == null) {
           System.out.println("    WARNING: Null image data");
       } else if (imageData.length == 0) {
           System.out.println("    WARNING: Empty image data (0 bytes)");
       } else {
           System.out.println("    Size: " + imageData.length + " bytes");
           
           // Check for potentially corrupted image data
           if (imageData.length < 100) { // Most valid images are larger than 100 bytes
               System.out.println("    WARNING: Suspiciously small image size");
           }
           
           // Check image header (simple check for common formats)
           boolean validHeader = false;
           
           // Check for PNG header
           if (imageData.length >= 8 && 
               imageData[0] == (byte)0x89 && 
               imageData[1] == (byte)0x50 && 
               imageData[2] == (byte)0x4E && 
               imageData[3] == (byte)0x47) {
               validHeader = true;
           }
           
           // Check for JPEG header
           if (imageData.length >= 2 && 
               imageData[0] == (byte)0xFF && 
               imageData[1] == (byte)0xD8) {
               validHeader = true;
           }
           
           // Check for GIF header
           if (imageData.length >= 6 && 
               imageData[0] == (byte)0x47 && 
               imageData[1] == (byte)0x49 && 
               imageData[2] == (byte)0x46 && 
               imageData[3] == (byte)0x38) {
               validHeader = true;
           }
           
           if (!validHeader) {
               System.out.println("    WARNING: Invalid image header - may be corrupted");
           }
       }
   }
   ```

2. Check for large images:
   ```java
   // Check for large images
   System.out.println("Checking for large images:");
   
   long totalSize = 0;
   StampingImage largestImage = null;
   int largestSize = 0;
   
   for (StampingImage image : StampingImage.findAllStampingImages()) {
       byte[] imageData = image.getImageBytes();
       if (imageData != null) {
           int size = imageData.length;
           totalSize += size;
           
           if (size > largestSize) {
               largestSize = size;
               largestImage = image;
           }
           
           if (size > 1024 * 1024) { // 1MB
               System.out.println("  WARNING: Large image: " + image.getImageName() + 
                                 " (" + formatSize(size) + ")");
           }
       }
   }
   
   System.out.println("Total image storage: " + formatSize(totalSize));
   if (largestImage != null) {
       System.out.println("Largest image: " + largestImage.getImageName() + 
                         " (" + formatSize(largestSize) + ")");
   }
   
   // Helper method to format file size
   private static String formatSize(long size) {
       if (size < 1024) {
           return size + " bytes";
       } else if (size < 1024 * 1024) {
           return String.format("%.2f KB", size / 1024.0);
       } else if (size < 1024 * 1024 * 1024) {
           return String.format("%.2f MB", size / (1024.0 * 1024.0));
       } else {
           return String.format("%.2f GB", size / (1024.0 * 1024.0 * 1024.0));
       }
   }
   ```

3. Check for duplicate image names:
   ```java
   // Check for duplicate image names
   System.out.println("Checking for duplicate image names:");
   
   List<String> imageNames = StampingImage.findImageNames();
   Set<String> uniqueNames = new HashSet<>();
   Set<String> duplicateNames = new HashSet<>();
   
   for (String name : imageNames) {
       if (!uniqueNames.add(name)) {
           duplicateNames.add(name);
       }
   }
   
   if (duplicateNames.isEmpty()) {
       System.out.println("  No duplicate image names found");
   } else {
       System.out.println("  WARNING: Found duplicate image names:");
       for (String name : duplicateNames) {
           System.out.println("    " + name);
       }
   }
   ```

4. Test image retrieval performance:
   ```java
   // Test image retrieval performance
   System.out.println("Testing image retrieval performance:");
   
   List<String> perfImageNames = StampingImage.findImageNames();
   if (!perfImageNames.isEmpty()) {
       // Test findStampingImage
       String sampleName = perfImageNames.get(0);
       long startTime = System.currentTimeMillis();
       StampingImage image = StampingImage.findStampingImage(sampleName);
       long endTime = System.currentTimeMillis();
       
       System.out.println("  findStampingImage: " + (endTime - startTime) + 
                         "ms for " + sampleName);
       
       // Test findImageNames
       startTime = System.currentTimeMillis();
       List<String> names = StampingImage.findImageNames();
       endTime = System.currentTimeMillis();
       
       System.out.println("  findImageNames: " + (endTime - startTime) + 
                         "ms for " + names.size() + " names");
       
       // Test findAllStampingImages
       startTime = System.currentTimeMillis();
       List<StampingImage> allImagesPerf = StampingImage.findAllStampingImages();
       endTime = System.currentTimeMillis();
       
       System.out.println("  findAllStampingImages: " + (endTime - startTime) + 
                         "ms for " + allImagesPerf.size() + " images");
   } else {
       System.out.println("  No images found for performance testing");
   }
   ```

5. Check for image format compatibility:
   ```java
   // Check for image format compatibility
   System.out.println("Checking for image format compatibility:");
   
   Map<String, Integer> formatCounts = new HashMap<>();
   
   for (StampingImage image : StampingImage.findAllStampingImages()) {
       String imageName = image.getImageName();
       byte[] imageData = image.getImageBytes();
       
       String format = "unknown";
       
       if (imageData != null && imageData.length > 0) {
           // Check image header
           if (imageData.length >= 8 && 
               imageData[0] == (byte)0x89 && 
               imageData[1] == (byte)0x50 && 
               imageData[2] == (byte)0x4E && 
               imageData[3] == (byte)0x47) {
               format = "png";
           } else if (imageData.length >= 2 && 
                     imageData[0] == (byte)0xFF && 
                     imageData[1] == (byte)0xD8) {
               format = "jpeg";
           } else if (imageData.length >= 6 && 
                     imageData[0] == (byte)0x47 && 
                     imageData[1] == (byte)0x49 && 
                     imageData[2] == (byte)0x46 && 
                     imageData[3] == (byte)0x38) {
               format = "gif";
           } else if (imageData.length >= 2 && 
                     imageData[0] == (byte)0x42 && 
                     imageData[1] == (byte)0x4D) {
               format = "bmp";
           }
       }
       
       // Check if file extension matches detected format
       String extension = "";
       int dotIndex = imageName.lastIndexOf('.');
       if (dotIndex > 0 && dotIndex < imageName.length() - 1) {
           extension = imageName.substring(dotIndex + 1).toLowerCase();
       }
       
       System.out.println("  Image: " + imageName);
       System.out.println("    Detected format: " + format);
       System.out.println("    File extension: " + extension);
       
       if (!format.equals("unknown") && !extension.isEmpty() && 
           !format.equals(extension) && 
           !(format.equals("jpeg") && extension.equals("jpg"))) {
           System.out.println("    WARNING: Format mismatch - detected " + format + 
                             " but extension is " + extension);
       }
       
       // Count formats
       formatCounts.put(format, formatCounts.getOrDefault(format, 0) + 1);
   }
   
   System.out.println("Format distribution:");
   for (Map.Entry<String, Integer> entry : formatCounts.entrySet()) {
       System.out.println("  " + entry.getKey() + ": " + entry.getValue());
   }
   ```

6. Check for database storage limitations:
   ```java
   // Check for database storage limitations
   System.out.println("Checking for database storage limitations:");
   
   long totalSize = 0;
   for (StampingImage image : StampingImage.findAllStampingImages()) {
       byte[] imageData = image.getImageBytes();
       if (imageData != null) {
           totalSize += imageData.length;
       }
   }
   
   System.out.println("  Total image storage: " + formatSize(totalSize));
   
   // Check against typical database BLOB limits
   long oracleBlobLimit = 4L * 1024 * 1024 * 1024; // 4GB
   long mysqlBlobLimit = 4L * 1024 * 1024 * 1024;  // 4GB
   long sqlServerBlobLimit = 2L * 1024 * 1024 * 1024; // 2GB
   
   double oracleUsagePercent = (totalSize * 100.0) / oracleBlobLimit;
   double mysqlUsagePercent = (totalSize * 100.0) / mysqlBlobLimit;
   double sqlServerUsagePercent = (totalSize * 100.0) / sqlServerBlobLimit;
   
   System.out.println("  Database BLOB usage:");
   System.out.println("    Oracle: " + String.format("%.2f%%", oracleUsagePercent));
   System.out.println("    MySQL: " + String.format("%.2f%%", mysqlUsagePercent));
   System.out.println("    SQL Server: " + String.format("%.2f%%", sqlServerUsagePercent));
   
   if (oracleUsagePercent > 80 || mysqlUsagePercent > 80 || sqlServerUsagePercent > 80) {
       System.out.println("  WARNING: Approaching database BLOB storage limits");
   }
   ```

7. Check for memory issues:
   ```java
   // Check for potential memory issues
   System.out.println("Checking for potential memory issues:");
   
   // Calculate memory requirements for loading all images
   long totalMemoryRequired = 0;
   for (StampingImage image : StampingImage.findAllStampingImages()) {
       byte[] imageData = image.getImageBytes();
       if (imageData != null) {
           totalMemoryRequired += imageData.length;
       }
   }
   
   // Get current JVM memory information
   Runtime runtime = Runtime.getRuntime();
   long maxMemory = runtime.maxMemory();
   long freeMemory = runtime.freeMemory();
   long totalMemory = runtime.totalMemory();
   long usedMemory = totalMemory - freeMemory;
   
   System.out.println("  JVM Memory:");
   System.out.println("    Max memory: " + formatSize(maxMemory));
   System.out.println("    Total memory: " + formatSize(totalMemory));
   System.out.println("    Used memory: " + formatSize(usedMemory));
   System.out.println("    Free memory: " + formatSize(freeMemory));
   
   System.out.println("  Memory required for all images: " + formatSize(totalMemoryRequired));
   
   double memoryUsagePercent = (totalMemoryRequired * 100.0) / maxMemory;
   System.out.println("  Memory usage for all images: " + 
                     String.format("%.2f%%", memoryUsagePercent));
   
   if (memoryUsagePercent > 50) {
       System.out.println("  WARNING: Loading all images could consume significant memory");
   }
   ```

### Resolution
- For missing or corrupted images: Replace with valid image data
- For large images: Resize or compress images to reduce storage requirements
- For duplicate image names: Rename or consolidate duplicate images
- For memory issues: Implement pagination or streaming for image retrieval
- For database storage limitations: Archive older images or use external storage
- For image format compatibility: Standardize on supported image formats
- For slow retrieval performance: Optimize queries or implement caching

### Monitoring
- Track image storage usage
- Monitor image retrieval performance
- Track image format distribution
- Monitor for corrupted images
- Track memory usage during image operations
- Monitor database BLOB storage usage
- Set up alerts for approaching storage limits
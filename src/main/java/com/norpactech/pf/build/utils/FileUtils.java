package com.norpactech.pf.build.utils;
/**
 * © 2025 Northern Pacific Technologies, LLC. All Rights Reserved. 
 *  
 * For license details, see the LICENSE file in this project root.
 */
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.norpactech.pf.build.vo.DownloadResponseVO;

public class FileUtils {
  
  public static void zipToFile(DownloadResponseVO downloadResponse) throws IOException {

    String basePath = 
        downloadResponse.getSourceDirectory() + "/" + 
        downloadResponse.getSubPackageDirectory() + "/";

    var byteArrayInputStream = new ByteArrayInputStream(downloadResponse.getZipFile());
    var zipInputStream = new ZipInputStream(byteArrayInputStream); 
    
    try  { 
      byte[] buffer = new byte[1024 * 8];

      ZipEntry entry;
      
      while ((entry = zipInputStream.getNextEntry()) != null) {        
        String fileName = entry.getName();
        String filePath = basePath + fileName;
        // Create directories if needed
        new File(filePath).getParentFile().mkdirs();
        // Create the file
        try (var fileOutputStream = new FileOutputStream(filePath)) {
          int read;
          while ((read = zipInputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, read);
          }
        }
        zipInputStream.closeEntry();
      }
    }
    finally {
      zipInputStream.close();
      byteArrayInputStream.close();
    }
  }
  
  public static void deleteAllFiles(String basePath) {
    File baseDir = new File(basePath);
    if (baseDir.exists() && baseDir.isDirectory()) {
      for (File file : baseDir.listFiles()) {
        if (file.isDirectory()) {
          deleteAllFiles(file.getAbsolutePath()); // Recursively delete files in subdirectories
        }
        file.delete(); // Delete the file or empty directory
      }
    }
  }    
}
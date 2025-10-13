package com.norpactech.pf.build.service;
/**
 * © 2025 Northern Pacific Technologies, LLC. All Rights Reserved. 
 *  
 * For license details, see the LICENSE file in this project root.
 */
import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.norpactech.pf.build.utils.AuthUtils;
import com.norpactech.pf.build.utils.FileUtils;
import com.norpactech.pf.build.utils.NetUtils;
import com.norpactech.pf.build.utils.TextUtils;
import com.norpactech.pf.build.vo.DownloadRequestVO;
import com.norpactech.pf.build.vo.DownloadResponseVO;
import com.norpactech.pf.build.vo.JwtRequestVO;

public class DownloadService {

  final static Logger logger = LoggerFactory.getLogger(DownloadService.class);

  public static int downloadRequest(String username, String password, String paretoServerUrl, String buildFile) throws Exception {

    var jwtRequest = new JwtRequestVO(username, password);
    var jwt = AuthUtils.getJwt(paretoServerUrl + "/access-token", jwtRequest);

    var reader = Files.newBufferedReader(Paths.get(buildFile));
    var csvParser = new CSVParser(reader, CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build());
    var downloadResponses = new ArrayList<DownloadResponseVO>(); 
    
    try {

      logger.info("Retrieving all Download Requests...");
      for (CSVRecord csvRecord : csvParser) {
      
        if (isComment(csvRecord)) {
          continue;
        }
        var tenant = TextUtils.toString(csvRecord.get("tenant"));
        var schema = TextUtils.toString(csvRecord.get("schema"));
        var project = TextUtils.toString(csvRecord.get("project"));
        var projectComponent = TextUtils.toString(csvRecord.get("project_component"));
        var sourceDirectory = TextUtils.toString(csvRecord.get("source_directory"));
        var subPackageDirectory = TextUtils.toString(csvRecord.get("sub_package_directory"));
        var downloadRequest = new DownloadRequestVO(tenant, schema, project, projectComponent);
        
        var downloadResponse = NetUtils.downloadRequest(
            paretoServerUrl, 
            jwt, 
            sourceDirectory, 
            subPackageDirectory, 
            projectComponent, 
            downloadRequest);

        downloadResponses.add(downloadResponse);
      }
    }
    catch (Exception e) {
      logger.error("Exception while retrieving Download Requests: {} Terminating...", e.getMessage());
      return 1;
    }
    finally {
      csvParser.close();
    }    
    logger.info("All download requests successfully retrieved");
    /*
     * List affected directories...
     */
    logger.warn("<<< Warning: This utility does not delete old files! >>>");
    logger.info("The following directories will be affected by the Pareto code install...");

    try {
      for (String directory : getDirectories(downloadResponses)) {
        logger.info(directory);
      }
    }
    catch (Exception e) {
      logger.error("Exception while getting affected directories: {} Terminating...", e.getMessage());
      return 1;
    }
    /*
     * Update affected directories...
     */
    logger.info("Beginning Pareto code installation...");
    try {
      for (DownloadResponseVO downloadResponse : downloadResponses) {
        FileUtils.zipToFile(downloadResponse);
        logger.info("Project Component: '{}' installed", downloadResponse.getProjectComponent());
      }
    }
    catch (Exception e) {
      logger.error("Exception while installing Pareto code: {}. Terminating...", e.getMessage());
      return 1;
    }
    logger.info("Completed Pareto code installation");
    return 0;
  }

  public static Set<String> getDirectories(List<DownloadResponseVO> downloadResponses) throws Exception {

    var directories = new HashSet<String>();
    
    for (DownloadResponseVO downloadResponse : downloadResponses) {
      var byteArrayInputStream = new ByteArrayInputStream(downloadResponse.getZipFile());
      var zipInputStream = new ZipInputStream(byteArrayInputStream);
      
      try { 
        ZipEntry entry;

        String baseDirectory = downloadResponse.getSourceDirectory() + downloadResponse.getSubPackageDirectory();
        
        while ((entry = zipInputStream.getNextEntry()) != null) {        
          String fileName = entry.getName();
          String directory = baseDirectory;
          if (fileName.contains("/")) {
            directory = baseDirectory + "/" + fileName.substring(0, fileName.lastIndexOf('/'));
          }
          directories.add(directory.trim());
        }
      }
      finally {
        zipInputStream.close();
        byteArrayInputStream.close();
      }
    }
    return directories;
  }

  public static boolean isComment(CSVRecord csvRecord) throws Exception {

    String start = null;
    try {
      start = csvRecord.get(0);

      if(StringUtils.isEmpty(start)) {
        throw new Exception("Record invalid - col(0) is empty");
      }
      return start.startsWith("//");
    }
    catch (Exception e) {
      throw new Exception("Record col(0) is empty or currupt");
    }
  }
}

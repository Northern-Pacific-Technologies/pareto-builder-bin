/**
 * © 2025 Northern Pacific Technologies, LLC. All Rights Reserved. 
 *  
 * For license details, see the LICENSE file in this project root.
 */
package com.norpactech.pf.build;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.norpactech.nc.config.load.ConfiguredAPI;
import com.norpactech.nc.config.load.Globals;
import com.norpactech.pf.build.service.DownloadService;

public class Application {
  
  final static Logger logger = LoggerFactory.getLogger(Application.class);

  public static void main(String[] args) throws Exception {
  
    String buildFile = System.getenv("PARETO_BUILD_FILE");
    String sourceRoot = System.getenv("SOURCE_ROOT_DIR");

    if (StringUtils.isEmpty(buildFile)) {
      logger.error("Null or empty build file. Set environment variable: PARETO_BUILD_FILE. Terminating...");
      System.exit(1);
    }

    if (StringUtils.isEmpty(sourceRoot)) {
      logger.error("Null or empty build file. Set environment variable: SOURCE_ROOT_DIR. Terminating...");
      System.exit(1);
    }

    try {
      
      logger.info("Beginning Pareto Build");
      
      ConfiguredAPI.configure(Globals.PARETO_API_URL, Globals.PARETO_API_VERSION, Globals.PARETO_API_USERNAME, Globals.PARETO_API_PASSWORD);
      logger.info("Pareto Build using file: {}", buildFile);

      DownloadService.downloadRequest(
        Globals.PARETO_API_USERNAME, 
        Globals.PARETO_API_PASSWORD, 
        Globals.PARETO_API_URL, 
        sourceRoot, 
        buildFile);
      
      logger.info("Completed Pareto Build");
    }
    catch (Exception e) {
      logger.error("Pareto Factory Loader Terminated Unexpectedly: " + e.getMessage());
      System.exit(1);
    }    
    System.exit(0);
  }
}
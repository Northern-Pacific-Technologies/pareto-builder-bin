/**
 * © 2025 Northern Pacific Technologies, LLC. All Rights Reserved. 
 *  
 * For license details, see the LICENSE file in this project root.
 */
package com.norpactech.pf.build;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.norpactech.pf.build.service.DownloadService;

public class Application {
  
  final static Logger logger = LoggerFactory.getLogger(Application.class);

  public static void main(String[] args) throws Exception {
  
    String username   = System.getenv("PARETO_USERNAME");
    String password   = System.getenv("PARETO_PASSWORD");
    String buildFile  = System.getenv("PARETO_BUILD_FILE");
    String factoryURL = System.getenv("PARETO_FACTORY_URL");
    String idTenant   = System.getenv("PARETO_TENANT_UUID");

    logger.info("Beginning Pareto Build");

    if (StringUtils.isEmpty(username)) {
      logger.error("Null or empty username. Set environment variable: PARETO_USERNAME. Terminating...");
      System.exit(1);
    }
    
    if (StringUtils.isEmpty(password)) {
      logger.error("Null or empty password. Set environment variable: PARETO_PASSWORD. Terminating...");
      System.exit(1);
    }
    
    if (StringUtils.isEmpty(buildFile)) {
      logger.error("Null or empty build file. Set environment variable: PARETO_BUILD_FILE. Terminating...");
      System.exit(1);
    }

    if (StringUtils.isEmpty(factoryURL)) {
      logger.error("Null or empty Pareto Factory URL. Set environment variable: PARETO_FACTORY_URL. Terminating...");
      System.exit(1);
    }

    if (StringUtils.isEmpty(idTenant)) {
      logger.error("Null or empty Tenant UUID. Set environment variable: PARETO_TENANT_UUID. Terminating...");
      System.exit(1);
    }
    
    logger.info("Pareto Build using file: {}", buildFile);
    int retVal = DownloadService.downloadRequest(username, password, factoryURL, buildFile);
    logger.info("Completed Pareto Build");

    System.exit(retVal);
  }
}

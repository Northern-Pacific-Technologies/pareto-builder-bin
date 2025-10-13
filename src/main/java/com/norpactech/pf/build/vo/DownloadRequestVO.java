package com.norpactech.pf.build.vo;
/**
 * © 2025 Northern Pacific Technologies, LLC. All Rights Reserved. 
 *  
 * For license details, see the LICENSE file in this project root.
 */
public class DownloadRequestVO {
  
  private String tenant;
  private String schema;
  private String project;
  private String projectComponent;

  public DownloadRequestVO(String tenant, String schema, String project, String projectComponent) {
    this.tenant = tenant;
    this.schema = schema;
    this.project = project;
    this.projectComponent = projectComponent;
  }
  
  public String getTenant() {
    return tenant;
  }
  
  public void setTenant(String tenant) {
    this.tenant = tenant;
  }
  
  public String getSchema() {
    return schema;
  }
  
  public void setSchema(String schema) {
    this.schema = schema;
  }
  
  public String getProject() {
    return project;
  }
  
  public void setProject(String project) {
    this.project = project;
  }
  
  public String getProjectComponent() {
    return projectComponent;
  }
  
  public void setProjectComponent(String projectComponent) {
    this.projectComponent = projectComponent;
  }
}

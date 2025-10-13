package com.norpactech.pf.build.vo;
/**
 * © 2025 Northern Pacific Technologies, LLC. All Rights Reserved. 
 *  
 * For license details, see the LICENSE file in this project root.
 */
public class DownloadResponseVO {
  
  private String sourceDirectory;
  private String subPackageDirectory;
  private String projectComponent;
  private int responseCode;
  private byte[] zipFile;
  
  public DownloadResponseVO(
      String sourceDirectory, 
      String subPackageDirectory, 
      String projectComponent,
      int responseCode, 
      byte[] zipFile) {
    
    this.sourceDirectory = sourceDirectory;
    this.subPackageDirectory = subPackageDirectory;
    this.projectComponent = projectComponent;
    this.responseCode = responseCode;
    this.zipFile = zipFile;
  }

  public String getSourceDirectory() {
    return sourceDirectory;
  }

  public void setSourceDirectory(String sourceDirectory) {
    this.sourceDirectory = sourceDirectory;
  }

  public String getSubPackageDirectory() {
    return subPackageDirectory;
  }

  public void setSubPackageDirectory(String subPackageDirectory) {
    this.subPackageDirectory = subPackageDirectory;
  }

  public String getProjectComponent() {
    return projectComponent;
  }

  public void setProjectComponent(String projectComponent) {
    this.projectComponent = projectComponent;
  }

  public int getResponseCode() {
    return responseCode;
  }

  public void setResponseCode(int responseCode) {
    this.responseCode = responseCode;
  }

  public byte[] getZipFile() {
    return zipFile;
  }

  public void setZipFile(byte[] zipFile) {
    this.zipFile = zipFile;
  }

}

package com.norpactech.pf.build.utils;
/**
 * © 2025 Northern Pacific Technologies, LLC. All Rights Reserved. 
 *  
 * For license details, see the LICENSE file in this project root.
 */
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.norpactech.pf.build.vo.DownloadRequestVO;
import com.norpactech.pf.build.vo.DownloadResponseVO;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class NetUtils {

  final static Logger log = LoggerFactory.getLogger(NetUtils.class);

  public static DownloadResponseVO downloadRequest(
      String host, 
      String jwt, 
      String sourceDirectory,
      String subPackageDirectory,
      String projectComponent,
      DownloadRequestVO downloadRequest) throws Exception {
    
    URL url = new URL(host + "/v01/generator/download");
    okhttp3.Response response = null;

    try {
      String json = new Gson().toJson(downloadRequest);
      
      OkHttpClient client = new OkHttpClient().newBuilder().build();
      MediaType mediaType = MediaType.parse("application/json");
      RequestBody body = RequestBody.create(json, mediaType);
      
      okhttp3.Request request = new okhttp3.Request.Builder()
          .url(url)
          .method("POST", body)
          .addHeader("Accept", "application/json")
          .addHeader("Content-Type", "application/json")
          .addHeader("Authorization", "Bearer " + jwt)
          .addHeader("X-Tenant-ID", System.getenv("PARETO_TENANT_UUID"))
          .build();
      response = client.newCall(request).execute();       
    } 
    catch (Exception e) {
      log.info("Download request failed: " + e.getMessage());
      throw e;
    }
    int responseCode = response.code();
    if (responseCode > 299) {
      log.info("Download request failed: " + response.message());
      throw new RuntimeException("Failed : HTTP error code : " + responseCode);
    }
    return new DownloadResponseVO(sourceDirectory, subPackageDirectory, projectComponent, responseCode, response.body().bytes());
  }
}
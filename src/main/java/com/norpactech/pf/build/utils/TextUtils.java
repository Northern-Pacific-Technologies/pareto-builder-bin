package com.norpactech.pf.build.utils;
/**
 * © 2025 Northern Pacific Technologies, LLC. All Rights Reserved. 
 *  
 * For license details, see the LICENSE file in this project root.
 */
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

public class TextUtils {

  public static UUID toUUID(String source) {

    if (StringUtils.isBlank(source)) {
      return null;
    }
    
    try {
      return UUID.fromString(source);
    } 
    catch (IllegalArgumentException e) {
      throw new RuntimeException("Invalid UUID: " + source);
    }
  }    
  
  public static String toString(String source) {

    if (StringUtils.isBlank(source)) {
      return null;
    }
    return source.trim();
  }
    
  public static Integer toInteger(String source) {

    if (StringUtils.isBlank(source)) {
      return null;
    }
    try {
      return Integer.parseInt(source);
    } 
    catch (NumberFormatException e) {
      throw new RuntimeException("Invalid integer: " + source);
    }
  }
  
  public static Boolean toBoolean(String source) {

    if (StringUtils.isBlank(source)) {
      return null;
    }

    String value = source.toLowerCase();
    if (value.equals("true")) {
      return true;
    }
    else if (value.equals("false")) {
      return false;
    }
    else {
      throw new RuntimeException("Invalid boolean: " + source);
    }
  }

  public static String toCamelCase(String snakeCase) {

    if (snakeCase == null || snakeCase.isEmpty()) {
      return snakeCase;
    }
    StringBuffer camelCase = new StringBuffer();
    boolean nextCharUpperCase = false;

    for (char c : snakeCase.toCharArray()) {
      if (c == '_') {
        nextCharUpperCase = true;
      } else {
        if (nextCharUpperCase) {
          camelCase.append(Character.toUpperCase(c));
          nextCharUpperCase = false;
        } else {
          camelCase.append(c);
        }
      }
    }
    return camelCase.toString();
  }
    
  public static String toShortClassName(String fullClassName) {

    if (fullClassName == null || fullClassName.isEmpty()) {
      return fullClassName;
    }
    int lastDotIndex = fullClassName.lastIndexOf('.');
    if (lastDotIndex == -1) {
      return fullClassName;
    }
    return fullClassName.substring(lastDotIndex + 1);
  }
  
  public static String toGetter(String shortClassName) {

    if (shortClassName == null || shortClassName.isEmpty()) {
      return shortClassName;
    }
    return "get" + shortClassName.substring(0, 1).toUpperCase() + shortClassName.substring(1);
  }  

  public static String toSetter(String shortClassName) {

    if (shortClassName == null || shortClassName.isEmpty()) {
      return shortClassName;
    }
    return "set" + shortClassName.substring(0, 1).toUpperCase() + shortClassName.substring(1);
  }  
}

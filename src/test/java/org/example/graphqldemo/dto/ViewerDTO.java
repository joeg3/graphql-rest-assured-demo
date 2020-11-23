package org.example.graphqldemo.dto;

import java.util.Map;

public class ViewerDTO {
  private String name;
  private String url;

  public String getName() {
    return name;
  }
  public String getUrl() {
    return url;
  }

  public ViewerDTO setName(String name) {
    this.name = name;
    return this;
  }

  public ViewerDTO setUrl(String url) {
    this.url = url;
    return this;
  }
}

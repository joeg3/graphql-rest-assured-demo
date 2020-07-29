package org.example.graphqldemo;

public class Viewer {
  private String name;
  private String url;

  public String getName() {
    return name;
  }

  public Viewer setName(String name) {
    this.name = name;
    return this;
  }

  public String getUrl() {
    return url;
  }

  public Viewer setUrl(String url) {
    this.url = url;
    return this;
  }
}

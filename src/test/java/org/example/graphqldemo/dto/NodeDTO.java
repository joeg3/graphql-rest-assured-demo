package org.example.graphqldemo.dto;

public class NodeDTO {
  private String name;
  private String description;

  public String getName() {
    return name;
  }
  public String getDescription() {
    return description;
  }

  public NodeDTO setName(String name) {
    this.name = name;
    return this;
  }

  public NodeDTO setDescription(String description) {
    this.description = description;
    return this;
  }
}

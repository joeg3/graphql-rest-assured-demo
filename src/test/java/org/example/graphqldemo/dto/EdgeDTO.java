package org.example.graphqldemo.dto;

public class EdgeDTO {
  private NodeDTO node;

  public NodeDTO getNode() {
    return node;
  }

  public EdgeDTO setNode(NodeDTO node) {
    this.node = node;
    return this;
  }

}

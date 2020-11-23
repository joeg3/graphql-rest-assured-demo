package org.example.graphqldemo.dto;

import java.util.List;

public class RepositoriesDTO {
  private Integer totalCount;
  private List<EdgeDTO> edges;

  public Integer getTotalCount() {
    return totalCount;
  }
  public List<EdgeDTO> getEdges() {
    return edges;
  }

  public RepositoriesDTO setTotalCount(Integer totalCount) {
    this.totalCount = totalCount;
    return this;
  }

  public RepositoriesDTO setEdges(List<EdgeDTO> edges) {
    this.edges = edges;
    return this;
  }
}

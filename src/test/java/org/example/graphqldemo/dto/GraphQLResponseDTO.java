package org.example.graphqldemo.dto;

public class GraphQLResponseDTO {
  private DataDTO data;

  public DataDTO getData() {
    return data;
  }

  public GraphQLResponseDTO setData(DataDTO data) {
    this.data = data;
    return this;
  }
}

package org.example.graphqldemo.dto;

import java.util.Map;

/*
Object to hold GraphQL POST request:
{
  "query": "...",
  "variables": { "myVariable": "myValue", ...}
}
 */
public class GraphQLPayloadDTO {
  String query;
  public Map<String, Object> variables;

  public String getQuery() {
    return query;
  }
  public Map<String, Object> getVariables() { return variables;}

  public GraphQLPayloadDTO setQuery(String query) {
    this.query = query;
    return this;
  }

  public GraphQLPayloadDTO setVariables(Map<String, Object> variables) {
    this.variables = variables;
    return this;
  }

}

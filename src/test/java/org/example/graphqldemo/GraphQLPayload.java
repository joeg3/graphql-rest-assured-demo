package org.example.graphqldemo;

import java.util.HashMap;
import java.util.Map;

import static org.example.graphqldemo.BaseTest.removeNewlinesFromString;

/**
Object to hold GraphQL POST request:
{
  "query": "...",
  "variables": { "myVariable": "myValue", ...}
}

 The GraphQL queries are stored in Java text blocks for readability, but the
 text blcoks have a newline on each line. Newlines are great for printing out the
 query, but the endpoint api doesn't accept newlines. So as a hack workaround, I have
 printableQuery variable to hold the query with new lines. and before calling
 the api endpoint in BaseTest, I remove the newlines from the query variable.
 */
public class GraphQLPayload {
  private String query;
  private String printableQuery;
  private Map<String, Object> variables = new HashMap<>();

  public GraphQLPayload () { }

  public GraphQLPayload (String query) {
    this.query = query;
    this.printableQuery = query;
  }

  public String getQuery() {
    return query;
  }
  public Map<String, Object> getVariables() { return variables;}

  public GraphQLPayload setQuery(String query) {
    this.query = query;
    this.printableQuery = query;
    return this;
  }

  public void removeNewlinesFromGraphQL() {
    this.query = removeNewlinesFromString(this.query);
  }

  public GraphQLPayload setVariables(Map<String, Object> variables) {
    this.variables = variables;
    return this;
  }

  public GraphQLPayload addVariable(String key, Object value) {
    this.variables.put(key, value);
    return this;
  }

  public String toString() {
    String printableGraphQL = "\n*** Query ***\n" + this.printableQuery;
    printableGraphQL += "\n*** Variables ***\n";
    if (variables.size() > 0) {
      for (String key : variables.keySet()) {
        printableGraphQL += key + ": " + variables.get(key) + "\n";
      }
    }
    return printableGraphQL;
  }

}

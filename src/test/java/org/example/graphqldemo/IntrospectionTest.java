package org.example.graphqldemo;

import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.graphqldemo.BaseTest.*;

@DisplayName("Examples of how to use introspection to get data about a GraphQL schema")
public class IntrospectionTest {

  @Test
  @DisplayName("Get all the types in the schema")
  void getTypes() {
    String graphQL = "{ \"query\": \" { __schema { types { name kind description } }}\" }";

    JsonPath json = apiCall(graphQL);

    // The schema has types "Project" and "Organization" (among many others)
    assertThat(json.getString("data.__schema.types")).contains("name:Project");
    assertThat(json.getString("data.__schema.types")).contains("name:Organization");
  }

  @Test
  @DisplayName("Get all the query types in the schema")
  void getQueryTypes() {
    // The query type can be named anything, but is named Query by convention
    String graphQL = "{ \"query\": \" { __schema {queryType {name}}}\" }";

    JsonPath json = apiCall(graphQL);

    // The schema has types "Project" and "Organization" (among many others)
    assertThat(json.getString("data.__schema.queryType")).contains("name:Query");
  }

  @Test
  @DisplayName("For a type/object in the schema, get all it's fields")
  void getFieldsOfType() {
    String graphQL = "{ \"query\": \" { __type(name:  \\\"Organization\\\") { name kind description fields ( includeDeprecated: true ) { name isDeprecated deprecationReason }} }\" }";

    JsonPath json = apiCall(graphQL);

    // The type "Organization" has fields of "name" and "url" (among many others)
    assertThat(json.getString("data.__type.fields")).contains("name:name");
    assertThat(json.getString("data.__type.fields")).contains("name:url");
  }

  @Test
  @DisplayName("We can use the __typename field to get the name of the type returned by the query")
  void getNameOfTypeReturnedByQuery() {
    String graphQL = "{ \"query\": \"query { viewer { name url __typename } }\" }";

    JsonPath json = apiCall(graphQL);

    assertThat(json.getString("data.viewer.name")).isEqualTo(GITHUB_NAME);
    assertThat(json.getString("data.viewer.url")).isEqualTo(GITHUB_URL);
    assertThat(json.getString("data.viewer.__typename")).isEqualTo("User");
  }
}

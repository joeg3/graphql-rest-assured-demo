package org.example.graphqldemo;

import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.graphqldemo.BaseTest.*;
import static org.example.graphqldemo.GraphQLFileReader.*;
import static org.example.graphqldemo.GraphQLQueries.*;

/**
 * These are the same tests as `QueryWithTextBlockTest.java`, but using
 * GraphQL loaded from files. The advantage is that the GraphQL isn't in
 * Java code and could be reused in other tools like Postman.
 */
public class QueryWithFileTest {

  @Test
  void basicQueryWithoutVariable() throws IOException {
    String requestPayload = String .format("{ \"query\": \"%s\" }", getNameAndUrlQuery());

    JsonPath json = apiCall(requestPayload);

    assertThat(json.getString("data.viewer.name")).isEqualTo(GITHUB_NAME);
    assertThat(json.getString("data.viewer.url")).isEqualTo(GITHUB_URL);
  }

  @Test
  void queryWithHardcodedVariables() throws IOException {
    String requestPayload = String .format("{ \"query\": \"%s\" }", getMicrosoftHardcodedQuery());

    JsonPath json = apiCall(requestPayload);

    assertThat(json.getString("data.organization.name")).isEqualTo("Microsoft");
    assertThat(json.getString("data.organization.url")).isEqualTo("https://github.com/microsoft");
  }

  @Test
  void queryWithVariablesQueryParameter() throws IOException {
    // Parameterize using the graphQL 'variables' construct
    // This makes the query more reusable
    String graphQLVariables = """
           {                                     
             \\\"orgName\\\": \\\"microsoft\\\"  
           }                                     
           """;
    String requestPayload = String .format("{ \"query\": \"%s\", \"variables\": \"%s\"}", getOrgNameAndUrlQuery(), graphQLVariables);

    JsonPath json = apiCall(requestPayload);

    assertThat(json.getString("data.organization.name")).isEqualTo("Microsoft");
    assertThat(json.getString("data.organization.url")).isEqualTo("https://github.com/microsoft");
  }

  @Test
  void queryWithDefaultParameter() throws IOException {
    String requestPayload = String .format("{ \"query\": \"%s\" }", getOrgNameAndUrlMicrosoftDefault());

    JsonPath json = apiCall(requestPayload);

    assertThat(json.getString("data.organization.name")).isEqualTo("Microsoft");
    assertThat(json.getString("data.organization.url")).isEqualTo("https://github.com/microsoft");
  }

  @Test
  void queryWithVariableAndFragment() throws IOException {
    // This is a contrived example, but it shows how fragments could be reused for large numbers of fields
    String graphQLVariables = """
           {                                     
             \\\"orgName\\\": \\\"microsoft\\\"  
           }                                     
           """;
    String requestPayload = String .format("{ \"query\": \"%s %s\", \"variables\": \"%s\"}", getOrgNameAndUrlWithFragmentQuery(), getOrgFieldsBasicFragment(), graphQLVariables);

    JsonPath json = apiCall(requestPayload);

    assertThat(json.getString("data.organization.name")).isEqualTo("Microsoft");
    assertThat(json.getString("data.organization.url")).isEqualTo("https://github.com/microsoft");
  }

  @Test
  void queryWithVariableInFragment() throws IOException {
    // The organization field accepts a login argument but does not have a parameter for "first"
    // We use the query keyword to accept "orgName" and "first" arguments, and "first" can be used in the fragment
    // See FIRST_REPOS_FOR_ORG_QUERY_WITH_FRAGMENT and ORG_FIELDS_WITH_REPOS_FRAGMENT
    String graphQLVariables = """
           {                                     
             \\\"orgName\\\": \\\"microsoft\\\", 
             \\\"first\\\": 4                    
           }                                     
           """;
    String requestPayload = String .format("{ \"query\": \"%s %s\", \"variables\": \"%s\"}", getFirstReposForOrgWithFragmentQuery(), getOrgFieldsWithReposFragment(), graphQLVariables);

    JsonPath json = apiCall(requestPayload);

    assertThat(json.getString("data.organization.name")).isEqualTo("Microsoft");
    assertThat(json.getString("data.organization.url")).isEqualTo("https://github.com/microsoft");
    assertThat(json.getInt("data.organization.repositories.edges.size")).isEqualTo(4);
  }
}

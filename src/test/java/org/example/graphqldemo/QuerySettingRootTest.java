package org.example.graphqldemo;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.graphqldemo.BaseTest.*;
import static org.example.graphqldemo.GraphQLQueries.*;

/**
 * These are the same tests as `QueryWithObjectTest.java`, but the
 * rest api call sets the root at "data" so that the root element "data"
 * in the returned JsonPath isn't part of the expectation for better readability,
 * but if you do a jsonPath.prettyPrint(), it includes the root.
 */
public class QuerySettingRootTest {

  @Test
  void basicQueryWithoutVariable() {
    // Since we use the personal access token in the header, which is set in the request specification,
    // this should return expected name and url that is set up in config file

    // Here we don't use the 'query' keyword
    String graphQLQuery = """
            {            
              viewer {   
                name     
                url      
              }          
            }            
            """;
    GraphQLPayload requestPayload = new GraphQLPayload(graphQLQuery);
    JsonPath json = apiCallRootPathData(requestPayload);
    assertThat(json.getString("viewer.name")).isEqualTo(GITHUB_NAME);
    assertThat(json.getString("viewer.url")).isEqualTo(GITHUB_URL);

    // Here we use the 'query' keyword for operation type
    graphQLQuery = """
            query {      
              viewer {   
                name     
                url      
              }          
            }            
            """;
    requestPayload = new GraphQLPayload(graphQLQuery);
    json = apiCallRootPathData(requestPayload);
    assertThat(json.getString("viewer.name")).isEqualTo(GITHUB_NAME);
    assertThat(json.getString("viewer.url")).isEqualTo(GITHUB_URL);

    // The GraphQL documentation recommends using the operation type
    // keyword and operation name (NameAndUrl) to make things less ambiguous
    // See: NAME_AND_URL_QUERY
    requestPayload = new GraphQLPayload(NAME_AND_URL_QUERY);

    json = apiCallRootPathData(requestPayload);

    assertThat(json.getString("viewer.name")).isEqualTo(GITHUB_NAME);
    assertThat(json.getString("viewer.url")).isEqualTo(GITHUB_URL);
  }

  @Test
  void queryWithHardcodedVariables() {
    GraphQLPayload requestPayload = new GraphQLPayload(MICROSOFT_HARDCODED_QUERY);

    JsonPath json = apiCallRootPathData(requestPayload);

    assertThat(json.getString("organization.name")).isEqualTo("Microsoft");
    assertThat(json.getString("organization.url")).isEqualTo("https://github.com/microsoft");
  }

  @Test
  void queryWithVariablesQueryParameter() {
    GraphQLPayload requestPayload = new GraphQLPayload(ORG_URL_QUERY);
    requestPayload.addVariable("orgName", "microsoft");

    JsonPath json = apiCallRootPathData(requestPayload);

    assertThat(json.getString("organization.name")).isEqualTo("Microsoft");
    assertThat(json.getString("organization.url")).isEqualTo("https://github.com/microsoft");
  }

  @Test
  void queryWithDefaultParameter() {
    GraphQLPayload requestPayload = new GraphQLPayload(MICROSOFT_DEFAULT_QUERY);

    JsonPath json = apiCallRootPathData(requestPayload);

    assertThat(json.getString("organization.name")).isEqualTo("Microsoft");
    assertThat(json.getString("organization.url")).isEqualTo("https://github.com/microsoft");
  }

  @Test
  void queryWithVariableAndFragment() {
    // This is a contrived example, but it shows how fragments could be reused for large numbers of fields
    GraphQLPayload requestPayload = new GraphQLPayload(ORG_URL_QUERY_WITH_FRAGMENT + ORG_FIELDS_BASIC_FRAGMENT);
    requestPayload.addVariable("orgName", "microsoft");

    JsonPath json = apiCallRootPathData(requestPayload);

    assertThat(json.getString("organization.name")).isEqualTo("Microsoft");
    assertThat(json.getString("organization.url")).isEqualTo("https://github.com/microsoft");
  }

  @Test
  void queryWithVariableInFragment() {
    // The organization field accepts a login argument but does not have a parameter for "first"
    // We use the query keyword to accept "orgName" and "first" arguments, and "first" can be used in the fragment
    // See FIRST_REPOS_FOR_ORG_QUERY_WITH_FRAGMENT and ORG_FIELDS_WITH_REPOS_FRAGMENT
    GraphQLPayload requestPayload = new GraphQLPayload(FIRST_REPOS_FOR_ORG_QUERY_WITH_FRAGMENT + ORG_FIELDS_WITH_REPOS_FRAGMENT);
    requestPayload.addVariable("orgName", "microsoft");
    requestPayload.addVariable("first", 4);

    JsonPath json = apiCallRootPathData(requestPayload);

    assertThat(json.getString("organization.name")).isEqualTo("Microsoft");
    assertThat(json.getString("organization.url")).isEqualTo("https://github.com/microsoft");
    assertThat(json.getInt("organization.repositories.edges.size")).isEqualTo(4);
  }
}

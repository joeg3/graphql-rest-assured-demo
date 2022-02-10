package org.example.graphqldemo;

import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.graphqldemo.BaseTest.*;
import static org.example.graphqldemo.GraphQLQueries.*;

/**
 * These are the same tests as `QueryWithStringTest.java`, but using
 * Java text blocks for better readability. The payload sent to the api
 * is JSON in the form of "query": {graphQL_Object}. So we'll just put
 * the GraphQL in a text block.
 */
public class QueryWithTextBlockTest {

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
    String requestPayload = String .format("{ \"query\": \"%s\" }", graphQLQuery);
    JsonPath json = apiCall(requestPayload);
    assertThat(json.getString("data.viewer.name")).isEqualTo(GITHUB_NAME);
    assertThat(json.getString("data.viewer.url")).isEqualTo(GITHUB_URL);

    // Here we use the 'query' keyword for operation type
    graphQLQuery = """
            query {      
              viewer {   
                name     
                url      
              }          
            }            
            """;
    requestPayload = String .format("{ \"query\": \"%s\" }", graphQLQuery);
    json = apiCall(requestPayload);
    assertThat(json.getString("data.viewer.name")).isEqualTo(GITHUB_NAME);
    assertThat(json.getString("data.viewer.url")).isEqualTo(GITHUB_URL);

    // The GraphQL documentation recommends using the operation type
    // keyword and operation name (NameAndUrl) to make things less ambiguous
    // See: NAME_AND_URL_QUERY
    requestPayload = String .format("{ \"query\": \"%s\" }", NAME_AND_URL_QUERY);

    json = apiCall(requestPayload);

    assertThat(json.getString("data.viewer.name")).isEqualTo(GITHUB_NAME);
    assertThat(json.getString("data.viewer.url")).isEqualTo(GITHUB_URL);
  }

  @Test
  void queryWithHardcodedVariables() {
    // When using a text block for the api call, we can't use
    // GraphQLQueries.MICROSOFT_FOUR_REPOS_QUERY because queries sent as text
    // use more escapes for double quotes than queries sent as objects.
    // So we use this query.
    String graphQLQuery = """
            query MicrosoftFourRepos {
              organization(login: \\\"microsoft\\\") {
                name
                url
                repositories(first: 4) {
                  edges {
                    node {
                      name
                      description
                    }
                  }
                  totalCount
                }
              }
            }
            """;
    String requestPayload = String .format("{ \"query\": \"%s\" }", graphQLQuery);

    JsonPath json = apiCall(requestPayload);

    assertThat(json.getString("data.organization.name")).isEqualTo("Microsoft");
    assertThat(json.getString("data.organization.url")).isEqualTo("https://github.com/microsoft");
  }

  @Test
  void queryWithVariablesQueryParameter() {
    // Parameterize using the graphQL 'variables' construct
    // This makes the query more reusable
    String graphQLVariables = """
           {                                     
             \\\"orgName\\\": \\\"microsoft\\\"  
           }                                     
           """;
    String requestPayload = String .format("{ \"query\": \"%s\", \"variables\": \"%s\"}", ORG_URL_QUERY, graphQLVariables);

    JsonPath json = apiCall(requestPayload);

    assertThat(json.getString("data.organization.name")).isEqualTo("Microsoft");
    assertThat(json.getString("data.organization.url")).isEqualTo("https://github.com/microsoft");
  }

  @Test
  void queryWithDefaultParameter() {
    // When using a text block for the api call, we can't use
    // GraphQLQueries.MICROSOFT_DEFAULT_QUERY because queries sent as text
    // use more escapes for double quotes than queries sent as objects.
    // So we use this query.
    String graphQLQuery = """
            query MicrosoftOrgUrl($orgName: String = \\\"microsoft\\\") {  
              organization(login: $orgName) {                              
                name                                                       
                url                                                        
              }                                                            
            }                                                              
            """;
    String requestPayload = String .format("{ \"query\": \"%s\" }", graphQLQuery);

    JsonPath json = apiCall(requestPayload);

    assertThat(json.getString("data.organization.name")).isEqualTo("Microsoft");
    assertThat(json.getString("data.organization.url")).isEqualTo("https://github.com/microsoft");
  }

  @Test
  void queryWithVariableAndFragment() {
    // This is a contrived example, but it shows how fragments could be reused for large numbers of fields
    String graphQLVariables = """
           {                                     
             \\\"orgName\\\": \\\"microsoft\\\"  
           }                                     
           """;
    String requestPayload = String .format("{ \"query\": \"%s %s\", \"variables\": \"%s\"}", ORG_URL_QUERY_WITH_FRAGMENT, ORG_FIELDS_BASIC_FRAGMENT, graphQLVariables);

    JsonPath json = apiCall(requestPayload);

    assertThat(json.getString("data.organization.name")).isEqualTo("Microsoft");
    assertThat(json.getString("data.organization.url")).isEqualTo("https://github.com/microsoft");
  }

  @Test
  void queryWithVariableInFragment() {
    // The organization field accepts a login argument but does not have a parameter for "first"
    // We use the query keyword to accept "orgName" and "first" arguments, and "first" can be used in the fragment
    // See FIRST_REPOS_FOR_ORG_QUERY_WITH_FRAGMENT and ORG_FIELDS_WITH_REPOS_FRAGMENT
    String graphQLVariables = """
           {                                     
             \\\"orgName\\\": \\\"microsoft\\\", 
             \\\"first\\\": 4                    
           }                                     
           """;
    String requestPayload = String .format("{ \"query\": \"%s %s\", \"variables\": \"%s\"}", FIRST_REPOS_FOR_ORG_QUERY_WITH_FRAGMENT, ORG_FIELDS_WITH_REPOS_FRAGMENT, graphQLVariables);

    JsonPath json = apiCall(requestPayload);

    assertThat(json.getString("data.organization.name")).isEqualTo("Microsoft");
    assertThat(json.getString("data.organization.url")).isEqualTo("https://github.com/microsoft");
    assertThat(json.getInt("data.organization.repositories.edges.size")).isEqualTo(4);
  }
}

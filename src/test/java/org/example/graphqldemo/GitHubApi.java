package org.example.graphqldemo;

import io.restassured.path.json.JsonPath;

import static org.example.graphqldemo.BaseTest.*;
import static io.restassured.RestAssured.given;
import static org.example.graphqldemo.GraphQLQueries.*;

/**
 * This class has our version of the GitHub api where each method
 * contains the details of the GraphQL query, so we can invoke it
 * easily using method calls.
 *
 */
public class GitHubApi {

  public static JsonPath apiCall(GraphQLPayload graphQL) {
    graphQL.removeNewlinesFromGraphQL();
    return given().
             spec(REQ_SPEC).
             // log().all().
             body(graphQL).
           when().
             post("").
           then().
             spec(RES_SPEC).
             // log().all().
             extract().jsonPath();
  }

  public static JsonPath getMyGitHubNameAndUrl() {
    // Since we use the personal access token in the header, which is set in the request specification,
    // this should return expected name and url that is set up in config file

    GraphQLPayload requestPayload = new GraphQLPayload(NAME_AND_URL_QUERY);

    return apiCall(requestPayload);
  }

  public static JsonPath getMicrosoftNameAndUrlHardcoded() {
    GraphQLPayload requestPayload = new GraphQLPayload(MICROSOFT_HARDCODED_QUERY);

    return apiCall(requestPayload);
  }

  public static JsonPath getOrgNameAndUrl(String orgName) {
    GraphQLPayload requestPayload = new GraphQLPayload(ORG_URL_QUERY);
    requestPayload.addVariable("orgName", orgName);

    return apiCall(requestPayload);
  }

  public static JsonPath getMicrosoftNameAndUrlDefaultParam() {
    GraphQLPayload requestPayload = new GraphQLPayload(MICROSOFT_DEFAULT_QUERY);

    return apiCall(requestPayload);
  }

  public static JsonPath getOrgNameAndUrlUsingFragment(String orgName) {
    GraphQLPayload requestPayload = new GraphQLPayload(ORG_URL_QUERY_WITH_FRAGMENT + ORG_FIELDS_BASIC_FRAGMENT);
    requestPayload.addVariable("orgName", orgName);

    return apiCall(requestPayload);
  }

  public static JsonPath getFirstReposForOrg(String orgName, int first) {
    GraphQLPayload requestPayload = new GraphQLPayload(FIRST_REPOS_FOR_ORG_QUERY_WITH_FRAGMENT + ORG_FIELDS_WITH_REPOS_FRAGMENT);
    requestPayload.addVariable("orgName", orgName);
    requestPayload.addVariable("first", first);

    return apiCall(requestPayload);
  }
}

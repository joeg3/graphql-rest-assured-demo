package org.example.graphqldemo;

import io.restassured.path.json.JsonPath;
//import org.example.graphqldemo.selectionset.GraphQlQuery;
import org.example.graphqldemo.dto.GraphQLPayloadDTO;
//import org.example.graphqldemo.selectionset.QueryDTO;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.graphqldemo.BaseTest.*;

public class QueryWithObjectTest {

  @Test
  void basicQueryWithoutVariable() {
    // Since we use the personal access token in the header, which is set in the request specification,
    // this should return expected name and url that is setup in config file
    GraphQLPayloadDTO graphQL = new GraphQLPayloadDTO().setQuery("query { viewer { name url } }");

    JsonPath json = apiCall(graphQL);

    assertThat(json.getString("data.viewer.name")).isEqualTo(GITHUB_NAME);
    assertThat(json.getString("data.viewer.url")).isEqualTo(GITHUB_URL);
  }

  @Test
  void queryWithHardcodedVariable() {
    String queryStr = "{ organization(login: \"microsoft\") { name url repositories(first: 4) {edges{node{name description}} totalCount} } }";
    GraphQLPayloadDTO graphQL = new GraphQLPayloadDTO().setQuery(queryStr);

    JsonPath json = apiCall(graphQL);

    assertThat(json.getString("data.organization.name")).isEqualTo("Microsoft");
    assertThat(json.getString("data.organization.url")).isEqualTo("https://github.com/microsoft");
  }

  @Test
  void queryWithVariablesQueryParameter() {
    // Parameterize using the graphQL 'variables' construct
    // Since we are doing this in Java, and not in a script, I don't think it really gains much
    String queryStr = "query ($orgName: String!) { organization(login: $orgName) { name url } }";
    GraphQLPayloadDTO graphQL = new GraphQLPayloadDTO().setQuery(queryStr);
    Map<String, Object> variables = new HashMap<>();
    variables.put("orgName", "microsoft");
    graphQL.setVariables(variables);

    JsonPath json = apiCall(graphQL);

    assertThat(json.getString("data.organization.name")).isEqualTo("Microsoft");
    assertThat(json.getString("data.organization.url")).isEqualTo("https://github.com/microsoft");
  }

  @Test
  void queryWithDefaultParameter() {
    String queryStr = "query($orgName: String = \"microsoft\") { organization(login: $orgName) { name url repositories(first: 4) {edges{node{name description}} totalCount}} }";
    GraphQLPayloadDTO graphQL = new GraphQLPayloadDTO().setQuery(queryStr);

    JsonPath json = apiCall(graphQL);

    assertThat(json.getString("data.organization.name")).isEqualTo("Microsoft");
    assertThat(json.getString("data.organization.url")).isEqualTo("https://github.com/microsoft");
  }

  //@Test
  void queryPassArgumentToFieldWithFragment() {
    // The organization field accepts a login argument
    String organizationFieldsFragment = "fragment organizationFields on Organization{name url }";
    String graphQL = String .format("{ \"query\": \"{ organization(login: \\\"microsoft\\\") { ...organizationFields } } %s\" }", organizationFieldsFragment);

    JsonPath json = apiCall(graphQL);

    assertThat(json.getString("data.organization.name")).isEqualTo("Microsoft");
    assertThat(json.getString("data.organization.url")).isEqualTo("https://github.com/microsoft");
  }

  //@Test
  //@DisplayName("Use query keyword to accept arguments, along with fragment")
  void queryUsingQueryKeywordWithFragment() {
    // The organization field accepts a login argument but does not have a parameter for "first"
    // We use the query keyword to accept "orgName" and "first" arguments, and "first" can be used in the fragment
    String organizationFieldsFragmentWithFirstParam = "fragment organizationFields on Organization{name url repositories(first: $first) {edges{node{name description}} totalCount} }";
    String graphQL = String .format("{ \"query\": \" query ($orgName: String = \\\"microsoft\\\" $first: Int = 4) { organization(login: $orgName) { ...organizationFields } } %s\" }", organizationFieldsFragmentWithFirstParam);
    System.out.println("Fragment graphql: " + graphQL);
    JsonPath json = apiCall(graphQL);
json.prettyPrint();
    assertThat(json.getString("data.organization.name")).isEqualTo("Microsoft");
    assertThat(json.getString("data.organization.url")).isEqualTo("https://github.com/microsoft");
    assertThat(json.getInt("data.organization.repositories.edges.size")).isEqualTo(4);
  }
}

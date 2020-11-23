package org.example.graphqldemo;

import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.graphqldemo.BaseTest.*;

public class QueryTest {

  @Test
  void basicQueryWithoutVariable() {
    // Since we use the personal access token in the header, which is set in the request specification,
    // this should return expected name and url that is setup in config file

    // Here we don't use the 'query' keyword
    String graphQL = "{ \"query\": \"{ viewer { name url } }\" }";

    JsonPath json = apiCall(graphQL);

    assertThat(json.getString("data.viewer.name")).isEqualTo(GITHUB_NAME);
    assertThat(json.getString("data.viewer.url")).isEqualTo(GITHUB_URL);

    // Here we use the 'query' keyword for operation type
    graphQL = "{ \"query\": \"query { viewer { name url } }\" }";

    json = apiCall(graphQL);

    assertThat(json.getString("data.viewer.name")).isEqualTo(GITHUB_NAME);
    assertThat(json.getString("data.viewer.url")).isEqualTo(GITHUB_URL);

    // Here we also come up with an operation name that we add
    // The GraphQL documentation recommends using the operation type keyword and
    // operation name to make things less ambiguous
    graphQL = "{ \"query\": \"query NameAndUrl{ viewer { name url } }\" }";

    json = apiCall(graphQL);

    assertThat(json.getString("data.viewer.name")).isEqualTo(GITHUB_NAME);
    assertThat(json.getString("data.viewer.url")).isEqualTo(GITHUB_URL);
  }

  @Test
  void queryWithHardcodedVariables() {
    String graphQL = "{ \"query\": \"query MicrosoftFourRepos{ organization(login: \\\"microsoft\\\") { name url repositories(first: 4) {edges{node{name description}} totalCount} } }\" }";

    JsonPath json = apiCall(graphQL);

    assertThat(json.getString("data.organization.name")).isEqualTo("Microsoft");
    assertThat(json.getString("data.organization.url")).isEqualTo("https://github.com/microsoft");
  }

  @Test
  void queryWithVariablesQueryParameter() {
    // Parameterize using the graphQL 'variables' construct
    // Since we are doing this in Java, and not in a script, I don't think it really gains much
    String graphQL = "{ \"query\": \"query OrgUrl($orgName: String!) { organization(login: $orgName) { name url } }\",\"variables\": \"{\\\"orgName\\\": \\\"microsoft\\\"}\"}";

    JsonPath json = apiCall(graphQL);

    assertThat(json.getString("data.organization.name")).isEqualTo("Microsoft");
    assertThat(json.getString("data.organization.url")).isEqualTo("https://github.com/microsoft");
  }

  @Test
  void queryWithDefaultParameter() {
    String graphQL = "{ \"query\": \"query MicrosoftOrgUrl($orgName: String = \\\"microsoft\\\") { organization(login: $orgName) { name url } }\"}";

    JsonPath json = apiCall(graphQL);

    assertThat(json.getString("data.organization.name")).isEqualTo("Microsoft");
    assertThat(json.getString("data.organization.url")).isEqualTo("https://github.com/microsoft");
  }

  @Test
  void queryPassArgumentToFieldWithFragment() {
    // The organization field accepts a login argument
    String organizationFieldsFragment = "fragment organizationFields on Organization{name url }";
    String graphQL = String .format("{ \"query\": \"query OrgUrl{ organization(login: \\\"microsoft\\\") { ...organizationFields } } %s\" }", organizationFieldsFragment);

    JsonPath json = apiCall(graphQL);

    assertThat(json.getString("data.organization.name")).isEqualTo("Microsoft");
    assertThat(json.getString("data.organization.url")).isEqualTo("https://github.com/microsoft");
  }

  @Test
  @DisplayName("Use query keyword to accept arguments, along with fragment")
  void queryUsingQueryKeywordWithFragment() {
    // The organization field accepts a login argument but does not have a parameter for "first"
    // We use the query keyword to accept "orgName" and "first" arguments, and "first" can be used in the fragment
    String organizationFieldsFragmentWithFirstParam = "fragment organizationFields on Organization{name url repositories(first: $first) {edges{node{name description}} totalCount} }";
    String graphQL = String .format("{ \"query\": \" query FourReposForOrg($orgName: String = \\\"microsoft\\\" $first: Int = 4) { organization(login: $orgName) { ...organizationFields } } %s\" }", organizationFieldsFragmentWithFirstParam);

    JsonPath json = apiCall(graphQL);

    assertThat(json.getString("data.organization.name")).isEqualTo("Microsoft");
    assertThat(json.getString("data.organization.url")).isEqualTo("https://github.com/microsoft");
    assertThat(json.getInt("data.organization.repositories.edges.size")).isEqualTo(4);
  }
}

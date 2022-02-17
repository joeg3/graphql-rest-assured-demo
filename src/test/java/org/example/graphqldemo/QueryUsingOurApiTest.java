package org.example.graphqldemo;

import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.graphqldemo.BaseTest.*;
import static org.example.graphqldemo.GitHubApi.*;

/**
 * These are the same tests as `QueryWithObjectTest.java`, but passing
 * arguments to methods we created in GitHubApi.java
 */
public class QueryUsingOurApiTest {

  @Test
  void basicQueryWithoutVariable() {
    // Since we use the personal access token in the header, which is set in the request specification,
    // this should return expected name and url that is set up in config file

    JsonPath json = getMyGitHubNameAndUrl();

    assertThat(json.getString("data.viewer.name")).isEqualTo(GITHUB_NAME);
    assertThat(json.getString("data.viewer.url")).isEqualTo(GITHUB_URL);
  }

  @Test
  void queryWithHardcodedVariables() {
    JsonPath json = getMicrosoftNameAndUrlHardcoded();

    assertThat(json.getString("data.organization.name")).isEqualTo("Microsoft");
    assertThat(json.getString("data.organization.url")).isEqualTo("https://github.com/microsoft");
  }

  @Test
  void queryWithVariablesQueryParameter() {
    JsonPath json = getOrgNameAndUrl("microsoft");

    assertThat(json.getString("data.organization.name")).isEqualTo("Microsoft");
    assertThat(json.getString("data.organization.url")).isEqualTo("https://github.com/microsoft");
  }

  @Test
  void queryWithDefaultParameter() {
    JsonPath json = getMicrosoftNameAndUrlDefaultParam();

    assertThat(json.getString("data.organization.name")).isEqualTo("Microsoft");
    assertThat(json.getString("data.organization.url")).isEqualTo("https://github.com/microsoft");
  }

  @Test
  void queryWithVariableAndFragment() {
    JsonPath json = getOrgNameAndUrlUsingFragment("microsoft");

    assertThat(json.getString("data.organization.name")).isEqualTo("Microsoft");
    assertThat(json.getString("data.organization.url")).isEqualTo("https://github.com/microsoft");
  }

  @Test
  void queryWithVariableInFragment() {
    JsonPath json = getFirstReposForOrg("microsoft", 4);

    assertThat(json.getString("data.organization.name")).isEqualTo("Microsoft");
    assertThat(json.getString("data.organization.url")).isEqualTo("https://github.com/microsoft");
    assertThat(json.getInt("data.organization.repositories.edges.size")).isEqualTo(4);
  }
}

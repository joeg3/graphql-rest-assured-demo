package org.example.graphqldemo;

import org.example.graphqldemo.dto.GraphQLResponseDTO;
import org.example.graphqldemo.dto.OrganizationDTO;
import org.example.graphqldemo.dto.ViewerDTO;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.graphqldemo.BaseTest.*;
import static org.example.graphqldemo.GraphQLQueries.*;

public class QueryReturnObjectTest {

  @Test
  void basicQueryWithoutVariable() {
    // Since we use the personal access token in the header, which is set in the request specification,
    // this should return expected name and url that is setup in config file
    GraphQLPayload requestPayload = new GraphQLPayload(NAME_AND_URL_QUERY);

    GraphQLResponseDTO resp = apiCall(requestPayload, GraphQLResponseDTO.class);
    ViewerDTO viewer = resp.getData().getViewer();

    assertThat(viewer.getName()).isEqualTo(GITHUB_NAME);
    assertThat(viewer.getUrl()).isEqualTo(GITHUB_URL);
  }

  @Test
  void queryWithHardcodedVariable() {
    GraphQLPayload requestPayload = new GraphQLPayload(MICROSOFT_HARDCODED_QUERY);

    GraphQLResponseDTO resp = apiCall(requestPayload, GraphQLResponseDTO.class);

    OrganizationDTO org = resp.getData().getOrganization();
    assertThat(org.getName()).isEqualTo("Microsoft");
    assertThat(org.getUrl()).isEqualTo("https://github.com/microsoft");
    assertThat(org.getRepositories().getEdges().size()).isEqualTo(4);
  }

  @Test
  void queryWithVariablesQueryParameter() {
    GraphQLPayload requestPayload = new GraphQLPayload(ORG_URL_QUERY);
    requestPayload.addVariable("orgName", "microsoft");

    GraphQLResponseDTO resp = apiCall(requestPayload, GraphQLResponseDTO.class);

    OrganizationDTO org = resp.getData().getOrganization();
    assertThat(org.getName()).isEqualTo("Microsoft");
    assertThat(org.getUrl()).isEqualTo("https://github.com/microsoft");
  }

  @Test
  void queryWithDefaultParameter() {
    GraphQLPayload requestPayload = new GraphQLPayload(MICROSOFT_DEFAULT_QUERY);

    GraphQLResponseDTO resp = apiCall(requestPayload, GraphQLResponseDTO.class);

    OrganizationDTO org = resp.getData().getOrganization();
    assertThat(org.getName()).isEqualTo("Microsoft");
    assertThat(org.getUrl()).isEqualTo("https://github.com/microsoft");
  }

  @Test
  void queryWithVariableAndFragment() {
    // This is a contrived example, but it shows how fragments could be reused for large numbers of fields
    GraphQLPayload requestPayload = new GraphQLPayload(ORG_URL_QUERY_WITH_FRAGMENT + ORG_FIELDS_BASIC_FRAGMENT);
    requestPayload.addVariable("orgName", "microsoft");

    GraphQLResponseDTO resp = apiCall(requestPayload, GraphQLResponseDTO.class);

    OrganizationDTO org = resp.getData().getOrganization();
    assertThat(org.getName()).isEqualTo("Microsoft");
    assertThat(org.getUrl()).isEqualTo("https://github.com/microsoft");
  }

  @Test
  void queryWithVariableInFragment() {
    // The organization field accepts a login argument but does not have a parameter for "first"
    // We use the query keyword to accept "orgName" and "first" arguments, and "first" can be used in the fragment
    // See FIRST_REPOS_FOR_ORG_QUERY_WITH_FRAGMENT and ORG_FIELDS_WITH_REPOS_FRAGMENT
    GraphQLPayload requestPayload = new GraphQLPayload(FIRST_REPOS_FOR_ORG_QUERY_WITH_FRAGMENT + ORG_FIELDS_WITH_REPOS_FRAGMENT);
    requestPayload.addVariable("orgName", "microsoft");
    requestPayload.addVariable("first", 4);

    GraphQLResponseDTO resp = apiCall(requestPayload, GraphQLResponseDTO.class);

    OrganizationDTO org = resp.getData().getOrganization();
    assertThat(org.getName()).isEqualTo("Microsoft");
    assertThat(org.getUrl()).isEqualTo("https://github.com/microsoft");
    assertThat(org.getRepositories().getEdges().size()).isEqualTo(4);
  }
}

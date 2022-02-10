package org.example.graphqldemo;

import org.example.graphqldemo.dto.GraphQLResponseDTO;
import org.example.graphqldemo.dto.OrganizationDTO;
import org.example.graphqldemo.dto.ViewerDTO;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.graphqldemo.BaseTest.*;

public class QueryReturnObjectTest {

//  @Test
//  void basicQueryWithoutVariable() {
//    // Since we use the personal access token in the header, which is set in the request specification,
//    // this should return expected name and url that is setup in config file
//    GraphQLPayloadDTO graphQL = new GraphQLPayloadDTO().setQuery("query NameAndUrl{ viewer { name url } }");
//
//    GraphQLResponseDTO resp = apiCall(graphQL, GraphQLResponseDTO.class);
//    ViewerDTO viewer = resp.getData().getViewer();
//
//    assertThat(viewer.getName()).isEqualTo(GITHUB_NAME);
//    assertThat(viewer.getUrl()).isEqualTo(GITHUB_URL);
//  }
//
//  @Test
//  void queryWithHardcodedVariable() {
//    String queryStr = "query MicrosoftFourRepos{ organization(login: \"microsoft\") { name url repositories(first: 4) {edges{node{name description}} totalCount} } }";
//    GraphQLPayloadDTO graphQL = new GraphQLPayloadDTO().setQuery(queryStr);
//
//    GraphQLResponseDTO resp = apiCall(graphQL, GraphQLResponseDTO.class);
//
//    OrganizationDTO org = resp.getData().getOrganization();
//    assertThat(org.getName()).isEqualTo("Microsoft");
//    assertThat(org.getUrl()).isEqualTo("https://github.com/microsoft");
//    assertThat(org.getRepositories().getEdges().size()).isEqualTo(4);
//  }
//
//  @Test
//  void queryWithVariablesQueryParameter() {
//    // Parameterize using the graphQL 'variables' construct
//    // Since we are doing this in Java, and not in a script, I don't think it really gains much
//    String queryStr = "query OrgUrl($orgName: String!) { organization(login: $orgName) { name url } }";
//    GraphQLPayloadDTO graphQL = new GraphQLPayloadDTO().setQuery(queryStr);
//    Map<String, Object> variables = new HashMap<>();
//    variables.put("orgName", "microsoft");
//    graphQL.setVariables(variables);
//
//    GraphQLResponseDTO resp = apiCall(graphQL, GraphQLResponseDTO.class);
//
//    OrganizationDTO org = resp.getData().getOrganization();
//    assertThat(org.getName()).isEqualTo("Microsoft");
//    assertThat(org.getUrl()).isEqualTo("https://github.com/microsoft");
//  }
//
//  @Test
//  void queryWithDefaultParameter() {
//    String queryStr = "query MicrosoftOrgUrl($orgName: String = \"microsoft\") { organization(login: $orgName) { name url repositories(first: 4) {edges{node{name description}} totalCount}} }";
//    GraphQLPayloadDTO graphQL = new GraphQLPayloadDTO().setQuery(queryStr);
//
//    GraphQLResponseDTO resp = apiCall(graphQL, GraphQLResponseDTO.class);
//
//    OrganizationDTO org = resp.getData().getOrganization();
//    assertThat(org.getName()).isEqualTo("Microsoft");
//    assertThat(org.getUrl()).isEqualTo("https://github.com/microsoft");
//    assertThat(org.getRepositories().getEdges().size()).isEqualTo(4);
//  }
//
//  @Test
//  void queryPassArgumentToFieldWithFragment() {
//    String organizationFieldsFragment = "fragment organizationFields on Organization{name url}";
//    String queryStr = String .format("query OrgUrl{ organization(login: \"microsoft\") { ...organizationFields } } %s", organizationFieldsFragment);
//    GraphQLPayloadDTO graphQL = new GraphQLPayloadDTO().setQuery(queryStr);
//
//    GraphQLResponseDTO resp = apiCall(graphQL, GraphQLResponseDTO.class);
//
//    OrganizationDTO org = resp.getData().getOrganization();
//    assertThat(org.getName()).isEqualTo("Microsoft");
//    assertThat(org.getUrl()).isEqualTo("https://github.com/microsoft");
//  }
//
//  @Test
//  void queryUsingQueryKeywordWithFragment() {
//    // The organization field accepts a login argument but does not have a parameter for "first"
//    // We use the query keyword to accept "orgName" and "first" arguments, and "first" can be used in the fragment
//    String organizationFieldsFragmentWithFirstParam = "fragment organizationFields on Organization{name url repositories(first: $first) {edges{node{name description}} totalCount} }";
//    String queryStr = String .format("query FourReposForOrg($orgName: String = \"microsoft\" $first: Int = 4) { organization(login: $orgName) { ...organizationFields } } %s", organizationFieldsFragmentWithFirstParam);
//    GraphQLPayloadDTO graphQL = new GraphQLPayloadDTO().setQuery(queryStr);
//
//    GraphQLResponseDTO resp = apiCall(graphQL, GraphQLResponseDTO.class);
//
//    OrganizationDTO org = resp.getData().getOrganization();
//    assertThat(org.getName()).isEqualTo("Microsoft");
//    assertThat(org.getUrl()).isEqualTo("https://github.com/microsoft");
//    assertThat(org.getRepositories().getEdges().size()).isEqualTo(4);
//  }
//
//  @Test
//  void queryUsingVariablesAndFragment() {
//    // The organization field accepts a login argument but does not have a parameter for "first"
//    // We use the query keyword to accept "orgName" and "first" arguments, and "first" can be used in the fragment
//    String organizationFieldsFragmentWithFirstParam = "fragment organizationFields on Organization{name url repositories(first: $first) {edges{node{name description}} totalCount} }";
//    String queryStr = String .format("query ReposForOrg($orgName: String! $first: Int!) { organization(login: $orgName) { ...organizationFields } } %s", organizationFieldsFragmentWithFirstParam);
//    GraphQLPayloadDTO graphQL = new GraphQLPayloadDTO().setQuery(queryStr);
//    Map<String, Object> variables = new HashMap<>();
//    variables.put("orgName", "microsoft");
//    variables.put("first", 4);
//    graphQL.setVariables(variables);
//
//    GraphQLResponseDTO resp = apiCall(graphQL, GraphQLResponseDTO.class);
//
//    OrganizationDTO org = resp.getData().getOrganization();
//    assertThat(org.getName()).isEqualTo("Microsoft");
//    assertThat(org.getUrl()).isEqualTo("https://github.com/microsoft");
//    assertThat(org.getRepositories().getEdges().size()).isEqualTo(4);
//  }
}

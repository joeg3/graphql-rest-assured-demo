package org.example.graphqldemo;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.path.json.JsonPath;
//import org.example.graphqldemo.selectionset.GraphQlQuery;
import org.example.graphqldemo.selectionset.Query;
//import org.example.graphqldemo.selectionset.QueryDTO;
import org.example.graphqldemo.selectionset.Viewer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.graphqldemo.BaseTest.*;

public class QueryTest {

  @Test
  void basicQueryWithoutVariable() {
    // Since we use the personal access token in the header, which is set in the request specification,
    // this should return expected name and url that is setup in config file
    String graphQL = "{ \"query\": \"query { viewer { name url } }\" }";

    JsonPath json = apiCall(graphQL);

    assertThat(json.getString("data.viewer.name")).isEqualTo(GITHUB_NAME);
    assertThat(json.getString("data.viewer.url")).isEqualTo(GITHUB_URL);
  }

  @Test
  void queryWithHardcodedVariables() {
    String graphQL = "{ \"query\": \"{ organization(login: \\\"microsoft\\\") { name url repositories(first: 4) {edges{node{name description}} totalCount} } }\" }";

    JsonPath json = apiCall(graphQL);

    assertThat(json.getString("data.organization.name")).isEqualTo("Microsoft");
    assertThat(json.getString("data.organization.url")).isEqualTo("https://github.com/microsoft");
  }

  @Test
  void queryWithVariablesQueryParameter() {
    // Parameterize using the graphQL 'variables' construct
    // Since we are doing this in Java, and not in a script, I don't think it really gains much
    String graphQL = "{ \"query\": \"query ($orgName: String!) { organization(login: $orgName) { name url } }\",\"variables\": \"{\\\"orgName\\\": \\\"microsoft\\\"}\"}";

    JsonPath json = apiCall(graphQL);

    json.prettyPrint();
    assertThat(json.getString("data.organization.name")).isEqualTo("Microsoft");
    assertThat(json.getString("data.organization.url")).isEqualTo("https://github.com/microsoft");
  }

  @Test
  void queryWithDefaultParameter() {
    String graphQL = "{ \"query\": \"query ($orgName: String = \\\"microsoft\\\") { organization(login: $orgName) { name url } }\"}";

    JsonPath json = apiCall(graphQL);

    assertThat(json.getString("data.organization.name")).isEqualTo("Microsoft");
    assertThat(json.getString("data.organization.url")).isEqualTo("https://github.com/microsoft");
  }

  @Test
  void queryPassArgumentToFieldWithFragment() {
    // The organization field accepts a login argument
    String organizationFieldsFragment = "fragment organizationFields on Organization{name url }";
    String graphQL = String .format("{ \"query\": \"{ organization(login: \\\"microsoft\\\") { ...organizationFields } } %s\" }", organizationFieldsFragment);

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
    String graphQL = String .format("{ \"query\": \" query ($orgName: String = \\\"microsoft\\\" $first: Int = 4) { organization(login: $orgName) { ...organizationFields } } %s\" }", organizationFieldsFragmentWithFirstParam);
    System.out.println("Fragment graphql: " + graphQL);
    JsonPath json = apiCall(graphQL);
json.prettyPrint();
    assertThat(json.getString("data.organization.name")).isEqualTo("Microsoft");
    assertThat(json.getString("data.organization.url")).isEqualTo("https://github.com/microsoft");
    assertThat(json.getInt("data.organization.repositories.edges.size")).isEqualTo(4);
  }

  //@Test
 // void bodyUsingQueryObject() throws JsonProcessingException {
    //String query = "{ viewer { name url } }";
    //String graphQL = "{ \"query\": \"query { viewer { name url } }\" }";
    //graphQL = String.format("{ \"query\": \"query %s\" }", query);
    //System.out.println("************************ GraphQL Query we want:\n" + graphQL);

//    QueryDTO graphQLqueryObject = new QueryDTO();
//    graphQLqueryObject.setQuery("query { viewer { name url } }");

//    GraphQlQuery graphQlQuery = new GraphQlQuery();
//    Query q = new Query();
//    Viewer v = new Viewer();
//    v.setName("");
//    v.setUrl("");
//    q.setViewer(v);
//    graphQlQuery.setQuery(q);
//
//    ObjectMapper objectMapper = new ObjectMapper();
//    objectMapper.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES,false);
//    objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
//    String queryString = objectMapper.writeValueAsString(graphQlQuery);
//    String viewString = objectMapper.writeValueAsString(v);
//    String innerQueryString = objectMapper.writeValueAsString(q);
//    // { "query": "query { viewer { name url } }" }
//System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& queryString: " + queryString);
//    System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& viewString: " + viewString);
//    System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& innerQueryString: " + innerQueryString);
//    String finalQuery = String.format("{ \"query\": \"query %s\" }", innerQueryString);
//    System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& finalQuery: " + finalQuery);
//    QueryDTO graphQLqueryObject = new QueryDTO();
//    graphQLqueryObject.setQuery(queryString);
//
//    JsonPath jsonPath = apiCall(finalQuery);
//
//    jsonPath.prettyPrint();
//
//  }

}

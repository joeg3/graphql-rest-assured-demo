package org.example.graphqldemo;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.example.graphqldemo.BaseTest.GITHUB_NAME;
import static org.example.graphqldemo.BaseTest.GITHUB_URL;
import static org.example.graphqldemo.BaseTest.*;

public class QueryTest {
  private static  RequestSpecification reqSpec;
  private static  ResponseSpecification resSpec;

  @Test
  void basicQueryWithoutVariable() {
    // Since we use the personal access token in the header, which is set in the request specification,
    // this should return info about that account
    String graphQL = "{ \"query\": \"query { viewer { name url } }\" }";
    System.out.println("GraphQL passed as body to API: " + graphQL);

    JsonPath jsonPath = apiCall(graphQL);

    jsonPath.prettyPrint();
    assertThat(jsonPath.getString("data.viewer.name")).isEqualTo(GITHUB_NAME);
    assertThat(jsonPath.getString("data.viewer.url")).isEqualTo(GITHUB_URL);
  }

  //@Test
  void basicQueryWithVariable() {

    // This works, login parameter hardcoded
    String graphQL1 = "{ \"query\": \"{ organization(login: \\\"microsoft\\\") { name url } }\" }";


    // Parameterize using the graphQL 'variables' construct
    // Since we are doing this in Java, and not in a script, I don't think it really gains much
    String graphQL2 = "{ \"query\": \"query ($organization: String!) { organization(login: $organization) { name url } }\",\"variables\": \"{\\\"organization\\\": \\\"microsoft\\\"}\"}";

    // Default parameter
    String graphQL3 = "{ \"query\": \"query ($organization: String = \\\"microsoft\\\") { organization(login: $organization) { name url } }\"}";

    // Set the graphQL passed to api
    String graphQL = graphQL1;
    System.out.println("GraphQL passed as body to API: " + graphQL);

    JsonPath jsonPath = apiCall(graphQL);

    jsonPath.prettyPrint();
    assertThat(jsonPath.getString("data.organization.name")).isEqualTo("Microsoft");
    assertThat(jsonPath.getString("data.organization.url")).isEqualTo("https://github.com/microsoft");
  }

  //@Test
  void bodyAsSimpleObjectOfQueryAndQueryString() {
    String query = "{ viewer { name url } }";
    String graphQL =" { \"query\": \"query { viewer { login }}\" }";
    graphQL = String.format("{ \"query\": \"query %s\" }", query);
    System.out.println("************************ GraphQL Query:\n" + graphQL);

    QueryDTO graphQLqueryObject = new QueryDTO();
    graphQLqueryObject.setQuery("query { viewer { name url } }");

    JsonPath jsonPath =
      given()
        .spec(reqSpec)
        .body(graphQLqueryObject)
        .when()
        .post("")
        .then()
        //.spec(resSpec)
        .log().body()
        .extract().jsonPath();

    jsonPath.prettyPrint();

  }

  //@Test
  void bodyUsingQueryObject() throws JsonProcessingException {
    //String query = "{ viewer { name url } }";
    //String graphQL = "{ \"query\": \"query { viewer { name url } }\" }";
    //graphQL = String.format("{ \"query\": \"query %s\" }", query);
    //System.out.println("************************ GraphQL Query we want:\n" + graphQL);

//    QueryDTO graphQLqueryObject = new QueryDTO();
//    graphQLqueryObject.setQuery("query { viewer { name url } }");

    GraphQlQuery graphQlQuery = new GraphQlQuery();
    Query q = new Query();
    Viewer v = new Viewer();
    v.setName("");
    v.setUrl("");
    q.setViewer(v);
    graphQlQuery.setQuery(q);

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES,false);
    objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    String queryString = objectMapper.writeValueAsString(graphQlQuery);
    String viewString = objectMapper.writeValueAsString(v);
    String innerQueryString = objectMapper.writeValueAsString(q);
    // { "query": "query { viewer { name url } }" }
System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& queryString: " + queryString);
    System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& viewString: " + viewString);
    System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& innerQueryString: " + innerQueryString);
    String finalQuery = String.format("{ \"query\": \"query %s\" }", innerQueryString);
    System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& finalQuery: " + finalQuery);
    QueryDTO graphQLqueryObject = new QueryDTO();
    graphQLqueryObject.setQuery(queryString);

    JsonPath jsonPath =
      given()
        .spec(reqSpec)
        .body(finalQuery)
        .when()
        .post("")
        .then()
        //.spec(resSpec)
        .log().body()
        .extract().jsonPath();

    jsonPath.prettyPrint();

  }

}

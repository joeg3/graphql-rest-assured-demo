package org.example.graphqldemo;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.example.graphqldemo.GraphqlBaseTest.GITHUB_NAME;
import static org.example.graphqldemo.GraphqlBaseTest.GITHUB_URL;

public class GraphqlTest {
  private static  RequestSpecification reqSpec;
  private static  ResponseSpecification resSpec;

  @BeforeAll
  static void beforeAllTests() {
    reqSpec = GraphqlBaseTest.createRequestSpec();
    resSpec = GraphqlBaseTest.createResponseSpec();
  }

  @Test
  void bodyAsStringResultAsJsonPath() {
    String query = "{ viewer { name url } }";
    String graphQL = "{ \"query\": \"query { viewer { name url } }\" }";
    graphQL = String.format("{ \"query\": \"query %s\" }", query);

    JsonPath jsonPath =
      given()
        .spec(reqSpec)
        .body(graphQL)
        .when()
        .post("")
        .then()
        //.spec(resSpec)
        //.log().body()
        .extract().jsonPath();

    jsonPath.prettyPrint();
    assertThat(jsonPath.getString("data.viewer.name")).isEqualTo(GITHUB_NAME);
    assertThat(jsonPath.getString("data.viewer.url")).isEqualTo(GITHUB_URL);
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

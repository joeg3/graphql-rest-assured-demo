package org.example.graphqldemo;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;

public class BaseTest {
  // These two properties are passed in through Gradle and required to run tests
  public static final String GITHUB_TOKEN = System.getProperty("github.token");
  public static final String PLATFORM = System.getProperty("platform");

  public static  RequestSpecification REQ_SPEC;
  public static  ResponseSpecification RES_SPEC;

  // Values from config.properties
  public static String BASE_URI;
  public static String GITHUB_NAME;
  public static String GITHUB_URL;

  // Load properties file only once for all test classes being run
  static {
    try {
      loadPropertiesFile();
      REQ_SPEC = createRequestSpec();
      RES_SPEC = createResponseSpec();
    } catch (IOException e) {
      throw new ExceptionInInitializerError(e);
    }
  }

  public static RequestSpecification createRequestSpec() {
    return new RequestSpecBuilder()
      .setContentType(ContentType.JSON)
      .addHeader("Authorization", "bearer " + GITHUB_TOKEN)
      .setBaseUri(BASE_URI)
      .build();
  }

  public static ResponseSpecification createResponseSpec() {
    return new ResponseSpecBuilder()
      .expectStatusCode(200)
      .expectContentType(ContentType.JSON)
      .build();
  }

  public static JsonPath apiCall(String graphQL) {
    String cleanGraphQL = removeNewlinesFromString(graphQL);
    return given().
             spec(REQ_SPEC).
             // log().all().
             body(cleanGraphQL).
           when().
             post("").
           then().
             spec(RES_SPEC).
             // log().all().
             extract().jsonPath();
  }

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

  public static <T> T apiCall(GraphQLPayload graphQL, Class<T> responseClass) {
    return given().
    spec(REQ_SPEC).
      // log().all().
      body(graphQL).
      when().
      post("").
      then().
      spec(RES_SPEC).
      // log().body().
      extract().as(responseClass);
  }

  public static String removeNewlinesFromString(String graphQL) {
    return graphQL.replaceAll("[\\t\\n\\r]+","");
  }

  private static void loadPropertiesFile() throws IOException {
    Properties properties = new Properties();
    properties.load(ClassLoader.getSystemResourceAsStream("config.properties"));

    // Populate values from config.properties
    BASE_URI = properties.getProperty(PLATFORM + ".api.base.uri"); // Api we are testing against

    // Expected results from calling API
    GITHUB_NAME = properties.getProperty(PLATFORM + ".github.name");
    GITHUB_URL = properties.getProperty(PLATFORM + ".github.url");
  }
}

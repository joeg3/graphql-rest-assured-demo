package org.example.graphqldemo;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import java.io.IOException;
import java.util.Properties;

public class GraphqlBaseTest {
  public static final String GITHUB_TOKEN = System.getProperty("github.token");

  // Platform property passed in and verified in Gradle
  public static final String PLATFORM = System.getProperty("platform");

  // Values from config.properties
  private static String BASE_URI;
  public static String GITHUB_NAME;
  public static String GITHUB_URL;

  // Load properties file only once for all test classes being run
  static {
    try {
      loadPropertiesFile();
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

  private static void loadPropertiesFile() throws IOException {
    Properties properties = new Properties();
    properties.load(ClassLoader.getSystemResourceAsStream("config.properties"));

    // Platform verified in Gradle, so we kow it's either 'test' or 'staging'
    String platform = PLATFORM.equals("test") ? "test" : "staging";

    // Values from config.properties
    BASE_URI = properties.getProperty(platform + ".api.base.uri"); // Api we are testing against

    // Expected results from calling API
    GITHUB_NAME = properties.getProperty(platform + ".github.name");
    GITHUB_URL = properties.getProperty(platform + ".github.url");
  }
}

# graphql-rest-assured-demo
Demo of Rest Assured for testing GraphQL APIs

## Project Info
- This GraphQL demo tests against a GitHub API at https://api.github.com/graphql using a [personal access token](https://github.com/settings/tokens)
- Test Cases: all test classes run the same tests, but do it in different ways:
    - Tests in the `QueryWithStringTest` class are the most basic and just put the GraphQL queries in strings.
    - Tests in the `QueryWithTextBlockTest` class use text blocks for GraphQL queries in the `GraphQLQueries` class where they are more readable and reusable.
    - Tests in the `QueryWithObjectTest` class reuse the text blocks from the previous test cases, and uses an object instead of strings for the JSON boilerplate that Rest Assured needs to send.
    - Tests in the `QueryReturnObjectTest` class are like `QueryWithObjectTest` except they return Java POJO objects instead of `JsonPath`.
    - Tests in the `QueryUsingOurApiTest` class are like `QueryWithObjectTest` except it uses the `GitHubApi` class which we created. `GitHubApi` hides details of constructing the api call, making each test case simpler, but possibly harder to understand what's going on.
    - Tests in the `QueryWithFileTest` class use queries stored in `.graphql` files. This has the advantage if an organization want's to keep the latest version of the queries in individual files where they can be in source control and used by others with tools like Postman, but at the expense of extra complexity for the test cases.
    - Tests in the `QuerySettingRootTest` class are like `QueryWithObjectTest` except in their Rest Assured call, the root is set to "data" so it can be removed from the JsonPath validation to reduce noise.

## To Create a Java Project Like This From Scratch
- Install `gradle`.
- Create project folder, in this case `graphql-rest-assured-demo` and `cd` into it.
- Run `gradle init --type java-library`. I selected defaults for all the prompts except Source Package, which I named `org.example`.

## Test Configuration
- You'll need to [create a GitHub token](https://help.github.com/en/github/authenticating-to-github/creating-a-personal-access-token-for-the-command-line) for authentication.
- This project uses a Gradle project property called `platform` to show how a test suite can be configured for different api endpoints to test against (test, staging). For each platform, its corresponding REST endpoint URL and other values are setup in `src/test/resources/config.properties`. At runtime, you select which platform to run the tests against.
- The test will not run without the two Gradle properties: GitHub token, and test platform. The `github.token` and `platform` properties can be specified on the command line, as an environment variable, or in a Gradle properties file (see next section).
- It's easiest to set default values for each of these properties as environment variables or in property files and then override as needed from the command line.

## Gradle Project Properties
Here are the ways to specify the required `platform` and `github.token` properties for the Gradle build, in order of precedence. These examples override both properties, but you can override just one property.
1. Command line, specify property: `./gradlew test -Pplatform=staging -Pgithub.token=123456`
2. Command line, Java system property: `./gradlew test -Dorg.gradle.project.platform=staging -Pgithub.token=123456`. However, I read that Gradle may fork the build in a new JVM, so the -D argument may not be passed along.
3. Environment variable: `ORG_GRADLE_PROJECT_platform=staging ORG_GRADLE_PROJECT_github.token=123456 ./gradlew test`
4. Set/change default in user `gradle.properties` file, usually in `~/.gradle/gradle.properties`, add values like `platform=staging github.token=123456` to file. This will then be the default and no need to specify on command line: `./gradlew test`.
5. Set/change default in project `gradle.properties` file, in project root directory, add values like `platform=staging github.token=123456` to file. This will then be the default and no need to specify on command line: `./gradlew test`.

## Run the tests
These examples assume the platform and GitHub token properties have defaults set in a property file or environment variable. See above section for different ways to specify the platform at run time.
- Run all tests: `./gradlew test`
- Run all tests in one class: `./gradlew test --tests org.example.graphqldemo.QueryWithStringTest`
- Run one test case: `./gradlew test --tests org.example.graphqldemo.QueryWithStringTest.queryWithDefaultParameter`

## References
- GitHub GraphQL API: https://developer.github.com/v4/
- GitHub GraphQL Docs: https://docs.github.com/en/free-pro-team@latest/graphql



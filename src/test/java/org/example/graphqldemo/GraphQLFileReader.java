package org.example.graphqldemo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class GraphQLFileReader {
    private static String NAME_AND_URL_QUERY = null;
    private static String MICROSOFT_HARDCODED_QUERY = null;
    private static String ORG_URL_QUERY = null;
    private static String ORG_URL_MICROSOFT_DEFAULT_QUERY = null;
    private static String ORG_URL_WITH_FRAGMENT_QUERY = null;
    private static String ORG_FIELDS_BASIC_FRAGMENT = null;
    private static String FIRST_REPOS_FOR_ORG_WITH_FRAGMENT_QUERY = null;
    private static String ORG_FIELDS_WITH_REPOS_FRAGMENT = null;

    public static String getNameAndUrlQuery() throws IOException {
        if (NAME_AND_URL_QUERY != null) { return NAME_AND_URL_QUERY; }
        NAME_AND_URL_QUERY = Files.readString(Path.of("src/test/resources/NameAndUrlQuery.graphql"));
        return NAME_AND_URL_QUERY;
    }

    public static String getMicrosoftHardcodedQuery() throws IOException {
        if (MICROSOFT_HARDCODED_QUERY != null) { return MICROSOFT_HARDCODED_QUERY; }
        MICROSOFT_HARDCODED_QUERY = Files.readString(Path.of("src/test/resources/MicrosoftHardcodedQuery.graphql"));
        return MICROSOFT_HARDCODED_QUERY;
    }

    public static String getOrgNameAndUrlQuery() throws IOException {
        if (ORG_URL_QUERY != null) { return ORG_URL_QUERY; }
        ORG_URL_QUERY = Files.readString(Path.of("src/test/resources/OrgAndUrlQuery.graphql"));
        return ORG_URL_QUERY;
    }

    public static String getOrgNameAndUrlMicrosoftDefault() throws IOException {
        if (ORG_URL_MICROSOFT_DEFAULT_QUERY != null) { return ORG_URL_MICROSOFT_DEFAULT_QUERY; }
        ORG_URL_MICROSOFT_DEFAULT_QUERY = Files.readString(Path.of("src/test/resources/OrgAndNameMicrosoftDefaultQuery.graphql"));
        return ORG_URL_MICROSOFT_DEFAULT_QUERY;
    }

    public static String getOrgNameAndUrlWithFragmentQuery() throws IOException {
        if (ORG_URL_WITH_FRAGMENT_QUERY != null) { return ORG_URL_WITH_FRAGMENT_QUERY; }
        ORG_URL_WITH_FRAGMENT_QUERY = Files.readString(Path.of("src/test/resources/OrgAndUrlWithFragmentQuery.graphql"));
        return ORG_URL_WITH_FRAGMENT_QUERY;
    }

    public static String getOrgFieldsBasicFragment() throws IOException {
        if (ORG_FIELDS_BASIC_FRAGMENT != null) { return ORG_FIELDS_BASIC_FRAGMENT; }
        ORG_FIELDS_BASIC_FRAGMENT = Files.readString(Path.of("src/test/resources/OrgFieldsBasicFragment.graphql"));
        return ORG_FIELDS_BASIC_FRAGMENT;
    }

    public static String getFirstReposForOrgWithFragmentQuery() throws IOException {
        if (FIRST_REPOS_FOR_ORG_WITH_FRAGMENT_QUERY != null) { return FIRST_REPOS_FOR_ORG_WITH_FRAGMENT_QUERY; }
        FIRST_REPOS_FOR_ORG_WITH_FRAGMENT_QUERY = Files.readString(Path.of("src/test/resources/FirstReposForOrgWithFragmentQuery.graphql"));
        return FIRST_REPOS_FOR_ORG_WITH_FRAGMENT_QUERY;
    }

    public static String getOrgFieldsWithReposFragment() throws IOException {
        if (ORG_FIELDS_WITH_REPOS_FRAGMENT != null) { return ORG_FIELDS_WITH_REPOS_FRAGMENT; }
        ORG_FIELDS_WITH_REPOS_FRAGMENT = Files.readString(Path.of("src/test/resources/OrgFieldsWithReposFragment.graphql"));
        return ORG_FIELDS_WITH_REPOS_FRAGMENT;
    }
}

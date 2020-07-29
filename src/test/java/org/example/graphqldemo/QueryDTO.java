package org.example.graphqldemo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "query")
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryDTO {
    String query;

    public String getQuery() {
        return query;
    }

    public QueryDTO setQuery(String query) {
        this.query = query;
        return this;
    }

    //    private Query query;
//
//    public Query getQuery() {
//        return query;
//    }
//
//    public QueryDTO setQuery(Query query) {
//        this.query = query;
//        return this;
//    }
}

package org.example.graphqldemo;

public class GraphQLQueries {

    public static String NAME_AND_URL_QUERY = """
            query NameAndUrl {      \
              viewer {              \
                name                \
                url                 \
              }                     \
            }                       \
            """;

    public static String MICROSOFT_FOUR_REPOS_QUERY = """
            query MicrosoftFourRepos {                  
              organization(login: \\\"microsoft\\\") {  
                name                                    
                url                                     
                repositories(first: 4) {                
                  edges {                               
                    node {                              
                      name                              
                      description                       
                    }                                   
                  }                                     
                  totalCount                            
                }                                       
              }                                         
            }                                           
            """;

    public static String ORG_URL_QUERY = """
            query OrgUrl($orgName: String!) {    
              organization(login: $orgName) {    
                name                             
                url                              
              }                                  
            }                                    
            """;

    public static  String ORG_URL_QUERY_WITH_FRAGMENT = """
            query OrgUrl($orgName: String!) {    
              organization(login: $orgName) {    
                ...organizationFields            
              }                                  
            }                                    
            """;

    public static String ORG_FIELDS_BASIC_FRAGMENT = """
            fragment organizationFields on Organization {  
              name                                         
              url                                          
            }                                              
            """;

    public static String MICROSOFT_ORG_URL_QUERY = """
            query MicrosoftOrgUrl($orgName: String = \\\"microsoft\\\") {  
              organization(login: $orgName) {                              
                name                                                       
                url                                                        
              }                                                            
            }                                                              
            """;

    public static String FIRST_REPOS_FOR_ORG_QUERY_WITH_FRAGMENT = """
            query FirstReposForOrg($orgName: String! $first: Int!) {  
              organization(login: $orgName) {                        
                ...organizationFields                                
              }                                                      
            }                                                        
            """;
    public static String ORG_FIELDS_WITH_REPOS_FRAGMENT = """
            fragment organizationFields on Organization {  
              name                                         
              url                                          
              repositories (first: $first) {               
                edges {                                    
                  node {                                   
                    name                                   
                    description                            
                  }                                        
                }                                          
                totalCount                                 
              }                                            
            }                                              
            """;
}

package org.example.graphqldemo.dto;

public class OrganizationDTO {
  private String name;
  private String url;
  private RepositoriesDTO repositories;

  public String getName() {
    return name;
  }
  public String getUrl() {
    return url;
  }
  public RepositoriesDTO getRepositories() { return repositories; }

  public OrganizationDTO setName(String name) {
    this.name = name;
    return this;
  }

  public OrganizationDTO setUrl(String url) {
    this.url = url;
    return this;
  }

  public OrganizationDTO setRepositories(RepositoriesDTO repositories) {
    this.repositories = repositories;
    return this;
  }
}

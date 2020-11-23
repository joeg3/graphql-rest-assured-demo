package org.example.graphqldemo.dto;

public class DataDTO {
  private OrganizationDTO organization;
  private ViewerDTO viewer;

  public OrganizationDTO getOrganization() { return organization; }

  public DataDTO setOrganization(OrganizationDTO organization) {
    this.organization = organization;
    return this;
  }

  public ViewerDTO getViewer() { return viewer; }

  public DataDTO setViewer(ViewerDTO viewer) {
    this.viewer = viewer;
    return this;
  }
}

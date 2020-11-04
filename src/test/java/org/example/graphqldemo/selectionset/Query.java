package org.example.graphqldemo.selectionset;

public class Query {
  private Viewer viewer;

  public Viewer getViewer() {
    return viewer;
  }

  public Query setViewer(Viewer viewer) {
    this.viewer = viewer;
    return this;
  }
}

package com.nwalter.gwt.scss.client.impl;

import com.google.common.annotations.VisibleForTesting;
import com.google.gwt.safehtml.shared.SafeUri;
import com.nwalter.gwt.scss.client.LinkInjector;
import com.nwalter.gwt.scss.client.ScssResource;

/**
 * Simple implementation of {@link ScssResource}.
 */
public class ScssResourcePrototype implements ScssResource {

  private String name;
  private SafeUri uri;
  private boolean injected;

  /**
   * Only called by rebind code.
   * 
   * @param name
   *          the name of the resource
   * @param uri
   *          the uri of the resource
   */
  public ScssResourcePrototype(String name, SafeUri uri) {
    super();
    this.name = name;
    this.uri = uri;
  }

  @VisibleForTesting
  public SafeUri getUri() {
    return uri;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean ensureInjected() {
    if (!injected) {
      injected = true;
      LinkInjector.injectLink(this.uri, "stylesheet");
      return true;
    }
    return false;
  }
}

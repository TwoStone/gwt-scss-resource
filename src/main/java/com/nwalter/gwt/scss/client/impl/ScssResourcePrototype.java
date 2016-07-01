package com.nwalter.gwt.scss.client.impl;

import com.google.common.annotations.VisibleForTesting;
import com.google.gwt.dom.client.HeadElement;
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
    private HeadElement head;

    /**
     * Only called by rebind code
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

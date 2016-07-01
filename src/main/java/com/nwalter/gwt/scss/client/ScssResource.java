package com.nwalter.gwt.scss.client;

import com.google.gwt.resources.client.ResourcePrototype;
import com.google.gwt.resources.ext.DefaultExtensions;
import com.google.gwt.resources.ext.ResourceGeneratorType;
import com.nwalter.gwt.scss.rebind.ScssBundleGenerator;

/**
 * Aggregates and compiles SCSS stylesheets to CSS.
 * A {@link ScssResource} represents a general SCSS file.
 */
@DefaultExtensions({".scss", ".sass"})
@ResourceGeneratorType(ScssBundleGenerator.class)
public interface ScssResource extends ResourcePrototype {

    /**
     * Injects the {@link ScssResource} into the host page.
     * Repeated calls to this method on an instance of a {@link ScssResource} will have no effect.
     *
     * @return <code>true</code> if this method mutated the DOM.
     */
    boolean ensureInjected();
}

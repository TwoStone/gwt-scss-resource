package com.nwalter.gwt.scss.client;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.gwt.resources.client.ResourcePrototype;
import com.google.gwt.resources.ext.DefaultExtensions;
import com.google.gwt.resources.ext.ResourceGeneratorType;
import com.nwalter.gwt.scss.rebind.ScssBundleGenerator;

/**
 * Aggregates and compiles SCSS stylesheets to CSS. A {@link ScssResource} represents a general SCSS
 * file.
 */
@DefaultExtensions({ ".scss", ".sass" })
@ResourceGeneratorType(ScssBundleGenerator.class)
public interface ScssResource extends ResourcePrototype {

  /**
   * Url mode specifies how urls appearing in an scss style sheet are
   * interpreted. When url mode is absolute, urls will appear in the generated
   * css as they are in the original scss file. In relative mode the folder of
   * an imported scss stylesheet is taken into account: for instance, when
   * importing the stylesheet foo/bar.scss containing url(baz.png), the url
   * will be output as url(foo/baz.png). Mixed mode is a mixture of absolute
   * and relative modes: most urls are taken to be absolute, but in simple
   * properties they are relative.
   */
  public enum UrlMode {
      ABSOLUTE, MIXED, RELATIVE
  }

  @Documented
  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.METHOD)
  public @interface SassUrlMode {
    /**
     * Sets the urlMode of the sass compiler.
     * Url mode specifies how urls appearing in an scss style sheet are interpreted.
     * @see UrlMode
     */
    UrlMode value();
  }

  @Documented
  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.METHOD)
  public @interface IncludePaths {
    /**
     * An array of paths that the Scss compiler can look
     * in to attempt to resolve your @import declarations.
     */
    String[] value();
  }

  /**
   * Injects the {@link ScssResource} into the host page. Repeated calls to this method on an
   * instance of a {@link ScssResource} will have no effect.
   *
   * @return <code>true</code> if this method mutated the DOM.
   */
  boolean ensureInjected();
}

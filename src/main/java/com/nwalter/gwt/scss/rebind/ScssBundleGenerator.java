package com.nwalter.gwt.scss.rebind;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.net.URL;
import java.nio.file.Files;

import com.google.gwt.codegen.server.SourceWriter;
import com.google.gwt.codegen.server.StringSourceWriter;
import com.google.gwt.core.ext.BadPropertyValueException;
import com.google.gwt.core.ext.PropertyOracle;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.resources.ext.AbstractResourceGenerator;
import com.google.gwt.resources.ext.ClientBundleRequirements;
import com.google.gwt.resources.ext.ResourceContext;
import com.google.gwt.resources.ext.ResourceGenerator;
import com.google.gwt.resources.ext.ResourceGeneratorUtil;
import com.google.gwt.safehtml.shared.UriUtils;
import com.nwalter.gwt.scss.client.ScssResource.IncludePaths;
import com.nwalter.gwt.scss.client.ScssResource.SassUrlMode;
import com.nwalter.gwt.scss.client.ScssResource.UrlMode;
import com.nwalter.gwt.scss.client.impl.ScssResourcePrototype;
import com.vaadin.sass.internal.ScssContext;
import com.vaadin.sass.internal.ScssStylesheet;
import com.vaadin.sass.internal.handler.SCSSDocumentHandlerImpl;
import com.vaadin.sass.internal.resolver.FilesystemResolver;

/**
 * ResourceGenerator implementation that compiles and minifies ScssResources to CSS.
 */
public class ScssBundleGenerator extends AbstractResourceGenerator implements ResourceGenerator {

  private static final String MINIFY_KEY = "scss.minify";

  @Override
  public void init(final TreeLogger logger, final ResourceContext context) throws UnableToCompleteException {
    final ClientBundleRequirements requirements = context.getRequirements();
    try {
      requirements.addConfigurationProperty(MINIFY_KEY);
    } catch (final BadPropertyValueException ex) {
      logger.log(Type.ERROR, ex.getMessage(), ex);
      throw new UnableToCompleteException();
    }
  }

  @Override
  public String createAssignment(final TreeLogger logger, final ResourceContext context, final JMethod method)
      throws UnableToCompleteException {

    final URL[] resources = ResourceGeneratorUtil.findResources(logger, context, method);

    if (resources.length != 1) {
      logger.log(Type.ERROR, "Exactly one resource must be specified");
      throw new UnableToCompleteException();
    }

    try {
      final PropertyOracle propertyOracle = context.getGeneratorContext().getPropertyOracle();
      final String minifyProperty = propertyOracle.getConfigurationProperty(MINIFY_KEY).getValues()
          .get(0);
      final boolean minify = Boolean.parseBoolean(minifyProperty);

      String[] customPaths = null;
      final IncludePaths includePaths = method.getAnnotation(IncludePaths.class);
      if (includePaths != null) {
        customPaths = includePaths.value();
      }

      final SassUrlMode urlModeAnnotation = method.getAnnotation(SassUrlMode.class);
      final UrlMode urlMode = urlModeAnnotation != null ? urlModeAnnotation.value() : null;

      final File input = new File(resources[0].toURI());
      final File outputFile = this.compileScss(logger, input, minify, customPaths, urlMode);

      final String result = context.deploy(outputFile.toURI().toURL(), "text/css", true);

      final SourceWriter sw = new StringSourceWriter();
      sw.println("new " + ScssResourcePrototype.class.getName() + "(");
      sw.indent();
      sw.println('"' + method.getName() + "\",");
      sw.println(UriUtils.class.getName() + ".fromTrustedString(" + result + ")");
      sw.outdent();
      sw.print(")");

      return sw.toString();

    } catch (final Exception ex) {
      logger.log(TreeLogger.ERROR, ex.getMessage());
      throw new UnableToCompleteException();
    }
  }

  private File compileScss(
      final TreeLogger logger,
      final File input,
      final boolean minify,
      final String[] customPaths,
      final UrlMode urlMode) throws Exception {

    final ScssStylesheet stylesheet = ScssStylesheet.get(input.getCanonicalPath(), null,
        new SCSSDocumentHandlerImpl(), new GwtErrorHandler(logger, input));

    if (customPaths != null) {
      stylesheet.addResolver(new FilesystemResolver(customPaths));
    }

    final com.vaadin.sass.internal.ScssContext.UrlMode mode = this.mapUrlMode(urlMode);
    stylesheet.compile(mode);

    final File tempFile = Files.createTempFile("gwt-sass", ".css").toFile();
    try (Writer w = new FileWriter(tempFile)) {
      stylesheet.write(w, minify);
    }
    return tempFile;
  }

  private com.vaadin.sass.internal.ScssContext.UrlMode mapUrlMode(final UrlMode mode) {
    if (mode == null) {
      return ScssContext.UrlMode.MIXED;
    }

    switch (mode) {
      case ABSOLUTE:
        return ScssContext.UrlMode.ABSOLUTE;
      case MIXED:
        return ScssContext.UrlMode.MIXED;
      case RELATIVE:
        return ScssContext.UrlMode.RELATIVE;
      default:
        throw new IllegalArgumentException("Unknown url mode " + mode); //$NON-NLS-1$
    }
  }

}

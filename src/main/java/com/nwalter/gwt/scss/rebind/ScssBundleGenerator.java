package com.nwalter.gwt.scss.rebind;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.net.URL;
import java.nio.file.Files;

import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CSSParseException;

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
import com.nwalter.gwt.scss.client.impl.ScssResourcePrototype;
import com.vaadin.sass.internal.ScssStylesheet;
import com.vaadin.sass.internal.handler.SCSSDocumentHandlerImpl;
import com.vaadin.sass.internal.handler.SCSSErrorHandler;

/**
 * ResourceGenerator implementation that compiles and minifies ScssResources to CSS.
 */
public class ScssBundleGenerator extends AbstractResourceGenerator implements ResourceGenerator {

  /**
   * {@link SCSSErrorHandler} that delegates messages to the TreeLogger.
   */
  private static class GwtErrorHandler extends SCSSErrorHandler {

    private TreeLogger logger;

    private GwtErrorHandler(TreeLogger logger) {
      this.logger = logger;
    }

    @Override
    public void error(CSSParseException ex) throws CSSException {
      super.error(ex);
      logger.log(Type.ERROR, ex.getMessage(), ex);
    }

    @Override
    public void warning(CSSParseException ex) throws CSSException {
      super.warning(ex);
      logger.log(Type.WARN, ex.getMessage(), ex);
    }

    @Override
    public void fatalError(CSSParseException ex) throws CSSException {
      super.fatalError(ex);
      logger.log(Type.ERROR, ex.getMessage(), ex);
    }
  }

  private static final String MINIFY_KEY = "scss.minify";

  @Override
  public void init(TreeLogger logger, ResourceContext context) throws UnableToCompleteException {
    ClientBundleRequirements requirements = context.getRequirements();
    try {
      requirements.addConfigurationProperty(MINIFY_KEY);
    } catch (BadPropertyValueException ex) {
      logger.log(Type.ERROR, ex.getMessage(), ex);
      throw new UnableToCompleteException();
    }
  }

  @Override
  public String createAssignment(TreeLogger logger, ResourceContext context, JMethod method)
      throws UnableToCompleteException {

    URL[] resources = ResourceGeneratorUtil.findResources(logger, context, method);

    if (resources.length != 1) {
      logger.log(Type.ERROR, "Exactly one resource must be specified");
      throw new UnableToCompleteException();
    }

    try {
      PropertyOracle propertyOracle = context.getGeneratorContext().getPropertyOracle();
      String minifyProperty = propertyOracle.getConfigurationProperty(MINIFY_KEY).getValues()
          .get(0);
      boolean minify = Boolean.parseBoolean(minifyProperty);

      File input = new File(resources[0].toURI());
      File outputFile = compileScss(logger, input, minify);

      String result = context.deploy(outputFile.toURI().toURL(), "text/css", true);

      SourceWriter sw = new StringSourceWriter();
      sw.println("new " + ScssResourcePrototype.class.getName() + "(");
      sw.indent();
      sw.println('"' + method.getName() + "\",");
      sw.println(UriUtils.class.getName() + ".fromTrustedString(" + result + ")");
      sw.outdent();
      sw.print(")");

      return sw.toString();

    } catch (Exception ex) {
      logger.log(TreeLogger.ERROR, ex.getMessage());
      throw new UnableToCompleteException();
    }
  }

  private File compileScss(TreeLogger logger, File input, boolean minify) throws Exception {
    ScssStylesheet stylesheet = ScssStylesheet.get(input.getCanonicalPath(), null,
        new SCSSDocumentHandlerImpl(), new GwtErrorHandler(logger));
    stylesheet.compile();

    File tempFile = Files.createTempFile("gwt-sass", ".css").toFile();
    try (Writer w = new FileWriter(tempFile)) {
      stylesheet.write(w, minify);
    }
    return tempFile;
  }

}

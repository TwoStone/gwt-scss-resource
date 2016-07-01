package com.nwalter.gwt.scss.rebind;

import com.google.gwt.codegen.server.SourceWriter;
import com.google.gwt.codegen.server.StringSourceWriter;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.resources.ext.AbstractResourceGenerator;
import com.google.gwt.resources.ext.ResourceContext;
import com.google.gwt.resources.ext.ResourceGenerator;
import com.google.gwt.resources.ext.ResourceGeneratorUtil;
import com.google.gwt.safehtml.shared.UriUtils;
import com.nwalter.gwt.scss.client.impl.ScssResourcePrototype;
import com.vaadin.sass.internal.ScssStylesheet;
import com.vaadin.sass.internal.handler.SCSSDocumentHandlerImpl;
import com.vaadin.sass.internal.handler.SCSSErrorHandler;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CSSParseException;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.net.URL;
import java.nio.file.Files;

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
        public void error(CSSParseException e) throws CSSException {
            super.error(e);
            logger.log(Type.ERROR, e.getMessage(), e);
        }

        @Override
        public void warning(CSSParseException e) throws CSSException {
            super.warning(e);
            logger.log(Type.WARN, e.getMessage(), e);
        }

        @Override
        public void fatalError(CSSParseException e) throws CSSException {
            super.fatalError(e);
            logger.log(Type.ERROR, e.getMessage(), e);
        }
    }

    @Override
    public String createAssignment(TreeLogger logger, ResourceContext context, JMethod method)
            throws UnableToCompleteException {
        URL[] resources = ResourceGeneratorUtil.findResources(logger, context, method);

        try {
            File input = new File(resources[0].toURI());
            File outputFile = compileScsss(logger, input);

            String result = context.deploy(outputFile.toURI().toURL(), "text/css", true);

            SourceWriter sw = new StringSourceWriter();
            sw.println("new " + ScssResourcePrototype.class.getName() + "(");
            sw.indent();
            sw.println('"' + method.getName() + "\",");
            sw.println(UriUtils.class.getName() + ".fromTrustedString(" + result + ")");
            sw.outdent();
            sw.print(")");

            return sw.toString();

        } catch (Exception e) {
            logger.log(TreeLogger.ERROR, e.getMessage());
            throw new UnableToCompleteException();
        }
    }

    private File compileScsss(TreeLogger logger, File input) throws Exception {
        ScssStylesheet stylesheet = ScssStylesheet.get(
                input.getCanonicalPath(),
                null,
                new SCSSDocumentHandlerImpl(),
                new GwtErrorHandler(logger));
        stylesheet.compile();

        File tempFile = Files.createTempFile("gwt-sass", ".css").toFile();
        try (Writer w = new FileWriter(tempFile)) {
            stylesheet.write(w);
        }
        return tempFile;
    }


}

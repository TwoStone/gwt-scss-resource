package com.nwalter.gwt.scss.rebind;

import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CSSParseException;

import java.io.File;
import java.text.MessageFormat;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.vaadin.sass.internal.handler.SCSSErrorHandler;

/**
 * {@link SCSSErrorHandler} that delegates messages to the TreeLogger.
 */
class GwtErrorHandler extends SCSSErrorHandler {

  private TreeLogger logger;
  private File file;

  GwtErrorHandler(TreeLogger logger, File file) {
    this.logger = logger;
    this.file = file;
  }

  @Override
  public void error(CSSParseException ex) throws CSSException {
    super.error(ex);
    logger.log(Type.ERROR, MessageFormat.format("Error in file {0}: {3} (line {1} column {2})", 
        file.getPath(),
        ex.getLineNumber(),
        ex.getColumnNumber(),
        ex.getMessage()), ex);
  }

  @Override
  public void warning(CSSParseException ex) throws CSSException {
    super.warning(ex);
    logger.log(Type.WARN, MessageFormat.format("Warning in file {0}: {3} (line {1} column {2})", 
        file.getPath(),
        ex.getLineNumber(),
        ex.getColumnNumber(),
        ex.getMessage()), ex);
  }

  @Override
  public void fatalError(CSSParseException ex) throws CSSException {
    super.fatalError(ex);
    logger.log(Type.ERROR, MessageFormat.format("Fatal error in file {0}: {3} (line {1} column {2})", 
        file.getPath(),
        ex.getLineNumber(),
        ex.getColumnNumber(),
        ex.getMessage()), ex);
  }
}
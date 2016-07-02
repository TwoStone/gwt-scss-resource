package com.nwalter.gwt.scss.rebind;

import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CSSParseException;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.vaadin.sass.internal.handler.SCSSErrorHandler;

/**
 * {@link SCSSErrorHandler} that delegates messages to the TreeLogger.
 */
class GwtErrorHandler extends SCSSErrorHandler {

  private TreeLogger logger;

  GwtErrorHandler(TreeLogger logger) {
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
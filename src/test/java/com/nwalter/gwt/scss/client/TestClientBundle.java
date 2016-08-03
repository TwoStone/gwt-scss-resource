package com.nwalter.gwt.scss.client;

import com.google.gwt.resources.client.ClientBundle;

public interface TestClientBundle extends ClientBundle {

  ScssResource style();
  
  ScssResource missingBracket();
}

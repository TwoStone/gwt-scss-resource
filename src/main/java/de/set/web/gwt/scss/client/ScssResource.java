package de.set.web.gwt.scss.client;

import com.google.gwt.resources.client.ResourcePrototype;
import com.google.gwt.resources.ext.DefaultExtensions;
import com.google.gwt.resources.ext.ResourceGeneratorType;
import de.set.web.gwt.scss.generator.ScssBundleGenerator;

@DefaultExtensions({".scss", ".sass"})
@ResourceGeneratorType(ScssBundleGenerator.class)
public interface ScssResource extends ResourcePrototype {
	
	boolean ensureInjected();
}

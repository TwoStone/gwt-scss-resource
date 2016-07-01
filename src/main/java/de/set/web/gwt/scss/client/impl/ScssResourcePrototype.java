package de.set.web.gwt.scss.client.impl;

import com.google.common.annotations.VisibleForTesting;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadElement;
import com.google.gwt.dom.client.LinkElement;
import com.google.gwt.dom.client.Text;
import com.google.gwt.safehtml.shared.SafeUri;
import de.set.web.gwt.scss.client.ScssResource;

public class ScssResourcePrototype implements ScssResource {

	private String name;
	private SafeUri uri; 
	private boolean injected;
	private HeadElement head;
	
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
			injectAsLink(uri);
			return true;
		}
		return false;
	}

	private void injectAsLink(SafeUri href) {
		Text comment = Document.get().createTextNode("<!-- " + name + " -->");
		getHead().appendChild(comment);
		LinkElement link = Document.get().createLinkElement();
		link.setHref(href.asString());
		link.setRel("stylesheet");
		getHead().appendChild(link);
	}
	
	private HeadElement getHead() {
	      if (head == null) {
	        Element elt = Document.get().getElementsByTagName(HeadElement.TAG).getItem(0);
	        assert elt != null : "The host HTML page does not have a <head> element"
	            + " which is required by StyleInjector";
	        head = HeadElement.as(elt);
	      }
	      return head;
	    }
}

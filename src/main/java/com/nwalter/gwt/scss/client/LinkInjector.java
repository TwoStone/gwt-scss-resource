package com.nwalter.gwt.scss.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadElement;
import com.google.gwt.dom.client.LinkElement;
import com.google.gwt.safehtml.shared.SafeUri;

/**
 * Injects a link element in the head of the document.
 */
public class LinkInjector {

    private static final LinkInjector IMPL = GWT.create(LinkInjector.class);

    private HeadElement head;

    public static LinkElement injectLink(SafeUri href, String rel) {
        return LinkInjector.IMPL.inject(href, rel);
    }

    /**
     * Injects a link element in the head of the document.
     *
     * @param href the uri of the resource
     * @param rel the rel of the resource
     *
     * @return the injected LinkElement
     */
    public LinkElement inject(SafeUri href, String rel) {
        LinkElement link = Document.get().createLinkElement();
        link.setHref(href.asString());
        link.setRel(rel);
        getHead().appendChild(link);
        return link;
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

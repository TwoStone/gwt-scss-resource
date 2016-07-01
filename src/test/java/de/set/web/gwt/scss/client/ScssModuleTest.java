package de.set.web.gwt.scss.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadElement;
import com.google.gwt.dom.client.LinkElement;
import com.google.gwt.dom.client.Node;
import com.google.gwt.junit.client.GWTTestCase;
import de.set.web.gwt.scss.client.impl.ScssResourcePrototype;

public class ScssModuleTest extends GWTTestCase {

	@Override
	public String getModuleName() {
		return "de.set.web.gwt.scss.ScssTestModule";
	}
	
	public void testInjectInDocument() {
		TestClientBundle bundle = GWT.create(TestClientBundle.class);
		ScssResourcePrototype sass = (ScssResourcePrototype) bundle.style();
		sass.ensureInjected();
		Element head = Document.get().getElementsByTagName(HeadElement.TAG).getItem(0);
		Node linkNode = head.getLastChild();
		LinkElement link = LinkElement.as(Element.as(linkNode));
		assertEquals(link.getRel(), "stylesheet");
		assertTrue(link.getHref().endsWith(sass.getUri().asString()));
	}

}

package de.set.web.gwt.scss.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import de.set.web.gwt.scss.client.ScssResource;

public class ScssTestModule implements EntryPoint {

	private ScssResource sass;
	
	@Override
	public void onModuleLoad() {
		TestClientBundle create = GWT.create(TestClientBundle.class);
		sass = create.style();
	}

}

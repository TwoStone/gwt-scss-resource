package com.nwalter.gwt.scss.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

public class ScssTestModule implements EntryPoint {

	private ScssResource sass;
	
	@Override
	public void onModuleLoad() {
		TestClientBundle create = GWT.create(TestClientBundle.class);
		sass = create.style();
	}

}

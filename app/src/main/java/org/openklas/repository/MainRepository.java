package org.openklas.repository;


import org.openklas.net.JSONService;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MainRepository {
	private final JSONService mService;

	@Inject
	public MainRepository(JSONService service) {
		mService = service;
	}
}

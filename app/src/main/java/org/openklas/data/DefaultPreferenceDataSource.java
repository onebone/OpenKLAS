package org.openklas.data;


import org.jetbrains.annotations.Nullable;
import org.openklas.MainApplication;

import javax.inject.Inject;
import javax.inject.Singleton;

import pyxis.uzuki.live.richutilskt.utils.RPreference;

@Singleton
public class DefaultPreferenceDataSource implements PreferenceDataSource {
	private final RPreference mPreference;

	private static final String USERID_KEY = "1af49084-8820-449e-9039-d86863184592";
	private static final String PASSWORD_KEY = "1af49084-8820-449e-9039-d86863184593";

	@Inject
	public DefaultPreferenceDataSource(MainApplication application) {
		mPreference = RPreference.getInstance(application);
	}

	@Nullable
	@Override
	public String getUserID() {
		return mPreference.getString(USERID_KEY, "");
	}

	@Override
	public void setUserID(String userID) {
		mPreference.put(USERID_KEY, userID);
	}

	@Nullable
	@Override
	public String getPassword() {
		return mPreference.getString(PASSWORD_KEY, "");
	}

	@Override
	public void setPassword(String password) {
		mPreference.put(PASSWORD_KEY, password);
	}
}

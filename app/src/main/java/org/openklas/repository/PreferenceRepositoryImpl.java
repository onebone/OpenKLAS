package org.openklas.repository;


import org.openklas.MainApplication;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import pyxis.uzuki.live.richutilskt.utils.RPreference;



@Singleton
public class PreferenceRepositoryImpl implements PreferenceRepository {
    private RPreference mPreference;

    private static final String USERID_KEY = "1af49084-8820-449e-9039-d86863184592";

    @Inject
    public PreferenceRepositoryImpl(MainApplication application) {
        mPreference = RPreference.getInstance(application);
    }

    @NotNull
    @Override
    public String getUserID() {
        return mPreference.getString(USERID_KEY, "");
    }

    @Override
    public void setUserID(@NotNull String userID) {
        mPreference.put(USERID_KEY, userID);
    }
}

package org.openklas;

import android.app.Activity;
import android.app.Service;
import android.content.Context;

import com.github.windsekirun.daggerautoinject.DaggerAutoInject;
import com.github.windsekirun.daggerautoinject.InjectApplication;
import org.openklas.di.AppComponent;

import org.openklas.base.BaseApplication;
import org.openklas.di.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasServiceInjector;
import pyxis.uzuki.live.attribute.parser.annotation.AttributeParser;

/**
 * OpenKlas
 * Class: MainApplication
 * Created by limmoong on 2020/11/10.
 * <p>
 * Description:
 */
@AttributeParser("org.openklas")
@InjectApplication(component = AppComponent.class)
public class MainApplication extends BaseApplication implements HasActivityInjector, HasServiceInjector {
    @Inject
    DispatchingAndroidInjector<Activity> mActivityDispatchingAndroidInjector;
    @Inject
    DispatchingAndroidInjector<Service> mServiceDispatchingAndroidInjector;
    private static AppComponent mAppComponent;

    @Override
    public String getConfigFilePath() {
        return "config.json";
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mAppComponent = DaggerAppComponent.builder()
                .application(this)
                .build();

        DaggerAutoInject.init(this, mAppComponent);

    }

    @SuppressWarnings("unchecked")
    public static MainApplication getApplication(Context context) {
        if (context instanceof BaseApplication) {
            return (MainApplication) context;
        }

        return (MainApplication) context.getApplicationContext();
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return mActivityDispatchingAndroidInjector;
    }

    @Override
    public AndroidInjector<Service> serviceInjector() {
        return mServiceDispatchingAndroidInjector;
    }


    /**
     * @return {@link DaggerAppComponent} to inject
     */
    public static AppComponent getAppComponent() {
        return mAppComponent;
    }
}
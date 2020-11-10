package org.openklas.di;


import com.github.windsekirun.daggerautoinject.ActivityModule;
import com.github.windsekirun.daggerautoinject.FragmentModule;
import com.github.windsekirun.daggerautoinject.ServiceModule;
import com.github.windsekirun.daggerautoinject.ViewModelModule;

import org.openklas.MainApplication;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(
		modules = {
				AndroidSupportInjectionModule.class,
				AppProvidesModule.class,
				AppBindsModule.class,
				AppInterceptorModule.class,
				ActivityModule.class,
				FragmentModule.class,
				ViewModelModule.class,
				ServiceModule.class
		}
)
public interface AppComponent {
	@Component.Builder
	interface Builder {
		@BindsInstance
		Builder application(MainApplication application);

		AppComponent build();
	}

	void inject(MainApplication mainApp);
}
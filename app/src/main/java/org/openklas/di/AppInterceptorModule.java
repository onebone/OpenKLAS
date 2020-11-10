package org.openklas.di;


import org.openklas.net.RWJacksonConfig;
import org.openklas.net.interceptor.RWInterceptor;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import okhttp3.Interceptor;


@Module
public class AppInterceptorModule {

	@Provides
	@IntoSet
	public Interceptor provideRWProvider() {
		return new RWInterceptor();
	}

	@Provides
	@IntoSet
	public RWJacksonConfig provideRWConfig() {
		return new RWJacksonConfig();
	}
}
package org.openklas.di;

/*
 * OpenKLAS
 * Copyright (C) 2020 OpenKLAS Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import com.github.windsekirun.daggerautoinject.ActivityModule;
import com.github.windsekirun.daggerautoinject.FragmentModule;
import com.github.windsekirun.daggerautoinject.ServiceModule;
import com.github.windsekirun.daggerautoinject.ViewModelModule;

import org.openklas.MainApplication;
import org.openklas.klas.KlasClient;

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
				GsonDeserializerModule.class,
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

	KlasClient klasClient();

	void inject(MainApplication mainApp);
}

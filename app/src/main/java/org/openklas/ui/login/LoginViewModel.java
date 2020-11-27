package org.openklas.ui.login;

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

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.Navigation;

import com.github.windsekirun.bindadapters.observable.ObservableString;
import com.github.windsekirun.daggerautoinject.InjectViewModel;

import org.openklas.MainApplication;
import org.openklas.repository.KlasRepository;

import org.openklas.base.BaseViewModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * OpenKlas
 * Class: LoginViewModel
 * Created by limmoong on 2020/11/10.
 * <p>
 * Description:
 */
@InjectViewModel
public class LoginViewModel extends BaseViewModel {
	@Inject
	KlasRepository mKlasRepository;

	public final ObservableString mId = new ObservableString();
	public final ObservableString mPw = new ObservableString();
	public final ObservableBoolean mRememberMe = new ObservableBoolean(true);

	public final ObservableString mResult = new ObservableString();

	@Inject
	public LoginViewModel(MainApplication application) {
		super(application);
	}

	@Override
	public void onCreate(@NonNull LifecycleOwner owner) {
	}

	public void clickLogin(View view) {
		// TODO handle empty username and password field

		addDisposable(mKlasRepository.performLogin(mId.get(), mPw.get(), mRememberMe.get())
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(
						v -> {
							Navigation.findNavController(view).navigate(LoginFragmentDirections.Companion.actionLoginHome());
						},
						e -> mResult.set("Failure: " + e.getMessage())
				));
	}
}

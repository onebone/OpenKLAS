package org.openklas.main;


import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.github.windsekirun.bindadapters.observable.ObservableString;
import com.github.windsekirun.daggerautoinject.InjectViewModel;

import org.openklas.MainApplication;
import org.openklas.klas.KlasClient;
import org.openklas.repository.MainRepository;

import org.openklas.base.BaseViewModel;
import org.openklas.repository.PreferenceRepository;
import org.openklas.repository.PreferenceRepositoryImpl;

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
	MainRepository mMainRepository;
	@Inject
	PreferenceRepository mPreferenceRepository;
	@Inject
	KlasClient klas;



	public ObservableString mId = new ObservableString();
	public ObservableString mPw = new ObservableString();

	public ObservableString mResult = new ObservableString();

	@Inject
	public LoginViewModel(MainApplication application) {
		super(application);
	}

	@Override
	public void onCreate(@NonNull LifecycleOwner owner) {

	}

	public void clickLogin(View view) {
		addDisposable(klas.login(mId.get(), mPw.get())
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(
						v -> mResult.set("Success"),
						e -> mResult.set("Failure: " + e.getMessage())
				));
	}
}

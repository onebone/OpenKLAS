package org.openklas.ui.login;


import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
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

	public final ObservableString mResult = new ObservableString();

	public final MutableLiveData<Void> mDidLogin = new MutableLiveData<>();

	@Inject
	public LoginViewModel(MainApplication application) {
		super(application);
	}

	@Override
	public void onCreate(@NonNull LifecycleOwner owner) {
	}

	public void clickLogin(View view) {
		addDisposable(mKlasRepository.performLogin(mId.get(), mPw.get())
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(
						v -> {
//							mDidLogin.setValue(null);
							Navigation.findNavController(view).navigate(LoginFragmentDirections.Companion.actionLoginHome());
						},
						e -> mResult.set("Failure: " + e.getMessage())
				));
	}
}

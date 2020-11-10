package org.openklas.main;


import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.github.windsekirun.bindadapters.observable.ObservableString;
import com.github.windsekirun.daggerautoinject.InjectViewModel;
import org.openklas.MainApplication;
import org.openklas.repository.MainRepository;

import org.openklas.base.BaseViewModel;

import javax.inject.Inject;

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

    public ObservableString mId = new ObservableString();
    public ObservableString mPw = new ObservableString();

    @Inject
    public LoginViewModel(MainApplication application) {
        super(application);
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {

    }

    public void clickLogin(View view){

    }
}

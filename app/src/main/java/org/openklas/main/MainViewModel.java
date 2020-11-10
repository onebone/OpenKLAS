package org.openklas.main;


import com.github.windsekirun.daggerautoinject.InjectViewModel;
import org.openklas.MainApplication;

import org.openklas.base.BaseViewModel;
import org.openklas.repository.MainRepository;

import javax.inject.Inject;

/**
 * OpenKlas
 * Class: LoginViewModel
 * Created by limmoong on 2020/11/10.
 * <p>
 * Description:
 */
@InjectViewModel
public class MainViewModel extends BaseViewModel {
    @Inject
    MainRepository mMainRepository;

    @Inject
    public MainViewModel(MainApplication application) {
        super(application);
    }
}

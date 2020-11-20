package org.openklas.main;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.github.windsekirun.daggerautoinject.InjectActivity;

import org.openklas.R;
import org.openklas.base.BaseActivity;
import org.openklas.databinding.LoginActivityBinding;

/**
 * OpenKlas
 * Class: MainActivity
 * Created by limmoong on 2020/11/10.
 * <p>
 * Description:
 */

@InjectActivity
public class LoginActivity extends BaseActivity<LoginActivityBinding> {
	private LoginViewModel mViewModel;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		mViewModel = getViewModel(LoginViewModel.class);
		mBinding.setViewModel(mViewModel);

		mViewModel.mDidLogin.observe(this, v -> startActivity(HomeActivity.class));
	}
}

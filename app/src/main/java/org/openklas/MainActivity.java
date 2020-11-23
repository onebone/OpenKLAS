package org.openklas;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.github.windsekirun.daggerautoinject.InjectActivity;


import org.greenrobot.eventbus.Subscribe;
import org.openklas.base.BaseActivity;
import org.openklas.databinding.MainActivityBinding;
import org.openklas.event.Event;

/**
 * OpenKlas
 * Class: MainActivity
 * Created by limmoong on 2020/11/10.
 * <p>
 * Description:
 */

@InjectActivity
public class MainActivity extends BaseActivity<MainActivityBinding> {
	private NavController controller;
	private NavController.OnDestinationChangedListener navListener;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

		NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
		controller = navHostFragment.getNavController();
		NavigationUI.setupWithNavController(mBinding.bottomNav, controller);

		navListener = (controller, destination, arguments) -> {
			if(destination.getId()== R.id.fragment_login){
				mBinding.bottomNav.setVisibility(View.GONE);
			}else{
				mBinding.bottomNav.setVisibility(View.VISIBLE);
			}
		};
	}

	public Fragment getForegroundFragment(){
		Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
		return navHostFragment == null ? null : navHostFragment.getChildFragmentManager().getFragments().get(0);
	}

	@Override
	protected void onResume() {
		super.onResume();
		controller.addOnDestinationChangedListener(navListener);
	}

	@Override
	protected void onPause() {
		super.onPause();
		controller.removeOnDestinationChangedListener(navListener);
	}

	@Subscribe
	public void getEvent(Event e){
		if(e.getEvent()){
			showToast("event hi");
		}
	}

}

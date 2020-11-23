package org.openklas;

import android.os.Bundle;

import androidx.annotation.Nullable;

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
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
	}

	@Subscribe
	public void getEvent(Event e){
		if(e.getEvent()){
			showToast("event hi");
		}
	}
}

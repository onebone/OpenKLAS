package org.openklas;

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
}

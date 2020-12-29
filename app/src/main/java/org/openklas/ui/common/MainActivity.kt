package org.openklas.ui.common

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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.openklas.base.BaseActivity
import org.openklas.databinding.MainActivityBinding

@AndroidEntryPoint
class MainActivity: BaseActivity<MainActivityBinding>() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		val binding = MainActivityBinding.inflate(LayoutInflater.from(this))
		setContentView(binding.root)

		val viewModel by viewModels<ActivityViewModel>()
		binding.viewModel = viewModel
		binding.view = this

		binding.lifecycleOwner = this
	}

	fun onTitleClickBack(view: View) {
		this.onBackPressed()
	}
}

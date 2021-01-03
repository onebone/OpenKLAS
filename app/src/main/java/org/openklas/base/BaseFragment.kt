package org.openklas.base

/*
 * OpenKLAS
 * Copyright (C) 2020-2021 OpenKLAS Team
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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import org.openklas.NavGraphDirections

abstract class BaseFragment<V: ViewDataBinding>: Fragment() {
	lateinit var mBinding: V

	protected fun createAndBindView(inflater: LayoutInflater,
	                                @LayoutRes layoutResID: Int, parent: ViewGroup?): View {
		mBinding = DataBindingUtil.inflate(inflater, layoutResID, parent, false)
		mBinding.lifecycleOwner = this
		return mBinding.root
	}

	protected fun setupSessionViewModel(viewModel: BaseViewModel) {
		if(viewModel is SessionViewModelDelegate) {
			viewModel.mustAuthenticate.observe(viewLifecycleOwner) {
				findNavController().navigate(NavGraphDirections.actionGlobalLogin())
			}
		}
	}

	protected fun prepareViewModel(viewModel: BaseViewModel) {
		this.lifecycle.addObserver(viewModel)

		setupSessionViewModel(viewModel)
	}

	protected inline fun <reified T : BaseViewModel> getViewModel(): T {
		val viewModel by viewModels<T>()
		prepareViewModel(viewModel)

		return viewModel
	}
}

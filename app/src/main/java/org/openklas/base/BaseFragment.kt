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

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import org.openklas.NavGraphDirections
import org.openklas.ui.shared.AppbarHolder
import org.openklas.utils.EventObserver
import org.openklas.widget.AppbarView

abstract class BaseFragment: Fragment() {
	private var appbarHolder: AppbarHolder? = null
	private var appbarConfiguration: AppbarConfiguration? = null

	protected var permissionHolder: PermissionHolder? = null
		private set

	override fun onAttach(context: Context) {
		super.onAttach(context)

		if(context is AppbarHolder) {
			appbarHolder = context
		}

		if(context is PermissionHolder) {
			permissionHolder = context
		}
	}

	override fun onDetach() {
		super.onDetach()

		appbarHolder = null
	}

	private fun ensureAppbarConfiguration() {
		if(appbarConfiguration == null) {
			appbarConfiguration = AppbarConfiguration("", AppbarView.HeaderType.NONE, AppbarView.SearchType.NONE, null)
		}
	}

	fun configureTitle(title: String, headerType: AppbarView.HeaderType, searchType: AppbarView.SearchType) {
		if(appbarHolder == null) {
			throw RuntimeException("configureTitle() is called, but the fragment is not attached to AppbarHolder")
		}

		appbarHolder!!.configureAppbar(title, headerType, searchType)

		ensureAppbarConfiguration()
		appbarConfiguration!!.apply {
			this.title = title
			this.headerType = headerType
			this.searchType = searchType
		}
	}

	fun setAppbarOnClickSearchListener(listener: AppbarView.OnClickSearchListener) {
		if(appbarHolder == null) {
			throw RuntimeException("setAppbarOnClickSearchListener() is called, but the fragment is not attached to AppbarHolder")
		}

		appbarHolder!!.setAppbarOnClickSearchListener(listener)

		ensureAppbarConfiguration()
		appbarConfiguration!!.apply {
			onClickSearchListener = listener
		}
	}

	fun setAppbarSearchState(search: Boolean) {
		if(appbarHolder == null) {
			throw RuntimeException("setAppbarSearchState() is called, but the fragment is not attached to AppbarHolder")
		}

		appbarHolder!!.setAppbarSearchState(search)

		ensureAppbarConfiguration()
		appbarConfiguration!!.apply {
			searchType = if(search) AppbarView.SearchType.SEARCH else AppbarView.SearchType.CANCEL
		}
	}

	override fun onResume() {
		super.onResume()

		appbarConfiguration?.let {
			appbarHolder?.configureAppbar(it.title, it.headerType, it.searchType)

			appbarHolder?.setAppbarOnClickSearchListener(it.onClickSearchListener)
		}
	}

	private fun setupSessionViewModel(viewModel: BaseViewModel) {
		if(viewModel is SessionViewModelDelegate) {
			viewModel.mustAuthenticate.observe(viewLifecycleOwner, EventObserver {
				findNavController().navigate(NavGraphDirections.actionGlobalLogin())
			})
		}
	}

	protected fun prepareViewModel(viewModel: BaseViewModel) {
		setupSessionViewModel(viewModel)
	}

	protected inline fun <reified T : BaseViewModel> getViewModel(): T {
		val viewModel by viewModels<T>()
		prepareViewModel(viewModel)

		return viewModel
	}

	private data class AppbarConfiguration(
		var title: String,
		var headerType: AppbarView.HeaderType,
		var searchType: AppbarView.SearchType,
		var onClickSearchListener: AppbarView.OnClickSearchListener?
	)
}

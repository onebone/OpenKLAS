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

package me.onebone.openklas.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import me.onebone.openklas.R
import me.onebone.openklas.base.BaseFragment
import me.onebone.openklas.databinding.LoginFragmentBinding
import me.onebone.openklas.klas.error.KlasSigninFailError
import me.onebone.openklas.widget.AppbarView

@AndroidEntryPoint
class LoginFragment: BaseFragment() {
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		configureTitle(resources.getString(R.string.app_name), AppbarView.HeaderType.NONE, AppbarView.SearchType.NONE)

		val binding = LoginFragmentBinding.inflate(inflater, container, false).apply {
			lifecycleOwner = this@LoginFragment.viewLifecycleOwner
		}

		val viewModel by viewModels<LoginViewModel>()
		prepareViewModel(viewModel)

		viewModel.checkSavedSession()

		binding.viewModel = viewModel

		viewModel.result.observe(viewLifecycleOwner) {
			if(it == null) {
				NavHostFragment.findNavController(this).navigate(LoginFragmentDirections.actionLoginHome())
			}else{
				AlertDialog.Builder(requireContext())
					.setTitle(R.string.login_error_title)
					.setMessage(when(it) {
						is LoginViewModel.AuthFieldEmptyException -> resources.getString(R.string.login_error_empty_field)
						is KlasSigninFailError -> it.message
						else -> resources.getString(R.string.common_unknown_error)
					})
					.setPositiveButton(R.string.common_ok, null)
					.show()
			}
		}

		return binding.root
	}
}

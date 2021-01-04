package org.openklas.ui.login

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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import org.openklas.R
import org.openklas.base.BaseFragment
import org.openklas.databinding.LoginFragmentBinding
import org.openklas.klas.error.KlasSigninFailError
import org.openklas.ui.common.configureTitle
import org.openklas.widget.TitleView

@AndroidEntryPoint
class LoginFragment : BaseFragment<LoginFragmentBinding>() {
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		return createAndBindView(inflater, R.layout.login_fragment, container)
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)

		val viewModel = getViewModel<LoginViewModel>()
		mBinding.viewModel = viewModel
		prepareViewModel(viewModel)

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
	}

	override fun onResume() {
		super.onResume()

		configureTitle("", TitleView.HeaderType.NONE, TitleView.SearchType.NONE)
	}
}

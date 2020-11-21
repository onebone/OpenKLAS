package org.openklas.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.github.windsekirun.daggerautoinject.InjectFragment
import org.openklas.R
import org.openklas.base.BaseFragment
import org.openklas.databinding.LoginFragmentBinding

@InjectFragment
class LoginFragment : BaseFragment<LoginFragmentBinding>() {
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return createAndBindView(inflater, R.layout.login_fragment, container)
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)

		val viewModel = getViewModel<LoginViewModel>()
		mBinding.viewModel = viewModel
		prepareViewModel(viewModel)

		viewModel.mDidLogin.observe(viewLifecycleOwner, {
			findNavController().navigate(LoginFragmentDirections.actionLoginHome())
		})
	}

	companion object {
		@JvmStatic
		fun newInstance() =
			LoginFragment().apply {
				arguments = Bundle()
			}
	}
}

package org.openklas.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import org.openklas.NavGraphDirections
import javax.inject.Inject

abstract class BaseFragment<V: ViewDataBinding>: Fragment() {
	@Inject
	lateinit var viewModelProvideFactory: ViewModelProvider.Factory

	lateinit var mBinding: V

	protected fun createAndBindView(inflater: LayoutInflater,
	                                @LayoutRes layoutResID: Int, parent: ViewGroup?): View {
		mBinding = DataBindingUtil.inflate(inflater, layoutResID, parent, false)
		mBinding.lifecycleOwner = this
		return mBinding.root
	}

	protected fun prepareViewModel(viewModel: BaseViewModel) {
		this.lifecycle.addObserver(viewModel)
		viewModel.lifecycle = this.lifecycle

		if(viewModel is SessionViewModelDelegate) {
			viewModel.mustAuthenticate.observe(viewLifecycleOwner, {
				findNavController().navigate(NavGraphDirections.actionGlobalLogin())
			})
		}
	}

	protected inline fun <reified T : BaseViewModel> getViewModel(): T {
		val viewModel by viewModels<T> { viewModelProvideFactory }
		prepareViewModel(viewModel)

		return viewModel
	}
}

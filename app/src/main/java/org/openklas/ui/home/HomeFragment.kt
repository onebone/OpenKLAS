package org.openklas.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.windsekirun.daggerautoinject.InjectFragment
import org.openklas.R
import org.openklas.base.BaseFragment
import org.openklas.databinding.HomeFragmentBinding

@InjectFragment
class HomeFragment : BaseFragment<HomeFragmentBinding>() {
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return createAndBindView(inflater, R.layout.home_fragment, container)
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)

		val viewModel = getViewModel<HomeViewModel>()
		mBinding.viewModel = viewModel

		// TODO set semester value dynamically
		viewModel.semester.value = "2020,2"
	}
}

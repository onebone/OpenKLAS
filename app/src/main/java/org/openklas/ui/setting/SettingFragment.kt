package org.openklas.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.windsekirun.daggerautoinject.InjectFragment
import org.openklas.R
import org.openklas.base.BaseFragment
import org.openklas.databinding.HomeFragmentBinding
import org.openklas.databinding.SettingFragmentBinding

@InjectFragment
class SettingFragment : BaseFragment<SettingFragmentBinding>() {
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return createAndBindView(inflater, R.layout.setting_fragment, container)
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)

		val viewModel = getViewModel<SettingViewModel>()
		mBinding.viewModel = viewModel

		viewModel.semester.value = "2020,2"
	}
}

package org.openklas.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.navGraphViewModels
import com.github.windsekirun.daggerautoinject.InjectFragment
import org.openklas.R
import org.openklas.base.BaseFragment
import org.openklas.databinding.TimetableFragmentBinding

@InjectFragment
class TimetableFragment : BaseFragment<TimetableFragmentBinding>() {
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		return createAndBindView(inflater, R.layout.timetable_fragment, container)
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)

		val viewModel by navGraphViewModels<HomeViewModel>(R.id.nav_home_container) { viewModelProvideFactory }
		mBinding.viewModel = viewModel
	}
}

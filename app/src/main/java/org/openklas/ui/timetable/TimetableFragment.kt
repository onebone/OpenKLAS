package org.openklas.ui.timetable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.windsekirun.daggerautoinject.InjectFragment
import org.openklas.R
import org.openklas.base.BaseFragment
import org.openklas.databinding.TimetableFragmentBinding

@InjectFragment
class TimetableFragment : BaseFragment<TimetableFragmentBinding>() {
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return createAndBindView(inflater, R.layout.timetable_fragment, container)
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)

		val viewModel = getViewModel<TimetableViewModel>()
		mBinding.viewModel = viewModel

		viewModel.semester.value = "2020,2"
	}
}

package org.openklas.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.windsekirun.daggerautoinject.InjectFragment
import org.openklas.R
import org.openklas.base.BaseFragment
import org.openklas.databinding.NotificationFragmentBinding

@InjectFragment
class NotificationFragment : BaseFragment<NotificationFragmentBinding>() {
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return createAndBindView(inflater, R.layout.notification_fragment, container)
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)

		val viewModel = getViewModel<NotificationViewModel>()
		mBinding.viewModel = viewModel
	}
}

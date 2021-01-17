package org.openklas.ui.common

import androidx.fragment.app.activityViewModels
import org.openklas.base.BaseFragment
import org.openklas.widget.TitleView

fun BaseFragment.configureTitle(title: String?,
                                                        titleHeaderType: TitleView.HeaderType?,
                                                        titleSearchType: TitleView.SearchType?) {
	val activityViewModel by activityViewModels<ActivityViewModel> {
		defaultViewModelProviderFactory
	}

	title?.let {
		activityViewModel.title.value = it
	}

	titleHeaderType?.let {
		activityViewModel.titleHeaderType.value = it
	}

	titleSearchType?.let {
		activityViewModel.titleSearchType.value = it
	}
}

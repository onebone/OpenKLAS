package org.openklas.ui.common

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import org.openklas.base.BaseViewModel
import org.openklas.base.SemesterViewModelDelegate
import org.openklas.widget.TitleView

class ActivityViewModel @ViewModelInject constructor(
	semesterViewModelDelegate: SemesterViewModelDelegate
): BaseViewModel(), SemesterViewModelDelegate by semesterViewModelDelegate {
	val title = MutableLiveData<String>().also {
		it.value = ""
	}

	val titleHeaderType = MutableLiveData<TitleView.HeaderType>().also {
		it.value = TitleView.HeaderType.NONE
	}

	val titleSearchType = MutableLiveData<TitleView.SearchType>().also {
		it.value = TitleView.SearchType.NONE
	}
}

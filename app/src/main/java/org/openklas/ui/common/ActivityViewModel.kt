package org.openklas.ui.common

import androidx.lifecycle.MutableLiveData
import org.openklas.base.BaseViewModel
import org.openklas.widget.TitleView

class ActivityViewModel: BaseViewModel() {
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

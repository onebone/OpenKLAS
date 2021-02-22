package org.openklas.ui.shared

import org.openklas.widget.AppbarView

interface AppbarHolder {
	fun configureAppbar(title: String, headerType: AppbarView.HeaderType, searchType: AppbarView.SearchType)

	fun setAppbarSearchState(search: Boolean = true)

	fun setAppbarOnClickSearchListener(listener: AppbarView.OnClickSearchListener?)
}

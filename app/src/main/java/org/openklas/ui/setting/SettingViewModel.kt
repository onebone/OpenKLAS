package org.openklas.ui.setting

import com.github.windsekirun.daggerautoinject.InjectViewModel
import org.openklas.MainApplication
import org.openklas.base.BaseViewModel
import javax.inject.Inject

@InjectViewModel
class SettingViewModel @Inject constructor(
	app: MainApplication
): BaseViewModel(app) {
}

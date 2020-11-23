package org.openklas.ui.notification

import com.github.windsekirun.daggerautoinject.InjectViewModel
import org.openklas.MainApplication
import org.openklas.base.BaseViewModel
import javax.inject.Inject

@InjectViewModel
class NotificationViewModel @Inject constructor(
	app: MainApplication
): BaseViewModel(app) {

}

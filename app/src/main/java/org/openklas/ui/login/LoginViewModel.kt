package org.openklas.ui.login

/*
 * OpenKLAS
 * Copyright (C) 2020 OpenKLAS Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.navigation.Navigation
import com.github.windsekirun.bindadapters.observable.ObservableString
import com.github.windsekirun.daggerautoinject.InjectViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.openklas.MainApplication
import org.openklas.base.BaseViewModel
import org.openklas.repository.KlasRepository
import org.openklas.ui.login.LoginFragmentDirections.Companion.actionLoginHome
import javax.inject.Inject

@InjectViewModel
class LoginViewModel @Inject constructor(application: MainApplication): BaseViewModel(application) {
	@Inject
	lateinit var mKlasRepository: KlasRepository

	val mId = ObservableString()
	val mPw = ObservableString()
	val mRememberMe = ObservableBoolean(true)

	val mResult = ObservableString()

	fun clickLogin(view: View) {
		// TODO handle empty username and password field

		addDisposable(mKlasRepository.performLogin(mId.get(), mPw.get(), mRememberMe.get())
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe { v, err ->
				if(err == null) {
					Navigation.findNavController(view).navigate(actionLoginHome())
				}else{
					mResult.set("Failure: " + err.message)
				}
			})
	}
}

package org.openklas.net.compose

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers


/**
 * OpenKlas
 * Class: AsyncTransformer
 * Created by limmoong on 2020/11/18.
 *
 * Description:
 */
class AsyncTransformer<T> : Transformer<T>() {
	override fun apply(upstream: Observable<T>): ObservableSource<T> {
		return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
	}
}
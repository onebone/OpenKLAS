package org.openklas.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.greenrobot.eventbus.EventBus
import org.openklas.base.impl.BaseInterface
import org.openklas.di.argsinjector.ArgsParser
import org.openklas.main.LoginActivity
import org.openklas.utils.catchAll
import org.openklas.utils.setupEditContentScrollable
import org.openklas.utils.weak
import javax.inject.Inject

abstract class BaseActivity<V : ViewDataBinding> : AppCompatActivity(), BaseInterface {
	@Inject
	lateinit var viewModelProvideFactory: ViewModelProvider.Factory

	protected lateinit var mBinding: V
	protected open val requireLandscape: Boolean
		get() = false
	protected open val requireScreenOn: Boolean
		get() = false
	protected open val hideKeyboardOnTouch: Boolean
		get() = false
	protected val compositeDisposable = CompositeDisposable()

	private var viewModel: BaseViewModel? by weak(null)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		catchAll {
			if (requireLandscape) {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
			} else if (Config.config.requirePortrait) {
				requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
			}
		}

		if (requireScreenOn) {
			window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
		} else {
			window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
		}

		if (Config.config.registerBusOnCreate) registerEventBus()
	}

	override fun setContentView(layoutResID: Int) {
		mBinding = DataBindingUtil.inflate(LayoutInflater.from(this), layoutResID, null, false)
		mBinding.lifecycleOwner = this
		super.setContentView(mBinding.root)
	}

	override fun onContentChanged() {
		super.onContentChanged()
		ArgsParser.inject(this)
		if (hideKeyboardOnTouch) setupEditContentScrollable(mBinding.root, this)
	}

	override fun onStart() {
		super.onStart()
		registerEventBus()
	}

	override fun onStop() {
		super.onStop()
		unregisterEventBus()
	}

	override fun onDestroy() {
		super.onDestroy()
		compositeDisposable.clear()
		viewModel?.let { lifecycle.removeObserver(it) }
		unregisterEventBus()
	}

	fun <T : BaseViewModel> getViewModel(viewModelClass: Class<T>): T {
		val viewModel = getViewModel(this, viewModelProvideFactory, viewModelClass)
		setViewModelObject(viewModel)
		return viewModel
	}

	protected fun addDisposable(disposable: Disposable) {
		compositeDisposable.add(disposable)
	}


	@Deprecated("Don't need to call manually")
	protected fun setViewModelReference(viewModel: BaseViewModel) {
		setViewModelObject(viewModel)
	}

	private fun setViewModelObject(viewModel: BaseViewModel) {
		lifecycle.addObserver(viewModel)
		viewModel.lifecycle = lifecycle
		this.viewModel = viewModel

		if(viewModel is SessionViewModelDelegate) {
			viewModel.mustAuthenticate.observe(this, Observer {
				startActivity(LoginActivity::class.java)
			})
		}
	}

	private fun registerEventBus() {
		try {
			EventBus.getDefault().register(this)
		} catch (t: Throwable) {
			Log.e("exception", "$t:" + t.message)
		}
	}

	private fun unregisterEventBus() {
		try {
			EventBus.getDefault().unregister(this)
		} catch (t: Throwable) {
			Log.e("exception", "$t:" + t.message)
		}

	}
}
